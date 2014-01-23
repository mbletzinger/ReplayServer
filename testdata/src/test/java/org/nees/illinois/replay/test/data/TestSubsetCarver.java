package org.nees.illinois.replay.test.data;

import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.DoubleMatrixI;
import org.nees.illinois.replay.test.utils.gen.DoubleArrayDataGenerator;
import org.nees.illinois.replay.test.utils.gen.tricks.SubsetCarver;
import org.nees.illinois.replay.test.utils.gen.tricks.SubsetSlicer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

/**
 * Test the matrix subset carver.
 * @author Michael Bletzinger
 */
public class TestSubsetCarver {
	/**
	 * Subset defined by a start and stop boundary for both rows and columns.
	 */
	private final double[][] bothCarveExpected;
	/**
	 * Defined boundaries array. Each row has the start, end, and size
	 * boundaries. The first row is for rows. The second row is for columns.
	 */
	private final int[][] boundaries = { { 3, 6, 8 }, { 2, 7, 9 } };
	/**
	 * Interval for column portion of the matrix element value.
	 */
	private final double colInterval = 0.1;
	/**
	 * Expected subset after carving both ends of the columns.
	 */
	private final double[][] columnCarveExpected;
	/**
	 * Expected subset after carving the end of the columns.
	 */
	private final double[][] columnCarveExpectedEarlyEnd;
	/**
	 * Expected subset after carving the beginning of the columns.
	 */
	private final double[][] columnCarveExpectedLateStart;
	/**
	 * Original matrix.
	 */
	private final double[][] original;
	/**
	 * Expected subset after carving both ends of the columns.
	 */
	private final double[][] rowCarveExpected;
	/**
	 * Expected subset after carving the end of the columns.
	 */
	private final double[][] rowCarveExpectedEarlyEnd;
	/**
	 * Expected subset after carving the beginning of the columns.
	 */
	private final double[][] rowCarveExpectedLateStart;
	/**
	 * Interval for the row portion of the matrix element value.
	 */
	private final double rowInterval = 1.1;
	/**
	 * Test slices.
	 */
	private final int[] slices = { 1, 3, 6 };
	/**
	 * Expected sliced rows.
	 */
	private final DoubleMatrixI rowSliceExpected;
	/**
	 * Expected sliced columns.
	 */
	private final DoubleMatrixI columnSliceExpected;
	/**
	 * Logger.
	 **/
	private final Logger log = LoggerFactory.getLogger(TestSubsetCarver.class);

	/**
	 *
	 */
	public TestSubsetCarver() {
		original = new double[boundaries[0][2]][boundaries[1][2]];
		rowCarveExpected = new double[boundaries[0][1] - boundaries[0][0]][boundaries[1][2]];
		rowCarveExpectedLateStart = new double[boundaries[0][2]
				- boundaries[0][0]][boundaries[1][2]];
		rowCarveExpectedEarlyEnd = new double[boundaries[0][1]][boundaries[1][2]];
		columnCarveExpected = new double[boundaries[0][2]][boundaries[1][1]
				- boundaries[1][0]];
		columnCarveExpectedLateStart = new double[boundaries[0][2]][boundaries[1][2]
				- boundaries[1][0]];
		columnCarveExpectedEarlyEnd = new double[boundaries[0][2]][boundaries[1][1]];
		bothCarveExpected = new double[boundaries[0][1] - boundaries[0][0]][boundaries[1][1]
				- boundaries[1][0]];
		List<Integer> sliceList = new ArrayList<Integer>();
		for (int s : slices) {
			sliceList.add(new Integer(s));
		}
		List<List<Double>> cslices = new ArrayList<List<Double>>();
		List<List<Double>> rslices = new ArrayList<List<Double>>();
		for (int r = 0; r < boundaries[0][2]; r++) {
			boolean rowSlice = sliceList.contains(new Integer(r));
			List<Double> slicedColumns = new ArrayList<Double>();
			for (int c = 0; c < boundaries[1][2]; c++) {
				double value = r * rowInterval + c * colInterval;
				original[r][c] = value;
				boolean rowStarted = r >= boundaries[0][0];
				boolean rowEnded = r < boundaries[0][1];
				boolean columnStarted = c >= boundaries[1][0];
				boolean columnEnded = c < boundaries[1][1];
				boolean columnSlice = sliceList.contains(new Integer(c));
//				log.debug("r " + r + " c " + c + " start r "
//						+ (r - boundaries[0][0]) + " c start "
//						+ (c - boundaries[1][0]));
//				log.debug((rowStarted ? "row started " : "")
//						+ (rowEnded ? "row ended " : "")
//						+ (columnStarted ? "column started " : "")
//						+ (columnEnded ? "column ended " : ""));
				if (rowStarted) {
					rowCarveExpectedLateStart[r - boundaries[0][0]][c] = value;
				}
				if (rowEnded) {
					rowCarveExpectedEarlyEnd[r][c] = value;
				}
				if (rowStarted && rowEnded) {
					rowCarveExpected[r - boundaries[0][0]][c] = value;
				}
				if (columnStarted) {
					columnCarveExpectedLateStart[r][c - boundaries[1][0]] = value;
				}
				if (columnEnded) {
					columnCarveExpectedEarlyEnd[r][c] = value;
				}
				if (columnStarted && columnEnded) {
					columnCarveExpected[r][c - boundaries[1][0]] = value;
				}
				if (rowStarted && rowEnded & columnStarted && columnEnded) {
					bothCarveExpected[r - boundaries[0][0]][c
							- boundaries[1][0]] = value;
				}
				if (columnSlice) {
					slicedColumns.add(new Double(value));
				}
			}
			cslices.add(slicedColumns);
			if (rowSlice) {
				List<Double> nrow = new ArrayList<Double>();
				for (double n : original[r]) {
					nrow.add(new Double(n));
				}
				rslices.add(nrow);
			}
		}
		rowSliceExpected = new DoubleMatrix(rslices);
		columnSliceExpected = new DoubleMatrix(cslices);
		log.debug("Original:\n" + dumpMatrix(original));
		log.debug("Row late start:\n" + dumpMatrix(rowCarveExpectedLateStart));
		log.debug("Row early end:\n" + dumpMatrix(rowCarveExpectedEarlyEnd));
		log.debug("Row:\n" + dumpMatrix(rowCarveExpected));
		log.debug("Column late start:\n"
				+ dumpMatrix(columnCarveExpectedLateStart));
		log.debug("Column early end:\n"
				+ dumpMatrix(columnCarveExpectedEarlyEnd));
		log.debug("Column:\n" + dumpMatrix(columnCarveExpected));
		log.debug("Both:\n" + dumpMatrix(bothCarveExpected));
		log.debug("Row slices:\n" + rowSliceExpected);
		log.debug("Column slices:\n" + columnSliceExpected);
	}

