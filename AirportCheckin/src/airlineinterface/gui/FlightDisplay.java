package airlineinterface.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import airlineinterface.Flight;

public class FlightDisplay extends JPanel {

	private Flight flight;

	private JLabel lPassengerCapacity, lWeightCapacity, lVolumeCapacity;

	public FlightDisplay(Flight flight) {
		super(new GridBagLayout());

		Border black = BorderFactory.createLineBorder(Color.DARK_GRAY);
		this.setBorder(black);

		this.flight = flight;

		// Build display

		String carrier = flight.getCarrier();
		String code = flight.getFlightCode();
		String[] travelPoints = flight.getTravelPoints();

		GridBagConstraints c = new GridBagConstraints();
		c.ipadx = 5;
		c.ipady = 5;

		c.gridx = 0;
		c.gridy = 0;
		add(new JLabel(carrier + ": " + code), c);
		JPanel locations = new JPanel(new GridBagLayout());
		c.gridx = 0;
		c.gridy = 0;
		locations.add(new JLabel("From " + travelPoints[0]), c);
		c.gridx = 1;
		c.gridy = 0;
		locations.add(new JLabel("To  " + travelPoints[1]), c);
		c.gridx = 0;
		c.gridy = 1;
		add(locations, c);

		lPassengerCapacity = new JLabel();
		lWeightCapacity = new JLabel();
		lVolumeCapacity = new JLabel();

		c.gridx = 0;
		c.gridy = 2;
		add(lPassengerCapacity, c);
		c.gridx = 0;
		c.gridy = 3;
		add(lWeightCapacity, c);
		c.gridx = 0;
		c.gridy = 4;
		add(lVolumeCapacity, c);
		
		updateDisplay();
	}

	public void updateDisplay() {
		float[] maxAttr = flight.getMaxAttributes();
		float[] curAttr = flight.getCustomerSumAttributes();
		lPassengerCapacity.setText("Passengers: " + curAttr[0] + " / " + maxAttr[0]);
		lWeightCapacity.setText("Weight: " + curAttr[1] + " / " + maxAttr[1]);
		lVolumeCapacity.setText("Volume: " + curAttr[2] + " / " + maxAttr[2]);
		revalidate();
		repaint();
	}

}
