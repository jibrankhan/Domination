// Yura Mamyrin, Group D

package net.yura.domination.engine;

import net.yura.domination.engine.core.RiskGame;

/**
 * <p> Risk Adapter </p>
 * @author Yura Mamyrin
 */

public abstract class RiskAdapter implements RiskListener {

    public void sendMessage(String output, boolean a, boolean b) {}
    public void needInput(int s) {}
    public void noInput() {}
    public void setGameStatus(String state) {}
    public void newGame(boolean t) {}
    public void startGame(boolean s) {}
    public void closeGame() {}
    public void showMapPic(RiskGame p) {}
    public void showCardsFile(String c, boolean m) {}
    public void serverState(boolean s) {}
    public void openBattle(int c1num, int c2num) {}
    public void closeBattle() {}
    public void addPlayer(int type, String name, int color, String ip) {}
    public void delPlayer(String name) {}
    public void showDiceResults(int[] att, int[] def) {}
    public void setNODAttacker(int n) {}
    public void setNODDefender(int n) {}
    public void sendDebug(String a) {}
    public void showMessageDialog(String a) {}
}
