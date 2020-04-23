package airlineinterface.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Queue;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import airlineinterface.Customer;
import airlineinterface.Observer;
import airlineinterface.WaitingQueue;

public class QueueDisplay extends Observer {
	
	private WaitingQueue q;									// Keep track of queue so up-to-date data can be retrieved
	private JPanel panel;									// Component to return to be displayed
	private JTable tableStandard;							// WaitingQueue contains two tables, so two JTables need to be displayed
	private JTable tableBusiness;

	/* Constructor */
	public QueueDisplay(WaitingQueue q) {
		panel = new JPanel(new GridBagLayout());
		this.q = q;
		q.addObserver(this);								// Observer pattern - each WaitingQueue updates its QueueDisplay 
		
		GridBagConstraints c = new GridBagConstraints();	// All components added with GridBagLayout
		c.ipadx = 2;
		c.ipady = 2;
		
		c.gridx = 0;
		c.gridy = 0;
		panel.add(new JLabel("Standard Class Queue"), c);
		tableStandard = new JTable();
		tableStandard.setFillsViewportHeight(true);
		JScrollPane standardTablePane = new JScrollPane(tableStandard);	// JTable is added to a JScrollPane to display it - done for both tables
		c.gridx = 0;
		c.gridy = 1;
		panel.add(standardTablePane, c);
		
		c.gridx = 1;
		c.gridy = 0;
		panel.add(new JLabel("Business Class Queue"), c);
		tableBusiness = new JTable();
		tableBusiness.setFillsViewportHeight(true);
		JScrollPane businessTablePane = new JScrollPane(tableBusiness);
		c.gridx = 1;
		c.gridy = 1;
		panel.add(businessTablePane, c);
		
		updateDisplay();									// Update tables
	}

	/* Updates the JTables to show up-to-date queues */
	public synchronized void updateDisplay()
	{	
		// New QueueTableModels are created with a queue copied from WaitingQueue
		// The queues are copied to prevent the GUI from blocking the simulation if construction takes a while
		tableStandard.setModel(new QueueTableModel(q.getWaitingCopyStandard()));
		tableBusiness.setModel(new QueueTableModel(q.getWaitingCopyBusiness()));
	}

	/* Returns the component displayed in the GUI */
	public JComponent getComponent() {
		return panel;
	}

	/* Callback when WaitingQueue internal data is updated - Observer pattern */
	@Override
	public void onNotify() {
		// Use invokeLater to avoid Swing errors from updating GUI from the wrong thread
		// Note - this error never occurred with the other Displays
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				updateDisplay();
			}
		});
	}

	/* Custom TableModel that works with a Queue of Customer objects */
	private class QueueTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 491620524778167217L;							// Automatically generated
		
		private String[] columnNames = { "Name", "Code", "Baggage (kg)", "Baggage (cm)" };
		private String[][] data;																	// Data returned to populate table in display

		/* Constructor */
		public QueueTableModel(Queue<Customer> customers) {
			SetCustomers(customers);
		}
		
		public void SetCustomers(Queue<Customer> customers)
		{
			data = new String[customers.size()][columnNames.length];
			int index = 0;
			for (Customer c : customers) {															// Add each (non-null) customer to the data
				if (c == null) continue;
				data[index][0] = c.getFirstName() + " " + c.getLastName();
				data[index][1] = c.getRefCode();
				data[index][2] = c.getBaggageWeightString();
				data[index][3] = c.getBaggageDimensionString();
				index++;
			}
		}

		/* Below - methods used by JTable to display data */
		// ----
		@Override
		public String getColumnName(int col) {
			return columnNames[col];
		}

		@Override
		public int getRowCount() {
			return data.length;
		}

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return data[rowIndex][columnIndex];
		}
		// ----
	}
}
