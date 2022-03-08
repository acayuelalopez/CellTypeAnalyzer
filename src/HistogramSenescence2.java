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
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.statistics.HistogramDataset;

import ij.IJ;
import ij.ImagePlus;

public class HistogramSenescence2 {

	private static int BINS = 256;
	private HistogramDataset dataset;
	private XYBarRenderer renderer;
	private XYPlot plot;
	private double[] r;
	public ChartPanel panel;
	public JCheckBox red, green, blue;

	public HistogramSenescence2() {

	}

	public void addSeries(ImagePlus imp) {

		panel.removeAll();
		dataset = new HistogramDataset();
		Image impInitial = imp.getImage().getScaledInstance(190, 170, Image.SCALE_SMOOTH);
		BufferedImage image = new ImagePlus("", impInitial).getBufferedImage();
		Raster raster = image.getRaster();
		final int w = image.getWidth();
		final int h = image.getHeight();
		r = new double[w * h];
		r = raster.getSamples(0, 0, w, h, 0, r);
		dataset.addSeries("R", r, BINS);
		r = raster.getSamples(0, 0, w, h, 1, r);
		dataset.addSeries("G", r, BINS);
		r = raster.getSamples(0, 0, w, h, 2, r);
		dataset.addSeries("B", r, BINS);
		JFreeChart chart = ChartFactory.createHistogram("", "Mean Intensiy", "Pixel Count", dataset,
				PlotOrientation.VERTICAL, true, true, false);
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
		panel.setPreferredSize(new Dimension(600, 240));

		chart.setBackgroundPaint(new Color(0xFF, 0xFF, 0xFF, 0));
		plot.setBackgroundPaint(new Color(0xFF, 0xFF, 0xFF, 0));
		chart.getLegend().setBackgroundPaint(new Color(0xFF, 0xFF, 0xFF, 0));
		panel.setMouseWheelEnabled(true);
		Font font3 = new Font("Dialog", Font.ITALIC, 10);
		plot.getDomainAxis().setLabelFont(font3);
		plot.getRangeAxis().setLabelFont(font3);
		plot.getRangeAxis().setTickLabelFont(font3);
		plot.getDomainAxis().setTickLabelFont(font3);

	}

	public ChartPanel createChartPanel(ImagePlus imp) {
		// dataset
		if (imp == null)
			dataset = new HistogramDataset();

		// chart
		JFreeChart chart = ChartFactory.createHistogram("", "Mean Intensiy", "Pixel Count", dataset,
				PlotOrientation.VERTICAL, true, true, false);
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
		panel.setPreferredSize(new Dimension(600, 240));

		chart.setBackgroundPaint(new Color(0xFF, 0xFF, 0xFF, 0));
		plot.setBackgroundPaint(new Color(0xFF, 0xFF, 0xFF, 0));
		chart.getLegend().setBackgroundPaint(new Color(0xFF, 0xFF, 0xFF, 0));
		panel.setMouseWheelEnabled(true);
		Font font3 = new Font("Dialog", Font.ITALIC, 10);
		plot.getDomainAxis().setLabelFont(font3);
		plot.getRangeAxis().setLabelFont(font3);
		plot.getRangeAxis().setTickLabelFont(font3);
		plot.getDomainAxis().setTickLabelFont(font3);

		return panel;
	}

	public JPanel createControlPanel() {
		JPanel panel = new JPanel();
		red = new JCheckBox();
		green = new JCheckBox();
		blue = new JCheckBox();
		panel.add(red);
		panel.add(green);
		panel.add(blue);

		return panel;
	}

	public void refreshControlPanel() {
		red.setAction(new VisibleAction(0));
		green.setAction(new VisibleAction(1));
		blue.setAction(new VisibleAction(2));

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