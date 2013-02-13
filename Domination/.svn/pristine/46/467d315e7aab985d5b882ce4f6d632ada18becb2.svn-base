// Yura Mamyrin, Group D

package net.yura.domination.engine;

import java.util.Vector;
import net.yura.domination.engine.core.RiskGame;

/**
 * <p> Risk Controller </p>
 * @author Yura Mamyrin
 */

public class RiskController {

    private Vector obs;

    public RiskController() {
	obs = new Vector();
    }

    /**
     * Adds an observer to the set of observers for this object, provided
     * that it is not the same as some observer already in the set.
     * The order in which notifications will be delivered to multiple
     * observers is not specified. See the class comment.
     *
     * @param   o   an observer to be added.
     * @throws NullPointerException   if the parameter o is null.
     */
    public synchronized void addListener(RiskListener o) {
        if (o == null)
            throw new NullPointerException();
	if (!obs.contains(o)) {
	    obs.addElement(o);
	}
    }

    public int countListeners() {

	return obs.size();

    }

    /**
     * Deletes an observer from the set of observers of this object.
     * Passing <CODE>null</CODE> to this method will have no effect.
     * @param   o   the observer to be deleted.
     */
    public synchronized void deleteListener(RiskListener o) {
        obs.removeElement(o);
    }


    public void sendMessage(String output, boolean redrawNeeded, boolean repaintNeeded) {

        Object[] arrLocal;

	synchronized (this) {
            arrLocal = obs.toArray();
        }

	try {
            for (int i = arrLocal.length-1; i>=0; i--)
                ((RiskListener)arrLocal[i]).sendMessage(output,redrawNeeded,repaintNeeded);
	}
	catch(Exception ex) {
	    RiskUtil.printStackTrace(ex);
	}
    }

    public void needInput(int s) {

        Object[] arrLocal;

	synchronized (this) {
            arrLocal = obs.toArray();
        }

	try {
            for (int i = arrLocal.length-1; i>=0; i--)
                ((RiskListener)arrLocal[i]).needInput(s);
        }
        catch(Exception ex) {
            RiskUtil.printStackTrace(ex);
        }
    }

    public void noInput() {

        Object[] arrLocal;

	synchronized (this) {
            arrLocal = obs.toArray();
        }

	try {
            for (int i = arrLocal.length-1; i>=0; i--)
                ((RiskListener)arrLocal[i]).noInput();
	}
        catch(Exception ex) {
            RiskUtil.printStackTrace(ex);
        }
    }

    public void setGameStatus(String state) {

        Object[] arrLocal;

	synchronized (this) {
            arrLocal = obs.toArray();
        }

	try {
            for (int i = arrLocal.length-1; i>=0; i--)
                ((RiskListener)arrLocal[i]).setGameStatus(state);
	}
        catch(Exception ex) {
            RiskUtil.printStackTrace(ex);
        }
    }

    public void newGame(boolean t) {

        Object[] arrLocal;

	synchronized (this) {
            arrLocal = obs.toArray();
        }

	try {
            for (int i = arrLocal.length-1; i>=0; i--)
                ((RiskListener)arrLocal[i]).newGame(t);
	}
        catch(Exception ex) {
            RiskUtil.printStackTrace(ex);
        }
    }

    public void startGame(boolean s) {

        Object[] arrLocal;

	synchronized (this) {
            arrLocal = obs.toArray();
        }

	try {
            for (int i = arrLocal.length-1; i>=0; i--)
                ((RiskListener)arrLocal[i]).startGame(s);
	}
        catch(Exception ex) {
            RiskUtil.printStackTrace(ex);
        }
    }

    public void closeGame() {

        Object[] arrLocal;

	synchronized (this) {
            arrLocal = obs.toArray();
        }

	try {
            for (int i = arrLocal.length-1; i>=0; i--)
                ((RiskListener)arrLocal[i]).closeGame();
	}
        catch(Exception ex) {
            RiskUtil.printStackTrace(ex);
        }
    }

//    public void setSlider(int min, int c1num, int c2num) {
//
//        Object[] arrLocal;
//
//	synchronized (this) {
//            arrLocal = obs.toArray();
//        }
//
//	try {
//            for (int i = arrLocal.length-1; i>=0; i--)
//                ((RiskListener)arrLocal[i]).setSlider(min,c1num,c2num);
//	}
//        catch(Exception ex) {
//            RiskUtil.printStackTrace(ex);
//        }
//    }

//    public void armiesLeft(int l, boolean s) {
//
//        Object[] arrLocal;
//
//	synchronized (this) {
//            arrLocal = obs.toArray();
//        }
//
//	try {
//            for (int i = arrLocal.length-1; i>=0; i--)
//                ((RiskListener)arrLocal[i]).armiesLeft(l,s);
//	}
//        catch(Exception ex) {
//            RiskUtil.printStackTrace(ex);
//        }
//    }

//    public void showDice(int n, boolean w) {
//
//        Object[] arrLocal;
//
//	synchronized (this) {
//            arrLocal = obs.toArray();
//        }
//
//	try {
//            for (int i = arrLocal.length-1; i>=0; i--)
//                ((RiskListener)arrLocal[i]).showDice(n,w);
//	}
//        catch(Exception ex) {
//            RiskUtil.printStackTrace(ex);
//        }
//    }

