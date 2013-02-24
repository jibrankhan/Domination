/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.yura.domination.engine.ai.planrecognition;

import net.yura.domination.engine.ai.planrecognition.events.Event;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import net.yura.domination.engine.ai.planrecognition.environmentdatamanagement.Agent;
import net.yura.domination.engine.ai.planrecognition.eventhandling.AbstractService;
import net.yura.domination.engine.ai.planrecognition.eventhandling.EventObserver;
import net.yura.domination.engine.ai.planrecognition.events.EventCountryReinforced;
import net.yura.domination.engine.ai.planrecognition.events.EventRegisterAgent;
import net.yura.domination.engine.ai.planrecognition.events.EventSuccessfulOccupation;
import net.yura.domination.engine.ai.planrecognition.events.EventCountryPlacement;
import net.yura.domination.engine.ai.planrecognition.events.EventArmyMoved;
import net.yura.domination.engine.ai.planrecognition.events.EventFailedOccupation;
import net.yura.domination.engine.ai.planrecognition.events.EventRemoveAgent;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.action.BasicAction;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.explanation.Explanation;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.observation.ObservationArmyMovement;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.observation.ObservationFailedDefence;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.observation.ObservationSuccessfulOccupation;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.observation.ObservationCountryReinforced;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.observation.ObservationFailedOccupation;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.observation.ObservationSuccessfulDefence;
import net.yura.domination.engine.core.Player;

/**
 *
 * @author s0914007
 */
public class PlanRecognition extends AbstractService implements Serializable{

    public ExplanationManager explanationManager;  
    List<Agent> agentManager = new ArrayList<Agent>();
    
    Set<Explanation> totalExplanationList;
    
    double totalProb;
    
    private static Vector Players;
 
    public PlanRecognition(EventObserver processing){
        
        super(processing);
    }

    public List<Agent> getAgentManager() {
        
        return agentManager;
    }

    @Override
    protected void startUp() throws Exception {

            super.startUp();
    }
    
    @Override
    protected void shutDown() throws Exception {

            super.shutDown();
    }
    
