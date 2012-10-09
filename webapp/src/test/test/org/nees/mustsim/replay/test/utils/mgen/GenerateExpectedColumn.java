package org.nees.mustsim.replay.test.utils.mgen;

import java.util.List;

public abstract class GenerateExpectedColumn implements GenerateColumn {
	protected final int increment;
	protected final int rowSize;
	protected final int gap;


	public GenerateExpectedColumn(int increment, int rowSize, int gap) {
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
		for (int r = 0; r < rowSize; r++) {
			double number;
			List<Double> row = result.get(r);
			if (isTail && (r >= gap)) {
				number = (gap - 1) * increment * (slopeNegative ? -1 : 1);
			} else if (r <= gap) {
				number = (gap + 1) * increment * (slopeNegative ? -1 : 1);
			} else {
				number = r * increment * (slopeNegative ? -1 : 1);
			}
			row.add(number);
		}
	}
}
