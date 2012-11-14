package org.nees.mustsim.replay.test.data;

import java.io.ByteArrayInputStream;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.nees.mustsim.replay.channels.ChannelNameRegistry;
import org.nees.mustsim.replay.conversions.ChannelList2Representation;
import org.nees.mustsim.replay.conversions.DoubleMatrix2Representation;
import org.nees.mustsim.replay.conversions.Representation2ChannelList;
import org.nees.mustsim.replay.conversions.Representation2DoubleMatrix;
import org.nees.mustsim.replay.data.DoubleMatrix;
import org.nees.mustsim.replay.data.RateType;
import org.nees.mustsim.replay.data.TableType;
import org.nees.mustsim.replay.test.utils.ChannelLists;
import org.nees.mustsim.replay.test.utils.DataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestStreamConversion {
	private final Logger log = LoggerFactory
			.getLogger(TestStreamConversion.class);
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testChannelListConversion() {
		ChannelLists lists = new ChannelLists();
		ChannelNameRegistry cnr = new ChannelNameRegistry();
		for (String c : lists.getChannels(TableType.OM)) {
			cnr.addChannel(TableType.OM, c);
		}
		ChannelList2Representation cl2rep = new ChannelList2Representation(
				cnr.getNames());

		Representation2ChannelList rep2cl = new Representation2ChannelList(cl2rep.getRep());
		ChannelNameRegistry cnr1 = new ChannelNameRegistry();
		for (String c : rep2cl.getChannels()) {
			cnr1.addChannel(TableType.OM, c);
		}
		log.debug("Original CNR " + cnr);
		log.debug("New CNR " + cnr1);
		for (String c : cnr.getNames()) {
			log.debug("Checking \"" + c + "\"");
			Assert.assertTrue(cnr1.getNames().contains(c));
		}
	}
	
	@Test
	public void testDoubleMatrixConversion() {
		double[][] data = DataGenerator.initData(RateType.CONT, 20, 6, 0.5);
		DoubleMatrix2Representation rep2os = new DoubleMatrix2Representation(data);
		DoubleMatrix orig = new DoubleMatrix(DataGenerator.toList(data), data[0].length);
		Representation2DoubleMatrix rep2dbl = new Representation2DoubleMatrix(rep2os.getRep());
		List<List<Double>> newL = rep2dbl.getNumbers();
		DoubleMatrix newD = new DoubleMatrix(newL,newL.get(0).size());
		log.debug("Original Data " + orig);
		log.debug("New  Data " + newD);
		DataGenerator.compareData(orig.getData(), newD.getData());
	}
	
}
