package org.nees.mustsim.replay.test.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.nees.mustsim.replay.data.DoubleMatrix;
import org.nees.mustsim.replay.test.utils.mgen.GenerateActualCoupleNulls;
import org.nees.mustsim.replay.test.utils.mgen.GenerateActualEarly;
import org.nees.mustsim.replay.test.utils.mgen.GenerateActualEmpty;
import org.nees.mustsim.replay.test.utils.mgen.GenerateActualFull;
import org.nees.mustsim.replay.test.utils.mgen.GenerateActualLate;
import org.nees.mustsim.replay.test.utils.mgen.GenerateActualSingleNull;
import org.nees.mustsim.replay.test.utils.mgen.GenerateColumn;
import org.nees.mustsim.replay.test.utils.mgen.GenerateExpectedEarly;
import org.nees.mustsim.replay.test.utils.mgen.GenerateExpectedFull;
import org.nees.mustsim.replay.test.utils.mgen.GenerateExpectedLate;

public class DoubleMatrixGenerator {

	public enum ColumnTypes {
		Full, Empty, SingleNull, MultiNull, CoupleNulls, SingleEarly, DoubleEarly, SingleLate, DoubleLate
	};

	private final Map<ColumnTypes, GenerateColumn> generators = new HashMap<DoubleMatrixGenerator.ColumnTypes, GenerateColumn>();
	private final Map<ColumnTypes, GenerateColumn> expected = new HashMap<DoubleMatrixGenerator.ColumnTypes, GenerateColumn>();
	private final int rowSize;

	public List<List<Double>> generate(List<ColumnTypes> specs,
			boolean isExpected) {
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

	public DoubleMatrixGenerator(int rowSize, double increment) {
		this.rowSize = rowSize;
		generators.put(ColumnTypes.Full, new GenerateActualFull(rowSize,
				increment));
		generators.put(ColumnTypes.Empty, new GenerateActualEmpty(rowSize,
				increment));
		generators.put(ColumnTypes.SingleNull, new GenerateActualSingleNull(
				rowSize, increment, 1));
		generators.put(ColumnTypes.MultiNull, new GenerateActualSingleNull(
				rowSize, increment, 3));
		generators.put(ColumnTypes.CoupleNulls, new GenerateActualCoupleNulls(
				rowSize, increment));
		generators.put(ColumnTypes.SingleEarly, new GenerateActualEarly(
				rowSize, increment, 1));
		generators.put(ColumnTypes.DoubleEarly, new GenerateActualEarly(
				rowSize, increment, 3));
		generators.put(ColumnTypes.SingleLate, new GenerateActualLate(rowSize,
				increment, 1));
		generators.put(ColumnTypes.DoubleLate, new GenerateActualLate(rowSize,
				increment, 3));

		expected.put(ColumnTypes.Full, new GenerateExpectedFull(rowSize,
				increment));
		expected.put(ColumnTypes.Empty, new GenerateActualEmpty(rowSize,
				increment));
		expected.put(ColumnTypes.SingleNull, new GenerateExpectedFull(rowSize,
				increment));
		expected.put(ColumnTypes.MultiNull, new GenerateExpectedFull(rowSize,
				increment));
		expected.put(ColumnTypes.CoupleNulls, new GenerateExpectedFull(rowSize,
				increment));
		expected.put(ColumnTypes.SingleEarly, new GenerateExpectedEarly(
				rowSize, increment, 1));
		expected.put(ColumnTypes.DoubleEarly, new GenerateExpectedEarly(
				rowSize, increment, 3));
		expected.put(ColumnTypes.SingleLate, new GenerateExpectedLate(rowSize,
				increment, 1));
		expected.put(ColumnTypes.DoubleLate, new GenerateExpectedLate(rowSize,
				increment, 3));
	}
	public void compareList2Doubles(DoubleMatrix expected, DoubleMatrix actual) {
		int [] esizes = expected.sizes();
		int [] asizes = actual.sizes();
		Assert.assertEquals(esizes[0], asizes[0]);
		Assert.assertEquals(esizes[1], asizes[1]);
		for (int r = 0; r < esizes[0]; r++) {
			for(int c = 0; c < esizes[1]; c++) {
				Assert.assertEquals(expected.value(r, c), actual.value(r, c), 0.01);
			}
		}
	}
}
