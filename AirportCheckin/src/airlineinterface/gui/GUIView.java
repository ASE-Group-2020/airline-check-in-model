package airlineinterface.gui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import airlineinterface.Logger;

public class GUIView {

	private JFrame frame;
	private JPanel pMaster, pQueue, pDesks, pFlights;

	public GUIView() {
		GridBagConstraints c = new GridBagConstraints();
		frame = new JFrame("Simulation");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1200, 600);
		pMaster = new JPanel(new GridBagLayout());
		frame.add(pMaster);
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

	public void addFlightDisplay(FlightDisplay f) {
		pFlights.add(f);
	}

	public void removeFlightDisplay(FlightDisplay f) {
		pFlights.remove(f);
	}

	public void addDeskDisplay(DeskDisplay d) {
		pDesks.add(d);
	}

	public void removeDeskDisplay(DeskDisplay d) {
		pDesks.remove(d);
	}
	
	public void addQueueDisplay(QueueDisplay q) {
		pQueue.add(q);
	}
	
	public void removeQueueDisplay(QueueDisplay q) {
		pQueue.remove(q);
	}
	
	public void setVisible(boolean vis) {
		frame.pack();
		frame.setVisible(vis);
	}

}
