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

	// Called during startup (reading from file) and with GUI
	// Needs exception
	/**
	 * Checks in a customer.
	 * 
	 * @param Customer object
	 * @param his baggage weight 
	 * @param his baggage volume
	 * @throws AlreadyCheckedInException if customer has been checked in before
	 */
	public void checkIn(Customer c, float weight, float volume) throws AlreadyCheckedInException {
		
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
		GUI g = new GUI(m);
		g.displayPanelStart();
	}

}
