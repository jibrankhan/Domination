// Yura Mamyrin, Group D

package net.yura.domination.ui.flashgui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FocusTraversalPolicy;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ResourceBundle;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import net.yura.domination.engine.Risk;
import net.yura.domination.engine.RiskUIUtil;
import net.yura.domination.engine.RiskUtil;
import net.yura.domination.engine.core.Player;
import net.yura.domination.engine.core.RiskGame;
import net.yura.domination.engine.guishared.AboutDialog;
import net.yura.domination.engine.guishared.BadgeButton;
import net.yura.domination.engine.guishared.RiskFileFilter;
import net.yura.domination.engine.translation.TranslationBundle;

/**
 * <p> New Game Frame for FlashGUI </p>
 * @author Yura Mamyrin
 * @author Christian Weiske <cweiske@cweiske.de>
 */
public class NewGameFrame extends JFrame implements ActionListener,MouseListener,KeyListener {

	private BufferedImage newgame;
	private Risk myrisk;
	private boolean localgame;
	private JLabel mapPic;
	private JTextField cardsFile;
	private JPanel PlayersPanel;

	private JButton chooseMap;
	private JButton defaultMap;

	private JButton chooseCards;
	private JButton defaultCards;

	private JButton resetplayers;
	private JButton addplayer;

	private JButton start;
	private JButton help;
	private JButton cancel;

	private JRadioButton domination;
	private JRadioButton capital;
	private JRadioButton mission;

	private JRadioButton human;
	private JRadioButton ai;
	private JRadioButton aismart;
	//private JRadioButton aicrap;

	private JRadioButton fixed;
	private JRadioButton increasing;
	private JRadioButton italianLike;

	private JCheckBox AutoPlaceAll;
	private JCheckBox recycle;

	private JTextField playerName;
	private JToggleButton playerColor;

	private String color;
	private Color thecolor;

	private JPanel nothing;
	private JPanel colorChooser;
	private MyColor[] Colors;

	private ResourceBundle resb;

	/**
	 * the tab focus cycle list
	 * elements are traversed in this order
	 */
	Component[] arCycleList;

	/**
	 * at which position the "remove player" buttons in the focus cycle list begin
	 */
	private int nRemoveButtonPos = 8;



	/**
	 * The NewGameFrame Constructor
	 * @param r The Risk Parser used for playing the game
	 * @param t States whether this game is local
	 */
	public NewGameFrame(Risk r) {
		resb = TranslationBundle.getBundle();
		myrisk=r;
		newgame = RiskUIUtil.getUIImage(this.getClass(),"newgame.jpg");
		initGUI();
		setIconImage(Toolkit.getDefaultToolkit().getImage( AboutDialog.class.getResource("icon.gif") ));
		setResizable(false);
		pack();
		chooseCards.requestFocus();
	}

