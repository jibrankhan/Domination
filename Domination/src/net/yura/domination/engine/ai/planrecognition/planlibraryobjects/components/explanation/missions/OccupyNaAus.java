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
public class OccupyNaAus extends OccupyExplanation implements Serializable {

    public OccupyNaAus() {
        
        this.setMissionName("Occupy-North-America-Australia");
        
        this.getRootGoalSet().add(new RootGoal("Occupy", "North-America-Australia", 1d/12d));  
        
        this.getSubGoalSet().add(new SubGoal("Occupy", "North America", 1d));
        this.getSubGoalSet().add(new SubGoal("Occupy", "Australia", 1d));
        
        this.calcInitialExpProb();
    }
}
