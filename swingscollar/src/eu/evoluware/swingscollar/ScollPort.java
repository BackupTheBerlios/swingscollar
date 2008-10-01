//Copyright (c) 2008 Fred Spiessens - Evoluware http://www.evoluware.eu

package eu.evoluware.swingscollar;

//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.atomic.AtomicBoolean;

import java.util.concurrent.*;

import javax.swing.SwingUtilities;

public class ScollPort { //singleton
	private static volatile ScollPort instance;
	public static synchronized void initialize(int pt, int controlPt, ScollPanel panel){
		ScollPort.instance = new ScollPort(pt, controlPt, panel);
//		ScollPort.instance.start();
	}
	public static synchronized ScollPort getInstance(){
		return ScollPort.instance;
	}
	
	private final ExecutorService executor = Executors.newSingleThreadExecutor();
	private volatile ScollPanel panel;
	private volatile ScollClient client;
	private volatile ScollClient controlClient;
	private ScollPort(int pt, int controlPt, ScollPanel panel){
		this.panel = panel;
		this.client = new ScollClient(pt);
		this.controlClient = new ScollClient(controlPt);
	}
	
	public synchronized void getNextReply(final ScollClient cl){  // use executor
		executor.execute(new Runnable() { 
			public void run(){	// only once
				final ScollReply R = cl.getNextReply();
				SwingUtilities.invokeLater(new Runnable(){
					public void run(){
						R.render();
					}
				});
			}
		});
	}	
	
	public synchronized void getNextReply(){
		getNextReply(this.client);
	}


	public synchronized void sendCmd(String cmd){	
		client.nextCmd(cmd);
	}

	public synchronized void interrupt(){	
		controlClient.sendInterrupt();
	}
	
	public synchronized ScollPanel getPanel(){
		return panel;
	}

}
