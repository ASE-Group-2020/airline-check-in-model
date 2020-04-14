package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import airlineinterface.Customer;
import airlineinterface.Desk;
import airlineinterface.Flight;
import airlineinterface.Logger;
import airlineinterface.Simulator;
import airlineinterface.WaitingQueue;
import exceptions.InvalidValueException;

/* This test unit will test a large chunk of code. The main purpose is to check if threading is working correctly. 
*  However issues can arise if there are other mistakes in the code.
*/

public class ThreadingTest {

	

	/*
	 * Other potential customers
	 * 
	 * Customer damienCust = new Customer("789", "damien", "Le Court",
	 * "LON-EDI-0");//, false, 23, 1); Customer paulCust = new Customer("1011",
	 * "Paul", "Dr.Jean-Francois", "LON-EDI-0");
	 */

	// Set of queues
	Queue<Customer> testQ1;
	// Queue<Customer> testQ2;
	// Queue<Customer> testQ3;
	// Queue<Customer> testQ4;
	// Queue<Customer> testQ5;

	// Create pointer for Flight
	Flight LON1;
	Flight PAR2;

	// Create pointer to a list of Flight objects
	List<Flight> flights;

	// Create pointers for Desks
	Desk d1;
	Desk d2;
	Desk d3;
	Desk d4;
	Desk d5;

	// Create pointers for WaitingQueues
	WaitingQueue q1;
	// WaitingQueue q2;
	// WaitingQueue q3;
	// WaitingQueue q4;
	// WaitingQueue q5;

