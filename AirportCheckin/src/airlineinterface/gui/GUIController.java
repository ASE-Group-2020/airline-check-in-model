package airlineinterface.gui;

import java.util.HashMap;
import java.util.Map;

import airlineinterface.Desk;
import airlineinterface.Flight;
import airlineinterface.WaitingQueue;
import exceptions.ObjectNotFoundException;

public class GUIController {

	private GUIView view;

	private Map<Desk, DeskDisplay> desks;			// Map used to link each DeskDisplay to the Desk it's created from - allows displays to be removed from only the Desk
	private Map<Flight, FlightDisplay> flights;		// Same as above, but for FlightDisplay and Flight
	
	private SpeedDisplay sd;

	/* Constructor */
	public GUIController(GUIView view) {
		this.view = view;
		desks = new HashMap<Desk, DeskDisplay>();
		flights = new HashMap<Flight, FlightDisplay>();
	}

	/* Create and add a display for a Flight */
	public void addFlight(Flight f) {
		FlightDisplay fd = new FlightDisplay(f);
		flights.put(f, fd);
		view.addFlightDisplay(fd);
	}

	/* Remove the display of a Flight */
	public void removeFlight(Flight f) throws ObjectNotFoundException {
		FlightDisplay fd = flights.get(f);
		if (fd == null)
			throw new ObjectNotFoundException("Flight display not found.");
		flights.remove(f, fd);
		view.removeFlightDisplay(fd);
	}

	/* Create and add a display for a Desk */
	public void addDesk(Desk d) {
		DeskDisplay dd = new DeskDisplay(d);
		desks.put(d, dd);
		view.addDeskDisplay(dd);
	}

	/* Remove the display of a Desk */
	public void removeDesk(Desk d) throws ObjectNotFoundException {
		DeskDisplay dd = desks.get(d);
		if (dd == null)
			throw new ObjectNotFoundException("Desk display not found.");
		desks.remove(d, dd);
		view.removeDeskDisplay(dd);
	}
	
	/* Create and add the display of a WaitingQueue */
	public void addQueue(WaitingQueue q) {
		QueueDisplay qd = new QueueDisplay(q);
		view.addQueueDisplay(qd);
	}
	
	/* Create and add the display for the simulation speed - also returns the object */
	public SpeedDisplay addSpeed()
	{
		sd = new SpeedDisplay();
		view.addSpeedDisplay(sd);
		return sd;
	}
	
	/* Remove the SpeedDisplay object */
	public void removeSpeed()
	{
		view.removeSpeedDisplay(sd);
	}
	
	/* Closes the GUI window */
	public void closeGui() {
		view.closeGui();
	}
	
	/* Enable or disable all buttons in the GUI */
	public void setAllButtons(boolean b)
	{
		view.setAllButtons(b);
	}
}
