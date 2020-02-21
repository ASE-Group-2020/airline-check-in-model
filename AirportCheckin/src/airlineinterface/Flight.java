package airlineinterface;

import java.util.Iterator;
import java.util.TreeSet;

import exceptions.InvalidValueException;

public class Flight {
	
	private TreeSet<Customer> customers;
	private String startLocation, endLocation, flightCode, carrier;
	private int capacity;
	private float maxWeight, maxVolume;
	
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
	public Flight(String departure, String destination, String flightRef, String carrier, int maxPassengers, float maxWeight, float maxVolume) throws InvalidValueException
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
		customers = new TreeSet<Customer>();
	}
	
	/**
	 * Adds a new customer to the flight's passenger list.
	 * 
	 * @param c the new customer to add to the flight
	 * @return if the input customer is a new entry, this method returns True. If this customer does already exist, this method will take no action and return false.
	 * @throws InvalidValueException when the customer's flight code does not match with this flight's code (i.e the customer is trying to check into the wrong flight)
	 */
	public boolean addCustomer(Customer c) throws InvalidValueException
	{
		if (!c.getFlightCode().equals(flightCode)) throw new InvalidValueException("customer does not belong to this flight");
		return customers.add(c);
	}
	
	/**
	 * Removes a customer from the flight's passenger list.
	 * 
	 * @param c the customer to remove from the flight
	 * @return e input customer exists in the list, it is removed and this method returns True. If this customer does not exist in the list, this method will take no action and return false.
	 */
	public boolean removeCustomer(Customer c) { return customers.remove(c); }
	
	/**@return the flight's maximum flight attributes in the following order: maximum number of passengers, the maximum allowed weight of luggage, and the maximum holding volume*/
	public float[] getMaxAttributes() { return new float[] {capacity, maxWeight, maxVolume} ; }
	
	/**@return the flight's current flight attributes of all the customers in the following order: current number of passengers, the total weight of luggage, and the total volume of luggage*/
	public float[] getCustomerSumAttributes()
	{
		float[] details = new float[] { customers.size(), 0, 0};
		Iterator<Customer> iter = customers.iterator();
		while (iter.hasNext())
		{
			float[] customerDetails = iter.next().getBaggageDetails();
			details[1] += customerDetails[0];
			details[2] += customerDetails[1];
		}
		return details;
	}
	
	/**@return the flight code of the current flight*/
	public String getFlightCode() { return flightCode; }
	
	/**@return a two-cell array for the travelling point of the flight: starting location, and finishing location*/
	public String[] getTravelPoints() { return new String[] {startLocation, endLocation}; }
	
	/**@return the name of the company providing the flight*/
	public String getCarrier() { return carrier; }
}
