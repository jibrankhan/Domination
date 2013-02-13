/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.action;

import java.io.Serializable;

/**
 * Pending state action for conquering a territory
 * @author s0914007
 */
public class ActionSuccessfulOccupation extends BasicAction implements Serializable {
    
    public ActionSuccessfulOccupation(String countryName, float probability){
        
        super("Successful Occupation", countryName, probability);
    }
}
