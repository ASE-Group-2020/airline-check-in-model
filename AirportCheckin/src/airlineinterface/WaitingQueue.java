package airlineinterface;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class WaitingQueue implements Runnable {

	Queue<Customer> waiting;
	List<Customer> notArrived;

	public WaitingQueue() {
		waiting = new LinkedList<Customer>();
		notArrived = new LinkedList<Customer>();
	}

	@Override
	public void run() {
		Logger.instance().log("Starting queue simulation");
		try { Thread.sleep(3000); } catch (InterruptedException e1) {}
		Logger.instance().log("People have started arriving at the airport");
		while (!notArrived.isEmpty()) {
			waiting.add(notArrived.remove(0));
			// Logging
			Customer c = ((LinkedList<Customer>) waiting).peekLast();
			Logger.instance().log("  " + c.getFirstName() + " " + c.getLastName() + " arrived at the airport");
			try { Thread.sleep(1000); } catch (InterruptedException e) {}
		}
		Logger.instance().log("Everyone has arrived");
	}

	// Adds every given customer to the not-arrived list, then shuffles
	public void addCustomersToList(List<Customer> customers) {
		Logger.instance().log("Adding customers to notArrived");
		notArrived.addAll(customers);
		Collections.shuffle(notArrived);
		Logger.instance().log("notArrives length: " + notArrived.size());
	}

	// Pop and return customer - null if empty
	public synchronized Customer getNext() {
		return waiting.poll();
	}

}
