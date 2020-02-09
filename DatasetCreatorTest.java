import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * JUnit tests to verify the reliability of the DatasetCreator class. 
 * 
 * @authors
 * Edi Edimov ee21@hw.ac.uk<br>
 * David Gladwell drg1@hw.ac.uk<br>
 * Alex Swift as184@hw.ac.uk<br>
 * Niko Tether nst2@hw.ac.uk
 * @since 9/02/2020
 */
@SuppressWarnings("static-access")
class DatasetCreatorTest {
	
	protected DatasetCreator dc;
	
	@BeforeEach
	protected void Setup()
	{
		dc = new DatasetCreator();
	}
	
	// ============= Input Variable Tests
	/**Checks the reliability of the ValidVariablesCheck() method by changing the flightCount variable*/
	@Test
	protected void FlightCountInputTest()
	{
		try { dc.ValidVariablesCheck(); }
		catch (InvalidValueInDatasetCreatorException e) { fail("Default Variables are invalid - this should not happen: " + e.getMessage()); }
		
		dc.flightCount = 100;
		try { dc.ValidVariablesCheck(); }
		catch (InvalidValueInDatasetCreatorException e) { fail("flightCount = 100 should be valid"); }
		
		dc.flightCount = 40000;
		try { dc.ValidVariablesCheck(); }
		catch (InvalidValueInDatasetCreatorException e) { fail("flightCount = 40000 should be valid"); }
		
		dc.flightCount = 0;
		String expected1 = "Invalid input variables detected: flightCount must be greater than zero";
		InvalidValueInDatasetCreatorException e1 = assertThrows(InvalidValueInDatasetCreatorException.class, () -> { dc.ValidVariablesCheck(); });
		String actual1 = e1.getMessage();
		assertTrue(actual1.equals(expected1), "\nExpected: " + expected1 + "\nActual: " + actual1);
		
		dc.flightCount = -100;
		String expected2 = "Invalid input variables detected: flightCount must be greater than zero";
		InvalidValueInDatasetCreatorException e2 = assertThrows(InvalidValueInDatasetCreatorException.class, () -> { dc.ValidVariablesCheck(); });
		String actual2 = e2.getMessage();
		assertTrue(actual2.equals(expected2), "\nExpected: " + expected2 + "\nActual: " + actual2);
	}
	
	/**Checks the reliability of the ValidVariablesCheck() method by changing the customerCount variable*/
	@Test
	protected void CustomerCountInputTest()
	{
		try { dc.ValidVariablesCheck(); }
		catch (InvalidValueInDatasetCreatorException e) { fail("Default Variables are invalid - this should not happen: " + e.getMessage()); }
		
		dc.customerCount = 100;
		try { dc.ValidVariablesCheck(); }
		catch (InvalidValueInDatasetCreatorException e) { fail("customerCount = 100 should be valid"); }
		
		dc.customerCount = 40000;
		try { dc.ValidVariablesCheck(); }
		catch (InvalidValueInDatasetCreatorException e) { fail("customerCount = 40000 should be valid"); }
		
		dc.customerCount = 0;
		String expected1 = "Invalid input variables detected: customerCount must be greater than zero";
		InvalidValueInDatasetCreatorException e1 = assertThrows(InvalidValueInDatasetCreatorException.class, () -> { dc.ValidVariablesCheck(); });
		String actual1 = e1.getMessage();
		assertTrue(actual1.equals(expected1), "\nExpected: " + expected1 + "\nActual: " + actual1);
		
		dc.customerCount = -100;
		String expected2 = "Invalid input variables detected: customerCount must be greater than zero";
		InvalidValueInDatasetCreatorException e2 = assertThrows(InvalidValueInDatasetCreatorException.class, () -> { dc.ValidVariablesCheck(); });
		String actual2 = e2.getMessage();
		assertTrue(actual2.equals(expected2), "\nExpected: " + expected2 + "\nActual: " + actual2);
	}
	
	/**Checks the reliability of the ValidVariablesCheck() method by changing the places variable*/
	@Test
	protected void PlacesInputTest()
	{
		try { dc.ValidVariablesCheck(); }
		catch (InvalidValueInDatasetCreatorException e) { fail("Default Variables are invalid - this should not happen: " + e.getMessage()); }
		
		dc.places = new String[] {"test"};
		try { dc.ValidVariablesCheck(); }
		catch (InvalidValueInDatasetCreatorException e) { fail("places: single cell array should be valid"); }
		
		dc.places = new String[0];
		String expected1 = "Invalid input variables detected: places must not be empty";
		InvalidValueInDatasetCreatorException e1 = assertThrows(InvalidValueInDatasetCreatorException.class, () -> { dc.ValidVariablesCheck(); });
		String actual1 = e1.getMessage();
		assertTrue(actual1.equals(expected1), "\nExpected: " + expected1 + "\nActual: " + actual1);
		
		dc.places = new String[1];
		String expected2 = "Invalid input variables detected: places contains empty cells";
		InvalidValueInDatasetCreatorException e2 = assertThrows(InvalidValueInDatasetCreatorException.class, () -> { dc.ValidVariablesCheck(); });
		String actual2 = e2.getMessage();
		assertTrue(actual2.equals(expected2), "\nExpected: " + expected2 + "\nActual: " + actual2);
	}
	
