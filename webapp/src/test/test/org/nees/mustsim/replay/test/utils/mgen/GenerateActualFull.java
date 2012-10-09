package org.nees.mustsim.replay.test.utils.mgen;

import java.util.ArrayList;
import java.util.List;

public class GenerateActualFull extends GenerateActualColumn {

	@Override
	public void gen(List<List<Double>> result, boolean slopeNegative) {
		List<Integer> skips = new ArrayList<Integer>();
		genCol(result, skips, slopeNegative);
	}

	public GenerateActualFull(int rowSize, int increment) {
		super(rowSize, increment,0);
	}

}
