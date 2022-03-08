import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYDrawableAnnotation;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.annotations.XYTitleAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.ui.HorizontalAlignment;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.function.Function2D;
import org.jfree.data.function.LineFunction2D;
import org.jfree.data.function.PowerFunction2D;
import org.jfree.data.general.DatasetUtils;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.Regression;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import ij.IJ;
import ij.gui.Roi;

public class ScatterPlotChannels {
	private static final long serialVersionUID = 6294689542092367723L;
	static double[] ch2XValues, ch3YValues;
	static String labelCh2, labelCh3, domainValue, rangeValue;
	private IntervalMarker markerRange, markerDomain;
	public ChartPanel panel;
	private XYSeriesCollection dataset;
	private XYPlot plot;
	static XYLineAndShapeRenderer renderer;
	static int lhCountAll, hhCountAll, llCountAll, hlCountAll, lhCountNID, hhCountNID, llCountNID, hlCountNID,
			lhCountClass, hhCountClass, llCountClass, hlCountClass, comboRegIndex, regOrder;
	static String itemFilters[];
	public List<Integer> roisNameTableIndex;
	public Color[] classColor;
	public Color selectedColorQuadrant;
	static XYSeries series1;

	public ScatterPlotChannels() {

	}

	public ChartPanel createScatterChartPanel(String labelCh2, String labelCh3, double[] ch2XValues,
			double[] ch3YValues, IntervalMarker markerRange, IntervalMarker markerDomain) {
		this.labelCh2 = labelCh2;
		this.labelCh3 = labelCh3;
		this.ch2XValues = ch2XValues;
		this.ch3YValues = ch3YValues;
		this.markerRange = markerRange;
		this.markerDomain = markerDomain;

		dataset = new XYSeriesCollection();
		XYSeries series1 = new XYSeries("Intensity Mean Values");
		for (int i = 0; i < ch2XValues.length; i++)
			series1.add(ch2XValues[i], ch3YValues[i]);
		dataset.addSeries(series1);

		// Create chart
		JFreeChart chart = ChartFactory.createScatterPlot("", "", "", dataset);

		// Changes background color
		plot = (XYPlot) chart.getPlot();
		Paint[] paintArray = { new Color(0x80ff0000, true), new Color(0x8000ff00, true), new Color(0x800000ff, true) };
		plot.setDrawingSupplier(new DefaultDrawingSupplier(paintArray,
				DefaultDrawingSupplier.DEFAULT_FILL_PAINT_SEQUENCE,
				DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE, DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
				DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE, DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE));
		panel = new ChartPanel(chart);
		panel.setMaximumDrawWidth(4000);
		panel.setPreferredSize(new Dimension(480, 270));
		chart.setBackgroundPaint(new Color(0xFF, 0xFF, 0xFF, 0));
		plot.setBackgroundPaint(new Color(0xFF, 0xFF, 0xFF, 0));
		plot.setBackgroundPaint(new GradientPaint(0, 0, Color.green, 200, 200, Color.white, false));
		plot.setBackgroundAlpha(0.1f);
		chart.getLegend().setBackgroundPaint(new Color(0xFF, 0xFF, 0xFF, 0));
		chart.getLegend().setVisible(false);
		panel.setMouseWheelEnabled(true);
		plot.addRangeMarker(markerRange);
		plot.addDomainMarker(markerDomain);

		Font font3 = new Font("Dialog", Font.ITALIC, 9);
		plot.getDomainAxis().setLabelFont(font3);
		plot.getRangeAxis().setLabelFont(font3);
		plot.getRangeAxis().setTickLabelFont(font3);
		plot.getDomainAxis().setTickLabelFont(font3);
		return panel;
	}

