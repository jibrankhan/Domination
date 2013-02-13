// Yura Mamyrin, Group D

package net.yura.domination.ui.increment1gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import net.yura.domination.engine.Risk;
import net.yura.domination.engine.RiskAdapter;
import net.yura.domination.engine.RiskUIUtil;
import net.yura.domination.engine.RiskUtil;
import net.yura.domination.engine.guishared.AboutDialog;

/**
 * <p> Increment1 GUI </p>
 * @author Yura Mamyrin
 */

public class Increment1Frame extends JFrame {

    private final static String version = "1.1.1.1";
    private final static String product = "Increment1 GUI for RISK";

    private BorderLayout layout;

    private JTextArea Console;
    private JTextArea Testing;
    private JTextField Command;
    private JButton Submit;

    private JLabel Pix;
    private JLabel statusBar;
    private JScrollPane Con;
    private JScrollPane Log;

    private JLabel logLabel;

    private Risk risk;
    private Vector history;
    private int pointer;
    private String temptext;

    /** Creates new form JFrame */

    /**
     * Creates a new RiskGUI
     * @param r The Risk object for this GUI
     */
    public Increment1Frame(Risk r) {

	risk = r;

	risk.addRiskListener( new Increment1RiskAdapter() );

	history = new Vector();
	pointer=-1;

	/* * UIManager used to be here * */

	// layout = new BorderLayout();

	Console = new JTextArea();
	Testing = new JTextArea();
	Command = new JTextField();
	Submit = new JButton();

	Pix = new JLabel(new javax.swing.ImageIcon( this.getClass().getResource("map.png") ));
	statusBar = new JLabel("Loading...");
	Con = new JScrollPane(Console);
	Log = new JScrollPane(Testing);

	logLabel = new JLabel("Test Log");

        initGUI();

        setIconImage(Toolkit.getDefaultToolkit().getImage( AboutDialog.class.getResource("icon.gif") ));

	// setSize(910,675); // (860, 670) // 580
	setResizable(false);

        Testing.append("creating GUI...\n");

        //Center the frame on screen
        //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //Dimension frameSize = getSize();
        //frameSize.height = ((frameSize.height > screenSize.height) ? screenSize.height : frameSize.height);
        //frameSize.width = ((frameSize.width > screenSize.width) ? screenSize.width : frameSize.width);
        //setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

        pack();
        //setVisible(true);

        Testing.append("Program loaded\n");
	statusBar.setText("Ready");

    }

    /** This method is called from within the constructor to initialize the form. */

