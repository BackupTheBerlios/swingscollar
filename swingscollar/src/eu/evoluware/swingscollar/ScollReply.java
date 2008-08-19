//Copyright (c) 2008 Fred Spiessens - Evoluware http://www.evoluware.eu
package eu.evoluware.swingscollar;

import java.awt.Cursor;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.text.*;

public class ScollReply {
	public static final int CHECK=0, FIXPTS=1, SOL=2, SHOW=3, STATUS=4, ERROR=5; 
	public static synchronized ScollReply testReply(String S){
		final ScollReply R = new ScollReply("<reply><check>");
		R.addLine(S);
		R.addLine("</reply>");
		return R;
	}
	protected volatile LinkedList<String> result; // not including reply/error
	// prefix/suffix
	protected volatile int lines; // not including reply/error prefix/suffix
	protected volatile Boolean hasError;
	protected volatile Boolean validStart;
	protected volatile Boolean completed;
	protected volatile Boolean endedPrematurely;
	protected volatile int type;
	protected volatile int showNmbr = 0;

	public ScollReply(String line) {
		result = new LinkedList<String>();
		lines = 0;
		hasError = false;
		validStart = true;
		completed = false;
		endedPrematurely = false;

		if (line.equals("<reply><check>")) {
			hasError = false;
			validStart = true;
			type = CHECK;
		} else if (line.equals("<reply><fixpts>")) {
			hasError = false;
			validStart = true;
			type = FIXPTS;
		} else if (line.equals("<reply><sol>")) {
			hasError = false;
			validStart = true;
			type = SOL;
		} else if (line.equals("<reply><status>")) {
			hasError = false;
			validStart = true;
			type = STATUS;
		} else if (line.equals("<error>")) {
			hasError = true;
			validStart = true;
			type = ERROR;
		} else if (line.substring(0, Math.min("<reply><show ".length(), line.length())).equals("<reply><show ")) {
			hasError = false;
			validStart = true;
			showNmbr = Integer.parseInt(line.substring("<reply><show ".length(), line.length()-1));
			type = SHOW;
		} else {
			hasError = true;
			validStart = false;
			completed = true;
			endedPrematurely = true;
			type = ERROR;
		}
	}

	public synchronized Boolean isComplete() {
		return completed;
	}

	public synchronized void addLine(String line) {
		if (validStart) {
			if ((hasError) && (line.equals("</error>"))) {
				completed = true;
			} else if (!(hasError) && (line.equals("</reply>"))) {
				completed = true;
			} else {
				result.add(line);
			}
		} else {
			result.add("debug info : " + line);
		}
	}

	public synchronized void endPrematurely() {
		endedPrematurely = true;
		completed = true;
		hasError = true;
		type = ERROR;
	}

	private synchronized ScollPanel getMainPanel(){
		return ScollPort.getInstance().getPanel();
	}

	public synchronized void render(){
		final ScollPanel mainPanel = getMainPanel();
		//mainPanel.getPatternPanel().statusLabel.setText("test");
		switch (type) {
		case CHECK:	
			renderLabel(mainPanel.getPatternPanel());
			mainPanel.getTabbedPane().setSelectedComponent(mainPanel.getPatternPanel());
			break;	
		case FIXPTS:
			render(mainPanel.getFixptPanel(), false);
			mainPanel.getTabbedPane().setEnabledAt(ScollPanel.FIXPTINDEX, true);
			mainPanel.getTabbedPane().setSelectedComponent(mainPanel.getFixptPanel());
			break;
		case SOL:
			mainPanel.endOfCalculation();
			render(mainPanel.getSolutionsPanel(), true);
			mainPanel.getTabbedPane().setSelectedComponent(mainPanel.getSolutionsPanel());
			break;
		case SHOW:
			render(mainPanel.getDetailPanel(showNmbr), false);
			mainPanel.getTabbedPane().setSelectedComponent(mainPanel.getDetailPanel(showNmbr));
			break;		
		case STATUS:
			renderProgress();
			mainPanel.getTabbedPane().setSelectedComponent(mainPanel.getPatternPanel());
			ScollPort.getInstance().getNextReply(); //status is never a final answer
			break;	
		case ERROR:
			renderLabel(mainPanel.getPatternPanel());
			mainPanel.getTabbedPane().setSelectedComponent(mainPanel.getPatternPanel());
			break;
		default:;
		}
	}


