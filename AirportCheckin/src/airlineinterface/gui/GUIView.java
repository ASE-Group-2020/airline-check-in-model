package airlineinterface.gui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import airlineinterface.Simulator;

public class GUIView {

	private JFrame frame;
	private JPanel pMaster, pQueue, pDesks, pFlights;

	public GUIView() {
		GridBagConstraints c = new GridBagConstraints();
		frame = new JFrame("Simulation");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		pMaster = new JPanel(new GridBagLayout());
		frame.add(pMaster);
		frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
            	Simulator.runSimulation = false;		// set flags to inform the simulation to start wrapping up and close
            	Simulator.closeWindow = true;
            	Simulator.simSpeed = 10000;				// set sim speed to a high number to close window instantly, cuz nobody wants to wait in 2020
            }
        });
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
	
	public void addSpeedDisplay(SpeedDisplay s) {
		pDesks.add(s.getComponent());
	}

	public void removeSpeedDisplay(SpeedDisplay s) {
		pDesks.remove(s.getComponent());
	}
	
	public void setVisible(boolean vis) {
		frame.pack();
		frame.setVisible(vis);
	}

}