	/**
         * This method is called from within the constructor to initialize the form.
	 * Initialises the GUI
	 */
	private void initGUI() {

		this.setFocusTraversalPolicy( new NewGameFrameFocusTraversalPolicy());

		Colors = new MyColor[12];

		Colors[0] = new MyColor(Color.black	, "black"	, 385, 410, 25, 25, KeyEvent.VK_K);
		Colors[1] = new MyColor(Color.blue	, "blue"	, 385, 435, 25, 25, KeyEvent.VK_B);
		Colors[2] = new MyColor(Color.cyan	, "cyan"	, 385, 460, 25, 25, KeyEvent.VK_C);
		Colors[3] = new MyColor(Color.darkGray	, "darkgray"	, 410, 410, 25, 25, KeyEvent.VK_D);
		Colors[4] = new MyColor(Color.green	, "green"	, 410, 435, 25, 25, KeyEvent.VK_G);
		Colors[5] = new MyColor(Color.lightGray	, "lightgray"	, 410, 460, 25, 25, KeyEvent.VK_L);
		Colors[6] = new MyColor(Color.magenta	, "magenta"	, 435, 410, 25, 25, KeyEvent.VK_M);
		Colors[7] = new MyColor(Color.orange	, "orange"	, 435, 435, 25, 25, KeyEvent.VK_O);
		Colors[8] = new MyColor(Color.pink	, "pink"	, 435, 460, 25, 25, KeyEvent.VK_P);
		Colors[9] = new MyColor(Color.red	, "red"		, 460, 410, 25, 25, KeyEvent.VK_R);
		Colors[10] = new MyColor(Color.white	, "white"	, 460, 435, 25, 25, KeyEvent.VK_W);
		Colors[11] = new MyColor(Color.yellow	, "yellow"	, 460, 460, 25, 25, KeyEvent.VK_Y);


		Dimension d = new Dimension(700, 600);

		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setPreferredSize(d);
		layeredPane.setMinimumSize(d);
		layeredPane.setMaximumSize(d);

		JPanel ngp = new NewGamePanel();

		ngp.setLayout(null);
		ngp.setBounds(0,0, (int)d.getWidth() , (int)d.getHeight() );

		mapPic = new JLabel();
		mapPic.setBounds(51, 51, 203 , 127 );

		int w=93;
		int h=32;

		chooseMap = new BadgeButton(resb.getString("newgame.choosemap"));
		sortOutButton( chooseMap , newgame.getSubimage(54, 191, w, h) , newgame.getSubimage(700, 105, w, h) , newgame.getSubimage(700, 137, w, h) );
		chooseMap.addActionListener( this );
		chooseMap.setBounds(54, 192, w , h );

		defaultMap = new JButton(resb.getString("newgame.defaultmap"));
		sortOutButton( defaultMap , newgame.getSubimage(159, 192, w, h) , newgame.getSubimage(700, 169, w, h) , newgame.getSubimage(700, 201, w, h) );
		defaultMap.addActionListener( this );
		defaultMap.setBounds(159, 192, 93 , 32 );

		cardsFile = new JTextField("");
		cardsFile.setEditable(false);
		cardsFile.setBorder(null);
		cardsFile.setOpaque(false);
		cardsFile.setBounds(54, 260, 200 , 27 );

		chooseCards = new JButton(resb.getString("newgame.choosecards"));
		sortOutButton( chooseCards , newgame.getSubimage(54, 191, w, h) , newgame.getSubimage(700, 105, w, h) , newgame.getSubimage(700, 137, w, h) );
		chooseCards.addActionListener( this );
		chooseCards.setBounds(54, 301, 93 , 32 );

		defaultCards = new JButton(resb.getString("newgame.defaultcards"));
		sortOutButton( defaultCards , newgame.getSubimage(159, 192, w, h) , newgame.getSubimage(700, 169, w, h) , newgame.getSubimage(700, 201, w, h) );
		defaultCards.addActionListener( this );
		defaultCards.setBounds(159, 301, 93 , 32 );

		ButtonGroup GameTypeButtonGroup = new ButtonGroup();
		ButtonGroup CardTypeButtonGroup = new ButtonGroup();

		domination = new JRadioButton(resb.getString("newgame.mode.domination"), true);
		sortOutButton( domination );
		domination.setBounds(60, 370, 90 , 25 );
		domination.addActionListener(this);

		capital = new JRadioButton(resb.getString("newgame.mode.capital"));
		sortOutButton( capital );
		capital.setBounds(60, 390, 90 , 25 );
		capital.addActionListener(this);

		mission = new JRadioButton(resb.getString("newgame.mode.mission"));
		sortOutButton( mission );
		mission.setBounds(60, 410, 90 , 25 );
		mission.addActionListener(this);

                int col2w = 130;

		AutoPlaceAll = new JCheckBox(resb.getString("newgame.autoplace"));
		sortOutButton( AutoPlaceAll );
		AutoPlaceAll.setBounds(60, 440, 100 , 25 );
                AutoPlaceAll.setSelected( "true".equals(myrisk.getRiskConfig("default.autoplaceall")) );

		recycle = new JCheckBox(resb.getString("newgame.recycle"));
		sortOutButton( recycle );
		recycle.setBounds(160, 440, col2w , 25 );
                recycle.setSelected( "true".equals(myrisk.getRiskConfig("default.recyclecards")) );


		increasing = new JRadioButton(resb.getString("newgame.cardmode.increasing"),true);
		sortOutButton( increasing );
		increasing.setBounds(160,370,col2w,25);

		fixed = new JRadioButton(resb.getString("newgame.cardmode.fixed"));
		sortOutButton( fixed );
		fixed.setBounds(160,390,col2w,25);

                italianLike = new JRadioButton(resb.getString("newgame.cardmode.italianlike"));
		sortOutButton( italianLike );
		italianLike.setBounds(160,410,col2w,25);

		//AutoEndGo = new JCheckBox("Auto End Go");
		//sortOutButton( AutoEndGo );
		//AutoEndGo.setBounds(410, 530, 100 , 25 );
		//AutoEndGo.addActionListener( this );
		//AutoEndGo.setSelected( myrisk.getAutoEndGo() );

		GameTypeButtonGroup.add ( domination );
		GameTypeButtonGroup.add ( capital );
		GameTypeButtonGroup.add ( mission );

		CardTypeButtonGroup.add ( italianLike );
		CardTypeButtonGroup.add ( fixed );
		CardTypeButtonGroup.add ( increasing );

		PlayersPanel = new JPanel();
		PlayersPanel.setBounds(340, 51, 309 , 210 ); // this will allow 6 players, 30 pixels per player
		PlayersPanel.setOpaque(false);
		PlayersPanel.setLayout(new javax.swing.BoxLayout(PlayersPanel, javax.swing.BoxLayout.Y_AXIS));

		w=115;
		h=31;

		resetplayers = new JButton(resb.getString("newgame.resetplayers"));
		sortOutButton( resetplayers , newgame.getSubimage(705, 488, w, h) , newgame.getSubimage(700, 357, w, h) , newgame.getSubimage(700, 388, w, h) );
		resetplayers.setBounds(437, 268, 115 , 31 );
		resetplayers.addActionListener( this );

		playerName = new JTextField(resb.getString("newgame.newplayername")) {
			protected Document createDefaultModel() {
				return new LimitedDocument();
			}
		};

		playerName.setBorder(null);
		playerName.setOpaque(false);
		playerName.setBounds(403, 335, 97 , 25);

		ButtonGroup playerTypeButtonGroup = new ButtonGroup();

                int typeX = 520;
                int typeY = 330;
                int typeGap = 20;
                int typeW = 160;
                int typeH = 25;
                
		human = new JRadioButton(resb.getString("newgame.player.type.human"), true);
		sortOutButton( human );
		human.setBounds(typeX, typeY, typeW , typeH);

                //typeY = typeY + typeGap;
		//aicrap = new JRadioButton(resb.getString("newgame.player.type.crapai"));
		//sortOutButton( aicrap );
		//aicrap.setBounds(typeX, typeY, typeW , typeH);

                typeY = typeY + typeGap;
		ai = new JRadioButton(resb.getString("newgame.player.type.easyai"));
		sortOutButton( ai );
		ai.setBounds(typeX, typeY, typeW , typeH);

                typeY = typeY + typeGap;
		aismart = new JRadioButton(resb.getString("newgame.player.type.hardai"));
		sortOutButton( aismart );
		aismart.setBounds(typeX, typeY, typeW , typeH);



		playerTypeButtonGroup.add ( human );
		playerTypeButtonGroup.add ( ai );
		playerTypeButtonGroup.add ( aismart );
		//playerTypeButtonGroup.add ( aicrap );

		color = "black";
		thecolor = Color.black;

		playerColor = new JToggleButton("");
		sortOutButton( playerColor , newgame.getSubimage(793, 105, 19, 19) , newgame.getSubimage(793, 125, 19, 19) , newgame.getSubimage(793, 145, 19, 19) );
		playerColor.addActionListener( this );
		playerColor.setBounds(475, 370, 25 , 25);

		addplayer = new JButton(resb.getString("newgame.addplayer"));
		sortOutButton( addplayer , newgame.getSubimage(437, 413, w, h) , newgame.getSubimage(700, 419, w, h) , newgame.getSubimage(700, 450, w, h) );
		addplayer.addActionListener( this );
		addplayer.setBounds(437, 413, 115 , 31 );

		cancel = new JButton(resb.getString("newgame.cancel"));
		sortOutButton( cancel , newgame.getSubimage(41, 528, w, h) , newgame.getSubimage(700, 233, w, h) , newgame.getSubimage(700, 264, w, h) );
		cancel.addActionListener( this );
		cancel.setBounds(41, 528, 115 , 31 );

		help = new JButton(); // 335 528
		sortOutButton( help , newgame.getSubimage(781, 526, 30 , 30) , newgame.getSubimage(794, 171, 30 , 30) , newgame.getSubimage(794, 202, 30 , 30) );
		help.addActionListener( this );
		help.setBounds(335, 529, 30 , 30 ); // should be 528

		start = new JButton(resb.getString("newgame.startgame"));
		sortOutButton( start , newgame.getSubimage(544, 528, w, h) , newgame.getSubimage(700, 295, w, h) , newgame.getSubimage(700, 326, w, h) );
		start.addActionListener( this );
		start.setBounds(544, 528, 115 , 31 );

		ngp.add(mapPic);
		ngp.add(chooseMap);
		ngp.add(defaultMap);

		ngp.add(cardsFile);
		ngp.add(chooseCards);
		ngp.add(defaultCards);

		ngp.add(domination);
		ngp.add(capital);
		ngp.add(mission);

		ngp.add(italianLike);
		ngp.add(fixed);
		ngp.add(increasing);

		ngp.add(PlayersPanel);

		ngp.add(playerName);
		ngp.add(human);
		ngp.add(ai);
		ngp.add(aismart);
		//ngp.add(aicrap);
		ngp.add(playerColor);

		ngp.add(resetplayers);
		ngp.add(addplayer);

		ngp.add(AutoPlaceAll);
		ngp.add(recycle);

		ngp.add(cancel);
		ngp.add(help);
		ngp.add(start);

		colorChooser = new colorChooserPanel();
		colorChooser.setBounds(0,0, (int)d.getWidth() , (int)d.getHeight() );
		colorChooser.addMouseListener(this);
		colorChooser.setOpaque(false);
		colorChooser.setVisible(false);
		colorChooser.addKeyListener( this);
		playerColor.addKeyListener( this);


		nothing = new JPanel();
		nothing.setBounds(0,0, (int)d.getWidth() , (int)d.getHeight() );
		nothing.addMouseListener(this);
		nothing.setOpaque(false);
		nothing.setVisible(false);

		layeredPane.add(nothing, 0);
		layeredPane.add(colorChooser, 1);
		layeredPane.add(ngp, 2);

		setContentPane(layeredPane);

		addWindowListener(
				new java.awt.event.WindowAdapter() {
					public void windowClosing(java.awt.event.WindowEvent evt) {
						exitForm();
					}
				}
		);


		//accelerators
		start.setDefaultCapable( true);
		human		.setMnemonic( 'u');
		ai			.setMnemonic( 'e');
		aismart		.setMnemonic( 'h');
		domination	.setMnemonic( 'd');
		capital		.setMnemonic( 'c');
		mission		.setMnemonic( 'm');
		addplayer	.setMnemonic( 'a');
		resetplayers.setMnemonic( 'r');
		AutoPlaceAll.setMnemonic( 'p');
		start		.setMnemonic( 's');

		/**
		 * set up the cycle list
		 */
		arCycleList = new Component[]{
			chooseMap,
			defaultMap,
			chooseCards,
			defaultCards,

			domination,
			capital,
			mission,

			AutoPlaceAll,

			//remove player buttons
			null,//1 - pos 8 UPDATE THE POSITION IF ADDING MORE BUTTONS TO THE CYCLE LIST
			null,
			null,
			null,//4
			null,
			null,//6

			resetplayers,
			playerName,
			playerColor,
			human,
			ai,
			aismart,
			addplayer,

			cancel,
			help,
			start
		};
		nRemoveButtonPos = 8;
	}//private void initGUI()

