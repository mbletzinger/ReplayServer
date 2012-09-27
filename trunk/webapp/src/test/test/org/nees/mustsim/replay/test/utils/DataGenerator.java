package org.nees.mustsim.replay.test.utils;

import java.util.Date;

import org.nees.mustsim.replay.db.table.RateType;

public class DataGenerator {
	private int recordNumber = 1;
	private final int numberOfColumns;
	private int[] stepNumber = new int[3];

	public DataGenerator(int numberOfColumns) {
		super();
		this.numberOfColumns = numberOfColumns;
		stepNumber[0] = 1;
		stepNumber[1] = 0;
		stepNumber[2] = 1;
	}

	public double[] genRecord(RateType rate) {
		double[] result = genData(rate.equals(RateType.CONT) ? 1 : 4);
		result[0] = 	(System.currentTimeMillis() / 1000L) + recordNumber;
		if(rate.equals(RateType.STEP)) {
			for( int s = 0; s < 3; s++) {
				result[s + 1] = stepNumber[s];
			}
			incrementStep();
		}
		recordNumber++;
		return result;
		
	}

	private double[] genData(int start) {
		double[] result = new double[start + numberOfColumns];
		for (int c = 0; c < numberOfColumns; c++) {
			result[c + start] = ((recordNumber % 20) * .01 + c * .003)
					* (c % 2 == 0 ? 1 : -1);
		}
		return result;
		
	}

	private void incrementStep() {
		if (recordNumber % 3 == 0) {
			stepNumber[0]++;
			stepNumber[1] = 0;
			stepNumber[2] = 0;
		}
		if (recordNumber % 2 == 0) {
			stepNumber[1]++;
			stepNumber[2] = 0;
		}
		stepNumber[2]++;
	}

	public void reset() {
		recordNumber = 0;
	}
}
