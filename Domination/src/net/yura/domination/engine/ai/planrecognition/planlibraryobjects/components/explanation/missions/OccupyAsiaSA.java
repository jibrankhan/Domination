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
public class OccupyAsiaSa extends OccupyExplanation implements Serializable{

    public OccupyAsiaSa() {
        
        this.setMissionName("Occupy Asia and South America");
        
        this.getRootGoalSet().add(new RootGoal("Occupy", "Asia and South America", 1.0f/20.0f));  
        
        this.getSubGoalSet().add(new SubGoal("Occupy", "Asia", 1.0f));
        this.getSubGoalSet().add(new SubGoal("Occupy", "South America", 1.0f));
        
        this.calcInitialExpProb();
    }
}
