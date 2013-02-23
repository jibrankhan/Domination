/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.rootgoalmanagement;

import java.io.Serializable;

/**
 *
 * @author s0914007
 */
public class RootGoal extends BasicRootGoal implements Serializable {
    
    public RootGoal(String actionType, String actionLocation, double probability){
        
        super(actionType, actionLocation, probability);
    }
}
