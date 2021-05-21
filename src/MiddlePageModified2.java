import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;

import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.Roi;
import ij.measure.ResultsTable;
import ij.plugin.frame.RoiManager;

@SuppressWarnings("serial")
public class MiddlePageModified2 extends AbstractWizardPage {

	AbstractWizardPage nextPage = new MiddlePageModified3();
	static String labelCh2, labelCh3, selectedClassQuadrant, hasBeenSelected = "";
	static Double[] ch2XValues, ch3YValues, dataCh2, dataCh3;
	double[] valuesRange;
	double[] valuesDomain;
	public double maxDomain, maxRange, minDomain, minRange;
	public IntervalMarker markerRange, markerDomain;
	private JSpinner filterRange, filterDomain, filterOrder;
	public JSlider sliderDomain, sliderRange;
	static Roi[] rois;
	static ImagePlus[] channels;
	static int selectedIndexCh2, selectedIndexCh3, numCh2Positive, numCh3Positive, countSenescentNumber, lhCountAll,
			hhCountAll, llCountAll, hlCountAll, lhCountNID, hhCountNID, llCountNID, hlCountNID, lhCountClass,
			hhCountClass, llCountClass, hlCountClass;
	private RoiManager roiManager;
	static JLabel scatLabel, sumLabel;
	public ChartPanel histogram;
	static JTable tablePositive, tablePositiveSum, tableSPositive, tablePositiveCount, tableCVersion;
	static DefaultTableModel modelPositive, modelPositiveSum, modelSPositive, modelPositiveCount;
	public ScatterPlotChannels sp2;
	static ImageIcon[] icons;
	static JComboBox<String> comboChDomain, comboChRange, comboFeatureDomain, comboFeatureRange, comboClass,
			comboQuadrant, comboPositive, comboRegression;
	static String itemFilters[];
	public Color[] colorArray;
	static JScrollPane jScrollPaneSPositive, jScrollPanePositiveCount;
	static Color selectedColorQuadrant;
	private int counterPositive;
	private JButton okButton, cancelButton;
	static JFrame frame;
	private JPanel totalPanel;

