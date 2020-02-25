package tests;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import airlineinterface.Customer;
import airlineinterface.Flight;
import airlineinterface.Master;
import exceptions.InvalidValueException;

class MasterTest {
	
	Master m;
	Customer adamCust;
	Customer marieCust;
	Flight LON123;
	@BeforeEach
	void setup() throws InvalidValueException {
		m = new Master();
		adamCust = new Customer("123", "adam", "smith", "LON-EDI-0", false, 82,  112);
		marieCust = new Customer("456", "maire", "curie", "LON-EDI-0", false, 60,  24);
		m.addCustomer(adamCust);
		m.addCustomer(marieCust);
		LON123 = new Flight("LON", "EDI", "LON-EDI-0", "RayanAir", 300, 10000, 300000);
		m.addFlight(LON123); 
	}
	
	@Test
	/** test if:
	 * 		when a Customer exists in the allCustomers HashMap the method returns the Customer
	 *      check that getCustomer is case-insensitive
	 *      // check that getCustomer is space-insensitive
	 * 		when a Customer doesn't exist in the allCustomers HahsMap the method returns null
	 * 		when the Customer last name doesn't match the reference code
	 *      when the Customer input name has spaces at the beginning, getCustomer ignores them 
	 *       *      when the Customer input name has spaces at the end, getCustomer ignores them 
	*/
	void getCustomerTest() throws InvalidValueException{
		assertEquals(m.getCustomer("123", "smith"),adamCust,"The method has failed to retrieve a Customer");
		assertEquals(m.getCustomer("456", "CuRIe"),marieCust,"The method has failed to retrieve a Customer");
		assertEquals(m.getCustomer("789", "Ceasar"),null,"The method has failed to retrieve a Customer");
		assertEquals(m.getCustomer("123", "ImNotSmith"),null,"the method has faild to return a null "
				+ "when the last name doesn't match an exsiting customerKey");
		#assertEquals()
	}
	
	@Test
	/** test if:
	 * 		when a Flight exists in the allFlights HashMap the method returns the Flight
	 * 		when a Flight doesn't exist in the allFlights HahsMap the method returns null
	*/
	void getFlightTest(){
			assertEquals(m.getFlight("LON-EDI-0"),LON123,"The method has failed to retrieve an exsisting flight object");
			assertEquals(m.getFlight("SUN-MOON-35"),null,"The method has failed to correctly return a null when asked to"
					+ "retrieve a none exsistant flight");
	}
	
 
}
