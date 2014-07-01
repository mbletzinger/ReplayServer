package org.nees.illinois.replay.test.db;

import java.sql.Connection;

import org.nees.illinois.replay.common.registries.ExperimentRegistries;
import org.nees.illinois.replay.common.registries.QueryDefiner;
import org.nees.illinois.replay.common.registries.QueryRegistry;
import org.nees.illinois.replay.common.types.CompositeQueryI;
import org.nees.illinois.replay.common.types.TimeBoundsI;
import org.nees.illinois.replay.data.DoubleMatrixI;
import org.nees.illinois.replay.db.DbPools;
import org.nees.illinois.replay.db.data.DbDataUpdates;
import org.nees.illinois.replay.db.events.DbEvents;
import org.nees.illinois.replay.db.events.EventQueries;
import org.nees.illinois.replay.db.events.EventsTableOps;
import org.nees.illinois.replay.db.query.CompositeQueryExecutor;
import org.nees.illinois.replay.events.EventI;
import org.nees.illinois.replay.events.EventListI;
import org.nees.illinois.replay.test.db.derby.process.DerbyDbControl;
import org.nees.illinois.replay.test.db.utils.DbTestsModule;
import org.nees.illinois.replay.test.utils.DatasetsDirector;
import org.nees.illinois.replay.test.utils.TestDataset;
import org.nees.illinois.replay.test.utils.TestDatasetParameters;
import org.nees.illinois.replay.test.utils.gen.TestCompositeQuery;
import org.nees.illinois.replay.test.utils.types.ExperimentNames;
import org.nees.illinois.replay.test.utils.types.QueryTestCases;
import org.nees.illinois.replay.test.utils.types.TestDataSource;
import org.nees.illinois.replay.test.utils.types.TestingParts;
import org.nees.illinois.replay.test.utils.types.TimeBoundaryTestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Test data queries with only one table.
 * @author Michael Bletzinger
 */
@Test(groups = { "db-qsimple" })
public class TestSimpleDataQueries {
	/**
	 * Map of database pools.
	 */
	private DbPools pools;
	/**
	 * Extra Derby controls.
	 */
	private final DerbyDbControl ddbc = new DerbyDbControl();
	/**
	 * Experiment name.
	 */
	private final ExperimentNames experimentName = ExperimentNames.HybridMasonry1;
	/**
	 * Module for injecting database properties.
	 */
	private DbTestsModule guiceMod;
	/**
	 * Flag that is true if the database is MySQL.
	 */
	private boolean ismysql;
	/**
	 * Registry set for the test.
	 */
	private ExperimentRegistries er;

	/**
	 * Test data.
	 */
	private DatasetsDirector dd;
	/**
	 * Logger.
	 **/
	private final Logger log = LoggerFactory
			.getLogger(TestSimpleDataQueries.class);

	/**
	 * Create a query name from a test case.
	 * @param q
	 *            query test case.
	 * @param isOld
	 *            true if this is the first part of the query.
	 * @return the name.
	 */
	private String createQueryName(final QueryTestCases q, final boolean isOld) {
		return q.name() + (isOld ? "-old" : "-new");

	}

	/**
	 * Load all of the dataset director's data into the database.
	 */
	private void loadDb() {
		DbDataUpdates ddu = new DbDataUpdates(pools);
		ddu.setExperiment(er);
// DbTableCheck dbCheck = new DbTableCheck(pools,
// experimentName.toString());
		for (TestDataSource tds : TestDataSource.values()) {
			TestDataset dat = dd.getDataset(tds);
			TestDatasetParameters set = dd.getParameters();
			ddu.createTable(tds.toString(), set.getTt(tds),
					set.getChannels(tds));
			log.debug("Loading " + tds);
			ddu.update(tds.toString(), dat.getData().getData());
// TableDefinitionI td = er.getTableDefs().getTable(tds.toString());
// dbCheck.checkTableContents(td, dat.getData());

		}
		DbEvents dbEvents = new DbEvents(pools, er);
		for (TestDataSource t : TestDataSource.values()) {
			EventListI list = dd.getDataset(t).getEvents();
			for (EventI l : list.getEvents()) {
				EventI dbe = dbEvents.createEvent(l.getTime(), l.getName(),
						l.getDescription(), l.getSource());
				Assert.assertEquals(dbe, l);
			}
		}
	}

