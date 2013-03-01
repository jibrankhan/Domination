// Yura Mamyrin, Group D

package net.yura.domination.ui.swinggui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.SwingUtilities;
import net.yura.domination.engine.ColorUtil;
import net.yura.domination.engine.Risk;
import net.yura.domination.engine.RiskAdapter;
import net.yura.domination.engine.RiskUIUtil;
import net.yura.domination.engine.RiskUtil;
import net.yura.domination.engine.core.Continent;
import net.yura.domination.engine.core.Player;
import net.yura.domination.engine.core.RiskGame;
import net.yura.domination.engine.guishared.BadgeButton;
import net.yura.domination.engine.guishared.MapMouseListener;
import net.yura.domination.engine.guishared.PicturePanel;
import net.yura.domination.engine.guishared.RiskFileFilter;
import net.yura.domination.engine.guishared.StatsPanel;
import net.yura.domination.engine.translation.TranslationBundle;
import net.yura.domination.tools.mapeditor.MapEditor;

/**
 * <p> Swing GUI Main Frame </p>
 * @author Yura Mamyrin
 */
public class SwingGUIPanel extends JPanel implements ActionListener{

	public final static String version = "2";
	public final static String product;

	static {

		product = "Swing GUI for " + RiskUtil.GAME_NAME;

	}

	// TRUE GUI GLOBAL THINGS:

	private ResourceBundle resbundle = TranslationBundle.getBundle();

	private Risk myrisk;

	private int gameState;
	private boolean localGame;

	private JTabbedPane tabbedpane;
	private JToolBar currentToolbar;
	private JMenuBar gMenuBar;

	private GameTab gameTab;
	private ConsoleTab consoleTab;
	private StatisticsTab statisticsTab;
	private DebugTab debugTab;
	private MapEditor editorTab;







	// should not really be here


	private PicturePanel pp;
	//private int c1Id;


	private JLabel attacker;

	private JTextField country1;
	private JTextField country2;

	private JPanel inGameInput;
	private CardLayout inGameCards;

	// these are needed here as they need to be changed to display different amount of buttons
	private JPanel defend;
	private JPanel roll;
        private winnerPanel winner;
        private tradeCardsPanel tradeCards;

	private JSlider slider;
	private JSlider moveNumber;

	private JLabel armies;

	private JButton autoplace;




	private JLabel resultsLabel;


	private SetupPanel guiSetup;


	private JPanel gameOptions;

	private JButton roll1;
	private JButton roll2;
	private JButton roll3;

	private JComboBox mapViewComboBox;

	private JButton showMission;
	private JButton showCards;
	private JButton Undo;

	private JButton lobby;

	/**
	 * Creates a new SwingGUI
	 * @param r The Risk object for this GUI
	 */
	public SwingGUIPanel(Risk r) {

		myrisk= r;

		//c1Id = -1;
		gameState=-1; // (-1 means no game)



		pp = new PicturePanel(myrisk);




		setLayout(new java.awt.BorderLayout());

		// set the border of the window
		//setDefaultLookAndFeelDecorated(true);
		//setUndecorated(true);
		//getRootPane().setWindowDecorationStyle(JRootPane.FRAME);


		// add menu bar
		gMenuBar = new JMenuBar();

		tabbedpane = new JTabbedPane();

		gameTab = new GameTab();
		consoleTab = new ConsoleTab();
		statisticsTab = new StatisticsTab();
		
		editorTab = new MapEditor(myrisk,this);

		addTab(gameTab);
		addTab( new FX3DPanel(pp) );
                try {
                    addTab( new LobbyTab(myrisk) );
                }
                catch (Throwable th) {
                    RiskUtil.printStackTrace(th); // midletrunner.jar could be missing
                }
		addTab(consoleTab);
		addTab(statisticsTab);
                try {
                    debugTab = new DebugTab();
                    addTab(debugTab);
                }
                catch (Throwable th) {
                    RiskUtil.printStackTrace(th); // Grasshopper.jar could be missing
                }
		addTab( new TestPanel(myrisk,pp) );
		addTab(editorTab);
                try {
                    addTab( new TranslationToolPanel() );
                }
                catch (Throwable th) {
                    RiskUtil.printStackTrace(th); // TranslationTool.jar could be missing
                }
		addTab(new BugsPanel());


		add(tabbedpane, java.awt.BorderLayout.CENTER );


		ChangeListener changeMenu = new ChangeListener() {
			public void stateChanged(ChangeEvent e) {

				SwingGUITab sgt = (SwingGUITab)tabbedpane.getSelectedComponent();

				if (currentToolbar!=null) { remove(currentToolbar); }
				currentToolbar = sgt.getToolBar();

                                if (currentToolbar!=null) {
                                    currentToolbar.setOrientation( javax.swing.JToolBar.HORIZONTAL );
                                    add( currentToolbar, java.awt.BorderLayout.NORTH );
                                }

                                revalidate();
				repaint();
			}
		};






		tabbedpane.addChangeListener( changeMenu );

		//oddpanelbug.add(toolbarGUI, java.awt.BorderLayout.NORTH );
		changeMenu.stateChanged(null);






		// create Help menu item
		JMenu gHelp = new JMenu(resbundle.getString("swing.menu.help"));
		gHelp.setMnemonic('H');

		JMenuItem gmManual = new JMenuItem(resbundle.getString("swing.menu.manual"));
		gmManual.setMnemonic('M');
		gmManual.setActionCommand("manual");
		gmManual.addActionListener( this );
		gHelp.add(gmManual);

		JMenuItem cmCommands = new JMenuItem(resbundle.getString("swing.menu.console.commands"));
		cmCommands.setMnemonic('C');
		cmCommands.setActionCommand("commands");
		cmCommands.addActionListener( this );
		gHelp.add(cmCommands);

		JMenuItem gmAbout = new JMenuItem(resbundle.getString("swing.menu.about"));
		gmAbout.setMnemonic('A');
		gmAbout.setActionCommand("about");
		gmAbout.addActionListener( this );
		gHelp.add(gmAbout);

		gMenuBar.add(gHelp);




		//add(gMenuBar, java.awt.BorderLayout.NORTH );
		// sets menu bar
		//setJMenuBar(gMenuBar);
		//setBounds(new java.awt.Rectangle(0,0,905,629));


		// now gui is setup u can listen
		myrisk.addRiskListener( new SwingRiskAdapter() );

                if (debugTab!=null) {
                    debugTab.start();
                }
                
                net.yura.domination.engine.ai.AIPlayer.setWait(5);
	}

        public JMenuBar getJMenuBar() {
            return gMenuBar;
        }

	public void setupLobbyButton() {

		if (RiskUIUtil.getAddLobby(myrisk)) {

			lobby.setVisible(true);

		}

		revalidate();
		repaint();

	}

	public void actionPerformed(ActionEvent a) {

		if (a.getActionCommand().equals("manual")) {

			try {
				RiskUtil.openDocs( resbundle.getString("helpfiles.swing") );
			}
			catch(Exception e) {
				showError("Unable to open manual: "+e.getMessage() );
			}

		}
		else if (a.getActionCommand().equals("about")) {

			openAbout();

		}
		else if (a.getActionCommand().equals("quit")) {

			System.exit(0);

		}
		else if (a.getActionCommand().equals("commands")) {
			Commands();
		}
		else {

			System.out.print("command \""+a.getActionCommand()+"\" is not implemented yet\n");

		}


	}

	public void addTab(SwingGUITab a) {

		//tabbedpane.addTab(a.getName(),(Component)a);
		tabbedpane.add((Component)a);

		JMenu menu = a.getMenu();

		if (menu!=null) {

			gMenuBar.add(menu);

		}


	}

class ConsoleTab extends JPanel implements SwingGUITab, ActionListener {

	private String temptext;
	private Vector history;
	private int pointer;

	JMenu cConsole;
	JToolBar toolbarCon;

	private JTextArea Console;
	private JTextField Command;
	private JButton Submit;

	private JLabel statusBar;
	private JScrollPane Con;


	public ConsoleTab() {

		setName( resbundle.getString("swing.tab.console") );

		// ################### CONSOLE #######################

		history = new Vector();
		pointer=-1;

		statusBar = new JLabel(resbundle.getString("swing.status.loading"));
		Console = new JTextArea();
		Con = new JScrollPane(Console);

		Dimension conSize = new Dimension(PicturePanel.PP_X,PicturePanel.PP_Y+60);

		Con.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		Con.setPreferredSize(conSize);
		Con.setMinimumSize(conSize);

		// Console.setBackground(Color.white); // not needed with swing
		Console.setEditable(false);


		Command = new JTextField("");
		// Command.setColumns(75); // dont use because it goes odd in linux
		Dimension CommandSize = new Dimension(PicturePanel.PP_X-50 , 20);
		//c.ipadx = 600; // width
		//c.ipadx = 0; // width
		Command.setPreferredSize(CommandSize);
		Command.setMinimumSize(CommandSize);
		Command.setMaximumSize(CommandSize);

		Submit = new JButton(resbundle.getString("swing.button.submit"));


		Submit.setActionCommand("read command");
		Submit.addActionListener( this );
		Command.setActionCommand("read command");
		Command.addActionListener( this );




		// make tool bar

		toolbarCon = new JToolBar();
		toolbarCon.setRollover(true);
		toolbarCon.setFloatable(false);

		JButton cRunScript	= new JButton(resbundle.getString("swing.menu.console.runscript"));
		JButton cSaveConsole	= new JButton(resbundle.getString("swing.menu.console.save"));
		JButton cClearConsole	= new JButton(resbundle.getString("swing.menu.console.clear"));
		JButton cClearHistory	= new JButton(resbundle.getString("swing.menu.console.histclear"));

		JButton cCommands	= new JButton(resbundle.getString("swing.menu.console.commands"));
		JButton cManual		= new JButton(resbundle.getString("swing.menu.manual"));
		JButton cAbout		= new JButton(resbundle.getString("swing.menu.about"));
		JButton cQuit		= new JButton(resbundle.getString("swing.menu.quit"));

		cRunScript.setActionCommand("run script");
		cRunScript.addActionListener( this );
		cSaveConsole.setActionCommand("save console");
		cSaveConsole.addActionListener( this );
		cClearConsole.setActionCommand("clear console");
		cClearConsole.addActionListener( this );
		cClearHistory.setActionCommand("clear history");
		cClearHistory.addActionListener( this );

		cCommands.setActionCommand("commands");
		cCommands.addActionListener( this );
		cManual.setActionCommand("manual");
		cManual.addActionListener( this );
		cAbout.setActionCommand("about");
		cAbout.addActionListener( this );
		cQuit.setActionCommand("quit");
		cQuit.addActionListener( this );

		toolbarCon.add(cRunScript);
		toolbarCon.add(cSaveConsole);
		toolbarCon.add(cClearConsole);
		toolbarCon.add(cClearHistory);
		toolbarCon.addSeparator();
		toolbarCon.add(cCommands);
		toolbarCon.add(cManual);
		toolbarCon.add(cAbout);
		if (RiskUIUtil.checkForNoSandbox()) { toolbarCon.add(cQuit); }









		// create Console menu item
		cConsole = new JMenu(resbundle.getString("swing.menu.console"));
		cConsole.setMnemonic('C');

		JMenuItem cmRunScript = new JMenuItem(resbundle.getString("swing.menu.console.runscript"));
		cmRunScript.setMnemonic('R');
		cmRunScript.setActionCommand("run script");
		cmRunScript.addActionListener( this );
		cConsole.add(cmRunScript);

		JMenuItem cmSaveConsole = new JMenuItem(resbundle.getString("swing.menu.console.save"));
		cmSaveConsole.setMnemonic('S');
		cmSaveConsole.setActionCommand("save console");
		cmSaveConsole.addActionListener( this );
		cConsole.add(cmSaveConsole);

		JMenuItem cmClearConsole = new JMenuItem(resbundle.getString("swing.menu.console.clear"));
		cmClearConsole.setMnemonic('C');
		cmClearConsole.setActionCommand("clear console");
		cmClearConsole.addActionListener( this );
		cConsole.add(cmClearConsole);

		JMenuItem cmClearHistory = new JMenuItem(resbundle.getString("swing.menu.console.histclear"));
		cmClearHistory.setMnemonic('H');
		cmClearHistory.setActionCommand("clear history");
		cmClearHistory.addActionListener( this );
		cConsole.add(cmClearHistory);





		GridBagConstraints c = new GridBagConstraints();
		c.insets = new java.awt.Insets(3, 3, 3, 3);
		c.fill = GridBagConstraints.BOTH;


		setLayout(new java.awt.GridBagLayout());

		c.gridx = 0; // col
		c.gridy = 0; // row
		c.gridwidth = 2; // width
		c.gridheight = 1; // height
		add(Con, c);

		c.gridx = 0; // col
		c.gridy = 1; // row
		c.gridwidth = 1; // width
		c.gridheight = 1; // height
		add(Command, c);

		c.gridx = 1; // col
		c.gridy = 1; // row
		c.gridwidth = 1; // width
		c.gridheight = 1; // height
		add(Submit, c);

		c.gridx = 0; // col
		c.gridy = 2; // row
		c.gridwidth = 2; // width
		c.gridheight = 1; // height
		add(statusBar, c);




		Command.addKeyListener( new KeyAdapter() {

			public void keyPressed(KeyEvent key) {


				if (key.getKeyCode() == 38) {
					// Testing.append("up key (history)\n");

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
					// Testing.append("down key (history)\n");


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
		});

		setOpaque(false);


		statusBar.setText(resbundle.getString( "swing.status.ready"));

	}

	private void cgo(String input) {

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
			// Testing.append("Console cleared\n");
			Console.setText("");
		}
		else if (input.equals("manual") ) {

			try {
				RiskUtil.openDocs( resbundle.getString("helpfiles.swing") );
			}
			catch(Exception e) {
				addOutput("Unable to open manual: "+e.getMessage() );
			}

		}
		else {

			go(input);

		}

	}

	public void addOutput(String output) {

		Console.append(output + System.getProperty("line.separator") );

		Console.setCaretPosition(Console.getDocument().getLength());

	}

	public void blockInput() {

		statusBar.setText(resbundle.getString("swing.status.working"));
		Submit.setEnabled(false);
		Command.setEnabled(false);

	}

	public void getInput() {

		Submit.setEnabled(true);
		Command.setEnabled(true);
		Command.requestFocus();
		statusBar.setText(resbundle.getString("swing.status.doneready"));

	}

	public void setVisible(boolean v) {

		super.setVisible(v);

		Command.requestFocus(); // does not work too well

	}


	public JToolBar getToolBar() {

		return toolbarCon;

	}
	public JMenu getMenu() {

		return cConsole;

	}

	public void actionPerformed(ActionEvent a) {

		if (a.getActionCommand().equals("read command")) {

			String input = Command.getText();
			Command.setText("");

			history.add(input);
			pointer = history.size()-1;
			cgo(input);

		}
		else if (a.getActionCommand().equals("run script")) {

			JFileChooser fc = new JFileChooser();
			RiskFileFilter filter = new RiskFileFilter(RiskFileFilter.RISK_SCRIPT_FILES);
			fc.setFileFilter(filter);

			int returnVal = fc.showOpenDialog( RiskUIUtil.findParentFrame(this) );
			if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
				java.io.File file = fc.getSelectedFile();
				// Write your code here what to do with selected file

				try {
					// Testing.append("Opening file: "+ file.getPath() +"\n");
					// Testing.append("Running Script...\n");

					FileReader filein = new FileReader(file);
					BufferedReader bufferin = new BufferedReader(filein);

					String input = bufferin.readLine();
					while(input != null) {

						go(input);
						input = bufferin.readLine();

					}
					bufferin.close();
					// Testing.append("Script end\n");

				}
				catch(Exception error) {
					// Testing.append("Error: "+error.getMessage() + "\n");
				}

			} else {
				// Write your code here what to do if user has canceled Open dialog
			}

		}
		else if (a.getActionCommand().equals("save console")) {

			saveLog(Console);

		}
		else if (a.getActionCommand().equals("clear console")) {

			Console.setText("");

		}
		else if (a.getActionCommand().equals("clear history")) {

			history.clear();
			pointer = -1;

		}
		else {

			SwingGUIPanel.this.actionPerformed(a);

		}
	}
}


class GameTab extends JPanel implements SwingGUITab, ActionListener {

