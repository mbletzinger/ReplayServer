package org.nees.illinois.replay.test.utils.mgen;

import java.util.ArrayList;
import java.util.List;

/**
 * Generate a column of doubles.
 * @author Michael Bletzinger
 */
public class GenerateActualFull implements GenerateColumnI {
	/**
	 * Actual generator.
	 */
	private final GenerateActualColumn gen;

	@Override
	public final void gen(final List<List<Double>> result,
			final boolean slopeNegative) {
		List<Integer> skips = new ArrayList<Integer>();
		gen.genCol(result, skips, slopeNegative);
	}

	/**
	 * @param rowSize
	 *            Number of values to generate.
	 * @param increment
	 *            interval between numbers.
	 */
	public GenerateActualFull(final int rowSize, final double increment) {
		gen = new GenerateActualColumn(rowSize, increment);
	}

}
