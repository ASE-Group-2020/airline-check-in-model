package airlineinterface;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import exceptions.InvalidValueException;

// Runs main, sets up everything by loading in CSV files
public class Simulator {

	public static void main(String[] args) {
		// Start logger
		Logger.instance().resetTimer();

		// Create queue
		WaitingQueue q = new WaitingQueue();
		// TODO: create desk(s)
		Desk desk = new Desk(q, "desk 1");
		
		// Add flights
		Desk.addFlights(addFlightsFromFile("dataFlight.csv"));
		// Add customers
		q.addCustomersToList(addCustomersFromFile("dataCustomer.csv"));

		Logger.instance().log("Starting simulation");
		
		// Start threads
		Thread threadQueue = new Thread(q);
		threadQueue.start();
		Thread threadDesk = new Thread(desk);
		threadDesk.start();
	}

	public static List<Customer> addCustomersFromFile(String filePath) {
		ArrayList<Customer> allCustomers = new ArrayList<Customer>();
		try { // open input stream
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			String line = ""; // store current line

			while ((line = reader.readLine()) != null) { // go through every line in the file
				String[] customerDetails = line.trim().split(","); // split the line and trim empty space, push results
																	// to small array
				// System.out.println(Arrays.toString(customerDetails)); <- debug
				Customer currCustomer;
				if (customerDetails.length == 7) { // handle partial data - only take data from full fields, ignore
													// partial data!
					try {
						float cWeight = 0, cVol = 0;
						try {
							cWeight = Float.parseFloat(customerDetails[5]);
							cVol = Float.parseFloat(customerDetails[6]);
						} catch (NumberFormatException e) {
						}
						currCustomer = new Customer(customerDetails[0], customerDetails[1], customerDetails[2],
								customerDetails[3], cWeight, cVol); // one-liner to initialize Customer object with data
																	// from current file line
					} catch (InvalidValueException e) {
						// Customer failed to be created - skip to next line
						continue;
					}
					if (!Boolean.parseBoolean(customerDetails[4])) {
						allCustomers.add(currCustomer);
					}
				} else {
					System.out.println(
							"Corrupted data found at line" + Arrays.toString(customerDetails) + "! Skipping...");
					continue; // break at corrupted data, let someone know that it's corrupted!
				}
			}
			reader.close(); // close reader
		} catch (

		Exception e) {
			if (e instanceof FileNotFoundException) {
				System.err.println("Error: Customer info file not found. Exiting...");
				System.exit(0);
			} else if (e instanceof IOException)
				System.err.println("Error: I/O error.");
			else {
				System.err.println("General error! Give the following information to the devs...");
				e.printStackTrace();
			}
		}
		return allCustomers;
	}
	
	public static List<Flight> addFlightsFromFile(String filePath) {
		List<Flight> allFlights = new ArrayList<Flight>();
		try { 														// open input stream
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			String line = ""; 										// store current line
			
			while ((line = reader.readLine()) != null) { 			// go through every line in the file
				String[] flightDetails = line.trim().split(","); 	// split the line and trim empty space, push results to small array
				if (Integer.parseInt(flightDetails[4]) < 0) System.err.println("Nonsensical flight capacity: "+System.lineSeparator() +  Arrays.deepToString(flightDetails));
				if (Float.parseFloat(flightDetails[5]) < 0) System.err.println("Nonsensical baggage weight: "+System.lineSeparator() +  Arrays.deepToString(flightDetails));
				if (Float.parseFloat(flightDetails[6]) < 0) System.err.println("Nonsensical baggage volume: "+System.lineSeparator() +  Arrays.deepToString(flightDetails));
				Flight currFlight = new Flight(flightDetails[0], 
						flightDetails[1], flightDetails[2], 
						flightDetails[3],
						Integer.parseInt(flightDetails[4]), 
						Float.parseFloat(flightDetails[5]),
						Float.parseFloat(flightDetails[6])); 		// one-liner to initialize Flight object with data from current file line
				allFlights.add(currFlight); 		// the key is the unique flight id (flight code), value is currFlight being added
			}
			reader.close(); 										// close reader
		}
			
		 catch (Exception e) {
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
		return allFlights;
	}

}
