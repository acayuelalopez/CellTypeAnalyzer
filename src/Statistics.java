import java.util.Collections;
import java.util.List;

public class Statistics {

	public static double median(List<Double> a) {
		int middle = a.size() / 2;

		if (a.size() % 2 == 1) {
			return a.get(middle);
		} else {
			return (a.get(middle - 1) + a.get(middle)) / 2.0;
		}
	}

	public static double IQR(List<Double> a, int n) {

		Collections.sort(a);
		int mid_index = medianIQR(a, 0, n);
		double Q1 = a.get(medianIQR(a, 0, mid_index));
		double Q3 = a.get(medianIQR(a, mid_index + 1, n));

		return (Q3 - Q1);
	}

	public static double Q3(List<Double> a, int n) {
		Collections.sort(a);
		int mid_index = medianIQR(a, 0, n);
		double Q3 = a.get(medianIQR(a, mid_index + 1, n));
		return (Q3);
	}

	public static double Q1(List<Double> a, int n) {
		Collections.sort(a);
		int mid_index = medianIQR(a, 0, n);
		double Q1 = a.get(medianIQR(a, 0, mid_index));
		return (Q1);
	}

	public static double variance(List<Double> a) {
		double mean = a.stream().mapToDouble(Double::doubleValue).sum() / a.size();
		double square = 0;
		for (double element : a)
			square += (element - mean) * (element - mean);
		return square / a.size();
	}

	public static double mean(List<Double> a) {
		double mean = a.stream().mapToDouble(Double::doubleValue).sum() / a.size();
		return mean;
	}

	public static double sum(List<Double> a) {
		double sum = a.stream().mapToDouble(Double::doubleValue).sum();
		return sum;
	}

	public static int medianIQR(List<Double> a, int d, int mid_index) {
		int n = mid_index - d + 1;
		n = (n + 1) / 2 - 1;
		return n + d;
	}

	public static double sd(List<Double> a) {
		int sum = 0;
		double mean = a.stream().mapToDouble(Double::doubleValue).sum() / a.size();

		for (Double i : a)
			sum += Math.pow((i - mean), 2);
		return Math.sqrt(sum / (a.size() - 1)); // sample
	}

}
