import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import ij.IJ;

public class ExportActionBatchMode {

	private JTable table0;
	private JTable tableS0;
	private JTable table1;
	private JTable tableS1;
	private JTable table2;
	private JTable tableS2;
	private JTable tableFinal;
	static File directFolder;
	private String imageTitle, path1;


	public ExportActionBatchMode(JTable table0, JTable tableS0, JTable table1, JTable tableS1, JTable table2,
			JTable tableS2, JTable tableFinal, String imageTitle, String path1) {

		this.table0 = table0;
		this.tableS0 = tableS0;
		this.table1 = table1;
		this.tableS1 = tableS1;
		this.table2 = table2;
		this.tableS2 = tableS2;
		this.tableFinal = tableFinal;
		this.imageTitle = imageTitle;
		this.path1 = path1;

	}

	public void run(String arg0) {

	}

	public void exportExcelXlsActionPerformed(java.awt.event.ActionEvent evt) throws IOException {

		directFolder = new File(path1 + File.separator + imageTitle.replaceAll("\\.tif+$", "") + "_BatchModeFiles");

		if (!directFolder.exists()) {

			boolean result = false;

			try {
				directFolder.mkdir();
				result = true;
			} catch (SecurityException se) {
				// handle it
			}

		}
		// 0
		TableModel model0 = table0.getModel();
		TableModel modelS0 = tableS0.getModel();
		FileWriter out0 = new FileWriter(new File(directFolder.getAbsolutePath() + File.separator + "TableData_w1" + ".xls"));
		for (int i = 0; i < model0.getColumnCount(); i++) {
			out0.write(model0.getColumnName(i) + "\t");
		}
		out0.write("\n");

		for (int i = 0; i < model0.getRowCount(); i++) {
			for (int j = 0; j < model0.getColumnCount(); j++) {
				out0.write(model0.getValueAt(i, j).toString() + "\t");
			}
			out0.write("\n");
		}
		for (int i = 0; i < modelS0.getColumnCount(); i++) {
			out0.write(modelS0.getColumnName(i) + "\t");
		}
		out0.write("\n");

		for (int i = 0; i < modelS0.getRowCount(); i++) {
			for (int j = 0; j < modelS0.getColumnCount(); j++) {
				out0.write(modelS0.getValueAt(i, j).toString() + "\t");
			}
			out0.write("\n");
		}

		out0.close();
		

	}

	public void exportExcelFinalXlsActionPerformed(java.awt.event.ActionEvent evt) throws IOException {

		directFolder = new File(path1 + File.separator + imageTitle.replaceAll("\\.tif+$", "") + "_BatchModeFiles");

		if (!directFolder.exists()) {

			boolean result = false;

			try {
				directFolder.mkdir();
				result = true;
			} catch (SecurityException se) {
				// handle it
			}

		}
		// Final
		TableModel modelFinal = tableFinal.getModel();
		FileWriter outFinal = new FileWriter(
				new File(directFolder.getAbsolutePath() + File.separator + "TableData_FinalResults" + ".xls"));
		for (int i = 0; i < modelFinal.getColumnCount(); i++) {
			outFinal.write(modelFinal.getColumnName(i) + "\t");
		}
		outFinal.write("\n");

		for (int i = 0; i < modelFinal.getRowCount(); i++) {
			for (int j = 0; j < modelFinal.getColumnCount(); j++) {
				outFinal.write(modelFinal.getValueAt(i, j).toString() + "\t");
			}
			outFinal.write("\n");
		}
		outFinal.write("\n");
		outFinal.write("\n");
		outFinal.close();

	}

