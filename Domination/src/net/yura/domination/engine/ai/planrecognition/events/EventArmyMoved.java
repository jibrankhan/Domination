/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.yura.domination.engine.ai.planrecognition.events;

/**
 *
 * @author s0914007
 */
public class EventArmyMoved extends Event {
    
    private String playerName;
    private String countryName;
    private String continentName;
    
    public EventArmyMoved(String playerName, String continentName, String countryName) {

        this.playerName = playerName;
        this.countryName = countryName;
        this.continentName = continentName;
    }

    public String getPlayerName() {

        return playerName;
    }

    public String getCountryName() {

        return countryName;
    }

    public String getContinentName() {

        return continentName;
    }
}
