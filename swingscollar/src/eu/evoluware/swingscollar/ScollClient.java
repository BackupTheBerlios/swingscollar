//Copyright (c) 2008 Fred Spiessens - Evoluware http://www.evoluware.eu

package eu.evoluware.swingscollar;

import java.io.*;
import java.net.*;
//import java.util.concurrent.LinkedBlockingQueue;
//import javax.swing.SwingUtilities;

public class ScollClient {
	private final int serverPort;
	private volatile Socket mainSocket;
	private volatile PrintWriter mainOut;
	private volatile BufferedReader mainIn;
//	private volatile LinkedBlockingQueue<ScollReply> replyQ;

	public ScollClient(int sp) {
		serverPort = sp;
		try {
			mainSocket = new Socket("localhost", serverPort);
//			mainSocket.setSendBufferSize(4096);
//			if (! (mainSocket.getSendBufferSize() == 4096)) {
//			System.out.print("sendbuffersize = ") ;
//			System.out.print(mainSocket.getSendBufferSize()) ;
//			}
			mainOut = new PrintWriter(mainSocket.getOutputStream(), false); // no autoflush after every newline!
			mainIn = new BufferedReader(new InputStreamReader(mainSocket.getInputStream()));
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: localhost.");
			//System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for "
					+ "the connection to: localhost.");
			//System.exit(1);
		}
	}

//	public synchronized void setReplyQ(LinkedBlockingQueue<ScollReply> Q){
//		replyQ = Q;
//	}

//	public BufferedReader getInReader(){
//	return mainIn;	
//	}	

	private synchronized ScollReply getBadReply(String msg){
		final ScollReply reply = new ScollReply("<error>");
		reply.addLine(msg);
		reply.addLine("</error>");
		return reply;
	}

//	public ScollReply replyTo(String request){
//	String replyType = null;
//	ScollReply reply;
//	String tmp;
//	try {
//	mainOut.flush();
//	mainOut.print(request);//mainOut.write(request);
//	mainOut.flush();
//	replyType = mainIn.readLine();
//	reply = new ScollReply(replyType);
//	while (! reply.isComplete()){
//	tmp = mainIn.readLine();
//	if (tmp == null) {
//	reply.endPrematurely();
//	break;
//	}
//	else {
//	reply.addLine(tmp);
//	}
//	}
//	}
//	catch (Exception e){
//	reply = getBadReply("bad communication mainIn ScollReply.replyTo()");	
//	}
//	return reply;
//	}

	public synchronized void nextCmd(String request){
		try {
			mainOut.flush();
			mainOut.print(request);//mainOut.write(request);
			mainOut.flush();
		}
		catch (Exception e){
			e.printStackTrace();	
		}
	}

	public synchronized void sendInterrupt(){
		try {
			mainOut.flush();
			mainOut.print("interrupt\n");//mainOut.write(request);
			mainOut.flush();
			//ScollPort.getInstance().getNextReply(this);
		}
		catch (Exception e){
			e.printStackTrace();	
		}
	}
	
	public synchronized ScollReply getNextReply(){
		String replyType = null;
		ScollReply reply;
		String tmp = null;
		try {
			replyType = mainIn.readLine();
			reply = new ScollReply(replyType);
			while (! reply.isComplete()){
				tmp = mainIn.readLine();
				if (tmp == null) {
					reply.endPrematurely();
					break;
				}
				else {
					reply.addLine(tmp);
				}
			}
		}
		catch (Exception e){
			reply = getBadReply("bad communication mainIn ScollReply.getNextReply()");	
		}
		return reply;
	}	

//	public synchronized void nextReply() throws InterruptedException{
//		replyQ.put(ScollReply.testReply("in client.nextreply()"));
//		final ScollReply R = getNextReply();
//		replyQ.put(R);
//	}


}

