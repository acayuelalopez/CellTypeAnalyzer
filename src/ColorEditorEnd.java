import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import ij.IJ;
import ij.plugin.frame.RoiManager;

public class ColorEditorEnd extends AbstractCellEditor implements TableCellEditor {
	private JPanel myPanel, panelAdd, panelEdit;
	private JLabel labelInitt;
	private int result, input;
	static DefaultTableModel modelC, modelPositive, modelSPositive;
	static JTable tableC, tablePositive, tableSPositive;
	private JButton addButton, editButton, deleteButton, colorButtonAdd, colorButtonEdit, okButton, okButtonEdit,
			cancelButton, cancelButtonEdit, okButtonAdd, cancelButtonAdd;
	static JList<String> filterListEnd, classListEnd, classListMiddle;
	static DefaultListModel<String> modelListEnd, modelListMiddle, modelListClassMiddle, modelListClassEnd;
	private JLabel[] labelsC, colorsC, featuresC;
	private JLabel addTextAdd, addTextEdit, labelsTableC;
	private JTextField addTextFAdd, addTextFEdit;
	private Color currentColorAdd, currentColorEdit, colorCInitial, colorCFinal;
	private Object labelC, colorC, featureC;
	private String addTextInitial, addTextFinal, featureInitial, featureFinal;
	static JFrame myFrame, myFrameAdd, myFrameEdit;
	private Icon iconOKCell, iconCancelCell;
	private int indexRowC, selectedRow;
	static JComboBox comboFilters;
	static Object[][] tableDataEnd;
	static ArrayList<List<String>> ch2List = new ArrayList<List<String>>(), ch3List = new ArrayList<List<String>>();
	static List<Color> listOfColors = new ArrayList<Color>();

	public ColorEditorEnd(JList<String> filterListEnd) {
		this.filterListEnd = filterListEnd;

		addButton = new JButton("");
		addButton.setBounds(50, 100, 95, 30);
		ImageIcon iconAdd = JFrameWizard.createImageIcon("images/add.png");
		Icon iconAddCell = new ImageIcon(iconAdd.getImage().getScaledInstance(17, 15, Image.SCALE_SMOOTH));
		addButton.setIcon(iconAddCell);
		addButton.setToolTipText("Click this button to add your class-label.");
		editButton = new JButton("");
		editButton.setBounds(50, 100, 95, 30);
		ImageIcon iconEdit = JFrameWizard.createImageIcon("images/edit.png");
		Icon iconEditCell = new ImageIcon(iconEdit.getImage().getScaledInstance(17, 15, Image.SCALE_SMOOTH));
		editButton.setIcon(iconEditCell);
		editButton.setToolTipText("Click this button to edit your class-label.");
		deleteButton = new JButton("");
		deleteButton.setBounds(50, 100, 95, 30);
		ImageIcon iconDelete = JFrameWizard.createImageIcon("images/bin.png");
		Icon iconDeleteCell = new ImageIcon(iconDelete.getImage().getScaledInstance(22, 20, Image.SCALE_SMOOTH));
		deleteButton.setIcon(iconDeleteCell);
		deleteButton.setToolTipText("Click this button to delete your class-label.");
		myFrame = new JFrame("Manage Labels");
		myFrame.setLocation(new Point(100, 100));
		myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		myPanel = new JPanel();
		myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
		Object[][] rowData2 = {};
		Object columnNames[] = { "Name", "Color", "Feature" };

		modelC = new DefaultTableModel(rowData2, columnNames) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			// Returning the Class of each column will allow different
			// renderers to be used based on Class

			public boolean isCellEditable(int row, int col) {
				return false;

			}

			@Override
			public Class<?> getColumnClass(int column) {
				if (getRowCount() > 0) {
					Object value = getValueAt(0, column);
					if (value != null) {
						return getValueAt(0, column).getClass();
					}
				}

				return super.getColumnClass(column);
			}
		};

		tableC = new JTable();
		tableC.getTableHeader().setDefaultRenderer(new SimpleHeaderRenderer());
		tableC.setSelectionBackground(new Color(229, 255, 204));
		tableC.setSelectionForeground(new Color(0, 102, 0));
		TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(modelC);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		tableC.setDefaultRenderer(JLabel.class, centerRenderer);
		tableC.setRowSorter(rowSorter);
		JPanel panelButtons = new JPanel();
		panelButtons.setLayout(new FlowLayout());
		panelButtons.add(addButton);
		panelButtons.add(editButton);
		panelButtons.add(deleteButton);
		tableC.setAutoCreateRowSorter(true);
		tableC.setEnabled(true);
		tableC.setCellSelectionEnabled(true);
		// tableC.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableC.setRowSelectionAllowed(true);
		tableC.setColumnSelectionAllowed(false);
		tableC.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tableC.setDefaultRenderer(JLabel.class, new Renderer());
		tableC.setDefaultRenderer(Color.class, new ColorRenderer(true));
		tableC.setModel(modelC);
		TableColumn column1 = null;
		column1 = tableC.getColumnModel().getColumn(0); // this was the column which was not highlighted
		column1.setPreferredWidth(7);
		column1.setCellRenderer(new ResultRendererC());
		TableColumn column2 = null;
		column2 = tableC.getColumnModel().getColumn(1); // this was the column which was not highlighted
		column2.setPreferredWidth(5);
		column2.setCellRenderer(new ResultRendererC());
		TableColumn column3 = null;
		column3 = tableC.getColumnModel().getColumn(2); // this was the column which was not highlighted
		column3.setPreferredWidth(15);
		column3.setCellRenderer(new ResultRendererC());
		JScrollPane scrollPane = new JScrollPane(tableC);
		for (int u = 0; u < tableC.getColumnCount(); u++)
			tableC.getColumnModel().getColumn(u).setPreferredWidth(90);
		tableC.setRowHeight(25);
		myPanel.add(Box.createHorizontalStrut(15));
		myPanel.add(panelButtons);
		myPanel.add(scrollPane, BorderLayout.CENTER);
		myPanel.setSize(300, 150);
		// frame.setVisible(true);
		myPanel.add(Box.createHorizontalStrut(15));
		okButton = new JButton("");
		okButton.setBounds(50, 100, 95, 30);
		ImageIcon iconOk = JFrameWizard.createImageIcon("images/add.png");
		iconOKCell = new ImageIcon(iconOk.getImage().getScaledInstance(17, 15, Image.SCALE_SMOOTH));
		okButton.setIcon(iconOKCell);
		okButton.setToolTipText("Click this button to edit your color selection.");
		cancelButton = new JButton("");
		cancelButton.setBounds(50, 100, 95, 30);
		ImageIcon iconCancel = JFrameWizard.createImageIcon("images/cancel.png");
		iconCancelCell = new ImageIcon(iconCancel.getImage().getScaledInstance(17, 15, Image.SCALE_SMOOTH));
		cancelButton.setIcon(iconCancelCell);
		cancelButton.setToolTipText("Click this button to cancel your color selection.");
		JPanel panelOkCancel = new JPanel();
		panelOkCancel.setLayout(new FlowLayout());
		panelOkCancel.add(okButton);
		panelOkCancel.add(cancelButton);
		myPanel.add(panelOkCancel);
		myFrame.getContentPane().add(myPanel);
		myFrame.pack();
		myFrame.setLocationByPlatform(true);

