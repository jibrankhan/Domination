/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.yura.domination.engine.ai.planrecognition;

import com.google.common.collect.ArrayListMultimap;
import net.yura.domination.engine.ai.planrecognition.events.Event;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Vector;
import net.yura.domination.engine.ai.planrecognition.environmentdatamanagement.Agent;
import net.yura.domination.engine.ai.planrecognition.eventhandling.AbstractService;
import net.yura.domination.engine.ai.planrecognition.eventhandling.Processing;
import net.yura.domination.engine.ai.planrecognition.events.EventCountryReinforced;
import net.yura.domination.engine.ai.planrecognition.events.EventRegisterAgent;
import net.yura.domination.engine.ai.planrecognition.events.EventSuccessfulOccupation;
import net.yura.domination.engine.ai.planrecognition.events.EventCountryPlacement;
import net.yura.domination.engine.ai.planrecognition.events.EventArmyMoved;
import net.yura.domination.engine.ai.planrecognition.events.EventFailedOccupation;
import net.yura.domination.engine.ai.planrecognition.events.EventRemoveAgent;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.action.BasicAction;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.explanation.Explanation;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.observation.BasicObservation;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.observation.ObservationArmyMovement;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.observation.ObservationFailedDefence;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.observation.ObservationSuccessfulOccupation;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.observation.ObservationCountryReinforced;
import net.yura.domination.engine.core.Country;
import net.yura.domination.engine.core.Player;

/**
 *
 * @author s0914007
 */
public class PlanRecognition extends AbstractService implements Serializable{

    public ExplanationManager actionManager;  
    ArrayList<Agent> agentManager = new ArrayList<Agent>();
    
    HashSet<Explanation> totalExplanationList;
    
    float totalProb;
    
    Vector Players;
 
    public PlanRecognition(Processing processing){
        
        super(processing);
    }

