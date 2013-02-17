/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.yura.domination.engine.ai.planrecognition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.explanation.Explanation;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.explanation.OccupyAsiaAfrica;
import net.yura.domination.engine.ai.planrecognition.planlibraryobjects.components.explanation.OccupyNAAusExp;
import net.yura.domination.engine.core.Continent;

/**
 *
 * @author s0914007
 */
public class ExplanationManager implements Serializable {
    
    private HashSet<Explanation> explanationList = new HashSet<Explanation>();
    
    OccupyNAAusExp occupyNAAusExp = new OccupyNAAusExp();
    OccupyAsiaAfrica occupyAsiaAfrica = new OccupyAsiaAfrica();
    
    public ExplanationManager(Vector Continent){
        
        // Automated building of each action set per country and each explanation 
        for(Object continent : Continent){
            
            Continent currentContinent = (Continent) continent;
            
            if(currentContinent.getName().equals("North America")){
                
                occupyNAAusExp.addConsistentActions(currentContinent);
            }
            
            if(currentContinent.getName().equals("Asia")){
                
                occupyAsiaAfrica.addConsistentActions(currentContinent);
            }
            
            if(currentContinent.getName().equals("Africa")){
                
                occupyAsiaAfrica.addConsistentActions(currentContinent);
            }
            
            if(currentContinent.getName().equals("Australia")){
                
                occupyNAAusExp.addConsistentActions(currentContinent);
            }   
        }
        
        explanationList.add(occupyNAAusExp);
        explanationList.add(occupyAsiaAfrica);
        
        System.out.println("All Explanations and Countires Initialized!");
    }  
    
    public HashSet<Explanation> getExplanationList() {
        
        return explanationList;
    }
}
