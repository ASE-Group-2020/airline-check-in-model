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
import airlineinterface.Simulator;

public class SpeedDisplay extends Observer {
	
	private JPanel panel, subPanel;
	private JTextField lSimTime, lSimEnd, lBlank, lSimSpeed, lNewSpeed;
	private JButton enterSpeed, stopButton;
	
	private ActionListener updateSpeed = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			try { Simulator.get().setSimulationSpeed(Math.max(0, Float.parseFloat(lNewSpeed.getText()))); }
			catch (NumberFormatException nfe) {}
			lNewSpeed.setText("");
		}
	};
	
	private ActionListener stopSimulation = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			Simulator.get().stopSimulation();
		}
	};
	
	public SpeedDisplay()
	{
		panel = new JPanel(new GridBagLayout());
		panel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		
		lSimTime = new JTextField("", 20);
		lSimEnd = new JTextField("", 20);
		lBlank = new JTextField("", 20);
		lSimSpeed = new JTextField("", 20);
		lNewSpeed = new JTextField("", 10);
		
		enterSpeed = new JButton("Set Speed");
		enterSpeed.addActionListener(updateSpeed);
		stopButton = new JButton("Stop Simulation");
		stopButton.addActionListener(stopSimulation);
		
		lSimTime.setEditable(false);
		lSimTime.setFocusable(false);
		lSimEnd.setEditable(false);
		lSimEnd.setFocusable(false);
		lBlank.setEditable(false);
		lBlank.setFocusable(false);
		lSimSpeed.setEditable(false);
		lSimSpeed.setFocusable(false);
		
		GridBagConstraints c = new GridBagConstraints();
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
		panel.add(lNewSpeed, c); 
	}
	
	// TODO get speed variables from Simulator
	public void updateDisplay()
	{
		lSimTime.setText("Simulator Time: " + Simulator.get().getCurrentTime());
		lSimEnd.setText("Simulator End: " + Simulator.get().getRealRunTime());
		lSimSpeed.setText("Simulator Speed: " + Simulator.get().getSimSpeed());
	}
	
	public JComponent getComponent() {
		return panel;
	}
	
	@Override
	public void onNotify()
	{
		updateDisplay();
	}

}
