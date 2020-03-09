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
		// TODO: simulate adding people from list to queue as they arrive
	}
	
	// Adds every given customer to the not-arrived list, then shuffles
	public void addCustomersToList(List<Customer> customers) {
		notArrived.addAll(customers);
		Collections.shuffle(notArrived);
	}
	
	// True if list has elements, false otherwise
	// TODO: make thread-safe
	public boolean hasNext() {
		return !waiting.isEmpty();
	}
	
	// Pop and return customer - null if empty
	// TODO: make thread-safe
	public Customer getNext() {
		return waiting.poll();
	}

}