    public ArrayList<Agent> getAgentManager() {
        
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
        
        // Adds players to playerManager
        if(event instanceof  EventRegisterAgent){
            
            EventRegisterAgent newA = (EventRegisterAgent) event;
            
            Agent newAgent = new Agent(newA.getNewPlayer());

            agentManager.add(newAgent);
            // Initialise observation list for agent
            //observationManager.put(newA.getPlayerName(), null);
            
            // Test
            /*for(Agent a : agentManager){
                
                System.out.println(a.getAgentName());
            }*/
        }
        
        // Removes a player given the players index number from the environment
        if(event instanceof EventRemoveAgent){
         
            EventRemoveAgent removeAgent = (EventRemoveAgent) event;
            
            // Removes Agent from Agent Manager
            agentManager.remove(removeAgent.getAgentLocationNumber());
            
            // Remove Agents observation list - AGENT'S CANT HAVE SAME NAME!
            //observationManager.removeAll(removeAgent.getAgentName());   
            /*for(Agent a : agentManager){
                
                System.out.println(a.getAgentName());
            }*/
        }
        
        // Adds country with pending set to the player that gains it
        if(event instanceof EventCountryPlacement){
            
            EventCountryPlacement currentEvent = (EventCountryPlacement) event;
                
            for(Agent agent : agentManager){

               // If name of player is equal to name of player that gained this country             
               boolean isAgent = agent.getAgentName().equals(currentEvent.getPlayerName());

               //System.out.println(isAgent);

               if(isAgent){

                   agent.updateExplanationList(totalExplanationList);

                   // Testing
                   //System.out.println(agent.getAgentName());
                   /*for(Explanation e : agent.getAgentExplanationList()){

                       System.out.println(e.getExplanationName());
                   }*/
                   //System.out.println(agent.getAgentExplanationList().size());
               } 
            }
        }
        
        if(event instanceof EventSuccessfulOccupation){
            
            EventSuccessfulOccupation currentEvent = (EventSuccessfulOccupation) event;
            
            String agentWon = currentEvent.getPlayerWonName();
            String agentLost = currentEvent.getPlayerLostName();
            
            String continentName = currentEvent.getContinentName();
            String countryName = currentEvent.getCountryName();
            
            //System.out.println(continentName);
            //System.out.println(countryName);
            
            // Compute Explanation probabilites based on latest observation
            for(Agent a : agentManager){
                
                if(a.getAgentName().equals(agentWon)){
                    
                    // Add observation to agents observation list
                    a.getAgentObservationSet().add(new ObservationSuccessfulOccupation(continentName, countryName));
                    
                    // Add any new explanations
                    a.updateExplanationList(totalExplanationList);
                    
                    // Set calculation mode here!
                    // Uniform = Uniform Distribution
                    // Weighted = Weighted Distribution
                    for(Explanation e : a.getAgentExplanationList()){
                        
                        a.computeExpProbability("Weighted", "Successful Occupation" , e);
                    }
                    
                    /*System.out.println(a.getAgentName());
                    
                    for(Explanation e : a.getAgentExplanationList()){
                        
                        System.out.println(e.getExplanationName());
                    }*/
                    
                    // Sum probabilities of all explanations
                    totalProb = a.computeTotalExpProbabilties();
                    
                    // Testing = Check if explanation values are right
                    for(Explanation e : a.getAgentExplanationList()){

                        // compute normalized probabilites
                        System.out.println(e.getExplanationName() + " " + e.normalizeExplanationProbability(totalProb));
                    }
                    System.out.println(" ");
                }
                
                if(a.getAgentName().equals(agentLost)){
                    
                    a.getAgentObservationSet().add(new ObservationFailedDefence(continentName, countryName));
                    
                    for(Explanation e : a.getAgentExplanationList()){
                        
                        a.computeExpProbability("Weighted", "Failed Defence", e);
                    }
                    
                    totalProb = a.computeTotalExpProbabilties();
                    //a.removeCountryAndUpdateList(continentName, country);
                    
                    // Testing = Check if explanation values are right
                    for(Explanation e : a.getAgentExplanationList()){

                        // compute normalized probabilites
                        System.out.println(e.getExplanationName() + " " + e.normalizeExplanationProbability(totalProb));
                    }
                    System.out.println(" ");
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
            
            
        }
        
        if(event instanceof EventCountryReinforced){
            
            EventCountryReinforced reinforcedCountry = (EventCountryReinforced) event;
            
            String playerName = reinforcedCountry.getPlayerName();
            
            String continentName = reinforcedCountry.getContinentName();
            String countryName = reinforcedCountry.getCountryName();
            
            //System.out.println("Hello?");
            
            for(Agent a : agentManager){
                
                if(a.getAgentName().equals(playerName)){
                    
                    a.getAgentObservationSet().add(new ObservationCountryReinforced(continentName, countryName));
                    
                    for(Explanation e : a.getAgentExplanationList()){
                        
                        a.computeExpProbability("Weighted", "Reinforce", e);
                    }
                    
                    totalProb = a.computeTotalExpProbabilties();
                    
                    // Testing = Check if explanation values are right
                    for(Explanation e : a.getAgentExplanationList()){

                        // compute normalized probabilites
                        System.out.println(e.getExplanationName() + " " + e.normalizeExplanationProbability(totalProb));
                    }
                    System.out.println(" ");
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
                }
                
                for(Explanation e : a.getAgentExplanationList()){
                        
                        a.computeExpProbability("Weighted", "Army Movement", e);
                }
                
                totalProb = a.computeTotalExpProbabilties();
                
                for(Explanation e : a.getAgentExplanationList()){

                        // compute normalized probabilites
                        System.out.println(e.getExplanationName() + " " + e.normalizeExplanationProbability(totalProb));
                }
                System.out.println(" ");
            }
            //System.out.println(countryMovedTo.getPlayerName() + " " + countryMovedTo.getCountryName()); 
        }
    }
    
        // Method to synchronize Player Vector
        public void updatePlayers(Vector Players){
            
            this.Players = Players;
            
            /*for(Object p : Players){
                
                Player currentPlayer = (Player) p;
                
                System.out.println(currentPlayer.getName());
            }*/
        }
        
        // Setup of action space
        public void initialiseActionSpace(Vector Continents){

            this.actionManager = new ExplanationManager(Continents);

            totalExplanationList = actionManager.getExplanationList();
            
            /*for(Explanation e : totalExplanationList){
                
                System.out.println(e.getExplanationName());
            }*/
        }
        
        public void cacheActivePendingSet(String playerName){
        
        for(Agent a : this.getAgentManager()){

            if(a.getAgentName().equals(playerName)){

                // Cache active pending set
                a.getAgentActivePendingSetHistory().add(a.generateActivePendingSet());
            }
        }
    }
}

