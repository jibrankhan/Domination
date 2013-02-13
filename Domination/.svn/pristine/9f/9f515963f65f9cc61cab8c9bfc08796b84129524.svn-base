//  Group D

package net.yura.domination.engine.ai;

import java.util.*;

import net.yura.domination.engine.core.Continent;
import net.yura.domination.engine.core.Country;
import net.yura.domination.engine.core.Player;
import net.yura.domination.engine.core.RiskGame;
import net.yura.domination.engine.core.Statistic;

/**
 * <p> Class for AIHardPlayer </p>
 * @author SE Group D
 * 
 * TODO:
 * fear reprisals
 */
public class AIHardDomination extends AIEasy {
	
	/**
	 * Contains quick information about the player
	 */
	static class PlayerState implements Comparable<PlayerState> {
		Player p;
		double attackValue;
		int defenseValue;
		int attackOrder;
		double playerValue;
		Set<Continent> owned;
		int armies;
		boolean strategic;
		
		public int compareTo(PlayerState ps) {
			if (playerValue != ps.playerValue) {
				return (int)Math.signum(playerValue - ps.playerValue);
			}
			return p.getCards().size() - ps.p.getCards().size();
		}
		
		public String toString() {
			return p.toString();
		}
	}
	
	/**
	 * Overview of the Game
	 */
	static class GameState {
		PlayerState me;
		Player[] owned; 
		List<PlayerState> orderedPlayers;
		List<Player> targetPlayers;
		Set<Country> capitals;
		PlayerState commonThreat;
	}
	
	/**
	 * A single target for attack that may contain may possible attack routes
	 */
	static class AttackTarget implements Comparable<AttackTarget>, Cloneable {
		int remaining = Integer.MIN_VALUE;
		int[] routeRemaining;
		int[] eliminationScore;
		Country[] attackPath;
		Country targetCountry;
		int depth;
		
		public AttackTarget(int fromCountries, Country targetCountry) {
			routeRemaining = new int[fromCountries];
			Arrays.fill(routeRemaining, Integer.MIN_VALUE);
			attackPath = new Country[fromCountries];
			this.targetCountry = targetCountry;
		}
		
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append(targetCountry).append(" ").append(remaining).append(":(");
			for (int i = 0; i < attackPath.length; i ++) {
				if (attackPath[i] == null) {
					continue;
				}
				sb.append(attackPath[i]).append(" ").append(routeRemaining[i]).append(" ");
			}
			sb.append(")");
			return sb.toString();
		}

		public int compareTo(AttackTarget obj) {
			int diff = remaining - obj.remaining;
			if (diff != 0) {
				return diff;
			}
			return targetCountry.getColor() - obj.targetCountry.getColor(); 
		}
		
