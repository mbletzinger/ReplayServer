package org.nees.mustsim.replay.test;

import java.util.List;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.nees.mustsim.replay.db.data.MergeSet;
import org.nees.mustsim.replay.db.statement.RateType;
import org.nees.mustsim.replay.test.utils.DataGenerator;

public class TestMerge {
	private List<List<Double>> omContData;
	private List<List<Double>> daqContData;
	private List<List<Double>> omStepData;
	private List<List<Double>> daqStepData;
	private final Logger log = Logger.getLogger(TestMerge.class);

	@Before
	public void setUp() throws Exception {
		double[][] dat = DataGenerator.initData(RateType.CONT, 20, 4, 0.7);
		omContData = DataGenerator.toList(dat);
		dat = DataGenerator.initData(RateType.CONT, 15, 3, 1.0);
		daqContData = DataGenerator.toList(dat);
		dat = DataGenerator.initData(RateType.STEP, 20, 4, 0.2);
		omStepData = DataGenerator.toList(dat);
		dat = DataGenerator.initData(RateType.STEP, 15, 3, 0.3);
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
		mset.merge(omContData);
		List<List<Double>> result = mset.getRecords();
		log.debug("with OM " + mset);
		log.debug("DAQ Cont[" + daqcsz[0] + "][" + daqcsz[1] + "]");
		log.debug("OM Cont[" + omcsz[0] + "][" + omcsz[1] + "]");

		for (List<Double> r : result) {
			log.info("Checking " + r );
			Assert.assertEquals(daqcsz[1] + omcsz[1] - 1, r.size());
		}
		MergeSet mset2 = new MergeSet(RateType.STEP);
		mset2.merge(daqStepData);
		result = mset2.getRecords();
		log.debug("DAQ only " + mset2);
		mset2.merge(omStepData);
		result = mset2.getRecords();
		log.debug("with OM " + mset2);
		for (List<Double> r : result) {
			log.info("Checking " + r );
			Assert.assertEquals(daqssz[1] + omssz[1] - 4, r.size());
		}

	}

}
