package eu.evoluware.swingscollar;

import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.SwingUtilities;

public class ScollPort {
	private final LinkedBlockingQueue<Runnable> replyQ = new LinkedBlockingQueue<Runnable>();
	private volatile ScollClient client;
	private volatile boolean running = true;
	public ScollPort(int pt){
		this.client = new ScollClient(pt);
	}

	private void handleNextReply() throws InterruptedException{
		Runnable R = replyQ.take();
		SwingUtilities.invokeLater(R);
	}
	
	public void start(){
		client.setReplyQ(this.replyQ);
		Thread T1 = new Thread(new Runnable(){
			public void run() {
				while (running){
					try {
						handleNextReply();
					} catch (InterruptedException e) {
						running = false;
					}
				}}});
		T1.start();
	}
	
	public void stop() {
		running = false;
		// add one more (dummy) runnable to replyQ, to end running thread created with start
		try {
			replyQ.put(new Runnable(){public void run(){;}});
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendCmd(String cmd){	
		client.nextCmd(cmd);
	}


}
