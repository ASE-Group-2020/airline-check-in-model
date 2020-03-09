package airlineinterface;

import java.util.HashMap;

import exceptions.AlreadyCheckedInException;
import exceptions.InvalidValueException;

public class Desk {
	private static HashMap<String, Flight> allFlights; // each desk holds a static reference to all flights
	private WaitingQueue queue;
	private boolean deskExists = true;
	
	/* Constructor that takes in a queue object
	*  Will start a method that loops endlessly.
	*  IE: all people have been checked in, it will pause.
	*  Else if will check-in people.
	*/
	public Desk(WaitingQueue queue) {
		this.queue = queue;   //Wrong as you need to interact <- what?

		//TODO: create a conditional statement that kills/pauses the desk. Depends on what garbage collection does? <- No.
		// Conditional statement to keep the desk check-in working.
		while(deskExists) {
			if(queue.hasNext() == true) {
				Customer currCustomer = queue.getNext();
				checkIn(currCustomer, currCustomer.getBaggageDetails()[0], currCustomer.getBaggageDetails()[1]); // Checks in a customer
			}
			else {
				//TODO pause the method until new members are added to the queue.  <- Thread.sleep(2000); or do nothing
			}
		}
	}
	
	/*  takes in the customer at the front of the queue
	 * using getNextCusomer from queue class.
	 * It will loop around whilst the queue has customers.
	 */
	
	// .hasNext returns a boolean if the queue has customers
	private void startCheckIn() {
		timeDelay(); // Order matters, adding a time delay at the start might avoid a lot of headache?
					// Technically checking is there are customer in queue should constantly happen ... 
		if(queue.hasNext()) {
			Customer currCustomer = queue.getNext();
			checkIn(currCustomer, currCustomer.getBaggageDetails()[0], currCustomer.getBaggageDetails()[1]); 
		}
		else {
			//TODO pause the method until new members are added to the queue.  <- Thread.sleep(2000); or do nothing
		}
	}
	private void timeDelay() {
		//TODO creates a time delay thats used on each check in <- Thread.sleep(2000);
	}
	
	private void checkIn(Customer c, float weight, float volume) {
		try {
			c.setCheckedIn(weight,volume);
			addCustomerToFlight(c, c.getFlightCode());
		} catch (AlreadyCheckedInException e) {
			System.out.println("Customer has already been checked in! Desk/CheckIn()");
		} catch (InvalidValueException e) {
			System.out.println("Invalid value passed at Desk/CheckIn().");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public float getOversizeFee(float currentWeight,float currentVolume) throws InvalidValueException {
		if (currentWeight < 15 && currentVolume < 20) {
			return 0;
		}
		else if (currentWeight < 25 && currentVolume < 35) {
			return 10;
		}				
		else if(currentWeight < 45 && currentVolume < 55) {
			return 20;
		}
		else if(currentWeight < 70 && currentVolume < 90) {
			return 40;
		}
		else if(currentWeight < 100 && currentVolume < 120) {
			return 60;
		}
		else if(currentWeight < 150 && currentVolume < 180) {
			return 80;
		}
		else if(currentWeight <= 200 && currentVolume <= 260) {
			return 100;
		}
		// this is the maximum weight and volume an individual is allowed to possess. Beyond 200kg 
		
		//TODO how to handle strange number or values.. ---> you need a formula here to unify the values, what if it has one value low, the other high ??
		else
			throw new InvalidValueException("the baggage shouldn't be more than 200 Kg in weight or 260 L in volume.");
	}
	
	private void addCustomerToFlight(Customer c, String flightCode) {
		Flight currFlight = allFlights.get(flightCode);
		try {
			currFlight.addCustomer( c, getOversizeFee(c.getBaggageDetails()[0], c.getBaggageDetails()[1]) );
		} catch (InvalidValueException e) {
			System.out.println("Invalid value of customer baggage details found at Desk/addCustomerToFlight()");
			e.printStackTrace();
		}
	}
	
	
}