	private JPanel Pix;
	private JPanel guiGame;

	private JLabel gameStatus;
	private JMenu gGame;
	private JToolBar toolbarGUI;

	private boolean serverOn;

	private JButton gNewGame;
	private JButton gLoadGame;
	private JButton gSaveGame;
	private JButton gCloseGame;

	private JButton gJoinGame;
	private JButton gStartServer;

	private JButton gOptions;
	private JMenuItem gmOptions;
	private JMenuItem gmReplay;

	private JMenuItem gmStartServer;
	private JMenuItem gmJoinGame;

	private JMenuItem gmNewGame;
	private JMenuItem gmLoadGame;
	private JMenuItem gmSaveGame;
	private JMenuItem gmCloseGame;

	public JMenu getMenu() {

		return gGame;
	}

	public JToolBar getToolBar() {

		return toolbarGUI;

	}

	public GameTab() {


		setName( resbundle.getString("swing.tab.game") );


		serverOn=false;


		// ################### GUI #######################



		gameStatus = new JLabel("");

		Pix = new JPanel( new BorderLayout() );
		Pix.setOpaque(false);

		lobby = new JButton( resbundle.getString("lobby.run") );
		lobby.setActionCommand("lobby");
		lobby.addActionListener( this );
		lobby.setBackground( Color.RED );
		lobby.setVisible(false);

		// cant use this as it goes even worse in xp
		//lobby.setContentAreaFilled(false);

		JLabel pixlogo = new JLabel("yura.net "+product+", "+RiskUtil.GAME_NAME+" IDE", new javax.swing.ImageIcon(  this.getClass().getResource("about.jpg") ) , JLabel.CENTER );
		pixlogo.setHorizontalTextPosition( JLabel.CENTER );
		pixlogo.setVerticalTextPosition( JLabel.TOP );

		Pix.add( lobby , BorderLayout.NORTH );
		Pix.add( pixlogo );


                JButton donate = new JButton();
		donate.addActionListener( this );
		donate.setActionCommand("donate");
                donate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                URL donateNow = this.getClass().getResource("donateNow.gif");
                if (donateNow!=null) {
                     donate.setIcon( new ImageIcon( donateNow ) );
                }
                else {
                    donate.setText("Donate");
                }
                Pix.add(donate, BorderLayout.SOUTH );
                

		setLayout(new java.awt.BorderLayout());

		add(Pix, java.awt.BorderLayout.CENTER );

		add(gameStatus, java.awt.BorderLayout.SOUTH);

		gameStatus.setBorder( BorderFactory.createLoweredBevelBorder() );

		Insets margin = new Insets(2,2,2,2);


		showMission = new JButton(resbundle.getString("swing.button.mission"));
		showMission.setToolTipText(resbundle.getString("game.button.mission"));
		showMission.addActionListener( this );
		showMission.setActionCommand("showmission");
		showMission.setMargin(margin);

		showCards = new JButton(resbundle.getString("swing.button.cards"));
		showCards.setToolTipText(resbundle.getString("game.button.cards"));
		showCards.addActionListener( this );
		showCards.setActionCommand("showcards");
		showCards.setMargin(margin);

		Undo = new JButton(resbundle.getString("swing.button.undo"));
		Undo.setToolTipText(resbundle.getString("game.button.undo"));
		Undo.addActionListener( this );
		Undo.setActionCommand("undo");
		Undo.setMargin(margin);

		JButton viewContinents = new JButton(resbundle.getString("swing.button.continents"));
		viewContinents.addActionListener( this );
		viewContinents.setActionCommand("continents");
		viewContinents.setMargin(margin);

		moveNumber = new JSlider();
		slider = new JSlider();

		armies = new JLabel();

		toolbarGUI = new JToolBar();
		toolbarGUI.setRollover(true);



		autoplace			= new JButton(resbundle.getString("game.button.go.autoplace"));


			Dimension gameOptionsSize = new Dimension(PicturePanel.PP_X,25);

			gameOptions = makeGameOptionsPanel();
			gameOptions.setPreferredSize(gameOptionsSize);
			gameOptions.setMinimumSize(gameOptionsSize);
			gameOptions.setMaximumSize(gameOptionsSize);


			gameOptions.add(viewContinents);


		guiSetup = new SetupPanel();
		guiGame = new GamePanel();

		guiSetup.setOpaque(false);
		guiGame.setOpaque(false);





		// make toolbar

		gNewGame		= new JButton(resbundle.getString("swing.menu.new"));
		gLoadGame		= new JButton(resbundle.getString("swing.menu.load"));
		gSaveGame		= new JButton(resbundle.getString("swing.menu.save"));
		gCloseGame		= new JButton(resbundle.getString("swing.menu.close"));

		gOptions		= new JButton(resbundle.getString("swing.menu.options"));

		gJoinGame		= new JButton(resbundle.getString("swing.menu.joingame"));
		gStartServer		= new JButton(resbundle.getString("swing.menu.startserver"));

		JButton gManual		= new JButton(resbundle.getString("swing.menu.manual"));
		JButton gAbout		= new JButton(resbundle.getString("swing.menu.about"));
		JButton gQuit		= new JButton(resbundle.getString("swing.menu.quit"));

		gNewGame.setActionCommand("new game");
		gNewGame.addActionListener( this );
		gLoadGame.setActionCommand("load game");
		gLoadGame.addActionListener( this );
		gSaveGame.setActionCommand("save game");
		gSaveGame.addActionListener( this );
		gCloseGame.setActionCommand("close game");
		gCloseGame.addActionListener( this );
		gOptions.setActionCommand("options");
		gOptions.addActionListener( this );
		gManual.setActionCommand("manual");
		gManual.addActionListener( this );
		gAbout.setActionCommand("about");
		gAbout.addActionListener( this );
		gQuit.setActionCommand("quit");
		gQuit.addActionListener( this );

		gStartServer.setActionCommand("start server");
		gStartServer.addActionListener( this );
		gJoinGame.setActionCommand("join game");
		gJoinGame.addActionListener( this );



		toolbarGUI.add(gNewGame);
		toolbarGUI.add(gLoadGame);
		toolbarGUI.add(gSaveGame);
		toolbarGUI.add(gCloseGame);
		toolbarGUI.addSeparator();
		toolbarGUI.add(gJoinGame);
		toolbarGUI.add(gStartServer);
		toolbarGUI.addSeparator();
		toolbarGUI.add(gOptions);
		toolbarGUI.add(gManual);
		toolbarGUI.add(gAbout);
		if (RiskUIUtil.checkForNoSandbox()) { toolbarGUI.add(gQuit); }

		toolbarGUI.setFloatable(false);






		// create Game menu item
		gGame = new JMenu(resbundle.getString("swing.menu.game"));
		gGame.setMnemonic('G');

		gmNewGame = new JMenuItem(resbundle.getString("swing.menu.new"));
		gmNewGame.setMnemonic('N');
		gmNewGame.setActionCommand("new game");
		gmNewGame.addActionListener( this );
		gGame.add(gmNewGame);

		gmLoadGame = new JMenuItem(resbundle.getString("swing.menu.load"));
		gmLoadGame.setMnemonic('L');
		gmLoadGame.setActionCommand("load game");
		gmLoadGame.addActionListener( this );
		gGame.add(gmLoadGame);

		gmSaveGame = new JMenuItem(resbundle.getString("swing.menu.save"));
		gmSaveGame.setMnemonic('S');
		gmSaveGame.setActionCommand("save game");
		gmSaveGame.addActionListener( this );
		gGame.add(gmSaveGame);

		gmCloseGame = new JMenuItem(resbundle.getString("swing.menu.close"));
		gmCloseGame.setMnemonic('C');
		gmCloseGame.setActionCommand("close game");
		gmCloseGame.addActionListener( this );
		gGame.add(gmCloseGame);

		gGame.addSeparator();

		gmJoinGame = new JMenuItem(resbundle.getString("swing.menu.joingame"));
		gmJoinGame.setMnemonic('J');
		gmJoinGame.setActionCommand("join game");
		gmJoinGame.addActionListener( this );
		gGame.add(gmJoinGame);

		gmStartServer = new JMenuItem(resbundle.getString("swing.menu.startserver"));
		gmStartServer.setMnemonic('V');
		gmStartServer.setActionCommand("start server");
		gmStartServer.addActionListener( this );
		gGame.add(gmStartServer);

		gGame.addSeparator();

		gmOptions = new JMenuItem(resbundle.getString("swing.menu.options"));
		gmOptions.setMnemonic('O');
		gmOptions.setActionCommand("options");
		gmOptions.addActionListener( this );
		gGame.add(gmOptions);

		gmReplay = new JMenuItem("Replay");
		gmReplay.setMnemonic('R');
		gmReplay.setActionCommand("replay");
		gmReplay.addActionListener( this );
		gGame.add(gmReplay);

		if (RiskUIUtil.checkForNoSandbox()) {

			gGame.addSeparator();

			JMenuItem gmQuit = new JMenuItem(resbundle.getString("swing.menu.quit"));
			gmQuit.setMnemonic('Q');
			gmQuit.setActionCommand("quit");
			gmQuit.addActionListener( this );
			gGame.add(gmQuit);

		}





		roll1 = new JButton(resbundle.getString("swing.dice.roll1"));
		roll2 = new JButton(resbundle.getString("swing.dice.roll2"));
		roll3 = new JButton(resbundle.getString("swing.dice.roll3"));

		roll1.setActionCommand("roll 1");
		roll2.setActionCommand("roll 2");
		roll3.setActionCommand("roll 3");

		roll1.addActionListener(this);
		roll2.addActionListener(this);
		roll3.addActionListener(this);


		gOptions.setEnabled(false);
		gmOptions.setEnabled(false);
		gmReplay.setEnabled(false);

		gSaveGame.setEnabled(false);
		gCloseGame.setEnabled(false);

		gmSaveGame.setEnabled(false);
		gmCloseGame.setEnabled(false);

		setOpaque(false);


	}

