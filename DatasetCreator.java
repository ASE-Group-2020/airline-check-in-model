import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DatasetCreator
{
	// customer:	refCode,firstName,lastName,flightCode,checkedIn,bagWeight,bagVolume
	// flight:		flightCode,destination,start,carrier,maxPassenger,maxWeight,maxVolume
	
	static int flightCount = 500;
	static int customerCount = 270000;
	
	static String[] places 	   = {"Edinburgh", "Londom", "Berlin", "Dubai", "Stockholm", "Llanfairpwllgwyngyllgogerychwyrndrobwllllantysiliogogogoch"};
	static String[] placeCodes = {"EDN"      , "LDN"   , "BLN"   , "DBI"  , "SKM"	   , "LWG"};
	static String[] carriers   = {"EasyJet", "British Airways", "Virgin", "Ryanair"};
	static String[] firstNames = {"may", "david", "mark", "phil", "martin", "john", "mike", "steve", "jack", "jenny", "olivia", "hannah", "irene"};
	static String[] lastNames  = {"macdonald", "xi", "barlow", "brown", "godliman", "gamble", "parris", "jones", "smith", "kumar", "barnes", "cameron"};
	
	private enum Spec
	{
		Passengers(50, 200), FlightWeight(500, 5000), FlightVolume(400, 4000), CheckIn(0.5f,1), CustomerWeight(0, 30), CustomerVolume(0, 0.5f);
		float min, max; Random r;
		private Spec(float a, float b) { min = a; max = b; r = new Random(); }
		public float RandomMinMax() { return min + r.nextFloat() * (max-min); }
		public boolean RandomBoolWithThreshold() { return min < r.nextFloat() * max; }
	}
	static Map<String, Integer> flightCodes = new HashMap<String, Integer>();
	static int limitLength = Integer.toString(customerCount).length();
	static int currentCustomerIndex = -1;
	static Random r = new Random();
	
	public static void main(String[] args)
	{
		try { ValidVariablesCheck(); }
		catch (InvalidValueInDatasetCreatorException e) { System.err.println(e.getMessage()); System.exit(-1); }
		
		try
		{
			FileWriter flightDataWriter = new FileWriter("dataFlight.txt");		
			for (int I = 0; I < flightCount; I++)
			{	
				flightDataWriter.write(NewFlight() + "\n");
			}
			flightDataWriter.close();
		} catch (IOException e) { System.err.println("dataFlight.txt creation has failed: IOException. Continuing program..."); }
		
		try
		{
			FileWriter customerDataWriter = new FileWriter("dataCustomer.txt");
			for (int I = 0; I < customerCount; I++)
			{			
				customerDataWriter.write(NewCustomer() + "\n");
			}
			customerDataWriter.close();
		} catch (IOException e) { System.err.println("dataCustomer.txt creation has failed: IOException. Exiting program..."); }
	}
	
	public static String NewFlight()
	{
		String start = RandomFromArray(places);
		String end = new String(start); while (start.equals(end)) { end = RandomFromArray(places);}			
		return 
			start + "," + 
			end + "," + 
			FlightCode(start, end) + "," + 
			RandomFromArray(carriers) + "," +
			(int) Spec.Passengers.RandomMinMax() + "," + 
			Spec.FlightWeight.RandomMinMax() + "," + 
			Spec.FlightVolume.RandomMinMax()
		;
	}
	
	public static String NewCustomer()
	{
		boolean checkedIn = Spec.CheckIn.RandomBoolWithThreshold();
		float weight;
		float volume;
		if (checkedIn)
		{
			weight = Spec.CustomerWeight.RandomMinMax();
			volume = Spec.CustomerVolume.RandomMinMax();
		}
		else { weight = 0; volume = 0; }
		
		return
			NewId() + "," + 
			RandomFromArray(firstNames) + "," +
			RandomFromArray(lastNames) + "," +
			RandomFlightCode() + "," +
			checkedIn + "," +
			weight + "," +
			volume
		;
	}
	
	private static String RandomFromArray(String[] array) { return array[r.nextInt(array.length)]; }
	
	private static String RandomFlightCode()
	{
		Object[] array = flightCodes.keySet().toArray();
		String codePrefix = (String) array[r.nextInt(array.length)];	
		return codePrefix + r.nextInt(flightCodes.get(codePrefix)+1);
	}
	
	private static String FlightCode(String start, String end)
	{
		String code = placeCodes[IndexInArray(start, places)] + "-" + placeCodes[IndexInArray(end, places)] + "-";
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
	
	private static int IndexInArray(String item, String[] array)
	{
		for (int I = 0; I < array.length; I++)
		{
			if (array[I].equals(item)) return I;
		}
		return -1;
	}
	
	private static String NewId() { currentCustomerIndex++; return IdFromNumber(currentCustomerIndex); }
	
	public static String IdFromNumber(int n)
	{
		if (n < 0) return "-ERROR-";
		String id = "";
		
		int delta = limitLength - Integer.toString(n).length();
		while (delta > 0)
		{
			id += 0;
			delta--;
		}
		
		return id + n;
	}
	
	private static void ValidVariablesCheck() throws InvalidValueInDatasetCreatorException
	{
		String errors = "";
		if (flightCount 		<= 0) errors += "flightCount must be greater than zero";
		if (customerCount 		<= 0) errors = AppendErrorString("customerCount must be greater than zero"	, errors);
		if (places.length 		== 0) errors = AppendErrorString("places must not be empty"					, errors);
		if (placeCodes.length	== 0) errors = AppendErrorString("placeCodes must not be empty"				, errors);
		if (carriers.length 	== 0) errors = AppendErrorString("carriers must not be empty"				, errors);
		if (firstNames.length 	== 0) errors = AppendErrorString("firstNames must not be empty"				, errors);
		if (lastNames.length 	== 0) errors = AppendErrorString("lastNames must not be empty"				, errors);
		if (errors.length() > 0) throw new InvalidValueInDatasetCreatorException(errors);
	}
	
	private static String AppendErrorString(String newString, String mainString)
	{
		if (mainString.length() == 0) return newString;
		else return mainString + ", " + newString;
	}
}
