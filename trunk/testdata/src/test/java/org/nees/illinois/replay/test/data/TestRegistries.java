package org.nees.illinois.replay.test.data;

import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.common.registries.ChannelNameRegistry;
import org.nees.illinois.replay.common.registries.QueryRegistry;
import org.nees.illinois.replay.common.registries.TableRegistry;
import org.nees.illinois.replay.common.registries.TableType;
import org.nees.illinois.replay.common.types.CompositeQuery;
import org.nees.illinois.replay.common.types.CompositeQueryI;
import org.nees.illinois.replay.common.types.TableDef;
import org.nees.illinois.replay.common.types.TableDefinitionI;
import org.nees.illinois.replay.test.utils.CompareLists;
import org.nees.illinois.replay.test.utils.TestDatasetParameters;
import org.nees.illinois.replay.test.utils.gen.TestCompositeQuery;
import org.nees.illinois.replay.test.utils.types.QueryTestCases;
import org.nees.illinois.replay.test.utils.types.TestDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Class to test the experiment scoped registries of the replay server.
 * @author Michael Bletzinger
 */
@Test(groups = { "register" })
public class TestRegistries {
	/**
	 * Logger.
	 */
	private final Logger log = LoggerFactory.getLogger(TestRegistries.class);


	/**
	 * Test the Channel Name Registry.
	 */
	@Test
	public final void addCnrNames() {
		TestDatasetParameters cltm = new TestDatasetParameters(false, "Test 1");
		ChannelNameRegistry cnr = new ChannelNameRegistry();
		int count = 1;
		for (String c : cltm.getChannels(TestDataSource.OM)) {
			cnr.addChannel(TableType.Control, c);
			String expected = TableType.Control.toString().toUpperCase()
					+ "_CHANNEL" + count;
			count++;
			Assert.assertEquals(cnr.getId(c),expected);
		}
		for (String c : cltm.getChannels(TestDataSource.DAQ)) {
			cnr.addChannel(TableType.DAQ, c);
			String expected = TableType.DAQ.toString().toUpperCase()
					+ "_CHANNEL" + count;
			count++;
			Assert.assertEquals(expected, cnr.getId(c));
		}
	}

	/**
	 * Another test to add channels to the Channel Name Registry.
	 */
	@Test
	public final void addCnrNames2() {
		TestDatasetParameters cltm = new TestDatasetParameters(false, "Test 1");
		ChannelNameRegistry cnr = new ChannelNameRegistry();
		int count = 1;
		List<String> expectedChannels = new ArrayList<String>();
		List<String> expectedIds = new ArrayList<String>();
		for (String c : cltm.getChannels(TestDataSource.OM)) {
			cnr.addChannel(TableType.Control, c);
			String id = TableType.Control.toString().toUpperCase() + "_CHANNEL"
					+ count;
			expectedIds.add(id);
			count++;
		}
		expectedChannels.addAll(cltm.getChannels(TestDataSource.OM));
		checkCnr(cnr, expectedChannels, expectedIds);
		for (String c : cltm.getChannels(TestDataSource.DAQ)) {
			cnr.addChannel(TableType.DAQ, c);
			String id = TableType.DAQ.toString().toUpperCase() + "_CHANNEL"
					+ count;
			expectedIds.add(id);
			count++;
		}
		expectedChannels.addAll(cltm.getChannels(TestDataSource.DAQ));
		checkCnr(cnr, expectedChannels, expectedIds);
	}
	/**
	 * Test Query Registry.
	 */
	@Test
	public final void addQueries() {
		String experiment = "QueriesTest";
		CompareLists<String> compL = new CompareLists<String>();
		TestDatasetParameters cltm = new TestDatasetParameters(false, experiment);
		QueryRegistry qr = new QueryRegistry();
		for (QueryTestCases t : QueryTestCases.values()) {
			log.debug("Checking query " + t);
			TestCompositeQuery tquery = cltm.getTestQuery(t);
			Assert.assertNotNull(tquery);
			CompositeQueryI query = new CompositeQuery(t.name(), tquery.combine());
			qr.setQuery(t.name(), query);
		}
		for (QueryTestCases t : QueryTestCases.values()) {
			log.debug("Checking query " + t);
			CompositeQueryI query = qr.getQuery(t.name());
			Assert.assertNotNull(query);
			TestCompositeQuery tquery = cltm.getTestQuery(t);
			compL.compare(query.getQueryList(), tquery.combine());
		}
	}

	/**
	 * Test Table Registry.
	 */
	@Test
	public final void addTables() {
		String experiment = "TableTest";
		CompareLists<String> compL = new CompareLists<String>();
		TestDatasetParameters cltm = new TestDatasetParameters(false, experiment);
		TableRegistry tr = new TableRegistry();
		for (TestDataSource t : TestDataSource.values()) {
			TableDefinitionI table = new TableDef(cltm.getChannels(t), cltm.getTableName(t));
			tr.setTable(table.getTableId(), table);
		}
		for (TestDataSource t : TestDataSource.values()) {
			String name = cltm.getTableName(t);
			log.debug("Checking dataet " + t + " with name " + name);
			TableDefinitionI table = tr.getTable(name);
			Assert.assertNotNull(table);
			compL.compare(table.getColumns(false), cltm.getChannels(t));
		}
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
