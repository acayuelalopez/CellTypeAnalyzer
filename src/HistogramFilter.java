import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.statistics.HistogramDataset;

import ij.IJ;
import ij.ImagePlus;

public class HistogramFilter {

	private int BINS;
	//private Image impInitial = IJ.getImage().getImage().getScaledInstance(190, 170, Image.SCALE_SMOOTH);
	//private final BufferedImage image = new ImagePlus("", impInitial).getBufferedImage();
	private HistogramDataset dataset;
	private XYBarRenderer renderer;
	private XYPlot plot;
	private double[] values;
	private String feature;
	private ChartPanel panel;
	public IntervalMarker intervalMarker;

	public HistogramFilter() {

	}

	public ChartPanel createChartPanel(String feature, double[] values, int BINS, IntervalMarker intervalMarker) {
		this.feature = feature;
		this.values = values;
		this.BINS = BINS;
		this.intervalMarker = intervalMarker;

		// dataset
		dataset = new HistogramDataset();
		dataset.addSeries(feature, values, BINS);
		// chart
		JFreeChart chart = ChartFactory.createHistogram("", feature, "Count", dataset, PlotOrientation.VERTICAL,
				true, true, false);

		plot = (XYPlot) chart.getPlot();
		renderer = (XYBarRenderer) plot.getRenderer();
		renderer.setBarPainter(new StandardXYBarPainter());
		// translucent red, green & blue
		Paint[] paintArray = { new Color(0x80ff0000, true), new Color(0x8000ff00, true), new Color(0x800000ff, true) };
		plot.setDrawingSupplier(new DefaultDrawingSupplier(paintArray,
				DefaultDrawingSupplier.DEFAULT_FILL_PAINT_SEQUENCE,
				DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE, DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
				DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE, DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE));
		panel = new ChartPanel(chart);
		panel.setMaximumDrawWidth(4000);
		panel.setPreferredSize(new Dimension(350, 160));

		chart.setBackgroundPaint(new Color(0xFF, 0xFF, 0xFF, 0));
		plot.setBackgroundPaint(new Color(0xFF, 0xFF, 0xFF, 0));
		chart.getLegend().setBackgroundPaint(new Color(0xFF, 0xFF, 0xFF, 0));
		panel.setMouseWheelEnabled(true);
		Font font3 = new Font("Dialog", Font.ITALIC, 9);
		plot.getDomainAxis().setLabelFont(font3);
		plot.getRangeAxis().setLabelFont(font3);
		plot.getRangeAxis().setTickLabelFont(font3);
		plot.getDomainAxis().setTickLabelFont(font3);
		chart.getLegend().setVisible(false);
		plot.addDomainMarker(intervalMarker);
		double x = (float) (0.05 * plot.getDomainAxis().getRange().getLength());
		double y = (float) (0.85 * plot.getRangeAxis().getUpperBound());

		return panel;
	}

	public JPanel createControlPanel() {
		JPanel panel = new JPanel();

		return panel;
	}

	public void addHistogramSeries(String feature, double[] values, int BINS, IntervalMarker intervalMarker) {
		this.feature = feature;
		this.values = values;
		this.BINS = BINS;
		this.intervalMarker = intervalMarker;

		panel.removeAll();
		dataset = new HistogramDataset();
		dataset.addSeries(feature, values, BINS);

		// chart
		JFreeChart chart = ChartFactory.createHistogram("", feature, "Roi Count", dataset, PlotOrientation.VERTICAL,
				true, true, false);

		plot = (XYPlot) chart.getPlot();
		renderer = (XYBarRenderer) plot.getRenderer();
		renderer.setBarPainter(new StandardXYBarPainter());
		// translucent red, green & blue
		Paint[] paintArray = { new Color(0x80ff0000, true), new Color(0x8000ff00, true), new Color(0x800000ff, true) };
		plot.setDrawingSupplier(new DefaultDrawingSupplier(paintArray,
				DefaultDrawingSupplier.DEFAULT_FILL_PAINT_SEQUENCE,
				DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE, DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
				DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE, DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE));
		panel.setChart(chart);
		panel.setMaximumDrawWidth(4000);
		panel.setPreferredSize(new Dimension(350, 160));

		chart.setBackgroundPaint(new Color(0xFF, 0xFF, 0xFF, 0));
		plot.setBackgroundPaint(new Color(0xFF, 0xFF, 0xFF, 0));
		chart.getLegend().setBackgroundPaint(new Color(0xFF, 0xFF, 0xFF, 0));
		panel.setMouseWheelEnabled(true);
		chart.getLegend().setVisible(false);
		Font font3 = new Font("Dialog", Font.ITALIC, 10);
		plot.getDomainAxis().setLabelFont(font3);
		plot.getRangeAxis().setLabelFont(font3);
		plot.getRangeAxis().setTickLabelFont(font3);
		plot.getDomainAxis().setTickLabelFont(font3);
		plot.addDomainMarker(intervalMarker);
	
	}

	public class VisibleAction extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private final int i;

		public VisibleAction(int i) {
			this.i = i;
			this.putValue(NAME, (String) dataset.getSeriesKey(i));
			this.putValue(SELECTED_KEY, true);
			renderer.setSeriesVisible(i, true);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			renderer.setSeriesVisible(i, !renderer.getSeriesVisible(i));
		}
	}



}