/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.subgoalmanagement;

import java.io.Serializable;

/**
 *
 * @author s0914007
 */
public class SubGoal extends BasicSubGoal implements Serializable {
    
    public SubGoal(String actionType, String actionLocation, double probability){
        
        super(actionType, actionLocation, probability);
    }
}