	/**Checks the reliability of the ValidVariablesCheck() method by changing the placeCodes variable*/
	@Test
	protected void PlaceCodesInputTest()
	{
		try { dc.ValidVariablesCheck(); }
		catch (InvalidValueInDatasetCreatorException e) { fail("Default Variables are invalid - this should not happen: " + e.getMessage()); }
		
		dc.placeCodes = new String[] {"test"};
		try { dc.ValidVariablesCheck(); }
		catch (InvalidValueInDatasetCreatorException e) { fail("placeCodes: single cell array should be valid"); }
		
		dc.placeCodes = new String[0];
		String expected1 = "Invalid input variables detected: placeCodes must not be empty";
		InvalidValueInDatasetCreatorException e1 = assertThrows(InvalidValueInDatasetCreatorException.class, () -> { dc.ValidVariablesCheck(); });
		String actual1 = e1.getMessage();
		assertTrue(actual1.equals(expected1), "\nExpected: " + expected1 + "\nActual: " + actual1);
		
		dc.placeCodes = new String[2];
		String expected2 = "Invalid input variables detected: placeCodes contains empty cells";
		InvalidValueInDatasetCreatorException e2 = assertThrows(InvalidValueInDatasetCreatorException.class, () -> { dc.ValidVariablesCheck(); });
		String actual2 = e2.getMessage();
		assertTrue(actual2.equals(expected2), "\nExpected: " + expected2 + "\nActual: " + actual2);
	}
	
	/**Checks the reliability of the ValidVariablesCheck() method by changing the carriers variable*/
	@Test
	protected void CarriersInputTest()
	{
		try { dc.ValidVariablesCheck(); }
		catch (InvalidValueInDatasetCreatorException e) { fail("Default Variables are invalid - this should not happen: " + e.getMessage()); }
		
		dc.carriers = new String[] {"test"};
		try { dc.ValidVariablesCheck(); }
		catch (InvalidValueInDatasetCreatorException e) { fail("carriers: single cell array should be valid"); }
		
		dc.carriers = new String[0];
		String expected1 = "Invalid input variables detected: carriers must not be empty";
		InvalidValueInDatasetCreatorException e1 = assertThrows(InvalidValueInDatasetCreatorException.class, () -> { dc.ValidVariablesCheck(); });
		String actual1 = e1.getMessage();
		assertTrue(actual1.equals(expected1), "\nExpected: " + expected1 + "\nActual: " + actual1);
		
		dc.carriers = new String[3];
		String expected2 = "Invalid input variables detected: carriers contains empty cells";
		InvalidValueInDatasetCreatorException e2 = assertThrows(InvalidValueInDatasetCreatorException.class, () -> { dc.ValidVariablesCheck(); });
		String actual2 = e2.getMessage();
		assertTrue(actual2.equals(expected2), "\nExpected: " + expected2 + "\nActual: " + actual2);
	}
	
	/**Checks the reliability of the ValidVariablesCheck() method by changing the firstNames variable*/
	@Test
	protected void FirstNamesInputTest()
	{
		try { dc.ValidVariablesCheck(); }
		catch (InvalidValueInDatasetCreatorException e) { fail("Default Variables are invalid - this should not happen: " + e.getMessage()); }
		
		dc.firstNames = new String[] {"test"};
		try { dc.ValidVariablesCheck(); }
		catch (InvalidValueInDatasetCreatorException e) { fail("firstNames: single cell array should be valid"); }
		
		dc.firstNames = new String[0];
		String expected1 = "Invalid input variables detected: firstNames must not be empty";
		InvalidValueInDatasetCreatorException e1 = assertThrows(InvalidValueInDatasetCreatorException.class, () -> { dc.ValidVariablesCheck(); });
		String actual1 = e1.getMessage();
		assertTrue(actual1.equals(expected1), "\nExpected: " + expected1 + "\nActual: " + actual1);
		
		dc.firstNames = new String[4];
		String expected2 = "Invalid input variables detected: firstNames contains empty cells";
		InvalidValueInDatasetCreatorException e2 = assertThrows(InvalidValueInDatasetCreatorException.class, () -> { dc.ValidVariablesCheck(); });
		String actual2 = e2.getMessage();
		assertTrue(actual2.equals(expected2), "\nExpected: " + expected2 + "\nActual: " + actual2);
	}
	
