package org.nees.illinois.replay.test.utils.mgen;

import java.util.List;

/**
 * Creates a column of nulls.
 * @author Michael Bletzinger
 */
public class GenerateActualEmpty implements GenerateColumnI {
	/**
	 * number of elements.
	 */
	private final int rowSize;

	@Override
	public final void gen(final List<List<Double>> result, final boolean slopeNegative) {
		for (int r = 0; r < rowSize; r++) {
			List<Double> row = result.get(r);
			row.add(null);
		}
	}

	/**
	 * @param rowSize
	 *            number of elements.
	 */
	public GenerateActualEmpty(final int rowSize) {
		this.rowSize = rowSize;
	}

}
