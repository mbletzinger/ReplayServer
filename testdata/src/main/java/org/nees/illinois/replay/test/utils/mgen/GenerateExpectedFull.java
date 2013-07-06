package org.nees.illinois.replay.test.utils.mgen;

import java.util.List;

/**
 * Generate an entire column interpolating any gaps.
 * @author Michael Bletzinger
 */
public class GenerateExpectedFull implements GenerateColumnI {
	/**
	 * Extrapolates and generates values.
	 */
	private final GenerateExpectedColumn gen;

	/**
	 * @param rowSize
	 *            Number of elements in the column.
	 * @param increment
	 *            Interval between column values.
	 */
	public GenerateExpectedFull(final int rowSize, final double increment) {
		gen = new GenerateExpectedColumn(rowSize, increment, 0);
	}

	@Override
	public final void gen(final List<List<Double>> result,
			final boolean slopeNegative) {
		gen.genFull(result, slopeNegative);
	}

}
