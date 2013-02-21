/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.explanation;

import java.io.Serializable;
import java.util.Set;
import net.yura.domination.engine.ai.planrecognition.ActionConstants;
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
    
    public void computeMissionProbability(Set<BasicAction> activePendingSet, BasicObservation currentObservation){
        
        String observationType = currentObservation.getActionType();
        
        // Filters out actions that are not in the same action type
        Set<BasicAction> conActiveSet = this.filterSet(observationType, this.getConActions());
        Set<BasicAction> filteredActiveSet = this.filterSet(observationType, activePendingSet);
        Set<BasicAction> inConActiveSet = this.filterSet(observationType, this.getInConActions());
        
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
        
        // Count number of inconsistent and consistent actions in active pending set when action took place
        for(BasicAction b : filteredActiveSet){

            //System.out.println(b.getActionType() + " " + b.getCountryName());
            if(inConActiveSet.contains(b)){

                inConActionCounter++;
            } 
        }
       
        float missionProbability = this.getExplanationProbability();
        
        //if(!observationType.equals("Failed Defence") && !observationType.equals("Successful Defence")){
            
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

                if(observationType.equals(ActionConstants.successfulDefence)){
                    
                    // Seperate probability for defence actions
                    conActionProb = 1.02f;
                }
                
                System.out.println(this.getMissionName() + " consistent action!" + " " + missionProbability + " * " + conActionProb);
                //System.out.println(" ");
                this.setExplanationProbability(missionProbability *= conActionProb);

            } else if(inConActiveSet.contains(currentObservation)) { 

                if(observationType.equals("Failed Defence")){

                    // Seperate probability for defence actions
                    inconActionProb = 0.98f;
                }
                
                System.out.println(this.getMissionName() + " inconsistent action!" + " " + missionProbability + " * " + inconActionProb);
                //System.out.println(" ");
                this.setExplanationProbability(missionProbability *= inconActionProb); 
                
            // Condition for when there is an inconsistent action that is not in the set of inconsistent actions - why because this is less intensive than maintaining the whole list.
            } else if(!inConActiveSet.contains(currentObservation) && (!observationType.equals("Failed Defence") && !observationType.equals("Successful Defence"))) {
                
                System.out.println(this.getMissionName() + " inconsistent action!" + " " + missionProbability + " * " + inconActionProb);
                //System.out.println(" ");
                this.setExplanationProbability(missionProbability *= inconActionProb); 
                
            } else {
                
                System.out.println(this.getMissionName() + " Does not apply!");
            }
    }
}
