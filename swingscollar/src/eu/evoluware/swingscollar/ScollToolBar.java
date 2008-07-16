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

	public ScollToolBar(final ScollTabPanel scollTabPanel) {
		tabPanel = scollTabPanel;
	} 

	public void setBtns(String[] BtnSelection){
		final List<String> SelectionLst = Arrays.asList(BtnSelection);
		final int NmbrBtns = (BtnSelection.length);
		btns = new JButton[NmbrBtns];
		final String[] ButtonStrings = {"check syntax", "check", "check scoll syntax"
				,"fixpoints", "fixpts", "find fixpoints"
				,"1 solution", "sol1", "find one solution"
				,"solution", "sols", "find all solutions"
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
//		else if (a == "test") { doTest();}
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
		ScollWorker scollWorker = new ScollWorker(ScollWorker.FIXPTS, tabPanel, input, tabPanel.getScollClient());
		scollWorker.execute();
		//new JF
	}	

	public void doSolveOne(){
		tabPanel.getMainPanel().getTabbedPane().removeAllDetails();
		String input="sol1 0\n"+ tabPanel.textPane.getText();
		ScollWorker scollWorker  = new ScollWorker(ScollWorker.SOLVEONE, tabPanel, input, tabPanel.getScollClient());
		scollWorker.execute();
	}

	public void doSolveAll(){
//		showProgressDialog();
		tabPanel.getMainPanel().getTabbedPane().removeAllDetails();
		String input="sols 0\n"+ tabPanel.textPane.getText();
		ScollWorker scollWorker = new ScollWorker(ScollWorker.SOLVEALL, tabPanel, input, tabPanel.getScollClient());
		scollWorker.execute();
	}	
	
	public void doNothing(){
		tabPanel.statusLabel.setText("nothing to do");
	}

//	public void doTest(){
//		new Thread(heavyRunnable).start(); 
//		ScollProgressDialog dialog = new ScollProgressDialog(this.tabPanel.mainPanel.frame);
//		dialog.setVisible(true);
//
//	}


}
