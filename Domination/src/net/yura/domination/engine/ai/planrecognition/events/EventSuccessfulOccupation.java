/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.yura.domination.engine.ai.planrecognition.events;

/**
 *
 * @author s0914007
 */
public class EventSuccessfulOccupation extends Event {
    
    private String playerWonName;
    private String playerLostName;
    
    private String countryName;
    private String continentName;
    
    public EventSuccessfulOccupation(String playerWonName, String playerLostName, String continentName, String countryName){
        
        this.playerWonName = playerWonName;
        this.playerLostName = playerLostName;
        
        this.countryName = countryName;
        this.continentName = continentName;
    }
    
    public String getPlayerWonName(){
        
        return playerWonName;
    }
     
    public String getPlayerLostName(){
        
        return playerLostName;
    }
    
    public String getCountryName(){
        
        return countryName;
    }
    
    public String getContinentName(){
        
        return continentName;
    }
}
