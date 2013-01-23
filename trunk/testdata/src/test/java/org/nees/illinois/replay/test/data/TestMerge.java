package org.nees.illinois.replay.test.data;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.AssertJUnit;
import java.util.List;

import org.apache.log4j.Logger;
import org.nees.illinois.replay.data.MergeSet;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.test.utils.DataGenerator;

public class TestMerge {
	private List<List<Double>> omContData;
	private List<List<Double>> daqContData;
	private List<List<Double>> omStepData;
	private List<List<Double>> daqStepData;
	private final Logger log = Logger.getLogger(TestMerge.class);

	@BeforeMethod
	public void setUp() throws Exception {
		double[][] dat = DataGenerator.initData(20, 4, 0.7);
		omContData = DataGenerator.toList(dat);
		dat = DataGenerator.initData(15, 3, 1.0);
		daqContData = DataGenerator.toList(dat);
		dat = DataGenerator.initData(20, 4, 0.2);
		omStepData = DataGenerator.toList(dat);
		dat = DataGenerator.initData(15, 3, 0.3);
		daqStepData = DataGenerator.toList(dat);
	}

	@Test
	public void testMerge() {
		int[] daqcsz = { daqContData.size(), daqContData.get(0).size() };
		int[] omcsz = { omContData.size(), omContData.get(0).size() };
		int[] daqssz = { daqStepData.size(), daqStepData.get(0).size() };
		int[] omssz = { omStepData.size(), omStepData.get(0).size() };
		MergeSet mset = new MergeSet(RateType.CONT);
		mset.merge(daqContData);
		log.debug("DAQ only " + mset);
		AssertJUnit.assertEquals(3, mset.getColumnSize(false));
		AssertJUnit.assertEquals(7, mset.getColumnSize(true));
		List<List<Double>> result = mset.getRecords();
		for (List<Double> r : result) {
			log.info("Checking " + r );
			AssertJUnit.assertEquals(daqcsz[1], r.size());
		}
		mset.merge(omContData);
		result = mset.getRecords();
		log.debug("with OM " + mset);
		AssertJUnit.assertEquals(7, mset.getColumnSize(false));
		AssertJUnit.assertEquals(11, mset.getColumnSize(true));
		log.debug("DAQ Cont[" + daqcsz[0] + "][" + daqcsz[1] + "]");
		log.debug("OM Cont[" + omcsz[0] + "][" + omcsz[1] + "]");

		for (List<Double> r : result) {
			log.info("Checking " + r );
			AssertJUnit.assertEquals(daqcsz[1] + omcsz[1] - 4, r.size());
		}
		mset = new MergeSet(RateType.STEP);
		mset.merge(daqStepData);
		result = mset.getRecords();
		log.debug("DAQ only " + mset);
		AssertJUnit.assertEquals(3, mset.getColumnSize(false));
		AssertJUnit.assertEquals(7, mset.getColumnSize(true));
		for (List<Double> r : result) {
			log.info("Checking " + r );
			AssertJUnit.assertEquals(daqssz[1], r.size());
		}
		mset.merge(omStepData);
		result = mset.getRecords();
		log.debug("with OM " + mset);
		AssertJUnit.assertEquals(7, mset.getColumnSize(false));
		AssertJUnit.assertEquals(11, mset.getColumnSize(true));
		for (List<Double> r : result) {
			log.info("Checking " + r );
			AssertJUnit.assertEquals(daqssz[1] + omssz[1] - 4, r.size());
		}

	}

}
