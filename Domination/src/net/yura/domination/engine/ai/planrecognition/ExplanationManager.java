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
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.explanation.OccupyExplanation;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.explanation.missions.OccupyAsiaAfrica;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.explanation.missions.OccupyAsiaSa;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.explanation.missions.OccupyEuAusAfrica;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.explanation.missions.OccupyEuAusAsia;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.explanation.missions.OccupyEuAusNa;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.explanation.missions.OccupyEuAusSa;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.explanation.missions.OccupyEuSaAfrica;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.explanation.missions.OccupyEuSaAsia;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.explanation.missions.OccupyEuSaAus;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.explanation.missions.OccupyEuSaNa;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.explanation.missions.OccupyNaAus;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.explanation.missions.OccupyNaAfrica;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.subgoalmanagement.SubGoal;
import net.yura.domination.engine.core.Continent;

/**
 *
 * @author s0914007
 */
public class ExplanationManager implements Serializable {
    
    private Set<Explanation> explanationSet = new HashSet<Explanation>();
    
    OccupyAsiaAfrica occupyAsiaAfrica = new OccupyAsiaAfrica();
    OccupyAsiaSa occupyAsiaSa = new OccupyAsiaSa();
    OccupyNaAus occupyNaAusExp = new OccupyNaAus();  
    OccupyNaAfrica occupyNaAfrica = new OccupyNaAfrica();
    
    OccupyEuAusAfrica occupyEuAusAfrica = new OccupyEuAusAfrica();
    OccupyEuAusAsia occupyEuAusAsia = new OccupyEuAusAsia();
    OccupyEuAusNa occupyEuAusNa = new OccupyEuAusNa();
    OccupyEuAusSa occupyEuAusSa = new OccupyEuAusSa();
    
    OccupyEuSaAfrica occupyEuSaAfrica = new OccupyEuSaAfrica();
    OccupyEuSaAsia occupyEuSaAsia = new OccupyEuSaAsia();
    OccupyEuSaNa occupyEuSaNa = new OccupyEuSaNa();
    OccupyEuSaAus occupyEuSaAus = new OccupyEuSaAus();
    
    public ExplanationManager(Vector Continent){
        
        explanationSet.add(occupyNaAusExp);
        explanationSet.add(occupyAsiaAfrica);
        explanationSet.add(occupyNaAfrica);
        explanationSet.add(occupyAsiaSa);
        
        explanationSet.add(occupyEuAusAfrica);
        explanationSet.add(occupyEuAusAsia);
        explanationSet.add(occupyEuAusNa);
        explanationSet.add(occupyEuAusSa);
        
        explanationSet.add(occupyEuSaAfrica);
        explanationSet.add(occupyEuSaAsia);
        explanationSet.add(occupyEuSaNa);
        explanationSet.add(occupyEuSaAus);
        
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
