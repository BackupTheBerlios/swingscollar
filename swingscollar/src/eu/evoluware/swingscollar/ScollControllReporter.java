package eu.evoluware.swingscollar;

import java.util.concurrent.ExecutionException;

import swingWorker.SwingWorker;

public class ScollControllReporter extends SwingWorker<String,Void>  {
	protected ScollClient client;
	ScollTabPanel tabPanel;
	boolean buzzy = false;

	public ScollControllReporter(ScollClient cl, ScollTabPanel t) {
		client = cl;
		tabPanel = t;
	}
	
	protected String doInBackground() throws Exception {
		String reply = client.readNextControlMsg();
		this.execute();
		return reply;
	}

	protected void done() {
		String nextMsg = "NO NEXT MSG";
		try {	
			nextMsg = (String) get();	
			tabPanel.statusLabel.setText(nextMsg);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//this.execute();
	}
	
//	protected void executeRepeatedly(){
//		while (true){	
//			execute();
//		}	
//	}
	


}
