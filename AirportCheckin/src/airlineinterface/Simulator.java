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

	public static void main(String[] args) {
		Logger.instance().resetTimer();									// Start logger
		
		Simulator sim = new Simulator(3);

		sim.readFlightsFromFile("dataFlight-40c.csv");
		sim.readCustomersFromFile("dataCustomer-40c.csv");

		sim.makeCustomersArrive(5);
		
		sim.start(10, 20, false);

		Logger.instance().MainLog("---Starting simulation--");

	}
	
	private List<Customer> allCustomers;	// All customers loaded into simulation
	private List<Flight> allFlights;		// All flights loaded into simulation
	private List<Desk> allDesks;			// All desks in simulation
	private WaitingQueue queue;
	private GUIView guiView;
	private GUIController guiController;
	
	public Simulator(int deskCount) {
		// Build GUI.
		guiView = new GUIView();
		guiController = new GUIController(guiView);
		// Create WaitingQueue
		queue = new WaitingQueue();
		guiController.addQueue(queue);
		// Build desks
		allDesks = new ArrayList<Desk>();
		addDesks(deskCount);
		// Create lists
		allFlights = new ArrayList<Flight>();
		allCustomers = new ArrayList<Customer>();
	}
		
	/* Reads flights from CSV file and adds them into flight list.
	*/
	public void readFlightsFromFile(String filepath) {
		List<Flight> flightsFromFile = addFlightsFromFile(filepath);

		allFlights.addAll(flightsFromFile);		// Add all flights to allFlights
		Desk.addFlights(flightsFromFile);		// Add all flights to desk

		for(Flight f : flightsFromFile) {		// Add all flights to guiController
			guiController.addFlight(f);
		}
    }
	/* Reads customers from CSV file and adds them into customer lists.
	*/
	public void readCustomersFromFile(String filepath) {
		allCustomers.addAll(addCustomersFromFile(filepath));
		// Separate customers to checked-in and not checked-in
		List<Customer> checkedIn = new ArrayList<Customer>();
		List<Customer> notCheckedIn = new ArrayList<Customer>();
		for(Customer c : allCustomers) {
			(c.isCheckedIn() ? checkedIn : notCheckedIn).add(c);
		}
		for (Customer c : checkedIn) {
			for (Flight f : allFlights) {
				if(f.getFlightCode().equals(c.getFlightCode())) {
					try {
						f.addCustomer(c, Desk.getOversizeFee(c.getBaggageDetails()[0], c.getBaggageDetails()[1]));
						break;
					} catch (InvalidValueException e) {}
				}
			}
		}
		queue.addCustomersToList(notCheckedIn);
    }
	
	// Creates desk instances adds them to an array list. 
	public void addDesks (int num) {
    	for(int i = 0;i<num;i++) {
    		Desk desk = new Desk(queue, "NAME_TEMP");
    		allDesks.add(desk);
    		guiController.addDesk(desk);
    	}
    }
	
	public void makeCustomersArrive(int num) {
		queue.makeCustomersArrive(num);
	}
	
	// Starts Simulator. Handles time management and randomness setting of the simulation.
	public void start(float simSpeed, float realRunTime, boolean randomness) {
		guiView.setVisible(true);

		List<Thread> allDeskThreads = new ArrayList<Thread>();
		Thread threadQueue = new Thread(queue);								// Start threads
		// threadQueue.start();
		for(Desk d : allDesks) {
			allDeskThreads.add(new Thread(d));
		}
		
		// Start threads
		for(Thread t : allDeskThreads)
			t.start();
		threadQueue.start();
		
		long stopAtTime = System.currentTimeMillis() + (long)(realRunTime * 1000);
		while (System.currentTimeMillis() < stopAtTime) {}	// Do nothing
		
		Logger.instance().MainLog("---Simulation Time Elapsed---");
		//
		// Stop desks and queue
		for(Desk d : allDesks)
			d.enable = false;
		queue.active = false;
		
		while (true)
		{
			if (threadQueue.isAlive()) continue;
			for(Thread t : allDeskThreads)
				if(t.isAlive()) continue;
			break;
		}
		
		for (Flight f : allFlights)
		{
			Logger.instance().LogFlightDetails(f);
		}
		
		Logger.instance().MainLog("---Saving Log To File---");
		
		Logger.instance().WriteSummaryToFile("Summary.txt");
	}

	private static List<Customer> addCustomersFromFile(String filePath) {
		ArrayList<Customer> fileCustomers = new ArrayList<Customer>();
		try { 															// open input stream
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			String line = ""; 											// store current line

			while ((line = reader.readLine()) != null) { 				// go through every line in the file
				String[] customerDetails = line.trim().split(","); 		// split the line and trim empty space, push results to small array
				Customer currCustomer;
				if (customerDetails.length == 9) { 						// handle partial data - only take data from full fields, ignore partial data!
					try {
						float cWeight = 0, cVol = 0;
						try {
							cWeight = Float.parseFloat(customerDetails[5]);
							cVol = Float.parseFloat(customerDetails[6]) 
									* Float.parseFloat(customerDetails[7]) 
									* Float.parseFloat(customerDetails[8]);	// Baggage volume calculation added 
						} catch (NumberFormatException e) { /* If the parsing fails (e.g. no value, so assume 0) it defaults to 0 */}
						
						currCustomer = new Customer(customerDetails[0],
													customerDetails[1], 
													customerDetails[2],
													customerDetails[3], 
													cWeight, cVol); 	// one-liner to initialize Customer object with data from current file line
					} catch (InvalidValueException e) {
						// Customer failed to be created - skip to next line (WHY NO NOTIFICATION???)
						continue;
					}
					fileCustomers.add(currCustomer);
					boolean checkedIn = Boolean.parseBoolean(customerDetails[4]); 
					if(checkedIn)
						currCustomer.setCheckedIn();
					Logger.instance().LogPassengerDetails(currCustomer);
				} else {
					Logger.instance().MainLog(
							"Corrupted data found at line" + Arrays.toString(customerDetails) + "! Skipping...");
					continue; 											// break at corrupted data, let someone know that it's corrupted!
				}
			}
			reader.close(); 											// close reader
		} catch (Exception e) {
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
		return fileCustomers;
	}
	
	/* In a real environment this would be a connection to a DB but making it multithreaded would be beneficial 
	 * and to show we know what we are supposed to be doing/learning. Here is the plan: 
	 *  - Allocate a big buffer object, read a chunk, parse back from the end to find the last EOL char,
	 * copy the last bit of the buffer into a temp string, shove a null into the buffer at the EOL+1, 
	 * queue off the buffer reference, immediately create a new one, copy in the temp string first, 
	 * then fill up the rest of the buffer and repeat until EOF. Repeat until done. Use a pool of 
	 * threads to parse/process the buffers. You have to queue up whole chunks of valid lines. 
	 * Queuing off single lines will result in the thread communications taking longer than the parsing.
	 * This would result in out-of-order chunk processing but we don't care about that.
	 * 
	 * Is it worth it though??? I/O works faster sequentially (HDDs definitely do, which most companies still 
	 * use on their servers)!
	 * 
	 * How to separate the processing from the file reading, though..? That could be threaded and made faster... Make the processing multi-threaded.
	 */
	private static List<Flight> addFlightsFromFile(String filePath) {
		List<Flight> fileFlights = new ArrayList<Flight>();
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
						Float.parseFloat(flightDetails[6])); 			// one-liner to initialize Flight object with data from current file line
				fileFlights.add(currFlight); 							// the key is the unique flight id (flight code), value is currFlight being added
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
		return fileFlights;
	}
	
	public static void sleep(int millisec) {
		try {
			if (randomness) {
				Random r = new Random();
				Thread.sleep((int) (millisec / simSpeed * (0.5 + r.nextFloat())));
			} else {
				Thread.sleep((int) (millisec / simSpeed));
			}
			
		} 
		catch (InterruptedException e) {
			Logger.instance().MainLog(e.getMessage() + " failed to interrupt thread for " + millisec + " milliseconds.");
		}
	}
	

}