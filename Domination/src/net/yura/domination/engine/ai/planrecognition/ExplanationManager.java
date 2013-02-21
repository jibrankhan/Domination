/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.yura.domination.engine.ai.planrecognition;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.explanation.Explanation;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.explanation.missions.OccupyAsiaAfrica;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.explanation.OccupyExplanation;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.explanation.missions.OccupyNAAfrica;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.explanation.missions.OccupyNAAus;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.subgoalmanagement.SubGoal;
import net.yura.domination.engine.core.Continent;

/**
 *
 * @author s0914007
 */
public class ExplanationManager implements Serializable {
    
    private Set<Explanation> explanationSet = new HashSet<Explanation>();
    
    OccupyNAAus occupyNAAusExp = new OccupyNAAus();
    OccupyAsiaAfrica occupyAsiaAfrica = new OccupyAsiaAfrica();
    OccupyNAAfrica occupyNAAfrica = new OccupyNAAfrica();
    
    public ExplanationManager(Vector Continent){
        
        explanationSet.add(occupyNAAusExp);
        explanationSet.add(occupyAsiaAfrica);
        explanationSet.add(occupyNAAfrica);
        
        // Automated building of explanations
        for(Object c : Continent){
            
            Continent continent = (Continent) c; 
            
            for(Explanation e : explanationSet){
                
                // Builing of Occupy explanations
                if(e instanceof OccupyExplanation){
                    
                    OccupyExplanation occupyExp = (OccupyExplanation) e;
                    
                    for(SubGoal s : e.getSubGoalSet()){

                        if(continent.getName().equals(s.getActionLocation())){

                            occupyExp.addConsistentActions(continent);
                        }
                    }
                }
            } 
        }
        
        System.out.println("ALL EXPLANATIONS INITIALIZED!");
    }  
    
    public Set<Explanation> getAllExplanations() {
        
        return explanationSet;
    }
}
