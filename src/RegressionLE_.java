import org.jfree.data.function.Function2D;
import org.jfree.data.function.LineFunction2D;
import org.jfree.data.function.PowerFunction2D;
import org.jfree.data.statistics.Regression;
import org.jfree.data.xy.XYDataset;

public class RegressionLE_ {

	private static double[][] getXYData(XYDataset data, int series) {
		int n = data.getItemCount(series);
		if (n < 1) {
			throw new IllegalArgumentException("Not enough data.");
		}
		double[][] result = new double[n][2];
		for (int i = 0; i < n; i++) {
			result[i][0] = data.getXValue(series, i);
			result[i][1] = data.getYValue(series, i);
		}
		return result;
	}

	/**
	 * Returns the parameters 'a' and 'b' for an equation y = a + (B*ln(x)), fitted
	 * to the data using a logarithmic regression equation. The result is returned
	 * as an array, where double[0] -- a, and double[1] -- b and result[2] -- r^2.
	 *
	 * @param data the data.
	 *
	 * @return The results.
	 */
	public static double[] getOLSRegression(double[][] data) {

		int n = data.length;
		if (n < 2) {
			throw new IllegalArgumentException("LinearRegression: Not enough data.");
		}

		double sumX = 0;
		double sumY = 0;
		double sumXX = 0;
		double sumYY = 0;
		double sumXY = 0;
		for (int i = 0; i < n; i++) {
			double x = data[i][0];
			double y = data[i][1];
			sumX += x;
			sumY += y;
			double xx = x * x;
			sumXX += xx;
			double yy = y * y;
			sumYY += yy;
			double xy = x * y;
			sumXY += xy;
		}
		double sxx = sumXX - (sumX * sumX) / n;
		double sxy = sumXY - (sumX * sumY) / n;
		double xbar = sumX / n;
		double ybar = sumY / n;

		// Compute r^2
		double tmp1 = (n * sumXX) - (sumX * sumX);
		double tmp2 = (n * sumYY) - (sumY * sumY);
		if ((tmp1 < 0.0) || (tmp2 < 0.0))
			throw new IllegalArgumentException("LinearRegression: Data would cause sqrt of a negative number.");
		double numerator = (n * sumXY) - (sumX * sumY);
		double denominator = Math.sqrt(tmp1) * Math.sqrt(tmp2);
		if (denominator == 0.0)
			throw new IllegalArgumentException("LinearRegression: Data would cause divide by zero error.");
		double r = numerator / denominator;

		double[] result = new double[3];
		result[2] = r * r;
		if (sxx < 0)
			throw new IllegalArgumentException("LinearRegression: Data would cause divide by zero error.");
		result[1] = sxy / sxx;
		result[0] = ybar - result[1] * xbar;

		return result;

	}

	/**
	 * Returns the parameters 'a' and 'b' for an equation y = mx + b, fitted to the
	 * data using ordinary least squares regression. The result is returned as a
	 * double[], where result[0] -- Y-Intercept (b), and result[1] -- Slope (m), and
	 * result[2] - r^2.
	 *
	 * @param data   the data.
	 * @param series the series (zero-based index).
	 *
	 * @return The results.
	 */
	public static double[] getOLSRegression(XYDataset data, int series) {

		return getOLSRegression(getXYData(data, series));

	}

