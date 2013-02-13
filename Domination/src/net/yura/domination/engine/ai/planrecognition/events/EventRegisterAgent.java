/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.yura.domination.engine.ai.planrecognition.events;

import net.yura.domination.engine.core.Player;

/**
 *
 * @author s0914007
 */
public class EventRegisterAgent extends Event {
    
    Player newPlayer;
    
    public EventRegisterAgent(Player newPlayer){
        
        super();
        
        this.newPlayer = newPlayer;
    }
    
    public Player getNewPlayer(){
        
        return newPlayer;
    }
}
