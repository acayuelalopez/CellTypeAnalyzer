import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;
import java.util.regex.PatternSyntaxException;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.RowFilter.ComparisonType;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.Roi;
import ij.measure.ResultsTable;
import ij.plugin.ChannelSplitter;
import ij.plugin.RoiEnlarger;
import ij.plugin.filter.Analyzer;
import ij.plugin.filter.MaximumFinder;
import ij.plugin.frame.RoiManager;
import ij.process.ImageProcessor;

public class JFrameWizard extends JFrame implements Wizard {
	private static final long serialVersionUID = 2818290889333414291L;

	private static final Dimension defaultminimumSize = new Dimension(500, 500);

	private JPanel wizardPageContainer = new JPanel(new GridLayout(1, 1));
	private JButton finishButton = new JButton("");
	private JButton previousButton = new JButton("");
	static JButton nextButton = new JButton("");
	static JButton exportButton = new JButton("");
	static JButton printButton = new JButton(""), okButtonClass, cancelButtonClass;
	public String CELLTYPEANALYZER_XML_PATH = "xml_path";
	public String CELLTYPEANALYZER_SAVE_PATH = "save_path";
	public Preferences prefXml = Preferences.userRoot();
	public Preferences prefSave = Preferences.userRoot();
	public String thresholdmethod, filterparameter, minvalue, maxvalue, morphooperatorname2, morphooperatorvalue2,
			classname2, classcolor2, classparameters2, classminvalues2, classmaxvalues2, morphooperatorname3,
			morphooperatorvalue3, classname3, classcolor3, classparameters3, classminvalues3, classmaxvalues3,
			parameters, className;
	public RoiManager rm;
	NodeList nodeList1, nodeList2, nodeList3;
	private List<String> roisFilteredName, roisNames;
	static File[] listOfFiles;
	public JTable table0, table1, table2, tableFinal, tableCVersion;
	private List<Double> classCounterDefL, classCounterDef2L, counterPositiveDefL, counterTable0List;
	private JRadioButton csvFileB, excelFileB;
	static int l, selectedRow;
	static JTextArea taskOutput;
	public static Object[][] tableData1;
	Double[][] data1, data2;
	public List<String> tableHeading1 = new ArrayList<String>();
	private File directFolder;
	private JFrame frameClass;
	static TableModel modelC;

	/**
	 * Creates an <code>JFrameWizard</code> with a title and
	 * <code>GraphicsConfiguration</code>.
	 * 
	 * @param title the title of the frame
	 * @param gc    the <code>GraphicsConfiguration</code> of the frame
	 * @see JFrame
	 */
	public JFrameWizard(String title, GraphicsConfiguration gc) {
		super(title, gc);
		setupWizard();
	}

	/**
	 * Creates an <code>JFrameWizard</code> with a title.
	 * 
	 * @param title the title of the frame
	 * @see JFrame
	 */
	public JFrameWizard(String title) {
		super(title);
		setupWizard();
	}

	/**
	 * Creates an <code>JFrameWizard</code> with a {@link GraphicsConfiguration}.
	 * 
	 * @param gc the <code>GraphicsConfiguration</code> of the frame
	 * @see JFrame
	 */
	public JFrameWizard(GraphicsConfiguration gc) {
		super(gc);
		setupWizard();
	}

	/**
	 * Creates an <code>JFrameWizard</code>.
	 * 
	 * @see JFrame
	 */
	public JFrameWizard() {
		super();
		setupWizard();
	}

