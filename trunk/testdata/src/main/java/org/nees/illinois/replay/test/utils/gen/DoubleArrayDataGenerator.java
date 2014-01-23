package org.nees.illinois.replay.test.utils.gen;

import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.data.Mtx2Str;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

/**
 * Class which generates a unique matrix of data for unit testing.
 * @author Michael Bletzinger
 */
public class DoubleArrayDataGenerator {
	/**
	 * Append two matrices column-wise.
	 * @param before
	 *            Matrix appearing first.
	 * @param after
	 *            Matrix to be appended.
	 * @return Appended matrix of doubles.
	 */
	public static double[][] append(final double[][] before,
			final double[][] after) {
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
	/**
	 * Compare a matrix with the supposedly identical generated version.
	 * @param actual
	 *            Supposedly identical matrix.
	 * @param expected
	 *            Expected matrix which was generated earlier.
	 */
	public static void compareData(final double[][] actual, final double[][] expected) {
		LoggerFactory.getLogger(DoubleArrayDataGenerator.class).debug(
				"Comparing expected " + Mtx2Str.matrix2String(expected)
				+ "\nwith actual\n" + Mtx2Str.matrix2String(actual));
		/**
		 * Tolerance in comparing two doubles to account for rounding errors
		 * produced by the numerical representation.
		 */
		final double tolerance = 0.001;
		Assert.assertEquals(actual.length, expected.length);
		Assert.assertEquals(actual[0].length, expected[0].length);
		for (int i = 0; i < expected.length; i++) {
			for (int j = 0; j < expected[0].length; j++) {

				if (Double.isNaN(expected[i][j])) {
					Assert.assertTrue(Double.isNaN(actual[i][j]));
					continue;
				}
				Assert.assertEquals(actual[i][j], expected[i][j], tolerance);
			}
		}
	}
	/**
	 * extract a submatrix from a given matrix and list of column indexes.
	 * @param data
	 *            Input matrix.
	 * @param columns
	 *            . column indexes.
	 * @return Extracted matrix of doubles.
	 */
	public static double[][] extract(final double[][] data, final int[] columns) {
		double[][] result = new double[data.length][columns.length];
		for (int r = 0; r < data.length; r++) {
			for (int c = 0; c < columns.length; c++) {
				result[r][c] = data[r][columns[c]];
			}
		}
		return result;
	}
	/**
	 * Convert a double matrix into a list of double lists.
	 * @param data
	 *            Matrix of doubles.
	 * @return List of lists.
	 */
	public static List<List<Double>> toList(final double[][] data) {
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

	/**
	 * Number of rows in the matrix.
	 */
	private final int numberOfRows;

	/**
	 * Number of columns in the matrix.
	 */
	private final int numberOfColumns;

	/**
	 * Row generator.
	 */
	private final DataRowGenerator rows;

	/**
	 * Time generator.
	 */
	private final TimeGenerator time;

	/**
	 * Generate a matrix with the first row having a given start time.
	 * @param numberOfRows
	 *            Rows in the matrix.
	 * @param numberOfColumns
	 *            Columns in the matrix.
	 * @param timeMultiplier
	 *            Time interval between rows.
	 * @param startTime
	 *            Time of the first row.
	 */
	public DoubleArrayDataGenerator(final int numberOfRows,
			final int numberOfColumns, final double timeMultiplier,
			final double startTime) {
		super();
		this.numberOfRows = numberOfRows;
		this.numberOfColumns = numberOfColumns;
		this.time = new TimeGenerator(timeMultiplier, startTime);
		this.rows = new DataRowGenerator(numberOfColumns);
	}

	/**
	 * Generate the matrix.
	 * @return matrix of doubles.
	 */
	public final double[][] generate() {
		double[][] data = new double[numberOfRows][numberOfColumns];
		for (int r = 0; r < numberOfRows; r++) {
			data[r] = rows.genRecord(time);
			time.increment();
		}
		return data;
	}
}
