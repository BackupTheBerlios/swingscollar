//Copyright (c) 2008 Fred Spiessens - Evoluware http://www.evoluware.eu
package eu.evoluware.swingscollar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;

@SuppressWarnings("serial")
public class ScollToolBar extends JToolBar implements ActionListener  {

	private  JButton[]  btns;
	private final ScollTabPanel tabPanel;
	private  Runnable heavyRunnable;

	public ScollToolBar(final ScollTabPanel scollTabPanel) {
		tabPanel = scollTabPanel;
		heavyRunnable = new Runnable(){ 
			public void run(){ 
				ScollProgressMonitor monitor = ScollProgressUtil.createScolllProgressMonitor(tabPanel.mainPanel.frame, 100, false, 1000); 
				monitor.start("Fetching 1 of 10 records from database..."); 
				try{ 
					for(int i=0; i<10; i+=1){ 
						fetchRecord(i); 
						monitor.setCurrent("Fetching "+(i+1)+" of 10 records from database", (i+1)*10); 
					} 
				} finally{ 
					// to ensure that progress dlg is closed in case of any exception 
					if(monitor.getCurrent()!=monitor.getTotal()) 
						monitor.setCurrent(null, monitor.getTotal()); 
				} 
			} 

			private void fetchRecord(int index){ 
				try{ 
					Thread.sleep(1000); 
				} catch(InterruptedException e){ 
					e.printStackTrace(); 
				} 
			} 
		}; 
	} 

	public void setBtns(String[] BtnSelection){
		final List<String> SelectionLst = Arrays.asList(BtnSelection);
		final int NmbrBtns = (BtnSelection.length);
		btns = new JButton[NmbrBtns];
		final String[] ButtonStrings = {"check syntax", "check", "check scoll syntax"
				,"fixpoints", "fixpts", "find fixpoints"
				,"1 solution", "sol1", "find one solution"
				,"solution", "sols", "find all solutions"
//				,"test", "test", "do test"
//				,"add panel", "addPanel", "add a panel"
//				,"reset", "reset", "remove details"
		};
		for (int i = 0; i < ButtonStrings.length; i+=3){
			if (SelectionLst.contains(ButtonStrings[i+1])){
				addButton(i/3,ButtonStrings[i],ButtonStrings[i+1],ButtonStrings[i+2]);
			}
		}

	}

	private void addButton(final int index, final String label, final String actionCommand, final String toolTipText){
		final JButton btn = new JButton(label);
		btn.setActionCommand(actionCommand);
		btn.setToolTipText(toolTipText);
		btn.addActionListener(this);
		add(btn);
		btns[index] = btn;
	}

	public void actionPerformed(final ActionEvent action) {
		final String a = action.getActionCommand();

		if (a == "check") { doCheckSyntax();}
		else if (a == "test") { doTest();}
//		else if (a == "addPanel") {doAddPanel();}
//		else if (a == "reset") {doReset();}
		else if (doTestSyntax()){
			if	(a == "fixpts") { doFixpts();}
			else if (a == "sol1") { doSolveOne();}
			else if (a == "sols") { doSolveAll();}
			else {;};
		}
		else {doNothing();}
	}

	public void doCheckSyntax(){
		ScollReply reply = tabPanel.getScollClient().replyTo("check\n"+ tabPanel.textPane.getText());
		reply.render(tabPanel, false, false);
		//reply.render(tabPanel, true);
	}

	public boolean doTestSyntax(){
		ScollReply reply = tabPanel.getScollClient().replyTo("check\n"+ tabPanel.textPane.getText());
		String str = reply.getFirstLine();
		if (reply.isComplete() && str.equals("OK")) {
			return true;
		}	
		else {
			reply.render(tabPanel, false, false);
			return false;
		}		
	}

	public void doFixpts(){
		tabPanel.getMainPanel().getTabbedPane().removeAllDetails();
		String input="fixpts\n"+ tabPanel.textPane.getText();
		ScollWorker worker = new ScollWorker(ScollWorker.FIXPTS, tabPanel, input, tabPanel.getScollClient());
		worker.execute();
		//new JF
	}	

	public void doSolveOne(){
		tabPanel.getMainPanel().getTabbedPane().removeAllDetails();
		String input="sol1 1 120\n"+ tabPanel.textPane.getText();
		ScollWorker worker = new ScollWorker(ScollWorker.SOLVEONE, tabPanel, input, tabPanel.getScollClient());
		worker.execute();
	}

	public void doSolveAll(){
//		showProgressDialog();
		tabPanel.getMainPanel().getTabbedPane().removeAllDetails();
		String input="sols 1 120\n"+ tabPanel.textPane.getText();
		ScollWorker worker = new ScollWorker(ScollWorker.SOLVEALL, tabPanel, input, tabPanel.getScollClient());
		worker.execute();
	}	


	public void doNothing(){
		tabPanel.statusLabel.setText("nothing to do");
	}

	public void doTest(){
		new Thread(heavyRunnable).start(); 
//		ScollProgressDialog dialog = new ScollProgressDialog(this.tabPanel.mainPanel.frame);
//		dialog.setVisible(true);

	}

//	private static void showProgressDialog() {
//	//Create and set up the window.
//	JFrame frame = new JFrame("Calculation In Progress");
//	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//	//Create and set up the content pane.
//	JComponent newContentPane 	= new ScollProgressDialog(frame);
//	newContentPane.setOpaque(true); //content panes must be opaque
//	frame.setContentPane(newContentPane);

//	//Display the window.
//	frame.pack();
//	frame.setVisible(true);
//	}

}
