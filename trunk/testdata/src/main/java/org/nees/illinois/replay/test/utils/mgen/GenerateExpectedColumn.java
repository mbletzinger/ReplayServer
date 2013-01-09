package org.nees.illinois.replay.test.utils.mgen;

import java.util.List;

public abstract class GenerateExpectedColumn implements GenerateColumn {
	protected final double increment;
	protected final int rowSize;
	protected final int gap;


	public GenerateExpectedColumn(int rowSize, double increment, int gap) {
		super();
		this.increment = increment;
		this.rowSize = rowSize;
		this.gap = gap;
	}

	protected void genFull(List<List<Double>> result, boolean slopeNegative) {
		for (int r = 0; r < rowSize; r++) {
			List<Double> row = result.get(r);
			double number = r * increment * (slopeNegative ? -1 : 1);
			row.add(number);
		}
	}

	protected void genExtrapolated(List<List<Double>> result,
			boolean slopeNegative, boolean isTail) {
			int end = result.size() ;
		for (int r = 0; r < rowSize; r++) {
			double number;
			List<Double> row = result.get(r);
			if (isTail && (r >= end - gap)) {
				number = (end - (gap + 1)) * increment * (slopeNegative ? -1 : 1);
			} else if ((r < gap) && (isTail == false)) {
				number = gap  * increment * (slopeNegative ? -1 : 1);
			} else {
				number = r * increment * (slopeNegative ? -1 : 1);
			}
			row.add(number);
		}
	}
}