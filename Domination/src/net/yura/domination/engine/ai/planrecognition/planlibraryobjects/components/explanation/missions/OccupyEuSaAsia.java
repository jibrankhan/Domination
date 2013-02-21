/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.explanation.missions;

import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.explanation.OccupyExplanation;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.explanation.OccupyMission;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.rootgoalmanagement.RootGoal;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.subgoalmanagement.SubGoal;

/**
 *
 * @author s0914007
 */
public class OccupyEuSaAsia extends OccupyExplanation implements OccupyMission {

    public OccupyEuSaAsia() {
        
        this.setMissionName("Occupy Europe, South America and Asia");
        
        this.getRootGoalSet().add(new RootGoal("Occupy", "Europe, South America and Asia", 1.0f/20.0f));  

        this.getSubGoalSet().add(new SubGoal("Occupy", "Europe", 1.0f));
        this.getSubGoalSet().add(new SubGoal("Occupy", "South America", 1.0f));
        this.getSubGoalSet().add(new SubGoal("Occupy", "Asia", 1.0f/4.0f));

        this.calcInitialExpProb();
    }
}
