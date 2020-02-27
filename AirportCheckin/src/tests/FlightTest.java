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
		try { new Flight("departure", "destination", "flightRef", "carrier", 1, 2, 3); } catch (InvalidValueException e) { fail("these values should be valid"); }		
		assertThrows(InvalidValueException.class, () -> { new Flight(""			, "destination", "flightRef", "carrier",  1,  2,  3); });
		assertThrows(InvalidValueException.class, () -> { new Flight("departure", ""		   , "flightRef", "carrier",  1,  2,  3); });
		assertThrows(InvalidValueException.class, () -> { new Flight("departure", "destination", ""			, "carrier",  1,  2,  3); });
		assertThrows(InvalidValueException.class, () -> { new Flight("departure", "destination", "flightRef", ""	   ,  1,  2,  3); });
		assertThrows(InvalidValueException.class, () -> { new Flight("departure", "destination", "flightRef", "carrier",  0,  2,  3); });
		assertThrows(InvalidValueException.class, () -> { new Flight("departure", "destination", "flightRef", "carrier", -1,  2,  3); });
		assertThrows(InvalidValueException.class, () -> { new Flight("departure", "destination", "flightRef", "carrier",  1, -1,  3); });
		assertThrows(InvalidValueException.class, () -> { new Flight("departure", "destination", "flightRef", "carrier",  1,  2, -1); });
		assertThrows(InvalidValueException.class, () -> { new Flight(null		, "destination", "flightRef", "carrier",  1,  2,  3); });
		assertThrows(InvalidValueException.class, () -> { new Flight("departure", null		   , "flightRef", "carrier",  1,  2,  3); });
		assertThrows(InvalidValueException.class, () -> { new Flight("departure", "destination", null		, "carrier",  1,  2,  3); });
		assertThrows(InvalidValueException.class, () -> { new Flight("departure", "destination", "flightRef", null	   ,  1,  2,  3); });
	}  
	
	@Test
	// tests that new customers are added to the flight passenger list, indicates when a passenger has already been added to the flight,
	// and throws an InvalidValueException when a customer tries to check into the wrong flight
	void addCustomerTest()
	{
		try
		{
			Flight f = new Flight("departure", "destination", "flightRef", "carrier", 1, 2, 3);
			Customer c1 = new Customer("1", "first1", "last1", "flightRef");
			Customer c2 = new Customer("2", "first2", "last2", "flightRef");
			Customer c3 = new Customer("3", "first3", "last3", "invalidFlightRef");
			
			assertTrue(f.addCustomer(c1, 0));
			assertTrue(f.addCustomer(c2, 0));
			assertFalse(f.addCustomer(c1, 0));
			assertThrows(InvalidValueException.class, () -> { f.addCustomer(c3, 0); });
		}
		catch (InvalidValueException e) { fail("these values should be valid"); }
	}
	
	@Test
	// adds customers to the flight's passenger list and ensures the getCustomerSumAttribute method outputs the current total of passengers, luggage weight, and luggage volume correctly
	void getCustomerSumAttributeTest()
	{
		try
		{
			Flight f = new Flight("departure", "destination", "flightRef", "carrier", 4, 10, 6);
			Customer c1 = new Customer("1", "first1", "last1", "flightRef");
			Customer c2 = new Customer("2", "first2", "last2", "flightRef");
			Customer c3 = new Customer("3", "first3", "last3", "flightRef");
			Customer c4 = new Customer("4", "first4", "last4", "flightRef");
			
			f.addCustomer(c1, 0);
			c1.setCheckedIn(1, 1);
			f.addCustomer(c2, 0);
			c2.setCheckedIn(2, 2);
			f.addCustomer(c3, 0); 
			c3.setCheckedIn(3, 2);
			float[] details1 = f.getCustomerSumAttributes();
			assertTrue(details1.length == 3, "getCustomerSumAttributes() must output an array of length 3");
			assertEquals(3,details1[0]); // how many passengers are there
			assertEquals(6,details1[1]); // total weight of customer's baggage
			assertEquals(5,details1[2]); // total volume of customer's baggage
			
			f.addCustomer(c4, 0);
			float[] details2 = f.getCustomerSumAttributes();
			assertTrue(details2.length == 3, "getCustomerSumAttributes() must output an array of length 3");
			assertEquals(3,details2[0]); // how many passengers are there
			assertEquals(6,details2[1]); // total weight of customer's baggage
			assertEquals(5,details2[2]); // total volume of customer's baggage
			
			// ensures the flights weight and volume are updated accordingly when a customer checks in
			try { c4.setCheckedIn(2, 3); } catch (AlreadyCheckedInException e) { fail("these values should be valid"); }
			float[] details3 = f.getCustomerSumAttributes();
			assertTrue(details3.length == 3, "getCustomerSumAttributes() must output an array of length 3");
			assertEquals(4,details3[0]); // how many passengers are there
			assertEquals(8,details3[1]); // total weight of customer's baggage
			assertEquals(8,details3[2]); // total volume of customer's baggage
		}
		catch (InvalidValueException | AlreadyCheckedInException e) { fail("these values should be valid"); }
	}
}
