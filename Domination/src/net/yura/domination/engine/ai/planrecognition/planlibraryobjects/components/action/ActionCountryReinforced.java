/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.action;

import java.io.Serializable;

/**
 *
 * @author s0914007
 */
public class ActionCountryReinforced extends BasicAction implements Serializable {
    
    public ActionCountryReinforced(String countryName, float probability){
        
        super("Reinforce", countryName, probability);
    }
}
