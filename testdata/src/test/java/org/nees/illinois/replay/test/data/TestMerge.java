package org.nees.illinois.replay.test.data;

import java.util.List;

import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.DoubleMatrixI;
import org.nees.illinois.replay.data.MergeSet;
import org.nees.illinois.replay.test.utils.TestDatasetParameters;
import org.nees.illinois.replay.test.utils.QuerySetsDirector;
import org.nees.illinois.replay.test.utils.data.ChannelDataGenerator;
import org.nees.illinois.replay.test.utils.data.DoubleArrayDataGenerator;
import org.nees.illinois.replay.test.utils.types.ExperimentNames;
import org.nees.illinois.replay.test.utils.types.MatrixMixType;
import org.nees.illinois.replay.test.utils.types.QueryParaTypes;
import org.nees.illinois.replay.test.utils.types.TestDatasetType;
import org.nees.illinois.replay.test.utils.types.TestingParts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

/**
 * Test the matrix merging functionality.
 * @author Michael Bletzinger
 */
@Test(groups = { "test_data" })
public class TestMerge {
	/**
	 * First continuous data set.
	 */
	private DoubleMatrix omContData;
	/**
	 * Second continuous data set.
	 */
	private DoubleMatrix daqContData;
	/**
	 * Logger.
	 */
	private final Logger log = LoggerFactory.getLogger(TestMerge.class);

	/**
	 * Test merges with plain data matrices.
	 */

	@Test
	public final void testRawMerge() {
		final double startTime = 222.0;
		final int firstRow = 20;
		final int secondRow = 15;
		final int firstColumn = 4;
		final int secondColumn = 3;
		final double firstTime = 0.7;
		final double secondTime = 1.0;
		final int timeColumns = 1;
		DoubleArrayDataGenerator dg = new DoubleArrayDataGenerator(firstRow,
				firstColumn, firstTime, startTime);
		double[][] dat = dg.generate();
		omContData = new DoubleMatrix(dat);
		dg = new DoubleArrayDataGenerator(secondRow, secondColumn, secondTime,
				startTime);
		dat = dg.generate();
		daqContData = new DoubleMatrix(dat);

		int[] daqcsz = daqContData.sizes();
		int[] omcsz = omContData.sizes();
		log.debug("DAQ Cont[" + daqcsz[0] + "][" + daqcsz[1] + "]");
		log.debug("OM Cont[" + omcsz[0] + "][" + omcsz[1] + "]");

		MergeSet mset = new MergeSet();
		mset.merge(daqContData);
		log.debug("DAQ only " + mset);
		final int expected3rdColumnSize = 7;
		AssertJUnit.assertEquals(secondColumn,
				mset.getColumnSize(false));
		AssertJUnit.assertEquals(secondColumn + timeColumns,
				mset.getColumnSize(true));
		DoubleMatrix result = mset.getResult();
		for (List<Double> r : result.toList()) {
			log.info("Checking " + r);
			AssertJUnit.assertEquals(daqcsz[1], r.size());
		}
		mset.merge(omContData);
		result = mset.getResult();
		log.debug("with OM " + mset);
		AssertJUnit.assertEquals(expected3rdColumnSize,
				mset.getColumnSize(false));

		for (List<Double> r : result.toList()) {
			log.info("Checking " + r);
			AssertJUnit.assertEquals(daqcsz[1] + omcsz[1] - 1, r.size());
		}
	}

	/**
	 * Test merges based on test queries.
	 */
	@Test(dependsOnMethods = { "testRawMerge" })
	public final void testQueryMerges() {
		QuerySetsDirector dd = new QuerySetsDirector(ExperimentNames.HybridMasonry1);
		TestDatasetParameters cltm = dd.getSet();
		for (TestDatasetType clt : cltm.getQueryTypes()) {
			if (clt.equals(TestDatasetType.QueryDaq)
					|| clt.equals(TestDatasetType.QueryOm)) {
				continue;
			}
			for (QueryParaTypes qpt : QueryParaTypes.values()) {
				for (MatrixMixType m : MatrixMixType.values()) {
					log.info("Testing " + clt + " " + qpt + " " + m);
					ChannelDataGenerator cdg = dd
							.generateQueryData(clt, qpt, m);
					cdg.generateParts();
					cdg.generateWhole();
					// cdg.mixColumns(); // Columns are not mixed until after
					// the merge
					MergeSet mset = new MergeSet();
					mset.merge(cdg.getData(TestingParts.First));
					mset.merge(cdg.getData(TestingParts.Second));
					DoubleMatrixI actual = mset.getResult();
					DoubleMatrixI expected = cdg.getData(TestingParts.All);
					DoubleArrayDataGenerator.compareData(actual.getData(),
							expected.getData());
				}
			}
		}

	}
	// private void testQueryMerge(ChannelListType quy, QueryParaTypes qt,
	// DatasetDirector dd) {
	// ChannelDataTestingLists cl = dd.
	// List<String> omChnls =
	// }

}
