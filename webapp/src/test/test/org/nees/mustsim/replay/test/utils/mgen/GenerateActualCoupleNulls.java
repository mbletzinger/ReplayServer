package org.nees.mustsim.replay.test.utils.mgen;

import java.util.ArrayList;
import java.util.List;

public class GenerateActualCoupleNulls extends GenerateActualColumn {

	@Override
	public void gen(List<List<Double>> result, boolean slopeNegative) {
		List<Integer> skips = new ArrayList<Integer>();
		skips.add(2);
		skips.add(5);
		genCol(result, skips, slopeNegative);		
	}

	public GenerateActualCoupleNulls(int rowSize, int increment) {
		super(rowSize, increment,0);
	}

}
