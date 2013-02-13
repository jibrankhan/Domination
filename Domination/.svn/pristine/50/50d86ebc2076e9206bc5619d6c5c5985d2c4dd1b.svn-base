package net.yura.domination.mobile.simplegui;

import java.util.Vector;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import net.yura.domination.engine.Risk;
import net.yura.domination.engine.RiskAdapter;
import net.yura.domination.engine.RiskUtil;
import net.yura.domination.mobile.MiniUtil;
import net.yura.domination.mobile.PicturePanel;
import net.yura.mobile.gui.ActionListener;
import net.yura.mobile.gui.DesktopPane;
import net.yura.mobile.gui.KeyEvent;
import net.yura.mobile.gui.border.LineBorder;
import net.yura.mobile.gui.components.Button;
import net.yura.mobile.gui.components.Frame;
import net.yura.mobile.gui.components.Label;
import net.yura.mobile.gui.components.Menu;
import net.yura.mobile.gui.components.MenuBar;
import net.yura.mobile.gui.components.OptionPane;
import net.yura.mobile.gui.components.Panel;
import net.yura.mobile.gui.components.ScrollPane;
import net.yura.mobile.gui.components.TextArea;
import net.yura.mobile.gui.components.TextField;
import net.yura.mobile.gui.layout.BorderLayout;
import net.yura.mobile.gui.layout.GridBagConstraints;
import net.yura.mobile.gui.layout.GridBagLayout;

public class GameFrame extends Frame {

    private final static String version = "1.1.1.1";
    private final static String product = "Simple GUI for RISK";

    private TextArea Console;
    //private StyledDocument doc;
    private TextField Command;
    private Button Submit;
    private PicturePanel pp;

    private Label statusBar;
    private Label gameStatus;
    private ScrollPane Con;

    private Risk risk;
    private Vector history;
    private int pointer;
    private String temptext;

    private Panel guiMain;
    private Panel gp;
    private Label Pix;
    private GamePanel gamecontrol;

