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
public class OccupyEuSaAus extends OccupyExplanation implements OccupyMission {

    public OccupyEuSaAus() {
        
        this.setMissionName("Occupy-Europe-South-America-Australia");
        
        this.getRootGoalSet().add(new RootGoal("Occupy", "Europe-South-America-Australia", 1d/12d));  

        this.getSubGoalSet().add(new SubGoal("Occupy", "Europe", 1d));
        this.getSubGoalSet().add(new SubGoal("Occupy", "South America", 1d));
        this.getSubGoalSet().add(new SubGoal("Occupy", "Australia", 1d));

        this.calcInitialExpProb();
    }
}
