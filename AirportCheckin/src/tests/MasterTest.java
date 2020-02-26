package tests;

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
	Customer damienCust;
	Customer paulCust;
	Flight LON123;

	@BeforeEach
	void setup() throws InvalidValueException {
		m = new Master();
		adamCust = new Customer("123", "adam", "smith", "LON-EDI-0", false, 82,  112);
		marieCust = new Customer("456", "maire", "curie", "LON-EDI-0", false, 60,  24);
		damienCust = new Customer("789", "damien", "Le Court", "LON-EDI-0", false, 23,  1);
		paulCust = new Customer("1011", "Paul", "Dr.Jean-Francois", "LON-EDI-0", false, 0,  0);
		m.addCustomer(adamCust);
		m.addCustomer(marieCust);
		m.addCustomer(damienCust);
		m.addCustomer(paulCust);
		LON123 = new Flight("LON", "EDI", "LON-EDI-0", "RayanAir", 300, 10000, 300000);
		m.addFlight(LON123); 
	}
	
	@Test
	/** test if:
	 * 		EQUALS
	 * 		when a Customer exists in the allCustomers HashMap the method returns the Customer
	 *      check that getCustomer is case-insensitive
	 * 		when a Customer doesn't exist in the allCustomers HahsMap the method returns null
	 * 		when the Customer last name doesn't match the reference code
	 * 		when the customer has a space in their name, the method should be able to read it.
	 *      when the Customer last name has a '.' or '-' symbol on it the method should acknowledge them
	 *      
	 *      THROWS
	 *      when the customer reference is an empty string
	 *      when the customer last name is an empty string
	 *      when the customer reference has letters
	 *      when the customer reference has spaces
	 *      when the customer reference has symbols
	 *      when the customer last name has numbers
	 *      when the customer last name has symbols
	 *      when the customer last name has a mix of acceptable symbols and none acceptable symbols
	 *      
	 *      Notes:
	 *      the GUI will handles the following
	 *      when the Customer input name has spaces at the beginning, getCustomer ignores them 
	 *      when the Customer input name has spaces at the end, getCustomer ignores them
	 *      when the Customer input name has spaces in the centre of the name, it should consider them
	 */      

	void getCustomerTest() throws InvalidValueException{
		assertEquals(m.getCustomer("123", "smith"),adamCust,"The method has failed to retrieve a Customer.");
		assertEquals(m.getCustomer("456", "CuRIe"),marieCust,"The method has failed to retrieve a Customer.");
		assertEquals(m.getCustomer("52", "Ceasar"),null,"The method has failed to retrieve a Customer.");
		assertEquals(m.getCustomer("123", "ImNotSmith"),null,"the method has faild to return a null "
				+ " when the last name doesn't match an exsiting customerKey.");
		assertEquals(m.getCustomer("789", "Le Court"), damienCust,"the method has failed to ignore the space in the cutomer last name.");
		assertEquals(m.getCustomer("1011", "Dr.Jean-Francois"),paulCust,"The method has failed to retrieve a Customer with '.' and '-' in there name.");
		assertThrows(InvalidValueException.class, () -> m.getCustomer("", "smith"), "the method didn't throw an exception when Customer reference is empty.");
		assertThrows(InvalidValueException.class, () -> m.getCustomer("1d3", ""), "the method didn't throw an exception when Customer last name is empty.");
		assertThrows(InvalidValueException.class, () -> m.getCustomer("1d3", "smith"), "the method didn't throw an exception when Customer reference has letters.");
		assertThrows(InvalidValueException.class, () -> m.getCustomer(" 1 23 ", "smith"), "the method didn't throw an exception when Customer reference has spaces.");
		assertThrows(InvalidValueException.class, () -> m.getCustomer("1-23@", "smith"), "the method didn't throw an exception when Customer reference has symbols.");
		assertThrows(InvalidValueException.class, () -> m.getCustomer("123", "s4i5h"), "the method didn't throw an exception when Customer last name has numbers.");
		assertThrows(InvalidValueException.class, () -> m.getCustomer("123", "s@i5#~"), "the method didn't throw an exception when Customer last name has symbols (excluding '.' and '-').");
		assertThrows(InvalidValueException.class, () -> m.getCustomer("1011", "Dr.Jean-Francois12"), "the method didn't throw an exception when Customer last name has symbols"
				+ " (excluding '.' and '-'), this failed to notice the 12 in the last name because of the '.' and '-' most likely.");
	}
	
	@Test
	/** test if:
	 * 		EQUALS
	 * 		when a Flight exists in the allFlights HashMap the method returns the Flight
	 * 		when a Flight doesn't exist in the allFlights HahsMap the method returns null
	 * 
	 * 		Notes:
	 * 
	 * 		There is no direct user interaction with the Flight object. (based on coursework sheet) It is also assumed the input file that
	 * 		initialises the allFlights HashMap of flight objects has no errors. Therefore it's assumed that there is
	 * 		no incorrect values inside flight object fields.
	*/
	void getFlightTest(){
			assertEquals(m.getFlight("LON-EDI-0"),LON123,"The method has failed to retrieve an exsisting flight object.");
			assertEquals(m.getFlight("SUN-MOON-35"),null,"The method has failed to correctly return a null when asked to"
					+ " retrieve a none exsistant flight.");
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
		assertEquals(m.getOversizeFee(9,0),0,"The method has failed to correctly set the fee to 0 when"
				+ " the weight is 0-14 kg or volume is 0-19 litres.");
		assertEquals(m.getOversizeFee(0,20),10,"The method has failed to correctly set the fee to 10 when"
				+ " the weight is 15-24 kg or volume is 20-34 litres.");
		assertEquals(m.getOversizeFee(40,50),20,"The method has failed to correctly set the fee to 20 when"
				+ " the weight is 25-44 kg or volume is 35-54 litres.");
		assertEquals(m.getOversizeFee(60,30),40,"The method has failed to correctly set the fee to 40 when"
				+ " the weight is 45-69 kg or volume is 55-89 litres.");
		assertEquals(m.getOversizeFee(75,89),60,"The method has failed to correctly set the fee to 60 when"
				+ " the weight is 70-99 kg or volume is 90-119 litres.");
		assertEquals(m.getOversizeFee(81,179),80,"The method has failed to correctly set the fee to 80 when"
				+ " the weight is 100-149 kg or volume is 120-179 litres.");
		assertEquals(m.getOversizeFee(199,260),100,"The method has failed to correctly set the fee to 100 when"
				+ " the weight is 150-200 kg or volume is 180-260 litres.");
		
		assertThrows(InvalidValueException.class, () -> m.getOversizeFee(500,1),"The method fail to thow an exception"
				+ " needed to prevent values that are excessive from being entered onto the flight.");
	}
	
	//to be used in GUI
	/*      when the Customer input name has spaces at the beginning, getCustomer ignores them 
	 *      when the Customer input name has spaces at the end, getCustomer ignores them
	 *      when the Customer input name has spaces in the centre of the name, it should consider them
	 */      
//	//the following four concern spacing around names 
//	assertEquals(m.getCustomer("123", "  smith"),adamCust,"The GUI buffer has failed to ignore the spaces at the"
//			+ " begining of the lastName read buffer.");
//	assertEquals(m.getCustomer("123", "smith  "),adamCust,"The GUI buffer has failed to ignore the spaces at the"
//			+ " end of the lastName read buffer.");
//	assertEquals(m.getCustomer("789", "Le Court"),damienCust,"The GUI buffer has failed consider the spaces in the"
//			+ " middle of the lastName read buffer.");
//	assertEquals(m.getCustomer("1011", "Jean-Francois"),paulCust,"The GUI buffer has failed consider the - in the"
//			+ " middle of the lastName read buffer.");
 
}
