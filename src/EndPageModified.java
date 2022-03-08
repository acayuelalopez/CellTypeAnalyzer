import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.RowFilter;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.IntervalMarker;

import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.Roi;
import ij.measure.ResultsTable;
import ij.plugin.ImageCalculator;
import ij.plugin.RoiEnlarger;
import ij.plugin.filter.Analyzer;
import ij.plugin.frame.RoiManager;
import ij.process.ImageProcessor;

@SuppressWarnings("serial")
public class EndPageModified extends AbstractWizardPage {

	AbstractWizardPage nextPage = new MiddlePageModified2();
	static String title, morpho, labelCh2, morphoAction;
	static JLabel channelLabel, iconImage, sumLabel, filterLabel, labelReset;
	private JSpinner filterMorpho, filterMin, filterMax;
	static RoiManager roiManager;
	private ImagePlus channelRoot, channelAnal;
	static JTable tablePositive, tableSPositive, tableCMiddle;
	static DefaultTableModel modelPositive, modelSPositive;
	static Roi[] rois, roisReset;
	static int selectedIndexCh1, selectedIndexCh3, numCh3Positive;
	static ImagePlus impMontage;
	private HistogramFilter hs2;
	private IntervalMarker intervalMarker;
	private ChartPanel histogram;
	private JComboBox<String> comboFilters, comboSumParam;;
	static DefaultListModel<String> modelList, modelListClass;
	static JList<String> filterList, classList;
	private Double[][] data;
	private double max, min;
	private TableRowSorter<TableModel> rowSorterR;
	private List<Roi> roisEnlargered;
	static List<String> roisName;
	public ColorEditorEnd colorEditor;

