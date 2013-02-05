package org.nees.illinois.replay.test.utils;

import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.data.Mtx2Str;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

public class DataGenerator {
	private int recordNumber = 0;
	private final int numberOfColumns;
	private int[] stepNumber = new int[3];
	private final double timeMultiplier;
	// private static double startTime = (System.currentTimeMillis() / 1000L) ;
	private static double startTime = 222.0;

	// private final Logger log = Logger.getLogger(DataGenerator.class);

	public DataGenerator(int numberOfColumns, double timeMultiplier) {
		super();
		this.numberOfColumns = numberOfColumns;
		stepNumber[0] = 1;
		stepNumber[1] = 0;
		stepNumber[2] = 1;
		this.timeMultiplier = timeMultiplier;
	}

	public double[] genRecord() {
		double[] result = genData(4);
		result[0] = startTime + (recordNumber * timeMultiplier);
		for (int s = 0; s < 3; s++) {
			result[s + 1] = stepNumber[s];
		}
		incrementStep();
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
		if (recordNumber % 4 == 0) {
			stepNumber[0]++;
			stepNumber[1] = 0;
			stepNumber[2] = 0;
		}
		if (recordNumber % 3 == 0) {
			stepNumber[1]++;
			stepNumber[2] = 0;
		}
		stepNumber[2] += timeMultiplier * 10;
		;
	}

	public void reset() {
		recordNumber = 0;
	}

	public static void compareData(double[][] expected, double[][] actual) {
		LoggerFactory.getLogger(DataGenerator.class).info(
				"Comparing expected " + Mtx2Str.matrix2String(expected)
						+ "\nwith actual\n" + Mtx2Str.matrix2String(actual));
		Assert.assertEquals(actual.length, expected.length);
		Assert.assertEquals(actual[0].length, expected[0].length);
		for (int i = 0; i < expected.length; i++) {
			for (int j = 0; j < expected[0].length; j++) {
				Assert.assertEquals(actual[i][j], expected[i][j], 0.001);
			}
		}
	}

	public static double[][] initData(int row, int col,
			double timeMultiplier) {
		double[][] data = new double[row][col];
		DataGenerator dg = new DataGenerator(col, timeMultiplier);
		for (int r = 0; r < row; r++) {
			data[r] = dg.genRecord();
		}
		return data;
	}

	public static double[][] extract(double[][] data, int[] columns) {
		double[][] result = new double[data.length][columns.length];
		for (int r = 0; r < data.length; r++) {
			for (int c = 0; c < columns.length; c++) {
				result[r][c] = data[r][columns[c]];
			}
		}
		return result;
	}

	public static double[][] append(double[][] before, double[][] after) {
		double[][] result = new double[before.length][before[0].length
				+ after[0].length];
		for (int r = 0; r < before.length; r++) {
			for (int b = 0; b < before[r].length; b++) {
				result[r][b] = before[r][b];
			}
			int b = before[r].length;
			for (int a = 0; a < after[r].length; a++) {
				result[r][b + a] = after[r][a];
			}
		}
		return result;
	}

	public static List<List<Double>> toList(double[][] data) {
		List<List<Double>> result = new ArrayList<List<Double>>();
		for (int r = 0; r < data.length; r++) {
			List<Double> row = new ArrayList<Double>();
			result.add(row);
			for (int c = 0; c < data[r].length; c++) {
				row.add(data[r][c]);
			}
		}
		return result;
	}
}