	/**
	 * Store all of the test queries to be executed.
	 */
	private void setQueries() {
		QueryDefiner qd = er.createQueryDefiner();
		for (QueryTestCases q : QueryTestCases.values()) {
			if (q.equals(QueryTestCases.QueryTriple)) {
				continue;
			}
			log.debug("Testing query " + q);
			TestDatasetParameters tdp = dd.getParameters();
			TestCompositeQuery tquery = tdp.getTestQuery(q);
			Assert.assertNotNull(tquery);
			CompositeQueryI query = null;
			String oldQName = null;
			if (tquery.getExisting() != null) {
				oldQName = createQueryName(q, true);
				query = qd.define(oldQName, tquery.getExistingList());
				Assert.assertNotNull(query);
			}
			String newQName = createQueryName(q, false);
			query = qd.define(newQName, tquery.getNewChannels());
			Assert.assertNotNull(query);
		}
	}

	/**
	 * Set up the data for the tests.
	 * @param db
	 *            Label for database.
	 * @throws Exception
	 *             if something breaks.
	 */
	@Parameters("db")
	@BeforeClass
	public final void setUp(@Optional("derby") final String db)
			throws Exception {
		guiceMod = new DbTestsModule(db);
		Injector injector = Guice.createInjector(guiceMod);
		pools = injector.getInstance(DbPools.class);
		ismysql = db.equals("mysql");
		if (ismysql == false) {
			ddbc.startDerby();
		}
		er = new ExperimentRegistries(experimentName.toString());
		pools.getOps().createDatabase(experimentName.toString());
		EventsTableOps eto = new EventsTableOps(pools,er);
		eto.create();
		final int numberOfRows = 50;
		final int numberOfEvents = 10;
		dd = new DatasetsDirector(experimentName, numberOfRows, numberOfEvents);
		loadDb();
		setQueries();
	}

	/**
	 * Remove the database and shut down the pools.
	 */
	@AfterClass
	public final void teardown() {
		DbDataUpdates ddu = new DbDataUpdates(pools);
		ddu.setExperiment(er);
		for (TestDataSource tds : TestDataSource.values()) {
			ddu.removeTable(tds.toString());
		}
		try {
			pools.getOps().removeDatabase(experimentName.toString());
		} catch (Exception e) {
			log.error("Database remove failed because ", e);
			AssertJUnit.fail();
		}
		if (ismysql == false) {
			ddbc.stopDerby();
		}
	}

	/**
	 * Run through all of the sole source queries and verify that the results
	 * are correct. Case ContWithTime
	 */
	@Test
	public final void test01ContWithTimeQueries() {
		Connection connection = pools.fetchConnection(
				experimentName.toString(), false);
		EventQueries eventQ = new EventQueries(connection, er);
		for (QueryTestCases q : QueryTestCases.values()) {
			if (q.equals(QueryTestCases.QueryTriple)) {
				continue;
			}
			log.debug("Testing query " + q);
			testQueries(q, connection, eventQ,
					TimeBoundaryTestType.ContWithTime);
		}
	}

	/**
	 * Run through all of the sole source queries and verify that the results
	 * are correct. Case ContWithStart2End
	 */
	@Test
	public final void test02ContWithStart2EndQueries() {
		Connection connection = pools.fetchConnection(
				experimentName.toString(), false);
		EventQueries eventQ = new EventQueries(connection, er);
		for (QueryTestCases q : QueryTestCases.values()) {
			if (q.equals(QueryTestCases.QueryTriple)) {
				continue;
			}
			log.debug("Testing query " + q);
			testQueries(q, connection, eventQ,
					TimeBoundaryTestType.ContWithStart2End);
		}
	}

