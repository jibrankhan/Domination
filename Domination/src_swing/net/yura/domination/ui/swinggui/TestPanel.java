// Yura Mamyrin

package net.yura.domination.ui.swinggui;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.AbstractTableModel;
import net.yura.domination.engine.ColorUtil;
import net.yura.domination.engine.Risk;
import net.yura.domination.engine.RiskUIUtil;
import net.yura.domination.engine.RiskUtil;
import net.yura.domination.engine.ai.AIPlayer;
import net.yura.domination.engine.core.Card;
import net.yura.domination.engine.core.Continent;
import net.yura.domination.engine.core.Country;
import net.yura.domination.engine.core.Player;
import net.yura.domination.engine.core.RiskGame;
import net.yura.domination.engine.guishared.PicturePanel;
import net.yura.domination.ui.flashgui.MainMenu;

/**
 * @author Yura Mamyrin
 */

public class TestPanel extends JPanel implements ActionListener, SwingGUITab {

	private Risk myrisk;

	private JToolBar toolbar;

	private AbstractTableModel countriesModel;
	private AbstractTableModel continentsModel;
	private AbstractTableModel cardsModel,cardsModel2;
	private AbstractTableModel playersModel;
        private AbstractTableModel gameInfo;
        private AbstractTableModel commands;

	private PicturePanel pp;

