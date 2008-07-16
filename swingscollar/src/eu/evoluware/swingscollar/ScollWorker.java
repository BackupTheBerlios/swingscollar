//Copyright (c) 2008 Fred Spiessens - Evoluware http://www.evoluware.eu
package eu.evoluware.swingscollar;

import java.util.concurrent.ExecutionException;

import swingWorker.SwingWorker;

public class ScollWorker extends SwingWorker<ScollReply, Void> {
	public static final int FIXPTS=0, SOLVEONE=1, SOLVEALL=2, INTERRUPT=3;
	protected int action;
	protected String input;
	protected ScollClient client;
	ScollTabPanel tabPanel;

	public ScollWorker(int action, ScollTabPanel tabPanel, String input, ScollClient client){
		super();
		this.action=action;
		this.tabPanel=tabPanel;
		this.input=input;
		this.client=client;
	}

	@Override
	protected ScollReply doInBackground() throws Exception {
		if (this.action == INTERRUPT) return client.controlReplyTo(this.input);
		else return client.replyTo(this.input); // is it this that can take a long time ?
	}

	@Override
	protected void done() {
		int panelIndex=2;
		if (action==FIXPTS) panelIndex=1;
		if (action==INTERRUPT) {
			try {	
				ScollReply reply = get();	
				panelIndex=0;		
				tabPanel.statusLabel.setText(reply.getFirstLine());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else	
			try {
				ScollReply reply = get();
				//JDialog dialog = (ScollTablPanel)tabPanel.getMainPanel().getCurrentDialog();
				reply.render((ScollTabPanel)tabPanel.getMainPanel().getTabbedPane().getComponent(panelIndex), true, (action != FIXPTS));
				tabPanel.getMainPanel().getTabbedPane().setEnabledAt(panelIndex, true);
				tabPanel.getMainPanel().getTabbedPane().setSelectedComponent(tabPanel.mainPanel.getTabbedPane().getComponentAt(panelIndex));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
