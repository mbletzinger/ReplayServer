package org.nees.illinois.replay.test.data;

import java.util.List;

import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.DoubleMatrixI;
import org.nees.illinois.replay.data.MergeSet;
import org.nees.illinois.replay.test.utils.ChannelDataGenerator;
import org.nees.illinois.replay.test.utils.ChannelDataGenerator.TestingParts;
import org.nees.illinois.replay.test.utils.ChannelListTestMaps;
import org.nees.illinois.replay.test.utils.ChannelListType;
import org.nees.illinois.replay.test.utils.DatasetDirector;
import org.nees.illinois.replay.test.utils.DatasetDirector.ExperimentNames;
import org.nees.illinois.replay.test.utils.DatasetDirector.QueryParaTypes;
import org.nees.illinois.replay.test.utils.DoubleArrayDataGenerator;
import org.nees.illinois.replay.test.utils.MatrixMixType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test(groups = { "test_data" })
public class TestMerge {
	private List<List<Double>> omContData;
	private List<List<Double>> daqContData;
	private List<List<Double>> omStepData;
	private List<List<Double>> daqStepData;
	private final Logger log = LoggerFactory.getLogger(TestMerge.class);

	@BeforeMethod
	public void setUp() throws Exception {
	}

	@Test
	public void testRawMerge() {

		DoubleArrayDataGenerator dg = new DoubleArrayDataGenerator(20, 4, 0.7, 222.0);
		double[][] dat = dg.generate();
		omContData = DoubleArrayDataGenerator.toList(dat);
		dg = new DoubleArrayDataGenerator(15, 3, 1.0, 222.0);
		dat = dg.generate();
		daqContData = DoubleArrayDataGenerator.toList(dat);
		dg = new DoubleArrayDataGenerator(20, 4, 0.2, 222.0);
		dat = dg.generate();
		omStepData = DoubleArrayDataGenerator.toList(dat);
		dg = new DoubleArrayDataGenerator(15, 3, 0.3, 222.0);
		dat = dg.generate();
		daqStepData = DoubleArrayDataGenerator.toList(dat);

		int[] daqcsz = { daqContData.size(), daqContData.get(0).size() };
		int[] omcsz = { omContData.size(), omContData.get(0).size() };
		int[] daqssz = { daqStepData.size(), daqStepData.get(0).size() };
		int[] omssz = { omStepData.size(), omStepData.get(0).size() };
		MergeSet mset = new MergeSet();
		mset.merge(daqContData);
		log.debug("DAQ only " + mset);
		AssertJUnit.assertEquals(3, mset.getColumnSize(false));
		AssertJUnit.assertEquals(7, mset.getColumnSize(true));
		List<List<Double>> result = mset.getResult();
		for (List<Double> r : result) {
			log.info("Checking " + r);
			AssertJUnit.assertEquals(daqcsz[1], r.size());
		}
		mset.merge(omContData);
		result = mset.getResult();
		log.debug("with OM " + mset);
		AssertJUnit.assertEquals(7, mset.getColumnSize(false));
		AssertJUnit.assertEquals(11, mset.getColumnSize(true));
		log.debug("DAQ Cont[" + daqcsz[0] + "][" + daqcsz[1] + "]");
		log.debug("OM Cont[" + omcsz[0] + "][" + omcsz[1] + "]");

		for (List<Double> r : result) {
			log.info("Checking " + r);
			AssertJUnit.assertEquals(daqcsz[1] + omcsz[1] - 4, r.size());
		}
		mset = new MergeSet();
		mset.merge(daqStepData);
		result = mset.getResult();
		log.debug("DAQ only " + mset);
		AssertJUnit.assertEquals(3, mset.getColumnSize(false));
		AssertJUnit.assertEquals(7, mset.getColumnSize(true));
		for (List<Double> r : result) {
			log.info("Checking " + r);
			AssertJUnit.assertEquals(daqssz[1], r.size());
		}
		mset.merge(omStepData);
		result = mset.getResult();
		log.debug("with OM " + mset);
		AssertJUnit.assertEquals(7, mset.getColumnSize(false));
		AssertJUnit.assertEquals(11, mset.getColumnSize(true));
		for (List<Double> r : result) {
			log.info("Checking " + r);
			AssertJUnit.assertEquals(daqssz[1] + omssz[1] - 4, r.size());
		}

	}
	
	@Test(dependsOnMethods = { "testRawMerge" })
	public void testQueryMerges() {
		DatasetDirector dd = new DatasetDirector(ExperimentNames.HybridMasonry1);
		ChannelListTestMaps cltm = dd.getCltm();
		for (ChannelListType clt : cltm.getQueryTypes()) {
			if(clt.equals(ChannelListType.QueryDaq) || clt.equals(ChannelListType.QueryOm)) {
				continue;
			}
			for( QueryParaTypes qpt : QueryParaTypes.values()) {
				for(MatrixMixType m : MatrixMixType.values() ) {
					log.info("Testing " + clt + " " + qpt + " " + m);
					ChannelDataGenerator cdg = dd.generateQueryData(clt, qpt, m);
					cdg.generateParts();
					cdg.generateWhole();
//					cdg.mixColumns(); // Columns are not mixed until after the merge
					MergeSet mset = new MergeSet();
					mset.merge(cdg.getData(TestingParts.First).toList());
					mset.merge(cdg.getData(TestingParts.Second).toList());
					DoubleMatrixI actual = new DoubleMatrix(mset.getResult());
					DoubleMatrixI expected = cdg.getData(TestingParts.All);
					DoubleArrayDataGenerator.compareData(actual.getData(), expected.getData());
				}
			}
		}
		
	}
//	private void testQueryMerge(ChannelListType quy, QueryParaTypes qt, DatasetDirector dd) {
//		ChannelDataTestingLists cl = dd.
//		List<String> omChnls = 
//	}

}