		public AttackTarget clone() {
			try {
				return (AttackTarget) super.clone();
			} catch (CloneNotSupportedException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	/**
	 * A target to eliminate
	 */
	static class EliminationTarget implements Comparable<EliminationTarget> {
		List<AttackTarget> attackTargets = new ArrayList<AttackTarget>();
		PlayerState ps;
		boolean target;
		boolean allOrNone;
		Continent co;
		
		public int compareTo(EliminationTarget other) {
			if (this.target) {
				return -1;
			}
			if (other.target) {
				return 1;
			}
			int diff = other.ps.p.getCards().size() - ps.p.getCards().size();
			if (diff != 0) {
				return diff;
			}
			return ps.defenseValue - other.ps.defenseValue;
		}
		
		public String toString() {
			return "Eliminate " + (co != null?co:ps.p);
		}
	}

	public String getPlaceArmies() {
	   if ( game.NoEmptyCountries() ) {
	       return plan(false);
	   }
	   return findEmptyCountry();
    }

    /** 
     * ai looks at all the continents and tries to see which one it should place on
     * first it simply looks at the troops on each continent
     * then it looks at each player's potential moves.
	 */
	private String findEmptyCountry() {
		Continent[] cont = game.getContinents();

		double check = -Double.MAX_VALUE;
		Country toPlace = null;
		Map<Player, Integer> players = new HashMap<Player, Integer>();
		for (int i = 0; i < this.game.getPlayers().size(); i++) {
			players.put((Player) this.game.getPlayers().get(i), Integer.valueOf(i));
		}

		outer: for (int i = 0; i < cont.length; i++) {
			Continent co = cont[i];

			List<Country> ct = co.getTerritoriesContained();
			int bestCountryScore = 0;
			
			boolean hasFree = false;
			Country preferedCountry = null;
			int[] troops = new int[game.getPlayers().size()];

			for (int j = 0; j < ct.size(); j++) {
				Country country = ct.get(j);
				if (country.getOwner() == null) {
					hasFree = true;
					int countryScore = scoreCountry(country);
					if (preferedCountry == null || countryScore < bestCountryScore || (countryScore == bestCountryScore && r.nextBoolean())) {
						bestCountryScore = countryScore;
						preferedCountry = country;
					}
				} else {
					Integer index = players.get(country.getOwner());
					troops[index.intValue()]++;
				}
			}
			
			if (!hasFree) {
				continue;
			}
			
			/* Calculate the base value of that continent */
			double continentValue = getContinentValue(co);
			
			for (int j = 0; j < troops.length; j++) {
				int numberofEnemyUnits = 0;
				int territorynum = 1;
				int numberOfEnemies = 0;
				for (int k = 0; k < troops.length; k++) {
					if (j == k) {
						territorynum += troops[k];
					} else {
						numberofEnemyUnits += troops[k];
						if (troops[k] > 0) {
							numberOfEnemies++;
						}
					}
				}
				
				double score = territorynum / Math.max(1d, (numberofEnemyUnits * numberOfEnemies));
				score *= continentValue;
				score /= bestCountryScore;

				Player p = (Player)game.getPlayers().get(j);
				
				if (p != this.player) {
					//always block
					if (territorynum == ct.size()) {
						toPlace = preferedCountry;
						break outer;
					}
					//otherwise prefer your best move over the opponents
					score/=4; 
				}
				
				if (check <= score) {
					check = score;
					toPlace = preferedCountry;
				} else if (toPlace == null) {
					toPlace = preferedCountry;
				}
			}
		}
		
		if (toPlace == null) {
			return "autoplace";
		}
		return "placearmies " + toPlace.getColor() + " 1";
	}

	/**
	 * Gives a score (lower is better) to a country
	 */
	protected int scoreCountry(Country country) {
		final int n = country.getIncomingNeighbours().size();
		int countryScore = n + 6; //normalize so that 1 is the best score for an empty country
		if (country.getArmies() > 0) {
			countryScore += n;
			countryScore -= country.getArmies();
		}
		if (n < 3) {
			countryScore -= 2;
		}
		if (game.getSetup() && country.getCrossContinentNeighbours().size() == 1) {
			countryScore -= 3;
		}
		int neighborBonus = 0;
		int neighbors = 0;
		//defense
		for (int k = 0; k < n; k++) {
			Country cn = country.getIncomingNeighbours().get(k);
			if (cn.getOwner() == player) {
				neighborBonus-=cn.getArmies();
				neighbors++;
			} else if (cn.getOwner() != null) {
				countryScore+=(cn.getArmies()/2 + cn.getArmies()%2);
			}
		}
		int n1 = country.getNeighbours().size();
		//attack
		for (int k = 0; k < n1; k++) {
			Country cn = (Country) country.getNeighbours().get(k);
			if (cn.getOwner() == player) {
				neighborBonus-=cn.getArmies();
				neighbors++;
			} else if (cn.getOwner() == null && cn.getContinent() != country.getContinent()) {
				countryScore--;
			}
		}
		
		neighbors = neighbors/2 + neighbors%2;
		countryScore += neighborBonus/4 + neighborBonus%2;
		
		if (!game.getSetup() || neighbors > 1) {
			countryScore -= Math.pow(neighbors, 2);
			if (!game.getSetup()) {
				countryScore = Math.max(1, countryScore);
			}
		}
		return countryScore;
	}

	/**
	 * General planning method for both attack and placement
	 * TODO should save placement planning state over the whole planning phase (requires refactoring of the aiplayer)
	 *      and should consider all placement moves waited by a utility/probability function and possibly combined
	 *      using an approximation algorithm - currently the logic is greedy and will miss easy lower priority opportunities 
	 * @param attack
	 * @return
	 */
	private String plan(boolean attack) {
		List<Country> attackable = findAttackableTerritories(player, attack);
		if (attack && attackable.isEmpty()) {
			return "endattack";
		}
		GameState gameState = getGameState(player);
		HashMap<Country, AttackTarget> targets = searchAllTargets(attack, attackable);
		return plan(attack, attackable, gameState, targets);
	}

	private HashMap<Country, AttackTarget> searchAllTargets(Boolean attack, List<Country> attackable) {
		HashMap<Country, AttackTarget> targets = new HashMap<Country, AttackTarget>();
		for (int i = 0; i < attackable.size(); i++) {
			Country c = attackable.get(i);
			int attackForce = c.getArmies();
			searchTargets(targets, c, attackForce, i, attackable.size(), game.getSetup()?player.getExtraArmies():(player.getExtraArmies()/2+player.getExtraArmies()%2), attack);
		}
		return targets;
	}

	protected String plan(boolean attack, List<Country> attackable, GameState gameState,
			Map<Country, AttackTarget> targets) {
		boolean shouldEndAttack = false;
		boolean pressAttack = false;
		int extra = player.getExtraArmies();
		Set<Country> allCountriesTaken = new HashSet<Country>();
		List<EliminationTarget> continents = findTargetContinents(gameState, targets, attack, true);
		List<Country> v = getBorder(gameState);
		//special case planning
		if (game.getSetup()) {
			pressAttack = pressAttack(gameState);
			shouldEndAttack = shouldEndAttack(gameState);

			//eliminate
			List<EliminationTarget> toEliminate = findEliminationTargets(targets, gameState, attack, extra);
			if (!toEliminate.isEmpty()) {
				Collections.sort(toEliminate);
				for (int i = 0; i < toEliminate.size(); i++) {
					EliminationTarget et = toEliminate.get(i);
					if (attack && (et.ps.p.getCards().size() < 2 || et.ps.armies > gameState.orderedPlayers.get(0).attackValue/3)) {
						toEliminate.remove(i--); //remove since we don't want to consider for a second pass
					}
					String result = eliminate(attackable, targets, gameState, attack, extra, allCountriesTaken, et, shouldEndAttack, false);
					if (result != null) {
						return result;
					}
				}
				//consider low probability eliminations during attack
				if (attack && !toEliminate.isEmpty()) {
					//redo the target search using low probability
					HashMap<Country, AttackTarget> newTargets = searchAllTargets(null, attackable);
					for (int i = 0; i < toEliminate.size(); i++) {
						EliminationTarget et = toEliminate.get(i);
						//reset the old targets - the new ones contain the new remaining estimates
						for (int j = 0; j < et.attackTargets.size(); j++) {
							AttackTarget newTarget = newTargets.get(et.attackTargets.get(j).targetCountry);
							if (newTarget == null) {
								throw new AssertionError(et.attackTargets.get(j).targetCountry + " no longer reachable");
							}
							et.attackTargets.set(j, newTarget);
						}
						String result = eliminate(attackable, newTargets, gameState, attack, extra, allCountriesTaken, et, shouldEndAttack, true);
						if (result != null) {
							return result;
						}
					}	
				}
			}

			if (!attack && allCountriesTaken.isEmpty() && shouldEndAttack && !pressAttack && !game.getCards().isEmpty()) {
				String result = ensureRiskCard(attackable, gameState, targets, pressAttack,
						continents);
				if (result != null) {
					return result;
				}
			}
			
			//attack the common threat
			if (gameState.commonThreat != null && !gameState.commonThreat.owned.isEmpty()) {
				String result = breakContinent(attackable, targets, gameState, attack, pressAttack, v);
				if (result != null) {
					return result;
				}
			}

			if (!attack) {
				//prefer attack to fortification
				if (!continents.isEmpty() && pressAttack) {
					String result = eliminate(attackable, targets, gameState, attack, extra, allCountriesTaken, continents.get(0), false, false);
					if (result != null) {
						return result;
					}
				}
				String result = fortify(gameState, attackable, true, v);
				if (result != null) {
					return result;
				}
			}
			
			//free a continent, but only plan to do so if in a good position
			//TODO: this does not consider countries already committed
			if (attack || pressAttack) {
				String result = breakContinent(attackable, targets, gameState, attack, pressAttack, v);
				if (result != null) {
					return result;
				}
			}
		} else if (!attack) {
			String result = fortify(gameState, attackable, true, v);
			if (result != null) {
				return result;
			}
		}
		
		//take over a continent
		if (!continents.isEmpty() && (!shouldEndAttack || (!game.isCapturedCountry() && !game.getCards().isEmpty()) || !attack || !isTooWeak(gameState))) {
			int toConsider = continents.size();
			if (attack && isTooWeak(gameState)) {
				toConsider = 1;
			}
			for (int i = 0; i < toConsider; i++) {
				String result = eliminate(attackable, targets, gameState, attack, extra, allCountriesTaken, continents.get(i), shouldEndAttack, false);
				if (result != null) {
					return result;
				}
			}
			if (!attack) {
				AttackTarget min = null;
				for (int i = 0; i < toConsider; i++) {
					EliminationTarget et = continents.get(i);
					for (int k = 0; k < et.attackTargets.size(); k++) {
						AttackTarget at = et.attackTargets.get(k);
						if (min == null || (!allCountriesTaken.contains(at.targetCountry) && at.remaining < min.remaining)) {
							min = at;
						}
					}
				}
				if (min != null) {
					int route = findBestRoute(attackable, gameState, attack, min.targetCountry.getContinent(), min, game.getSetup()?(Player)gameState.targetPlayers.get(0):null, targets); 
					if (route != -1) {
						int toPlace = -min.routeRemaining[route] + 2;
						if (toPlace < 0) {
							toPlace = player.getExtraArmies()/3;
						}
						return getPlaceCommand(attackable.get(route), toPlace);
					}
				}
			}
		}

		if (attack) {
			return lastAttacks(attack, attackable, gameState, targets, shouldEndAttack, v);
		}
		
		String result = fortify(gameState, attackable, false, v);
		if (result != null) {
			return result;
		}
		
		//fail-safe - TODO: should probably just pile onto the max
		return super.getPlaceArmies();
	}

	private String ensureRiskCard(List<Country> attackable, GameState gameState,
			Map<Country, AttackTarget> targets, boolean pressAttack, List<EliminationTarget> continents) {
		List<AttackTarget> attacks = new ArrayList<AttackTarget>(targets.values());
		Collections.sort(attacks);
		AttackTarget target = null;
		boolean found = false;
		int bestRoute = 0;
		for (int i = attacks.size() - 1; i >= 0; i--) {
			AttackTarget at = attacks.get(i);
			if (target != null && at.remaining < target.remaining) {
				break;
			}
			if (found) {
				continue;
			}
			if (at.remaining > 0) {
				target = null;
				break;
			}
			if (continents.size() > 0 && at.targetCountry.getContinent() == continents.get(0).co) {
				bestRoute = findBestRoute(attackable, gameState, pressAttack, null, at, game.getSetup()?(Player) gameState.targetPlayers.get(0):null, targets);
				target = at;
				found = true;
			} else {
				int route = findBestRoute(attackable, gameState, pressAttack, null, at, game.getSetup()?(Player) gameState.targetPlayers.get(0):null, targets);
				if (target == null || gameState.targetPlayers.contains(at.targetCountry.getOwner()) || r.nextBoolean()) {
					bestRoute = route;
					target = at;
				}
			} 
		}
		if (target != null) {
			return getPlaceCommand(attackable.get(bestRoute), -target.remaining + 1);
		}
		return null;
	}

	/**
	 * one last pass looking to get a risk card or reduce forces
	 */
	private String lastAttacks(boolean attack, List<Country> attackable,
		GameState gameState, Map<Country, AttackTarget> targets, boolean shouldEndAttack, List<Country> border) {
		boolean forceReduction = shouldEndAttack && (game.isCapturedCountry() || game.getCards().isEmpty());
		List<AttackTarget> sorted = new ArrayList<AttackTarget>(targets.values());
		Collections.sort(sorted);
		for (int i = sorted.size() - 1; i >= 0; i--) {
			AttackTarget target = sorted.get(i);
			if (target.depth > 1) {
				break; //we don't want to bother considering anything beyond an initla attack 
			}
			int bestRoute = findBestRoute(attackable, gameState, attack, null, target, gameState.targetPlayers.get(0), targets);
			if (bestRoute == -1) {
				continue; //shouldn't happen
			}
			Country attackFrom = attackable.get(bestRoute);
			Country initialAttack = getCountryToAttack(targets, target, bestRoute, attackFrom);
			if (forceReduction) {
				if (attackFrom.getArmies() < 5) {
					//we need at least five so that have some fall back if things don't go well
					continue;
				}
				//peephole break continent
				if ((attackFrom.getCrossContinentNeighbours().size() == 1 || !border.contains(attackFrom))
						&& attackFrom.getCrossContinentNeighbours().contains(initialAttack) 
						&& (gameState.commonThreat == null || gameState.commonThreat.p == initialAttack.getOwner() || gameState.targetPlayers.contains(initialAttack.getOwner())) 
						&& initialAttack.getContinent().getOwner() != null) {
					return getAttack(targets, target, bestRoute, attackFrom);
				}
				if ((gameState.commonThreat == null && game.getCards().isEmpty() && isTooWeak(gameState)) || border.contains(attackFrom)) {
					continue;
				}
				if (gameState.commonThreat != null) {
					if (gameState.commonThreat.p == initialAttack.getOwner()) {
						return getAttack(targets, target, bestRoute, attackFrom);	
					}
				} else if (!isTooWeak(gameState)) {
					for (int j = 0; j < gameState.orderedPlayers.size(); j++) {
						PlayerState ps = gameState.orderedPlayers.get(j);
						if (ps.defenseValue > gameState.me.playerValue && ps.p == initialAttack.getOwner()) {
							return getAttack(targets, target, bestRoute, attackFrom);	
						}
					}
				}
			} else if ((!shouldEndAttack && attackFrom.getArmies() > 4 && (gameState.commonThreat == null || initialAttack.getOwner() == gameState.commonThreat.p)) 
					|| (!game.getCards().isEmpty() && !game.isCapturedCountry() && attackFrom.getArmies() > 2 && initialAttack.getArmies() < attackFrom.getArmies() + 3)) {
				if (gameState.commonThreat != null && gameState.commonThreat.p != initialAttack.getOwner() && !gameState.targetPlayers.contains(initialAttack.getOwner()) && initialAttack.getContinent().getOwner() != null) {
					continue;
				}
				return getAttack(targets, target, bestRoute, attackFrom);
			}
		}
		return "endattack";
	}
	
	/**
	 * Quick check to see if we're significantly weaker than the strongest player
	 */
	protected boolean isTooWeak(GameState gameState) {
		return gameState.me.defenseValue < gameState.orderedPlayers.get(0).attackValue / Math.min(2, gameState.orderedPlayers.size() - 1);
	}

	/**
	 * Stops non-priority attacks if there is too much pressure
	 * @param gameState
	 * @return
	 */
	protected boolean shouldEndAttack(GameState gameState) {
		if (gameState.orderedPlayers.size() < 2) {
			return false;
		}
		int defense = gameState.me.defenseValue;
		double sum = 0;
		for (int i = 0; i < gameState.orderedPlayers.size(); i++) {
			sum += gameState.orderedPlayers.get(i).attackValue;
		}
		if (defense > sum) {
			return false;
		}
		double ratio = defense/sum;
		if (ratio < .5) {
			return true;
		}
		//be slightly probabilistic about this decision
		return r.nextDouble() > (ratio-.5)*2;
	}
	
	/**
	 * If the ai should be more aggressive
	 * @param gameState
	 * @return
	 */
	protected boolean pressAttack(GameState gameState) {
		if (gameState.orderedPlayers.size() < 2) {
			return true;
		}
		int defense = gameState.me.defenseValue;
		double sum = 0;
		for (int i = 0; i < gameState.orderedPlayers.size(); i++) {
			sum += gameState.orderedPlayers.get(i).attackValue;
		}
		return defense > sum;
	}

	/**
	 * Find the continents that we're interested in competing for.
	 * This is based upon how much we control the continent and weighted for its value.
	 */
	private List<EliminationTarget> findTargetContinents(GameState gameState, Map<Country, AttackTarget> targets, boolean attack, boolean filterNoAttacks) {
		Continent[] c = game.getContinents();
		int targetContinents = Math.max(1, c.length - gameState.orderedPlayers.size());
		//step 1 examine continents
		List<Double> vals = new ArrayList<Double>();
		List<EliminationTarget> result = new ArrayList<EliminationTarget>();
		HashSet<Country> seen = new HashSet<Country>();
		for (int i = 0; i < c.length; i++) {
			Continent co = c[i];
			if (gameState.owned[i] != null && (gameState.owned[i] == player || (gameState.commonThreat != null && gameState.commonThreat.p != gameState.owned[i]))) {
				continue;
			}
			List<Country> ct = co.getTerritoriesContained();
			List<AttackTarget> at = new ArrayList<AttackTarget>();
			int territories = 0; 
			int troops = 0;
			int enemyTerritories = 0;
		    int enemyTroops = 0;
		    seen.clear();
		    //look at each country to see who owns it
			for (int j = 0; j < ct.size(); j++) {
				Country country = ct.get(j);
				if (country.getOwner() == player) {
					territories++;
					troops += country.getArmies();
				} else {
					AttackTarget t = targets.get(country);
					if (t != null) {
						at.add(t);
					}
					enemyTerritories++;
					if (gameState.commonThreat == null || gameState.commonThreat.p != country.getOwner()) {
						enemyTroops += country.getArmies();
					} else {
						//this will draw the attack toward continents mostly controlled by the common threat
						enemyTroops += country.getArmies()/2;
					}
				}
				//account for the immediate neighbours
				if (!country.getCrossContinentNeighbours().isEmpty()) {
					for (int k = 0; k < country.getCrossContinentNeighbours().size(); k++) {
						Country ccn = country.getCrossContinentNeighbours().get(k);
						if (seen.add(ccn)) { //prevent counting the same neighbor multiple times
							if (ccn.getOwner() == player) {
								if (country.getOwner() != player) {
									troops += ccn.getArmies()-1;
								}
							} else if (gameState.commonThreat == null) {
								enemyTroops += ccn.getArmies()/2;
							}
						}
					}
				}
			}
			if (at.isEmpty() && filterNoAttacks) {
				continue; //nothing to attack this turn
			}
			int needed = 3*enemyTroops/2 + enemyTerritories + territories - troops;
			if (attack && game.isCapturedCountry() && (needed*.75 > troops)) {
				continue; //should build up, rather than attack
			}
			double ratio = Math.max(1, territories + 2d*troops + player.getExtraArmies()/(game.getSetup()?2:3))/(enemyTerritories + 2*enemyTroops);
			int pow = 2;
			if (!game.getSetup()) {
				pow = 3;
			}
			if (ratio < .5) {
				if (gameState.commonThreat != null) {
					continue;
				}
				//when we have a low ratio, further discourage using a divisor
				ratio/=Math.pow(Math.max(1, enemyTroops-enemyTerritories), pow);
			} else {
				targetContinents++;
			}
			if (gameState.commonThreat == null) {
				//lessen the affect of the value modifier as you control more continents
				ratio *= Math.pow(getContinentValue(co), 1d/(gameState.me.owned.size() + 1));
			}
			Double key = Double.valueOf(-ratio);
			int index = Collections.binarySearch(vals, key);
			if (index < 0) {
				index = -index-1;
			} 
			vals.add(index, key);
			EliminationTarget et = new EliminationTarget();
			et.allOrNone = false;
			et.attackTargets = at;
			et.co = co;
			et.ps = gameState.orderedPlayers.get(0);
			result.add(index, et);
		}
		if (result.size() > targetContinents) {
			result = result.subList(0, targetContinents);
		}
		return result;
	}

	/**
	 * Find the best route (the index in attackable) for the given target selection
	 */
	protected int findBestRoute(List<Country> attackable, GameState gameState,
			boolean attack, Continent targetCo, AttackTarget selection, Player targetPlayer, Map<Country, AttackTarget> targets) {
		int bestRoute = 0;
		Set<Country> bestPath = null;
		for (int i = 1; i < selection.routeRemaining.length; i++) {
			if (selection.routeRemaining[i] == Integer.MIN_VALUE) {
				continue;
			}
			int diff = selection.routeRemaining[bestRoute] - selection.routeRemaining[i];
			Country start = attackable.get(i);
			
			if (selection.routeRemaining[bestRoute] == Integer.MIN_VALUE) {
				bestRoute = i;
				continue;
			}
			
			//short sighted check to see if we're cutting off an attack line
			if (attack && selection.routeRemaining[i] >= 0 && diff != 0 && selection.routeRemaining[bestRoute] >= 0) {
				HashSet<Country> path = getPath(selection, targets, i, start);
				if (bestPath == null) {
					bestPath = getPath(selection, targets, bestRoute, attackable.get(bestRoute));
				}
				HashSet<Country> path1 = new HashSet<Country>(path);
				for (Iterator<Country> iter = path1.iterator(); iter.hasNext();) {
					Country attacked = iter.next();
					if (!bestPath.contains(attacked) || attacked.getArmies() > 4) {
						iter.remove();
					}
				}
				if (diff < 0 && !path1.isEmpty()) {
			    	HashMap<Country, AttackTarget> specificTargets = new HashMap<Country, AttackTarget>();
			    	searchTargets(specificTargets, start, start.getArmies(), 0, 1, player.getExtraArmies(), attack, Collections.EMPTY_SET, path1);
			    	int forwardMin = getMinRemaining(specificTargets, start.getArmies(), false);
			    	if (forwardMin > -diff) {
			    		bestRoute = i;
						bestPath = path;
			    	}
				} else if (diff > 0 && path1.isEmpty() && start.getArmies() >= 3) {
					bestRoute = i;
					bestPath = path;					
				}
				continue;
			}
			
			if (diff == 0 && attack) {
				//range planning during attack is probably too greedy, we try to counter that here
				Country start1 = attackable.get(bestRoute);
				int adjustedCost1 = start1.getArmies() - selection.routeRemaining[bestRoute];
				int adjustedCost2 = start.getArmies() - selection.routeRemaining[i];
				if (adjustedCost1 < adjustedCost2) {
					continue;
				}
				if (adjustedCost2 < adjustedCost1) {
					bestRoute = i;
					continue;
				}
			}
			
			if ((diff < 0 && (!attack || selection.routeRemaining[bestRoute] < 0)) 
					|| (diff == 0
							&& ((selection.attackPath[i] != null && selection.attackPath[i].getOwner() == targetPlayer)
									|| (targetPlayer == null || selection.attackPath[bestRoute].getOwner() != targetPlayer) && start.getContinent() == targetCo))) {
				bestRoute = i;
			}
		}
		if (selection.routeRemaining[bestRoute] == Integer.MIN_VALUE) {
			return -1;
		}
		return bestRoute;
	}

	/**
	 * Get a set of the path from start (exclusive) to the given target
	 */
	private HashSet<Country> getPath(AttackTarget at, Map<Country, AttackTarget> targets, int i,
			Country start) {
		HashSet<Country> path = new HashSet<Country>();
		Country toAttack = at.targetCountry;
		path.add(toAttack);
		while (!start.isNeighbours(toAttack)) {
			at = targets.get(at.attackPath[i]);
			toAttack = at.targetCountry;
			path.add(toAttack);
		}
		return path;
	}

	/**
	 * Return the attack string for the given selection
	 */
	protected String getAttack(Map<Country, AttackTarget> targets, AttackTarget selection, int best,
			Country start) {
		Country toAttack = getCountryToAttack(targets, selection, best, start);
		return "attack " + start.getColor() + " " + toAttack.getColor();
	}

	/**
	 * Gets the initial country to attack given the final selection
	 */
	private Country getCountryToAttack(Map<Country, AttackTarget> targets, AttackTarget selection,
			int best, Country start) {
		Country toAttack = selection.targetCountry;
		while (!start.isNeighbours(toAttack)) {
			selection = targets.get(selection.attackPath[best]);
			toAttack = selection.targetCountry;
		}
		return toAttack;
	}

	/**
	 * Simplistic fortification
	 * TODO: should be based upon pressure/continent value
	 */
	protected String fortify(GameState gs, List<Country> attackable, boolean minimal, List<Country> borders) {
		int min = Math.max(2, getMinPlacement());
		//at least put 2, which increases defensive odds
		for (int i = 0; i < borders.size(); i++) {
			Country c = borders.get(i);
			if (c.getArmies() < min) {
				return getPlaceCommand(c, min - c.getArmies());
			}
		}
		if (minimal && (!game.getSetup() || (game.getCardMode() == RiskGame.CARD_INCREASING_SET && !game.getCards().isEmpty()))) {
			return null;
		}
		for (int i = 0; i < borders.size(); i++) {
			Country c = borders.get(i);
			//this is a hotspot, at least match the immediate troop level
			int diff = additionalTroopsNeeded(c, gs);
			if (diff > 0) {
				return getPlaceCommand(c, Math.min(player.getExtraArmies(), diff));
			} 
			if (!minimal && -diff < c.getArmies() + 2) {
				return getPlaceCommand(c, Math.min(player.getExtraArmies(), c.getArmies() + 2 + diff));
			}
		}
		return null;
	}

	/**
	 * Simplistic (immediate) guess at the additional troops needed.
	 */
	protected int additionalTroopsNeeded(Country c, GameState gs) {
		int needed = 0;
		boolean minimal = !gs.capitals.contains(c); 
		List<Country> v = c.getIncomingNeighbours();
		for (int j = 0; j < v.size(); j++) {
			Country n = v.get(j);
			if (n.getOwner() != player) {
				if (minimal) {
					needed = Math.max(needed, n.getArmies());
				} else {
					needed += (n.getArmies() -1);
				}
			}
		}
		int diff = needed - c.getArmies();
		return diff;
	}

	protected int getMinPlacement() {
		return 1;
	}

	/**
	 * Get the border of my continents, starting with actual borders then the front
	 */
	protected List<Country> getBorder(GameState gs) {
		List<Country> borders = new ArrayList<Country>();
		if (gs.me.owned.isEmpty()) {
			//TODO: could look to build a front
			return borders;
		}
		Set<Country> front = new HashSet<Country>();
		Set<Country> visited = new HashSet<Country>();
		for (Iterator<Continent> i = gs.me.owned.iterator(); i.hasNext();) {
			Continent myCont = i.next();
			List<Country> v = myCont.getBorderCountries();
			for (int j = 0; j < v.size(); j++) {
				Country border = v.get(j);
				if (!ownsNeighbours(border) || isAttackable(border)) {
					borders.add(border);
				} else {
					if (border.getCrossContinentNeighbours().size() == 1) {
						Country country = border.getCrossContinentNeighbours().get(0);
						if (country.getOwner() != player) {
							borders.add(country);
							continue;
						}
					}
					List<Country> n = border.getCrossContinentNeighbours();
					findFront(gs, front, myCont, visited, n);
				}
			}
		}
		borders.addAll(front); //secure borders first, then the front
		return borders;
	}

	private boolean ownsNeighbours(Country c) {
		return ownsNeighbours(player, c);
	}
	
	/**
	 * return true if the country can be attacked
	 */
	private boolean isAttackable(Country c) {
		for (Country country : c.getIncomingNeighbours()) {
			if (country.getOwner() != player) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Search for the front of my continent
	 */
	private void findFront(GameState gs, Set<Country> front, Continent myCont,
			Set<Country> visited, List<Country> n) {
		for (int k = 0; k < n.size(); k++) {
			Country b = n.get(k);
			if (!visited.add(b)) {
				continue;
			}
			if (b.getOwner() == player && b.getContinent() != myCont) {
				if (gs.me.owned.contains(b.getContinent())) {
					continue;
				}
				if (!ownsNeighbours(b) && isAttackable(b)) {
					front.add(b);
				} else {
					findFront(gs, front, myCont, visited, b.getNeighbours());
				}
			}
		}
	}

	/**
	 * Estimates a baseline value for a continent
	 * @param co
	 * @return
	 */
	protected double getContinentValue(Continent co) {
		int players = 0;
		for (int i = 0; i < game.getPlayers().size(); i++) {
			if (!((Player)game.getPlayers().get(i)).getTerritoriesOwned().isEmpty()) {
				players++;
			}
		}
		int freeContinents = game.getContinents().length - players;
		double continentValue = co.getArmyValue() + co.getTerritoriesContained().size()/3;
		int neighbors = 0;
		for (int i = 0; i < co.getBorderCountries().size(); i++) {
			//TODO: update for 1-way
			neighbors += ((Country)co.getBorderCountries().get(i)).getCrossContinentNeighbours().size();
		}
		continentValue /= Math.pow(2*neighbors - co.getBorderCountries().size(), 2);
		if (freeContinents > co.getBorderCountries().size()) {
			continentValue *= co.getBorderCountries().size(); 
		}
		return continentValue;
	}

	/**
	 * Break continents starting with the strongest player
	 */
	private String breakContinent(List<Country> attackable, Map<Country, AttackTarget> targets, GameState gameState, boolean attack, boolean press, List<Country> borders) {
		List<Continent> toBreak = getContinentsToBreak(gameState);
		outer: for (int i = 0; i < toBreak.size(); i++) {
			Continent c = toBreak.get(i);
			Player tp = ((Country)c.getTerritoriesContained().get(0)).getOwner();
			//next level check to see if breaking is a good idea
			if ((!press || !attack) && !gameState.targetPlayers.contains(tp)) {
				if (gameState.commonThreat != null) {
					continue outer;
				}
				for (int j = 0; j < gameState.orderedPlayers.size(); j++) {
					PlayerState ps = gameState.orderedPlayers.get(j);
					if (ps.p == tp) {
						if (ps.attackOrder != 1 && ps.playerValue < gameState.me.playerValue) {
							continue outer;
						}
						break;
					}
				}
			}
			//find the best territory to attack
			List<Country> t = c.getTerritoriesContained();
			int best = Integer.MAX_VALUE;
			AttackTarget selection = null;
			int bestRoute = 0;
			for (int j = 0; j < t.size(); j++) {
				Country target = t.get(j);
				AttackTarget attackTarget = targets.get(target);
				if (attackTarget == null 
						|| attackTarget.remaining == Integer.MIN_VALUE
						|| (attackTarget.remaining + player.getExtraArmies() < 1
								&& (!game.getCards().isEmpty() || !press))) {
					continue;
				}
				int route = findBestRoute(attackable, gameState, attack, null, attackTarget, gameState.orderedPlayers.get(0).p, targets);
				Country attackFrom = attackable.get(route);
				if (gameState.commonThreat == null && gameState.me.owned.isEmpty() && attackTarget.routeRemaining[route] + player.getExtraArmies() < 1) {
					continue;
				}
				int cost = attackFrom.getArmies() - attackTarget.routeRemaining[route];
				if (borders.contains(attackFrom)) {
					cost += 2;
				}
				if (cost < best || (cost == best && r.nextBoolean())) {
					best = cost;
					bestRoute = route;
					selection = attackTarget;
				}
			}
			if (selection != null) {
				Country attackFrom = attackable.get(bestRoute);
				if (best > (2*c.getArmyValue() + 2*selection.targetCountry.getArmies())) {
					//ensure that breaking doesn't do too much collateral damage
					int value = 2*c.getArmyValue();
					Set<Country> path = getPath(selection, targets, bestRoute, attackFrom);
					for (Iterator<Country> j = path.iterator(); j.hasNext();) {
						Country attacked = j.next();
						value++;
						if (attacked.getOwner() == selection.targetCountry.getOwner() || gameState.targetPlayers.contains(attacked.getOwner())) {
							value += 3*attacked.getArmies()/2 + attacked.getArmies()%2;
						}
					}
					if (value < best && (!attack || r.nextInt(best - value) != 0)) {
						continue outer;
					}
				}
				String result = getMove(targets, attack, selection, bestRoute, attackFrom);
				if (result == null) {
					continue outer;
				}
				return result;
			}
		}
		return null;
	}
	
	/**
	 * Get a list of continents to break in priority order
	 */
	protected List<Continent> getContinentsToBreak(GameState gs) {
		List<Continent> result = new ArrayList<Continent>();
		List<Double> vals = new ArrayList<Double>();
		for (int i = 0; i < gs.owned.length; i++) {
			if (gs.owned[i] != null && gs.owned[i] != player) {
				Continent co = game.getContinents()[i];
				Double val = Double.valueOf(-getContinentValue(co) * game.getContinents()[i].getArmyValue());
				int index = Collections.binarySearch(vals, val);
				if (index < 0) {
					index = -index-1;
				}
				vals.add(index, val);
				result.add(index, co);
			}
		}
		return result;
	}
	
	/**
	 * Determine if elimination is possible.  Rather than performing a more
	 * advanced combinatorial search, this planning takes simple heuristic passes
	 */
	private String eliminate(List<Country> attackable, Map<Country, AttackTarget> targets, GameState gameState, boolean attack, int remaining, Set<Country> allCountriesTaken, EliminationTarget et, boolean shouldEndAttack, boolean lowProbability) {
		AttackTarget selection = null;
		int bestRoute = 0;
		if (!et.allOrNone && !et.target && shouldEndAttack && attack) {
			//just be greedy, take the best (least costly) attack first
			for (int i = 0; i < et.attackTargets.size(); i++) {
				AttackTarget at = et.attackTargets.get(i);
				if (at.depth != 1 || allCountriesTaken.contains(at.targetCountry)) {
					continue;
				}
				int route = findBestRoute(attackable, gameState, attack, null, at, et.ps.p, targets);
				Country attackFrom = attackable.get(route);
				if (((at.routeRemaining[route] > 0 && (selection == null || at.routeRemaining[route] < selection.routeRemaining[bestRoute] || selection.routeRemaining[bestRoute] < 1)) 
						|| (at.remaining > 1 && attackFrom.getArmies() > 3 && (selection != null && at.remaining < selection.remaining)))
						&& isGoodIdea(gameState, targets, route, at, attackFrom, et, attack)) {
					selection = at;
					bestRoute = route;
				}
			}
			return getMove(targets, attack, selection, bestRoute, attackable.get(bestRoute));
		}
		//otherwise we use more logic to plan a more complete attack
		//we start with the targets from easiest to hardest and build up the attack paths from there
		Set<Country> countriesTaken = new HashSet<Country>(allCountriesTaken);
		Set<Country> placements = new HashSet<Country>();
		int bestCost = Integer.MAX_VALUE;
		Collections.sort(et.attackTargets, Collections.reverseOrder());
		HashSet<Country> toTake = new HashSet<Country>();
		for (int i = 0; i < et.attackTargets.size(); i++) {
			AttackTarget at = et.attackTargets.get(i);
			if (!allCountriesTaken.contains(at.targetCountry)) {
				toTake.add(at.targetCountry);
			}
		}
		outer: for (int i = 0; i < et.attackTargets.size() && !toTake.isEmpty(); i++) {
			AttackTarget attackTarget = et.attackTargets.get(i);
			if (!toTake.contains(attackTarget.targetCountry)) {
				continue;
			}
			Country attackFrom = null;
			int route = 0;
			boolean clone = true;
			Set<Country> path = null;
			int pathRemaining;
			while (true) {
				route = findBestRoute(attackable, gameState, attack, null, attackTarget, et.ps.p, targets);
				if (route == -1) {
					if (!et.allOrNone) {
						continue outer;
					}
					return null;
				}
				attackFrom = attackable.get(route);
				if (!placements.contains(attackFrom)) {
					pathRemaining = attackTarget.routeRemaining[route];
					if ((pathRemaining + remaining >= 1 //valid single path
							|| (attackTarget.remaining + remaining >= 2 && attackFrom.getArmies() + remaining >= 4)) //valid combination
							&& (et.allOrNone || isGoodIdea(gameState, targets, route, attackTarget, attackFrom, et, attack))) {
						//TODO this is a choice point if there is more than 1 valid path
						path = getPath(attackTarget, targets, route, attackFrom);
						//check to see if this path is good
						if (Collections.disjoint(path, countriesTaken)) {
							//check to see if we can append this path with a nearest neighbor path
							if (pathRemaining + remaining >= 3) {
								HashSet<Country> exclusions = new HashSet<Country>(countriesTaken);
								exclusions.addAll(path);
								Map<Country, AttackTarget> newTargets = new HashMap<Country, AttackTarget>();
								searchTargets(newTargets, attackTarget.targetCountry, pathRemaining, 0, 1, remaining, lowProbability?null:attack, toTake, exclusions);
								//find the best fit new path if one exists
								AttackTarget newTarget = null;
								for (Iterator<AttackTarget> j = newTargets.values().iterator(); j.hasNext();) {
									AttackTarget next = j.next();
									if (toTake.contains(next.targetCountry) 
											&& next.routeRemaining[0] < pathRemaining 
											&& next.routeRemaining[0] + remaining >= 1) { 
										pathRemaining = next.routeRemaining[0];
										newTarget = next;
									}
								}
								if (newTarget != null) {
									path.addAll(getPath(newTarget, newTargets, 0, attackTarget.targetCountry));
									attackTarget.routeRemaining[route] = pathRemaining;
								}
							}
							break; //a good path, continue with planning
						}
					}
				}
				if (clone) {
					//clone the attack target so that the find best route logic can have a path excluded
					attackTarget = attackTarget.clone();
					attackTarget.routeRemaining = ArraysCopyOf(attackTarget.routeRemaining, attackTarget.routeRemaining.length);
					clone = false;
				}
				attackTarget.routeRemaining[route] = Integer.MIN_VALUE;
			}
			//process the path found and update the countries take and what to take
			for (Iterator<Country> j = path.iterator(); j.hasNext();) {
				Country c = j.next();
				countriesTaken.add(c);
				toTake.remove(c);
			}
			if (pathRemaining < 1) {
				remaining += pathRemaining -1;
			}
			int cost = attackFrom.getArmies() - pathRemaining;
			if (selection == null || (attack && cost < bestCost && cost > 0)) {
				selection = attackTarget;
				bestCost = cost;
				bestRoute = route;
			}
			placements.add(attackFrom);
		}
		Country attackFrom = attackable.get(bestRoute);
		String result = getMove(targets, attack, selection, bestRoute, attackFrom);
		if (result != null) {
			allCountriesTaken.addAll(countriesTaken);
		}
		return result;
	}

    /**
     * @see Arrays#copyOf(int[], int) 
     */
    public static int[] ArraysCopyOf(int[] original, int newLength) {
        int[] copy = new int[newLength];
        System.arraycopy(original, 0, copy, 0,
                         Math.min(original.length, newLength));
        return copy;
    }

	/**
	 * ensure that we're not doing something stupid like breaking using too many troops for too little reward or pushing a player to elimination
	 */
	private boolean isGoodIdea(GameState gameState, Map<Country, AttackTarget> targets, int route, AttackTarget attackTarget, Country attackFrom, EliminationTarget et, boolean attack) {
		Country c = getCountryToAttack(targets, attackTarget, route, attackFrom);
		if (gameState.orderedPlayers.size() > 1 && (et.ps == null || c.getOwner() != et.ps.p)) {
			if (gameState.commonThreat != null && c.getOwner() != gameState.commonThreat.p && c.getContinent().getOwner() != null) {
				return false;
			}
			if (attack && game.isCapturedCountry() && (c.getOwner().getCards().size() > 1 || (c.getOwner().getCards().size() == 1 && game.getCards().isEmpty()))) {
				for (int i = gameState.orderedPlayers.size() - 1; i >= 0; i--) {
					PlayerState ps = gameState.orderedPlayers.get(i);
					if (ps.playerValue >= gameState.me.playerValue) {
						break;
					}
					if (ps.p == c.getOwner()) {
						PlayerState top = gameState.orderedPlayers.get(0);
						if (ps.defenseValue - 5*c.getArmies()/4 - c.getArmies()%4 - 1 < 2*(top.attackValue - top.armies/3)/3) {
							return false;
						}
						break;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Gets the move (placement or attack) or returns null if it's not a good attack
	 */
	private String getMove(Map<Country, AttackTarget> targets, boolean attack, AttackTarget selection,
			int route, Country attackFrom) {
		if (selection == null) {
			return null;
		}
		if (attack) {
			if (attackFrom.getArmies() < 5 && selection.remaining < 1) {
				Country toAttack = getCountryToAttack(targets, selection, route, attackFrom);
				if (toAttack.getArmies() >= attackFrom.getArmies()) {
					return null;
				}
			}
			return getAttack(targets, selection, route, attackFrom);
		}
		if (selection.remaining < 1 || selection.routeRemaining[route] < 2) {
			return getPlaceCommand(attackFrom, -selection.routeRemaining[route] + 2);
		}
		return null;
	}

	/**
	 * find the possible elimination targets in priority order
	 * will filter out attacks that seem too costly or if the target has no cards
	 */
	private List<EliminationTarget> findEliminationTargets(Map<Country, AttackTarget> targets, GameState gameState,
			boolean attack, int remaining) {
		List<EliminationTarget> toEliminate = new ArrayList<EliminationTarget>();
		players: for (int i = 0; i < gameState.orderedPlayers.size(); i++) {
			PlayerState ps = gameState.orderedPlayers.get(i);
			Player player2 = ps.p;
			
			if ((player2.getCards().isEmpty() && player2.getTerritoriesOwned().size() > 1) || ps.defenseValue > gameState.me.attackValue + player.getExtraArmies()) {
				continue;
			}
			
			boolean isTarget = gameState.targetPlayers.size() > 1 && gameState.targetPlayers.get(0) == player2;
			int divisor = 1;
			int cardCount = player2.getCards().size();
			if ((!attack || player2.getTerritoriesOwned().size() > 1) && !game.getCards().isEmpty() && cardCount < 3 && (game.getCardMode()==RiskGame.CARD_ITALIANLIKE_SET||(cardCount+player.getCards().size()<RiskGame.MAX_CARDS))) {
				if (game.getCardMode() != RiskGame.CARD_INCREASING_SET) {
					divisor+=1;
				}
				if (cardCount < 2) {
					divisor+=1;
				}
			}
			
			if (!isTarget && ps.defenseValue > gameState.me.armies/divisor + player.getExtraArmies()) {
				continue;
			}
			
			List<Country> targetCountries = player2.getTerritoriesOwned();
			EliminationTarget et = new EliminationTarget();
			et.ps = ps;
			//check for sufficient troops on critical path
			for (int j = 0; j < targetCountries.size(); j++) {
				Country target = targetCountries.get(j);
				AttackTarget attackTarget = targets.get(target);
				if (attackTarget == null 
						|| attackTarget.remaining == Integer.MIN_VALUE 
						|| (!attack && -attackTarget.remaining > remaining)) {
					continue players;
				}
				et.attackTargets.add(attackTarget);
			}
			et.target = isTarget;
			et.allOrNone = gameState.orderedPlayers.size() != 1;
			toEliminate.add(et);
		}
		return toEliminate;
	}
	
	private void searchTargets(Map<Country, AttackTarget> targets, Country startCountry, int startArmies, final int start, int totalStartingPoints, int extra, Boolean attack) {
		searchTargets(targets, startCountry, startArmies, start, totalStartingPoints, extra, attack, Collections.EMPTY_SET, Collections.EMPTY_SET);
	}
	
	/**
	 * search using Dijkstra's algorithm
	 * If the way points are set, then we're basically doing a traveling salesman nearest neighbor heuristic.  
	 * the attack parameter controls three cost calculations
	 *  - true neutral
	 *  - false slightly pessimistic
	 *  - null optimistic
	 */
	private void searchTargets(Map<Country, AttackTarget> targets, Country startCountry, int startArmies, final int start, int totalStartingPoints, int extra, Boolean attack, Set<Country> wayPoints, Set<Country> exclusions) {
		PriorityQueue<AttackTarget> remaining = new PriorityQueue<AttackTarget>(11, new Comparator<AttackTarget>() {
			@Override
			public int compare(AttackTarget o1, AttackTarget o2) {
				//TODO: consider the number of paths out of the country
				return o2.routeRemaining[start] - o1.routeRemaining[start];
			}
		});
		AttackTarget at = new AttackTarget(totalStartingPoints, startCountry);
		at.routeRemaining[start] = startArmies;
		remaining.add(at);
		outer: while (!remaining.isEmpty()) {
			AttackTarget current = remaining.poll();
			
			int attackForce = current.routeRemaining[start];
			attackForce -= getMinPlacement();
			if (attack != null) {
				attackForce -= Math.min(current.targetCountry.getArmies()/(attack?3:2), current.depth);
			}

			if (attackForce + extra < 1) {
				break;
			}
			
			List<Country> v = current.targetCountry.getNeighbours();
			
			for (int i = 0; i < v.size(); i++) {
				Country c = v.get(i);
				if (c.getOwner() == player) {
					continue;
				}
				AttackTarget cumulativeForces = targets.get(c);
				if (cumulativeForces == null) {
					if (exclusions.contains(c)) {
						continue;
					}
					cumulativeForces = new AttackTarget(totalStartingPoints, c);
					targets.put(c, cumulativeForces);
				} else if (cumulativeForces.routeRemaining[start] != Integer.MIN_VALUE) {
					continue;
				}
				cumulativeForces.depth = current.depth+1;
				int available = attackForce;
				int toAttack = c.getArmies();
				if (attack == null || attack) {
					while (toAttack >= 10 || (available >= 10 && toAttack >= 5)) {
						toAttack -= 4;
						available -= 3;
					}
				}
				while (toAttack >= 5 || (available >= 5 && toAttack >= 2)) {
					toAttack -= 2;
					available -= 2;
				}
				if (attack == null) {
					available -= toAttack;
				} else if (attack && available == toAttack + 1) {
					available = 1; //special case to allow 4 on 2 and 3 on 1 attacks
				} else {
					available = available - 3*toAttack/2 - toAttack%2;
				}
				cumulativeForces.attackPath[start] = current.targetCountry;
				cumulativeForces.routeRemaining[start] = available;
				if (cumulativeForces.remaining>=0 && available>=0) {
					cumulativeForces.remaining = cumulativeForces.remaining += available;
				} else {
					cumulativeForces.remaining = Math.max(cumulativeForces.remaining, available);
				}
				//if this is the nearest waypoint, continue the search from this point
				//TODO: make a better selection among neighbors
				if (wayPoints.contains(c)) {
					Set<Country> path = getPath(cumulativeForces, targets, start, startCountry);
					exclusions.addAll(path);
					startCountry = c;
					targets.keySet().retainAll(exclusions);
					remaining.clear();
					remaining.add(cumulativeForces);
					continue outer;
				}
				remaining.add(cumulativeForces);
			}
		}
	}
	
    public String getBattleWon() {
    	GameState gameState = getGameState(player);
    	return getBattleWon(gameState);
    }

    /**
     * Compute the battle won move.  We are just doing a quick reasoning here.
     * Ideally we would consider the full state of move all vs. move min vs. some mix.
     */
	protected String getBattleWon(GameState gameState) {
		if (ownsNeighbours(game.getDefender())) {
    		return "move " + game.getMustMove();
    	}
    	if (ownsNeighbours(game.getAttacker())) {
    		return "move " + Math.max(game.getMustMove(), game.getAttacker().getArmies() - getMinPlacement());
    	}
    	int forwardMin = 0;
    	if (game.getAttacker().getArmies() - 1 > game.getMustMove()) {
	    	Country defender = game.getDefender();
	    	HashMap<Country, AttackTarget> targets = new HashMap<Country, AttackTarget>();
	    	searchTargets(targets, defender, game.getAttacker().getArmies() - 1, 0, 1, player.getExtraArmies(), true);
	    	forwardMin = getMinRemaining(targets,  game.getAttacker().getArmies() - 1, getBorder(gameState).contains(game.getAttacker()));
	    	if (forwardMin == Integer.MAX_VALUE) {
	    		return "move " + game.getMustMove(); 
	    	}
    	}
    	return "move " + Math.max(game.getAttacker().getArmies() - Math.max(getMinPlacement(), forwardMin), game.getMustMove());
	}

	/**
	 * Get an estimate of the remaining troops after taking all possible targets
	 */
	private int getMinRemaining(HashMap<Country, AttackTarget> targets, int forwardMin, boolean isBorder) {
		for (Iterator<AttackTarget> i = targets.values().iterator(); i.hasNext();) {
			AttackTarget attackTarget = i.next();
			if (attackTarget.remaining < 0 && !isBorder) {
				return 0;
			}
			//estimate a cost for the territory
			int result = 2;
			if (attackTarget.targetCountry.getArmies() > 4) {
				result += attackTarget.targetCountry.getArmies() - 4;
			}
			result += 3*Math.min(4, attackTarget.targetCountry.getArmies())/2 + attackTarget.targetCountry.getArmies()%2;
			forwardMin -= result;
			if (forwardMin < 0) {
				break;
			}
		}
		return Math.max(isBorder?2:0, forwardMin);
	}
	

	/**
	 * Takes several passes over applicable territories to determine the tactical move.
	 * 1. Find all countries with more than the min placement and do the best border fortification possible.
	 *  1.a. If there is a common threat see if we can move off of a continent we don't want
	 * 2. Move the most troops to the battle from a non-front country.
	 * 3. just move from the interior - however this doesn't yet make a smart choice.
	 */
	public String getTacMove() {
		List<Country> t = player.getTerritoriesOwned();
		Country sender = null;
		Country receiver = null;
		int lowestScore = Integer.MAX_VALUE;
		GameState gs = getGameState(player);
		//fortify the border
		List<Country> v = getBorder(gs);
		List<Country> filtered = new ArrayList<Country>();

		List<Continent> targetContinents = null;
		
		for (int i = 0; i < t.size(); i++) {
			Country c = t.get(i);
			if (c.getArmies() <= getMinPlacement() || gs.capitals.contains(c)) {
				continue;
			}
			//cooperation check to see if we should leave this continent
			if (c.getArmies() > 2 && gs.commonThreat != null && c.getCrossContinentNeighbours().size() > 0 && !ownsNeighbours(c)) {
				coop: for (int j = 0; j < c.getNeighbours().size(); j++) {
					Country n = (Country)c.getNeighbours().get(j);
					if (n.getOwner() == player && n.getContinent() != c.getContinent()) {
						//we have another continent to go to, ensure that the original continent is not desirable
						if (targetContinents == null) {
							List<EliminationTarget> co = findTargetContinents(gs, Collections.EMPTY_MAP, false, false);
							targetContinents = new ArrayList<Continent>();
							for (int k = 0; k < co.size(); k++) {
								EliminationTarget et = co.get(k);
								targetContinents.add(et.co);
							}
						}
						int index = targetContinents.indexOf(c.getContinent());
						if (index == -1 && c.getContinent().getOwner() == player) {
							break coop;
						}
						int indexOther = targetContinents.indexOf(n.getContinent());
						if ((indexOther > -1 && (index == -1 || index > indexOther)) || ((index == -1 || index > 0) && n.getContinent().getOwner() == player)) {
							int toSend = c.getArmies() - getMinPlacement();
							return getMoveCommand(c, n, toSend);
						}
					}
				}
			}
			if (v.contains(c) && additionalTroopsNeeded(c, gs)/2 + getMinPlacement() >= 0) {
				continue;	
			}
			filtered.add(c);
			int score = scoreCountry(c);
			for (int j = 0; j < c.getNeighbours().size(); j++) {
				Country n = (Country)c.getNeighbours().get(j);
				if (n.getOwner() != player || !v.contains(n) || additionalTroopsNeeded(n, gs) < -1) {
					continue;
				}
				int total = -score + scoreCountry(n);
				if (total < lowestScore) {
					sender = c;
					receiver = n;
					lowestScore = total;
				}
			}
		}
		if (receiver != null) {
			int toSend = sender.getArmies() - getMinPlacement();
			if (v.contains(sender)) {
				toSend = -additionalTroopsNeeded(sender, gs)/2 - getMinPlacement();
			}
			return getMoveCommand(sender, receiver, toSend);
		} 
		//move to the battle
		Country max = null;
		for (int i = filtered.size() - 1; i >= 0; i--) {
			Country c = filtered.get(i);
			if (!ownsNeighbours(c)) {
				filtered.remove(i);
				continue;
			} 
			if (max == null || c.getArmies() > max.getArmies()) {
				max = c;
			}
			int score = scoreCountry(c);
			for (int j = 0; j < c.getNeighbours().size(); j++) {
				Country n = (Country)c.getNeighbours().get(j);
				if (n.getOwner() != player || ownsNeighbours(n)) {
					continue;
				}
				int total = -score + scoreCountry(n);
				if (total < lowestScore) {
					sender = c;
					receiver = n;
					lowestScore = total;
				}
			}
		}	
		if (receiver != null) {
			int toSend = sender.getArmies() - getMinPlacement();
			if (v.contains(sender)) {
				toSend = -additionalTroopsNeeded(sender, gs)/2 - getMinPlacement();
			}
			return getMoveCommand(sender, receiver, toSend);
		} 
		//move from the interior (not very smart)
		if (max != null && max.getArmies() > getMinPlacement() + 1) {
			int least = Integer.MAX_VALUE;
			for (int j = 0; j < max.getNeighbours().size(); j++) {
				Country n = (Country)max.getNeighbours().get(j);
				if (max.getOwner() != player) {
					continue;
				}
				if (n.getArmies() < least) {
					receiver = n;
					least = n.getArmies();
				}
			}
			if (receiver != null) { 
				return getMoveCommand(max, receiver,  (max.getArmies() - getMinPlacement() - 1));
			}
		}
		return "nomove";
	}

	private String getMoveCommand(Country sender, Country receiver, int toSend) {
		return "movearmies " + sender.getColor() + " "
				+ receiver.getColor() + " " + toSend;
	}

	public String getAttack() {
		return plan(true);
	}

	/**
	 * Will roll the maximum, but checks to see if the attack is still the
	 * best plan every 3rd roll
	 */
    public String getRoll() {
		int n=game.getAttacker().getArmies() - 1;
		int m=game.getDefender().getArmies();
		
		if (n < 3 && game.getBattleRounds() > 0 && (n < m || (n == m && game.getDefender().getOwner().getTerritoriesOwned().size() != 1))) {
			return "retreat";
		}

		//spot check the plan
    	if (game.getBattleRounds()%3 == 2) {
    		String result = plan(true);
    		//TODO: rewrite to not use string parsing
    		if (result.equals("endattack")) {
    			return "retreat";
    		}
    		StringTokenizer st = new StringTokenizer(result);
    		st.nextToken();
    		if (game.getAttacker().getColor() != Integer.parseInt(st.nextToken())
    				|| game.getDefender().getColor() != Integer.parseInt(st.nextToken())) {
    			return "retreat";
    		}
    	}
		return "roll " + Math.min(3, n);
    }

    /**
     * Get a quick overview of the game state - capitals, player ordering, if there is a common threat, etc.
     * @param p
     * @return
     */
    public GameState getGameState(Player p) {
    	List<Player> players = game.getPlayers();
    	GameState g = new GameState();
    	Continent[] c = game.getContinents();
    	if (player.getCapital() == null) {
    		g.capitals = Collections.EMPTY_SET;
    	} else {
    		g.capitals = new HashSet<Country>();
    	}
    	g.owned = new Player[c.length];
    	for (int i = 0; i < c.length; i++) {
			g.owned[i] = c[i].getOwner();
		}
    	int index = -1;
    	int playerCount = 1;
    	//find the set of capitals
    	for (int i = 0; i < players.size(); i++) {
    		Player player2 = players.get(i);
    		if (player2.getCapital() != null) {
    			g.capitals.add(player2.getCapital());
    		}
    		if (player2.getTerritoriesOwned().isEmpty()) {
    			continue;
    		}
    		if (player2 == p) {
    			index = i;
    		} else {
    			playerCount++;
    		}
    	}
    	g.orderedPlayers = new ArrayList<PlayerState>(playerCount);
    	int attackOrder = 0;
    	for (int i = 0; i < players.size(); i++) {
    		Player player2 = players.get((index + i)%players.size());
    		if (player2.getTerritoriesOwned().isEmpty()) {
    			continue;
    		}
    		//estimate the trade-in
    		int cards = player2.getCards().size() + 1;
    		int tradeIn = game.getCardMode() == RiskGame.CARD_FIXED_SET?10:game.getNewCardState();
			int cardEstimate = cards < 4?0:(int)((cards/3 - 1 + cards%3*.25)*tradeIn);
			PlayerState ps = new PlayerState();
			List<Country> t = player2.getTerritoriesOwned();
			int noArmies = 0;
			int attackable = 0;
			boolean strategic = isStrategic(player2);
			//determine what is available to attack with, discounting if land locked
			for (int j = 0; j < t.size(); j++) {
				Country country = t.get(j);
				noArmies += country.getArmies();
				int available = country.getArmies() - 1;
				if (ownsNeighbours(player2, country)) {
					available = country.getArmies()/2;
				}
				//quick multipliers to prevent turtling/concentration
				if (available > 4) {
					if (available > 8 && strategic) {
						if (available > 13) {
							available *= 1.3;
						}
						available += 2;
					}
					available += 1;
				}
				attackable += available;
			}
			int reenforcements = Math.max(3, player2.getNoTerritoriesOwned()/3) + cardEstimate;
			int attack = attackable + reenforcements;
			HashSet<Continent> owned = new HashSet<Continent>();
			//update the attack and player value for the continents owned
			for (int j = 0; j < g.owned.length; j++) {
				if (g.owned[j] == player2) {
					attack += c[j].getArmyValue();
					if (strategic) {
						ps.playerValue += 2*c[j].getArmyValue();						
					} else {
						ps.playerValue += 1.5 * c[j].getArmyValue() + 1;
					}
					owned.add(c[j]);
				}
			}
			ps.strategic = strategic;
			ps.armies = noArmies;
			ps.owned = owned;
			ps.attackValue = attack;
			ps.attackOrder = attackOrder;
			//use a small multiplier for the defensive value
			ps.defenseValue = 5*noArmies/4 + noArmies%4 + player2.getNoTerritoriesOwned();
			ps.p = player2;
			if (i == 0) {
				g.me = ps;
			} else {
				g.orderedPlayers.add(ps);
			}
			ps.playerValue += ps.attackValue + ps.defenseValue*2;
			attackOrder++;
    	}
    	//put the players in order of strongest to weakest
    	Collections.sort(g.orderedPlayers, Collections.reverseOrder());
    	//check to see if there is a common threat
    	//the logic will allow the ai to team up against the strongest player
    	//TODO: similar logic could be expanded to understand alliances/treaties
    	if (game.getSetup() && !g.orderedPlayers.isEmpty()) {
    		//base top player multiplier - will be lower if mission/capital or if there are no risk cards available
    		double multiplier = game.getCards().isEmpty()?(game.isRecycleCards()?1.2:1.1):(player.getMission()!=null||player.getCapital()!=null)?1.2:1.3;
    		PlayerState topPlayer = g.orderedPlayers.get(0);
    		if (topPlayer.strategic) {
    			multiplier *= .9; 
    		}
			g.targetPlayers = Arrays.asList(topPlayer.p);
			//look to see if you and the next highest player are at the multiplier below the highest
    		if (g.orderedPlayers.size() > 1 
    				&& topPlayer.playerValue > multiplier * g.me.playerValue 
    				&& topPlayer.playerValue > multiplier * g.orderedPlayers.get(1).playerValue) {
    			g.commonThreat = topPlayer;
				PlayerState ps = g.orderedPlayers.get(1);
				if (ps.playerValue > g.me.playerValue && ps.strategic && ps.playerValue + g.me.playerValue > topPlayer.playerValue) {
					g.commonThreat = null; //don't team up with a hard player if the alliance is stronger than the top player
				}
    		}
    	} else {
    		g.targetPlayers = Collections.EMPTY_LIST;
    	}
    	return g;
    }
    
    /**
     * Provides a quick measure of how the player has performed
     * over the last several turns
     */
	private boolean isStrategic(Player player2) {
		if (player2 == this.player) {
			return false;
		}
		List<Statistic> stats = player2.getStatistics();
		//look over the last 4 turns
		int end = Math.max(0, stats.size() - 4);
		int reenforcements = 0;
		int kills = 0;
		int casualities = 0;
		for (int i = stats.size() - 1; i >= end; i--) {
			Statistic s = stats.get(i);
			reenforcements += s.statistics[Statistic.reinforcements];
			kills += s.statistics[Statistic.kills];
			casualities += s.statistics[Statistic.casualties];
		}
		return reenforcements + kills/((player2.getCards().size() > 2)?1:2) > 2*casualities;
	}

	/**
     * Delay trading in cards when sensible
     * TODO: this should be more strategic, such as looking ahead for elimination
     */
    public String getTrade() {
    	if (!game.getTradeCap()) {
    		if (game.getCardMode() != RiskGame.CARD_ITALIANLIKE_SET && player.getCards().size() >= RiskGame.MAX_CARDS) {
    			return super.getTrade();
    		}
    		GameState gs = getGameState(player);
    		if (game.getCards().isEmpty() && game.isRecycleCards() && gs.orderedPlayers.size() > 1 && !shouldEndAttack(gs) && !pressAttack(gs)) {
    			return "endtrade";	        			
        	}
	    	switch (game.getCardMode()) {
	    	case RiskGame.CARD_INCREASING_SET:
        		if (gs.orderedPlayers.size() > 1 && !shouldEndAttack(gs)) {
        			return "endtrade";	        			
        		}
	        	break;
	    	case RiskGame.CARD_ITALIANLIKE_SET: 
        		if (gs.orderedPlayers.size() > 1 && !shouldEndAttack(gs) && !pressAttack(gs)) {
        			return "endtrade";	        			
        		}
	    		break;
	    	}
    	}
    	return super.getTrade();
    }
    
    @Override
    public int tradeCombinationsToScan() {
    	return 100000;
    }
    
}
