import java.awt.Color;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ResultRendererC extends DefaultTableCellRenderer {

		/*
		 * @see TableCellRenderer#getTableCellRendererComponent(JTable, Object, boolean,
		 * boolean, int, int)
		 */
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			Component comp = null;

			if (value instanceof JLabel)
				comp = (JLabel) value;
			if (value instanceof JTable)
				comp = (JTable) value;
			if (value instanceof JScrollPane)
				comp = (JScrollPane) value;
			if (value instanceof JButton)
				comp = (JButton) value;
			if (value instanceof JScrollPane)
				comp = (JScrollPane) value;

			if (isSelected) {
				comp.setForeground(Color.BLUE);// table.getSelectionForeground());
				// comp.setBackground(currentColor);//table.getSelectionBackground());
			} else {
				comp.setForeground(Color.BLACK);

			}

			return comp;
		}
	}