	public void addScatterPlotSeries(String labelCh2, String labelCh3, double[] ch2xValues2, double[] ch3yValues2,
			IntervalMarker markerRange, IntervalMarker markerDomain, String domainValue, String rangeValue,
			List<Integer> roisNameTableIndex, Color[] classColor, int comboRegIndex, int regOrder) {
		this.labelCh2 = labelCh2;
		this.labelCh3 = labelCh3;
		this.ch2XValues = ch2xValues2;
		this.ch3YValues = ch3yValues2;
		this.markerRange = markerRange;
		this.markerDomain = markerDomain;
		ScatterPlotChannels.domainValue = domainValue;
		ScatterPlotChannels.rangeValue = rangeValue;
		this.roisNameTableIndex = roisNameTableIndex;
		this.classColor = classColor;
		this.comboRegIndex = comboRegIndex;
		this.regOrder = regOrder;

		panel.removeAll();
		dataset = new XYSeriesCollection();
		series1 = new XYSeries("Intensity Mean Values");
		for (int i = 0; i < ch2xValues2.length; i++)
			series1.add(ch2xValues2[i], ch3yValues2[i]);
		dataset.addSeries(series1);
		// List<Double> domainList = Arrays.asList(ch2xValues2);
		double minDomain = getMin(ch2xValues2);
		double maxDomain = getMax(ch2xValues2);
		Function2D curve = null;
		if (comboRegIndex == 0) {
			double[] coefficients = Regression.getOLSRegression(dataset, 0);
			curve = new LineFunction2D(coefficients[0], coefficients[1]);

		}
		if (comboRegIndex == 1) {
			double[] coefficients = Regression.getPolynomialRegression(dataset, 0, regOrder);
			curve = new PowerFunction2D(coefficients[0], coefficients[1]);

		}
		if (comboRegIndex == 2) {
			double[] coefficients = Regression.getPowerRegression(dataset, 0);
			curve = new PowerFunction2D(coefficients[0], coefficients[1]);

		}
		if (comboRegIndex == 3) {
			double[] coefficients = RegressionLE_.getLogarithmicRegression(dataset, 0);
			curve = new LogarithmicFunction2D(coefficients[0], coefficients[1]);

		}
		if (comboRegIndex == 4) {
			double[] coefficients = RegressionLE_.getExponentialRegression(dataset, 0);
			curve = new ExponentialFunction2D(coefficients[0], coefficients[1]);

		}
		XYDataset regressionData = DatasetUtils.sampleFunction2D(curve, minDomain, maxDomain, ch2xValues2.length,
				"Fitted Regression Line");
		// Create chart
		JFreeChart chart = ChartFactory.createScatterPlot("", "", "", dataset, PlotOrientation.VERTICAL, true, true,
				false);

		plot = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint(new Color(255, 228, 196));
		Paint[] paintArray = { new Color(0x80ff0000, true), new Color(0x8000ff00, true), new Color(0x800000ff, true) };
		plot.setDrawingSupplier(new DefaultDrawingSupplier(paintArray,
				DefaultDrawingSupplier.DEFAULT_FILL_PAINT_SEQUENCE,
				DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE, DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
				DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE, DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE));
		plot.setDataset(1, regressionData);
		XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer(true, false);
		renderer2.setSeriesPaint(0, Color.RED);
		plot.setRenderer(1, renderer2);
		Shape cross = new Ellipse2D.Double(0, 0, 5, 5);
		List<String[]> featureLists = new ArrayList<String[]>();
		for (int i = 0; i < ColorEditorEnd.tableC.getModel().getRowCount(); i++)
			featureLists.add(((JLabel) ColorEditorEnd.tableC.getModel().getValueAt(i,
					ColorEditorEnd.tableC.convertColumnIndexToModel(2))).getText().replace("<html>", "").split("<br>"));

		renderer = new XYLineAndShapeRenderer() {
			@Override
			public Paint getItemPaint(int row, int col) {
				Paint cpaint = getItemColor(row, col);
				if (cpaint == null) {
					cpaint = super.getItemPaint(row, col);
				}
				return cpaint;
			}

			public Color getItemColor(int row, int col) {
				double x = dataset.getXValue(row, col);
				double y = dataset.getYValue(row, col);

				int selectedIndex = 0;
				if (MiddlePageModified2.comboChDomain.getSelectedIndex() == 0
						&& MiddlePageModified2.comboChRange.getSelectedIndex() == 1) {
					for (int i = 0; i < itemFilters.length; i++) {
						for (int a = 0; a < featureLists.size(); a++)
							for (int e = 0; e < featureLists.get(a).length - 1; e++)
								if (itemFilters[i].equals(
										featureLists.get(a)[e].substring(0, featureLists.get(a)[e].lastIndexOf(":"))))
									;
						selectedIndex = i;

					}

				}

				for (int i = 0; i < roisNameTableIndex.size(); i++)
					if (x == ch2xValues2[roisNameTableIndex.get(i)] && y == ch3yValues2[roisNameTableIndex.get(i)])
						return classColor[i];

				for (int i = 0; i < roisNameTableIndex.size(); i++)
					if (x != ch2xValues2[roisNameTableIndex.get(i)] && y != ch3yValues2[roisNameTableIndex.get(i)])
						return Color.LIGHT_GRAY;
				return null;

			}

		};
		plot.setRenderer(renderer);
		renderer.setUseOutlinePaint(true);
		renderer.setSeriesShape(0, cross);
		renderer.setSeriesOutlinePaint(0, Color.black);
		renderer.setSeriesOutlineStroke(0, new BasicStroke(1));
		renderer.setSeriesLinesVisible(0, false);
		renderer.setSeriesPaint(0, Color.LIGHT_GRAY);

		// Create Panel
		panel.setChart(chart);
		panel.setMaximumDrawWidth(6000);
		panel.setPreferredSize(new Dimension(480, 270));
		chart.setBackgroundPaint(new Color(0xFF, 0xFF, 0xFF, 0));
		plot.setBackgroundPaint(new Color(0xFF, 0xFF, 0xFF, 0));
		plot.setBackgroundPaint(new GradientPaint(0, 0, Color.green, 200, 200, Color.white, false));
		plot.setBackgroundAlpha(0.1f);
		chart.getLegend().setBackgroundPaint(new Color(0xFF, 0xFF, 0xFF, 0));
		panel.setMouseWheelEnabled(true);
		chart.getLegend().setVisible(false);
		plot.addRangeMarker(markerRange);
		plot.addDomainMarker(markerDomain);
		markerRange.setLabelFont(new Font("SansSerif", 0, 11));
		markerRange.setLabelPaint(new Color(0, 102, 0));

		Font font3 = new Font("Dialog", Font.BOLD, 11);
		plot.getDomainAxis().setLabelFont(font3);
		plot.getRangeAxis().setLabelFont(font3);
		plot.getRangeAxis().setTickLabelFont(font3);
		plot.getDomainAxis().setTickLabelFont(font3);
		double minRange = getMin(ch3yValues2);
		double maxRange = getMax(ch3yValues2);

		NumberAxis domain = (NumberAxis) plot.getDomainAxis();
		NumberAxis range = (NumberAxis) plot.getRangeAxis();
		domain.setAutoRange(true);
		range.setAutoRange(true);
		// colorr
		if (comboRegIndex == 0)
			computeLinearCoefficients(chart, plot, dataset);
		if (comboRegIndex == 1)
			computePolynomialCoefficients(chart, plot, dataset, regOrder);
		if (comboRegIndex == 2)
			computePowerCoefficients(chart, plot, dataset);
		if (comboRegIndex == 3)
			computeLogarithmicCoefficients(chart, plot, dataset);
		if (comboRegIndex == 4)
			computeExponentialCoefficients(chart, plot, dataset);

	}

