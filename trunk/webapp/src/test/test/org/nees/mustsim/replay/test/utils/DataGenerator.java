package org.nees.mustsim.replay.test.utils;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.nees.mustsim.replay.db.data.Mtx2Str;
import org.nees.mustsim.replay.db.table.RateType;

public class DataGenerator {
	private int recordNumber = 1;
	private final int numberOfColumns;
	private int[] stepNumber = new int[3];
//	private final Logger log = Logger.getLogger(DataGenerator.class);

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
	public static void compareData(double[][] expected, double[][] actual) {
		Logger.getLogger(DataGenerator.class).info("Comparing " + Mtx2Str.matrix2String(expected) + "\nwith\n"
				+ Mtx2Str.matrix2String(actual));
		Assert.assertEquals(expected.length, actual.length);
		Assert.assertEquals(expected[0].length, actual[0].length);
		for (int i = 0; i < expected.length; i++) {
			for (int j = 0; j < expected[0].length; j++) {
				Assert.assertEquals(expected[i][j], actual[i][j], 0.001);
			}
		}
	}


	public static double[][] initData(RateType rate, int row, int col) {
		double[][] data = new double[row][col];
		DataGenerator dg = new DataGenerator(col);
		for (int r = 0; r < row; r++) {
			data[r] = dg.genRecord(rate);
		}
		return data;
	}

	public static double [][] extract(double [][] data, int [] columns) {
		double [][] result = new double[data.length][columns.length];
		for(int r = 0; r < data.length; r++) {
			for (int c = 0; c < columns.length; c++) {
				result[r][c] = data[r][columns[c]];
			}
		}
		return result;
	}
	
	public static double [][] append(double [][] before, double [][] after) {
		double [][] result = new double[before.length][before[0].length + after[0].length];
		for (int r = 0; r < before.length; r++) {
			for(int b = 0; b < before[r].length;b++) {
				result[r][b] = before[r][b];
			}
			int b = before[r].length;
			for (int a = 0; a < after[r].length;a++) {
				result[r][b + a] = after[r][a]; 
			}
		}
		return result;
	}
}
