// Yura Mamyrin, Group D

package net.yura.domination.ui.simplegui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import javax.swing.event.MouseInputListener;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import net.yura.domination.engine.ColorUtil;
import net.yura.domination.engine.Risk;
import net.yura.domination.engine.RiskAdapter;
import net.yura.domination.engine.RiskUIUtil;
import net.yura.domination.engine.RiskUtil;
import net.yura.domination.engine.guishared.AboutDialog;
import net.yura.domination.engine.guishared.PicturePanel;
import net.yura.domination.engine.guishared.RiskFileFilter;

/**
 * <p> Simple GUI for Risk </p>
 * @author Yura Mamyrin
 */

public class RiskGUI extends JFrame implements MouseInputListener {

	private final static String version = "1.1.1.1";
	private final static String product = "Simple GUI for RISK";

	private JTextPane Console;
	private StyledDocument doc;
	private JTextField Command;
	private JButton Submit;
	private PicturePanel pp;

	private JLabel statusBar;
	private JLabel gameStatus;
	private JScrollPane Con;

	private Risk risk;
	private Vector history;
	private int pointer;
	private String temptext;

	private JPanel guiMain;
	private JPanel gp;
	private JLabel Pix;

	private JComboBox mapViewComboBox;

	/** Creates new form JFrame */

	/**
	 * Creates a new RiskGUI
	 * @param r The Risk object for this GUI
	 */
	public RiskGUI(Risk r) {

		risk=r;

// this get all the commands from the game and does what needs to be done
		RiskAdapter SimpleRiskAdapter = new RiskAdapter() {

			/**
			 * Checks if redrawing or repainting is needed
			 * @param output
			 * @param redrawNeeded If frame needs to be redrawn
			 * @param repaintNeeded If frame needs to be repainted
			 */
			public void sendMessage(String output, boolean redrawNeeded, boolean repaintNeeded) {

				Style style = doc.addStyle("StyleName", null);

				int c = risk.getCurrentPlayerColor();

				if (c != 0) {
					StyleConstants.setForeground(style, new Color(c).darker() );
				}
				else {
					StyleConstants.setForeground(style, Color.black);
				}
				//StyleConstants.setBold(style, true);

				try {
					//if (doc.getLength() > 12000) {
					//doc.remove(0,2000);
					//}

					doc.insertString(doc.getLength(), output + System.getProperty("line.separator") , style);

					Console.setCaretPosition(doc.getLength());
				}
				catch (Exception e) { }

				if (redrawNeeded) {
					pprepaintCountries();
				}
				if (repaintNeeded) {
					repaint();
				}

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

				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				try {
					pp.load();
				}
				catch (IOException e) {

				}

				mapViewComboBox.setSelectedIndex(0);

				guiMain.remove(Pix);
				guiMain.add(gp, java.awt.BorderLayout.CENTER );

				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

			}

			/**
			 * Closes the game
			 */
			public void closeGame() {

				guiMain.remove(gp);
				guiMain.add(Pix, java.awt.BorderLayout.CENTER );

			}

		};

		gameStatus = new JLabel("");

		risk.addRiskListener( SimpleRiskAdapter );
		history = new Vector();
		pointer=-1;

		Console = new JTextPane();
		doc = (StyledDocument)Console.getDocument();

		Command = new JTextField();
		Submit = new JButton();

                Pix = new JLabel();
                URL about = this.getClass().getResource("about.png");
                if (about!=null) {
                    Pix.setIcon( new javax.swing.ImageIcon( about ) );
                }
                else {
                    System.out.println("WARN: no about.png image found");
                }
		
		pp = new PicturePanel(risk);
		gp = new GamePanel();

		statusBar = new JLabel("Loading...");

		Con = new JScrollPane(Console);

		initGUI();

		setResizable(false);

		pack();

		statusBar.setText("Ready");
	}

	/** This method is called from within the constructor to initialize the form. */

