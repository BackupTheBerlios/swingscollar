package eu.evoluware.swingscollar;

import java.io.*;
import java.net.*;

public class ScollClient {
	private int serverPort;
	private Socket mainSocket;
	private PrintWriter mainOut;
	private BufferedReader mainIn;
	
	public ScollClient(int sp) {
		serverPort = sp;
		try {
            mainSocket = new Socket("localhost", serverPort);
//          mainSocket.setSendBufferSize(4096);
//            if (! (mainSocket.getSendBufferSize() == 4096)) {
//            	System.out.print("sendbuffersize = ") ;
//            	System.out.print(mainSocket.getSendBufferSize()) ;
//            }
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
	
	public BufferedReader getInReader(){
		return mainIn;	
	}	
	
	public ScollReply getBadReply(String msg){
		ScollReply reply = new ScollReply("<error>");
		reply.addLine(msg);
		reply.addLine("</error>");
		return reply;
	}
	
	public ScollReply replyTo(String request){
		String replyType = null;
		ScollReply reply;
		String tmp;
		try {
			mainOut.flush();
			mainOut.print(request);//mainOut.write(request);
			mainOut.flush();
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
			reply = getBadReply("bad communication mainIn ScollReply.replyTo()");	
		}
		return reply;
	}
	
	
}

