/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.yura.domination.engine.ai.planrecognition.environmentdatamanagement;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import net.yura.domination.engine.ai.planrecognition.PlanRecognition;
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
public class Agent implements Serializable {
    
    String agentName;
    
    private Set<Explanation> agentExplanationList = new HashSet<Explanation>();
    private List<BasicObservation> agentObservationSet = new ArrayList<BasicObservation>();
    private ListMultimap<String,Map<Integer,Double>> myMutlimap = ArrayListMultimap.create();
    
    private double agentTotalExplanationProbabilities;
    
    private int turnNumber = 0;
   
    public Agent(String agentName){
        
        this.agentName = agentName;
    }
    
    public String getAgentName(){
        
        return agentName;
    }
    
    public Set<Explanation> getAgentExplanationList() {
        
        return agentExplanationList;
    }    

    public List<BasicObservation> getAgentObservationSet() {
        
        return agentObservationSet;
    }
    
    public int getTurnNumber(){
        
        return turnNumber;
    }

    public void setAgentTotalExplanationProbabilities(double totalExplanationProbabilities) {
        
        this.agentTotalExplanationProbabilities = totalExplanationProbabilities;
    }

    public ListMultimap<String, Map<Integer, Double>> getMyMutlimap() {
        
        return myMutlimap;
    }

    public void setMyMutlimap(ListMultimap<String, Map<Integer, Double>> myMutlimap) {
        
        this.myMutlimap = myMutlimap;
    }

    

    // Generates Active Pending Set
    public synchronized Set<BasicAction> generateActivePendingSet(){
        
        Set<BasicAction> activePendingSet = new HashSet<BasicAction>();
        
        Player thisPlayer = null;

        //Synchronises Player Object
        for(Object p : PlanRecognition.getPlayers()){
            
            Player currentPlayer = (Player) p;
            
            if(currentPlayer.getName().equals(this.getAgentName())){
               
                thisPlayer = currentPlayer;
            }
        }
                
        Vector playerTerritories = new Vector();
                
        playerTerritories.addAll(thisPlayer.getTerritoriesOwned());
        
        for(Object c : playerTerritories){
            
            Country currentCountry = (Country) c;
            
            //System.out.println(currentCountry.getName());
            
            // Adds reinforce action
            activePendingSet.add(new ActionCountryReinforced(currentCountry.getName(), 1.0f));
            activePendingSet.add(new ActionFailedDefence(currentCountry.getName(), 1.0f));
            activePendingSet.add(new ActionSuccessfulDefence(currentCountry.getName(), 1.0f));
            //System.out.println("Reinforce " + currentCountry.getName());
            
            // Generates Occupy actions for each neigbouring country
            for(Object n : currentCountry.getNeighbours()){
                
                Country currentNeighbour = (Country) n;
                
                try{
                // Addition of movement actions and occupy actions based on whether player owns the country or not
                    if(currentNeighbour.getOwner().getName().equals(thisPlayer.getName())){

                        //System.out.println(currentNeighbour.getName() + " " + thisPlayer.getName());
                        activePendingSet.add(new ActionArmyMovement(currentNeighbour.getName(), 1.0f));
                        
                    } else if (!currentNeighbour.getOwner().getName().equals(thisPlayer.getName())){
                        
                        activePendingSet.add(new ActionSuccessfulOccupation(currentNeighbour.getName(), 1.0f));
                        activePendingSet.add(new ActionFailedOccupation(currentNeighbour.getName(), 1.0f));
                    }
                } catch (NullPointerException e ){
                    
                    //System.out.println("No player assigned yet!");
                    activePendingSet.add(new ActionSuccessfulOccupation(currentNeighbour.getName(), 1.0f));
                    activePendingSet.add(new ActionFailedOccupation(currentNeighbour.getName(), 1.0f));
                }
            }
        }
        
        /*System.out.println(thisPlayer.getName());
        for(BasicAction b : activePendingSet){
            
            System.out.println(b.getActionType() + " " + b.getCountryName());
        }
        System.out.println(activePendingSet.size());*/
        
        return activePendingSet;
    }
    
    public synchronized void generateExplanationList(Set<Explanation> fullExplanationList){
        
        System.out.println(this.getAgentName());
        
        for(Explanation e : fullExplanationList){ 
                            
            try{
                    
                Explanation clonedExp = (Explanation) e.clone();
                //System.out.println(clonedExp.getMissionName());
                agentExplanationList.add(clonedExp);
                    
            }catch (CloneNotSupportedException excep){

                excep.printStackTrace();
                System.out.println("Cloneable not implemented");

            }
        }
    }
    
    public synchronized double computeTotalExpProbabilties(){
        
        this.agentTotalExplanationProbabilities = 0;
        
        for(Explanation e : agentExplanationList){
            
            this.agentTotalExplanationProbabilities += e.getExplanationProbability();
        }
        
        return agentTotalExplanationProbabilities;
    }
    
    public synchronized Set<BasicAction> filterActiveSet(String observationType, Set<BasicAction> activePendingSet){
        
        Set<BasicAction> filteredActiveSet = new HashSet<BasicAction>();
           
        for(BasicAction b: activePendingSet){

            if(b.getActionType().equals(observationType)){

                filteredActiveSet.add(b);
                //System.out.println(b.getActionType() + " " + b.getCountryName());
            }
        }
        
        return  filteredActiveSet;
    }
}