	/**
	 * Run through all of the sole source queries and verify that the results
	 * are correct. Case ContWithStartStop
	 */
	@Test
	public final void test03ContWithStartStopQueries() {
		Connection connection = pools.fetchConnection(
				experimentName.toString(), false);
		EventQueries eventQ = new EventQueries(connection, er);
		for (QueryTestCases q : QueryTestCases.values()) {
			if (q.equals(QueryTestCases.QueryTriple)) {
				continue;
			}
			log.debug("Testing query " + q);
			testQueries(q, connection, eventQ,
					TimeBoundaryTestType.ContWithStartStop);
		}
	}

	/**
	 * Run through all of the sole source queries and verify that the results
	 * are correct. Case Event
	 */
	@Test
	public final void test04EventQueries() {
		Connection connection = pools.fetchConnection(
				experimentName.toString(), false);
		EventQueries eventQ = new EventQueries(connection, er);
		for (QueryTestCases q : QueryTestCases.values()) {
			if (q.equals(QueryTestCases.QueryTriple)) {
				continue;
			}
			log.debug("Testing query " + q);
			testQueries(q, connection, eventQ, TimeBoundaryTestType.Event);
		}
	}

	/**
	 * Run through all of the sole source queries and verify that the results
	 * are correct. Case ContWithEventStartStop
	 */
	@Test
	public final void test05ContWithEventStartStopQueries() {
		Connection connection = pools.fetchConnection(
				experimentName.toString(), false);
		EventQueries eventQ = new EventQueries(connection, er);
		for (QueryTestCases q : QueryTestCases.values()) {
			if (q.equals(QueryTestCases.QueryTriple)) {
				continue;
			}
			log.debug("Testing query " + q);
			testQueries(q, connection, eventQ,
					TimeBoundaryTestType.ContWithEventStartStop);
		}
	}

	/**
	 * Run through all of the sole source queries and verify that the results
	 * are correct. Case EventsWithStartStop
	 */
	@Test
	public final void test05EventsWithStartStopQueries() {
		Connection connection = pools.fetchConnection(
				experimentName.toString(), false);
		EventQueries eventQ = new EventQueries(connection, er);
		for (QueryTestCases q : QueryTestCases.values()) {
			if (q.equals(QueryTestCases.QueryTriple)) {
				continue;
			}
			log.debug("Testing query " + q);
			testQueries(q, connection, eventQ,
					TimeBoundaryTestType.EventsWithStartStop);
		}
	}

	/**
	 * Execute the queries and check the return values.
	 * @param q
	 *            Test case to execute.
	 * @param connection
	 *            to the database.
	 * @param eventQ
	 *            Executor for querying events.
	 * @param qbounds
	 *            Time boundary test case to execute.
	 */
	private void testQueries(final QueryTestCases q,
			final Connection connection, final EventQueries eventQ,
			final TimeBoundaryTestType qbounds) {
		TestDatasetParameters tdp = dd.getParameters();
		TestCompositeQuery tquery = tdp.getTestQuery(q);
		Assert.assertNotNull(tquery);
		CompositeQueryI query = null;
		QueryRegistry qr = er.getQueries();
		CompositeQueryExecutor exec;
		TimeBoundsI tb;
		DoubleMatrixI actual;
		if (tquery.getExisting() != null) {
			query = qr.getQuery(createQueryName(q, true));
			exec = new CompositeQueryExecutor(query, connection, eventQ);
			tb = dd.getTimeBounds(qbounds, tquery, TestingParts.First);
			log.debug("Executing first query  for time case" + qbounds);
			actual = exec.execute(tb);
			dd.checkData(experimentName, qbounds, tquery, actual,
					TestingParts.First);
		}
		query = qr.getQuery(createQueryName(q, false));
		exec = new CompositeQueryExecutor(query, connection, eventQ);
		tb = dd.getTimeBounds(qbounds, tquery, TestingParts.Second);
		log.debug("Executing second query  for time case" + qbounds);
		actual = exec.execute(tb);
		dd.checkData(experimentName, qbounds, tquery, actual,
				TestingParts.Second);

	}
}
