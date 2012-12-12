package org.nees.illinois.replay.test.utils.mgen;

import java.util.ArrayList;
import java.util.List;

public class GenerateActualLate extends GenerateActualColumn {

	@Override
	public void gen(List<List<Double>> result, boolean slopeNegative) {
		List<Integer> skips = new ArrayList<Integer>();
		for (int s = rowSize - gap; s < rowSize; s++) {
			skips.add(s);
		}
		genCol(result, skips, slopeNegative);		
	}

	public GenerateActualLate(int rowSize, double increment, int gap) {
		super(rowSize, increment, gap);
	}



}