	/**
	 * Sets up wizard upon construction.
	 */
	private void setupWizard() {
		setupComponents();
		layoutComponents();

		setMinimumSize(defaultminimumSize);

		// Center on screen
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int xPosition = (screenSize.width / 2) - (defaultminimumSize.width / 2);
		int yPosition = (screenSize.height / 2) - (defaultminimumSize.height / 2);
		setLocation(xPosition, yPosition);
		//setSize(550, 700);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	/**
	 * Sets up the components of the wizard with listeners and mnemonics.
	 */
	private void setupComponents() {
		finishButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				if (RoiManager.getRoiManager() != null)
					RoiManager.getRoiManager().close();
				if (StartPageModified.impMontage != null)
					StartPageModified.impMontage.hide();
				if (StartPageModified.imp != null)
					StartPageModified.imp.hide();
				BatchModeGUI();
			}
		});

		exportButton.setMnemonic(KeyEvent.VK_A);
		ImageIcon iconExport = createImageIcon("images/export.png");
		Icon iconExportCell = new ImageIcon(iconExport.getImage().getScaledInstance(20, 19, Image.SCALE_SMOOTH));
		exportButton.setIcon(iconExportCell);
		exportButton.setToolTipText("Click this button to export table/summary.");
		exportButton.setMaximumSize(new Dimension(150, 50));

		printButton.setMnemonic(KeyEvent.VK_B);
		ImageIcon iconPrint = createImageIcon("images/print.png");
		Icon iconPrintCell = new ImageIcon(iconPrint.getImage().getScaledInstance(20, 19, Image.SCALE_SMOOTH));
		printButton.setIcon(iconPrintCell);
		printButton.setToolTipText("Click this button to print/save as PDF table/summary.");
		printButton.setMaximumSize(new Dimension(150, 50));

		finishButton.setMnemonic(KeyEvent.VK_C);
		ImageIcon iconFinish = createImageIcon("images/finish.png");
		Icon iconFinishCell = new ImageIcon(iconFinish.getImage().getScaledInstance(20, 19, Image.SCALE_SMOOTH));
		finishButton.setIcon(iconFinishCell);
		finishButton.setToolTipText("Click this button to finish this process.");
		finishButton.setMaximumSize(new Dimension(150, 50));
		previousButton.setMnemonic(KeyEvent.VK_P);
		ImageIcon iconPrevious = createImageIcon("images/next.png");
		Icon iconPreviousCell = new ImageIcon(iconPrevious.getImage().getScaledInstance(20, 19, Image.SCALE_SMOOTH));
		previousButton.setIcon(iconPreviousCell);
		previousButton.setToolTipText("Click this button to go back through wizards.");
		previousButton.setMaximumSize(new Dimension(150, 50));
		nextButton.setMnemonic(KeyEvent.VK_N);
		ImageIcon iconNext = createImageIcon("images/back.png");
		Icon iconNextCell = new ImageIcon(iconNext.getImage().getScaledInstance(20, 19, Image.SCALE_SMOOTH));
		nextButton.setIcon(iconNextCell);
		nextButton.setToolTipText("Click this button to go ahead through wizards.");
		nextButton.setMaximumSize(new Dimension(150, 50));

		wizardPageContainer.addContainerListener(new MinimumSizeAdjuster());
	}

	/**
	 * Lays out the components in the wizards content pane.
	 */
	private void layoutComponents() {
		GridBagLayout layout = new GridBagLayout();
		layout.rowWeights = new double[] { 1.0, 0.0, 0.0 };
		layout.columnWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0 };
		layout.rowHeights = new int[] { 0, 0, 0 };
		layout.columnWidths = new int[] { 0, 0, 0, 0, 0, 0 };
		getContentPane().setLayout(layout);

		GridBagConstraints wizardPageContainerConstraint = new GridBagConstraints();
		wizardPageContainerConstraint.gridwidth = 6;
		wizardPageContainerConstraint.fill = GridBagConstraints.BOTH;
		wizardPageContainerConstraint.gridx = 0;
		wizardPageContainerConstraint.gridy = 0;
		wizardPageContainerConstraint.insets = new Insets(5, 5, 5, 5);
		JScrollPane scrollPane = new JScrollPane(wizardPageContainer);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		getContentPane().add(scrollPane, wizardPageContainerConstraint);

		GridBagConstraints separatorConstraints = new GridBagConstraints();
		separatorConstraints.gridwidth = 6;
		separatorConstraints.fill = GridBagConstraints.HORIZONTAL;
		separatorConstraints.gridx = 0;
		separatorConstraints.gridy = 1;
		separatorConstraints.insets = new Insets(5, 5, 5, 5);
		getContentPane().add(new JSeparator(), separatorConstraints);

		GridBagConstraints exportButtonConstraints = new GridBagConstraints();
		exportButtonConstraints.gridx = 1;
		exportButtonConstraints.gridy = 2;
		exportButtonConstraints.insets = new Insets(5, 5, 5, 5);
		getContentPane().add(exportButton, exportButtonConstraints);

		GridBagConstraints printButtonConstraints = new GridBagConstraints();
		printButtonConstraints.gridx = 2;
		printButtonConstraints.gridy = 2;
		printButtonConstraints.insets = new Insets(5, 5, 5, 5);
		getContentPane().add(printButton, printButtonConstraints);

		GridBagConstraints finishButtonConstraints = new GridBagConstraints();
		finishButtonConstraints.gridx = 3;
		finishButtonConstraints.gridy = 2;
		finishButtonConstraints.insets = new Insets(5, 5, 5, 5);
		getContentPane().add(finishButton, finishButtonConstraints);

		GridBagConstraints previousButtonConstraints = new GridBagConstraints();
		previousButtonConstraints.gridx = 4;
		previousButtonConstraints.gridy = 2;
		previousButtonConstraints.insets = new Insets(5, 5, 5, 5);
		getContentPane().add(previousButton, previousButtonConstraints);

		GridBagConstraints nextButtonConstraints = new GridBagConstraints();
		nextButtonConstraints.gridx = 5;
		nextButtonConstraints.gridy = 2;
		nextButtonConstraints.insets = new Insets(5, 5, 5, 5);
		getContentPane().add(nextButton, nextButtonConstraints);

	}

	@Override
	public JPanel getWizardPageContainer() {
		return wizardPageContainer;
	}

	@Override
	public JButton getExportButton() {
		return exportButton;
	}

	@Override
	public JButton getPrintButton() {
		return printButton;
	}

	@Override
	public AbstractButton getFinishButton() {
		return finishButton;
	}

	@Override
	public JButton getPreviousButton() {
		return previousButton;
	}

	@Override
	public JButton getNextButton() {
		return nextButton;
	}

	private class MinimumSizeAdjuster implements ContainerListener {

		@Override
		public void componentAdded(ContainerEvent e) {
			Dimension currentSize = getSize();
			Dimension preferredSize = getPreferredSize();

			Dimension newSize = new Dimension(currentSize);
			newSize.width = Math.max(currentSize.width, preferredSize.width);
			newSize.height = Math.max(currentSize.height, preferredSize.height)-60;

			setMinimumSize(newSize);
			//setMinimumSize(new Dimension(500,400));
		}

		@Override
		public void componentRemoved(ContainerEvent e) {
		}

	}

	public static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = JFrameWizard.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	public void BatchModeGUI() {

		okButtonClass = new JButton("");
		ImageIcon iconOk = JFrameWizard.createImageIcon("images/ok.png");
		Icon iconOkCell = new ImageIcon(iconOk.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
		okButtonClass.setIcon(iconOkCell);
		cancelButtonClass = new JButton("");
		ImageIcon iconCancel = JFrameWizard.createImageIcon("images/cancel.png");
		Icon iconCancelCell = new ImageIcon(iconCancel.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
		cancelButtonClass.setIcon(iconCancelCell);
		JPanel panelOkCancel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panelOkCancel.add(okButtonClass);
		panelOkCancel.add(cancelButtonClass);
		JPanel totalPanel = new JPanel();
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
		frameClass = new JFrame();
		frameClass.add(totalPanel);
		frameClass.setTitle("Class Selection Viewer");
		frameClass.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frameClass.pack();
		counterTable0List = new ArrayList<Double>();
		classCounterDefL = new ArrayList<Double>();
		classCounterDef2L = new ArrayList<Double>();
		counterPositiveDefL = new ArrayList<Double>();
		final JFrame frameInitial = new JFrame("CellType-Analyzer:  Batch-Mode");
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, 1));
		buttonsPanel.add(Box.createVerticalStrut(8));
		final TextField textXml = new TextField(20);
		final TextField textSave = new TextField(20);
		textXml.setText(this.prefXml.get(this.CELLTYPEANALYZER_XML_PATH, ""));
		textSave.setText(this.prefSave.get(this.CELLTYPEANALYZER_SAVE_PATH, ""));
		ImageIcon iconXml = JFrameWizard.createImageIcon("images/xml.png");
		ImageIcon iconImages = JFrameWizard.createImageIcon("images/browse.png");
		ImageIcon iconClass = JFrameWizard.createImageIcon("images/classes.png");
		JButton buttonXml = new JButton("");
		JButton buttonImages = new JButton("");
		JButton buttonSave = new JButton("");
		JButton buttonClass = new JButton("");
		Icon iconXmlCell = new ImageIcon(iconXml.getImage().getScaledInstance(24, 26, 4));
		Icon iconImagesCell = new ImageIcon(iconImages.getImage().getScaledInstance(20, 22, 4));
		Icon iconClassCell = new ImageIcon(iconClass.getImage().getScaledInstance(20, 22, 4));
		buttonXml.setIcon(iconXmlCell);
		buttonImages.setIcon(iconImagesCell);
		buttonSave.setIcon(iconImagesCell);
		buttonClass.setIcon(iconClassCell);
		DirectoryListener listenerXml = new DirectoryListener("Browse for .Xml file ", textXml, 2);
		DirectoryListener listenerSave = new DirectoryListener("Browse for images to analyze", textSave, 2);
		buttonXml.addActionListener(listenerXml);
		buttonSave.addActionListener(listenerSave);
		JPanel panelXml = new JPanel();
		JPanel panelSave = new JPanel();
		JPanel panelExport = new JPanel();
		panelXml.setLayout(new FlowLayout(0));
		panelSave.setLayout(new FlowLayout(0));
		panelExport.setLayout(new FlowLayout(0));
		JLabel directXml = new JLabel("     .XML Analysis :   ");
		JRadioButton xmlCheck = new JRadioButton("");
		xmlCheck.setSelected(true);
		JLabel directSave = new JLabel("         Directoy to Export Results :   ");
		JLabel fileToExport = new JLabel("         File Format to export results :   ");
		directXml.setEnabled(true);
		textXml.setEnabled(true);
		buttonXml.setEnabled(true);
		directXml.setFont(new Font("Helvetica", 1, 12));
		directSave.setFont(new Font("Helvetica", 1, 12));
		fileToExport.setFont(new Font("Helvetica", 1, 12));
		panelXml.add(xmlCheck);
		panelXml.add(directXml);
		panelXml.add(textXml);
		panelXml.add(buttonXml);
		// panelDefault.add(defaultAnalysis);
		// panelDefault.add(defaultAnal);
		// panelDefault.add(buttonClass);
		panelExport.add(fileToExport);
		panelSave.add(directSave);
		panelSave.add(textSave);
		panelSave.add(buttonSave);
		JButton okButton = new JButton("");
		Icon okCell = new ImageIcon(iconOk.getImage().getScaledInstance(20, 20, 4));
		okButton.setIcon(okCell);
		okButton.setToolTipText("Click this button to get Batch-Mode Analysis.");
		JButton cancelButton = new JButton("");
		Icon cancelCell = new ImageIcon(iconCancel.getImage().getScaledInstance(20, 20, 4));
		cancelButton.setIcon(cancelCell);
		cancelButton.setToolTipText("Click this button to cancel Batch-Mode Analysis.");
		JPanel panelOptions = new JPanel(new FlowLayout(0));
		JPanel panelBox = new JPanel();
		panelBox.setLayout(new BoxLayout(panelBox, 1));
		this.csvFileB = new JRadioButton(".CSV file", true);
		this.excelFileB = new JRadioButton("EXCEL file");
		final JComboBox<String> comboExt = new JComboBox();
		comboExt.addItem(".xls");
		comboExt.addItem(".xlsx");
		ButtonGroup bgroup = new ButtonGroup();
		bgroup.add(this.csvFileB);
		bgroup.add(this.excelFileB);
		panelBox.add(panelExport);
		panelBox.add(this.excelFileB);
		panelBox.add(this.csvFileB);
		panelOptions.add(panelBox);
		panelOptions.add(comboExt);
		comboExt.setEnabled(false);
		JPanel mainPanel1 = new JPanel(new FlowLayout(0));
		mainPanel1.add(panelOptions);
		JPanel panelButtons = new JPanel(new FlowLayout(1));
		panelButtons.add(okButton);
		panelButtons.add(cancelButton);
		buttonsPanel.add(Box.createVerticalStrut(5));
		buttonsPanel.add(panelXml);
		buttonsPanel.add(Box.createVerticalStrut(5));
		// buttonsPanel.add(panelDefault);
		buttonsPanel.add(Box.createVerticalStrut(5));
		buttonsPanel.add(Box.createVerticalStrut(5));
		buttonsPanel.add(panelSave);
		buttonsPanel.add(mainPanel1);
		buttonsPanel.add(Box.createVerticalStrut(5));
		buttonsPanel.add(panelButtons);
		frameInitial.setSize(500, 350);
		frameInitial.add(buttonsPanel);
		frameInitial.setLocationRelativeTo((Component) null);
		frameInitial.setDefaultCloseOperation(2);
		frameInitial.setVisible(true);
		this.excelFileB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comboExt.setEnabled(true);
			}
		});

		xmlCheck.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					directXml.setEnabled(true);
					textXml.setEnabled(true);
					buttonXml.setEnabled(true);
				}
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					directXml.setEnabled(false);
					textXml.setEnabled(false);
					buttonXml.setEnabled(false);
				}

			}
		});

		buttonClass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tableCVersion.setModel(modelC);
				frameClass.setVisible(true);
			}
		});

		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {

				frameInitial.dispatchEvent(new WindowEvent(frameInitial, WindowEvent.WINDOW_CLOSING));
				Thread mainProcess = new Thread(new Runnable() {
					public void run() {
						File imageFolder = new File(StartPageModified.textImages.getText());
						listOfFiles = imageFolder.listFiles();
						String[] imageTitles = new String[listOfFiles.length];
						int MAX = listOfFiles.length;
						JFrame frame = new JFrame("Analyzing...");
						final JProgressBar pb = new JProgressBar();
						pb.setMinimum(0);
						pb.setMaximum(MAX);
						pb.setStringPainted(true);
						taskOutput = new JTextArea(5, 20);
						taskOutput.setMargin(new Insets(5, 5, 5, 5));
						taskOutput.setEditable(false);
						JPanel panel = new JPanel();
						panel.setLayout(new BoxLayout(panel, 1));
						panel.add(pb);
						panel.add(Box.createVerticalStrut(5));
						panel.add(new JScrollPane(taskOutput), "Center");
						panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
						frame.getContentPane().add(panel);
						frame.setDefaultCloseOperation(2);
						frame.setSize(700, 200);
						frame.setVisible(true);

						if (xmlCheck.isSelected() == Boolean.TRUE) {

							try {
								prefXml.put(CELLTYPEANALYZER_XML_PATH, textXml.getText());
								File file = new File(textXml.getText() + File.separator + "Parameters_file.xml");
								DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
								DocumentBuilder db = dbf.newDocumentBuilder();
								Document doc = db.parse(file);
								doc.getDocumentElement().normalize();
								nodeList1 = doc.getElementsByTagName("channelonecellcount");
								nodeList2 = doc.getElementsByTagName("channeltwoanalysis");
								nodeList3 = doc.getElementsByTagName("channelthreeanalysis");

								int i;
								Node node3;
								Element eElement3;
								for (i = 0; i < nodeList1.getLength(); ++i) {
									node3 = nodeList1.item(i);
									if (node3.getNodeType() == 1) {
										eElement3 = (Element) node3;
										thresholdmethod = eElement3.getElementsByTagName("thresholdmethod").item(0)
												.getTextContent();
										filterparameter = eElement3.getElementsByTagName("filterparameter").item(0)
												.getTextContent();
										minvalue = eElement3.getElementsByTagName("minvalue").item(0).getTextContent();
										maxvalue = eElement3.getElementsByTagName("maxvalue").item(0).getTextContent();
									}
								}

								for (i = 0; i < nodeList2.getLength(); ++i) {
									node3 = nodeList2.item(i);
									if (node3.getNodeType() == 1) {
										eElement3 = (Element) node3;
										morphooperatorname2 = eElement3.getElementsByTagName("morphooperatorname")
												.item(0).getTextContent();
										morphooperatorvalue2 = eElement3.getElementsByTagName("morphooperatorvalue")
												.item(0).getTextContent();
										classname2 = eElement3.getElementsByTagName("classname").item(0)
												.getTextContent();
										classcolor2 = eElement3.getElementsByTagName("classcolor").item(0)
												.getTextContent();
										classparameters2 = eElement3.getElementsByTagName("classparameters").item(0)
												.getTextContent();
										classminvalues2 = eElement3.getElementsByTagName("classminvalues").item(0)
												.getTextContent();
										classmaxvalues2 = eElement3.getElementsByTagName("classmaxvalues").item(0)
												.getTextContent();
									}
								}

								for (i = 0; i < nodeList3.getLength(); ++i) {
									node3 = nodeList3.item(i);
									if (node3.getNodeType() == 1) {
										eElement3 = (Element) node3;
										morphooperatorname3 = eElement3.getElementsByTagName("morphooperatorname")
												.item(0).getTextContent();
										morphooperatorvalue3 = eElement3.getElementsByTagName("morphooperatorvalue")
												.item(0).getTextContent();
										classname3 = eElement3.getElementsByTagName("classname").item(0)
												.getTextContent();
										classcolor3 = eElement3.getElementsByTagName("classcolor").item(0)
												.getTextContent();
										classparameters3 = eElement3.getElementsByTagName("classparameters").item(0)
												.getTextContent();
										classminvalues3 = eElement3.getElementsByTagName("classminvalues").item(0)
												.getTextContent();
										classmaxvalues3 = eElement3.getElementsByTagName("classmaxvalues").item(0)
												.getTextContent();
									}

									directFolder = new File(textSave.getText() + File.separator + "Analysis_Summary");
									if (!directFolder.exists()) {
										boolean var76 = false;

										try {
											directFolder.mkdir();
											var76 = true;
										} catch (SecurityException var67) {
										}
									}
								}
							} catch (Exception var70) {
								var70.printStackTrace();
							}

							for (int ix = 0; ix < listOfFiles.length; ++ix) {
								File directFolderImage = new File(
										textSave.getText() + File.separator + listOfFiles[ix].getName());
								if (!directFolderImage.exists()) {
									boolean var76 = false;

									try {
										directFolderImage.mkdir();
										var76 = true;
									} catch (SecurityException var67) {
									}
								}

								final int currentValue = ix + 1;

								try {
									SwingUtilities.invokeLater(new Runnable() {
										public void run() {
											pb.setValue(currentValue);
											taskOutput.append(String.format(
													"IMAGE TITLE:  " + listOfFiles[currentValue - 1].getName()
															+ "- %f%% of task.\n",
													(double) currentValue * 100.0D / (double) listOfFiles.length));
										}
									});
									Thread.sleep(100L);
								} catch (InterruptedException var69) {
									JOptionPane.showMessageDialog(frame, var69.getMessage());
								}

								if (listOfFiles[ix].isFile()) {
									imageTitles[ix] = listOfFiles[ix].getName();
								}

								ImagePlus imp = new ImagePlus(
										StartPageModified.textImages.getText() + File.separator + imageTitles[ix]);
								ImagePlus[] channels = ChannelSplitter.split(imp);
								ImagePlus channelAnalCellCount = null;
								if (nodeList1.item(0).getAttributes().item(0).getNodeName()
										.contains("Red") == Boolean.TRUE)
									channelAnalCellCount = channels[0];

								if (nodeList1.item(0).getAttributes().item(0).getNodeName()
										.contains("Green") == Boolean.TRUE)
									channelAnalCellCount = channels[1];

								if (nodeList1.item(0).getAttributes().item(0).getNodeName()
										.contains("Blue") == Boolean.TRUE)
									channelAnalCellCount = channels[2];

								ImagePlus impSeg = channelAnalCellCount.duplicate();
								if (thresholdmethod.equals("Default") == Boolean.TRUE)
									IJ.run(impSeg, "Auto Threshold", "method=Default ignore_black white");

								if (thresholdmethod.equals("Huang") == Boolean.TRUE)
									IJ.run(impSeg, "Auto Threshold", "method=Huang ignore_black white");

								if (thresholdmethod.equals("Huang2") == Boolean.TRUE)
									IJ.error("This threshold method is no available in batch-mode.");

								if (thresholdmethod.equals("Intermodes") == Boolean.TRUE)
									IJ.run(impSeg, "Auto Threshold", "method=Intermodes ignore_black white");

								if (thresholdmethod.equals("Li") == Boolean.TRUE)
									IJ.run(impSeg, "Auto Threshold", "method=Li ignore_black white");

								if (thresholdmethod.equals("MaxEntropy") == Boolean.TRUE)
									IJ.run(impSeg, "Auto Threshold", "method=MaxEntropy ignore_black white");

								if (thresholdmethod.equals("Mean") == Boolean.TRUE)
									IJ.run(impSeg, "Auto Threshold", "method=Mean ignore_black white");

								if (thresholdmethod.equals("MinError(I)") == Boolean.TRUE)
									IJ.run(impSeg, "Auto Threshold", "method=MinError(I) ignore_black white");

								if (thresholdmethod.equals("Minimum") == Boolean.TRUE)
									IJ.run(impSeg, "Auto Threshold", "method=Minimum ignore_black white");

								if (thresholdmethod.equals("Otsu") == Boolean.TRUE)
									IJ.run(impSeg, "Auto Threshold", "method=Otsu ignore_black white");

								if (thresholdmethod.equals("RenyiEntropy") == Boolean.TRUE)
									IJ.run(impSeg, "Auto Threshold", "method=RenyiEntropy ignore_black white");

								if (thresholdmethod.equals("Shanbhag") == Boolean.TRUE)
									IJ.run(impSeg, "Auto Threshold", "method=Shanbhag ignore_black white");

								if (thresholdmethod.equals("Triangle") == Boolean.TRUE)
									IJ.run(impSeg, "Auto Threshold", "method=Triangle ignore_black white");

								if (thresholdmethod.equals("Yen") == Boolean.TRUE)
									IJ.run(impSeg, "Auto Threshold", "method=Yen ignore_black white");

								if (rm != null) {
									rm.reset();
								}

								IJ.run(impSeg, "Analyze Particles...", "size=350-2147483647 exclude clear add");
								rm = RoiManager.getInstance();
								Roi[] roisInitial = rm.getRoisAsArray();
								Analyzer aSys = new Analyzer(channelAnalCellCount); // System
								aSys.setMeasurements(Analyzer.ALL_STATS);
								ResultsTable rtSys = Analyzer.getResultsTable();
								ResultsTable rtMulti = new ResultsTable(roisInitial.length - 1);
								rtMulti.showRowNumbers(true);
								rtSys.reset();

								rtMulti.incrementCounter();
								int roiIndex = 0;
								for (int i = 0; i < roisInitial.length; i++) {
									channelAnalCellCount.setRoi(roisInitial[i]);
									roiIndex++;
									aSys.measure();
									for (int j = 0; j <= rtSys.getLastColumn(); j++) {
										float[] col = rtSys.getColumn(j);
										String head = rtSys.getColumnHeading(j);
										String suffix = "" + roiIndex;
										Roi roi = channelAnalCellCount.getRoi();
										if (roi != null) {
											String name = roi.getName();
											if (name != null && name.length() > 0
													&& (name.length() < 9 || !Character.isDigit(name.charAt(0))))
												suffix = "(" + name + ")";
										}
										if (head != null && col != null && !head.equals("Slice"))
											rtMulti.setValue(head, i, rtSys.getValue(j, rtSys.getCounter() - 1));
									}
								}

								ResultsTable rt0 = rtMulti;

								roisNames = new ArrayList();

								for (int jxxxx = 0; jxxxx < rm.getCount(); ++jxxxx) {
									roisNames.add(rm.getName(jxxxx));
								}

								if (excelFileB.isSelected() == Boolean.TRUE && comboExt.getSelectedIndex() == 0) {
									try {
										rt0.saveAs(directFolderImage + File.separator + "TableData_"
												+ StartPageModified.tfCh1.getText() + ".xls");
									} catch (IOException var66) {
										var66.printStackTrace();

									}
								}
								if (excelFileB.isSelected() == Boolean.TRUE && comboExt.getSelectedIndex() == 1) {
									try {
										rt0.saveAs(directFolderImage + File.separator + "TableData_"
												+ StartPageModified.tfCh1.getText() + ".xlsx");
									} catch (IOException var66) {
										var66.printStackTrace();
									}

								}

								if (csvFileB.isSelected() == Boolean.TRUE) {
									try {
										rt0.saveAs(directFolderImage + File.separator + "TableData_"
												+ StartPageModified.tfCh1.getText() + ".csv");
									} catch (IOException var66) {
										var66.printStackTrace();
									}

								}
								counterTable0List.add((double) rt0.size());
								String[] columnHeaders0 = rt0.getHeadings();
								List<String> columnHeadersS0 = new ArrayList();

								for (int r = 0; r < columnHeaders0.length; ++r) {
									columnHeadersS0.add(columnHeaders0[r]);
								}

								String[] columnHeadersSS0 = new String[columnHeaders0.length];
								columnHeadersS0.toArray(columnHeadersSS0);
								int rows0 = rt0.size();
								List<List<String>> dataList0 = new ArrayList();

								int jxxxxx;
								for (int rx = 0; rx < rows0; ++rx) {
									List<String> strings0 = new ArrayList();

									for (jxxxxx = 0; jxxxxx < columnHeaders0.length; ++jxxxxx) {
										String values0 = rt0.getStringValue(columnHeaders0[jxxxxx], rx);
										strings0.add(values0);
									}

									dataList0.add(strings0);
								}

								Double[][] data0 = new Double[dataList0.size()][];

								int j;
								for (j = 0; j < data0.length; ++j) {
									data0[j] = new Double[((List) dataList0.get(j)).size()];
								}

								for (j = 0; j < dataList0.size(); ++j) {
									for (jxxxxx = 0; jxxxxx < ((List) dataList0.get(j)).size(); ++jxxxxx) {
										data0[j][jxxxxx] = Double
												.parseDouble((String) ((List) dataList0.get(j)).get(jxxxxx));
									}
								}

								DefaultTableModel model0 = new DefaultTableModel(data0, columnHeaders0) {
									public Class<?> getColumnClass(int column) {
										if (this.getRowCount() > 0) {
											Object value = this.getValueAt(0, column);
											if (value != null) {
												return this.getValueAt(0, column).getClass();
											}
										}

										return super.getColumnClass(column);
									}
								};
								table0 = new JTable();
								table0.setModel(model0);

								for (jxxxxx = 0; jxxxxx < roisNames.size(); ++jxxxxx) {
									table0.setValueAt(roisNames.get(jxxxxx), jxxxxx, 0);
								}

								ArrayList listMaxValue;
								int row;
								int x;
								if (filterparameter.isEmpty() == Boolean.FALSE) {
									List<String> listFiltersName = new ArrayList();
									List<String> listMinValue = new ArrayList();
									listMaxValue = new ArrayList();
									String[] filterParameterSplit = filterparameter.split(",");
									String[] minValueSplit = minvalue.split(",");
									String[] maxValueSplit = maxvalue.split(",");

									for (row = 0; row < filterParameterSplit.length; ++row) {
										listFiltersName.add(filterParameterSplit[row]);
										listMinValue.add(minValueSplit[row]);
										listMaxValue.add(maxValueSplit[row]);
									}

									List<Integer> indexFilters = new ArrayList();

									for (int jxxxxxxx = 0; jxxxxxxx < listFiltersName.size(); ++jxxxxxxx) {
										indexFilters.add(rt0.getColumnIndex((String) listFiltersName.get(jxxxxxxx)));
									}

									TableRowSorter rowSorterR = new TableRowSorter(table0.getModel());
									table0.setRowSorter(rowSorterR);
									List<RowFilter<TableModel, Integer>> low = new ArrayList();
									List<RowFilter<TableModel, Integer>> high = new ArrayList();

									for (int jxxxxxxxx = 0; jxxxxxxxx < listMinValue.size(); ++jxxxxxxxx) {
										low.add(RowFilter.numberFilter(ComparisonType.AFTER,
												Double.parseDouble((String) listMinValue.get(jxxxxxxxx)),
												new int[] { table0.convertColumnIndexToModel(
														(Integer) indexFilters.get(jxxxxxxxx) + 1) }));
										high.add(RowFilter.numberFilter(ComparisonType.BEFORE,
												Double.parseDouble((String) listMaxValue.get(jxxxxxxxx)),
												new int[] { table0.convertColumnIndexToModel(
														(Integer) indexFilters.get(jxxxxxxxx) + 1) }));
									}

									List<RowFilter<TableModel, Integer>> listOfFilters = new ArrayList();

									int jxxxxxxxxx;
									for (jxxxxxxxxx = 0; jxxxxxxxxx < low.size(); ++jxxxxxxxxx) {
										listOfFilters.add((RowFilter) low.get(jxxxxxxxxx));
									}

									for (jxxxxxxxxx = 0; jxxxxxxxxx < high.size(); ++jxxxxxxxxx) {
										listOfFilters.add((RowFilter) high.get(jxxxxxxxxx));
									}

									RowFilter<TableModel, Integer> filter = RowFilter.andFilter(listOfFilters);

									for (x = 0; x < indexFilters.size(); ++x) {
										if (Double.parseDouble((String) listMinValue.get(x)) == 0.0D
												&& Double.parseDouble((String) listMaxValue.get(ix)) == 0.0D) {
											rowSorterR.setRowFilter((RowFilter) null);
										} else {
											try {
												rowSorterR.setRowFilter(filter);
											} catch (PatternSyntaxException var68) {
												System.out.println("Bad regex pattern");
											}
										}
									}
								}

								Object[][] tableData = new Object[table0.getRowCount()][table0.getColumnCount()];

								int col;
								for (int jx = 0; jx < table0.getRowCount(); ++jx) {
									for (col = 0; col < table0.getColumnCount(); ++col) {
										tableData[jx][col] = table0.getValueAt(jx, col);
									}
								}

								if (WindowManager.getFrame("Results") != null) {
									IJ.selectWindow("Results");
									IJ.run("Close");
								}

								int jxx;
								ArrayList roisEnlargered;
								if (filterparameter.isEmpty() == Boolean.FALSE) {
									roisFilteredName = new ArrayList();
									roisEnlargered = new ArrayList();

									for (jxx = 0; jxx < table0.getRowCount(); ++jxx) {
										roisFilteredName.add((String) table0.getValueAt(jxx, 0));
										roisEnlargered.add(roisNames.indexOf(roisFilteredName.get(jxx)));
									}

									int jxxx;
									ArrayList roisEnlargeredx;
									if (morphooperatorname2.equals("Erosion") == Boolean.TRUE) {
										rm.reset();
										roisEnlargeredx = new ArrayList();

										for (jxxx = 0; jxxx < roisEnlargered.size(); ++jxxx) {
											roisEnlargeredx.add(
													RoiEnlarger.enlarge(roisInitial[(Integer) roisEnlargered.get(jxxx)],
															(double) (~(Integer.parseInt(morphooperatorvalue2) - 1))));
										}

										for (jxxx = 0; jxxx < roisEnlargeredx.size(); ++jxxx) {
											rm.addRoi((Roi) roisEnlargeredx.get(jxxx));
											rm.rename(jxxx, (String) roisFilteredName.get(jxxx));
										}
									}

									if (morphooperatorname2.equals("Dilation") == Boolean.TRUE) {
										rm.reset();
										roisEnlargeredx = new ArrayList();

										for (jxxx = 0; jxxx < roisEnlargered.size(); ++jxxx) {
											roisEnlargeredx.add(
													RoiEnlarger.enlarge(roisInitial[(Integer) roisEnlargered.get(jxxx)],
															(double) Integer.parseInt(morphooperatorvalue2)));
										}

										for (jxxx = 0; jxxx < roisEnlargeredx.size(); ++jxxx) {
											rm.addRoi((Roi) roisEnlargeredx.get(jxxx));
											rm.rename(jxxx, (String) roisFilteredName.get(jxxx));
										}
									}
								}

								roisEnlargered = null;
								if (filterparameter.isEmpty() == Boolean.TRUE) {
									if (morphooperatorname2.equals("Erosion") == Boolean.TRUE) {
										rm.reset();
										roisEnlargered = new ArrayList();

										for (jxx = 0; jxx < roisInitial.length; ++jxx) {
											roisEnlargered.add(RoiEnlarger.enlarge(roisInitial[jxx],
													(double) (~(Integer.parseInt(morphooperatorvalue2) - 1))));
										}

										for (jxx = 0; jxx < roisEnlargered.size(); ++jxx) {
											rm.addRoi((Roi) roisEnlargered.get(jxx));
											rm.rename(jxx, (String) roisNames.get(jxx));
										}
									}

									if (morphooperatorname2.equals("Dilation") == Boolean.TRUE) {
										rm.reset();
										roisEnlargered = new ArrayList();

										for (jxx = 0; jxx < roisInitial.length; ++jxx) {
											roisEnlargered.add(RoiEnlarger.enlarge(roisInitial[jxx],
													(double) Integer.parseInt(morphooperatorvalue2)));
										}

										for (jxx = 0; jxx < roisEnlargered.size(); ++jxx) {
											rm.addRoi((Roi) roisEnlargered.get(jxx));
											rm.rename(jxx, (String) roisNames.get(jxx));
										}
									}
								}

								ImagePlus channelAnalysis1 = null;
								if (nodeList2.item(0).getAttributes().item(0).getNodeName()
										.contains("Red") == Boolean.TRUE)
									channelAnalysis1 = channels[0];

								if (nodeList2.item(0).getAttributes().item(0).getNodeName()
										.contains("Green") == Boolean.TRUE)
									channelAnalysis1 = channels[1];

								if (nodeList2.item(0).getAttributes().item(0).getNodeName()
										.contains("Blue") == Boolean.TRUE)
									channelAnalysis1 = channels[2];

								rm.deselect();

								Analyzer aSys1 = new Analyzer(channelAnalysis1); // System
								aSys1.setMeasurements(Analyzer.ALL_STATS);
								ResultsTable rtSys1 = Analyzer.getResultsTable();
								ResultsTable rtMulti1 = new ResultsTable(roisInitial.length - 1);
								rtMulti1.showRowNumbers(true);
								rtSys1.reset();

								rtMulti1.incrementCounter();
								int roiIndex1 = 0;
								for (int i = 0; i < roisInitial.length; i++) {
									channelAnalysis1.setRoi(roisInitial[i]);
									roiIndex1++;
									aSys1.measure();
									for (int j1 = 0; j1 <= rtSys1.getLastColumn(); j1++) {
										float[] col1 = rtSys1.getColumn(j1);
										String head1 = rtSys1.getColumnHeading(j1);
										String suffix1 = "" + roiIndex1;
										Roi roi1 = channelAnalysis1.getRoi();
										if (roi1 != null) {
											String name1 = roi1.getName();
											if (name1 != null && name1.length() > 0
													&& (name1.length() < 9 || !Character.isDigit(name1.charAt(0))))
												suffix1 = "(" + name1 + ")";
										}
										if (head1 != null && col1 != null && !head1.equals("Slice"))
											rtMulti1.setValue(head1, i, rtSys1.getValue(j1, rtSys1.getCounter() - 1));
									}
								}

								ResultsTable rt1 = rtMulti1;

								if (excelFileB.isSelected() == Boolean.TRUE && comboExt.getSelectedIndex() == 0) {
									try {
										rt1.saveAs(directFolderImage + File.separator + "TableData_"
												+ StartPageModified.tfCh2.getText() + ".xls");
									} catch (IOException var66) {
										var66.printStackTrace();
									}
								}

								if (excelFileB.isSelected() == Boolean.TRUE && comboExt.getSelectedIndex() == 1) {
									try {
										rt1.saveAs(directFolderImage + File.separator + "TableData_"
												+ StartPageModified.tfCh2.getText() + ".xlsx");
									} catch (IOException var66) {
										var66.printStackTrace();
									}
								}

								if (csvFileB.isSelected() == Boolean.TRUE) {
									try {
										rt1.saveAs(directFolderImage + File.separator + "TableData_"
												+ StartPageModified.tfCh2.getText() + ".csv");
									} catch (IOException var66) {
										var66.printStackTrace();
									}

								}
								Double[] counterFM = new Double[rm.getCount()];
								ImageProcessor[] processorFM = new ImageProcessor[rm.getCount()];
								String[] classNames = classname2.split(",");
								String[] classParameters = classparameters2.split(",");
								String[] classMinValues = classminvalues2.split(",");
								String[] classMaxValues = classmaxvalues2.split(",");
								if (classParameters[0].contains("FociNuc") == Boolean.TRUE) {
									for (x = 0; x < rm.getCount(); ++x) {
										processorFM[x] = channelAnalysis1.getProcessor();
										processorFM[x].setRoi((Roi) roisEnlargered.get(x));
										counterFM[x] = (double) (new MaximumFinder()).getMaxima(processorFM[x].crop(),
												30.0D, false).npoints;
									}
								}

								String[] columnHeaders1 = rt1.getHeadings();
								List<String> columnHeadersS1 = new ArrayList();

								for (int rxx = 0; rxx < columnHeaders1.length; ++rxx) {
									columnHeadersS1.add(columnHeaders1[rxx]);
								}

								String[] columnHeadersSS1 = new String[columnHeaders1.length];
								columnHeadersS1.toArray(columnHeadersSS1);
								int rows1 = rt1.size();
								List<List<String>> dataList1 = new ArrayList();

								int jxxxxxxxxxxx;
								for (int rxxx = 0; rxxx < rows1; ++rxxx) {
									List<String> strings1 = new ArrayList();

									for (jxxxxxxxxxxx = 0; jxxxxxxxxxxx < columnHeaders1.length; ++jxxxxxxxxxxx) {
										String values1 = rt1.getStringValue(columnHeaders1[jxxxxxxxxxxx], rxxx);
										strings1.add(values1);
									}

									dataList1.add(strings1);
								}

								data1 = new Double[dataList1.size()][];

								int jxxxxxx;
								for (jxxxxxx = 0; jxxxxxx < data1.length; ++jxxxxxx) {
									data1[jxxxxxx] = new Double[((List) dataList1.get(jxxxxxx)).size()];
								}

								for (jxxxxxx = 0; jxxxxxx < dataList1.size(); ++jxxxxxx) {
									for (jxxxxxxxxxxx = 0; jxxxxxxxxxxx < ((List) dataList1.get(jxxxxxx))
											.size(); ++jxxxxxxxxxxx) {
										data1[jxxxxxx][jxxxxxxxxxxx] = Double.parseDouble(
												(String) ((List) dataList1.get(jxxxxxx)).get(jxxxxxxxxxxx));
									}
								}

								if (WindowManager.getFrame("Results") != null) {
									IJ.selectWindow("Results");
									IJ.run("Close");
								}

								DefaultTableModel model1 = new DefaultTableModel(data1, columnHeaders1) {
									public Class<?> getColumnClass(int column) {
										if (this.getRowCount() > 0) {
											Object value = this.getValueAt(0, column);
											if (value != null) {
												return this.getValueAt(0, column).getClass();
											}
										}

										return super.getColumnClass(column);
									}
								};
								table1 = new JTable();
								table1.setModel(model1);

								for (jxxxxxxxxxxx = 0; jxxxxxxxxxxx < roisNames.size(); ++jxxxxxxxxxxx) {
									table1.setValueAt(roisNames.get(jxxxxxxxxxxx), jxxxxxxxxxxx,
											table1.convertColumnIndexToModel(0));
								}

								if (classParameters[0].contains("FociNuc") == Boolean.TRUE) {
									model1.addColumn("FociNuc", counterFM);
									table1.moveColumn(table1.getColumnCount() - 1, 2);
								}

								model1.addColumn("Class-Label");
								table1.moveColumn(table1.getColumnCount() - 1, 0);
								if (classParameters[0].contains("FociNuc") == Boolean.TRUE) {
									table1.moveColumn(table1.getColumnCount() - 1, 2);
								}

								tableData1 = new Object[table1.getModel().getRowCount()][table1.getModel()
										.getColumnCount()];

								if (classParameters[0].contains("FociNuc") == Boolean.FALSE) {

									for (int ux = 0; ux < classParameters.length; ++ux) {
										for (int c = 0; c < model1.getRowCount(); ++c) {
											if (classParameters[ux].contains("-" + (ux + 1))
													&& classMinValues[ux].contains("-" + (ux + 1))
													&& classMaxValues[ux].contains("-" + (ux + 1))
													&& (Double) table1.getModel()
															.getValueAt(
																	c, table1
																			.getColumn(classParameters[ux].substring(0,
																					classParameters[ux]
																							.lastIndexOf("-")))
																			.getModelIndex()) >= Double.parseDouble(
																					classMinValues[ux].substring(0,
																							classMinValues[ux]
																									.lastIndexOf("-")))
													&& (Double) table1.getModel()
															.getValueAt(
																	c, table1
																			.getColumn(classParameters[ux].substring(0,
																					classParameters[ux]
																							.lastIndexOf("-")))
																			.getModelIndex()) <= Double.parseDouble(
																					classMaxValues[ux].substring(0,
																							classMaxValues[ux]
																									.lastIndexOf(
																											"-")))) {
												model1.setValueAt(classNames[0], c,
														table1.convertColumnIndexToModel(0));

											}
										}
									}
								}

								if (classParameters[0].contains("FociNuc") == Boolean.TRUE) {

									for (int u = 0; u < model1.getRowCount(); ++u) {
										if ((Double) table1.getModel().getValueAt(u,
												table1.getColumn("FociNuc").getModelIndex()) >= Double
														.parseDouble(classMinValues[0].substring(0,
																classMinValues[0].lastIndexOf("-")))
												&& (Double) table1.getModel().getValueAt(u,
														table1.getColumn("FociNuc").getModelIndex()) <= Double
																.parseDouble(classMaxValues[0].substring(0,
																		classMaxValues[0].lastIndexOf("-")))
												&& (Double) table1.getModel().getValueAt(u,
														table1.getColumn("Mean").getModelIndex()) >= Double
																.parseDouble(classMinValues[1].substring(0,
																		classMinValues[1].lastIndexOf("-")))
												&& (Double) table1.getModel().getValueAt(u,
														table1.getColumn("Mean").getModelIndex()) <= Double
																.parseDouble(classMaxValues[1].substring(0,
																		classMaxValues[1].lastIndexOf("-")))) {
											model1.setValueAt(classNames[0], u, table1.convertColumnIndexToModel(0));

										}

										for (int jxxxxxxxxxxxx = 0; jxxxxxxxxxxxx < table1.getModel()
												.getColumnCount(); ++jxxxxxxxxxxxx) {
											tableData1[u][jxxxxxxxxxxxx] = table1.getModel().getValueAt(u,
													table1.convertColumnIndexToModel(jxxxxxxxxxxxx));

										}
									}
								}

								model1.fireTableDataChanged();
								table1.repaint();

								ImagePlus channelAnalysis2 = null;
								if (nodeList3.item(0).getAttributes().item(0).getNodeName()
										.contains("Red") == Boolean.TRUE)
									channelAnalysis2 = channels[0];

								if (nodeList3.item(0).getAttributes().item(0).getNodeName()
										.contains("Green") == Boolean.TRUE)
									channelAnalysis2 = channels[1];

								if (nodeList3.item(0).getAttributes().item(0).getNodeName()
										.contains("Blue") == Boolean.TRUE)
									channelAnalysis2 = channels[2];

								rm.reset();

								for (int jxxxxxxxxxxxx = 0; jxxxxxxxxxxxx < roisInitial.length; ++jxxxxxxxxxxxx) {
									rm.addRoi(roisInitial[jxxxxxxxxxxxx]);
									rm.rename(jxxxxxxxxxxxx, (String) roisNames.get(jxxxxxxxxxxxx));
								}

								rm.deselect();

								Analyzer aSys2 = new Analyzer(channelAnalysis2); // System
								aSys2.setMeasurements(Analyzer.ALL_STATS);
								ResultsTable rtSys2 = Analyzer.getResultsTable();
								ResultsTable rtMulti2 = new ResultsTable(roisInitial.length - 1);
								rtMulti2.showRowNumbers(true);
								rtSys2.reset();

								rtMulti2.incrementCounter();
								int roiIndex2 = 0;
								for (int i = 0; i < roisInitial.length; i++) {
									channelAnalysis2.setRoi(roisInitial[i]);
									roiIndex2++;
									aSys2.measure();
									for (int j2 = 0; j2 <= rtSys2.getLastColumn(); j2++) {
										float[] col2 = rtSys2.getColumn(j2);
										String head2 = rtSys2.getColumnHeading(j2);
										String suffix2 = "" + roiIndex2;
										Roi roi2 = channelAnalysis2.getRoi();
										if (roi2 != null) {
											String name2 = roi2.getName();
											if (name2 != null && name2.length() > 0
													&& (name2.length() < 9 || !Character.isDigit(name2.charAt(0))))
												suffix2 = "(" + name2 + ")";
										}
										if (head2 != null && col2 != null && !head2.equals("Slice"))
											rtMulti2.setValue(head2, i, rtSys2.getValue(j2, rtSys2.getCounter() - 1));
									}
								}

								ResultsTable rt2 = rtMulti2;

								if (excelFileB.isSelected() == Boolean.TRUE && comboExt.getSelectedIndex() == 0) {
									try {
										rt2.saveAs(directFolderImage + File.separator + "TableData_"
												+ StartPageModified.tfCh3.getText() + ".xls");
									} catch (IOException var66) {
										var66.printStackTrace();
									}

								}

								if (excelFileB.isSelected() == Boolean.TRUE && comboExt.getSelectedIndex() == 1) {
									try {
										rt2.saveAs(directFolderImage + File.separator + "TableData_"
												+ StartPageModified.tfCh3.getText() + ".xlsx");
									} catch (IOException var66) {
										var66.printStackTrace();
									}

								}

								if (csvFileB.isSelected() == Boolean.TRUE) {
									try {
										rt2.saveAs(directFolderImage + File.separator + "TableData_"
												+ StartPageModified.tfCh3.getText() + ".csv");
									} catch (IOException var66) {
										var66.printStackTrace();
									}

								}
								String[] columnHeaders2 = rt2.getHeadings();
								List<String> columnHeadersS2 = new ArrayList();

								for (int rxxxx = 0; rxxxx < columnHeaders2.length; ++rxxxx) {
									columnHeadersS2.add(columnHeaders2[rxxxx]);
								}

								String[] columnHeadersSS2 = new String[columnHeaders2.length];
								columnHeadersS2.toArray(columnHeadersSS2);
								int rows2 = rt2.size();
								List<List<String>> dataList2 = new ArrayList();

								int jxxxxxxxxxxxxxx;
								for (int rxxxxx = 0; rxxxxx < rows2; ++rxxxxx) {
									List<String> strings2 = new ArrayList();

									for (jxxxxxxxxxxxxxx = 0; jxxxxxxxxxxxxxx < columnHeaders2.length; ++jxxxxxxxxxxxxxx) {
										String values2 = rt2.getStringValue(columnHeaders2[jxxxxxxxxxxxxxx], rxxxxx);
										strings2.add(values2);
									}

									dataList2.add(strings2);
								}

								data2 = new Double[dataList2.size()][];

								int jxxxxxxxxxx;
								for (jxxxxxxxxxx = 0; jxxxxxxxxxx < data2.length; ++jxxxxxxxxxx) {
									data2[jxxxxxxxxxx] = new Double[((List) dataList2.get(jxxxxxxxxxx)).size()];
								}

								for (jxxxxxxxxxx = 0; jxxxxxxxxxx < dataList2.size(); ++jxxxxxxxxxx) {
									for (jxxxxxxxxxxxxxx = 0; jxxxxxxxxxxxxxx < ((List) dataList2.get(jxxxxxxxxxx))
											.size(); ++jxxxxxxxxxxxxxx) {
										data2[jxxxxxxxxxx][jxxxxxxxxxxxxxx] = Double.parseDouble(
												(String) ((List) dataList2.get(jxxxxxxxxxx)).get(jxxxxxxxxxxxxxx));
									}
								}

								if (WindowManager.getFrame("Results") != null) {
									IJ.selectWindow("Results");
									IJ.run("Close");
								}

								DefaultTableModel model2 = new DefaultTableModel(data2, columnHeaders2) {
									public Class<?> getColumnClass(int column) {
										if (this.getRowCount() > 0) {
											Object value = this.getValueAt(0, column);
											if (value != null) {
												return this.getValueAt(0, column).getClass();
											}
										}

										return super.getColumnClass(column);
									}
								};
								table2 = new JTable();
								table2.setModel(model2);

								for (jxxxxxxxxxxxxxx = 0; jxxxxxxxxxxxxxx < roisNames.size(); ++jxxxxxxxxxxxxxx) {
									table2.setValueAt(roisNames.get(jxxxxxxxxxxxxxx), jxxxxxxxxxxxxxx,
											table2.convertColumnIndexToModel(0));
								}

								model2.addColumn("Class-Label");
								table2.moveColumn(table2.getColumnCount() - 1, 0);
								String[] classNames2 = classname3.split(",");
								String[] classParameters2 = classparameters3.split(",");
								String[] classMinValues2 = classminvalues3.split(",");
								String[] classMaxValues2 = classmaxvalues3.split(",");

								int ux;
								int c;
								if (classParameters[0].contains("FociNuc") == Boolean.FALSE) {

									for (ux = 0; ux < classParameters2.length; ++ux) {
										for (c = 0; c < model2.getRowCount(); ++c) {
											if (classParameters2[ux].contains("-" + ux)
													&& classMinValues2[ux].contains("-" + ux)
													&& classMaxValues2[ux].contains("-" + ux)
													&& (Double) table2.getModel()
															.getValueAt(
																	c, table2
																			.getColumn(classParameters2[ux].substring(0,
																					classParameters2[ux]
																							.lastIndexOf("-")))
																			.getModelIndex()) >= Double.parseDouble(
																					classMinValues2[ux].substring(0,
																							classMinValues2[ux]
																									.lastIndexOf("-")))
													&& (Double) table2.getModel()
															.getValueAt(
																	c, table2
																			.getColumn(classParameters2[ux].substring(0,
																					classParameters2[ux]
																							.lastIndexOf("-")))
																			.getModelIndex()) <= Double.parseDouble(
																					classMaxValues2[ux].substring(0,
																							classMaxValues2[ux]
																									.lastIndexOf(
																											"-")))) {
												model2.setValueAt(classNames2[0], c,
														table2.convertColumnIndexToModel(0));

											}
										}
									}
								}

								int xx;
								if (classParameters[0].contains("FociNuc") == Boolean.TRUE) {
									ux = 0;

									label890: while (true) {
										if (ux >= classParameters2.length) {
											ux = 0;

											while (true) {
												if (ux >= classParameters2.length) {
													break label890;
												}

												++ux;
											}
										}

										for (c = 0; c < model2.getRowCount(); ++c) {
											if (classParameters2[ux].contains("-" + ux)
													&& classMinValues2[ux].contains("-" + ux)
													&& classMaxValues2[ux].contains("-" + ux)
													&& (Double) table2.getModel()
															.getValueAt(
																	c, table2
																			.getColumn(classParameters2[1].substring(0,
																					classParameters2[1]
																							.lastIndexOf("-")))
																			.getModelIndex()) >= Double.parseDouble(
																					classMinValues2[1].substring(0,
																							classMinValues2[1]
																									.lastIndexOf("-")))
													&& (Double) table2.getModel()
															.getValueAt(
																	c, table2
																			.getColumn(classParameters2[1].substring(0,
																					classParameters2[1]
																							.lastIndexOf("-")))
																			.getModelIndex()) <= Double.parseDouble(
																					classMaxValues2[1].substring(0,
																							classMaxValues2[1]
																									.lastIndexOf(
																											"-")))) {
												model2.setValueAt(classNames2[0], c,
														table2.convertColumnIndexToModel(0));

											}
										}

										++ux;
									}
								}

								model2.fireTableDataChanged();
								Object[][] dataPositiveDetection = new Object[model0.getRowCount()][2];

								for (c = 0; c < dataPositiveDetection.length; ++c) {
									dataPositiveDetection[c][0] = model1.getValueAt(c,
											table1.convertColumnIndexToModel(0));
									dataPositiveDetection[c][1] = model2.getValueAt(c,
											table2.convertColumnIndexToModel(0));

								}

								c = 0;
								int counterPositiveDef = 0;
								int counter1 = 0;
								int counter1Def = 0;
								int counter2 = 0;
								int counter2Def = 0;

								for (int rowx = 0; rowx < dataPositiveDetection.length; ++rowx) {

									if (dataPositiveDetection[rowx][0] == classNames[0]) {
										counter1Def = counter1++;
									}

									if (dataPositiveDetection[rowx][1] == classNames2[0]) {
										counter2Def = counter2++;
									}

									if (dataPositiveDetection[rowx][0] == classNames[0]
											&& dataPositiveDetection[rowx][1] == classNames2[0]) {
										counterPositiveDef = c++;
									}
								}

								classCounterDefL.add((double) counter1Def);
								classCounterDef2L.add((double) counter2Def);
								counterPositiveDefL.add((double) counterPositiveDef);

							}
							Object[] columHeadersFinal = new Object[] { "Image-Name",
									nodeList1.item(0).getAttributes().item(0).getNodeValue() + "-Count",
									nodeList1.item(0).getAttributes().item(0).getNodeValue() + "-%",
									nodeList2.item(0).getAttributes().item(0).getNodeValue() + "-Count",
									nodeList2.item(0).getAttributes().item(0).getNodeValue() + "-%",
									nodeList3.item(0).getAttributes().item(0).getNodeValue() + "-Count",
									nodeList3.item(0).getAttributes().item(0).getNodeValue() + "-%", "+ -Count",
									"+ -%" };

							ResultsTable rtSummary = new ResultsTable(listOfFiles.length);

							int o;
							for (o = 0; o < columHeadersFinal.length; ++o)
								rtSummary.setHeading(o, (String) columHeadersFinal[o]);

							for (o = 0; o < rtSummary.size(); ++o) {
								rtSummary.setValue(0, o, listOfFiles[o].getName().substring(0,
										listOfFiles[o].getName().lastIndexOf(".")));
								rtSummary.setValue(1, o, (Double) counterTable0List.get(o));
								rtSummary.setValue(2, o, (double) Math.round((Double) counterTable0List.get(o) * 100.0D
										/ (Double) counterTable0List.get(o) * 100.0D) / 100.0D);
								rtSummary.setValue(3, o, (Double) classCounterDefL.get(o));
								rtSummary.setValue(4, o, (double) Math.round((Double) classCounterDefL.get(o) * 100.0D
										/ (Double) counterTable0List.get(o) * 100.0D) / 100.0D);
								rtSummary.setValue(5, o, (Double) classCounterDef2L.get(o));
								rtSummary.setValue(6, o, (double) Math.round((Double) classCounterDef2L.get(o) * 100.0D
										/ (Double) counterTable0List.get(o) * 100.0D) / 100.0D);
								rtSummary.setValue(7, o, (Double) counterPositiveDefL.get(o));
								rtSummary.setValue(8, o, (double) Math.round((Double) counterPositiveDefL.get(o)
										* 100.0D / (Double) counterTable0List.get(o) * 100.0D) / 100.0D);
							}
							if (excelFileB.isSelected() == Boolean.TRUE && comboExt.getSelectedIndex() == 0) {
								try {
									rtSummary
											.saveAs(directFolder + File.separator + "TableData_Final_Results" + ".xls");

								} catch (IOException var66) {
									var66.printStackTrace();
								}

							}

							if (excelFileB.isSelected() == Boolean.TRUE && comboExt.getSelectedIndex() == 1) {
								try {
									rtSummary.saveAs(
											directFolder + File.separator + "TableData_Final_Results" + ".xlsx");

								} catch (IOException var65) {
									var65.printStackTrace();
								}

							}

							if (csvFileB.isSelected() == Boolean.TRUE) {
								try {
									rtSummary
											.saveAs(directFolder + File.separator + "TableData_Final_Results" + ".csv");

								} catch (IOException var64) {
									var64.printStackTrace();
								}

							}

							rm.close();
							taskOutput.append("Done!");
							frame.dispatchEvent(new WindowEvent(frame, 201));

						}

					}
				});
				mainProcess.start();
			}
		});
		cancelButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt) {
				frameInitial.dispatchEvent(new WindowEvent(frameInitial, 201));
			}

		});
	}

}