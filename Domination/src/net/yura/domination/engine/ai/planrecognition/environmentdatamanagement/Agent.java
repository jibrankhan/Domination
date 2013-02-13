/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.yura.domination.engine.ai.planrecognition.environmentdatamanagement;

import com.google.common.collect.ArrayListMultimap;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;
import net.yura.domination.engine.ai.planrecognition.events.Event;
import net.yura.domination.engine.ai.planrecognition.events.EventSuccessfulOccupation;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.action.ActionArmyMovement;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.action.ActionFailedDefence;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.action.ActionSuccessfulOccupation;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.action.ActionCountryReinforced;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.action.ActionFailedOccupation;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.action.ActionSuccessfulDefence;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.action.BasicAction;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.explanation.Explanation;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.observation.BasicObservation;
import net.yura.domination.engine.core.Country;
import net.yura.domination.engine.core.Player;

/**
 *
 * @author s0914007
 */
public class Agent implements Serializable{
    
    private Player player;
    
    private HashSet<Explanation> agentExplanationList = new HashSet<Explanation>();
    
    private ArrayList< HashSet<BasicAction> > agentActivePendingSetHistory = new ArrayList<HashSet<BasicAction>>();
    private ArrayList<BasicObservation> agentObservationSet = new ArrayList<BasicObservation>();
    
    private float agentTotalExplanationProbabilities;
    
    private int turnNumber = 0;
   
    public Agent(Player player){
        
        this.player = player;
    }
    
    public String getAgentName(){
        
        return player.getName();
    }
    
    public HashSet<Explanation> getAgentExplanationList() {
        
        return agentExplanationList;
    }    

    public ArrayList<BasicObservation> getAgentObservationSet() {
        
        return agentObservationSet;
    }
    
    public int getTurnNumber(){
        
        return turnNumber;
    }

    public void setAgentTotalExplanationProbabilities(float totalExplanationProbabilities) {
        
        this.agentTotalExplanationProbabilities = totalExplanationProbabilities;
    }

    public ArrayList<HashSet<BasicAction>> getAgentActivePendingSetHistory() {
        
        return agentActivePendingSetHistory;
    }

    public void setAgentActivePendingSetHistory(ArrayList<HashSet<BasicAction>> agentActivePendingSetHistory) {
        
        this.agentActivePendingSetHistory = agentActivePendingSetHistory;
    }
    
    // Generates Active Pending Set
    public HashSet<BasicAction> generateActivePendingSet(){
        
        HashSet<BasicAction> activePendingSet = new HashSet<BasicAction>();
        
        Vector playerTerritories = new Vector();
                
        playerTerritories.addAll(player.getTerritoriesOwned());
        
        for(Object c : playerTerritories){
            
            Country currentCountry = (Country) c;
            
            // Adds reinforce action
            activePendingSet.add(new ActionCountryReinforced(currentCountry.getName(), 1.0f));
            activePendingSet.add(new ActionFailedDefence(currentCountry.getName(), 1.0f));
            activePendingSet.add(new ActionSuccessfulDefence(currentCountry.getName(), 1.0f));
            //System.out.println("Reinforce " + currentCountry.getName());
            
            // Generates Occupy actions for each neigbouring country
            for(Object n : currentCountry.getNeighbours()){
                
                Country currentNeighbour = (Country) n;
                
                try{
                // Addition of movement actions
                    if(currentNeighbour.getOwner().getName().equals(this.player.getName())){

                        activePendingSet.add(new ActionArmyMovement(currentNeighbour.getName(), 1.0f));
                    }
                } catch (NullPointerException e ){
                    
                    //System.out.println("No player assigned yet!");
                }
                
                // Addition of occupy actions
                // Removes the addition of occupy pending state actions for countries a player owns
                try{
                    
                    if(!currentNeighbour.getOwner().getName().equals(this.player.getName())){

                        activePendingSet.add(new ActionSuccessfulOccupation(currentNeighbour.getName(), 1.0f));
                        activePendingSet.add(new ActionFailedOccupation(currentNeighbour.getName(), 1.0f));
                    }
                //System.out.println("Occupy " + currentNeighbour.getName());
                } catch(NullPointerException e){
                    
                    //System.out.println("No player assigned yet!");
                    activePendingSet.add(new ActionSuccessfulOccupation(currentNeighbour.getName(), 1.0f));
                }
            }
        }
        
        /*System.out.println(player.getName());
        for(BasicAction b : activePendingSet){
            
            System.out.println(b.getActionType() + " " + b.getCountryName());
        }
        System.out.println(activePendingSet.size());*/
        
        return activePendingSet;
    }
    
    public Player getPlayer(){
        
        return player;
    }
    
