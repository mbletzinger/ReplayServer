package org.nees.illinois.replay.test.utils.mgen;

import java.util.ArrayList;
import java.util.List;

/**
 * Generate a column which skips a number of values first.
 * @author Michael Bletzinger
 */
public class GenerateActualEarly implements GenerateColumnI {
	/**
	 * Actual generator.
	 */
	private final GenerateActualColumn gen;
	/**
	 * amount of elements to skip.
	 */
	private final int gap;

	@Override
	public final void gen(final List<List<Double>> result, final boolean slopeNegative) {
		List<Integer> skips = new ArrayList<Integer>();
		for (int s = 0; s < gap; s++) {
			skips.add(s);
		}
		gen.genCol(result, skips, slopeNegative);
	}

	/**
	 * @param rowSize
	 *            Number of values to generate.
	 * @param increment
	 *            interval between numbers.
	 * @param gap
	 *            Number of elements to skip.
	 */
	public GenerateActualEarly(final int rowSize, final double increment, final int gap) {
		gen = new GenerateActualColumn(rowSize, increment);
		this.gap = gap;
	}

}
