package airlineinterface.gui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import airlineinterface.Simulator;

public class GUIView {

	private JFrame frame;												// Main window for the GUI
	private JPanel pMaster, pQueue, pDesks, pFlights;					// Master panel, as well as the panels for QueueDisplay, every DeskDisplay, and every FlightDisplay

	public GUIView() {
		GridBagConstraints c = new GridBagConstraints();
		frame = new JFrame("Simulation");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);		// Close operation is managed through Simulator
		pMaster = new JPanel(new GridBagLayout());
		frame.add(pMaster);
		frame.addWindowListener(new WindowAdapter() {					// Close operation calls Simulator.closeSimulation()
            @Override
            public void windowClosing(WindowEvent event) {
            	Simulator.get().closeSimulation();
            }
        });
		// Add panels to the master panel
		pQueue = new JPanel(new FlowLayout());
		pDesks = new JPanel(new FlowLayout());
		pFlights = new JPanel(new FlowLayout());
		c.gridx = 0;
		c.gridy = 0;
		pMaster.add(pQueue, c);
		c.gridx = 0;
		c.gridy = 1;
		pMaster.add(pDesks, c);
		c.gridx = 0;
		c.gridy = 2;
		pMaster.add(pFlights, c);
	}

	/* 
	 * Below - methods for adding and removing all Display objects
	 * The *Display classes aren't JComponents themselves - instead each returns a JPanel from the getComponent method
	 * This was necessary to allow them to extend Observer 
	 */
	public void addFlightDisplay(FlightDisplay f) {
		pFlights.add(f.getComponent());
	}

	public void removeFlightDisplay(FlightDisplay f) {
		pFlights.remove(f.getComponent());
	}

	public void addDeskDisplay(DeskDisplay d) {
		pDesks.add(d.getComponent());
	}

	public void removeDeskDisplay(DeskDisplay d) {
		pDesks.remove(d.getComponent());
	}
	
	public void addQueueDisplay(QueueDisplay q) {
		pQueue.add(q.getComponent());
	}
	
	public void removeQueueDisplay(QueueDisplay q) {
		pQueue.remove(q.getComponent());
	}
	
	/* SpeedDisplay is the first panel added to pDesks */
	public void addSpeedDisplay(SpeedDisplay s) {
		pDesks.add(s.getComponent());
	}

	public void removeSpeedDisplay(SpeedDisplay s) {
		pDesks.remove(s.getComponent());
	}
	
	/* Pack the main frame and change visibility */
	public void setVisible(boolean vis) {
		frame.pack();
		frame.setVisible(vis);
	}
	
	/* Close the GUI entirely */
	public void closeGui() {
		frame.dispose();
	}

	/* Set all GUI buttons to enabled or disabled */
	public void setAllButtons(boolean b)
	{
		// Enables/disables all buttons on flight displays
		for (Component p : pFlights.getComponents())
		{
			if (p instanceof JPanel)
			{
				for (Component c : ((JPanel)p).getComponents())
				{
					if (c instanceof JButton) ((JButton)c).setEnabled(b);
				}
			}
		}
		
		// Enables/disables all buttons on desk displays
		boolean ignoredFirstItem = false; // the first instance is the SpeedDisplay, which requires a different way to disable the buttons
		for (Component p : pDesks.getComponents())
		{
			if (!ignoredFirstItem) { ignoredFirstItem = true; continue; }
			else if (p instanceof JPanel)
			{
				for (Component c : ((JPanel)p).getComponents())
				{
					if (c instanceof JButton) ((JButton)c).setEnabled(b);
				}
			}
		}
		
		// Enables/disables all buttons on SpeedDisplay
		JPanel panel = ((JPanel) pDesks.getComponent(0)); // SpeedDisplay's panel
		JPanel subPanel = ((JPanel) panel.getComponent(0)); // SpeedDisplay button panel
		for (Component c : subPanel.getComponents()) if (c instanceof JButton) ((JButton)c).setEnabled(b);
	}
}
