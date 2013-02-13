/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.yura.domination.engine.ai.planrecognition.events;

/**
 *
 * @author s0914007
 */
public class EventRemoveAgent extends Event {
    
    String agentName;
    int agentListLocation;
    
    public EventRemoveAgent(String agentName, int agentListLocation){
        
        super();
        
        this.agentName = agentName;
        this.agentListLocation = agentListLocation;
    }
    
    public int getAgentLocationNumber(){
        
        return agentListLocation;
    }

    public String getAgentName() {
        
        return agentName;
    }
}
