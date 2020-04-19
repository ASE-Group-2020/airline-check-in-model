package airlineinterface;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {

	// Statics
	private static Logger instance = new Logger();
	public static Logger instance() { return instance; }

	private long startMillis;
	/*
	private String passengerDetails, flightDetails, queueJoin, 
					deskJoin, checkedIn, flightClosed, mainLog = "";
	*/
	
	private String passengerDetails = "";
	private String flightDetails = "----" + System.lineSeparator();
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
	
	public synchronized void MainLog(String s)
	{
		String t = GetTimeStamp() + s + System.lineSeparator();
		mainLog += t;
		System.out.print(t);
	}
	
	public synchronized void LogPassengerDetails(Customer c)
	{
		passengerDetails += c.toString() + System.lineSeparator();
	}
	
	public synchronized void LogFlightDetails(Flight f)
	{
		//flightDetails += GetTimeStamp() + System.lineSeparator();
		String[] s = f.toString().split(System.lineSeparator());
		for (String i : s)
		{
			flightDetails += i + System.lineSeparator();
		}
		flightDetails += "----" + System.lineSeparator();
	}
	
	public synchronized void PassengerJoinedQueue(Customer c)
	{
		String s = GetTimeStamp() + "\"" + c.toString() + "\" " + "has entered the queue" + System.lineSeparator();
		queueJoin += s;
		mainLog += s;
	}
	
	public synchronized void PassengerMovedToDesk(Customer c, String deskName)
	{
		String s = GetTimeStamp() + "\"" + c.toString() + "\" " + "has moved to " + deskName + System.lineSeparator();
		deskJoin += s;
		mainLog += s;
	}
	
	public synchronized void PassengerCheckedIn(Customer c, Flight f, String deskName, float baggageFee)
	{
		String s = GetTimeStamp() + "\"" + c.toString() + "\" " + "has been checked in at " + deskName + ". Baggage Fee: " + baggageFee + System.lineSeparator();
		checkedIn += s;
		mainLog += s;
	}
	
	public synchronized void FlightClosed(Flight f)
	{
		String s = GetTimeStamp() + f.getFlightCode() + " is now closed" + System.lineSeparator();
		flightClosed += s;
		mainLog += s;
	}
	
	public String GetSummary()
	{
		String sum = "Simulation Log:" + System.lineSeparator() + System.lineSeparator();
		
		// main log
		sum += "Main Log:" + System.lineSeparator() + mainLog + System.lineSeparator();
		
		// additional information
		sum += "Queue Join:" + System.lineSeparator() + queueJoin + System.lineSeparator();
		sum += "Desk Login:" + System.lineSeparator() + deskJoin + System.lineSeparator();
		sum += "Check-In:" + System.lineSeparator() + checkedIn + System.lineSeparator();
		sum += "Flight Status:" + System.lineSeparator() + flightClosed + System.lineSeparator();
		
		// flights and passengers
		sum += "Passenger Details:" + System.lineSeparator() + passengerDetails + System.lineSeparator();
		sum += "Flight Details:" + System.lineSeparator() + flightDetails;
		
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
