package org.nees.mustsim.replay.test.utils.mgen;

import java.util.ArrayList;
import java.util.List;

public class GenerateActualEarly extends GenerateActualColumn {

	@Override
	public void gen(List<List<Double>> result, boolean slopeNegative) {
		List<Integer> skips = new ArrayList<Integer>();
		for (int s = 0; s < gap; s++) {
			skips.add(s);
		}
		genCol(result, skips, slopeNegative);
	}

	public GenerateActualEarly(int increment, int rowSize, int gap) {
		super(increment, rowSize, gap);
	}

}