	public static double[] getPowerRegression(double[][] data) {

		int n = data.length;
		if (n < 2) {
			throw new IllegalArgumentException("Not enough data.");
		}

		double sumX = 0;
		double sumY = 0;
		double sumXX = 0;
		double sumYY = 0;
		double sumXY = 0;
		for (int i = 0; i < n; i++) {
			double x, y;
			if (data[i][0] > 0.0 && data[i][1] > 0.0) {
				x = Math.log(data[i][0]);
				y = Math.log(data[i][1]);
			} else
				throw new IllegalArgumentException("PowerRegression: X & Y Data must be greater than zero.");
			sumX += x;
			sumY += y;
			double xx = x * x;
			sumXX += xx;
			double yy = y * y;
			sumYY += yy;
			double xy = x * y;
			sumXY += xy;
		}
		double sxx = sumXX - (sumX * sumX) / n;
		double sxy = sumXY - (sumX * sumY) / n;
		double xbar = sumX / n;
		double ybar = sumY / n;

		// Compute r^2
		double tmp1 = (n * sumXX) - (sumX * sumX);
		double tmp2 = (n * sumYY) - (sumY * sumY);
		if ((tmp1 < 0.0) || (tmp2 < 0.0))
			throw new IllegalArgumentException("PowerRegression: Data would cause sqrt of a negative number.");
		double numerator = (n * sumXY) - (sumX * sumY);
		double denominator = Math.sqrt(tmp1) * Math.sqrt(tmp2);
		if (denominator == 0.0)
			throw new IllegalArgumentException("PowerRegression: Data would cause divide by zero error.");
		double r = numerator / denominator;

		double[] result = new double[3];
		result[2] = r * r;
		if (sxx < 0)
			throw new IllegalArgumentException("PowerRegression: Data would cause divide by zero error.");
		result[1] = sxy / sxx;
		result[0] = Math.pow(Math.exp(1.0), ybar - result[1] * xbar);
		return result;

	}

	/**
	 * Returns the parameters 'a' and 'b' for an equation y = ax^b, fitted to the
	 * data using a power regression equation. The result is returned as an array,
	 * where double[0] -- a, and double[1] -- b and result[2] -- r^2.
	 *
	 * @param data   the data.
	 * @param series the series to fit the regression line against.
	 *
	 * @return The results.
	 */
	public static double[] getPowerRegression(XYDataset data, int series) {

		return getPowerRegression(getXYData(data, series));

	}

	public static double[] getLogarithmicRegression(double[][] data) {

		int n = data.length;
		if (n < 2) {
			throw new IllegalArgumentException("LogarithmicRegression: Not enough data.");
		}

		double sumX = 0;
		double sumY = 0;
		double sumXX = 0;
		double sumYY = 0;
		double sumXY = 0;
		for (int i = 0; i < n; i++) {
			double x, y;
			if (data[i][0] > 0.0) {
				x = Math.log(data[i][0]);
			} else
				throw new IllegalArgumentException("LogarithmicRegression: X Data must be greater than zero.");
			y = data[i][1];
			sumX += x;
			sumY += y;
			double xx = x * x;
			sumXX += xx;
			double yy = y * y;
			sumYY += yy;
			double xy = x * y;
			sumXY += xy;
		}
		double sxx = sumXX - (sumX * sumX) / n;
		double sxy = sumXY - (sumX * sumY) / n;
		double xbar = sumX / n;
		double ybar = sumY / n;

		// Compute r^2
		double tmp1 = (n * sumXX) - (sumX * sumX);
		double tmp2 = (n * sumYY) - (sumY * sumY);
		if ((tmp1 < 0.0) || (tmp2 < 0.0))
			throw new IllegalArgumentException("LogarithmicRegression: Data would cause sqrt of a negative number.");
		double numerator = (n * sumXY) - (sumX * sumY);
		double denominator = Math.sqrt(tmp1) * Math.sqrt(tmp2);
		if (denominator == 0.0)
			throw new IllegalArgumentException("LogarithmicRegression: Data would cause divide by zero error.");
		double r = numerator / denominator;

		double[] result = new double[3];
		result[2] = r * r;
		if (sxx < 0)
			throw new IllegalArgumentException("LogarithmicRegression: Data would cause divide by zero error.");
		result[1] = sxy / sxx;
		result[0] = ybar - result[1] * xbar;
		return result;

	}

	/**
	 * Returns the parameters 'a' and 'b' for an equation y = a + (B*ln(x)), fitted
	 * to the data using a logarithmic regression equation. The result is returned
	 * as an array, where double[0] -- a, and double[1] -- b and result[2] -- r^2.
	 *
	 * @param data   the data.
	 * @param series the series to fit the regression line against.
	 *
	 * @return The results.
	 */
	public static double[] getLogarithmicRegression(XYDataset data, int series) {

		return getLogarithmicRegression(getXYData(data, series));

	}

