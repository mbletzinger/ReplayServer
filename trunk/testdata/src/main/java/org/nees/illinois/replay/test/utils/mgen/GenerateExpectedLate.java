package org.nees.illinois.replay.test.utils.mgen;

import java.util.List;

public class GenerateExpectedLate extends GenerateExpectedColumn {


	public GenerateExpectedLate(int rowSize, double increment, int gap) {
		super(rowSize, increment, gap);
	}

	@Override
	public void gen(List<List<Double>> result, boolean slopeNegative) {
		genExtrapolated(result, slopeNegative, true);
	}

}
