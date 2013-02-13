// Yura Mamyrin, Group D

package net.yura.domination.ui.flashgui;

import javax.swing.JFrame;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.Toolkit;
import java.awt.Dimension;
import javax.swing.JLayeredPane;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.Insets;
import java.awt.font.TextLayout;
import java.awt.font.FontRenderContext;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.event.MouseInputAdapter;
import net.yura.domination.engine.ColorUtil;
import net.yura.domination.engine.Risk;
import net.yura.domination.engine.RiskUIUtil;
import net.yura.domination.engine.RiskUtil;
import net.yura.domination.engine.core.Country;
import net.yura.domination.engine.core.RiskGame;
import net.yura.domination.engine.guishared.AboutDialog;
import net.yura.domination.engine.guishared.MapMouseListener;
import net.yura.domination.engine.guishared.PicturePanel;
import net.yura.domination.engine.translation.TranslationBundle;

/**
 * Game Frame for FlashGUI
 * @author Yura Mamyrin
 */
public class GameFrame extends JFrame implements KeyListener {

        public static final Color UI_COLOR = Color.RED;
    
	private BufferedImage gameImg;
	private Risk myrisk;
	private PicturePanel pp;
	private GameMenuPanel gm;
	private int mapView;
	private String gameStatus;
	private boolean localGame;
	private int gameState;
	private String note;

	private JButton cardsbutton;
	private JButton missionbutton;
	private JButton undobutton;
	private JButton menubutton;
	private JButton graphbutton;
	private JButton gobutton;

	private java.util.ResourceBundle resb;
	private StatsDialog graphdialog;

	private boolean menuOn;
	private boolean graphOn;

	private int[] colors;
	private CardsDialog cardsDialog;

	//private int c1Id;
	private MoveDialog movedialog;


	private JButton savebutton;
	private JButton resumebutton;

	private JCheckBox AutoEndGo;
	private JCheckBox AutoDefend;

	private JButton helpbutton;
	private JButton closebutton;

        private MouseInputAdapter mapListener;

