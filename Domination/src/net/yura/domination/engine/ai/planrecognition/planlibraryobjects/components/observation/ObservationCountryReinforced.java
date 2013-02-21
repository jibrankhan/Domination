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
public class ObservationCountryReinforced extends BasicObservation implements Serializable {
    
    public ObservationCountryReinforced(String continentName, String countryName){
     
        super("Reinforce", continentName, countryName);
    }
}