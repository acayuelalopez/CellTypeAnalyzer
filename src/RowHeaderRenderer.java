import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

class RowHeaderRenderer extends DefaultTableCellRenderer {
	 public RowHeaderRenderer() {
	        setHorizontalAlignment(JLabel.CENTER);
	    }

	    public Component getTableCellRendererComponent(JTable table,
	            Object value, boolean isSelected, boolean hasFocus, int row,
	            int column) {
	        if (table != null) {
	            JTableHeader header = table.getTableHeader();

	            if (row == 0 && column == 0 && (isSelected == true || isSelected == false)) {
	                setFont(getFont().deriveFont(Font.BOLD));
	            }
	            if (row == 1 && column == 0 && (isSelected == true || isSelected == false)) {
	                setFont(getFont().deriveFont(Font.BOLD));
	            }
	        }

	      

	        setValue(value);
	        return this;
	    }
	}




