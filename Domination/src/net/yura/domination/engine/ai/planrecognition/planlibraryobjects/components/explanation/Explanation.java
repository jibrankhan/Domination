/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.explanation;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.action.BasicAction;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.rootgoalmanagement.RootGoal;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.subgoalmanagement.SubGoal;

/**
 *
 * @author s0914007
 */
public abstract class Explanation implements Mission, Cloneable, Serializable {

    private Set<RootGoal> rootGoalSet = new HashSet<RootGoal>();
    private Set<SubGoal> subGoalSet = new HashSet<SubGoal>();
    
    private Set<BasicAction> conActions = new HashSet<BasicAction>();
    private Set<BasicAction> inConActions = new HashSet<BasicAction>();
    
    private double explanationProbability = 1.0f;
    private String missionName;
    
    public String getMissionName() {
    
        return missionName;
    }

    public void setMissionName(String missionName) {
        
        this.missionName = missionName;
    } 
    
    public void setRootGoalSet(Set<RootGoal> setRootGoal) {
        
        this.rootGoalSet = setRootGoal;
    }

    public Set<RootGoal> getRootGoalSet() {
        
        return rootGoalSet;
    }

    public void setSubGoalSet(Set<SubGoal> methodChoiceSet) {
        
        this.subGoalSet = methodChoiceSet;
    }
    
    public Set<SubGoal> getSubGoalSet() {
        
        return subGoalSet;
    }
    
    public Set<BasicAction> getConActions() {
        
        return conActions;
    }

    public Set<BasicAction> getInConActions() {
        
        return inConActions;
    }

    public void setExplanationProbability(double explanationProbability) {
        
        this.explanationProbability = explanationProbability;
    }
    
        
    public double getExplanationProbability() {
        
        return explanationProbability;
    }
    
    public double normalizeExplanationProbability(double sumExplanationProbabilites){
        
        return explanationProbability / sumExplanationProbabilites;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException{
        
        return super.clone();
    }
    
    public void calcInitialExpProb(){
        
        for(RootGoal r : getRootGoalSet()){
            
            explanationProbability *= r.getProbability();
        }
        
        // Multiply each method choice/sub-goal
        for(SubGoal s : getSubGoalSet()){
            
            explanationProbability *= s.getProbability();
        }
    }
    
    public Set<BasicAction> filterSet(String observationType, Set<BasicAction> actionSet){
        
        Set<BasicAction> filteredSet = new HashSet<BasicAction>();
           
        for(BasicAction b: actionSet){

            if(b.getActionType().equals(observationType)){

                filteredSet.add(b);
                //System.out.println(b.getActionType() + " " + b.getCountryName());
            }
        }
        
        return  filteredSet;
    }
    
    // Method to calculate weight based on provided number of actions and actions to weight
    public double computeBaseWeight(double weight, int numActions, int numActionsToWeight){
        
        double sumWeight = weight * (double) numActionsToWeight;
        double leftOver = 1d - sumWeight;
        
        double base = leftOver / (double) numActions;
        
        return base;
    }
}
