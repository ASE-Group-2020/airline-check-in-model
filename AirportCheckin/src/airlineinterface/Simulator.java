package airlineinterface;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import airlineinterface.gui.GUIController;
import airlineinterface.gui.GUIView;
import exceptions.InvalidValueException;

// Runs main, sets up everything by loading in CSV files
public class Simulator {
	
	private static boolean randomness;
	
	private static List<Flight> allFlights = new ArrayList<Flight>();
	
	private static float runtimeInSeconds = 10;

	public static void main(String[] args) {
		Logger.instance().resetTimer();									// Start logger
		
		WaitingQueue q = new WaitingQueue();							// Create queue
		Desk desk = new Desk(q, "desk 1");								// Create desk(s)
		
		Desk.addFlights(addFlightsFromFile("dataFlight.csv"));			// Add flights
		q.addCustomersToList(addCustomersFromFile("dataCustomer.csv"));	// Add customer
		q.makeCustomersArrive(5);
		
		GUIView guiView = new GUIView();
		GUIController guiController = new GUIController(guiView);
		guiController.addDesk(desk);
		guiController.addQueue(q);
		for(Flight f : allFlights) {
			guiController.addFlight(f);
		}
		guiView.setVisible(true);

		Logger.instance().MainLog("---Starting simulation--");
		
		Thread threadQueue = new Thread(q);								// Start threads
		threadQueue.start();
		Thread threadDesk = new Thread(desk);
		threadDesk.start();
		
		long stopAtTime = System.currentTimeMillis() + (long)(runtimeInSeconds * 1000);
		while (System.currentTimeMillis() < stopAtTime) {}
		
		Logger.instance().MainLog("---Simulation Time Elapsed---");
		
		desk.deskExists = false;
		q.active = false;
		while (true)
		{
			if (threadQueue.isAlive()) continue;
			if (threadDesk.isAlive()) continue;
			break;
		}
		
		for (Flight f : allFlights)
		{
			Logger.instance().LogFlightDetails(f);
		}
		
		Logger.instance().MainLog("---Saving Log To File---");
		
		Logger.instance().WriteSummaryToFile("Summary.txt");
	}

	public static List<Customer> addCustomersFromFile(String filePath) {
		ArrayList<Customer> allCustomers = new ArrayList<Customer>();
		try { 															// open input stream
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			String line = ""; 											// store current line

			while ((line = reader.readLine()) != null) { 				// go through every line in the file
				String[] customerDetails = line.trim().split(","); 		// split the line and trim empty space, push results to small array
				// System.out.println(Arrays.toString(customerDetails)); <- debug
				Customer currCustomer;
				if (customerDetails.length == 7) { 						// handle partial data - only take data from full fields, ignore partial data!
					try {
						float cWeight = 0, cVol = 0;
						try {
							cWeight = Float.parseFloat(customerDetails[5]);
							cVol = Float.parseFloat(customerDetails[6]);
						} catch (NumberFormatException e) {/* HELLO, SHOULD ANYTHING BE DONE HERE????? */}
						currCustomer = new Customer(customerDetails[0],
													customerDetails[1], 
													customerDetails[2],
													customerDetails[3], 
													cWeight, cVol); 	// one-liner to initialise Customer object with data from current file line
					} catch (InvalidValueException e) {
						// Customer failed to be created - skip to next line (WHY NO NOTIFICATION???)
						continue;
					}
					if (!Boolean.parseBoolean(customerDetails[4])) {
						allCustomers.add(currCustomer);
					}
					Logger.instance().LogPassengerDetails(currCustomer);
				} else {
					System.out.println(
							"Corrupted data found at line" + Arrays.toString(customerDetails) + "! Skipping...");
					continue; 											// break at corrupted data, let someone know that it's corrupted!
				}
			}
			reader.close(); 											// close reader
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
	
	/* In a real environment this would be a connection to a DB but making it multithreaded would be beneficial 
	 * and to show we know what we are supposed to be doing/learning. Here is the plan: 
	 *  - Allocate a big buffer object, read a chunk, parse back from the end to find the last EOL char,
	 * copy the last bit of the buffer into a temp string, shove a null into the buffer at the EOL+1, 
	 * queue off the buffer reference, immediately create a new one, copy in the temp string first, 
	 * then fill up the rest of the buffer and repeat until EOF. Repeat until done. Use a pool of 
	 * threads to parse/process the buffers. You have to queue up whole chunks of valid lines. 
	 * Queueing off single lines will result in the thread communications taking longer than the parsing.
	 * This would result in out-of-order chunk processing but we don't care about that.
	 * 
	 * Is it worth it though??? I/O works faster sequentially (HDDs definitely do, which most companies still 
	 * use on their servers)!
	 * 
	 * How to separate the processing from the file reading, though..? That could be threaded and made faster... Make the processing multi-threaded.
	 */
	public static List<Flight> addFlightsFromFile(String filePath) {
		try { 															// open input stream
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			String line = ""; 											// store current line
			
			while ((line = reader.readLine()) != null) { 				// go through every line in the file
				String[] flightDetails = line.trim().split(","); 		// split the line and trim empty space, push results to small array
				
				if (Integer.parseInt(flightDetails[4]) < 0) 
					System.err.println("Nonsensical flight capacity: "+System.lineSeparator() 
													+  Arrays.deepToString(flightDetails));
				if (Float.parseFloat(flightDetails[5]) < 0) 
					System.err.println("Nonsensical baggage weight: "+System.lineSeparator() 
													+  Arrays.deepToString(flightDetails));
				if (Float.parseFloat(flightDetails[6]) < 0) 
					System.err.println("Nonsensical baggage volume: "+System.lineSeparator() 
													+  Arrays.deepToString(flightDetails));
				
				Flight currFlight = new Flight(flightDetails[0], 
						flightDetails[1], flightDetails[2], 
						flightDetails[3],
						Integer.parseInt(flightDetails[4]), 
						Float.parseFloat(flightDetails[5]),
						Float.parseFloat(flightDetails[6])); 			// one-liner to initialise Flight object with data from current file line
				allFlights.add(currFlight); 							// the key is the unique flight id (flight code), value is currFlight being added
			}
			reader.close(); 											// close reader
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
	
	public synchronized static void sleep(int millisec) {
		try {
			if (randomness) {
				Random r = new Random();
				Thread.sleep((int) (millisec * (0.5 + r.nextFloat())));
			} else {
				Thread.sleep(millisec);
			}
			
		} 
		catch (InterruptedException e) {
			System.out.println(e.getMessage() + " failed to interrupt thread for " + millisec + " milliseconds.");
		}
	}
	

}
