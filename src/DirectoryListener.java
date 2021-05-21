import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;

public class DirectoryListener implements ActionListener {
		String title;
		TextField text;
		int fileSelectionMode;

		public DirectoryListener(String title, TextField text) {
			this(title, text, JFileChooser.DIRECTORIES_ONLY);
		}

		public DirectoryListener(String title, TextField text, int fileSelectionMode) {
			this.title = title;
			this.text = text;
			this.fileSelectionMode = fileSelectionMode;
		}

		public void actionPerformed(ActionEvent e) {
			File directory = new File(text.getText());
			while (directory != null && !directory.exists())
				directory = directory.getParentFile();

			JFileChooser fc = new JFileChooser(directory);
			fc.setFileSelectionMode(fileSelectionMode);

			fc.showOpenDialog(null);
			File selFile = fc.getSelectedFile();
			if (selFile != null)
				text.setText(selFile.getAbsolutePath());
		}
	}