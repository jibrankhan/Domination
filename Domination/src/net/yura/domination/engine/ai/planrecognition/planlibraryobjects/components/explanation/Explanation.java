/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.explanation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.action.BasicAction;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.observation.BasicObservation;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.subgoalmanagement.SubGoal;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.rootgoalmanagement.RootGoal;

/**
 *
 * @author s0914007
 */
public class Explanation extends BasicExplanation implements Serializable {
    
    private float explanationProbability;
    
    public Explanation(String explanationName, HashSet<RootGoal> rootGoalSet, HashSet<SubGoal> methodChoiceSet, ArrayList<BasicAction> conActions, ArrayList<BasicAction> inConActions){
        
        super(explanationName, rootGoalSet, methodChoiceSet, conActions, inConActions);
        explanationProbability = getRootSubGoalProbability();
    }
    
    // Updates the explanation with a single observation from the environment
    public void computeExplanationProbabilitySingleObservation(int activePendingSetSize, BasicObservation currentObservation){
            
            explanationProbability *= computeActionProbabilityEqual(activePendingSetSize);
    }
    
    public void computeExplanationProbabilityWeighted(String ObservationType, HashSet<BasicAction> activeSet, BasicObservation currentObservation){
        
        ArrayList<BasicAction> conActiveSet = new ArrayList<BasicAction>();
        ArrayList<BasicAction> inConActiveSet = new ArrayList<BasicAction>();
        
        int conActionCounter = 0;
        int inConActionCounter = 0;
        
        if(!ObservationType.equals("Failed Defence") && !ObservationType.equals("Successful Defence")){

            // Filters out actions that are not in the same action type
            for(BasicAction ba : this.getConActions()){

                if(ba.getActionType().equals(ObservationType)){

                    conActiveSet.add(ba);
                }
            }

            /*System.out.println("Agents Active Pending Set");
            for(BasicAction a : activeSet){

                System.out.println(a.getActionType() + " " + a.getCountryName());
            }
            System.out.println(" ");
            System.out.println("Explanation Consistent Actions");
            for(BasicAction b : conActiveSet){

                System.out.println(b.getActionType() + " " + b.getCountryName());
            }
            System.out.println(" ");*/

            // Count number of inconsistent and consistent actions in active pending set when action took place
            for(BasicAction b : activeSet){

                if(conActiveSet.contains(b)){

                    conActionCounter++;

                } 
            }

            //System.out.println(conActionCounter);
            //System.out.println(" ");

            // Use a different maths function to calculate weighting amounts!
            float conActionProb = 0.6f / conActionCounter;
            // If it is not consistent it is inconsistent
            float inconActionProb = 0.4f / (activeSet.size() - conActionCounter);

            //System.out.println(currentObservation.getActionType() + " " + currentObservation.getCountryName());

            if(conActiveSet.contains(currentObservation)){

                System.out.println(getExplanationName() + " consistent action!" + " " + explanationProbability + " * " + conActionProb);
                //System.out.println(" ");
                explanationProbability *= conActionProb;

            } else { 

                System.out.println(getExplanationName() + " inconsistent action!" + " " + explanationProbability + " * " + inconActionProb);
                //System.out.println(" ");
                explanationProbability *= inconActionProb;     
            }
        } else {
            
            // Filters out actions that are not in the same action type
            for(BasicAction ba : this.getInConActions()){

                if(ba.getActionType().equals(ObservationType)){

                    inConActiveSet.add(ba);
                }
            }
            
            for(BasicAction ba : this.getConActions()){
                
                if(ba.getActionType().equals(ObservationType)){
                    
                    conActiveSet.add(ba);
                }
            }
            
            // Count number of inconsistent and consistent actions in active pending set when action took place
            for(BasicAction b : activeSet){

                //System.out.println(b.getActionType() + " " + b.getCountryName());
                if(inConActiveSet.contains(b)){

                    inConActionCounter++;
                } 
            }
            
            for(BasicAction b : activeSet){
                
                if(conActiveSet.contains(b)){
                    
                    conActionCounter++;
                }
            }
            
             // Use a different maths function to calculate weighting amounts!
            float conActionProb = 1.02f;
            // If it is not consistent it is inconsistent
            float inconActionProb = 0.98f;
            
            //System.out.println(currentObservation.getActionType() + " " + currentObservation.getContinentName() + " " + currentObservation.getCountryName());
            if(inConActiveSet.contains(currentObservation)){

                System.out.println(getExplanationName() + " inconsistent action!" + " " + explanationProbability + " * " + inconActionProb);
                //System.out.println(" ");
                explanationProbability *= inconActionProb;

            } else if(conActiveSet.contains(currentObservation)){
                
                System.out.println(getExplanationName() + " consistent action!" + " " + explanationProbability + " * " + conActionProb );
                explanationProbability *= conActionProb;
                
            } else { 

                System.out.println(getExplanationName() + " action does not apply!");
                //System.out.println(" ");   
            }
        }
    }
    
    public float normalizeExplanationProbability(float sumExplanationProbabilites){
        
        return explanationProbability / sumExplanationProbabilites;
    }
    
    public float getExplanationProbability(){
        
        return explanationProbability;
    }

    public void setExplanationProbability(float explanationProbability) {
        
        this.explanationProbability = explanationProbability;
    }
    
    // TODO Make weighted distribution
    // Simple equal distribution
    public float computeActionProbabilityEqual(int activePendingSetSize){
        
        //System.out.println(activePendingSetSize);
        float actionProbability = 1.0f/activePendingSetSize;
        
        return actionProbability;
    }
}
