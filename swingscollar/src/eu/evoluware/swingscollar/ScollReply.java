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
	protected LinkedList<String> result; // not including reply/error
	// prefix/suffix
	protected int lines; // not including reply/error prefix/suffix
	protected Boolean hasError;
	protected Boolean validStart;
	protected Boolean completed;
	protected Boolean endedPrematurely;

	public ScollReply(String line) {
		result = new LinkedList<String>();
		lines = 0;
		hasError = false;
		validStart = true;
		completed = false;
		endedPrematurely = false;

		if (line.equals("<reply>")) {
			hasError = false;
			validStart = true;
		} else if (line.equals("<control>")) {
			hasError = false;
			validStart = true;
		} else if (line.equals("<error>")) {
			hasError = true;
			validStart = true;
		} else {
			hasError = true;
			validStart = false;

		}
	}

	public Boolean isComplete() {
		return completed;
	}

	public void addLine(String line) {
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

	public void endPrematurely() {
		endedPrematurely = true;
		completed = true;
		hasError = true;
	}

	public String getFirstLine(){
		return (result.size() == 0) ? "" : result.getFirst();
	}


//	public void render(ScollTabPanel tabPanel, boolean inText){
//	render(tabPanel, inText, false);
//	}

	public void render(ScollTabPanel tabPanel, boolean inText, boolean addButtons){
		Iterator<String> it = result.iterator();
		if (inText) 
			try {
				tabPanel.textPane.setText("");
				renderText(it, tabPanel, addButtons);
				tabPanel.textPane.setCaretPosition(0);
			}	 
		catch (BadLocationException e) {
			// TODO Auto-generated catch block
			;
		}
		else 
			renderLabel(it, tabPanel);
	}	


	public void renderLabel(Iterator<String> it, ScollTabPanel tabPanel) {
		String content = "";
		String str = "";
		while (it.hasNext()) {
			str = it.next();
			content += (str + "\n");
		}
		tabPanel.statusLabel.setText(content);				
	}

	public void renderText(Iterator<String> it, ScollTabPanel tabPanel)
	throws BadLocationException{
		this.renderText(it, tabPanel, false);
	}

	public void renderText(Iterator<String> it, ScollTabPanel tabPanel, boolean addButtons)
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

	public void renderTable(Iterator<String> it, ScollTabPanel tabPanel) throws BadLocationException {
		this.renderTable(it, tabPanel, false);
	}

	public void renderTable(Iterator<String> it, ScollTabPanel tabPanel, boolean addButtons) throws BadLocationException {
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

	public void renderJpg(Iterator<String> it, ScollTabPanel tabPanel) throws BadLocationException {
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
