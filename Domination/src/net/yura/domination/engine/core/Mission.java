// Yura Mamyrin, Group D

package net.yura.domination.engine.core;

import java.io.Serializable;

/**
 * <p> Risk Mission </p>
 * @author Yura Mamyrin
 */

public class Mission implements Serializable {

	private static final long serialVersionUID = 1L;

    private Player player;
    private int noofcountries;
    private int noofarmies;
    private Continent con1;
    private Continent con2;
    private Continent con3;
    private String discription;

    // destroy x occupy x x continents x x x
    /**
     * Creates a new Mission
     * @param p The player who must be killed
     * @param noc The number of countries needed to own
     * @param noa The minimum number of armies needed on a territory
     * @param c1 The first continent needed to own
     * @param c2 The second continent needed to own
     * @param c3 The third continent needed to own
     * @param d The description of the mission
     */
    public Mission(Player p, int noc, int noa, Continent c1, Continent c2, Continent c3, String d) {

	player = p;
	noofcountries = noc;
	noofarmies = noa;
	con1 = c1;
	con2 = c2;
	con3 = c3;
	discription = d;

    }

    /**
     * Gets the player who has this mission
     * @return Player Returns the player who has this mission 
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the number of countries needed to conquer
     * @return int Returns the number of countries needed to conquer
     */
    public int getNoofcountries() {
        return noofcountries;
    }

    /**
     * Gets the minimum number of armies needed on each territory
     * @return int Returns the minimum number of armies needed on each territory 
     */
    public int getNoofarmies() {
        return noofarmies;
    }

    /**
     * Gets the first continent needed to own
     * @return Continent Returns the first continent needed to own
     */
    public Continent getContinent1() {
        return con1;
    }

    /**
     * Gets the second continent needed to own
     * @return Continent Returns the second continent needed to own
     */
    public Continent getContinent2() {
        return con2;
    }

    /**
     * Gets the third continent needed to own
     * @return Continent Returns the third continent needed to own
     */
    public Continent getContinent3() {
        return con3;
    }

    /**
     * Gets the description of the mission
     * @return String Returns the description of the mission
     */
    public String getDiscription() {
        return discription;
    }



    public String toString() {

	return "".equals(discription)?"(Discription Missing)":discription;

    }



    public void setPlayer(Player a) {
        player = a;
    }

    public void setNoofcountries(int a) {
        noofcountries = a;
    }

    public void setNoofarmies(int a) {
        noofarmies = a;
    }

    public void setContinent1(Continent a) {
        con1 = a;
    }

    public void setContinent2(Continent a) {
        con2 = a;
    }

    public void setContinent3(Continent a) {
        con3 = a;
    }

    public void setDiscription(String a) {
        discription = a;
    }
}
