/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.observation;

import java.io.Serializable;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.ActionTypeComponent;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.ContinentNameComponent;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.CountryNameComponent;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.action.BasicAction;

/**
 *
 * @author s0914007
 */
public class BasicObservation extends BasicAction implements ActionTypeComponent, CountryNameComponent, ContinentNameComponent, Serializable {
    
    private String continentName;
    
    // TODO Refactor to remove useless probability
    public BasicObservation(String actionType, String continentName, String countryName){
        
        super(actionType, countryName, 0);
        this.continentName = continentName;
    }

    public String getContinentName() {
        
        return continentName;
    }
}