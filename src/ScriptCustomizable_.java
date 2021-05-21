import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.util.StringTokenizer;
import java.util.prefs.Preferences;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import ij.IJ;
import ij.io.OpenDialog;
import ij.plugin.Macro_Runner;
import ij.plugin.NewPlugin;
import ij.plugin.frame.*;;

public class ScriptCustomizable_ {

	JTextArea scriptArea;
	JRadioButton pathButton, scriptButton;
	JComboBox<String> comboLanguage = new JComboBox<String>();
	String[] languages = new String[] { "BeanShell", "JavaScript", "IJ Macro", "Python" };
	String completed = " code below : ";
	TextField textScript = new TextField(15);
	Preferences prefScript = Preferences.userRoot();
	String CELLTYPEANALYZER_SCRIPT_DEFAULT_PATH = "images_path";
	JButton runButton = new JButton("Run", new ImageIcon(JFrameWizard.createImageIcon("images/compile.png").getImage()
			.getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
	static String text2;

	public void scriptGUI() {
		for (int j = 0; j < languages.length; j++)
			comboLanguage.addItem(languages[j]);
		pathButton = new JRadioButton("Run Macro (.txt or .ijm)/ Script (.js, .bsh or .py) :");
		scriptButton = new JRadioButton("Write or Copy/Paste your  ");
		scriptButton.setSelected(true);
		ButtonGroup group = new ButtonGroup();
		group.add(pathButton);
		group.add(scriptButton);
		scriptArea = new JTextArea();
		scriptArea.setPreferredSize(new Dimension(400, 300));
		scriptArea.setBackground(new Color(245, 252, 255));
		scriptArea.setLineWrap(true);
		scriptArea.setWrapStyleWord(false);
		textScript.setText(prefScript.get(CELLTYPEANALYZER_SCRIPT_DEFAULT_PATH, ""));
		DirectoryListener listenerScript = new DirectoryListener("Browse for script...  ", textScript,
				JFileChooser.FILES_AND_DIRECTORIES);
		JButton buttonBrowse = new JButton("");
		ImageIcon iconBrowse = JFrameWizard.createImageIcon("images/browse.png");
		Icon iconBrowseCell = new ImageIcon(iconBrowse.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
		buttonBrowse.setIcon(iconBrowseCell);
		buttonBrowse.addActionListener(listenerScript);
		JPanel panelScriptDirect = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelScriptDirect.add(pathButton);
		panelScriptDirect.add(textScript);
		panelScriptDirect.add(buttonBrowse);
		textScript.setEnabled(false);
		buttonBrowse.setEnabled(false);
		JPanel panelScriptArea = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelScriptArea.add(scriptButton);
		panelScriptArea.add(comboLanguage);
		panelScriptArea.add(new JLabel(completed));
		JPanel areaPanel = new JPanel();
		areaPanel.setLayout(new BoxLayout(areaPanel, BoxLayout.Y_AXIS));
		areaPanel.add(panelScriptDirect);
		areaPanel.add(panelScriptArea);
		JScrollPane jsp = new JScrollPane(scriptArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		areaPanel.add(jsp);
		areaPanel.add(runButton);
		scriptArea.setEnabled(true);
		JFrame frame = new JFrame();
		frame.setTitle("Script Custom");
		frame.setResizable(false);
		frame.add(areaPanel);
		frame.pack();
		frame.setSize(540, 400);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		runButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Macro_Runner mr = new Macro_Runner();
				if (pathButton.isSelected() == Boolean.TRUE) {
					mr.run(textScript.getText());
					frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
				}

				if (scriptButton.isSelected() == Boolean.TRUE) {
					if (comboLanguage.getSelectedIndex() == 0)
						Macro_Runner.runBeanShell(scriptArea.getText(), "");
					if (comboLanguage.getSelectedIndex() == 1)
						mr.runJavaScript(scriptArea.getText(), "");
					if (comboLanguage.getSelectedIndex() == 2)
						mr.runMacro(scriptArea.getText(), "");
					if (comboLanguage.getSelectedIndex() == 3)
						Macro_Runner.runPython(scriptArea.getText(), "");
					frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
				}
				createJavaCode(scriptArea, textScript);

			}
		});
		pathButton.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					scriptArea.setEnabled(false);
					textScript.setEnabled(true);
					buttonBrowse.setEnabled(true);
				}
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					scriptArea.setEnabled(true);
					textScript.setEnabled(false);
					buttonBrowse.setEnabled(false);
				}

			}
		});
		scriptButton.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					scriptArea.setEnabled(true);
					textScript.setEnabled(false);
					buttonBrowse.setEnabled(false);
				}
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					scriptArea.setEnabled(false);
					textScript.setEnabled(true);
					buttonBrowse.setEnabled(true);
				}

			}
		});

	}

	public void createJavaCode(JTextArea scriptArea, TextField textScript) {

		Recorder.saveCommand();
		String text = null;
		if (scriptButton.isSelected() == Boolean.TRUE)
			text = scriptArea.getText();

		String name = null;
		if (pathButton.isSelected() == Boolean.TRUE) {
			name = textScript.getText();
			File f = new File(name);
			try {
				int size = (int) f.length();
				byte[] buffer = new byte[size];
				FileInputStream in = new FileInputStream(f);
				in.read(buffer, 0, size);
				text = new String(buffer, 0, size, "ISO8859_1");
				in.close();
				OpenDialog.setLastDirectory(f.getParent() + File.separator);
				OpenDialog.setLastName(f.getName());

			} catch (Exception e) {

			}

		}
		boolean java = true;
		if (text.indexOf("rm.") != -1) {
			text = (java ? "RoiManager " : "") + "rm = RoiManager.getRoiManager();\n" + text;
		}
		if (text.contains("overlay.add"))
			text = (java ? "Overlay " : "") + "overlay = new Overlay();\n" + text;
		if ((text.contains("imp.") || text.contains("(imp") || text.contains("overlay.add"))
				&& !text.contains("IJ.openImage") && !text.contains("IJ.openVirtual")
				&& !text.contains("IJ.createImage"))
			text = (java ? "ImagePlus " : "") + "imp = IJ.getImage();\n" + text;
		if (text.contains("overlay.add"))
			text = text + "imp.setOverlay(overlay);\n";
		if (text.indexOf("imp =") != -1 && !(text.indexOf("IJ.getImage") != -1 || text.indexOf("IJ.saveAs") != -1
				|| text.indexOf("imp.close") != -1))
			text = text + "imp.show();\n";

		StringTokenizer st = new StringTokenizer(text, "\n");
		int n = st.countTokens();
		boolean impDeclared = false;
		boolean lutDeclared = false;
		String line;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < n; i++) {
			line = st.nextToken();
			if (line != null && line.length() > 3) {
				sb.append("\t\t");
				if (line.startsWith("imp =") && !impDeclared) {
					sb.append("ImagePlus ");
					impDeclared = true;
				}

				if (line.startsWith("lut =") && !lutDeclared) {
					sb.append("LUT ");
					lutDeclared = true;
				}
				/*
				 * if (line.startsWith("run") && !lutDeclared) { sb.insert(4, 'i'); sb.insert(5,
				 * 'm'); sb.insert(6, 'p'); sb.insert(7, ','); lutDeclared = true; }
				 */
				sb.append(line);
				sb.append('\n');
			}
		}
		text2 = new String(sb);
		text2 = text2.replaceAll("print", "IJ.log");
	
	}

}