    /**
     * Creates a new RiskGUI
     * @param r The Risk object for this GUI
     */
    public GameFrame(Risk r) {

            risk=r;

            gameStatus = new Label("");
            gameStatus.setPreferredSize(200, -1);

            history = new Vector();
            pointer=-1;

            Console = new TextArea("AAAAAAAA");

            Submit = new Button();

            Pix = new Label("Hello!");


            pp = new PicturePanel(risk);
            gp = new Panel(new BorderLayout());

            gamecontrol = new GamePanel(risk,pp);

            gp.add(gamecontrol, Graphics.TOP );
            gp.add(pp);

            statusBar = new Label("Loading...");

            Con = new ScrollPane(Console);

            //setResizable(false);

            statusBar.setText("Ready");


            Command = new TextField() {


                public boolean processKeyEvent(KeyEvent key) {

                    if (key.isDownAction(Canvas.UP) || key.isDownAction(Canvas.DOWN) || key.justReleasedAction(Canvas.UP) || key.justReleasedAction(Canvas.DOWN)) {
                        if (key.justReleasedAction(Canvas.UP)) {

                                if (pointer < 0) {
                                        //Toolkit.getDefaultToolkit().beep();
                                }
                                else {
                                        if (pointer == history.size()-1) { temptext=Command.getText(); }
                                        Command.setText( (String)history.elementAt(pointer) );
                                        pointer--;
                                }
                        }
                        else if(key.justReleasedAction(Canvas.DOWN)) {

                                if (pointer > history.size()-2 ) {
                                        //Toolkit.getDefaultToolkit().beep();
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
                        return true;
                    }
                    else {
                            pointer = history.size()-1;
                    }

                    return super.processKeyEvent(key);
                }


            };

            initGUI();

          //this get all the commands from the game and does what needs to be done
            RiskAdapter SimpleRiskAdapter = new RiskAdapter() {

                    /**
                     * Checks if redrawing or repainting is needed
                     * @param output
                     * @param redrawNeeded If frame needs to be redrawn
                     * @param repaintNeeded If frame needs to be repainted
                     */
                    public void sendMessage(final String output,final boolean redrawNeeded,final boolean repaintNeeded) {
                            DesktopPane.invokeLater( new Runnable() {
                                @Override
                                public void run() {
                                    Console.append(output+"\n");

                                    if (Console.getHeight() > 10000) {
                                        Console.setText("");
                                    }

                                    if (redrawNeeded) {
                                            pp.repaintCountries( gamecontrol.getMapView() );
                                    }
                                    if (repaintNeeded) {
                                            repaint();
                                    }
                                }
                            });
                    }

                    /**
                     * checks if the the frame needs input
                     * @param s determines what needs input
                     */
                    public void needInput(int s) {

                            Submit.setFocusable(true);
                            Command.setFocusable(true);
                            Command.requestFocusInWindow();
                            statusBar.setText("Done... Ready");

                    }

                    /**
                     * Blocks Input
                     */
                    public void noInput() {

                            statusBar.setText("Working...");
                            Submit.setFocusable(false);
                            Command.setFocusable(false);

                    }

                    /**
                     * Displays a message
                     */
                    public void setGameStatus(String state) {

                            gameStatus.setText(state);
                            gameStatus.repaint();

                    }

                    /**
                     * Starts the game
                     * @param s If the game is a local game
                     */
                    public void startGame(boolean s) {

                            try {
                                    pp.load();
                            }
                            catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            gamecontrol.resetMapView();

                            guiMain.remove(Pix);
                            guiMain.add(gp);

                            guiMain.revalidate();
                            guiMain.repaint();
                    }

                    /**
                     * Closes the game
                     */
                    public void closeGame() {

                            guiMain.remove(gp);
                            guiMain.add(Pix);

                            guiMain.revalidate();
                            guiMain.repaint();
                    }

            };

            risk.addRiskListener( SimpleRiskAdapter );

    }

    /** This method is called from within the constructor to initialize the form. */

    /**
     * Initialises the GUI
     */
    private void initGUI() {

            // set title
            setTitle(product);
            //setIconImage(Toolkit.getDefaultToolkit().getImage( AboutDialog.class.getResource("icon.gif") ));

            getContentPane().setLayout( new GridBagLayout(2,3,3,3,3,3) );

            pp.setPreferredSize(PicturePanel.PP_X,PicturePanel.PP_Y);

            pp.setBorder(new LineBorder(0xFF000000,1));

            //pp.addMouseListener(this);
            //pp.addMouseMotionListener(this);

            Console.setText("");
            // Console.setBackground(Color.white); // not needed with swing
            Console.setFocusable(false);

            //Con.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            Con.setPreferredSize(200,100);

            Command.setPreferredSize(200,20);

            Submit.setText("Submit");

            guiMain = new Panel();

            //guiMain.setPreferredSize(PicturePanel.PP_X, PicturePanel.PP_Y+30);
            guiMain.setPreferredSize(200,200);

            guiMain.setLayout(new BorderLayout());

            guiMain.add(Pix);

            GridBagConstraints c = new GridBagConstraints();

            c.colSpan = 2; // width
            c.rowSpan = 2; // height
            c.weightx = 1;
            c.weighty = 1;

            getContentPane().add(guiMain, c); // Pix

            //c.fill = GridBagConstraints.BOTH;

            c = new GridBagConstraints();
            c.colSpan = 2; // width
            c.rowSpan = 1; // height
            getContentPane().add(Con, c);

            c = new GridBagConstraints();
            c.colSpan = 1; // width
            c.rowSpan = 1; // height
            c.weightx = 1;
            getContentPane().add(Command, c);

            c = new GridBagConstraints();
            c.colSpan = 1; // width
            c.rowSpan = 1; // height
            getContentPane().add(Submit, c);

            c = new GridBagConstraints();
            c.colSpan = 1; // width
            c.rowSpan = 1; // height
            // add status bar
            getContentPane().add(gameStatus, c);

            c = new GridBagConstraints();
            c.colSpan = 1; // width
            c.rowSpan = 1; // height
            // add status bar
            getContentPane().add(statusBar, c);

            ActionListener readCommand = new ActionListener() {
                    public void actionPerformed(String a) {

                            String input = Command.getText();
                            Command.setText("");

                            history.add(input);
                            pointer = history.size()-1;
                            go(input);

                    }
            };

            Submit.addActionListener( readCommand );
            Command.addActionListener( readCommand );


            //Command.addKeyListener( new CommandKeyAdapter(this) );


            // add menu bar
            MenuBar menuBar = new MenuBar();
            Menu menuFile = new Menu("File");
            menuFile.setMnemonic('F');

            // create About menu item
            Menu menuHelp = new Menu("Help");
            menuHelp.setMnemonic('H');

            Button Commands = new Button("Commands");
            Commands.setMnemonic('C');
            Commands.addActionListener(
                            new ActionListener() {
                                    public void actionPerformed(String e) {
                                            Commands();
                                    }
                            });
            menuHelp.add(Commands);

            Button helpMan = new Button("Manual");
            helpMan.setMnemonic('M');
            helpMan.addActionListener(
                            new ActionListener() {
                                    public void actionPerformed(String e) {
                                            go("manual");
                                    }
                            });
            menuHelp.add(helpMan);

            Button helpAbout = new Button("About");
            helpAbout.setMnemonic('A');
            helpAbout.addActionListener(
                            new ActionListener() {
                                    public void actionPerformed(String e) {
                                            openAbout();
                                    }
                            });
            menuHelp.add(helpAbout);

            // create Clear menu item
            Menu menuClear = new Menu("Clear");
            menuClear.setMnemonic('C');

            Button ClearConsole = new Button("Clear Console");
            ClearConsole.setMnemonic('C');
            ClearConsole.addActionListener(
                            new ActionListener() {
                                    public void actionPerformed(String e) {
                                            Console.setText("");
                                    }
                            });
            menuClear.add(ClearConsole);

            Button ClearHistory = new Button("Clear History");
            ClearHistory.setMnemonic('H');
            ClearHistory.addActionListener(
                            new ActionListener() {
                                    public void actionPerformed(String e) {
                                            history.clear();
                                            pointer = -1;
                                    }
                            });
            menuClear.add(ClearHistory);

            // create Open menu item

            Button openFile = new Button("Run Script");
            openFile.setMnemonic('R');
            openFile.addActionListener(
                            new ActionListener() {
                                    public void actionPerformed(String e) {
/*
                                            final JFileChooser fc = new JFileChooser();
                                            RiskFileFilter filter = new RiskFileFilter(RiskFileFilter.RISK_SCRIPT_FILES);
                                            fc.setFileFilter(filter);

                                            int returnVal = fc.showDialog(RiskGUI.this, "Run");
                                            if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
                                                    java.io.File file = fc.getSelectedFile();
                                                    // Write your code here what to do with selected file

                                                    try {

                                                            FileReader filein = new FileReader(file);
                                                            BufferedReader bufferin = new BufferedReader(filein);

                                                            String input = bufferin.readLine();
                                                            while(input != null) {

                                                                    go(input);
                                                                    input = bufferin.readLine();

                                                            }
                                                            bufferin.close();

                                                    }
                                                    catch(Exception error) {

                                                    }

                                            } else {
                                                    // Write your code here what to do if user has canceled Open dialog
                                            }
*/
                                    }
                            });
            menuFile.add(openFile);

            // create Save menu item
            Button saveFile = new Button("Save Console");
            saveFile.setMnemonic('S');
            saveFile.addActionListener(
                            new ActionListener() {
                                    public void actionPerformed(String e) {
/*
                                            final JFileChooser fc = new JFileChooser();
                                            RiskFileFilter filter = new RiskFileFilter(RiskFileFilter.RISK_LOG_FILES);
                                            fc.setFileFilter(filter);

                                            int returnVal = fc.showSaveDialog(RiskGUI.this);
                                            if (returnVal == JFileChooser.APPROVE_OPTION) {
                                                    java.io.File file = fc.getSelectedFile();
                                                    // Write your code here what to do with selected file

                                                    String fileName = file.getAbsolutePath();

                                                    if (!(fileName.endsWith( "." + RiskFileFilter.RISK_LOG_FILES ))) {
                                                            fileName = fileName + "." + RiskFileFilter.RISK_LOG_FILES;
                                                    }

                                                    try {

                                                            FileWriter fileout = new FileWriter(fileName);
                                                            BufferedWriter buffer = new BufferedWriter(fileout);
                                                            PrintWriter printer = new PrintWriter(buffer);

                                                            printer.write( doc.getText(0, doc.getLength() ) );

                                                            printer.close();

                                                    }

                                                    catch(Exception error) {

                                                    }

                                            } else {
                                                    // Write your code here what to do if user has canceled Save dialog
                                            }
*/
                                    }
                            });
            menuFile.add(saveFile);

            // create Exit menu item
            Button fileExit = new Button("Exit");
            fileExit.setMnemonic('E');
            fileExit.addActionListener(
                            new ActionListener() {
                                    public void actionPerformed(String e) {
                                            System.exit(0);
                                    }
                            });
            menuFile.add(fileExit);

            menuBar.add(menuFile);
            menuBar.add(menuClear);
            menuBar.add(menuHelp);

            // sets menu bar
            setMenuBar(menuBar);

            /*
            setBounds(new java.awt.Rectangle(0,0,905,629));
            addWindowListener(
                            new java.awt.event.WindowAdapter() {
                                    public void windowClosing(java.awt.event.WindowEvent evt) {
                                            exitForm();
                                    }
                            });

            pack();
            */
    }

    /** Exit the Application */

    /**
     * Closes the GUI
     */
    private void exitForm() {

            System.exit(0);
    }

    /**
     * Submits input to parser if neccessary
     * @param input The string that is checked
     */
    public void go(String input) {

            if (input.equals("exit") ) {
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
            }
            else if (input.equals("manual") ) {
                    try {
                            RiskUtil.openDocs("help/index_commands.htm");
                    }
                    catch(Exception e) {
                            OptionPane.showMessageDialog(null,"Unable to open manual: "+e.getMessage(),"Error", OptionPane.ERROR_MESSAGE);
                    }

            }
            else {
                    //statusBar.setText("Working...");
                    //Submit.setEnabled(false);
                    //Command.setEnabled(false);
                    risk.parser(input);
            }

    }

    /**
     * This reads in a file for the commands
     */
    public void Commands() {
/*
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

                    JOptionPane.showMessageDialog(this, commands, "Commands:", JOptionPane.PLAIN_MESSAGE);
            }
            catch (FileNotFoundException e) {
                    //Testing.append("Unable to find file commands.txt\n");
            }
            catch (IOException e) {
                    //Testing.append("Unable to read file commands.txt\n");
            }
*/
    }

    /**
     * This opens the about dialog box
     */
    public void openAbout() {

            //RiskUIUtil.openAbout(RiskGUI.this,product, version);
        MiniUtil.showAbout();

    }


    //**********************************************************************
    //                     MouseListener Interface
    //**********************************************************************
/*
    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {

            if (pp.getHighLight() != 255) {
                    pp.setHighLight(255);
                    pp.repaint();
            }

    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {

            if ((e.getX() < PicturePanel.PP_X) && (e.getY() < PicturePanel.PP_Y) && (e.getX() >= 0) && (e.getY() >= 0) ) {

                    int pixColor = pp.getCountryNumber(e.getX(),e.getY());

                    if (pixColor != 255 ) {

                            String name = pixColor+""; // risk.getCountryInt( pixColor );
                            Command.setText( Command.getText() + " " + name );

                    }
            }

    }

    public void mouseDragged(MouseEvent e) {
    }
    public void mouseMoved(MouseEvent e) {

            int cc = pp.getCountryNumber(e.getX(),e.getY());

            if (pp.getHighLight() != cc) {
                    pp.setHighLight(cc);
                    pp.repaint();
            }

    }
    */
}
