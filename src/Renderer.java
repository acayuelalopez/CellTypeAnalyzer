import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class Renderer extends DefaultTableCellRenderer {

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {

			if (value instanceof JLabel) {
				// This time return only the JLabel without icon
				return (JLabel) value;
			}
			
		
			else
				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		}
	}