/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.explanation;

import java.io.Serializable;
import java.util.Set;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.action.ActionArmyMovement;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.action.ActionCountryReinforced;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.action.ActionFailedDefence;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.action.ActionSuccessfulDefence;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.action.ActionSuccessfulOccupation;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.action.BasicAction;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.observation.BasicObservation;
import net.yura.domination.engine.core.Continent;
import net.yura.domination.engine.core.Country;

/**
 *
 * @author s0914007
 */
public abstract class OccupyExplanation extends Explanation implements OccupyMission, Serializable {
    
    public void addConsistentActions(Continent continent) {
        
        for(Object country : continent.getTerritoriesContained()){
                
            Country currentCountry = (Country) country;

            this.getConActions().add(new ActionSuccessfulOccupation(currentCountry.getName(), 1.0f));
            this.getConActions().add(new ActionCountryReinforced(currentCountry.getName(), 1.0f));
            this.getConActions().add(new ActionArmyMovement(currentCountry.getName(), 1.0f));
            this.getConActions().add(new ActionSuccessfulDefence(currentCountry.getName(), 1.0f));

            this.getInConActions().add(new ActionFailedDefence(currentCountry.getName(), 1.0f));
        }
    }
    
    public boolean isConsistent(BasicAction action) {
        
        throw new UnsupportedOperationException("Not supported yet.");
    }    
    
    public void computeMissionProbability(String observationType, Set<BasicAction> activePendingSet, BasicObservation currentObservation){
        
        // Filters out actions that are not in the same action type
        Set<BasicAction> conActiveSet = this.filterSet(observationType, this.getConActions());
        Set<BasicAction> filteredActiveSet = this.filterSet(observationType, activePendingSet);
        
        int conActionCounter = 0;
        int inConActionCounter = 0;
        
        // Count number of inconsistent and consistent actions in active pending set when action took place
        for(BasicAction b : filteredActiveSet){
                
            if(conActiveSet.contains(b)){
                    
                conActionCounter++;
            }
        }
        //System.out.println(conActionCounter);
        //System.out.println(" ");
        
        float missionProbability = this.getExplanationProbability();
        
        if(!observationType.equals("Failed Defence") && !observationType.equals("Successful Defence")){
            
            /*System.out.println("Agents Active Pending Set");
            for(BasicAction a : activeSet){

                System.out.println(a.getActionType() + " " + a.getCountryName());
            }
            System.out.println(" ");*/

            // Use a different maths function to calculate weighting amounts!
            float conActionProb = 0.6f / conActionCounter;
            // If it is not consistent it is inconsistent
            float inconActionProb = 0.4f / (filteredActiveSet.size() - conActionCounter);

            //System.out.println(currentObservation.getActionType() + " " + currentObservation.getCountryName());

            if(conActiveSet.contains(currentObservation)){

                System.out.println(this.getMissionName() + " consistent action!" + " " + missionProbability + " * " + conActionProb);
                //System.out.println(" ");
                this.setExplanationProbability(missionProbability *= conActionProb);

            } else { 

                System.out.println(this.getMissionName() + " inconsistent action!" + " " + missionProbability + " * " + inconActionProb);
                //System.out.println(" ");
                this.setExplanationProbability(missionProbability *= inconActionProb);  
            }
        } else {
            
            // Filters out actions that are not in the same action type
            Set<BasicAction> inConActiveSet = this.filterSet(observationType, this.getInConActions());
            
            // Count number of inconsistent and consistent actions in active pending set when action took place
            for(BasicAction b : filteredActiveSet){

                //System.out.println(b.getActionType() + " " + b.getCountryName());
                if(inConActiveSet.contains(b)){

                    inConActionCounter++;
                } 
            }
            
             // Use a different maths function to calculate weighting amounts!
            float conActionProb = 1.02f;
            // If it is not consistent it is inconsistent
            float inconActionProb = 0.98f;
            
            //System.out.println(currentObservation.getActionType() + " " + currentObservation.getContinentName() + " " + currentObservation.getCountryName());
            if(inConActiveSet.contains(currentObservation)){

                System.out.println(this.getMissionName() + " inconsistent action!" + " " + missionProbability + " * " + inconActionProb);
                //System.out.println(" ");
                this.setExplanationProbability(missionProbability *= inconActionProb);    

            } else if(conActiveSet.contains(currentObservation)){
                
                System.out.println(this.getMissionName() + " consistent action!" + " " + missionProbability + " * " + conActionProb );
                this.setExplanationProbability(missionProbability *= conActionProb);
                
            } else { 

                System.out.println(this.getMissionName() + " action does not apply!");
                //System.out.println(" ");   
            }
        }
    }
}
