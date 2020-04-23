import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * The DatasetCreator class randomly generates two text files, dataFlight.txt and dataCustomer.txt, which contain example data to be used in the development of an Airline Check-In system.
 * <br><br>
 * Dataset formats:<ul>
 * <li>Customer:	refCode,firstName,lastName,flightCode,checkedIn,bagWeight,bagVolume</li>
 * <li>Flight:		flightCode,destination,start,carrier,maxPassenger,maxWeight,maxVolume</li></ul>
 * 
 * @authors
 * Edi Edimov ee21@hw.ac.uk<br>
 * David Gladwell drg1@hw.ac.uk<br>
 * Alex Swift as184@hw.ac.uk<br>
 * Niko Tether nst2@hw.ac.uk
 * @since 9/02/2020
 */
public class DatasetCreator
{
	/**The number of flight entries to be produced*/
	static int flightCount;
	/**The number of customer entries to be produced*/
	static int customerCount;	
	/**Starting location where the check-in desk is located*/
	static String airportLocation;
	/**A list of place names used to create flight paths*/
	static String[] places;
	/**A list of place codes used to create flight paths - MUST match the above names in the correct order*/
	static String[] placeCodes;
	/**A list of airline companies providing the flights*/
	static String[] carriers;
	/**A list of first names used to generate the customer data*/
	static String[] firstNames;
	/**A list of last names used to generate the customer data*/ 
	static String[] lastNames;
	
	static float flightClassPercentage;
	
	static float flightDelaySecondsPerCustomer = 5;
	
	/**Used to generate the random values for the flight and customer data*/
	private enum Spec
	{
		/**Used to randomly generate the capacity of an airliner*/
		Passengers(50, 200),
		/**Used to randomly generate the maximum weight capacity of an airliner*/
		FlightWeight(500, 5000),
		/**Used to randomly generate the maximum storage volume of an airliner*/
		FlightVolume(1500, 5000),
		/**Used to create the departure delay*/
		FlightDepartureTime(60000,300000),
		/**Used to alter the percentage chance of a random boolean calculation for whether or not a passenger as checked into the airport*/
		CheckIn(0.5f,1),
		/**Used to randomly generate the weight of a passenger's baggage*/
		CustomerWeight(0, 30),
		/**Used to randomly generate the volume of a passenger's baggage*/
		CustomerVolume(0, 6f);
		
		float min, max;
		private Spec(float a, float b) { min = a; max = b; }
		
		/** @return a random value between the Spec's min and max values*/
		public float RandomMinMax() { return min + r.nextFloat() * (max-min); }
		
		/** @return a random boolean; the boolean's chance is based on the Spec's min and max values*/
		public boolean RandomBoolWithThreshold() { return min < r.nextFloat() * max; }
	}
	
	/**Holds all the flight nodes that have been generated so far*/
	static Map<String, Integer> flightCodes;
	/**Used by the IdFromNumber() method to output a consistent number of digits for each id string*/
	static int limitLength;
	/**Used by NewID() method to distribute new IDs for new passengers*/
	static int currentCustomerIndex = 0;
	/**Used by the Spec enum to create random weight, volume and passenger numbers, as well as assigning random flight codes to passengers*/
	static Random r;
	
	
	private static int RandomDepartureTime()
	{
		return (int) Spec.FlightDepartureTime.RandomMinMax();
	}
	
	/**Resets the DatasetCreator object with specified default variables*/
	public static void Reset()
	{
		customerCount = 10000;
		flightClassPercentage = 0.7f; // between 0 and 1
		
		flightCount = 6;
		
		airportLocation = "Edinburgh";
		
		places 	   = new String[] {"Londom", "Berlin", "Dubai", "Stockholm", "Costa Rica", "Phuket", "Tahiti", "Paris", "Maldives", "Rio de Janeiro", "Santorini"};
		placeCodes = new String[] {"LDN"   , "BLN"   , "DBI"  , "SKM"	   , "CRA"		 , "RKT"   , "THI"	 , "PRI"  , "MLD"	  , "RDJ"			, "STI"};
		carriers   = new String[] {"EasyJet", "British Airways", "Virgin", "Ryanair", "BlueJet", "Delta Air Lines", "Frontier Airlines", "United Airlines"};
		firstNames = new String[] {"May", "David", "Mark", "Phil", "Martin", "John", "Mike", "Steve", "Jack", "Jenny", "Olivia", "Hannah", "Irene", "Amanpreet", "Nyla", "Reis", "Kiya", "Lisa", "Sumayya", "Edna"};
		lastNames  = new String[] {"Macdonald", "Xi", "Barlow", "Brown", "Godliman", "Gamble", "Parris", "Jones", "Smith", "Kumar", "Barnes", "Cameron", "Mac", "Scott", "Gray", "Wise", "McGregor", "Chandler"};
		r = new Random();
		
		flightCodes = new HashMap<String, Integer>();
		limitLength = Integer.toString(customerCount-1).length();
		currentCustomerIndex = -1;
	}
	