	@SuppressWarnings("static-access")
	@BeforeEach
	void setup() throws InvalidValueException {
		
		Logger.instance().resetTimer();		
		
		// Create pointer to customer objects and lists

		Customer adamCust1 = new Customer("1", "adam", "smith", "LON-EDI-0", 10.0f, 52.0f);// , false, 82, 112);
		Customer adamCust2 = new Customer("2", "adam", "smith", "LON-EDI-0", 10.0f, 52.0f);// , false, 82, 112);
		Customer adamCust3 = new Customer("3", "adam", "smith", "LON-EDI-0", 10.0f, 52.0f);// , false, 82, 112);
		Customer adamCust4 = new Customer("4", "adam", "smith", "LON-EDI-0", 10.0f, 52.0f);// , false, 82, 112);
		Customer adamCust5 = new Customer("5", "adam", "smith", "LON-EDI-0", 10.0f, 52.0f);// , false, 82, 112);
		Customer adamCust6 = new Customer("6", "adam", "smith", "LON-EDI-0", 10.0f, 52.0f);// , false, 82, 112);
		Customer adamCust7 = new Customer("7", "adam", "smith", "LON-EDI-0", 10.0f, 52.0f);// , false, 82, 112);
		Customer adamCust8 = new Customer("8", "adam", "smith", "LON-EDI-0", 10.0f, 52.0f);// , false, 82, 112);
		Customer adamCust9 = new Customer("9", "adam", "smith", "LON-EDI-0", 10.0f, 52.0f);// , false, 82, 112);
		Customer adamCust10 = new Customer("10", "adam", "smith", "LON-EDI-0", 10.0f, 52.0f);// , false, 82, 112);
		Customer adamCust11 = new Customer("11", "adam", "smith", "LON-EDI-0", 10.0f, 52.0f);// , false, 82, 112);
		Customer adamCust12 = new Customer("12", "adam", "smith", "LON-EDI-0", 10.0f, 52.0f);// , false, 82, 112);
		Customer adamCust13 = new Customer("13", "adam", "smith", "LON-EDI-0", 10.0f, 52.0f);// , false, 82, 112);
		Customer adamCust14 = new Customer("14", "adam", "smith", "LON-EDI-0", 10.0f, 52.0f);// , false, 82, 112);
		Customer adamCust15 = new Customer("15", "adam", "smith", "LON-EDI-0", 10.0f, 52.0f);// , false, 82, 112);
		Customer adamCust16 = new Customer("16", "adam", "smith", "LON-EDI-0", 10.0f, 52.0f);// , false, 82, 112);
		Customer adamCust17 = new Customer("17", "adam", "smith", "LON-EDI-0", 10.0f, 52.0f);// , false, 82, 112);
		Customer adamCust18 = new Customer("18", "adam", "smith", "LON-EDI-0", 10.0f, 52.0f);// , false, 82, 112);
		Customer adamCust19 = new Customer("19", "adam", "smith", "LON-EDI-0", 10.0f, 52.0f);// , false, 82, 112);
		Customer adamCust20 = new Customer("20", "adam", "smith", "LON-EDI-0", 10.0f, 52.0f);// , false, 82, 112);
		Customer adamCust21 = new Customer("21", "adam", "smith", "LON-EDI-0", 10.0f, 52.0f);// , false, 82, 112);
		Customer adamCust22 = new Customer("22", "adam", "smith", "LON-EDI-0", 10.0f, 52.0f);// , false, 82, 112);
		Customer adamCust23 = new Customer("23", "adam", "smith", "LON-EDI-0", 10.0f, 52.0f);// , false, 82, 112);
		Customer adamCust24 = new Customer("24", "adam", "smith", "LON-EDI-0", 10.0f, 52.0f);// , false, 82, 112);
		Customer adamCust25 = new Customer("25", "adam", "smith", "LON-EDI-0", 10.0f, 52.0f);// , false, 82, 112);

		Customer marieCust1 = new Customer("26", "maire", "curie", "PAR-BER-2", 26.0f, 132.0f);// , false, 60, 24);
		Customer marieCust2 = new Customer("27", "maire", "curie", "PAR-BER-2", 26.0f, 132.0f);// , false, 60, 24);
		Customer marieCust3 = new Customer("28", "maire", "curie", "PAR-BER-2", 26.0f, 132.0f);// , false, 60, 24);
		Customer marieCust4 = new Customer("29", "maire", "curie", "PAR-BER-2", 26.0f, 132.0f);// , false, 60, 24);
		Customer marieCust5 = new Customer("30", "maire", "curie", "PAR-BER-2", 26.0f, 132.0f);// , false, 60, 24);
		Customer marieCust6 = new Customer("31", "maire", "curie", "PAR-BER-2", 26.0f, 132.0f);// , false, 60, 24);
		Customer marieCust7 = new Customer("32", "maire", "curie", "PAR-BER-2", 26.0f, 132.0f);// , false, 60, 24);
		Customer marieCust8 = new Customer("33", "maire", "curie", "PAR-BER-2", 26.0f, 132.0f);// , false, 60, 24);
		Customer marieCust9 = new Customer("34", "maire", "curie", "PAR-BER-2", 26.0f, 132.0f);// , false, 60, 24);
		Customer marieCust10 = new Customer("35", "maire", "curie", "PAR-BER-2", 26.0f, 132.0f);// , false, 60, 24);
		Customer marieCust11 = new Customer("36", "maire", "curie", "PAR-BER-2", 26.0f, 132.0f);// , false, 60, 24);
		Customer marieCust12 = new Customer("37", "maire", "curie", "PAR-BER-2", 26.0f, 132.0f);// , false, 60, 24);
		Customer marieCust13 = new Customer("38", "maire", "curie", "PAR-BER-2", 26.0f, 132.0f);// , false, 60, 24);
		Customer marieCust14 = new Customer("39", "maire", "curie", "PAR-BER-2", 26.0f, 132.0f);// , false, 60, 24);
		Customer marieCust15 = new Customer("40", "maire", "curie", "PAR-BER-2", 26.0f, 132.0f);// , false, 60, 24);
		Customer marieCust16 = new Customer("41", "maire", "curie", "PAR-BER-2", 26.0f, 132.0f);// , false, 60, 24);
		Customer marieCust17 = new Customer("42", "maire", "curie", "PAR-BER-2", 26.0f, 132.0f);// , false, 60, 24);
		Customer marieCust18 = new Customer("43", "maire", "curie", "PAR-BER-2", 26.0f, 132.0f);// , false, 60, 24);
		Customer marieCust19 = new Customer("44", "maire", "curie", "PAR-BER-2", 26.0f, 132.0f);// , false, 60, 24);
		Customer marieCust20 = new Customer("45", "maire", "curie", "PAR-BER-2", 26.0f, 132.0f);// , false, 60, 24);
		Customer marieCust21 = new Customer("46", "maire", "curie", "PAR-BER-2", 26.0f, 132.0f);// , false, 60, 24);
		Customer marieCust22 = new Customer("47", "maire", "curie", "PAR-BER-2", 26.0f, 132.0f);// , false, 60, 24);
		Customer marieCust23 = new Customer("48", "maire", "curie", "PAR-BER-2", 26.0f, 132.0f);// , false, 60, 24);
		Customer marieCust24 = new Customer("49", "maire", "curie", "PAR-BER-2", 26.0f, 132.0f);// , false, 60, 24);
		Customer marieCust25 = new Customer("50", "maire", "curie", "PAR-BER-2", 26.0f, 132.0f);// , false, 60, 24);
		
		testQ1 = new LinkedList<Customer>();
		flights = new LinkedList<Flight>();

		testQ1.add(adamCust1);
		testQ1.add(adamCust2);
		testQ1.add(adamCust3);
		testQ1.add(adamCust4);
		testQ1.add(adamCust5);
		testQ1.add(adamCust6);
		testQ1.add(adamCust7);
		testQ1.add(adamCust8);
		testQ1.add(adamCust9);
		testQ1.add(adamCust10);
		testQ1.add(adamCust11);
		testQ1.add(adamCust12);
		testQ1.add(adamCust13);
		testQ1.add(adamCust14);
		testQ1.add(adamCust15);
		testQ1.add(adamCust16);
		testQ1.add(adamCust17);
		testQ1.add(adamCust18);
		testQ1.add(adamCust19);
		testQ1.add(adamCust20);
		testQ1.add(adamCust21);
		testQ1.add(adamCust22);
		testQ1.add(adamCust23);
		testQ1.add(adamCust24);
		testQ1.add(adamCust25);
		testQ1.add(marieCust1);
		testQ1.add(marieCust2);
		testQ1.add(marieCust3);
		testQ1.add(marieCust4);
		testQ1.add(marieCust5);
		testQ1.add(marieCust6);
		testQ1.add(marieCust7);
		testQ1.add(marieCust8);
		testQ1.add(marieCust9);
		testQ1.add(marieCust10);
		testQ1.add(marieCust11);
		testQ1.add(marieCust12);
		testQ1.add(marieCust13);
		testQ1.add(marieCust14);
		testQ1.add(marieCust15);
		testQ1.add(marieCust16);
		testQ1.add(marieCust17);
		testQ1.add(marieCust18);
		testQ1.add(marieCust19);
		testQ1.add(marieCust20);
		testQ1.add(marieCust21);
		testQ1.add(marieCust22);
		testQ1.add(marieCust23);
		testQ1.add(marieCust24);
		testQ1.add(marieCust25);

		// Create a new flight object
		LON1 = new Flight("LON", "EDI", "LON-EDI-0", "RayanAir", 30, 10000, 300000);
		PAR2 = new Flight("PAR", "BER", "PAR-BER-2", "EasyJet", 30, 10000, 300000);

		flights.add(LON1);
		flights.add(PAR2);

		this.q1 = new WaitingQueue();
		// add a linkedlist of customers to WaitingQueue obj
		q1.setWaiting(testQ1);

		this.d1 = new Desk(q1, "desk1");
		this.d2 = new Desk(q1, "desk2");
		this.d3 = new Desk(q1, "desk3");
		this.d4 = new Desk(q1, "desk4");
		this.d5 = new Desk(q1, "desk5");
		// add flight obj to the flights list
		d1.addFlights(flights);
		d2.addFlights(flights);
		d3.addFlights(flights);
		d4.addFlights(flights);
		d5.addFlights(flights);
	}

