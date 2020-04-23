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

	/* Thread run method - Gradually adds passengers to the waiting queue. Stops running once everyone's arrived or when the siumlation has stopped */
	@Override
	public void run() {
		Logger.instance().MainLog("Starting queue simulation");
		Simulator.get().sleep(3000);
		Logger.instance().MainLog("People have started arriving at the airport");
		while (!notArrived.isEmpty() && active)
		{
			Customer c = nextCustomerArrived();
			notifyObservers();
			Logger.instance().PassengerJoinedQueue(c);
			Simulator.get().sleep(1000);
		}
		notifyObservers();
		if (active) Logger.instance().MainLog("Everyone has arrived");
	}

	// Adds every given customer to the not-arrived list, then shuffles
	public void addCustomersToList(List<Customer> customers) {
		Logger.instance().MainLog("Adding customers to notArrived");
		notArrived.addAll(customers);
		Collections.shuffle(notArrived);
		Logger.instance().MainLog("notArrived length: " + notArrived.size());
		notifyObservers();
	}
	
	// forces a number of passengers to arrive and wait in the queue
	public void makeCustomersArrive(int numberToArrive) {
		for (int i = 0; i < numberToArrive && notArrived.size() > 0; i++)
			nextCustomerArrived();
		notifyObservers();
	}

	// Pop and return customer - null if empty
	public synchronized Customer getNext() {	
		Customer c;
		if 		(standardWaiting.isEmpty()) { c = businessWaiting.poll(); justPulledBusinessClass = true; }
		else if (businessWaiting.isEmpty()) { c = standardWaiting.poll(); justPulledBusinessClass = false; }
		else
		{
			if (justPulledBusinessClass) c = standardWaiting.poll();
			else 						 c = businessWaiting.poll();
			justPulledBusinessClass = !justPulledBusinessClass;
		}
		notifyObservers();
		return c;
	}
	
	// makes a customer arrive at the airport
	private Customer nextCustomerArrived()
	{
		Customer c = notArrived.remove(0);
		if (c != null)
		{
			switch (c.getSeatingClass().toLowerCase())
			{
			case "standard":
				standardWaiting.add(c);
				break;
			case "business":
				businessWaiting.add(c);
				break;
			default:
				System.out.println("ERROR: known seating class detected: " + c.getSeatingClass());
				break;
			}
		}
		return c;
	}
	
	public Queue<Customer> getWaitingCopyStandard() {
		return new LinkedList<Customer>(standardWaiting);
	}
	
	public Queue<Customer> getWaitingCopyBusiness() {
		return new LinkedList<Customer>(businessWaiting);
	}

	public List<Customer> getNotArrived() {
		return notArrived;
	}

}