	public TestPanel(Risk r,PicturePanel p) {

		myrisk = r;
		pp=p;

		setName( "Testing" );

		setOpaque(false);

		toolbar = new JToolBar();

		toolbar.setRollover(true);
		toolbar.setFloatable(false);

		JButton refresh = new JButton("Refresh");
		refresh.setActionCommand("refresh");
		refresh.addActionListener(this);
		toolbar.add(refresh);

		JButton allcards = new JButton("All Cards");
		allcards.setActionCommand("allcards");
		allcards.addActionListener(this);
		toolbar.add(allcards);

		toolbar.addSeparator();

		JButton flash = new JButton("Run FlashGUI with current backend");
		flash.setActionCommand("flash");
		flash.addActionListener(this);
		toolbar.add(flash);

		toolbar.addSeparator();

		JButton changeaiwait = new JButton("Change AI wait");
		changeaiwait.setActionCommand("aiwait");
		changeaiwait.addActionListener(this);
		toolbar.add(changeaiwait);

		countriesModel = new AbstractTableModel() {

			private final String[] columnNames = { "Color/No.","ID", "Name", "Continent", "Owner" , "Armies","No. Neighbours", "x", "y" };

			public int getColumnCount() {
				return columnNames.length;
			}

			public int getRowCount() {

				RiskGame game = myrisk.getGame();

				if (game != null) {

					Country[] countries = game.getCountries();

					if (countries != null) {

						return countries.length;
					}
				}

				return 0;
  			}

			public String getColumnName(int col) {
				return columnNames[col];
			}

			public Object getValueAt(int row, int col) {

				Country country = myrisk.getGame().getCountries()[row];

				switch(col) {

					case 0: return new Integer( country.getColor() );
					case 1: return country.getIdString();
					case 2: return country.getName();
					case 3: return country.getContinent();
					case 4: return country.getOwner();
					case 5: return new Integer( country.getArmies() );
					case 6: return new Integer( country.getNeighbours().size() );
					case 7: return new Integer( country.getX() );
					case 8: return new Integer( country.getY() );
					default: throw new RuntimeException();

				}

			}

		};




		continentsModel = new AbstractTableModel() {

			private final String[] columnNames = { "No.", "ID","Name", "Army Value", "No. Countries","Color" };

			public int getColumnCount() {
				return columnNames.length;
			}

			public int getRowCount() {

				RiskGame game = myrisk.getGame();

				if (game != null) {

					Continent[] continents = game.getContinents();

					if (continents != null) {

						return continents.length;
					}
				}

				return 0;
  			}

			public String getColumnName(int col) {
				return columnNames[col];
			}

			public Object getValueAt(int row, int col) {

				Continent continent = myrisk.getGame().getContinents()[row];

				switch(col) {

					case 0: return new Integer( row+1 );
					case 1: return continent.getIdString();
					case 2: return continent.getName();
					case 3: return new Integer( continent.getArmyValue() );
					case 4: return new Integer( continent.getTerritoriesContained().size() );
					case 5: return ColorUtil.getStringForColor( continent.getColor() );
					default: throw new RuntimeException();

				}

			}

		};


		cardsModel = new CardsTableModel() {
                    List getCards() {
                        RiskGame game = myrisk.getGame();
                        if (game!=null) {
                            List l = game.getCards();
                            return l==null?Collections.EMPTY_LIST:l;
                        }
                        return Collections.EMPTY_LIST;
                    }
                };

                cardsModel2 = new CardsTableModel() {
                    List getCards() {
                        RiskGame game = myrisk.getGame();
                        if (game!=null) {
                            List l = game.getUsedCards();
                            return l==null?Collections.EMPTY_LIST:l;
                        }
                        return Collections.EMPTY_LIST;
                    }
                };

		playersModel = new AbstractTableModel() {

			private final String[] columnNames = { "Name", "Color", "Type", "Extra Armies", "No. Cards", "No. Countries", "No. Player Eliminated", "Capital", "Mission", "Address", "autodefend","autoendgo"};

			public int getColumnCount() {
				return columnNames.length;
			}

			public int getRowCount() {

				RiskGame game = myrisk.getGame();

				if (game != null) {

					Vector players = game.getPlayers();

					if (players != null) {

						return players.size();
					}
				}

				return 0;
  			}

			public String getColumnName(int col) {
				return columnNames[col];
			}

			public Object getValueAt(int row, int col) {

				Player player = (Player)myrisk.getGame().getPlayers().elementAt(row);

				switch(col) {

					case 0: return player.getName();
					case 1: return ColorUtil.getStringForColor( player.getColor() );
					case 2: switch( player.getType() ) {

							case Player.PLAYER_HUMAN: return "Human";
							case Player.PLAYER_AI_CRAP: return "AI Crap";
							case Player.PLAYER_AI_EASY: return "AI Easy";
							case Player.PLAYER_AI_HARD: return "AI Hard";
							default: throw new RuntimeException();

						}
					case 3: return new Integer( player.getExtraArmies() );
					case 4: return new Integer( player.getCards().size() );
					case 5: return new Integer( player.getNoTerritoriesOwned() );
					case 6: return new Integer( player.getPlayersEliminated().size() );
					case 7: return player.getCapital();
					case 8: return player.getMission();
					case 9: return player.getAddress();
					case 10: return new Boolean( player.getAutoDefend() );
					case 11: return new Boolean( player.getAutoEndGo() );
					default: throw new RuntimeException();

				}

			}

		};

                
                gameInfo = new ObjectTableModel() {
                    public Object getObject() {
                        return myrisk.getGame();
                    }
		};
                
                commands = new AbstractTableModel() {

			private final String[] columnNames = { "No", "Command"};

			public int getColumnCount() {
				return columnNames.length;
			}

			public int getRowCount() {
				RiskGame game = myrisk.getGame();
				if (game != null) {
					Vector players = game.getCommands();
					if (players != null) {
						return players.size();
					}
				}
				return 0;
  			}

			public String getColumnName(int col) {
				return columnNames[col];
			}

			public Object getValueAt(int row, int col) {
				Object command = myrisk.getGame().getCommands().elementAt(row);
				switch(col) {
					case 0: return String.valueOf( row );
					case 1: return String.valueOf(command);
					default: throw new RuntimeException();
				}
			}

		};
                
                
		JTabbedPane views = new JTabbedPane();

		views.add( "Countries" , new JScrollPane(new JTable(countriesModel)) );
		views.add( "Continents" , new JScrollPane(new JTable(continentsModel)) );
		views.add( "Cards" , new JScrollPane(new JTable(cardsModel)) );
                views.add( "Spent Cards" , new JScrollPane(new JTable(cardsModel2)) );
		views.add( "Players" , new JScrollPane(new JTable(playersModel)) );
                views.add( "Game" , new JScrollPane(new JTable(gameInfo)) );
                views.add( "Commands" , new JScrollPane(new JTable(commands)) );

		setLayout( new BorderLayout() );
		add( views );

	}
        
        abstract class ObjectTableModel extends AbstractTableModel {
            
                private final String[] columnNames = { "Name", "Value" };
                private Field[] fields;

                public int getColumnCount() {
                        return columnNames.length;
                }

