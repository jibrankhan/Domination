// Yura Mamyrin, Group D

package net.yura.domination.ui.flashgui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import net.yura.domination.engine.Risk;
import net.yura.domination.engine.RiskListener;
import net.yura.domination.engine.RiskUIUtil;
import net.yura.domination.engine.core.Country;
import net.yura.domination.engine.core.RiskGame;
import net.yura.domination.engine.guishared.PicturePanel;

/**
 * <p> Risk Listener for FlashGUI </p>
 * @author Yura Mamyrin
 */

// this get all the commands from the game and does what needs to be done
public class FlashRiskAdapter implements RiskListener {

	private MainMenu menu;
	private Risk myrisk;

	protected GameFrame gameFrame;
	private NewGameFrame newgameframe;
	private BattleDialog battledialog;
	protected PicturePanel pp;

	private int nogames;

        public FlashRiskAdapter(Risk r) {
		myrisk = r;

		myrisk.addRiskListener(this);

		pp = new PicturePanel(myrisk);
		gameFrame = new GameFrame(myrisk, pp);
		battledialog = new BattleDialog(gameFrame, false, myrisk);
		gameFrame.setBattleDialog(battledialog);

                RiskUIUtil.center(battledialog);

	}


	FlashRiskAdapter(MainMenu m, Risk r) {
		this(r);
		menu = m;
		newgameframe = new NewGameFrame(myrisk);
	}

	/**
	 * Checks if redrawing or repainting is needed
	 * @param output
	 * @param redrawNeeded If frame needs to be redrawn
	 * @param repaintNeeded If frame needs to be repainted
	 */
	public void sendMessage(String output, boolean redrawNeeded, boolean repaintNeeded) {

		try {
			if (redrawNeeded) {
				gameFrame.repaintCountries();
			}
			if (repaintNeeded) {
				gameFrame.repaint();
			}
		}
		catch (NullPointerException e) { }
	}

	public void sendDebug(String a) {
            try {
                net.yura.grasshopper.PopupBug.log( a + System.getProperty("line.separator") );
            }
            catch(Throwable th) {
            }
        }

	public void showMessageDialog(String a) {

		if ( gameFrame!=null && gameFrame.isVisible() ) {

			JOptionPane.showMessageDialog(gameFrame,a);

		}
		else if (menu != null && menu.isVisible()) {

			JOptionPane.showMessageDialog(menu,a);

		}
		else if (newgameframe != null && newgameframe.isVisible()) {

			JOptionPane.showMessageDialog(newgameframe,a);

		}
		else {

			JOptionPane.showMessageDialog(null,a);

		}

	}

	/**
	 * Sets the game state
	 * @param state The game state
	 */
	public void setGameStatus(String state) {
		try {
			gameFrame.setGameStatus(state);
		}
		catch (NullPointerException e) { }

	}

	/**
	 * checks if the the frame needs input
	 * @param s determines what needs input
	 */
	public void needInput(int s) {

		if ( gameFrame.isVisible() ) {

			if (s == RiskGame.STATE_ROLLING) {

				battledialog.needInput(myrisk.getGame().getNoAttackDice(), true);

			}
			else if (s == RiskGame.STATE_DEFEND_YOURSELF) {

				battledialog.needInput(myrisk.getGame().getNoDefendDice(), false);

			}
			//else { // this will update the state in the gameframe

				gameFrame.needInput(s);

			//}

		}

	}

	/**
	 * Starts a battle dialogue
	 * @param c1num Country number of the attacker
	 * @param c2num Country number of the defender
	 */
	public void openBattle(int c1num, int c2num) {

		BufferedImage c1img = gameFrame.getCountryImage( c1num );
		BufferedImage c2img = gameFrame.getCountryImage( c2num );

		Country country1 = myrisk.getGame().getCountryInt( c1num);
		Country country2 = myrisk.getGame().getCountryInt( c2num);

		Color color1 = new Color( myrisk.getColorOfOwner( c1num ) );
		Color color2 = new Color( myrisk.getColorOfOwner( c2num ) );

		battledialog.setup(c1num, c2num, c1img, c2img, country1, country2, color1 ,color2);

		battledialog.setVisible(true);

	}

