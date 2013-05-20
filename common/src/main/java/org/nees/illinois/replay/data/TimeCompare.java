package org.nees.illinois.replay.data;

import java.util.List;

/**
 * Compares two rows based on the time columns.
 * @author Michael Bletzinger
 */
public class TimeCompare {
	/**
	 * Determines which columns to use. Assume column 0 is time in seconds and
	 * columns 1 thru 3 are step number columns.
	 */
	private final RateType rate;

	/**
	 *@param rate
	 *Used to determine which time columns to compare.
	 */
	public TimeCompare(final RateType rate) {
		this.rate = rate;
	}

	/**
	 * Compare 2 double values.
	 * @param a
	 *            First value.
	 * @param b
	 *            Second value.
	 * @return -1, 1, or 0 depending on a < b, a > b, or a = b
	 */
	private int compare(final double a, final double b) {
		Double aD = new Double(a);
		Double bD = new Double(b);
		return aD.compareTo(bD);
	}

	/**
	 * Compares two rows with the step number columns.
	 * @param first
	 *            First row.
	 * @param second
	 *            Second row.
	 * @return -1, 1, or 0 depending on a < b, a > b, or a = b
	 */
	private int compare(final List<Double> first, final List<Double> second) {
		final int stepC = 1;
		final int substepC = 2;
		final int correctionStepC = 3;
		StepNumber aS = new StepNumber(first.get(stepC), first.get(substepC), first.get(correctionStepC));
		StepNumber tS = new StepNumber(second.get(stepC), second.get(substepC),
				second.get(correctionStepC));
		return aS.compareTo(tS);
	}

	/**
	 * Compares two rows.
	 * @param first
	 *            First row.
	 * @param second
	 *            Second row.
	 * @return -1, 1, or 0 depending on a < b, a > b, or a = b
	 */
	public final int compareRow(final List<Double> first, final List<Double> second) {
		if (rate.equals(RateType.CONT)) {
			return compare(first.get(0), second.get(0));
		}
		return compare(first, second);
	}

}
