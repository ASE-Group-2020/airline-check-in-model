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

import airlineinterface.Customer;
import airlineinterface.Desk;
import airlineinterface.Observer;
import exceptions.ObjectNotFoundException;

public class DeskDisplay extends Observer {

	private Desk desk;										// Keep track of desk so up-to-date data can be retrieved
	
	private JPanel panel;									// Component to return to be displayed
	private JTextField lCustomerName, lCustomerClass, lBaggage, lBookingRef, lFlightCode, lDeskStage;
															// No text entry is used - JTextFields are treated as labels. This was used to fix a bug relating to
															// the QueueDisplay JTable not having enough room to display correctly JLabel text changed.
	private JLabel lDeskName;								// Label still used for DeskName display as it never changes
	private JButton bDeskToggle;							// Toggles the desk between enabled and disabled
	
	private String newButtonText = "";
	
	/* Set text the desk toggle button can have */
	private final static String WAITING_FOR_DESK = "Please Wait...";
	private final static String READY_TO_OPEN = "Open";
	private final static String READY_TO_CLOSE = "Close";
	// Return from Desk.getCurrentStage if the desk is closed - used when changing button text
	private final static String CLOSED_DESK_STAGE = "Desk action: Closed.";
	
	/* ActionListener called when desk toggle button is pressed */
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
				bDeskToggle.setText(WAITING_FOR_DESK);
			}
		}
	};

	/* Constructor */
	public DeskDisplay(Desk desk) {
		panel = new JPanel(new GridBagLayout());

		Border black = BorderFactory.createLineBorder(Color.DARK_GRAY);
		panel.setBorder(black);								// Put a nice border around the display

		this.desk = desk;
		desk.addObserver(this);								// Observer pattern - each Desk updates its DeskDisplay

		lCustomerName = new JTextField("", 20);				// Initialise all JTextField 'labels' with the same width and blank text - updated in updateDisplay
		lCustomerClass = new JTextField("", 20);
		lBaggage = new JTextField("", 20);
		lBookingRef = new JTextField("", 20);
		lFlightCode = new JTextField("", 20);
		lDeskStage = new JTextField("", 20);
		lDeskName = new JLabel(desk.getDeskName());
		bDeskToggle = new JButton("Close");
		bDeskToggle.addActionListener(openCloseDesk);
		
		lCustomerName.setEditable(false);					// None of the JTextField "labels" should be focusable or editable
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
		lDeskName.setFocusable(false);

		GridBagConstraints c = new GridBagConstraints();	// All components added with GridBagLayout
		c.ipadx = 5;
		c.ipady = 7;
		c.gridwidth = 2;
		
		c.gridx = 0;
		c.gridy = 0;
		panel.add(lCustomerName, c);
		c.gridy = 1;
		panel.add(lCustomerClass, c);
		c.gridy = 2;
		panel.add(lBaggage, c);
		c.gridy = 3;
		panel.add(lBookingRef, c);
		c.gridy = 4;
		panel.add(lFlightCode, c);
		c.gridy = 5;
		panel.add(lDeskStage, c);
		c.gridy = 6;
		c.gridwidth = 1;
		panel.add(lDeskName, c);
		c.gridx = 1;
		panel.add(bDeskToggle, c);
		
		updateDisplay();									// Update text in the "labels"
	}

	/* Updates JTextField 'labels' to show up-to-date information */
	public void updateDisplay() {
		try {
			Customer c = desk.getCurrentCustomer();			// Exception called if no customer is available
			if (c == null) NoCustomer();					// Extra catch in case that callback changes
			else
			{
				// Customer exists, get their details and display them in the labels
				lCustomerName.setText(c.getFirstName() + " " + c.getLastName());
				lCustomerClass.setText(c.getSeatingClass() + " Class");
				lBaggage.setText("Baggage: " + c.getBaggageWeightString() + ", " + c.getBaggageDimensionString());
				lBookingRef.setText("Booking code: " + c.getRefCode());
				lFlightCode.setText("Flight code: " + c.getFlightCode());
				lDeskStage.setText(desk.getCurrentStage());
			}
		} catch (ObjectNotFoundException e) { NoCustomer(); }
		
		if (bDeskToggle.getText().equals(WAITING_FOR_DESK))	// Update button text if it was waiting
		{
			// if the desk is being opened
			if (newButtonText.equals(READY_TO_CLOSE) && !desk.getCurrentStage().equals(CLOSED_DESK_STAGE))
			{
				bDeskToggle.setText(READY_TO_CLOSE);
				newButtonText = "";
			}
			// if the desk is being closed
			else if (newButtonText.equals(READY_TO_OPEN) && desk.getCurrentStage().equals(CLOSED_DESK_STAGE))
			{
				bDeskToggle.setText(READY_TO_OPEN); 
				newButtonText = "";
			}
		}
		// Update panel display
		panel.revalidate();
		panel.repaint();
	}
	
	/* Clear display labels */
	private void NoCustomer()
	{
		lCustomerName.setText("");
		lCustomerClass.setText("");
		lBaggage.setText("");
		lBookingRef.setText("");
		lFlightCode.setText("");
		lDeskStage.setText(desk.getCurrentStage());
	}
	
	/* Returns the component displayed in the GUI */
	public JComponent getComponent() {
		return panel;
	}

	/* Callback when Desk internal data is updated - Observer pattern */
	@Override
	public void onNotify() {
		updateDisplay();		// Update the display when data changes
	}

}
