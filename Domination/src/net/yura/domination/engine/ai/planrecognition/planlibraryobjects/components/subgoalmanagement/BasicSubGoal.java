/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.subgoalmanagement;

import java.io.Serializable;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.BasicPlanComponent;


/**
 *
 * @author s0914007
 */
public class BasicSubGoal extends BasicPlanComponent implements Serializable {
    
    private final String actionLocation;
    
    public BasicSubGoal(String actionType, String actionLocation , double probability){
        
        super(actionType, probability);  
        this.actionLocation = actionLocation;
    }
    
    public String getActionLocation(){
       
        return  this.actionLocation;
    }
}
