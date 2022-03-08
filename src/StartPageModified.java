import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.annotations.*;
import ij.IJ;
import ij.ImagePlus;
import ij.Macro;
import ij.WindowManager;
import ij.gui.Roi;
import ij.macro.Interpreter;
import ij.measure.Calibration;
import ij.measure.Measurements;
import ij.measure.ResultsTable;
import ij.plugin.ChannelSplitter;
import ij.plugin.Duplicator;
import ij.plugin.ImagesToStack;
import ij.plugin.Macro_Runner;
import ij.plugin.filter.Analyzer;
import ij.plugin.filter.ParticleAnalyzer;
import ij.plugin.frame.PlugInFrame;
import ij.plugin.frame.RoiManager;
import ij.process.ImageProcessor;
import ij.process.ImageStatistics;
import ij.text.TextWindow;

@SuppressWarnings("serial")
public class StartPageModified extends AbstractWizardPage {

	private AbstractWizardPage nextPage;
	public static ImagePlus imp = null, imageToSet = null, impToAnalyze = null, impNext = null, channelAnal = null,
			channelAnalShow = null, impMontage = null;
	private JSpinner pXMin, pXMax, pYMin, pYMax, pwidth, pheight;
	static int xstart, xend, ystart, yend, selectedIndexCh1 = 0, selectedIndexCh2 = 0, meas;
	private Roi roi;
	private JLabel channelLabel, imageLabel;
	public static ImagePlus[] channels = null, imps = null, impbs = null;
	private JLabel iconImage, sumLabel, filterLabel;
	static String xmin, xmax, ymin, ymax, selectedItem, selectedTh, waterChecked, pickerChecked, previewButton,
			textCh2 = "", CHANNEL1_TEXT, CHANNEL2_TEXT, CHANNEL3_TEXT, CELLTYPEANALYZER_IMAGES_DEFAULT_PATH;
	public static String title;
	static JTable table, tableS, tableC, tableImages;
	private DefaultTableModel model, modelS, modelImages;
	private JSpinner filterMin, filterMax;
	static JComboBox<String> comboFilters, comboCh1, comboCh2, comboCh3, thresholdMeths, comboSumParam;
	private TableRowSorter<TableModel> rowSorterR;
	static Roi[] rois, roisFiltered;
	static RoiManager roiManager;
	private List<String> roisNames;
	public List<Double> areas5;
	public static List<String> roisNameTable;
	private Double[][] data;
	private ChartPanel histogram;
	private HistogramFilter hs2 = new HistogramFilter();
	private JPanel controlPanel2;
	private double max, min;
	public IntervalMarker intervalMarker;
	private DefaultListModel<String> modelList;
	static JList<String> filterList;
	private List<List<String>> dataList;
	private JList listRoiManager;
	static JTextField tfCh1, tfCh2, tfCh3;
	private Preferences prefs1, prefs2, prefs3;
	static ResultsTable rtCh2, rtCh3;
	static TextField textImages;
	public ImageIcon[] icons;
	public JScrollPane jScrollPaneImages;
	public JPanel boxPanel, controlPanel, chartPanel;
	public HistogramSenescence2 hs;
	public ChartPanel cpReal;
	static ImagePlus[] arrayOfImages = null;
	Preferences prefImages;
	static String itemFilters[] = new String[] { "Area", "Mean", "StdDev", "Mode", "Min", "Max", "X", "Y", "XM", "YM",
			"Perim.", "BX", "BY", "Width", "Height", "Major", "Minor", "Angle", "Circ.", "Ferret", "IntDen", "Median",
			"Skew", "Kurt", "%Area", "RawIntDen", "Slice", "FeretX", "FeretY", "FeretAngle", "MinFeret", "AR", "Round",
			"Solidity", "MinThr", "MaxThr" };
	static String[] columnHeadersS = new String[] { "Channel-Label", "N", "Sum", "Mean", "Median", "Variance", "stdDev",
			"Min", "Max", "Q1", "Q3", "IQR" };
	static List<String> textSaved = new ArrayList<String>();;
	JCheckBox checkWater;