    @Override
    protected void handle(Event event) throws Exception {
        
        // Adds players to agentManager
        if(event instanceof  EventRegisterAgent){
            
            EventRegisterAgent newA = (EventRegisterAgent) event;
            
            Agent newAgent = new Agent(newA.getNewPlayer());

            agentManager.add(newAgent);
        }
        
        // Removes a player given the players index number from the environment
        if(event instanceof EventRemoveAgent){
         
            EventRemoveAgent removeAgent = (EventRemoveAgent) event;
            
            // Removes Agent from Agent Manager
            agentManager.remove(removeAgent.getAgentLocationNumber());
        }
        
        if(event instanceof EventSuccessfulOccupation){
            
            EventSuccessfulOccupation currentEvent = (EventSuccessfulOccupation) event;
            
            String agentWon = currentEvent.getPlayerWonName();
            String agentLost = currentEvent.getPlayerLostName();
            
            String continentName = currentEvent.getContinentName();
            String countryName = currentEvent.getCountryName();
            
            // Compute Explanation probabilites based on latest observation
            for(Agent a : agentManager){
                
                if(a.getAgentName().equals(agentWon)){
                    
                    // Add observation to agents observation list
                    a.getAgentObservationSet().add(new ObservationSuccessfulOccupation(continentName, countryName));

                    System.out.println(a.getAgentName());
                    System.out.println(ActionConstants.successfulOccupation + " " + countryName);
                    
                    this.computeExpProbs("Weighted", ActionConstants.successfulOccupation, a);
                    
                    // Sum probabilities of all explanations
                    totalProb = a.computeTotalExpProbabilties();
                    
                    this.printExpProbs(a.getAgentExplanationList());
                }
                
                if(a.getAgentName().equals(agentLost)){
                    
                    a.getAgentObservationSet().add(new ObservationFailedDefence(continentName, countryName));
                    
                    System.out.println(a.getAgentName());
                    System.out.println(ActionConstants.failedDefence + " " + countryName);
                    
                    this.computeExpProbs("Weighted", ActionConstants.failedDefence, a);
                    
                    totalProb = a.computeTotalExpProbabilties();
                    //a.removeCountryAndUpdateList(continentName, country);
                    
                    this.printExpProbs(a.getAgentExplanationList());
                }
            }
            // Testing - Iterate through observation manager
            /*for(String s : observationManager.keySet()){
                
                System.out.println(s);
                
                for(BasicObservation b : observationManager.get(s)){
                    
                    if(b instanceof ObservationCountryOccupied){ System.out.println(b.getActionType() + " " + b.getCountryName()); }
                    if(b instanceof ObservationCountryLost){ System.out.println(b.getActionType() + " " + b.getCountryName()); }
                }
            }*/
            //System.out.println(" ");
        }
        
        if(event instanceof EventFailedOccupation){
            
            EventFailedOccupation currentEvent = (EventFailedOccupation) event;
            
            String agentWon = currentEvent.getPlayerWonName();
            String agentLost = currentEvent.getPlayerLostName();
            
            String continentName = currentEvent.getContinentName();
            String countryName = currentEvent.getCountryName();
            
            // Compute Explanation probabilites based on latest observation
            for(Agent a : agentManager){
                
                if(a.getAgentName().equals(agentWon)){
                    
                    // Add observation to agents observation list
                    a.getAgentObservationSet().add(new ObservationSuccessfulDefence(continentName, countryName));
                    
                    System.out.println(a.getAgentName());
                    System.out.println(ActionConstants.successfulDefence + " " + countryName);

                    this.computeExpProbs("Weighted", ActionConstants.successfulDefence, a);
                    
                    // Sum probabilities of all explanations
                    totalProb = a.computeTotalExpProbabilties();
                    
                   this.printExpProbs(a.getAgentExplanationList());
                }
                
                if(a.getAgentName().equals(agentLost)){
                    
                    a.getAgentObservationSet().add(new ObservationFailedOccupation(continentName, countryName));
                    
                    System.out.println(a.getAgentName());
                    System.out.println(ActionConstants.failedOccupation + " " + countryName);
                    
                    this.computeExpProbs("Weighted", ActionConstants.failedOccupation, a);
                    
                    totalProb = a.computeTotalExpProbabilties();
                    //a.removeCountryAndUpdateList(continentName, country);
                    
                    this.printExpProbs(a.getAgentExplanationList());
                }
            }
        }
        
        if(event instanceof EventCountryReinforced){
            
            EventCountryReinforced reinforcedCountry = (EventCountryReinforced) event;
            
            String playerName = reinforcedCountry.getPlayerName();
            
            String continentName = reinforcedCountry.getContinentName();
            String countryName = reinforcedCountry.getCountryName();
            
            for(Agent a : agentManager){
                
                if(a.getAgentName().equals(playerName)){
                    
                    a.getAgentObservationSet().add(new ObservationCountryReinforced(continentName, countryName));
  
                    System.out.println(a.getAgentName());
                    System.out.println(ActionConstants.countryReinforced + " " + countryName);
                    
                    this.computeExpProbs("Weighted", ActionConstants.countryReinforced, a);
                    
                    totalProb = a.computeTotalExpProbabilties();
                    
                    this.printExpProbs(a.getAgentExplanationList());
                }
            }
            //System.out.println(reinforcedCountry.getPlayerName() + " reinforced " + reinforcedCountry.getCountryName());
        }
        
        if(event instanceof EventArmyMoved){
            
            EventArmyMoved countryMovedTo = (EventArmyMoved) event;
            
            String playerName = countryMovedTo.getPlayerName();
            
            String continentName = countryMovedTo.getContinentName();
            String countryName = countryMovedTo.getCountryName();
            
            for(Agent a : agentManager){
                
                if(a.getAgentName().equals(playerName)){
                    
                    a.getAgentObservationSet().add(new ObservationArmyMovement(continentName, countryName));
                
                    System.out.println(a.getAgentName());
                    System.out.println(ActionConstants.armyMovement + " " + countryName);
                    
                    this.computeExpProbs("Weighted", ActionConstants.armyMovement, a);
                    
                    totalProb = a.computeTotalExpProbabilties();
                
                    this.printExpProbs(a.getAgentExplanationList());
                } 
            }
            //System.out.println(countryMovedTo.getPlayerName() + " " + countryMovedTo.getCountryName()); 
        }
    }
    
    // Method to synchronize Player Vector in this class and all classes that contain a player object
    public void updatePlayers(Vector Players){

        PlanRecognition.setPlayers(Players);

        for(Object p : PlanRecognition.Players){
            
            Player player = (Player) p;
            
            for(Agent agent : agentManager){
                
                if(player.getName().equals(agent.getAgentName())){
                    
                    agent.setPlayer(player);
                }
            }
        }
    }

    // Setup of action space
    public void initialiseActionSpace(Vector Continents){

        this.explanationManager = new ExplanationManager(Continents);

        totalExplanationList = explanationManager.getAllExplanations();
        
        for(Agent a : agentManager){
            
            a.generateExplanationList(totalExplanationList);
        }
    }
        
    public void cacheActivePendingSet(String playerName){
        
        for(Agent a : this.getAgentManager()){

            if(a.getAgentName().equals(playerName)){

                // Cache active pending set
                a.getAgentActivePendingSetHistory().add(a.generateActivePendingSet());
            }
        }
    }
    
    public void printExpProbs(Set<Explanation> expList){
        
        for(Explanation e : expList){

            e.setExplanationProbability(e.normalizeExplanationProbability(totalProb));
            // compute normalized probabilites
            if(e.getExplanationProbability() > 0.10){
                
                System.out.println(e.getMissionName() + " " + e.getExplanationProbability());
            }
        }
        System.out.println(" ");
    }
    
    public void computeExpProbs(String calculationType, String observationType, Agent agent){
        
        Set<BasicAction> filterActiveSet = agent.filterActiveSet(observationType, agent.generateActivePendingSet());
        
        for(Explanation e : agent.getAgentExplanationList()){
        
            e.computeMissionProbability(filterActiveSet, agent.getAgentObservationSet().get(agent.getAgentObservationSet().size() - 1));
        }
    }

    public static Vector getPlayers() {
        
        return PlanRecognition.Players;
    }

    public static void setPlayers(Vector Players) {
        
        PlanRecognition.Players = Players;
    }
}

