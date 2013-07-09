package org.nees.illinois.replay.test.data;

import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.DoubleMatrixI;
import org.nees.illinois.replay.data.MatrixFix;
import org.nees.illinois.replay.test.utils.InterpolateTestData;
import org.nees.illinois.replay.test.utils.InterpolateTestData.ColumnTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test(groups = { "test_data" })
public class TestInterpolate {
	private final Logger log = LoggerFactory.getLogger(TestInterpolate.class);
	private InterpolateTestData dmg = new InterpolateTestData(10, 0.02);

	@BeforeMethod
	public void setUp() throws Exception {
	}

	@AfterMethod
	public void tearDown() throws Exception {
	}

	@Test
	public void testFull() {
		List<ColumnTypes> spec = new ArrayList<InterpolateTestData.ColumnTypes>();
		for (int i = 0; i < 4; i++) {
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

	@Test
	public void testEmpty() {
		List<ColumnTypes> spec = new ArrayList<InterpolateTestData.ColumnTypes>();
		for (int i = 0; i < 4; i++) {
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

	@Test
	public void testNulls() {
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
		// log.debug("Expected: " + expected);
		dmg.compareList2Doubles(expected, actual);
	}

	@Test
	public void testExtrapolate() {
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

}
