package airlineinterface;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Master {
	private HashMap<String, Flight> allFlights;
	private HashMap<String, Flight> allCustomers;
	
	public Master() {}
	
	// Needs exception
	public void addFlight(Flight f) {}
	
	// Probably not needed
	// Needs exception
	public void removeFlight(Flight f) {}
	
	// Called during startup (reading from file) and with GUI
	// Needs exception
	public void checkIn(Customer c, float weight, float volume) {
		
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
	
	public static void main(String[] args) {
		Master m = new Master();
		GUI g = new GUI(m);
		g.showWindowStart();
	}

}
