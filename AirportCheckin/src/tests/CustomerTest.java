package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import airlineinterface.*;
import exceptions.*;

class CustomerTest {

	@Test
	// tests that the Customer constructor only accepts valid variables and throws an InvalidValueException when an invalid value is given
	void constructorTest()
	{
		try { new Customer("code", "first", "last", "flightCode"); } catch (InvalidValueException e) { fail("these values should be valid"); }		
		assertThrows(InvalidValueException.class, () -> { new Customer(""	 , "first", "last", "flightCode"); }); // empty code
		assertThrows(InvalidValueException.class, () -> { new Customer("code", ""	  , "last", "flightCode"); }); // empty firstName
		assertThrows(InvalidValueException.class, () -> { new Customer("code", "first", ""	  , "flightCode"); }); // empty lastName
		assertThrows(InvalidValueException.class, () -> { new Customer("code", "first", "last", ""			); }); // empty flightCode
		
		assertThrows(InvalidValueException.class, () -> { new Customer(null	 , "first", "last", "flightCode"); }); // empty code
		assertThrows(InvalidValueException.class, () -> { new Customer("code", null	  , "last", "flightCode"); }); // empty firstName
		assertThrows(InvalidValueException.class, () -> { new Customer("code", "first", null  , "flightCode"); }); // empty lastName
		assertThrows(InvalidValueException.class, () -> { new Customer("code", "first", "last", null		); }); // empty flightCode
	}
	
	@Test
	// tests that the customer cannot check in with negative weight or volume values, that a customer who hasn't booked in can be booked in,
	// and throws an InvalidValueException when a customer tries to check in when they're already checked in
	void checkinTest()
	{
		try
		{
			Customer c = new Customer("code", "first", "last", "flightCode");
			assertFalse(c.isCheckedIn());
			
			assertThrows(InvalidValueException.class, () -> { c.setCheckedIn(-1,  1); });
			
			assertThrows(InvalidValueException.class, () -> { c.setCheckedIn( 1, -1); });
			
			try { c.setCheckedIn(1, 1); } catch (AlreadyCheckedInException e) { fail("false -> true should be valid"); }	
			assertTrue(c.isCheckedIn());
			
			assertThrows(AlreadyCheckedInException.class, () -> { c.setCheckedIn(1, 1); });
		}
		catch (InvalidValueException e) { fail("these values should be valid"); }
	}
}
