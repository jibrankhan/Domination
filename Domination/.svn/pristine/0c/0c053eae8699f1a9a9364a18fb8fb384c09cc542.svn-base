// Yura Mamyrin, Group D

package net.yura.domination.ui.commandline;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import net.yura.domination.engine.Risk;
import net.yura.domination.engine.RiskAdapter;
import net.yura.domination.engine.RiskUIUtil;
import net.yura.domination.engine.RiskUtil;

/**
 * <p> Command Line Interface for Risk </p>
 * @author Yura Mamyrin
 */

public class CommandText extends Thread {

    private final static String version="1.0.4.1";

    private Risk risk;
    private BufferedReader br;

    private boolean IgnoreNext;

    CommandText(Risk r) {

	risk = r;

	risk.addRiskListener( new RiskAdapter() {

	    /**
	     * Checks if redrawing or repainting is needed 
	     * @param output
	     * @param redrawNeeded If frame needs to be redrawn
	     * @param repaintNeeded If frame needs to be repainted 
	     */
	    public void sendMessage(String output, boolean redrawNeeded, boolean repaintNeeded) {

		if (IgnoreNext) {
		    IgnoreNext=false;
		}
		else {
		    System.out.print(output+"\n");
		}

	    }

	    /**
	     * checks if the the frame needs input 
	     * @param s determines what needs input
	     */
	    public void needInput(int s) {

		synchronized(CommandText.this) {
		    CommandText.this.notify();
		}

	    }


	    /**
	     * Blocks the game panel
	     */
	    public void noInput() {

	// This does not work, something should be here

	//	synchronized(CommandText.this) {
	//	    try { CommandText.this.wait(); }
	//	    catch(InterruptedException e){}
	//	}

	    }

	} );


	InputStreamReader in = new InputStreamReader(System.in);
	br = new BufferedReader(in);

	System.out.print("Welcome to Command Line Risk!\n");

	this.start();

    }

    public static void Help() {

	String commands="";

	try {
	    FileReader filein = new FileReader("commands.txt");
	    BufferedReader bufferin = new BufferedReader(filein);
	    String input = bufferin.readLine();
	    System.out.print("Commands:\n");
	    while(input != null) {
		System.out.print(input+"\n");
		input = bufferin.readLine();
	    }
	    bufferin.close();

	}
	catch (FileNotFoundException e) {
	    System.out.print("Unable to find file commands.txt\n");
	}
	catch (IOException e) {
	    System.out.print("Unable to read file commands.txt\n");
	}

    }

    /**
     * loops through when Input is needed
     */
    public void run() {

      while(true) {

	String input="";
	System.out.print(">");

	try {
	    input = br.readLine();
	    if (input.equals("help")) {
		Help();
		continue;
	    }
	    else if (input.equals("about")) {
		System.out.print("Command Line for Risk, version: "+version+"\nMade by Yura Mamyrin (yura@yura.net)\n");

		String os = System.getProperty("os.name");
		String jv = System.getProperty("java.version");

		System.out.print("Java version: " + jv + "\n");
		System.out.print("Operating System: " + os + "\n");

		continue;
	    }
	    else if (input.equals("manual")) {
		try {
			RiskUtil.openDocs("help/index_commands.htm");
			System.out.print("Manual opened.\n");
		}
		catch(Exception e) {
			System.out.print("unable to open manual: "+e.getMessage()+"\n");
		}
		continue;
	    }
	    else if (input.equals("exit")) {
		System.out.print("Thank you for Playing Risk.\n");
		System.exit(0);
	    }
	    else {

		synchronized(this) {

		    IgnoreNext=true;
		    risk.parser(input);

		    try { this.wait(); }
		    catch(InterruptedException e){}

		}

	    }

	}
	catch (Exception e) {
	    System.out.print("\nThank you for Playing Risk.\n");
	    System.exit(0);
	}

      }

    }

    /**
     * This runs the program
     * @param argv
     */
    public static void main(String[] argv) {

        RiskUIUtil.parseArgs(argv);

	new CommandText( new Risk() );

    }

}
