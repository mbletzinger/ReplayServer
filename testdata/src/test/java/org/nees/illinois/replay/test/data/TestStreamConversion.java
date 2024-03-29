package org.nees.illinois.replay.test.data;

import java.util.List;

import org.nees.illinois.replay.common.registries.ChannelNameRegistry;
import org.nees.illinois.replay.common.registries.TableType;
import org.nees.illinois.replay.conversions.StringList2Representation;
import org.nees.illinois.replay.conversions.DoubleMatrix2Representation;
import org.nees.illinois.replay.conversions.Representation2StringList;
import org.nees.illinois.replay.conversions.Representation2DoubleMatrix;
import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.DoubleMatrixI;
import org.nees.illinois.replay.test.utils.TestDatasetParameters;
import org.nees.illinois.replay.test.utils.gen.DoubleMatrixGenerator;
import org.nees.illinois.replay.test.utils.types.ExperimentNames;
import org.nees.illinois.replay.test.utils.types.TestDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

/**
 * Class to test the representation conversion functions.
 * @author Michael Bletzinger
 */
@Test(groups = { "convert" })
public class TestStreamConversion {
	/**
	 * Logger.
	 */
	private final Logger log = LoggerFactory
			.getLogger(TestStreamConversion.class);

	/**
	 * Test conversions between streams and text lists.
	 */
	@Test
	public final void testChannelListConversion() {
		TestDatasetParameters lists = new TestDatasetParameters(false,
				ExperimentNames.HybridMasonry1.toString());
		ChannelNameRegistry cnr = new ChannelNameRegistry();
		ChannelNameRegistry expectedCnr = new ChannelNameRegistry();
		TableType tname = lists.getTt(TestDataSource.OM);
		for (String c : lists.getChannels(TestDataSource.OM)) {
			cnr.addChannel(tname, c);
			expectedCnr.addChannel(tname, c);
		}
		StringList2Representation cl2rep = new StringList2Representation(
				cnr.getNames());

		Representation2StringList rep2cl = new Representation2StringList(
				cl2rep.getRep());
		ChannelNameRegistry actualCnr = new ChannelNameRegistry();
		for (String c : rep2cl.getIl2cl().getStrings()) {
			actualCnr.addChannel(tname, c);
		}
		log.debug("expected CNR " + expectedCnr);
		log.debug("New CNR " + actualCnr);
		for (String c : expectedCnr.getNames()) {
			log.debug("Checking \"" + c + "\"");
			AssertJUnit.assertTrue(actualCnr.getNames().contains(c));
		}
	}

	/**
	 * Test conversions between streams and double matrices.
	 */
	@Test
	public final void testDoubleMatrixConversion() {
		final DoubleMatrixGenerator dg = new DoubleMatrixGenerator(20, 6,
				0.5, 222.0);
		double[][] data = dg.generate();
		DoubleMatrix2Representation rep2os = new DoubleMatrix2Representation(
				data);
		DoubleMatrixI orig = new DoubleMatrix(
				DoubleMatrixGenerator.toList(data));
		Representation2DoubleMatrix rep2dbl = new Representation2DoubleMatrix(
				rep2os.getRep());
		List<List<Double>> newL = rep2dbl.getIn2dm().getNumbers();
		DoubleMatrixI newD = new DoubleMatrix(newL);
		log.debug("Original Data " + orig);
		log.debug("New  Data " + newD);
		DoubleMatrixGenerator.compareData(newD.getData(), orig.getData());
	}

}
