/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.explanation;

import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.action.BasicAction;

/**
 *
 * @author s0914007
 */
public interface Mission {
 
    String getMissionName();
    String setMissionName();
    boolean isConsistent(BasicAction action);
}