	public GameFrame(Risk r, PicturePanel p) {

		//setResizable(false);

		//setGameStatus(null); // not needed?

		myrisk=r;
		pp=p;

                final MapMouseListener mml = new MapMouseListener(myrisk,pp);
                mapListener = new MouseInputAdapter() {
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

		menuOn=false;
		graphOn=false;

		gameImg = RiskUIUtil.getUIImage(this.getClass(),"game.jpg");

		initGUI();

		setIconImage(Toolkit.getDefaultToolkit().getImage( AboutDialog.class.getResource("icon.gif") ));

		pack();

		graphdialog = null;

		try {

			setMinimumSize( getPreferredSize() );

		}
		catch(NoSuchMethodError ex) {

			// must me java 1.4
			setResizable(false);

		}

	}

	/** This method is called from within the constructor to initialize the form. */

	/**
	 * Initialises the GUI
	 */
	private void initGUI() {

		resb = TranslationBundle.getBundle();

		// set title
		setTitle("yura.net " + RiskUtil.GAME_NAME ); // resb.getString("game.title")

		//JLayeredPane layeredPane = new JLayeredPane();
		//layeredPane.setPreferredSize(d);
		//layeredPane.setMinimumSize(d);
		//layeredPane.setMaximumSize(d);

		int l=715,m=6;

		final BufferedImage topleft=gameImg.getSubimage(63,0,l,54);
		final BufferedImage topmiddle=gameImg.getSubimage(63+l,0,m,54);
		final BufferedImage topright=gameImg.getSubimage(63+l+m,0,740-(l+m),54);

		JPanel fp = new JPanel() {
		    public void paintComponent(Graphics g) {

			//		  destination		source
			//g.drawImage(game,0,0,740,54,     63,0,803,54,this); // top

			g.drawImage(topleft,0,0,this);
			for (int c=topleft.getWidth();c<getWidth()-topright.getWidth();c=c+topmiddle.getWidth() ) {
				g.drawImage(topmiddle,c,0,this);
			}
			g.drawImage(topright,getWidth()-topright.getWidth(),0,this);

			Graphics2D g2 = (Graphics2D)g;

			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			FontRenderContext frc = g2.getFontRenderContext();
			Font font = g2.getFont();
			g2.setColor( Color.BLACK );
			TextLayout tl;

			tl = new TextLayout( resb.getString("game.tabs.continents") , font, frc);
			tl.draw( g2, (float) (81-tl.getBounds().getWidth()/2), (float)26 );

			tl = new TextLayout( resb.getString("game.tabs.ownership") , font, frc);
			tl.draw( g2, (float) (196-tl.getBounds().getWidth()/2), (float)26 );

			tl = new TextLayout( resb.getString("game.tabs.borderthreat") , font, frc);
			tl.draw( g2, (float) (311-tl.getBounds().getWidth()/2), (float)26 );

			tl = new TextLayout( resb.getString("game.tabs.cardownership") , font, frc);
			tl.draw( g2, (float) (426-tl.getBounds().getWidth()/2), (float)26 );

			tl = new TextLayout( resb.getString("game.tabs.troopstrength") , font, frc);
			tl.draw( g2, (float) (541-tl.getBounds().getWidth()/2), (float)26 );

			tl = new TextLayout( resb.getString("game.tabs.connectedempire") , font, frc);
			tl.draw( g2, (float) (656-tl.getBounds().getWidth()/2), (float)26 );



			if (mapView==PicturePanel.VIEW_CONTINENTS) {
				g.drawImage(gameImg,24,32,139,39,   64,383,179,390,this);
			}
			else if (mapView==PicturePanel.VIEW_OWNERSHIP) {
				g.drawImage(gameImg,139,32,254,39,     64,390,179,397,this);
			}
			else if (mapView==PicturePanel.VIEW_BORDER_THREAT) {
				g.drawImage(gameImg,254,32,369,39,     64,397,179,404,this);
			}
			else if (mapView==PicturePanel.VIEW_CARD_OWNERSHIP) {
				g.drawImage(gameImg,369,32,484,39,     64,404,179,411,this);
			}
			else if (mapView==PicturePanel.VIEW_TROOP_STRENGTH) {
				g.drawImage(gameImg,484,32,599,39,     64,411,179,418,this);
			}
			else if (mapView==PicturePanel.VIEW_CONNECTED_EMPIRE) {
				g.drawImage(gameImg,599,32,714,39,     64,418,179,425,this);
			}

			g.drawLine(30,53,getWidth()-32,53);

		    }
		};
		//fp.setBounds(0,0, (int)d.getWidth() , (int)d.getHeight() );
		fp.addMouseListener( new MouseInputAdapter() {
                    public void mouseReleased(MouseEvent e) {
                	int click=insideButton(e.getX(),e.getY());
			if (click != -1) { // this means it was one of the view buttons
				if (mapView !=click) {
					setMapView(click);
				}
			}
                    }
                } );
		//fp.addMouseMotionListener(this);


		l=551;
		m=6;

		final BufferedImage bottomleft=gameImg.getSubimage(63,54,l,121);
		final BufferedImage bottommiddle=gameImg.getSubimage(63+l,54,m,121);
		final BufferedImage bottomright=gameImg.getSubimage(63+l+m,54,740-(l+m),121);

		JPanel fpBottom = new JPanel() {
		    public void paintComponent(Graphics g) {

			//g.drawImage(game,0,0,740,121,  63,54,803,175,this); // bottom

			g.drawImage(bottomleft,0,0,this);
			for (int c=bottomleft.getWidth();c<getWidth()-bottomright.getWidth();c=c+bottommiddle.getWidth() ) {
				g.drawImage(bottommiddle,c,0,this);
			}
			g.drawImage(bottomright,getWidth()-bottomright.getWidth(),0,this);

			Graphics2D g2 = (Graphics2D)g;

			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


			int[] cols = colors;

			for (int c=0; c<cols.length; c++) {

				Color col = new Color( cols[c] );

				g.setColor( new Color(col.getRed(),col.getGreen(),col.getBlue(), 100) );

				if (c==0) {
					g.fillArc(8, 89, 24 , 24, 90, 180);
					g.fillRect( 20 , 89 , (getWidth()-173)-(24*(cols.length-c)) , 24);
				}
				else {
					g.fillRect( (getWidth()-177)-(24*(cols.length-c)) , 89 , 24 , 24);
				}

			}

			if (gameStatus!=null) {

				g.setColor( new Color( ColorUtil.getTextColorFor( cols[0] ) ) );
				g.setFont( new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 11) );
				g.drawString(gameStatus, 22, 105);
			}

			g2.setColor( Color.BLACK );

			TextLayout tl=null;
			FontRenderContext frc = g2.getFontRenderContext();
			Font font = g2.getFont();

			if ( gameState==RiskGame.STATE_NEW_GAME ) {
				tl = new TextLayout( resb.getString("game.pleasewait") , font, frc);
			}
			else if ( (gameState==RiskGame.STATE_TRADE_CARDS || gameState==RiskGame.STATE_PLACE_ARMIES || gameState==RiskGame.STATE_ATTACKING || gameState==RiskGame.STATE_SELECT_CAPITAL || gameState==RiskGame.STATE_FORTIFYING ) && !(note.equals("")) ) {
				tl = new TextLayout( note , font, frc);
			}

			if (tl!=null) {
				tl.draw( g2, (float) (getWidth()-84-tl.getBounds().getWidth()/2), (float)57 );
			}


			g.drawLine(30,0,getWidth()-32,0);

		    }

		    public void setBounds(int x,int y,int w,int h) {
			super.setBounds(x,y,w,h);
			gobutton.setBounds(getWidth()-140, 74, 115 , 31 );
		    }
		};
		fpBottom.setLayout(null);

		JPanel fpLeft = new JPanel() {
		    public void paintComponent(Graphics g) {
			// getHeight() = 425
			g.drawImage(gameImg,0,0,31,getHeight(),    0,0,31,425,this); // left
			g.setColor(Color.BLACK);
			g.drawLine(30,0,30,getHeight());
		    }
		};

		JPanel fpRight = new JPanel() {
		    public void paintComponent(Graphics g) {
			// getHeight() = 425
			g.drawImage(gameImg,0,0,32,getHeight(), 31,0,63,425,this); // right
			g.setColor(Color.BLACK);
			g.drawLine(0,0,0,getHeight());
		    }
		};

		int w=114;
		int h=66;
		// normal - pressed - rollover - disabled
		int x=63;
		int y=77;

		graphbutton = makeRiskButton(gameImg.getSubimage(x, y, w, h), gameImg.getSubimage(x, y+230, w, h), gameImg.getSubimage(x, y+164, w, h), gameImg.getSubimage(x, y+98, w, h) );
		graphbutton.setBounds(x-63, y-54, w , h );
		graphbutton.addActionListener( buttonActionListener );
		graphbutton.setToolTipText( resb.getString("game.button.statistics") );

		x=x+w;

		cardsbutton = makeRiskButton(gameImg.getSubimage(x, y, w, h), gameImg.getSubimage(x, y+230, w, h), gameImg.getSubimage(x, y+164, w, h), gameImg.getSubimage(x, y+98, w, h) );
		cardsbutton.setBounds(x-63, y-54, w , h );
		cardsbutton.addActionListener( buttonActionListener );
		cardsbutton.setToolTipText(resb.getString("game.button.cards"));

		x=x+w;

		missionbutton = makeRiskButton(gameImg.getSubimage(x, y, w, h), gameImg.getSubimage(x, y+230, w, h), gameImg.getSubimage(x, y+164, w, h), gameImg.getSubimage(x, y+98, w, h) );
		missionbutton.setBounds(x-63, y-54, w , h );
		missionbutton.addActionListener( buttonActionListener );
		missionbutton.setToolTipText(resb.getString("game.button.mission"));

		x=x+w;

		undobutton = makeRiskButton(gameImg.getSubimage(x, y, w, h), gameImg.getSubimage(x, y+230, w, h), gameImg.getSubimage(x, y+164, w, h), gameImg.getSubimage(x, y+98, w, h) );
		undobutton.setBounds(x-63, y-54, w , h );
		undobutton.addActionListener( buttonActionListener );
		undobutton.setToolTipText(resb.getString("game.button.undo"));

		x=x+w;

		menubutton = makeRiskButton(gameImg.getSubimage(x, y, w, h), gameImg.getSubimage(x, y+230, w, h), gameImg.getSubimage(x, y+164, w, h), gameImg.getSubimage(x, y+98, w, h) );
		menubutton.setBounds(x-63, y-54, w , h );
		menubutton.addActionListener( buttonActionListener );
		menubutton.setToolTipText( resb.getString("game.button.menu") );



		w=115;
		h=31;
		gobutton = makeRiskButton(gameImg.getSubimage(663, 128, w, h), gameImg.getSubimage(412, 394, w, h), gameImg.getSubimage(296, 394, w, h), gameImg.getSubimage(180, 394, w, h) );
		gobutton.addActionListener( buttonActionListener );

		fpBottom.add(graphbutton);
		fpBottom.add(cardsbutton);
		fpBottom.add(missionbutton);
		fpBottom.add(undobutton);
		fpBottom.add(menubutton);
		fpBottom.add(gobutton);

		//pp.setBounds(31, 54, PicturePanel.PP_X , PicturePanel.PP_Y);
		//pp.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0,0,0),1));
		pp.addMouseListener(mapListener);
		pp.addMouseMotionListener(mapListener);
		pp.setBackground(Color.BLACK);

		gm = new GameMenuPanel();
		gm.setVisible(false);
		gm.setBounds(285, 141, 170 , 250);
		//gm.addMouseListener(this);
		//gm.addMouseMotionListener(this);
		getRootPane().getLayeredPane().add(gm , JLayeredPane.MODAL_LAYER);

		//layeredPane.add(gm, 0);
		//layeredPane.add(pp, 1);
		//layeredPane.add(fp, 2);

		//getContentPane().add(layeredPane);

		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

		addWindowListener(
			new java.awt.event.WindowAdapter() {
				public void windowClosing(java.awt.event.WindowEvent evt) {
					closeleave();
				}
			}
		);

		this			.addKeyListener( this);
		cardsbutton		.addKeyListener( this);
		missionbutton		.addKeyListener( this);
		undobutton		.addKeyListener( this);
		menubutton		.addKeyListener( this);
		graphbutton		.addKeyListener( this);
		gobutton		.addKeyListener( this);

		cardsDialog = new CardsDialog(GameFrame.this, true, myrisk, pp);

                RiskUIUtil.center(cardsDialog);

		movedialog = new MoveDialog(this, false);

		RiskUIUtil.center(movedialog);

		// #################################################################################
		// sort out size and add to main panel

		Dimension d = new Dimension(740, 54);
		fp.setPreferredSize(d);
		fp.setMinimumSize(d);
		fp.setMaximumSize(d);

		d = new Dimension(740, 121);
		fpBottom.setPreferredSize(d);
		fpBottom.setMinimumSize(d);
		fpBottom.setMaximumSize(d);

		d = new Dimension(31,425);
		fpLeft.setPreferredSize(d);
		fpLeft.setMinimumSize(d);
		fpLeft.setMaximumSize(d);

		d = new Dimension(32,425);
		fpRight.setPreferredSize(d);
		fpRight.setMinimumSize(d);
		fpRight.setMaximumSize(d);

		JPanel flashPanel = new JPanel( new BorderLayout() );
		flashPanel.add(fp,BorderLayout.NORTH);
		flashPanel.add(pp);
		flashPanel.add(fpBottom,BorderLayout.SOUTH);
		flashPanel.add(fpLeft,BorderLayout.WEST);
		flashPanel.add(fpRight,BorderLayout.EAST);

		getContentPane().add( flashPanel );

	}

