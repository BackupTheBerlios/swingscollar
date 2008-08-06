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
				,"interrupt", "interrupt", "interrupt current calculation"
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

	public synchronized void actionPerformed(final ActionEvent action) {
		final String a = action.getActionCommand();
		if (a == "check") { doCheckSyntax();}
		else if	(a == "fixpts") { doFixpts();}
		else if (a == "sol1") { doSolveOne();}
		else if (a == "sols") { doSolveAll();}
		else if (a == "interrupt") { doInterrupt();}
		else {;};
			//}
			//else {doNothing();}
	}

	public synchronized void doCheckSyntax(){
		ScollPort.getInstance().sendCmd("check\n"+ tabPanel.textPane.getText());
		ScollPort.getInstance().getNextReply();
	}

//	public boolean doTestSyntax(){
//	ScollReply reply = tabPanel.getScollClient().replyTo("check\n"+ tabPanel.textPane.getText());
//	String str = reply.getFirstLine();
//	if (reply.isComplete() && str.equals("OK")) {
//	return true;
//	}	
//	else {
//	reply.render(tabPanel, false, false);
//	return false;
//	}		
//	}

	public synchronized void doFixpts(){
		tabPanel.getMainPanel().getTabbedPane().removeAllDetails();
		String input="fixpts\n"+ tabPanel.textPane.getText();
		ScollPort.getInstance().sendCmd(input);
		ScollPort.getInstance().getNextReply();
	}	

	public synchronized void doSolveOne(){
		tabPanel.getMainPanel().getTabbedPane().removeAllDetails();
		String input="sol1 0\n"+ tabPanel.textPane.getText(); // timeout = 0
		ScollPort.getInstance().sendCmd(input);
		ScollPort.getInstance().getNextReply();
	}

	public synchronized void doSolveAll(){
//		showProgressDialog();
		tabPanel.getMainPanel().getTabbedPane().removeAllDetails();
		String input="sols 0\n"+ tabPanel.textPane.getText(); // timeout = 0
		ScollPort.getInstance().sendCmd(input);
		ScollPort.getInstance().getNextReply();
	}	

	public synchronized void doInterrupt(){
		ScollPort.getInstance().interrupt();
	}
	
	public synchronized void doNothing(){
		tabPanel.statusLabel.setText("nothing to do");
	}

//	public void doTest(){
//	new Thread(heavyRunnable).start(); 
//	ScollProgressDialog dialog = new ScollProgressDialog(this.tabPanel.mainPanel.frame);
//	dialog.setVisible(true);

//	}


}
