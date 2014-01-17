package org.nees.illinois.replay.test.utils.mgen;

import java.util.List;

/**
 * Generates a column based on instructions.
 * @author Michael Bletzinger
 */
public class GenerateActualColumn {
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

	/**
	 * interval between numbers.
	 */
	private final double increment;
	/**
	 * Number of values to generate.
	 */
	private final int rowSize;

	/**
	 * @param rowSize
	 *            Number of values to generate.
	 * @param increment
	 *            interval between numbers.
	 */
	public GenerateActualColumn(final int rowSize, final double increment) {
		super();
		this.increment = increment;
		this.rowSize = rowSize;
	}

	/**
	 * Generate a set of doubles.
	 * @param result
	 *            Matrix to insert the column into.
	 * @param skips
	 *            Columns to skip.
	 * @param slopeNegative
	 *            Decrement interval if true.
	 */
	public final void genCol(final List<List<Double>> result, final List<Integer> skips,
			final boolean slopeNegative) {
		for (int r = 0; r < rowSize; r++) {
			List<Double> row = result.get(r);
			if (skips.contains(r)) {
				row.add(null);
				continue;
			}
			double number = r * increment * (slopeNegative ? -1 : 1);
			row.add(number);
		}
	}
}

