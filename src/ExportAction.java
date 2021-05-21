import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import ij.IJ;

public class ExportAction {
	private JTable tableDapi;
	private JTable tableSDapi;
	private JTable tableP21;
	private JTable tableSP21;
	private JTable tableBgal;
	private JTable tableSBgal;
	private JTable tablePositive;
	private JTable tableChannel;
	private JTable tableQuadrant;
	private JTable tableIdentity;

	public ExportAction(JTable tableDapi, JTable tableSDapi, JTable tableP21, JTable tableSP21, JTable tableBgal,
			JTable tableSBgal, JTable tablePositive, JTable tableChannel, JTable tableQuadrant, JTable tableIdentity) {
		this.tableDapi = tableDapi;
		this.tableSDapi = tableSDapi;
		this.tableP21 = tableP21;
		this.tableSP21 = tableSP21;
		this.tableBgal = tableBgal;
		this.tableSBgal = tableSBgal;
		this.tablePositive = tablePositive;
		this.tableChannel = tableChannel;
		this.tableQuadrant = tableQuadrant;
		this.tableIdentity = tableIdentity;

	}

	public void createAndShowExportGUI() {

		// Create and set up the window.
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// If Nimbus is not available, you can set the GUI to another look and feel.
		}
		JFrame frame = new JFrame("File Exporter");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel panelOptions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JPanel panelBox = new JPanel();
		panelBox.setLayout(new BoxLayout(panelBox, BoxLayout.Y_AXIS));
		JRadioButton csvFileB = new JRadioButton(".CSV file", true);
		JRadioButton excelFileB = new JRadioButton("EXCEL file");
		JComboBox<String> comboExt = new JComboBox<String>();
		comboExt.addItem(".xls");
		comboExt.addItem(".xlsx");
		ButtonGroup bgroup = new ButtonGroup();
		bgroup.add(csvFileB);
		bgroup.add(excelFileB);
		panelOptions.setBorder(
				BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), "Check to export:  "));
		JLabel labelFormat = new JLabel("  ➟ File Format : ");
		labelFormat.setBorder(BorderFactory.createRaisedBevelBorder());
		panelBox.add(labelFormat);
		panelBox.add(excelFileB);
		panelBox.add(csvFileB);
		panelOptions.add(panelBox);
		panelOptions.add(comboExt);
		comboExt.setEnabled(false);
		JPanel mainPanel1 = new JPanel(new FlowLayout());
		mainPanel1.add(panelOptions);

		JButton okButton = new JButton("");
		ImageIcon iconOk = JFrameWizard.createImageIcon("images/ok.png");
		Icon okCell = new ImageIcon(iconOk.getImage().getScaledInstance(26, 28, Image.SCALE_SMOOTH));
		okButton.setIcon(okCell);
		okButton.setToolTipText("Click this button to export files.");

		JButton cancelButton = new JButton("");
		ImageIcon iconCancel = JFrameWizard.createImageIcon("images/cancel.png");
		Icon cancelCell = new ImageIcon(iconCancel.getImage().getScaledInstance(26, 28, Image.SCALE_SMOOTH));
		cancelButton.setIcon(cancelCell);
		cancelButton.setToolTipText("Click this button to cancel this process.");

		JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panelButtons.add(okButton);
		panelButtons.add(cancelButton);

		JPanel defPanel = new JPanel();
		defPanel.setLayout(new BoxLayout(defPanel, BoxLayout.Y_AXIS));
		defPanel.add(mainPanel1);
		defPanel.add(panelButtons);

		frame.setSize(200, 50);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().add(defPanel);
		frame.pack();
		frame.setVisible(true);

		excelFileB.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {

				if (e.getStateChange() == ItemEvent.SELECTED) {

					comboExt.setEnabled(true);
				}
				if (e.getStateChange() == ItemEvent.DESELECTED) {

					comboExt.setEnabled(false);

				}

			}
		});

		okButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
				String newMode = (String) comboExt.getSelectedItem();
				if (excelFileB.isSelected() == true && csvFileB.isSelected() == false
						&& newMode.equals(".xlsx") == true) {

					try {
						exportExcelXlsxActionPerformed(evt);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (excelFileB.isSelected() == true && csvFileB.isSelected() == false
						&& newMode.equals(".xls") == true) {

					try {
						exportExcelXlsActionPerformed(evt);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (csvFileB.isSelected() == true && excelFileB.isSelected() == false)
					exportCsvActionPerformed(evt);
			}
		});

		cancelButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		});

	}

	private void exportExcelXlsActionPerformed(java.awt.event.ActionEvent evt) throws IOException {

		JFileChooser save = new JFileChooser();
		java.util.Locale.setDefault(java.util.Locale.ENGLISH);
		save.setLocale(java.util.Locale.ENGLISH);
		save.setLocale(java.util.Locale.getDefault());
		save.updateUI();
		save.setDialogTitle("Choose a directory to export result files...");
		save.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		save.setFileFilter(new FileNameExtensionFilter("xls", "xlsx", "xlsm"));
		int choose = save.showSaveDialog(null);

		if (choose == JFileChooser.APPROVE_OPTION) {

			String path1 = save.getSelectedFile().getAbsolutePath();
			File directFolder = new File(path1 + File.separator + IJ.getImage().getTitle().replaceAll("\\.tif+$", "") + "_files");

			if (!directFolder.exists()) {
				IJ.log("creating directory: " + directFolder.getName());
				boolean result = false;

				try {
					directFolder.mkdir();
					result = true;
				} catch (SecurityException se) {
					// handle it
				}

				save.setCurrentDirectory(save.getSelectedFile());

			}

			// Dapi
			TableModel modelDapi = tableDapi.getModel();
			TableModel modelSDapi = tableSDapi.getModel();
			FileWriter outDapi = new FileWriter(
					new File(directFolder.getAbsolutePath() + File.separator + "TableData_w1" + ".xls"));
			for (int i = 0; i < modelDapi.getColumnCount(); i++) {
				outDapi.write(modelDapi.getColumnName(i) + "\t");
			}
			outDapi.write("\n");

			for (int i = 0; i < modelDapi.getRowCount(); i++) {
				for (int j = 0; j < modelDapi.getColumnCount(); j++) {
					outDapi.write(modelDapi.getValueAt(i, j).toString() + "\t");
				}
				outDapi.write("\n");
			}
			for (int i = 0; i < modelSDapi.getColumnCount(); i++) {
				outDapi.write(modelSDapi.getColumnName(i) + "\t");
			}
			outDapi.write("\n");

			for (int i = 0; i < modelSDapi.getRowCount(); i++) {
				for (int j = 0; j < modelSDapi.getColumnCount(); j++) {
					outDapi.write(modelSDapi.getValueAt(i, j).toString() + "\t");
				}
				outDapi.write("\n");
			}

			outDapi.close();
			// p21
			TableModel modelP21 = tableP21.getModel();
			TableModel modelSP21 = tableSP21.getModel();
			FileWriter outP21 = new FileWriter(
					new File(directFolder.getAbsolutePath() + File.separator + "TableData_w2" + ".xls"));
			for (int i = 0; i < modelP21.getColumnCount(); i++) {
				outP21.write(modelP21.getColumnName(i) + "\t");
			}
			outP21.write("\n");

			for (int i = 0; i < modelP21.getRowCount(); i++) {
				for (int j = 0; j < modelP21.getColumnCount(); j++) {
					outP21.write(modelP21.getValueAt(i, j).toString() + "\t");
				}
				outP21.write("\n");
			}
			for (int i = 0; i < modelSP21.getColumnCount(); i++) {
				outP21.write(modelSP21.getColumnName(i) + "\t");
			}
			outP21.write("\n");

			for (int i = 0; i < modelSP21.getRowCount(); i++) {
				for (int j = 0; j < modelSP21.getColumnCount(); j++) {
					outP21.write(modelSP21.getValueAt(i, j).toString() + "\t");
				}
				outP21.write("\n");
			}

			outP21.close();

			// Bgal
			TableModel modelBgal = tableBgal.getModel();
			TableModel modelSBgal = tableSBgal.getModel();
			FileWriter outBgal = new FileWriter(
					new File(directFolder.getAbsolutePath() + File.separator + "TableData_w3" + ".xls"));
			for (int i = 0; i < modelBgal.getColumnCount(); i++) {
				outBgal.write(modelBgal.getColumnName(i) + "\t");
			}
			outBgal.write("\n");

			for (int i = 0; i < modelBgal.getRowCount(); i++) {
				for (int j = 0; j < modelBgal.getColumnCount(); j++) {
					outBgal.write(modelBgal.getValueAt(i, j).toString() + "\t");
				}
				outBgal.write("\n");
			}
			for (int i = 0; i < modelBgal.getColumnCount(); i++) {
				outBgal.write(modelSBgal.getColumnName(i) + "\t");
			}
			outBgal.write("\n");

			for (int i = 0; i < modelSBgal.getRowCount(); i++) {
				for (int j = 0; j < modelSBgal.getColumnCount(); j++) {
					outBgal.write(modelSBgal.getValueAt(i, j).toString() + "\t");
				}
				outBgal.write("\n");
			}

			outBgal.close();

			// Scatter
			TableModel modelPositive = tablePositive.getModel();
			TableModel modelChannel = tableChannel.getModel();
			TableModel modelQuadrant = tableQuadrant.getModel();
			TableModel modelIdentity = tableIdentity.getModel();
			FileWriter outScatter = new FileWriter(
					new File(directFolder.getAbsolutePath() + File.separator + "TableData_FinalResults" + ".xls"));
			for (int i = 0; i < modelPositive.getColumnCount(); i++) {
				outScatter.write(modelPositive.getColumnName(i) + "\t");
			}
			outScatter.write("\n");

			for (int i = 0; i < modelPositive.getRowCount(); i++) {
				for (int j = 0; j < modelPositive.getColumnCount(); j++) {
					outScatter.write(modelPositive.getValueAt(i, j).toString() + "\t");
				}
				outScatter.write("\n");
			}
			outScatter.write("\n");
			outScatter.write("\n");

			for (int i = 0; i < modelChannel.getColumnCount(); i++) {
				outScatter.write(modelChannel.getColumnName(i) + "\t");
			}
			outScatter.write("\n");

			for (int i = 0; i < modelChannel.getRowCount(); i++) {
				for (int j = 0; j < modelChannel.getColumnCount(); j++) {
					outScatter.write(modelChannel.getValueAt(i, j).toString() + "\t");
				}
				outScatter.write("\n");
			}
			outScatter.write("\n");
			outScatter.write("\n");

			for (int i = 0; i < modelQuadrant.getColumnCount(); i++) {
				outScatter.write(modelQuadrant.getColumnName(i) + "\t");
			}
			outScatter.write("\n");

			for (int i = 0; i < modelQuadrant.getRowCount(); i++) {
				for (int j = 0; j < modelQuadrant.getColumnCount(); j++) {
					outScatter.write(modelQuadrant.getValueAt(i, j).toString() + "\t");
				}
				outScatter.write("\n");
			}
			outScatter.write("\n");
			outScatter.write("\n");

			for (int i = 0; i < modelIdentity.getColumnCount(); i++) {
				outScatter.write(modelIdentity.getColumnName(i) + "\t");
			}
			outScatter.write("\n");

			for (int i = 0; i < modelIdentity.getRowCount(); i++) {
				for (int j = 0; j < modelIdentity.getColumnCount(); j++) {
					outScatter.write(modelIdentity.getValueAt(i, j).toString() + "\t");
				}
				outScatter.write("\n");
			}
			outScatter.write("\n");
			outScatter.write("\n");

			outScatter.close();

			JOptionPane.showMessageDialog(null, "Exported Succesfully in " + directFolder.getAbsolutePath() + ".");
		}
	}

	private void exportExcelXlsxActionPerformed(java.awt.event.ActionEvent evt) throws IOException {
		JFileChooser save = new JFileChooser();
		java.util.Locale.setDefault(java.util.Locale.ENGLISH);
		save.setLocale(java.util.Locale.ENGLISH);
		save.setLocale(java.util.Locale.getDefault());
		save.updateUI();
		save.setDialogTitle("Choose a directory to export result files...");
		save.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		save.setFileFilter(new FileNameExtensionFilter("xls", "xlsx", "xlsm"));
		int choose = save.showSaveDialog(null);

		if (choose == JFileChooser.APPROVE_OPTION) {

			String path1 = save.getSelectedFile().getAbsolutePath();
			File directFolder = new File(path1 + File.separator + IJ.getImage().getTitle().replaceAll("\\.tif+$", "") + "_files");

			if (!directFolder.exists()) {
				IJ.log("creating directory: " + directFolder.getName());
				boolean result = false;

				try {
					directFolder.mkdir();
					result = true;
				} catch (SecurityException se) {
					// handle it
				}

				save.setCurrentDirectory(save.getSelectedFile());

			}

			/// Dapi

			HSSFWorkbook fWorkbookDapi = new HSSFWorkbook();
			HSSFCell cellDapi = null;
			HSSFSheet fSheetDapi = fWorkbookDapi.createSheet("new Sheet");
			try {
				HSSFFont sheetTitleFontDapi = fWorkbookDapi.createFont();
				HSSFCellStyle cellStyleDapi = fWorkbookDapi.createCellStyle();
				sheetTitleFontDapi.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

				// Get Header
				TableColumnModel tcmDapi = tableDapi.getColumnModel();
				HSSFRow hRowDapi = fSheetDapi.createRow((short) 0);
				for (int j = 0; j < tcmDapi.getColumnCount() - 1; j++) {
					cellDapi = hRowDapi.createCell((short) j);
					cellDapi.setCellValue(tcmDapi.getColumn(j).getHeaderValue().toString());
					cellDapi.setCellStyle(cellStyleDapi);
				}

				// Get Other details
				for (int i = 0; i < tableDapi.getModel().getRowCount(); i++) {
					HSSFRow fRowDapi = fSheetDapi.createRow((short) i + 1);
					for (int j = 0; j < tableDapi.getModel().getColumnCount() - 1; j++) {
						cellDapi = fRowDapi.createCell((short) j);
						cellDapi.setCellValue(tableDapi.getModel().getValueAt(i, j).toString());
						cellDapi.setCellStyle(cellStyleDapi);
					}
				}
				cellDapi.setCellValue("\n");
				cellDapi.setCellValue("\n");

				// Get Header
				TableColumnModel tcmSDapi = tableSDapi.getColumnModel();
				fSheetDapi.createRow((short) 0);
				for (int j = 0; j < tcmSDapi.getColumnCount() - 1; j++) {
					hRowDapi.createCell((short) j);

					cellDapi.setCellValue(tcmSDapi.getColumn(j).getHeaderValue().toString());
					cellDapi.setCellStyle(cellStyleDapi);
				}

				// Get Other details
				for (int i = 0; i < tableSDapi.getModel().getRowCount(); i++) {
					fSheetDapi.createRow((short) i + 1);
					for (int j = 0; j < tableSDapi.getModel().getColumnCount() - 1; j++) {
						// cellDapi = fRowDapi.createCell((short) j);
						cellDapi.setCellValue(tableSDapi.getModel().getValueAt(i, j).toString());
						cellDapi.setCellStyle(cellStyleDapi);
					}
				}

				FileOutputStream fileOutputStreamDapi;
				fileOutputStreamDapi = new FileOutputStream(
						new File(directFolder.getAbsolutePath() + File.separator + "TableData_w1" + ".xlsx"));
				try (BufferedOutputStream bosDapi = new BufferedOutputStream(fileOutputStreamDapi)) {
					fWorkbookDapi.write(bosDapi);
				}
				fileOutputStreamDapi.close();
				JOptionPane.showMessageDialog(null, "Exported succesfully in " + directFolder.getAbsolutePath());
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e);
			}
			/// P-21

			HSSFWorkbook fWorkbookP21 = new HSSFWorkbook();
			HSSFCell cellP21 = null;
			HSSFSheet fSheetP21 = fWorkbookP21.createSheet("new Sheet");
			try {
				HSSFFont sheetTitleFontP21 = fWorkbookP21.createFont();
				HSSFCellStyle cellStyleP21 = fWorkbookP21.createCellStyle();
				sheetTitleFontP21.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

				// Get Header
				TableColumnModel tcmP21 = tableP21.getColumnModel();
				HSSFRow hRowP21 = fSheetP21.createRow((short) 0);
				for (int j = 0; j < tcmP21.getColumnCount() - 1; j++) {
					cellP21 = hRowP21.createCell((short) j);
					cellP21.setCellValue(tcmP21.getColumn(j).getHeaderValue().toString());
					cellP21.setCellStyle(cellStyleP21);
				}

				// Get Other details
				for (int i = 0; i < tableP21.getModel().getRowCount(); i++) {
					HSSFRow fRowP21 = fSheetP21.createRow((short) i + 1);
					for (int j = 0; j < tableP21.getModel().getColumnCount() - 1; j++) {
						cellP21 = fRowP21.createCell((short) j);
						cellP21.setCellValue(tableP21.getModel().getValueAt(i, j).toString());
						cellP21.setCellStyle(cellStyleP21);
					}
				}
				cellP21.setCellValue("\n");
				cellP21.setCellValue("\n");

				// Get Header
				TableColumnModel tcmSP21 = tableSP21.getColumnModel();
				fSheetP21.createRow((short) 0);
				for (int j = 0; j < tcmSP21.getColumnCount() - 1; j++) {
					hRowP21.createCell((short) j);

					cellP21.setCellValue(tcmSP21.getColumn(j).getHeaderValue().toString());
					cellP21.setCellStyle(cellStyleP21);
				}

				// Get Other details
				for (int i = 0; i < tableSP21.getModel().getRowCount(); i++) {
					fSheetP21.createRow((short) i + 1);
					for (int j = 0; j < tableSP21.getModel().getColumnCount() - 1; j++) {
						// cellDapi = fRowDapi.createCell((short) j);
						cellP21.setCellValue(tableSP21.getModel().getValueAt(i, j).toString());
						cellP21.setCellStyle(cellStyleP21);
					}
				}

				FileOutputStream fileOutputStreamP21;
				fileOutputStreamP21 = new FileOutputStream(
						new File(directFolder.getAbsolutePath() + File.separator + "TableData_w2" + ".xlsx"));
				try (BufferedOutputStream bosDapi = new BufferedOutputStream(fileOutputStreamP21)) {
					fWorkbookP21.write(bosDapi);
				}
				fileOutputStreamP21.close();
				JOptionPane.showMessageDialog(null, "Exported succesfully in " + directFolder.getAbsolutePath());
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e);
			}
			/// B-Gal

			HSSFWorkbook fWorkbookBGal = new HSSFWorkbook();
			HSSFCell cellBGal = null;
			HSSFSheet fSheetBGal = fWorkbookBGal.createSheet("new Sheet");
			try {
				HSSFFont sheetTitleFontBGal = fWorkbookBGal.createFont();
				HSSFCellStyle cellStyleBGal = fWorkbookBGal.createCellStyle();
				sheetTitleFontBGal.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

				// Get Header
				TableColumnModel tcmBGal = tableBgal.getColumnModel();
				HSSFRow hRowBGal = fSheetBGal.createRow((short) 0);
				for (int j = 0; j < tcmBGal.getColumnCount() - 1; j++) {
					cellBGal = hRowBGal.createCell((short) j);
					cellBGal.setCellValue(tcmBGal.getColumn(j).getHeaderValue().toString());
					cellBGal.setCellStyle(cellStyleBGal);
				}

				// Get Other details
				for (int i = 0; i < tableBgal.getModel().getRowCount(); i++) {
					HSSFRow fRowBGal = fSheetBGal.createRow((short) i + 1);
					for (int j = 0; j < tableBgal.getModel().getColumnCount() - 1; j++) {
						cellBGal = fRowBGal.createCell((short) j);
						cellBGal.setCellValue(tableP21.getModel().getValueAt(i, j).toString());
						cellBGal.setCellStyle(cellStyleBGal);
					}
				}
				cellBGal.setCellValue("\n");
				cellBGal.setCellValue("\n");

				// Get Header
				TableColumnModel tcmSBGal = tableSBgal.getColumnModel();
				fSheetBGal.createRow((short) 0);
				for (int j = 0; j < tcmSBGal.getColumnCount() - 1; j++) {
					hRowBGal.createCell((short) j);

					cellBGal.setCellValue(tcmSBGal.getColumn(j).getHeaderValue().toString());
					cellBGal.setCellStyle(cellStyleBGal);
				}

				// Get Other details
				for (int i = 0; i < tableSBgal.getModel().getRowCount(); i++) {
					fSheetBGal.createRow((short) i + 1);
					for (int j = 0; j < tableSBgal.getModel().getColumnCount() - 1; j++) {
						// cellDapi = fRowDapi.createCell((short) j);
						cellBGal.setCellValue(tableSBgal.getModel().getValueAt(i, j).toString());
						cellBGal.setCellStyle(cellStyleBGal);
					}
				}

				FileOutputStream fileOutputStreamBGal;
				fileOutputStreamBGal = new FileOutputStream(
						new File(directFolder.getAbsolutePath() + File.separator + "TableData_w3" + ".xlsx"));
				try (BufferedOutputStream bosBGal = new BufferedOutputStream(fileOutputStreamBGal)) {
					fWorkbookBGal.write(bosBGal);
				}
				fileOutputStreamBGal.close();
				JOptionPane.showMessageDialog(null, "Exported succesfully in " + directFolder.getAbsolutePath());
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e);
			}
			/// B-Scatter

			HSSFWorkbook fWorkbookScatter = new HSSFWorkbook();
			HSSFCell cellScatter = null;
			HSSFSheet fSheetScatter = fWorkbookScatter.createSheet("new Sheet");
			try {
				HSSFFont sheetTitleFontScatter = fWorkbookScatter.createFont();
				HSSFCellStyle cellStyleScatter = fWorkbookScatter.createCellStyle();
				sheetTitleFontScatter.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

				// Get Header
				TableColumnModel tcmPositive = tablePositive.getColumnModel();
				HSSFRow hRowPositive = fSheetScatter.createRow((short) 0);
				for (int j = 0; j < tcmPositive.getColumnCount() - 1; j++) {
					cellScatter = hRowPositive.createCell((short) j);
					cellScatter.setCellValue(tcmPositive.getColumn(j).getHeaderValue().toString());
					cellScatter.setCellStyle(cellStyleScatter);
				}

				// Get Other details
				for (int i = 0; i < tablePositive.getModel().getRowCount(); i++) {
					HSSFRow fRowPositive = fSheetScatter.createRow((short) i + 1);
					for (int j = 0; j < tablePositive.getModel().getColumnCount() - 1; j++) {
						cellScatter = fRowPositive.createCell((short) j);
						cellScatter.setCellValue(tablePositive.getModel().getValueAt(i, j).toString());
						cellScatter.setCellStyle(cellStyleScatter);
					}
				}
				cellScatter.setCellValue("\n");
				cellScatter.setCellValue("\n");

				// Get Header
				TableColumnModel tcmChannel = tableChannel.getColumnModel();
				HSSFRow hRowChannel = fSheetScatter.createRow((short) 0);
				for (int j = 0; j < tcmChannel.getColumnCount() - 1; j++) {
					cellScatter = hRowChannel.createCell((short) j);
					cellScatter.setCellValue(tcmChannel.getColumn(j).getHeaderValue().toString());
					cellScatter.setCellStyle(cellStyleScatter);
				}

				// Get Other details
				for (int i = 0; i < tableChannel.getModel().getRowCount(); i++) {
					HSSFRow fRowChannel = fSheetScatter.createRow((short) i + 1);
					for (int j = 0; j < tablePositive.getModel().getColumnCount() - 1; j++) {
						cellScatter = fRowChannel.createCell((short) j);
						cellScatter.setCellValue(tableChannel.getModel().getValueAt(i, j).toString());
						cellScatter.setCellStyle(cellStyleScatter);
					}
				}
				cellScatter.setCellValue("\n");
				cellScatter.setCellValue("\n");

				// Get Header
				TableColumnModel tcmQuadrant = tableQuadrant.getColumnModel();
				HSSFRow hRowQuadrant = fSheetScatter.createRow((short) 0);
				for (int j = 0; j < tcmQuadrant.getColumnCount() - 1; j++) {
					cellScatter = hRowQuadrant.createCell((short) j);
					cellScatter.setCellValue(tcmQuadrant.getColumn(j).getHeaderValue().toString());
					cellScatter.setCellStyle(cellStyleScatter);
				}

				// Get Other details
				for (int i = 0; i < tableQuadrant.getModel().getRowCount(); i++) {
					HSSFRow fRowQuadrant = fSheetScatter.createRow((short) i + 1);
					for (int j = 0; j < tableQuadrant.getModel().getColumnCount() - 1; j++) {
						cellScatter = fRowQuadrant.createCell((short) j);
						cellScatter.setCellValue(tableQuadrant.getModel().getValueAt(i, j).toString());
						cellScatter.setCellStyle(cellStyleScatter);
					}
				}
				cellScatter.setCellValue("\n");
				cellScatter.setCellValue("\n");

				// Get Header
				TableColumnModel tcmIdentity = tableQuadrant.getColumnModel();
				HSSFRow hRowIdentity = fSheetScatter.createRow((short) 0);
				for (int j = 0; j < tcmIdentity.getColumnCount() - 1; j++) {
					cellScatter = hRowIdentity.createCell((short) j);
					cellScatter.setCellValue(tcmIdentity.getColumn(j).getHeaderValue().toString());
					cellScatter.setCellStyle(cellStyleScatter);
				}

				// Get Other details
				for (int i = 0; i < tableIdentity.getModel().getRowCount(); i++) {
					HSSFRow fRowIdentity = fSheetScatter.createRow((short) i + 1);
					for (int j = 0; j < tableIdentity.getModel().getColumnCount() - 1; j++) {
						cellScatter = fRowIdentity.createCell((short) j);
						cellScatter.setCellValue(tableIdentity.getModel().getValueAt(i, j).toString());
						cellScatter.setCellStyle(cellStyleScatter);
					}
				}
				cellScatter.setCellValue("\n");
				cellScatter.setCellValue("\n");

				FileOutputStream fileOutputStreamScatter;
				fileOutputStreamScatter = new FileOutputStream(
						new File(directFolder.getAbsolutePath() + File.separator + "TableData_Final_Results" + ".xlsx"));
				try (BufferedOutputStream bosScatter = new BufferedOutputStream(fileOutputStreamScatter)) {
					fWorkbookScatter.write(bosScatter);
				}
				fileOutputStreamScatter.close();
				JOptionPane.showMessageDialog(null, "Exported succesfully in " + directFolder.getAbsolutePath());
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e);
			}
		}

	}

	private boolean exportCsvActionPerformed(java.awt.event.ActionEvent evt) {
		JFileChooser save = new JFileChooser();
		save.setDialogTitle("Save as...");
		save.setFileFilter(new FileNameExtensionFilter("xls", "xlsx", "xlsm", "csv"));
		int choose = save.showSaveDialog(null);

		if (choose == JFileChooser.APPROVE_OPTION) {

			String path1 = save.getSelectedFile().getAbsolutePath();
			File directFolder = new File(path1 + File.separator + IJ.getImage().getTitle().replaceAll("\\.tif+$", "") + "_files");

			if (!directFolder.exists()) {
				IJ.log("creating directory: " + directFolder.getName());
				boolean result = false;

				try {
					directFolder.mkdir();
					result = true;
				} catch (SecurityException se) {
					// handle it
				}

				save.setCurrentDirectory(save.getSelectedFile());

			}
//Dapi
			try {

				FileWriter csvDapi = new FileWriter(
						new File(directFolder.getAbsolutePath() + File.separator + "TableData_w1" + ".csv"));

				for (int i = 0; i < tableDapi.getModel().getColumnCount(); i++) {
					csvDapi.write(tableDapi.getModel().getColumnName(i) + ",");
				}

				csvDapi.write("\n");

				for (int i = 0; i < tableDapi.getModel().getRowCount(); i++) {
					for (int j = 0; j < tableDapi.getModel().getColumnCount(); j++) {
						csvDapi.write(tableDapi.getModel().getValueAt(i, j).toString() + ",");
					}
					csvDapi.write("\n");
				}

				csvDapi.write("\n");
				csvDapi.write("\n");

				for (int i = 0; i < tableSDapi.getModel().getColumnCount(); i++) {
					csvDapi.write(tableSDapi.getModel().getColumnName(i) + ",");
				}

				csvDapi.write("\n");

				for (int i = 0; i < tableSDapi.getModel().getRowCount(); i++) {
					for (int j = 0; j < tableSDapi.getModel().getColumnCount(); j++) {
						csvDapi.write(tableSDapi.getModel().getValueAt(i, j).toString() + ",");
					}
					csvDapi.write("\n");
				}

				csvDapi.close();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			//P21
			try {

				FileWriter csvP21 = new FileWriter(
						new File(directFolder.getAbsolutePath() + File.separator + "TableData_w2" + ".csv"));

				for (int i = 0; i < tableP21.getModel().getColumnCount(); i++) {
					csvP21.write(tableP21.getModel().getColumnName(i) + ",");
				}

				csvP21.write("\n");

				for (int i = 0; i < tableP21.getModel().getRowCount(); i++) {
					for (int j = 0; j < tableP21.getModel().getColumnCount(); j++) {
						csvP21.write(tableP21.getModel().getValueAt(i, j).toString() + ",");
					}
					csvP21.write("\n");
				}

				csvP21.write("\n");
				csvP21.write("\n");

				for (int i = 0; i < tableSP21.getModel().getColumnCount(); i++) {
					csvP21.write(tableSP21.getModel().getColumnName(i) + ",");
				}

				csvP21.write("\n");

				for (int i = 0; i < tableSP21.getModel().getRowCount(); i++) {
					for (int j = 0; j < tableSP21.getModel().getColumnCount(); j++) {
						csvP21.write(tableSP21.getModel().getValueAt(i, j).toString() + ",");
					}
					csvP21.write("\n");
				}

				csvP21.close();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//Bgal
			try {

				FileWriter csvBgal = new FileWriter(
						new File(directFolder.getAbsolutePath() + File.separator + "TableData_w3" + ".csv"));

				for (int i = 0; i < tableBgal.getModel().getColumnCount(); i++) {
					csvBgal.write(tableBgal.getModel().getColumnName(i) + ",");
				}

				csvBgal.write("\n");

				for (int i = 0; i < tableBgal.getModel().getRowCount(); i++) {
					for (int j = 0; j < tableBgal.getModel().getColumnCount(); j++) {
						csvBgal.write(tableBgal.getModel().getValueAt(i, j).toString() + ",");
					}
					csvBgal.write("\n");
				}

				csvBgal.write("\n");
				csvBgal.write("\n");

				for (int i = 0; i < tableSBgal.getModel().getColumnCount(); i++) {
					csvBgal.write(tableSBgal.getModel().getColumnName(i) + ",");
				}

				csvBgal.write("\n");

				for (int i = 0; i < tableSBgal.getModel().getRowCount(); i++) {
					for (int j = 0; j < tableSBgal.getModel().getColumnCount(); j++) {
						csvBgal.write(tableSBgal.getModel().getValueAt(i, j).toString() + ",");
					}
					csvBgal.write("\n");
				}

				csvBgal.close();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//Scatter
			try {

				FileWriter csvScatter = new FileWriter(
						new File(directFolder.getAbsolutePath() + File.separator + "TableData_Final_Results" + ".csv"));

				for (int i = 0; i < tablePositive.getModel().getColumnCount(); i++) {
					csvScatter.write(tablePositive.getModel().getColumnName(i) + ",");
				}

				csvScatter.write("\n");

				for (int i = 0; i < tablePositive.getModel().getRowCount(); i++) {
					for (int j = 0; j < tablePositive.getModel().getColumnCount(); j++) {
						csvScatter.write(tablePositive.getModel().getValueAt(i, j).toString() + ",");
					}
					csvScatter.write("\n");
				}

				csvScatter.write("\n");
				csvScatter.write("\n");

				for (int i = 0; i < tableChannel.getModel().getColumnCount(); i++) {
					csvScatter.write(tableChannel.getModel().getColumnName(i) + ",");
				}

				csvScatter.write("\n");

				for (int i = 0; i < tableChannel.getModel().getRowCount(); i++) {
					for (int j = 0; j < tableChannel.getModel().getColumnCount(); j++) {
						csvScatter.write(tableChannel.getModel().getValueAt(i, j).toString() + ",");
					}
					csvScatter.write("\n");
				}
				
				for (int i = 0; i < tableQuadrant.getModel().getColumnCount(); i++) {
					csvScatter.write(tableQuadrant.getModel().getColumnName(i) + ",");
				}

				csvScatter.write("\n");

				for (int i = 0; i < tableQuadrant.getModel().getRowCount(); i++) {
					for (int j = 0; j < tableQuadrant.getModel().getColumnCount(); j++) {
						csvScatter.write(tableQuadrant.getModel().getValueAt(i, j).toString() + ",");
					}
					csvScatter.write("\n");
				}
				
				for (int i = 0; i < tableIdentity.getModel().getColumnCount(); i++) {
					csvScatter.write(tableIdentity.getModel().getColumnName(i) + ",");
				}

				csvScatter.write("\n");

				for (int i = 0; i < tableIdentity.getModel().getRowCount(); i++) {
					for (int j = 0; j < tableIdentity.getModel().getColumnCount(); j++) {
						csvScatter.write(tableIdentity.getModel().getValueAt(i, j).toString() + ",");
					}
					csvScatter.write("\n");
				}

				csvScatter.close();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return false;

	}

}
