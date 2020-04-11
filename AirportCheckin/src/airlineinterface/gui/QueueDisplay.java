package airlineinterface.gui;

import java.awt.GridBagLayout;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import airlineinterface.Customer;
import airlineinterface.WaitingQueue;

public class QueueDisplay extends JPanel {
	private static final long serialVersionUID = -6114396293239580467L;

	private WaitingQueue q;
	private JTable table;

	public QueueDisplay(WaitingQueue q) {
		super(new GridBagLayout());
		this.q = q;
		buildDisplay();
	}

	public void buildDisplay() {
		if (table != null)
			remove(table);
		Queue<Customer> copy = new LinkedList<Customer>(q.getWaiting());
		table = new JTable(new QueueTableModel(copy));
		JScrollPane pane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		pane.setSize(100, 40);
		add(pane);
	}

	private class QueueTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 491620524778167217L;
		
		private String[] columnNames = { "Name", "Code", "Baggage (kg)", "Baggage (l)" };
		private String[][] data;

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
			data = new String[customers.size()][columnNames.length];
			int index = 0;
			for (Customer c : customers) {
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
