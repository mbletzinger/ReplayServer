package org.nees.illinois.replay.data;

import java.util.List;

/**
 * Compares two rows based on the time columns.
 * @author Michael Bletzinger
 */
public class TimeCompare {
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
	 * Compares two rows.
	 * @param first
	 *            First row.
	 * @param second
	 *            Second row.
	 * @return -1, 1, or 0 depending on a < b, a > b, or a = b
	 */
	public final int compareRow(final List<Double> first, final List<Double> second) {
			return compare(first.get(0), second.get(0));
	}

}
