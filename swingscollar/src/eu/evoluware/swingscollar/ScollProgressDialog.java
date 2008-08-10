//Copyright (c) 2008 Fred Spiessens - Evoluware http://www.evoluware.eu
package eu.evoluware.swingscollar;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.HeadlessException;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class ScollProgressDialog extends JDialog implements ChangeListener{ 
//	volatile int solutions = 0;
	volatile JLabel statusLabel = new JLabel(); 
	volatile JProgressBar progressBar = new JProgressBar(); 
	volatile JLabel progressLabel = new JLabel(); 


	public ScollProgressDialog(Frame owner) throws HeadlessException{ 
		super(owner, "Scollar Calculation Progress", false); 
		init();
	} 

	private synchronized void init(){
		progressBar = new JProgressBar(0, 100); 
		progressBar.setValue(0); 
		statusLabel.setText("No solutions found yet"); 
		progressLabel.setText("  /  "); 
		JPanel contents = (JPanel)getContentPane(); 
		contents.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); 
		contents.add(statusLabel, BorderLayout.NORTH); 
		contents.add(progressBar); 
		contents.add(progressLabel, BorderLayout.WEST); 
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE); 
	} 


//	public synchronized void addSolution(int i){ 
//		solutions =+ i;
//		statusLabel.setText(Integer.toString(solutions) + " solutions found."); 	
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
		}
		else	{
			statusLabel.setText(str);
		}

	}


	public synchronized void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub

	}

} 