package airlineinterface;

import java.util.HashMap;
import java.util.List;

import exceptions.AlreadyCheckedInException;
import exceptions.InvalidValueException;

public class Desk implements Runnable {
	
	// Statics
	private static HashMap<String, Flight> allFlights = new HashMap<String, Flight>(); // each desk holds a static reference to all flights
	
	public static void addFlights(List<Flight> flights) {
		for (Flight f : flights) {
			allFlights.put(f.getFlightCode(), f);
		}
	}
	
	// Instance-specific
	private WaitingQueue queue;
	private boolean deskExists = true;
	private String deskName; 
	
	/* Constructor that takes in a queue object
	*  and the sting object is the desk identifier.
	*/
	public Desk(WaitingQueue queue, String deskName) {
		this.queue = queue;   //Will the queue update after desk initialisation or is it static? Additionalu how to deal with multiple desks trying to grab the same customer.
		this.deskName = deskName;
		
	}
	// this method extends thread. objectDesk.start()will begin this thread
	public void run() {
	Logger.instance().log("Starting simulation of " + deskName);
	//TODO: create a conditional statement that kills/pauses the desk. Do we want a deconstructor? 
			// Conditional statement to keep the desk check-in working.
			while(deskExists) {
				Customer c = queue.getNext(); // Returns null customer object is queue is empty
				if(!(c == null)) {  //TODO depends on the queue object how we check if it isn't empty 
					Logger.instance().log(" ##DESK##  A new customer " + c.getFirstName()+" "+
				c.getLastName()+" has moved from the queue to checkin at " + deskName);
					timeDelay(3000); // 3 second delay for pesron to move to help desk
					try { startCheckIn(queue,c);} catch (InvalidValueException e) {System.out.println(e.getMessage());} 
				}
				else {
					try {
					Thread.sleep(2000); 
					}catch(InterruptedException e) {
						System.out.println(e.getMessage());
					}
				
				}
			}
			Logger.instance().log(" ##DESK##  The "+ deskName+" has stopped accepting customers.");
	}
	
	/* Takes in the customer at the front of the queue
	 * using .peek(), else TODO a custom method from queue class.
	 */
	private void startCheckIn(WaitingQueue queue, Customer currCustomer) throws InvalidValueException {
		//TODO add randomness to speeds
		
		timeDelay(6000); // 6 second delay for person to get baggage fee
		float currCustomerFee = getOversizeFee(currCustomer.getBaggageDetails()[0],currCustomer.getBaggageDetails()[1]); // Get the bagge fee of the first customer
		// Log the customers baggage fee
		Logger.instance().log(" ##DESK##  The baggage fee of "+currCustomerFee+ " was collected from " + currCustomer.getFirstName()+" "+
				currCustomer.getLastName()+" at "+ deskName);
		timeDelay(3000); // 3 seconds to confirm check in and leave desk;
		checkIn(currCustomer, currCustomerFee); // Checks in a customer
	}
	private void timeDelay(int milisec) {
		try {
			Thread.sleep(milisec); 
			}catch(InterruptedException e) {
				System.out.println(e.getMessage() + " failed to interupt thread for "+ milisec+" miliseconds.");
			}
	}
	
	private void checkIn(Customer currCustomer,float baggageFee) {
		try {
			addCustomerToFlight(currCustomer, currCustomer.getFlightCode(), baggageFee); //add customer to their selected flight 
			currCustomer.setCheckedIn(); //Change boolean flag in customer object
			// Log that the customer has finished checking in.
			Logger.instance().log(" ##DESK##  "+ currCustomer.getFirstName()+" "+
					currCustomer.getLastName()+" has finished checking in");
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
	
	private void addCustomerToFlight(Customer currCustomer, String flightCode, float baggageFee) {
		Flight currFlight = allFlights.get(flightCode);
		try {
			currFlight.addCustomer( currCustomer,baggageFee);
			// Log that the customer has been added to the flight
			Logger.instance().log(" ##DESK##  "+ currCustomer.getFirstName()+" "+
					currCustomer.getLastName()+" has been added to "+ currFlight.getFlightCode());
		} catch (InvalidValueException e) {
			System.out.println("Invalid value of customer baggage details found at Desk/addCustomerToFlight()");
			e.printStackTrace();
		}
		
	}
	
	
}
