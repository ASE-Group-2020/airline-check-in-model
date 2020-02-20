package airlineinterface;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import exceptions.InvalidValueException;

public class Master {
	//private HashMap<String, Flight> allFlights;
	//private  HashMap<String, Customer> allCustomers; //I CHANGED THE VALUES OF THIS HASHMAP TO CUSTOMER OBJECTS, I ALSO MADE THIS STATIC, CHANGE LATER
	private static HashMap<String, Customer> allCustomers = new HashMap<>(); //IDEALLY, THE KEY IS CUSTOMER REF CODE (THIS MEANS THE CUSTOMER CODES NEED TO BE UNIQUE)
	private static HashMap<String, Flight> allFlights = new HashMap<>(); //IDEALLY, THE KEY IS FLIGHTCODE  (THIS MEANS THE FLIGHT CODES NEED TO BE UNIQUE)
	public Master() {}
	
	// Needs exception
	public void addFlight(Flight f) {}
	
	// Probably not needed
	// Needs exception
	public void removeFlight(Flight f) {}
	
	
	/* Method which returns a Flight object by searching the
	*  allflights HashMap for the flight with a specific flightCode.
	*/ 
	public Flight getFlight(String flightCode) {
		/* Ideally this isn't necessary because the HashMap key for allFlights
		 * is the flight code. 
		 */ 
		 if(allFlights.containsKey(flightCode)) return allFlights.get(flightCode);
		 else {
			 return null; //if returns null the GUI will handle the exception
		 }
		 /* 
		 * Otherwise, we iterate through the values of allFlights. Each iteration
		 * we inspect a flight object and check its flight code. If the correct
		 * flight code is found then return that Flight object f. 
		 *
		for(Flight f : allFlights.values())
		{
			String currentFlightCode = f.getFlightCode(); //getFlightCode is a getter method from Flight
			if(currentFlightCode.equals(flightCode))
			{
				return f;
			}
		}
		return null;
		*/
	}
	
	/* Method which returns a Customer object by searching the
	*  allCustomer HashMap for the Customer with a specific refCode. 
	*/ 
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
	
	public void addFlightsFromFile(String filePath) {
		try { //open input stream 
		BufferedReader reader = new BufferedReader(new FileReader(filePath));

        String line = "";
        while( (line = reader.readLine()) != null ) {
            String [] flightDetails = line.trim().split(",");
            Flight currFlight = 
            		new Flight(flightDetails[0], flightDetails[1], flightDetails[2], flightDetails[3], 
            				Integer.parseInt(flightDetails[4]), Float.parseFloat(flightDetails[5]), Float.parseFloat(flightDetails[6]));
            allFlights.put(flightDetails[2],currFlight);
        }
        
		reader.close(); //close reader
		}
		catch(Exception e) {
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
	
	public void addCustomersFromFile(String filePath) {}
	
	// Output info for each flight somehow
	public void display() {}
	
	
	//JUST USED FOR TESTING
	public static void addObjectsMaps() throws InvalidValueException {
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
	
	public static void main(String[] args) throws InvalidValueException {
		addObjectsMaps(); //USED FOR TESTING
		Master m = new Master();
		GUI g = new GUI(m);
		g.displayPanelStart();
	}


}
