/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components;

import java.io.Serializable;

/**
 *
 * @author s0914007
 */
public class BasicPlanComponent implements ActionTypeComponent, ProbabilityComponent, Serializable {

    String actionName;
    float probability;
    
    public BasicPlanComponent(String actionType, float probability){
        
        this.actionName = actionType;
        this.probability = probability;   
    }
    
    @Override
    public float getProbability() {
        
        return probability;
    }

    @Override
    public String getActionType() {
        
        return actionName;
    }
}
