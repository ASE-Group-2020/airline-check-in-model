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
	private float currentWeight, currentVolume;

	public GUI(Master m) {
		this.m = m;
		// Frame
		frame = new JFrame("Check-in");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300, 300);
		// Constraints
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		// Start panel
		pStart = new JPanel(new GridBagLayout());
		JLabel lCheckIn = new JLabel("Check In");
		JLabel lInfo = new JLabel("[ . . . . . . . . information . . . . . . . . ]");
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
		lFeeDetails = new JLabel("£[ . . money . . ]");
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

	public void displayWindowConfirm() {
		lDetails.setText(getFlightDetails());
		lFeeDetails.setText(getBaggageFeeDetails());
		cards.show(container, DETAILSPANEL);
	}
	
	private String getFlightDetails() {
		String sFlightInfo = "";
		if (currentCustomer != null) {
			sFlightInfo += String.format("Name:\t%s %s", currentCustomer.getFirstName(), currentCustomer.getLastName());
			String sFlightCode = currentCustomer.getFlightCode();
			sFlightInfo += String.format("\nFlight code:\t%s", sFlightCode);
			
			// TODO: get flight from flight code
			/*
			Flight f = m.getFlight(sFlightCode);
			String[] sTravelPoints = f.getTravelPoints();
			sFlightInfo += String.format("\nDeparture:\t%s\tArrival:\t%s", sTravelPoints[0], sTravelPoints[1]);
			sFlightInfo += String.format("\nCarrier:\t%s", f.getCarrier());
			*/
		} else {
			sFlightInfo += "[Error: No flight details to display]";
		}
		return sFlightInfo;
	}
	private String getBaggageFeeDetails() {
		String sBaggageInfo = "";
		sBaggageInfo += String.format("Weight: %skg\tVolume: %sl", currentWeight, currentVolume);
		// TODO: Oversize fee
		// sBaggageInfo += String.format("\nOversize fee: £%s", m.getOversizeFee(currentWeight, currentVolume));
		return sBaggageInfo;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		switch (action) {
		case "checkin":
			// TODO: Get customer object from master, show customer details, and show error
			// if no valid customer
			String customerCode = tfBookingRef.getText();
			String customerLastName = tfLastName.getText();
			currentCustomer = null;
			displayPanelBaggage();
			break;
		case "baggageback":
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
			displayWindowConfirm();
			break;
		case "detailsback":
			displayPanelBaggage();
			break;
		case "detailsproceed":
			// TODO: check in customer, change outcome based on check-in success
			m.checkIn(currentCustomer, currentWeight, currentVolume);
			displayPanelStart();
			break;
		}
	}

}
