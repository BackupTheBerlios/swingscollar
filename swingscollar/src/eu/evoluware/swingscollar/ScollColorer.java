/*code partially derived from the syntaxcoloring project 
 * Copyrighted (C) 2000-2001 by Stephen Ostermiller (Syntax.Ostmiller.com) */
/*
 * Copyright (C) 2008 by Fred Spiessens and Stephen Ostermiller */

package eu.evoluware.swingscollar;


import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.swing.text.AttributeSet;


/**
 * Run the Syntax Highlighting as a separate thread. Things that need to be
 * colored are messaged to the thread and put in a list.
 */
public class ScollColorer extends Thread {
	/**
	 * A simple wrapper representing something that needs to be colored. Placed
	 * into an object so that it can be stored in a Vector.
	 */
	private static class RecolorEvent {
		public int position;
		public int adjustment;

		public RecolorEvent(int position, int adjustment) {
			this.position = position;
			this.adjustment = adjustment;
		}
	}

	/**
	 * Stores the document we are coloring. We use a WeakReference
	 * so that the document is eligible for garbage collection when
	 * it is no longer being used. At that point, this thread will
	 * shut down itself.
	 */
	private WeakReference<ScollDocument> document;

	/**
	 * Keep a list of places in the file that it is safe to restart the
	 * highlighting. This happens whenever the lexer reports that it has
	 * returned to its initial state. Since this list needs to be sorted and
	 * we need to be able to retrieve ranges from it, it is stored in a
	 * balanced tree.
	 */
	private TreeSet<ScollDocPosition> iniPositions = new TreeSet<ScollDocPosition>(ScollDocPositionComparator.instance);

	/**
	 * As we go through and remove invalid positions we will also be finding
	 * new valid positions. Since the position list cannot be deleted from
	 * and written to at the same time, we will keep a list of the new
	 * positions and simply add it to the list of positions once all the old
	 * positions have been removed.
	 */
	private HashSet<ScollDocPosition> newPositions = new HashSet<ScollDocPosition>();

	/**
	 * Vector that stores the communication between the two threads.
	 */
	private volatile LinkedList<RecolorEvent> events = new LinkedList<RecolorEvent>();

	/**
	 * When accessing the linked list, we need to create a critical section.
	 * we will synchronize on this object to ensure that we don't get unsafe
	 * thread behavior.
	 */
	private Object eventsLock = new Object();

	/**
	 * The amount of change that has occurred before the place in the
	 * document that we are currently highlighting (lastPosition).
	 */
	private volatile int change = 0;

	/**
	 * The last position colored
	 */
	private volatile int lastPosition = -1;

	/**
	 * Creates the coloring thread for the given document.
	 * 
	 * @param document The document to be colored.
	 */
	public ScollColorer(ScollDocument document) {
		this.document = new WeakReference<ScollDocument>(document);
	}

	/**
	 * Tell the Syntax Highlighting thread to take another look at this
	 * section of the document. It will process this as a FIFO. This method
	 * should be done inside a docLock.
	 */
	public void color(int position, int adjustment) {
		// figure out if this adjustment effects the current run.
		// if it does, then adjust the place in the document
		// that gets highlighted.
		if (position < lastPosition) {
			if (lastPosition < position - adjustment) {
				change -= lastPosition - position;
			} else {
				change += adjustment;
			}
		}
		synchronized (eventsLock) {
			if(!events.isEmpty()) {
				// check whether to coalesce with current last element
				RecolorEvent curLast = (RecolorEvent) events.getLast();
				if(adjustment < 0 && curLast.adjustment < 0) {
					// both are removals
					if(position == curLast.position) {
						curLast.adjustment += adjustment;
						return;
					}
				} else if(adjustment >= 0 && curLast.adjustment >= 0) {
					// both are insertions
					if(position == curLast.position + curLast.adjustment) {
						curLast.adjustment += adjustment;
						return;
					} else if(curLast.position == position + adjustment) {
						curLast.position = position;
						curLast.adjustment += adjustment;
						return;
					}
				}
			}
			events.add(new RecolorEvent(position, adjustment));
			eventsLock.notifyAll();
		}
	}

	/**
	 * The colorer runs forever and may sleep for long periods of time. It
	 * should be interrupted every time there is something for it to do.
	 */
	public void run() {
		while(document.get() != null) {
			try {
				RecolorEvent re;
				synchronized (eventsLock) {
					// get the next event to process - stalling until the
					// event becomes available
					//while(events.isEmpty() && document.get() != null) {	
					while(events.isEmpty() || document.get() == null) {	
						// stop waiting after a second in case document
						// has been cleared.
						if (document.get() == null) eventsLock.wait(1000);
						else eventsLock.wait(100);
					}
					re = (RecolorEvent) events.removeFirst();
					eventsLock.wait(200); // process only last event in a fast sequence
					                      // ok for now since all events start recoloring from the beginning
					while(! events.isEmpty()) {	
						re = (RecolorEvent) events.removeFirst();
						while(! events.isEmpty()) {	
							re = (RecolorEvent) events.removeFirst();
						}
						eventsLock.wait(200);
					}
				}
				processEvent(re.position, re.adjustment);
				Thread.sleep(100);
			} catch(InterruptedException e) { }
		}
	}