	/**
	 * Test row carving.
	 */
	@Test
	public final void testRowCarve() {
		SubsetCarver carve = new SubsetCarver(new DoubleMatrix(original));
		carve.setStartRow(boundaries[0][0]);
		DoubleMatrixI actual = carve.subset();
		DoubleArrayDataGenerator.compareData(actual.getData(),
				rowCarveExpectedLateStart);
		carve.setStartRow(null);
		carve.setStopRow(boundaries[0][1]);
		actual = carve.subset();
		DoubleArrayDataGenerator.compareData(actual.getData(),
				rowCarveExpectedEarlyEnd);
		carve.setStartRow(boundaries[0][0]);
		actual = carve.subset();
		DoubleArrayDataGenerator
				.compareData(actual.getData(), rowCarveExpected);
	}

	/**
	 * Test column carving.
	 */
	@Test
	public final void testColumnCarve() {
		SubsetCarver carve = new SubsetCarver(new DoubleMatrix(original));
		carve.setStartColumn(boundaries[1][0]);
		DoubleMatrixI actual = carve.subset();
		DoubleArrayDataGenerator.compareData(actual.getData(),
				columnCarveExpectedLateStart);
		carve.setStartColumn(null);
		carve.setStopColumn(boundaries[1][1]);
		actual = carve.subset();
		DoubleArrayDataGenerator.compareData(actual.getData(),
				columnCarveExpectedEarlyEnd);
		carve.setStartColumn(boundaries[1][0]);
		actual = carve.subset();
		DoubleArrayDataGenerator.compareData(actual.getData(),
				columnCarveExpected);
	}

	/**
	 * Test carving both rows and columns.
	 */
	@Test
	public final void testCombinedCarve() {
		SubsetCarver carve = new SubsetCarver(new DoubleMatrix(original));
		carve.setStartRow(boundaries[0][0]);
		carve.setStopRow(boundaries[0][1]);
		carve.setStartColumn(boundaries[1][0]);
		carve.setStopColumn(boundaries[1][1]);
		DoubleMatrixI actual = carve.subset();
		DoubleArrayDataGenerator.compareData(actual.getData(),
				bothCarveExpected);
	}

	/**
	 * Imports the double matrix into a {@link DoubleMatrix} so that it can be
	 * pretty-printed.
	 * @param matrix
	 *            the double matrix to convert.
	 * @return the pretty-print string.
	 */
	private String dumpMatrix(final double[][] matrix) {
		DoubleMatrix dm = new DoubleMatrix(matrix);
		return dm.toString();
	}

	/**
	 * Test Matrix slices.
	 */
	@Test
	public final void testSlicing() {
		SubsetSlicer slicer = new SubsetSlicer(new DoubleMatrix(original));
		slicer.addSlices(slices);
		DoubleMatrixI actual = slicer.slice();
		DoubleArrayDataGenerator.compareData(actual.getData(),
				rowSliceExpected.getData());
		slicer.setSliceColumn(true);
		actual = slicer.slice();
		DoubleArrayDataGenerator.compareData(actual.getData(),
				columnSliceExpected.getData());
	}

}