	/**
	 * Initialises the GUI
	 */
	private void initGUI() {

		// set title
		setTitle(product);
		setIconImage(Toolkit.getDefaultToolkit().getImage( AboutDialog.class.getResource("icon.gif") ));

		getContentPane().setLayout(new java.awt.GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		c.insets = new java.awt.Insets(3, 3, 3, 3);

		Dimension ppSize = new Dimension(PicturePanel.PP_X,PicturePanel.PP_Y);

		pp.setPreferredSize(ppSize);
		pp.setMinimumSize(ppSize);
		pp.setMaximumSize(ppSize);

		pp.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0,0,0),1));

		pp.addMouseListener(this);
		pp.addMouseMotionListener(this);

		Console.setText("");
		// Console.setBackground(Color.white); // not needed with swing
		Console.setEditable(false);

		Con.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		Con.setPreferredSize(new java.awt.Dimension(PicturePanel.PP_X,100));
		Con.setMinimumSize(new java.awt.Dimension(PicturePanel.PP_X,100));

		Command.setPreferredSize(new java.awt.Dimension(600,20));
		Command.setMinimumSize(new java.awt.Dimension(600,20));
		Command.setMaximumSize(new java.awt.Dimension(600,20));

		Submit.setText("Submit");

		guiMain = new JPanel();

		Dimension guiMainSize = new Dimension(PicturePanel.PP_X, PicturePanel.PP_Y+30);
		guiMain.setPreferredSize(guiMainSize);
		guiMain.setMinimumSize(guiMainSize);
		guiMain.setMaximumSize(guiMainSize);

		guiMain.setLayout(new java.awt.BorderLayout());

		guiMain.add(Pix, java.awt.BorderLayout.CENTER );

		c.gridx = 0; // col
		c.gridy = 0; // row
		c.gridwidth = 2; // width
		c.gridheight = 2; // height

		getContentPane().add(guiMain, c); // Pix

		c.fill = GridBagConstraints.BOTH;

		c.gridx = 1; // col
		c.gridy = 3; // row
		c.gridwidth = 1; // width
		c.gridheight = 1; // height
		getContentPane().add(Submit, c);

		c.gridx = 0; // col
		c.gridy = 2; // row
		c.gridwidth = 2; // width
		c.gridheight = 1; // height
		getContentPane().add(Con, c);

		c.gridx = 0; // col
		c.gridy = 3; // row
		c.gridwidth = 1; // width
		c.gridheight = 1; // height
		getContentPane().add(Command, c);

		c.gridx = 0; // col
		c.gridy = 4; // row
		c.gridwidth = 1; // width
		c.gridheight = 1; // height
		// add status bar
		getContentPane().add(gameStatus, c);

		c.gridx = 1; // col
		c.gridy = 4; // row
		c.gridwidth = 1; // width
		c.gridheight = 1; // height
		// add status bar
		getContentPane().add(statusBar, c);

		ActionListener readCommand = new ActionListener() {
			public void actionPerformed(ActionEvent a) {

				String input = Command.getText();
				Command.setText("");

				history.add(input);
				pointer = history.size()-1;
				go(input);

			}
		};

		Submit.addActionListener( readCommand );
		Command.addActionListener( readCommand );


		class CommandKeyAdapter extends KeyAdapter {
			RiskGUI adaptee;


			CommandKeyAdapter(RiskGUI adaptee) {
				this.adaptee = adaptee;
			}
			public void keyPressed(KeyEvent key) {


				if (key.getKeyCode() == 38) {

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
		ClearConsole.setMnemonic('C');
		ClearConsole.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Console.setText("");
					}
				});
		menuClear.add(ClearConsole);

		JMenuItem ClearHistory = new JMenuItem("Clear History");
		ClearHistory.setMnemonic('H');
		ClearHistory.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						history.clear();
						pointer = -1;
					}
				});
		menuClear.add(ClearHistory);

		// create Open menu item

		JMenuItem openFile = new JMenuItem("Run Script");
		openFile.setMnemonic('R');
		openFile.addActionListener(
				new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {

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
					}
				});
		menuFile.add(openFile);

		// create Save menu item
		JMenuItem saveFile = new JMenuItem("Save Console");
		saveFile.setMnemonic('S');
		saveFile.addActionListener(
				new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {

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
					}
				});
		menuFile.add(saveFile);

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
						exitForm();
					}
				});

		// pack();

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
				JOptionPane.showMessageDialog(this,"Unable to open manual: "+e.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
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

	}

	/**
	 * This opens the about dialog box
	 */
	public void openAbout() {

		RiskUIUtil.openAbout(RiskGUI.this,product, version);

	}

	class playersPanel extends JPanel {

		public void paintComponent(Graphics g) {

			int[] colors = risk.getPlayerColors();

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

	class GamePanel extends JPanel {

		public GamePanel() {

			JPanel OptionsPanel = new JPanel();

			OptionsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 0));

			JLabel mapLookLabel = new JLabel("Map Look:");

			mapViewComboBox = new JComboBox();
			JButton closegame = new JButton("closegame");
			JButton about = new JButton("About");

			//JButton leave = new JButton("leave");

			Dimension mapViewSize = new Dimension(150 , 20);

			mapViewComboBox.setPreferredSize(mapViewSize);
			mapViewComboBox.setMinimumSize(mapViewSize);
			mapViewComboBox.setMaximumSize(mapViewSize);

			mapViewComboBox.addItem("Continents");
			mapViewComboBox.addItem("Ownership");
			mapViewComboBox.addItem("Border Threat");
			mapViewComboBox.addItem("Risk Card Ownership");
			mapViewComboBox.addItem("Troop Strength");
			mapViewComboBox.addItem("Connected Empire");

			mapViewComboBox.addActionListener(
					new ActionListener() {
						public void actionPerformed(ActionEvent a) {

							pprepaintCountries();
							pp.repaint();

						}
					}
			);

			JLabel playersLabel = new JLabel("Players:");

			Dimension playerPanelSize = new Dimension(120 , 20);

			JPanel players = new playersPanel();

			players.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0,0,0),1));

			players.setPreferredSize(playerPanelSize);
			players.setMinimumSize(playerPanelSize);
			players.setMaximumSize(playerPanelSize);

			closegame.addActionListener(
					new ActionListener() {
						public void actionPerformed(ActionEvent a) {
							go("closegame");
						}
					}
			);

			//leave.addActionListener(
			//		new ActionListener() {
			//			public void actionPerformed(ActionEvent a) {
			//				go("leave");
			//			}
			//		}
			//);

			about.addActionListener(
					new ActionListener() {
						public void actionPerformed(ActionEvent a) {
							openAbout();
						}
					}
			);

			Dimension size = new Dimension(PicturePanel.PP_X , 30);

			OptionsPanel.setPreferredSize(size);
			OptionsPanel.setMinimumSize(size);
			OptionsPanel.setMaximumSize(size);

			OptionsPanel.add(mapLookLabel);
			OptionsPanel.add(mapViewComboBox);
			OptionsPanel.add(playersLabel);
			OptionsPanel.add(players);
			OptionsPanel.add(closegame);
			OptionsPanel.add(about);
			//OptionsPanel.add(leave);

			this.setLayout(new java.awt.BorderLayout());

			this.add(OptionsPanel, java.awt.BorderLayout.NORTH );
			this.add(pp, java.awt.BorderLayout.CENTER);

		}
	}

	public void pprepaintCountries() {

		int tmp = mapViewComboBox.getSelectedIndex();
		int newview = -1;

        	switch (tmp) {
		    case 0: newview=PicturePanel.VIEW_CONTINENTS; break;
		    case 1: newview=PicturePanel.VIEW_OWNERSHIP; break;
		    case 2: newview=PicturePanel.VIEW_BORDER_THREAT; break;
		    case 3: newview=PicturePanel.VIEW_CARD_OWNERSHIP; break;
		    case 4: newview=PicturePanel.VIEW_TROOP_STRENGTH; break;
		    case 5: newview=PicturePanel.VIEW_CONNECTED_EMPIRE; break;
		}

		pp.repaintCountries( newview );

	}

	//**********************************************************************
	//                     MouseListener Interface
	//**********************************************************************

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

	/**
	 * This runs the program
	 * @param argv
	 */
	public static void main(String[] argv) {

                RiskUIUtil.parseArgs(argv);

		RiskGUI gui = new RiskGUI( new Risk() );

                RiskUIUtil.center(gui);
		gui.setVisible(true);

	}

}
