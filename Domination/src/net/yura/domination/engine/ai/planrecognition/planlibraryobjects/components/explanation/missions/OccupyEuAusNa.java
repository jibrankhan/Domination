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
public class OccupyEuAusNa extends OccupyExplanation implements Serializable {

    public OccupyEuAusNa() {
        
        this.setMissionName("Occupy-Europe-Australia-North-America");
        
        this.getRootGoalSet().add(new RootGoal("Occupy", "Europe-Australia-North-America", 1d/12d));  
        
        this.getSubGoalSet().add(new SubGoal("Occupy", "Europe", 1d));
        this.getSubGoalSet().add(new SubGoal("Occupy", "Australia", 1d));
        this.getSubGoalSet().add(new SubGoal("Occupy", "North America", 1d));
        
        this.calcInitialExpProb();
    }
}
