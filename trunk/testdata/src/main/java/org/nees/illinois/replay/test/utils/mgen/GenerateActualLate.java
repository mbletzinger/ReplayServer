package org.nees.illinois.replay.test.utils.mgen;

import java.util.ArrayList;
import java.util.List;

/**
 * Generate a column of doubles with nulls at the end.
 * @author Michael Bletzinger
 */
public class GenerateActualLate implements GenerateColumnI {
	/**
	 * Size of nulls at the end of the column.
	 */
	private final int gap;
	/**
	 * Actual generator.
	 */
	private final GenerateActualColumn gen;

	@Override
	public final void gen(final List<List<Double>> result, final boolean slopeNegative) {
		List<Integer> skips = new ArrayList<Integer>();
		for (int s = gen.getRowSize() - gap; s < gen.getRowSize(); s++) {
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
	 *            Size of nulls at the end of the column.
	 */
	public GenerateActualLate(final int rowSize, final double increment, final int gap) {
		gen = new GenerateActualColumn(rowSize, increment);
		this.gap = gap;
	}

}
