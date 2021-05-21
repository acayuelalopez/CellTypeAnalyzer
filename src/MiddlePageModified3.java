import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.ValueMarker;

import ij.ImagePlus;

@SuppressWarnings("serial")
public class MiddlePageModified3 extends AbstractWizardPage {

	public MiddlePageModified3() {
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
	}

	@Override
	protected AbstractWizardPage getNextPage() {
		return null;
	}

	@Override
	protected boolean isExportAllowed() {
		return true;
	}

	@Override
	protected boolean isPrintAllowed() {
		return true;
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