                public int getRowCount() {
                        Object game = getObject();
                        if (game != null) {
                            if (fields==null) {
                                Field[] fs = game.getClass().getDeclaredFields();
                                List result = new ArrayList();
                                for (int c=0;c<fs.length;c++) {
                                    if (!java.lang.reflect.Modifier.isStatic(fs[c].getModifiers())) {
                                        fs[c].setAccessible(true);
                                        result.add(fs[c]);
                                    }
                                }
                                fields = (Field[])result.toArray(new Field[result.size()]);
                            }
                            return fields.length;
                        }
                        return 0;
                }

                public String getColumnName(int col) {
                        return columnNames[col];
                }

                public Object getValueAt(int row, int col) {
                        Object game = getObject();
                        switch(col) {
                                case 0: return fields[row].getName();
                                case 1: {
                                    try {
                                        return String.valueOf( fields[row].get(game) );
                                    }
                                    catch (Exception ex){
                                        return ex.toString();
                                    }
                                }
                                default: throw new RuntimeException();
                        }
                }
                
                public abstract Object getObject();
            
        }

        abstract class CardsTableModel extends AbstractTableModel {

                private final String[] columnNames = { "No.", "Type", "Country" };

                abstract List getCards();
                
                public int getColumnCount() {
                        return columnNames.length;
                }

                public int getRowCount() {
                        return getCards().size();
                }

                public String getColumnName(int col) {
                        return columnNames[col];
                }

                public Object getValueAt(int row, int col) {

                        Card card = (Card)getCards().get(row);

                        switch(col) {

                                case 0: return new Integer( row+1 );
                                case 1: return card.getName();
                                case 2: return card.getCountry();
                                default: throw new RuntimeException();

                        }

                }

        }

	public void actionPerformed(ActionEvent a) {

		if (a.getActionCommand().equals("refresh")) {

			countriesModel.fireTableDataChanged();
			continentsModel.fireTableDataChanged();
			cardsModel.fireTableDataChanged();
                        cardsModel2.fireTableDataChanged();
			playersModel.fireTableDataChanged();
                        gameInfo.fireTableDataChanged();
                        commands.fireTableDataChanged();

			repaint();
		}
		else if (a.getActionCommand().equals("flash")) {

			MainMenu.newMainMenuFrame( myrisk, JFrame.DISPOSE_ON_CLOSE );

		}

		else if (a.getActionCommand().equals("aiwait")) {

			Object[] message = new Object[2];
			message[0] = new JLabel("AI wait time (in milliseconds):");
			message[1] = new JSpinner( new SpinnerNumberModel( AIPlayer.getWait(),0,10000,100 ) );

			String[] options = {
			    "OK", 
			    "cancel"
			}; 

			int result = JOptionPane.showOptionDialog( 
			    this,                             // the parent that the dialog blocks 
			    message,                                    // the dialog message array 
			    "AI Options", // the title of the dialog window 
			    JOptionPane.OK_CANCEL_OPTION,                 // option type 
			    JOptionPane.PLAIN_MESSAGE,            // message type 
			    null,                                       // optional icon, use null to use the default icon 
			    options,                                    // options string array, will be made into buttons 
			    options[0]                                  // option that should be made into a default button 
			);

			if (result == JOptionPane.OK_OPTION ) {

				AIPlayer.setWait( ((Integer)((JSpinner)message[1]).getValue()).intValue() );
			}

		}
		else if (a.getActionCommand().equals("allcards")) {

			if (myrisk.getGame()!=null && myrisk.getGame().getCards()!=null) {

				Frame frame = RiskUIUtil.findParentFrame(this);

				CardsDialog cardsDialog = new CardsDialog( frame ,pp, false, myrisk , false );
				Dimension frameSize = frame.getSize();
				Dimension aboutSize = cardsDialog.getPreferredSize();
				int x = frame.getLocation().x + (frameSize.width - aboutSize.width) / 2;
				int y = frame.getLocation().y + (frameSize.height - aboutSize.height) / 2;
				if (x < 0) x = 0;
				if (y < 0) y = 0;
				cardsDialog.setLocation(x, y);

				cardsDialog.populate( myrisk.getGame().getCards() );

				cardsDialog.setVisible(true);
			}
		}
		else {

			throw new RuntimeException("TestTab: unknown command found: "+a.getActionCommand());

		}

	}

	public JToolBar getToolBar() {

		return toolbar;

	}
	public JMenu getMenu() {

		return null;

	}

}
