package airlineinterface;

public class Customer {
	
	public Customer(String name, String bookingRef, String flightCode) {}
	
	// Throw exception if already checked in
	public void setCheckedIn() {}
	
	public boolean isCheckedIn() { return false; }
	
	// Note - not in class diagram
	public String getName() { return ""; }
	
	public String getFlightCode() { return ""; }
	
	public void display() {}

}
