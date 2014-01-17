package org.nees.illinois.replay.test.utils.mgen;

import java.util.List;
/**
 * Extrapolate the end of the column.
 * @author Michael Bletzinger
 *
 */

public class GenerateExpectedLate implements GenerateColumnI {


	/**
	 * Extrapolates and generates values.
	 */
	private final GenerateExpectedColumn gen;

	/**
	 * @param rowSize
	 *            Number of elements in the column.
	 * @param increment
	 *            Interval between column values.
	 *            @param gap
	 *            size of the missing elements at the end.
	 */
	public GenerateExpectedLate(final int rowSize, final double increment, final int gap) {
		gen = new GenerateExpectedColumn(rowSize, increment, gap);
	}

	@Override
	public final void gen(final List<List<Double>> result, final boolean slopeNegative) {
		gen.genExtrapolated(result, slopeNegative, true);
	}

}
