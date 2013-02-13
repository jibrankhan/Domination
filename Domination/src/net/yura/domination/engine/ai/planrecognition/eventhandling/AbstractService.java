package net.yura.domination.engine.ai.planrecognition.eventhandling;

import net.yura.domination.engine.ai.planrecognition.events.Event;
import com.google.common.util.concurrent.AbstractExecutionThreadService;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class AbstractService extends AbstractExecutionThreadService implements Observer {
    
    private Processing processing;
	
    private boolean keepRunning;

    private Queue<Event> eventQueue;
    
    protected Processing getProcessing() {	

        return this.processing;

    }
    
    public AbstractService(Processing processing) {
		
	super();
		
	this.processing = processing;
		
	this.eventQueue = new ConcurrentLinkedQueue<Event>();
                
    }
    
    @Override
    protected void startUp() throws Exception {

            super.startUp();

            this.processing.addObserver(this);

            this.keepRunning = true;
    }

    @Override
    protected void run() throws Exception {

            while (this.keepRunning) {

                    while (!this.eventQueue.isEmpty()) {

                            this.handle(this.eventQueue.remove());

                            if (!this.keepRunning) {

                                    break;
                            }
                    }
            }
    }

    @Override
    public void update(Observable observable, Object object) {

            if (observable == this.processing) {

                    if (object instanceof Event) {

                            this.eventQueue.add((Event) object);
                    }
                    else {

                            System.err.println("Object not an Event instance: " + object.toString());
                    }
            }
    }

    @Override
    protected void triggerShutdown() {

            super.triggerShutdown();

            this.keepRunning = false;
    }

    @Override
    protected void shutDown() throws Exception {

            super.shutDown();

            this.processing.deleteObserver(this);

            this.eventQueue.clear();
    }

    protected abstract void handle(Event event) throws Exception;
}
