import java.awt.print.PrinterException;
import java.text.MessageFormat;

import javax.swing.JOptionPane;
import javax.swing.JTable;

public class PrintTable {
	private JTable jTable;
	private String header;
	private String footer;
	private boolean showPrintDialog;

	public PrintTable(JTable jTable, String header, String footer, boolean showPrintDialog) {
		this.jTable = jTable;
		this.header = header;
		this.footer = footer;
		this.showPrintDialog = showPrintDialog;

	}

	public void utilJTablePrint() {
		
		boolean fitWidth = true;
		boolean interactive = true;
		// We define the print mode (Definimos el modo de impresión)
		JTable.PrintMode mode = fitWidth ? JTable.PrintMode.FIT_WIDTH : JTable.PrintMode.NORMAL;
		try {
			// Print the table (Imprimo la tabla)
			boolean complete = jTable.print(mode, new MessageFormat(header), new MessageFormat(footer), showPrintDialog,
					null, interactive);
			if (complete) {
				// Mostramos el mensaje de impresión existosa
				JOptionPane.showMessageDialog(jTable, "Print complete", "Print result",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				// Mostramos un mensaje indicando que la impresión fue cancelada
				JOptionPane.showMessageDialog(jTable, "Print canceled", "Print result", JOptionPane.WARNING_MESSAGE);
			}
		} catch (PrinterException pe) {
			JOptionPane.showMessageDialog(jTable, "Print fail: " + pe.getMessage(), "Print result",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
