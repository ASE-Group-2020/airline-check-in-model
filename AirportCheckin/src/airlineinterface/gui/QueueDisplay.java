package airlineinterface.gui;

import java.awt.GridBagLayout;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import airlineinterface.Customer;
import airlineinterface.Observer;
import airlineinterface.WaitingQueue;

public class QueueDisplay extends Observer {
	
	private WaitingQueue q;
	
	private JPanel panel;
	private JTable table;
	private JScrollPane tablePane;

	public QueueDisplay(WaitingQueue q) {
		panel = new JPanel(new GridBagLayout());
		q.addObserver(this);
		
		table = new JTable();
		table.setFillsViewportHeight(true);
		tablePane = new JScrollPane(table);
		panel.add(tablePane);
		
		this.q = q;
		updateDisplay();
	}

	public synchronized void updateDisplay()
	{	
		//if (tablePane != null) panel.remove(tablePane);
		
		
		
		table.setModel(new QueueTableModel(q.getWaitingCopy()));
	}
	
	public JComponent getComponent() {
		return panel;
	}

	@Override
	public void onNotify() {
		updateDisplay();
	}

	private class QueueTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 491620524778167217L;
		
		private String[] columnNames = { "Name", "Code", "Baggage (kg)", "Baggage (l)" };
		private String[][] data;
		
		public QueueTableModel() {}

		public QueueTableModel(Queue<Customer> customers) {
			/*
			 * JTable of customers
			 * 		lastName
			 * 		firstName
			 * 		booking ref
			 * 		flight code
			 * 		baggage weight
			 * 		baggage vol
			 */
			/*
			data = new String[customers.size()][columnNames.length];
			int index = 0;
			for (Customer c : customers) {
				data[index][0] = c.getFirstName() + " " + c.getLastName();
				data[index][1] = c.getRefCode();
				float[] bd = c.getBaggageDetails();
				data[index][2] = "" + bd[0];
				data[index][3] = "" + bd[1];
				index++;
			}*/
			SetCustomers(customers);
		}
		
		public void SetCustomers(Queue<Customer> customers)
		{
			data = new String[customers.size()][columnNames.length];
			int index = 0;
			for (Customer c : customers) {
				if (c == null) continue;
				data[index][0] = c.getFirstName() + " " + c.getLastName();
				data[index][1] = c.getRefCode();
				float[] bd = c.getBaggageDetails();
				data[index][2] = "" + bd[0];
				data[index][3] = "" + bd[1];
				index++;
			}
		}

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

	}
}
