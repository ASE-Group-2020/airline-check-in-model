package airlineinterface;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import exceptions.InvalidValueException;

// Runs main, sets up everything by loading in CSV files
public class Simulator {

	public static void main(String[] args) {
		// Create queue
		WaitingQueue q = new WaitingQueue();
		// TODO: create desk(s)
		Desk desk = new Desk(q);
		// TODO: create flight(s)
		
		// Load customers from file
		q.addCustomersToList(addCustomersFromFile("dataCustomer.csv"));
		
		Thread threadQueue= new Thread(q);
		threadQueue.start();
		// Thread threadDesk = new Thread(desk);
		// threadDesk.start();
	}

	

	public static ArrayList<Customer> addCustomersFromFile(String filePath) {
		ArrayList<Customer> allCustomers = new ArrayList<Customer>();
		try { 														// open input stream
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			String line = ""; 										// store current line
			
			while ((line = reader.readLine()) != null) { 			// go through every line in the file
				String[] customerDetails = line.trim().split(","); 	// split the line and trim empty space, push results to small array
				//System.out.println(Arrays.toString(customerDetails)); <- debug
				Customer currCustomer;
				if(customerDetails.length == 7) {					// handle partial data - only take data from full fields, ignore partial data!
					try {
						currCustomer = new Customer(customerDetails[0], 
								customerDetails[1], customerDetails[2], 
								customerDetails[3]);	// one-liner to initialize Customer object with data from current file line
					} catch (InvalidValueException e) {
						// Customer failed to be created - skip to next line
						continue;
					}
					if (!Boolean.parseBoolean(customerDetails[4])) {
						allCustomers.add(currCustomer);
					}
				}
				else {
					System.out.println("Corrupted data found at line" + Arrays.toString(customerDetails) + "! Skipping...");
					continue;											// break at corrupted data, let someone know that it's corrupted!
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
		return allCustomers;
	}

}
