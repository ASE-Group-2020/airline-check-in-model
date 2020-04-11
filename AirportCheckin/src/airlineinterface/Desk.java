package airlineinterface;

import java.util.HashMap;
import java.util.List;

import exceptions.AlreadyCheckedInException;
import exceptions.InvalidValueException;
import exceptions.ObjectNotFoundException;

public class Desk implements Runnable {

	// Statics - each desk holds a static reference to all flights
	private static HashMap<String, Flight> allFlights = new HashMap<String, Flight>();
	
	// Instance-specific
	public boolean enable = true;		// Connect to terminal to control opening/closing desks via setter method
	private WaitingQueue queue;
	private String deskName;
	private Customer currCustomer;			// The current customer at the desk
	private enum Stage {
		GETTING_CUSTOMER,
		CALCULATING_FEE,
		CHECKING_IN,
		WAITING
	}
	private Stage action;
	
	
	/* No need to synchronize this, the HashMap will be made concurrent..? 
	 */
	public static void addFlights(List<Flight> flights) {
		for (Flight f : flights) {
			allFlights.put(f.getFlightCode(), f);
		}
	}

	/* Constructor that takes in a queue object and the sting object is the desk name.
	 * TODO: Will the queue update after desk initialization (answer: yes)? 
	 * Additionally, how to deal with multiple desks trying to grab the same customer?
	 */
	
	/* Constructor that takes in a queue object
	*  and the sting object is the desk identifier.
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
	 * Run one desk method (desk task) per thread. This is the cleanest way to do it, as long as the collections
	 * are thread-safe. The print statements and logger are essential to the work of the program and need to 
	 * happen in order with the operations if we are to be able to measure the program's viability without endless
	 * testing and patches. The potential speed-up from making synchronised calls to individual parts of the methods,
	 * instead of the whole method is negligible.
	 */ 
	public synchronized void run() {
		Logger.instance().MainLog("Starting simulation of " + deskName);

		// While (queue OR list are NOT empty) and (enable is turned on) i.e the terminal is working ... do ...
		// while ( (!queue.getNotArrived().isEmpty() || !queue.getWaiting().isEmpty()) && enable ) { <- OLD WAY, we want the desks waiting now should we add more customers later
		while (enable) {
			Customer c = queue.getNext(); 								// Returns null customer object is queue is empty
			this.currCustomer = c;										// this is the current customer the desk is working with
			
			if (c != null) { 											// If a customer exists in the queue, get them...
				action = Stage.GETTING_CUSTOMER;
				Logger.instance().PassengerMovedToDesk(c, deskName);
				Simulator.sleep(9000); 									// 9 second delay for person to move to help desk and calculate fee
				try {
					float currCustomerFee = getOversizeFee(currCustomer.getBaggageDetails()[0],			// Calculate oversize fees
														currCustomer.getBaggageDetails()[1]); 			// ...and set respective action in the method
					Simulator.sleep(3000); 																// 3 seconds to confirm check in and leave desk
					checkIn(currCustomer, currCustomerFee); 											// Check in the customer
					Logger.instance().MainLog("Checked in: " + c.getFirstName() + " " + c.getLastName());
				}
				catch (InvalidValueException e) {
					Logger.instance().MainLog(" ##DESK##  The " + deskName + " has reported the following error: " + e.getMessage());
				}
			}
			else {														//If not, wait
				action = Stage.WAITING;
				Simulator.sleep(2000);
			}
		}
		
		Logger.instance().MainLog(" ##DESK##  The " + deskName + " has stopped accepting customers.");
	}
	
	/*	Get the current customer the desk is working on
	 * 	@throws ObjectNotFoundException if the current customer is null
	 */
	public Customer getCurrentCustomer() throws ObjectNotFoundException {
		if (this.currCustomer == null) throw new ObjectNotFoundException("No current customer is at the desk!");
		return this.currCustomer;
	}
	
	public String getCurrentStage() {
		switch(action) {
			case GETTING_CUSTOMER:
				return "Desk action: Getting customer details.";
			case CALCULATING_FEE:
				return "Desk action: Calculating baggage fee.";
			case CHECKING_IN:
				return "Desk action: Checking a customer in.";
			case WAITING:
				return "Desk action: Waiting...";
			default:
				return "Desk action: Not set.";
		}
	}

	private synchronized void checkIn(Customer currCustomer, float baggageFee) {
		try {
			action = Stage.CHECKING_IN;
			addCustomerToFlight(currCustomer, currCustomer.getFlightCode(), baggageFee);			// Add customer to their selected flight
			currCustomer.setCheckedIn(); 															// Change boolean flag in customer object			
			// Log that the customer has finished checking in.
		} 
		catch (AlreadyCheckedInException e) {System.out.println("Customer has already been checked in! Desk/CheckIn()");} 
		catch (InvalidValueException e) {System.out.println("Invalid value passed at Desk/CheckIn().");} 
		catch (Exception e) {e.printStackTrace();}
	}

	public synchronized float getOversizeFee(float currentWeight, float currentVolume) throws InvalidValueException {
		action = Stage.CALCULATING_FEE;
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
		// This is the maximum weight and volume an individual is allowed to possess, beyond 200kg

		else throw new InvalidValueException("the baggage shouldn't be more than 200kg in weight or 260 litres in volume.");
	}

	private synchronized void addCustomerToFlight(Customer currCustomer, String flightCode, float baggageFee) {
		Flight currFlight = allFlights.get(flightCode);
		try {
			currFlight.addCustomer(currCustomer, baggageFee);
			
			Logger.instance().PassengerCheckedIn(currCustomer, currFlight, deskName, baggageFee);		// Log the customer added to the flight
		} catch (InvalidValueException e) {
			System.out.println("Invalid value of customer baggage details found at Desk/addCustomerToFlight()");
			e.printStackTrace();
		}
	}
	
}