	public EndPageModified() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		channelLabel = new JLabel();
		channelLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 11));
		channelLabel.setBorder(BorderFactory.createTitledBorder(""));
		iconImage = new JLabel();
		iconImage.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(),
				BorderFactory.createLoweredBevelBorder()));
		JPanel channelPanel = new JPanel();
		channelPanel.setLayout(new BoxLayout(channelPanel, BoxLayout.Y_AXIS));
		channelPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		channelPanel.add(iconImage);
		channelPanel.add(Box.createVerticalStrut(8));
		channelPanel.add(channelLabel);

		JPanel morphoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton morphoOkButton = new JButton("");
		ImageIcon iconMorphoOk = JFrameWizard.createImageIcon("images/morphoOk.png");
		Icon morphoOkCell = new ImageIcon(iconMorphoOk.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
		morphoOkButton.setIcon(morphoOkCell);
		morphoOkButton.setToolTipText("Click this button to erode/dilate rois.");
		JToggleButton zoomButton = new JToggleButton("");
		ImageIcon iconZoom = JFrameWizard.createImageIcon("images/zoom.png");
		Icon zoomCell = new ImageIcon(iconZoom.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
		zoomButton.setIcon(zoomCell);
		zoomButton.setToolTipText("Click this button to zoom +/-.");
		JToggleButton showLabelsButton = new JToggleButton("");
		ImageIcon iconShowLab = JFrameWizard.createImageIcon("images/showlabels.png");
		Icon showLabCell = new ImageIcon(iconShowLab.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
		showLabelsButton.setIcon(showLabCell);
		showLabelsButton.setToolTipText("Click this button to display cell labels.");
		JToggleButton colorRoiButton = new JToggleButton("");
		ImageIcon iconColorRoi = JFrameWizard.createImageIcon("images/paint.png");
		Icon colorRoiCell = new ImageIcon(iconColorRoi.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
		colorRoiButton.setIcon(colorRoiCell);
		colorRoiButton.setToolTipText("Click this button to color roi depending on class belongs.");

		JPanel filtersMorpho = new JPanel(new FlowLayout(FlowLayout.LEFT));
		filterMorpho = new JSpinner(new SpinnerNumberModel(0, 0, 5000, 1));
		filterMorpho.setPreferredSize(new Dimension(60, 20));
		JSlider sliderMorpho = new JSlider(0, 25, 10);
		sliderMorpho.setPreferredSize(new Dimension(150, 15));
		sliderMorpho.setValue(0);
		JLabel filterMorphoLabel = new JLabel(" +/- :  ");
		filtersMorpho.add(filterMorphoLabel);
		filtersMorpho.add(sliderMorpho);
		filtersMorpho.add(Box.createHorizontalStrut(2));
		filtersMorpho.add(filterMorpho);
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		buttonsPanel.add(morphoOkButton);
		buttonsPanel.add(zoomButton);
		buttonsPanel.add(showLabelsButton);
		buttonsPanel.add(colorRoiButton);

		JRadioButton Erosion = new JRadioButton(" Erosion", true);
		JRadioButton Dilation = new JRadioButton(" Dilation");
		JPanel panelED = new JPanel(new FlowLayout(FlowLayout.LEFT));
		ButtonGroup bgroup = new ButtonGroup();
		bgroup.add(Erosion);
		bgroup.add(Dilation);
		panelED.add(Erosion);
		panelED.add(Dilation);
		JPanel filtersMain = new JPanel();
		filtersMain.setLayout(new BoxLayout(filtersMain, BoxLayout.Y_AXIS));
		filtersMorpho.add(Box.createVerticalStrut(2));
		filtersMain.add(panelED);
		filtersMain.add(filtersMorpho);
		filtersMain.add(buttonsPanel);

		morphoPanel.add(filtersMain);

		JPanel mainTopPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		mainTopPanel.add(channelPanel);
		mainTopPanel.add(morphoPanel);
		nextPage.add(mainTopPanel);

		tablePositive = new JTable();
		tableSPositive = new JTable();
		modelPositive = new DefaultTableModel();
		modelSPositive = new DefaultTableModel();
		tablePositive.setModel(modelPositive);
		tableSPositive.setModel(modelSPositive);
		JScrollPane jScrollPanePositive = new JScrollPane(tablePositive);
		jScrollPanePositive.setMaximumSize(new Dimension(1000, 270));
		jScrollPanePositive.setPreferredSize(new Dimension(0, 270));

		nextPage.add(jScrollPanePositive);
		nextPage.add(Box.createVerticalStrut(5));
		sumLabel = new JLabel();
		sumLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 11));
		sumLabel.setBorder(BorderFactory.createTitledBorder(""));
		JPanel sumPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		sumPanel.add(sumLabel);
		comboSumParam = new JComboBox<String>();
		for (int i = 0; i < StartPageModified.itemFilters.length; i++)
			comboSumParam.addItem(StartPageModified.itemFilters[i]);
		JPanel sumParamPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		sumParamPanel.add(comboSumParam);
		sumPanel.add(sumParamPanel);
		nextPage.add(sumPanel);
		nextPage.add(Box.createVerticalStrut(5));
		JScrollPane jScrollPaneSPositive = new JScrollPane(tableSPositive);
		jScrollPaneSPositive.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jScrollPaneSPositive.setMaximumSize(new Dimension(1000, 83));
		jScrollPaneSPositive.setPreferredSize(new Dimension(0, 83));
		nextPage.add(jScrollPaneSPositive);
		nextPage.add(Box.createVerticalStrut(8));
		filterLabel = new JLabel();
		filterLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 11));
		filterLabel.setBorder(BorderFactory.createTitledBorder(""));
		JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		filterPanel.add(filterLabel);
		JPanel filtersMin = new JPanel(new FlowLayout(FlowLayout.CENTER));
		filterMin = new JSpinner(new SpinnerNumberModel(30, 0, 5000, 1));
		filterMin.setPreferredSize(new Dimension(60, 20));
		JSlider sliderMin = new JSlider(0, 300, 50);
		sliderMin.setPreferredSize(new Dimension(150, 15));
		JLabel filterMinLabel = new JLabel(" Min :  ");
		filtersMin.add(filterMinLabel);
		filtersMin.add(sliderMin);
		filtersMin.add(Box.createHorizontalStrut(2));
		filtersMin.add(filterMin);

		JPanel filtersMax = new JPanel(new FlowLayout(FlowLayout.CENTER));
		filterMax = new JSpinner(new SpinnerNumberModel(200, 0, 5000, 1));
		filterMax.setPreferredSize(new Dimension(60, 20));
		JSlider sliderMax = new JSlider(0, 300, 150);
		sliderMax.setPreferredSize(new Dimension(150, 15));
		JLabel filterMaxLabel = new JLabel(" Max :  ");
		filtersMax.add(filterMaxLabel);
		filtersMax.add(sliderMax);
		filtersMax.add(Box.createHorizontalStrut(2));
		filtersMax.add(filterMax);

		JPanel boxPanel2 = new JPanel();
		boxPanel2.setLayout(new BoxLayout(boxPanel2, BoxLayout.Y_AXIS));
		JPanel chartPanel2 = new JPanel();
		hs2 = new HistogramFilter();
		intervalMarker = new IntervalMarker(0, 0, new Color(229, 255, 204), new BasicStroke(), new Color(0, 102, 0),
				new BasicStroke(1.5f), 0.5f);
		histogram = hs2.createChartPanel("Area", new double[] { 0.0, 0.0, 0.0 }, 100, intervalMarker);
		chartPanel2.add(histogram);
		boxPanel2.add(chartPanel2);
		JPanel controlPanel2 = hs2.createControlPanel();
		boxPanel2.add(controlPanel2);

		JPanel filtersMain2 = new JPanel();
		filtersMain2.setLayout(new BoxLayout(filtersMain2, BoxLayout.Y_AXIS));
		filtersMain2.add(filterPanel);
		filtersMain2.add(boxPanel2);
		filtersMain2.add(filtersMin);
		filtersMain2.add(filtersMax);

		JLabel filterFeatureL = new JLabel(" Cell-Features :  ");
		String itemFilters[] = new String[] { "Area", "Mean", "StdDev", "Mode", "Min", "Max", "X", "Y", "XM", "YM",
				"Perim.", "BX", "BY", "Width", "Height", "Major", "Minor", "Angle", "Circ.", "Ferret", "IntDen",
				"Median", "Skew", "Kurt", "%Area", "RawIntDen", "Slice", "FeretX", "FeretY", "FeretAngle", "MinFeret",
				"AR", "Round", "Solidity", "MinThr", "MaxThr" };

		comboFilters = new JComboBox<String>();
		for (int i = 0; i < itemFilters.length; i++)
			comboFilters.addItem(itemFilters[i]);
		comboFilters.setSelectedIndex(0);
		comboFilters.setOpaque(true);
		comboFilters.setPreferredSize(new Dimension(80, 25));
		ColorEditorEnd.comboFilters = comboFilters;
		JButton btnClasses = new JButton();
		ImageIcon iconClasses = JFrameWizard.createImageIcon("images/classes.png");
		Icon classesCell = new ImageIcon(iconClasses.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
		btnClasses.setIcon(classesCell);
		btnClasses.setToolTipText("Click this button to assign a class.");
		btnClasses.setActionCommand("Assign Class");
		JButton btnAdd = new JButton();
		ImageIcon iconAdd = JFrameWizard.createImageIcon("images/plus.png");
		Icon addCell = new ImageIcon(iconAdd.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
		btnAdd.setIcon(addCell);
		btnAdd.setToolTipText("Click this button to add a filter to list.");
		JButton btnRem = new JButton();
		ImageIcon iconRem = JFrameWizard.createImageIcon("images/minus.png");
		Icon remCell = new ImageIcon(iconRem.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
		btnRem.setIcon(remCell);
		btnRem.setToolTipText("Click this button to remove a filter from list.");
		modelList = new DefaultListModel<String>();
		ColorEditorEnd.modelListEnd = modelList;
		filterList = new JList<String>(modelList);
		colorEditor = new ColorEditorEnd(filterList);
		JPanel boxMPButtons = new JPanel();
		boxMPButtons.setLayout(new BoxLayout(boxMPButtons, BoxLayout.Y_AXIS));
		boxMPButtons.add(btnAdd);
		boxMPButtons.add(btnRem);

		JPanel listButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JScrollPane scrollListFilter = new JScrollPane(filterList);
		Dimension d = filterList.getPreferredSize();
		d.width = 150;
		d.height = 100;
		scrollListFilter.setPreferredSize(d);
		listButtons.add(scrollListFilter);
		listButtons.add(boxMPButtons);
		JPanel filterFeature = new JPanel(new FlowLayout(FlowLayout.LEFT));
		filterFeature.add(filterFeatureL);
		filterFeature.add(comboFilters);
		JPanel boxFilter = new JPanel();
		boxFilter.setLayout(new BoxLayout(boxFilter, BoxLayout.Y_AXIS));
		// JButton filterButton = new JButton("");
		// ImageIcon iconFilter = JFrameWizard.createImageIcon("images/filter.png");
		// Icon filterCell = new ImageIcon(iconFilter.getImage().getScaledInstance(26,
		// 28, Image.SCALE_SMOOTH));
		// filterButton.setIcon(filterCell);
		// filterButton.setToolTipText("Click this button to filter your spot
		// detections.");
		// JButton resetFilterButton = new JButton("");
		// ImageIcon iconResetFilter =
		// JFrameWizard.createImageIcon("images/resetfilter.png");
		// Icon resetFilterCell = new
		// ImageIcon(iconResetFilter.getImage().getScaledInstance(23, 25,
		// Image.SCALE_SMOOTH));
		// resetFilterButton.setIcon(resetFilterCell);
		// resetFilterButton.setToolTipText("Click this button to reset your filtering
		// selection.");
		// boxFilter.add(Box.createVerticalStrut(42));
		boxFilter.add(filterFeature);
		boxFilter.add(Box.createVerticalStrut(1));
		boxFilter.add(listButtons);
		boxFilter.add(Box.createVerticalStrut(3));
		JPanel buttonFilter = new JPanel(new FlowLayout(FlowLayout.CENTER));
		// buttonFilter.add(filterButton);
		// buttonFilter.add(resetFilterButton);
		boxFilter.add(buttonFilter);
		JPanel filterDef = new JPanel(new FlowLayout(FlowLayout.LEFT));
		filterDef.add(filtersMain2);
		filterDef.add(boxFilter);
		modelListClass = new DefaultListModel<String>();
		classList = new JList<String>(modelListClass);
		ColorEditorEnd.classListEnd = classList;
		ColorEditorEnd.modelListClassEnd = modelListClass;
		JButton btnRemClass = new JButton();
		btnRemClass.setIcon(remCell);
		btnRemClass.setToolTipText("Click this button to remove a class from list.");
		JPanel boxButtonClass = new JPanel();
		boxButtonClass.setLayout(new BoxLayout(boxButtonClass, BoxLayout.Y_AXIS));
		boxButtonClass.add(btnClasses);
		boxButtonClass.add(btnRemClass);
		JPanel panelClassFlow = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JScrollPane scrollListClass = new JScrollPane(classList);
		scrollListClass.setPreferredSize(d);
		panelClassFlow.add(scrollListClass);
		panelClassFlow.add(boxButtonClass);
		JPanel panelClassManagerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel classManagerL = new JLabel(" Class Manager:  ");
		panelClassManagerPanel.add(classManagerL);
		JPanel boxClass = new JPanel();
		boxClass.setLayout(new BoxLayout(boxClass, BoxLayout.Y_AXIS));
		boxClass.add(panelClassManagerPanel);
		boxClass.add(Box.createVerticalStrut(5));
		boxClass.add(panelClassFlow);
		boxFilter.add(boxClass);
		JPanel panelDefLists = new JPanel();
		panelDefLists.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelDefLists.add(filterDef);
		nextPage.add(panelDefLists);

		comboFilters.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedName = (String) comboFilters.getSelectedItem();
				int selectedIndex = (int) comboFilters.getSelectedIndex();

				double values[] = new double[data.length];
				for (int r = 0; r < data.length; r++)
					for (int c = 0; c < data[r].length; c++)
						values[r] = data[r][selectedIndex];

				int i;
				max = values[0];
				min = values[0];
				for (i = 1; i < values.length; i++) {
					if (values[i] > max)
						max = values[i];
					if (values[i] < min)
						min = values[i];
				}
				sliderMin.setMinimum((int) min);
				sliderMin.setMaximum((int) max);
				sliderMax.setMinimum((int) min);
				sliderMax.setMaximum((int) max);

				hs2.addHistogramSeries(selectedName, values, (int) max, intervalMarker);

			}
		});

		sliderMorpho.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				filterMorpho.setValue(sliderMorpho.getValue());
			}
		});

		filterMorpho.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				sliderMorpho.setValue((int) filterMorpho.getValue());
			}
		});

		morphoOkButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				morphoAction = "morphoAction";

				if (Dilation.isSelected() == true)
					morphoDilationAction();

				if (Erosion.isSelected() == true)
					morphoErosionAction();
				createMainTable();
			}
		});

		tablePositive.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(final MouseEvent e) {
				int indexSelected = tablePositive.getSelectedRow();
				roiManager = RoiManager.getInstance();
				roiManager.select(indexSelected);

			}
		});
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				List<String> listFilters = new ArrayList<String>();

				if (filterList.getModel().getSize() < 1)
					modelList.addElement((String) comboFilters.getSelectedItem() + ":  [" + filterMin.getValue() + ","
							+ filterMax.getValue() + "]" + "-" + StartPageModified.tfCh3.getText());

				if (filterList.getModel().getSize() >= 1) {
					for (int i = 0; i < filterList.getModel().getSize(); i++)
						listFilters.add(String.valueOf(filterList.getModel().getElementAt(i).substring(0,
								filterList.getModel().getElementAt(i).lastIndexOf(":"))));

					if (listFilters.contains(comboFilters.getSelectedItem().toString()) == false)
						modelList.addElement((String) comboFilters.getSelectedItem() + ":  [" + filterMin.getValue()
								+ "," + filterMax.getValue() + "]" + "-" + StartPageModified.tfCh3.getText());

					if (listFilters.contains(comboFilters.getSelectedItem().toString()) == true)
						return;

				}

			}
		});
		btnRem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					int[] indexes = filterList.getSelectedIndices();
					for (int i = 0; i < indexes.length; i++)
						modelList.remove(indexes[i]);
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});

		btnClasses.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object columnNames[] = { "Name", "Color", "Feature" };
				ColorEditorEnd.modelC.setDataVector(new Object[][] {}, columnNames);
				if (ColorEditorEnd.tableDataEnd != null)
					ColorEditorEnd.modelC.setDataVector(ColorEditorEnd.tableDataEnd, columnNames);
				if (ColorEditorEnd.tableDataEnd == null)
					ColorEditorEnd.modelC.setDataVector(new Object[][] {}, columnNames);
				ColorEditorEnd.tableC.setRowHeight(filterList.getHeight());
				TableColumn column1 = null;
				column1 = ColorEditorEnd.tableC.getColumnModel().getColumn(0); // this was the column which was not
																				// highlighted
				column1.setPreferredWidth(7);
				column1.setCellRenderer(new ResultRendererC());
				TableColumn column2 = null;
				column2 = ColorEditorEnd.tableC.getColumnModel().getColumn(1); // this was the column which was not
																				// highlighted
				column2.setPreferredWidth(5);
				column2.setCellRenderer(new ResultRendererC());
				TableColumn column3 = null;
				column3 = ColorEditorEnd.tableC.getColumnModel().getColumn(2); // this was the column which was not
																				// highlighted
				column3.setPreferredWidth(15);
				column3.setCellRenderer(new ResultRendererC());
				ColorEditorEnd.myFrame.setVisible(true);
				colorEditor.setClassAction();

			}
		});

		btnRemClass.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (classList.getSelectedIndices().length < 1)
					return;
				if (classList.getSelectedIndices().length >= 1) {

					String classSelectedValue = classList.getSelectedValue();
					int[] classSelectedIndex = classList.getSelectedIndices();
					for (int i = 0; i < modelPositive.getRowCount(); i++)
						if (((JLabel) modelPositive.getValueAt(i, tablePositive.convertColumnIndexToModel(2))).getText()
								.equals(classSelectedValue) == true) {
							modelPositive.setValueAt(labelReset, i, tablePositive.convertColumnIndexToModel(2));
						}
					for (int i = 0; i < classSelectedIndex.length; i++)
						modelListClass.removeElementAt(classSelectedIndex[i]);

					for (int i = 0; i < modelSPositive.getRowCount(); i++)
						if (((String) modelSPositive.getValueAt(i, tableSPositive.convertColumnIndexToModel(0)))
								.contains(classSelectedValue) == true)
							modelSPositive.removeRow(i);

					for (int i = 0; i < MiddlePageModified.modelPositive.getRowCount(); i++)
						if (((JLabel) MiddlePageModified.modelPositive.getValueAt(i,
								MiddlePageModified.tablePositive.convertColumnIndexToModel(2))).getText()
										.equals(classSelectedValue) == true) {
							MiddlePageModified.modelPositive.setValueAt(labelReset, i,
									MiddlePageModified.tablePositive.convertColumnIndexToModel(2));
						}
					for (int i = 0; i < classSelectedIndex.length; i++)
						MiddlePageModified.modelListClass.removeElementAt(classSelectedIndex[i]);

					for (int i = 0; i < MiddlePageModified.modelSPositive.getRowCount(); i++)
						if (((String) MiddlePageModified.modelSPositive.getValueAt(i,
								MiddlePageModified.tableSPositive.convertColumnIndexToModel(0)))
										.contains(classSelectedValue) == true)
							MiddlePageModified.modelSPositive.removeRow(i);

				}
			}
		});

		sliderMin.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {

				filterMin.setValue(sliderMin.getValue());
				intervalMarker.setStartValue(sliderMin.getValue());

			}
		});

		filterMin.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				sliderMin.setValue((int) filterMin.getValue());
				intervalMarker.setStartValue((int) filterMin.getValue());

			}
		});

		sliderMax.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				filterMax.setValue(sliderMax.getValue());
				intervalMarker.setEndValue(sliderMax.getValue());
			}
		});

		filterMax.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				sliderMax.setValue((int) filterMax.getValue());
				intervalMarker.setEndValue((int) filterMax.getValue());

			}
		});

		zoomButton.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent itemEvent) {
				int state = itemEvent.getStateChange();
				impMontage.setSlice(1);
				if (state == ItemEvent.SELECTED) {
					IJ.setTool("zoom");

				} else {
					IJ.setTool("hand");

				}
			}
		});
		showLabelsButton.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent itemEvent) {
				int state = itemEvent.getStateChange();
				impMontage.setSlice(3);
				roiManager = RoiManager.getInstance();

				if (state == ItemEvent.SELECTED) {
					roiManager.runCommand(impMontage, "Show All with labels");

				} else {
					roiManager.runCommand(impMontage, "Show All without labels");

				}
			}
		});
		colorRoiButton.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent itemEvent) {
				int state = itemEvent.getStateChange();

				if (state == ItemEvent.SELECTED) {
					colorRoisAction();

				} else {
					resetColorRoisAction();

				}
			}
		});

	}

	public void colorRoisAction() {
		List<String> roisToColorName = new ArrayList<String>();
		Color[] colorRois = new Color[modelPositive.getRowCount()];
		for (int i = 0; i < tablePositive.getRowCount(); i++)
			colorRois[i] = ((JLabel) tablePositive.getModel().getValueAt(tablePositive.convertRowIndexToModel(i),
					tablePositive.convertColumnIndexToModel(2))).getBackground();

		roiManager = RoiManager.getInstance();
		Roi[] roisToColor = roiManager.getRoisAsArray();
		for (int i = 0; i < roiManager.getCount(); i++)
			roisToColorName.add(roiManager.getName(i));
		for (int i = 0; i < roisToColor.length; i++)
			roisToColor[i].setStrokeColor(colorRois[i]);

		roiManager.reset();
		for (int i = 0; i < roisToColor.length; i++)
			roiManager.addRoi(roisToColor[i]);
		for (int i = 0; i < roiManager.getCount(); i++)
			roiManager.rename(i, roisToColorName.get(i));
		roiManager.runCommand("UseNames", "true");
		roiManager.runCommand("Show All without labels");

	}

	public void resetColorRoisAction() {
		List<String> roisToColorName = new ArrayList<String>();
		roiManager = RoiManager.getInstance();
		Roi[] roisToColor = roiManager.getRoisAsArray();
		for (int i = 0; i < roiManager.getCount(); i++)
			roisToColorName.add(roiManager.getName(i));
		for (int i = 0; i < roisToColor.length; i++)
			roisToColor[i].setStrokeColor(Color.YELLOW);

		roiManager.reset();
		for (int i = 0; i < roisToColor.length; i++)
			roiManager.addRoi(roisToColor[i]);
		for (int i = 0; i < roiManager.getCount(); i++)
			roiManager.rename(i, roisToColorName.get(i));
		roiManager.runCommand("UseNames", "true");
		roiManager.runCommand("Show All without labels");
	}

	public void morphoErosionAction() {

		roiManager = RoiManager.getInstance();
		roiManager.reset();
		roisEnlargered = new ArrayList<Roi>();
		for (int i = 0; i < rois.length; i++) {
			roisEnlargered.add(RoiEnlarger.enlarge(rois[i], (~((int) filterMorpho.getValue() - 1))));
			roiManager.addRoi(roisEnlargered.get(i));
			roiManager.rename(i, roisName.get(i));
		}

	}

	public void morphoDilationAction() {

		roiManager = RoiManager.getInstance();
		roiManager.reset();
		roisEnlargered = new ArrayList<Roi>();
		for (int i = 0; i < rois.length; i++) {
			roisEnlargered.add(RoiEnlarger.enlarge(rois[i], (int) filterMorpho.getValue()));
			roiManager.addRoi(roisEnlargered.get(i));
			roiManager.rename(i, roisName.get(i));
		}

	}

	public void createMainTable() {

		roiManager = RoiManager.getInstance();
		channelRoot = StartPageModified.channels[StartPageModified.selectedIndexCh1].duplicate();
		channelAnal = StartPageModified.channels[selectedIndexCh3].duplicate();
		roiManager.runCommand(StartPageModified.channels[StartPageModified.comboCh3.getSelectedIndex()],
				"Show All without labels");

		Analyzer aSys = new Analyzer(StartPageModified.channels[StartPageModified.comboCh3.getSelectedIndex()]); // System
		// Analyzer
		ResultsTable rtSys = Analyzer.getResultsTable();
		ResultsTable rtMulti = new ResultsTable(rois.length - 1);
		rtMulti.showRowNumbers(true);
		rtSys.reset();

		rtMulti.incrementCounter();
		int roiIndex = 0;
		for (int i = 0; i < rois.length; i++) {
			StartPageModified.channels[StartPageModified.comboCh3.getSelectedIndex()].setRoi(rois[i]);
			roiIndex++;
			aSys.measure();
			for (int j = 0; j <= rtSys.getLastColumn(); j++) {
				float[] col = rtSys.getColumn(j);
				String head = rtSys.getColumnHeading(j);
				String suffix = "" + roiIndex;
				Roi roi = StartPageModified.channels[StartPageModified.comboCh3.getSelectedIndex()].getRoi();
				if (roi != null) {
					String name = roi.getName();
					if (name != null && name.length() > 0 && (name.length() < 9 || !Character.isDigit(name.charAt(0))))
						suffix = "(" + name + ")";
				}
				if (head != null && col != null && !head.equals("Slice"))
					rtMulti.setValue(head, i, rtSys.getValue(j, rtSys.getCounter() - 1));
			}
		}

		ResultsTable rt = rtMulti;
		

		int[] rmIndexes = roiManager.getIndexes();
		ImageProcessor[] imageArray = new ImageProcessor[rmIndexes.length];
		ImagePlus[] impbChannel = new ImagePlus[rmIndexes.length];
		ImagePlus[] impbs = new ImagePlus[rmIndexes.length];
		for (int u = 0; u < rmIndexes.length; u++) {
			impbChannel[u] = StartPageModified.arrayOfImages[0];
			imageArray[u] = impbChannel[u].getProcessor();
			imageArray[u].setRoi(rois[u]);
			impbs[u] = new ImagePlus(StartPageModified.arrayOfImages[0].getTitle(), imageArray[rmIndexes[u]].crop());

		}


		String[] columnHeaders = rt.getHeadings();
		List<String> columnHeadersS = new ArrayList<String>();
		for (int r = 1; r < columnHeaders.length; r++)
			columnHeadersS.add(columnHeaders[r]);
		String[] columnHeadersSS = new String[columnHeaders.length - 1];
		columnHeadersS.toArray(columnHeadersSS);

		int rows = rt.size();
		List<List<String>> dataList = new ArrayList<List<String>>();
		for (int r = 0; r < rows; r++) {
			List<String> strings = new ArrayList<String>();
			for (int c = 0; c < columnHeaders.length; c++) {
				String values = rt.getStringValue(columnHeaders[c], r);
				strings.add(values);

			}
			dataList.add(strings);
		}
		data = new Double[dataList.size()][];
		for (int i = 0; i < data.length; i++)
			data[i] = new Double[dataList.get(i).size()];

		for (int i = 0; i < dataList.size(); i++)
			for (int j = 0; j < dataList.get(i).size(); j++)
				data[i][j] = Double.parseDouble(dataList.get(i).get(j));

		if (WindowManager.getFrame("Results") != null) {
			IJ.selectWindow("Results");
			IJ.run("Close");
		}

		modelPositive = new DefaultTableModel(data, columnHeaders) {

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

			public boolean isCellEditable(int row, int col) {
				return false;
			}

		};
		modelSPositive = new DefaultTableModel(new Double[][] { {} }, MiddlePageModified.columnHeadersS) {

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

			public boolean isCellEditable(int row, int col) {
				return false;
			}

		};

		tablePositive.setModel(modelPositive);
		tableSPositive.setModel(modelSPositive);
		modelPositive.addColumn("Single-Cell");
		modelPositive.addColumn("ID");
		modelPositive.addColumn("Cell-Type Label");
		tablePositive.moveColumn(tablePositive.getColumnCount() - 1, 0);
		tablePositive.moveColumn(tablePositive.getColumnCount() - 1, 0);
		tablePositive.moveColumn(tablePositive.getColumnCount() - 1, 0);
		tablePositive.setSelectionBackground(new Color(229, 255, 204));
		tablePositive.setSelectionForeground(new Color(0, 102, 0));
		tableSPositive.setSelectionBackground(new Color(229, 255, 204));
		tableSPositive.setSelectionForeground(new Color(0, 102, 0));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		tablePositive.setDefaultRenderer(String.class, centerRenderer);
		tablePositive.setDefaultRenderer(Double.class, centerRenderer);
		tableSPositive.setDefaultRenderer(Double.class, centerRenderer);
		tableSPositive.setDefaultRenderer(String.class, centerRenderer);
		tableSPositive.setDefaultRenderer(Integer.class, centerRenderer);
		tablePositive.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableSPositive.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tablePositive.setRowHeight(40);
		tableSPositive.setRowHeight(40);
		tablePositive.setAutoCreateRowSorter(true);
		tableSPositive.setAutoCreateRowSorter(true);
		tablePositive.getTableHeader().setDefaultRenderer(new SimpleHeaderRenderer());
		tableSPositive.getTableHeader().setDefaultRenderer(new SimpleHeaderRenderer());

		ImageIcon[] iconsPositive = new ImageIcon[modelPositive.getRowCount()];

		for (int i = 0; i < modelPositive.getRowCount(); i++) {
			iconsPositive[i] = new ImageIcon(StartPageModified.getScaledImage(impbs[i].getImage(), 70, 75));
			modelPositive.setValueAt(iconsPositive[i], i, tablePositive.convertColumnIndexToModel(0));
			modelPositive.setValueAt(roisName.get(i), i, tablePositive.convertColumnIndexToModel(1));

		}

		roiManager.runCommand(impMontage, "Show All with labels");
		roiManager.runCommand("UseNames", "true");
		List<Double> ch3YValuesList = new ArrayList<Double>();
		for (int i = 0; i < tablePositive.getRowCount(); i++)
			ch3YValuesList.add((Double) tablePositive.getModel().getValueAt(tablePositive.convertRowIndexToModel(i),
					tablePositive.convertColumnIndexToModel(3)));
		Double[] ch3YValuesArray = new Double[ch3YValuesList.size()];
		ch3YValuesList.toArray(ch3YValuesArray);
		MiddlePageModified2.ch3YValues = ch3YValuesArray;
		// MiddlePageModified2.dataCh3 = data;
		tablePositive.getColumnModel().getColumn(2).setCellRenderer(new Renderer());
		labelReset = new JLabel();
		labelReset.setText("");
		labelReset.setOpaque(true);
		labelReset.setBackground(new Color(214, 217, 223));
		for (int i = 0; i < modelPositive.getRowCount(); i++)
			modelPositive.setValueAt(labelReset, i, tablePositive.convertColumnIndexToModel(2));

		for (int u = 0; u < tablePositive.getColumnCount(); u++)
			tablePositive.getColumnModel().getColumn(u).setPreferredWidth(90);
		tablePositive.getColumnModel().getColumn(0).setPreferredWidth(130);
		tablePositive.getColumnModel().getColumn(1).setPreferredWidth(130);
		tablePositive.getColumnModel().getColumn(2).setPreferredWidth(130);

		List<Double> values = new ArrayList<Double>();
		for (int i = 0; i < tablePositive.getRowCount(); i++)
			values.add((Double) tablePositive.getModel().getValueAt(tablePositive.convertRowIndexToModel(i),
					tablePositive.convertColumnIndexToModel(comboSumParam.getSelectedIndex() + 3)));
		modelSPositive.setValueAt(StartPageModified.tfCh3.getText(), 0, tableSPositive.convertColumnIndexToModel(0));
		modelSPositive.setValueAt(modelPositive.getRowCount(), 0, tableSPositive.convertColumnIndexToModel(1));
		modelSPositive.setValueAt(Math.round(100 * 1000.0) / 1000.0, 0, tableSPositive.convertColumnIndexToModel(2));
		modelSPositive.setValueAt(Math.round(Statistics.sum(values) * 1000.0) / 1000.0, 0,
				tableSPositive.convertColumnIndexToModel(3));
		modelSPositive.setValueAt(Math.round(Statistics.mean(values) * 1000.0) / 1000.0, 0,
				tableSPositive.convertColumnIndexToModel(4));
		modelSPositive.setValueAt(Math.round(Statistics.median(values) * 1000.0) / 1000.0, 0,
				tableSPositive.convertColumnIndexToModel(5));
		modelSPositive.setValueAt(Math.round(Statistics.variance(values) * 1000.0) / 1000.0, 0,
				tableSPositive.convertColumnIndexToModel(6));
		modelSPositive.setValueAt(Math.round(Statistics.sd(values) * 1000.0) / 1000.0, 0,
				tableSPositive.convertColumnIndexToModel(7));
		modelSPositive.setValueAt(Math.round(Collections.min(values) * 1000.0) / 1000.0, 0,
				tableSPositive.convertColumnIndexToModel(8));
		modelSPositive.setValueAt(Math.round(Collections.max(values) * 1000.0) / 1000.0, 0,
				tableSPositive.convertColumnIndexToModel(9));
		modelSPositive.setValueAt(Math.round(Statistics.Q1(values, values.size()) * 1000.0) / 1000.0, 0,
				tableSPositive.convertColumnIndexToModel(10));
		modelSPositive.setValueAt(Math.round(Statistics.Q3(values, values.size()) * 1000.0) / 1000.0, 0,
				tableSPositive.convertColumnIndexToModel(11));
		modelSPositive.setValueAt(Math.round(Statistics.IQR(values, values.size()) * 1000.0) / 1000.0, 0,
				tableSPositive.convertColumnIndexToModel(12));
		for (int u = 0; u < tableSPositive.getColumnCount(); u++)
			tableSPositive.getColumnModel().getColumn(u).setPreferredWidth(90);
		tableSPositive.getColumnModel().getColumn(0).setPreferredWidth(130);

		ColorEditorEnd.tablePositive = tablePositive;
		ColorEditorEnd.modelPositive = modelPositive;
		ColorEditorEnd.tableSPositive = tableSPositive;
		ColorEditorEnd.modelSPositive = modelSPositive;
		

	}

	public void filterTool() {

		if (filterList.getModel().getSize() < 1)
			IJ.error("You should add a filter!!");

		if (filterList.getModel().getSize() >= 1) {
			List<String> listFiltersName = new ArrayList<String>();
			List<String> listFiltersMin = new ArrayList<String>();
			List<String> listFiltersMax = new ArrayList<String>();
			for (int i = 0; i < filterList.getModel().getSize(); i++) {
				listFiltersName.add(String.valueOf(filterList.getModel().getElementAt(i).substring(0,
						filterList.getModel().getElementAt(i).lastIndexOf(":"))));
				listFiltersMin.add(String.valueOf(filterList.getModel().getElementAt(i).substring(
						filterList.getModel().getElementAt(i).lastIndexOf("["),
						filterList.getModel().getElementAt(i).lastIndexOf(","))).replace("[", ""));
				listFiltersMax.add(String.valueOf(filterList.getModel().getElementAt(i).substring(
						filterList.getModel().getElementAt(i).lastIndexOf(","),
						filterList.getModel().getElementAt(i).lastIndexOf("]"))).replace(",", ""));
			}

			DefaultComboBoxModel<String> modelComboFilters = (DefaultComboBoxModel<String>) comboFilters.getModel();
			List<Integer> indexesComboFilters = new ArrayList<Integer>();
			for (int i = 0; i < listFiltersName.size(); i++)
				indexesComboFilters.add(modelComboFilters.getIndexOf(listFiltersName.get(i)));

			rowSorterR = new TableRowSorter<>(tablePositive.getModel());
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

			List<String> roisNameRM = new ArrayList<String>();
			List<String> roisNameTable = new ArrayList<String>();
			List<String> roisNameTableIndex = new ArrayList<String>();
			for (int i = 0; i < rois.length; i++)
				roisNameRM.add(rois[i].getName());
			roiManager = RoiManager.getInstance();
			roiManager.reset();

			for (int i = 0; i < tablePositive.getRowCount(); i++) {
				roisNameTable.add((String) tablePositive.getModel().getValueAt(tablePositive.convertRowIndexToModel(i),
						tablePositive.convertColumnIndexToModel(1)));
				roisNameTableIndex.add(roisNameTable.get(i).substring(0, 4));

			}
			for (int i = 0; i < roisNameTableIndex.size(); i++)
				roiManager.addRoi(roisEnlargered.get(Integer.parseInt(roisNameTableIndex.get(i)) - 1));

			int nRow = tablePositive.getRowCount(), nCol = tablePositive.getColumnCount();
			Object[][] tableData = new Object[nRow][nCol];
			for (int i = 0; i < tablePositive.getRowCount(); i++)
				for (int j = 0; j < tablePositive.getColumnCount(); j++)
					tableData[i][j] = tablePositive.getModel().getValueAt(tablePositive.convertRowIndexToModel(i),
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
				for (int j = 3; j < tableSPositive.getColumnCount(); j++)
					modelSPositive.setValueAt(Math.round(total.get(j - 3) * 100.0) / 100.0,
							tableSPositive.convertRowIndexToModel(0), tableSPositive.convertColumnIndexToModel(j));
				modelPositive.fireTableDataChanged();
				tablePositive.repaint();
				List<String> meanValuesCh3 = new ArrayList<String>();
				for (int i = 0; i < tablePositive.getRowCount(); i++)
					meanValuesCh3.add((String) tablePositive.getModel()
							.getValueAt(i, tablePositive.convertColumnIndexToModel(3)).toString());

				int counter = 0;
				for (int i = 0; i < meanValuesCh3.size(); i++)
					if (meanValuesCh3.get(i).startsWith("0.") == false)
						numCh3Positive = counter++;

				modelSPositive.setValueAt(numCh3Positive, tableSPositive.convertRowIndexToModel(0),
						tableSPositive.convertColumnIndexToModel(1));
				modelSPositive.setValueAt((double) Math.round(
						((double) numCh3Positive * 100.0 / (double) StartPageModified.roiManager.getCount()) * 100.0)
						/ 100.0, tableSPositive.convertRowIndexToModel(0), tableSPositive.convertColumnIndexToModel(2));
				modelSPositive.setValueAt(Math.subtractExact(StartPageModified.roiManager.getCount(), numCh3Positive),
						tableSPositive.convertRowIndexToModel(0), tableSPositive.convertColumnIndexToModel(3));
				modelSPositive.setValueAt(
						Math.round(((double) Math.subtractExact(StartPageModified.roiManager.getCount(), numCh3Positive)
								* 100.0 / (double) modelPositive.getRowCount()) * 100.0) / 100.0,
						tableSPositive.convertRowIndexToModel(0), tableSPositive.convertColumnIndexToModel(4));

				for (int i = 0; i < roiManager.getCount(); i++)
					roiManager.rename(i, roisNameTable.get(i));

				roiManager.runCommand(impMontage, "Show All with labels");
				roiManager.runCommand("UseNames", "true");

			}

		}

	}

	public void resetFilter() {

		TableRowSorter<TableModel> rowReset = new TableRowSorter<>(modelPositive);
		tablePositive.setRowSorter(rowReset);

		int nRow = tablePositive.getRowCount(), nCol = tablePositive.getColumnCount();
		Object[][] tableDataPositive = new Object[nRow][nCol];
		for (int i = 0; i < tablePositive.getRowCount(); i++)
			for (int j = 0; j < tablePositive.getColumnCount(); j++)
				tableDataPositive[i][j] = tablePositive.getModel().getValueAt(tablePositive.convertRowIndexToModel(i),
						tablePositive.convertColumnIndexToModel(j));

		List<Double> total = new ArrayList<Double>();

		for (int col = 3; col < tableDataPositive[col].length; col++) {
			double sum = 0;
			for (int row = 0; row < tableDataPositive.length; row++) {
				sum += (double) tableDataPositive[row][col];

			}
			total.add(sum / tableDataPositive.length);
		}

		List<String> meanValuesCh3 = new ArrayList<String>();
		for (int i = 0; i < tablePositive.getRowCount(); i++)
			meanValuesCh3.add((String) tablePositive.getModel()
					.getValueAt(i, tablePositive.convertColumnIndexToModel(4)).toString());

		int counter = 0;
		for (int i = 0; i < meanValuesCh3.size(); i++)
			if (meanValuesCh3.get(i).startsWith("0.") == false)
				numCh3Positive = counter++;

		modelSPositive.setValueAt(numCh3Positive, tableSPositive.convertRowIndexToModel(0),
				tableSPositive.convertColumnIndexToModel(1));
		modelSPositive.setValueAt((double) Math
				.round(((double) numCh3Positive * 100.0 / (double) StartPageModified.roiManager.getCount()) * 100.0)
				/ 100.0, tableSPositive.convertRowIndexToModel(0), tableSPositive.convertColumnIndexToModel(2));
		modelSPositive.setValueAt(Math.subtractExact(StartPageModified.roiManager.getCount(), numCh3Positive),
				tableSPositive.convertRowIndexToModel(0), tableSPositive.convertColumnIndexToModel(3));
		modelSPositive.setValueAt(
				Math.round(((double) Math.subtractExact(StartPageModified.roiManager.getCount(), numCh3Positive) * 100.0
						/ (double) modelPositive.getRowCount()) * 100.0) / 100.0,
				tableSPositive.convertRowIndexToModel(0), tableSPositive.convertColumnIndexToModel(4));

		for (int j = 5; j < tableSPositive.getColumnCount(); j++)
			modelSPositive.setValueAt(Math.round(total.get(j - 5) * 100.0) / 100.0,
					tableSPositive.convertRowIndexToModel(0), tableSPositive.convertColumnIndexToModel(j));

		roiManager = RoiManager.getInstance();
		roiManager.reset();
		for (int i = 0; i < roisEnlargered.size(); i++)
			roiManager.addRoi(roisEnlargered.get(i));

		for (int i = 0; i < roiManager.getCount(); i++)
			roiManager.rename(i, roisName.get(i));

		roiManager.runCommand(impMontage, "Show All with labels");
		roiManager.runCommand("UseNames", "true");

	}

	@Override
	protected AbstractWizardPage getNextPage() {
		return nextPage;
	}

	@Override
	protected boolean isExportAllowed() {
		return false;
	}

	@Override
	protected boolean isPrintAllowed() {
		return false;
	}

	@Override
	protected boolean isCancelAllowed() {
		return true;
	}

	@Override
	protected boolean isPreviousAllowed() {
		return true;
	}

	@Override
	protected boolean isNextAllowed() {
		return true;
	}

}