package airlineinterface;

import java.util.HashSet;
import java.util.Iterator;

import exceptions.AlreadyCheckedInException;
import exceptions.InvalidValueException;

public class Flight extends Observable implements Runnable  {
	
	public HashSet<Customer> customers;
	private String startLocation, endLocation, flightCode, carrier;
	private int capacity, closeCheckInTime, checkInTimeRemaining;
	private float maxWeight, maxVolume;
	
	//variables that are useful for the report
	private int currentCapacity;
	private float currentWeight, currentVolume, totalFee;
	
	private enum DepartureState {waiting,check_in_closed}
	private DepartureState flightState = DepartureState.waiting;
	
	@SuppressWarnings("unused")
	private Flight() {}
	
	/**
	 * The Flight object holds all the relevant data of a single flight.
	 * 
	 * @param departure the starting location name
	 * @param destination the finishing location name
	 * @param flightRef the unique flight code
	 * @param carrier the carrier company providing the flight
	 * @param maxPassengers the maximum number of passengers this flight can hold
	 * @param maxWeight the maximum weight of customer luggage this flight can hold
	 * @param maxVolume the maximum volume of customer luggage this flight can hold
	 * @throws InvalidValueException when any of the input values are either empty, null or if the integer values are below zero (except maxPassengers which refuses values less than OR EQUAL TO zero
	 */
	public Flight(String departure, String destination, String flightRef, String carrier, int maxPassengers, float maxWeight, float maxVolume, int closeCheckInTime) throws InvalidValueException
	{
		if (departure == null)	 throw new InvalidValueException("departure must not be null");
		if (destination == null) throw new InvalidValueException("destination must not be null");
		if (flightRef == null) 	 throw new InvalidValueException("flightRef must not be null");
		if (carrier == null) 	 throw new InvalidValueException("carrier must not be null");
		
		if (departure.equals(""))	throw new InvalidValueException("departure must not be empty");
		if (destination.equals("")) throw new InvalidValueException("destination must not be empty");
		if (flightRef.equals("")) 	throw new InvalidValueException("flightRef must not be empty");
		if (carrier.equals("")) 	throw new InvalidValueException("carrier must not be empty");
		
		if (maxPassengers <= 0) 	throw new InvalidValueException("maxPassengers must not be less than or equal to zero");
		if (maxWeight 	   < 0) 	throw new InvalidValueException("maxWeight must not be less than zero");
		if (maxVolume	   < 0) 	throw new InvalidValueException("maxVolume must not be less than zero");
		
		startLocation = departure;
		endLocation = destination;
		flightCode = flightRef;
		this.carrier = carrier;
		capacity = maxPassengers;
		this.maxWeight = maxWeight;
		this.maxVolume = maxVolume;
		this.closeCheckInTime = closeCheckInTime;
		customers = new HashSet<Customer>();
	}
	public void run() {
		checkInTimeRemaining = closeCheckInTime / 1000;
		while (checkInTimeRemaining > 0) {
			checkInTimeRemaining--;
			Simulator.get().nonRandomSleep(1000);
			notifyObservers();
		}
		flightDeparting();
	}
	
	public boolean isFlightWaiting() {
		if(flightState == DepartureState.waiting) {
			return true;
		}else{
			return false;
		}
	}
	
	public void flightDeparting() { 
		flightState = DepartureState.check_in_closed;
		notifyObservers();
	}
	
	public String getCurrentState() {
		switch(flightState) {
			case waiting:
				return "Flight status: Check-in open (" + checkInTimeRemaining + "s).";
			case check_in_closed:
				return "Flight status: Check-in closed.";
			default:
				return "This shouldn't happen. Please advise technical team of the issue.";
		}
	}
	