	public JPanel createControlPanel() {
		JPanel panel = new JPanel();

		return panel;
	}

	public void display() {
		JFrame f = new JFrame("Histogram");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		f.add(createControlPanel(), BorderLayout.SOUTH);
		f.pack();
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}

	public double getMin(double[] inputArray) {
		Double minValue = inputArray[0];
		for (int i = 1; i < inputArray.length; i++) {
			if (inputArray[i] < minValue) {
				minValue = inputArray[i];
			}
		}
		return minValue;
	}

	public double getMax(double[] inputArray) {
		Double maxValue = inputArray[0];
		for (int i = 1; i < inputArray.length; i++) {
			if (inputArray[i] > maxValue) {
				maxValue = inputArray[i];
			}
		}
		return maxValue;
	}

	private void computeLinearCoefficients(JFreeChart chart, XYPlot plot, XYSeriesCollection dataset) {
		Function2D retVal = null;
		double r2 = 0.0;
		double[] coefficients = null;

		// Calculate Linear Regression
		try {
			coefficients = RegressionLE_.getOLSRegression(dataset, 0);
			retVal = new LineFunction2D(coefficients[0], coefficients[1]);
			r2 = coefficients[2];
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		final double intercept = coefficients[0];
		final double slope = coefficients[1];
		final String linearEquation;
		if (intercept >= 0) {
			linearEquation = "y = " + String.format("%.2f", slope) + "x + " + String.format("%.2f", intercept);
		} else {
			linearEquation = "y = " + String.format("%.2f", slope) + "x - " + Math.abs(intercept);
		}

		TextTitle tt = new TextTitle(linearEquation + "\nR² = " + String.format("%.2f", r2));
		tt.setTextAlignment(HorizontalAlignment.RIGHT);
		tt.setFont(chart.getLegend().getItemFont());
		tt.setBackgroundPaint(new Color(200, 200, 255, 100));
		tt.setFrame(new BlockBorder(Color.white));
		tt.setPosition(RectangleEdge.BOTTOM);
		XYTitleAnnotation r2Annotation = new XYTitleAnnotation(0.85, 0.15, tt);
		r2Annotation.setMaxWidth(0.48);
		plot.addAnnotation(r2Annotation);
	}

	private void computePowerCoefficients(JFreeChart chart, XYPlot plot, XYSeriesCollection dataset) {
		Function2D retVal = null;
		double r2 = 0.0;
		double[] coefficients = null;

		// Calculate Linear Regression

		// Calculate Power Regression
		try {
			coefficients = RegressionLE_.getPowerRegression(dataset, 0);
			if (coefficients[2] > r2) {
				retVal = new PowerFunction2D(coefficients[0], coefficients[1]);
				r2 = coefficients[2];
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		final double intercept = coefficients[0];
		final double slope = coefficients[1];
		final String linearEquation;
		if (intercept >= 0) {
			linearEquation = "y = " + String.format("%.2f", slope) + "x^ " + String.format("%.2f", intercept);
		} else {
			linearEquation = "y = " + String.format("%.2f", slope) + "x^ -" + Math.abs(intercept);
		}

		TextTitle tt = new TextTitle(linearEquation + "\nR² = " + String.format("%.2f", r2));
		tt.setTextAlignment(HorizontalAlignment.RIGHT);
		tt.setFont(chart.getLegend().getItemFont());
		tt.setBackgroundPaint(new Color(200, 200, 255, 100));
		tt.setFrame(new BlockBorder(Color.white));
		tt.setPosition(RectangleEdge.BOTTOM);

		XYTitleAnnotation r2Annotation = new XYTitleAnnotation(0.85, 0.15, tt);
		r2Annotation.setMaxWidth(0.48);
		plot.addAnnotation(r2Annotation);
	}

	private void computePolynomialCoefficients(JFreeChart chart, XYPlot plot, XYSeriesCollection dataset,
			int filterOrder) {

		final double[] coefficients = Regression.getPolynomialRegression(dataset, 0, filterOrder);
		double r2 = coefficients[coefficients.length - 1];
		String polynomialEquation = "";
		for (int i = coefficients.length - 1; i >= 0; i--) {
			if (i == 0) {
				polynomialEquation += String.format("%.2f", coefficients[i]);

			} else if (i == 1) {
				polynomialEquation += String.format("%.2f", coefficients[i]) + "*x+";
			} else if (i > 1) {
				polynomialEquation += String.format("%.2f", coefficients[i]) + "*x^" + i + "+";

			}
		}

		TextTitle tt = new TextTitle("y = " + polynomialEquation + "\nR² = " + String.format("%.2f", r2));
		tt.setTextAlignment(HorizontalAlignment.RIGHT);
		tt.setFont(chart.getLegend().getItemFont());
		tt.setBackgroundPaint(new Color(200, 200, 255, 100));
		tt.setFrame(new BlockBorder(Color.white));
		tt.setPosition(RectangleEdge.BOTTOM);

		XYTitleAnnotation r2Annotation = new XYTitleAnnotation(0.85, 0.15, tt);
		r2Annotation.setMaxWidth(0.48);
		plot.addAnnotation(r2Annotation);
	}

	private void computeLogarithmicCoefficients(JFreeChart chart, XYPlot plot, XYSeriesCollection dataset) {
		Function2D retVal = null;
		double r2 = 0.0;
		double[] coefficients = null;

		try {
			coefficients = RegressionLE_.getLogarithmicRegression(dataset, 0);
			if (coefficients[2] > r2) {
				retVal = new LogarithmicFunction2D(coefficients[0], coefficients[1]);
				r2 = coefficients[2];
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		String logarithmicEquation = "y = " + String.format("%.2f", coefficients[0]) + " + " + "( "
				+ String.format("%.2f", coefficients[1]) + " * ln(x) ) ";

		TextTitle tt = new TextTitle(logarithmicEquation + "\nR² = " + String.format("%.2f", r2));
		tt.setTextAlignment(HorizontalAlignment.RIGHT);
		tt.setFont(chart.getLegend().getItemFont());
		tt.setBackgroundPaint(new Color(200, 200, 255, 100));
		tt.setFrame(new BlockBorder(Color.white));
		tt.setPosition(RectangleEdge.BOTTOM);

		XYTitleAnnotation r2Annotation = new XYTitleAnnotation(0.85, 0.15, tt);
		r2Annotation.setMaxWidth(0.48);
		plot.addAnnotation(r2Annotation);
	}

	private void computeExponentialCoefficients(JFreeChart chart, XYPlot plot, XYSeriesCollection dataset) {
		Function2D retVal = null;
		double r2 = 0.0;
		double[] coefficients = null;

		try {
			coefficients = RegressionLE_.getExponentialRegression(dataset, 0);
			if (coefficients[2] > r2) {
				retVal = new LogarithmicFunction2D(coefficients[0], coefficients[1]);
				r2 = coefficients[2];
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		String exponentialEquation = "y = " + String.format("%.2f", coefficients[0]) + " * " + "( e^("
				+ String.format("%.2f", coefficients[1]) + " * x) ) ";

		TextTitle tt = new TextTitle(exponentialEquation + "\nR² = " + String.format("%.2f", r2));
		tt.setTextAlignment(HorizontalAlignment.RIGHT);
		tt.setFont(chart.getLegend().getItemFont());
		tt.setBackgroundPaint(new Color(200, 200, 255, 100));
		tt.setFrame(new BlockBorder(Color.white));
		tt.setPosition(RectangleEdge.BOTTOM);

		XYTitleAnnotation r2Annotation = new XYTitleAnnotation(0.85, 0.15, tt);
		r2Annotation.setMaxWidth(0.48);
		plot.addAnnotation(r2Annotation);
	}

}