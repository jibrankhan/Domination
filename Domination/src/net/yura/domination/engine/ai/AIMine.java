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
public class AIMine extends AICrap {
    
    final static String fileName = "C:\\temp\\input.txt";
    List<String> commandList = new ArrayList<String>();
    int commandCounter = 0;

    public AIMine() {
        
        try{
            
            this.read(fileName);
            
        } catch(IOException e){
            
        }
    }
    
    @Override
    public String getBattleWon() {
        
        return "move all";
    }
    
    @Override
    public String getTacMove() {
	return "nomove";
    }
    
    @Override
    public String getTrade() {

		List<Card> cards = player.getCards();

		if (cards.size() < 3) {
			return "endtrade";
		}

		Card[] result = game.getBestTrade(cards, tradeCombinationsToScan());

		if (result != null) {
			String output = "trade ";
			output = getCardName(result[0], output);
			output = output + " ";
			output = getCardName(result[1], output);
			output = output + " ";
			output = getCardName(result[2], output);
			return output;
		}

		return "endtrade";
    }
    
    @Override
    public int tradeCombinationsToScan() {
		return 1;
    }
           
    @Override
    public String getCardName(Card card1, String output) {
		if (card1.getName().equals("wildcard")) {
			output = output + card1.getName();
		} else {
			output = output + card1.getCountry().getColor();
		}
		return output;
    }
    
    @Override
    public String getPlaceArmies() {
		if ( game.NoEmptyCountries()==false ) {
                   
                    return commandList.get(commandCounter++);
		}
		return "placearmies " + randomCountry(player.getTerritoriesOwned()).getColor() +" 1";
    }
    
    @Override
    public String getAttack() {
	return "endattack";
    }

    @Override
    public String getRoll() {
	return "retreat";
    }

    @Override
    public String getCapital() {
	    return "capital " + randomCountry(player.getTerritoriesOwned()).getColor();
    }
    
    @Override
    public Country randomCountry(List<Country> countries) {
    	if (countries.isEmpty()) {
    		return null;
    	}
    	return countries.get( r.nextInt(countries.size()) );
    }

	public String getAutoDefendString() {
	    int n=game.getDefender().getArmies();
        return "roll "+Math.min(game.getMaxDefendDice(), n);
	}
    
    /**
     * Checks whether a country owns its neighbours
     * @param p player object, c Country object
     * @return boolean True if the country owns its neighbours, else returns false
     */
    @Override
    public boolean ownsNeighbours(Player p, Country c) {
        List<Country> neighbours = c.getNeighbours();

        for (int i=0; i<neighbours.size(); i++) {
           if ( neighbours.get(i).getOwner() != p) {
        	   return false;
           }
        }

        return true;
    }
    
    @Override
    public void setPlayer(RiskGame game) {
    	this.game = game;
    	this.player = game.getCurrentPlayer();
    }
        
    /**
     * Finds all countries that can be attacked from.
     * @param p player object
     * @param attack true if this is durning attack, which requires the territority to have 2 or more armies
     * @return a Vector of countries, never null
     */
    public List<Country> findAttackableTerritories(Player p, boolean attack) {
    	List<Country> countries = p.getTerritoriesOwned();
    	List<Country> result = new ArrayList<Country>();
    	for (int i=0; i<countries.size(); i++) {
    		Country country = countries.get(i);
    		if ((!attack || country.getArmies() > 1) && !ownsNeighbours(p, country)) {
				result.add(country);
    		}
    	}
    	return result;
    }
    
    void read(String fileName) throws IOException {
     
        log("Reading from file " + fileName);
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line = null;
        while((line = reader.readLine()) != null){
            
            commandList.add(line);
            //reader.close();
        }
        
        for(String s : commandList){
            
            System.out.println(s);
        }
        
        reader.close();
    }
	
    private void log(String aMessage){
		
	    System.out.println(aMessage);
    }
}