	/**
	 * Adds a new customer to the flight's passenger list.
	 * 
	 * @param c the new customer to add to the flight
	 * @throws InvalidValueException when the customer's flight code does not match with this flight's code (i.e the customer is trying to check into the wrong flight)
	 */
	public boolean addCustomer(Customer c, float oversizeFee) throws InvalidValueException, AlreadyCheckedInException
	{
		if (!c.getFlightCode().equals(flightCode)) throw new InvalidValueException("customer does not belong to this flight");
		c.setCheckedIn();										//Set customer to being checked in, throwing exception if customer is already checked in
		boolean b = customers.add(c);							//Add customer to TreeSet of customer on flight
		currentCapacity = currentCapacity + 1;					//Add one additional passengers to the flight
		float[] baggageDetails = c.getBaggageDetails();			//Get an array which holds the 2 values: checked in baggage weight and volume
	    currentWeight = currentWeight + baggageDetails[0];		//Add baggage weight to the flight
	    currentVolume = currentVolume + baggageDetails[1];		//Add baggage volume to the flight
	    totalFee += oversizeFee;
	    notifyObservers();
	    return b;
	}
	
	/**
	 * Removes a customer from the flight's passenger list.
	 * 
	 * @param c the customer to remove from the flight
	 * @return e input customer exists in the list, it is removed and this method returns True. If this customer does not exist in the list, this method will take no action and return false.
	 */
	public boolean removeCustomer(Customer c)
	{
		boolean b = customers.remove(c);
		if (b) notifyObservers();
		return b;
	}
	
	/**@return the flight's maximum flight attributes in the following order: maximum number of passengers, the maximum allowed weight of luggage, and the maximum holding volume*/
	public float[] getMaxAttributes() { return new float[] {capacity, maxWeight, maxVolume} ; }
	
	/**@return the flight's current flight attributes of all the customers in the following order: current number of passengers, the total weight of luggage, and the total volume of luggage*/
	public float[] getCustomerSumAttributes()
	{
		float[] details = new float[] { 0, 0, 0};
		Iterator<Customer> iter = customers.iterator();
		while (iter.hasNext())
		{
			Customer c = iter.next();
			if (c.isCheckedIn())
			{
				float[] customerDetails = c.getBaggageDetails();
				details[0] += 1;
				details[1] += customerDetails[0];
				details[2] += customerDetails[1];
			}
		}
		return details;
	}
	
	/**@return a textual representation of the current flight's details: checked-in customers and baggage info*/
	public String toString() {
		String overweight = "No";
		String overCapacity = "No";

		if (maxWeight < currentWeight || maxVolume < currentVolume) overweight="Yes";
		if (currentCapacity > capacity) overCapacity="Yes";
		
		/*for (Customer c : customers) {
			output = output + c.toString() + System.lineSeparator();
		}*/
		return  "Flight Code: " 				+ flightCode 		+ System.lineSeparator() +
				"Departure Location: " 			+ startLocation 	+ System.lineSeparator() +
				"Arrival Location: " 			+ endLocation 		+ System.lineSeparator() +
				"Checked-in customers: " 		+ currentCapacity 	+ "/" + capacity 		 + System.lineSeparator() +
				"Current baggage weight: "  	+ currentWeight 	+ System.lineSeparator() +
				"Current baggage volume: "  	+ currentVolume 	+ System.lineSeparator() +
				"Max baggage weight: "			+ maxWeight 		+ System.lineSeparator() +
				"Max baggage volume: " 			+ maxVolume 		+ System.lineSeparator() + 
				"Over passenger capacity?: " 	+ overCapacity 		+ System.lineSeparator() +
				"Baggage out of bounds?: " 		+ overweight 		+ System.lineSeparator() +
				"Total oversize fee: " 			+ totalFee;
	}
	
	/**@return a two-cell array for the traveling point of the flight: starting location, and finishing location*/
	public String[] getTravelPoints() { return new String[] {startLocation, endLocation}; }
	
	/**@return the name of the company providing the flight*/
	public String getCarrier() { return carrier; }
	
	/**@return the name of the flight code of the flight*/
	public String getFlightCode() { return flightCode; }
}
