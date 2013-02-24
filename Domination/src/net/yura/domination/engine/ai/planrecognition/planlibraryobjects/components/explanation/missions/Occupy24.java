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
public class Occupy24 extends OccupyExplanation implements Serializable {

    public Occupy24() {
        
        this.setMissionName("Occupy 24 Territories");
        
        this.getRootGoalSet().add(new RootGoal("Occupy", "24 Territories", 1.0f));
        this.getSubGoalSet().add(new SubGoal("Occupy", "24 Territories", 1.0f));
        
        this.calcInitialExpProb();
    }
}
