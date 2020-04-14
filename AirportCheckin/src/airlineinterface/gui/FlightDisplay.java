package airlineinterface.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import airlineinterface.Flight;
import airlineinterface.Observer;

public class FlightDisplay extends Observer {

	private Flight flight;
	private JPanel panel;

	private JTextField lPassengerCapacity, lWeightCapacity, lVolumeCapacity;

	public FlightDisplay(Flight flight) {
		panel = new JPanel(new GridBagLayout());

		Border black = BorderFactory.createLineBorder(Color.DARK_GRAY);
		panel.setBorder(black);

		this.flight = flight;
		flight.addObserver(this);

		// Build display

		String carrier = flight.getCarrier();
		String code = flight.getFlightCode();
		String[] travelPoints = flight.getTravelPoints();

		GridBagConstraints c = new GridBagConstraints();
		c.ipadx = 5;
		c.ipady = 5;

		c.gridx = 0;
		c.gridy = 0;
		panel.add(new JLabel(carrier + ": " + code), c);
		JPanel locations = new JPanel(new GridBagLayout());
		c.gridx = 0;
		c.gridy = 0;
		locations.add(new JLabel("From " + travelPoints[0]), c);
		c.gridx = 1;
		c.gridy = 0;
		locations.add(new JLabel("To  " + travelPoints[1]), c);
		c.gridx = 0;
		c.gridy = 1;
		panel.add(locations, c);

		lPassengerCapacity = new JTextField();
		lWeightCapacity = new JTextField();
		lVolumeCapacity = new JTextField();

		lPassengerCapacity.setEditable(false);
		lPassengerCapacity.setFocusable(false);
		lWeightCapacity.setEditable(false);
		lWeightCapacity.setFocusable(false);
		lVolumeCapacity.setEditable(false);
		lVolumeCapacity.setFocusable(false);
		
		c.gridx = 0;
		c.gridy = 2;
		panel.add(lPassengerCapacity, c);
		c.gridx = 0;
		c.gridy = 3;
		panel.add(lWeightCapacity, c);
		c.gridx = 0;
		c.gridy = 4;
		panel.add(lVolumeCapacity, c);
		
		updateDisplay();
	}

	public void updateDisplay() {
		float[] maxAttr = flight.getMaxAttributes();
		float[] curAttr = flight.getCustomerSumAttributes();
		lPassengerCapacity.setText("Passengers: " + curAttr[0] + " / " + maxAttr[0]);
		lWeightCapacity.setText("Weight: " + curAttr[1] + " / " + maxAttr[1]);
		lVolumeCapacity.setText("Volume: " + curAttr[2] + " / " + maxAttr[2]);
		panel.revalidate();
		panel.repaint();
	}
	
	public JComponent getComponent() {
		return panel;
	}

	@Override
	public void onNotify() {
		updateDisplay();
	}

}
