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
import java.util.ArrayList;
import java.util.List;
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

import ij.Executer;
import ij.IJ;
import ij.io.OpenDialog;
import ij.plugin.Macro_Runner;
import ij.plugin.NewPlugin;
import ij.plugin.frame.*;;

public class ScriptCustomizable_ {

	JTextArea scriptArea;
	JRadioButton scriptButton;
	static JComboBox<String> comboLanguage = new JComboBox<String>();
	// String[] languages = new String[] { "IJ Macro" };
	String completed = " code below : ";
	Preferences prefScript = Preferences.userRoot();
	String CELLTYPEANALYZER_SCRIPT_DEFAULT_PATH = "images_path";
	JButton runButton = new JButton("Run", new ImageIcon(JFrameWizard.createImageIcon("images/compile.png").getImage()
			.getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
	static ArrayList<String> linesAsAL = null;
	static int comboIndex;

	// static String text2;

	public void scriptGUI() {
		if (comboLanguage.getItemCount() == 0)
			comboLanguage.addItem("IJ Macro");
		scriptButton = new JRadioButton("Write or Copy/Paste your  ");
		scriptButton.setSelected(true);
		ButtonGroup group = new ButtonGroup();
		group.add(scriptButton);
		scriptArea = new JTextArea();
		scriptArea.setPreferredSize(new Dimension(400, 300));
		scriptArea.setBackground(new Color(245, 252, 255));
		scriptArea.setLineWrap(true);
		scriptArea.setWrapStyleWord(false);
		JPanel panelScriptArea = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelScriptArea.add(scriptButton);
		panelScriptArea.add(comboLanguage);
		panelScriptArea.add(new JLabel(completed));
		JPanel areaPanel = new JPanel();
		areaPanel.setLayout(new BoxLayout(areaPanel, BoxLayout.Y_AXIS));
		areaPanel.add(panelScriptArea);
		JScrollPane jsp = new JScrollPane(scriptArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		areaPanel.add(jsp);
		areaPanel.add(runButton);
		scriptArea.setEnabled(true);
		if (StartPageModified.textSaved.isEmpty() == false)
			for (int j = 0; j < StartPageModified.textSaved.size(); j++) {
				scriptArea.append(StartPageModified.textSaved.get(j));
				scriptArea.append("\n");
			}
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

				/*
				 * if (pathButton.isSelected() == Boolean.TRUE) { mr.run(textScript.getText());
				 * frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)); }
				 */

				if (scriptButton.isSelected() == Boolean.TRUE) {
					comboIndex = comboLanguage.getSelectedIndex();
					linesAsAL = new ArrayList<String>();
					String txt = scriptArea.getText();
					String[] arrayOfLines = txt.split("\n");
					for (String line : arrayOfLines) {
						linesAsAL.add(line);
						StartPageModified.textSaved.add(line);
					}

					frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
				}
				if (scriptButton.isSelected() == Boolean.FALSE) {
					linesAsAL = null;
				}

			}
		});

	}

}