	public void actionPerformed(ActionEvent a) {

                String actionCommand = a.getActionCommand();
            
		if ("showmission".equals(actionCommand)) {

			showMission( myrisk.getCurrentMission() );

		}
		else if ("showcards".equals(actionCommand)) {

			openCards();

		}
		else if ("undo".equals(actionCommand)) {

                        pp.setC1(PicturePanel.NO_COUNTRY);
                        pp.setC2(PicturePanel.NO_COUNTRY);

			go("undo");


		}
		else if ("continents".equals(actionCommand)) {

			StringBuffer buffer = new StringBuffer();
			buffer.append("<html><table>");

			Continent[] continents = myrisk.getGame().getContinents();

			for (int c=0;c<continents.length;c++) {

				Continent continent = continents[c];

				buffer.append("<tr style=\"background-color: ");
				buffer.append(ColorUtil.getHexForColor(continent.getColor()));
				buffer.append("; color:");
				buffer.append(ColorUtil.getHexForColor(ColorUtil.getTextColorFor(continent.getColor())));
				buffer.append("\"><td>");
				buffer.append(continent.getName());
				buffer.append("</td><td> - </td><td>");
				buffer.append(continent.getArmyValue());
				buffer.append("</td></tr>");

			}

			buffer.append("</table></html>");

			JOptionPane.showMessageDialog(this, buffer.toString(), resbundle.getString("swing.button.continents"), JOptionPane.PLAIN_MESSAGE );

		}
		else if ("roll 1".equals(actionCommand)) {

			go("roll 1");

		}
		else if ("roll 2".equals(actionCommand)) {

			go("roll 2");

		}
		else if ("roll 3".equals(actionCommand)) {

			go("roll 3");

		}
		else if ("join game".equals(actionCommand)) {

			String result = JOptionPane.showInputDialog(RiskUIUtil.findParentFrame(this), "type the server name", myrisk.getRiskConfig("default.host") );
			if (result!=null) { go("join "+result); }

		}
		else if ("new game".equals(actionCommand)) {

			SwingGUIPanel.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			go("newgame");

		}
		else if ("load game".equals(actionCommand)) {

			String name = RiskUIUtil.getLoadFileName(
				RiskUIUtil.findParentFrame(this)
				//RiskUtil.SAVES_DIR,
				//RiskFileFilter.RISK_SAVE_FILES
			);

			if (name!=null) {

				go("loadgame " + name );

			}

		}
		else if ("save game".equals(actionCommand)) {

			String name = RiskUIUtil.getSaveFileName(
				RiskUIUtil.findParentFrame(this)
				//RiskUtil.SAVES_DIR,
				//RiskFileFilter.RISK_SAVE_FILES
			);

			if (name!=null) {

				go("savegame " + name );

			}

		}
		else if ("close game".equals(actionCommand)) {

			//if (localGame) {
				go("closegame");
			//}
			//else {
			//	go("leave");
			//}
		}
		else if ("options".equals(actionCommand)) {

			Object[] message = new Object[2];
			message[0] = new JCheckBox("Auto End Go");
			message[1] = new JCheckBox("Auto Defend");


			((JCheckBox)message[0]).setSelected( myrisk.getAutoEndGo() );
			((JCheckBox)message[1]).setSelected( myrisk.getAutoDefend() );

			String[] options = {
			    "OK", 
			    "cancel"
			}; 

			int result = JOptionPane.showOptionDialog( 
			    this,                             // the parent that the dialog blocks 
			    message,                                    // the dialog message array 
			    "Options", // the title of the dialog window 
			    JOptionPane.OK_CANCEL_OPTION,                 // option type 
			    JOptionPane.PLAIN_MESSAGE,            // message type 
			    null,                                       // optional icon, use null to use the default icon 
			    options,                                    // options string array, will be made into buttons 
			    options[0]                                  // option that should be made into a default button 
			);

			if (result == JOptionPane.OK_OPTION ) {

                                myrisk.parser( "autodefend " + ((((JCheckBox)message[1]).isSelected()) ? "on" : "off") );
                                // "autoendgo on" may trigger the end of my go
				myrisk.parser( "autoendgo " + ((((JCheckBox)message[0]).isSelected()) ? "on" : "off") );

			}
/*

			Frame frame = Risk.findParentFrame(this);

			OptionsDialog optionsDialog = new OptionsDialog( frame , true, myrisk);
			Dimension frameSize = frame.getSize();
			Dimension optionsSize = optionsDialog.getPreferredSize();
			int x = frame.getLocation().x + (frameSize.width - optionsSize.width) / 2;
			int y = frame.getLocation().y + (frameSize.height - optionsSize.height) / 2;
			if (x < 0) x = 0;
			if (y < 0) y = 0;
			optionsDialog.setLocation(x, y);
			optionsDialog.setVisible(true);
*/
		}
		else if ("replay".equals(actionCommand)) {

			go("replay");

		}
		else if ("start server".equals(actionCommand)) {

			if (serverOn) {
				go("killserver");
			}
			else {
				go("startserver");
			}


		}
		else if ("lobby".equals(actionCommand)) {

			RiskUIUtil.runLobby(myrisk);

		}
                else if ("donate".equals(actionCommand)) {
                    
                        RiskUIUtil.donate(this);
                    
                }
		else {

			SwingGUIPanel.this.actionPerformed(a);

		}

	}

	public void blockInput() {

		gSaveGame.setEnabled(false);
		gmSaveGame.setEnabled(false);

		showMission.setEnabled(false);
		showCards.setEnabled(false);
		Undo.setEnabled(false);

		gOptions.setEnabled(false);
		gmOptions.setEnabled(false);
		gmReplay.setEnabled(false);

		// this is so close is not selected
		mapViewComboBox.grabFocus();

	}

	public void getInput() {

		if (localGame) {
			gSaveGame.setEnabled(true);
			gmSaveGame.setEnabled(true);
                        if (myrisk.getGame().getState()!=RiskGame.STATE_DEFEND_YOURSELF) {
                            Undo.setEnabled(true);
                        }
			gmReplay.setEnabled(true);
		}

		showMission.setEnabled(true);
		showCards.setEnabled(true);

		gOptions.setEnabled(true);
		gmOptions.setEnabled(true);

	}

	public void newGame() {

            // if we do not use this here we get ClassCastException in java 1.7
            SwingUtilities.invokeLater( new Runnable() {
                public void run() {
            
			gNewGame.setEnabled(false);
			gLoadGame.setEnabled(false);
			//gSaveGame.setEnabled(true);
			gCloseGame.setEnabled(true);

			gmNewGame.setEnabled(false);
			gmLoadGame.setEnabled(false);
			gmStartServer.setEnabled(false);
			gmJoinGame.setEnabled(false);

			gStartServer.setEnabled(false);
			gJoinGame.setEnabled(false);

			//gmSaveGame.setEnabled(true);
			gmCloseGame.setEnabled(true);

			remove(Pix);
			remove(guiGame);

			guiSetup.setupGame();

			add(guiSetup, java.awt.BorderLayout.CENTER );

			SwingGUIPanel.this.setCursor(null); // Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
                }
            } );

	}

	public void closeGame() {


			gNewGame.setEnabled(true);
			gLoadGame.setEnabled(true);
			gSaveGame.setEnabled(false);
			gCloseGame.setEnabled(false);

			gmNewGame.setEnabled(true);
			gmLoadGame.setEnabled(true);
			gmStartServer.setEnabled(true);
			gmJoinGame.setEnabled(true);

			gStartServer.setEnabled(true);
			gJoinGame.setEnabled(true);

			gmSaveGame.setEnabled(false);
			gmCloseGame.setEnabled(false);

			gOptions.setEnabled(false);
			gmOptions.setEnabled(false);
			gmReplay.setEnabled(false);

			remove(guiGame);
			remove(guiSetup);

			add(Pix, java.awt.BorderLayout.CENTER );

                        pp.stopAni(); // stop anmations
	}

	public void startGame() {

            // if we do not use this here we get ClassCastException in java 1.7 when we load a saved game
            SwingUtilities.invokeLater( new Runnable() {
                public void run() {
            
			gNewGame.setEnabled(false);
			gLoadGame.setEnabled(false);
			//gSaveGame.setEnabled(true);
			gCloseGame.setEnabled(true);

			gmNewGame.setEnabled(false);
			gmLoadGame.setEnabled(false);
			gmStartServer.setEnabled(false);
			gmJoinGame.setEnabled(false);

			gStartServer.setEnabled(false);
			gJoinGame.setEnabled(false);

			//gmSaveGame.setEnabled(true);
			gmCloseGame.setEnabled(true);

			remove(Pix);
			remove(guiSetup);

			add(guiGame, java.awt.BorderLayout.CENTER );

                }
            } );

	}

	public void serverState(boolean s) {


			serverOn=s;

			if (serverOn) {

				gmStartServer.setText(resbundle.getString("swing.menu.stopserver"));
				gStartServer.setText(resbundle.getString("swing.menu.stopserver"));

			}
			else {

				gmStartServer.setText(resbundle.getString("swing.menu.startserver"));
				gStartServer.setText(resbundle.getString("swing.menu.startserver"));

			}

	}

	public void setGameStatus(String state) {

		gameStatus.setText(" "+state);

	}

}

    public static void submitBug(Component parent, String text,String from,String subjectIn,String cause) {

            String subject = RiskUtil.GAME_NAME +" "+Risk.RISK_VERSION+" SwingGUI "+ TranslationBundle.getBundle().getLocale().toString()+" "+subjectIn;
        
            try {
                    net.yura.grasshopper.BugSubmitter.submitBug(text, from, subject, cause,
                            RiskUtil.GAME_NAME,
                                Risk.RISK_VERSION+" (save: " + RiskGame.SAVE_VERSION + " network: "+RiskGame.NETWORK_VERSION+")"
                                , TranslationBundle.getBundle().getLocale().toString()

                            );
                    JOptionPane.showMessageDialog(parent, "SENT!");
            }
            catch(Throwable ex) {
                try {
                    // for some reason + does not get decoded, so we set it back to a space
                    URL url = new URL("mailto:yura@yura.net"+
                            "?subject="+URLEncoder.encode(subject, "UTF-8").replace('+', ' ')+
                            "&body="+URLEncoder.encode(text, "UTF-8").replace('+', ' '));
                
                    RiskUtil.openURL(url);
                }
                catch (Throwable th) {
                    JOptionPane.showMessageDialog(parent, "ERROR: "+ex+" "+th );
                }
            }

    }

class DebugTab extends JSplitPane implements SwingGUITab,ActionListener {

	private JTextArea debugText;
	private JTextArea errText;

	private JToolBar toolbarDebug;
	private JMenu mDebug;
        
        String cause;

	public JToolBar getToolBar() {

		return toolbarDebug;

	}

	public JMenu getMenu() {

		return mDebug;

	}

	public void sendDebug(String a) {

			debugText.append(a + System.getProperty("line.separator") );

			debugText.setCaretPosition(debugText.getDocument().getLength());

	}

	public DebugTab() {
		super(JSplitPane.HORIZONTAL_SPLIT);

		setName("Debug");

		//##################### Debug ####################


		toolbarDebug = new JToolBar();
		toolbarDebug.setRollover(true);

		JButton tdSaveDebug  = new JButton("Save Debug Log");
		JButton tdPlayDebug  = new JButton("Play Debug Log");
		JButton tdClearDebug = new JButton("Clear Debug Log");
		JButton tdSaveError = new JButton("Save Error Log");
		JButton sendError = new JButton("Send Error Log");

		tdSaveDebug.setActionCommand("save debug");
		tdPlayDebug.setActionCommand("play debug");
		tdClearDebug.setActionCommand("clear debug");
		tdSaveError.setActionCommand("save error");
		sendError.setActionCommand("send error");

		tdSaveDebug.addActionListener(this);
		tdPlayDebug.addActionListener(this);
		tdClearDebug.addActionListener(this);
		tdSaveError.addActionListener(this);
		sendError.addActionListener(this);

                JButton cr = new JButton("Clear Error");
		cr.setActionCommand("clear error");
		cr.addActionListener(this);
                
                JButton gc = new JButton("GC");
		gc.setActionCommand("gc");
		gc.addActionListener(this);
		
                
                
		toolbarDebug.add(tdSaveDebug);
		toolbarDebug.add(tdPlayDebug);
		toolbarDebug.add(tdClearDebug);
		toolbarDebug.addSeparator();
		toolbarDebug.add(tdSaveError);
		toolbarDebug.add(sendError);
                toolbarDebug.add(cr);
                toolbarDebug.addSeparator();
                toolbarDebug.add(gc);

		toolbarDebug.setFloatable(false);





		mDebug = new JMenu("Debug");
		mDebug.setMnemonic('D');

		JMenuItem dSave = new JMenuItem("Save Debug Log");
		dSave.setMnemonic('S');
		dSave.setActionCommand("save debug");
		dSave.addActionListener( this );
		mDebug.add(dSave);

		JMenuItem dPlay = new JMenuItem("Play Debug Log");
		dPlay.setMnemonic('P');
		dPlay.setActionCommand("play debug");
		dPlay.addActionListener( this );
		mDebug.add(dPlay);

		JMenuItem dClear = new JMenuItem("Clear Debug Log");
		dClear.setMnemonic('C');
		dClear.setActionCommand("clear debug");
		dClear.addActionListener( this );
		mDebug.add(dClear);

		mDebug.addSeparator();

		JMenuItem dSaveErr = new JMenuItem("Save Error Log");
		dSaveErr.setMnemonic('E');
		dSaveErr.setActionCommand("save error");
		dSaveErr.addActionListener( this );
		mDebug.add(dSaveErr);

		JMenuItem send = new JMenuItem("Send Error Log");
		send.setMnemonic('S');
		send.setActionCommand("send error");
		send.addActionListener( this );
		mDebug.add(send);

		//mDebug.addSeparator();

		//JMenuItem aiwait = new JMenuItem("Change AI wait");
		//aiwait.setMnemonic('A');
		//aiwait.setActionCommand("aiwait");
		//aiwait.addActionListener( this );
		//mDebug.add(aiwait);






		debugText = new JTextArea();
		debugText.setEditable(false);

		JScrollPane debugScroll = new JScrollPane(debugText);
		debugScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		//debugScroll.setBorder( new javax.swing.border.TitledBorder( goodBorder, "Debug Log", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.ABOVE_TOP ) );

		JPanel debugPanel = new JPanel( new BorderLayout() );
		debugPanel.add( new JLabel("  Debug Log"), BorderLayout.NORTH );
		debugPanel.add(debugScroll);

		// ####### err

		errText = new JTextArea();
		errText.setEditable(false);


		JScrollPane errScroll = new JScrollPane( errText );
		//errScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		//errScroll.setBorder( BorderFactory.createTitledBorder( BorderFactory.createLoweredBevelBorder() , "Error Log", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.ABOVE_TOP ) );

		JPanel errPanel = new JPanel( new BorderLayout() );
		errPanel.add( new JLabel("  Error Log"), BorderLayout.NORTH );
		errPanel.add(errScroll);

		// ######## split

		debugPanel.setOpaque(false);
		errPanel.setOpaque(false);

		setLeftComponent(debugPanel);
		setRightComponent(errPanel);

		setContinuousLayout(true); 
		setOneTouchExpandable(true); 
		setDividerLocation(400);
		setBorder( BorderFactory.createEmptyBorder() );

		setOpaque(false);

	}

