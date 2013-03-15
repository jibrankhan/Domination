//  Group D

package net.yura.domination.engine.ai;

import java.util.Arrays;
import java.util.List;

import net.yura.domination.engine.core.Continent;
import net.yura.domination.engine.core.Country;
import net.yura.domination.engine.core.Player;

/**
 * <p> Class for AIHardPlayer </p>
 * @author SE Group D
 * TODO infer the other missions
 */
public class AIHardMission extends AIHardDomination {

	protected List<Country> getBorder(GameState gs) {
		List<Country> result = super.getBorder(gs);
		if (player.getMission().getNoofarmies() > 1 
				&& (player.getTerritoriesOwned().size()*2 > player.getMission().getNoofcountries() && pressAttack(gs) || player.getTerritoriesOwned().size() - 3 >= player.getMission().getNoofcountries())) {
			//this is redundant, but that's ok, what matters is the absolute ordering
			result.addAll(player.getTerritoriesOwned());
		}
		return result;
	}
	
	protected boolean shouldEndAttack(GameState gameState) {
		boolean result = super.shouldEndAttack(gameState);
		if (result && isCloseToTerritoryTarget()) {
			return false;
		}
		return result;
	}
	
	protected int scoreCountry(Country country) {
		int result = super.scoreCountry(country);
		if (player.getMission().getPlayer() != null && !isTargetMoot()) {
			List<Country> n = country.getNeighbours();
			for (int i = 0; i < n.size(); i++) {
				Country nc = n.get(i);
				if (nc.getOwner() == player.getMission().getPlayer()) {
					result--;
				}
			}
		}
		return result;
	}
	
	private boolean isCloseToTerritoryTarget() {
		return player.getMission().getNoofcountries() > 0 
		&& (player.getMission().getPlayer() == null || isTargetMoot())
		&& player.getTerritoriesOwned().size() - 3 >= player.getMission().getNoofcountries();
	}
	
	protected boolean pressAttack(GameState gameState) {
		boolean result = super.pressAttack(gameState);
		if (!result && isCloseToTerritoryTarget()) {
			return true;
		}
		return result;
	}
	
	protected int getMinPlacement() {
		return Math.max(1, player.getMission().getNoofarmies());
	}
	
	public GameState getGameState(Player p) {
		GameState g = super.getGameState(p);
		if (player.getMission().getPlayer() != null && !isTargetMoot() && player.getMission().getPlayer() != g.orderedPlayers.get(0).p) {
			g.targetPlayers = Arrays.asList(player.getMission().getPlayer(), g.orderedPlayers.get(0).p);
		}
		return g;
	}
	
	private boolean isTargetMoot() {
		return player.getMission().getPlayer() != null && (player.getMission().getPlayer() == player || player.getMission().getPlayer().getTerritoriesOwned().isEmpty());
	}
	
	protected double getContinentValue(Continent co) {
		double result = super.getContinentValue(co);
		if (player.getMission().getContinent1() == co
				|| player.getMission().getContinent2() == co
				|| player.getMission().getContinent3() == co
				) {
			result *= 4;
		}
		return result;
	}

}