	public void setup(boolean s) {

            	try {
			pp.load();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
            
		gameState=0; // -1 or 0 means no input needed
		mapView=PicturePanel.VIEW_CONTINENTS;

		//gameStatus="";
		setGameStatus(null);

		note="";
		//c1Id = -1;

		localGame = s;

		closebutton.setText(resb.getString(localGame?"game.menu.close":"game.menu.leave"));

		repaintCountries();

                // disable all buttons at the start of the game
                AbstractButton[] buttons = new AbstractButton[] {savebutton,AutoEndGo,AutoDefend,cardsbutton,missionbutton,undobutton,gobutton};
                for (int c=0;c<buttons.length;c++) {
                    buttons[c].setEnabled(false);
                }
	}

	ActionListener buttonActionListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {

			if (e.getSource()==cardsbutton) {
				displayCards();
			}
			else if (e.getSource()==missionbutton) {
				displayMission();
			}
			else if (e.getSource()==undobutton) {
				doUndo();
			}
			else if (e.getSource()==menubutton) {
				displayMenu();
			}
			else if (e.getSource()==graphbutton) {

				displayGraph();

			}
			else if (e.getSource()==gobutton) {
				goOn();
			}
			else if (e.getSource()==savebutton) {

				String name = RiskUIUtil.getSaveFileName(
					GameFrame.this
					//RiskUtil.SAVES_DIR,
					//RiskFileFilter.RISK_SAVE_FILES
				);

				if (name!=null) {

					go("savegame " + name );

				}

			}
			else if (e.getSource()==resumebutton) {

				displayMenu();

			}
			else if (e.getSource()==AutoEndGo) {

				if ( AutoEndGo.isSelected() ) {
					go("autoendgo on");
				}
				else {
					go("autoendgo off");
				}

			}
			else if (e.getSource()==closebutton) {

				closeleave();

			}
			else if (e.getSource()==AutoDefend) {

				if ( AutoDefend.isSelected() ) {
					go("autodefend on");
				}
				else {
					go("autodefend off");
				}

			}
			else if (e.getSource()==helpbutton) {

				try {
					RiskUtil.openDocs( resb.getString("helpfiles.flash") );
				}
				catch(Exception er) {
					JOptionPane.showMessageDialog(GameFrame.this,"Unable to open manual: "+er.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
				}

			}
		}
	};

	public void closeleave() {
		if (graphOn) { graphdialog.setVisible(false); graphOn=false; }
                go("closegame");
	}

	public void repaintCountries() {

		pp.repaintCountries( mapView );
/*
		if (mapView==1) {
			pp.repaintCountries( PicturePanel.VIEW_CONTINENTS );
		}
		else if (mapView==2) {
			pp.repaintCountries( PicturePanel.VIEW_OWNERSHIP );
		}
		else if (mapView==3) {
			pp.repaintCountries( PicturePanel.VIEW_BORDER_THREAT );
		}
		else if (mapView==4) {
			pp.repaintCountries( PicturePanel.VIEW_CARD_OWNERSHIP );
		}
		else if (mapView==5) {
			pp.repaintCountries( PicturePanel.VIEW_TROOP_STRENGTH );
		}
		else if (mapView==6) {
			pp.repaintCountries( PicturePanel.VIEW_CONNECTED_EMPIRE );
		}
*/
	}

	public void setGameStatus(String state) {

                int[] cols = null;
		if (state!=null) {
			// the colors for the bottom display r collected here
			cols = myrisk.getPlayerColors();
                        if (cols.length == 0) { cols = null; }
		}

		if (cols==null) {
			cols = new int[] { ColorUtil.GRAY };
		}


		gameStatus=state;
                colors = cols;

		repaint();
	}

	public void needInput(int s) {

		gameState=s;

		String goButtonText=null;

		switch (gameState) {

			case RiskGame.STATE_TRADE_CARDS: {

				// after wiping out someone if you go into trade mode
				pp.setC1(255);
				pp.setC2(255);

                                if (myrisk.getGame().canEndTrade()) {
                                    goButtonText = resb.getString("game.button.go.endtrade");
                                }
                                note = getArmiesLeftText();
				break;
			}
			case RiskGame.STATE_PLACE_ARMIES: {
				if ( !myrisk.getGame().NoEmptyCountries() ) {
					goButtonText = resb.getString("game.button.go.autoplace");
				}
                                note = getArmiesLeftText();
				break;
			}
			case RiskGame.STATE_ATTACKING: {

				pp.setC1(255);
				pp.setC2(255);

				note = resb.getString("game.note.selectattacker");

				goButtonText = resb.getString("game.button.go.endattack");

				break;
			}
			case RiskGame.STATE_FORTIFYING: {

				note = resb.getString("game.note.selectsource");

				goButtonText = resb.getString("game.button.go.nomove");

				break;
			}
			case RiskGame.STATE_END_TURN: {

				goButtonText = resb.getString("game.button.go.endgo");

				break;

			}
			case RiskGame.STATE_GAME_OVER: {
                                if (myrisk.getGame().canContinue()) {
                                    goButtonText = resb.getString("game.button.go.continue");
                                }
                                else {
                                    if (localGame) {
                                            goButtonText = resb.getString("game.button.go.closegame");
                                    }
                                    else {
                                            goButtonText = resb.getString("game.button.go.leavegame");
                                    }
                                }
				break;

			}
			case RiskGame.STATE_SELECT_CAPITAL: {

				note = resb.getString("game.note.happyok");

				goButtonText = resb.getString("game.button.go.ok");

				break;
			}
			case RiskGame.STATE_BATTLE_WON: {

                                RiskGame game = myrisk.getGame();
                                openMove(game.getMustMove(), game.getAttacker().getColor(), game.getDefender().getColor(), false);
				movedialog.setVisible(true);

				break;
			}
			// for gameState 4 look in FlashRiskAdapter.java
			// for gameState 10 look in FlashRiskAdapter.java
			default: break;

		}





		if (goButtonText!=null) {

			gobutton.setEnabled(true);
			gobutton.setText(goButtonText);

		}
		else {
			gobutton.setEnabled(false);
			gobutton.setText("");
		}

		// can not use this as it sometimes just does not work, no idea why, mainly a vista problem
		//if (!(gobutton.getText().equals(""))) {
		//	gobutton.setEnabled(true);
		//}
		//else {
		//	gobutton.setEnabled(false);
		//}

                cardsbutton.setEnabled(true);
                missionbutton.setEnabled(true);

                if (localGame) {

                    if (gameState!=RiskGame.STATE_DEFEND_YOURSELF) {
                        undobutton.setEnabled(true);
                    }
                    savebutton.setEnabled(true);

                }

                AutoEndGo.setEnabled(true);
                AutoEndGo.setBackground( Color.white );
                AutoEndGo.setSelected( myrisk.getAutoEndGo() );

                AutoDefend.setEnabled(true);
                AutoDefend.setBackground( Color.white );
                AutoDefend.setSelected( myrisk.getAutoDefend() );

		repaint(); // SwingGUI has this here, if here then not needed in set status
	}

        public String getArmiesLeftText() {
                int l = myrisk.getGame().getCurrentPlayer().getExtraArmies();
                return RiskUtil.replaceAll( resb.getString("game.note.armiesleft"),"{0}", String.valueOf(l));
        }

	/**
	 * checks if the coordinates are in one of the
	 * tabs at the top of the window
	 */
	public int insideButton(int x, int y) {

		int B=-1;

		if (y >=9 && y < 32 ) {

			int W=115;

			if (x >= 24 && x < (24 + W)) {
				B=PicturePanel.VIEW_CONTINENTS;
			}
			else if (x >= 139 && x < (139 + W)) {
				B=PicturePanel.VIEW_OWNERSHIP;
			}
			else if (x >= 254 && x < (254 + W)) {
				B=PicturePanel.VIEW_BORDER_THREAT;
			}
			else if (x >= 369 && x < (369 + W)) {
				B=PicturePanel.VIEW_CARD_OWNERSHIP;
			}
			else if (x >= 484 && x < (484 + W)) {
				B=PicturePanel.VIEW_TROOP_STRENGTH;
			}
			else if (x >= 599 && x < (599 + W)) {
				B=PicturePanel.VIEW_CONNECTED_EMPIRE;
			}

		}

		return B;

	}

	/** calls the parser with the command  */

	private BattleDialog battledialog;

	public void setBattleDialog(BattleDialog bd) {
		battledialog=bd;
	}

	/**
	 * calls the parser
	 * @param command sends the input command to the parser via a string
	 */
	public void go(String command) {

		blockInput();

		myrisk.parser(command);

	}

	public void blockInput() {

		pp.setHighLight(255);

		//c1Id = -1;

		if (gameState==RiskGame.STATE_ROLLING || gameState==RiskGame.STATE_DEFEND_YOURSELF) {

			//this does not close it, just resets its params
			battledialog.reset();
		}


		if (gameState==RiskGame.STATE_BATTLE_WON || gameState==RiskGame.STATE_FORTIFYING) {

			// this hides the dailog
			movedialog.exitForm();
		}

		if (gameState!=RiskGame.STATE_PLACE_ARMIES || !myrisk.getGame().getSetup() ) { noInput(); }

	}

	public void noInput() {

		cardsbutton.setEnabled(false);
		missionbutton.setEnabled(false);
		undobutton.setEnabled(false);

		savebutton.setEnabled(false);

		AutoEndGo.setEnabled(false);
		AutoEndGo.setBackground( Color.lightGray );

		AutoDefend.setEnabled(false);
		AutoDefend.setBackground( Color.lightGray );

		gobutton.setText("");
		gobutton.setEnabled(false);

		note="";
		gameState=0;

	}

	/**
	 * Returns an image of a given country
	 * @param a Index position of country
	 */
	public BufferedImage getCountryImage(int a) {

		return pp.getCountryImage(a, true);

	}

	public void openMove(int min, int c1num, int c2num, boolean tacmove) {

		int src = myrisk.hasArmiesInt( c1num );
		int des = myrisk.hasArmiesInt( c2num );
		BufferedImage c1img = pp.getCountryImage(c1num ,true);
		BufferedImage c2img = pp.getCountryImage(c2num ,true);
		Country country1 = myrisk.getGame().getCountryInt( c1num);
		Country country2 = myrisk.getGame().getCountryInt( c2num);

		int color = myrisk.getCurrentPlayerColor();

		movedialog.setup(tacmove,       min,      src, des, c1img, c2img, country1, country2, new Color( color ) );

	}

        public void mapClick(final int[] countries,MouseEvent e) {

            Object oldnote = note;

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
                    note=resb.getString("game.note.selectattacker");
                }
                else if (countries.length == 1) {
                    note=resb.getString("game.note.selectdefender");
                }
                else {
                    go("attack " + countries[0] + " " + countries[1]);
                    note=resb.getString("game.note.selectattacker");
                }

            }
            else if (gameState == RiskGame.STATE_FORTIFYING) {
                if (countries.length==0) {
                    note=resb.getString("game.note.selectsource");
                }
                else if (countries.length==1) {
                    note=resb.getString("game.note.selectdestination");
                }
                else {
                    note="";
                    repaint();

                    openMove(1,countries[0] , countries[1], true);

                    // this comes in on the mouse event thread
                    // we need to make this dialog blocking so the user
                    // can not click on the map while this dialog is up
                    movedialog.setModal(true);
                    movedialog.setVisible(true);
                    movedialog.setModal(false);
                    // now we set it back to a none-blocking dialog
                    // for use with the move of armies after a attack

                    // clean up
                    pp.setC1(255);
                    pp.setC2(255);
                    note=resb.getString("game.note.selectsource");

                }
            }
            else if (gameState == RiskGame.STATE_SELECT_CAPITAL) {
                // do nothing ??
            }

