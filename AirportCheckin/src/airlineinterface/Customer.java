package airlineinterface;

import java.util.EnumSet;
import java.util.Iterator;

import exceptions.*;

public class Customer implements Comparable<Customer> {
	
	// customer details
	private String refCode, firstName, lastName, flightCode;
	private boolean isCheckedIn = false;
	private float baggageWeight, volX, volY, volZ;
	
	private enum BoardingClass { Invalid, Standard, Business}
	private BoardingClass customerClass = BoardingClass.Invalid;
	
	@SuppressWarnings("unused")
	private Customer() {}
	
	/**
	 * The Customer object holds all the relevant data of a single customer.
	 * 
	 * @param _code customer's unique reference code
	 * @param _firstName customer's first name
	 * @param _lastName customer's last name
	 * @param _flightCode flight code unique to each individual flight
	 * @param _booked has the customer been checked in yet
	 * @param _weight weight of the customer's baggage
	 * @param _volume volume of the customer's baggage
	 * @throws InvalidValueException when any of the input values are either empty, null or if the integer values are below zero
	 */
	public Customer(String _code, String _firstName, String _lastName, String _flightCode, String customerClass, float _weight, float _volumeX, float _volumeY, float _volumeZ) throws InvalidValueException
	{
		// ensures all input data is valid
		if (_code == null)		  	throw new InvalidValueException("_code must not be null");
		if (_firstName == null)    	throw new InvalidValueException("_firstName must not be null");
		if (_lastName == null)	   	throw new InvalidValueException("_lastName must not be null");
		if (_flightCode == null)  	throw new InvalidValueException("_flightCode must not be null");
		if (_code.equals(""))	   	throw new InvalidValueException("_code must not be empty");
		if (_firstName.equals("")) 	throw new InvalidValueException("_firstName must not be empty");
		if (_lastName.equals(""))  	throw new InvalidValueException("_lastName must not be empty");
		if (_flightCode.equals(""))	throw new InvalidValueException("_flightCode must not be empty");
		if (_weight < 0) 			throw new InvalidValueException("_weight must not be null");
		if (_volumeX < 0) 			throw new InvalidValueException("_volumeX must not be null");
		if (_volumeY < 0) 			throw new InvalidValueException("_volumeY must not be null");
		if (_volumeZ < 0) 			throw new InvalidValueException("_volumeZ must not be null");
		
		// iterates through list of boarding classes and finds the one specified in constructor argument
		Iterator<BoardingClass> iter = EnumSet.allOf(BoardingClass.class).iterator();
		while (iter.hasNext())
		{
			BoardingClass c = iter.next();
			if (c.equals(BoardingClass.Invalid)) continue;
			else if (c.toString().equalsIgnoreCase(customerClass)) 
			{
				this.customerClass = c;
				break;
			}
		}
		if (this.customerClass == BoardingClass.Invalid) throw new InvalidValueException("unrecognised boarding class");
		
		refCode = _code;
		firstName = _firstName;
		lastName = _lastName;
		flightCode = _flightCode;
		baggageWeight = _weight;
		volX = _volumeX;
		volY = _volumeY;
		volZ = _volumeZ;
	}
	
	/**@return customer's seating class*/
	public String getSeatingClass() { return customerClass.toString(); }
	
	/**
	 * Checks in the customer and sets their baggage weight and volume values.
	 * 
	 * @param _weight weight of the customer's baggage
	 * @param _volume volume of the customer's baggage
	 * @throws AlreadyCheckedInException if the customer has already been checked into the system 
	 * @throws InvalidValueException if the input weight or volume values are invalid (similar to the customer constructor check)
	 */
	public void setCheckedIn() throws AlreadyCheckedInException
	{
		if (isCheckedIn) throw new AlreadyCheckedInException(firstName + " " + lastName);
		else isCheckedIn = true;
	}
	
	/**@return if the customer has been booked in yet*/
	public boolean isCheckedIn() { return isCheckedIn; }
	
	/**@return the customer's first name*/
	public String getFirstName() { return firstName; }
	
	/**@return the customer's last name*/
	public String getLastName() { return lastName; }
	
	/**@return the customer's flight code*/
	public String getFlightCode() { return flightCode; }
	
	/**@return the customer's unique reference code*/
	public String getRefCode() { return refCode; }
	
	/**@return a string representation of the customer's ref code and names*/
	public String toString() {return refCode + " " + firstName + " " + lastName;}

	/**@return a string describing the customer's baggage dimensions*/
	public String getBaggageDimensionString() {
		// Volumes are stored in decimetres instead of centimetres
		// Convert to cm for display
		return String.format("%.0fx%.0fx%.0f cm", volX*10+0.5, volY*10+0.5, volZ+0.5 );
	}
	
	/**@return a string describing the customer's baggage weight*/
	public String getBaggageWeightString() { return String.format("%.2f kg", baggageWeight); }
	
	/**@return an array of the following attributes (in order):
	 * weight of customer's baggage
	 * volume of customer's baggage
	 * */
	public float[] getBaggageDetails() { return new float[] {baggageWeight, volX, volY, volZ}; }
	
	/**Compares two customers by their reference codes (which should be unique)*/
	public int compareTo(Customer other) { return refCode.compareTo(other.getRefCode()); }
}
