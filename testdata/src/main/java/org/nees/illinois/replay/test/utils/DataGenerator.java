package org.nees.illinois.replay.test.utils;

import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.data.Mtx2Str;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

public class DataGenerator {
	private final int numberOfRows;
	private final int numberOfColumns;
	private final DataRowGenerator rows;
	private final TimeGenerator time;

	public static void compareData(double[][] expected, double[][] actual) {
		LoggerFactory.getLogger(DataGenerator.class).debug(
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

	public DataGenerator(int numberOfRows, int numberOfColumns,
			double timeMultiplier, double startTime) {
		super();
		this.numberOfRows = numberOfRows;
		this.numberOfColumns = numberOfColumns;
		this.time = new TimeGenerator(timeMultiplier, startTime);
		this.rows = new DataRowGenerator(numberOfColumns);
	}

	public double[][] generate() {
		double[][] data = new double[numberOfRows][numberOfColumns];
		for (int r = 0; r < numberOfRows; r++) {
			data[r] = rows.genRecord(time);
			time.increment();
			time.incrementStep();
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
