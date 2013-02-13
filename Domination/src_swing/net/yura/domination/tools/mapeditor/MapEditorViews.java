// Yura Mamyrin

package net.yura.domination.tools.mapeditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.yura.domination.engine.RiskUIUtil;
import net.yura.domination.engine.core.Card;
import net.yura.domination.engine.core.Continent;
import net.yura.domination.engine.core.Country;
import net.yura.domination.engine.core.Mission;
import net.yura.domination.engine.core.Player;
import net.yura.domination.engine.core.RiskGame;

/**
 * <p> Options Dialog for SwingGUI </p>
 * @author Yura Mamyrin
 */

public class MapEditorViews extends JDialog implements ActionListener,ListSelectionListener {

    private MapEditorPanel editPanel;

    private RiskGame map;

    private JList countriesList;
    private JList continentsList;
    private JList cardsList;
    private JList missionsList;

    private JTabbedPane tabs;
    private String[] cardTypes;

    private JButton add;
    private JButton remove;
    private JButton edit;

    public MapEditorViews(Frame parent,MapEditorPanel ep) {

        super(parent, "Map Editor Views", false);

	editPanel = ep;

	JToolBar optionspanel = new JToolBar();

	add = new JButton("Add");
	add.setActionCommand("add");
	add.addActionListener( this );
	optionspanel.add(add);

	remove = new JButton("Remove");
	remove.setActionCommand("remove");
	remove.addActionListener( this );
	optionspanel.add(remove);

	edit = new JButton("Edit");
	edit.setActionCommand("edit");
	edit.addActionListener( this );
	optionspanel.add(edit);

	add.setEnabled(false);
	remove.setEnabled(false);
	edit.setEnabled(false);

	tabs = new JTabbedPane();

	countriesList = new JList( new CountriesListModel() );
	continentsList = new JList( new ContinentsListModel() );
	cardsList = new JList( new CardsListModel() );
	missionsList = new JList( new MissionsListModel() );

        MouseListener clicky = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    //int index = ((JList)e.getSource()).locationToIndex(e.getPoint());
                    //if (index>=0) {
                        edit.doClick();
                    //}
                }
            }
        };
        
        countriesList.addMouseListener( clicky );
	continentsList.addMouseListener( clicky );
	cardsList.addMouseListener( clicky );
	missionsList.addMouseListener( clicky );
        
	tabs.addTab("Countries",new JScrollPane( countriesList ));
	tabs.addTab("Continents",new JScrollPane( continentsList ));
	tabs.addTab("Cards",new JScrollPane( cardsList ));
	tabs.addTab("Missions",new JScrollPane( missionsList ));

	getContentPane().add(optionspanel, BorderLayout.SOUTH);
	getContentPane().add(tabs);

	cardTypes = new String[4];
	cardTypes[0] = Card.CAVALRY;
	cardTypes[1] = Card.INFANTRY;
	cardTypes[2] = Card.CANNON;
	cardTypes[3] = Card.WILDCARD;

	countriesList.addListSelectionListener(this);

        continentsList.setCellRenderer(new DefaultListCellRenderer() {
            
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                
                Continent con = (Continent)value;
                
                final Color color = new Color(con.getColor());
                //setBackground( color );
                //setForeground( RiskUIUtil.getTextColorFor(color) );
                
                setIcon( new Icon() {

                    public void paintIcon(Component c, Graphics g, int x, int y) {
                        g.setColor(color);
                        g.fillOval(x, y, getIconWidth(), getIconHeight());
                    }

                    public int getIconWidth() {
                        return 10;
                    }

                    public int getIconHeight() {
                        return 10;
                    }
                } );
                
                return c;
            }
        });
        
    }

    public void valueChanged(ListSelectionEvent e) {

	editPanel.setCountry( (Country)countriesList.getSelectedValue() );

    }

    public void setMap(RiskGame m) {

	map = m;

	((ViewTab)countriesList.getModel()).changed();
	((ViewTab)continentsList.getModel()).changed();
	((ViewTab)cardsList.getModel()).changed();
	((ViewTab)missionsList.getModel()).changed();

	add.setEnabled(true);
	remove.setEnabled(true);
	edit.setEnabled(true);

    }

    public void actionPerformed(ActionEvent a) {

	ViewTab current = ((ViewTab)((JList)((javax.swing.JViewport)((JScrollPane)tabs.getSelectedComponent()).getViewport()).getView()).getModel());


	if (a.getActionCommand().equals("add")) {

		current.add();

	}
	else if (a.getActionCommand().equals("remove")) {

		current.remove();

	}
	else if (a.getActionCommand().equals("edit")) {

		current.edit();

	}	
	else {

		throw new RuntimeException(a.getActionCommand());

	}

    }

	public int openOptionDialog(Object[] message,String title) {

			String[] options = {
			    "OK", 
			    "cancel"
			}; 

			int result = JOptionPane.showOptionDialog( 
			    this,                             // the parent that the dialog blocks 
			    message,                                    // the dialog message array 
			    title, // the title of the dialog window 
			    JOptionPane.OK_CANCEL_OPTION,                 // option type 
			    JOptionPane.PLAIN_MESSAGE,            // message type 
			    null,                                       // optional icon, use null to use the default icon 
			    options,                                    // options string array, will be made into buttons 
			    options[0]                                  // option that should be made into a default button 
			);

			return result;

	}

	public void removeCountries(Object[] a) {

		if (a.length>0) {

				Vector countries = new Vector( Arrays.asList( map.getCountries() ) );
				List removeList = Arrays.asList(a);

				countries.removeAll( removeList );

				Country[] newCountries = (Country[])countries.toArray( new Country[countries.size()] );

				Map updateMap = new HashMap();
				updateMap.put(new Integer(255),new Integer(255));

				for (int c=0;c<a.length;c++) {

					updateMap.put( new Integer(((Country)a[c]).getColor()), new Integer(255) );

				}

				for (int c=0;c<newCountries.length;c++) {

					updateMap.put( new Integer(newCountries[c].getColor()), new Integer(c+1) );
					newCountries[c].setColor(c+1);
					newCountries[c].getNeighbours().removeAll( removeList );
				}

				Vector cards = map.getCards();

				for (int c=0;c<cards.size();c++) {

					Card card = (Card)cards.elementAt(c);

					if (removeList.contains(card.getCountry())) {

						cards.remove(c);
						c--;
					}

				}


				Continent[] continents = map.getContinents();

				for (int c=0;c<continents.length;c++) {

					continents[c].getTerritoriesContained().removeAll( removeList );

				}


				map.setCountries(newCountries);

				editPanel.update(updateMap);

				//countriesList.revalidate();
				//cardsList.revalidate();
				//countriesList.clearSelection();
				//cardsList.clearSelection();

				((ViewTab)countriesList.getModel()).changed();
				((ViewTab)cardsList.getModel()).changed();

		}

	}

	interface ViewTab {

		void edit();
		void remove();
		void add();
		void changed();
	}


    class CountriesListModel extends AbstractListModel implements ViewTab {

	public void changed() {
		fireContentsChanged(this,0, map.getNoCountries()-1 );
		countriesList.clearSelection();
	}

	public Object getElementAt(int index) {

		if (map==null) { return null; }
		return map.getCountries()[index];

	}

	public int getSize() {

		if (map==null) { return 0; }
		return map.getNoCountries();

	}

	public void edit() {

		int sel = countriesList.getSelectedIndex();

		if (sel != -1) {

			Country country = (Country)getElementAt(sel);
			Continent oldContinent = country.getContinent();

			Object[] message = new Object[4];
			message[0] = new JLabel("Name:");
			message[1] = new JTextField( country.getIdString().replace('_',' ') );
			message[2] = new JLabel("Continent:");
			message[3] = new JComboBox( map.getContinents() );

			((JComboBox)message[3]).setSelectedItem( oldContinent );

			int result = openOptionDialog(message,"Edit country");

			if (result == JOptionPane.OK_OPTION ) {
                            String name = ((JTextField)message[1]).getText();
                            if (!"".equals(name)) {

				Continent newContinent = (Continent)((JComboBox)message[3]).getSelectedItem();

				country.setIdString( name.replace(' ','_') );
				country.setName( name );
				country.setContinent( newContinent );

				oldContinent.getTerritoriesContained().remove(country);
				newContinent.addTerritoriesContained(country);

				fireContentsChanged(this,sel, sel );

				editPanel.repaint();
                            }
			}

		}

	}
	public void remove() {

		Object[] a = countriesList.getSelectedValues();

		if (a.length!=0) {

			String countriesString="";

			for (int c=0;c<a.length;c++) {

				countriesString = countriesString + "\n" + a[c];

			}

			int result = JOptionPane.showConfirmDialog(MapEditorViews.this, "Are you sure you want to remove:"+
			countriesString
			, "Remove?", JOptionPane.YES_NO_OPTION);

			if (result == JOptionPane.YES_OPTION) {

				removeCountries( a );

			}



		}

	}
	public void add() {


			if (map.getContinents().length == 0) {

				JOptionPane.showMessageDialog(MapEditorViews.this,"Need to add a continent first");
				return;

			}


			Object[] message = new Object[4];
			message[0] = new JLabel("Names: (each line will be made into a new country)");
			message[1] = new OptionPaneTextArea( "" );
			message[2] = new JLabel("Select continent:");
			message[3] = new JComboBox( map.getContinents() );

			int result = openOptionDialog(message,"New countries");

			if (result == JOptionPane.OK_OPTION ) {

				String[] names = ((OptionPaneTextArea)message[1]).getLines();

				Country[] oldCountries = map.getCountries();

				if (oldCountries.length+names.length > 254) {

					JOptionPane.showMessageDialog(MapEditorViews.this,"Too many countries. 254 is the max number.");
					return;

				}

				Vector newCountries = new Vector( Arrays.asList(oldCountries) );

				Continent continent = (Continent)((JComboBox)message[3]).getSelectedItem();

				for (int c=0;c<names.length;c++) {

					Country country = new Country(oldCountries.length+c+1,names[c].replace(' ','_'),names[c],continent,20*(c+1),20);
					newCountries.add(country);
					continent.addTerritoriesContained(country);
				}

				map.setCountries( (Country[])newCountries.toArray( new Country[newCountries.size()] ) );

				fireContentsChanged(this,oldCountries.length, oldCountries.length+names.length-1 );

				editPanel.repaint();
			}


	}

    }


    class ContinentsListModel extends AbstractListModel implements ViewTab {

	public void changed() {
		super.fireContentsChanged(this, 0, map.getNoContinents()-1 );
		continentsList.clearSelection();
	}

	public Object getElementAt(int index) {

		if (map==null) { return null; }
		return map.getContinents()[index];

	}

	public int getSize() {

		if (map==null) { return 0; }
		return map.getNoContinents();

	}

	private Color editcolor;

	public void edit() {

		int sel = continentsList.getSelectedIndex();

		if (sel != -1) {

			Continent continent = (Continent)getElementAt(sel);

			final Object[] message = new Object[6];
			message[0] = new JLabel("Name:");
			message[1] = new JTextField( continent.getIdString().replace('_',' ') );
			message[2] = new JLabel("Army Value:");
			message[3] = new JSpinner( new SpinnerNumberModel(continent.getArmyValue(),0,100,1) );
			message[4] = new JLabel("Color:");
			message[5] = new JButton("change");

			editcolor = new Color( continent.getColor() );

			((JButton)message[5]).setBackground( editcolor );
			((JButton)message[5]).setForeground( RiskUIUtil.getTextColorFor(editcolor) );

			((JButton)message[5]).addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent a) {

						Color newcolor = JColorChooser.showDialog(MapEditorViews.this,"select color",editcolor);

						if (newcolor!=null) {

							editcolor = newcolor;

							((JButton)message[5]).setBackground( editcolor );
							((JButton)message[5]).setForeground( RiskUIUtil.getTextColorFor(editcolor) );
						}

					}
				}
			);

			int result = openOptionDialog(message,"Edit continent");

			if (result == JOptionPane.OK_OPTION ) {
                            String name = ((JTextField)message[1]).getText();
                            if (!"".equals(name)) { 

				continent.setIdString( name.replace(' ','_') );
				continent.setName(name);
				continent.setArmyValue( ((Integer)((JSpinner)message[3]).getValue()).intValue() );
				continent.setColor( editcolor.getRGB() );

				fireContentsChanged(this,sel, sel );

				editPanel.repaint();
                            }
			}

		}

	}
	public void remove() {


		Object[] a = continentsList.getSelectedValues();

		if (a.length!=0) {

			String continentsString="";

			for (int c=0;c<a.length;c++) {

				continentsString = continentsString + "\n" + a[c];

			}

			int result = JOptionPane.showConfirmDialog(MapEditorViews.this, "Are you sure you want to remove:"+
			continentsString
			, "Remove?", JOptionPane.YES_NO_OPTION);

			if (result == JOptionPane.YES_OPTION) {

				Vector continents = new Vector( Arrays.asList( map.getContinents() ) );
				List removeList = Arrays.asList(a);
				Vector removeCountreis = new Vector();

				for (int c=0;c<a.length;c++) {

					removeCountreis.addAll( ((Continent)a[c]).getTerritoriesContained() );

				}

				removeCountries( removeCountreis.toArray() );

				continents.removeAll(removeList);
				map.setContinents( (Continent[])continents.toArray( new Continent[continents.size()] ) );

				fireContentsChanged(this,0,getSize()-1);
				continentsList.clearSelection();


				Vector missions = map.getMissions();

				for (int c=0;c<missions.size();c++) {

					Mission mission = (Mission)missions.elementAt(c);

					if (removeList.contains( mission.getContinent1() )) {
						mission.setContinent1(null);
					}
					if (removeList.contains( mission.getContinent2() )) {
						mission.setContinent2(null);
					}
					if (removeList.contains( mission.getContinent3() )) {
						mission.setContinent3(null);
					}

				}

			}



		}




	}
	public void add() {


			Object[] message = new Object[4];
			message[0] = new JLabel("Names: (each line will be made into a new continent)");
			message[1] = new OptionPaneTextArea( "" );
			message[2] = new JLabel("Initial army value:");
			message[3] = new JSpinner( new SpinnerNumberModel( 5,0,100,1) );

			int result = openOptionDialog(message,"New continents");

			if (result == JOptionPane.OK_OPTION ) {

				int armies = ((Integer)((JSpinner)message[3]).getValue()).intValue();

				String[] names = ((OptionPaneTextArea)message[1]).getLines();

				Continent[] oldContinents = map.getContinents();
				Vector newContinents = new Vector( Arrays.asList(oldContinents) );

				for (int c=0;c<names.length;c++) {

					Continent continent = new Continent(names[c].replace(' ','_'),names[c],armies, RiskGame.getRandomColor() );
					newContinents.add(continent);
				}

				map.setContinents( (Continent[])newContinents.toArray( new Continent[newContinents.size()] ) );

				fireContentsChanged(this,oldContinents.length, oldContinents.length+names.length-1 );

				editPanel.repaint();
			}

	}

    }


    class CardsListModel extends AbstractListModel implements ViewTab {

	public void changed() {
		super.fireContentsChanged(this, 0, map.getNoCards()-1);
		cardsList.clearSelection();
	}

	public Object getElementAt(int index) {

		if (map==null) { return null; }
		return map.getCards().elementAt(index);

	}

	public int getSize() {

		if (map==null) { return 0; }
		return map.getNoCards();

	}

	public void edit() {


		int sel = cardsList.getSelectedIndex();

		if (sel != -1) {

			Card card = (Card)getElementAt(sel);

			Country country = map.getCountryInt(sel+1);

			Object[] message = new Object[2];
			message[0] = new JLabel("Type:");

			if (country == null || ( sel > 0 && ((Card)getElementAt(sel-1)).getName().equals(Card.WILDCARD) ) ) {
				message[1] = new JComboBox( new String[] { Card.WILDCARD } );
			}
			else if (
					( getSize()==(sel+1) || ((Card)getElementAt(sel+1)).getName().equals(Card.WILDCARD) ) &&
					( sel == 0 || !((Card)getElementAt(sel-1)).getName().equals(Card.WILDCARD) )
				) {
				message[1] = new JComboBox( cardTypes );
			}
			else {
				message[1] = new JComboBox( new String[] { Card.CAVALRY,Card.INFANTRY,Card.CANNON } );
			}

			//message[2] = new JLabel("Country:");
			//message[3] = new JComboBox( map.getCountries() );

			((JComboBox)message[1]).setSelectedItem( card.getName() );
			//((JComboBox)message[3]).setSelectedItem( card.getCountry() );

			int result = openOptionDialog(message,"Edit card");

			if (result == JOptionPane.OK_OPTION ) {

				String newtype = (String)((JComboBox)message[1]).getSelectedItem();

				card.setName(newtype);

				if (Card.WILDCARD.equals(newtype)) {
					card.setCountry(null);
				}
				else {
					//card.setCountry( (Country)((JComboBox)message[3]).getSelectedItem() );

					card.setCountry( country );
				}

				fireContentsChanged(this,sel, sel );
			}

		}

	}
	public void remove() {

		Object[] a = cardsList.getSelectedValues();

		if (a.length!=0) {

			String cardsString="";

			for (int c=0;c<a.length;c++) {

				cardsString = cardsString + "\n" + ((Card)a[c]).getName();

			}

			int result = JOptionPane.showConfirmDialog(MapEditorViews.this, "Are you sure you want to remove:"+
			cardsString
			, "Remove?", JOptionPane.YES_NO_OPTION);

			if (result == JOptionPane.YES_OPTION) {

				Vector cards = map.getCards();
				cards.removeAll( Arrays.asList(a) );

				for (int c=0;c<cards.size();c++) {

					Card card = (Card)cards.elementAt(c);

					if (!card.getName().equals(Card.WILDCARD)) {
						card.setCountry( map.getCountryInt(c+1) );
					}
				}

				fireContentsChanged(this,0,getSize()-1);
				cardsList.clearSelection();


			}



		}

	}
	public void add() {


			Object[] message = new Object[2];
			message[0] = new JLabel("Amount of new cards:");
			message[1] = new JSpinner( new SpinnerNumberModel( 20,0,500,1) );

			int result = openOptionDialog(message,"New Cards");

			if (result == JOptionPane.OK_OPTION ) {

				int number = ((Integer)((JSpinner)message[1]).getValue()).intValue();
				int n = number;

				Vector cards = map.getCards();
				Vector wilds = new Vector();

				for (int c=0;c<cards.size();c++) {

					if ( ((Card)cards.elementAt(c)).getName().equals(Card.WILDCARD) ) {

						wilds.add( cards.remove(c) );
						c--;
					}

				}

				Country[] countries = map.getCountries();

				for (int c=cards.size(); c<countries.length && number>0;c++) {

					cards.add( new Card(

						((c%3)==0)?Card.CAVALRY:
						(  ((c%3)==1)?Card.INFANTRY:Card.CANNON  )

						,countries[c]) );
					number--;
				}

				for (int c=0;c<number;c++) {

					cards.add( new Card(Card.WILDCARD,null) );

				}

				cards.addAll(wilds);

				fireContentsChanged(this,cards.size()-n, cards.size()-1 );
			}

	}

    }


    class MissionsListModel extends AbstractListModel implements ViewTab {

	public void changed() {
		super.fireContentsChanged(this, 0, map.getNoMissions()-1);
		missionsList.clearSelection();
	}

	public Object getElementAt(int index) {

		if (map==null) { return null; }
		return map.getMissions().elementAt(index);

	}

	public int getSize() {

		if (map==null) { return 0; }
		return map.getNoMissions();

	}

	private JComboBox makeContinentsJComboBox() {

		JComboBox a = new JComboBox( map.getContinents() );
		a.insertItemAt(RiskGame.ANY_CONTINENT,0);
		a.insertItemAt(null,0);
		return a;

	}

	private Object[] newBox(int a,int b,String text,Player p,Continent c1,Continent c2, Continent c3) {

			Object[] message = new Object[14];
			message[0] = new JLabel("Player:");
			message[1] = new JComboBox( map.getPlayers().toArray() );
			((JComboBox)message[1]).insertItemAt(null,0);

			message[2] = new JLabel("Number of countries:");
			message[3] = new JSpinner( new SpinnerNumberModel(a,0,100,1) );

			message[4] = new JLabel("Number of armies:");
			message[5] = new JSpinner( new SpinnerNumberModel(b,0,100,1) );

			message[6] = new JLabel("Continent 1:");
			message[7] = makeContinentsJComboBox();

			message[8] = new JLabel("Continent 2:");
			message[9] = makeContinentsJComboBox();

			message[10] = new JLabel("Continent 3:");
			message[11] = makeContinentsJComboBox();

			message[12] = new JLabel("Discription:");
			message[13] = new OptionPaneTextArea(text);


			// set all the values from the current one
			((JComboBox)message[1]).setSelectedItem( p );

			((JComboBox)message[7]).setSelectedItem( c1 );
			((JComboBox)message[9]).setSelectedItem( c2 );
			((JComboBox)message[11]).setSelectedItem( c3 );

			return message;

	}

	public void edit() {


		int sel = missionsList.getSelectedIndex();

		if (sel != -1) {

			Mission mission = (Mission)getElementAt(sel);

			Object[] message = newBox(

				mission.getNoofcountries(),
				mission.getNoofarmies(),
				mission.getDiscription(),
				mission.getPlayer(),
				mission.getContinent1(),
				mission.getContinent2(),
				mission.getContinent3()
			);

			int result = openOptionDialog(message,"Edit mission");

			if (result == JOptionPane.OK_OPTION ) {

				mission.setPlayer( (Player) ((JComboBox)message[1]).getSelectedItem() );
				mission.setNoofcountries( ((Integer)((JSpinner)message[3]).getValue()).intValue() );
				mission.setNoofarmies( ((Integer)((JSpinner)message[5]).getValue()).intValue() );
				mission.setContinent1( (Continent) ((JComboBox)message[7]).getSelectedItem() );
				mission.setContinent2( (Continent) ((JComboBox)message[9]).getSelectedItem() );
				mission.setContinent3( (Continent) ((JComboBox)message[11]).getSelectedItem() );
				mission.setDiscription( ((OptionPaneTextArea)message[13]).getLineText() );

				fireContentsChanged(this,sel, sel );

			}

		}


	}
	public void remove() {

		Object[] m = missionsList.getSelectedValues();

		if (m.length!=0) {

			String missionsString="";

			for (int c=0;c<m.length;c++) {

				missionsString = missionsString + "\n" + m[c];

			}

			int result = JOptionPane.showConfirmDialog(MapEditorViews.this, "Are you sure you want to remove:"+
			missionsString
			, "Remove?", JOptionPane.YES_NO_OPTION);

			if (result == JOptionPane.YES_OPTION) {

				Vector missions = map.getMissions();

				missions.removeAll( Arrays.asList(m) );

				fireContentsChanged(this,0,missions.size()-1);
				missionsList.clearSelection();
			}



		}



	}
	public void add() {


			Object[] message = newBox(0,0,"",null,null,null,null);

			int result = openOptionDialog(message,"New mission");

			if (result == JOptionPane.OK_OPTION ) {

				Mission mission = new Mission(

					(Player) ((JComboBox)message[1]).getSelectedItem(),
					((Integer)((JSpinner)message[3]).getValue()).intValue(),
					((Integer)((JSpinner)message[5]).getValue()).intValue(),
					(Continent) ((JComboBox)message[7]).getSelectedItem(),
					(Continent) ((JComboBox)message[9]).getSelectedItem(),
					(Continent) ((JComboBox)message[11]).getSelectedItem(),
					((OptionPaneTextArea)message[13]).getLineText()

				);

				Vector missions = map.getMissions();

				missions.add(mission);

				fireContentsChanged(this,missions.size()-1,missions.size()-1);

			}


	}

    }

    static class OptionPaneTextArea extends JScrollPane {

	private JTextArea textarea;

	public OptionPaneTextArea(String a) {
		super();

		textarea = new JTextArea( a , 5, 20 );
		textarea.setWrapStyleWord(true);
		textarea.setLineWrap(true);

		setViewportView(textarea);

	}

	public String getText() {

		return textarea.getText();

	}

	public String getLineText() {

		return getText().replace('\n',' ');

	}

	public String[] getLines() {
            String[] strings = getText().split("\\n");
            Vector goodStrings = new Vector();
            for (int c=0;c<strings.length;c++) {
                if (!"".equals(strings[c])) {
                    goodStrings.add( strings[c] );
                }
            }
            return (String[]) goodStrings.toArray( new String[goodStrings.size()] );
	}

    }

}

