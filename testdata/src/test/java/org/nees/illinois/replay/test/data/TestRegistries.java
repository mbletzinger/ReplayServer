package org.nees.illinois.replay.test.data;

import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.common.registries.ChannelNameManagement;
import org.nees.illinois.replay.common.registries.ChannelNameRegistry;
import org.nees.illinois.replay.common.registries.QueryRegistry;
import org.nees.illinois.replay.common.registries.SavedQueryDeleteMe;
import org.nees.illinois.replay.common.registries.TableType;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.test.utils.ChannelListTestMaps;
import org.nees.illinois.replay.test.utils.ChannelListType;
import org.nees.illinois.replay.test.utils.QueryChannelLists;
import org.nees.illinois.replay.test.utils.CompareLists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = { "cnr" })
public class TestRegistries {
	private final Logger log = LoggerFactory.getLogger(TestRegistries.class);

	@Test
	public void AddCnrNames() {
		ChannelListTestMaps cltm = new ChannelListTestMaps(false, "Test 1");
		ChannelNameRegistry cnr = new ChannelNameRegistry();
		int count = 1;
		for (String c : cltm.getChannels(ChannelListType.OM)) {
			cnr.addChannel(TableType.Control, c);
			String expected = TableType.Control.toString().toLowerCase()
					+ "_channel" + count;
			count++;
			Assert.assertEquals(expected, cnr.getId(c));
		}
		for (String c : cltm.getChannels(ChannelListType.DAQ)) {
			cnr.addChannel(TableType.DAQ, c);
			String expected = TableType.DAQ.toString().toLowerCase()
					+ "_channel" + count;
			count++;
			Assert.assertEquals(expected, cnr.getId(c));
		}
	}

	@Test
	public void AddCnrNames2() {
		ChannelListTestMaps cltm = new ChannelListTestMaps(false, "Test 1");
		ChannelNameRegistry cnr = new ChannelNameRegistry();
		int count = 1;
		List<String> expectedChannels = new ArrayList<String>();
		List<String> expectedIds = new ArrayList<String>();
		for (String c : cltm.getChannels(ChannelListType.OM)) {
			cnr.addChannel(TableType.Control, c);
			String id = TableType.Control.toString().toLowerCase() + "_channel"
					+ count;
			expectedIds.add(id);
			count++;
		}
		expectedChannels.addAll(cltm.getChannels(ChannelListType.OM));
		checkCnr(cnr, expectedChannels, expectedIds);
		for (String c : cltm.getChannels(ChannelListType.DAQ)) {
			cnr.addChannel(TableType.DAQ, c);
			String id = TableType.DAQ.toString().toLowerCase() + "_channel"
					+ count;
			expectedIds.add(id);
			count++;
		}
		expectedChannels.addAll(cltm.getChannels(ChannelListType.DAQ));
		checkCnr(cnr, expectedChannels, expectedIds);
	}

	@Test
	public void AddSavedQueries() {
		ChannelListTestMaps cltm = new ChannelListTestMaps(false, "Test 1");
		ChannelNameManagement cnm = new ChannelNameManagement();
		for (ChannelListType tt : cltm.getTableTypes()) {
			List<String> channels = cltm.getChannels(tt);
			cnm.lookupChannels(cltm.getTt(tt), channels);
		}
		
		QueryRegistry qr = new QueryRegistry();
		CompareLists<String> compare = new CompareLists<String>();
		for (ChannelListType qt : cltm.getQueryTypes()) {
			QueryChannelLists ctl = cltm.getChannelLists(qt);
			for (RateType r : RateType.values()) {
				SavedQueryDeleteMe query = new SavedQueryDeleteMe(ctl.combine(), qt.name(),
						cnm.getCnr(), r);
				log.debug("Created query " + query);
				Assert.assertEquals(ctl.combine().size(), query.getNoc()
						.getNumber(false), "Query" + qt
						+ " has the wrong number of data columns");
				Assert.assertEquals(ctl.combine().size() + 4, query.getNoc()
						.getNumber(true), "Query" + qt
						+ " has the wrong number of time columns");
				List<String> expected = cnm.getCnr().names2Ids(ctl.combine());
				compare.compare(query.getQueryOrder(), expected);
				qr.setQuery(qt.name(), query);
			}
		}
	}

	private void checkCnr(ChannelNameRegistry cnr, List<String> expectedNames,
			List<String> expectedIds) {
		List<String> names = cnr.getNames();
		List<String> ids = cnr.names2Ids(names);
		for (String c : expectedNames) {
			Assert.assertTrue(names.contains(c), "CNR " + cnr
					+ "\n\tdoes not contain name \"" + c + "\"");
		}
		for (String id : expectedIds) {
			Assert.assertTrue(ids.contains(id), "CNR " + cnr
					+ "\n\tdoes not contain id \"" + id + "\"");
		}

	}

}
