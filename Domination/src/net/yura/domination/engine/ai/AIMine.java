/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.yura.domination.engine.ai;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;
import net.yura.domination.engine.core.Card;
import net.yura.domination.engine.core.Country;
import net.yura.domination.engine.core.Player;
import net.yura.domination.engine.core.RiskGame;

/**
 *
 * @author s0914007
 */
public class AIMine extends AIEasy {
    
    final static String fileName = "C:\\temp\\game.txt";
    List<String> commandList = new ArrayList<String>();
    int commandCounter = 0;

    public AIMine() {
        try{
            
            this.read(fileName);
            
        } catch(IOException e){
            
        }
    }

    @Override
    public String getPlaceArmies() {
        
        System.out.println("Game State " + this.game.getState());
        
        super.getPlaceArmies();
        return commandList.get(commandCounter++);
    }
    
    @Override
    public String getAttack() {
        
        System.out.println("Game State " + this.game.getState());
        
        super.getAttack();
        return commandList.get(commandCounter++);
    }

    @Override
    public String endGo() {
        
        super.endGo();
        return commandList.get(commandCounter++);
    }

    @Override
    public String getRoll() {
        
        super.getRoll();
        return commandList.get(commandCounter++);
    }
    
    void read(String fileName) throws IOException {
     
        //log("Reading from file " + fileName);
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line = null;
        while((line = reader.readLine()) != null){
            
            commandList.add(line);
            //reader.close();
        }
        
        /*for(String s : commandList){
            
            System.out.println(s);
        }*/
        
        reader.close();
    }
	
    private void log(String aMessage){
		
	    System.out.println(aMessage);
    }
}
