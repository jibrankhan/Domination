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
public class ObservationFailedOccupation extends BasicObservation implements Serializable {
    
    public ObservationFailedOccupation(String continentName, String countryName){
        
        super("Failed Occupation", continentName, countryName);
    }
}
