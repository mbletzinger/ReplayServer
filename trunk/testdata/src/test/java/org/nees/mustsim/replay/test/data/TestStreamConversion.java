package org.nees.mustsim.replay.test.data;

import java.io.ByteArrayInputStream;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.nees.mustsim.replay.conversions.ChannelList2OutputStream;
import org.nees.mustsim.replay.conversions.Double2OutputStream;
import org.nees.mustsim.replay.conversions.InputStream2ChannelList;
import org.nees.mustsim.replay.conversions.InputStream2Double;
import org.nees.mustsim.replay.data.ChannelNameRegistry;
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
		ChannelList2OutputStream cl2os = new ChannelList2OutputStream(
				cnr.getNames());

		InputStream2ChannelList is2cl = new InputStream2ChannelList(
				new ByteArrayInputStream(cl2os.getBuffer()));
		ChannelNameRegistry cnr1 = new ChannelNameRegistry();
		for (String c : is2cl.getChannels()) {
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
		Double2OutputStream dbl2os = new Double2OutputStream(data);
		DoubleMatrix orig = new DoubleMatrix(DataGenerator.toList(data), data[0].length);
		InputStream2Double is2dbl = new InputStream2Double(new ByteArrayInputStream(dbl2os.getBuffer()));
		List<List<Double>> newL = is2dbl.getNumbers();
		DoubleMatrix newD = new DoubleMatrix(newL,newL.get(0).size());
		log.debug("Original Data " + orig);
		log.debug("New  Data " + newD);
		DataGenerator.compareData(orig.getData(), newD.getData());
	}
	
}
