package airlineinterface;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
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
		// declare the HashMaps in the constructor else you can get null pointer exception
		allFlights = new HashMap<String, Flight>();
		allCustomers = new HashMap<String, Customer>();
	}
	
	
	/**
	 * @return the allCustomers HashMap's size
	 */
	public int getCustomerDatasetSize() {
		return allCustomers.size();
	}
	
	/* Method which returns a Flight object by searching the
	*  allflights HashMap for the flight with a specific flightCode.
	*/ 
	public Flight getFlight(String flightCode) {
		 // this if statement allows programmers to handle no-existing or wrong flight codes.
		 if(allFlights.containsKey(flightCode)) return allFlights.get(flightCode);
		 else {
			 return null; //if returns null the GUI will handle the exception   
		 }
	}
	
	/* Method which returns a Customer object by searching the
	*  allCustomers HashMap for the flight with a specific flightCode.
	*/ 
	public Customer getCustomer(String customerCode, String customerLastName) throws InvalidValueException {
		// this if statement allows programmers to handle non-existing or wrong customerCode.
		for(char c : customerCode.toCharArray()) {
		    if(!Character.isDigit(c)) {
		        throw new InvalidValueException("The reference code of a customer must contain only numbers.");
		    }
		}
		 // this if statement allows programmers to handle no-existing or wrong customer name.
	    for(char c2 : customerLastName.toCharArray()) {
		    if(!Character.isLetter(c2)) {
		        throw new InvalidValueException("The customer's last name must contain only letters.");
		    }
		}
		if(allCustomers.containsKey(customerCode)){
			 	Customer C = allCustomers.get(customerCode);
				/* Check that the current customer has entered a last name and refCode that matches
			 	*  This is an extra layer of security
			 	*/
			 	// we compare the String of the method input the a String from a field in Customer.
				if(C.getLastName().toLowerCase().equals(customerLastName.toLowerCase())) { 
					// the .toLowerCase() is there ^ because we don't care about case-sensitivity. This makes the comparisant easier.
					System.out.println("getCustomer in Master - last name is " + C.getLastName());
					return C; 
				}
				else {
					//TODO 
					System.out.println("getCustomer in Master - customer doesnt match refcode");
					return null; // deals with the customer having a name that doesn't match the entered referenceCode 
				}
		 }	
		 else {
			 //TODO
			 System.out.println("getCustomer in Master - refcode not in hashmap");
			 return null; // deals with the reference code not existing in the hashmap
		 }
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
	public void checkIn(Customer c, Flight f, float weight, float volume) throws InvalidValueException, AlreadyCheckedInException {
		c.setCheckedIn(weight,volume);
		f.addCustomer(c);
	}
	
	// Look for the weight/volume bracket your object fits in. This is a pretty odd way to do Oversize fees..
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
		else if(currentWeight < 100 && currentVolume < 120) {
			return 40;
		}
		else if(currentWeight < 150 && currentVolume < 180) {
			return 60;
		}
		else if(currentWeight < 200 && currentVolume < 260) {
			return 80;
		}
		// this is the maximum weight and volume an individual is allowed to possess. Beyond 200kg 
		
		//TODO how to handle strange number or values.. ---> you need a formula here to unify the values, what if it has one value low, the other high ??
		else
			throw new InvalidValueException("the baggage shouldn't be more than 200 Kg in weight or 260 L in volume.");
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
	

	/** A method that adds flights info from a csv file. The fields are as follows: source, destination, 
	 * fright ref code, company, max amount of passengers, max bagage weight, max baggage volume 
	 * @param the path to the .csv file
	 */
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
			if (e instanceof FileNotFoundException) {
				System.err.println("Error: Flight info file not found. Exiting...");
				System.exit(0);
			}
			else if (e instanceof IOException)
				System.err.println("Error: I/O error.");
			else {
				System.err.println("General error! Give the following information to the devs...");
				e.printStackTrace();
			}
		}
	}
	
	public void addCustomer(Customer C) throws InvalidValueException {
		if (C.getRefCode().equals(null) || C.getRefCode().contentEquals("")) 
			throw new InvalidValueException("flight code");
		else allCustomers.put(C.getRefCode(), C);
	}

	
	/** @param Flight to remove from HashMap. Does nothing if specified Flight doesn't exist. */
	public void removeCustomer(Customer C) {
		allCustomers.remove(C.getRefCode(), C);
	}

	/** A method that adds customer info from a csv file. The fields are as follows: customer code, first name, 
	 * last name, flight code, check in status, baggage weight, baggage volume 
	 * @param the path to the .csv file
	 */
	public void addCustomersFromFile(String filePath) {
		try { 														// open input stream
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			String line = ""; 										// store current line
			
			while ((line = reader.readLine()) != null) { 			// go through every line in the file
				String[] customerDetails = line.trim().split(","); 	// split the line and trim empty space, push results to small array
				//System.out.println(Arrays.toString(customerDetails)); <- debug
				if(customerDetails.length == 7) {					// handle partial data - only take data from full fields, ignore partial data!	
					Customer currCustomer = new Customer(customerDetails[0], 
							customerDetails[1], customerDetails[2], 
							customerDetails[3],
							Boolean.parseBoolean(customerDetails[4]), 
							Float.parseFloat(customerDetails[5]),
							Float.parseFloat(customerDetails[6])); 	// one-liner to initialize Customer object with data from current file line
					allCustomers.put(customerDetails[0].toString(), currCustomer); // the key is the unique customer reservation id (flight code), value is currCustomer being added
				}
				else {
					System.err.println("Corrupted data found at line" + Arrays.toString(customerDetails) + "! Aborting...");
					break;											// break at corrupted data, let someone know that it's corrupted!
				}
			}
			
			reader.close(); // close reader
		} catch (Exception e) {
			if (e instanceof FileNotFoundException) {
				System.err.println("Error: Customer info file not found. Exiting...");
				System.exit(0);
			}
			else if (e instanceof IOException)
				System.err.println("Error: I/O error.");
			else {
				System.err.println("General error! Give the following information to the devs...");
				e.printStackTrace();
			}
		}
	}

	// Output info for each flight somehow
	public void display() {
	}

	public static void main(String[] args) {
		Master m = new Master();
		m.addFlightsFromFile("dataFlight.csv"); // put files in main folder
		m.addCustomersFromFile("dataCustomer.csv");
		System.out.println(m.allCustomers.get("000001"));
		System.out.println(m.allCustomers.get("1"));
		System.out.println(m.allCustomers.get("0000001"));
		
		GUI g = new GUI(m);
		g.displayPanelStart();
	}

}