	public void exportExcelXlsxActionPerformed() {

		directFolder = new File(path1 + File.separator + imageTitle.replaceAll("\\.tif+$", "") + "_BatchModeFiles");

		if (!directFolder.exists()) {

			boolean result = false;

			try {
				directFolder.mkdir();
				result = true;
			} catch (SecurityException se) {
				// handle it
			}
		}

		/// 0

		HSSFWorkbook fWorkbook0 = new HSSFWorkbook();
		HSSFCell cell0 = null;
		HSSFSheet fSheet0 = fWorkbook0.createSheet("new Sheet");
		try {
			HSSFFont sheetTitleFont0 = fWorkbook0.createFont();
			HSSFCellStyle cellStyle0 = fWorkbook0.createCellStyle();
			sheetTitleFont0.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

			// Get Header
			TableColumnModel tcm0 = table0.getColumnModel();
			HSSFRow hRow0 = fSheet0.createRow((short) 0);
			for (int j = 0; j < tcm0.getColumnCount() - 1; j++) {
				cell0 = hRow0.createCell((short) j);
				cell0.setCellValue(tcm0.getColumn(j).getHeaderValue().toString());
				cell0.setCellStyle(cellStyle0);
			}

			// Get Other details
			for (int i = 0; i < table0.getModel().getRowCount(); i++) {
				HSSFRow fRow0 = fSheet0.createRow((short) i + 1);
				for (int j = 0; j < table0.getModel().getColumnCount() - 1; j++) {
					cell0 = fRow0.createCell((short) j);
					cell0.setCellValue(table0.getModel().getValueAt(i, j).toString());
					cell0.setCellStyle(cellStyle0);
				}
			}
			cell0.setCellValue("\n");
			cell0.setCellValue("\n");

			// Get Header
			TableColumnModel tcmS0 = tableS0.getColumnModel();
			fSheet0.createRow((short) 0);
			for (int j = 0; j < tcmS0.getColumnCount() - 1; j++) {
				hRow0.createCell((short) j);

				cell0.setCellValue(tcmS0.getColumn(j).getHeaderValue().toString());
				cell0.setCellStyle(cellStyle0);
			}

			// Get Other details
			for (int i = 0; i < tableS0.getModel().getRowCount(); i++) {
				fSheet0.createRow((short) i + 1);
				for (int j = 0; j < tableS0.getModel().getColumnCount() - 1; j++) {
					// cell0 = fRow0.createCell((short) j);
					cell0.setCellValue(tableS0.getModel().getValueAt(i, j).toString());
					cell0.setCellStyle(cellStyle0);
				}
			}

			FileOutputStream fileOutputStream0;
			fileOutputStream0 = new FileOutputStream(
					new File(directFolder.getAbsolutePath() + File.separator + "TableData_w1" + ".xlsx"));
			try (BufferedOutputStream bos0 = new BufferedOutputStream(fileOutputStream0)) {
				fWorkbook0.write(bos0);
			}
			fileOutputStream0.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
		/// P-21

		HSSFWorkbook fWorkbook1 = new HSSFWorkbook();
		HSSFCell cell1 = null;
		HSSFSheet fSheet1 = fWorkbook1.createSheet("new Sheet");
		try {
			HSSFFont sheetTitleFont1 = fWorkbook1.createFont();
			HSSFCellStyle cellStyle1 = fWorkbook1.createCellStyle();
			sheetTitleFont1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

			// Get Header
			TableColumnModel tcm1 = table1.getColumnModel();
			HSSFRow hRow1 = fSheet1.createRow((short) 0);
			for (int j = 0; j < tcm1.getColumnCount() - 1; j++) {
				cell1 = hRow1.createCell((short) j);
				cell1.setCellValue(tcm1.getColumn(j).getHeaderValue().toString());
				cell1.setCellStyle(cellStyle1);
			}

			// Get Other details
			for (int i = 0; i < table1.getModel().getRowCount(); i++) {
				HSSFRow fRow1 = fSheet1.createRow((short) i + 1);
				for (int j = 0; j < table1.getModel().getColumnCount() - 1; j++) {
					cell1 = fRow1.createCell((short) j);
					cell1.setCellValue(table1.getModel().getValueAt(i, j).toString());
					cell1.setCellStyle(cellStyle1);
				}
			}
			cell1.setCellValue("\n");
			cell1.setCellValue("\n");

			// Get Header
			TableColumnModel tcmS1 = tableS1.getColumnModel();
			fSheet1.createRow((short) 0);
			for (int j = 0; j < tcmS1.getColumnCount() - 1; j++) {
				hRow1.createCell((short) j);

				cell1.setCellValue(tcmS1.getColumn(j).getHeaderValue().toString());
				cell1.setCellStyle(cellStyle1);
			}

			// Get Other details
			for (int i = 0; i < tableS1.getModel().getRowCount(); i++) {
				fSheet1.createRow((short) i + 1);
				for (int j = 0; j < tableS1.getModel().getColumnCount() - 1; j++) {
					// cell0 = fRow0.createCell((short) j);
					cell1.setCellValue(tableS1.getModel().getValueAt(i, j).toString());
					cell1.setCellStyle(cellStyle1);
				}
			}

			FileOutputStream fileOutputStream1;
			fileOutputStream1 = new FileOutputStream(
					new File(directFolder.getAbsolutePath() + File.separator + "TableData_w2" + ".xlsx"));
			try (BufferedOutputStream bos0 = new BufferedOutputStream(fileOutputStream1)) {
				fWorkbook1.write(bos0);
			}
			fileOutputStream1.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
		/// B-Gal

		HSSFWorkbook fWorkbook2 = new HSSFWorkbook();
		HSSFCell cell2 = null;
		HSSFSheet fSheet2 = fWorkbook2.createSheet("new Sheet");
		try {
			HSSFFont sheetTitleFont2 = fWorkbook2.createFont();
			HSSFCellStyle cellStyle2 = fWorkbook2.createCellStyle();
			sheetTitleFont2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

			// Get Header
			TableColumnModel tcm2 = table2.getColumnModel();
			HSSFRow hRow2 = fSheet2.createRow((short) 0);
			for (int j = 0; j < tcm2.getColumnCount() - 1; j++) {
				cell2 = hRow2.createCell((short) j);
				cell2.setCellValue(tcm2.getColumn(j).getHeaderValue().toString());
				cell2.setCellStyle(cellStyle2);
			}

			// Get Other details
			for (int i = 0; i < table2.getModel().getRowCount(); i++) {
				HSSFRow fRow2 = fSheet2.createRow((short) i + 1);
				for (int j = 0; j < table2.getModel().getColumnCount() - 1; j++) {
					cell2 = fRow2.createCell((short) j);
					cell2.setCellValue(table1.getModel().getValueAt(i, j).toString());
					cell2.setCellStyle(cellStyle2);
				}
			}
			cell2.setCellValue("\n");
			cell2.setCellValue("\n");

			// Get Header
			TableColumnModel tcmS2 = tableS2.getColumnModel();
			fSheet2.createRow((short) 0);
			for (int j = 0; j < tcmS2.getColumnCount() - 1; j++) {
				hRow2.createCell((short) j);

				cell2.setCellValue(tcmS2.getColumn(j).getHeaderValue().toString());
				cell2.setCellStyle(cellStyle2);
			}

			// Get Other details
			for (int i = 0; i < tableS2.getModel().getRowCount(); i++) {
				fSheet2.createRow((short) i + 1);
				for (int j = 0; j < tableS2.getModel().getColumnCount() - 1; j++) {
					// cell0 = fRow0.createCell((short) j);
					cell2.setCellValue(tableS2.getModel().getValueAt(i, j).toString());
					cell2.setCellStyle(cellStyle2);
				}
			}

			FileOutputStream fileOutputStream2;
			fileOutputStream2 = new FileOutputStream(
					new File(directFolder.getAbsolutePath() + File.separator + "TableData_w3" + ".xlsx"));
			try (BufferedOutputStream bos2 = new BufferedOutputStream(fileOutputStream2)) {
				fWorkbook2.write(bos2);
			}
			fileOutputStream2.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}

	}

	public void exportExcelFinalXlsxActionPerformed() {

		directFolder = new File(path1 + File.separator + imageTitle.replaceAll("\\.tif+$", "") + "_BatchModeFiles");

		if (!directFolder.exists()) {

			boolean result = false;

			try {
				directFolder.mkdir();
				result = true;
			} catch (SecurityException se) {
				// handle it
			}
		}

		/// B-Final

		HSSFWorkbook fWorkbookFinal = new HSSFWorkbook();
		HSSFCell cellFinal = null;
		HSSFSheet fSheetFinal = fWorkbookFinal.createSheet("new Sheet");
		try {
			HSSFFont sheetTitleFontFinal = fWorkbookFinal.createFont();
			HSSFCellStyle cellStyleFinal = fWorkbookFinal.createCellStyle();
			sheetTitleFontFinal.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

			// Get Header
			TableColumnModel tcmFinal = tableFinal.getColumnModel();
			HSSFRow hRowFinal = fSheetFinal.createRow((short) 0);
			for (int j = 0; j < tcmFinal.getColumnCount() - 1; j++) {
				cellFinal = hRowFinal.createCell((short) j);
				cellFinal.setCellValue(tcmFinal.getColumn(j).getHeaderValue().toString());
				cellFinal.setCellStyle(cellStyleFinal);
			}

			// Get Other details
			for (int i = 0; i < tableFinal.getModel().getRowCount(); i++) {
				HSSFRow fRowFinal = fSheetFinal.createRow((short) i + 1);
				for (int j = 0; j < tableFinal.getModel().getColumnCount() - 1; j++) {
					cellFinal = fRowFinal.createCell((short) j);
					cellFinal.setCellValue(tableFinal.getModel().getValueAt(i, j).toString());
					cellFinal.setCellStyle(cellStyleFinal);
				}
			}
			cellFinal.setCellValue("\n");
			cellFinal.setCellValue("\n");
			FileOutputStream fileOutputStreamFinal;
			fileOutputStreamFinal = new FileOutputStream(
					new File(directFolder.getAbsolutePath() + File.separator + "TableData_Final_Results" + ".xlsx"));
			try (BufferedOutputStream bosFinal = new BufferedOutputStream(fileOutputStreamFinal)) {
				fWorkbookFinal.write(bosFinal);
			}
			fileOutputStreamFinal.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}

	}

	public boolean exportCsvActionPerformed(java.awt.event.ActionEvent evt) {
		directFolder = new File(path1 + File.separator + imageTitle.replaceAll("\\.tif+$", "") + "_BatchModeFiles");

		if (!directFolder.exists()) {

			boolean result = false;

			try {
				directFolder.mkdir();
				result = true;
			} catch (SecurityException se) {
				// handle it
			}

		}
//0
		try {

			FileWriter csv0 = new FileWriter(new File(directFolder.getAbsolutePath() + File.separator + "TableData_w1" + ".csv"));

			for (int i = 0; i < table0.getModel().getColumnCount(); i++) {
				csv0.write(table0.getModel().getColumnName(i) + ",");
			}

			csv0.write("\n");

			for (int i = 0; i < table0.getModel().getRowCount(); i++) {
				for (int j = 0; j < table0.getModel().getColumnCount(); j++) {
					csv0.write(table0.getModel().getValueAt(i, j).toString() + ",");
				}
				csv0.write("\n");
			}

			csv0.write("\n");
			csv0.write("\n");

			for (int i = 0; i < tableS0.getModel().getColumnCount(); i++) {
				csv0.write(tableS0.getModel().getColumnName(i) + ",");
			}

			csv0.write("\n");

			for (int i = 0; i < tableS0.getModel().getRowCount(); i++) {
				for (int j = 0; j < tableS0.getModel().getColumnCount(); j++) {
					csv0.write(tableS0.getModel().getValueAt(i, j).toString() + ",");
				}
				csv0.write("\n");
			}

			csv0.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 1
		try {

			FileWriter csv1 = new FileWriter(new File(directFolder.getAbsolutePath() + File.separator + "TableData_w2" + ".csv"));

			for (int i = 0; i < table1.getModel().getColumnCount(); i++) {
				csv1.write(table1.getModel().getColumnName(i) + ",");
			}

			csv1.write("\n");

			for (int i = 0; i < table1.getModel().getRowCount(); i++) {
				for (int j = 0; j < table1.getModel().getColumnCount(); j++) {
					csv1.write(table1.getModel().getValueAt(i, j).toString() + ",");
				}
				csv1.write("\n");
			}

			csv1.write("\n");
			csv1.write("\n");

			for (int i = 0; i < tableS1.getModel().getColumnCount(); i++) {
				csv1.write(tableS1.getModel().getColumnName(i) + ",");
			}

			csv1.write("\n");

			for (int i = 0; i < tableS1.getModel().getRowCount(); i++) {
				for (int j = 0; j < tableS1.getModel().getColumnCount(); j++) {
					csv1.write(tableS1.getModel().getValueAt(i, j).toString() + ",");
				}
				csv1.write("\n");
			}

			csv1.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 2
		try {

			FileWriter csv2 = new FileWriter(new File(directFolder.getAbsolutePath() + File.separator + "TableData_w3" + ".csv"));

			for (int i = 0; i < table2.getModel().getColumnCount(); i++) {
				csv2.write(table2.getModel().getColumnName(i) + ",");
			}

			csv2.write("\n");

			for (int i = 0; i < table2.getModel().getRowCount(); i++) {
				for (int j = 0; j < table2.getModel().getColumnCount(); j++) {
					csv2.write(table2.getModel().getValueAt(i, j).toString() + ",");
				}
				csv2.write("\n");
			}

			csv2.write("\n");
			csv2.write("\n");

			for (int i = 0; i < tableS2.getModel().getColumnCount(); i++) {
				csv2.write(tableS2.getModel().getColumnName(i) + ",");
			}

			csv2.write("\n");

			for (int i = 0; i < tableS2.getModel().getRowCount(); i++) {
				for (int j = 0; j < tableS2.getModel().getColumnCount(); j++) {
					csv2.write(tableS2.getModel().getValueAt(i, j).toString() + ",");
				}
				csv2.write("\n");
			}

			csv2.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;

	}

	public boolean exportFinalCsvActionPerformed(java.awt.event.ActionEvent evt) {

		// Final
		try {

			FileWriter csvFinal = new FileWriter(
					new File(directFolder.getAbsolutePath() + File.separator + "TableData_Final_Results" + ".csv"));

			for (int i = 0; i < tableFinal.getModel().getColumnCount(); i++) {
				csvFinal.write(tableFinal.getModel().getColumnName(i) + ",");
			}

			csvFinal.write("\n");

			for (int i = 0; i < tableFinal.getModel().getRowCount(); i++) {
				for (int j = 0; j < tableFinal.getModel().getColumnCount(); j++) {
					csvFinal.write(tableFinal.getModel().getValueAt(i, j).toString() + ",");
				}
				csvFinal.write("\n");
			}

			csvFinal.write("\n");
			csvFinal.write("\n");
			csvFinal.close();
			return true;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;

	}

}
