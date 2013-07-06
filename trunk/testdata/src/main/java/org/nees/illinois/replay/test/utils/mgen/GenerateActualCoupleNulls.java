package org.nees.illinois.replay.test.utils.mgen;

import java.util.ArrayList;
import java.util.List;

/**
 * Generate a column with two nulls.
 * @author Michael Bletzinger
 */
public class GenerateActualCoupleNulls implements GenerateColumnI {
	/**
	 * Actual generator.
	 */
	private final GenerateActualColumn gen;

	/**
	 * @param rowSize
	 *            Number of values to generate.
	 * @param increment
	 *            interval between numbers.
	 */
	public GenerateActualCoupleNulls(final int rowSize, final double increment) {
		gen = new GenerateActualColumn(rowSize, increment);
	}

	@Override
	public final void gen(final List<List<Double>> result,
			final boolean slopeNegative) {
		List<Integer> skips = new ArrayList<Integer>();
		skips.add(2);
		final int five = 5;
		skips.add(five);
		gen.genCol(result, skips, slopeNegative);
	}

}
