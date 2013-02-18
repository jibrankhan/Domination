/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.explanation;

import java.util.Set;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.action.BasicAction;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.observation.BasicObservation;

/**
 *
 * @author s0914007
 */
public interface Mission {
 
    String getMissionName();
    void setMissionName(String missionName);
    boolean isConsistent(BasicAction action);
    void computeMissionProbability(String ObservationType, Set<BasicAction> activeSet, BasicObservation currentObservation);
}
