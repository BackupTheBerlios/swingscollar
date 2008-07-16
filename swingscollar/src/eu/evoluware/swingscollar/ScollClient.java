package eu.evoluware.swingscollar;

import java.io.*;
import java.net.*;

public class ScollClient {
	private int serverPort, controlPort;
	private Socket mainSocket, controlSocket;
	@SuppressWarnings("unused")
	private PrintWriter mainOut,  controlOut;
	@SuppressWarnings("unused")
	private BufferedReader mainIn,  controlIn;
	
	public ScollClient(int sp, int cp) {
		serverPort = sp;
		controlPort = cp;
		try {
            mainSocket = new Socket("localhost", serverPort);
//          mainSocket.setSendBufferSize(4096);
//            if (! (mainSocket.getSendBufferSize() == 4096)) {
//            	System.out.print("sendbuffersize = ") ;
//            	System.out.print(mainSocket.getSendBufferSize()) ;
//            }
            mainOut = new PrintWriter(mainSocket.getOutputStream(), false); // no autoflush after every newline!
            mainIn = new BufferedReader(new InputStreamReader(mainSocket.getInputStream()));
            controlSocket = new Socket("localhost", controlPort);
           controlOut = new PrintWriter(controlSocket.getOutputStream(), false);
            controlIn = new BufferedReader(new InputStreamReader(controlSocket.getInputStream()));
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
	
	
	public ScollReply controlReplyTo(String code){
		String replyCode = "Java exception in sendControl()";
		ScollReply reply;
		try{ 
			controlOut.flush();
			controlOut.print(code);
			controlOut.flush();
			replyCode = "INTERRUPT WAS SENT";
			reply = new ScollReply("<control>");
			reply.addLine(replyCode);
		}
		catch (Exception e){
			reply = getBadReply("bad communication mainIn ScollReply.controlReplyTo()");	
		}
		return reply;
	}
	
	public String readNextControlMsg(){
		String msg = "Java exception in readNextControlMsg()";
		try{ 
			msg = controlIn.readLine();
		}
		catch (Exception e){
			;	
		}
		return msg;
	}
	
	
}

