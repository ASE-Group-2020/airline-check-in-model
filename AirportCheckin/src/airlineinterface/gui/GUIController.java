package airlineinterface.gui;

import java.util.HashMap;
import java.util.Map;

import airlineinterface.Desk;
import airlineinterface.Flight;
import airlineinterface.WaitingQueue;
import exceptions.ObjectNotFoundException;

public class GUIController {

	private GUIView view;

	private Map<Desk, DeskDisplay> desks;
	private Map<Flight, FlightDisplay> flights;
	
	private SpeedDisplay sd;

	public GUIController(GUIView view) {
		this.view = view;
		desks = new HashMap<Desk, DeskDisplay>();
		flights = new HashMap<Flight, FlightDisplay>();
	}

	public void addFlight(Flight f) {
		FlightDisplay fd = new FlightDisplay(f);
		flights.put(f, fd);
		view.addFlightDisplay(fd);
	}

	public void removeFlight(Flight f) throws ObjectNotFoundException {
		FlightDisplay fd = flights.get(f);
		if (fd == null)
			throw new ObjectNotFoundException("Flight display not found.");
		flights.remove(f, fd);
		view.removeFlightDisplay(fd);
	}

	public void addDesk(Desk d) {
		DeskDisplay dd = new DeskDisplay(d);
		desks.put(d, dd);
		view.addDeskDisplay(dd);
	}

	public void removeDesk(Desk d) throws ObjectNotFoundException {
		DeskDisplay dd = desks.get(d);
		if (dd == null)
			throw new ObjectNotFoundException("Desk display not found.");
		desks.remove(d, dd);
		view.removeDeskDisplay(dd);
	}
	
	public void addQueue(WaitingQueue q) {
		QueueDisplay qd = new QueueDisplay(q);
		view.addQueueDisplay(qd);
	}
	
	public SpeedDisplay addSpeed()
	{
		sd = new SpeedDisplay();
		view.addSpeedDisplay(sd);
		return sd;
	}
	
	public void removeSpeed()
	{
		view.removeSpeedDisplay(sd);
	}
}
