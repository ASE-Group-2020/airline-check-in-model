package airlineinterface;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class WaitingQueue implements Runnable {

	private Queue<Customer> waiting;
	private List<Customer> notArrived;
	public boolean active = true;

	public WaitingQueue() {
		setWaiting(new LinkedList<Customer>());
		notArrived = new LinkedList<Customer>();
	}

	@Override
	public void run() {
		Logger.instance().MainLog("Starting queue simulation");
		try { Thread.sleep(3000); } catch (InterruptedException e1) {}
		Logger.instance().MainLog("People have started arriving at the airport");
		while (!notArrived.isEmpty() && active) {
			getWaiting().add(notArrived.remove(0));
			// Logging
			Customer c = ((LinkedList<Customer>) getWaiting()).peekLast();
			Logger.instance().PassengerJoinedQueue(c);
			try { Thread.sleep(1000); } catch (InterruptedException e) {}
		}
		Logger.instance().MainLog("Everyone has arrived");
	}

	// Adds every given customer to the not-arrived list, then shuffles
	public void addCustomersToList(List<Customer> customers) {
		Logger.instance().MainLog("Adding customers to notArrived");
		notArrived.addAll(customers);
		Collections.shuffle(notArrived);
		Logger.instance().MainLog("notArrives length: " + notArrived.size());
	}

	// Pop and return customer - null if empty
	public synchronized Customer getNext() {
		return getWaiting().poll();
	}

	// Get / Set methods
	public Queue<Customer> getWaiting() {
		return waiting;
	}

	public void setWaiting(Queue<Customer> waiting) {
		this.waiting = waiting;
	}

	public List<Customer> getnotArrived() {
		return notArrived;
	}

}
