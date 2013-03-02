/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.explanation.missions;

import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.explanation.Mission;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.explanation.OccupyExplanation;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.rootgoalmanagement.RootGoal;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.subgoalmanagement.SubGoal;

/**
 *
 * @author s0914007
 */
public class OccupyEuSaAfrica extends OccupyExplanation implements Mission {

    public OccupyEuSaAfrica() {
        
        this.setMissionName("Occupy Europe, South America and Africa");
        
        this.getRootGoalSet().add(new RootGoal("Occupy", "Europe, South America and Africa", 1d/12d));  
        
        this.getSubGoalSet().add(new SubGoal("Occupy", "Europe", 1d));
        this.getSubGoalSet().add(new SubGoal("Occupy", "South America", 1d));
        this.getSubGoalSet().add(new SubGoal("Occupy", "Africa", 1d));
        
        this.calcInitialExpProb();
    }
}
