package org.nees.mustsim.replay.test.utils.mgen;

import java.util.List;

public class GenerateActualEmpty extends GenerateActualColumn {

	@Override
	public void gen(List<List<Double>> result, boolean slopeNegative) {
		for (int r = 0; r < rowSize; r++) {
			List<Double> row = result.get(r);
			row.add(null);
		}
	}

	public GenerateActualEmpty(int rowSize, int increment) {
		super(rowSize, increment,0);
	}

}
