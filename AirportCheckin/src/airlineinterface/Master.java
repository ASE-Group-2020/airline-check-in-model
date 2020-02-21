package airlineinterface;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import exceptions.AlreadyCheckedInException;
import exceptions.InvalidValueException;

public class Master {
	private HashMap<String, Flight> allFlights;
	private HashMap<String, Customer> allCustomers;

	/**
	 * The Master object is responsible for managing all Flights and Customers, 
	 * reading them from files, checking in and adding them to HashMaps.
	 * It works together with the GUI class to display an interface for the app.
	 */
	public Master() {
	}
	
	public Customer getCustomer(String customerCode, String customerLastName) {
		System.out.println("In getCustomer");
		for(Customer C : allCustomers.values())
		{
			String currentRefCode = C.getRefCode();
			System.out.println("the checked refCode: " + customerCode + "\t the RefCode of the customer in the HashMap: " + currentRefCode  );
			String currentLastName = C.getLastName(); 
			System.out.println("the checked LastName: " + customerLastName + "\t the LastName of the customer in the HashMap: " + currentLastName  );
			//Check that the current customer matches the last name and refCode that's entered 
			if(currentRefCode.equals(customerCode) && currentLastName.equals(customerLastName)) {
				return C;
			}
		}
		// if there are no matches return null
		return null; 
	}
	
	// Called during startup (reading from file) and with GUI
		// Needs exception
		/* The GUI communicates to the master with m.checkIn(...),  which communicates to the Flight object f.addCustomer(customer).
		 * It could be simpler to communicate directly from the GUI to the Flight obj. But this would be detrimental to code cohesiveness
		 * by adding more coupling between classes. 
		 */	
	/**
	 * Checks in a customer.
	 * 
	 * @param Customer object
	 * @param his baggage weight 
	 * @param his baggage volume
	 * @throws AlreadyCheckedInException if customer has been checked in before
	 */
	public void checkIn(Customer c, Flight f, float weight, float volume) throws InvalidValueException {
		f.addCustomer(c);
	}
	
	//We return the oversize and overweight fee as a float. It's calculated with a small flat fee + (current - booked) x 2  
	public float getOversizeFee(Customer currentCustomer, float currentWeight,float currentVolume) {
		if (currentCustomer.getWeight() < currentWeight && currentCustomer.getVolume() < currentVolume){
			System.out.println("case 1");
			System.out.println("#################################################################################################");
			System.out.println("the current customer is: " + currentCustomer.getFirstName() + " " + currentCustomer.getLastName());
			System.out.println("the current baggageWeight is: " + currentWeight + "\t the current baggageVolume is: " + currentVolume);
			System.out.println("the getWeight baggageWeight is: " + currentCustomer.getWeight() + "\t the getVolume baggageVolume is: " + currentCustomer.getVolume());
			System.out.println("#################################################################################################");
			return (currentWeight-currentCustomer.getWeight())*2+(currentVolume-currentCustomer.getVolume())*2+5;
		}
		else if (currentCustomer.getWeight() < currentWeight) {
			return (currentWeight-currentCustomer.getWeight())*2+5;
		}				
		else if(currentCustomer.getVolume() < currentVolume){
			return (currentVolume-currentCustomer.getVolume())*2+5;
		}
		else {
			return 0; //It should never get here 
		}
		
	}
	
	//JUST USED FOR TESTING
	public void addObjectsMaps() throws InvalidValueException {
		/*
		 * Creation of some dummy customers to test the code, can be deleted later
		 */	
		//String _code, String _firstName, String _lastName, String _flightCode, boolean _booked, float _weight, float _volume
		allCustomers.put("123", new Customer("123", "Adam", "Smith", "LON123", false, 5,  5));
		allCustomers.put("456", new Customer("456", "Marie", "Curie", "LON123", false, 15,  1));
		
		/*
		 * Creation of some dummy flights to test the code, can be deleted later
		 */
		//String departure, String destination, String flightRef, String carrier, int maxPassengers, float maxWeight, float maxVolume
		allFlights.put("LON123" , new Flight("LON", "EDI", "LON123", "RayanAir", 300, 10000, 300000));
		
	}

	/**@param Flight to add to HashMap
	 * @throws InvalidValueException if flight code is empty
	 */
	public void addFlight(Flight f) throws InvalidValueException {
		if (f.getFlightCode().equals(null) || f.getFlightCode().contentEquals("")) 
			throw new InvalidValueException("flight code");
		else allFlights.put(f.getFlightCode(), f);
	}

	
	/** @param Flight to remove from HashMap. Does nothing if specified Flight doesn't exist. */
	public void removeFlight(Flight f) {
		allFlights.remove(f.getFlightCode(), f);
	}
	

	public void addFlightsFromFile(String filePath) {
		try { 														// open input stream
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			String line = ""; 										// store current line
			
			while ((line = reader.readLine()) != null) { 			// go through every line in the file
				String[] flightDetails = line.trim().split(","); 	// split the line and trim empty space, push results to small array
				Flight currFlight = new Flight(flightDetails[0], 
						flightDetails[1], flightDetails[2], 
						flightDetails[3],
						Integer.parseInt(flightDetails[4]), 
						Float.parseFloat(flightDetails[5]),
						Float.parseFloat(flightDetails[6])); 		// one-liner to initialize Flight object with data from current file line
				allFlights.put(flightDetails[2], currFlight); 		// the key is the unique flight id (flight code), value is currFlight being added
			}

			reader.close(); // close reader
		} catch (Exception e) {
			if (e instanceof FileNotFoundException)
				System.out.println("Error: File not found.");
			else if (e instanceof IOException)
				System.out.println("Error: I/O error.");
			else {
				System.out.println("General error! Give the following information to the devs...");
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param filePath
	 */
	public void addCustomersFromFile(String filePath) {
		try { 														// open input stream
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			String line = ""; 										// store current line
			
			while ((line = reader.readLine()) != null) { 			// go through every line in the file
				String[] customerDetails = line.trim().split(","); 	// split the line and trim empty space, push results to small array
				Customer currCustomer = new Customer(customerDetails[0], 
						customerDetails[1], customerDetails[2], 
						customerDetails[3],
						Boolean.parseBoolean(customerDetails[4]), 
						Float.parseFloat(customerDetails[5]),
						Float.parseFloat(customerDetails[6])); 		// one-liner to initialize Customer object with data from current file line
				allCustomers.put(customerDetails[3], currCustomer); // the key is the unique customer reservation id (flight code), value is currCustomer being added
			}

			reader.close(); // close reader
		} catch (Exception e) {
			if (e instanceof FileNotFoundException)
				System.out.println("Error: File not found.");
			else if (e instanceof IOException)
				System.out.println("Error: I/O error.");
			else {
				System.out.println("General error! Give the following information to the devs...");
				e.printStackTrace();
			}
		}
	}

	// Output info for each flight somehow
	public void display() {
	}

	public static void main(String[] args) {
		Master m = new Master();
		
		try {
			m.addObjectsMaps(); //USED FOR TESTING
		}
		catch(InvalidValueException e) {System.out.println("INVALID VALUE FOUND AT TESTING OBJ MAPS (DAVID)");}
		
		GUI g = new GUI(m);
		g.displayPanelStart();
	}

}
