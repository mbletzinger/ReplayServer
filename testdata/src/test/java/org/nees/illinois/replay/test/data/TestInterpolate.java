package org.nees.illinois.replay.test.data;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.queries.Interpolate;
import org.nees.illinois.replay.test.utils.DoubleMatrixGenerator;
import org.nees.illinois.replay.test.utils.DoubleMatrixGenerator.ColumnTypes;

public class TestInterpolate {
	private final Logger log = Logger.getLogger(TestInterpolate.class);
	private DoubleMatrixGenerator dmg = new DoubleMatrixGenerator(10, 0.02);
	
	@BeforeMethod
	public void setUp() throws Exception {
	}

	@AfterMethod
	public void tearDown() throws Exception {
	}

	@Test
	public void testFull() {
		List<ColumnTypes> spec = new ArrayList<DoubleMatrixGenerator.ColumnTypes>();
		for (int i = 0; i < 4; i++) {
			spec.add(ColumnTypes.Full);
		}
		List<List<Double>> actualL = dmg.generate(spec, false);
		DoubleMatrix actual = new DoubleMatrix(actualL, spec.size());
		log.debug("Actual: " + actual);
		Interpolate iplt = new Interpolate(actual);
		iplt.fix();
		log.debug("Fixed: " + actual);		
		List<List<Double>> expectedL = dmg.generate(spec, true);
		DoubleMatrix expected = new DoubleMatrix(expectedL, spec.size());
//		log.debug("Expected: " + expected);
		dmg.compareList2Doubles(expected, actual);
	}
	@Test
	public void testEmpty() {
		List<ColumnTypes> spec = new ArrayList<DoubleMatrixGenerator.ColumnTypes>();
		for (int i = 0; i < 4; i++) {
			spec.add((i % 2 == 0 ? ColumnTypes.Empty : ColumnTypes.Full));
		}
		List<List<Double>> actualL = dmg.generate(spec, false);
		DoubleMatrix actual = new DoubleMatrix(actualL, spec.size());
		log.debug("Actual: " + actual);
		Interpolate iplt = new Interpolate(actual);
		iplt.fix();
		log.debug("Fixed: " + actual);		
		List<List<Double>> expectedL = dmg.generate(spec, true);
		DoubleMatrix expected = new DoubleMatrix(expectedL, spec.size());
//		log.debug("Expected: " + expected);
		dmg.compareList2Doubles(expected, actual);
	}
	@Test
	public void testNulls() {
		List<ColumnTypes> spec = new ArrayList<DoubleMatrixGenerator.ColumnTypes>();
		spec.add(ColumnTypes.Empty);
		spec.add(ColumnTypes.Full);
		spec.add(ColumnTypes.SingleNull);
		spec.add(ColumnTypes.MultiNull);
		spec.add(ColumnTypes.CoupleNulls);

		List<List<Double>> actualL = dmg.generate(spec, false);
		DoubleMatrix actual = new DoubleMatrix(actualL, spec.size());
		log.debug("Actual: " + actual);
		Interpolate iplt = new Interpolate(actual);
		iplt.fix();
		log.debug("Fixed: " + actual);				
		List<List<Double>> expectedL = dmg.generate(spec, true);
		DoubleMatrix expected = new DoubleMatrix(expectedL, spec.size());
//		log.debug("Expected: " + expected);
		dmg.compareList2Doubles(expected, actual);
	}
	@Test
	public void testExtrapolate() {
		List<ColumnTypes> spec = new ArrayList<DoubleMatrixGenerator.ColumnTypes>();
		spec.add(ColumnTypes.Empty);
		spec.add(ColumnTypes.Full);
		spec.add(ColumnTypes.SingleEarly);
		spec.add(ColumnTypes.DoubleEarly);
		spec.add(ColumnTypes.SingleLate);
		spec.add(ColumnTypes.DoubleLate);

		List<List<Double>> actualL = dmg.generate(spec, false);
		DoubleMatrix actual = new DoubleMatrix(actualL, spec.size());
		log.debug("Actual: " + actual);
		Interpolate iplt = new Interpolate(actual);
		iplt.fix();
		log.debug("Fixed: " + actual);				
		List<List<Double>> expectedL = dmg.generate(spec, true);
		DoubleMatrix expected = new DoubleMatrix(expectedL, spec.size());
		log.debug("Expected: " + expected);
		dmg.compareList2Doubles(expected, actual);
	}

}