	private void processEvent(int position, int adjustment) {
		ScollDocument doc = (ScollDocument) document.get();
		if(doc == null) return;

		// slurp everything up into local variables in case another
		// thread changes them during coloring process
		AttributeSet globalStyle = doc.getGlobalStyle();
		ScollLexer syntaxLexer = doc.getSyntaxLexer();
		ScollDocumentReader documentReader = doc.getDocumentReader();
		Object docLock = doc.getDocumentLock(); 

		if(globalStyle != null) {
			int start = Math.min(position, position + adjustment);
			int stop = Math.max(position, position + adjustment);
			synchronized(docLock) {
				doc.setCharacterAttributes(start, stop - start,
						globalStyle, true);
			}
			return;
		}

		SortedSet<ScollDocPosition> workingSet;
		Iterator<ScollDocPosition> workingIt;
		ScollDocPosition startRequest = new ScollDocPosition(position);
		ScollDocPosition endRequest = new ScollDocPosition(position + Math.abs(adjustment));
		ScollDocPosition dp;
		ScollDocPosition dpStart = null;
		ScollDocPosition dpEnd = null;

		// find the starting position. We must start at least one
		// token before the current position
		try {
			// all the good positions before
			workingSet = iniPositions.headSet(startRequest);
			// the last of the stuff before
			dpStart = (ScollDocPosition) workingSet.last();
		} catch (NoSuchElementException x) {
			// if there were no good positions before the requested
			// start,
			// we can always start at the very beginning.
			dpStart = new ScollDocPosition(0);
		}

		// if stuff was removed, take any removed positions off the
		// list.
		if (adjustment < 0) {
			workingSet = iniPositions.subSet(startRequest,
					endRequest);
			workingIt = workingSet.iterator();
			while (workingIt.hasNext()) {
				workingIt.next();
				workingIt.remove();
			}
		}

		// adjust the positions of everything after the
		// insertion/removal.
		workingSet = iniPositions.tailSet(startRequest);
		workingIt = workingSet.iterator();
		while (workingIt.hasNext()) {
			((ScollDocPosition) workingIt.next()).adjustPosition(adjustment);
		}

		// now go through and highlight as much as needed
		workingSet = iniPositions.tailSet(dpStart);
		workingIt = workingSet.iterator();
		dp = null;
		if (workingIt.hasNext()) {
			dp = (ScollDocPosition) workingIt.next();
		}
		try {
			ScollToken t;
			boolean done = false;
			dpEnd = dpStart;
			synchronized (docLock) {
				// we are playing some games with the lexer for
				// efficiency.
				// we could just create a new lexer each time here,
				// but instead,
				// we will just reset it so that it thinks it is
				// starting at the
				// beginning of the document but reporting a funny
				// start position.
				// Reseting the lexer causes the close() method on
				// the reader
				// to be called but because the close() method has
				// no effect on the
				// DocumentReader, we can do this.
				syntaxLexer.reset(documentReader, 0, dpStart
						.getPosition(), 0);
				// After the lexer has been set up, scroll the
				// reader so that it
				// is in the correct spot as well.
				documentReader.seek(dpStart.getPosition());
				// we will highlight tokens until we reach a good
				// stopping place.
				// the first obvious stopping place is the end of
				// the document.
				// the lexer will return null at the end of the
				// document and wee
				// need to stop there.
				t = syntaxLexer.getNextToken();
			}
			newPositions.add(dpStart);
			while (!done && t != null) {
				// this is the actual command that colors the stuff.
				// Color stuff with the description of the styles
				// stored in tokenStyles.
				if (t.getCharEnd() <= doc.getLength()) {
					doc.setCharacterAttributes(t.getCharBegin() + change,
							t.getCharEnd()	- t.getCharBegin(),
							ScollTokenStyles.getStyle(t.getDescription()),
							true);
					// record the position of the last bit of
					// text that we colored
					dpEnd = new ScollDocPosition(t.getCharEnd());
				}
				lastPosition = (t.getCharEnd() + change);
				// The other more complicated reason for doing no
				// more highlighting
				// is that all the colors are the same from here on
				// out anyway.
				// We can detect this by seeing if the place that
				// the lexer returned
				// to the initial state last time we highlighted is
				// the same as the
				// place that returned to the initial state this
				// time.
				// As long as that place is after the last changed
				// text, everything
				// from there on is fine already.
				if (t.getState() == ScollToken.INITIAL_STATE) {
					// look at all the positions from last time that
					// are less than or
					// equal to the current position
					while (dp != null
							&& dp.getPosition() <= t.getCharEnd()) {
						if (dp.getPosition() == t.getCharEnd()
								&& dp.getPosition() >= endRequest
								.getPosition()) {
							// we have found a state that is the
							// same
							done = true;
							dp = null;
						} else if (workingIt.hasNext()) {
							// didn't find it, try again.
							dp = (ScollDocPosition) workingIt.next();
						} else {
							// didn't find it, and there is no more
							// info from last
							// time. This means that we will just
							// continue
							// until the end of the document.
							dp = null;
						}
					}
					// so that we can do this check next time,
					// record all the
					// initial states from this time.
					newPositions.add(dpEnd);
				}
				synchronized (docLock) {
					t = syntaxLexer.getNextToken();
				}
			}

			// remove all the old initial positions from the place
			// where
			// we started doing the highlighting right up through
			// the last
			// bit of text we touched.
			workingIt = iniPositions.subSet(dpStart, dpEnd)
			.iterator();
			while (workingIt.hasNext()) {
				workingIt.next();
				workingIt.remove();
			}

			// Remove all the positions that are after the end of
			// the file.:
			workingIt = iniPositions.tailSet(
					new ScollDocPosition(doc.getLength())).iterator();
			while (workingIt.hasNext()) {
				workingIt.next();
				workingIt.remove();
			}

			// and put the new initial positions that we have found
			// on the list.
			iniPositions.addAll(newPositions);
			newPositions.clear();
		} catch (IOException x) {
		}
		synchronized (docLock) {
			lastPosition = -1;
			change = 0;
		}
	}
}
