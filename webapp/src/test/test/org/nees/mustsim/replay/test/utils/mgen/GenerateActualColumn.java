package org.nees.mustsim.replay.test.utils.mgen;

import java.util.List;

public abstract class GenerateActualColumn  implements GenerateColumn{
	protected final int increment;
	protected final int rowSize;
	protected final int gap; 

	public GenerateActualColumn(int increment, int rowSize, int gap) {
		super();
		this.increment = increment;
		this.rowSize = rowSize;
		this.gap = gap;
	}

	protected void genCol(List<List<Double>> result, List<Integer> skips, boolean slopeNegative) {
		for(int r = 0; r < rowSize; r++) {
			if(skips.contains(r)) {
				continue;
			}
			List<Double> row = result.get(r);
			double number = r*increment * (slopeNegative ? -1:1);
			row.add(number);
		}
	}
}
