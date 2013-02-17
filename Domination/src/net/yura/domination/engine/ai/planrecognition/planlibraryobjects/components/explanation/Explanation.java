/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.explanation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.ComputeExplanationProbabilityWeighted;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.action.BasicAction;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.rootgoalmanagement.RootGoal;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.subgoalmanagement.SubGoal;

/**
 *
 * @author s0914007
 */
public abstract class Explanation implements Mission, ComputeExplanationProbabilityWeighted, Serializable {

    private Set<RootGoal> rootGoalSet = new HashSet<RootGoal>();
    private Set<SubGoal> subGoalSet = new HashSet<SubGoal>();
    
    private List<BasicAction> conActions = new ArrayList<BasicAction>();
    private List<BasicAction> inConActions = new ArrayList<BasicAction>();
    
    private float explanationProbability = 1.0f;
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
    
    public List<BasicAction> getConActions() {
        
        return conActions;
    }

    public List<BasicAction> getInConActions() {
        
        return inConActions;
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
    
    public float getExplanationProbability() {
        
        return explanationProbability;
    }
    
    public void setExplanationProbability(float explanationProbability) {
        
        this.explanationProbability = explanationProbability;
    }
    
    public float normalizeExplanationProbability(float sumExplanationProbabilites){
        
        return explanationProbability / sumExplanationProbabilites;
    }
    
    public void duplicateExplanation(String missionName, Set<RootGoal> rootGoalSet, Set<SubGoal> subGoalSet, List<BasicAction> conActions, List<BasicAction> inConActions){

        this.missionName = missionName;
        this.rootGoalSet = rootGoalSet;
        this.subGoalSet = subGoalSet;
        this.conActions = conActions;
        this.inConActions = inConActions;
    }
}
