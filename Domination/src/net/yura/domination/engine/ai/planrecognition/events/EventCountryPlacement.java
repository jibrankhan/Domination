/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.yura.domination.engine.ai.planrecognition.events;

/**
 * Event for when a player gains a country
 * @author s0914007
 */
public class EventCountryPlacement extends Event {
 
    private String playerName;
    private String countryName;
    private String continent;
    
    public EventCountryPlacement(String playerName, String countryName, String continent){
        
        this.playerName = playerName;
        this.countryName = countryName;
        this.continent = continent;
    }
    
    public String getPlayerName(){
        
        return playerName;
    }
    
    public String getCountryName(){
        
        return countryName;
    }
    
    public String getContinent(){
        
        return continent;
    }
}