		///
		panelAdd = new JPanel();
		panelAdd.setPreferredSize(new Dimension(200, 100));
		JPanel panel1 = new JPanel();
		panel1.setLayout(new FlowLayout());
		panelAdd.setLayout(new FlowLayout());
		addTextAdd = new JLabel("Label Name: ");
		addTextFAdd = new JTextField(8);
		panel1.add(addTextAdd);
		panel1.add(addTextFAdd);
		JLabel pickC = new JLabel("Pick a color: ");
		panelAdd.add(pickC);
		colorButtonAdd = new JButton();
		colorButtonAdd.setPreferredSize(new Dimension(200, 75));

		panelAdd.add(colorButtonAdd);
		okButtonAdd = new JButton("");
		okButtonAdd.setBounds(50, 100, 95, 30);
		okButtonAdd.setIcon(iconOKCell);
		okButtonAdd.setToolTipText("Click this button to edit your color selection.");
		cancelButtonAdd = new JButton("");
		cancelButtonAdd.setBounds(50, 100, 95, 30);
		cancelButtonAdd.setIcon(iconCancelCell);
		cancelButtonAdd.setToolTipText("Click this button to cancel your color selection.");
		JPanel panelOkCancelAdd = new JPanel();
		panelOkCancelAdd.setLayout(new FlowLayout());
		panelOkCancelAdd.add(okButtonAdd);
		panelOkCancelAdd.add(cancelButtonAdd);
		myFrameAdd = new JFrame("Add Label");
		JPanel mainPanel = new JPanel();
		mainPanel.add(panel1);
		mainPanel.add(panelAdd);
		mainPanel.add(panelOkCancelAdd);
		myFrameAdd.setPreferredSize(new Dimension(250, 250));
		myFrameAdd.getContentPane().add(mainPanel);
		myFrameAdd.pack();
		myFrameAdd.setLocationByPlatform(true);

