package airlineinterface;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class WaitingQueue extends Observable implements Runnable {

	private Queue<Customer> standardWaiting;
	private Queue<Customer> businessWaiting;
	private List<Customer> notArrived;
	public boolean active = true;
	
	private boolean justPulledBusinessClass = false;

	public WaitingQueue() {
		standardWaiting = new LinkedList<Customer>();
		businessWaiting = new LinkedList<Customer>();
		notArrived = new LinkedList<Customer>();
	}

	@Override
	public void run() {
		Logger.instance().MainLog("Starting queue simulation");
		Simulator.sleep(3000);
		Logger.instance().MainLog("People have started arriving at the airport");
		while (!notArrived.isEmpty() && active) {
			Customer c = nextCustomerArrived();
			notifyObservers();
			// Logging
			Logger.instance().PassengerJoinedQueue(c);
			Simulator.sleep(1000);
		}
		notifyObservers();
		if (active) Logger.instance().MainLog("Everyone has arrived");
	}

	// Adds every given customer to the not-arrived list, then shuffles
	public void addCustomersToList(List<Customer> customers) {
		Logger.instance().MainLog("Adding customers to notArrived");
		notArrived.addAll(customers);
		Collections.shuffle(notArrived);
		Logger.instance().MainLog("notArrives length: " + notArrived.size());
		notifyObservers();
	}
	
	public void makeCustomersArrive(int numberToArrive) {
		for (int i = 0; i < numberToArrive && notArrived.size() > 0; i++)
			nextCustomerArrived();
		notifyObservers();
	}

	// Pop and return customer - null if empty
	public synchronized Customer getNext() {	
		Customer c;
		if 		(standardWaiting.isEmpty()) { c = businessWaiting.poll(); justPulledBusinessClass = true; }
		else if (standardWaiting.isEmpty()) { c = standardWaiting.poll(); justPulledBusinessClass = false; }
		else
		{
			if (justPulledBusinessClass) c = standardWaiting.poll();
			else 						 c = businessWaiting.poll();
			justPulledBusinessClass = !justPulledBusinessClass;
		}
		notifyObservers();
		return c;
	}
	
	public Customer nextCustomerArrived()
	{
		Customer c = notArrived.remove(0);
		if 		(c.getSeatingClass().equalsIgnoreCase("Standard")) standardWaiting.add(c);
		else if (c.getSeatingClass().equalsIgnoreCase("Business")) businessWaiting.add(c);
		else System.out.println("ERROR: known seating class detected: " + c.getSeatingClass());
		return c;
	}

	// Get / Set methods TODO not referenced outside class, is it public for JUnit tests?
	/*
	public synchronized Queue<Customer> getWaiting() {
		return waiting;
	}
	*/
	
	public Queue<Customer> getWaitingCopyStandard() {
		return new LinkedList<Customer>(standardWaiting);
	}
	
	public Queue<Customer> getWaitingCopyBusiness() {
		return new LinkedList<Customer>(businessWaiting);
	}
	/*// methods TODO not referenced outside class, is it public for JUnit tests?
	public void setWaiting(Queue<Customer> waiting) {
		this.waiting = waiting;
	}*/

	public List<Customer> getNotArrived() {
		return notArrived;
	}

}