	/**
	 * Sets number of attackers
	 * @param n number of attackers
	 */
	public void setNODAttacker(int n) {

		if (battledialog.isVisible() ) {
			battledialog.setNODAttacker(n);
		}

	}

	/**
	 * Sets number of defenders
	 * @param n number of defenders
	 */
	public void setNODDefender(int n) {

		if (battledialog.isVisible() ) {
			battledialog.setNODDefender(n);
		}

	}

	/**
	 * Shows the dice results
	 * @param att The attacker's results
	 * @param def The defender's results
	 */
	public void showDiceResults(int[] att, int[] def) {

		if (battledialog.isVisible() ) {
			battledialog.showDiceResults(att, def);
		}

	}

	/**
	 * Closes the battle dialogue
	 */
	public void closeBattle() {

		if (battledialog.isVisible() ) {

			battledialog.setVisible(false);

		}

	}

	/**
	 * Starts the game
	 * @param localGame If the game is a local game
	 */
	public void newGame(boolean localGame) {

		menu.hideJoinDialog(localGame);

		newgameframe.setup(localGame);

                RiskUIUtil.center(newgameframe);

		menu.hide();

		newgameframe.setVisible(true);
		newgameframe.requestFocus();

	}

	/**
	 * Blocks the game panel
	 */
	public void noInput() {

		if ( newgameframe.isVisible() ) {
			newgameframe.noInput();
		}
		else {
			gameFrame.noInput();
		}
	}

	/**
	 * Starts the game
	 * @param s If the game is a local game
	 */
	public void startGame(boolean s) {

		if ( newgameframe.isVisible() ) {
			newgameframe.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		}
		else {
			menu.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		}

		gameFrame.setup(s);

                RiskUIUtil.center(gameFrame);

		if ( newgameframe.isVisible() ) {
			newgameframe.setVisible(false);
		}
		else {
			menu.hide();
		}

		gameFrame.setVisible(true);

		// this should not have to be here, but is the only way to get rid of it
		battledialog.setVisible(false);

		gameFrame.requestFocus();

	}

	/**
	 * Closes the game
	 */
	public void closeGame() {

		if ( gameFrame.isVisible() ) {

                        pp.stopAni(); // stop any animations
                    
			gameFrame.setVisible(false);

		}
		else {
			newgameframe.setVisible(false);

		}

		nogames++;

                try {
                    net.yura.grasshopper.PopupBug.clearLog();
                    net.yura.grasshopper.PopupBug.log( "game "+nogames+" closed, log cleared"+System.getProperty("line.separator") );
                }
                catch(Throwable th) {
                }

		newgameframe.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

		System.gc();




		menu.show();





		menu.requestFocus();
		menu.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

	}

	/**
	 * Checks the server's state
	 * @param s The server's state
	 */
	public void serverState(boolean s) {
		menu.setServerRunning(s);
	}

	/**
	 * Shows the picture of the map
	 * @param p The image of the map
	 */
	public void showMapPic(RiskGame p) {
            ImageIcon i=null;
            try {
                i = new ImageIcon( PicturePanel.getImage(p) );
            }
            catch (Throwable th) { }
            newgameframe.setMap( i ); // SCALE_DEFAULT
	}

	/**
	 * Shows the card file
	 * @param c The name of the cards file
	 */
	public void showCardsFile(String c, boolean m) {
		newgameframe.setCards(c, m);
	}

	/**
	 * Adds a player to the game
	 * @param type The player type
	 * @param name The player name
	 * @param color The player color
	 * @param ip The player ip
	 */
	public void addPlayer(int type, String name, int color, String ip) {
		newgameframe.addPlayer(type, name, new Color( color ), ip);
	}

	/**
	 * Delete a player from the game
	 * @param name Name of the player to be deleted
	 */
	public void delPlayer(String name) {
		newgameframe.delPlayer(name);
	}

	public GameFrame getGameFrame() {
		return gameFrame;
	}

}
