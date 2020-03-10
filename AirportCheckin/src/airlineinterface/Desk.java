package airlineinterface;

import java.util.HashMap;
import exceptions.AlreadyCheckedInException;
import exceptions.InvalidValueException;
import java.lang.Thread;

public class Desk extends Thread{
	private static HashMap<String, Flight> allFlights; // each desk holds a static reference to all flights
	private WaitingQueue queue;
	private boolean deskExists = true;
	
	/* Constructor that takes in a queue object
	*  Will start a method that loops endlessly.
	*  IE: all people have been checked in, it will pause.
	*  Else if will check-in people.
	*/
	public Desk(WaitingQueue queue) {
		this.queue = queue;   //Will the queue update after desk initialisation or is it static? Additionalu how to deal with multiple desks trying to grab the same customer.

		
	}
	// this method extends thread. objectDesk.start()will begin this thread
	public void run() {
	//TODO: create a conditional statement that kills/pauses the desk. Do we want a deconstructor? 
			// Conditional statement to keep the desk check-in working.
			while(deskExists) {
				if(queue.hasNext() == true) {  //TODO depends on the queue object how we check if it isn't empty 
					startCheckIn(queue); // method that handles checking in customers in queue
				}
				else {
					try {
					sleep(2000); 
					}catch(InterruptedException e) {
						
					}
				
				}
			}
	}
	
	/* Takes in the customer at the front of the queue
	 * using .peek(), else TODO a custom method from queue class.
	 */
	// .hasNext returns a boolean if the queue has customers
	private void startCheckIn(WaitingQueue queue) {
		//TODO add randomness to speeds
		sleep(8000); // 8 second delay for peson to move to help desk
		Customer currCustomer = queue.peek(); // Get the first customer in the queue
		sleep(15000); // 15 second delay for peson to get baggae fee
		float currCustomerFee = getOversizeFee(currCustomer.getWeight,currCustomer.getVolume) // Get the bagge fee of the first customer
		sleep(8000); // 8 second delay for peson to confirm checkin and move out the way
		checkIn(currCustomer, currCustomerFee); // Checks in a customer
	}
	private void timeDelay() {
		//TODO creates a time delay thats used on each check in <- Thread.sleep(2000);
	}
	
	private void checkIn(Customer c,float fee) {
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
		// Look at Junit test to see if getOversizeFee can handle if one value is low and the other is high.
		// FYI, it does.
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
