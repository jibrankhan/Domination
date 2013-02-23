/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.action;

import java.io.Serializable;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.BasicPlanComponent;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.CountryNameComponent;

/**
 *
 * @author s0914007
 */
public class BasicAction extends BasicPlanComponent implements CountryNameComponent, Serializable {
  
   String countryName;
   
   // Basic Pending State Action Class
   public BasicAction(String actionType, String countryName, double probability){
       
       super(actionType, probability);
       this.countryName = countryName;    
   }

    public String getCountryName() {
        
        return countryName;
    }

    @Override
    public boolean equals(Object o) {
        
        if(o instanceof BasicAction){
            
            return this.equals((BasicAction) o);
        }
        else {
            
            return super.equals(o);
        }
    }
    
    public boolean equals(BasicAction action){
        
        if(action.getActionType().equals(this.getActionType())){
                
                if (action.getCountryName().equals(this.getCountryName())){
            
                    return true;
                }            
        } 
        
        return  false;
    }

    @Override
    public int hashCode() {
        
        return this.getCountryName().hashCode();
    }
}
