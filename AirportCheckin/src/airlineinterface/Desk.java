package airlineinterface;

import java.util.HashMap;
import java.util.List;

import exceptions.AlreadyCheckedInException;
import exceptions.InvalidValueException;

public class Desk implements Runnable {

	// Statics - each desk holds a static reference to all flights
	private static HashMap<String, Flight> allFlights = new HashMap<String, Flight>(); 
	
	//Instance-specific
	private WaitingQueue queue;
	private String deskName;
	private boolean deskExists = true;
	

	public static void addFlights(List<Flight> flights) {
		for (Flight f : flights) {
			allFlights.put(f.getFlightCode(), f);
		}
	}

	/* Constructor that takes in a queue object and the sting object is the desk name.
	 * TODO: Will the queue update after desk initialisation (answer: yes)? 
	 * Additionally, how to deal with multiple desks trying to grab the same customer?
	 */
	public Desk(WaitingQueue queue, String deskName) {
		this.queue = queue; 
		this.deskName = deskName;
	}

	/* Implements Runnable, so started with a new Thread(new MyRunnable()).start() call
	 * TODO: Create a conditional statement that kills/pauses the desk. Do we want a
	 * deconstructor?
	 * 
	 * Done. Nah, just check if there's people in the list and queue.
	 * Run one desk per thread. This is the cleanest way to do it, as long as the collections
	 * are thread-safe.
	 */ 
	public synchronized void run() {
		Logger.instance().log("Starting simulation of " + deskName);

		// While (queue OR list are NOT empty) and (deskExists is turned on) i.e the terminal is working ... do ...
		while ( (!queue.notArrived.isEmpty() || !queue.waiting.isEmpty()) && deskExists ) {
			Customer c = queue.getNext(); 		// Returns null customer object is queue is empty
			if (!(c == null)) { 				// TODO depends on the queue object how we check if it isn't empty
				Logger.instance().log(" ##DESK##  A new customer " 
									+ c.getFirstName() + " " 
									+ c.getLastName()
									+ " has moved from the queue to check in at " 
									+ deskName);
				timeDelay(3000); 				// 3 second delay for person to move to help desk
				try {startCheckIn(queue, c);}
				catch (InvalidValueException e) {System.out.println(e.getMessage());}
			}
			else {
				try {Thread.sleep(2000);} 		// Sleep until
				catch (InterruptedException e) {System.out.println(e.getMessage());}
			}
		}
		
		Logger.instance().log(" ##DESK##  The " + deskName + " has stopped accepting customers.");
	}

	/* Takes in the customer at the front of the queue using .peek(), else TODO a
	 * custom method from queue class.
	 * TODO: Add randomness to speeds
	 */
	private void startCheckIn(WaitingQueue queue, Customer currCustomer) throws InvalidValueException {
		timeDelay(6000); // 6 second delay for person to get baggage fee
		float currCustomerFee = getOversizeFee(currCustomer.getBaggageDetails()[0],
											currCustomer.getBaggageDetails()[1]); // Get the baggage fee of the first customer
		// Log the customer's baggage fee
		Logger.instance().log(" ##DESK##  The baggage fee of "
									+ currCustomerFee + " was collected from "
									+ currCustomer.getFirstName()
									+ " " + currCustomer.getLastName() 
									+ " at " + deskName);
		timeDelay(3000); // 3 seconds to confirm check in and leave desk;
		checkIn(currCustomer, currCustomerFee); // Checks in a customer
	}

	private void timeDelay(int millisec) {
		try {
			Thread.sleep(millisec);
		} 
		catch (InterruptedException e) {
			System.out.println(e.getMessage() + " failed to interrupt thread for " + millisec + " milliseconds.");
		}
	}

	private void checkIn(Customer currCustomer, float baggageFee) {
		try {
			addCustomerToFlight(currCustomer, currCustomer.getFlightCode(), baggageFee); // add customer to their selected flight
			currCustomer.setCheckedIn(); // Change boolean flag in customer object
			// Log that the customer has finished checking in.
			Logger.instance().log(" ##DESK##  " 
									+ currCustomer.getFirstName() + " " 
									+ currCustomer.getLastName()
									+ " has finished checking in");
		} 
		catch (AlreadyCheckedInException e) {System.out.println("Customer has already been checked in! Desk/CheckIn()");} 
		catch (InvalidValueException e) {System.out.println("Invalid value passed at Desk/CheckIn().");} 
		catch (Exception e) {e.printStackTrace();}
	}

	public float getOversizeFee(float currentWeight, float currentVolume) throws InvalidValueException {
		if (currentWeight < 15 && currentVolume < 20) {
			return 0;
		} else if (currentWeight < 25 && currentVolume < 35) {
			return 10;
		} else if (currentWeight < 45 && currentVolume < 55) {
			return 20;
		} else if (currentWeight < 70 && currentVolume < 90) {
			return 40;
		} else if (currentWeight < 100 && currentVolume < 120) {
			return 60;
		} else if (currentWeight < 150 && currentVolume < 180) {
			return 80;
		} else if (currentWeight <= 200 && currentVolume <= 260) {
			return 100;
		}
		// This is the maximum weight and volume an individual is allowed to possess.
		// Beyond 200kg

		// FYI, it does.  - Well allrighty then.
		else throw new InvalidValueException("the baggage shouldn't be more than 200kg in weight or 260 litres in volume.");
	}

	private void addCustomerToFlight(Customer currCustomer, String flightCode, float baggageFee) {
		Flight currFlight = allFlights.get(flightCode);
		try {
			currFlight.addCustomer(currCustomer, baggageFee);
			// Log that the customer has been added to the flight
			Logger.instance().log(" ##DESK##  " 
									+ currCustomer.getFirstName() + " " 
									+ currCustomer.getLastName()
									+ " has been added to " 
									+ currFlight.getFlightCode());
		} catch (InvalidValueException e) {
			System.out.println("Invalid value of customer baggage details found at Desk/addCustomerToFlight()");
			e.printStackTrace();
		}

	}

}