	/**
	 * Returns the parameters 'a' and 'b' for an equation y = a * (e^(b*x)), fitted
	 * to the data using a exponential regression equation. The result is returned
	 * as an array, where double[0] -- a, and double[1] -- b and result[2] -- r^2.
	 *
	 * @param data the data.
	 *
	 * @return The results.
	 */
	public static double[] getExponentialRegression(double[][] data) {

		int n = data.length;
		if (n < 2) {
			throw new IllegalArgumentException("ExponentialRegression: Not enough data.");
		}

		double sumX = 0;
		double sumY = 0;
		double sumXX = 0;
		double sumYY = 0;
		double sumXY = 0;
		for (int i = 0; i < n; i++) {
			double x, y;
			x = data[i][0];
			if (data[i][1] > 0.0) {
				y = Math.log(data[i][1]);
			} else
				throw new IllegalArgumentException("ExponentialRegression: Y Data must be greater than zero.");
			sumX += x;
			sumY += y;
			double xx = x * x;
			sumXX += xx;
			double yy = y * y;
			sumYY += yy;
			double xy = x * y;
			sumXY += xy;
		}
		double sxx = sumXX - (sumX * sumX) / n;
		double sxy = sumXY - (sumX * sumY) / n;
		double xbar = sumX / n;
		double ybar = sumY / n;

		// Compute r^2
		double tmp1 = (n * sumXX) - (sumX * sumX);
		double tmp2 = (n * sumYY) - (sumY * sumY);
		if ((tmp1 < 0.0) || (tmp2 < 0.0))
			throw new IllegalArgumentException("ExponentialRegression: Data would cause sqrt of a negative number.");
		double numerator = (n * sumXY) - (sumX * sumY);
		double denominator = Math.sqrt(tmp1) * Math.sqrt(tmp2);
		if (denominator == 0.0)
			throw new IllegalArgumentException("ExponentialRegression: Data would cause divide by zero error.");
		double r = numerator / denominator;

		double[] result = new double[3];
		result[2] = r * r;
		if (sxx < 0)
			throw new IllegalArgumentException("ExponentialRegression: Data would cause divide by zero error.");
		result[1] = sxy / sxx;
		result[0] = Math.exp(ybar - result[1] * xbar);
		return result;

	}

	public static double[] getExponentialRegression(XYDataset data, int series) {

		return getExponentialRegression(getXYData(data, series));

	}

	public static Function2D getBestRegressionFunction(double[][] data) {
		Function2D retVal = null;
		double r2 = 0.0;
		double[] coefficients = null;

		// Calculate Linear Regression
		try {
			coefficients = RegressionLE_.getOLSRegression(data);
			retVal = new LineFunction2D(coefficients[0], coefficients[1]);
			r2 = coefficients[2];
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		// Calculate Power Regression
		try {
			coefficients = RegressionLE_.getPowerRegression(data);
			if (coefficients[2] > r2) {
				retVal = new PowerFunction2D(coefficients[0], coefficients[1]);
				r2 = coefficients[2];
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		// Calculate Logarithmic Regression
		try {
			coefficients = RegressionLE_.getLogarithmicRegression(data);
			if (coefficients[2] > r2) {
				retVal = new LogarithmicFunction2D(coefficients[0], coefficients[1]);
				r2 = coefficients[2];
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		// Calculate Exponential Regression
		try {
			coefficients = RegressionLE_.getExponentialRegression(data);
			if (coefficients[2] > r2) {
				retVal = new ExponentialFunction2D(coefficients[0], coefficients[1]);
				r2 = coefficients[2];
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		if (retVal == null) {
			throw new IllegalArgumentException("No regression functions were found with current dataset.");
		}

		return retVal;

	}

	public static Function2D getBestRegressionFunction(XYDataset data, int series) {
		return getBestRegressionFunction(getXYData(data, series));
	}

}