		/////

		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(200, 100));
		JPanel panel2 = new JPanel();
		panel2.setLayout(new FlowLayout());
		panel.setLayout(new FlowLayout());
		addTextEdit = new JLabel("Label Name: ");
		addTextFEdit = new JTextField(8);
		panel2.add(addTextEdit);
		panel2.add(addTextFEdit);
		JLabel pickEdit = new JLabel("Pick a Color: ");
		panel.add(pickEdit);
		colorButtonEdit = new JButton();
		colorButtonEdit.setPreferredSize(new Dimension(200, 75));
		panel.add(colorButtonEdit);
		okButtonEdit = new JButton("");
		okButtonEdit.setBounds(50, 100, 95, 30);
		okButtonEdit.setIcon(iconOKCell);
		okButtonEdit.setToolTipText("Click this button to edit your color selection.");
		cancelButtonEdit = new JButton("");
		cancelButtonEdit.setBounds(50, 100, 95, 30);
		cancelButtonEdit.setIcon(iconCancelCell);
		cancelButtonEdit.setToolTipText("Click this button to cancel your color selection.");
		JPanel panelOkCancelEdit = new JPanel();
		panelOkCancelEdit.setLayout(new FlowLayout());
		panelOkCancelEdit.add(okButtonEdit);
		panelOkCancelEdit.add(cancelButtonEdit);
		myFrameEdit = new JFrame("Edit Label");
		JPanel mainPanelEdit = new JPanel();
		mainPanelEdit.add(panel2);
		mainPanelEdit.add(panel);
		mainPanelEdit.add(panelOkCancelEdit);
		myFrameEdit.setPreferredSize(new Dimension(250, 250));
		myFrameEdit.getContentPane().add(mainPanelEdit);
		myFrameEdit.pack();
		myFrameEdit.setLocationByPlatform(true);

		///
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myFrameAdd.setVisible(true);

			}

		});
		okButtonAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JLabel labelString = new JLabel();
				JLabel labelColor = new JLabel();
				JLabel labelFeature = new JLabel();
				labelColor.setText("");
				labelColor.setBackground(currentColorAdd);
				labelString.setText(addTextFAdd.getText());
				labelString.setHorizontalAlignment(SwingConstants.CENTER);
				labelString.setBackground(currentColorAdd);
				labelColor.setOpaque(true);
				StringBuilder filterItems = new StringBuilder();
				for (int x = 0; x < filterListEnd.getModel().getSize(); x++)
					filterItems.append(filterListEnd.getModel().getElementAt(x) + ";").append("<br>");
				for (int y = 0; y < MiddlePageModified.filterList.getModel().getSize(); y++)
					filterItems.append(MiddlePageModified.filterList.getModel().getElementAt(y) + ";").append("<br>");

				labelFeature.setText("<html>" + filterItems.toString() + "</html>");
				modelC.addRow(new Object[] { labelString, labelColor, labelFeature });
				// ColorEditorMiddle.modelC.addRow(new Object[] { labelString, labelColor,
				// labelFeature });
				modelC.fireTableDataChanged();
				tableC.repaint();

				myFrameAdd.dispatchEvent(new WindowEvent(myFrameAdd, WindowEvent.WINDOW_CLOSING));
			}

		});

		cancelButtonAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myFrameAdd.dispatchEvent(new WindowEvent(myFrameAdd, WindowEvent.WINDOW_CLOSING));
			}

		});
		editButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				myFrameEdit.setVisible(true);

				indexRowC = tableC.getSelectedRow();
				if (tableC.getSelectedRowCount() == 0)
					return;
				if (tableC.getSelectedRowCount() == 1) {
					labelC = new Object();
					colorC = new Object();
					labelC = (JLabel) modelC.getValueAt(tableC.convertRowIndexToModel(indexRowC),
							tableC.convertColumnIndexToModel(0));
					colorC = (JLabel) modelC.getValueAt(tableC.convertRowIndexToModel(indexRowC),
							tableC.convertColumnIndexToModel(1));
					addTextInitial = ((JLabel) labelC).getText();
					colorCInitial = ((JLabel) colorC).getBackground();
				}

				colorButtonEdit.setBackground(((JLabel) colorC).getBackground());
				currentColorEdit = ((JLabel) colorC).getBackground();
				colorButtonEdit.setContentAreaFilled(false);
				colorButtonEdit.setOpaque(true);

				addTextFEdit.setText(((JLabel) labelC).getText());

			}
		});

		colorButtonAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				java.util.Locale.setDefault(java.util.Locale.ENGLISH);
				JColorChooser.setDefaultLocale(java.util.Locale.ENGLISH);
				JColorChooser.setDefaultLocale(java.util.Locale.getDefault());
				currentColorAdd = JColorChooser.showDialog(null, "Pick a Color: ", colorButtonAdd.getBackground());
				if (currentColorAdd != null)
					colorButtonAdd.setBackground(currentColorAdd);
			}
		});

		okButtonEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JLabel labelString = new JLabel();
				JLabel labelColor = new JLabel();
				labelColor.setText("");
				labelColor.setBackground(currentColorEdit);
				labelString.setText(addTextFEdit.getText());
				labelString.setHorizontalAlignment(SwingConstants.CENTER);
				labelString.setBackground(currentColorEdit);
				labelColor.setOpaque(true);
				addTextFinal = ((JLabel) labelString).getText();
				colorCFinal = ((JLabel) labelColor).getBackground();

				if (addTextFinal.equals(addTextInitial) == false)
					modelC.setValueAt(labelString, tableC.convertRowIndexToModel(indexRowC),
							tableC.convertColumnIndexToModel(0));
				if (addTextFinal.equals(addTextInitial) == true)
					modelC.setValueAt(labelC, tableC.convertRowIndexToModel(indexRowC),
							tableC.convertColumnIndexToModel(0));
				if (currentColorEdit != colorCInitial)
					modelC.setValueAt(labelColor, tableC.convertRowIndexToModel(indexRowC),
							tableC.convertColumnIndexToModel(1));
				if (currentColorEdit == colorCInitial)
					modelC.setValueAt(colorC, tableC.convertRowIndexToModel(indexRowC),
							tableC.convertColumnIndexToModel(1));

				modelC.fireTableCellUpdated(tableC.convertRowIndexToModel(indexRowC),
						tableC.convertColumnIndexToModel(0));
				modelC.fireTableCellUpdated(tableC.convertRowIndexToModel(indexRowC),
						tableC.convertColumnIndexToModel(1));
				tableC.repaint();

				myFrameEdit.dispatchEvent(new WindowEvent(myFrameEdit, WindowEvent.WINDOW_CLOSING));

			}

		});

		cancelButtonEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myFrameEdit.dispatchEvent(new WindowEvent(myFrameEdit, WindowEvent.WINDOW_CLOSING));
			}

		});

	}

	public void setClassAction() {
		colorButtonEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				java.util.Locale.setDefault(java.util.Locale.ENGLISH);
				JColorChooser.setDefaultLocale(java.util.Locale.ENGLISH);
				JColorChooser.setDefaultLocale(java.util.Locale.getDefault());
				currentColorEdit = JColorChooser.showDialog(null, "Pick a Color: ", colorButtonEdit.getBackground());
				if (currentColorEdit != null)
					colorButtonEdit.setBackground(currentColorEdit);
			}
		});
		okButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				JFrameWizard.modelC = tableC.getModel();

				tableDataEnd = new Object[ColorEditorEnd.modelC.getRowCount()][ColorEditorEnd.modelC.getColumnCount()];
				for (int i = 0; i < ColorEditorEnd.modelC.getRowCount(); i++)
					for (int j = 0; j < ColorEditorEnd.modelC.getColumnCount(); j++)
						tableDataEnd[i][j] = ColorEditorEnd.modelC.getValueAt(i, j);
				if (tableC.getSelectedRowCount() <= 0)
					myFrame.dispatchEvent(new WindowEvent(myFrame, WindowEvent.WINDOW_CLOSING));

				if (tableC.getSelectedRowCount() == 1) {

					List<String> listClasses = new ArrayList<String>();
					selectedRow = tableC.getSelectedRow();

					if (MiddlePageModified.modelListClass.getSize() == 0) {
						MiddlePageModified.modelListClass.addElement(
								((JLabel) tableC.getModel().getValueAt(tableC.convertRowIndexToModel(selectedRow),
										tableC.convertColumnIndexToModel(0))).getText());
						MiddlePageModified2.comboClass.addItem(
								((JLabel) tableC.getModel().getValueAt(tableC.convertRowIndexToModel(selectedRow),
										tableC.convertColumnIndexToModel(0))).getText());
						MiddlePageModified2.comboQuadrant.addItem(
								((JLabel) tableC.getModel().getValueAt(tableC.convertRowIndexToModel(selectedRow),
										tableC.convertColumnIndexToModel(0))).getText());
						listOfColors
								.add(((JLabel) tableC.getModel().getValueAt(tableC.convertRowIndexToModel(selectedRow),
										tableC.convertColumnIndexToModel(1))).getBackground());
						labelsTableC = new JLabel();
						for (int i = 0; i < tableC.getModel().getRowCount(); i++) {
							labelsTableC.setText(((JLabel) tableC.getModel().getValueAt(selectedRow,
									tableC.convertColumnIndexToModel(0))).getText());
							labelsTableC.setHorizontalAlignment(SwingConstants.CENTER);
							labelsTableC.setBackground(((JLabel) tableC.getModel().getValueAt(selectedRow,
									tableC.convertColumnIndexToModel(1))).getBackground());
							labelsTableC.setOpaque(true);
						}

						String filterFeature[] = ((JLabel) tableC.getModel().getValueAt(selectedRow,
								tableC.convertColumnIndexToModel(2))).getText().replace("<html>", "").split("<br>");
						try {
							MiddlePageModified.modelList.removeAllElements();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						for (int i = 0; i < filterFeature.length - 1; i++)
							if (filterFeature[i].contains(StartPageModified.tfCh2.getText()) == true)
								MiddlePageModified.modelList.addElement(filterFeature[i]);

						filterToolMiddle();
						int counter = 0;
						for (int i = 0; i < MiddlePageModified.tablePositive.getRowCount(); i++) {
							MiddlePageModified.tablePositive.getModel().setValueAt(labelsTableC,
									MiddlePageModified.tablePositive.convertRowIndexToModel(i),
									MiddlePageModified.tablePositive.convertColumnIndexToModel(2));
							counter++;
						}

						Object[][] tableData = new Object[MiddlePageModified.tablePositive
								.getRowCount()][MiddlePageModified.tablePositive.getColumnCount()];
						for (int i = 0; i < MiddlePageModified.tablePositive.getRowCount(); i++)
							for (int j = 0; j < MiddlePageModified.tablePositive.getColumnCount(); j++)
								tableData[i][j] = MiddlePageModified.tablePositive.getModel().getValueAt(
										MiddlePageModified.tablePositive.convertRowIndexToModel(i),
										MiddlePageModified.tablePositive.convertColumnIndexToModel(j));

						List<Double> total = new ArrayList<Double>();
						if (MiddlePageModified.tablePositive.getRowCount() >= 1) {
							for (int j = 3; j < MiddlePageModified.tablePositive.getColumnCount(); j++) {
								double sum = 0.0;
								for (int i = 0; i < MiddlePageModified.tablePositive.getRowCount(); i++) {
									sum += (double) MiddlePageModified.tablePositive.getValueAt(i, j);

								}
								total.add(sum / MiddlePageModified.tablePositive.getRowCount());
							}

						}

						resetFilterMiddle();
						myFrame.dispatchEvent(new WindowEvent(myFrame, WindowEvent.WINDOW_CLOSING));
						double percentage = Math.round(
								((double) (counter * 100.0) / (double) MiddlePageModified.tablePositive.getRowCount())
										* 100.0)
								/ 100.0;
						List<String> rowDataList = new ArrayList<String>();
						for (int i = 0; i < total.size(); i++)
							rowDataList.add(String.valueOf(Math.round(total.get(i) * 100.0) / 100.0));
						rowDataList.add(labelsTableC.getText());
						rowDataList.add(String.valueOf(counter));
						rowDataList.add(String.valueOf(percentage));
						Object[] rowData = new Object[rowDataList.size()];
						rowDataList.toArray(rowData);
						MiddlePageModified.modelSPositive.addRow(rowData);
						for (int i = 3; i < MiddlePageModified.modelSPositive.getColumnCount(); i++)
							MiddlePageModified.modelSPositive.setValueAt(
									String.valueOf(Math.round(total.get(i - 3) * 100.0) / 100.0),
									MiddlePageModified.modelSPositive.getRowCount() - 1,
									MiddlePageModified.tableSPositive.convertColumnIndexToModel(i));
						MiddlePageModified.modelSPositive.setValueAt(labelsTableC.getText(),
								MiddlePageModified.modelSPositive.getRowCount() - 1,
								MiddlePageModified.tableSPositive.convertColumnIndexToModel(0));
						MiddlePageModified.modelSPositive.setValueAt(String.valueOf(counter),
								MiddlePageModified.modelSPositive.getRowCount() - 1,
								MiddlePageModified.tableSPositive.convertColumnIndexToModel(1));
						MiddlePageModified.modelSPositive.setValueAt(String.valueOf(percentage),
								MiddlePageModified.modelSPositive.getRowCount() - 1,
								MiddlePageModified.tableSPositive.convertColumnIndexToModel(2));

					}

					if (MiddlePageModified.modelListClass.getSize() >= 1) {
						for (int i = 0; i < MiddlePageModified.modelListClass.getSize(); i++)
							listClasses.add(MiddlePageModified.modelListClass.getElementAt(i));

						if (listClasses.contains(
								((JLabel) tableC.getModel().getValueAt(tableC.convertRowIndexToModel(selectedRow),
										tableC.convertColumnIndexToModel(0))).getText()) == false) {
							MiddlePageModified.modelListClass.addElement(
									((JLabel) tableC.getModel().getValueAt(tableC.convertRowIndexToModel(selectedRow),
											tableC.convertColumnIndexToModel(0))).getText());
							MiddlePageModified2.comboClass.addItem(
									((JLabel) tableC.getModel().getValueAt(tableC.convertRowIndexToModel(selectedRow),
											tableC.convertColumnIndexToModel(0))).getText());
							MiddlePageModified2.comboQuadrant.addItem(
									((JLabel) tableC.getModel().getValueAt(tableC.convertRowIndexToModel(selectedRow),
											tableC.convertColumnIndexToModel(0))).getText());
							listOfColors
									.add(((JLabel) tableC.getModel().getValueAt(tableC.convertRowIndexToModel(selectedRow),
											tableC.convertColumnIndexToModel(1))).getBackground());
							labelsTableC = new JLabel();
							for (int i = 0; i < tableC.getModel().getRowCount(); i++) {
								labelsTableC.setText(((JLabel) tableC.getModel().getValueAt(selectedRow,
										tableC.convertColumnIndexToModel(0))).getText());
								labelsTableC.setHorizontalAlignment(SwingConstants.CENTER);
								labelsTableC.setBackground(((JLabel) tableC.getModel().getValueAt(selectedRow,
										tableC.convertColumnIndexToModel(1))).getBackground());
								labelsTableC.setOpaque(true);
							}

							String filterFeature[] = ((JLabel) tableC.getModel().getValueAt(selectedRow,
									tableC.convertColumnIndexToModel(2))).getText().replace("<html>", "").split("<br>");
							try {
								MiddlePageModified.modelList.removeAllElements();
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							for (int i = 0; i < filterFeature.length - 1; i++)
								if (filterFeature[i].contains(StartPageModified.tfCh2.getText()) == true)
									MiddlePageModified.modelList.addElement(filterFeature[i]);
							filterToolMiddle();
							int counter = 0;
							for (int i = 0; i < MiddlePageModified.tablePositive.getRowCount(); i++) {
								MiddlePageModified.tablePositive.getModel().setValueAt(labelsTableC,
										MiddlePageModified.tablePositive.convertRowIndexToModel(i),
										MiddlePageModified.tablePositive.convertColumnIndexToModel(2));
								counter++;
							}
							for (int i = 0; i < MiddlePageModified.tablePositive.getRowCount(); i++)
								MiddlePageModified.tablePositive.getModel().setValueAt(labelsTableC,
										MiddlePageModified.tablePositive.convertRowIndexToModel(i),
										MiddlePageModified.tablePositive.convertColumnIndexToModel(2));

							Object[][] tableData = new Object[MiddlePageModified.tablePositive
									.getRowCount()][MiddlePageModified.tablePositive.getColumnCount()];
							for (int i = 0; i < MiddlePageModified.tablePositive.getRowCount(); i++)
								for (int j = 0; j < MiddlePageModified.tablePositive.getColumnCount(); j++)
									tableData[i][j] = MiddlePageModified.tablePositive.getModel().getValueAt(
											MiddlePageModified.tablePositive.convertRowIndexToModel(i),
											MiddlePageModified.tablePositive.convertColumnIndexToModel(j));

							List<Double> total = new ArrayList<Double>();
							if (tableData.length >= 1) {
								for (int col = 3; col < tableData[col].length; col++) {
									double sum = 0;
									for (int row = 0; row < tableData.length; row++) {
										sum += (double) tableData[row][col];

									}
									total.add(sum / tableData.length);
								}
							}

							resetFilterMiddle();
							myFrame.dispatchEvent(new WindowEvent(myFrame, WindowEvent.WINDOW_CLOSING));
							double percentage = Math
									.round(((double) (counter * 100.0) / (double) tablePositive.getRowCount()) * 100.0)
									/ 100.0;
							List<String> rowDataList = new ArrayList<String>();
							for (int i = 0; i < total.size(); i++)
								rowDataList.add(String.valueOf(Math.round(total.get(i) * 100.0) / 100.0));
							rowDataList.add(labelsTableC.getText());
							rowDataList.add(String.valueOf(counter));
							rowDataList.add(String.valueOf(percentage));
							Object[] rowData = new Object[rowDataList.size()];
							rowDataList.toArray(rowData);
							MiddlePageModified.modelSPositive.addRow(rowData);
							for (int i = 3; i < MiddlePageModified.modelSPositive.getColumnCount(); i++)
								MiddlePageModified.modelSPositive.setValueAt(
										String.valueOf(Math.round(total.get(i - 3) * 100.0) / 100.0),
										MiddlePageModified.modelSPositive.getRowCount() - 1,
										MiddlePageModified.tableSPositive.convertColumnIndexToModel(i));
							MiddlePageModified.modelSPositive.setValueAt(labelsTableC.getText(),
									MiddlePageModified.modelSPositive.getRowCount() - 1,
									MiddlePageModified.tableSPositive.convertColumnIndexToModel(0));
							MiddlePageModified.modelSPositive.setValueAt(String.valueOf(counter),
									MiddlePageModified.modelSPositive.getRowCount() - 1,
									MiddlePageModified.tableSPositive.convertColumnIndexToModel(1));
							MiddlePageModified.modelSPositive.setValueAt(String.valueOf(percentage),
									MiddlePageModified.modelSPositive.getRowCount() - 1,
									MiddlePageModified.tableSPositive.convertColumnIndexToModel(2));

						}

						if (listClasses.contains(
								((JLabel) tableC.getModel().getValueAt(tableC.convertRowIndexToModel(selectedRow),
										tableC.convertColumnIndexToModel(0))).getText()) == true)
							myFrame.dispatchEvent(new WindowEvent(myFrame, WindowEvent.WINDOW_CLOSING));

					}

				}
				if (tableC.getSelectedRowCount() == 1) {

					List<String> listClasses = new ArrayList<String>();
					EndPageModified.classList = classListEnd;
					EndPageModified.modelListClass = modelListClassEnd;
					int selectedRow = tableC.getSelectedRow();

					if (modelListClassEnd.getSize() == 0) {
						modelListClassEnd.addElement(
								((JLabel) tableC.getModel().getValueAt(tableC.convertRowIndexToModel(selectedRow),
										tableC.convertColumnIndexToModel(0))).getText());
						labelsTableC = new JLabel();
						for (int i = 0; i < tableC.getModel().getRowCount(); i++) {
							labelsTableC.setText(((JLabel) tableC.getModel().getValueAt(selectedRow,
									tableC.convertColumnIndexToModel(0))).getText());
							labelsTableC.setHorizontalAlignment(SwingConstants.CENTER);
							labelsTableC.setBackground(((JLabel) tableC.getModel().getValueAt(selectedRow,
									tableC.convertColumnIndexToModel(1))).getBackground());
							labelsTableC.setOpaque(true);
						}

						String filterFeature[] = ((JLabel) tableC.getModel().getValueAt(selectedRow,
								tableC.convertColumnIndexToModel(2))).getText().replace("<html>", "").split("<br>");
						try {
							modelListEnd.removeAllElements();
						} catch (Exception e1) {
							e1.printStackTrace();
						}

						for (int i = 0; i < filterFeature.length - 1; i++)
							if (filterFeature[i].contains(StartPageModified.tfCh3.getText()) == true)
								modelListEnd.addElement(filterFeature[i]);

						filterToolEnd();
						int counter = 0;
						for (int i = 0; i < tablePositive.getRowCount(); i++) {
							tablePositive.getModel().setValueAt(labelsTableC, tablePositive.convertRowIndexToModel(i),
									tablePositive.convertColumnIndexToModel(2));
							counter++;
						}

						Object[][] tableData = new Object[tablePositive.getRowCount()][tablePositive.getColumnCount()];
						for (int i = 0; i < tablePositive.getRowCount(); i++)
							for (int j = 0; j < tablePositive.getColumnCount(); j++)
								tableData[i][j] = tablePositive.getModel().getValueAt(
										tablePositive.convertRowIndexToModel(i),
										tablePositive.convertColumnIndexToModel(j));

						List<Double> total = new ArrayList<Double>();
						if (tablePositive.getRowCount() >= 1) {
							for (int j = 3; j < tablePositive.getColumnCount(); j++) {
								double sum = 0.0;
								for (int i = 0; i < tablePositive.getRowCount(); i++) {
									sum += (double) tablePositive.getValueAt(i, j);

								}
								total.add(sum / tablePositive.getRowCount());
							}

						}

						resetFilterEnd();
						myFrame.dispatchEvent(new WindowEvent(myFrame, WindowEvent.WINDOW_CLOSING));
						double percentage = Math.round(
								((double) (counter * 100.0) / (double) tablePositive.getRowCount()) * 100.0) / 100.0;
						List<String> rowDataList = new ArrayList<String>();
						for (int i = 0; i < total.size(); i++)
							rowDataList.add(String.valueOf(Math.round(total.get(i) * 100.0) / 100.0));
						rowDataList.add(labelsTableC.getText());
						rowDataList.add(String.valueOf(counter));
						rowDataList.add(String.valueOf(percentage));
						Object[] rowData = new Object[rowDataList.size()];
						rowDataList.toArray(rowData);
						modelSPositive.addRow(rowData);
						for (int i = 3; i < modelSPositive.getColumnCount(); i++)
							modelSPositive.setValueAt(String.valueOf(Math.round(total.get(i - 3) * 100.0) / 100.0),
									modelSPositive.getRowCount() - 1, tableSPositive.convertColumnIndexToModel(i));
						modelSPositive.setValueAt(labelsTableC.getText(), modelSPositive.getRowCount() - 1,
								tableSPositive.convertColumnIndexToModel(0));
						modelSPositive.setValueAt(String.valueOf(counter), modelSPositive.getRowCount() - 1,
								tableSPositive.convertColumnIndexToModel(1));
						modelSPositive.setValueAt(String.valueOf(percentage), modelSPositive.getRowCount() - 1,
								tableSPositive.convertColumnIndexToModel(2));

					}

					if (modelListClassEnd.getSize() >= 1) {
						for (int i = 0; i < modelListClassEnd.getSize(); i++) {
							if (modelListClassEnd.getElementAt(i).contains("Foci/Nuc") == Boolean.TRUE)
								modelListClassEnd.removeElementAt(i);
							listClasses.add(modelListClassEnd.getElementAt(i));
						}

						if (listClasses.contains(
								((JLabel) tableC.getModel().getValueAt(tableC.convertRowIndexToModel(selectedRow),
										tableC.convertColumnIndexToModel(0))).getText()) == false) {
							modelListClassEnd.addElement(
									((JLabel) tableC.getModel().getValueAt(tableC.convertRowIndexToModel(selectedRow),
											tableC.convertColumnIndexToModel(0))).getText());

							labelsTableC = new JLabel();
							for (int i = 0; i < tableC.getModel().getRowCount(); i++) {
								labelsTableC.setText(((JLabel) tableC.getModel().getValueAt(selectedRow,
										tableC.convertColumnIndexToModel(0))).getText());
								labelsTableC.setHorizontalAlignment(SwingConstants.CENTER);
								labelsTableC.setBackground(((JLabel) tableC.getModel().getValueAt(selectedRow,
										tableC.convertColumnIndexToModel(1))).getBackground());
								labelsTableC.setOpaque(true);
							}

							String filterFeature[] = ((JLabel) tableC.getModel().getValueAt(selectedRow,
									tableC.convertColumnIndexToModel(2))).getText().replace("<html>", "").split("<br>");
							try {
								modelListEnd.removeAllElements();
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							for (int i = 0; i < filterFeature.length - 1; i++)
								if (filterFeature[i].contains(StartPageModified.tfCh3.getText()) == true)
									modelListEnd.addElement(filterFeature[i]);
							filterToolEnd();
							int counter = 0;
							for (int i = 0; i < tablePositive.getRowCount(); i++) {
								tablePositive.getModel().setValueAt(labelsTableC,
										tablePositive.convertRowIndexToModel(i),
										tablePositive.convertColumnIndexToModel(2));
								counter++;
							}
							for (int i = 0; i < tablePositive.getRowCount(); i++)
								tablePositive.getModel().setValueAt(labelsTableC,
										tablePositive.convertRowIndexToModel(i),
										tablePositive.convertColumnIndexToModel(2));

							Object[][] tableData = new Object[tablePositive.getRowCount()][tablePositive
									.getColumnCount()];
							for (int i = 0; i < tablePositive.getRowCount(); i++)
								for (int j = 0; j < tablePositive.getColumnCount(); j++)
									tableData[i][j] = tablePositive.getModel().getValueAt(
											tablePositive.convertRowIndexToModel(i),
											tablePositive.convertColumnIndexToModel(j));

							List<Double> total = new ArrayList<Double>();
							if (tableData.length >= 1) {
								for (int col = 3; col < tableData[col].length; col++) {
									double sum = 0;
									for (int row = 0; row < tableData.length; row++) {
										sum += (double) tableData[row][col];

									}
									total.add(sum / tableData.length);
								}
							}

							resetFilterEnd();
							myFrame.dispatchEvent(new WindowEvent(myFrame, WindowEvent.WINDOW_CLOSING));
							double percentage = Math
									.round(((double) (counter * 100.0) / (double) tablePositive.getRowCount()) * 100.0)
									/ 100.0;
							List<String> rowDataList = new ArrayList<String>();
							for (int i = 0; i < total.size(); i++)
								rowDataList.add(String.valueOf(Math.round(total.get(i) * 100.0) / 100.0));
							rowDataList.add(labelsTableC.getText());
							rowDataList.add(String.valueOf(counter));
							rowDataList.add(String.valueOf(percentage));
							Object[] rowData = new Object[rowDataList.size()];
							rowDataList.toArray(rowData);
							modelSPositive.addRow(rowData);
							for (int i = 3; i < modelSPositive.getColumnCount(); i++)
								modelSPositive.setValueAt(String.valueOf(Math.round(total.get(i - 3) * 100.0) / 100.0),
										modelSPositive.getRowCount() - 1, tableSPositive.convertColumnIndexToModel(i));
							modelSPositive.setValueAt(labelsTableC.getText(), modelSPositive.getRowCount() - 1,
									tableSPositive.convertColumnIndexToModel(0));
							modelSPositive.setValueAt(String.valueOf(counter), modelSPositive.getRowCount() - 1,
									tableSPositive.convertColumnIndexToModel(1));
							modelSPositive.setValueAt(String.valueOf(percentage), modelSPositive.getRowCount() - 1,
									tableSPositive.convertColumnIndexToModel(2));

						}

						if (listClasses.contains(
								((JLabel) tableC.getModel().getValueAt(tableC.convertRowIndexToModel(selectedRow),
										tableC.convertColumnIndexToModel(0))).getText()) == true)
							myFrame.dispatchEvent(new WindowEvent(myFrame, WindowEvent.WINDOW_CLOSING));

					}

				}

				comboClassAction();

			}

		});

		cancelButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				myFrame.dispatchEvent(new WindowEvent(myFrame, WindowEvent.WINDOW_CLOSING));
			}

		});

		deleteButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Object labelC = null;
				Object colorC = null;
				Object[] labelsC = null;
				Object[] colorsC = null;
				int[] indexesRowC = tableC.getSelectedRows();
				int indexRowC = tableC.getSelectedRow();
				if (tableC.getSelectedRowCount() == 1) {
					labelC = new Object();
					colorC = new Object();
					labelC = (JLabel) modelC.getValueAt(tableC.convertRowIndexToModel(indexRowC),
							tableC.convertColumnIndexToModel(0));
					colorC = (JLabel) modelC.getValueAt(tableC.convertRowIndexToModel(indexRowC),
							tableC.convertColumnIndexToModel(1));
				}
				labelsC = new Object[indexesRowC.length];
				colorsC = new Object[indexesRowC.length];
				if (tableC.getSelectedRowCount() > 1) {

					for (int k = 0; k < indexesRowC.length; k++) {
						labelsC[k] = (JLabel) modelC.getValueAt(tableC.convertRowIndexToModel(indexesRowC[k]),
								tableC.convertColumnIndexToModel(0));
						colorsC[k] = (JLabel) modelC.getValueAt(tableC.convertRowIndexToModel(indexesRowC[k]),
								tableC.convertColumnIndexToModel(1));
					}
				}

				java.util.Locale.setDefault(java.util.Locale.ENGLISH);
				JOptionPane.setDefaultLocale(java.util.Locale.ENGLISH.getDefault());
				if (tableC.getSelectedRowCount() > 1) {
					String labelsCtoString[] = new String[indexesRowC.length];
					for (int k = 0; k < indexesRowC.length; k++)
						labelsCtoString[k] = (((JLabel) labelsC[k]).getText());
					input = JOptionPane.showConfirmDialog(null, "Are you sure to delete the selected labels?",
							"Delete a label", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);

					if (input == JOptionPane.YES_OPTION) {
						for (int f = 0; f < indexesRowC.length; f++)
							modelC.removeRow(indexesRowC[f] - f);
						modelC.fireTableDataChanged();
						tableC.repaint();

					}
					if (input == JOptionPane.NO_OPTION)
						return;
					if (input == JOptionPane.CANCEL_OPTION)
						return;

				}
				if (tableC.getSelectedRowCount() == 1) {
					String labelCtoString = (((JLabel) labelC).getText());
					input = JOptionPane.showConfirmDialog(null,
							"Are you sure to delete the following label?----- " + labelCtoString, "Delete a label",
							JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);

					if (input == JOptionPane.YES_OPTION) {
						modelC.removeRow(indexRowC);
						modelC.fireTableDataChanged();

					}
					if (input == JOptionPane.NO_OPTION)
						return;
					if (input == JOptionPane.CANCEL_OPTION)
						return;

				}

			}

		});

	}

	public void comboClassAction() {

		List<String> ch2Initial = new ArrayList<String>();
		List<String> ch3Initial = new ArrayList<String>();
		for (int i = 0; i < MiddlePageModified.tablePositive.getRowCount(); i++) {
			ch2Initial.add(((JLabel) MiddlePageModified.tablePositive.getModel().getValueAt(
					MiddlePageModified.tablePositive.convertRowIndexToModel(i),
					MiddlePageModified.tablePositive.convertColumnIndexToModel(2))).getText());
			ch3Initial.add(((JLabel) EndPageModified.tablePositive.getModel().getValueAt(
					EndPageModified.tablePositive.convertRowIndexToModel(i),
					EndPageModified.tablePositive.convertColumnIndexToModel(2))).getText());
		}
		ch2List.add(ch2Initial);
		ch3List.add(ch3Initial);
	}

	public void filterToolMiddle() {

		if (MiddlePageModified.filterList.getModel().getSize() >= 1) {
			List<String> listFiltersName = new ArrayList<String>();
			List<String> listFiltersMin = new ArrayList<String>();
			List<String> listFiltersMax = new ArrayList<String>();
			for (int i = 0; i < MiddlePageModified.filterList.getModel().getSize(); i++) {
				listFiltersName.add(String.valueOf(MiddlePageModified.filterList.getModel().getElementAt(i).substring(0,
						MiddlePageModified.filterList.getModel().getElementAt(i).lastIndexOf(":"))));
				listFiltersMin.add(String
						.valueOf(MiddlePageModified.filterList.getModel().getElementAt(i).substring(
								MiddlePageModified.filterList.getModel().getElementAt(i).lastIndexOf("["),
								MiddlePageModified.filterList.getModel().getElementAt(i).lastIndexOf(",")))
						.replace("[", ""));
				listFiltersMax.add(String
						.valueOf(MiddlePageModified.filterList.getModel().getElementAt(i).substring(
								MiddlePageModified.filterList.getModel().getElementAt(i).lastIndexOf(","),
								MiddlePageModified.filterList.getModel().getElementAt(i).lastIndexOf("]")))
						.replace(",", ""));
			}

			DefaultComboBoxModel<String> modelComboFilters = (DefaultComboBoxModel<String>) MiddlePageModified.comboFilters
					.getModel();
			List<Integer> indexesComboFilters = new ArrayList<Integer>();
			for (int i = 0; i < listFiltersName.size(); i++)
				indexesComboFilters.add(modelComboFilters.getIndexOf(listFiltersName.get(i)));

			TableRowSorter rowSorterR = new TableRowSorter<>(MiddlePageModified.tablePositive.getModel());
			MiddlePageModified.tablePositive.setRowSorter(rowSorterR);
			List<RowFilter<TableModel, Integer>> low = new ArrayList<>();
			List<RowFilter<TableModel, Integer>> high = new ArrayList<>();
			for (int i = 0; i < indexesComboFilters.size(); i++) {
				low.add(RowFilter.numberFilter(RowFilter.ComparisonType.AFTER,
						Double.parseDouble(listFiltersMin.get(i)),
						MiddlePageModified.tablePositive.convertColumnIndexToModel(indexesComboFilters.get(i) + 3)));
				high.add(RowFilter.numberFilter(RowFilter.ComparisonType.BEFORE,
						Double.parseDouble(listFiltersMax.get(i)),
						MiddlePageModified.tablePositive.convertColumnIndexToModel(indexesComboFilters.get(i) + 3)));
			}

			List<RowFilter<TableModel, Integer>> listOfFilters = new ArrayList<>();
			for (int i = 0; i < low.size(); i++)
				listOfFilters.add(low.get(i));
			for (int i = 0; i < high.size(); i++)
				listOfFilters.add(high.get(i));

			final RowFilter<TableModel, Integer> filter = RowFilter.andFilter(listOfFilters);

			for (int i = 0; i < indexesComboFilters.size(); i++)
				if (Double.parseDouble(listFiltersMin.get(i)) == 0 && Double.parseDouble(listFiltersMax.get(i)) == 0) {
					rowSorterR.setRowFilter(null);
				} else {
					try {
						rowSorterR.setRowFilter(filter);
					} catch (PatternSyntaxException pse) {
						System.out.println("Bad regex pattern");
					}
				}

		}

	}

	public void resetFilterMiddle() {

		TableRowSorter<TableModel> rowReset = new TableRowSorter<>(MiddlePageModified.modelPositive);
		MiddlePageModified.tablePositive.setRowSorter(rowReset);
	}

	public void filterToolEnd() {

		if (filterListEnd.getModel().getSize() >= 1) {

			List<String> listFiltersName = new ArrayList<String>();
			List<String> listFiltersMin = new ArrayList<String>();
			List<String> listFiltersMax = new ArrayList<String>();
			for (int i = 0; i < filterListEnd.getModel().getSize(); i++) {
				if (filterListEnd.getModel().getElementAt(i).contains("Foci/Nuc") == Boolean.TRUE)
					((DefaultListModel) filterListEnd.getModel()).removeElementAt(i);
				listFiltersName.add(String.valueOf(filterListEnd.getModel().getElementAt(i).substring(0,
						filterListEnd.getModel().getElementAt(i).lastIndexOf(":"))));
				listFiltersMin.add(String.valueOf(filterListEnd.getModel().getElementAt(i).substring(
						filterListEnd.getModel().getElementAt(i).lastIndexOf("["),
						filterListEnd.getModel().getElementAt(i).lastIndexOf(","))).replace("[", ""));
				listFiltersMax.add(String.valueOf(filterListEnd.getModel().getElementAt(i).substring(
						filterListEnd.getModel().getElementAt(i).lastIndexOf(","),
						filterListEnd.getModel().getElementAt(i).lastIndexOf("]"))).replace(",", ""));
			}

			DefaultComboBoxModel<String> modelComboFilters = (DefaultComboBoxModel<String>) comboFilters.getModel();
			List<Integer> indexesComboFilters = new ArrayList<Integer>();
			for (int i = 0; i < listFiltersName.size(); i++)
				indexesComboFilters.add(modelComboFilters.getIndexOf(listFiltersName.get(i)));

			TableRowSorter rowSorterR = new TableRowSorter<>(tablePositive.getModel());
			tablePositive.setRowSorter(rowSorterR);
			List<RowFilter<TableModel, Integer>> low = new ArrayList<>();
			List<RowFilter<TableModel, Integer>> high = new ArrayList<>();
			for (int i = 0; i < indexesComboFilters.size(); i++) {
				low.add(RowFilter.numberFilter(RowFilter.ComparisonType.AFTER,
						Double.parseDouble(listFiltersMin.get(i)),
						tablePositive.convertColumnIndexToModel(indexesComboFilters.get(i) + 3)));
				high.add(RowFilter.numberFilter(RowFilter.ComparisonType.BEFORE,
						Double.parseDouble(listFiltersMax.get(i)),
						tablePositive.convertColumnIndexToModel(indexesComboFilters.get(i) + 3)));
			}

			List<RowFilter<TableModel, Integer>> listOfFilters = new ArrayList<>();
			for (int i = 0; i < low.size(); i++)
				listOfFilters.add(low.get(i));
			for (int i = 0; i < high.size(); i++)
				listOfFilters.add(high.get(i));

			final RowFilter<TableModel, Integer> filter = RowFilter.andFilter(listOfFilters);

			for (int i = 0; i < indexesComboFilters.size(); i++)
				if (Double.parseDouble(listFiltersMin.get(i)) == 0 && Double.parseDouble(listFiltersMax.get(i)) == 0) {
					rowSorterR.setRowFilter(null);
				} else {
					try {
						rowSorterR.setRowFilter(filter);
					} catch (PatternSyntaxException pse) {
						System.out.println("Bad regex pattern");
					}
				}

		}

	}

	public void resetFilterEnd() {

		TableRowSorter<TableModel> rowReset = new TableRowSorter<>(modelPositive);
		tablePositive.setRowSorter(rowReset);
	}

	public Object getCellEditorValueAdd() {
		return currentColorAdd;
	}

	public Object getCellEditorValueEdit() {
		return currentColorEdit;
	}

	public Component getTableCellEditorComponentAdd(JTable table, Object value, boolean isSelected, int row,
			int column) {
		currentColorAdd = (Color) value;
		return colorButtonAdd;
	}

	public Component getTableCellEditorComponentEdit(JTable table, Object value, boolean isSelected, int row,
			int column) {
		currentColorEdit = (Color) value;
		return colorButtonEdit;
	}

	@Override
	public Component getTableCellEditorComponent(JTable arg0, Object arg1, boolean arg2, int arg3, int arg4) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getCellEditorValue() {
		// TODO Auto-generated method stub
		return null;
	}

}