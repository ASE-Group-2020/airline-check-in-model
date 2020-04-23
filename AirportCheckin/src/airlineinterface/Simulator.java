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
public class Simulator extends Observable // Observable allows the speed display to see the simulator's current run time
{	
	// Singleton pattern for Simulator - Desk and WaitingQueue need to sleep so must have access to a single Simulator. Plus there will never be a need for more than 1 simulator here.
	private static Simulator instance;
	public static Simulator get() {
		if (instance == null)
			instance = new Simulator();
		return instance;
	}
	
	// data-sets of customers and flights
	private List<Customer> allCustomers;	// All customers loaded into simulation
	public List<Flight> allFlights;			// All flights loaded into simulation
	
	// structure of simulation
	private List<Desk> allDesks;			// All desks in simulation
	private WaitingQueue queue;				// waiting queues before the check-in desks
	private GUIView guiView;				// main GUI class
	private GUIController guiController;	// gui controller
	private int deskCount;					// Used for naming desks 
	
	/* Time and sleep-related variables */
	private boolean randomness; 
	private float simSpeed;
	private long stopAtTime;
	private long startTime;
	private long currentTime;
	private float realRunTime;
	private int sleepTimeStep = 50;		// Simulator.sleep waits for this time step as many times as needed - makes simSpeed reactive 
	private Random r = new Random();
	private boolean runSimulation = true;
	private volatile boolean closeWindow = false;	// Volatile as otherwise the window refuses to close if simulation is saved to file through button
	
	private Simulator()
	{
		// Build GUI.
		guiView = new GUIView();
		guiController = new GUIController(guiView);
		// Create WaitingQueue
		queue = new WaitingQueue();
		guiController.addQueue(queue);
		// Build desks
		allDesks = new ArrayList<Desk>();
		// Create lists
		allFlights = new ArrayList<Flight>();
		allCustomers = new ArrayList<Customer>();
		// allows the speed display to see the simulator's current run time
		addObserver(guiController.addSpeed());
	}
	
	public float getSimSpeed() {
		return simSpeed;
	}
	
	public float getRealRunTime() {
		return realRunTime;
	}
	
	public long getCurrentTime() {
		return currentTime;
	}
	
	/* Causes simulation to stop and write to file */
	public void stopSimulation() {
		runSimulation = false;
		guiController.setAllButtons(false);
	}
	/* Causes simulation to stop, write to file, and closes GUI */
	public void closeSimulation() {
    	runSimulation = false;		// set flags to inform the simulation to start wrapping up and close
    	closeWindow = true;
	}
	
	public void setSimulationSpeed(float speed) {
		simSpeed = speed;
	}
		
	/* Reads flights from CSV file and adds them into flight list. */
	public void addFlightsFromFile(String filepath) {
		List<Flight> flightsFromFile = getFlightsFromFile(filepath);

		allFlights.addAll(flightsFromFile);		// Add all flights to allFlights
		Desk.addFlights(flightsFromFile);		// Add all flights to desk

		for(Flight f : flightsFromFile) {		// Add all flights to guiController
			guiController.addFlight(f);
		}
    }
	
