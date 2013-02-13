package net.yura.domination.lobby.client;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.yura.domination.engine.OnlineRisk;
import net.yura.domination.engine.Risk;
import net.yura.domination.engine.RiskIO;
import net.yura.domination.engine.RiskUIUtil;
import net.yura.domination.engine.RiskUtil;
import net.yura.domination.engine.translation.TranslationBundle;
import net.yura.domination.ui.flashgui.FlashRiskAdapter;
import net.yura.domination.ui.flashgui.GameFrame;
import net.yura.lobby.client.LobbyClientGUI;
import net.yura.lobby.client.ResBundle;
import net.yura.lobby.client.TurnBasedAdapter;
import net.yura.lobby.model.Game;

public class ClientGameRisk extends TurnBasedAdapter implements OnlineRisk {

        private final static Logger logger = Logger.getLogger( ClientGameRisk.class.getName() );
    
	private final static String product;
	private final static String version = "0.2";

	static {
                final String RISK_PATH = RiskUtil.GAME_NAME + "/";
                final String MAP_PATH = "maps/";

		product = RiskUtil.GAME_NAME + " Lobby Client";

                RiskUtil.streamOpener = new RiskIO() {
                    public InputStream openStream(String name) throws IOException {
                            return LobbyClientGUI.openStream(RISK_PATH+name);
                    }
                    public InputStream openMapStream(String name) throws IOException {
                            return openStream(MAP_PATH+name);
                    }
                    public ResourceBundle getResourceBundle(Class a,String n,Locale l) {
                            return ResBundle.getBundle(a,n,l);
                    }
                    public void openURL(URL url) throws Exception {
                            LobbyClientGUI.openURL(url);
                    }
                    public void openDocs(String doc) throws Exception {
                            openURL( new URL( LobbyClientGUI.getCodeBase(), RISK_PATH+doc) );
                    }
                    public void saveGameFile(String name, Object obj) throws Exception {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }
                    public InputStream loadGameFile(String file) throws Exception {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }
                    public void getMap(String filename, Risk risk,Exception ex) {
                        RiskUtil.printStackTrace(ex);
                        risk.getMapError(ex.toString());
                    }
                    public OutputStream saveMapFile(String fileName) throws Exception {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }
                    public void renameMapFile(String oldName, String newName) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }
            };
	}

	public ClientGameRisk() {

	}


	//##################################################################################
	// game setup
	//##################################################################################

	private GameSetupPanel gsp;

	public Game newGameDialog(Frame parent, String serveroptions,String myname) { // String serveroptions is a list of maps

            if (gsp==null) {
                gsp = new GameSetupPanel();
            }
            
            return gsp.showDialog(parent, serveroptions, myname);
	}

        public Icon getIcon(String options) {

		RiskMap iconedmap = GameSetupPanel.getRiskMap( RiskUtil.getMapNameFromLobbyStartGameOption(options) );
                
                // TODO this is a long task and should NOT be done in the caller thread
		iconedmap.loadInfo();

                // TODO return some sort of icon placeholder, then load full icon later on
		return iconedmap.getSmallIcon();
	}

        public String getGameDescription(String string) {
            return RiskUtil.getGameDescriptionFromLobbyStartGameOption(string);
        }

	//##################################################################################
	// in game client stuff
	//##################################################################################


	private Risk myrisk;

	private JLabel nameLabel;

	private GameFrame frame;


	public void startNewGame(String name) {

		if (frame==null) {

			myrisk = new Risk();

			makeNewGameFrame();

		}

		nameLabel.setText(name);
	}

	private void makeNewGameFrame() {

		ResourceBundle resb = TranslationBundle.getBundle();

		//setReplay(false);

		final ImageIcon borderimage = new ImageIcon( ClientGameRisk.class.getResource("back.jpg") );



		final Box sidepanel = new Box(javax.swing.BoxLayout.Y_AXIS);/* {

			public void paintComponent(java.awt.Graphics g) {

				java.awt.Image img = borderimage.getImage();

				int w = img.getWidth(this);
				int h = img.getHeight(this);

				for (int i = 0; i < getWidth(); i += w) for (int j = 0; j < getHeight(); j += h) {

					g.drawImage(img, i, j, this);

				}
			}

		};*/




		JButton aboutButton = new JButton( resb.getString( "mainmenu.about") );

		aboutButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				RiskUIUtil.openAbout(frame,product, version);
			}
		});


		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		JPanel panel3 = new JPanel( new GridLayout(1,2,5,5) );
		panel3.setBorder( new EmptyBorder(5,5,5,5) );

		Insets insets = new Insets( startButton.getMargin().top ,0, startButton.getMargin().bottom ,0);
		startButton.setMargin(insets);
		aboutButton.setMargin(insets);

		nameLabel = new JLabel();

		panel1.add( nameLabel );
		panel2.add( timer );
		panel3.add( startButton );
		panel3.add( aboutButton );

		sidepanel.add( panel1 );
		sidepanel.add( panel2 );
		sidepanel.add( playerListArea );
		sidepanel.add( panel3 );
		sidepanel.add( chatBoxArea );


		//panel2.setBorder( new EmptyBorder(20, 0, 20, 0) );
		panel2.setBorder( BorderFactory.createMatteBorder(20, 0, 20, 0, borderimage ) );
		playerListArea.setBorder( BorderFactory.createMatteBorder(0, 0, 20, 0, borderimage ) );
		chatBoxArea.setBorder( BorderFactory.createMatteBorder(20, 0, 0, 0, borderimage ) );
		sidepanel.setBorder( BorderFactory.createMatteBorder(20, 20, 20, 20, borderimage ) );

		Dimension topsize = new Dimension(160,120);

		playerListArea.setPreferredSize( topsize );
		playerListArea.setMaximumSize( topsize );
		playerListArea.setMinimumSize( topsize );

		sidepanel.setPreferredSize( new Dimension(200,600) );



		FlashRiskAdapter riskadapter = new FlashRiskAdapter(myrisk) {

			public void addPlayer(int type, String name, java.awt.Color color, String ip) {}
			public void sendDebug(String a) {  } // System.out.println("\tRISK "+ a);

			public void noInput() {

				if (gameFrame!=null) {
					gameFrame.noInput();
				}
			}

			public void startGame(boolean s) {

				gameFrame.setup(s);

				gameFrame.setVisible(true);
				gameFrame.requestFocus();

			}

			public void closeGame() {

				gameFrame.setVisible(false);

			}

			//public void needInput(int s) {
			//	super.needInput(s);
			//}

		};




		frame = riskadapter.getGameFrame();

		frame.getContentPane().add(sidepanel, java.awt.BorderLayout.EAST );
		frame.pack();

		try {

			frame.setMinimumSize( frame.getPreferredSize() );

		}
		catch(NoSuchMethodError ex) {

			// must me java 1.4
			// dont need to do anything here as it would have already been set to resizable false
		}

		// amoung other things, the newplayer command needs to be passed with option that matches this
		// computers Risk.myAddress, or the Risk game would not know when to ask for input



	}

	// this NEEDS to call leaveGame();
	public void closegame() {
            // simulate a normal ui command into the game
            if (myrisk.getGame()!=null) {
		myrisk.parser("closegame");
            }
            else {
                // we are here coz the game failed to open
                leaveGame();
            }
	}

        public boolean hasSpaceToJoin() {
            return myrisk.findEmptySpot() != null;
        }
        
        /**
         * this is called when I resign from a game
         */
	public void blockInput() {
                myrisk.closeBattle();
		frame.blockInput();
	}

	public void gameString(String message) {

		//System.out.println("\tGOT: "+message);

		myrisk.parserFromNetwork(message);
	}

	public void gameObject(Object object) {
		Map map = (Map)object;
                myrisk.lobbyMessage(map, lgml.whoAmI(), this);
	}

        /**
         * this is called when a player of this game logs in mid-game
         */
	public void renamePlayer(String oldser,String newuser) {
	    myrisk.renamePlayer(oldser,newuser);
	}

        // WMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMW
        // WMWMWMWMWMWMWMWMWMWMWMWMWMW OnlineRisk MWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMW
        // WMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMWMW

        public void sendUserCommand(String messagefromgui) {
            sendGameMessage(messagefromgui);
        }

        public void sendGameCommand(String mtemp) {
            // this happens for game commands on my go
            logger.info("ignore GameCommand "+mtemp );
        }

        public void closeGame() {
            leaveGame();
        }
}