	/**Uses default variables in Reset() method to produce new Flight and Customer data, and print them into two separate data files in the form of .txt files.*/
	public static void main(String[] args)
	{
		Reset();
		
		try { ValidVariablesCheck(); }
		catch (InvalidValueInDatasetCreatorException e) { System.err.println(e.getMessage()); System.exit(-1); }
		
		// generates and writes each new flight entry to file individually
		try
		{
			FileWriter flightDataWriter = new FileWriter("dataFlight-demo.csv");		
			for (int I = 0; I < flightCount; I++)
			{	
				flightDataWriter.write(NewFlight() + "\n");
			}
			flightDataWriter.close();
		} catch (IOException e) { System.err.println("dataFlight.txt creation has failed: IOException. Continuing program..."); }
		
		// generates and writes each new customer entry to file individually
		try
		{
			FileWriter customerDataWriter = new FileWriter("dataCustomer-demo.csv");
			for (int I = 0; I < customerCount; I++)
			{			
				customerDataWriter.write(NewCustomer() + "\n");
			}
			customerDataWriter.close();
		} catch (IOException e) { System.err.println("dataCustomer.txt creation has failed: IOException. Exiting program..."); }
	}
	
	/**
	 * Creates a randomly generated flight entry in the form of a string, with each variable separated by commas.
	 * 
	 * @return a new flight entry in the following format:
	 * flightCode,destination,start,carrier,maxPassenger,maxWeight,maxVolume <br>
	 * (string),(string),(string),(string),(integer),(float),(float)
	 */
	public static String NewFlight()
	{
		String end = RandomFromArray(places);		
		return 
			airportLocation + "," + 
			end + "," + 
			FlightCode(end) + "," + 
			RandomFromArray(carriers) + "," +
			(int) Spec.Passengers.RandomMinMax() + "," + 
			(int) Spec.FlightWeight.RandomMinMax() + "," + 
			(int) Spec.FlightVolume.RandomMinMax() + "," +
			RandomDepartureTime()
		;
	}
	
	/**
	 * Creates a randomly generated customer entry in the form of a string, with each variable separated by commas.
	 * 
	 * @return a new customer entry in the following format:
	 * refCode,firstName,lastName,flightCode,checkedIn,bagWeight,bagVolume <br>
	 * (string),(string),(string),(string),(boolean),(float),(float)
	 */
	public static String NewCustomer()
	{		
		return
			NewID() + "," + 
			RandomFromArray(firstNames) + "," +
			RandomFromArray(lastNames) + "," +
			RandomFlightCode() + "," +
			RandomClass(flightClassPercentage) + "," +
			Spec.CheckIn.RandomBoolWithThreshold() + "," +
			Spec.CustomerWeight.RandomMinMax() + "," +
			Spec.CustomerVolume.RandomMinMax() + "," +
			Spec.CustomerVolume.RandomMinMax() + "," +
			Spec.CustomerVolume.RandomMinMax()
		;
	}
	
	private static String RandomClass(float percent) { return (r.nextFloat() < percent ? "standard" : "business"); }
	
	/**
	 * Returns a random item from the input array.
	 * 
	 * @param array of Strings
	 * @return a random String from the input array
	 */
	private static String RandomFromArray(String[] array) { return array[r.nextInt(array.length)]; }
	
	/**
	 * Returns a random flight code from the flightCodes map.
	 * 
	 * @return a random flight code
	 */
	private static String RandomFlightCode()
	{
		Object[] array = flightCodes.keySet().toArray();
		String codePrefix = (String) array[r.nextInt(array.length)];	
		return codePrefix + r.nextInt(flightCodes.get(codePrefix)+1);
	}
	
