package org.nees.illinois.replay.test.data;

import java.util.List;

import org.nees.illinois.replay.common.registries.ChannelNameRegistry;
import org.nees.illinois.replay.common.registries.TableType;
import org.nees.illinois.replay.conversions.ChannelList2Representation;
import org.nees.illinois.replay.conversions.DoubleMatrix2Representation;
import org.nees.illinois.replay.conversions.Representation2ChannelList;
import org.nees.illinois.replay.conversions.Representation2DoubleMatrix;
import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.test.utils.ChannelListTestMaps;
import org.nees.illinois.replay.test.utils.ChannelListType;
import org.nees.illinois.replay.test.utils.DatasetDirector.ExperimentNames;
import org.nees.illinois.replay.test.utils.DoubleArrayDataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test(groups = { "test_data" })
public class TestStreamConversion {
	private final Logger log = LoggerFactory
			.getLogger(TestStreamConversion.class);
	@BeforeMethod
	public void setUp() throws Exception {
	}

	@Test
	public void testChannelListConversion() {
		ChannelListTestMaps lists = new ChannelListTestMaps(false, ExperimentNames.HybridMasonry1.toString());
		ChannelNameRegistry cnr = new ChannelNameRegistry();
		ChannelNameRegistry expectedCnr = new ChannelNameRegistry();
		for (String c : lists.getChannels(ChannelListType.OM)) {
			cnr.addChannel(TableType.Control, c);
			expectedCnr.addChannel(TableType.Control, c);
		}
		ChannelList2Representation cl2rep = new ChannelList2Representation(
				cnr.getNames());

		Representation2ChannelList rep2cl = new Representation2ChannelList(cl2rep.getRep());
		ChannelNameRegistry actualCnr = new ChannelNameRegistry();
		for (String c : rep2cl.getIl2cl().getChannels()) {
			actualCnr.addChannel(TableType.Control, c);
		}
		log.debug("expected CNR " + expectedCnr);
		log.debug("New CNR " + actualCnr);
		for (String c : expectedCnr.getNames()) {
			log.debug("Checking \"" + c + "\"");
			AssertJUnit.assertTrue(actualCnr.getNames().contains(c));
		}
	}
	
	@Test
	public void testDoubleMatrixConversion() {
		DoubleArrayDataGenerator dg = new DoubleArrayDataGenerator(20, 6, 0.5, 222.0);
		double[][] data = dg.generate();
		DoubleMatrix2Representation rep2os = new DoubleMatrix2Representation(data);
		DoubleMatrix orig = new DoubleMatrix(DoubleArrayDataGenerator.toList(data));
		Representation2DoubleMatrix rep2dbl = new Representation2DoubleMatrix(rep2os.getRep());
		List<List<Double>> newL = rep2dbl.getIn2dm().getNumbers();
		DoubleMatrix newD = new DoubleMatrix(newL);
		log.debug("Original Data " + orig);
		log.debug("New  Data " + newD);
		DoubleArrayDataGenerator.compareData(newD.getData(), orig.getData());
	}
	
}
