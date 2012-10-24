package org.nees.mustsim.replay.test.utils.mgen;

import java.util.List;

public abstract class GenerateActualColumn  implements GenerateColumn{
	protected final double increment;
	protected final int rowSize;
	protected final int gap; 

	public GenerateActualColumn(int rowSize, double increment, int gap) {
		super();
		this.increment = increment;
		this.rowSize = rowSize;
		this.gap = gap;
	}

	protected void genCol(List<List<Double>> result, List<Integer> skips, boolean slopeNegative) {
		for(int r = 0; r < rowSize; r++) {
			List<Double> row = result.get(r);
			if(skips.contains(r)) {
				row.add(null);
				continue;
			}
			double number = r*increment * (slopeNegative ? -1:1);
			row.add(number);
		}
	}
}
