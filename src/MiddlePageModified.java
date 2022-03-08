import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
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
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JViewport;
import javax.swing.RowFilter;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.IntervalMarker;
import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.PointRoi;
import ij.gui.PolygonRoi;
import ij.gui.Roi;
import ij.measure.ResultsTable;
import ij.plugin.ChannelSplitter;
import ij.plugin.ImageCalculator;
import ij.plugin.RoiEnlarger;
import ij.plugin.filter.Analyzer;
import ij.plugin.filter.ExtendedPlugInFilter;
import ij.plugin.filter.MaximumFinder;
import ij.plugin.filter.PlugInFilterRunner;
import ij.plugin.frame.RoiManager;
import ij.process.ImageProcessor;

@SuppressWarnings("serial")
public class MiddlePageModified extends AbstractWizardPage implements ExtendedPlugInFilter {

	AbstractWizardPage nextPage = new EndPageModified();
	static JRadioButton Erosion, Dilation, subThrOptions;
	private ImagePlus[] channels;
	static String title, morpho, labelCh2, morphoAction, filterIsPressed;
	static JLabel channelLabel, iconImage, sumLabel, filterLabel, labelReset;
	static JSpinner filterMorpho, filterMin, filterMax, toleranceSpinner;
	static RoiManager roiManager;
	static JTable tablePositive, tableSPositive;
	static DefaultTableModel modelPositive, modelSPositive;
	static Roi[] rois, roisReset;
	static int selectedIndexCh1, selectedIndexCh2, numCh2Positive;
	static ImagePlus impMontage, channelAnal, channelRoot;
	private HistogramFilter hs2;
	private IntervalMarker intervalMarker;
	private ChartPanel histogram;
	static JComboBox<String> comboFilters, comboSumParam;
	static DefaultListModel<String> modelList, modelListClass;
	static JList<String> filterList, classList;
	private Double[][] data;
	private double max, min;
	static List<String> roisName;
	private List<Roi> roisEnlargered;
	public Double[] countPoints;
	static String[] columnHeadersS = new String[] { "Marker-Label", "N", "%", "Sum", "Mean", "Median", "Variance",
			"stdDev", "Min", "Max", "Q1", "Q3", "IQR" };
	static String itemFilters[] = new String[] { "Area", "Mean", "StdDev", "Mode", "Min", "Max", "X", "Y", "XM", "YM",
			"Perim.", "BX", "BY", "Width", "Height", "Major", "Minor", "Angle", "Circ.", "Ferret", "IntDen", "Median",
			"Skew", "Kurt", "%Area", "RawIntDen", "Slice", "FeretX", "FeretY", "FeretAngle", "MinFeret", "AR", "Round",
			"Solidity", "MinThr", "MaxThr" };

