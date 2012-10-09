package org.nees.mustsim.replay.test.utils.mgen;

import java.util.ArrayList;
import java.util.List;

public class GenerateActualSingleNull extends GenerateActualColumn {

	@Override
	public void gen(List<List<Double>> result, boolean slopeNegative) {
		List<Integer> skips = new ArrayList<Integer>();
		for (int s = 2; s < 2 + gap; s++) {
			skips.add(s);
		}
		genCol(result, skips, slopeNegative);
	}

	public GenerateActualSingleNull(int increment, int rowSize, int gap) {
		super(increment, rowSize, gap);
	}

}
