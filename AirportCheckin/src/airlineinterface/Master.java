package airlineinterface;

public class Master {

	public static void main(String[] args) {
	}
	
	public Master() {}
	
	// Needs exception
	public void addFlight(Flight f) {}
	
	// Probably not needed
	// Needs exception
	public void removeFlight(Flight f) {}
	
	// Called during startup (reading from file) and with GUI
	// Needs exception
	public void checkIn(Customer c, float weight, float volume) {}
	
	public void addFlightsFromFile(String filePath) {}
	
	public void addCustomersFromFile(String filePath) {}
	
	// Output info for each flight somehow
	public void display() {}

}