	/**Checks the reliability of the ValidVariablesCheck() method by changing the lastNames variable*/
	@Test
	protected void LastNamesInputTest()
	{
		try { dc.ValidVariablesCheck(); }
		catch (InvalidValueInDatasetCreatorException e) { fail("Default Variables are invalid - this should not happen: " + e.getMessage()); }
		
		dc.lastNames = new String[] {"test"};
		try { dc.ValidVariablesCheck(); }
		catch (InvalidValueInDatasetCreatorException e) { fail("lastNames: single cell array should be valid"); }
		
		dc.lastNames = new String[0];
		String expected1 = "Invalid input variables detected: lastNames must not be empty";
		InvalidValueInDatasetCreatorException e1 = assertThrows(InvalidValueInDatasetCreatorException.class, () -> { dc.ValidVariablesCheck(); });
		String actual1 = e1.getMessage();
		assertTrue(actual1.equals(expected1), "\nExpected: " + expected1 + "\nActual: " + actual1);
		
		dc.lastNames = new String[5];
		String expected2 = "Invalid input variables detected: lastNames contains empty cells";
		InvalidValueInDatasetCreatorException e2 = assertThrows(InvalidValueInDatasetCreatorException.class, () -> { dc.ValidVariablesCheck(); });
		String actual2 = e2.getMessage();
		assertTrue(actual2.equals(expected2), "\nExpected: " + expected2 + "\nActual: " + actual2);
	}
	
	/**Checks the reliability of the ValidVariablesCheck() method by creating multiple errors and ensuring they are returned correctly*/
	@Test
	protected void MultipleInputTest()
	{
		try { dc.ValidVariablesCheck(); }
		catch (InvalidValueInDatasetCreatorException e) { fail("Default Variables are invalid - this should not happen: " + e.getMessage()); }
		
		dc.flightCount = -100;
		dc.places = new String[0];
		dc.firstNames = new String[3];
		String expected1 = "Invalid input variables detected: flightCount must be greater than zero, places must not be empty, firstNames contains empty cells";
		InvalidValueInDatasetCreatorException e1 = assertThrows(InvalidValueInDatasetCreatorException.class, () -> { dc.ValidVariablesCheck(); });
		String actual1 = e1.getMessage();
		assertTrue(actual1.equals(expected1), "\nExpected: " + expected1 + "\nActual: " + actual1);
		
		Setup();
		dc.customerCount = -100;
		dc.placeCodes = new String[6];
		dc.carriers = new String[7];
		dc.lastNames = new String[0];
		String expected2 = "Invalid input variables detected: customerCount must be greater than zero, placeCodes contains empty cells, carriers contains empty cells, lastNames must not be empty";
		InvalidValueInDatasetCreatorException e2 = assertThrows(InvalidValueInDatasetCreatorException.class, () -> { dc.ValidVariablesCheck(); });
		String actual2 = e2.getMessage();
		assertTrue(actual2.equals(expected2), "\nExpected: " + expected2 + "\nActual: " + actual2);
	}
	// =============
	
	// ============= Data Creation Tests
	/**Ensures that the generated flight entry is valid*/
	@Test
	protected void NewFlightTest()
	{
		String s = dc.NewFlight();
		String[] flight = s.split(",");
		if (flight.length != 7) fail("Incorrect format" + s);
		else if (!flight[2].contains("-")) fail("2: Does not contain a flight code: " + s);
		int I = 4;
		try
		{
			Integer.parseInt(flight[I]); I = 5;
			Float.parseFloat(flight[I]); I = 6;
			Float.parseFloat(flight[I]);
		}
		catch (NumberFormatException e)
		{
			switch (I)
			{
			case 4: fail("4: Capacity variable not an integer: " + s);
			case 5: fail("5: Weight variable not a float: " + s);
			case 6: fail("6: Volume variable not a float: " + s);
			default: fail("UNKNOWN ERROR");
			}
		}
	}
	
	/**Ensures that the generated customer entry is valid*/
	@Test
	protected void NewCustomerTest()
	{
		dc.flightCodes.put("SRT-END-", 0);
		String s = dc.NewCustomer();
		String[] customer = s.split(",");
		if (customer.length != 7) fail("Incorrect format: " + s);
		else if (!customer[3].contains("-")) fail("3: Does not contain a flight code: " + s);
		int I = 0;
		try
		{
			Integer.parseInt(customer[I]); 	   I = 4;
			Boolean.parseBoolean(customer[I]); I = 5;
			Float.parseFloat(customer[I]); 	   I = 6;
			Float.parseFloat(customer[I]);
		}
		catch (NumberFormatException e)
		{
			switch (I)
			{
			case 0: fail("0: Ref. code variable not an integer: " + s);
			case 4: fail("0: checked-in variable not a boolean: " + s);
			case 5: fail("5: Weight variable not a float: " + s);
			case 6: fail("6: Volume variable not a float: " + s);
			default: fail("UNKNOWN ERROR: " + s);
			}
		}
	}
	// =============
}
