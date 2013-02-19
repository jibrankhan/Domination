// Yura Mamyrin

package net.yura.domination.engine.ai;

import java.util.List;
import java.util.Random;

import net.yura.domination.engine.core.Card;
import net.yura.domination.engine.core.Country;
import net.yura.domination.engine.core.Player;
import net.yura.domination.engine.core.RiskGame;

/**
 * THIS IS NOT A REAL AI, THIS IS WHAT A HUMAN PLAYER THAT HAS RESIGNED FROM A GAME BECOMES
 * SO THAT OTHER PLAYERS CAN CARRY ON PLAYING, THIS AI NEVER ATTACKS ANYONE, JUST FOLLOWS RULES
 * @author Yura Mamyrin
 */
public class AICrap {

    protected Random r = new Random(); // this was always static

    protected RiskGame game;
    protected Player player;

    public String getBattleWon() {
	return "move all";
    }

    public String getTacMove() {
	return "nomove";
    }

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

	/**
	 * @return a bounding factor for the number of trades to scan
	 */
	public int tradeCombinationsToScan() {
		return 1;
	}

	public String getCardName(Card card1, String output) {
		if (card1.getName().equals("wildcard")) {
			output = output + card1.getName();
		} else {
			output = output + card1.getCountry().getColor();
		}
		return output;
	}

    public String getPlaceArmies() {
		if ( game.NoEmptyCountries()==false ) {
		    return "autoplace";
		}
		return "placearmies " + randomCountry(player.getTerritoriesOwned()).getColor() +" 1";
    }

    public String getAttack() {
	return "endattack";
    }

    public String getRoll() {
	return "retreat";
    }

    public String getCapital() {
	    return "capital " + randomCountry(player.getTerritoriesOwned()).getColor();
    }
    
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
    public boolean ownsNeighbours(Player p, Country c) {
        List<Country> neighbours = c.getNeighbours();

        for (int i=0; i<neighbours.size(); i++) {
           if ( neighbours.get(i).getOwner() != p) {
        	   return false;
           }
        }

        return true;
    }
    
    public void setPlayer(RiskGame game) {
    	this.game = game;
    	this.player = game.getCurrentPlayer();
    }
    
    public String endGo(){
        
        return "endgo";
    }

}
