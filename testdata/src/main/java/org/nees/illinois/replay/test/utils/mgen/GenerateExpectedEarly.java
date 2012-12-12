package org.nees.illinois.replay.test.utils.mgen;

import java.util.List;

public class GenerateExpectedEarly extends GenerateExpectedColumn {

	public GenerateExpectedEarly(int rowSize, double increment, int gap) {
		super(rowSize, increment, gap);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void gen(List<List<Double>> result, boolean slopeNegative) {
		genExtrapolated(result, slopeNegative, false);
	}

}
