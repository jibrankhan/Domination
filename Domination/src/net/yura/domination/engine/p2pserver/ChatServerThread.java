// Yura Mamyrin, Group D

package net.yura.domination.engine.p2pserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import net.yura.domination.engine.core.RiskGame;

/**
 * <p> Chat Server Thread </p>
 * @author Yura Mamyrin
 */

// The main child thread waits for new information in the ChatArea, and 
// sends it out to the eagerly waiting clients

public class ChatServerThread extends Thread {

    LinkedList m_lList = new LinkedList();

    private Socket socket = null;
    int myIndex;
    ChatReader myReaderThread; 
    ChatArea myChatArea;

    public ChatServerThread(Socket socket, ChatArea cArea, int me) {
           super("ChatServerThread");
           this.socket = socket;
           myChatArea = cArea;
           myIndex= me;
           //System.out.println("Creating Thread "+myIndex);
    }

	public void run() {

		String outputLine;
                String id=null;

		try {

			// First create the Streams associated with the Socket

			// Outbound Stream (actually a PrintWriter)

			PrintWriter outChat = new PrintWriter(
                           socket.getOutputStream(), true);

			// Inbound Stream (actually a BufferedReader)

			BufferedReader inChat = new BufferedReader(
                               new InputStreamReader(
                                   socket.getInputStream()));


			// got a connection from the client and do a read right away to get the version

			String hello = inChat.readLine(); // "10 myID#123456 default.map"
			int index = hello.indexOf(' ');
			String version = hello.substring(0,index);
                        
                        if (!RiskGame.NETWORK_VERSION.equals(version)) {

                            outChat.println( "ERROR version missmatch, server: "+RiskGame.NETWORK_VERSION+", and client: "+version );

                        }
                        else {

                            hello = hello.substring(index+1);
                            index = hello.indexOf(' ');
                            id = hello.substring(0,index);
                            String map = hello.substring(index+1);
                        
                            if (!RiskGame.getDefaultMap().equals( map )) {
                                    outChat.println( "server choosemap "+RiskGame.getDefaultMap() );
                            }

                            // Create a separate thread to handle the incomming socket data      
                            myReaderThread = new ChatReader(inChat, myChatArea, myIndex);
                            myReaderThread.start();

                            // meanwhile, this thread will wait for new chatArea data and when
                            // received, it will be dispersed to the connected client. 

                            do  {
                                    outputLine = myChatArea.waitForString(myIndex);
                                    if (outputLine != null)
                                            outChat.println(outputLine);
                            }
                            while (outputLine != null);  

                        }

			// seems to get stuck if called here
			//inChat.close();
			//outChat.close();

			socket.shutdownInput();
			socket.shutdownOutput();

			socket.close();

			inChat.close();
			outChat.close();

		}
		catch (IOException e) {
                       //System.out.println("ChatServerThread IOException: "+
                       //e.getMessage());
                       //RiskUtil.printStackTrace(e);
		}

                myChatArea.putString(myIndex, "LEAVE "+id);
                
		//System.out.println("ChatServerThread Terminating: " + myIndex);

	}
}
