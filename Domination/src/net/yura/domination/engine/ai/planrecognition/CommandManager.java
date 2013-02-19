/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.yura.domination.engine.ai.planrecognition;

/**
 *
 * @author s0914007
 */
public class CommandManager {
    
    private CommandManager(){
        
    }
    
    public static final String placeArmies(int countryId, int num){
        
        return "placearmies " + Integer.toString(countryId) + " " + Integer.toString(num);
    }
    
    public static final String attacking(int attackingCountry, int defendingCountry){
        
        return "attack " + attackingCountry + " " + defendingCountry;
    }
    
    public static final String roll(int numberOfArmies){
        
        return "roll " + Math.min(3, numberOfArmies);
    }
}
