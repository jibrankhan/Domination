/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.yura.domination.engine.ai.planrecognition.events;

import java.io.Serializable;

/**
 *
 * @author s0914007
 */
public class EventFailedOccupation extends Event implements Serializable {
    
    private String playerWonName;
    private String playerLostName;
    
    String continentName;
    String countryName;
    
    public EventFailedOccupation(String playerWonName, String playerLostName, String continentName, String countryName){
        
        this.playerWonName = playerWonName;
        this.playerLostName = playerLostName;
        
        this.continentName = continentName;
        this.countryName = countryName;
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
