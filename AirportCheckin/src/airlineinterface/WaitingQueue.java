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
		System.out.println("In run");
		try { Thread.sleep(3000); } catch (InterruptedException e1) {}
		while (!notArrived.isEmpty()) {
			waiting.add(notArrived.remove(0));
			try { Thread.sleep(1000); } catch (InterruptedException e) {}
			System.out.println("Just added someone");
		}
	}
	
	// Adds every given customer to the not-arrived list, then shuffles
	public void addCustomersToList(List<Customer> customers) {
		System.out.println("Adding customers to notArrived");
		notArrived.addAll(customers);
		Collections.shuffle(notArrived);
		System.out.println("notArrives length: " + notArrived.size());
	}

	// Pop and return customer - null if empty
	public synchronized Customer getNext() {
		return waiting.poll();
	}

}
