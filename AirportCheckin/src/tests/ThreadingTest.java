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

	

	void setup(){
		Logger.instance().resetTimer();									// Start logger
		
		Simulator sim = new Simulator(3);								// the argument is the number of desks instantiated 

		sim.readFlightsFromFile("dataFlight-40c.csv");
		sim.readCustomersFromFile("dataCustomer-40c.csv");

		//sim.makeCustomersArrive(5);										// Delays the arrival of customers
		
		sim.start(1000, 120, false);										// (simSpeed, runTime, randomness)
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
		/*
		 * Thread threadQueue1 = new Thread(q1); // Start threads threadQueue1.start();
		 * Thread threadDesk1 = new Thread(d1); threadDesk1.start(); Thread threadDesk2
		 * = new Thread(d2); threadDesk2.start(); Thread threadDesk3 = new Thread(d3);
		 * threadDesk3.start(); Thread threadDesk4 = new Thread(d4);
		 * threadDesk4.start(); Thread threadDesk5 = new Thread(d5);
		 * threadDesk5.start();
		 * 
		 * System.out.println("London outbond flight number of passangers: "+
		 * LON1.customers.size());
		 * System.out.println("Paris outbound flight number of passangers: "+
		 * PAR2.customers.size());
		 */
	}
}
