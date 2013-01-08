package org.nees.illinois.replay.test.utils.mgen;

import java.util.List;

public class GenerateExpectedFull extends GenerateExpectedColumn {

	@Override
	public void gen(List<List<Double>> result, boolean slopeNegative) {
		genFull(result, slopeNegative);
	}

	public GenerateExpectedFull(int rowSize, double increment) {
		super(rowSize, increment, 0);
	}


}