	public void setup(boolean t) {

		localgame=t;

		// set title
		if (localgame) {
			setTitle(resb.getString("newgame.title.local"));
			resetplayers.setVisible(true);
		}
		else {
			setTitle(resb.getString("newgame.title.network"));
			resetplayers.setVisible(false);
		}

		Component[] players = PlayersPanel.getComponents();

		for (int c=0; c< players.length ; c++) {

			PlayersPanel.remove(players[c]);

		}

		nothing.setVisible(false);

		if (t) {
                    RiskUtil.loadPlayers(myrisk,getClass());
                }

	}

	static class LimitedDocument extends PlainDocument {

		public void insertString(int offs, String str, AttributeSet a) throws javax.swing.text.BadLocationException {

			if (str == null) {
				return;
			}

			if ( (getLength() + str.length()) > 15 ) {

				str = str.substring(0, str.length() - ((getLength() + str.length())-15) );
				Toolkit.getDefaultToolkit().beep();

			}

                        super.insertString(offs, str, a);
		}
	}

	/**
	 * Sets the game map
	 * @param a The ImageIcon where the map is stored
	 */
	public void setMap(ImageIcon a) {

		mapPic.setIcon(a);

		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

	}

	/**
	 * Sets the cards file
	 * @param c The name of the cards file
	 */
	public void setCards(String c, boolean m) {
		cardsFile.setText(c);

		if ( m==false && mission.isSelected() ) { domination.setSelected(true); AutoPlaceAll.setEnabled(true); }

		mission.setEnabled(m);

	}