	/* Reads customers from CSV file and adds them into customer lists. */
	public void addCustomersFromFile(String filepath) {
		List<List<Customer>> allFileCustomers = getCustomersFromFile(filepath);
		// Separate customers to checked-in and not checked-in
		List<Customer> checkedIn = allFileCustomers.get(0);
		List<Customer> notCheckedIn = allFileCustomers.get(1);
		allCustomers.addAll(checkedIn);
		allCustomers.addAll(notCheckedIn);
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
	public void start(float simSpeed, float realRunTime, boolean randomness)
	{
		// sets operation parameters
		runSimulation = true;
		this.realRunTime = realRunTime;
		this.simSpeed = simSpeed;
		this.randomness = randomness;
		
		// GUI setup and wait
		guiView.setVisible(true);
		try { Thread.sleep(50); }
		catch (InterruptedException e) { e.printStackTrace(); }
		
		// thread setups
		Thread threadQueue = new Thread(queue);
		
		List<Thread> allDeskThreads = new ArrayList<Thread>();
		for(Desk d : allDesks) allDeskThreads.add(new Thread(d));
		
		List<Thread> allFlightThreads = new ArrayList<Thread>();
		for(Flight f : allFlights) allFlightThreads.add(new Thread(f)); 
		
		Logger.instance().MainLog("---Starting simulation--"); //Start threads marks beginning of simulation
		
		// Starts queue and desk threads
		for(Thread t : allDeskThreads) t.start();
		for(Thread t : allFlightThreads) t.start();
		threadQueue.start();
		
		// main simulation loop
		// ----
		startTime = System.currentTimeMillis();
		stopAtTime = startTime + (long)(this.realRunTime * 1000);
		currentTime = 0;
		while (stopAtTime > System.currentTimeMillis() && runSimulation)
		{
			currentTime = (System.currentTimeMillis() - startTime) / 1000;
			notifyObservers();
		}
		currentTime = (long) this.realRunTime;
		notifyObservers();
		if (runSimulation) stopSimulation();
		// ----
		
		// Stop all desks and queue
		for(Desk d : allDesks) d.enable = false;
		queue.active = false;
		Logger.instance().MainLog("---Simulation Time Elapsed---");
		
		// halts code until all threads have been stopped
		while (true)
		{
			if (threadQueue.isAlive()) continue; // checks if waiting queue is still running
			boolean allThreadsStopped = true;
			for(Thread t : allDeskThreads) if(t.isAlive()) { allThreadsStopped = false; break; } // checks if any desks are still running
			if (allThreadsStopped) break;
		}
		
		for(Desk d : allDesks) d.notifyObservers(); // refreshes display one final time
		for (Flight f : allFlights) Logger.instance().LogFlightDetails(f); // logs all flight details
		
		// saves log
		Logger.instance().MainLog("---Saving Log To File---");
		Logger.instance().WriteSummaryToFile("Summary.txt");
		
		while (!closeWindow) {try {Thread.sleep(100);} catch (InterruptedException e) {}}	// wait until window has been closed
		guiController.closeGui();
	}

	/* getCustomersFromFile() - Helper function to read all customers from a CSV file */
	private static List<List<Customer>> getCustomersFromFile(String filePath) {
		ArrayList<Customer> fileCheckedInCustomers = new ArrayList<Customer>();
		ArrayList<Customer> fileNotCheckedInCustomers = new ArrayList<Customer>();
		try { 															// open input stream
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			String line = ""; 											// store current line

			while ((line = reader.readLine()) != null) { 				// go through every line in the file
				String[] customerDetails = line.trim().split(","); 		// split the line and trim empty space, push results to small array
				Customer currCustomer;
				if (customerDetails.length == 10) { 						// handle partial data - only take data from full fields, ignore partial data!
					try {
						float cWeight = 0, cVolX = 0, cVolY = 0, cVolZ = 0;
						try {
							cWeight = Float.parseFloat(customerDetails[6]);
							cVolX = Float.parseFloat(customerDetails[7]);
							cVolY = Float.parseFloat(customerDetails[8]);
							cVolZ = Float.parseFloat(customerDetails[9]);	// Baggage volume calculation added 
						} catch (NumberFormatException e) { /* If the parsing fails (e.g. no value, so assume 0) it defaults to 0 */}
						
						currCustomer = new Customer(customerDetails[0],
													customerDetails[1], 
													customerDetails[2],
													customerDetails[3],
													customerDetails[4],
													cWeight, cVolX, cVolY, cVolZ); 	// one-liner to initialize Customer object with data from current file line
					} catch (InvalidValueException e) {
						// Customer failed to be created - skip to next line (WHY NO NOTIFICATION???)
						continue;
					}
					boolean checkedIn = Boolean.parseBoolean(customerDetails[5]); 
					(checkedIn ? fileCheckedInCustomers : fileNotCheckedInCustomers).add(currCustomer);
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
				System.err.println("Error: Customer dataset " + filePath + " not found. Exiting...");
				System.exit(0);
			} else if (e instanceof IOException)
				System.err.println("Error: I/O error.");
			else {
				System.err.println("General error! Give the following information to the devs...");
				e.printStackTrace();
			}
		}
		List<List<Customer>> rtrn = new ArrayList<List<Customer>>();
		rtrn.add(fileCheckedInCustomers);
		rtrn.add(fileNotCheckedInCustomers);
		return rtrn;
	}
	
	/* addFlightsFromFile() - Helper function to read all flights from a CSV file */
	private static List<Flight> getFlightsFromFile(String filePath) {
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
						Float.parseFloat(flightDetails[6]),
						Integer.parseInt(flightDetails[7])); 			// one-liner to initialize Flight object with data from current file line
				fileFlights.add(currFlight); 							// the key is the unique flight id (flight code), value is currFlight being added
			}
			reader.close(); 											// close reader
		} catch (Exception e) {
			if (e instanceof FileNotFoundException) {
				System.err.println("Error: Flight dataset " + filePath + " not found. Exiting...");
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
	
	/* Public method that handles sleeping with potential randomness and different simulation speeds */
	public void sleep(int millisec)
	{
		int sleepTimeRemaining = (int) ( (float) millisec * ( randomness ? 0.5f * r.nextFloat() : 1 ));	// Total time to sleep for (including randomness)	    
	    nonRandomSleep(sleepTimeRemaining);
	}
	
	/* Public method that handles sleeping with different simulation speeds */
	public void nonRandomSleep(int millisec)
	{
		float sleepTimeRemaining = (float) millisec;												// Total time to sleep for
	    while (sleepTimeRemaining > 0 && runSimulation) {											// If sleeping hasn't completed, keep looping
	        try { Thread.sleep(sleepTimeStep); }													// Sleep for a set, small time - allows CPU to do other tasks
	        catch (InterruptedException e) { Logger.instance().MainLog(e.getMessage() + " failed to interrupt thread for " + millisec + " milliseconds."); }
	        sleepTimeRemaining -= sleepTimeStep * simSpeed;											// Update remaining time, considering simSpeed
	    }
	}
	
	/* Initialisation of simulation program */
	public static void main(String[] args)
	{
		// default variables - can be overwritten through console arguments
		String flights = "dataFlight-100c.csv";
		String customers = "dataCustomer-100c.csv";
		int desks = 6;
		float initialSimSpeed = 1;
		float runTime = 300;
		boolean randomness = true;
		
		/*
		String flights = "dataFlight-demo.csv";
		String customers = "dataCustomer-demo.csv";
		int desks = 6;
		float initialSimSpeed = 1;
		float runTime = 300;
		boolean randomness = true;
		*/
		
		Logger.instance().resetTimer();	// Start logger
		
		// Deals with the case of input arguments via console for different running parameters
		if (args.length > 0)
		{
			if (args[0].equalsIgnoreCase("-h") || args[0].equalsIgnoreCase("--help"))
			{
				System.out.println("Please specify arguments for custom parameters:");
				System.out.println("  1: Flight Dataset Path Address");
				System.out.println("  2: Custoemr Dataset Path Address");
				System.out.println("  3: Number of Desks");
				System.out.println("  4: Initial Simulation Speed in Seconds");
				System.out.println("  5: Simulation Runtime in Seconds");
				System.out.println("  6: Include Randomness in Waiting Times?");
				System.out.println("Otherwise, the default variables will be used.");
				System.exit(1);
			}
			try
			{
				flights = args[0];
				customers = args[1];
				desks = Math.max(Integer.parseInt(args[2]), 1);
				initialSimSpeed = Math.max(Float.parseFloat(args[3]), 0);
				runTime = Math.max(Float.parseFloat(args[4]), 0);
				randomness = Boolean.parseBoolean(args[5]);
				Logger.instance().MainLog("Imported new parameters");
			}
			catch (IndexOutOfBoundsException e1)
			{
				Logger.instance().MainLog("ERROR: Not enough arguments given to import new settings");
				e1.printStackTrace();
				System.exit(-1);
			}
			catch (NumberFormatException e2)
			{
				Logger.instance().MainLog("ERROR: Invalid arguments given to import new settings");
				e2.printStackTrace();
				System.exit(-2);
			}
		}
		else Logger.instance().MainLog("Using default parameters");
		
		// Simulation setup
		Simulator sim = Simulator.get();					// Only one Simulator can exist at a time
		sim.addDesks(desks);								// No desks are added by default - specify the number here

		sim.addFlightsFromFile(flights);					// import flights from specified file			
		sim.addCustomersFromFile(customers);				// import customers from specified file

		//sim.makeCustomersArrive(5);						// Immediate arrival of customers
		
		sim.start(initialSimSpeed, runTime, randomness);	// (simSpeed, runTime, randomness)
	}
}