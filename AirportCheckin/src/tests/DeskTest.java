package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Queue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import airlineinterface.Customer;
import airlineinterface.Desk;
import airlineinterface.Flight;
import airlineinterface.Master;
import airlineinterface.Simulator;
import airlineinterface.WaitingQueue;
import exceptions.InvalidValueException;

public class DeskTest {
	//Create pointer to customers obj and lists 
	Customer adamCust;
	Customer marieCust;
	//Customer damienCust;
	//Customer paulCust;

	Queue<Customer> testQ;
	List<Customer> notArrived;
	
	//Create pointer to a flight obj and list
	Flight LON123;
	
	List<Flight> flights;
	
	
	Desk d;
	WaitingQueue q;
	
	
	
	@BeforeEach
	void setup() throws InvalidValueException {
		
		
		adamCust = new Customer("123", "adam", "smith", "LON-EDI-0",10.0f, 52.0f);//, false, 82,  112);
		marieCust = new Customer("456", "maire", "curie", "LON-EDI-0",26.0f, 132.0f);//, false, 60,  24);
		//damienCust = new Customer("789", "damien", "Le Court", "LON-EDI-0");//, false, 23,  1);
		//paulCust = new Customer("1011", "Paul", "Dr.Jean-Francois", "LON-EDI-0");
		testQ.add(adamCust);
		testQ.add(marieCust);
		
		//Create a new flight object 
		LON123 = new Flight("LON", "EDI", "LON-EDI-0", "RayanAir", 300, 10000, 300000);
		
		flights.add(LON123);
		
		this.q = new WaitingQueue();
		//add a linkedlist of customers to WaitingQueue obj
		q.setWaiting(testQ);

		this.d = new Desk(q, "desk1");
		//add flight obj to the flights list 
		d.addFlights(flights);
	}
	
	
	
	@Test
	/** test if:
	 * 		EQUALS 
	 * 
	 * 		When the queue has a customer 
	 * 		When the queue doesn't have a queue 
	 * 
	 * 			
	*/
	void runTest() {
		
	}
	
	@Test
	/** test if:
	 * 		EQUALS 
	 * 
	 * 		When the queue has a customer 
	 * 		When the queue doesn't have a queue 
	 * 
	 * 			
	*/
	void startCheckInTest() {
		
	}
	
	@Test
	/** test if:
	 * 		EQUALS 
	 * 
	 * 		When the queue has a customer 
	 * 		When the queue doesn't have a queue 
	 * 
	 * 			
	*/
	void checkIn() {
		
	}
	
	@Test
	/** test if:
	 * 		EQUALS
	 * 		when the weight is smaller than 15 kg or volume is less than 20 litres: fee is 0 
	 * 		when the weight is smaller than 25 kg or volume is less than 35 litres: fee is 10 
	 * 		when the weight is smaller than 45 kg or volume is less than 55 litres: fee is 20 
	 * 		when the weight is smaller than 70 kg or volume is less than 90 litres: fee is 40 
	 * 		when the weight is smaller than 100 kg or volume is less than 120 litres: fee is 60 
	 * 		when the weight is smaller than 150 kg or volume is less than 180 litres: fee is 80 
	 * 		when the weight is smaller than 200 kg or volume is less than 260 litres: fee is 100 
	 * 		
	 * 		THROWS
	 * 		when the weight is bigger than 200 kg or volume is bigger than 260 litres: send and exception
	 * 
	 *      Notes:
	 * 		GUI handles the issue of the input no being numbers or having spaces
	*/
	void getOversizeTest() throws InvalidValueException{
		assertEquals(d.getOversizeFee(9,0),0,"The method has failed to correctly set the fee to 0 when"
				+ " the weight is 0-14 kg or volume is 0-19 litres.");
		assertEquals(d.getOversizeFee(0,20),10,"The method has failed to correctly set the fee to 10 when"
				+ " the weight is 15-24 kg or volume is 20-34 litres.");
		assertEquals(d.getOversizeFee(40,50),20,"The method has failed to correctly set the fee to 20 when"
				+ " the weight is 25-44 kg or volume is 35-54 litres.");
		assertEquals(d.getOversizeFee(60,30),40,"The method has failed to correctly set the fee to 40 when"
				+ " the weight is 45-69 kg or volume is 55-89 litres.");
		assertEquals(d.getOversizeFee(75,89),60,"The method has failed to correctly set the fee to 60 when"
				+ " the weight is 70-99 kg or volume is 90-119 litres.");
		assertEquals(d.getOversizeFee(81,179),80,"The method has failed to correctly set the fee to 80 when"
				+ " the weight is 100-149 kg or volume is 120-179 litres.");
		assertEquals(d.getOversizeFee(199,260),100,"The method has failed to correctly set the fee to 100 when"
				+ " the weight is 150-200 kg or volume is 180-260 litres.");
		
		assertThrows(InvalidValueException.class, () -> d.getOversizeFee(500,1),"The method fail to thow an exception"
				+ " needed to prevent values that are excessive from being entered onto the flight.");
	}
	
	@Test
	/** test if:
	 * 		EQUALS 
	 * 
	 * 		When adding a current customer to a flight, the HashSet of the flight reflects this 
	 * 
	 * 
	 * 			
	*/
	void addCustomerToFlight() {
	}



}
