/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.yura.domination.engine.ai.planrecognition.eventhandling;

import net.yura.domination.engine.ai.planrecognition.events.Event;
import java.io.Serializable;
import java.util.Observable;

/**
 * Processing object
 * @author s0914007
 */
public class EventObserver extends Observable implements Serializable {
	
	public void fireEvent(Event event) {
		
		this.setChanged();
		this.notifyObservers(event);
	}
}