    public void showMapPic(RiskGame p) {

        Object[] arrLocal;

	synchronized (this) {
            arrLocal = obs.toArray();
        }

	try {
            for (int i = arrLocal.length-1; i>=0; i--)
                ((RiskListener)arrLocal[i]).showMapPic(p);
	}
        catch(Exception ex) {
            RiskUtil.printStackTrace(ex);
        }
    }

    public void showCardsFile(String c, boolean m) {

        Object[] arrLocal;

	synchronized (this) {
            arrLocal = obs.toArray();
        }

	try {
            for (int i = arrLocal.length-1; i>=0; i--)
                ((RiskListener)arrLocal[i]).showCardsFile(c, m);
	}
        catch(Exception ex) {
            RiskUtil.printStackTrace(ex);
        }
    }

    public void serverState(boolean s) {

        Object[] arrLocal;

	synchronized (this) {
            arrLocal = obs.toArray();
        }

	try {
            for (int i = arrLocal.length-1; i>=0; i--)
                ((RiskListener)arrLocal[i]).serverState(s);
	}
        catch(Exception ex) {
            RiskUtil.printStackTrace(ex);
        }
    }

    public void openBattle(int c1num, int c2num) {

        Object[] arrLocal;

	synchronized (this) {
            arrLocal = obs.toArray();
        }

	try {
            for (int i = arrLocal.length-1; i>=0; i--)
                ((RiskListener)arrLocal[i]).openBattle(c1num,c2num);
	}
        catch(Exception ex) {
            RiskUtil.printStackTrace(ex);
        }
    }

    public void closeBattle() {

        Object[] arrLocal;

	synchronized (this) {
            arrLocal = obs.toArray();
        }

	try {
            for (int i = arrLocal.length-1; i>=0; i--)
                ((RiskListener)arrLocal[i]).closeBattle();
	}
        catch(Exception ex) {
            RiskUtil.printStackTrace(ex);
        }
    }

    public void addPlayer(int type, String name, int color, String ip) {

        Object[] arrLocal;

	synchronized (this) {
            arrLocal = obs.toArray();
        }

	try {
            for (int i = arrLocal.length-1; i>=0; i--)
                ((RiskListener)arrLocal[i]).addPlayer(type, name, color, ip);
	}
        catch(Exception ex) {
            RiskUtil.printStackTrace(ex);
        }
    }

    public void delPlayer(String name) {

        Object[] arrLocal;

	synchronized (this) {
            arrLocal = obs.toArray();
        }

	try {
            for (int i = arrLocal.length-1; i>=0; i--)
                ((RiskListener)arrLocal[i]).delPlayer(name);
	}
        catch(Exception ex) {
            RiskUtil.printStackTrace(ex);
        }
    }

    public void showDiceResults(int[] att, int[] def) {

        Object[] arrLocal;

	synchronized (this) {
            arrLocal = obs.toArray();
        }

	try {
            for (int i = arrLocal.length-1; i>=0; i--)
                ((RiskListener)arrLocal[i]).showDiceResults(att,def);
	}
        catch(Exception ex) {
            RiskUtil.printStackTrace(ex);
        }
    }

    public void setNODAttacker(int n) {

        Object[] arrLocal;

	synchronized (this) {
            arrLocal = obs.toArray();
        }

	try {
            for (int i = arrLocal.length-1; i>=0; i--)
                ((RiskListener)arrLocal[i]).setNODAttacker(n);
	}
        catch(Exception ex) {
            RiskUtil.printStackTrace(ex);
        }
    }

    public void setNODDefender(int n) {

        Object[] arrLocal;

	synchronized (this) {
            arrLocal = obs.toArray();
        }

	try {
            for (int i = arrLocal.length-1; i>=0; i--)
                ((RiskListener)arrLocal[i]).setNODDefender(n);
	}
        catch(Exception ex) {
            RiskUtil.printStackTrace(ex);
        }
    }

    public void sendDebug(String a) {

        Object[] arrLocal;

	synchronized (this) {
            arrLocal = obs.toArray();
        }

	try {
            for (int i = arrLocal.length-1; i>=0; i--)
                ((RiskListener)arrLocal[i]).sendDebug(a);
	}
        catch(Exception ex) {
            RiskUtil.printStackTrace(ex);
        }
    }

    public void showMessageDialog(String a) {

        Object[] arrLocal;

	synchronized (this) {
            arrLocal = obs.toArray();
        }

	try {
            for (int i = arrLocal.length-1; i>=0; i--)
                ((RiskListener)arrLocal[i]).showMessageDialog(a);
	}
        catch(Exception ex) {
            RiskUtil.printStackTrace(ex);
        }
    }

}