	public StartPageModified() {

		nextPage = new MiddlePageModified();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		CELLTYPEANALYZER_IMAGES_DEFAULT_PATH = "images_path";
		prefImages = Preferences.userRoot();
		imageLabel = new JLabel();
		imageLabel.setBorder(BorderFactory.createTitledBorder(""));
		imageLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 11));
		JLabel imageCal = new JLabel("  Data Spatial Calibration :   ");
		imageCal.setBorder(BorderFactory.createTitledBorder(""));
		imageCal.setFont(new Font(Font.DIALOG, Font.BOLD, 11));
		JLabel imageCrop = new JLabel("  Crop Settings (in pixels, 0-based) :   ");
		imageCrop.setBorder(BorderFactory.createTitledBorder(""));
		imageCrop.setFont(new Font(Font.DIALOG, Font.BOLD, 11));

		pwidth = new JSpinner(new SpinnerNumberModel(1.0, 0.0, 1000.0, 0.25));
		pwidth.setPreferredSize(new Dimension(65, 25));
		pheight = new JSpinner(new SpinnerNumberModel(1.0, 0.0, 1000.0, 0.25));
		pheight.setPreferredSize(new Dimension(65, 25));
		pXMin = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1000000000000.0, 1.0));
		pXMin.setPreferredSize(new Dimension(65, 25));
		pXMax = new JSpinner(new SpinnerNumberModel(1.0, 0.0, 1000000000000.0, 1.0));
		pXMax.setPreferredSize(new Dimension(65, 25));
		pYMin = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1000000000000.0, 1.0));
		pYMin.setPreferredSize(new Dimension(65, 25));
		pYMax = new JSpinner(new SpinnerNumberModel(1.0, 0.0, 1000000000000.0, 1.0));
		pYMax.setPreferredSize(new Dimension(65, 25));
		JLabel labelW = new JLabel("Pixel Width: ");
		JLabel labelH = new JLabel("Pixel Height: ");
		JLabel labelX = new JLabel("X : ");
		JLabel labelY = new JLabel("Y : ");
		JLabel labelPixelW = new JLabel("   μm ");
		JLabel labelPixelH = new JLabel("   μm ");
		JButton pviewButton = new JButton("");
		ImageIcon iconPreview = JFrameWizard.createImageIcon("images/preview.png");
		Icon PreviewCell = new ImageIcon(iconPreview.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
		pviewButton.setIcon(PreviewCell);
		pviewButton.setToolTipText("Click this button to make a preview for image size.");
		JButton pviewButton1 = new JButton("");
		pviewButton1.setIcon(PreviewCell);
		pviewButton1.setToolTipText("Click this button to make a preview for thresholding.");
		pviewButton1.setMaximumSize(new Dimension(170, 80));
		JPanel panelW = new JPanel(new FlowLayout());
		panelW.add(labelW);
		panelW.add(pwidth);
		panelW.add(labelPixelW);
		JPanel panelH = new JPanel(new FlowLayout());
		panelH.add(labelH);
		panelH.add(pheight);
		panelH.add(labelPixelH);
		JPanel panelX = new JPanel(new FlowLayout());
		panelX.add(labelX);
		panelX.add(pXMin);
		panelX.add(new JLabel("   to   "));
		panelX.add(pXMax);
		JPanel panelY = new JPanel(new FlowLayout());
		panelY.add(labelY);
		panelY.add(pYMin);
		panelY.add(new JLabel("   to   "));
		panelY.add(pYMax);

		JButton buttonRefresh = new JButton("");
		ImageIcon iconRefresh = JFrameWizard.createImageIcon("images/refresh.png");
		Icon iconRefreshCell = new ImageIcon(iconRefresh.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
		buttonRefresh.setIcon(iconRefreshCell);
		JButton buttonOpenImage = new JButton("");
		ImageIcon iconOpenImage = JFrameWizard.createImageIcon("images/openimage.png");
		Icon iconOpenImageCell = new ImageIcon(iconOpenImage.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
		buttonOpenImage.setIcon(iconOpenImageCell);
		JButton buttonBrowse = new JButton("");
		ImageIcon iconBrowse = JFrameWizard.createImageIcon("images/browse.png");
		Icon iconBrowseCell = new ImageIcon(iconBrowse.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
		buttonBrowse.setIcon(iconBrowseCell);
		textImages = (TextField) new TextField(15);
		textImages.setText(prefImages.get(CELLTYPEANALYZER_IMAGES_DEFAULT_PATH, ""));
		DirectoryListener listenerImages = new DirectoryListener("Browse for movies...  ", textImages,
				JFileChooser.FILES_AND_DIRECTORIES);
		buttonBrowse.addActionListener(listenerImages);
		JPanel panelImagesDirect = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelImagesDirect.add(textImages);
		panelImagesDirect.add(buttonBrowse);

		JPanel panelCal = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel panelCrop = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel panelPicture = new JPanel();
		panelPicture.setLayout(new BoxLayout(panelPicture, BoxLayout.Y_AXIS));
		Border loweredbevel = BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(),
				BorderFactory.createLoweredBevelBorder());
		JPanel bLabel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		bLabel.add(imageLabel);
		bLabel.add(pviewButton);
		JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
		JSeparator separator1 = new JSeparator(SwingConstants.VERTICAL);
		Dimension dime = separator.getPreferredSize();
		dime.height = pviewButton.getPreferredSize().height;
		separator.setPreferredSize(dime);
		separator1.setPreferredSize(dime);
		bLabel.add(separator);
		bLabel.add(panelImagesDirect);
		bLabel.add(buttonRefresh);
		bLabel.add(separator1);
		bLabel.add(buttonOpenImage);
		panelPicture.add(bLabel);
		tableImages = new JTable();
		modelImages = new DefaultTableModel();
		tableImages.setModel(modelImages);
		jScrollPaneImages = new JScrollPane(tableImages);
		jScrollPaneImages.setPreferredSize(new Dimension(655, 200));
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		imagePanel.add(jScrollPaneImages);
		panelPicture.add(imagePanel);
		panelPicture.add(Box.createVerticalStrut(3));
		panelPicture.add(bLabel);
		panelCal.add(imageCal);
		panelCrop.add(imageCrop);

		JLabel channelLabelSet = new JLabel("  Marker-Channel Matching :   ");
		channelLabelSet.setBorder(BorderFactory.createTitledBorder(""));
		channelLabelSet.setFont(new Font(Font.DIALOG, Font.BOLD, 11));
		JLabel channe1 = new JLabel("          ▪  Single-Cell Detection on Marker I :   ");
		JLabel channe2 = new JLabel("          ▪  Cell-Type Customization on Marker II :   ");
		JLabel channe3 = new JLabel("          ▪  Cell-Type Customization on Marker III :   ");
		JLabel labelCh1 = new JLabel("   ▪  Label:   ");
		JLabel labelCh2 = new JLabel("   ▪  Label:   ");
		JLabel labelCh3 = new JLabel("   ▪  Label:   ");
		tfCh1 = new JTextField(10);
		tfCh2 = new JTextField(10);
		tfCh3 = new JTextField(10);

		CHANNEL1_TEXT = "channel1_text";
		CHANNEL2_TEXT = "channel2_text";
		CHANNEL3_TEXT = "channel3_text";

		prefs1 = Preferences.userRoot();
		prefs2 = Preferences.userRoot();
		prefs3 = Preferences.userRoot();

		tfCh1.setText(prefs1.get(CHANNEL1_TEXT, ""));
		tfCh2.setText(prefs2.get(CHANNEL2_TEXT, ""));
		tfCh3.setText(prefs3.get(CHANNEL3_TEXT, ""));

		comboCh1 = new JComboBox();
		comboCh2 = new JComboBox();
		comboCh3 = new JComboBox();
		String[] comboItems = new String[] { "Red", "Green", "Blue" };
		for (int i = 0; i < comboItems.length; i++) {
			comboCh1.addItem(comboItems[i]);
			comboCh2.addItem(comboItems[i]);
			comboCh3.addItem(comboItems[i]);
		}

		comboCh1.setSelectedIndex(0);
		comboCh2.setSelectedIndex(1);
		comboCh3.setSelectedIndex(2);

		JPanel ch1Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		ch1Panel.add(channe1);
		ch1Panel.add(comboCh1);
		ch1Panel.add(labelCh1);
		ch1Panel.add(tfCh1);
		JPanel ch2Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		ch2Panel.add(channe2);
		ch2Panel.add(comboCh2);
		ch2Panel.add(labelCh2);
		ch2Panel.add(tfCh2);
		JPanel ch3Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		ch3Panel.add(channe3);
		ch3Panel.add(comboCh3);
		ch3Panel.add(labelCh3);
		ch3Panel.add(tfCh3);
		JPanel boxChannel = new JPanel();
		boxChannel.setLayout(new BoxLayout(boxChannel, BoxLayout.Y_AXIS));
		JPanel channelLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		channelLabelPanel.add(channelLabelSet);
		boxChannel.add(channelLabelPanel);
		boxChannel.add(Box.createVerticalStrut(3));
		boxChannel.add(ch1Panel);
		boxChannel.add(Box.createVerticalStrut(3));
		boxChannel.add(ch2Panel);
		boxChannel.add(Box.createVerticalStrut(3));
		boxChannel.add(ch3Panel);
		boxChannel.add(Box.createVerticalStrut(3));
		add(panelPicture);
		add(Box.createVerticalStrut(5));
		add(boxChannel);
		add(Box.createVerticalStrut(5));
		add(panelCal);
		add(Box.createVerticalStrut(3));
		add(panelW);
		add(Box.createVerticalStrut(3));
		add(panelH);
		add(Box.createVerticalStrut(5));
		add(panelCrop);
		add(Box.createVerticalStrut(3));
		add(panelX);
		add(Box.createVerticalStrut(3));
		add(panelY);
		Border border = BorderFactory.createLoweredBevelBorder();
		setBorder(border);
		add(Box.createVerticalStrut(5));
		boxPanel = new JPanel();
		boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));
		chartPanel = new JPanel();
		hs = new HistogramSenescence2();
		chartPanel.add(hs.createChartPanel(imp));
		boxPanel.add(chartPanel);
		controlPanel = hs.createControlPanel();
		boxPanel.add(controlPanel);
		add(boxPanel);

		channelLabel = new JLabel();
		channelLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 11));
		channelLabel.setBorder(BorderFactory.createTitledBorder(""));
		iconImage = new JLabel();
		iconImage.setBorder(loweredbevel);
		JPanel firstPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel channelPanel = new JPanel();
		channelPanel.setLayout(new BoxLayout(channelPanel, BoxLayout.Y_AXIS));
		channelPanel.add(iconImage);
		channelPanel.add(Box.createVerticalStrut(8));
		channelPanel.add(channelLabel);
		firstPanel.add(channelPanel);
		JLabel cellLabel = new JLabel(" Cell Segmentation and Feature Extraction on Marker I");
		cellLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 11));
		cellLabel.setBorder(BorderFactory.createTitledBorder(""));
		JPanel boxCell = new JPanel();
		boxCell.setLayout(new BoxLayout(boxCell, BoxLayout.Y_AXIS));
		JLabel thresholdlLabel = new JLabel("Auto-Threshold Global Method : ");
		thresholdMeths = new JComboBox<String>();
		String[] thresholds = new String[] { "Default", "Huang", "Huang2", "Intermodes", "Li", "MaxEntropy", "Mean",
				"MinError(I)", "Minimum", "Otsu", "RenyiEntropy", "Shanbhag", "Triangle", "Yen" };
		for (int i = 0; i < thresholds.length; i++)
			thresholdMeths.addItem(thresholds[i]);
		JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		comboPanel.add(thresholdlLabel);
		comboPanel.add(thresholdMeths);
		checkWater = new JCheckBox("Watershed");
		comboPanel.add(checkWater);
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
		JButton scriptButton = new JButton("Script");
		JPanel cellPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		cellPanel.add(cellLabel);
		JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelButtons.add(pviewButton1);
		panelButtons.add(zoomButton);
		panelButtons.add(showLabelsButton);
		panelButtons.add(scriptButton);
		JPanel panelOptions = new JPanel();
		panelOptions.setLayout(new BoxLayout(panelOptions, BoxLayout.Y_AXIS));
		panelOptions.add(cellPanel);
		panelOptions.add(comboPanel);
		panelOptions.add(panelButtons);
		JPanel panelNext = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelNext.add(firstPanel);
		panelNext.add(panelOptions);

		nextPage.add(panelNext);
		nextPage.add(Box.createVerticalStrut(5));
		table = new JTable();
		tableS = new JTable();
		model = new DefaultTableModel();
		modelS = new DefaultTableModel();
		table.setModel(model);
		tableS.setModel(modelS);
		JScrollPane jScrollPane = new JScrollPane(table);
		jScrollPane.setMaximumSize(new Dimension(1000, 270));
		jScrollPane.setPreferredSize(new Dimension(0, 270));
		nextPage.add(jScrollPane);
		nextPage.add(Box.createVerticalStrut(5));
		sumLabel = new JLabel();
		sumLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 11));
		sumLabel.setBorder(BorderFactory.createTitledBorder(""));
		JPanel sumPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		sumPanel.add(sumLabel);
		comboSumParam = new JComboBox<String>();
		for (int i = 0; i < itemFilters.length; i++)
			comboSumParam.addItem(itemFilters[i]);
		JPanel sumParamPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		sumParamPanel.add(comboSumParam);
		sumPanel.add(sumParamPanel);
		nextPage.add(sumPanel);
		nextPage.add(Box.createVerticalStrut(5));
		JScrollPane jScrollPaneS = new JScrollPane(tableS);
		jScrollPaneS.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jScrollPaneS.setMaximumSize(new Dimension(1000, 83));
		jScrollPaneS.setPreferredSize(new Dimension(0, 83));
		nextPage.add(jScrollPaneS);
		nextPage.add(Box.createVerticalStrut(8));
		filterLabel = new JLabel(" Filter Settings for : ");
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
		controlPanel2 = hs2.createControlPanel();
		boxPanel2.add(controlPanel2);

		JPanel filtersMain = new JPanel();
		filtersMain.setLayout(new BoxLayout(filtersMain, BoxLayout.Y_AXIS));
		filtersMain.add(filterPanel);
		filtersMain.add(boxPanel2);
		filtersMain.add(filtersMin);
		filtersMain.add(filtersMax);

		JLabel filterFeatureL = new JLabel(" Feature Filters:  ");
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
		JScrollPane scrollFilterList = new JScrollPane(filterList);
		scrollFilterList.setPreferredSize(new Dimension(190, 150));
		listButtons.add(scrollFilterList);
		listButtons.add(boxMPButtons);

		JPanel filterFeature = new JPanel(new FlowLayout(FlowLayout.LEFT));
		filterFeature.add(filterFeatureL);
		filterFeature.add(comboFilters);
		JPanel boxFilter = new JPanel();
		boxFilter.setLayout(new BoxLayout(boxFilter, BoxLayout.Y_AXIS));
		JButton filterButton = new JButton("");
		ImageIcon iconFilter = JFrameWizard.createImageIcon("images/filter.png");
		Icon filterCell = new ImageIcon(iconFilter.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
		filterButton.setIcon(filterCell);
		filterButton.setToolTipText("Click this button to filter your spot detections.");
		JButton resetFilterButton = new JButton("");
		ImageIcon iconResetFilter = JFrameWizard.createImageIcon("images/resetfilter.png");
		Icon resetFilterCell = new ImageIcon(iconResetFilter.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
		resetFilterButton.setIcon(resetFilterCell);
		resetFilterButton.setToolTipText("Click this button to reset your filtering selection.");
		boxFilter.add(filterFeature);
		boxFilter.add(Box.createVerticalStrut(1));
		boxFilter.add(listButtons);
		boxFilter.add(Box.createVerticalStrut(3));
		JPanel buttonFilter = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonFilter.add(filterButton);
		buttonFilter.add(resetFilterButton);
		boxFilter.add(buttonFilter);

		JPanel filterDef = new JPanel(new FlowLayout(FlowLayout.LEFT));
		filterDef.add(filtersMain);
		filterDef.add(boxFilter);

		nextPage.add(filterDef);

		scriptButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ScriptCustomizable_().scriptGUI();
			}
		});
		buttonRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshAction();
			}
		});
		buttonOpenImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				List<ImagePlus> impAnalClose = new ArrayList<ImagePlus>();
				int[] IDs = WindowManager.getIDList();
				if (IDs != null)
					for (int i = 0; i < IDs.length; i++)
						impAnalClose.add(WindowManager.getImage(IDs[i]));

				if (tableImages.getSelectedRow() != -1) {
					if (IDs != null)
						for (int i = 0; i < IDs.length; i++)
							impAnalClose.get(i).hide();
					imp = imps[tableImages.getSelectedRow()];
				}
				if (imp == null)
					IJ.error("Please, select an image within the main directory.");
				if (imp != null) {
					imp.show();
					title = imp.getTitle();
					imageLabel.setText(title);
					pXMax.setValue(imp.getWidth());
					pYMax.setValue(imp.getHeight());
					pwidth.setValue(imp.getCalibration().pixelWidth);
					pheight.setValue(imp.getCalibration().pixelHeight);
					channelLabel.setText(" Marker I : " + tfCh1.getText() + " ");
					sumLabel.setText(" Summary for " + tfCh1.getText() + ": ");
					hs.addSeries(imp);
					hs.refreshControlPanel();
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

		pviewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				previewTool();
			}
		});
		pviewButton1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				preview1Tool();
				previewButton = "previewButton";
			}
		});

		JFrameWizard.nextButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nextTool();

			}
		});
		table.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(final MouseEvent e) {
				int indexSelected = table.getSelectedRow();
				roiManager = RoiManager.getInstance();
				roiManager.select(indexSelected);

			}
		});

		JFrameWizard.printButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				PrintTable pt = new PrintTable(table, "Senescence Table:", "- {0} -", true);
				pt.utilJTablePrint();

			}
		});

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
				impMontage.setSlice(1);
				roiManager = RoiManager.getInstance();
				if (state == ItemEvent.SELECTED) {
					roiManager.runCommand(impMontage, "Show All with labels");

				} else {
					roiManager.runCommand(impMontage, "Show All without labels");

				}
			}
		});

		selectedTh = (String) thresholdMeths.getSelectedItem();
		if (selectedTh.equals("Default") == true)
			selectedItem = "Default";
		if (selectedTh.equals("Huang") == true)
			selectedItem = "Huang";
		if (selectedTh.equals("Huang2") == true)
			selectedItem = "Huang2";
		if (selectedTh.equals("Intermodes") == true)
			selectedItem = "Intermodes";
		if (selectedTh.equals("Li") == true)
			selectedItem = "Li";
		if (selectedTh.equals("MaxEntropy") == true)
			selectedItem = "MaxEntropy";
		if (selectedTh.equals("Mean") == true)
			selectedItem = "Mean";
		if (selectedTh.equals("MinError(I)") == true)
			selectedItem = "MinError(I)";
		if (selectedTh.equals("Minimum") == true)
			selectedItem = "Minimum";
		if (selectedTh.equals("Otsu") == true)
			selectedItem = "Otsu";
		if (selectedTh.equals("RenyiEntropy") == true)
			selectedItem = "RenyiEntropy";
		if (selectedTh.equals("Shanbhag") == true)
			selectedItem = "Shanbhag";
		if (selectedTh.equals("Triangle") == true)
			selectedItem = "Triangle";
		if (selectedTh.equals("Yen") == true)
			selectedItem = "Yen";

		filterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (previewButton != null)
					filterTool();

			}
		});
		resetFilterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (previewButton != null)
					resetFilter();

			}
		});

		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				List<String> listFilters = new ArrayList<String>();

				if (filterList.getModel().getSize() < 1)
					modelList.addElement((String) comboFilters.getSelectedItem() + ":  [" + filterMin.getValue() + ","
							+ filterMax.getValue() + "]");

				if (filterList.getModel().getSize() >= 1) {
					for (int i = 0; i < filterList.getModel().getSize(); i++)
						listFilters.add(String.valueOf(filterList.getModel().getElementAt(i).substring(0,
								filterList.getModel().getElementAt(i).lastIndexOf(":"))));

					if (listFilters.contains(comboFilters.getSelectedItem().toString()) == false)
						modelList.addElement((String) comboFilters.getSelectedItem() + ":  [" + filterMin.getValue()
								+ "," + filterMax.getValue() + "]");

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
	}

	@SuppressWarnings("deprecation")
	public void filterTool() {

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

			roiManager = RoiManager.getInstance();
			roiManager.reset();
			for (int i = 0; i < rois.length; i++)
				roiManager.addRoi(rois[i]);
			rowSorterR = new TableRowSorter<>(table.getModel());
			table.setRowSorter(rowSorterR);
			List<RowFilter<TableModel, Integer>> low = new ArrayList<>();
			List<RowFilter<TableModel, Integer>> high = new ArrayList<>();
			for (int i = 0; i < indexesComboFilters.size(); i++) {
				low.add(RowFilter.numberFilter(RowFilter.ComparisonType.AFTER,
						Double.parseDouble(listFiltersMin.get(i)),
						table.convertColumnIndexToModel(indexesComboFilters.get(i) + 2)));
				high.add(RowFilter.numberFilter(RowFilter.ComparisonType.BEFORE,
						Double.parseDouble(listFiltersMax.get(i)),
						table.convertColumnIndexToModel(indexesComboFilters.get(i) + 2)));
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
			roisNameTable = new ArrayList<String>();
			List<String> roisNameTableIndex = new ArrayList<String>();
			for (int i = 0; i < rois.length; i++)
				roisNameRM.add(rois[i].getName());

			roiManager = RoiManager.getInstance();
			roiManager.reset();

			for (int i = 0; i < table.getRowCount(); i++) {
				roisNameTable.add((String) table.getModel().getValueAt(table.convertRowIndexToModel(i),
						table.convertColumnIndexToModel(1)));
				roisNameTableIndex.add(roisNameTable.get(i).substring(0, 4));
				roiManager.add(rois[Integer.parseInt(roisNameTableIndex.get(i)) - 1],
						Integer.parseInt(roisNameTableIndex.get(i)) - 1);

			}

			List<Roi> roisList = new ArrayList<Roi>();
			for (int i = 0; i < table.getRowCount(); i++)
				roisList.add(rois[Integer.parseInt(roisNameTableIndex.get(i)) - 1]);

			roisFiltered = new Roi[roisList.size()];
			roisList.toArray(roisFiltered);

			if (table.getRowCount() >= 1) {

				List<Double> listFiltered = new ArrayList<Double>();
				for (int row = 0; row < table.getRowCount(); row++)
					listFiltered.add((Double) table.getModel().getValueAt(table.convertRowIndexToModel(row),
							table.convertColumnIndexToModel(comboSumParam.getSelectedIndex() + 2)));
				modelS.setValueAt(tfCh1.getText(), 0, tableS.convertColumnIndexToModel(0));
				modelS.setValueAt(listFiltered.size(), 0, tableS.convertColumnIndexToModel(1));
				modelS.setValueAt(Math.round(Statistics.sum(listFiltered) * 1000.0) / 1000.0, 0,
						tableS.convertColumnIndexToModel(2));
				modelS.setValueAt(Math.round(Statistics.mean(listFiltered) * 1000.0) / 1000.0, 0,
						tableS.convertColumnIndexToModel(3));
				modelS.setValueAt(Math.round(Statistics.median(listFiltered) * 1000.0) / 1000.0, 0,
						tableS.convertColumnIndexToModel(4));
				modelS.setValueAt(Math.round(Statistics.variance(listFiltered) * 1000.0) / 1000.0, 0,
						tableS.convertColumnIndexToModel(5));
				modelS.setValueAt(Math.round(Statistics.sd(listFiltered) * 1000.0) / 1000.0, 0,
						tableS.convertColumnIndexToModel(6));
				modelS.setValueAt(Math.round(Collections.min(listFiltered) * 1000.0) / 1000.0, 0,
						tableS.convertColumnIndexToModel(7));
				modelS.setValueAt(Math.round(Collections.max(listFiltered) * 1000.0) / 1000.0, 0,
						tableS.convertColumnIndexToModel(8));
				modelS.setValueAt(Math.round(Statistics.Q1(listFiltered, listFiltered.size()) * 1000.0) / 1000.0, 0,
						tableS.convertColumnIndexToModel(9));
				modelS.setValueAt(Math.round(Statistics.Q3(listFiltered, listFiltered.size()) * 1000.0) / 1000.0, 0,
						tableS.convertColumnIndexToModel(10));
				modelS.setValueAt(Math.round(Statistics.IQR(listFiltered, listFiltered.size()) * 1000.0) / 1000.0, 0,
						tableS.convertColumnIndexToModel(11));
				for (int i = 0; i < roiManager.getCount(); i++)
					roiManager.rename(i, roisNameTable.get(i));

				roiManager.runCommand("UseNames", "true");

			}
			if (pickerChecked != null && pickerChecked.equals("pickerChecked") == true) {

				Field listField = null;
				try {
					listField = RoiManager.class.getDeclaredField("list");
				} catch (NoSuchFieldException | SecurityException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				listField.setAccessible(true);
				try {
					listRoiManager = (JList<?>) listField.get(roiManager);
				} catch (IllegalArgumentException | IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				listRoiManager.addListSelectionListener(new ListSelectionListener() {

					@Override
					public void valueChanged(ListSelectionEvent listSelectionEvent) {

						boolean adjust = listSelectionEvent.getValueIsAdjusting();

						if (!adjust) {
							JList<?> list = (JList<?>) listSelectionEvent.getSource();
							int selection = list.getSelectedIndex();
							table.clearSelection();
							table.addRowSelectionInterval(selection, selection);
							JViewport viewport = (JViewport) table.getParent();
							Rectangle rect = table.getCellRect(selection, 0, true);
							Rectangle r2 = viewport.getVisibleRect();
							table.scrollRectToVisible(
									new Rectangle(rect.x, rect.y, (int) r2.getWidth(), (int) r2.getHeight()));
						}
					}

				});
			}
			MiddlePageModified2.rois = roisFiltered;
			MiddlePageModified.rois = roisFiltered;
			MiddlePageModified.roisName = roisNameTable;
			EndPageModified.rois = roisFiltered;
			EndPageModified.roisName = roisNameTable;
		}

	}

	public void resetFilter() {

		TableRowSorter<TableModel> rowReset = new TableRowSorter<>(model);
		table.setRowSorter(rowReset);

		List<Double> values = new ArrayList<Double>();
		for (int i = 0; i < model.getRowCount(); i++)
			values.add((Double) model.getValueAt(i,
					table.convertColumnIndexToModel(comboSumParam.getSelectedIndex() + 2)));

		modelS.setValueAt(tfCh1.getText(), 0, tableS.convertColumnIndexToModel(0));
		modelS.setValueAt(model.getRowCount(), 0, tableS.convertColumnIndexToModel(1));
		modelS.setValueAt(Math.round(Statistics.sum(values) * 1000.0) / 1000.0, 0, tableS.convertColumnIndexToModel(2));
		modelS.setValueAt(Math.round(Statistics.mean(values) * 1000.0) / 1000.0, 0,
				tableS.convertColumnIndexToModel(3));
		modelS.setValueAt(Math.round(Statistics.median(values) * 1000.0) / 1000.0, 0,
				tableS.convertColumnIndexToModel(4));
		modelS.setValueAt(Math.round(Statistics.variance(values) * 1000.0) / 1000.0, 0,
				tableS.convertColumnIndexToModel(5));
		modelS.setValueAt(Math.round(Statistics.sd(values) * 1000.0) / 1000.0, 0, tableS.convertColumnIndexToModel(6));
		modelS.setValueAt(Math.round(Collections.min(values) * 1000.0) / 1000.0, 0,
				tableS.convertColumnIndexToModel(7));
		modelS.setValueAt(Math.round(Collections.max(values) * 1000.0) / 1000.0, 0,
				tableS.convertColumnIndexToModel(8));
		modelS.setValueAt(Math.round(Statistics.Q1(values, values.size()) * 1000.0) / 1000.0, 0,
				tableS.convertColumnIndexToModel(9));
		modelS.setValueAt(Math.round(Statistics.Q3(values, values.size()) * 1000.0) / 1000.0, 0,
				tableS.convertColumnIndexToModel(10));
		modelS.setValueAt(Math.round(Statistics.IQR(values, values.size()) * 1000.0) / 1000.0, 0,
				tableS.convertColumnIndexToModel(11));

		roiManager = RoiManager.getInstance();
		roiManager.reset();
		for (int i = 0; i < rois.length; i++)
			roiManager.addRoi(rois[i]);

		for (int i = 0; i < roiManager.getCount(); i++)
			roiManager.rename(i, roisNames.get(i));

		roiManager.runCommand("UseNames", "true");
		roiManager = RoiManager.getInstance();

		MiddlePageModified.rois = rois;
		MiddlePageModified2.rois = rois;
		MiddlePageModified.roisName = roisNames;
		EndPageModified.rois = rois;
		EndPageModified.roisName = roisNames;
	}

	public void preview1Tool() {

		IJ.run("Set Measurements...",
				"area mean standard modal min centroid center perimeter bounding fit shape feret's integrated median skewness kurtosis area_fraction stack display redirect=None decimal=3");

		if (impMontage != null)
			impMontage.setSlice(1);

		if (roiManager != null)
			roiManager.close();
		roiManager = new RoiManager();
		if (channelAnalShow != null)
			channelAnalShow.hide();

		channelAnalShow = channels[comboCh1.getSelectedIndex()].duplicate();
		channelAnal = channels[comboCh1.getSelectedIndex()].duplicate();
		if (ScriptCustomizable_.linesAsAL != null) {

			if (ScriptCustomizable_.comboIndex == 0) {
				Interpreter.activateImage(channelAnal);
				for (int i = 0; i < ScriptCustomizable_.linesAsAL.size(); i++) {
					List<String> linesAsALSegment = Arrays.asList(ScriptCustomizable_.linesAsAL.get(i).split(","));
					IJ.run(channelAnal, linesAsALSegment.get(0).replace("run(", "").replace("\"", ""),
							linesAsALSegment.get(1).replace(");", "").replace("\"", ""));

				}

			}
			ScriptCustomizable_.linesAsAL = null;
		}

		if (selectedItem.equals("Default") == true)
			IJ.run(channelAnal, "Auto Threshold", "method=Default white");
		if (selectedItem.equals("Huang") == true)
			IJ.run(channelAnal, "Auto Threshold", "method=Huang white");
		if (selectedItem.equals("Huang2") == true)
			IJ.run(channelAnal, "Auto Threshold", "method=Huang2 white");
		if (selectedItem.equals("Intermodes") == true)
			IJ.run(channelAnal, "Auto Threshold", "method=Intermodes white");
		if (selectedItem.equals("Li") == true)
			IJ.run(channelAnal, "Auto Threshold", "method=Li white");
		if (selectedItem.equals("MaxEntropy") == true)
			IJ.run(channelAnal, "Auto Threshold", "method=MaxEntropy white");
		if (selectedItem.equals("Mean") == true)
			IJ.run(channelAnal, "Auto Threshold", "method=Mean white");
		if (selectedItem.equals("MinError(I)") == true)
			IJ.run(channelAnal, "Auto Threshold", "method=MinError(I) white");
		if (selectedItem.equals("Minimum") == true)
			IJ.run(channelAnal, "Auto Threshold", "method=Minimum white");
		if (selectedItem.equals("Otsu") == true)
			IJ.run(channelAnal, "Auto Threshold", "method=Otsu white");
		if (selectedItem.equals("RenyiEntropy") == true)
			IJ.run(channelAnal, "Auto Threshold", "method=RenyiEntropy white");
		if (selectedItem.equals("Shanbhag") == true)
			IJ.run(channelAnal, "Auto Threshold", "method=Shanbhag white");
		if (selectedItem.equals("Triangle") == true)
			IJ.run(channelAnal, "Auto Threshold", "method=Triangle white");
		if (selectedItem.equals("Yen") == true)
			IJ.run(channelAnal, "Auto Threshold", "method=Yen white");
		if (checkWater.isSelected() == Boolean.TRUE) {
			IJ.run(channelAnal, "Invert LUT", "");
			IJ.run(channelAnal, "Watershed", "");

		}

		IJ.run(channelAnal, "Analyze Particles...", "size=5-2147483647 exclude clear add");
		rois = roiManager.getRoisAsArray();
		ResultsTable rt = null;

		Analyzer aSys = new Analyzer(channels[comboCh1.getSelectedIndex()]); // System Analyzer
		ResultsTable rtSys = Analyzer.getResultsTable();
		ResultsTable rtMulti = new ResultsTable(rois.length - 1);
		rtMulti.showRowNumbers(true);
		rtSys.reset();

		rtMulti.incrementCounter();
		int roiIndex = 0;
		for (int i = 0; i < rois.length; i++) {
			channels[comboCh1.getSelectedIndex()].setRoi(rois[i]);
			roiIndex++;
			aSys.measure();
			for (int j = 0; j <= rtSys.getLastColumn(); j++) {
				float[] col = rtSys.getColumn(j);
				String head = rtSys.getColumnHeading(j);
				String suffix = "" + roiIndex;
				Roi roi = channels[comboCh1.getSelectedIndex()].getRoi();
				if (roi != null) {
					String name = roi.getName();
					if (name != null && name.length() > 0 && (name.length() < 9 || !Character.isDigit(name.charAt(0))))
						suffix = "(" + name + ")";
				}
				if (head != null && col != null && !head.equals("Slice"))
					rtMulti.setValue(head, i, rtSys.getValue(j, rtSys.getCounter() - 1));

			}
		}

		rt = rtMulti;

		int[] rmIndexes = null;
		if (roiManager != null)
			rmIndexes = roiManager.getIndexes();
		ImageProcessor[] imageArray = new ImageProcessor[rmIndexes.length];
		ImagePlus[] impbChannel = new ImagePlus[rmIndexes.length];
		impbs = new ImagePlus[rmIndexes.length];
		for (int u = 0; u < rmIndexes.length; u++) {
			impbChannel[u] = imp;
			imageArray[u] = impbChannel[u].getProcessor();
			Roi[] roisPrueba = roiManager.getRoisAsArray();
			imageArray[u].setRoi(roisPrueba[u]);
			impbs[u] = new ImagePlus(imp.getTitle(), imageArray[rmIndexes[u]].crop());

		}

		String[] columnHeaders = rt.getHeadings();
		dataList = new ArrayList<List<String>>();
		for (int r = 0; r < rt.size(); r++) {
			List<String> doubles = new ArrayList<String>();
			for (int c = 0; c < columnHeaders.length; c++)
				doubles.add(rt.getStringValue(columnHeaders[c], r));
			dataList.add(doubles);
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

		model = new DefaultTableModel(data, columnHeaders) {

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

		modelS = new DefaultTableModel(new Double[][] { {} }, columnHeadersS) {

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

		table.setModel(model);
		tableS.setModel(modelS);
		model.addColumn("Single Cell");
		model.addColumn("ID");
		table.moveColumn(table.getColumnCount() - 1, 0);
		table.moveColumn(table.getColumnCount() - 1, 0);
		table.setSelectionBackground(new Color(229, 255, 204));
		table.setSelectionForeground(new Color(0, 102, 0));
		tableS.setSelectionBackground(new Color(229, 255, 204));
		tableS.setSelectionForeground(new Color(0, 102, 0));

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(String.class, centerRenderer);
		table.setDefaultRenderer(Double.class, centerRenderer);
		tableS.setDefaultRenderer(Double.class, centerRenderer);
		tableS.setDefaultRenderer(String.class, centerRenderer);
		tableS.setDefaultRenderer(Integer.class, centerRenderer);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableS.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setRowHeight(40);
		tableS.setRowHeight(40);
		table.setAutoCreateRowSorter(true);
		tableS.setAutoCreateRowSorter(true);
		table.getTableHeader().setDefaultRenderer(new SimpleHeaderRenderer());
		tableS.getTableHeader().setDefaultRenderer(new SimpleHeaderRenderer());

		ImageIcon[] icons = new ImageIcon[model.getRowCount()];
		roisNames = new ArrayList<String>();
		for (int i = 0; i < model.getRowCount(); i++) {
			icons[i] = new ImageIcon(getScaledImage(impbs[i].getImage(), 70, 75));
			model.setValueAt(icons[i], i, table.convertColumnIndexToModel(0));
			model.setValueAt(rois[i].getName(), i, table.convertColumnIndexToModel(1));
			roisNames.add(rois[i].getName());

		}

		List<Double> values = new ArrayList<Double>();
		for (int i = 0; i < model.getRowCount(); i++)
			values.add((Double) model.getValueAt(i,
					table.convertColumnIndexToModel(comboSumParam.getSelectedIndex() + 2)));

		modelS.setValueAt(tfCh1.getText(), 0, tableS.convertColumnIndexToModel(0));
		modelS.setValueAt(model.getRowCount(), 0, tableS.convertColumnIndexToModel(1));
		modelS.setValueAt(Math.round(Statistics.sum(values) * 1000.0) / 1000.0, 0, tableS.convertColumnIndexToModel(2));
		modelS.setValueAt(Math.round(Statistics.mean(values) * 1000.0) / 1000.0, 0,
				tableS.convertColumnIndexToModel(3));
		modelS.setValueAt(Math.round(Statistics.median(values) * 1000.0) / 1000.0, 0,
				tableS.convertColumnIndexToModel(4));
		modelS.setValueAt(Math.round(Statistics.variance(values) * 1000.0) / 1000.0, 0,
				tableS.convertColumnIndexToModel(5));
		modelS.setValueAt(Math.round(Statistics.sd(values) * 1000.0) / 1000.0, 0, tableS.convertColumnIndexToModel(6));
		modelS.setValueAt(Math.round(Collections.min(values) * 1000.0) / 1000.0, 0,
				tableS.convertColumnIndexToModel(7));
		modelS.setValueAt(Math.round(Collections.max(values) * 1000.0) / 1000.0, 0,
				tableS.convertColumnIndexToModel(8));
		modelS.setValueAt(Math.round(Statistics.Q1(values, values.size()) * 1000.0) / 1000.0, 0,
				tableS.convertColumnIndexToModel(9));
		modelS.setValueAt(Math.round(Statistics.Q3(values, values.size()) * 1000.0) / 1000.0, 0,
				tableS.convertColumnIndexToModel(10));
		modelS.setValueAt(Math.round(Statistics.IQR(values, values.size()) * 1000.0) / 1000.0, 0,
				tableS.convertColumnIndexToModel(11));

		for (int u = 0; u < tableS.getColumnCount(); u++)
			tableS.getColumnModel().getColumn(u).setPreferredWidth(90);
		table.getColumnModel().getColumn(0).setPreferredWidth(140);
		table.getColumnModel().getColumn(1).setPreferredWidth(140);
		tableS.getColumnModel().getColumn(0).setPreferredWidth(140);

		MiddlePageModified.rois = rois;
		MiddlePageModified.roisName = roisNames;
		EndPageModified.rois = rois;
		EndPageModified.roisName = roisNames;
		MiddlePageModified2.rois = rois;

		roiManager.runCommand(channels[comboCh2.getSelectedIndex()], "Show All without labels");

	}

	public void previewTool() {
		int[] IDs = WindowManager.getIDList();
		if (IDs != null) {
			roi = imp.getRoi();

			if (roi == null) {

				xstart = 0;
				xend = imp.getWidth() - 1;
				ystart = 0;
				yend = imp.getHeight() - 1;
				pXMin.setValue(xstart);
				pXMax.setValue(xend);
				pYMin.setValue(ystart);
				pYMax.setValue(yend);
				impNext = imp;

			}

			if (roi != null) {

				Rectangle boundingRect = roi.getBounds();
				xstart = boundingRect.x;
				xend = boundingRect.width + boundingRect.x;
				ystart = boundingRect.y;
				yend = boundingRect.height + boundingRect.y;
				pXMin.setValue(xstart);
				pXMax.setValue(xend);
				pYMin.setValue(ystart);
				pYMax.setValue(yend);
				ImageProcessor impDup = imp.getProcessor();
				impDup.setRoi(roi);
				ImageProcessor impDupCrop = impDup.crop();
				imageToSet = new ImagePlus("", impDupCrop);
				impNext = imageToSet;

			}
		}

	}

	public void nextTool() {

		prefs1.put(CHANNEL1_TEXT, tfCh1.getText());
		prefs2.put(CHANNEL2_TEXT, tfCh2.getText());
		prefs1.put(CHANNEL3_TEXT, tfCh3.getText());

		List<ImagePlus> listImages = new ArrayList<ImagePlus>();

		if (imp == null) {
			IJ.error("You should select and open an image from your directory.");
		} else {
			if (impNext == null) {
				if (impMontage != null)
					impMontage.hide();
				if (imp.getCalibration().getUnit().equals("pixel") == Boolean.TRUE && (double) pwidth.getValue() != 1.00
						&& (double) pheight.getValue() != 1.00) {
					IJ.run(imp, "Set Scale...",
							String.format("distance=1 known=%f unit=microns global", (double) pwidth.getValue()));
					imp.getCalibration().setUnit("microns");
				}

				channels = ChannelSplitter.split(imp);
				listImages.add(imp.duplicate());
				for (int i = 0; i < channels.length; i++)
					listImages.add(channels[i]);
				arrayOfImages = new ImagePlus[listImages.size()];
				for (int i = 0; i < listImages.size(); i++)
					arrayOfImages[i] = listImages.get(i);
				listImages.toArray(arrayOfImages);
				impMontage = ImagesToStack.run(arrayOfImages);
				impMontage.setTitle(title);
				imp.hide();
				impMontage.show();

				MiddlePageModified.impMontage = impMontage;
				EndPageModified.impMontage = impMontage;

			}
			if (impNext != null) {
				if (impMontage != null)
					impMontage.hide();
				if (impNext.getCalibration().getUnit().equals("pixel") == Boolean.TRUE
						&& (double) pwidth.getValue() != 1.00 && (double) pheight.getValue() != 1.00) {
					IJ.run(impNext, "Set Scale...",
							String.format("distance=1 known=%f unit=microns global", (double) pwidth.getValue()));
					impNext.getCalibration().setUnit("microns");
				}
				channels = ChannelSplitter.split(impNext);
				listImages.add(impNext.duplicate());
				for (int i = 0; i < channels.length; i++)
					listImages.add(channels[i]);
				arrayOfImages = new ImagePlus[listImages.size()];
				for (int i = 0; i < listImages.size(); i++)
					arrayOfImages[i] = listImages.get(i);
				listImages.toArray(arrayOfImages);
				impMontage = ImagesToStack.run(arrayOfImages);
				impMontage.setTitle(title);
				imp.hide();
				impMontage.show();
			}

			iconImage.setIcon(new ImageIcon(
					channels[comboCh1.getSelectedIndex()].getImage().getScaledInstance(160, 135, Image.SCALE_SMOOTH)));
			channelLabel.setText(" Marker I : " + tfCh1.getText() + " ");
			sumLabel.setText(" Summary for " + tfCh1.getText() + " " + " :");
			filterLabel.setText(" Filter Settings for " + tfCh1.getText() + " ");
			MiddlePageModified.channelLabel.setText(" Marker II : " + tfCh2.getText() + " ");
			MiddlePageModified.sumLabel.setText(" Summary for " + tfCh2.getText() + " " + " :");
			MiddlePageModified.filterLabel.setText(" Filter Settings for " + tfCh2.getText() + " ");
			MiddlePageModified.iconImage.setIcon(new ImageIcon(
					channels[comboCh2.getSelectedIndex()].getImage().getScaledInstance(160, 135, Image.SCALE_SMOOTH)));
			MiddlePageModified.selectedIndexCh1 = comboCh1.getSelectedIndex();
			MiddlePageModified.selectedIndexCh2 = comboCh2.getSelectedIndex();
			EndPageModified.channelLabel.setText(" Marker III : " + tfCh3.getText() + "  ");
			EndPageModified.sumLabel.setText(" Summary for " + tfCh3.getText() + " : ");
			EndPageModified.filterLabel.setText(" Filter Settings for " + tfCh3.getText() + " : ");
			EndPageModified.iconImage.setIcon(new ImageIcon(
					channels[comboCh3.getSelectedIndex()].getImage().getScaledInstance(160, 135, Image.SCALE_SMOOTH)));
			EndPageModified.selectedIndexCh1 = comboCh1.getSelectedIndex();
			EndPageModified.selectedIndexCh3 = comboCh3.getSelectedIndex();

			MiddlePageModified2.labelCh2 = tfCh2.getText();
			MiddlePageModified2.labelCh3 = tfCh3.getText();
			MiddlePageModified2.scatLabel
					.setText(" Scatter Plot for " + tfCh2.getText() + " vs. " + tfCh3.getText() + " Markers :   ");
			MiddlePageModified2.channels = channels;
			MiddlePageModified2.selectedIndexCh2 = comboCh2.getSelectedIndex();
			MiddlePageModified2.selectedIndexCh3 = comboCh3.getSelectedIndex();

		}

	}

	public void refreshAction() {

		prefImages.put(CELLTYPEANALYZER_IMAGES_DEFAULT_PATH, textImages.getText());
		File imageFolder = new File(textImages.getText());
		File[] listOfFiles = imageFolder.listFiles();
		String[] imageTitles = new String[listOfFiles.length];
		imps = new ImagePlus[imageTitles.length];
		icons = new ImageIcon[imps.length];
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile())
				imageTitles[i] = listOfFiles[i].getName();
			imps[i] = IJ.openImage(textImages.getText() + File.separator + imageTitles[i]);
			icons[i] = new ImageIcon(getScaledImage(imps[i].getImage(), 90, 60));

		}
		Object[] columnNames = new Object[] { "Image", "Title", "Extension" };
		Object[][] dataTImages = new Object[imps.length][columnNames.length];
		for (int i = 0; i < dataTImages.length; i++)
			for (int j = 0; j < dataTImages[i].length; j++)
				dataTImages[i][j] = "";
		modelImages = new DefaultTableModel(dataTImages, columnNames) {

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
		tableImages.setModel(modelImages);
		tableImages.setSelectionBackground(new Color(229, 255, 204));
		tableImages.setSelectionForeground(new Color(0, 102, 0));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		tableImages.setDefaultRenderer(String.class, centerRenderer);
		tableImages.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableImages.setRowHeight(60);
		tableImages.setAutoCreateRowSorter(true);
		tableImages.getTableHeader().setDefaultRenderer(new SimpleHeaderRenderer());

		for (int i = 0; i < modelImages.getRowCount(); i++) {
			modelImages.setValueAt(icons[i], i, tableImages.convertColumnIndexToModel(0));
			modelImages.setValueAt(imps[i].getShortTitle(), i, tableImages.convertColumnIndexToModel(1));
			modelImages.setValueAt(imps[i].getTitle().substring(imps[i].getTitle().lastIndexOf(".")), i,
					tableImages.convertColumnIndexToModel(2));
		}
		tableImages.getColumnModel().getColumn(0).setPreferredWidth(100);
		tableImages.getColumnModel().getColumn(1).setPreferredWidth(450);
		tableImages.getColumnModel().getColumn(2).setPreferredWidth(100);

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

	public static String getTitle() {
		return title;
	}

	public static List<String> getRoisNameDapi() {
		List<String> roisNameTable2 = new ArrayList<String>();
		for (int i = 0; i < roisNameTable.size(); i++)
			roisNameTable2.add(roisNameTable.get(i));

		return roisNameTable2;
	}

	public static Image getScaledImage(Image srcImg, int w, int h) {
		BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = resizedImg.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(srcImg, 0, 0, w, h, null);
		g2.dispose();
		return resizedImg;
	}

}