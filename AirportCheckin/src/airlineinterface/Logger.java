package airlineinterface;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {

	// Statics
	private static Logger instance = new Logger();

	public static Logger instance() {
		return instance;
	}

	private long startMillis;
	/*
	private String passengerDetails, flightDetails, queueJoin, 
					deskJoin, checkedIn, flightClosed, mainLog = "";
	*/
	
	private String passengerDetails = "";
	private String flightDetails = "";
	private String queueJoin = "";
	private String deskJoin = "";
	private String checkedIn = "";
	private String flightClosed = "";
	private String mainLog = "";
	
	private Logger() {
	}
	/*
	public void log(String s) {
		long time = System.currentTimeMillis() - startMillis;
		String timeString = "[" + String.format("%07d",  time) + "] ";
		System.out.println(timeString + s);
	}
	*/
	public void resetTimer() {
		startMillis = System.currentTimeMillis();
	}
	
	private String GetTimeStamp()
	{
		return "[" + String.format("%07d",  (System.currentTimeMillis() - startMillis)) + "] ";
	}
	
	public void MainLog(String s)
	{
		String t = GetTimeStamp() + s + "\n";
		mainLog += t;
		System.out.print(t);
	}
	
	public void LogPassengerDetails(Customer c)
	{
		passengerDetails += GetTimeStamp() + c.toString() + "\n";
	}
	
	public void LogFlightDetails(Flight f)
	{
		flightDetails += GetTimeStamp() + "\n";
		String[] s = f.toString().split("\n");
		for (String i : s)
		{
			flightDetails += "	" + i + "\n";
		}
	}
	
	public void PassengerJoinedQueue(Customer c)
	{
		String s = GetTimeStamp() + "\"" + c.toString() + "\" " + "has entered the queue\n";
		queueJoin += s;
		mainLog += s;
	}
	
	public void PassengerMovedToDesk(Customer c, String deskName)
	{
		String s = GetTimeStamp() + "\"" + c.toString() + "\" " + "has moved to " + deskName + "\n";
		deskJoin += s;
		mainLog += s;
	}
	
	public void PassengerCheckedIn(Customer c, Flight f, String deskName, float baggageFee)
	{
		String s = GetTimeStamp() + "\"" + c.toString() + "\" " + "has been checked in at " + deskName + ". Baggage Fee: " + baggageFee + "\n";
		checkedIn += s;
		mainLog += s;
	}
	
	public void FlightClosed(Flight f)
	{
		String s = "Flight " + f.toString() + "is now closed\n";
		flightClosed += s;
		mainLog += s;
	}
	
	public String GetSummary()
	{
		String sum = "Simulation Log:\n\n";
		
		// main log
		sum += "Main Log:\n" + mainLog + "\n";
		
		// additional information
		sum += "Queue Join:\n" + queueJoin + "\n";
		sum += "Desk Login:\n" + deskJoin + "\n";
		sum += "Check-In:\n" + checkedIn + "\n";
		sum += "Flight Status:\n" + flightClosed + "\n";
		
		// flights and passengers
		sum += "\nPassenger Details:\n" + passengerDetails + "\n";
		sum += "Flight Details:\n" + flightDetails;
		
		return sum;
	}
	
	public void WriteSummaryToFile(String filename)
	{		
		try 
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
			writer.write(GetSummary());
			writer.close();
		}
		catch (IOException e) { e.printStackTrace(); }
	}
}
