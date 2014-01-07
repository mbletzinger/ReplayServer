package org.nees.illinois.replay.test.utils.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.nees.illinois.replay.data.DoubleMatrixI;
import org.nees.illinois.replay.test.utils.mgen.GenerateActualCoupleNulls;
import org.nees.illinois.replay.test.utils.mgen.GenerateActualEarly;
import org.nees.illinois.replay.test.utils.mgen.GenerateActualEmpty;
import org.nees.illinois.replay.test.utils.mgen.GenerateActualFull;
import org.nees.illinois.replay.test.utils.mgen.GenerateActualLate;
import org.nees.illinois.replay.test.utils.mgen.GenerateActualSingleNull;
import org.nees.illinois.replay.test.utils.mgen.GenerateColumnI;
import org.nees.illinois.replay.test.utils.mgen.GenerateExpectedEarly;
import org.nees.illinois.replay.test.utils.mgen.GenerateExpectedFull;
import org.nees.illinois.replay.test.utils.mgen.GenerateExpectedLate;

/**
 * Test the interpolation of a column with nulls elements at different places.
 * @author Michael Bletzinger
 */
public class InterpolateTestData {
	/**
	 * Test cases.
	 * @author Michael Bletzinger
	 */
	public enum ColumnTypes {
		/**
		 * No nulls in column.
		 */
		Full,
		/**
		 * Column has no values.
		 */
		Empty,
		/**
		 * Column has one missing element.
		 */
		SingleNull,
		/**
		 * Column is missing several elements.
		 */
		MultiNull,
		/**
		 * Column is missing a couple of elements.
		 */
		CoupleNulls,
		/**
		 * First element is null.
		 */
		SingleEarly,
		/**
		 * First two elements are null.
		 */
		DoubleEarly,
		/**
		 * Last element is null.
		 */
		SingleLate,
		/**
		 * Last two elements are null.
		 */
		DoubleLate
	};

	/**
	 * Column generates for the different test cases.
	 */
	private final Map<ColumnTypes, GenerateColumnI> generators = new HashMap<InterpolateTestData.ColumnTypes, GenerateColumnI>();
	/**
	 * Expected result generators for the different test cases.
	 */
	private final Map<ColumnTypes, GenerateColumnI> expected = new HashMap<InterpolateTestData.ColumnTypes, GenerateColumnI>();
	/**
	 * Size of the column.
	 */
	private final int rowSize;

	/**
	 * Generate a matrix based on the column specifications.
	 * @param specs
	 *            Column specifications.
	 * @param isExpected
	 *            True if the matrix is an expected result.
	 * @return The generated matrix.
	 */
	public final List<List<Double>> generate(final List<ColumnTypes> specs,
			final boolean isExpected) {
		List<List<Double>> result = new ArrayList<List<Double>>();
		for (int r = 0; r < rowSize; r++) {
			result.add(new ArrayList<Double>());
		}
		boolean slopeNegative = false;
		for (ColumnTypes s : specs) {
			if (isExpected) {
				expected.get(s).gen(result, slopeNegative);

			} else {
				generators.get(s).gen(result, slopeNegative);
			}
			slopeNegative = !slopeNegative;
		}
		return result;
	}

	/**
	 * @param rowSize
	 *            Size of the generated columns.
	 * @param increment
	 *            Increment between column elements.
	 */
	public InterpolateTestData(final int rowSize, final double increment) {
		final int threeGap = 3;
		this.rowSize = rowSize;
		generators.put(ColumnTypes.Full, new GenerateActualFull(rowSize,
				increment));
		generators.put(ColumnTypes.Empty, new GenerateActualEmpty(rowSize));
		generators.put(ColumnTypes.SingleNull, new GenerateActualSingleNull(
				rowSize, increment, 1));
		generators.put(ColumnTypes.MultiNull, new GenerateActualSingleNull(
				rowSize, increment, threeGap));
		generators.put(ColumnTypes.CoupleNulls, new GenerateActualCoupleNulls(
				rowSize, increment));
		generators.put(ColumnTypes.SingleEarly, new GenerateActualEarly(
				rowSize, increment, 1));
		generators.put(ColumnTypes.DoubleEarly, new GenerateActualEarly(
				rowSize, increment, threeGap));
		generators.put(ColumnTypes.SingleLate, new GenerateActualLate(rowSize,
				increment, 1));
		generators.put(ColumnTypes.DoubleLate, new GenerateActualLate(rowSize,
				increment, threeGap));

		expected.put(ColumnTypes.Full, new GenerateExpectedFull(rowSize,
				increment));
		expected.put(ColumnTypes.Empty, new GenerateActualEmpty(rowSize));
		expected.put(ColumnTypes.SingleNull, new GenerateExpectedFull(rowSize,
				increment));
		expected.put(ColumnTypes.MultiNull, new GenerateExpectedFull(rowSize,
				increment));
		expected.put(ColumnTypes.CoupleNulls, new GenerateExpectedFull(rowSize,
				increment));
		expected.put(ColumnTypes.SingleEarly, new GenerateExpectedEarly(
				rowSize, increment, 1));
		expected.put(ColumnTypes.DoubleEarly, new GenerateExpectedEarly(
				rowSize, increment, threeGap));
		expected.put(ColumnTypes.SingleLate, new GenerateExpectedLate(rowSize,
				increment, 1));
		expected.put(ColumnTypes.DoubleLate, new GenerateExpectedLate(rowSize,
				increment, threeGap));
	}

	/**
	 * Compare two matrices.
	 * @param expected
	 *            The expected matrix.
	 * @param actual
	 *            The matrix generated fro mthe code under test.
	 */
	public final void compareList2Doubles(final DoubleMatrixI expected,
			final DoubleMatrixI actual) {
		int[] esizes = expected.sizes();
		int[] asizes = actual.sizes();
		Assert.assertEquals(esizes[0], asizes[0]);
		Assert.assertEquals(esizes[1], asizes[1]);
		final double errorTolerance = 0.01;
		for (int r = 0; r < esizes[0]; r++) {
			for (int c = 0; c < esizes[1]; c++) {
				Assert.assertEquals(expected.value(r, c), actual.value(r, c),
						errorTolerance);
			}
		}
	}
}