	/*
	 * @Test
	 *//**
		 * test if: EQUALS
		 * 
		 * When the queue has a customer When the queue doesn't have a queue
		 * 
		 * 
		 *//*
			 * void runTest() {
			 * 
			 * //check that the if statement works correctly Customer c = q.getNext();
			 * assertTrue(!(c == null)); //There is a next customer
			 * 
			 * //reset the waitingQueue and desk with an empty queue q.setWaiting(emptyQ);
			 * this.d = new Desk(q, "desk1");
			 * 
			 * //check that the if statement works correctly c = q.getNext(); assertTrue(c
			 * == null); //There isn't a next customer
			 * 
			 * }
			 */

	@Test
	/**Check if 5 desks can pull from one queue of people and put them into two flights correctly
	 * 
	 * 
	 * Will make sure the correct number of people are on board a flight, 25 ppl for the RayanAir flight and 25 ppl for the EasyJet Flight.
	 * If this isn't the result the code has issue. Potentially threading issues. By repeating the test numerous times and checking if the issue appears 
	 * inconsistently, we can conclude if threading is a problem.  
	 * 
	 */
	void fiveDeskThreadTest() {
		Thread threadQueue1 = new Thread(q1);								// Start threads
		threadQueue1.start();
		Thread threadDesk1 = new Thread(d1);
		threadDesk1.start();
		Thread threadDesk2 = new Thread(d2);
		threadDesk2.start();
		Thread threadDesk3 = new Thread(d3);
		threadDesk3.start();
		Thread threadDesk4 = new Thread(d4);
		threadDesk4.start();
		Thread threadDesk5 = new Thread(d5);
		threadDesk5.start();
		
		System.out.println("London outbond flight number of passangers: "+ LON1.customers.size());
		System.out.println("Paris outbound flight number of passangers: "+ PAR2.customers.size());
	}
}
