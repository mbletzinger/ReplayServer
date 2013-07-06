package org.nees.illinois.replay.test.utils.mgen;

import java.util.List;

/**
 * Generate the expected results of an interpolated column.
 * @author Michael Bletzinger
 */
public class GenerateExpectedColumn {
	/**
	 * Number of elements that require interpolation/extrapolation.
	 */
	private final int gap;

	/**
	 * Interval between column values.
	 */
	private final double increment;

	/**
	 * Number of elements in the column.
	 */
	private final int rowSize;

	/**
	 * @param rowSize
	 *            Number of elements in the column.
	 * @param increment
	 *            Interval between column values.
	 * @param gap
	 *            Number of elements that require interpolation/extrapolation.
	 */
	public GenerateExpectedColumn(final int rowSize, final double increment,
			final int gap) {
		super();
		this.increment = increment;
		this.rowSize = rowSize;
		this.gap = gap;
	}
	/**
	 * Extrapolate values in the column based on other elements in the column.
	 * matrix.
	 * @param result
	 *            Matrix to insert the result into.
	 * @param slopeNegative
	 *            True if element interval is decrementing.
	 * @param isTail
	 *            True if the extrapolation is happening at the end of the column.
	 */
	public final void genExtrapolated(final List<List<Double>> result,
			final boolean slopeNegative, final boolean isTail) {
		int end = result.size();
		for (int r = 0; r < rowSize; r++) {
			double number;
			List<Double> row = result.get(r);
			if (isTail && (r >= end - gap)) {
				number = (end - (gap + 1)) * increment
						* (slopeNegative ? -1 : 1);
			} else if ((r < gap) && (isTail == false)) {
				number = gap * increment * (slopeNegative ? -1 : 1);
			} else {
				number = r * increment * (slopeNegative ? -1 : 1);
			}
			row.add(number);
		}
	}
	/**
	 * Generate a set of doubles.
	 * @param result
	 *            Matrix to insert the result into.
	 * @param slopeNegative
	 *            True if element interval is decrementing.
	 */
	public final void genFull(final List<List<Double>> result,
			final boolean slopeNegative) {
		for (int r = 0; r < rowSize; r++) {
			List<Double> row = result.get(r);
			double number = r * increment * (slopeNegative ? -1 : 1);
			row.add(number);
		}
	}

	/**
	 * @return the gap
	 */
	public final int getGap() {
		return gap;
	}

	/**
	 * @return the increment
	 */
	public final double getIncrement() {
		return increment;
	}

	/**
	 * @return the rowSize
	 */
	public final int getRowSize() {
		return rowSize;
	}
}
