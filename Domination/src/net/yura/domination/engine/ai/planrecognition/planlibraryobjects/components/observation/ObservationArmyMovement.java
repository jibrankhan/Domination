/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.observation;

import java.io.Serializable;

/**
 *
 * @author s0914007
 */
public class ObservationArmyMovement extends BasicObservation implements Serializable {
    
    public ObservationArmyMovement(String continentName, String countryName){
        
        super("Army Movement", continentName, countryName);
    }
}