        public void start() {

		int size = 16;
		Image img = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		g.setColor(Color.RED );
		g.fillOval(0,0,size,size);
		g.dispose();
		final Icon icon = new ImageIcon(img);


                if (RiskUIUtil.checkForNoSandbox()) {

                    try {
                        // Could not open/create prefs root node Software\JavaSoft\Prefs at root 0x80000002. Windows RegCreateKeyEx(...) returned error code 5.
                        // HACK this will print any problems loading the Preferences before we start grasshopper
                        java.util.prefs.Preferences.userRoot(); // returns java.util.prefs.WindowsPreferences
                    }
                    catch (Throwable th) { }

                    net.yura.grasshopper.BugManager.interceptAndAlert(new Writer() {
                        public void write(char[] cbuf, int off, int len) {
                            errText.append(String.valueOf(cbuf, off, len));
                        }
                        public void flush() { }
                        public void close() { }
                    }, new net.yura.grasshopper.BugManager() {
                        public void action(String thecause) {
                                cause = thecause;
                                int nom = tabbedpane.indexOfComponent(DebugTab.this);

                                if (tabbedpane.getIconAt(nom)==null) {
                                    tabbedpane.setIconAt(nom,icon);
                                }
                        }
                    });

                    try {
                        net.yura.swingme.core.CoreUtil.setupLogging();
                    }
                    catch (Throwable th) {
                        RiskUtil.printStackTrace(th);
                    }
                    
		}

        }

	public void actionPerformed(ActionEvent a) {

		if (a.getActionCommand().equals("play debug")) {

			JFileChooser fc = new JFileChooser();
			RiskFileFilter filter = new RiskFileFilter(RiskFileFilter.RISK_LOG_FILES);
			fc.setFileFilter(filter);

			int returnVal = fc.showOpenDialog( RiskUIUtil.findParentFrame(this) );
			if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
				java.io.File file = fc.getSelectedFile();
				// Write your code here what to do with selected file

				String fileName = file.getAbsolutePath();

				go("newgame");
				go("play " + fileName );

			} else {
				// Write your code here what to do if user has canceled Open dialog
			}

		}
		else if (a.getActionCommand().equals("save debug")) {

			saveLog(debugText);

		}
		else if (a.getActionCommand().equals("clear debug")) {

			debugText.setText("");

		}
		else if (a.getActionCommand().equals("save error")) {

			saveLog(errText);

		}

		else if (a.getActionCommand().equals("send error")) {

                        String email = JOptionPane.showInputDialog(this,"tell me your e-mail please");

                        if (email == null) { email ="none"; }

                        submitBug( this, debugText.getText() + errText.getText(), email, "Bug",cause );

		}
                else if (a.getActionCommand().equals("clear error")) {
                
                    errText.setText("");
                    tabbedpane.setIconAt(tabbedpane.indexOfComponent(this), null);
                    
                }
                else if (a.getActionCommand().equals("gc")) {
                    
                    System.gc();
                    
                }
		else {

			throw new RuntimeException("command \""+a.getActionCommand()+"\" is not implemented yet\n");

		}
	}
}


class StatisticsTab extends JPanel implements SwingGUITab,ActionListener {

	private JToolBar toolbarStat;
	private JMenu sStatistics;
	private StatsPanel graph;

	private AbstractButton[] statbuttons;

	public JToolBar getToolBar() {

		return toolbarStat;
	}

	public JMenu getMenu() {

		return sStatistics;
	}

	public void actionPerformed(ActionEvent a) {

		graph.repaintStats( Integer.parseInt( a.getActionCommand() ) );
		graph.repaint();
	}

	public StatisticsTab() {

		setName( resbundle.getString("swing.tab.statistics") );

		//##################### graph ####################



		toolbarStat = new JToolBar();
		toolbarStat.setRollover(true);

		toolbarStat.setFloatable(false);



		graph = new StatsPanel(myrisk);
		graph.setBorder( BorderFactory.createLoweredBevelBorder() );



		statbuttons = new AbstractButton[24];



		statbuttons[0]=new JButton(resbundle.getString("swing.toolbar.countries"));
		statbuttons[1]=new JButton(resbundle.getString("swing.toolbar.armies"));
		statbuttons[2]=new JButton(resbundle.getString("swing.toolbar.kills"));
		statbuttons[3]=new JButton(resbundle.getString("swing.toolbar.casualties"));
		statbuttons[4]=new JButton(resbundle.getString("swing.toolbar.reinforcements"));
		statbuttons[5]=new JButton(resbundle.getString("swing.toolbar.continents"));
		statbuttons[6]=new JButton(resbundle.getString("swing.toolbar.empire"));
		statbuttons[7]=new JButton(resbundle.getString("swing.toolbar.attacks"));
		statbuttons[8]=new JButton(resbundle.getString("swing.toolbar.retreats"));
		statbuttons[9]=new JButton(resbundle.getString("swing.toolbar.victories"));
		statbuttons[10]=new JButton(resbundle.getString("swing.toolbar.defeats"));
		statbuttons[11]=new JButton(resbundle.getString("swing.toolbar.attacked"));

		for (int a=0; a<12; a++) {

			statbuttons[a].setActionCommand( (a+1)+"" );
			statbuttons[a].addActionListener(this);
			toolbarStat.add(statbuttons[a]);
			statbuttons[a].setEnabled(false);

		}




		// create Statistics menu item
		sStatistics = new JMenu( resbundle.getString("swing.tab.statistics") );
		sStatistics.setMnemonic('S');

		statbuttons[12]=new JMenuItem(resbundle.getString("swing.toolbar.countries"));
		statbuttons[13]=new JMenuItem(resbundle.getString("swing.toolbar.armies"));
		statbuttons[14]=new JMenuItem(resbundle.getString("swing.toolbar.kills"));
		statbuttons[15]=new JMenuItem(resbundle.getString("swing.toolbar.casualties"));
		statbuttons[16]=new JMenuItem(resbundle.getString("swing.toolbar.reinforcements"));
		statbuttons[17]=new JMenuItem(resbundle.getString("swing.toolbar.continents"));
		statbuttons[18]=new JMenuItem(resbundle.getString("swing.toolbar.empire"));
		statbuttons[19]=new JMenuItem(resbundle.getString("swing.toolbar.attacks"));
		statbuttons[20]=new JMenuItem(resbundle.getString("swing.toolbar.retreats"));
		statbuttons[21]=new JMenuItem(resbundle.getString("swing.toolbar.victories"));
		statbuttons[22]=new JMenuItem(resbundle.getString("swing.toolbar.defeats"));
		statbuttons[23]=new JMenuItem(resbundle.getString("swing.toolbar.attacked"));


		for (int a=12; a<statbuttons.length; a++) {

			statbuttons[a].setActionCommand( (a-11)+"" );
			statbuttons[a].addActionListener(this);
			sStatistics.add(statbuttons[a]);
			statbuttons[a].setEnabled(false);

		}

		setLayout( new BorderLayout() );
		add(graph);

	}

	public void startGame() {

			for (int a=0; a<statbuttons.length; a++) {

				statbuttons[a].setEnabled(true);

			}

	}

	public void closeGame() {



			for (int a=0; a<statbuttons.length; a++) {

				statbuttons[a].setEnabled(false);

			}

	}

}



	public void saveLog(JTextArea textArea) {



			JFileChooser fc = new JFileChooser();
			RiskFileFilter filter = new RiskFileFilter(RiskFileFilter.RISK_LOG_FILES);
			fc.setFileFilter(filter);

			int returnVal = fc.showSaveDialog( RiskUIUtil.findParentFrame(this) );
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

					printer.write(textArea.getText());

					printer.close();

				}

				catch(Exception error) {
					showError( error.getMessage() );
				}

			} else {
				// Write your code here what to do if user has canceled Save dialog
			}


	}

	/**
	 * Submits input to parser if neccessary
	 * @param input The string that is checked
	 */
	public void go(String input) {

		pp.setHighLight(PicturePanel.NO_COUNTRY);
		// Testing.append("Submitted: \""+input+"\"\n");

		if (gameState!=2 || !myrisk.getGame().getSetup() ) { blockInput(); }

		myrisk.parser(input);

		// Console.setCaretPosition(Console.getDocument().getLength());

	}

	/**
	 * Blocks the game panel
	 */
	public void blockInput() {

		gameState= -1;

		inGameCards.show(inGameInput, "nothing");

		gameTab.blockInput();
		consoleTab.blockInput();

	}

	/**
	 * This reads in a file for the commands
	 */
	public void Commands() {

		String commands="";

		try {

			BufferedReader bufferin=new BufferedReader(new InputStreamReader( RiskUtil.openStream("commands.txt") ));

			String input = bufferin.readLine();
			while(input != null) {
				if (commands.equals("")) { commands = input; }
				else { commands = commands + "\n" + input; }
				input = bufferin.readLine();
			}
			bufferin.close();
			// Testing.append("Commands Box opened\n");
			JOptionPane.showMessageDialog( RiskUIUtil.findParentFrame(this) , commands, resbundle.getString("swing.message.commands"), JOptionPane.PLAIN_MESSAGE);
		}
		catch (Exception e) {
			showError("error with commands.txt file: "+e.getMessage() );
		}


	}

	/**
	 * This opens the about dialog box
	 */
	public void openAbout() {

		RiskUIUtil.openAbout( RiskUIUtil.findParentFrame(this) ,product, version);
	}

	/**
	 * This opens the about dialog box
	 */
	public void openCards() {

		Frame frame = RiskUIUtil.findParentFrame(this);

		CardsDialog cardsDialog = new CardsDialog( frame ,pp, true, myrisk , (gameState==1) );
		Dimension frameSize = frame.getSize();
		Dimension aboutSize = cardsDialog.getPreferredSize();
		int x = frame.getLocation().x + (frameSize.width - aboutSize.width) / 2;
		int y = frame.getLocation().y + (frameSize.height - aboutSize.height) / 2;
		if (x < 0) x = 0;
		if (y < 0) y = 0;
		cardsDialog.setLocation(x, y);

		cardsDialog.populate( myrisk.getCurrentCards() );

		cardsDialog.setVisible(true);
	}

	public void showQuestion(int n) {


		moveNumber.setMaximum(n);
		moveNumber.setMinimum(1);
		moveNumber.setValue(1);



		String[] options = {
			resbundle.getString("swing.move.move"),
			resbundle.getString("swing.move.moveall"),
			resbundle.getString("swing.move.cancel")
		};

		int a = JOptionPane.showOptionDialog(
				RiskUIUtil.findParentFrame(this),                            // the parent that the dialog blocks
				moveNumber,                      // the dialog message array
				resbundle.getString("swing.move.title"), // the title of the dialog window
				JOptionPane.DEFAULT_OPTION,      // option type
				JOptionPane.QUESTION_MESSAGE,    // message type
				null,                            // optional icon, use null to use the default icon
				options,                         // options string array, will be made into buttons
				options[0]                       // option that should be made into a default button
		);

		if (a==0) {
			go("movearmies " + pp.getC1() + " " + pp.getC2() + " "+ moveNumber.getValue() );
		}
		if (a==1) {
			go("movearmies " + pp.getC1() + " " + pp.getC2() + " "+ n );
		}

	}

	public void showError(String error) {
		JOptionPane.showMessageDialog(this, resbundle.getString("swing.message.error") + " " + error, resbundle.getString("swing.title.error"), JOptionPane.ERROR_MESSAGE);
	}

	public void showMission(String mission) {
		JOptionPane.showMessageDialog(this, resbundle.getString("swing.message.mission") + " " + mission, resbundle.getString("swing.title.mission"), JOptionPane.INFORMATION_MESSAGE);
	}

	public void pprepaintCountries() {

		String tmp = (String)mapViewComboBox.getSelectedItem();
		int newview = -1;

		if (tmp.equals(resbundle.getString("game.tabs.continents")))           { newview=PicturePanel.VIEW_CONTINENTS; }
		else if (tmp.equals(resbundle.getString("game.tabs.ownership")))       { newview=PicturePanel.VIEW_OWNERSHIP; }
		else if (tmp.equals(resbundle.getString("game.tabs.borderthreat")))    { newview=PicturePanel.VIEW_BORDER_THREAT; }
		else if (tmp.equals(resbundle.getString("game.tabs.cardownership")))   { newview=PicturePanel.VIEW_CARD_OWNERSHIP; }
		else if (tmp.equals(resbundle.getString("game.tabs.troopstrength")))   { newview=PicturePanel.VIEW_TROOP_STRENGTH; }
		else if (tmp.equals(resbundle.getString("game.tabs.connectedempire"))) { newview=PicturePanel.VIEW_CONNECTED_EMPIRE; }

		pp.repaintCountries( newview );

	}

        public void showMapImage(Icon p) {
            guiSetup.showMapImage( p );
        }

	//############################################################################################################

