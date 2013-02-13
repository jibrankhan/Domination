package net.yura.domination.lobby.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.yura.domination.engine.RiskUtil;
import net.yura.domination.engine.core.RiskGame;
import net.yura.domination.engine.translation.TranslationBundle;
import net.yura.domination.ui.flashgui.NewGameFrame;
import net.yura.lobby.model.Game;

/**
 * <p> New Game Frame for FlashGUI </p>
 * @author Yura Mamyrin
 */
public class GameSetupPanel extends JPanel implements ActionListener {

	private BufferedImage newgame;
	private BufferedImage game2;
	private BufferedImage card;

	private JLabel mapPic;
	private JPanel mapsMissions;

	private JButton start;
	//private JButton help;
	private JButton cancel;

	private JRadioButton domination;
	private JRadioButton capital;
	private JRadioButton mission;

	private JRadioButton fixed;
	private JRadioButton increasing;
        private JRadioButton italian;

	private JCheckBox AutoPlaceAll;
	private JCheckBox recycle;

	private ResourceBundle resb;

	private String options;
	private JList list;

	private JDialog dialog;
	private RiskMap riskmap;

	private JSpinner human;
	//private JSpinner aipassive;
	private JSpinner aieasy;
	private JSpinner aihard;

	private JTextField gamename;

	/**
	 * The NewGameFrame Constructor
	 * @param r The Risk Parser used for playing the game
	 * @param t States whether this game is local
	 */
	public GameSetupPanel() {

		setLayout(null);

		Dimension d = new Dimension(700, 600);

		setPreferredSize(d);
		setMinimumSize(d);
		setMaximumSize(d);


		try {
			newgame = ImageIO.read( NewGameFrame.class.getResource("newgame.jpg") );
			game2 = ImageIO.read( getClass().getResource("game2.jpg") );

			card = game2.getSubimage(0,247,23,35);

		}
		catch (IOException ex) { throw new RuntimeException(ex); }










		mapPic = new JLabel();
		mapPic.setBounds(51, 51, 203 , 127 );
		add(mapPic);


		mapsMissions = new JPanel();
		mapsMissions.setOpaque(false);
		mapsMissions.setLayout(new javax.swing.BoxLayout(mapsMissions, javax.swing.BoxLayout.Y_AXIS));

		final JScrollPane sp2 = new JScrollPane(mapsMissions);
		sp2.setBounds(340, 51, 309 , 210 ); // this will allow 6 players, 30 pixels per player
		sp2.setBorder(null);

		sp2.setOpaque(false);
		sp2.getViewport().setOpaque(false);

		add( sp2 );










		list = new JList();
		list.setCellRenderer(new RiskMapListCellRenderer());
		//list.setVisibleRowCount(10);
		list.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setBounds(54, 192, 200 , 260 );
		scrollPane.setBorder(null);
		list.setOpaque(false);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);

		add(scrollPane);






		list.addListSelectionListener( new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent e) {

				if (e.getValueIsAdjusting()) {return;}

				RiskMap it = ((RiskMap)list.getSelectedValue());

				if (it!=null) {

					riskmap = it;
					mapPic.setIcon( riskmap.getBigIcon() );

					mapsMissions.removeAll();

					String[] missions = riskmap.getMissions();

					for (int c=0;c<missions.length;c++) {

						mapsMissions.add( makeNewMission(missions[c]) );

						mapsMissions.add( Box.createVerticalStrut(3) );

					}

					if (missions.length == 0) {

						mapsMissions.add( new JLabel(" No missions for this map.") );

						if (mission.isSelected()) { domination.setSelected(true); AutoPlaceAll.setEnabled(true); }

					}

					mission.setEnabled( missions.length != 0 );

					mapsMissions.revalidate();
					// @TODO: scroll to the top

				}

			}


		});





		human = new JSpinner( new SpinnerNumberModel(2,1,6,1) );
		//aipassive = new JSpinner( new SpinnerNumberModel(0,0,6,1) );
		aieasy = new JSpinner( new SpinnerNumberModel(2,0,6,1) );
		aihard = new JSpinner( new SpinnerNumberModel(2,0,6,1) );

		JPanel playernum = new JPanel();
		playernum.setBounds(300,310,400,60);
		playernum.setOpaque(false);
		add(playernum);

		playernum.add(new JLabel("human"));
		playernum.add(human);
		//playernum.add(new JLabel("crap ai"));
		//playernum.add(aipassive);
		playernum.add(new JLabel("easy ai"));
		playernum.add(aieasy);
		playernum.add(new JLabel("hard ai"));
		playernum.add(aihard);

		ButtonGroup GameTypeButtonGroup = new ButtonGroup();
		ButtonGroup CardTypeButtonGroup = new ButtonGroup();



