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
	public boolean enable = true;			// Connect to terminal to control opening/closing desks via setter method
	private WaitingQueue queue;
	private String deskName;
	private Customer currCustomer;			// The current customer at the desk
	private Flight currCustomerFlight;		// Flight of the current customer
	private float currCustomerFee;			// Baggage fee of the current customer
	private enum Stage {
		GETTING_CUSTOMER,					// Get customer from the queue
		CHECKING_CUSTOMER_DETAILS,			// Ensure flight exists and is accepting check-ins
		MEASURING_BAG,						// Check bag dimensions are acceptable
		PRINTING_BOARDING_PASS,				// Print pass
		CHARGING_FEE,						// Charge customer
		REJECTING_CUSTOMER,					// Customer not accepted
		CLOSED								// Not accepting anyone
	}
	private Stage currentStage = Stage.GETTING_CUSTOMER;
	private boolean waitingToClose = false;
	
	public void OpenDesk() { waitingToClose = false; if (currentStage == Stage.CLOSED) currentStage = Stage.GETTING_CUSTOMER; notifyObservers(); }
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
	
	private void stageGetCustomer() {
		if (waitingToClose) {												// First check if desk should close
			currentStage = Stage.CLOSED;
			return;
		}
		Simulator.get().sleep(1000);
		currCustomer = queue.getNext();										// Customer joins from queue
		if (currCustomer != null)											// If the customer is null don't change stage
			currentStage = Stage.CHECKING_CUSTOMER_DETAILS;
	}
	private void stageCheckCustomerDetails() {
		Simulator.get().sleep(2000);
		currCustomerFlight = allFlights.get(currCustomer.getFlightCode());	// Get customer flight
		if (currCustomerFlight == null) {									// Make sure flight exists
			currentStage = Stage.REJECTING_CUSTOMER;						// Customer rejected
			Logger.instance().MainLog(deskName + ": " + currCustomer.toString() + " rejected; flight " + currCustomer.getFlightCode() + " not found.");
			return;
		}
		if (!currCustomerFlight.isFlightWaiting()) {
			currentStage = Stage.REJECTING_CUSTOMER;
			Logger.instance().MainLog(deskName + ": Customer rejected; flight not boarding.");
			return;
		}
		currentStage = Stage.MEASURING_BAG;
	}
	private void stageMeasureBag() {
		Simulator.get().sleep(4000);
		currCustomerFee = 0;												// Just in case some data is carried over
		try {
			currCustomerFee = getOversizeFee(currCustomer.getBaggageDetails());
		} catch (InvalidValueException e) {
			currentStage = Stage.REJECTING_CUSTOMER;
			Logger.instance().MainLog(deskName + ": " + currCustomer.toString() + " rejected; baggage too large");
			return;
		}
		currentStage = (currCustomerFee == 0) ? Stage.PRINTING_BOARDING_PASS : Stage.CHARGING_FEE;
	}
	private void stagePrintBoardingPass() {
		Simulator.get().sleep(4000);
		try {
			currCustomerFlight.addCustomer(currCustomer, currCustomerFee);
			Logger.instance().PassengerCheckedIn(currCustomer, currCustomerFlight, deskName, currCustomerFee);
		} catch (InvalidValueException e) {			// Flight code incorrect - shouldn't occur
			currentStage = Stage.REJECTING_CUSTOMER;
			Logger.instance().MainLog(deskName + ": " + currCustomer.toString() + " rejected; customer has flight code " 
			+ currCustomer.getFlightCode() + ", expected " + currCustomerFlight.getFlightCode() + ".");
			return;
		} catch (AlreadyCheckedInException e) {		// Customer already checked in - also shouldn't occur
			currentStage = Stage.REJECTING_CUSTOMER;
			Logger.instance().MainLog(deskName + ": " + currCustomer.toString() + " rejected; already checked in.");
			return;
		}
		currCustomer = null;						// Shouldn't strictly be needed, here just in case
		currentStage = Stage.GETTING_CUSTOMER;
	}
	private void stageChargeFee() {
		// Fee is charged in stagePrintBoardingPass, just wait
		Simulator.get().sleep(4000);
		currentStage = Stage.PRINTING_BOARDING_PASS;
	}
	private void stageRejectCustomer() {
		// Customer is turned away, just wait
		Simulator.get().sleep(1000);
		currCustomer = null;
		currentStage = Stage.GETTING_CUSTOMER;
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
		while(enable || (currentStage != Stage.GETTING_CUSTOMER && currentStage != Stage.CLOSED)) {		// Only exit if not handling a customer
			notifyObservers();
			switch(currentStage) {
			case GETTING_CUSTOMER:
				stageGetCustomer();
				break;
			case CHECKING_CUSTOMER_DETAILS:
				stageCheckCustomerDetails();
				break;
			case MEASURING_BAG:
				stageMeasureBag();
				break;
			case PRINTING_BOARDING_PASS:
				stagePrintBoardingPass();
				break;
			case CHARGING_FEE:
				stageChargeFee();
				break;
			case REJECTING_CUSTOMER:
				stageRejectCustomer();
				break;
			case CLOSED:
				// Nothing to do
				break;
			}
		}
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
		switch(currentStage) {
			case GETTING_CUSTOMER:
				return "Desk action: Waiting for customer.";
			case CHECKING_CUSTOMER_DETAILS:
				return "Desk action: Checking customer details.";
			case MEASURING_BAG:
				return "Desk action: Measuring bag.";
			case PRINTING_BOARDING_PASS:
				return "Desk action: Printing boarding pass.";
			case CHARGING_FEE:
				return "Desk action: Charging baggage fee.";
			case REJECTING_CUSTOMER:
				return "Desk action: Rejecting customer.";
			case CLOSED:
				return "Desk action: Closed.";
			default:
				return "Desk action: Unknown.";
		}
	}
	
	public String getDeskName() {
		return deskName;
	}
	
	/*	Customer baggage details takes a float array, the 0th element is the weight and 1,2,3 are the X,Y,Z for the volume
	 */
	public static float getOversizeFee(float[] customerBaggage) throws InvalidValueException {
		float weight = customerBaggage[0];
		float volume = customerBaggage[1]*customerBaggage[2]*customerBaggage[3];
		
		if (weight < 15 && volume < 20) {
			return 0;
		} else if (weight < 25 && volume < 35) {
			return 10;
		} else if (weight < 45 && volume < 55) {
			return 20;
		} else if (weight < 70 && volume < 90) {
			return 40;
		} else if (weight < 100 && volume < 120) {
			return 60;
		} else if (weight < 150 && volume < 180) {
			return 80;
		} else if (weight <= 200 && volume <= 260) {
			return 100;
		}
		// This is the maximum weight and volume an individual is allowed to possess, beyond 200kg

		else throw new InvalidValueException("the baggage shouldn't be more than 200kg in weight or 260 litres in volume.");
	}
	
}