	public MiddlePageModified2() {

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		okButton = new JButton("");
		ImageIcon iconOk = JFrameWizard.createImageIcon("images/ok.png");
		Icon iconOkCell = new ImageIcon(iconOk.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
		okButton.setIcon(iconOkCell);
		cancelButton = new JButton("");
		ImageIcon iconCancel = JFrameWizard.createImageIcon("images/cancel.png");
		Icon iconCancelCell = new ImageIcon(iconCancel.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
		cancelButton.setIcon(iconCancelCell);
		Panel panelOkCancel = new Panel();
		panelOkCancel.setLayout(new FlowLayout(FlowLayout.CENTER));
		panelOkCancel.add(okButton);
		panelOkCancel.add(cancelButton);
		totalPanel = new JPanel();
		totalPanel.setLayout(new BoxLayout(totalPanel, BoxLayout.Y_AXIS));
		tableCVersion = new JTable();
		tableCVersion.getTableHeader().setDefaultRenderer(new SimpleHeaderRenderer());
		tableCVersion.setSelectionBackground(new Color(229, 255, 204));
		tableCVersion.setSelectionForeground(new Color(0, 102, 0));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		tableCVersion.setDefaultRenderer(JLabel.class, centerRenderer);
		tableCVersion.setAutoCreateRowSorter(true);
		tableCVersion.setEnabled(true);
		tableCVersion.setCellSelectionEnabled(true);
		tableCVersion.setColumnSelectionAllowed(false);
		tableCVersion.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tableCVersion.setDefaultRenderer(JLabel.class, new Renderer());
		tableCVersion.setDefaultRenderer(Color.class, new ColorRenderer(true));
		tableCVersion.setRowHeight(25);
		totalPanel.add(new JScrollPane(tableCVersion));
		totalPanel.add(panelOkCancel);
		frame = new JFrame();
		frame.add(totalPanel);
		frame.setTitle("Class Selection Viewer");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		scatLabel = new JLabel("");
		scatLabel.setBorder(BorderFactory.createTitledBorder(""));
		scatLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 11));

		JPanel chartPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		sp2 = new ScatterPlotChannels();
		markerRange = new IntervalMarker(0, 0, new Color(252, 255, 253), new BasicStroke(), new Color(0, 102, 0),
				new BasicStroke(2.5f), 0.3f);
		markerDomain = new IntervalMarker(0, 0, new Color(252, 255, 253), new BasicStroke(), new Color(0, 102, 0),
				new BasicStroke(2.5f), 0.3f);
		histogram = sp2.createScatterChartPanel("Ch-2", "Ch-3", new double[] { 0.0, 0.0, 0.0 },
				new double[] { 0.0, 0.0, 0.0 }, markerRange, markerDomain);
		JButton refreshButton = new JButton("");
		ImageIcon iconRefresh = JFrameWizard.createImageIcon("images/resetmorpho.png");
		Icon refreshCell = new ImageIcon(iconRefresh.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
		refreshButton.setIcon(refreshCell);
		refreshButton.setToolTipText("Click this button to refresh data plot.");
		JButton paintButton = new JButton("");
		ImageIcon iconPaint = JFrameWizard.createImageIcon("images/paint.png");
		Icon paintCell = new ImageIcon(iconPaint.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
		paintButton.setIcon(paintCell);
		paintButton.setToolTipText("Click this button to paint positive cells.");
		JButton xmlButton = new JButton("");
		ImageIcon iconXml = JFrameWizard.createImageIcon("images/xml.png");
		Icon xmlCell = new ImageIcon(iconXml.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
		xmlButton.setIcon(xmlCell);
		xmlButton.setToolTipText("Click this button to export.XML file.");
		itemFilters = new String[] { "Area", "Mean", "StdDev", "Mode", "Min", "Max", "X", "Y", "XM", "YM", "Perim.",
				"BX", "BY", "Width", "Height", "Major", "Minor", "Angle", "Circ.", "Ferret", "IntDen", "Median", "Skew",
				"Kurt", "%Area", "RawIntDen", "Slice", "FeretX", "FeretY", "FeretAngle", "MinFeret", "AR", "Round",
				"Solidity", "MinThr", "MaxThr" };
		ScatterPlotChannels.itemFilters = itemFilters;
		comboChDomain = new JComboBox<String>();
		comboChDomain.addItem("Ch-2");
		comboChDomain.addItem("Ch-3");
		comboChDomain.setSelectedIndex(0);
		comboChDomain.setOpaque(true);
		// comboChDomain.setPreferredSize(new Dimension(65, 25));
		comboFeatureDomain = new JComboBox<String>();
		for (int i = 0; i < itemFilters.length; i++)
			comboFeatureDomain.addItem(itemFilters[i]);
		comboFeatureDomain.setSelectedIndex(0);
		comboFeatureDomain.setOpaque(true);
		comboFeatureDomain.setPreferredSize(new Dimension(65, 25));
		filterRange = new JSpinner(new SpinnerNumberModel(30, 0, 100000000, 1));
		filterRange.setMaximumSize(new Dimension(60, 20));
		sliderRange = new JSlider(JSlider.VERTICAL, 0, 100000000, 50);
		JPanel filtersMax = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		filterDomain = new JSpinner(new SpinnerNumberModel(50, 0, 100000000, 1));
		filterDomain.setPreferredSize(new Dimension(65, 25));
		sliderDomain = new JSlider(JSlider.HORIZONTAL, 0, 100000000, 150);
		sliderDomain.setPreferredSize(new Dimension(150, 15));
		filtersMax.add(sliderDomain);
		filtersMax.add(Box.createHorizontalStrut(2));
		filtersMax.add(filterDomain);
		filtersMax.add(comboChDomain);
		filtersMax.add(comboFeatureDomain);

		comboChRange = new JComboBox<String>();
		comboChRange.addItem("Ch-2");
		comboChRange.addItem("Ch-3");
		comboChRange.setSelectedIndex(0);
		comboChRange.setMaximumSize(new Dimension(60, 20));
		comboChRange.setOpaque(true);
		comboFeatureRange = new JComboBox<String>();
		for (int i = 0; i < itemFilters.length; i++)
			comboFeatureRange.addItem(itemFilters[i]);
		comboFeatureRange.setSelectedIndex(0);
		comboFeatureRange.setOpaque(true);
		comboFeatureRange.setMaximumSize(new Dimension(65, 25));
		JPanel rangePanelF = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel rangePanelBox = new JPanel();
		rangePanelBox.setLayout(new BoxLayout(rangePanelBox, BoxLayout.Y_AXIS));
		rangePanelBox.add(sliderRange);
		rangePanelBox.add(Box.createVerticalStrut(2));
		rangePanelBox.add(filterRange);
		rangePanelBox.add(comboChRange);
		rangePanelBox.add(comboFeatureRange);
		JPanel rangePanelBoxF = new JPanel(new FlowLayout(FlowLayout.LEFT));
		rangePanelBoxF.add(rangePanelBox);
		chartPanel2.add(rangePanelBoxF);
		chartPanel2.add(histogram);
		comboRegression = new JComboBox<String>();
		comboRegression.setPreferredSize(new Dimension(90, 20));
		comboRegression.addItem("Linear");
		comboRegression.addItem("Polynomial");
		comboRegression.addItem("Power");
		comboRegression.addItem("Logarithmic");
		comboRegression.addItem("Exponential");
		comboRegression.setSelectedIndex(0);
		comboRegression.setOpaque(true);
		comboClass = new JComboBox();
		comboClass.setPreferredSize(new Dimension(80, 25));
		comboClass.setToolTipText("Select a class to be analyzed.");
		filterOrder = new JSpinner(new SpinnerNumberModel(1, 0, 1000, 1));
		filterOrder.setPreferredSize(new Dimension(40, 20));
		filterOrder.setEnabled(false);
		JPanel regreOrderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		regreOrderPanel.add(comboRegression);
		JPanel filterOrderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		filterOrderPanel.add(filterOrder);
		filterOrderPanel.add(comboClass);
		JPanel chartDomainPanelBox = new JPanel();
		chartDomainPanelBox.setLayout(new BoxLayout(chartDomainPanelBox, BoxLayout.Y_AXIS));
		JPanel buttonBox = new JPanel(new FlowLayout(FlowLayout.LEFT));
		buttonBox.add(refreshButton);
		buttonBox.add(paintButton);
		buttonBox.add(xmlButton);
		buttonBox.add(regreOrderPanel);
		buttonBox.add(filterOrderPanel);
		JPanel scatPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		scatPanel.add(scatLabel);
		chartDomainPanelBox.add(scatPanel);
		chartDomainPanelBox.add(buttonBox);
		chartDomainPanelBox.add(chartPanel2);
		chartDomainPanelBox.add(filtersMax);
		// controlPanel2 = sp2.createControlPanel();
		// chartDomainPanelBox.add(controlPanel2);
		rangePanelF.add(chartDomainPanelBox);

		// rangePanelF.add(buttonBox);
		nextPage.add(rangePanelF);
		tablePositive = new JTable();
		tablePositiveSum = new JTable();
		modelPositive = new DefaultTableModel();
		modelPositiveSum = new DefaultTableModel();
		tablePositive.setModel(modelPositive);
		tablePositiveSum.setModel(modelPositiveSum);

		JScrollPane jScrollPanePositive = new JScrollPane(tablePositive);
		jScrollPanePositive.setPreferredSize(new Dimension(600, 160));
		JScrollPane jScrollPanePositiveSum = new JScrollPane(tablePositiveSum);
		jScrollPanePositiveSum.setPreferredSize(new Dimension(445, 80));
		nextPage.add(Box.createVerticalStrut(5));
		// nextPage.add(sumPanel);
		nextPage.add(Box.createVerticalStrut(5));
		JPanel tablePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		modelSPositive = new DefaultTableModel(2, 3);
		modelPositiveCount = new DefaultTableModel(1, 2);

		tableSPositive = new JTable(modelSPositive) {
			private static final long serialVersionUID = 1L;

			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int columnIndex) {

				JComponent component = (JComponent) super.prepareRenderer(renderer, rowIndex, columnIndex);

				if (columnIndex == 0) {
					component.setBackground(new Color(214, 217, 223));
					component.setForeground(Color.DARK_GRAY);
					component.setBorder(MiddlePageModified.tableSPositive.getTableHeader().getBorder());
				} else {
					component.setBackground(Color.WHITE);
					component.setForeground(Color.black);
				}

				return component;
			}
		};
		tablePositiveCount = new JTable(modelPositiveCount);
		tableSPositive.getColumnModel().getColumn(0).setCellRenderer(new RowHeaderRenderer());
		JTableHeader headerPCount = tablePositiveCount.getTableHeader();
		TableColumnModel colModPCount = headerPCount.getColumnModel();
		TableColumn tabColPCount0 = colModPCount.getColumn(0);
		TableColumn tabColPCount1 = colModPCount.getColumn(1);
		tabColPCount0.setHeaderValue("+ Count");
		tabColPCount1.setHeaderValue("+ %");
		headerPCount.repaint();
		JTableHeader header = tableSPositive.getTableHeader();
		TableColumnModel colMod = header.getColumnModel();
		TableColumn tabCol0 = colMod.getColumn(0);
		TableColumn tabCol1 = colMod.getColumn(1);
		TableColumn tabCol2 = colMod.getColumn(2);
		tabCol0.setHeaderValue("");
		tabCol1.setHeaderValue("Low");
		tabCol2.setHeaderValue("High");
		header.repaint();

		modelSPositive.setValueAt("High", tableSPositive.convertRowIndexToModel(0),
				tableSPositive.convertColumnIndexToModel(0));
		modelSPositive.setValueAt("Low", tableSPositive.convertRowIndexToModel(1),
				tableSPositive.convertColumnIndexToModel(0));
		jScrollPaneSPositive = new JScrollPane(tableSPositive);
		jScrollPaneSPositive.setPreferredSize(new Dimension(262, 110));

		jScrollPanePositiveCount = new JScrollPane(tablePositiveCount);
		jScrollPanePositiveCount.setPreferredSize(new Dimension(177, 70));

		String CELLTYPEANALYZER_XML_PATH = "xml_path";
		Preferences pref1 = Preferences.userRoot();
		TextField textImg = (TextField) new TextField(20);
		textImg.setText(pref1.get(CELLTYPEANALYZER_XML_PATH, ""));
		ImageIcon iconBrowse = JFrameWizard.createImageIcon("images/browse.png");
		JButton buttonImg = new JButton("");
		Icon iconImgCell = new ImageIcon(iconBrowse.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
		buttonImg.setIcon(iconImgCell);
		DirectoryListener listenerImg = new DirectoryListener("Browse for images ", textImg,
				JFileChooser.FILES_AND_DIRECTORIES);
		buttonImg.addActionListener(listenerImg);
		JPanel panelImg = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel directLabel = new JLabel("   ▸ Directoy for .XML file to get Analysis in Batch-Mode:   ");
		directLabel.setFont(new Font("Helvetica", Font.BOLD, 12));
		panelImg.add(directLabel);
		panelImg.add(textImg);
		panelImg.add(buttonImg);
		JPanel smallTableBox = new JPanel();
		smallTableBox.setLayout(new BoxLayout(smallTableBox, BoxLayout.Y_AXIS));
		JPanel bigTableBox = new JPanel();
		bigTableBox.setLayout(new BoxLayout(bigTableBox, BoxLayout.Y_AXIS));
		JLabel labelTablePositive = new JLabel("➟ Single-Cell Identification: ");
		labelTablePositive.setFont(new Font(Font.DIALOG, Font.BOLD, 12));
		JPanel panelTablePositive = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelTablePositive.add(labelTablePositive);
		JPanel panelTablePositive2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panelTablePositive2.add(jScrollPanePositive);
		bigTableBox.add(panelTablePositive);
		bigTableBox.add(panelTablePositive2);
		// bigTableBox.add(panelImg);
		JLabel labelClass = new JLabel();
		JLabel labelQuadrant = new JLabel();
		JLabel labelPositive = new JLabel();
		labelPositive.setText("➟ Cell-Positive Counter:  ");
		labelPositive.setFont(new Font(Font.DIALOG, Font.BOLD, 12));
		labelClass.setText("➟ Channel-Class Counter:  ");
		labelClass.setFont(new Font(Font.DIALOG, Font.BOLD, 12));
		labelQuadrant.setText("➟ Quadrant-Class Counter:  ");
		labelQuadrant.setFont(new Font(Font.DIALOG, Font.BOLD, 12));
		JPanel panelPositive = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel panelPositive2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel panelClass = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel panelClass2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel panelQuadrant = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel panelQuadrant2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		comboPositive = new JComboBox();
		comboQuadrant = new JComboBox();
		comboQuadrant.addItem("All");
		comboQuadrant.addItem("No Identified");
		panelClass.add(labelClass);
		panelPositive.add(labelPositive);
		panelPositive2.add(jScrollPanePositiveCount);
		panelClass2.add(jScrollPanePositiveSum);
		panelQuadrant.add(labelQuadrant);
		panelQuadrant.add(comboQuadrant);
		panelQuadrant2.add(jScrollPaneSPositive);
		JPanel positiveBox = new JPanel();
		positiveBox.setLayout(new BoxLayout(positiveBox, BoxLayout.Y_AXIS));
		positiveBox.add(panelPositive);
		positiveBox.add(panelPositive2);
		JPanel quadrantBox = new JPanel();
		quadrantBox.setLayout(new BoxLayout(quadrantBox, BoxLayout.Y_AXIS));
		quadrantBox.add(panelQuadrant);
		quadrantBox.add(panelQuadrant2);
		JPanel classBox = new JPanel();
		classBox.setLayout(new BoxLayout(classBox, BoxLayout.Y_AXIS));
		classBox.add(panelClass);
		classBox.add(panelClass2);
		smallTableBox.add(bigTableBox);
		smallTableBox.add(classBox);
		JPanel qpPanel = new JPanel();
		qpPanel.setLayout(new GridLayout(1, 2));
		qpPanel.add(quadrantBox);
		qpPanel.add(positiveBox);
		smallTableBox.add(qpPanel);
		JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
		Dimension dime = separator.getPreferredSize();
		dime.width = (histogram.getPreferredSize().width / 2) + 5;
		separator.setPreferredSize(dime);
		tablePanel.add(smallTableBox);
		// tablePanel.add(bigTableBox);
		nextPage.add(separator);
		nextPage.add(tablePanel);

		comboRegression.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (comboRegression.getSelectedIndex() == 1)
					filterOrder.setEnabled(true);
				if (comboRegression.getSelectedIndex() != 1)
					filterOrder.setEnabled(false);
			}
		});
		sliderRange.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {

				filterRange.setValue(sliderRange.getValue());
				markerRange.setStartValue(sliderRange.getValue());
				markerRange.setEndValue(maxRange + 20);

			}
		});

		filterRange.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				sliderRange.setValue((int) filterRange.getValue());
				markerRange.setStartValue((int) filterRange.getValue());
				markerRange.setEndValue(maxRange + 20);

			}
		});

		sliderDomain.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				filterDomain.setValue(sliderDomain.getValue());
				markerDomain.setStartValue(sliderDomain.getValue());
				markerDomain.setEndValue(maxDomain + 20);
			}
		});

		filterDomain.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				sliderDomain.setValue((int) filterDomain.getValue());
				markerDomain.setStartValue((int) filterDomain.getValue());
				markerDomain.setEndValue(maxDomain + 20);

			}
		});
		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshAction();
				buildTable();
				comboClassChannelAction();
				comboPositiveAction();

			}
		});

		paintButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				paintRoisAction();

			}
		});

		comboQuadrant.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					selectedClassQuadrant = "";
					if (comboQuadrant.getItemCount() != 0)
						selectedClassQuadrant = comboQuadrant.getSelectedItem().toString();
					if (comboQuadrant.getSelectedIndex() == 1) {

						int lhCounterNID = 0;
						for (int i = 0; i < ScatterPlotChannels.series1.getItemCount(); i++) {
							if (ScatterPlotChannels.series1.getX(i).doubleValue() <= Double
									.parseDouble(ScatterPlotChannels.domainValue)
									&& ScatterPlotChannels.series1.getY(i).doubleValue() >= Double
											.parseDouble(ScatterPlotChannels.rangeValue)
									&& ScatterPlotChannels.renderer.getItemPaint(0, i) == Color.LIGHT_GRAY)
								lhCountNID = (lhCounterNID++)+1;

						}
						int hhCounterNID = 0;
						for (int i = 0; i < ScatterPlotChannels.series1.getItemCount(); i++) {
							if (ScatterPlotChannels.series1.getX(i).doubleValue() >= Double
									.parseDouble(ScatterPlotChannels.domainValue)
									&& ScatterPlotChannels.series1.getY(i).doubleValue() >= Double
											.parseDouble(ScatterPlotChannels.rangeValue)
									&& ScatterPlotChannels.renderer.getItemPaint(0, i) == Color.LIGHT_GRAY)
								hhCountNID = (hhCounterNID++)+1;
						}

						int llCounterNID = 0;
						for (int i = 0; i < ScatterPlotChannels.series1.getItemCount(); i++) {
							if (ScatterPlotChannels.series1.getX(i).doubleValue() <= Double
									.parseDouble(ScatterPlotChannels.domainValue)
									&& ScatterPlotChannels.series1.getY(i).doubleValue() <= Double
											.parseDouble(ScatterPlotChannels.rangeValue)
									&& ScatterPlotChannels.renderer.getItemPaint(0, i) == Color.LIGHT_GRAY)
								llCountNID = (llCounterNID++)+1;
						}

						int hlCounterNID = 0;
						for (int i = 0; i < ScatterPlotChannels.series1.getItemCount(); i++) {
							if (ScatterPlotChannels.series1.getX(i).doubleValue() >= Double
									.parseDouble(ScatterPlotChannels.domainValue)
									&& ScatterPlotChannels.series1.getY(i).doubleValue() <= Double
											.parseDouble(ScatterPlotChannels.rangeValue)
									&& ScatterPlotChannels.renderer.getItemPaint(0, i) == Color.LIGHT_GRAY)
								hlCountNID = (hlCounterNID++)+1;

						}
						modelSPositive.setValueAt(lhCountNID, tableSPositive.convertRowIndexToModel(0),
								tableSPositive.convertColumnIndexToModel(1));
						modelSPositive.setValueAt(hhCountNID, tableSPositive.convertRowIndexToModel(0),
								tableSPositive.convertColumnIndexToModel(2));
						modelSPositive.setValueAt(llCountNID, tableSPositive.convertRowIndexToModel(1),
								tableSPositive.convertColumnIndexToModel(1));
						modelSPositive.setValueAt(hlCountNID, tableSPositive.convertRowIndexToModel(1),
								tableSPositive.convertColumnIndexToModel(2));

					}

					if (comboQuadrant.getSelectedIndex() > 1) {
						for (int i = 0; i < ColorEditorEnd.tableC.getModel().getRowCount(); i++)
							if (((JLabel) ColorEditorEnd.tableC.getModel().getValueAt(i,
									ColorEditorEnd.tableC.convertColumnIndexToModel(0)))
											.getText() == selectedClassQuadrant)
								selectedColorQuadrant = ((JLabel) ColorEditorEnd.tableC.getModel().getValueAt(i,
										ColorEditorEnd.tableC.convertColumnIndexToModel(1))).getBackground();

						int lhCounterClass = 0;
						for (int i = 0; i < ScatterPlotChannels.series1.getItemCount(); i++) {
							if (ScatterPlotChannels.series1.getX(i).doubleValue() <= Double
									.parseDouble(ScatterPlotChannels.domainValue)
									&& ScatterPlotChannels.series1.getY(i).doubleValue() >= Double
											.parseDouble(ScatterPlotChannels.rangeValue)
									&& ScatterPlotChannels.renderer.getItemPaint(0, i) == selectedColorQuadrant
									&& ScatterPlotChannels.renderer.getItemPaint(0, i) != Color.LIGHT_GRAY)
								lhCountClass = lhCounterClass++;

						}
						int hhCounterClass = 0;
						for (int i = 0; i < ScatterPlotChannels.series1.getItemCount(); i++) {
							if (ScatterPlotChannels.series1.getX(i).doubleValue() >= Double
									.parseDouble(ScatterPlotChannels.domainValue)
									&& ScatterPlotChannels.series1.getY(i).doubleValue() >= Double
											.parseDouble(ScatterPlotChannels.rangeValue)
									&& ScatterPlotChannels.renderer.getItemPaint(0, i) == selectedColorQuadrant
									&& ScatterPlotChannels.renderer.getItemPaint(0, i) != Color.LIGHT_GRAY)
								hhCountClass = hhCounterClass++;
						}

						int llCounterClass = 0;
						for (int i = 0; i < ScatterPlotChannels.series1.getItemCount(); i++) {
							if (ScatterPlotChannels.series1.getX(i).doubleValue() <= Double
									.parseDouble(ScatterPlotChannels.domainValue)
									&& ScatterPlotChannels.series1.getY(i).doubleValue() <= Double
											.parseDouble(ScatterPlotChannels.rangeValue)
									&& ScatterPlotChannels.renderer.getItemPaint(0, i) == selectedColorQuadrant
									&& ScatterPlotChannels.renderer.getItemPaint(0, i) != Color.LIGHT_GRAY)
								llCountClass = llCounterClass++;
						}

						int hlCounterClass = 0;
						for (int i = 0; i < ScatterPlotChannels.series1.getItemCount(); i++) {
							if (ScatterPlotChannels.series1.getX(i).doubleValue() >= Double
									.parseDouble(ScatterPlotChannels.domainValue)
									&& ScatterPlotChannels.series1.getY(i).doubleValue() <= Double
											.parseDouble(ScatterPlotChannels.rangeValue)
									&& ScatterPlotChannels.renderer.getItemPaint(0, i) == selectedColorQuadrant
									&& ScatterPlotChannels.renderer.getItemPaint(0, i) != Color.LIGHT_GRAY)
								hlCountClass = hlCounterClass++;

						}
						modelSPositive.setValueAt(lhCountClass, tableSPositive.convertRowIndexToModel(0),
								tableSPositive.convertColumnIndexToModel(1));
						modelSPositive.setValueAt(hhCountClass, tableSPositive.convertRowIndexToModel(0),
								tableSPositive.convertColumnIndexToModel(2));
						modelSPositive.setValueAt(llCountClass, tableSPositive.convertRowIndexToModel(1),
								tableSPositive.convertColumnIndexToModel(1));
						modelSPositive.setValueAt(hlCountClass, tableSPositive.convertRowIndexToModel(1),
								tableSPositive.convertColumnIndexToModel(2));

					}

					if (comboQuadrant.getSelectedIndex() == 0) {
						// lh
						int lhCounterAll = 0;
						for (int i = 0; i < ScatterPlotChannels.series1.getItemCount(); i++) {
							if (ScatterPlotChannels.series1.getX(i).doubleValue() <= Double
									.parseDouble(ScatterPlotChannels.domainValue)
									&& ScatterPlotChannels.series1.getY(i).doubleValue() >= Double
											.parseDouble(ScatterPlotChannels.rangeValue))
								lhCountAll = (lhCounterAll++)+1;
						}

						// hh
						int hhCounter = 0;
						for (int i = 0; i < ScatterPlotChannels.series1.getItemCount(); i++) {
							if (ScatterPlotChannels.series1.getX(i).doubleValue() >= Double
									.parseDouble(ScatterPlotChannels.domainValue)
									&& ScatterPlotChannels.series1.getY(i).doubleValue() >= Double
											.parseDouble(ScatterPlotChannels.rangeValue))
								hhCountAll = (hhCounter++)+1;
						}

						// ll
						int llCounter = 0;
						for (int i = 0; i < ScatterPlotChannels.series1.getItemCount(); i++) {
							if (ScatterPlotChannels.series1.getX(i).doubleValue() <= Double
									.parseDouble(ScatterPlotChannels.domainValue)
									&& ScatterPlotChannels.series1.getY(i).doubleValue() <= Double
											.parseDouble(ScatterPlotChannels.rangeValue))
								llCountAll = (llCounter++)+1;
						}

						// ll
						int hlCounter = 0;
						for (int i = 0; i < ScatterPlotChannels.series1.getItemCount(); i++)
							if (ScatterPlotChannels.series1.getX(i).doubleValue() >= Double
									.parseDouble(ScatterPlotChannels.domainValue)
									&& ScatterPlotChannels.series1.getY(i).doubleValue() <= Double
											.parseDouble(ScatterPlotChannels.rangeValue))
								hlCountAll = (hlCounter++)+1;

						modelSPositive.setValueAt(lhCountAll, tableSPositive.convertRowIndexToModel(0),
								tableSPositive.convertColumnIndexToModel(1));
						modelSPositive.setValueAt(hhCountAll, tableSPositive.convertRowIndexToModel(0),
								tableSPositive.convertColumnIndexToModel(2));
						modelSPositive.setValueAt(llCountAll, tableSPositive.convertRowIndexToModel(1),
								tableSPositive.convertColumnIndexToModel(1));
						modelSPositive.setValueAt(hlCountAll, tableSPositive.convertRowIndexToModel(1),
								tableSPositive.convertColumnIndexToModel(2));

					}
				}

			}

		});

		JButton exportButton = JFrameWizard.exportButton;
		exportButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ExportAction ea = new ExportAction(StartPageModified.table, StartPageModified.tableS,
						MiddlePageModified.tablePositive, MiddlePageModified.tableSPositive,
						EndPageModified.tablePositive, EndPageModified.tableSPositive,
						MiddlePageModified2.tablePositive, MiddlePageModified2.tableSPositive,
						MiddlePageModified2.tablePositiveSum, MiddlePageModified2.tablePositiveCount);
				ea.createAndShowExportGUI();

			}
		});
		xmlButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				classViewerDisplay();
			}
		});
		okButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				int selectedRow = ColorEditorEnd.tableC.getSelectedRow();
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
				exportXMLAction(selectedRow);

			}
		});
		cancelButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));

			}
		});

	}

	public void exportXMLAction(int selectedRow) {
		JFileChooser save = new JFileChooser();
		java.util.Locale.setDefault(java.util.Locale.ENGLISH);
		save.setLocale(java.util.Locale.ENGLISH);
		save.setLocale(java.util.Locale.getDefault());
		save.updateUI();
		save.setDialogTitle("Choose a directory to export .XML file...");
		save.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		save.setFileFilter(new FileNameExtensionFilter("xls", "xlsx"));
		int choose = save.showSaveDialog(null);

		if (choose == JFileChooser.APPROVE_OPTION) {
			String xmlFilePath = save.getSelectedFile().getAbsolutePath();
			File directFolder = new File(
					xmlFilePath + File.separator + StartPageModified.imp.getShortTitle() + "_XMLFile");

			if (!directFolder.exists()) {
				boolean result = false;

				try {
					directFolder.mkdir();
					result = true;
				} catch (SecurityException se) {
					// handle it
				}

				save.setCurrentDirectory(save.getSelectedFile());

			}
			CreateXMLFile xmlFile = new CreateXMLFile(
					xmlFilePath + File.separator + StartPageModified.imp.getShortTitle() + "_XMLFile", selectedRow);
			xmlFile.exportXMLAction();
			JOptionPane.showMessageDialog(null, "Exported Succesfully in " + directFolder.getAbsolutePath() + ".");
		}

	}

	public void comboPositiveAction() {

		int counterTotalPositive = 0;
		for (int i = 0; i < modelPositive.getRowCount(); i++)
			if (((String) modelPositive.getValueAt(i, tablePositive.convertColumnIndexToModel(4)))
					.equals("✔") == Boolean.TRUE)
				counterPositive = counterTotalPositive++;

		modelPositiveCount.setValueAt(counterPositive + 1, tablePositiveCount.convertRowIndexToModel(0),
				tablePositiveCount.convertColumnIndexToModel(0));
		modelPositiveCount.setValueAt(
				(double) Math
						.round(((double) (counterPositive + 1) * 100.0 / (double) modelPositive.getRowCount()) * 100.0)
						/ 100.0,
				tablePositiveCount.convertRowIndexToModel(0), tablePositiveCount.convertColumnIndexToModel(1));

	}

	public void refreshAction() {

		List<Double> valuesDomainList = new ArrayList<Double>();
		int selectedIndexDomain = (int) comboFeatureDomain.getSelectedIndex();

		if (comboChDomain.getSelectedIndex() == 0) {

			for (int i = 0; i < MiddlePageModified.tablePositive.getRowCount(); i++)
				valuesDomainList.add((Double) MiddlePageModified.tablePositive.getModel().getValueAt(
						MiddlePageModified.tablePositive.convertRowIndexToModel(i),
						MiddlePageModified.tablePositive.convertColumnIndexToModel(selectedIndexDomain + 3)));
		}
		if (comboChDomain.getSelectedIndex() == 1) {

			for (int i = 0; i < EndPageModified.tablePositive.getRowCount(); i++)
				valuesDomainList.add((Double) EndPageModified.tablePositive.getModel().getValueAt(
						EndPageModified.tablePositive.convertRowIndexToModel(i),
						EndPageModified.tablePositive.convertColumnIndexToModel(selectedIndexDomain + 3)));
		}
		valuesDomain = valuesDomainList.stream().mapToDouble(d -> d).toArray();

		int iDomain;
		maxDomain = valuesDomain[0];
		minDomain = valuesDomain[0];
		for (iDomain = 1; iDomain < valuesDomain.length; iDomain++) {
			if (valuesDomain[iDomain] > maxDomain)
				maxDomain = valuesDomain[iDomain];
			if (valuesDomain[iDomain] < minDomain)
				minDomain = valuesDomain[iDomain];
		}

		List<Double> valuesRangeList = new ArrayList<Double>();
		int selectedIndexRange = (int) comboFeatureRange.getSelectedIndex();
		if (comboChRange.getSelectedIndex() == 0) {
			for (int j = 0; j < MiddlePageModified.tablePositive.getRowCount(); j++)
				valuesRangeList.add((Double) MiddlePageModified.tablePositive.getModel().getValueAt(
						MiddlePageModified.tablePositive.convertRowIndexToModel(j),
						MiddlePageModified.tablePositive.convertColumnIndexToModel(selectedIndexRange + 3)));
		}
		if (comboChRange.getSelectedIndex() == 1) {
			for (int j = 0; j < EndPageModified.tablePositive.getRowCount(); j++)
				valuesRangeList.add((Double) EndPageModified.tablePositive.getModel().getValueAt(
						EndPageModified.tablePositive.convertRowIndexToModel(j),
						EndPageModified.tablePositive.convertColumnIndexToModel(selectedIndexRange + 3)));
		}
		valuesRange = valuesRangeList.stream().mapToDouble(d -> d).toArray();
		int iRange;
		maxRange = valuesRange[0];
		minRange = valuesRange[0];
		for (iRange = 1; iRange < valuesRange.length; iRange++) {
			if (valuesRange[iRange] > maxRange)
				maxRange = valuesRange[iRange];
			if (valuesRange[iRange] < minRange)
				minRange = valuesRange[iRange];
		}

		sliderDomain.setMinimum((int) minDomain);
		sliderDomain.setMaximum((int) maxDomain);
		sliderRange.setMinimum((int) minRange);
		sliderRange.setMaximum((int) maxRange);

		List<Integer> roisNameTableIndex2 = new ArrayList<Integer>();
		for (int i = 0; i < modelPositive.getRowCount(); i++) {
			if (((String) modelPositive.getValueAt(i, tablePositive.convertColumnIndexToModel(4)))
					.equals("✔") == Boolean.TRUE)
				roisNameTableIndex2.add(i);

		}
		colorArray = new Color[roisNameTableIndex2.size()];
		for (int i = 0; i < roisNameTableIndex2.size(); i++)
			colorArray[i] = ColorEditorEnd.listOfColors.get(comboClass.getSelectedIndex());
		// listColor.add(((JLabel)
		// EndPageModified.tablePositive.getModel().getValueAt(roisNameTableIndex2.get(i),
		// EndPageModified.tablePositive.convertColumnIndexToModel(2))).getBackground());
		// classColor2 = new Color[roisNameTableIndex2.size()];
		// listColor.toArray(classColor2);

		if (filterOrder.getValue() != null)
			sp2.addScatterPlotSeries(labelCh2, labelCh3, valuesDomain, valuesRange, markerRange, markerDomain,
					filterDomain.getValue().toString(), filterRange.getValue().toString(), roisNameTableIndex2,
					colorArray, comboRegression.getSelectedIndex(), (int) filterOrder.getValue());
		if (filterOrder.getValue() == null)
			sp2.addScatterPlotSeries(labelCh2, labelCh3, valuesDomain, valuesRange, markerRange, markerDomain,
					filterDomain.getValue().toString(), filterRange.getValue().toString(), roisNameTableIndex2,
					colorArray, comboRegression.getSelectedIndex(), 0);

	}

	public void paintRoisAction() {

		List<String> roisName = new ArrayList<String>();
		List<String> roisNameTableIndex = new ArrayList<String>();
		List<String> roisClasses = new ArrayList<String>();

		for (int i = 0; i < modelPositive.getRowCount(); i++) {
			roisName.add((String) modelPositive.getValueAt(i, tablePositive.convertColumnIndexToModel(1)));
			if (((String) modelPositive.getValueAt(i, tablePositive.convertColumnIndexToModel(4)))
					.equals("✔") == Boolean.TRUE) {
				roisNameTableIndex
						.add(((String) modelPositive.getValueAt(i, tablePositive.convertColumnIndexToModel(1)))
								.substring(0, 4));
				roisClasses.add(((String) modelPositive.getValueAt(i, tablePositive.convertColumnIndexToModel(2))));

			}
		}
		Roi[] roisToColor = new Roi[roisNameTableIndex.size()];
		for (int i = 0; i < roisNameTableIndex.size(); i++)
			roisToColor[i] = StartPageModified.rois[Integer.parseInt(roisNameTableIndex.get(i)) - 1];
		for (int i = 0; i < roisToColor.length; i++)
			roisToColor[i].setStrokeColor(colorArray[i]);
		roiManager = RoiManager.getInstance();
		if (null == roiManager)
			roiManager = new RoiManager();
		roiManager.reset();
		for (int i = 0; i < roisNameTableIndex.size(); i++) {
			roiManager.addRoi(roisToColor[i]);
			roiManager.rename(i, roisName.get(Integer.parseInt(roisNameTableIndex.get(i)) - 1));
		}
		roiManager.runCommand("Show All without labels");

	}

	public void buildTable() {

		String[] columnHeaders = new String[] { "Cell-Image", "Cell-Label", labelCh2, labelCh3, "+ ?" };
		String[] columnHeadersS = new String[] { "Low", "High" };
		String[] columnHeadersSum = new String[] { labelCh2, labelCh2 + "  %", labelCh3, labelCh3 + " %" };
		List<String> roisNameEnd = new ArrayList<String>();
		for (int j = 0; j < EndPageModified.modelPositive.getRowCount(); j++)
			roisNameEnd.add((String) EndPageModified.modelPositive.getValueAt(j,
					EndPageModified.tablePositive.convertColumnIndexToModel(1)));
		modelPositive = new DefaultTableModel(columnHeaders, roisNameEnd.size()) {

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
		modelPositiveSum = new DefaultTableModel(new Double[][] { {} }, columnHeadersSum) {

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

		tablePositive.setModel(modelPositive);
		tablePositiveSum.setModel(modelPositiveSum);
		tablePositive.setSelectionBackground(new Color(229, 255, 204));
		tablePositive.setSelectionForeground(new Color(0, 102, 0));
		tablePositiveSum.setSelectionBackground(new Color(229, 255, 204));
		tablePositiveSum.setSelectionForeground(new Color(0, 102, 0));

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		tablePositive.setDefaultRenderer(String.class, centerRenderer);
		tablePositive.setDefaultRenderer(Double.class, centerRenderer);
		tablePositiveSum.setDefaultRenderer(Integer.class, centerRenderer);
		tablePositiveSum.setDefaultRenderer(String.class, centerRenderer);
		tablePositiveSum.setDefaultRenderer(Double.class, centerRenderer);
		tablePositive.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tablePositiveSum.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableSPositive.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tablePositiveCount.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tablePositive.setRowHeight(40);
		tablePositiveSum.setRowHeight(40);
		tableSPositive.setRowHeight(40);
		tablePositiveCount.setRowHeight(40);
		tablePositive.getTableHeader().setDefaultRenderer(new SimpleHeaderRenderer());
		tablePositiveSum.getTableHeader().setDefaultRenderer(new SimpleHeaderRenderer());
		tableSPositive.getTableHeader().setDefaultRenderer(new SimpleHeaderRenderer());
		tablePositiveCount.getTableHeader().setDefaultRenderer(new SimpleHeaderRenderer());

		for (int i = 0; i < icons.length; i++) {
			modelPositive.setValueAt(icons[i], i, tablePositive.convertColumnIndexToModel(0));
			modelPositive.setValueAt(roisNameEnd.get(i), i, tablePositive.convertColumnIndexToModel(1));
		}

		for (int i = 0; i < modelPositive.getRowCount(); i++) {
			modelPositive.setValueAt(ColorEditorEnd.ch2List.get(comboClass.getSelectedIndex()).get(i), i,
					tablePositive.convertColumnIndexToModel(2));
			modelPositive.setValueAt(ColorEditorEnd.ch3List.get(comboClass.getSelectedIndex()).get(i), i,
					tablePositive.convertColumnIndexToModel(3));
			if (((String) modelPositive.getValueAt(i, tablePositive.convertColumnIndexToModel(2))).equals(
					((String) modelPositive.getValueAt(i, tablePositive.convertColumnIndexToModel(3)))) == Boolean.TRUE
					|| (((String) modelPositive.getValueAt(i, tablePositive.convertColumnIndexToModel(2))).length() != 0
							&& ((String) modelPositive.getValueAt(i, tablePositive.convertColumnIndexToModel(2)))
									.length() != 0))
				modelPositive.setValueAt("✔", i, tablePositive.convertColumnIndexToModel(4));
			if (((String) modelPositive.getValueAt(i, tablePositive.convertColumnIndexToModel(2))).equals(
					((String) modelPositive.getValueAt(i, tablePositive.convertColumnIndexToModel(3)))) == Boolean.FALSE
					|| (((String) modelPositive.getValueAt(i, tablePositive.convertColumnIndexToModel(2))).length() == 0
							&& ((String) modelPositive.getValueAt(i, tablePositive.convertColumnIndexToModel(2)))
									.length() == 0))
				modelPositive.setValueAt("✗", i, tablePositive.convertColumnIndexToModel(4));

			if (((String) modelPositive.getValueAt(i, tablePositive.convertColumnIndexToModel(2)))
					.isEmpty() == Boolean.TRUE
					|| ((String) modelPositive.getValueAt(i, tablePositive.convertColumnIndexToModel(2)))
							.equals(ColorEditorEnd.ch2List.get(comboClass.getSelectedIndex()).get(i)) == Boolean.FALSE)
				modelPositive.setValueAt("No Identified", i, tablePositive.convertColumnIndexToModel(2));
			if (((String) modelPositive.getValueAt(i, tablePositive.convertColumnIndexToModel(3)))
					.isEmpty() == Boolean.TRUE
					|| ((String) modelPositive.getValueAt(i, tablePositive.convertColumnIndexToModel(3)))
							.equals(ColorEditorEnd.ch3List.get(comboClass.getSelectedIndex()).get(i)) == Boolean.FALSE)
				modelPositive.setValueAt("No Identified", i, tablePositive.convertColumnIndexToModel(3));
		}

		Double[] ch2XValuesCopy = Arrays.copyOf(ch2XValues, ch2XValues.length);
		Double[] ch3YValuesCopy = Arrays.copyOf(ch3YValues, ch3YValues.length);

		String[] ch2XValuesCopyS = new String[ch2XValuesCopy.length];
		String[] ch3YValuesCopyS = new String[ch3YValuesCopy.length];

		for (int i = 0; i < ch2XValuesCopyS.length; i++)
			ch2XValuesCopyS[i] = String.valueOf(ch2XValuesCopy[i]);
		for (int i = 0; i < ch3YValuesCopyS.length; i++)
			ch3YValuesCopyS[i] = String.valueOf(ch3YValuesCopy[i]);

		int counter2X = 0;
		for (int i = 0; i < ch2XValuesCopyS.length; i++) {
			if (ch2XValuesCopyS[i].startsWith("0.") == true) {
				ch2XValuesCopyS[i] = "-";

			} else {
				ch2XValuesCopyS[i] = "+";
				numCh2Positive = counter2X++;

			}
		}

		int counter3Y = 0;
		for (int i = 0; i < ch3YValuesCopyS.length; i++) {
			if (ch3YValuesCopyS[i].startsWith("0.") == true) {
				ch3YValuesCopyS[i] = "-";
			} else {
				ch3YValuesCopyS[i] = "+";
				numCh3Positive = counter3Y++;
			}

		}

		for (int u = 0; u < tablePositive.getColumnCount(); u++)
			tablePositive.getColumnModel().getColumn(u).setPreferredWidth(140);

		for (int u = 0; u < tablePositiveSum.getColumnCount(); u++)
			tablePositiveSum.getColumnModel().getColumn(u).setPreferredWidth(110);

		for (int u = 0; u < tableSPositive.getColumnCount(); u++)
			tableSPositive.getColumnModel().getColumn(u).setPreferredWidth(85);

		for (int u = 0; u < tablePositiveCount.getColumnCount(); u++)
			tablePositiveCount.getColumnModel().getColumn(u).setPreferredWidth(85);

		tableSPositive.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		tableSPositive.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
		tablePositiveCount.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		tablePositiveCount.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

	}

	public void comboClassChannelAction() {

		String selectedClass = "";
		if (comboClass.getItemCount() != 0)
			selectedClass = comboClass.getSelectedItem().toString();
		int classCounter1 = 0;
		int classCounter2 = 0;
		int classCounter3 = 0;

		for (int i = 0; i < modelPositive.getRowCount(); i++) {
			// counter1
			if (((String) modelPositive.getValueAt(i, tablePositive.convertColumnIndexToModel(2)))
					.equals(selectedClass) == Boolean.TRUE) {
				int counter1 = classCounter1++;
				modelPositiveSum.setValueAt(new Integer(counter1 + 1), tablePositiveSum.convertRowIndexToModel(0),
						tablePositiveSum.convertColumnIndexToModel(0));
				modelPositiveSum.setValueAt(
						(double) Math
								.round(((double) (counter1 + 1) * 100.0 / (double) modelPositive.getRowCount()) * 100.0)
								/ 100.0,
						tablePositiveSum.convertRowIndexToModel(0), tablePositiveSum.convertColumnIndexToModel(1));
			}
			// chanel3
			if (((String) modelPositive.getValueAt(i, tablePositive.convertColumnIndexToModel(3)))
					.equals(selectedClass) == Boolean.TRUE) {
				int counter2 = classCounter2++;
				modelPositiveSum.setValueAt(new Integer(counter2 + 1), tablePositiveSum.convertRowIndexToModel(0),
						tablePositiveSum.convertColumnIndexToModel(2));
				modelPositiveSum.setValueAt(
						(double) Math
								.round(((double) (counter2 + 1) * 100.0 / (double) modelPositive.getRowCount()) * 100.0)
								/ 100.0,
						tablePositiveSum.convertRowIndexToModel(0), tablePositiveSum.convertColumnIndexToModel(3));
			}

		}
	}

	public void classViewerDisplay() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				tableCVersion.setModel(ColorEditorEnd.tableC.getModel());
				ColorEditorEnd.tableC.setRowHeight(25);
				frame.setVisible(true);
			}
		});
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