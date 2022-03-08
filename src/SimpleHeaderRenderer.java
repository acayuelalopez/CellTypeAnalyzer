import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

public class SimpleHeaderRenderer extends JLabel implements TableCellRenderer {

		public SimpleHeaderRenderer() {
			setFont(new Font("SansSerif", Font.BOLD, 12));
			setForeground(Color.DARK_GRAY);
			setBorder(BorderFactory.createEtchedBorder());
			setBackground(new Color(230, 250, 240, 50));
			setHorizontalAlignment(SwingConstants.CENTER);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			setText(value.toString());
			return this;
		}

	}