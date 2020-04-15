package airlineinterface.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import airlineinterface.Customer;
import airlineinterface.Desk;
import airlineinterface.Observer;
import exceptions.ObjectNotFoundException;

public class DeskDisplay extends Observer {

	private Desk desk;
	
	private JPanel panel;
	private JTextField lCustomerName, lBaggage, lBookingRef, lFlightCode, lDeskStage;
	private JButton toggle;
	
	private ActionListener openCloseDesk = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			toggle.setText(desk.getCurrentStage().equals("Desk action: Closed") ? "Open" : "Close");
			// TODO implement open/close desk functionality
			
		}
	};

	public DeskDisplay(Desk desk) {
		panel = new JPanel(new GridBagLayout());

		Border black = BorderFactory.createLineBorder(Color.DARK_GRAY);
		panel.setBorder(black);

		this.desk = desk;
		desk.addObserver(this);

		lCustomerName = new JTextField("", 20);
		lBaggage = new JTextField("", 20);
		lBookingRef = new JTextField("", 20);
		lFlightCode = new JTextField("", 20);
		lDeskStage = new JTextField("", 20);
		toggle = new JButton("Close");
		toggle.addActionListener(openCloseDesk);
		
		lCustomerName.setEditable(false);
		lCustomerName.setFocusable(false);
		lBaggage.setEditable(false);
		lBaggage.setFocusable(false);
		lBookingRef.setEditable(false);
		lBookingRef.setFocusable(false);
		lFlightCode.setEditable(false);
		lFlightCode.setFocusable(false);
		lDeskStage.setEditable(false);
		lDeskStage.setFocusable(false);

		GridBagConstraints c = new GridBagConstraints();
		c.ipadx = 5;
		c.ipady = 6;
		
		c.gridx = 0;
		c.gridy = 0;
		panel.add(lCustomerName, c);
		c.gridx = 0;
		c.gridy = 1;
		panel.add(lBaggage, c);
		c.gridx = 0;
		c.gridy = 2;
		panel.add(lBookingRef, c);
		c.gridx = 0;
		c.gridy = 3;
		panel.add(lFlightCode, c);
		c.gridx = 0;
		c.gridy = 4;
		panel.add(lDeskStage, c);
		c.gridx = 0;
		c.gridy = 5;
		panel.add(toggle, c); // TODO add button here
		
		updateDisplay();
	}

	public void updateDisplay() {
		Customer c;
		try {
			c = desk.getCurrentCustomer();
			lCustomerName.setText(c.getFirstName() + " " + c.getLastName());
			float[] bd = c.getBaggageDetails();
			lBaggage.setText("Baggage: " + bd[0] + "kg, " + bd[1] + "l");
			lBookingRef.setText("Booking code: " + c.getRefCode());
			lFlightCode.setText("Flight code: " + c.getFlightCode());
			lDeskStage.setText(desk.getCurrentStage());
		} catch (ObjectNotFoundException e) {
			lCustomerName.setText("No current customer");
			lBaggage.setText("");
			lBookingRef.setText("");
			lFlightCode.setText("");
			lDeskStage.setText(desk.getCurrentStage());
		}
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