// this get all the commands from the game and does what needs to be done
	class SwingRiskAdapter extends RiskAdapter {

		/**
		 * Checks if redrawing or repainting is needed
		 * @param output
		 * @param redrawNeeded If frame needs to be redrawn
		 * @param repaintNeeded If frame needs to be repainted
		 */
		public void sendMessage(String output, boolean redrawNeeded, boolean repaintNeeded) {

			// Testing.append("Returned: \""+output+"\"\n");

			consoleTab.addOutput(output);

			if (redrawNeeded) {
				pprepaintCountries();
			}
			if (repaintNeeded) {
				repaint();
			}
		}

		public void sendDebug(String a) {
                    if (debugTab!=null) {
			debugTab.sendDebug(a);
                    }
		}

		public void showMessageDialog(String a) {
			showError(a);
		}

		/**
		 * Blocks the game panel
		 */
		public void noInput() {
			blockInput();
		}

		/**
		 * checks if the the frame needs input
		 * @param s determines what needs input
		 */
		public void needInput(int s) {

			gameState=s;

			if (gameState != -1 && gameState!=RiskGame.STATE_NEW_GAME) {
				gameTab.getInput();
			}

			if (gameState == RiskGame.STATE_NEW_GAME) {
				inGameCards.show(inGameInput, "nothing");
			}
			else if (gameState == RiskGame.STATE_TRADE_CARDS) {

				// after wiping out someone if you go into trade mode
				pp.setC1(PicturePanel.NO_COUNTRY);
				pp.setC2(PicturePanel.NO_COUNTRY);

                                tradeCards.endtrade.setVisible( myrisk.getGame().canEndTrade() );
                                
                                armiesLeft( myrisk.getGame().getCurrentPlayer().getExtraArmies() , myrisk.getGame().NoEmptyCountries() );
				inGameCards.show(inGameInput, "tradeCards");
			}
			else if (gameState == RiskGame.STATE_PLACE_ARMIES) {
                            
                                armiesLeft( myrisk.getGame().getCurrentPlayer().getExtraArmies() , myrisk.getGame().NoEmptyCountries() );
				inGameCards.show(inGameInput, "placeArmies");
			}
			else if (gameState == RiskGame.STATE_ATTACKING) {

                                pp.setC1(PicturePanel.NO_COUNTRY);
                                pp.setC2(PicturePanel.NO_COUNTRY);
                                attacker.setText(resbundle.getString("game.note.selectattacker"));

				inGameCards.show(inGameInput, "attack");
			}
			else if (gameState == RiskGame.STATE_ROLLING) {
                                showDice(myrisk.getGame().getNoAttackDice(), true);
				inGameCards.show(inGameInput, "roll");
			}
			else if (gameState == RiskGame.STATE_BATTLE_WON) {
                            
                                int min = myrisk.getGame().getMustMove();
                            	int max = myrisk.hasArmiesInt( myrisk.getGame().getAttacker().getColor() ) -1;
                                slider.setMaximum(max);
                                slider.setMinimum(min);
                                slider.setValue(min);
                            
				inGameCards.show(inGameInput, "move");
			}
			else if (gameState == RiskGame.STATE_FORTIFYING) {
				inGameCards.show(inGameInput, "tacMove");
			}
			else if (gameState == RiskGame.STATE_END_TURN) {
				inGameCards.show(inGameInput, "endgo");
			}
			else if (gameState == RiskGame.STATE_GAME_OVER) {
                            
                                winner.continueButton.setVisible( myrisk.getGame().canContinue() );
                            
				inGameCards.show(inGameInput, "winner");
			}
			else if (gameState == RiskGame.STATE_SELECT_CAPITAL) {
				inGameCards.show(inGameInput, "capital");
			}
			else if (gameState == RiskGame.STATE_DEFEND_YOURSELF) {
                                showDice(myrisk.getGame().getNoDefendDice(), false);
				inGameCards.show(inGameInput, "defend");
			}

			consoleTab.getInput();

			repaint();

		}

		/**
		 * Displays a message
		 * @param state The message that is needed to be displayed
		 */
		public void setGameStatus(String state) {
			gameTab.setGameStatus(state);
		}

		public void showMapPic(RiskGame p) {
                    ImageIcon i=null;
                    try {
                        i = new ImageIcon( PicturePanel.getImage(p) );
                    }
                    catch(Throwable e) { }
                    guiSetup.showMapImage( i );
		}

		public void showCardsFile(String c, boolean m) {
                    guiSetup.showCardsFile(c,m);
		}

		public void newGame(boolean t) { // t==true: this is a local game
			localGame = t;
			gameTab.newGame();
		}

		/**
		 * Starts the game
		 * @param s If the game is a local game
		 */
		public void startGame(boolean s) {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			localGame = s;
			// check maybe we should load from memory
			if (myrisk.getGame().getMapFile() == null && myrisk.getGame().getCardsFile() == null) {
				pp.memoryLoad(editorTab.getImageMap(),editorTab.getImagePic());
			}
			else {
				try {
					pp.load();
				}
				catch(IOException e) {
                                        RiskUtil.printStackTrace(e);
				}
			}

                        blockInput();

			// YURA: not sure why this needs to be here, used to work without it
			pprepaintCountries();
			gameTab.startGame();
			statisticsTab.startGame();

                        
			SwingGUIPanel.this.setCursor(null); // Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
		}

		/**
		 * Closes the game
		 */
		public void closeGame() {
			gameTab.closeGame();
			statisticsTab.closeGame();
			System.gc();
		}

		private void armiesLeft(int l, boolean s) {
			armies.setText( resbundle.getString("core.input.armiesleft").replaceAll("\\{0\\}", "" + l));

			if (s) {
				autoplace.setVisible(false);
			}
			else {
				autoplace.setVisible(true);
			}
		}

		private void showDice(int n, boolean w) {

			JPanel p;

			if (w) { p=roll; }
			else { p=defend; }

			p.remove(roll1);
			p.remove(roll2);
			p.remove(roll3);

			GridBagConstraints c = new GridBagConstraints();
			c.insets = new java.awt.Insets(3, 3, 3, 3);
			c.fill = GridBagConstraints.BOTH;

			if (n > 0) {

				c.gridx = 0; // col
				c.gridy = 0; // row
				c.gridwidth = 1; // width
				c.gridheight = 1; // height
				p.add(roll1, c);


				if (n > 1) {

					c.gridx = 1; // col
					c.gridy = 0; // row
					c.gridwidth = 1; // width
					c.gridheight = 1; // height
					p.add(roll2, c);


					if (n > 2) {

						c.gridx = 2; // col
						c.gridy = 0; // row
						c.gridwidth = 1; // width
						c.gridheight = 1; // height
						p.add(roll3, c);

					}

				}

			}

		}

		public void showDiceResults(int[] att, int[] def) {

			String output=resbundle.getString("core.dice.results");

			output = output + " " + resbundle.getString("core.dice.attacker");
			for (int c=0; c< att.length ; c++) {
				output = output + " " + (att[c]+1);
			}

			output = output + " " + resbundle.getString("core.dice.defender");
			for (int c=0; c< def.length ; c++) {
				output = output + " " + (def[c]+1);
			}

			resultsLabel.setText(output);

			inGameCards.show(inGameInput, "results");

		}

		public void closeBattle() {
			blockInput();
		}

		public void serverState(boolean s) {
			gameTab.serverState(s);
		}

		public void addPlayer(int t, String name, int color, String ip) {
			guiSetup.addPlayer(t, name, color, ip);
		}

		public void delPlayer(String name) {
			guiSetup.delPlayer(name);
		}

/*  // other things that need to be done

public void openBattle(int c1num, int c2num) {}
public void setNODAttacker(int n) {}
public void setNODDefender(int n) {}

*/

	}

	//############################################################################################################

	class playersPanel extends JPanel {

		public void paintComponent(Graphics g) {

			int[] colors = myrisk.getPlayerColors();

			for (int c=0; c < colors.length ; c++) {
				g.setColor( new Color( colors[c] ) );
				g.fillRect( ((120/colors.length) * c) , 0 , (120/colors.length) , 20);
			}

			g.setColor( new Color( ColorUtil.getTextColorFor( colors[0] ) ) );

			g.drawRect( 2 , 2 , (120/colors.length)-5 , 15);

			g.setColor( Color.black );
			g.drawLine( (120/colors.length)-1 , 0, (120/colors.length)-1, 19);

		}

	}




	public JPanel makeGameOptionsPanel() {

			JPanel gameOptionsPanel = new JPanel();

			gameOptionsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 0));

			gameOptionsPanel.setOpaque(false);

			JLabel mapLookLabel = new JLabel(resbundle.getString("game.tabs.mapview") + ":");

			mapViewComboBox = new JComboBox();

			Dimension mapViewSize = new Dimension(120 , 20);

			mapViewComboBox.setPreferredSize(mapViewSize);
			mapViewComboBox.setMinimumSize(mapViewSize);
			mapViewComboBox.setMaximumSize(mapViewSize);

			mapViewComboBox.addItem(resbundle.getString("game.tabs.continents"));
			mapViewComboBox.addItem(resbundle.getString("game.tabs.ownership"));
			mapViewComboBox.addItem(resbundle.getString("game.tabs.borderthreat"));
			mapViewComboBox.addItem(resbundle.getString("game.tabs.cardownership"));
			mapViewComboBox.addItem(resbundle.getString("game.tabs.troopstrength"));
			mapViewComboBox.addItem(resbundle.getString("game.tabs.connectedempire"));

			mapViewComboBox.addActionListener(
					new ActionListener() {
						public void actionPerformed(ActionEvent a) {

							pprepaintCountries();

							pp.repaint();
						}
					}
			);

			JLabel playersLabel = new JLabel(resbundle.getString("newgame.label.players"));

			Dimension playerPanelSize = new Dimension(120 , 20);

			JPanel players = new playersPanel();

			players.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0,0,0),1));

			players.setPreferredSize(playerPanelSize);
			players.setMinimumSize(playerPanelSize);
			players.setMaximumSize(playerPanelSize);

			gameOptionsPanel.add(mapLookLabel);
			gameOptionsPanel.add(mapViewComboBox);
			gameOptionsPanel.add(playersLabel);
			gameOptionsPanel.add(players);
			gameOptionsPanel.add(showMission);
			gameOptionsPanel.add(showCards);
			gameOptionsPanel.add(Undo);

			return gameOptionsPanel;

	}


	class GamePanel extends JPanel {

		public GamePanel() {

			resultsLabel = new JLabel("RESULTS");

			Dimension mapSize = new Dimension(PicturePanel.PP_X,PicturePanel.PP_Y);

			pp.setPreferredSize(mapSize);
			pp.setMinimumSize(mapSize);
			pp.setMaximumSize(mapSize);

                        final MapMouseListener mml = new MapMouseListener(myrisk,pp);
                        MouseInputAdapter mapListener = new MouseInputAdapter() {
                            public void mouseExited(MouseEvent e) {
                                mml.mouseExited();
                            }
                            public void mouseReleased(MouseEvent e) {
                                int[] click = mml.mouseReleased(e.getX(),e.getY(),gameState);
                                if (click!=null) {
                                    mapClick(click,e);
                                }
                            }
                            public void mouseMoved(MouseEvent e) {
                                mml.mouseMoved(e.getX(),e.getY(),gameState);
                            }
                        };


			pp.addMouseListener(mapListener);
			pp.addMouseMotionListener(mapListener);

			Dimension d = new Dimension(PicturePanel.PP_X , 50);

			inGameCards = new CardLayout();

			inGameInput = new JPanel();
			inGameInput.setLayout( inGameCards );
			inGameInput.setPreferredSize(d);
			inGameInput.setMinimumSize(d);
			inGameInput.setMaximumSize(d);

			JPanel nothing = new JPanel();
			nothing.setPreferredSize(d);
			nothing.setMinimumSize(d);
			nothing.setMaximumSize(d);
			nothing.add(new JLabel(resbundle.getString("game.pleasewaitnetwork")) );

			JPanel results = new JPanel();
			results.setPreferredSize(d);
			results.setMinimumSize(d);
			results.setMaximumSize(d);
			results.add(resultsLabel);

			JPanel placeArmies = new placeArmiesPanel();
			placeArmies.setPreferredSize(d);
			placeArmies.setMinimumSize(d);
			placeArmies.setMaximumSize(d);

			roll = new rollPanel();
			roll.setPreferredSize(d);
			roll.setMinimumSize(d);
			roll.setMaximumSize(d);

			JPanel move = new movePanel();
			move.setPreferredSize(d);
			move.setMinimumSize(d);
			move.setMaximumSize(d);

			JPanel attack = new attackPanel();
			attack.setPreferredSize(d);
			attack.setMinimumSize(d);
			attack.setMaximumSize(d);

			defend = new defendPanel();
			defend.setPreferredSize(d);
			defend.setMinimumSize(d);
			defend.setMaximumSize(d);

			JPanel tacMove = new tacMovePanel();
			tacMove.setPreferredSize(d);
			tacMove.setMinimumSize(d);
			tacMove.setMaximumSize(d);

			JPanel capital = new capitalPanel();
			capital.setPreferredSize(d);
			capital.setMinimumSize(d);
			capital.setMaximumSize(d);

			tradeCards = new tradeCardsPanel();
			tradeCards.setPreferredSize(d);
			tradeCards.setMinimumSize(d);
			tradeCards.setMaximumSize(d);

			winner = new winnerPanel();
			winner.setPreferredSize(d);
			winner.setMinimumSize(d);
			winner.setMaximumSize(d);

			JPanel endgo = new endgoPanel();
			endgo.setPreferredSize(d);
			endgo.setMinimumSize(d);
			endgo.setMaximumSize(d);

			inGameInput.setOpaque(false);

			nothing.setOpaque(false);
			placeArmies.setOpaque(false);
			roll.setOpaque(false);
			move.setOpaque(false);
			attack.setOpaque(false);
			defend.setOpaque(false);
			tacMove.setOpaque(false);
			capital.setOpaque(false);
			tradeCards.setOpaque(false);
			winner.setOpaque(false);
			endgo.setOpaque(false);
			results.setOpaque(false);

			inGameInput.add(nothing, "nothing");
			inGameInput.add(placeArmies, "placeArmies");
			inGameInput.add(roll, "roll");
			inGameInput.add(move, "move");
			inGameInput.add(attack, "attack");
			inGameInput.add(defend, "defend");
			inGameInput.add(tacMove, "tacMove");
			inGameInput.add(capital, "capital");
			inGameInput.add(tradeCards, "tradeCards");
			inGameInput.add(winner, "winner");
			inGameInput.add(endgo, "endgo");
			inGameInput.add(results, "results");

			// ################### IN GAME #######################
/*

			this.setLayout(new java.awt.GridBagLayout());

			GridBagConstraints c = new GridBagConstraints();
			c.insets = new java.awt.Insets(3, 3, 3, 3);
			c.fill = GridBagConstraints.BOTH;

			c.gridx = 0; // col
			c.gridy = 0; // row
			c.gridwidth = 1; // width
			c.gridheight = 1; // height
			this.add(gameOptions, c);

			c.gridx = 0; // col
			c.gridy = 1; // row
			c.gridwidth = 1; // width
			c.gridheight = 1; // height
			this.add(pp, c);

			c.gridx = 0; // col
			c.gridy = 2; // row
			c.gridwidth = 1; // width
			c.gridheight = 1; // height
			this.add(inGameInput, c);

*/
			JPanel ppBorder = new JPanel( new BorderLayout() );
			ppBorder.setBorder(
				BorderFactory.createCompoundBorder(
					BorderFactory.createEmptyBorder(5,10,5,10),
					BorderFactory.createLineBorder(Color.BLACK,1)
				)
			);
			ppBorder.setOpaque(false);
			ppBorder.add(pp);

			setLayout(new BorderLayout());
			add(gameOptions, BorderLayout.NORTH);
			add(ppBorder);
			add(inGameInput, BorderLayout.SOUTH);

			setBorder( BorderFactory.createEmptyBorder(5,0,5,0) );

		}

		//**********************************************************************
		//                     MouseListener Interface
		//**********************************************************************

		public void mapClick(int[] countries,MouseEvent e) {

                    if (gameState == RiskGame.STATE_PLACE_ARMIES) {
                        if (countries.length==1) {
                            if ( e.getModifiers() == java.awt.event.InputEvent.BUTTON1_MASK ) {
                                go( "placearmies " + countries[0] + " 1" );
                            }
                            else {
                                go( "placearmies " + countries[0] + " 10" );
                            }
                        }
                    }
                    else if (gameState == RiskGame.STATE_ATTACKING) {
                            if (countries.length==0) {
                                attacker.setText(resbundle.getString("game.note.selectattacker"));
                            }
                            else if (countries.length == 1) {
                                attacker.setText(resbundle.getString("game.note.attackerisseldefender").replaceAll( "\\{0\\}", myrisk.getCountryName( countries[0])));
                            }
                            else {
                                go("attack " + countries[0] + " " + countries[1]);
                            }

                    }
                    else if (gameState == RiskGame.STATE_FORTIFYING) {
                            if (countries.length==0) {
                                    country1.setText("");
                            }
                            else if (countries.length == 1) {
                                    country1.setText( myrisk.getCountryName( countries[0]) );
                                    country2.setText("");
                            }
                            else if (countries.length == 2) {
                                    country2.setText( myrisk.getCountryName( countries[1]) );
                            }
                    }
                    else if (gameState == RiskGame.STATE_SELECT_CAPITAL) {
                            if (countries.length==0) {
                                capitalLabel.setText( resbundle.getString("core.help.selectcapital") );
                            }
                            else if (countries.length == 1) {
                                capitalLabel.setText( resbundle.getString("core.help.selectcapital")+": "+myrisk.getCountryName( countries[0]) );
                            }
                    }
                }
        }

	class SetupPanel extends JPanel implements ActionListener {

            private JRadioButton domination;
            private JRadioButton capital;
            private JRadioButton mission;
            private JCheckBox AutoPlaceAll;
            private JCheckBox recycle;

            private JLabel mapPic;
            private JTextField cardsFile;

            private void showCardsFile(String c, boolean m) {
                cardsFile.setText(c);

                if ( m==false && mission.isSelected() ) { domination.setSelected(true); AutoPlaceAll.setEnabled(true); }

                mission.setEnabled(m);
            }
            public void showMapImage(Icon p) {
                mapPic.setIcon( p ); // SCALE_DEFAULT
                SwingGUIPanel.this.setCursor(null); // Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
            }

			class NamedColor extends Color {

				private String name;
				private String realname;

				public NamedColor(Color color, String rn, String n) {
					super(color.getRGB());
					realname = rn;
					name = n;
				}

				public String toString() {
					return name;
				}

				public String getRealName() {
					return realname;
				}

			}
		private ButtonGroup CardTypeButtonGroup;
		private ButtonGroup GameTypeButtonGroup;
		private JTable players;
		private TableModel dataModel;
		private JButton defaultPlayers;
		private Object[][] data;
		private NamedColor[] namedColors;

		public void actionPerformed(ActionEvent e) {

			if (e.getSource()==mission) {

				AutoPlaceAll.setEnabled(false);

			}
			else if (e.getSource()==domination) {

				AutoPlaceAll.setEnabled(true);

			}
			else if (e.getSource()==capital) {

				AutoPlaceAll.setEnabled(true);

			}

		}

		public SetupPanel() {

			GridBagConstraints c = new GridBagConstraints();
			c.insets = new java.awt.Insets(3, 3, 3, 3);
			c.fill = GridBagConstraints.BOTH;

		// ##########################################################################################################

			JPanel playerOptions = new JPanel();
			playerOptions.setLayout(new java.awt.GridBagLayout());
			playerOptions.setBorder(javax.swing.BorderFactory.createTitledBorder( resbundle.getString("newgame.label.players") ));

			namedColors = new NamedColor[] {

				new NamedColor(Color.black,	"black"     , resbundle.getString("color.black")),
				new NamedColor(Color.blue,	"blue"      , resbundle.getString("color.blue")),
				new NamedColor(Color.cyan,	"cyan"      , resbundle.getString("color.cyan")),
				new NamedColor(Color.darkGray,  "darkgray"  , resbundle.getString("color.darkgray")),
				new NamedColor(Color.green,	"green"     , resbundle.getString("color.green")),
				new NamedColor(Color.lightGray, "lightgray" , resbundle.getString("color.lightgray")),
				new NamedColor(Color.magenta,	"magenta"   , resbundle.getString("color.magenta")),
				new NamedColor(Color.orange,	"orange"    , resbundle.getString("color.orange")),
				new NamedColor(Color.pink,	"pink"      , resbundle.getString("color.pink")),
				new NamedColor(Color.red, 	"red"       , resbundle.getString("color.red")),
				new NamedColor(Color.white,	"white"     , resbundle.getString("color.white")),
				new NamedColor(Color.yellow,	"yellow"    , resbundle.getString("color.yellow"))

			};

			final String[] names = {
				resbundle.getString("newgame.label.name"),
				resbundle.getString("newgame.label.color"),
				resbundle.getString("newgame.label.type"),
			};

			//String name="appletuser";
			//if (Risk.applet == null) {
			//	name = System.getProperty("user.name");
			//}

			// Create the dummy data (a few rows of names)
/*
			data = new Object[][] {
				{ name , namedColors[4], resbundle.getString("newgame.player.type.human")},
				{"bob", namedColors[1], resbundle.getString("newgame.player.type.easyai")},
				{"fred", namedColors[9], resbundle.getString("newgame.player.type.easyai")},
				{"ted", namedColors[11], resbundle.getString("newgame.player.type.easyai")},
				{"yura", namedColors[6], resbundle.getString("newgame.player.type.hardai")},
				{"lala", namedColors[2], resbundle.getString("newgame.player.type.hardai")}
			};
*/
			data = new Object[6][3];

			for (int cc=1;cc<=RiskGame.MAX_PLAYERS;cc++) {

				data[cc-1][0] = myrisk.getRiskConfig("default.player"+cc+".name");
				data[cc-1][1] = findColor( ColorUtil.getColor( myrisk.getRiskConfig("default.player"+cc+".color") ) );
				data[cc-1][2] = findType( Risk.getType( myrisk.getRiskConfig("default.player"+cc+".type") ) );

			}

			dataModel = new DefaultTableModel(data, names) { // AbstractTableModel
				//public int getColumnCount() { return names.length; }
				//public int getRowCount() { return data.length;}
				//public Object getValueAt(int row, int col) {return data[row][col];}
				public String getColumnName(int column) {return names[column];}
				public Class getColumnClass(int c) {return getValueAt(0, c).getClass();}
				public boolean isCellEditable(int row, int col) { return localGame; }
				public void setValueAt(Object aValue, int row, int column) { if(column==0) { if( ((String)aValue).length() > 15 ) { aValue = ((String)aValue).substring(0,15); } } super.setValueAt(aValue,row,column); } // if(column==0 && ((String)aValue).indexOf('$')!=-1 ) { return; } else { }
			};

			players = new JTable( dataModel );
			players.setSelectionMode(0);
			players.getTableHeader().setReorderingAllowed(false);

			DefaultTableCellRenderer colorRenderer = new DefaultTableCellRenderer() {
				public void setValue(Object value) {
					if (value instanceof NamedColor) {
						NamedColor c = (NamedColor) value;
						setBackground(c);
						setForeground( RiskUIUtil.getTextColorFor( c ) );
						setText(c.toString());
					} else {
						super.setValue(value);
					}
				}
			};

			final JComboBox colorComboBox = new JComboBox( namedColors );
			//for (int a=0; a < namedColors.length; a++) {

			//	colorComboBox.addItem(namedColors[a]);

			//}

			final JComboBox typeComboBox = new JComboBox();
			typeComboBox.addItem(resbundle.getString("newgame.player.type.human"));
			typeComboBox.addItem(resbundle.getString("newgame.player.type.crapai"));
			typeComboBox.addItem(resbundle.getString("newgame.player.type.easyai"));
			typeComboBox.addItem(resbundle.getString("newgame.player.type.hardai"));



			TableColumn colorColumn = players.getColumn(resbundle.getString("newgame.label.color"));
			colorColumn.setCellEditor(new DefaultCellEditor(colorComboBox));
			colorRenderer.setHorizontalAlignment(JLabel.CENTER);
			colorColumn.setCellRenderer(colorRenderer);

			TableColumn typeColumn = players.getColumn(resbundle.getString("newgame.label.type"));
			typeColumn.setCellEditor(new DefaultCellEditor(typeComboBox));


			JScrollPane scrollpane = new JScrollPane(players);
			Dimension psize = new Dimension(200,140);

			scrollpane.setPreferredSize(psize);
			scrollpane.setMinimumSize(psize);
			scrollpane.setMaximumSize(psize);

			// re-useing the combo box created for the table
			colorComboBox.addActionListener(
			    new ActionListener() {
				public void actionPerformed(ActionEvent a) {

					Color c = (Color)colorComboBox.getSelectedItem();
					colorComboBox.setBackground( c );
					colorComboBox.setForeground( RiskUIUtil.getTextColorFor(c) );

				}
			    }
			);

			JButton newPlayer = new JButton(resbundle.getString("newgame.newplayer"));

			newPlayer.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent a) {

						if (localGame) {

							((DefaultTableModel)dataModel).addRow( new Object[] { resbundle.getString("newgame.newplayername") ,namedColors[0],resbundle.getString("newgame.player.type.human")} );

						}
						else {

                					colorComboBox.setSelectedIndex(0);
							typeComboBox.setSelectedIndex(0);

 							// Messages 
							Object[] message = new Object[6];

							message[0] = resbundle.getString("newgame.label.name");
							message[1] = new JTextField( resbundle.getString("newgame.newplayername") );
							message[2] = resbundle.getString("newgame.label.color");
							message[3] = colorComboBox;
							message[4] = resbundle.getString("newgame.label.type");
							message[5] = typeComboBox;

							// Options 
							String[] options = { 
								"OK",
								"Cancel"
							}; 
							int result = JOptionPane.showOptionDialog( 
								RiskUIUtil.findParentFrame(SwingGUIPanel.this),			// the parent that the dialog blocks
								message,				// the dialog message array 
								"create new player",			// the title of the dialog window 
								JOptionPane.OK_CANCEL_OPTION,		// option type 
								JOptionPane.QUESTION_MESSAGE,		// message type 
								null,					// optional icon, use null to use the default icon 
								options,				// options string array, will be made into buttons 
								options[0]				// option that should be made into a default button 
							); 

							if (result==0) {


								String type=null;
								int types=typeComboBox.getSelectedIndex();

								switch (types) {
									case 0: type = "human"; break;
									case 1: type = "ai crap"; break;
									case 2: type = "ai easy"; break;
									case 3: type = "ai hard"; break;
								}

								go("newplayer "+type+" "+((NamedColor)colorComboBox.getSelectedItem()).getRealName()+" "+((JTextField)message[1]).getText());

							}
						}
					}
				}
			);


			JButton delPlayer = new JButton(resbundle.getString("newgame.removeplayer"));

			delPlayer.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent a) {

						if (players.getSelectedRow() == -1 ) { return; }

						if (localGame) {

							players.removeEditor();

							((DefaultTableModel)dataModel).removeRow( players.getSelectedRow() );

						}
						else {
							go("delplayer "+ players.getValueAt( players.getSelectedRow() , 0));
						}

					}
				}
			);

			defaultPlayers = new JButton(resbundle.getString("newgame.resetplayers"));

			defaultPlayers.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent a) {

						players.removeEditor();
						resetPlayers();

					}
				}
			);

			c.gridx = 0; // col
			c.gridy = 0; // row
			c.gridwidth = 3; // width
			c.gridheight = 1; // height
			playerOptions.add(scrollpane, c);

			c.gridx = 0; // col
			c.gridy = 1; // row
			c.gridwidth = 1; // width
			c.gridheight = 1; // height
			playerOptions.add(newPlayer, c);

			c.gridx = 1; // col
			c.gridy = 1; // row
			c.gridwidth = 1; // width
			c.gridheight = 1; // height
			playerOptions.add(delPlayer, c);

			c.gridx = 2; // col
			c.gridy = 1; // row
			c.gridwidth = 1; // width
			c.gridheight = 1; // height
			playerOptions.add(defaultPlayers, c);

		// ##########################################################################################################

			JPanel mapOptions = new JPanel();
			mapOptions.setLayout(new java.awt.GridBagLayout());
			mapOptions.setBorder(javax.swing.BorderFactory.createTitledBorder( resbundle.getString("newgame.label.map") ));

			JButton chooseMap = new BadgeButton(resbundle.getString("newgame.choosemap"));

			chooseMap.addActionListener(
					new ActionListener() {
						public void actionPerformed(ActionEvent a) {

							String name = RiskUIUtil.getNewMap( RiskUIUtil.findParentFrame(SwingGUIPanel.this) );

							if (name != null) {

								SwingGUIPanel.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

								go("choosemap " + name );

							}
						}
					}
			);


			JButton defaultMap = new JButton(resbundle.getString("newgame.defaultmap"));

			defaultMap.addActionListener(
					new ActionListener() {
						public void actionPerformed(ActionEvent a) {

							go("choosemap "+RiskGame.getDefaultMap() );
						}
					}
			);



		mapPic = new JLabel();
		mapPic.setBorder( BorderFactory.createLoweredBevelBorder() );
		Dimension size = new Dimension(203,127);
		mapPic.setPreferredSize(size);
		mapPic.setMinimumSize(size);
		mapPic.setMaximumSize(size);

			c.gridx = 0; // col
			c.gridy = 0; // row
			c.gridwidth = 2; // width
			c.gridheight = 1; // height
			mapOptions.add(mapPic, c);

			c.gridx = 0; // col
			c.gridy = 1; // row
			c.gridwidth = 1; // width
			c.gridheight = 1; // height
			mapOptions.add(chooseMap, c);

			c.gridx = 1; // col
			c.gridy = 1; // row
			c.gridwidth = 1; // width
			c.gridheight = 1; // height
			mapOptions.add(defaultMap, c);



			JPanel cardOptions = new JPanel();
			cardOptions.setLayout(new java.awt.GridBagLayout());
			cardOptions.setBorder(javax.swing.BorderFactory.createTitledBorder( resbundle.getString("newgame.label.cards") ));


			cardsFile = new JTextField("");
			cardsFile.setEditable(false);
			cardsFile.setBackground( mapOptions.getBackground() ); // SystemColor.control

			size = new Dimension(200,20);

			cardsFile.setPreferredSize(size);
			cardsFile.setMinimumSize(size);
			cardsFile.setMaximumSize(size);

			JButton chooseCards = new JButton(resbundle.getString("newgame.choosecards"));

			chooseCards.addActionListener(
					new ActionListener() {
						public void actionPerformed(ActionEvent a) {

							String name = RiskUIUtil.getNewFile( RiskUIUtil.findParentFrame(SwingGUIPanel.this), RiskFileFilter.RISK_CARDS_FILES);

							if (name != null) {

								go("choosecards " + name );
							}

						}
					}
			);


			JButton defaultCards = new JButton(resbundle.getString("newgame.defaultcards"));

			defaultCards.addActionListener(
					new ActionListener() {
						public void actionPerformed(ActionEvent a) {

							go("choosecards "+RiskGame.getDefaultCards() );
						}
					}
			);


			c.gridx = 0; // col
			c.gridy = 0; // row
			c.gridwidth = 2; // width
			c.gridheight = 1; // height
			cardOptions.add(cardsFile, c);

			c.gridx = 0; // col
			c.gridy = 1; // row
			c.gridwidth = 1; // width
			c.gridheight = 1; // height
			cardOptions.add(chooseCards, c);

			c.gridx = 1; // col
			c.gridy = 1; // row
			c.gridwidth = 1; // width
			c.gridheight = 1; // height
			cardOptions.add(defaultCards, c);


			JPanel GameTypeButtons = new JPanel();
			GameTypeButtons.setLayout(new javax.swing.BoxLayout(GameTypeButtons, javax.swing.BoxLayout.Y_AXIS));
			GameTypeButtons.setBorder(javax.swing.BorderFactory.createTitledBorder(resbundle.getString("newgame.label.gametype")));

			JPanel cardsOptions = new JPanel();
			cardsOptions.setLayout(new javax.swing.BoxLayout(cardsOptions, javax.swing.BoxLayout.Y_AXIS));
			cardsOptions.setBorder(javax.swing.BorderFactory.createTitledBorder(resbundle.getString("newgame.label.cardsoptions")));


			GameTypeButtonGroup = new ButtonGroup();
			CardTypeButtonGroup = new ButtonGroup();

			domination = new JRadioButton(resbundle.getString("newgame.mode.domination"));
			capital = new JRadioButton(resbundle.getString("newgame.mode.capital"));
			mission = new JRadioButton(resbundle.getString("newgame.mode.mission"), true);

			domination.setOpaque(false);
			capital.setOpaque(false);
			mission.setOpaque(false);

			domination.addActionListener( this );
			capital.addActionListener( this );
			mission.addActionListener( this );

			final JRadioButton increasing = new JRadioButton(resbundle.getString("newgame.cardmode.increasing"));
			final JRadioButton fixed = new JRadioButton(resbundle.getString("newgame.cardmode.fixed"), true);
                        final JRadioButton italian = new JRadioButton(resbundle.getString("newgame.cardmode.italianlike"));

			increasing.setOpaque(false);
			fixed.setOpaque(false);
                        italian.setOpaque(false);

			GameTypeButtonGroup.add ( domination );
			GameTypeButtonGroup.add ( capital );
			GameTypeButtonGroup.add ( mission );

			CardTypeButtonGroup.add ( increasing );
			CardTypeButtonGroup.add ( fixed );
                        CardTypeButtonGroup.add ( italian );

			GameTypeButtons.add( domination );
			GameTypeButtons.add( capital );
			GameTypeButtons.add( mission );

			cardsOptions.add( increasing );
			cardsOptions.add( fixed );
                        cardsOptions.add( italian );

			JPanel GameOptionsButtons = new JPanel();
			GameOptionsButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
			GameOptionsButtons.setBorder(javax.swing.BorderFactory.createTitledBorder(resbundle.getString("newgame.label.startgameoptions")));

			AutoPlaceAll = new JCheckBox(resbundle.getString("newgame.autoplace"));
			GameOptionsButtons.add( AutoPlaceAll );
			AutoPlaceAll.setOpaque(false);
                        AutoPlaceAll.setSelected( "true".equals(myrisk.getRiskConfig("default.autoplaceall")) );

			recycle = new JCheckBox(resbundle.getString("newgame.recycle"));
			GameOptionsButtons.add( recycle );
			recycle.setOpaque(false);
                        recycle.setSelected( "true".equals(myrisk.getRiskConfig("default.recyclecards")) );

			JButton startGame = new JButton(resbundle.getString("newgame.startgame"));

			startGame.addActionListener(
					new ActionListener() {
						public void actionPerformed(ActionEvent a) {

							boolean setupOK=true;
							String error="";

							if (players.getRowCount() >= 2 && players.getRowCount() <= RiskGame.MAX_PLAYERS) {

								for (int c=0; c < players.getRowCount(); c++) {
									for (int b=(c+1); b < players.getRowCount(); b++) {
										if ( ((String)players.getValueAt(c, 0)).equals( players.getValueAt(b, 0) ) ) {
											setupOK=false;
											error=resbundle.getString("newgame.error.samename");
										}
										if ( players.getValueAt(c, 1) == ( players.getValueAt(b, 1) ) ) {
											setupOK=false;
											error=resbundle.getString("newgame.error.samecolor");
										}
									}
								}

							}
							else {
								setupOK=false;
								error=resbundle.getString("newgame.error.2to6players");
							}

							//if ((players.getRowCount() == 2) && ( !(((String)players.getValueAt(0, 2)).equals("Human")) || !(((String)players.getValueAt(1, 2)).equals("Human")) || !(domination.isSelected()) ) ) {
							//	setupOK=false;
							//	error=resbundle.getString("newgame.error.2playerdominationonly");
							//}

							if (setupOK == true) {

								if (localGame) {

								    for (int c=0; c < players.getRowCount(); c++) {

									String Ptype="";

									if ( players.getValueAt(c, 2).equals(      resbundle.getString("newgame.player.type.human")  ) ) { Ptype="human"; }
									else if ( players.getValueAt(c, 2).equals( resbundle.getString("newgame.player.type.crapai") ) ) { Ptype="ai crap"; }
									else if ( players.getValueAt(c, 2).equals( resbundle.getString("newgame.player.type.easyai") ) ) { Ptype="ai easy"; }
									else if ( players.getValueAt(c, 2).equals( resbundle.getString("newgame.player.type.hardai") ) ) { Ptype="ai hard"; }

									go("newplayer " + Ptype + " " + ((NamedColor)players.getValueAt(c, 1)).getRealName() + " " + players.getValueAt(c, 0) );


								    }

								}

								String type="";

								if (domination.isSelected()) { type = "domination"; }
								else if (capital.isSelected()) { type = "capital"; }
								else if (mission.isSelected()) { type = "mission"; }

								if (increasing.isSelected()) { type += " increasing"; }
								else if (fixed.isSelected()) { type += " fixed"; }
                                                                else { type += " italianlike"; }

								go("startgame " + type + (( AutoPlaceAll.isSelected() )?(" autoplaceall"):("")) + (( recycle.isSelected() )?(" recycle"):("")) );

							}
							else {
								showError(error);
							}

						}
					}
			);


			// ################### GAME SETUP #######################
			this.setLayout(new java.awt.GridBagLayout());

			playerOptions.setOpaque(false);
			GameTypeButtons.setOpaque(false);
			mapOptions.setOpaque(false);
			cardOptions.setOpaque(false);
			GameOptionsButtons.setOpaque(false);
			cardsOptions.setOpaque(false);

			c.gridx = 0; // col
			c.gridy = 0; // row
			c.gridwidth = 1; // width
			c.gridheight = 1; // height
			this.add(playerOptions, c);

			c.gridx = 0; // col
			c.gridy = 2; // row
			c.gridwidth = 1; // width
			c.gridheight = 2; // height
			this.add(GameTypeButtons, c);


			c.gridx = 1; // col
			c.gridy = 0; // row
			c.gridwidth = 1; // width
			c.gridheight = 1; // height
			this.add(mapOptions, c);

			c.gridx = 0; // col
			c.gridy = 1; // row
			c.gridwidth = 1; // width
			c.gridheight = 1; // height
			this.add(cardsOptions, c);

			c.gridx = 1; // col
			c.gridy = 1; // row
			c.gridwidth = 1; // width
			c.gridheight = 1; // height
			this.add(cardOptions, c);

			c.gridx = 1; // col
			c.gridy = 2; // row
			c.gridwidth = 1; // width
			c.gridheight = 1; // height
			this.add(GameOptionsButtons, c);

			c.gridx = 1; // col
			c.gridy = 3; // row
			c.gridwidth = 1; // width
			c.gridheight = 1; // height
			this.add(startGame, c);

		}

		public void resetPlayers() {

			while (players.getRowCount() != 0) ((DefaultTableModel)dataModel).removeRow( 0 );

			for (int c=0; c < data.length; c++) {
				((DefaultTableModel)dataModel).addRow( data[c] );
			}

		}

		public void setupGame() {

			players.removeEditor();

			if (localGame) {

				resetPlayers();
				defaultPlayers.setEnabled(true);
			}
			else {

				// remove all players
				while (players.getRowCount() != 0) ((DefaultTableModel)dataModel).removeRow( 0 );
				defaultPlayers.setEnabled(false);
			}

		}


		public void addPlayer(int t, String name, int color, String ip) {

			if (!localGame) {

				NamedColor c=findColor(color);

				String type=findType(t);

				((DefaultTableModel)dataModel).addRow( new Object[] {name, c ,type} );

			}

		}

		public NamedColor findColor(int color) {

				// go though array of colors and find correct NamedColor

				for (int a=0; a < namedColors.length; a++) {

					if (namedColors[a].getRGB()==color) { return namedColors[a]; }

				}

				return null;
		}
		public String findType(int t) {

				if (t == Player.PLAYER_HUMAN) {
					return resbundle.getString("newgame.player.type.human");
				}

				if (t == Player.PLAYER_AI_CRAP) {
					return resbundle.getString("newgame.player.type.crapai");
				}

				if (t == Player.PLAYER_AI_EASY) {
					return resbundle.getString("newgame.player.type.easyai");
				}

				if (t == Player.PLAYER_AI_HARD) {
					return resbundle.getString("newgame.player.type.hardai");
				}

				return null;

		}

		public void delPlayer(String name) {

			if (!localGame) {

				for (int c=0; c < players.getRowCount(); c++) {

					if ( players.getValueAt(c,0).equals(name) ) {
						((DefaultTableModel)dataModel).removeRow( c );
						break;
					}
				}
			}
		}


	}

        private JLabel capitalLabel;
	class capitalPanel extends JPanel {

		public capitalPanel() {

			this.setLayout(new java.awt.GridBagLayout());

			GridBagConstraints c = new GridBagConstraints();
			c.insets = new java.awt.Insets(3, 3, 3, 3);
			c.fill = GridBagConstraints.BOTH;

			JButton endtrade = new JButton(resbundle.getString("about.okbutton"));

			endtrade.addActionListener(
					new ActionListener() {
						public void actionPerformed(ActionEvent a) {
                                                        int c1Id = pp.getC1();
							pp.setC1(PicturePanel.NO_COUNTRY);
							go("capital "+c1Id );
						}
					}
			);

			capitalLabel = new JLabel(resbundle.getString("core.help.selectcapital"));

			c.gridx = 0; // col
			c.gridy = 0; // row
			c.gridwidth = 1; // width
			c.gridheight = 1; // height
			this.add(capitalLabel, c);

			c.gridx = 1; // col
			c.gridy = 0; // row
			c.gridwidth = 1; // width
			c.gridheight = 1; // height
			this.add(endtrade, c);

		}
	}

	class tradeCardsPanel extends JPanel {

                JButton endtrade;
            
		public tradeCardsPanel() {

			endtrade = new JButton(resbundle.getString("game.button.go.endtrade"));

			endtrade.addActionListener(
					new ActionListener() {
						public void actionPerformed(ActionEvent a) {
							go("endtrade");
						}
					}
			);

			add( new JLabel(resbundle.getString("cards.totradeclick")) );
			add(endtrade);

		}
	}

	class endgoPanel extends JPanel {

		public endgoPanel() {

			this.setLayout(new java.awt.GridBagLayout());

			GridBagConstraints c = new GridBagConstraints();
			c.insets = new java.awt.Insets(3, 3, 3, 3);
			c.fill = GridBagConstraints.BOTH;

			JButton endturn = new JButton("End Go");

			endturn.addActionListener(
					new ActionListener() {
						public void actionPerformed(ActionEvent a) {
							go("endgo");
						}
					}
			);

			c.gridx = 0; // col
			c.gridy = 0; // row
			c.gridwidth = 1; // width
			c.gridheight = 1; // height
			this.add(endturn, c);

		}
	}

	class placeArmiesPanel extends JPanel {

		public placeArmiesPanel() {

			this.setLayout(new java.awt.GridBagLayout());

			GridBagConstraints c = new GridBagConstraints();
			c.insets = new java.awt.Insets(3, 3, 3, 3);
			c.fill = GridBagConstraints.BOTH;

			autoplace.addActionListener(
					new ActionListener() {
						public void actionPerformed(ActionEvent a) {
							go("autoplace");
						}
					}
			);

			c.gridx = 0; // col
			c.gridy = 0; // row
			c.gridwidth = 1; // width
			c.gridheight = 1; // height
			this.add(armies, c);

			c.gridx = 1; // col
			c.gridy = 0; // row
			c.gridwidth = 1; // width
			c.gridheight = 1; // height
			this.add(autoplace, c);

		}
	}

	class attackPanel extends JPanel {

		public attackPanel() {

			this.setLayout(new java.awt.GridBagLayout());

			Dimension size = new Dimension(300,20);

			attacker = new JLabel(resbundle.getString("game.note.selectattacker"));

			attacker.setPreferredSize(size);
			attacker.setMinimumSize(size);
			attacker.setMaximumSize(size);

			GridBagConstraints c = new GridBagConstraints();
			c.insets = new java.awt.Insets(3, 3, 3, 3);
			c.fill = GridBagConstraints.BOTH;


			JButton endAttackButton = new JButton(resbundle.getString("game.button.go.endattack"));

			endAttackButton.addActionListener(
					new ActionListener() {
						public void actionPerformed(ActionEvent a) {
							go("endattack");
							attacker.setText(resbundle.getString("game.note.selectattacker"));

							pp.setC1(PicturePanel.NO_COUNTRY);
							pp.setC2(PicturePanel.NO_COUNTRY);
							pp.repaint();
						}
					}
			);


			c.gridx = 0; // col
			c.gridy = 0; // row
			c.gridwidth = 1; // width
			c.gridheight = 1; // height
			this.add(attacker, c);

			c.gridx = 1; // col
			c.gridy = 0; // row
			c.gridwidth = 1; // width
			c.gridheight = 1; // height
			this.add(endAttackButton, c);

		}
	}

	class tacMovePanel extends JPanel {

		public tacMovePanel() {

			this.setLayout(new java.awt.GridBagLayout());

			Dimension size = new Dimension(200,40);

			country1 = new JTextField("");
			country1.setEditable(false);
			//country1.setBackground(SystemColor.control);
			country1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED),"Source",javax.swing.border.TitledBorder.LEADING,javax.swing.border.TitledBorder.TOP,new java.awt.Font("SansSerif",0,11),new java.awt.Color(60,60,60)));

			country1.setPreferredSize(size);
			country1.setMinimumSize(size);
			country1.setMaximumSize(size);

			country2 = new JTextField("");
			country2.setEditable(false);
			//country2.setBackground(SystemColor.control);
			country2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED),"Target",javax.swing.border.TitledBorder.LEADING,javax.swing.border.TitledBorder.TOP,new java.awt.Font("SansSerif",0,11),new java.awt.Color(60,60,60)));

			country2.setPreferredSize(size);
			country2.setMinimumSize(size);
			country2.setMaximumSize(size);

			country1.setOpaque(false);
			country2.setOpaque(false);

			// s.setBorder(new TitledBorder("Number of Armies to move") );

			moveNumber.setMinimumSize(new Dimension(300, 50));

			moveNumber.putClientProperty("JSlider.isFilled", Boolean.TRUE );

			moveNumber.setPaintTicks(true);
			moveNumber.setMajorTickSpacing(1);
			// moveNumber.setMinorTickSpacing(1);

			moveNumber.setPaintLabels( true );
			moveNumber.setSnapToTicks( true );

			moveNumber.getLabelTable().put(new Integer(11), new JLabel(new Integer(11).toString(), JLabel.CENTER));
			moveNumber.setLabelTable( slider.getLabelTable() );

			moveNumber.getAccessibleContext().setAccessibleName("slider");
			moveNumber.getAccessibleContext().setAccessibleDescription("move armies slider");

			// moveNumber.addChangeListener(listener);


			GridBagConstraints c = new GridBagConstraints();
			c.insets = new java.awt.Insets(3, 3, 3, 3);
			c.anchor = GridBagConstraints.SOUTH;

			JButton moveButton = new JButton(resbundle.getString("move.move"));

			moveButton.addActionListener(
					new ActionListener() {
						public void actionPerformed(ActionEvent a) {

							if ( !(country1.getText().equals("")) && !(country2.getText().equals("")) ) {

								//FIXME: get the id of the country, don't use the string name
								int nCountryId = myrisk.getGame().getCountryInt(pp.getC1()).getColor();
								//int nCountryId = myrisk.getGame().getCountry(country1.getText()).getColor();

								showQuestion( myrisk.hasArmiesInt(nCountryId) - 1);
//			    showQuestion( myrisk.hasArmies(country1.getText() )-1 );

								country1.setText("");
								country2.setText("");
								pp.setC1(PicturePanel.NO_COUNTRY);
								pp.setC2(PicturePanel.NO_COUNTRY);
                                                                pp.repaint();
							}
						}
					}
			);

			JButton noMoveButton = new JButton(resbundle.getString("game.button.go.nomove"));

			noMoveButton.addActionListener(
					new ActionListener() {
						public void actionPerformed(ActionEvent a) {
							go("nomove");
							country1.setText("");
							country2.setText("");
							pp.setC1(PicturePanel.NO_COUNTRY);
							pp.setC2(PicturePanel.NO_COUNTRY);
						}
					}
			);

			c.gridx = 0; // col
			c.gridy = 0; // row
			c.gridwidth = 1; // width
			c.gridheight = 1; // height
			this.add(country1, c);

			c.gridx = 1; // col
			c.gridy = 0; // row
			c.gridwidth = 1; // width
			c.gridheight = 1; // height
			this.add(country2, c);

			c.gridx = 2; // col
			c.gridy = 0; // row
			c.gridwidth = 1; // width
			c.gridheight = 1; // height
			this.add(moveButton, c);

			c.gridx = 3; // col
			c.gridy = 0; // row
			c.gridwidth = 1; // width
			c.gridheight = 1; // height
			this.add(noMoveButton, c);

		}
	}

	class rollPanel extends JPanel {

		public rollPanel() {

			this.setLayout(new java.awt.GridBagLayout());

			GridBagConstraints c = new GridBagConstraints();
			c.insets = new java.awt.Insets(3, 3, 3, 3);
			c.fill = GridBagConstraints.BOTH;

			JButton retreat = new JButton(resbundle.getString("battle.retreat"));

			retreat.addActionListener(
                            new ActionListener() {
                                public void actionPerformed(ActionEvent a) {
                                    go("retreat");
                                }
                            }
			);

			c.gridx = 3; // col
			c.gridy = 0; // row
			c.gridwidth = 1; // width
			c.gridheight = 1; // height
			this.add(retreat, c);

		}
	}

	class defendPanel extends JPanel {

		public defendPanel() {

			this.setLayout(new java.awt.GridBagLayout());

			GridBagConstraints c = new GridBagConstraints();
			c.insets = new java.awt.Insets(3, 3, 3, 3);
			c.fill = GridBagConstraints.BOTH;

		}
	}

	class movePanel extends JPanel {

		public movePanel() {

			this.setLayout(new java.awt.GridBagLayout());

			GridBagConstraints c = new GridBagConstraints();
			c.insets = new java.awt.Insets(3, 3, 3, 3);
			//c.fill = GridBagConstraints.BOTH;


			// s.setBorder(new TitledBorder("Number of Armies to move") );

			slider.setMinimumSize(new Dimension(300, 50));

			slider.putClientProperty("JSlider.isFilled", Boolean.TRUE );

			slider.setPaintTicks(true);
			slider.setMajorTickSpacing(1);
			// slider.setMinorTickSpacing(1);

			slider.setPaintLabels( true );
			slider.setSnapToTicks( true );

			slider.getLabelTable().put(new Integer(11), new JLabel(new Integer(11).toString(), JLabel.CENTER));
			slider.setLabelTable( slider.getLabelTable() );

			slider.getAccessibleContext().setAccessibleName("slider");
			slider.getAccessibleContext().setAccessibleDescription("move armies slider");

			// slider.addChangeListener(listener);

			slider.setOpaque(false);

			JLabel label = new JLabel(resbundle.getString("move.numberofarmies"));

			JButton move1 = new JButton(resbundle.getString("move.move"));

			move1.addActionListener(
					new ActionListener() {
						public void actionPerformed(ActionEvent a) {
							go("move " + slider.getValue() );
						}
					}
			);

			JButton moveall = new JButton(resbundle.getString("move.moveall"));

			moveall.addActionListener(
					new ActionListener() {
						public void actionPerformed(ActionEvent a) {
							go("move all");
						}
					}
			);

			c.gridx = 0; // col
			c.gridy = 0; // row
			c.gridwidth = 1; // width
			c.gridheight = 1; // height
			this.add(label, c);

			c.gridx = 1; // col
			c.gridy = 0; // row
			c.gridwidth = 1; // width
			c.gridheight = 1; // height
			this.add(slider, c);

			c.gridx = 2; // col
			c.gridy = 0; // row
			c.gridwidth = 1; // width
			c.gridheight = 1; // height
			this.add(move1, c);

			c.gridx = 3; // col
			c.gridy = 0; // row
			c.gridwidth = 1; // width
			c.gridheight = 1; // height
			this.add(moveall, c);


		}
	}

	class winnerPanel extends JPanel {

                JButton continueButton;
            
		public winnerPanel() {

                        continueButton = new JButton(resbundle.getString("game.button.go.continue"));
			continueButton.addActionListener(
                            new ActionListener() {
                                public void actionPerformed(ActionEvent a) {
                                    go("continue");
                                }
                            }
			);
                        
                        add( new JLabel(resbundle.getString("game.over")) );
                        add(continueButton);
		}
	}

}
