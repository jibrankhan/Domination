/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.yura.domination.engine.ai.planrecognition.environmentdatamanagement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    
    private Player player;
    
    private Set<Explanation> agentExplanationList = new HashSet<Explanation>();
    
    private List<Set<BasicAction>> agentActivePendingSetHistory = new ArrayList<Set<BasicAction>>();
    private List<BasicObservation> agentObservationSet = new ArrayList<BasicObservation>();
    
    private double agentTotalExplanationProbabilities;
    
    private int turnNumber = 0;
   
    public Agent(Player player){
        
        this.player = player;
    }
    
    public String getAgentName(){
        
        return player.getName();
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

    public List<Set<BasicAction>> getAgentActivePendingSetHistory() {
        
        return agentActivePendingSetHistory;
    }

    public void setAgentActivePendingSetHistory(List<Set<BasicAction>> agentActivePendingSetHistory) {
        
        this.agentActivePendingSetHistory = agentActivePendingSetHistory;
    }

    public void setPlayer(Player player) {
        
        this.player = player;
    }
    
    public Player getPlayer(){
        
        return player;
    }
    
    // Generates Active Pending Set
    public Set<BasicAction> generateActivePendingSet(){
        
        Set<BasicAction> activePendingSet = new HashSet<BasicAction>();
        
        //Synchronises Player Object
        
        Vector playerTerritories = new Vector();
        
        for(Object p : PlanRecognition.getPlayers()){
            
            Player currentPlayer = (Player) p;
            
            if(currentPlayer.getName().equals(this.getAgentName())){
                
                this.setPlayer(currentPlayer);
            }
        }
                
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
    
    public void generateExplanationList(Set<Explanation> fullExplanationList){
        
        //System.out.println(this.getAgentName());
        
        for(Explanation e : fullExplanationList){
                            
            try{
                    
                Explanation clonedExp = (Explanation) e.clone();
                agentExplanationList.add(clonedExp);
                    
            }catch (CloneNotSupportedException excep){

                excep.printStackTrace();
                System.out.println("Cloneable not implemented");

            }
        }
    }
    
    public double computeTotalExpProbabilties(){
        
        this.agentTotalExplanationProbabilities = 0;
        
        for(Explanation e : agentExplanationList){
            
            this.agentTotalExplanationProbabilities += e.getExplanationProbability();
        }
        
        return agentTotalExplanationProbabilities;
    }
    
    public Set<BasicAction> filterActiveSet(String observationType, Set<BasicAction> activePendingSet){
        
        Set<BasicAction> filteredActiveSet = new HashSet<BasicAction>();
           
        for(BasicAction b: this.generateActivePendingSet()){

            if(b.getActionType().equals(observationType)){

                filteredActiveSet.add(b);
                //System.out.println(b.getActionType() + " " + b.getCountryName());
            }
        }
        
        return  filteredActiveSet;
    }
}
