package airlineinterface;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	private JLabel lBaggageError, lFlightInfo, lDetails, lFeeDetails;

	private Customer currentCustomer = null;
	private Flight customerFlight = null;
	private float currentWeight, currentVolume;

	// label variables
	private JLabel lInfo;

	private String padding; // helper variable to ensure dynamic padding with accordance to allCustomers HashMap size

	public GUI(Master m) {
		this.m = m;
		// Frame
		frame = new JFrame("Check-in");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
		// Constraints
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		// Start panel
		pStart = new JPanel(new GridBagLayout());
		JLabel lCheckIn = new JLabel("Check In");
		lInfo = new JLabel("[ . . . . . . . . information . . . . . . . . ]");
		JLabel lLastName = new JLabel("Last Name:");
		tfLastName = new JTextField("");
		JLabel lBookingRef = new JLabel("Booking reference code:");
		tfBookingRef = new JTextField("");
		JButton bCheckIn = new JButton("Check In");
		bCheckIn.addActionListener(this);
		bCheckIn.setActionCommand("checkin");
		addComponent(pStart, lCheckIn, 0, 0);
		addComponent(pStart, lInfo, 0, 1, 2, 1);
		addComponent(pStart, lLastName, 0, 2);
		addComponent(pStart, tfLastName, 1, 2, 120);
		addComponent(pStart, lBookingRef, 0, 3);
		addComponent(pStart, tfBookingRef, 1, 3, 120);
		addComponent(pStart, bCheckIn, 0, 4);
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
		lFeeDetails = new JLabel("�[ . . money . . ]");
		JButton bDetailsProceed = new JButton("Proceed");
		bDetailsProceed.addActionListener(this);
		bDetailsProceed.setActionCommand("detailsproceed");
		addComponent(pDetails, lDetailsTitle, 0, 0);
		addComponent(pDetails, lDetails, 0, 1);
		addComponent(pDetails, bDetailsBack, 0, 2);
		addComponent(pDetails, lFeeInfo, 0, 3);
		addComponent(pDetails, lFeeDetails, 0, 4);
		addComponent(pDetails, bDetailsProceed, 0, 5);
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
		padding = "%0" + String.valueOf(m.getCustomerDatasetSize()/10).length() + "d";
	}

	private void addComponent(JPanel p, JComponent c, int x, int y) {
		GridBagConstraints con = new GridBagConstraints();
		con.fill = GridBagConstraints.BOTH;
		con.gridx = x;
		con.gridy = y;
		p.add(c, con);
	}

	private void addComponent(JPanel p, JComponent c, int x, int y, int padx) {
		GridBagConstraints con = new GridBagConstraints();
		con.fill = GridBagConstraints.BOTH;
		con.gridx = x;
		con.gridy = y;
		con.ipadx = padx;
		p.add(c, con);
	}

	private void addComponent(JPanel p, JComponent c, int x, int y, int width, int height) {
		GridBagConstraints con = new GridBagConstraints();
		con.fill = GridBagConstraints.BOTH;
		con.gridwidth = width;
		con.gridheight = height;
		con.gridx = x;
		con.gridy = y;
		p.add(c, con);
	}

	public void displayPanelStart() {
		currentCustomer = null;
		tfBookingRef.setText("");
		tfLastName.setText("");
		tfWeight.setText("");
		tfVolume.setText("");
		cards.show(container, STARTPANEL);
		frame.setVisible(true);
	}

	// Note - Customer c not in class diagram
	public void displayPanelBaggage() {
		lBaggageError.setVisible(false);
		lFlightInfo.setText(getFlightDetails());
		cards.show(container, BAGGAGEPANEL);
	}

	public void displayWindowConfirm() throws InvalidValueException {
		lDetails.setText(getFlightDetails());
		lFeeDetails.setText(getBaggageFeeDetails());
		cards.show(container, DETAILSPANEL);
	}

	/*
	 * Create String giving the customer and customers flight information. Calls
	 * getFlight(String) from master to get the flight that the customer will be
	 * boarding, returns object Flight.
	 */
	private String getFlightDetails() {
		System.out.println("in getFlightDetails!!");
		String sFlightInfo = "";
		if (currentCustomer != null) {
			sFlightInfo += String.format("Name:\t%s %s", currentCustomer.getFirstName(), currentCustomer.getLastName());
			String sFlightCode = currentCustomer.getFlightCode();
			sFlightInfo += String.format("\nFlight code:\t%s", sFlightCode);

			/*
			 * IF sFlightCode DOESN'T EXSIST IN allFlights HashMap, return is NULL. A NULL
			 * RETURN WILL CRASH THE CODE, NEED TO FIND A WAY TO HANDLE THAT.
			 */
			customerFlight = m.getFlight(sFlightCode); // what is this? (David)
			if (customerFlight == null) {
				sFlightInfo += "[Error: No flight details to display]";
			} else {
				// The customer and flight obj are associated by the FlightCode that they both
				// possess.
				// This line of code retrieves the flight obj linked to that customer. (to whom
				// ever asked the question)

				String[] sTravelPoints = customerFlight.getTravelPoints();
				sFlightInfo += String.format("\nDeparture:\t%s\tArrival:\t%s", sTravelPoints[0], sTravelPoints[1]);
				sFlightInfo += String.format("\nCarrier:\t%s", customerFlight.getCarrier());
			}

		} else {
			sFlightInfo += "[Error: No Customer details to display]";
		}
		return sFlightInfo;
	}

	/*
	 * Create String giving the baggage oversize & overvolume fee. Calls
	 * getOversizefee(Customer, float, float) from master to get the float.
	 */
	private String getBaggageFeeDetails() throws InvalidValueException {
		String sBaggageInfo = "";
		sBaggageInfo += String.format("Weight: %skg\tVolume: %sl", currentWeight, currentVolume);
		sBaggageInfo += String.format("\nOversize fee: %s", m.getOversizeFee(currentWeight, currentVolume));
		System.out.println("the sBaggageInfo String looks like this: \n" + sBaggageInfo);
		return sBaggageInfo;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		System.out.println("in actionPerformed \n the current action is: " + action);
		switch (action) {
		case "checkin":
			System.out.println("in check in my dude");
			// TODO: Get customer object from master, show customer details, and show error
			// if no valid customer
			//String customerCode = String.format(padding, Integer.parseInt(tfBookingRef.getText().trim())); // <-- padding code, uncomment line to use
			String customerCode = tfBookingRef.getText().trim();
			System.out.println(customerCode);
			String customerLastName = tfLastName.getText().trim();
			System.out.println(customerLastName);

			// customerLastName.toLowerCase(); // Names will be changed to all lower case,
			// as this is what they are in input file <- you can use .ignoreCase() in the
			// equals check instead :)

			/*
			 * IF customerCode && customerCode DOESN'T EXSIST IN allCustomer HashMap, return
			 * is NULL. If error occurs restarts displayPAnelStart with info on error to
			 * help end user.
			 */

			try {
				System.out.println("In currentCustomer");
				currentCustomer = m.getCustomer(customerCode, customerLastName);
				System.out.println(currentCustomer);
			} catch (InvalidValueException e2) {
				// catches invalid format of name or customerReference
				lInfo.setText("[" + e2.getMessage() + "]");
				lInfo.setForeground(Color.red);
				displayPanelStart();
				break;
			}

			// This deals with the customer returning null because it wasn't found in
			// HashMap or the last name doesn't match.
			if (currentCustomer == null) {
				lInfo.setText("[ No customer could be found with this reference code and last name ]");
				lInfo.setForeground(Color.red);
				displayPanelStart();
				break;
			}
			if (currentCustomer.isCheckedIn()) {
				lInfo.setText("[ The customer has already check in ]");
				lInfo.setForeground(Color.red);
				displayPanelStart();
				break;
			}

			displayPanelBaggage();
			break;

		case "baggageback":
			// rest label
			lInfo.setText("[ . . . . . . . . information . . . . . . . . ]");
			lInfo.setForeground(Color.black);
			displayPanelStart();
			break;
		case "baggageproceed":
			try {
				currentWeight = Float.parseFloat(tfWeight.getText());
				currentVolume = Float.parseFloat(tfVolume.getText());
				if (currentWeight < 0 || currentVolume < 0) {
					lBaggageError.setText("Weight and volume must be positive.");
					lBaggageError.setVisible(true);
					return;
				}
			} catch (NumberFormatException ex) {
				lBaggageError.setText("Weight and volume must be valid numbers.");
				lBaggageError.setVisible(true);
				return;
			}
			// rest label
			lInfo.setText("[ . . . . . . . . information . . . . . . . . ]");
			lInfo.setForeground(Color.black);
			try {
				displayWindowConfirm();
			} catch (InvalidValueException e2) {
				lBaggageError.setText("Weight and volume can't be above 200 Kg or 260 Liters.");
				lBaggageError.setVisible(true);
			}
			break;
		case "detailsback":
			displayPanelBaggage();
			break;
		case "detailsproceed":
			// TODO: check in customer, change outcome based on check-in success
			System.out.println("customerFlight.getCarrier(): " + customerFlight.getCarrier());
			try {
				m.checkIn(currentCustomer, customerFlight, currentWeight, currentVolume);
			} catch (InvalidValueException e1) {
				System.err.println(
						"There is an issue with one of the input types for checkIn(Customer, Flight, Float,Float)");
			} catch (AlreadyCheckedInException e1) {
				System.err.println(
						"The customer has already booked in, this exception shouldn't have been possible at this stage of the GUI");
				e1.printStackTrace();
			}
			displayPanelStart();
			break;
		}
	}

}
