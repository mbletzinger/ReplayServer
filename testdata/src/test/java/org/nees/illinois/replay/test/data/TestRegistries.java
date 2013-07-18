package org.nees.illinois.replay.test.data;

import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.common.registries.ChannelNameRegistry;
import org.nees.illinois.replay.common.registries.QueryRegistry;
import org.nees.illinois.replay.common.registries.TableIdentityRegistry;
import org.nees.illinois.replay.common.registries.TableRegistry;
import org.nees.illinois.replay.common.registries.TableType;
import org.nees.illinois.replay.common.types.CompositeQuery;
import org.nees.illinois.replay.common.types.CompositeQueryI;
import org.nees.illinois.replay.common.types.TableColumns;
import org.nees.illinois.replay.common.types.TableColumnsI;
import org.nees.illinois.replay.common.types.TableIdentityI;
import org.nees.illinois.replay.test.utils.CompareLists;
import org.nees.illinois.replay.test.utils.QueryChannelLists;
import org.nees.illinois.replay.test.utils.TestDatasetType;
import org.nees.illinois.replay.test.utils.TestDatasets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Class to test the experiment scoped registries of the replay server.
 * @author Michael Bletzinger
 */
@Test(groups = { "cnr" })
public class TestRegistries {
	/**
	 * Logger.
	 */
	private final Logger log = LoggerFactory.getLogger(TestRegistries.class);

	/**
	 * Test the Table ID registry.
	 */
	@Test
	public final void addTableId() {
		String experiment = "TableIDTest";
		TableIdentityRegistry tir = new TableIdentityRegistry();
		final int numberOfDuplicates = 4;
		log.debug("Adding table id's");
		for (int c = 0; c < numberOfDuplicates; c++) {
			for (TableType tt : TableType.values()) {
				String dname = tt.toString() + "Name" + c;
				tir.addTable(experiment, dname, tt);
			}
		}
		for (int c = 0; c < numberOfDuplicates; c++) {
			List<String> tnames = tir.getNames();
			for (TableType tt : TableType.values()) {
				log.debug("Checking " + tt + " count " + c);
				String dname = tt.toString() + "Name" + c;
				Assert.assertTrue(tnames.contains(dname));
				Assert.assertEquals(tir.getId(dname).getDbName(), experiment
						+ "." + tt.toString().toLowerCase() + "_" + c);
			}
		}
	}

	/**
	 * Test the Channel Name Registry.
	 */
	@Test
	public final void addCnrNames() {
		TestDatasets cltm = new TestDatasets(false, "Test 1");
		ChannelNameRegistry cnr = new ChannelNameRegistry();
		int count = 1;
		for (String c : cltm.getChannels(TestDatasetType.OM)) {
			cnr.addChannel(TableType.Control.toString(), c);
			String expected = TableType.Control.toString().toLowerCase()
					+ "_channel" + count;
			count++;
			Assert.assertEquals(expected, cnr.getId(c));
		}
		for (String c : cltm.getChannels(TestDatasetType.DAQ)) {
			cnr.addChannel(TableType.DAQ.toString(), c);
			String expected = TableType.DAQ.toString().toLowerCase()
					+ "_channel" + count;
			count++;
			Assert.assertEquals(expected, cnr.getId(c));
		}
	}

	/**
	 * Test Table Registry.
	 */
	@Test
	public final void addTables() {
		String experiment = "TableTest";
		CompareLists<String> compL = new CompareLists<String>();
		TestDatasets cltm = new TestDatasets(false, experiment);
		TableIdentityRegistry tir = new TableIdentityRegistry();
		TableRegistry tr = new TableRegistry();
		for (TestDatasetType t : cltm.getTableTypes()) {
			TableIdentityI tableId = tir.addTable(experiment, cltm.getTableName(t),
					cltm.getTt(t));
			TableColumnsI table = new TableColumns(cltm.getChannels(t), tableId);
			tr.setTable(tableId.getDatasetName(), table);
		}
		for (TestDatasetType t : cltm.getTableTypes()) {
			String name = cltm.getTableName(t);
			log.debug("Checking dataet " + t + " with name " + name);
			TableColumnsI table = tr.getTable(name);
			Assert.assertNotNull(table);
			compL.compare(table.getColumns(false), cltm.getChannels(t));
		}
	}
	/**
	 * Test Query Registry.
	 */
	@Test
	public final void addQueries() {
		String experiment = "QueriesTest";
		CompareLists<String> compL = new CompareLists<String>();
		TestDatasets cltm = new TestDatasets(false, experiment);
		QueryRegistry qr = new QueryRegistry();
		for (TestDatasetType t : cltm.getQueryTypes()) {
			QueryChannelLists tquery = cltm.getTestQuery(t);
			CompositeQueryI query = new CompositeQuery(t.name(), tquery.combine());
			qr.setQuery(t.name(), query);
		}
		for (TestDatasetType t : cltm.getQueryTypes()) {
			log.debug("Checking query " + t);
			CompositeQueryI query = qr.getQuery(t.name());
			Assert.assertNotNull(query);
			compL.compare(query.getQueryList(), cltm.getChannels(t));
		}
	}

	/**
	 * Another test to add channels to the Channel Name Registry.
	 */
	@Test
	public final void addCnrNames2() {
		TestDatasets cltm = new TestDatasets(false, "Test 1");
		ChannelNameRegistry cnr = new ChannelNameRegistry();
		int count = 1;
		List<String> expectedChannels = new ArrayList<String>();
		List<String> expectedIds = new ArrayList<String>();
		for (String c : cltm.getChannels(TestDatasetType.OM)) {
			cnr.addChannel(TableType.Control.toString(), c);
			String id = TableType.Control.toString().toLowerCase() + "_channel"
					+ count;
			expectedIds.add(id);
			count++;
		}
		expectedChannels.addAll(cltm.getChannels(TestDatasetType.OM));
		checkCnr(cnr, expectedChannels, expectedIds);
		for (String c : cltm.getChannels(TestDatasetType.DAQ)) {
			cnr.addChannel(TableType.DAQ.toString(), c);
			String id = TableType.DAQ.toString().toLowerCase() + "_channel"
					+ count;
			expectedIds.add(id);
			count++;
		}
		expectedChannels.addAll(cltm.getChannels(TestDatasetType.DAQ));
		checkCnr(cnr, expectedChannels, expectedIds);
	}

	/**
	 * Determine if the actual channel name registry has the expected content.
	 * @param cnr
	 *            Registry under test.
	 * @param expectedNames
	 *            List of expected names.
	 * @param expectedIds
	 *            List of expected IDs
	 */
	private void checkCnr(final ChannelNameRegistry cnr,
			final List<String> expectedNames, final List<String> expectedIds) {
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