            if (oldnote!=note) {
                repaint();
            }

        }


	/**
	 * the map view has changed
	 * (normal, border threat, ...)
	 * @param click		The tab number the user has clicked on
	 */
	private void setMapView(int click)
	{
		mapView = click;
		repaintCountries();
		repaint();
	}//private void setMapView(int click)


	/**
	 * displays the cards dialog
	 */
	private void displayCards() {

		cardsDialog.setup( (gameState==RiskGame.STATE_TRADE_CARDS) );

		cardsDialog.setVisible(true);

	}



	/**
	 * displays the mission window
	 */
	private void displayMission()
	{
		MissionDialog missiondialog = new MissionDialog(GameFrame.this, true, myrisk);

		Dimension frameSize = getSize();
		Dimension aboutSize = missiondialog.getSize();
		int x = getLocation().x + (frameSize.width - aboutSize.width) / 2;
		int y = getLocation().y + (frameSize.height - aboutSize.height) / 2;
		if (x < 0) x = 0;
		if (y < 0) y = 0;
		missiondialog.setLocation(x, y);

		missiondialog.setVisible(true);
	}//private void displayMission()



	/**
	 * does an "undo"
	 */
	private void doUndo()
	{
            
                pp.setC1(PicturePanel.NO_COUNTRY);
                pp.setC2(PicturePanel.NO_COUNTRY);
            
		go("undo");
	}//private void doUndo()



	/**
	 * displays the menu
	 */
	private void displayMenu() {

	    if(menuOn) {

		gm.setVisible(false);
		pp.addMouseListener(mapListener);
		pp.addMouseMotionListener(mapListener);

		menuOn=false;

	    }
	    else {

		pp.removeMouseListener(mapListener);
		pp.removeMouseMotionListener(mapListener);

		if (myrisk.getGame().getCurrentPlayer()!=null) {

			AutoEndGo.setSelected( myrisk.getAutoEndGo() );
			AutoDefend.setSelected( myrisk.getAutoDefend() );

		}

		gm.setVisible(true);
		menuOn=true;
	    }

	}

	public void displayGraph() {

		if (graphOn) {
			graphdialog.setVisible(false);
			graphOn=false;
		}
		else {

			if (graphdialog==null) {

				graphdialog = new StatsDialog(GameFrame.this, false, myrisk);

				Dimension frameSize = (GameFrame.this).getPreferredSize();
				Dimension graphSize = graphdialog.getSize();
				int a = (GameFrame.this).getLocation().x + (frameSize.width - graphSize.width) / 2;
				int b = (GameFrame.this).getLocation().y + (frameSize.height - graphSize.height) / 2;
				if (a < 0) a = 0;
				if (b < 0) b = 0;
				graphdialog.setLocation(a, b);

			}

			graphdialog.setVisible(true);
			graphOn=true;
		}

	}

	/**
	 * the user has clicked the "go" button
	 */
	private void goOn() {
		if (gameState==RiskGame.STATE_TRADE_CARDS) {
			go("endtrade");
		}
		else if (gameState==RiskGame.STATE_PLACE_ARMIES) {
			go("autoplace");
		}
		else if (gameState==RiskGame.STATE_ATTACKING) {
			pp.setC1(255);
			go("endattack");
		}
		else if (gameState==RiskGame.STATE_FORTIFYING) {
			pp.setC1(255);
			go("nomove");
		}
		else if (gameState==RiskGame.STATE_END_TURN) {
			go("endgo");
		}
		else if (gameState==RiskGame.STATE_GAME_OVER) {
                        RiskGame game = myrisk.getGame();
                        if (game!=null && game.canContinue()) {
                            go("continue");
                        }
                        else {
                            closeleave();
                        }
		}
		else if (gameState == RiskGame.STATE_SELECT_CAPITAL) {
                        int c1Id = pp.getC1();
			pp.setC1(255);
			go("capital " + c1Id);
		}
	}//private void goOn()


	public static JButton makeRiskButton(Image gobutton1, Image gobutton2, Image gobutton3, Image gobutton4) {
		JButton button = new JButton();
		NewGameFrame.sortOutButton( button, gobutton1, gobutton3, gobutton2 );
		button.setDisabledIcon( new ImageIcon( gobutton4 ) );
		return button;
	}

	/**
	 * The user has released a key
	 */
	public void keyReleased( KeyEvent event ) {

		//if (event.isControlDown()) {

			//with CTRL down

			// when the ctrl+number bit was here is gave the "?" key when the number 6 was pressed

		//} else {

			switch (event.getKeyCode())
			{
				case KeyEvent.VK_1: this.setMapView(PicturePanel.VIEW_CONTINENTS); break;
				case KeyEvent.VK_2: this.setMapView(PicturePanel.VIEW_OWNERSHIP); break;
				case KeyEvent.VK_3: this.setMapView(PicturePanel.VIEW_BORDER_THREAT); break;
				case KeyEvent.VK_4: this.setMapView(PicturePanel.VIEW_CARD_OWNERSHIP); break;
				case KeyEvent.VK_5: this.setMapView(PicturePanel.VIEW_TROOP_STRENGTH); break;
				case KeyEvent.VK_6: this.setMapView(PicturePanel.VIEW_CONNECTED_EMPIRE); break;

					// can not use this as it may be not a int
					//Integer.parseInt( event.getKeyChar() + ""));
					
			}

			//no modifier button pressed
			switch (event.getKeyCode())
			{
				case KeyEvent.VK_C:
					//cards
					if (gameState!=RiskGame.STATE_NEW_GAME) this.displayCards();
					break;
				case KeyEvent.VK_M:
					//mission
					if (gameState!=RiskGame.STATE_NEW_GAME) this.displayMission();
					break;
				case KeyEvent.VK_U:
					//undo
					if (gameState!=RiskGame.STATE_NEW_GAME && gameState!=RiskGame.STATE_DEFEND_YOURSELF) this.doUndo();
					break;
				case KeyEvent.VK_F10:
					this.displayMenu();
					//menu
					break;
				case KeyEvent.VK_G:
					//go button
					if (gameState!=RiskGame.STATE_NEW_GAME) this.goOn();
					break;
			}
		//}
	}//public void keyReleased( KeyEvent event )

	public void keyTyped( KeyEvent event ) {}
	public void keyPressed( KeyEvent event ) {}

	/**
	 * the game menu
	 * which you get when pressing the "Menu" button
	 */
	class GameMenuPanel extends JPanel {

		public GameMenuPanel() {

			setLayout(null);

			int w=100;

			savebutton = makeRiskButton(gameImg.getSubimage(480, 373, w, 21), gameImg.getSubimage(380, 373, w, 21), gameImg.getSubimage(280, 373, w, 21), gameImg.getSubimage(180, 373, w, 21) );
			savebutton.setText(resb.getString("game.menu.save"));
			savebutton.setBounds(35, 50, w , 20 );
			savebutton.addActionListener( buttonActionListener );

			closebutton = makeRiskButton(gameImg.getSubimage(480, 373, w, 21), gameImg.getSubimage(380, 373, w, 21), gameImg.getSubimage(280, 373, w, 21), gameImg.getSubimage(180, 373, w, 21) );

			closebutton.setBounds(35, 80, w , 20 );
			closebutton.addActionListener( buttonActionListener );





			AutoEndGo = new JCheckBox(resb.getString("game.menu.autoendgo"));
			AutoEndGo.setToolTipText( resb.getString("game.menu.autoendgo"));

			AutoEndGo.setMargin(new Insets(0,0,0,0));
			AutoEndGo.setBorderPainted(false);
			AutoEndGo.setFocusPainted(false);

			AutoEndGo.setBounds(35, 110, w , 20 );
			AutoEndGo.addActionListener( buttonActionListener );
			AutoEndGo.setBackground( Color.lightGray );






			AutoDefend = new JCheckBox(resb.getString("game.menu.autodefend"));
			AutoDefend.setToolTipText( resb.getString("game.menu.autodefend"));

			AutoDefend.setMargin(new Insets(0,0,0,0));
			AutoDefend.setBorderPainted(false);
			AutoDefend.setFocusPainted(false);

			AutoDefend.setBounds(35, 140, w , 20 );
			AutoDefend.addActionListener( buttonActionListener );
			AutoDefend.setBackground( Color.lightGray );





			helpbutton = makeRiskButton(gameImg.getSubimage(480, 373, w, 21), gameImg.getSubimage(380, 373, w, 21), gameImg.getSubimage(280, 373, w, 21), gameImg.getSubimage(180, 373, w, 21) );
			helpbutton.setText(resb.getString("game.menu.manual"));
			helpbutton.setBounds(35, 170, w , 20 );
			helpbutton.addActionListener( buttonActionListener );




			resumebutton = makeRiskButton(gameImg.getSubimage(480, 373, w, 21), gameImg.getSubimage(380, 373, w, 21), gameImg.getSubimage(280, 373, w, 21), gameImg.getSubimage(180, 373, w, 21) );
			resumebutton.setText(resb.getString("game.menu.closemenu"));
			resumebutton.setBounds(35, 200, w , 20 );
			resumebutton.addActionListener( buttonActionListener );

			add(savebutton);
			add(AutoDefend);
			add(helpbutton);
			add(AutoEndGo);
			add(closebutton);
			add(resumebutton);

		}

		public void paintComponent(Graphics g) {

			Graphics2D g2 = (Graphics2D)g;

			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f);
			g2.setComposite(ac);

//					  destination		source
			g2.drawImage(gameImg,0,0,170,250,     633,175,803,425,this); // top

			FontRenderContext frc = g2.getFontRenderContext();

			Font font = new java.awt.Font("Arial", java.awt.Font.BOLD, 24);
			g2.setColor( Color.black );
			TextLayout tl;

			tl = new TextLayout( resb.getString("game.menu.title") , font, frc);
			tl.draw( g2, (float) (85-tl.getBounds().getWidth()/2), (float)40 );

		}

	}




}

