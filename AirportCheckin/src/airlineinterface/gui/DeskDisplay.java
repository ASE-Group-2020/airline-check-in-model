package airlineinterface.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import airlineinterface.Customer;
import airlineinterface.Desk;

public class DeskDisplay extends JPanel {

	private Desk desk;

	private JLabel lCustomerName, lBaggage, lBookingRef, lFlightCode, lDeskStage;

	public DeskDisplay(Desk desk) {
		super(new GridBagLayout());

		Border black = BorderFactory.createLineBorder(Color.DARK_GRAY);
		this.setBorder(black);

		this.desk = desk;
		lCustomerName = new JLabel();
		lBaggage = new JLabel();
		lBookingRef = new JLabel();
		lFlightCode = new JLabel();
		lDeskStage = new JLabel();

		GridBagConstraints c = new GridBagConstraints();
		c.ipadx = 5;
		c.ipady = 5;
		
		c.gridx = 0;
		c.gridy = 0;
		add(lCustomerName, c);
		c.gridx = 0;
		c.gridy = 1;
		add(lBaggage, c);
		c.gridx = 0;
		c.gridy = 2;
		add(lBookingRef, c);
		c.gridx = 0;
		c.gridy = 3;
		add(lFlightCode, c);
		c.gridx = 0;
		c.gridy = 4;
		add(lDeskStage, c);
		
		updateDisplay();
	}

	public void updateDisplay() {
		// TODO: Change from placeholder text to actual text
		lCustomerName.setText("CUSTOMER NAME");
		lBaggage.setText("BAGGAGE");
		lBookingRef.setText("BOOKING REF");
		lFlightCode.setText("FLIGHT CODE");
		lDeskStage.setText("DESK STAGE");
		/*
		Customer c = desk.getCurrentCustomer();
		lCustomerName.setText(c.getFirstName() + " " + c.getLastName());
		float[] bd = c.getBaggageDetails();
		lBaggage.setText("Baggage: " + bd[0] + "kg, " + bd[1] + "l");
		lBookingRef.setText("Booking code: " + c.getRefCode());
		lFlightCode.setText("Flight code: " + c.getFlightCode());
		lDeskStage.setText(desk.getCurrentStage());
		*/
		revalidate();
		repaint();
	}

}
