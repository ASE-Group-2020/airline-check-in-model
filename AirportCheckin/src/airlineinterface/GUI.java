package airlineinterface;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import exceptions.AlreadyCheckedInException;
import exceptions.InvalidValueException;

public class GUI implements ActionListener {

	private final static String STARTPANEL = "START";
	private final static String BAGGAGEPANEL = "BAGGAGE";
	private final static String DETAILSPANEL = "DETAILS";

	private Master m;

	private JFrame frame;
	private Container container;
	private CardLayout cards;
	private JTextField tfLastName, tfBookingRef, tfWeight, tfVolume;
	private JPanel pStart, pBaggage, pDetails;
	private JLabel lBaggageError, lFlightInfo, lDetails, lFeeDetails, lCheckInError;

	private Customer currentCustomer = null;
	private Flight currentFlight = null;
	private float currentWeight, currentVolume, currentFee;

	/**
	 * The GUI object is used for user interaction with the stored data.
	 * 
	 * @param m the Master object that stores all flight and customer data
	 */
	public GUI(Master m) {
		this.m = m;
		// Frame
		frame = new JFrame("Check-in");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				exitProcedure();
			}
		});
		frame.setSize(500, 500);
		// Constraints
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		// Start panel
		pStart = new JPanel(new GridBagLayout());
		JLabel lCheckIn = new JLabel("Check In");
		JLabel lCheckInInfo = new JLabel("Enter your last name and booking reference below.");
		lCheckInError = new JLabel("");
		lCheckInError.setForeground(Color.red);
		JLabel lLastName = new JLabel("Last Name:");
		tfLastName = new JTextField("");
		JLabel lBookingRef = new JLabel("Booking reference code:");
		tfBookingRef = new JTextField("");
		JButton bCheckIn = new JButton("Check In");
		bCheckIn.addActionListener(this);
		bCheckIn.setActionCommand("checkin");
		addComponent(pStart, lCheckIn, 0, 0);
		addComponent(pStart, lCheckInInfo, 0, 1, 2, 1);
		addComponent(pStart, lCheckInError, 0, 2, 2, 1);
		addComponent(pStart, lLastName, 0, 3);
		addComponent(pStart, tfLastName, 1, 3, 120);
		addComponent(pStart, lBookingRef, 0, 4);
		addComponent(pStart, tfBookingRef, 1, 4, 120);
		addComponent(pStart, bCheckIn, 0, 5);
		// Baggage panel
		pBaggage = new JPanel(new GridBagLayout());
		lFlightInfo = new JLabel("[ . . . . . . . . flight info . . . . . . . . ]");
		JLabel lBaggage = new JLabel("Input baggage weight and volume below");
		tfWeight = new JTextField();
		JLabel lWeight = new JLabel("kg");
		tfVolume = new JTextField();
		JLabel lVolume = new JLabel("l");
		JButton bBaggageBack = new JButton("Back");
		bBaggageBack.addActionListener(this);
		bBaggageBack.setActionCommand("baggageback");
		JButton bBaggageProceed = new JButton("Proceed");
		bBaggageProceed.addActionListener(this);
		bBaggageProceed.setActionCommand("baggageproceed");
		lBaggageError = new JLabel();
		lBaggageError.setForeground(Color.RED);
		addComponent(pBaggage, lFlightInfo, 0, 0, 4, 1);
		addComponent(pBaggage, lBaggage, 0, 1, 4, 1);
		addComponent(pBaggage, tfWeight, 0, 2, 40);
		addComponent(pBaggage, lWeight, 1, 2);
		addComponent(pBaggage, tfVolume, 2, 2, 40);
		addComponent(pBaggage, lVolume, 3, 2);
		addComponent(pBaggage, bBaggageBack, 0, 3, 2, 1);
		addComponent(pBaggage, bBaggageProceed, 2, 3, 2, 1);
		addComponent(pBaggage, lBaggageError, 0, 4, 4, 1);
		// Details panel
		pDetails = new JPanel(new GridBagLayout());
		JLabel lDetailsTitle = new JLabel("Your details are:");
		lDetails = new JLabel("[ . . . . . . . . details . . . . . . . . ]");
		JButton bDetailsBack = new JButton("Back");
		bDetailsBack.addActionListener(this);
		bDetailsBack.setActionCommand("detailsback");
		JLabel lFeeInfo = new JLabel("Total amount payable for baggage fees:");
		lFeeDetails = new JLabel("[ . . money . . ]");
		JButton bDetailsProceed = new JButton("Proceed");
		bDetailsProceed.addActionListener(this);
		bDetailsProceed.setActionCommand("detailsproceed");
		addComponent(pDetails, lDetailsTitle, 0, 0, 2, 1);
		addComponent(pDetails, lDetails, 0, 1, 2, 1);
		addComponent(pDetails, lFeeInfo, 0, 2, 2, 1);
		addComponent(pDetails, lFeeDetails, 0, 3, 2, 1);
		addComponent(pDetails, bDetailsBack, 0, 4);
		addComponent(pDetails, bDetailsProceed, 1, 4);
		// Master container
		container = frame.getContentPane();
		cards = new CardLayout();
		container.setLayout(cards);
		container.add(pStart, STARTPANEL);
		container.add(pBaggage, BAGGAGEPANEL);
		container.add(pDetails, DETAILSPANEL);
		/*
		 * Dynamic Zero Padding -> %06d if hashmap size is 6 digits long /!\ Only true
		 * if the hashmap keys start from 1, otherwise might have edge case scenario
		 * where values are 0-9, therefore a size of 10 but have padding of 1, not 2 :)
		 */
		// padding = "%0" + String.valueOf(m.getCustomerDatasetSize()/10).length() +
		// "d";
	}

	// Helper method for adding a component to a panel
	private void addComponent(JPanel p, JComponent c, int x, int y) {
		GridBagConstraints con = new GridBagConstraints();
		con.fill = GridBagConstraints.BOTH;
		con.gridx = x;
		con.gridy = y;
		p.add(c, con);
	}

	// Helper method for adding a component to a panel - used for text fields to
	// ensure they are wide
	private void addComponent(JPanel p, JComponent c, int x, int y, int padx) {
		GridBagConstraints con = new GridBagConstraints();
		con.fill = GridBagConstraints.BOTH;
		con.gridx = x;
		con.gridy = y;
		con.ipadx = padx;
		p.add(c, con);
	}

	// Helper method for adding a component to a panel where the component covers
	// multiple rows/columns
	private void addComponent(JPanel p, JComponent c, int x, int y, int width, int height) {
		GridBagConstraints con = new GridBagConstraints();
		con.fill = GridBagConstraints.BOTH;
		con.gridwidth = width;
		con.gridheight = height;
		con.gridx = x;
		con.gridy = y;
		p.add(c, con);
	}

	/**
	 * Clears the details for the current customer from the gui.
	 */
	public void clearDetails() {
		// Remove current customer and flight
		currentCustomer = null;
		currentFlight = null;
		// Remove all values entered in text fields
		tfBookingRef.setText("");
		tfLastName.setText("");
		tfWeight.setText("");
		tfVolume.setText("");
		// Remove all customer details
		lFlightInfo.setText("");
		lDetails.setText("");
		lFeeDetails.setText("");
	}

	/**
	 * Displays the check in panel in the gui.
	 */
	public void displayPanelStart() {
		// Clear customer-specific details
		clearDetails();
		// Hide error message
		lCheckInError.setVisible(false);
		cards.show(container, STARTPANEL);
		frame.setVisible(true);
	}

	/**
	 * Displays the flight details and baggage entry panel in the gui.
	 */
	public void displayPanelBaggage() {
		// Display flight details
		lFlightInfo.setText(getFlightDetails());
		// Hide baggage error message
		lBaggageError.setVisible(false);
		cards.show(container, BAGGAGEPANEL);
	}

	/**
	 * Displays the confirmation panel in the gui as long as the baggage values are
	 * accepted.
	 * 
	 * @throws InvalidValueException if the baggage was rejected.
	 */
	// TODO: refactor to remove exception here?
	public void displayPanelConfirm() throws InvalidValueException {
		// Display flight and baggage fee details
		lDetails.setText(getFlightDetails());
		lFeeDetails.setText(getBaggageFeeDetails());
		cards.show(container, DETAILSPANEL);
	}

	/*
	 * Create String giving the customer and customers flight information. Calls
	 * getFlight(String) from master to get the flight that the customer will be
	 * boarding, returns object Flight.
	 */
	/**
	 * Creates the String used to display flight details.
	 * 
	 * @return flight details String
	 */
	private String getFlightDetails() {
		// Create multiline label with HTML
		String sFlightInfo = "<html>";
		if (currentCustomer != null) {
			// Customer name
			sFlightInfo += String.format("Name:\t%s %s", currentCustomer.getFirstName(), currentCustomer.getLastName());
			String sFlightCode = currentCustomer.getFlightCode();
			// Flight code
			sFlightInfo += String.format("<br>Flight code:\t%s", sFlightCode);
			// Get the relevant flight from the code
			currentFlight = m.getFlight(sFlightCode);
			if (currentFlight == null) {
				// If the flight doesn't exist display an error message
				sFlightInfo += "<br>Error: No flight details to display";
			} else {
				// Display departure, arrival, and carrier
				String[] sTravelPoints = currentFlight.getTravelPoints();
				sFlightInfo += String.format("<br>Departure: %s<br>Arrival: %s", sTravelPoints[0], sTravelPoints[1]);
				sFlightInfo += String.format("<br>Carrier: %s", currentFlight.getCarrier());
			}

		} else {
			// If the customer isn't found display an error
			// Currently this shouldn't occur
			sFlightInfo += "Error: No Customer details to display";
		}
		return sFlightInfo;
	}

	/**
	 * Creates the String used to display the baggage fee.
	 * 
	 * @return the baggage fee String.
	 * @throws InvalidValueException if the oversize fee doesn't accept the baggage
	 *                               weight or volume.
	 */
	// TODO: refactor this elsewhere?
	private String getBaggageFeeDetails() throws InvalidValueException {
		// Get fee
		currentFee = m.getOversizeFee(currentWeight, currentVolume);
		// Multiline comment with HTML
		String sBaggageInfo = "<html>";
		// Display weight and volume
		sBaggageInfo += String.format("Weight: %skg<br>Volume: %sl", currentWeight, currentVolume);
		// Display fee
		sBaggageInfo += String.format("<br>Oversize fee: £%s", currentFee);
		return sBaggageInfo;
	}

	/**
	 * Called whenever a button in the gui is pressed.
	 */
	// TODO: refactor to break down?
	@Override
	public void actionPerformed(ActionEvent e) {
		// Find which button was pressed (use command String)
		String action = e.getActionCommand();
		// Switch to the correct button command
		switch (action) {
		case "checkin":
			// Check in button was pressed

			// Get customer reference code and customer surname
			String customerCode = tfBookingRef.getText().trim();
			String customerLastName = tfLastName.getText().trim();
			// Get the relevant customer from Master
			try {
				currentCustomer = m.getCustomer(customerCode, customerLastName);
			} catch (InvalidValueException e2) {
				// If InvalidValueException is thrown then the text fields had incorrect values
				// Display a meaningful error message
				lCheckInError.setText("<html>Last name must be purely alphabetical characters"
						+ "<br>Booking code must be purely numerical values");
				lCheckInError.setVisible(true);
				clearDetails();
				return;
			}
			// If the customer returned as null then there isn't a customer with these details
			if (currentCustomer == null) {
				// Display a meaningful error message
				lCheckInError.setText("No customer could be found with this reference code and last name");
				lCheckInError.setVisible(true);
				clearDetails();
				return;
			}
			// Don't allow a customer to check in twice
			if (currentCustomer.isCheckedIn()) {
				// Display a meaningful error message
				lCheckInError.setText("This customer has already check in");
				lCheckInError.setVisible(true);
				clearDetails();
				return;
			}
			// Move to the next panel
			displayPanelBaggage();
			return;
		case "baggageback":
			// Back button on baggage entering panel returns to start panel
			displayPanelStart();
			return;
		case "baggageproceed":
			// Proceed button on baggage entering panel
			// Try to parse the weight and volume as floats
			try {
				currentWeight = Float.parseFloat(tfWeight.getText());
				currentVolume = Float.parseFloat(tfVolume.getText());
				// If either value is -ve display an error 
				if (currentWeight < 0 || currentVolume < 0) {
					lBaggageError.setText("Weight and volume must be positive.");
					lBaggageError.setVisible(true);
					return;
				}
			} catch (NumberFormatException ex) {
				// If parsing the values failed with NumberFormatException a non-numeric character was entered
				// Display a meaningful message
				lBaggageError.setText("Weight and volume must be valid numbers.");
				lBaggageError.setVisible(true);
				return;
			}
			// Move to the next window
			// TODO: refactor this to not handle with try/catch here?
			try {
				displayPanelConfirm();
			} catch (InvalidValueException e2) {
				// InvaludValueException occurs if the baggage was rejected
				// TODO: add getter methods to obtain the maximum values from Master
				lBaggageError.setText("Weight and volume can't be above 200 Kg or 260 Liters.");
				lBaggageError.setVisible(true);
			}
			return;
		case "detailsback":
			// Back button on details confirmation panel returns to baggage
			displayPanelBaggage();
			return;
		case "detailsproceed":
			// Proceed button on details page finalises customer check-in
			try {
				// Check in the customer
				m.checkIn(currentCustomer, currentFlight, currentWeight, currentVolume, currentFee);
			} catch (InvalidValueException e1) {
				// If InvalidValueException is thrown something was wrong with the baggage 
				System.err.println(
						"There is an issue with one of the input types for checkIn(Customer, Flight, Float, Float)");
			} catch (AlreadyCheckedInException e1) {
				// If AlreadyCheckedInException is thrown then the customer was already checked in
				// This shouldn't be possible currently
				System.err.println(
						"The customer has already booked in, this exception shouldn't have been possible at this stage of the GUI");
				e1.printStackTrace();
			}
			// Return to start panel
			displayPanelStart();
			return;
		}
	}

	private void exitProcedure() {
		m.display();
		System.exit(0);
	}

}
