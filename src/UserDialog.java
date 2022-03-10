import ij.*;
import ij.gui.GUI;
import ij.gui.MultiLineLabel;
import ij.plugin.frame.RoiManager;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;

public class UserDialog extends JDialog implements ActionListener, KeyListener {
	static JButton button;
	public MultiLineLabel label;
	static protected int xloc = -1, yloc = -1;
	private boolean escPressed;

	public UserDialog(String title, String text, JScrollPane scrollParameters) {
		super(IJ.getInstance(), title, false);
		IJ.protectStatusBar(false);
		if (text != null && text.startsWith("IJ: "))
			text = text.substring(4);
		label = new MultiLineLabel(text, 175);
		if (!IJ.isLinux())
			label.setFont(new Font("SansSerif", Font.PLAIN, 14));
		if (IJ.isMacOSX()) {
			RoiManager rm = RoiManager.getInstance();
			if (rm != null)
				rm.runCommand("enable interrupts");
		}
		GridBagLayout gridbag = new GridBagLayout(); // set up the layout
		GridBagConstraints c = new GridBagConstraints();
		setLayout(gridbag);
		c.insets = new Insets(6, 6, 0, 6);
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		add(label, c);
		button = new JButton("  OK  ");
		button.setBounds(50, 100, 95, 30);
		button.setToolTipText("Click this button to process next image.");
		button.addActionListener(this);
		button.addKeyListener(this);
		c.insets = new Insets(2, 6, 6, 6);
		c.gridx = 0;
		c.gridy = 2;
		c.anchor = GridBagConstraints.EAST;
		add(scrollParameters, c);
		add(button);
		setResizable(true);
		addKeyListener(this);
		GUI.scale(this);
		pack();
		if (xloc == -1)
			GUI.centerOnImageJScreen(this);
		else
			setLocation(xloc, yloc);
		setAlwaysOnTop(true);
	}

	public UserDialog(String text, JScrollPane scrollParameters) {
		this("Action Required", text, scrollParameters);
	}

	public void show() {
		super.show();
		synchronized (this) { // wait for OK
			try {
				wait();
			} catch (InterruptedException e) {
				return;
			}
		}
	}

	public void close() {
		synchronized (this) {
			notify();
		}
		xloc = getLocation().x;
		yloc = getLocation().y;
		dispose();
	}

	public void actionPerformed(ActionEvent e) {
		close();
	}

	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		IJ.setKeyDown(keyCode);
		if (keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_ESCAPE) {
			escPressed = keyCode == KeyEvent.VK_ESCAPE;
			close();
		}
	}

	public boolean escPressed() {
		return escPressed;
	}

	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		IJ.setKeyUp(keyCode);
	}

	public void keyTyped(KeyEvent e) {
	}

	/** Returns a reference to the 'OK' button */
	public JButton getButton() {
		return button;
	}

	/** Display the next WaitForUser dialog at the specified location. */
	public static void setNextLocation(int x, int y) {
		xloc = x;
		yloc = y;
	}

}