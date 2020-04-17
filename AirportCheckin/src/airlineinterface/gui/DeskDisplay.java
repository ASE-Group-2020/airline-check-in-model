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
	private JTextField lCustomerName, lCustomerClass, lBaggage, lBookingRef, lFlightCode, lDeskStage;
	private JButton toggle;
	
	private String newButtonText = "";
	
	private static String WAITING_FOR_DESK = "Please Wait...";
	private static String CLOSED_DESK_STAGE = "Desk action: Closed.";
	private static String READY_TO_OPEN = "Open";
	private static String READY_TO_CLOSE = "Close";
	
	private ActionListener openCloseDesk = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (newButtonText.equals(""))
			{
				// if the desk is already closed
				if (desk.getCurrentStage().equals(CLOSED_DESK_STAGE))
				{
					newButtonText = READY_TO_CLOSE;
					desk.OpenDesk();
				}
				else // if the desk is open
				{
					newButtonText = READY_TO_OPEN;
					desk.CloseDesk();
				}
				toggle.setText(WAITING_FOR_DESK);
			}
		}
	};

	public DeskDisplay(Desk desk) {
		panel = new JPanel(new GridBagLayout());

		Border black = BorderFactory.createLineBorder(Color.DARK_GRAY);
		panel.setBorder(black);

		this.desk = desk;
		desk.addObserver(this);

		lCustomerName = new JTextField("", 20);
		lCustomerClass = new JTextField("", 20);
		lBaggage = new JTextField("", 20);
		lBookingRef = new JTextField("", 20);
		lFlightCode = new JTextField("", 20);
		lDeskStage = new JTextField("", 20);
		toggle = new JButton("Close");
		toggle.addActionListener(openCloseDesk);
		
		lCustomerName.setEditable(false);
		lCustomerName.setFocusable(false);
		lCustomerClass.setEditable(false);
		lCustomerClass.setFocusable(false);
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
		c.ipady = 7;
		
		c.gridx = 0;
		c.gridy = 0;
		panel.add(lCustomerName, c);
		c.gridx = 0;
		c.gridy = 1;
		panel.add(lCustomerClass, c);
		c.gridx = 0;
		c.gridy = 2;
		panel.add(lBaggage, c);
		c.gridx = 0;
		c.gridy = 3;
		panel.add(lBookingRef, c);
		c.gridx = 0;
		c.gridy = 4;
		panel.add(lFlightCode, c);
		c.gridx = 0;
		c.gridy = 5;
		panel.add(lDeskStage, c);
		c.gridx = 0;
		c.gridy = 6;
		panel.add(toggle, c);
		
		updateDisplay();
	}

	public void updateDisplay() {
		try {
			Customer c = desk.getCurrentCustomer();
			if (c == null) NoCustomer();
			else
			{
				lCustomerName.setText(c.getFirstName() + " " + c.getLastName());
				lCustomerClass.setText(c.getSeatingClass() + " Class");
				float[] bd = c.getBaggageDetails();
				lBaggage.setText("Baggage: " + bd[0] + "kg, " + bd[1] + "l");
				lBookingRef.setText("Booking code: " + c.getRefCode());
				lFlightCode.setText("Flight code: " + c.getFlightCode());
				lDeskStage.setText(desk.getCurrentStage());
			}
		} catch (ObjectNotFoundException e) { NoCustomer(); }
		
		if (toggle.getText().equals(WAITING_FOR_DESK))
		{
			// if the desk is being opened
			if (newButtonText.equals(READY_TO_CLOSE) && !desk.getCurrentStage().equals(CLOSED_DESK_STAGE))
			{
				toggle.setText(READY_TO_CLOSE);
				newButtonText = "";
			}
			// if the desk is being closed
			else if (newButtonText.equals(READY_TO_OPEN) && desk.getCurrentStage().equals(CLOSED_DESK_STAGE))
			{
				toggle.setText(READY_TO_OPEN); 
				newButtonText = "";
			}
		}
		
		panel.revalidate();
		panel.repaint();
	}
	
	private void NoCustomer()
	{
		lCustomerName.setText("");
		lBaggage.setText("");
		lBookingRef.setText("");
		lFlightCode.setText("");
		lDeskStage.setText(desk.getCurrentStage());
	}
	
	public JComponent getComponent() {
		return panel;
	}

	@Override
	public void onNotify() {
		updateDisplay();
	}

}
