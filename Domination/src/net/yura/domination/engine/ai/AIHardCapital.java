//  Group D

package net.yura.domination.engine.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.yura.domination.engine.core.Continent;
import net.yura.domination.engine.core.Country;
import net.yura.domination.engine.core.Player;

/**
 * <p> Class for AIHardPlayer </p>
 * @author SE Group D
 */
public class AIHardCapital extends AIHardDomination {
	
	/**
	 * Adds the best defended capital to the border when there is a threat
	 */
	protected List<Country> getBorder(GameState gs) {
		List<Country> border = super.getBorder(gs);
		if (gs.commonThreat == null) {
			return border;
		}
		int attack = (int)gs.commonThreat.attackValue/(gs.orderedPlayers.size() + 1);
		int minNeeded=Integer.MAX_VALUE;
		Country priority = null;
		for (Iterator<Country> i = gs.capitals.iterator(); i.hasNext();) {
			Country c = i.next();
			if (c.getOwner() != player) {
				continue;
			}
			int additional = Math.max(attack - c.getArmies(), additionalTroopsNeeded(c, gs));
			if (additional <= 0) {
				return border;
			}
			if (additional < minNeeded) {
				minNeeded = additional;
				priority = c;
			}
		}
		if (priority != null) {
			border.add(0, priority);
		}
		return border;
	}
	
	/**
	 * Overrides the planning behavior to consider taking capital logic
	 */
	protected String plan(boolean attack, List<Country> attackable,
			GameState gameState, Map<Country, AttackTarget> targets) {
		if (game.getSetup()) {
			Map<Player, Integer> owned = new HashMap<Player, Integer>();
			for (Iterator<Country> i = gameState.capitals.iterator(); i.hasNext();) {
				Country c = i.next();
				Integer count = owned.get(c.getOwner());
				if (count == null) {
					count = Integer.valueOf(1);
				} else {
					count = Integer.valueOf(count.intValue() + 1);
				}
				owned.put(c.getOwner(), count);
			}
			double num = gameState.capitals.size();
			Integer myowned = owned.get(player);
			//offensive planning
			if (myowned != null && myowned.intValue()/num >= .5) {
				String result = planCapitalMove(attack, attackable, gameState, targets, null, true);
				if (result != null) {
					return result;
				}
			}
			//defensive planning
			for (Iterator<Map.Entry<Player, Integer>> i = owned.entrySet().iterator(); i.hasNext();) {
				Map.Entry<Player, Integer> e = i.next();
				Integer numOwned = e.getValue();
				Player other = e.getKey();
				if (other == player || numOwned/num < .5) {
					continue;
				}
				//see what the danger level is
				int primaryDefense = 0;
				for (Iterator<Country> j = gameState.capitals.iterator(); j.hasNext();) {
					Country c = j.next();
					if (c.getOwner() != other) {
						primaryDefense+=c.getArmies();
					}
				}
				for (int j = 0; j < gameState.orderedPlayers.size(); j++) {
					PlayerState ps = gameState.orderedPlayers.get(j);
					if (ps.p == other) {
						if (gameState.commonThreat == null && gameState.orderedPlayers.size() > 1 && ps.attackValue > (ps.strategic?3:4)*primaryDefense) {
							gameState.commonThreat = ps;
							if (!gameState.targetPlayers.contains(ps.p)) {
								gameState.targetPlayers = new ArrayList<Player>(gameState.targetPlayers);
								gameState.targetPlayers.add(ps.p);
							}
						}
						if (ps.attackValue > 2*primaryDefense) {
							//can we take one - TODO: coordinate with break continent
							String result = planCapitalMove(attack, attackable, gameState, targets, e.getKey(), false);
							if (result != null) {
								return result;
							}
						}
						break;
					}
				}
				break;
			}
		} else if (r.nextInt(game.getPlayers().size()) == 0) {
			//defend the capital more - ensures that when playing with a small number of players
			//the initial capital should be well defended
			Country c = findCapital();
			return getPlaceCommand(c, 1);
		}
		return super.plan(attack, attackable, gameState, targets);
	}
	
	/**
	 * Plans to take one (owned by the target player) or all of the remaining capitals.
	 */
	private String planCapitalMove(boolean attack, List<Country> attackable,
			GameState gameState, Map<Country, AttackTarget> targets, Player target, boolean allOrNone) {
		int remaining = player.getExtraArmies();
		List<AttackTarget> toAttack = new ArrayList<AttackTarget>();
		for (Iterator<Country> i = gameState.capitals.iterator(); i.hasNext();) {
			Country c = i.next();
			if (c.getOwner() == player || (target != null && target != c.getOwner())) {
				continue;
			}
			AttackTarget at = targets.get(c);
			if (at == null) {
				if (allOrNone) {
					return null;
				}
				continue;
			}
			if (at.remaining < 1) {
				remaining += at.remaining;
				if (remaining < 1 && allOrNone) {
					return null;
				}
				if (!attack && !allOrNone) {
					toAttack.add(at);
				}
			} else if (attack) {
				toAttack.add(at);
			} else {
				return null; //should be taken
			}
		}	
		if (!toAttack.isEmpty()) {
			if (allOrNone) {
				Collections.sort(toAttack); //hardest first
			} else {
				Collections.sort(toAttack, Collections.reverseOrder()); 
			}
			AttackTarget at = toAttack.get(0);
			int route = findBestRoute(attackable, gameState, attack, null, at, gameState.targetPlayers.get(0), targets);
			Country start = attackable.get(route);
			if (attack) {
				return getAttack(targets, at, route, start);
			}
			return getPlaceCommand(start, -at.remaining + 1);
		}
		return null;
	}

	/**
	 * Overrides the default battle won behavior to defend capitals more
	 */
	protected String getBattleWon(GameState gameState) {
		if (gameState.commonThreat == null) {
			return super.getBattleWon(gameState);
		}
		if (gameState.capitals.contains(game.getAttacker())) {
			int needed = additionalTroopsNeeded(game.getAttacker(), gameState);
			if (needed > 0) {
				return "move " + game.getMustMove();
			}
			return "move " + Math.max(game.getMustMove(), -needed/2 - getMinPlacement());
		}
		if (gameState.capitals.contains(game.getDefender())
				&& (ownsNeighbours(player, game.getAttacker()) || !ownsNeighbours(player, game.getDefender()))) {
			return "move all";
		}
		return super.getBattleWon(gameState);
	}
	
	public String getCapital() {
		return "capital " + findCapital().getColor();
	}
	
	/**
	 * Searches for the country with the lowest (best) score as the capital.
	 */
	protected Country findCapital() {
		int score = Integer.MAX_VALUE;
		Country result = null;
		List<Country> v = player.getTerritoriesOwned();
		for (int i = 0; i < v.size(); i++) {
			Country c = v.get(i);
			int val = scoreCountry(c);
			val -= c.getArmies()/game.getPlayers().size();
			if (val < score || (val == score && r.nextBoolean())) {
				score = val;
				result = c;
			}
		}
		return result;
	}
	
	protected double getContinentValue(Continent co) {
		double value = super.getContinentValue(co);
		if (!game.getSetup()) {
			//blunt the affect of continent modification so that we'll mostly consider
			//contiguous countries
			return Math.sqrt(value);
		}
		return super.getContinentValue(co);
	}

}
