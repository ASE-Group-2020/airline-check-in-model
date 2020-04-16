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
import exceptions.AlreadyCheckedInException;
import exceptions.InvalidValueException;

// Runs main, sets up everything by loading in CSV files
public class Simulator extends Observable {

	private List<Customer> allCustomers;	// All customers loaded into simulation
	public List<Flight> allFlights;			// All flights loaded into simulation
	private List<Desk> allDesks;			// All desks in simulation
	private WaitingQueue queue;
	private GUIView guiView;
	private GUIController guiController;
	private int deskCount;					// Used for naming desks 
	
	private static boolean randomness; 
	public static float simSpeed;
	public static long stopAtTime;
	public static long startTime;
	public static long currentTime;
	public static float realRunTime;
	
	public static boolean runSimulation = true;
	public static boolean closeWindow = false;
	private static Random r;
	
	public Simulator(int deskCount)
	{
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
		
		addObserver(guiController.addSpeed());
		
		r = new Random();
		
		//instance = this;
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
						f.addCustomer(c, Desk.getOversizeFee(c.getBaggageDetails()));
						break;
					} catch (InvalidValueException e) {/* Would occur if the customer flight code was incorrect */}
					catch (AlreadyCheckedInException e) {/* Would occur if the customer was already set to checked in */}
				}
			}
		}
		queue.addCustomersToList(notCheckedIn);
    }
	
	// Creates desk instances adds them to an array list. 
	public void addDesks (int num) {
    	for(int i = 0;i<num;i++) {
    		Desk desk = new Desk(queue, "Desk " + (++deskCount));
    		allDesks.add(desk);
    		guiController.addDesk(desk);
    	}
    }
	// makes queue available from outside simulation
	public void makeCustomersArrive(int num) {
		queue.makeCustomersArrive(num);
	}
	
	// Starts Simulator. Handles time management and randomness setting of the simulation.
	public void start(float simSpeed, float realRunTime, boolean randomness) {
		runSimulation = true;
		Simulator.realRunTime = realRunTime;
		Simulator.simSpeed = simSpeed;
		Simulator.randomness = randomness;
		
		guiView.setVisible(true);
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<Thread> allDeskThreads = new ArrayList<Thread>();
		Thread threadQueue = new Thread(queue);								// Start threads
		// threadQueue.start();
		for(Desk d : allDesks) allDeskThreads.add(new Thread(d));
		
		Logger.instance().MainLog("---Starting simulation--"); //Start threads marks beginning of simulation
		
		// Start threads
		for(Thread t : allDeskThreads) t.start();
		threadQueue.start();
		
		startTime = System.currentTimeMillis();
		stopAtTime = startTime + (long)(Simulator.realRunTime * 1000);
		currentTime = 0;
		while (stopAtTime > System.currentTimeMillis() && runSimulation)
		{
			currentTime = (System.currentTimeMillis() - startTime) / 1000;
			notifyObservers();
		}
		currentTime = (long) Simulator.realRunTime;
		notifyObservers();
		
		// Stop desks and queue
		for(Desk d : allDesks) d.enable = false;
		queue.active = false;
		
		Logger.instance().MainLog("---Simulation Time Elapsed---");
		
		// halts code until all threads have been stopped
		while (true)
		{
			if (threadQueue.isAlive()) continue;
			boolean allThreadsStopped = true;
			for(Thread t : allDeskThreads) 
			{
				if(t.isAlive()) { allThreadsStopped = false; break; }
			}
			if (allThreadsStopped) break;
		}
		
		for(Desk d : allDesks) d.notifyObservers();
		
		for (Flight f : allFlights)
		{
			Logger.instance().LogFlightDetails(f);
		}
		
		Logger.instance().MainLog("---Saving Log To File---");
		
		Logger.instance().WriteSummaryToFile("Summary.txt");
		
		while(!closeWindow) {}											// wait until window has been closed
	}

	/* addCustomersFromFile() - adds Customers data from a CSV file
	 */
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
						float cWeight = 0, cVolX = 0, cVolY = 0, cVolZ = 0;
						try {
							cWeight = Float.parseFloat(customerDetails[5]);
							cVolX = Float.parseFloat(customerDetails[6]);
							cVolY = Float.parseFloat(customerDetails[7]);
							cVolZ = Float.parseFloat(customerDetails[8]);	// Baggage volume calculation added 
						} catch (NumberFormatException e) { /* If the parsing fails (e.g. no value, so assume 0) it defaults to 0 */}
						
						currCustomer = new Customer(customerDetails[0],
													customerDetails[1], 
													customerDetails[2],
													customerDetails[3], 
													cWeight, cVolX, cVolY, cVolZ); 	// one-liner to initialize Customer object with data from current file line
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
	
	/* addFlightsFromFile() - adds Flight data from a CSV file
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
	
	private static int realTimeStep = 50;
	public static void sleep(int millisec)
	{
		float sleepTimeRemaining = (float) (millisec * ( randomness ? 0.5f * r.nextFloat() : 1 ));
	    while (sleepTimeRemaining > 0) {
	        try { Thread.sleep(realTimeStep); }
	        catch (InterruptedException e) { Logger.instance().MainLog(e.getMessage() + " failed to interrupt thread for " + millisec + " milliseconds."); }
	        sleepTimeRemaining -= realTimeStep * simSpeed;
	    }
	}
	

	public static void main(String[] args) {
		Logger.instance().resetTimer();									// Start logger
		
		Simulator sim = new Simulator(3);								// the argument is the number of desks instantiated 

		sim.readFlightsFromFile("dataFlight-40c.csv");
		sim.readCustomersFromFile("dataCustomer-40c.csv");

		//sim.makeCustomersArrive(5);									// Delays the arrival of customers
		
		sim.start(1, 120, false);										// (simSpeed, runTime, randomness)
		System.exit(0);													// Exit the program after all threads have been stopped and GUI closed
	}
}