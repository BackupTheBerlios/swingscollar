//Copyright (c) 2008 Fred Spiessens - Evoluware http://www.evoluware.eu
package eu.evoluware.swingscollar;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class ScollProgressDialog extends JDialog implements ChangeListener, ActionListener{ 
//	volatile int solutions = 0;
	volatile JLabel statusLabel = new JLabel(); 
	volatile JProgressBar progressBar = new JProgressBar(0,100); 
	volatile JLabel progressLabel = new JLabel(); 
	volatile JButton interruptBtn = new  JButton("Interrupt"); 

	public ScollProgressDialog(Frame owner) throws HeadlessException{ 
		super(owner, "Scollar Calculation Progress", false); 
		init();
	} 

	private synchronized void init(){
		progressBar.setValue(0); 
		statusLabel.setText("No solutions found yet"); 
		progressLabel.setText("  /  "); 
		progressBar.setStringPainted(true);
		progressBar.setString("  /  ");
		interruptBtn.setActionCommand("interrupt");
		interruptBtn.setToolTipText("Stop the ongoing calculation and see the partial results");
		interruptBtn.addActionListener(this);
		JPanel contents = (JPanel)getContentPane(); 
		contents.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); 
		contents.add(statusLabel, BorderLayout.NORTH); 
		contents.add(progressBar,BorderLayout.CENTER); 
		contents.add(progressLabel,BorderLayout.WEST); 
		contents.add(interruptBtn, BorderLayout.SOUTH); 
//		contents.validate();
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE); 
	} 

	public synchronized void actionPerformed(final ActionEvent action) {
		final String a = action.getActionCommand();
		if (a == "interrupt") {
			this.endOfCalculation();
			ScollPort.getInstance().interrupt();
		}
		else {;};
		//}
		//else {doNothing();}
	}
//	public synchronized void addSolution(int i){ 
//	solutions =+ i;
//	statusLabel.setText(Integer.toString(solutions) + " solutions found."); 	
//	}

	public synchronized void parseUpdate(String str){
		int splitpt = str.indexOf("/");
		if (splitpt > 0) {
			String p1 = str.substring(0,splitpt);
			String p2 = str.substring(splitpt+1,str.length());
			int i1 = Integer.parseInt(p1);
			int i2 = Integer.parseInt(p2);
			progressBar.setValue((i1 * 100) / i2);
			progressLabel.setText(str);
			progressBar.setString(str);
		}
		else	{
			statusLabel.setText(str);
		}

	}


	public synchronized void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub

	}

	public synchronized void endOfCalculation(){
		interruptBtn.setEnabled(false);
		progressBar.setEnabled(false);
	}

} 