    /**
     * Initialises the GUI
     */
    private void initGUI() {

        getContentPane().setLayout(new java.awt.GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        // set title
        setTitle(product);

        // set the border of the window
        //setDefaultLookAndFeelDecorated(true);
        //setUndecorated(true);
        //getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        //Testing.setBounds(new java.awt.Rectangle(1,1,53,16));
        // getContentPane().setLayout(layout);
        // JPanel content = new JPanel();
        // content.setPreferredSize(new Dimension(300, 200));
        // content.setLayout(new java.awt.GridBagLayout());

        c.insets = new java.awt.Insets(3, 3, 3, 3);
        c.fill = GridBagConstraints.BOTH;

        Console.setText("");
        // Console.setBackground(Color.white); // not needed with swing
        Console.setEditable(false);

        // Testing.setColumns(20); // the one (causes odd problems)
        Testing.setEditable(false);

	Log.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	Log.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	Log.setPreferredSize(new java.awt.Dimension(200,550));
	Log.setMinimumSize(new java.awt.Dimension(200,550));

	Con.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	Con.setPreferredSize(new java.awt.Dimension(677,150));
	Con.setMinimumSize(new java.awt.Dimension(677,150));

        //	c.ipadx = 600; // width
        //	c.ipadx = 0; // width
	Command.setPreferredSize(new java.awt.Dimension(600,20));
	Command.setMinimumSize(new java.awt.Dimension(600,20));
	Command.setMaximumSize(new java.awt.Dimension(600,20));
        // Command.setColumns(75); // dont use because it goes odd in linux


        Submit.setText(" Submit ");

        c.gridx = 0; // col
        c.gridy = 0; // row
        c.gridwidth = 2; // width
        c.gridheight = 2; // height
        getContentPane().add(Pix, c);

        c.gridx = 1; // col
        c.gridy = 3; // row
        c.gridwidth = 1; // width
        c.gridheight = 1; // height
        getContentPane().add(Submit, c);

        c.gridx = 2; // col
        c.gridy = 0; // row
        c.gridwidth = 1; // width
        c.gridheight = 1; // height
        getContentPane().add(logLabel, c);

        // c.ipadx = 700; // width
        // c.ipady = 140; // height

        c.gridx = 0; // col
        c.gridy = 2; // row
        c.gridwidth = 2; // width
        c.gridheight = 1; // height
        getContentPane().add(Con, c);

        // c.ipadx = 600; // width
        // c.ipady = 0; // height

        c.gridx = 0; // col
        c.gridy = 3; // row
        c.gridwidth = 1; // width
        c.gridheight = 1; // height
        getContentPane().add(Command, c);

        // c.ipadx = 60; // width
        // c.ipady = 300; // 570 height

        c.gridx = 2; // col
        c.gridy = 1; // row
        c.gridwidth = 1; // width
        c.gridheight = 3; // height
        getContentPane().add(Log, c);

        // c.ipadx = 0; // width
        // c.ipady = 0; // height

        //c.gridx = 0; // col
        //c.gridy = 4; // row
        //c.gridwidth = 2; // width
        //c.gridheight = 1; // height
        // add status bar
        //getContentPane().add(gameStatus, c);

        c.gridx = 0; // col
        c.gridy = 4; // row
        c.gridwidth = 3; // width
        c.gridheight = 1; // height
        // add status bar
        getContentPane().add(statusBar, c);

        ActionListener readCommand = new ActionListener() {
	    public void actionPerformed(ActionEvent a) {

		//Submit.setEnabled(false);
		//Command.setEnabled(false);
		//statusBar.setText("Working...");

		String input = Command.getText();
		Command.setText("");

		history.add(input);
		pointer = history.size()-1;
		go(input);

		//Submit.setEnabled(true);
		//Command.setEnabled(true);
		Command.requestFocus();
		//statusBar.setText("Done... Ready");

	    }
        };

        Submit.addActionListener( readCommand );
	Command.addActionListener( readCommand );


	class CommandKeyAdapter extends KeyAdapter {
	   Increment1Frame adaptee;
 

	   CommandKeyAdapter(Increment1Frame adaptee) {
	     this.adaptee = adaptee;
	   }
	   public void keyPressed(KeyEvent key) {


		if (key.getKeyCode() == 38) {
		    Testing.append("up key (history)\n");

		    if (pointer < 0) {
			Toolkit.getDefaultToolkit().beep();
		    }
		    else {
			if (pointer == history.size()-1) { temptext=Command.getText(); }
			Command.setText( (String)history.elementAt(pointer) );
			pointer--;
		    }
		}
		else if(key.getKeyCode() == 40) {
		    Testing.append("down key (history)\n");


		    if (pointer > history.size()-2 ) {
			Toolkit.getDefaultToolkit().beep();
		    }
		    else if (pointer == history.size()-2 ) {
			Command.setText(temptext);
			pointer++;
		    }
		    else {
			pointer=pointer+2;
			Command.setText( (String)history.elementAt(pointer) );
			pointer--;
		    }

		}
		else {
		    pointer = history.size()-1;
		}

	   }
	}

	Command.addKeyListener( new CommandKeyAdapter(this) );




        // add menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        menuFile.setMnemonic('F');

        // create About menu item
        JMenu menuHelp = new JMenu("Help");
        menuHelp.setMnemonic('H');

        JMenuItem Commands = new JMenuItem("Commands");
        Commands.setMnemonic('C');
        Commands.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
		    Commands();
                }
            });
        menuHelp.add(Commands);

        JMenuItem helpMan = new JMenuItem("Manual");
        helpMan.setMnemonic('M');
        helpMan.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
		    go("manual");
                }
            });
        menuHelp.add(helpMan);

        JMenuItem helpAbout = new JMenuItem("About");
        helpAbout.setMnemonic('A');
        helpAbout.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    openAbout();
                }
            });
        menuHelp.add(helpAbout);

        // create Clear menu item
        JMenu menuClear = new JMenu("Clear");
        menuClear.setMnemonic('C');

        JMenuItem ClearConsole = new JMenuItem("Clear Console");
        ClearConsole.setMnemonic('s');
        ClearConsole.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Testing.append("Console cleared\n");
		    Console.setText("");
                }
            });
        menuClear.add(ClearConsole);

        JMenuItem ClearTestLog = new JMenuItem("Clear TestLog");
        ClearTestLog.setMnemonic('T');
        ClearTestLog.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Testing.append("TestLog cleared\n");
		    Testing.setText("");
                }
            });
        menuClear.add(ClearTestLog);

        JMenuItem ClearHistory = new JMenuItem("Clear History");
        ClearHistory.setMnemonic('H');
        ClearHistory.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
		    history.clear();
		    pointer = -1;
                    Testing.append("History cleared\n");
                }
            });
        menuClear.add(ClearHistory);

        // create Open menu item
        final JFileChooser fc = new JFileChooser();
        JMenuItem openFile = new JMenuItem("Run Script");
        openFile.setMnemonic('R');
        openFile.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    int returnVal = fc.showOpenDialog(Increment1Frame.this);
                    if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
                        java.io.File file = fc.getSelectedFile();
                        // Write your code here what to do with selected file

			    try {
				//Submit.setEnabled(false);
				//Command.setEnabled(false);
				//statusBar.setText("Working...");

        			Testing.append("Opening file: "+ file.getPath() +"\n");
        			Testing.append("Running Script...\n");

				FileReader filein = new FileReader(file);
				BufferedReader bufferin = new BufferedReader(filein);

				String input = bufferin.readLine();
				while(input != null) {

				    go(input);
				    input = bufferin.readLine();

				}
				bufferin.close();
        			Testing.append("Script end\n");

				//Submit.setEnabled(true);
				//Command.setEnabled(true);
				//statusBar.setText("Done... Ready");
			    }
			    catch(Exception error) {
				    Testing.append("Error: "+error.getMessage() + "\n");
 			    }

                    } else {
                        // Write your code here what to do if user has canceled Open dialog
                    }
                }
            });
        menuFile.add(openFile);

        // create Save menu item
        JMenuItem saveFile = new JMenuItem("Save Console");
        saveFile.setMnemonic('S');
        saveFile.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    int returnVal = fc.showSaveDialog(Increment1Frame.this);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        java.io.File file = fc.getSelectedFile();
                        // Write your code here what to do with selected file

			    try {
				//Submit.setEnabled(false);
				//Command.setEnabled(false);
				//statusBar.setText("Working...");

        			Testing.append("Creating file: "+ file.getPath() +"\n");
        			Testing.append("Writing to file...\n");

			        FileWriter fileout = new FileWriter(file);
			        BufferedWriter buffer = new BufferedWriter(fileout);
			        PrintWriter printer = new PrintWriter(buffer);

			        printer.write(Console.getText());

			        printer.close();
        			Testing.append("File Saved\n");

				//Submit.setEnabled(true);
				//Command.setEnabled(true);
				//statusBar.setText("Done... Ready");
			    }

			    catch(Exception error) {
				    Testing.append("Error: "+error.getMessage() + "\n");
 			    }

                    } else {
                        // Write your code here what to do if user has canceled Save dialog
                    }
                }
            });
        menuFile.add(saveFile);

        // create Print menu item
        JMenuItem print = new JMenuItem("Print");
        print.setMnemonic('P');
        print.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    PrintDialog.print();
                }
            }); menuFile.add(print);

        // create Exit menu item
        JMenuItem fileExit = new JMenuItem("Exit");
        fileExit.setMnemonic('E');
        fileExit.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
        menuFile.add(fileExit);

        menuBar.add(menuFile);
        menuBar.add(menuClear);
        menuBar.add(menuHelp);

        // sets menu bar
        setJMenuBar(menuBar);
        setBounds(new java.awt.Rectangle(0,0,905,629));
        addWindowListener(
            new java.awt.event.WindowAdapter() {
                public void windowClosing(java.awt.event.WindowEvent evt) {
                    exitForm(evt);
                }
            }
	);

        // pack();

    }

    /** Exit the Application */

    /**
     * Closes the GUI
     * @param evt Close button was pressed
     */
    private void exitForm(WindowEvent evt) {
        Testing.append("Exit\n");
        System.exit(0);
    }

    /**
     * Submits input to parser if neccessary
     * @param input The string that is checked
     */
    public void go(String input) {

        Testing.append("Submitted: \""+input+"\"\n");

	if (input.equals("exit") ) {
	    // Testing.append("Exit.\n");
            System.exit(0);
	}
	else if (input.equals("help") ) {
	    Commands();
	}
	else if (input.equals("about") ) {
	    openAbout();
	}
	else if (input.equals("clear") ) {
	    Console.setText("");
	    Testing.append("Console cleared\n");
	}
	else if (input.equals("manual") ) {

		try {
			RiskUtil.openDocs("help/index_commands.htm");
			Testing.append("Manual Opened\n");
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(this,"Unable to open manual: "+e.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
		}

	}
	else {

	    //statusBar.setText("Working...");
	    //Submit.setEnabled(false);
	    //Command.setEnabled(false);

	    risk.parser(input);
	}

	// Console.setCaretPosition(Console.getDocument().getLength());

    }

    /**
     * This reads in a file for the commands
     * @throws FileNotFoundException The file cannot be found
     * @throws IOException
     */
    public void Commands() {

	String commands="";

	try {
	    FileReader filein = new FileReader("commands.txt");
	    BufferedReader bufferin = new BufferedReader(filein);
	    String input = bufferin.readLine();
	    while(input != null) {
		if (commands.equals("")) { commands = input; }
		else { commands = commands + "\n" + input; }
		input = bufferin.readLine();
	    }
	    bufferin.close();
            Testing.append("Commands Box opened\n");
	    JOptionPane.showMessageDialog(this, commands, "Commands:", JOptionPane.PLAIN_MESSAGE);
	}
	catch (FileNotFoundException e) {
	    Testing.append("Unable to find file commands.txt\n");
	}
	catch (IOException e) {
	    Testing.append("Unable to read file commands.txt\n");
	}

    }

    /**
     * This opens the about dialog box
     */
    public void openAbout() {

	RiskUIUtil.openAbout(Increment1Frame.this,product, version);

	Testing.append("About Box opened\n");

    }

class Increment1RiskAdapter extends RiskAdapter {

    /**
     * Checks if redrawing or repainting is needed 
     * @param output
     * @param redrawNeeded If frame needs to be redrawn
     * @param repaintNeeded If frame needs to be repainted 
     */
    public void sendMessage(String output, boolean redrawNeeded, boolean repaintNeeded) {

	Testing.append("Returned: \""+output+"\"\n");
	Console.append(output + System.getProperty("line.separator") );

	Console.setCaretPosition(Console.getDocument().getLength());

    }

    /**
     * checks if the the frame needs input 
     * @param s determines what needs input
     */
    public void needInput(int s) {

	Submit.setEnabled(true);
	Command.setEnabled(true);
	Command.requestFocus();
	statusBar.setText("Done... Ready");

    }

    /**
     * Blocks Input
     */
    public void noInput() {

	statusBar.setText("Working...");
	Submit.setEnabled(false);
	Command.setEnabled(false);

        Testing.append("Input Blocked\n");

    }

}

    /**
     * This runs the program
     * @param argv
     */
    public static void main(String[] argv) {

        RiskUIUtil.parseArgs(argv);

	Increment1Frame gui = new Increment1Frame( new Risk("sersom.map","risk.cards") );

        RiskUIUtil.center(gui);
        gui.setVisible(true);

    }

}
