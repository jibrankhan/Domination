/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.explanation;

import java.io.Serializable;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.rootgoalmanagement.RootGoal;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.subgoalmanagement.SubGoal;

/**
 *
 * @author s0914007
 */
public class OccupyNAAusExp extends OccupyExplanation implements Serializable {

    public OccupyNAAusExp() {
        
        this.setMissionName("Occupy North America and Australia");
        
        this.getRootGoalSet().add(new RootGoal("Occupy", "North America and Australia", 1.0f/20.0f));  
        
        this.getSubGoalSet().add(new SubGoal("Occupy", "North America", 1.0f));
        this.getSubGoalSet().add(new SubGoal("Occupy", "Australia", 1.0f));
        
        this.calcInitialExpProb();
    }
}
