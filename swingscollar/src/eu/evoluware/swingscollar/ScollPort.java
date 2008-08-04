package eu.evoluware.swingscollar;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.SwingUtilities;

public class ScollPort { //singleton
	private static volatile ScollPort instance;
	public static synchronized void initialize(int pt, ScollPanel panel){
		ScollPort.instance = new ScollPort(pt, panel);
//		ScollPort.instance.start();
	}
	public static synchronized ScollPort getInstance(){
		return ScollPort.instance;
	}

	private final LinkedBlockingQueue<ScollReply> replyQ = new LinkedBlockingQueue<ScollReply>();
	private volatile ScollPanel panel;
	private volatile ScollClient client;
	private final AtomicBoolean running = new AtomicBoolean(false);
	private ScollPort(int pt, ScollPanel panel){
		this.panel = panel;
		this.client = new ScollClient(pt);
		this.client.setReplyQ(this.replyQ);
	}
	
	public synchronized void getNextReply(){  // use executor
		final Thread T1 = new Thread(){
			public void run() { // only once
				final ScollReply R = client.getNextReply();
				SwingUtilities.invokeLater(new Runnable(){
					public void run(){
						R.render();
					}
				});
			}
		};
		T1.start();
	}	

	public synchronized void stop() {
		running.set(false);
		// add one more (dummy) runnable to replyQ, to end running thread created with start()
		try {
			replyQ.put(ScollReply.testReply("DONE"));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized void sendCmd(String cmd){	
		client.nextCmd(cmd);
	}

	public synchronized ScollClient getClient(){
		return client;
	}

	public synchronized ScollPanel getPanel(){
		return panel;
	}

}
