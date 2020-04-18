package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

import airlineinterface.Desk;
import airlineinterface.Logger;
import airlineinterface.Simulator;
import airlineinterface.Flight;
/* This test unit will test a large chunk of code. The main purpose is to check if threading is working correctly. 
*  However issues can arise if there are other mistakes in the code.
*/



public class ThreadingTest {

	public Simulator sim;
	public int num;
	
	@BeforeEach
	void setup(){
		Logger.instance().resetTimer();									// Start logger
		
	    sim = Simulator.get();											// the argument is the number of desks instantiated 

	    sim.addDesks(3);
		sim.addFlightsFromFile("dataFlight-Testc.csv");
		sim.addCustomersFromFile("dataCustomer-Testc.csv");

		//sim.makeCustomersArrive(5);									// Delays the arrival of customers
		
		sim.start(100000, 4, true);										// (simSpeed, runTime, randomness)
		this.num = 12;
	}


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
		//for(Desk d : sim.allDesks) {		// Add all flights to guiController
		int num = 20;
		for(Flight f : sim.allFlights) {
			System.out.println("the number of people in flight: " + f.customers.size());
			assertEquals("The code has failed to correctly place the queing people into both flights.",f.customers.size(),num);
		}
		
	}
	
}
