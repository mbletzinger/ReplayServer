package org.nees.illinois.replay.client.test;

import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.client.test.InterpolateTestData.ColumnTypes;
import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.DoubleMatrixI;
import org.nees.illinois.replay.restlet.client.MatrixFix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

/**
 * Class to test the interpolate/extrapolate functionality of the replay server.
 * @author Michael Bletzinger
 */
@Test(groups = { "test_data" })
public class TestInterpolate {
	/**
	 * Logger.
	 */
	private final Logger log = LoggerFactory.getLogger(TestInterpolate.class);
	/**
	 * Size of the matrix to test.
	 */
	private final int numberOfChannels = 4;
	/**
	 * Size of the matrix to test.
	 */
	private final int numberOfRecords = 8;
	/**
	 * Time interval.
	 */
	private final int timeInterval = 4;
	/**
	 * Interpolate data sets.
	 */
	private final InterpolateTestData dmg = new InterpolateTestData(numberOfRecords, timeInterval);

	/**
	 * Test a matrix with no values.
	 */
	@Test
	public final void testEmpty() {
		List<ColumnTypes> spec = new ArrayList<InterpolateTestData.ColumnTypes>();
		for (int i = 0; i < numberOfChannels; i++) {
			spec.add((i % 2 == 0 ? ColumnTypes.Empty : ColumnTypes.Full));
		}
		List<List<Double>> actualL = dmg.generate(spec, false);
		DoubleMatrixI actual = new DoubleMatrix(actualL);
		log.debug("Actual: " + actual);
		MatrixFix iplt = new MatrixFix(actual);
		iplt.fix();
		log.debug("Fixed: " + actual);
		List<List<Double>> expectedL = dmg.generate(spec, true);
		DoubleMatrixI expected = new DoubleMatrix(expectedL);
		// log.debug("Expected: " + expected);
		dmg.compareList2Doubles(expected, actual);
	}

	/**
	 * Test a matrix with columns that either start late or end early.
	 */
	@Test
	public final void testExtrapolate() {
		List<ColumnTypes> spec = new ArrayList<InterpolateTestData.ColumnTypes>();
		spec.add(ColumnTypes.Empty);
		spec.add(ColumnTypes.Full);
		spec.add(ColumnTypes.SingleEarly);
		spec.add(ColumnTypes.DoubleEarly);
		spec.add(ColumnTypes.SingleLate);
		spec.add(ColumnTypes.DoubleLate);

		List<List<Double>> actualL = dmg.generate(spec, false);
		DoubleMatrixI actual = new DoubleMatrix(actualL);
		log.debug("Actual: " + actual);
		MatrixFix iplt = new MatrixFix(actual);
		iplt.fix();
		log.debug("Fixed: " + actual);
		List<List<Double>> expectedL = dmg.generate(spec, true);
		DoubleMatrixI expected = new DoubleMatrix(expectedL);
		log.debug("Expected: " + expected);
		dmg.compareList2Doubles(expected, actual);
	}

	/**
	 * Test a matrix that has no missing values.
	 */
	@Test
	public final void testFull() {
		List<ColumnTypes> spec = new ArrayList<InterpolateTestData.ColumnTypes>();
		for (int i = 0; i < numberOfChannels; i++) {
			spec.add(ColumnTypes.Full);
		}
		List<List<Double>> actualL = dmg.generate(spec, false);
		DoubleMatrixI actual = new DoubleMatrix(actualL);
		log.debug("Actual: " + actual);
		MatrixFix iplt = new MatrixFix(actual);
		iplt.fix();
		log.debug("Fixed: " + actual);
		List<List<Double>> expectedL = dmg.generate(spec, true);
		DoubleMatrixI expected = new DoubleMatrix(expectedL);
		// log.debug("Expected: " + expected);
		dmg.compareList2Doubles(expected, actual);
	}

	/**
	 * Test a matrix with columns that have various missing elements.
	 */
	@Test
	public final void testNulls() {
		List<ColumnTypes> spec = new ArrayList<InterpolateTestData.ColumnTypes>();
		spec.add(ColumnTypes.Empty);
		spec.add(ColumnTypes.Full);
		spec.add(ColumnTypes.SingleNull);
		spec.add(ColumnTypes.MultiNull);
		spec.add(ColumnTypes.CoupleNulls);

		List<List<Double>> actualL = dmg.generate(spec, false);
		DoubleMatrixI actual = new DoubleMatrix(actualL);
		log.debug("Actual: " + actual);
		MatrixFix iplt = new MatrixFix(actual);
		iplt.fix();
		log.debug("Fixed: " + actual);
		List<List<Double>> expectedL = dmg.generate(spec, true);
		DoubleMatrixI expected = new DoubleMatrix(expectedL);
		log.debug("Expected: " + expected);
		dmg.compareList2Doubles(expected, actual);
	}

}
