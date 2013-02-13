// Yura Mamyrin, Group D

package net.yura.domination.engine.core;

import java.io.Serializable;

/**
 * <p> Risk Card </p>
 * @author Yura Mamyrin
 */

public class Card implements Serializable {

	private static final long serialVersionUID = 1L;

    public final static String CAVALRY = "Cavalry";
    public final static String INFANTRY = "Infantry";
    public final static String CANNON = "Cannon";
    public final static String WILDCARD = "wildcard";

    private String name;
    private Country country;

    /**
     * Creates a new game card
     * @param n describes the type of the card, e.g. calvary, cannon, infantry or wildcard
     * @param t the name of the country
     */
    public Card(String n, Country t) {

	setName(n);
	setCountry(t);

    }

    /**
     * Returns the name of the card type
     * @return name 
     */
    public String getName() {
        return name;
    }

    public void setName(String n) {

	if ( !n.equals(CAVALRY) && !n.equals(INFANTRY) && !n.equals(CANNON) && !n.equals(WILDCARD) ) {

		throw new IllegalArgumentException("trying to make a card with an unknown type: "+n);

	}

	name = n;

    }

    /**
     * Returns the Country object
     * @return Country 
     */
    public Country getCountry() {
        return country;
    }

    public void setCountry(Country a) {
        country = a;
    }

    public String toString() {

	if (country!=null) {

		return name+" - "+country;
	}

	return name;

    }

}
