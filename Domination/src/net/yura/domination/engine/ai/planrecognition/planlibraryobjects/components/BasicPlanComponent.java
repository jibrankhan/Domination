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
    double probability;
    
    public BasicPlanComponent(String actionType, double probability){
        
        this.actionName = actionType;
        this.probability = probability;   
    }
    
    @Override
    public double getProbability() {
        
        return probability;
    }

    @Override
    public String getActionType() {
        
        return actionName;
    }
}
