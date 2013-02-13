/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.explanation;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.ExplanationNameComponent;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.action.BasicAction;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.subgoalmanagement.SubGoal;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.rootgoalmanagement.RootGoal;

/**
 *
 * @author s0914007
 */
public class BasicExplanation implements ExplanationNameComponent, Serializable {

    private String explanationName;
    private HashSet<RootGoal> rootGoalSet;
    private HashSet<SubGoal> methodChoiceSet;
    private ArrayList<BasicAction> conActions;
    private ArrayList<BasicAction> inConActions;
    
    private float rootSubGoalProbability = 1.0f;
    
    public BasicExplanation(String explanationName, HashSet<RootGoal> setRootGoal, HashSet<SubGoal> methodChoiceSet, ArrayList<BasicAction> conActions, ArrayList<BasicAction> inConActions) {
        
        this.explanationName = explanationName;
        this.rootGoalSet = setRootGoal;
        this.methodChoiceSet = methodChoiceSet;
        this.conActions = conActions;
        this.inConActions = inConActions;
        
        // Explanation computation
        // Multiply each root goal
        for(RootGoal r : getRootGoalSet()){
            
            rootSubGoalProbability *= r.getProbability();
        }
        
        // Multiply each method choice/sub-goal
        for(SubGoal s : getMethodChoiceSet()){
            
            rootSubGoalProbability *= s.getProbability();
        }
    }
       
    public void setRootGoalSet(HashSet<RootGoal> setRootGoal) {
        
        this.rootGoalSet = setRootGoal;
    }

    public HashSet<RootGoal> getRootGoalSet() {
        
        return rootGoalSet;
    }

    public void setMethodChoiceSet(HashSet<SubGoal> methodChoiceSet) {
        
        this.methodChoiceSet = methodChoiceSet;
    }
    
    public HashSet<SubGoal> getMethodChoiceSet() {
        
        return methodChoiceSet;
    }

    public float  getRootSubGoalProbability(){
        
        return rootSubGoalProbability;
    }
    
    public ArrayList<BasicAction> getConActions() {
        
        return conActions;
    }

    public ArrayList<BasicAction> getInConActions() {
        
        return inConActions;
    }

    @Override
    public String getExplanationName() {
        
        return explanationName;
    }
}
