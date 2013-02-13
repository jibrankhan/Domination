package net.yura.domination.engine.p2pserver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import net.yura.domination.engine.RiskController;

/**
 * <p> Chat Area </p>
 * @author Yura Mamyrin
 */

public class ChatArea extends Thread {


    private ServerSocket serverSocket = null;
    //private static int port = 4444;
    private RiskController gui;


    // You could make this more dynamic, but it's a little
    // simpler to keep the simple array approach
    private ChatServerThread chatArr[]= new ChatServerThread[100];
    private boolean stopFlag = false;



    public ChatArea(RiskController g,int port) throws Exception {

	gui = g;

        InetAddress iaddr = InetAddress.getLocalHost();

	serverSocket = new ServerSocket(port);

	gui.sendMessage("getHostName = " + iaddr.getHostName() , false, false);
	gui.sendMessage("getHostAddress = " + iaddr.getHostAddress() , false, false);
	gui.sendMessage("port = " + port , false, false);

	start();

    }

    public void run() {

	//System.out.print("Server Started\n");

        Socket nextSock;
        int nThreadCount=0;
        ChatServerThread childThread;

	//Vector sockets = new Vector();

        try {

		while(true) {// Forever loop
			// We only exit when someone (main) closes
			// our socket.  At that point we get an 
			// IOException

                       nextSock = serverSocket.accept();

/*
			// this stops 2 clients joining from the same host
			boolean bad=false;
			for (int c=0; c < sockets.size();c++) {

				if (nextSock.getInetAddress().equals( ((Socket)sockets.elementAt(c)).getInetAddress() )) { bad=true; break; }

			}
			if (bad) {

				nextSock.shutdownOutput();
				nextSock.shutdownInput();
				nextSock.close();

				continue;

			}



			sockets.add(nextSock);
*/

                       //System.out.println(nThreadCount +
                       // " Another Thread Created");
			gui.sendMessage( "Another Client has joined: " + nThreadCount, false, false);

                       //chatArr[nThreadCount] = new Chat();

                       chatArr[nThreadCount] = childThread = new ChatServerThread(nextSock, this, nThreadCount++);

                       if (childThread != null) childThread.start();

                       //else 
                           //System.out.println("Unable to create a child thread");
		}              

	} 
	catch (IOException e) {
                   //System.err.println("IOException in Server: " +
                   //e.getMessage());
                   //RiskUtil.printStackTrace(e);
                   //System.exit(-1);

	}
	// The following call should terminate all child threads
	// myChatArea.setStopFlag();
	//System.out.println("Terminating ChatServer");

	gui.sendMessage("no one can join now",false,false);

    }

    // Calling this routine should tell all of the chatserver
    // threads to terminate

    public synchronized void closeSocket() throws IOException {

	serverSocket.close();

	stopFlag=true;

	notifyAll();

    }

    public boolean isOff() {

	return serverSocket.isClosed();
    }


    // Add a new string to all linked lists
    synchronized void putString(int index, String s) {

	for (int i= 0; i < chatArr.length; i++)
           if (chatArr[i] != null)
               chatArr[i].m_lList.addLast(s);
	notifyAll();
	//System.out.println("putString: "+ s);


	// kill the serverSocket so noone can join the game now
	try { serverSocket.close(); } catch (IOException e) { }


    }

    // called to get the list of strings awaiting any given
    // thread
    synchronized String getStrings(int index) {
       if (chatArr[index]==null) return null;

        int i, num;        
       String str;
       StringBuffer sb = new StringBuffer("");
       LinkedList lList = chatArr[index].m_lList;
       num=lList.size();
       try {

           for (i=0; i < num; i++) {

               str = (String)lList.removeFirst();
               sb.append( str);
               sb.append("\n");
           }
       }
       catch (NoSuchElementException e) {
           System.err.println("Our List Count is Messed Up???");
       }
      
       return sb.toString();
    }

    // called to wait for any new messages for a given thread

    synchronized String waitForString(int index) {

       String str;

       do {
		str = getStrings(index);
                
                if (str == null) return null;
                
		try {
                       if (str.length()== 0)               
                           wait(); 
		}
		catch (InterruptedException e) {

                           //System.out.println("Interrupted wait call");
		}

               if (stopFlag)
                   return null;

       } while (str.length() == 0);

       return str;
    }

    // Cread a new chat data structure
    //synchronized void addNewChat(int index) {
    //   chatArr[index] = new Chat();
    //}

    //synchronized void removeChat(int index) {
    //   chatArr[index] = null;
    //}

    synchronized void imDead(int index) {
        chatArr[index] = null;
	notifyAll();
    }
}
