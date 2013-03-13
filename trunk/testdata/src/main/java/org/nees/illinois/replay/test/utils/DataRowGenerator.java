package org.nees.illinois.replay.test.utils;

public class DataRowGenerator {
	private final int numberOfColumns;
	public double[] genRecord(TimeGenerator time) {
		double[] result = genData(4, time.getRecordNumber());
		int [] stepNumber = time.getStepNumber();
		result[0] = time.getTime();
		for (int s = 0; s < 3; s++) {
			result[s + 1] = stepNumber[s];
		}
		return result;
	}

	public DataRowGenerator(int numberOfColumns) {
		super();
		this.numberOfColumns = numberOfColumns;
	}

	private double[] genData(int start, int recordNumber) {
		double[] result = new double[start + numberOfColumns];
		for (int c = 0; c < numberOfColumns; c++) {
			result[c + start] = ((recordNumber % 20) * .01 + c * .003)
					* (c % 2 == 0 ? 1 : -1);
		}
		return result;

	}

}
