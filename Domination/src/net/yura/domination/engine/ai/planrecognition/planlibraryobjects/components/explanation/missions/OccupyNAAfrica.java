/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.explanation.missions;

import java.io.Serializable;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.explanation.OccupyExplanation;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.rootgoalmanagement.RootGoal;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.subgoalmanagement.SubGoal;

/**
 *
 * @author s0914007
 */
public class OccupyNAAfrica extends OccupyExplanation implements Serializable {
    
    public OccupyNAAfrica() {
        
        this.setMissionName("Occupy North America and Africa");
        
        this.getRootGoalSet().add(new RootGoal("Occupy", "North America and Africa", 1.0f/20.0f));  
        
        this.getSubGoalSet().add(new SubGoal("Occupy", "North America", 1.0f));
        this.getSubGoalSet().add(new SubGoal("Occupy", "Africa", 1.0f));
        
        this.calcInitialExpProb();
    }
}
