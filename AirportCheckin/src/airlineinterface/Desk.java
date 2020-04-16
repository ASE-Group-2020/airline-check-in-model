package airlineinterface;

import java.util.HashMap;
import java.util.List;

import exceptions.AlreadyCheckedInException;
import exceptions.InvalidValueException;
import exceptions.ObjectNotFoundException;

public class Desk extends Observable implements Runnable {

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
		WAITING,
		CLOSED
	}
	private Stage action = Stage.WAITING;
	private boolean waitingToClose = false;
	
	public void OpenDesk() { waitingToClose = false; if (action == Stage.CLOSED) action = Stage.WAITING; notifyObservers(); }
	public void CloseDesk() { waitingToClose = true; }
	
	
	/* No need to synchronize this, the HashMap will be made concurrent..? Doesn't need to be concurrent, just reading from it is a-okay. :)
	 */
	public static void addFlights(List<Flight> flights) {
		for (Flight f : flights) {
			allFlights.put(f.getFlightCode(), f);
		}
	}

	/* Constructor that takes in a queue object and the string object is the desk identifier.
	*/
	public Desk(WaitingQueue queue, String deskName) {
		this.queue = queue; 
		this.deskName = deskName;
	}

	/* Implements Runnable, so started with a new Thread(new MyRunnable()).start() call
	 * 
	 * Done. Nah, just check if there's people in the list and queue.
	 * Run one desk method (desk task) per thread. This is the cleanest way to do it, as long as the collections
	 * are thread-safe. The print statements and logger are essential to the work of the program and need to 
	 * happen in order with the operations if we are to be able to measure the program's viability without endless
	 * testing and patches. The potential speed-up from making synchronized calls to individual parts of the methods,
	 * instead of the whole method is negligible.
	 */ 
	public void run() {
		Logger.instance().MainLog("Starting simulation of " + deskName);

		// While (queue OR list are NOT empty) and (enable is turned on) i.e the terminal is working ... do ...
		// while ( (!queue.getNotArrived().isEmpty() || !queue.getWaiting().isEmpty()) && enable ) { <- OLD WAY, we want the desks waiting now should we add more customers later
		while (enable) {
			//System.out.println(deskName + " tick");
			if (waitingToClose)
			{
				action = Stage.CLOSED;
				currCustomer = null;
				notifyObservers();
				waitingToClose = false;
			}
			if (action == Stage.CLOSED)
			{
				Simulator.sleep(1000); if (!enable) break;
				continue;
			}
			//System.out.println(System.currentTimeMillis() + " 1 " + deskName);																// Returns null customer object is queue is empty
			currCustomer = queue.getNext();										// this is the current customer the desk is working with
			//System.out.println(System.currentTimeMillis() + " 2 " + deskName);
			if (currCustomer != null) { 											// If a customer exists in the queue, get them...
				
				action = Stage.GETTING_CUSTOMER;
				Logger.instance().PassengerMovedToDesk(currCustomer, deskName);
				notifyObservers();
				Simulator.sleep(9000); if (!enable) break; 									// 9 second delay for person to move to help desk and calculate fee
				try {
					action = Stage.CALCULATING_FEE;
					float currCustomerFee = getOversizeFee(currCustomer.getBaggageDetails()[0],			// Calculate oversize fees
														currCustomer.getBaggageDetails()[1]); 			// ...and set respective action in the method
					notifyObservers();
					
					Simulator.sleep(3000); if (!enable) break; 											// 3 seconds to confirm check in and leave desk
					checkIn(currCustomer, currCustomerFee); 											// Check in the customer
					Logger.instance().MainLog("Checked in: " + currCustomer.getFirstName() + " " + currCustomer.getLastName());
					notifyObservers();
					
					Simulator.sleep(3000);
				}
				catch (InvalidValueException e) {
					Logger.instance().MainLog(" ##DESK##  The " + deskName + " has reported the following error: " + e.getMessage());
				}
			}
			else {														// If not, wait
				action = Stage.WAITING;
				//System.out.println(deskName + " currently waiting for a customer");
				notifyObservers();
				Simulator.sleep(2000);
			}
		}
		action = Stage.CLOSED;
		currCustomer = null;
		notifyObservers();
		Logger.instance().MainLog(" ##DESK##  The " + deskName + " has been shut down.");
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
			case CLOSED:
				return "Desk action: Closed.";
			default:
				return "Desk action: Not set.";
		}
	}

	private void checkIn(Customer currCustomer, float baggageFee) {
		try {
			action = Stage.CHECKING_IN;
			addCustomerToFlight(currCustomer, currCustomer.getFlightCode(), baggageFee);			// Add customer to their selected flight
			currCustomer.setCheckedIn(); 															// Change boolean flag in customer object			
			// Log that the customer has finished checking in.
		} 
		catch (AlreadyCheckedInException e) {System.out.println("Customer has already been checked in! Desk/CheckIn()");} 
		catch (Exception e) { System.err.println("DEBUG: Unknown error in Desk.checkIn"); e.printStackTrace();}
	}

	public static float getOversizeFee(float currentWeight, float currentVolume) throws InvalidValueException {
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

	private void addCustomerToFlight(Customer currCustomer, String flightCode, float baggageFee) {
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
