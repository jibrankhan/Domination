/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.yura.domination.engine.ai.planrecognition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.action.ActionArmyMovement;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.action.ActionFailedDefence;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.action.ActionSuccessfulOccupation;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.action.ActionCountryReinforced;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.action.ActionSuccessfulDefence;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.action.BasicAction;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.explanation.Explanation;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.rootgoalmanagement.RootGoal;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.subgoalmanagement.SubGoal;
import net.yura.domination.engine.core.Continent;
import net.yura.domination.engine.core.Country;

/**
 *
 * @author s0914007
 */
public class ExplanationManager implements Serializable {
    
    private HashSet<Explanation> explanationList = new HashSet<Explanation>();
    
    public ExplanationManager(Vector Continent){
        
        // Automated building of each action set per country and each explanation 
        for(Object continent : Continent){
            
            Continent currentContinent = (Continent) continent;
            
            ArrayList<BasicAction> conActions = new ArrayList<BasicAction>();
            ArrayList<BasicAction> inConActions = new ArrayList<BasicAction>();
            HashSet rootGoals = new HashSet<RootGoal>();
            HashSet methodChoices = new HashSet<SubGoal>();
            
            for(Object country : currentContinent.getTerritoriesContained()){
                
                Country currentCountry = (Country) country;
                
                // Building explanations
                // Occupying countries that are part of a continent is consistent with the plan of conquering that continent
                conActions.add(new ActionSuccessfulOccupation(currentCountry.getName(), 1.0f));
                conActions.add(new ActionCountryReinforced(currentCountry.getName(), 1.0f));
                conActions.add(new ActionArmyMovement(currentCountry.getName(), 1.0f));
                conActions.add(new ActionSuccessfulDefence(currentCountry.getName(), 1.0f));
                
                inConActions.add(new ActionFailedDefence(currentCountry.getName(), 1.0f));
            }      
            
            rootGoals.add(new RootGoal("Occupy", currentContinent.getName(), (float) 1/Continent.size()));
            
            explanationList.add(new Explanation("Occupy " + currentContinent.getName(), rootGoals, methodChoices, conActions, inConActions));       
        }
        
        System.out.println("All Explanations and Countires Initialized!");
    }  
    
    public HashSet<Explanation> getExplanationList() {
        
        return explanationList;
    }
}
