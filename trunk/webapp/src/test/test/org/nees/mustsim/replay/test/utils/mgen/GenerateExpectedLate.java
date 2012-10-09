package org.nees.mustsim.replay.test.utils.mgen;

import java.util.List;

public class GenerateExpectedLate extends GenerateExpectedColumn {

	public GenerateExpectedLate(int increment, int rowSize, int gap) {
		super(increment, rowSize, gap);
	}

	@Override
	public void gen(List<List<Double>> result, boolean slopeNegative) {
		genExtrapolated(result, slopeNegative, true);
	}

}
