package airlineinterface.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
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
	private JTextField lStatus, lPassengerCapacity, lWeightCapacity, lVolumeCapacity;
	private JButton bDepart;

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
		c.ipady = 7; // was 5

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

		lStatus = new JTextField("", 20);
		lPassengerCapacity = new JTextField("", 20);
		lWeightCapacity = new JTextField("", 20);
		lVolumeCapacity = new JTextField("", 20);
		bDepart = new JButton("Depart");
		bDepart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (bDepart.getText().equals("Depart")) {
					flight.flightDeparting();
				}
			}
		});

		lStatus.setEditable(false);
		lStatus.setFocusable(false);
		lPassengerCapacity.setEditable(false);
		lPassengerCapacity.setFocusable(false);
		lWeightCapacity.setEditable(false);
		lWeightCapacity.setFocusable(false);
		lVolumeCapacity.setEditable(false);
		lVolumeCapacity.setFocusable(false);

		c.gridx = 0;
		c.gridy = 2;
		panel.add(lStatus, c);
		c.gridx = 0;
		c.gridy = 3;
		panel.add(lPassengerCapacity, c);
		c.gridx = 0;
		c.gridy = 4;
		panel.add(lWeightCapacity, c);
		c.gridx = 0;
		c.gridy = 5;
		panel.add(lVolumeCapacity, c);
		c.gridx = 0;
		c.gridy = 6;
		panel.add(bDepart, c); // TODO add button here

		updateDisplay();
	}

	public synchronized void updateDisplay() {
		float[] maxAttr = flight.getMaxAttributes();
		float[] curAttr = flight.getCustomerSumAttributes();
		lStatus.setText(flight.getCurrentState());
		if (!flight.isFlightWaiting()) {
			flightDeparted();
		}
		lPassengerCapacity.setText("Passengers: " + curAttr[0] + " / " + maxAttr[0]);
		lWeightCapacity.setText("Weight: " + curAttr[1] + " / " + maxAttr[1]);
		lVolumeCapacity.setText("Volume: " + curAttr[2] + " / " + maxAttr[2]);
		panel.revalidate();
		panel.repaint();
	}

	private void flightDeparted() {
		bDepart.setEnabled(false);
		bDepart.setText("Check-in Closed");
	}

	public JComponent getComponent() {
		return panel;
	}

	@Override
	public void onNotify() {
		updateDisplay();
	}

	public void disableButtons() {

	}
}