	private synchronized void render(ScollTabPanel tabPanel, boolean addButtons){
		Iterator<String> it = result.iterator();
		try {
			tabPanel.textPane.setText("");
			renderText(it, tabPanel, addButtons);
			tabPanel.textPane.setCaretPosition(0);
		}	 
		catch (BadLocationException e) {
			// TODO Auto-generated catch block
			;}
	}	

	private synchronized void renderLabel(ScollTabPanel tabPanel) {
		Iterator<String> it = result.iterator();
		String content = "";
		String str = "";
		while (it.hasNext()) {
			str = it.next();
			content += (str + "\n");
		}
		tabPanel.statusLabel.setText(content);				
	}

	private synchronized void renderProgress() {
		final ScollProgressDialog dialog = getMainPanel().getProgressDialog();
		if(!(dialog == null)) {
			dialog.parseUpdate(result.getFirst());
		}	
		else {
			renderLabel(getMainPanel().getPatternPanel());
			};
	}

	private synchronized void renderText(Iterator<String> it, ScollTabPanel tabPanel, boolean addButtons)
	throws BadLocationException {
		String str = "";
		Document doc = tabPanel.textPane.getDocument();
		Boolean renderPlainText = true;
		Boolean renderTable = false;
		Boolean renderJpg = false;
		while (it.hasNext()) {
			renderTable = false;
			renderJpg = false;
			str = it.next();
			renderTable = str.equals("<table>");
			renderJpg = str.equals("<jpg>");
			renderPlainText = !(renderTable || renderJpg);
			if (renderPlainText) {
				doc.insertString(doc.getLength(), str + "\n", null);
			} 
			else if (renderTable) {
				renderTable(it, tabPanel, addButtons);
			}
			else if (renderJpg ){ 
				renderJpg(it, tabPanel);
			}					
		}
	}

	private synchronized void renderTable(Iterator<String> it, ScollTabPanel tabPanel, boolean addButtons) throws BadLocationException {
		String str;
		String[] desc;
		String subjectname = null;
		@SuppressWarnings("unused")
		int colcount = 0;
		int rowcount = 0;
		String[][] rows = null;
		String[] cols =  null;
		Boolean error= false;
		Document doc = tabPanel.textPane.getDocument();
		try {
			str = it.next();
			desc = str.split("\t");
			subjectname = desc[0];
			colcount = Integer.parseInt(desc[1]);
			rowcount = Integer.parseInt(desc[2]);
			cols = (it.next()).split("\t");
			rows = new String[rowcount][];
			for (int i = 0; i < rowcount; i++){ 
				rows[i] = (it.next()).split("\t");
				//for (int j = 1; j < colcount; j++){
				//rows[i][j]= Integer.parseInt(valstr[j])
			}	
			if (! it.next().equals("</table>")) throw (new Exception("Received invalid table format from Oz process"));
		}	
		catch(Exception E) {
			error = true;
			tabPanel.textPane.setText(E.getMessage());
		}
		if (! error){
			JTable tab = addButtons ? new ScollSolutionsTable(new ScollSolutionsTableModel(rows,cols,tabPanel)) : new ScollMultiCellTable(rows,cols) ;
			doc.insertString(doc.getLength(), " \n", null);
			tabPanel.textPane.insertComponent(new JLabel(subjectname)); 
			doc.insertString(doc.getLength(), " \n", null);
			tabPanel.textPane.insertComponent(tab.getTableHeader());
			if (addButtons) {
				tab.getTableHeader().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}	
			else {;
			}
			doc.insertString(doc.getLength(), " \n", null);
			tabPanel.textPane.insertComponent(tab);
			doc.insertString(doc.getLength(), " \n", null);
		}
	}

	private synchronized void renderJpg(Iterator<String> it, ScollTabPanel tabPanel) throws BadLocationException {
		String filename = null;
		Boolean error= false;
		Document doc = tabPanel.textPane.getDocument();
		try {
			filename = it.next();
			if (! it.next().equals("</jpg>")) throw (new Exception("Received invalid jpg filename format from Oz process"));
		}	
		catch(Exception E) {
			error = true;
			tabPanel.statusLabel.setText(E.getMessage());
		}
		if (! error){
			//doc.insertString(doc.getLength(), "filename = "+filename+" \n", null);
			ImageIcon graph = new ImageIcon(filename);
			JLabel graphLbl = new JLabel(graph,JLabel.CENTER);
			doc.insertString(doc.getLength(), "\n", null);
			tabPanel.textPane.insertComponent(graphLbl); 
			doc.insertString(doc.getLength(), "\n", null);
		}
	}

}
