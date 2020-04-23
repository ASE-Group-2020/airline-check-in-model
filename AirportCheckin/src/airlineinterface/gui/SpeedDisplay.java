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

import airlineinterface.Observer;
import airlineinterface.Simulator;

public class SpeedDisplay extends Observer {
	
	private JPanel panel, subPanel;											// Panel to be returned and entered into GUI
	private JTextField lSimTime, lSimEnd, lBlank, lSimSpeed, tfNewSpeed;	// "labels" for updating information, tfNewSpeed to enter simulation speed
	private JButton enterSpeed, stopButton;									// Buttons to change simulation speed and stop simulation entirely
	
	// action listener for new simulation speed button
	private ActionListener updateSpeed = new ActionListener()				// Speed change ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{																	// If parsing succeeds change Simulator speed
			try { Simulator.get().setSimulationSpeed(Math.max(0, Float.parseFloat(tfNewSpeed.getText()))); }
			catch (NumberFormatException nfe) {}							// Otherwise clear speed text
			tfNewSpeed.setText("");											// Clear text
		}
	};
	
	// action listener for stop simulation button
	private ActionListener stopSimulation = new ActionListener()			// Simulation-stopping ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			Simulator.get().stopSimulation();								// Call Simulator-stopping method
		}
	};
	
	// SpeedDisplay shows information relating to the Simulator, so it doesn't need to store a reference to any model objects
	public SpeedDisplay()
	{
		panel = new JPanel(new GridBagLayout());
		panel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));	// Add a border around the display
		
		lSimTime = new JTextField("", 20);									// Set up 'labels' to be updated
		lSimEnd = new JTextField("", 20);
		lBlank = new JTextField("", 20);
		lSimSpeed = new JTextField("", 20);
		tfNewSpeed = new JTextField("", 10);
		
		// Create buttons and add ActionListeners
		enterSpeed = new JButton("Set Speed");
		enterSpeed.addActionListener(updateSpeed);
		stopButton = new JButton("Stop Simulation");
		stopButton.addActionListener(stopSimulation);
		
		lSimTime.setEditable(false);										// None of the JTextField "labels" should be focusable or editable
		lSimTime.setFocusable(false);
		lSimEnd.setEditable(false);
		lSimEnd.setFocusable(false);
		lBlank.setEditable(false);
		lBlank.setFocusable(false);
		lSimSpeed.setEditable(false);
		lSimSpeed.setFocusable(false);
		
		GridBagConstraints c = new GridBagConstraints();
		// Add components to the panel
		c.ipadx = 5;
		c.ipady = 6;
		subPanel = new JPanel(new GridBagLayout());
		c.gridx = 0;
		c.gridy = 5;
		panel.add(subPanel, c); 
		c.gridx = 0;
		c.gridy = 0;
		subPanel.add(enterSpeed, c);
		c.gridx = 1;
		c.gridy = 0;
		subPanel.add(stopButton, c); 
		
		c.gridx = 0;
		c.gridy = 0;
		panel.add(lSimTime, c);
		c.gridx = 0;
		c.gridy = 1;
		panel.add(lSimEnd, c);
		c.gridx = 0;
		c.gridy = 2;
		panel.add(lBlank, c);
		c.gridx = 0;
		c.gridy = 3;
		panel.add(lSimSpeed, c); 
		c.gridx = 0;
		c.gridy = 4;
		panel.add(tfNewSpeed, c); 
	}

	/* Updates JTextField 'labels' to show up-to-date information */
	public void updateDisplay()
	{
		lSimTime.setText("Simulator Time: " + Simulator.get().getCurrentTime());
		lSimEnd.setText("Simulator End: " + Simulator.get().getRealRunTime());
		lSimSpeed.setText("Simulator Speed: " + Simulator.get().getSimSpeed());
	}

	/* Returns the component displayed in the GUI */
	public JComponent getComponent() {
		return panel;
	}

	/* Callback when Simulator speed or time data is updated - Observer pattern */
	@Override
	public void onNotify()
	{
		updateDisplay();
	}

}
