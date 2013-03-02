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
public class OccupyEuAusSa extends OccupyExplanation implements Serializable {

    public OccupyEuAusSa() {
        
        this.setMissionName("Occupy Europe, Australia and South America");
        
        this.getRootGoalSet().add(new RootGoal("Occupy", "Europe, Australia and South America", 1.0d/20.0d));  
        
        this.getSubGoalSet().add(new SubGoal("Occupy", "Europe", 1.0d));
        this.getSubGoalSet().add(new SubGoal("Occupy", "Australia", 1.0d));
        this.getSubGoalSet().add(new SubGoal("Occupy", "South America", 1.0d/4.0d));
        
        this.calcInitialExpProb();
    }
}