	/**
	 * Creates a flight code with the specified start and end locations, and adds it to a map of generated flight codes which also holds the
	 * number of occurrences of this code. 
	 * 
	 * @param start departure location (SRT)
	 * @param end arrival location (END)
	 * @return a flight code e.g. SRT-END-13
	 */
	private static String FlightCode(String end)
	{
		String code = "EDN-" + placeCodes[IndexInArray(end, places)] + "-";
		if (!flightCodes.containsKey(code))
		{
			flightCodes.put(code, 0);
			code += "0";
		}
		else
		{
			int index = flightCodes.get(code) + 1;
			flightCodes.put(code, index);
			code += index;
		}
		return code;
	}
	
	/**
	 * Identifies the index of a given string inside of a given array of strings.
	 * 
	 * @param item the method is searching for
	 * @param array the method with searching in
	 * @return the index of the first occurrence of item in the array; returns -1 if the item is not found
	 */
	private static int IndexInArray(String item, String[] array)
	{
		for (int I = 0; I < array.length; I++)
		{
			if (array[I].equals(item)) return I;
		}
		return -1;
	}
	
	/**
	 * Generates a unique reference code for a new passenger.
	 * 
	 * @return a new reference code for a passenger
	 */
	private static String NewID()
	{
		currentCustomerIndex++;
		if (currentCustomerIndex < 0) return "-ERROR-";
		String id = "";
		
		int delta = limitLength - Integer.toString(currentCustomerIndex).length();
		while (delta > 0)
		{
			id += 0;
			delta--;
		}
		
		return id + currentCustomerIndex;
	}
	
	/**
	 * Checks all the default values in the Reset() method to ensure they are valid and the program will run as expected.
	 * 
	 * @throws InvalidValueInDatasetCreatorException when one or more of these input variables are not valid
	 */
	public static void ValidVariablesCheck() throws InvalidValueInDatasetCreatorException
	{
		String errors = "";
		if (flightCount 		<= 0) 		errors = "flightCount must be greater than zero";
		if (customerCount 		<= 0) 		errors = AppendErrorString("customerCount must be greater than zero"	, errors);
		if (places.length 		== 0) 		errors = AppendErrorString("places must not be empty"					, errors);
		if (ArrayHasEmptyCells(places)) 	errors = AppendErrorString("places contains empty cells"				, errors);
		if (placeCodes.length	== 0) 		errors = AppendErrorString("placeCodes must not be empty"				, errors);
		if (ArrayHasEmptyCells(placeCodes)) errors = AppendErrorString("placeCodes contains empty cells"			, errors);
		if (carriers.length 	== 0) 		errors = AppendErrorString("carriers must not be empty"					, errors);
		if (ArrayHasEmptyCells(carriers)) 	errors = AppendErrorString("carriers contains empty cells"				, errors);
		if (firstNames.length 	== 0) 		errors = AppendErrorString("firstNames must not be empty"				, errors);
		if (ArrayHasEmptyCells(firstNames)) errors = AppendErrorString("firstNames contains empty cells"			, errors);
		if (lastNames.length 	== 0) 		errors = AppendErrorString("lastNames must not be empty"				, errors);
		if (ArrayHasEmptyCells(lastNames)) 	errors = AppendErrorString("lastNames contains empty cells"				, errors);
		if (errors.length() > 0) throw new InvalidValueInDatasetCreatorException(errors);
	}
	
	/**
	 * Used by the ValidVariablesCheck() to append multiple errors to the InvalidValueInDatasetCreatorException error message. If multiple errors have occurred, a comma is added to separate them.
	 * 
	 * @param newString the new error message
	 * @param mainString the current error string
	 * @return the updated full error message
	 */
	private static String AppendErrorString(String newString, String mainString)
	{
		if (mainString.length() == 0) return newString;
		else return mainString + ", " + newString;
	}
	
	/**
	 * Used by the ValidVariablesCheck() to check if any cells of the given array have empty of null cells.
	 * 
	 * @param array of strings
	 * @return false if there are no empty or null cells in the given array; otherwise returns true
	 */
	private static boolean ArrayHasEmptyCells(String[] array)
	{
		for (int I = 0; I < array.length; I++)
		{
			if (array[I] == null || "".equals(array[I])) return true;
		}
		return false;
	}
}