    public void updateExplanationList(HashSet<Explanation> fullExplanationList){
        
        //System.out.println(this.getAgentName());
        
        for(Explanation e : fullExplanationList){
                            
            int addFlag = 0;
            
            //System.out.println(e.getExplanationName());

            for(BasicAction b : this.generateActivePendingSet()){

                // If active pending set contains an action that is consistent with an explanation,
                // add the explanation to the agents explanation list
                if(addFlag != 1 && e.getConActions().contains(b)){

                    addFlag = 1;
                    //System.out.println("Its consistent!");
                    //System.out.println(b.getActionType() + " " + b.getCountryName());
                } 
            }

            // Check if set already contains 
            for(Explanation agentsE : agentExplanationList){
                
                if(agentsE.getExplanationName().equals(e.getExplanationName())){
                    
                    addFlag = 0;
                }
            }
            // If explanation is new to set of agents explanations
            if(addFlag == 1){
                
                Explanation newExplanation = new Explanation(e.getExplanationName(), e.getRootGoalSet(), e.getMethodChoiceSet(), e.getConActions(), e.getInConActions());
                  
                // Set calculation mode here!
                // Uniform = Uniform Distribution
                // Weighted = Weighted Distribution
                    
                //generateNewExplanationProbability("Weighted", newExplanation);
                
                //System.out.println(newExplanation.getExplanationName());
                //System.out.println(" ");
                agentExplanationList.add(newExplanation);
            }
        }
        
        /*for(Explanation e : this.getAgentExplanationList()){
            
            System.out.println(e.getExplanationName());
        }*/
    }
    
    /**
     * Recomputes an explanation with ONLY the last observation from the environment.
     * @param expToBeUpdated Explanation to be computed.
     */
    public void computeExpProbability(String calculationMethod, String ObservationType, Explanation expToBeUpdated){
        
        // Computing explanation probabilities
        
        /*for(BasicObservation b : agentObservationSet){
            
            System.out.println(b.getActionType() + " " + b.getCountryName());
        }*/
        
        HashSet<BasicAction> filteredActiveSet = new HashSet<BasicAction>();
        
        int activePendingSetSize = this.generateActivePendingSet().size();
        // Turn - 20 BECAUSE = Observations only start on turn 20 after placement is done
        // If there is a null pointer exception problem check HERE!
        if(calculationMethod.equals("Uniform")){
            
            if(ObservationType.equals("Occupy")){
 
                expToBeUpdated.computeExplanationProbabilitySingleObservation(activePendingSetSize, agentObservationSet.get(agentObservationSet.size()-1));
                
            } else if(ObservationType.equals("Reinforce")){
                
            }
        }
        
        if(calculationMethod.equals("Weighted")){
             
            if(ObservationType.equals("Successful Occupation")){
                
                for(BasicAction b: this.generateActivePendingSet()){
                    
                    if(b.getActionType().equals(ObservationType)){
                        
                        filteredActiveSet.add(b);
                        //System.out.println(b.getActionType() + " " + b.getCountryName());
                    }
                }
                   
                expToBeUpdated.computeExplanationProbabilityWeighted(ObservationType, filteredActiveSet, agentObservationSet.get(agentObservationSet.size()-1));
            }
            
            else if(ObservationType.equals("Reinforce")){
                
                for(BasicAction b: this.generateActivePendingSet()){
                    
                    if(b.getActionType().equals(ObservationType)){
                        
                        filteredActiveSet.add(b);
                        //System.out.println(b.getActionType() + " " + b.getCountryName());
                    }
                }
                   
                expToBeUpdated.computeExplanationProbabilityWeighted(ObservationType, filteredActiveSet, agentObservationSet.get(agentObservationSet.size()-1));
            }
            
            else if(ObservationType.equals("Army Movement")){
                
                for(BasicAction b : this.generateActivePendingSet()){
                    
                    if(b.getActionType().equals(ObservationType)){
                        
                        filteredActiveSet.add(b);
                    }
                }
                
                expToBeUpdated.computeExplanationProbabilityWeighted(ObservationType, filteredActiveSet, agentObservationSet.get(agentObservationSet.size()-1));
            }
            
            else if(ObservationType.equals("Failed Defence")){
                
                for(BasicAction b : this.generateActivePendingSet()){
                    
                    if(b.getActionType().equals(ObservationType)){
                        
                        filteredActiveSet.add(b);
                    }
                }
                
                expToBeUpdated.computeExplanationProbabilityWeighted(ObservationType, filteredActiveSet, agentObservationSet.get(agentObservationSet.size()-1));
            }
            
            else if(ObservationType.equals("Failed Occupation")){
                
                for(BasicAction b : this.generateActivePendingSet()){
                    
                    if(b.getActionType().equals(ObservationType)){
                        
                        filteredActiveSet.add(b);
                    }
                }
                
                expToBeUpdated.computeExplanationProbabilityWeighted(ObservationType, filteredActiveSet, agentObservationSet.get(agentObservationSet.size()-1));
            }
            
            else if(ObservationType.equals("Successful Defence")){
                
                for(BasicAction b : this.generateActivePendingSet()){
                    
                    if(b.getActionType().equals(ObservationType)){
                        
                        filteredActiveSet.add(b);
                    }
                }
                
                expToBeUpdated.computeExplanationProbabilityWeighted(ObservationType, filteredActiveSet, agentObservationSet.get(agentObservationSet.size()-1));
            }
        }
            
        //System.out.println(e.getExplanationName() + " " + e.getExplanationProbability()); 
    }
    
    public float computeTotalExpProbabilties(){
        
        this.agentTotalExplanationProbabilities = 0;
        
        for(Explanation e : agentExplanationList){
            
            this.agentTotalExplanationProbabilities += e.getExplanationProbability();
        }
        
        return agentTotalExplanationProbabilities;
    }
}