	public MiddlePageModified() {

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		if (StartPageModified.channels != null)
			channels = StartPageModified.channels;

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
		subThrOptions = new JRadioButton(" Bright Spots Analysis", false);
		JButton morphoOkButton = new JButton("");
		toleranceSpinner = new JSpinner(new SpinnerNumberModel(30, 0, 5555555, 1));
		toleranceSpinner.setPreferredSize(new Dimension(60, 20));
		JPanel aMIPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JLabel toleranceLabel = new JLabel("           Tolerance: ");
		aMIPanel.add(toleranceLabel);
		aMIPanel.add(toleranceSpinner);
		JPanel subRoiPanelMain = new JPanel();
		subRoiPanelMain.setLayout(new BoxLayout(subRoiPanelMain, BoxLayout.Y_AXIS));
		JPanel subRoiPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		subRoiPanel1.add(subThrOptions);
		JPanel subRoiPanel3 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		subRoiPanel3.add(aMIPanel);
		subRoiPanelMain.add(subRoiPanel1);
		subRoiPanelMain.add(subRoiPanel3);

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
		JPanel filtersButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
		filterMorpho = new JSpinner(new SpinnerNumberModel(0, 0, 5000, 1));
		filterMorpho.setPreferredSize(new Dimension(60, 20));
		JSlider sliderMorpho = new JSlider(0, 25, 10);
		sliderMorpho.setValue(0);
		sliderMorpho.setPreferredSize(new Dimension(150, 15));
		JLabel filterMorphoLabel = new JLabel(" +/- :  ");
		filtersMorpho.add(filterMorphoLabel);
		filtersMorpho.add(sliderMorpho);
		filtersMorpho.add(Box.createHorizontalStrut(2));
		filtersMorpho.add(filterMorpho);
		filtersButtons.add(morphoOkButton);
		filtersButtons.add(zoomButton);
		filtersButtons.add(showLabelsButton);
		filtersButtons.add(colorRoiButton);
		Erosion = new JRadioButton(" Erosion", true);
		Dilation = new JRadioButton(" Dilation");
		JPanel panelED = new JPanel(new FlowLayout(FlowLayout.LEFT));
		ButtonGroup bgroup = new ButtonGroup();
		bgroup.add(Erosion);
		bgroup.add(Dilation);
		panelED.add(Erosion);
		panelED.add(Dilation);
		JPanel filtersMain = new JPanel();
		filtersMain.setLayout(new BoxLayout(filtersMain, BoxLayout.Y_AXIS));
		// filtersMain.add(morphoLabelP);
		filtersMorpho.add(Box.createVerticalStrut(2));
		filtersMain.add(panelED);
		filtersMain.add(filtersMorpho);
		filtersMain.add(filtersButtons);
		filtersMain.add(subRoiPanelMain);
		morphoPanel.add(filtersMain);
		JPanel mainTopPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		mainTopPanel.add(channelPanel);
		mainTopPanel.add(morphoPanel);
		nextPage.add(mainTopPanel);
		toleranceSpinner.setEnabled(false);
		toleranceLabel.setEnabled(false);
		tablePositive = new JTable();
		tableSPositive = new JTable();
		modelPositive = new DefaultTableModel();
		modelSPositive = new DefaultTableModel();
		tablePositive.setModel(modelPositive);
		tableSPositive.setModel(modelSPositive);
		JScrollPane jScrollPanePositive = new JScrollPane(tablePositive);
		jScrollPanePositive.setPreferredSize(new Dimension(655, 270));
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
		jScrollPaneSPositive.setPreferredSize(new Dimension(655, 83));
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
		comboFilters = new JComboBox<String>();
		for (int i = 0; i < itemFilters.length; i++)
			comboFilters.addItem(itemFilters[i]);
		comboFilters.setSelectedIndex(0);
		comboFilters.setOpaque(true);
		comboFilters.setPreferredSize(new Dimension(80, 25));
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
		filterList = new JList<String>(modelList);
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

		boxFilter.add(filterFeature);
		boxFilter.add(Box.createVerticalStrut(1));
		boxFilter.add(listButtons);
		boxFilter.add(Box.createVerticalStrut(3));
		JPanel buttonFilter = new JPanel(new FlowLayout(FlowLayout.CENTER));
		boxFilter.add(buttonFilter);
		JPanel filterDef = new JPanel(new FlowLayout(FlowLayout.LEFT));
		filterDef.add(filtersMain2);
		filterDef.add(boxFilter);
		modelListClass = new DefaultListModel<String>();
		classList = new JList<String>(modelListClass);
		JPanel panelClassFlow = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JScrollPane scrollListClass = new JScrollPane(classList);
		scrollListClass.setPreferredSize(d);
		panelClassFlow.add(scrollListClass);
		JPanel panelClassManagerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel classManagerL = new JLabel(" Cell-Type Manager:  ");
		panelClassManagerPanel.add(classManagerL);
		JPanel boxClass = new JPanel();
		boxClass.setLayout(new BoxLayout(boxClass, BoxLayout.Y_AXIS));
		boxClass.add(panelClassManagerPanel);
		boxClass.add(Box.createVerticalStrut(5));
		boxClass.add(panelClassFlow);
		boxFilter.add(boxClass);
		JPanel panelDefLists = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelDefLists.add(filterDef);
		nextPage.add(panelDefLists);

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

		subThrOptions.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {

				if (e.getStateChange() == ItemEvent.SELECTED) {
					toleranceSpinner.setEnabled(true);
					toleranceLabel.setEnabled(true);
				}

				if (e.getStateChange() == ItemEvent.DESELECTED) {
					toleranceSpinner.setEnabled(false);
					toleranceLabel.setEnabled(false);
				}

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
							+ filterMax.getValue() + "]" + "-" + StartPageModified.tfCh2.getText());

				if (filterList.getModel().getSize() >= 1) {
					for (int i = 0; i < filterList.getModel().getSize(); i++)
						listFilters.add(String.valueOf(filterList.getModel().getElementAt(i).substring(0,
								filterList.getModel().getElementAt(i).lastIndexOf(":"))));

					if (listFilters.contains(comboFilters.getSelectedItem().toString()) == false)
						modelList.addElement((String) comboFilters.getSelectedItem() + ":  [" + filterMin.getValue()
								+ "," + filterMax.getValue() + "]" + "-" + StartPageModified.tfCh2.getText());

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
				impMontage.setSlice(2);
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
				impMontage.setSlice(1);
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

		comboFilters.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedName = (String) comboFilters.getSelectedItem();
				int selectedIndex = (int) comboFilters.getSelectedIndex();
				double values[] = null;

				if (tablePositive.getColumnName(3).equals("Bright Spots") == Boolean.TRUE) {
					if (data == null)
						IJ.error("You should have added nuclei table before creating filter or classes!! ");
					if (data != null) {
						values = new double[tablePositive.getRowCount()];
						if (tablePositive.getModel().getValueAt(tablePositive.convertRowIndexToModel(0),
								tablePositive.convertColumnIndexToModel(selectedIndex + 3)) != null) {
							for (int r = 0; r < tablePositive.getRowCount(); r++)
								values[r] = (double) tablePositive.getModel().getValueAt(
										tablePositive.convertRowIndexToModel(r),
										tablePositive.convertColumnIndexToModel(selectedIndex + 3));
							int i;
							max = values[0];
							min = values[0];
							for (i = 1; i < values.length; i++) {
								if (values[i] > max)
									max = values[i];
								if (values[i] < min)
									min = values[i];
							}
						}
					}
				}
				if (tablePositive.getColumnName(3).equals("Bright Spots") == Boolean.FALSE) {

					if (data == null)
						IJ.error("You should have added nuclei table before creating filter or classes!! ");
					if (data != null) {
						values = new double[tablePositive.getRowCount()];
						for (int r = 0; r < tablePositive.getRowCount(); r++) {
							values[r] = (double) tablePositive.getModel().getValueAt(
									tablePositive.convertRowIndexToModel(r),
									tablePositive.convertColumnIndexToModel(selectedIndex + 3));
						}
						int i;
						max = values[0];
						min = values[0];
						for (i = 1; i < values.length; i++) {
							if (values[i] > max)
								max = values[i];
							if (values[i] < min)
								min = values[i];
						}
					}
				}
			
					sliderMin.setMinimum((int) min);
					sliderMin.setMaximum((int) max);
					sliderMax.setMinimum((int) min);
					sliderMax.setMaximum((int) max);
					if (values != null && max > 1) {
					hs2.addHistogramSeries(selectedName, values, (int) max, intervalMarker);
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
		channelAnal = StartPageModified.channels[StartPageModified.selectedIndexCh2].duplicate();
		roiManager.runCommand(StartPageModified.channels[StartPageModified.comboCh2.getSelectedIndex()],
				"Show All without labels");

		Analyzer aSys = new Analyzer(StartPageModified.channels[StartPageModified.comboCh2.getSelectedIndex()]); // System
																													// Analyzer
		ResultsTable rtSys = Analyzer.getResultsTable();
		ResultsTable rtMulti = new ResultsTable(rois.length - 1);
		rtMulti.showRowNumbers(true);
		rtSys.reset();

		rtMulti.incrementCounter();
		int roiIndex = 0;
		for (int i = 0; i < rois.length; i++) {
			StartPageModified.channels[StartPageModified.comboCh2.getSelectedIndex()].setRoi(rois[i]);
			roiIndex++;
			aSys.measure();
			for (int j = 0; j <= rtSys.getLastColumn(); j++) {
				float[] col = rtSys.getColumn(j);
				String head = rtSys.getColumnHeading(j);
				String suffix = "" + roiIndex;
				Roi roi = StartPageModified.channels[StartPageModified.comboCh2.getSelectedIndex()].getRoi();
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
		rtMulti.show("Results");

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

		// ResultsTable rt = ResultsTable.getResultsTable();
		String[] columnHeaders = rt.getHeadings();
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
		modelSPositive = new DefaultTableModel(new Double[][] { {} }, columnHeadersS) {

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
		modelPositive.addColumn("Single Cell");
		modelPositive.addColumn("ID");
		modelPositive.addColumn("Cell-Type Label");
		if (subThrOptions.isSelected() == true)
			modelPositive.addColumn("Bright Spots");

		tablePositive.moveColumn(tablePositive.getColumnCount() - 1, 0);
		tablePositive.moveColumn(tablePositive.getColumnCount() - 1, 0);
		tablePositive.moveColumn(tablePositive.getColumnCount() - 1, 0);
		if (subThrOptions.isSelected() == true)
			tablePositive.moveColumn(tablePositive.getColumnCount() - 1, 0);

		if (subThrOptions.isSelected() == Boolean.TRUE) {

			if (comboFilters.getItemAt(0).equals("Bright Spots") == Boolean.FALSE)
				comboFilters.insertItemAt("Bright Spots", 0);
			comboFilters.setSelectedIndex(0);
			if (comboSumParam.getItemAt(0).equals("Bright Spots") == Boolean.FALSE)
				comboSumParam.insertItemAt("Bright Spots", 0);
			comboSumParam.setSelectedIndex(0);

			countPoints = new Double[roiManager.getCount()];
			ImagePlus impToShow = StartPageModified.channels[StartPageModified.comboCh2.getSelectedIndex()].duplicate();
			ImageProcessor[] processorFM = new ImageProcessor[roiManager.getCount()];
			countPoints = new Double[roiManager.getCount()];
			processorFM = new ImageProcessor[roiManager.getCount()];
			for (int i = 0; i < roiManager.getCount(); i++) {
				processorFM[i] = impToShow.getProcessor();
				processorFM[i].setRoi(roisEnlargered.get(i));
				countPoints[i] = (double) new MaximumFinder().getMaxima(processorFM[i].crop(),
						Double.parseDouble(toleranceSpinner.getValue().toString()), false).npoints;

			}

		}

		if (subThrOptions.isSelected() == Boolean.FALSE) {

			if (comboFilters.getItemAt(0).equals("Bright Spots") == Boolean.TRUE)
				comboFilters.removeItemAt(0);
			comboFilters.setSelectedIndex(0);
			if (comboSumParam.getItemAt(0).equals("Bright Spots") == Boolean.TRUE)
				comboSumParam.removeItemAt(0);
			comboSumParam.setSelectedIndex(0);
		}
		ImageIcon[] iconsPositive = new ImageIcon[modelPositive.getRowCount()];
		labelReset = new JLabel();
		labelReset.setText("");
		labelReset.setOpaque(true);
		labelReset.setBackground(new Color(214, 217, 223));

		for (int i = 0; i < modelPositive.getRowCount(); i++) {
			iconsPositive[i] = new ImageIcon(StartPageModified.getScaledImage(impbs[i].getImage(), 70, 75));
			modelPositive.setValueAt(iconsPositive[i], i, tablePositive.convertColumnIndexToModel(0));
			modelPositive.setValueAt(roisName.get(i), i, tablePositive.convertColumnIndexToModel(1));
			modelPositive.setValueAt(labelReset, i, tablePositive.convertColumnIndexToModel(2));
			if (subThrOptions.isSelected() == true)
				modelPositive.setValueAt(countPoints[i], i, tablePositive.convertColumnIndexToModel(3));

		}

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
		List<String> meanValuesCh2 = new ArrayList<String>();
		for (int i = 0; i < tablePositive.getRowCount(); i++)
			meanValuesCh2.add((String) tablePositive.getModel()
					.getValueAt(i, tablePositive.convertColumnIndexToModel(3)).toString());

		int counter = 0;
		for (int i = 0; i < meanValuesCh2.size(); i++) {
			if (meanValuesCh2.get(i).startsWith("0.") == false)
				numCh2Positive = counter++;
		}

		roiManager.runCommand(impMontage, "Show All with labels");
		roiManager.runCommand("UseNames", "true");

		List<Double> ch2XValuesList = new ArrayList<Double>();
		for (int i = 0; i < tablePositive.getRowCount(); i++)
			ch2XValuesList.add((Double) tablePositive.getModel().getValueAt(tablePositive.convertRowIndexToModel(i),
					tablePositive.convertColumnIndexToModel(3)));
		Double[] ch2XValuesArray = new Double[ch2XValuesList.size()];
		ch2XValuesList.toArray(ch2XValuesArray);
		MiddlePageModified2.ch2XValues = ch2XValuesArray;
		MiddlePageModified2.icons = iconsPositive;

		for (int u = 0; u < tablePositive.getColumnCount(); u++)
			tablePositive.getColumnModel().getColumn(u).setPreferredWidth(90);
		tablePositive.getColumnModel().getColumn(0).setPreferredWidth(130);
		tablePositive.getColumnModel().getColumn(1).setPreferredWidth(130);
		tablePositive.getColumnModel().getColumn(2).setPreferredWidth(130);
		tablePositive.getColumnModel().getColumn(2).setCellRenderer(new Renderer());

		List<Double> values = new ArrayList<Double>();
		for (int i = 0; i < tablePositive.getRowCount(); i++)
			values.add((Double) tablePositive.getModel().getValueAt(tablePositive.convertRowIndexToModel(i),
					tablePositive.convertColumnIndexToModel(comboSumParam.getSelectedIndex() + 3)));
		modelSPositive.setValueAt(StartPageModified.tfCh2.getText(), 0, tableSPositive.convertColumnIndexToModel(0));
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

	@Override
	public void run(ImageProcessor arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public int setup(String arg0, ImagePlus arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setNPasses(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public int showDialog(ImagePlus arg0, String arg1, PlugInFilterRunner arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

}