	/**
	 * Adds a player to the game
	 * @param type Indicates whether the player is human or AI
	 * @param name Name of player
	 * @param color Color of the player
	 * @param ip Holds the ip address of the player
	 */
	public void addPlayer(final int type,final String name,final Color color,final String ip) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
		    public void run() {

			final playerPanel player = new playerPanel(type, name, color, ip);

			PlayersPanel.add(player);
			PlayersPanel.validate();
			PlayersPanel.repaint();

			//let it cycle
			arCycleList[nRemoveButtonPos + PlayersPanel.getComponents().length - 1] = player.getRemoveButton();

		    }
		});
	}

	/**
	 * Removes the given player from the game
	 * @param name Name of the player to be deleted
	 */
	public void delPlayer(final String name) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
		    public void run() {

			Component[] players = PlayersPanel.getComponents();

			for (int c=0; c < players.length ; c++) {

				if ( ((playerPanel)players[c]).getName().equals(name) ) {

					PlayersPanel.remove(c);
					PlayersPanel.validate();
					PlayersPanel.repaint();

					int nCyclePos = nRemoveButtonPos + c;

					//remove it from the cycle list
					arCycleList[nCyclePos] = null;
					//and now move the playerpanels above one down

					for (int d = nCyclePos + 1; d < nRemoveButtonPos + 6; d++) {
						arCycleList[d - 1] = arCycleList[d];
					}
					//last has to be null
					arCycleList[nRemoveButtonPos + 5] = null;

					break;
				}

			}


		    }
		});

	}


	/** Exit the Application */

	/**
	 * Closes the GUI
	 * @param evt Close button was pressed
	 */
	private void exitForm() {

		//if (localgame) {
			myrisk.parser("closegame");
		//}
		//else {
		//	myrisk.parser("leave");
		//}

	}

	class playerPanel extends JPanel {

		private int type;
		private String name;
		private Color color;
		private String ip;
		private JButton remove;



		/**
		 * Creates the panel where the player details are shown
		 * @param t Holds the type of player, human or AI
		 * @param n Name
		 * @param c Color
		 * @param i IP address
		 */
		public playerPanel(int t, String n, Color c, String i) {

			type=t;
			name=n;
			color=c;
			ip=i;

			Dimension d = new Dimension(309, 30);

			this.setPreferredSize(d);
			this.setMinimumSize(d);
			this.setMaximumSize(d);
			// this.setBorder(javax.swing.BorderFactory.createLineBorder( color , 1));
			this.setLayout(null);

			int w=80;
			int h=25;

			remove = new JButton(resb.getString("newgame.removeplayer"));

			BufferedImage remove1 = new BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_INT_RGB );
			BufferedImage remove2 = new BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_INT_RGB );
			BufferedImage remove3 = new BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_INT_RGB );

			Graphics g1 = remove1.getGraphics();
			g1.drawImage( newgame.getSubimage(700, 525, w, h) ,0 ,0, this);
			g1.setColor( new Color(c.getRed(),c.getGreen(), c.getBlue(), 50) );
			g1.fillRect(0,0,w,h);
			g1.dispose();

			Graphics g2 = remove2.getGraphics();
			g2.drawImage( newgame.getSubimage(700, 550, w, h) ,0 ,0, this);
			g2.setColor( new Color(c.getRed(),c.getGreen(), c.getBlue(), 50) );
			g2.fillRect(0,0,w,h);
			g2.dispose();

			Graphics g3 = remove3.getGraphics();
			g3.drawImage( newgame.getSubimage(700, 575, w, h) ,0 ,0, this);
			g3.setColor( new Color(c.getRed(),c.getGreen(), c.getBlue(), 50) );
			g3.fillRect(0,0,w,h);
			g3.dispose();

			sortOutButton( remove , remove1 , remove2 , remove3 );
			remove.setBounds(226, 3, 80 , 25 );

			remove.addActionListener(
					new ActionListener() {
						public void actionPerformed(ActionEvent a) {

							myrisk.parser("delplayer " + name);

						}
					}
			);

			add(remove);

		}

		/**
		 * Paints a graphic
		 * @param g Graphics Component
		 */
		public void paintComponent(Graphics g) {

			g.setColor( new Color(color.getRed(), color.getGreen(), color.getBlue(), 125) );

			g.fillRect(0, 0, 309, 30);

			g.setColor( RiskUIUtil.getTextColorFor(color) );


			g.drawString( name, 10, 20);


			if (type == Player.PLAYER_HUMAN) {
				g.drawString( resb.getString("newgame.player.type.human"), 120, 20);
			}
			else if (type == Player.PLAYER_AI_EASY) {
				g.drawString( resb.getString("newgame.player.type.easyai"), 120, 20);
			}
			else if (type == Player.PLAYER_AI_HARD) {
				g.drawString( resb.getString("newgame.player.type.hardai"), 120, 20);
			}
			else {
				g.drawString( resb.getString("newgame.player.type.crapai"), 120, 20);
			}

			//if (localgame) { g.drawString( resb.getString("newgame.type.local"), 140, 20); }
			//else { g.drawString( ip, 140, 20); }

		}

		/**
		 * Returns name
		 * @return String
		 */
		public String getName() {
			return name;
		}

		/**
		 * Returns the type of player
		 * @return String
		 */
		public int getType() {
			return type;
		}

		/**
		 * returns the "remove player" button
		 */
		public JButton getRemoveButton() {
			return this.remove;
		}


	}

	class NewGamePanel extends JPanel {

		/**
		 * Paints a graphic
		 * @param g Graphics Component
		 */
		public void paintComponent(Graphics g) {

			((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

//			  destination		source
			g.drawImage(newgame,0,0,700,600,     0,0,700,600,this);

			if (localgame) {

				g.drawImage(newgame,432,262,557,305,     700,482,825,525,this);

			}

			g.setColor( thecolor );
			g.fillRect(400, 370, 100, 25);

			g.setColor( Color.black );

			g.drawString( resb.getString("newgame.label.map"), 55, 40);
			g.drawString( resb.getString("newgame.label.players"), 350, 40);
			g.drawString( resb.getString("newgame.label.cards"), 55, 250);
			g.drawString( resb.getString("newgame.label.gametype"), 70, 365);
			g.drawString( resb.getString("newgame.label.cardsoptions"), 170, 365);
			g.drawString( resb.getString("newgame.label.name"), 400, 325);
			g.drawString( resb.getString("newgame.label.type"), 520, 325);

			g.setColor( RiskUIUtil.getTextColorFor( thecolor ) );

			g.drawString( resb.getString("newgame.label.color"), 410, 387);

		}

	}

	class colorChooserPanel extends JPanel
	{

		/**
		 * Paints a graphic
		 * @param g Graphics Component
		 */
		public void paintComponent(Graphics g) {

			Graphics2D g2 = (Graphics2D)g;

			AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f);
			g2.setComposite(ac);

//			  destination		source
			g.drawImage(newgame,370,395,500,500,     700,0,830,105,this);

			for (int c=0; c< Colors.length ; c++) {

				g.setColor( Colors[c].getColor() );
				g.fillRect(Colors[c].getX(), Colors[c].getY(), Colors[c].getWidth(), Colors[c].getHeight());

			}

		}

	}//class colorChooserPanel extends JPanel


	/**
	 * Actionlistener applies the correct command to the button pressed
	 * @param e The ActionEvent Object
	 */
	public void actionPerformed(ActionEvent e) {

		if (e.getSource()==chooseMap) {

			String name = RiskUIUtil.getNewMap(this);

			if (name != null) {

				myrisk.parser("choosemap " + name );

				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			}
		}
		else if (e.getSource()==defaultMap) {

			myrisk.parser("choosemap " + RiskGame.getDefaultMap() );

		}
		else if (e.getSource()==chooseCards) {

			String name = RiskUIUtil.getNewFile( this, RiskFileFilter.RISK_CARDS_FILES);

			if (name != null) {

				myrisk.parser("choosecards " + name );

			}

		}
		else if (e.getSource()==defaultCards) {

			myrisk.parser("choosecards " + RiskGame.getDefaultCards() );

		}
		else if (e.getSource()==resetplayers) {

			Component[] players = PlayersPanel.getComponents();

			for (int c=0; c< players.length ; c++) {

				myrisk.parser("delplayer " + ((playerPanel)players[c]).getName() );

			}

			resetPlayers();

		}

		else if (e.getSource()==addplayer) {

			String type="";

			if (human.isSelected())	{ type = "human"; }
			else if (ai.isSelected())	{ type = "ai easy"; }
			else if (aismart.isSelected())	{ type = "ai hard"; }
			else			{ type = "ai crap"; }

			myrisk.parser("newplayer "+ type +" "+ color +" "+ playerName.getText() );

		}
		else if (e.getSource()==start) {

			Component[] players = PlayersPanel.getComponents();

			if (
					(players.length >= 2 && players.length <= RiskGame.MAX_PLAYERS )
					// || (players.length == 2 && domination.isSelected() && ((playerPanel)players[0]).getType() == 0 && ((playerPanel)players[1]).getType() == 0 )
			) {

                                if (localgame) {
                                    RiskUtil.savePlayers(myrisk,getClass());
                                }

				String type="";
				if (domination.isSelected()) type = "domination";
				else if (capital.isSelected()) type = "capital";
				else if (mission.isSelected()) type = "mission";

				if (increasing.isSelected()) type += " increasing";
				else if (fixed.isSelected()) type += " fixed";
				else if (italianLike.isSelected()) type += " italianlike";

				myrisk.parser("startgame " + type + (( AutoPlaceAll.isSelected() )?(" autoplaceall"):("")) + (( recycle.isSelected() )?(" recycle"):("")) );

			}
			else {

				JOptionPane.showMessageDialog(this, resb.getString("newgame.error.numberofplayers") , resb.getString("newgame.error.title"), JOptionPane.ERROR_MESSAGE );

			}

		}
		else if (e.getSource()==help) {

			try {
				RiskUtil.openDocs( resb.getString("helpfiles.flash") );
			}
			catch(Exception er) {
				JOptionPane.showMessageDialog(this,"Unable to open manual: "+er.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
			}

		}
		else if (e.getSource()==cancel) {


			exitForm();


		}
		else if (e.getSource()==playerColor) {
			colorChooser.setVisible(playerColor.isSelected());

		}
		else if (e.getSource()==mission) {

			AutoPlaceAll.setEnabled(false);

		}
		else if (e.getSource()==domination) {

			AutoPlaceAll.setEnabled(true);

		}
		else if (e.getSource()==capital) {

			AutoPlaceAll.setEnabled(true);

		}
	}

	/**
	 * Applies the correct command to the button pressed
	 * @param e The MouseEvent Object
	 */
	public void mouseClicked(MouseEvent e) {

		if (e.getSource()==colorChooser) {

			for (int c=0; c< Colors.length ; c++) {

				if (e.getX() >= Colors[c].getX() && e.getX() < Colors[c].getX()+Colors[c].getWidth() && e.getY() >= Colors[c].getY() && e.getY() < Colors[c].getY()+Colors[c].getHeight() ) {
					setSelectedPlayerColor( Colors[c]);
					break;
				}
			}

			colorChooser.setVisible(false);//this line is absolutely required
			playerColor.setSelected(false);
		}
	}

	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}


	/**
	 * the user has selected a player color on the player color panel
	 */
	protected void setSelectedPlayerColor(MyColor col)
	{
		color		= col.getName();
		thecolor	= col.getColor();
	}//protected void setSelectedPlayerColor(MyColor col)



	/**
	 * Resets the players
	 */
	public void resetPlayers()
	{

		myrisk.parser("autosetup");
/*
		String strUsername = "appletuser";

		if (Risk.applet == null) {

			strUsername = System.getProperty("user.name");
		}

		myrisk.parser("newplayer human   green   " + strUsername);
		myrisk.parser("newplayer ai easy blue    bob");
		myrisk.parser("newplayer ai easy red     fred");
		myrisk.parser("newplayer ai easy yellow  ted");
		myrisk.parser("newplayer ai hard magenta yura");
		myrisk.parser("newplayer ai hard cyan    lala");
*/
	}//public void resetPlayers()

	class MyColor {

		private Color color;
		private String name;
		private int myX;
		private int myY;
		private int myW;
		private int myH;
		private int keyCode;

		/**
		 * Sets the players details accorind to color
		 * @param c The color
		 * @param n The name
		 * @param x The x-coordinate
		 * @param y The y-coordinate
		 * @param w The width
		 * @param h The height
		 */
		public MyColor(Color c, String n, int x, int y, int w, int h, int keyCode) {

			color=c;
			name=n;
			myX=x;
			myY=y;
			myW=w;
			myH=h;
			this.keyCode = keyCode;

		}

		/**
		 * Returns the color
		 * @return Color
		 */
		public Color getColor() {
			return color;
		}

		/**
		 * Returns the name
		 * @return String
		 */
		public String getName() {
			return name;
		}

		/**
		 * Returns the x-coordinate
		 * @return int
		 */
		public int getX() {
			return myX;
		}

		/**
		 * Returns the y-coordinate
		 * @return int
		 */
		public int getY() {
			return myY;
		}

		/**
		 * Returns the width
		 * @return int
		 */
		public int getWidth() {
			return myW;
		}

		/**
		 * Returns the height
		 * @return int
		 */
		public int getHeight() {
			return myH;
		}

		/**
		 * returns the key code for this color
		 */
		public int getKeyCode() {
			return this.keyCode;
		}

	}

	/**
	 * Block the gamepanel
	 */
	public void noInput() {

		nothing.setVisible(true);
		//System.out.print("BLOCK INPUT\n");

	}

	/**
	 * Sorts the buttons out and adds rollover images to the button
	 * @param button Button
	 * @param button1 Image
	 * @param button2 Image
	 * @param button3 Image
	 */
	public static void sortOutButton(AbstractButton button, Image button1, Image button2, Image button3) {

		button.setIcon( new ImageIcon( button1 ) );
                button.setRolloverIcon( new ImageIcon( button2 ) );
                Icon down = new ImageIcon( button3 );
                button.setRolloverSelectedIcon( down );
                button.setSelectedIcon( down );
                button.setPressedIcon( down );
                
		button.setMargin(new Insets(0,0,0,0));
		button.setBorderPainted(false);
		button.setFocusPainted(false);
		button.setContentAreaFilled(false);

		button.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

		button.addFocusListener( new ImageButtonFocusListener(button));
	}

	/**
	 * Makes the button look neat
	 * @param button Button
	 */
	public static void sortOutButton(AbstractButton button) {

		button.setMargin(new Insets(0,0,0,0));
		button.setBorderPainted(false);
//		button.setFocusPainted(false);
		button.setContentAreaFilled(false);

		button.addFocusListener( new ImageButtonFocusListener(button));
	}


	/**
	 * That the user can tab through the tabs in the right order
	 * @author Christian Weiske <cweiske@cweiske.de>
	 */
	class NewGameFrameFocusTraversalPolicy extends FocusTraversalPolicy
	{

		public Component getComponentAfter( Container container, Component component )
		{
			int nPos = getComponentIndex( component) + 1;
			if (nPos >= arCycleList.length) {
				nPos = 0;
			}

			//remove buttons could be null, so ignore them
			while (arCycleList[nPos] == null && nPos < arCycleList.length) {
				nPos++;
			}

			return arCycleList[nPos];
		}

		public Component getComponentBefore( Container container, Component component )
		{
			int nPos = getComponentIndex( component) - 1;
			if (nPos < 0) {
				nPos = arCycleList.length - 1;
			}

			//remove buttons could be null, so ignore them
			while (arCycleList[nPos] == null && nPos > 0) {
				nPos--;
			}

			return arCycleList[nPos];
		}

		/**
		 * returns the index of the component in the cycle array
		 */
		private int getComponentIndex(Component comp)
		{
			for( int nA = 0; nA < arCycleList.length; nA++ ) {
				if (arCycleList[nA] == comp) {
					return nA;
				}
			}
			return -1;
		}//private int getComponentIndex(Component comp)

		public Component getFirstComponent( Container container )
		{
			return arCycleList[0];
		}

		public Component getLastComponent( Container container )
		{
			return arCycleList[arCycleList.length - 1];
		}

		public Component getDefaultComponent( Container container )
		{
			return arCycleList[0];
		}
	}//class NewGameFrameFocusTraversalPolicy extends FocusTraversalPolicy



	/**
	 * the user has released a key on the colorchooser panel
	 */
	public void keyReleased( KeyEvent event )
	{
		if (event.getSource() != colorChooser
			&& event.getSource() != playerColor) {
			return;
		}

		for( int nA = 0; nA < Colors.length; nA++ ) {
			if (Colors[nA].getKeyCode() == event.getKeyCode()) {
				this.colorChooser.setVisible( true);
				this.setSelectedPlayerColor( Colors[nA]);
				this.colorChooser.setVisible( false);
				this.playerColor.setSelected( false);
				break;
			}
		}
	}//public void keyReleased( KeyEvent event )


	public void keyTyped( KeyEvent event ) {}
	public void keyPressed( KeyEvent event ){}

}//public class NewGameFrame extends JFrame implements ActionListener,MouseListener,KeyListener



/**
 * Makes the button highlighted when they have focus
 * and resets the image if they loose it
 */
class ImageButtonFocusListener implements FocusListener {
	AbstractButton button;
        Icon defaultIcon;
	ImageButtonFocusListener(AbstractButton button) {
		this.button = button;
	}
	public void focusGained( FocusEvent event ) {
		if (button.getIcon() == null) {
			//no icon, so change the bg color
			//button.setBackground( Color.WHITE );
			//button.setContentAreaFilled( true);
		}
                else {
                        defaultIcon = button.getIcon();
			button.setIcon( button.getRolloverIcon() );
		}
	}

	public void focusLost( FocusEvent event ) {
		//this works only because we set the selected icon
		//to the same as the normal icon
		if (button.getSelectedIcon() == null) {
			//no icon, so reset bg color
			//button.setBackground( null);
			//button.setContentAreaFilled( false);
		}
                else {
			button.setIcon( defaultIcon );
		}
	}
}//class ImageButtonFocusListener


