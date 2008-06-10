/*code partially derived from the syntaxcoloring project 
 * Copyrighted (C) 2000-2001 by Stephen Ostermiller (Syntax.Ostmiller.com) */
/*
 * Copyright (C) 2008 by Fred Spiessens and Stephen Ostermiller */

package eu.evoluware.swingscollar;


import javax.swing.text.*;

@SuppressWarnings("serial")
public class ScollDocument extends DefaultStyledDocument {

	private ScollDocumentReader documentReader;
	private AttributeSet globalStyle = null;
	private ScollLexer syntaxLexer;
	/**
	 * A thread that handles the actual coloring.
	 */
	private ScollColorer colorer;

	/**
	 * A lock for modifying the document, or for actions that depend on the
	 * document not being modified.
	 */
	private Object docLock = new Object();
//	public ScollDocument() {
//	super();
//	}
	public ScollDocument() {

		// Start the thread that does the coloring
		colorer = new ScollColorer(this);
		colorer.start();

		// create the new document.
		documentReader = new ScollDocumentReader(this);
		syntaxLexer = new ScollLexer(documentReader);
	}

	public ScollDocument(StyleContext arg0) {
		super(arg0);
	}

	public ScollDocument(Content arg0, StyleContext arg1) {
		super(arg0, arg1);
	}

	/**
	 * Color or recolor the entire document
	 */
	public void colorAll() {
		color(0, getLength());
	}

	/**
	 * Color a section of the document. The actual coloring will start somewhere
	 * before the requested position and continue as long as needed.
	 * 
	 * @param position
	 *            the starting point for the coloring.
	 * @param adjustment
	 *            amount of text inserted or removed at the starting point.
	 */
	public void color(int position, int adjustment) {
		colorer.color(position, adjustment);
	}

	public void setGlobalStyle(AttributeSet value) {
		globalStyle = value;
		colorAll();
	}

	public void setHighlightStyle(Object value) {
		syntaxLexer = new ScollLexer(documentReader);
		globalStyle = null;
		colorAll();
	}

	//
	// Intercept inserts and removes to color them.
	//
	public void insertString(int offs, String str, AttributeSet a)
	throws BadLocationException {
		synchronized (docLock) {
			super.insertString(offs, str, a);
			colorAll();
			//color(offs, str.length());
			documentReader.update(offs, str.length());
		}
	}

	public void remove(int offs, int len) throws BadLocationException {
		synchronized (docLock) {
			super.remove(offs, len);
			colorAll();
			//color(offs, -len);
			documentReader.update(offs, -len);
		}
	}

	// methods for Colorer to retrieve information
	ScollDocumentReader getDocumentReader() { return documentReader; }
	Object getDocumentLock() { return docLock; }
	ScollLexer getSyntaxLexer() { return syntaxLexer; }
	AttributeSet getGlobalStyle() { return globalStyle; }
}