// ALL PROBLEMS COME FROM THIS!!!!
// when this was at the start of the init loads of class problems started
resb = TranslationBundle.getBundle();


		domination = new JRadioButton(resb.getString("newgame.mode.domination"), true);
		NewGameFrame.sortOutButton( domination );
		domination.setBounds(380, 370, 90 , 25 );
		domination.addActionListener(this);

		capital = new JRadioButton(resb.getString("newgame.mode.capital"));
		NewGameFrame.sortOutButton( capital );
		capital.setBounds(380, 390, 90 , 25 );
		capital.addActionListener(this);

		mission = new JRadioButton(resb.getString("newgame.mode.mission"));
		NewGameFrame.sortOutButton( mission );
		mission.setBounds(380, 410, 90 , 25 );
		mission.addActionListener(this);




		increasing = new JRadioButton(resb.getString("newgame.cardmode.increasing"),true);
		NewGameFrame.sortOutButton( increasing );
		increasing.setBounds(500,370,90,25);

		fixed = new JRadioButton(resb.getString("newgame.cardmode.fixed"));
		NewGameFrame.sortOutButton( fixed );
		fixed.setBounds(500,390,90,25);

                italian = new JRadioButton(resb.getString("newgame.cardmode.italianlike"));
		NewGameFrame.sortOutButton( italian );
		italian.setBounds(500,410,90,25);


		GameTypeButtonGroup.add ( domination );
		GameTypeButtonGroup.add ( capital );
		GameTypeButtonGroup.add ( mission );

		CardTypeButtonGroup.add ( fixed );
		CardTypeButtonGroup.add ( increasing );
                CardTypeButtonGroup.add ( italian );


		add(domination);
		add(capital);
		add(mission);

		add(fixed);
		add(increasing);
                add(italian);

		AutoPlaceAll = new JCheckBox(resb.getString("newgame.autoplace"));
		NewGameFrame.sortOutButton( AutoPlaceAll );
		AutoPlaceAll.setBounds(380, 440, 120 , 25 );

		recycle = new JCheckBox(resb.getString("newgame.recycle"));
		NewGameFrame.sortOutButton( recycle );
		recycle.setBounds(500, 440, 120 , 25 );

		add(AutoPlaceAll);
		add(recycle);

		int w=115;
		int h=31;

		cancel = new JButton(resb.getString("newgame.cancel"));
		NewGameFrame.sortOutButton( cancel , newgame.getSubimage(41, 528, w, h) , newgame.getSubimage(700, 233, w, h) , newgame.getSubimage(700, 264, w, h) );
		cancel.addActionListener( this );
		cancel.setBounds(41, 528, 115 , 31 );

		//help = new JButton();
		//NewGameFrame.sortOutButton( help , newgame.getSubimage(335, 528, 30 , 30) , newgame.getSubimage(794, 171, 30 , 30) , newgame.getSubimage(794, 202, 30 , 30) );
		//help.addActionListener( this );
		//help.setBounds(335, 529, 30 , 30 ); // should be 528

                JPanel bottompanel = new JPanel();
                bottompanel.setOpaque(false);
                
                bottompanel.add(new JLabel(resb.getString("newgame.label.name"))); // "Game Name:"
		gamename = new JTextField();
		bottompanel.add(gamename);

                bottompanel.add(new JLabel(resb.getString("newgame.label.timeout"))); // "Turn Timeout:"
                int hour = 60*60;
                Timeout[] timeouts = new Timeout[] {
                    new Timeout("1min",60),
                    new Timeout("5min",60*5),
                    new Timeout("10min",60*10),
                    new Timeout("20min",60*20),
                    new Timeout("30min",60*30),
                    new Timeout("1hour",hour),
                    new Timeout("2hours",hour*2),
                    new Timeout("6hours",hour*6),
                    new Timeout("12hours",hour*12),
                    new Timeout("24hours",hour*24)
                };
		timeout = new JComboBox(timeouts);
		bottompanel.add(timeout);
                
                bottompanel.setBounds(150, 525, 400 , 80 ); // should be 528
                add(bottompanel);

		start = new JButton(resb.getString("newgame.startgame"));
		NewGameFrame.sortOutButton( start , newgame.getSubimage(544, 528, w, h) , newgame.getSubimage(700, 295, w, h) , newgame.getSubimage(700, 326, w, h) );
		start.addActionListener( this );
		start.setBounds(544, 528, 115 , 31 );

		add(cancel);
		//add(help);
		add(start);

		list.setFixedCellHeight(33);

	}
        private JComboBox timeout;
        class Timeout {
            String name;
            int time;
            Timeout(String name,int time) {
                this.name = resb.getString("newgame.timeout."+name);
                this.time = time;
            }
            int getTime() {
                return time;
            }
            public String toString() {
                return name;
            }
        }

        private String newGameOptions;
	//private String mapsurl;
	private RiskMap[] maps;
        
        public Game showDialog(Window parent,String serveroptions, String myname) {
            
                gamename.setText( myname+"'s "+RiskUtil.GAME_NAME+" Game" );
            
                if (dialog == null) {

                        // TODO parent is passed in each time but is only used the first time
                        if (parent instanceof Frame) {
                            dialog = new JDialog((Frame)parent,"Game Options",true);
                        }
                        else {
                            dialog = new JDialog((Dialog)parent,"Game Options",true);
                        }

			// @todo:
			// do noting on close
			// when close then send event to gsp

			dialog.setContentPane(this);
			dialog.setResizable(false);
			dialog.pack();

		}


		if (serveroptions!=null && !serveroptions.equals(newGameOptions) ) {

			newGameOptions = serveroptions;

			String[] split = newGameOptions.split(",");

			maps = new RiskMap[split.length];

			for (int c=0;c<split.length;c++) {
				maps[c] = getRiskMap(split[c]);
			}

			setMaps(maps);

			final javax.swing.JList list = getList();

			new Thread() {
				public void run() {
					for (int c=0;c<maps.length;c++) {
						maps[c].loadInfo();
						if (c==0) {
							setSelected(c);
						}
						list.repaint();
					}
				}

			}.start();
		}


		reset();

		dialog.setVisible(true);

		String op = getOptions();

		if (op!=null) { return new Game( getGameName(), op, getNumberOfHumanPlayers(), ((Timeout)timeout.getSelectedItem()).getTime() ); }

		return null;

        }

        private static Map MapMap;
        public static RiskMap getRiskMap(String name) {

		//Risk.setupMapsDir(applet);

		if (MapMap==null) { MapMap = new HashMap(); }

		RiskMap themap = (RiskMap)MapMap.get(name);

		if (themap==null) {

			themap = new RiskMap(name);

			MapMap.put(name,themap);

		}

		return themap;

	}
        
        
	public void setMaps(final RiskMap[] maps) {

		list.setListData(maps);

	}

	public JList getList() {

		return list;
	}

	public void setSelected(int a) {

		list.setSelectedIndex(a);

	}

	public JPanel makeNewMission(String a) {


			JPanel mission = new JPanel() {

				public void paintComponent(Graphics g) {

					g.setColor( new Color(255,255,255,100) );
					g.fillRect(0,0,getWidth(),getHeight());

				}
			};

			//Dimension size = new Dimension(290, 35);

			//mission.setPreferredSize(size);
			//mission.setMinimumSize(size);
			//mission.setMaximumSize(size);
			mission.setOpaque(false);
			mission.setLayout( new BorderLayout() );

			JLabel cl = new JLabel( new ImageIcon(card) );
			cl.setBorder( BorderFactory.createEmptyBorder(1,3,1,3) );

			mission.add( cl , BorderLayout.WEST );

			JTextArea text = new JTextArea(a);
			text.setLineWrap(true);
			text.setWrapStyleWord(true);
			text.setEditable(false);

			text.setOpaque(false);

			//JScrollPane sp = new JScrollPane(text);
			//sp.setBorder(null);

			//sp.setOpaque(false);
			//sp.getViewport().setOpaque(false);

			//mission.add( sp );
			mission.add( text );

			//mission.setBackground( new Color(255,255,255,100) );

			return mission;


	}

	public String getOptions() {

		return options;

	}

	public String getGameName() {

		return gamename.getText();

	}

	public int getNumberOfHumanPlayers() {

		return ((Integer)human.getValue()).intValue();

	}

	public void paintComponent(Graphics g) {

			((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

//			  destination		source
			g.drawImage(newgame,0,0,this);


			g.drawImage(game2.getSubimage(0,0,223,155), 41 ,185 ,this);
			g.drawImage(game2.getSubimage(25,155,169,127), 391 ,325 ,this );

			g.setColor( Color.black );

			g.drawString( resb.getString("newgame.label.map"), 55, 40);
			g.drawString( "Missions:", 350, 40);

			g.drawString( "Number of Players", 440, 300);

			//g.drawString( "Game Name:", 240, 545);

			g.drawString( resb.getString("newgame.label.gametype"), 400, 365);
			g.drawString( resb.getString("newgame.label.cardsoptions"), 515, 365);


	}

	public void reset() {

		options=null;
	}
        
	/**
	 * Actionlistener applies the correct command to the button pressed
	 * @param e The ActionEvent Object
	 */
	public void actionPerformed(ActionEvent e) {

		if (e.getSource()==start) {

			int a = ((Integer)human.getValue()).intValue();
			//int b = ((Integer)aipassive.getValue()).intValue();
			int c = ((Integer)aieasy.getValue()).intValue();
			int d = ((Integer)aihard.getValue()).intValue();

			int sum = a+c+d;

			if (sum >=2 && sum <= RiskGame.MAX_PLAYERS) {

                                int gameMode;
                                int cardsMode;
                            
                                if (domination.isSelected()) gameMode = RiskGame.MODE_DOMINATION;
                                else if (capital.isSelected()) gameMode = RiskGame.MODE_CAPITAL;
                                else gameMode = RiskGame.MODE_SECRET_MISSION; // if (mission.isSelected())

                                if (increasing.isSelected()) cardsMode = RiskGame.CARD_INCREASING_SET;
                                else if (fixed.isSelected()) cardsMode = RiskGame.CARD_FIXED_SET;
                                else cardsMode = RiskGame.CARD_ITALIANLIKE_SET; // if (italian.isSelected())

				options = RiskUtil.createGameString(0,c,d,gameMode, cardsMode, AutoPlaceAll.isSelected(), recycle.isSelected(), riskmap.getFileName() );

				dialog.setVisible(false);

			}
			else {

				JOptionPane.showMessageDialog(this, resb.getString("newgame.error.numberofplayers") , resb.getString("newgame.error.title"), JOptionPane.ERROR_MESSAGE );

			}

		}/*
		else if (e.getSource()==help) {

			try {
				Risk.openDocs( resb.getString("helpfiles.flash") );
			}
			catch(Exception er) {
				JOptionPane.showMessageDialog(this,"Unable to open manual: "+er.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
			}

		}*/
		else if (e.getSource()==cancel) {

			dialog.setVisible(false);

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



    class RiskMapListCellRenderer extends DefaultListCellRenderer {
       public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus)
        {
            Component retValue = super.getListCellRendererComponent(
		list, value, index, isSelected, cellHasFocus
 	    );



		if (isSelected) { setBackground( new Color(255,255,255,100) ); }
		else { setBackground( new Color(0,0,0,0) ); }

            setIcon( ((RiskMap)value).getIcon() );
	    return retValue;
        }

    }



}
