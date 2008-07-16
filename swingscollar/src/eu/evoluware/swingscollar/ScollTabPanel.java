//Copyright (c) 2008 Fred Spiessens - Evoluware http://www.evoluware.eu
package eu.evoluware.swingscollar;

import java.awt.Dimension;
import javax.swing.*;
import javax.swing.text.*;

@SuppressWarnings("serial")
 public class ScollTabPanel extends JPanel {
	protected static final String newline = "\n";
	protected final SpringLayout tabSpringlayout;
	protected final JLabel statusLabel;
	protected ScollTextPane textPane;
	//protected ScollListener scollListener;
	protected final ScollToolBar scollBtns;
	protected final ScollPanel mainPanel;
	protected final JScrollPane paneScrollPane; 
	protected final StyledDocument doc;
	
	public ScollTabPanel(ScollPanel mp)
	{
		mainPanel = mp;
	
		//Create the button bqr 
		scollBtns = new ScollToolBar(this);
		
		//Create a label to put messages during an action event.
		statusLabel = new JLabel(this.getClass().getName());
		
		//Create a text pane.
		textPane = new ScollTextPane();
		paneScrollPane = new JScrollPane(textPane);
		
		
		//layout
		tabSpringlayout = new SpringLayout();
		tabSpringlayout.putConstraint(SpringLayout.WEST, scollBtns, 5,
				SpringLayout.WEST, this);
		tabSpringlayout.putConstraint(SpringLayout.WEST, paneScrollPane, 5,
				SpringLayout.WEST, this);
		tabSpringlayout.putConstraint(SpringLayout.WEST, statusLabel, 5,
				SpringLayout.WEST, this);
		tabSpringlayout.putConstraint(SpringLayout.EAST, this, 5,
				SpringLayout.EAST, paneScrollPane);
		tabSpringlayout.putConstraint(SpringLayout.NORTH, scollBtns, 5,
				SpringLayout.NORTH, this);
		tabSpringlayout.putConstraint(SpringLayout.NORTH, paneScrollPane, 5,
				SpringLayout.SOUTH, scollBtns);
		tabSpringlayout.putConstraint(SpringLayout.NORTH, statusLabel, 5,
				SpringLayout.SOUTH, paneScrollPane);
		tabSpringlayout.putConstraint(SpringLayout.SOUTH, this, 5,
				SpringLayout.SOUTH, statusLabel);
		this.setLayout(tabSpringlayout);
		
		paneScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		//paneScrollPane.setPreferredSize(new Dimension(800, 600));
		this.setPreferredSize(new Dimension(600, 600));
		textPane.setEditorKit(new ScollEditorKit());

		//Put everything together.	
		this.add(scollBtns);
		this.add(paneScrollPane);
		this.add(statusLabel);

		//create pattern document
		doc = new ScollDocument();
		//scollListener = new ScollListener(textPane, statusLabel);
	}
	
	public ScollPanel getMainPanel(){
		return this.mainPanel;
	}

	public ScollClient getScollClient(){
		return mainPanel.scollClient;
	}
	
	public ScollToolBar getToolBar(){
		return scollBtns;
	}

}
