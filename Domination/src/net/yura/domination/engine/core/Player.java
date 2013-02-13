// Yura Mamyrin, Group D

package net.yura.domination.engine.core;

//import java.awt.Color;
import java.io.Serializable;
import java.util.List;
import java.util.Vector;

/**
 * <p> Risk Player </p>
 * @author Yura Mamyrin
 */

public class Player implements Serializable {

	private static final long serialVersionUID = 1L;

	public final static int PLAYER_HUMAN   = 0;
	public final static int PLAYER_AI_CRAP = 3;
	public final static int PLAYER_AI_EASY = 1;
	public final static int PLAYER_AI_HARD = 2;


    private String name;
    private int color;
    private int extraArmies;
    private Vector cardsOwned;
    private Vector territoriesOwned;
    private Vector playersEliminated;
    private Country capital;
    private Mission mission;
    private int type;
    private String address;

    private Vector Statistics;
    protected Statistic currentStatistic;

    private boolean autoendgo;
    private boolean autodefend;
    // private int attackUntill;

    /**
     * Creates a new Player
     * @param t The type of the player (human or AI)
     * @param n The name of the player
     * @param c The color of the player
     * @param a THe address of the player on the current computer
     */
    public Player(int t, String n, int c, String a) {
	type = t;
	name = n;
	color = c;
        territoriesOwned = new Vector();
        cardsOwned = new Vector();
	playersEliminated = new Vector();
        extraArmies = 0;
	address=a;

	autoendgo=true;

	// well everyone wants this really, i hope
	autodefend=true;

	Statistics = new Vector();
	currentStatistic = new Statistic();
	Statistics.add(currentStatistic);

    }

    public void rename(String na) {

	name = na;

    }

    public String toString() {

	return name;

    }

    public void nextTurn() {

	currentStatistic = new Statistic();
	Statistics.add(currentStatistic);

    }

    public int[] getStatistics(int a) {

	int[] thing = new int[ Statistics.size() ];

	for (int c=0; c< thing.length ; c++) {

	    thing[c] = ((Statistic)Statistics.elementAt(c)).get(a);

	}

	return thing;
    }
    
    public List<Statistic> getStatistics() {
		return Statistics;
	}

    public int getNoArmies() {

	int n=0;

	// add new armies for the Continents Owned
	for (int c=0; c< territoriesOwned.size() ; c++) {

	    n = n + ((Country)territoriesOwned.elementAt(c)).getArmies();

	}

	return n;

    }

    /**
     * Gets the address of the player on the current computer
     * @return String The name of the player
     */
    public String getAddress() {
        return address;
    }

    /**
     * Gets the name of the player
     * @return String The name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the color of the player
     * @return Color Returns the color of the player
     */
    public int getColor() {
	return color;
    }

    /**
     * Gets the number of extra armies that the player has
     * @return int Returns the number of extra armies that the player has
     */
    public int getExtraArmies() {
	return extraArmies;
    }

    /**
     * Adds the number of armies to the player's extra armies
     * @param n The number of armies you want to add
     */
    public void addArmies(int n) {
	extraArmies = extraArmies + n;
    }

    /**
     * Minus the number of armies from the player's extra armies
     * @param n The number of armies you want to minus
     */
    public void loseExtraArmy(int n) {
        extraArmies = extraArmies - n;
    }

    /**
     * Gains a card
     * @param card The card you have gained
     */
    public void giveCard(Card card) {
        cardsOwned.add(card);
    }

    /**
     * Gets the cards that the player owns
     * @return Vector Returns the vector of all the cards the player owns
     */
    public Vector getCards() {
        return cardsOwned;
    }

    /**
     * Gets the captical that the player chose
     * @return Country Returns the player's capital country
     */
    public Country getCapital() {
	return capital;
    }

    /**
     * Sets the player's capital country
     * @param c The new capital country
     */
    public void setCapital(Country c) {
	capital=c;
    }

    /**
     * Gets the player's mission
     * @return Mission Returns player's mission
     */
    public Mission getMission() {
	return mission;
    }

    /**
     * Sets the player's mission
     * @param m The new mission
     */
    public void setMission(Mission m) {
	mission=m;
    }

    /**
     * Takes the player's first card
     * @return Card The card that is taken
     */
    public Card takeCard() {
	Card c=(Card)cardsOwned.firstElement();
	cardsOwned.removeElementAt(0);
        return c;
    }

    public static final int noaFORcard = 2;
    
    /**
     * Trading in the cards, adds 2 extra armies to the first country that it owns on the cards and removing the cards from the player
     * @param card1 First card
     * @param card2 Second card
     * @param card3 Third card
     */
    public void tradeInCards(Card card1, Card card2, Card card3) {

	// check if you should get extra armies on the territoriesOwned
	if (territoriesOwned.contains( card1.getCountry() ) ) {
	    ((Country)card1.getCountry()).addArmies(noaFORcard);
	    currentStatistic.addReinforcements(noaFORcard);
	}
	else if (territoriesOwned.contains( card2.getCountry() ) ) {
	    ((Country)card2.getCountry()).addArmies(noaFORcard);
	    currentStatistic.addReinforcements(noaFORcard);
	}
	else if (territoriesOwned.contains( card3.getCountry() ) ) {
	    ((Country)card3.getCountry()).addArmies(noaFORcard);
	    currentStatistic.addReinforcements(noaFORcard);
	}

	cardsOwned.remove(card1);
	cardsOwned.remove(card2);
	cardsOwned.remove(card3);
	cardsOwned.trimToSize();
    }


    /**
     * Gets the countries that the player owns
     * @return Vector Returns a vector of all the countries the player owns
     */
    public Vector getTerritoriesOwned() {
        return territoriesOwned;
    }

    /**
     * Gets the number of countries that the player owns
     * @return int Returns the number of countries owned
     */
    public int getNoTerritoriesOwned() {
        return territoriesOwned.size();
    }

    public void newCountry(Country newCountry) {
        territoriesOwned.addElement(newCountry);
    }

    /**
     * Removes a country from the player
     * @param lessCountry The country you want to remove
     */
    public void lostCountry(Country lessCountry){
	territoriesOwned.remove(lessCountry);
	territoriesOwned.trimToSize();
    }

    /**
     * Eliminates a player
     * @param p The player to be eliminated
     */
    public void addPlayersEliminated(Player p) {
	playersEliminated.add(p);
    }

    /**
     * Gets all the players that are eliminated
     * @return Vector Returns all the eliminated players
     */
    public Vector getPlayersEliminated() {
	return playersEliminated;
    }

    /**
     * Gets the type of player
     * @return int Returns the type of player
     */
    public int getType() {
	return type;
    }

    /**
     * Sets the type of player
     * @param t The type of player
     */
    public void setType(int t) {
	type =t;
    }

    /**
     * Sets the address of the current player
     * @param a String The address of the computer
     */
    public void setAddress(String a) {
	address =a;
    }

    public void setAutoEndGo(boolean b) {
	autoendgo=b;
    }

    public boolean getAutoEndGo() {
	return autoendgo;
    }

    public void setAutoDefend(boolean b) {
	autodefend=b;
    }

    public boolean getAutoDefend() {
	return autodefend;
    }

}
