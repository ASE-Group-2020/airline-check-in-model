package tests;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import airlineinterface.*;
import exceptions.*;

class FlightTest {

	@Test
	// tests that the Flight constructor only accepts valid variables and throws an InvalidValueException when an invalid value is given
	void constructorTest()
	{
		try { new Flight("departure", "destination", "flightRef", "carrier", 1, 2, 3, 4); } catch (InvalidValueException e) { fail("these values should be valid"); }		
		assertThrows(InvalidValueException.class, () -> { new Flight(""			, "destination", "flightRef", "carrier",  1,  2,  3, 4); });
		assertThrows(InvalidValueException.class, () -> { new Flight("departure", ""		   , "flightRef", "carrier",  1,  2,  3, 4); });
		assertThrows(InvalidValueException.class, () -> { new Flight("departure", "destination", ""			, "carrier",  1,  2,  3, 4); });
		assertThrows(InvalidValueException.class, () -> { new Flight("departure", "destination", "flightRef", ""	   ,  1,  2,  3, 4); });
		assertThrows(InvalidValueException.class, () -> { new Flight("departure", "destination", "flightRef", "carrier",  0,  2,  3, 4); });
		assertThrows(InvalidValueException.class, () -> { new Flight("departure", "destination", "flightRef", "carrier", -1,  2,  3, 4); });
		assertThrows(InvalidValueException.class, () -> { new Flight("departure", "destination", "flightRef", "carrier",  1, -1,  3, 4); });
		assertThrows(InvalidValueException.class, () -> { new Flight("departure", "destination", "flightRef", "carrier",  1,  2, -1, 4); });
		assertThrows(InvalidValueException.class, () -> { new Flight(null		, "destination", "flightRef", "carrier",  1,  2,  3, 4); });
		assertThrows(InvalidValueException.class, () -> { new Flight("departure", null		   , "flightRef", "carrier",  1,  2,  3, 4); });
		assertThrows(InvalidValueException.class, () -> { new Flight("departure", "destination", null		, "carrier",  1,  2,  3, 4); });
		assertThrows(InvalidValueException.class, () -> { new Flight("departure", "destination", "flightRef", null	   ,  1,  2,  3, 4); });
	}  
	
	@Test
	// tests that new customers are added to the flight passenger list, indicates when a passenger has already been added to the flight,
	// and throws an InvalidValueException when a customer tries to check into the wrong flight
	void addCustomerTest()
	{
		try
		{
			Flight f = new Flight("departure", "destination", "flightRef", "carrier", 1, 2, 3, 4);
			Customer c1 = new Customer("1", "first1", "last1", "flightRef", "standard", 1, 2, 3, 4);
			Customer c2 = new Customer("1", "first1", "last1", "flightRef", "business", 1, 2, 3, 4);
			
			try { assertTrue(f.addCustomer(c1, 0)); } catch (AlreadyCheckedInException e) { fail("customer should not already be checked in"); }
			try { assertTrue(f.addCustomer(c2, 0)); } catch (AlreadyCheckedInException e) { fail("customer should not already be checked in"); }
			assertThrows(AlreadyCheckedInException.class, () -> {f.addCustomer(c1, 0);});
		}
		catch (InvalidValueException e) { fail("these values should be valid"); }
	}
	
	@Test
	// adds customers to the flight's passenger list and ensures the getCustomerSumAttribute method outputs the current total of passengers, luggage weight, and luggage volume correctly
	void getCustomerSumAttributeTest()
	{
		try
		{
			Flight f = new Flight("departure", "destination", "flightRef", "carrier", 4, 10, 6, 4);
			Customer c1 = new Customer("1", "first1", "last1", "flightRef", "standard", 2, 3, 3, 4);
			Customer c2 = new Customer("2", "first2", "last2", "flightRef", "standard", 2, 2, 1, 4);
			Customer c3 = new Customer("3", "first3", "last3", "flightRef", "business", 2, 3, 3, 5);
			Customer c4 = new Customer("4", "first4", "last4", "flightRef", "business", 2, 3, 2, 1);
			
			f.addCustomer(c1, 0);
			f.addCustomer(c2, 0);
			f.addCustomer(c3, 0); 
			
			float[] details1 = f.getCurrentAttributes();
			assertTrue(details1.length == 3, "getCustomerSumAttributes() must output an array of length 3");
			assertEquals(3,details1[0]); // how many passengers are there
			assertEquals(6,details1[1]); // total weight of customer's baggage
			assertEquals(89,details1[2]); // total volume of customer's baggage
			
			f.addCustomer(c4, 0);
			float[] details2 = f.getCurrentAttributes();
			assertTrue(details2.length == 3, "getCustomerSumAttributes() must output an array of length 3");
			assertEquals(4,details2[0]); // how many passengers are there
			assertEquals(8,details2[1]); // total weight of customer's baggage
			assertEquals(95,details2[2]); // total volume of customer's baggage
		}
		catch (InvalidValueException e) { fail("these values should be valid"); }
		catch (AlreadyCheckedInException e) { fail("customer should not already be checked in"); }
	}
}
