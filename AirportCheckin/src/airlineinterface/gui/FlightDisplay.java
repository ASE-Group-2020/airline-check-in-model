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

	private Flight flight;									// Keep track of flight so up-to-date data can be retrieved

	private JPanel panel;									// Component to return to be displayed
	private JTextField lStatus, lPassengerCapacity, lWeightCapacity, lVolumeCapacity;
															// No text entry is used - JTextFields are treated as labels. This was used to fix a bug relating to
															// the QueueDisplay JTable not having enough room to display correctly JLabel text changed.
	// Forces check-in to be disabled for the observed flight
	private JButton bDepart;
	
	// ActionListener for the "Depart" button
	private ActionListener departCommand = new ActionListener() {	
		@Override
		public void actionPerformed(ActionEvent e) {
			if (bDepart.getText().equals("Depart")) {
				flight.flightDeparting();				// Call flightDeparting on the flight it observes
			}
		}
	};

	/* Constructor */
	public FlightDisplay(Flight flight) {
		panel = new JPanel(new GridBagLayout());

		Border black = BorderFactory.createLineBorder(Color.DARK_GRAY);
		panel.setBorder(black);								// Put a nice border around the display

		this.flight = flight;
		flight.addObserver(this);							// Observer pattern - each Flight updates its FlightDisplay 

		// Build display
		String[] travelPoints = flight.getTravelPoints();

		GridBagConstraints c = new GridBagConstraints();	// All components added with GridBagLayout
		c.ipadx = 5;
		c.ipady = 7;

		c.gridx = 0;
		c.gridy = 0;
		panel.add(new JLabel(flight.getCarrier() + ": " + flight.getFlightCode()), c);
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
		bDepart.addActionListener(departCommand);

		lStatus.setEditable(false);							// None of the JTextField "labels" should be focusable or editable
		lStatus.setFocusable(false);
		lPassengerCapacity.setEditable(false);
		lPassengerCapacity.setFocusable(false);
		lWeightCapacity.setEditable(false);
		lWeightCapacity.setFocusable(false);
		lVolumeCapacity.setEditable(false);
		lVolumeCapacity.setFocusable(false);

		// Add components to the panel
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
		panel.add(bDepart, c);

		updateDisplay();									// Update text in the "labels"
	}

	/* Updates JTextField 'labels' to show up-to-date information */
	private synchronized void updateDisplay() {
		float[] maxAttr = flight.getMaxAttributes();
		float[] curAttr = flight.getCurrentAttributes();
		lStatus.setText(flight.getCurrentState());
		if (!flight.isFlightWaiting()) {
			flightDeparted();								// Called if the flight isn't accepting customers
		}
		// Show current and max capacities
		lPassengerCapacity.setText("Passengers: " + curAttr[0] + " / " + maxAttr[0]);
		lWeightCapacity.setText("Weight: " + curAttr[1] + " / " + maxAttr[1]);
		lVolumeCapacity.setText("Volume: " + curAttr[2] + " / " + maxAttr[2]);
		// Update panel display
		panel.revalidate();
		panel.repaint();
	}

	/* Disables "Depart" button and changes its text */
	private void flightDeparted() {
		bDepart.setEnabled(false);
		bDepart.setText("Check-in Closed");
	}

	/* Returns the component displayed in the GUI */
	public JComponent getComponent() {
		return panel;
	}

	/* Callback when Flight internal data is updated - Observer pattern */
	@Override
	public void onNotify() {
		updateDisplay();
	}
}
