package net.yura.domination.engine.ai;

import net.yura.domination.engine.Risk;
import net.yura.domination.engine.core.Player;
import net.yura.domination.engine.core.RiskGame;

public class AIPlayer {

	public static AICrap aicrap = new AICrap();
	private static AIEasy aieasy = new AIEasy();
	private static AIEasy aihard = new AIHardDomination();
	private static AIEasy aihardc = new AIHardCapital();
	private static AIEasy aihardm = new AIHardMission();

	private static int wait=500;

	public static int getWait() {

		return wait;

	}
	public static void setWait(int w) {

		wait = w;

	}

	public static void play(Risk risk) {

		RiskGame game = risk.getGame();

		int skill =  game.getCurrentPlayer().getType();

		AICrap usethisAI=null;

		if (skill == Player.PLAYER_AI_CRAP ) {

			usethisAI = aicrap;

		}
		else if (skill == Player.PLAYER_AI_EASY ) {

			usethisAI = aieasy;

		}
		else if (skill == Player.PLAYER_AI_HARD) {

			int mode = game.getGameMode();

			if (mode == RiskGame.MODE_DOMINATION) {

				usethisAI = aihard;

			}
			else if (mode == RiskGame.MODE_CAPITAL) {

				usethisAI = aihardc;

			}
			else if (mode == RiskGame.MODE_SECRET_MISSION) {

				usethisAI = aihardm;

			}

		}

		String output = getOutput(game,usethisAI);
                
                System.out.println(output);

		try { Thread.sleep(wait); }
		catch(InterruptedException e) {}

		risk.parser(output);

	}


	public static String getOutput(RiskGame game,AICrap usethisAI) {

		usethisAI.setPlayer(game);

		String output=null;

		switch ( game.getState() ) {
			case RiskGame.STATE_TRADE_CARDS:	output = usethisAI.getTrade(); break;
			case RiskGame.STATE_PLACE_ARMIES:	output = usethisAI.getPlaceArmies(); break;
			case RiskGame.STATE_ATTACKING:		output = usethisAI.getAttack(); break;
			case RiskGame.STATE_ROLLING:		output = usethisAI.getRoll(); break;
			case RiskGame.STATE_BATTLE_WON:		output = usethisAI.getBattleWon(); break;
			case RiskGame.STATE_FORTIFYING:		output = usethisAI.getTacMove(); break;
			case RiskGame.STATE_SELECT_CAPITAL:	output = usethisAI.getCapital(); break;

			case RiskGame.STATE_END_TURN:		output = "endgo"; break;
			case RiskGame.STATE_GAME_OVER:		/* output="closegame"; */ break;
			case RiskGame.STATE_DEFEND_YOURSELF:	output = usethisAI.getAutoDefendString(); break;

			default: throw new RuntimeException("AI error: unknown state "+ game.getState() );
		}

		if (output==null) { throw new NullPointerException("AI ERROR!"); }

		return output;

	}



}
