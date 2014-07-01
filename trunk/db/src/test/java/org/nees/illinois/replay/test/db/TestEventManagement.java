package org.nees.illinois.replay.test.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nees.illinois.replay.common.registries.ExperimentRegistries;
import org.nees.illinois.replay.common.registries.TableDefiner;
import org.nees.illinois.replay.db.DbPools;
import org.nees.illinois.replay.db.events.DbEvents;
import org.nees.illinois.replay.db.events.EventQueries;
import org.nees.illinois.replay.db.events.EventsTableOps;
import org.nees.illinois.replay.events.EventI;
import org.nees.illinois.replay.events.EventListI;
import org.nees.illinois.replay.test.db.derby.process.DerbyDbControl;
import org.nees.illinois.replay.test.db.utils.DbTestsModule;
import org.nees.illinois.replay.test.utils.CompareLists;
import org.nees.illinois.replay.test.utils.DatasetsDirector;
import org.nees.illinois.replay.test.utils.TestDatasetParameters;
import org.nees.illinois.replay.test.utils.types.ExperimentNames;
import org.nees.illinois.replay.test.utils.types.TestDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Class which test how event data is managed by the database.
 * @author Michael Bletzinger
 */
@Test(groups = { "db-events" })
public class TestEventManagement {
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
	 * Logger.
	 **/
	private final Logger log = LoggerFactory
			.getLogger(TestEventManagement.class);
	/**
	 * Event and data generators.
	 */
	private DatasetsDirector dd;
	/**
	 * Set of registries for the experiment.
	 */
	private ExperimentRegistries er;

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
		final int numberOfRows = 50;
		final int numberOfEvents = 10;
		pools.getOps().createDatabase(experimentName.toString());
		er = new ExperimentRegistries(experimentName.toString());
		dd = new DatasetsDirector(experimentName, numberOfRows, numberOfEvents);
		TableDefiner td = er.createTableDefiner();
		TestDatasetParameters tdp = dd.getParameters();
		for(TestDataSource tds : TestDataSource.values()) {
			td.define(tds.toString(), tdp.getTt(tds), tdp.getChannels(tds));
		}
	}

	/**
	 * Set up the event table.
	 * @throws Exception
	 *             If anything breaks.
	 */
	@BeforeMethod
	public final void setup1() throws Exception {
		EventsTableOps eto = new EventsTableOps(pools, er);
		Assert.assertTrue(eto.create());
		Assert.assertTrue(eto.exists());
	}

	/**
	 * Clean up the databases.
	 * @throws Exception
	 *             If anything breaks.
	 */
	@AfterMethod
	public final void tearDown() throws Exception {
		EventsTableOps eto = new EventsTableOps(pools, er);
		eto.remove();
	}

	/**
	 * Remove the database and shut down the pools.
	 */
	@AfterClass
	public final void teardown1() {
		try {
			pools.getOps().removeDatabase("HybridMasonry1");
		} catch (Exception e) {
			log.error("Database remove failed because ", e);
			AssertJUnit.fail();
		}
		if (ismysql == false) {
			ddbc.stopDerby();
		}
	}

	/**
	 * Test posting events.
	 */
	@Test
	public final void test01EventStorage() {
		DbEvents dbEvents = new DbEvents(pools, er);
		for (TestDataSource t : TestDataSource.values()) {
			EventListI list = dd.getDataset(t).getEvents();
			for (EventI l : list.getEvents()) {
				EventI dbe = dbEvents.createEvent(l.getTime(),
						l.getName(), l.getDescription(), l.getSource());
				Assert.assertEquals(dbe, l);
			}
		}

	}

	/**
	 * Test querying events.
	 */
	@Test
	public final void test02EventQueries() {

		DbEvents dbEvents = new DbEvents(pools, er);
		Map<TestDataSource, EventListI> expected = new HashMap<TestDataSource, EventListI>();
		for (TestDataSource t : TestDataSource.values()) {
			EventListI list = dd.getDataset(t).getEvents();
			for (EventI l : list.getEvents()) {
				dbEvents.createEvent(l.getTime(), l.getName(),
						l.getDescription(), l.getSource());
			}
			expected.put(t, list);
		}
		EventsTableOps eto = new EventsTableOps(pools, er);
		EventQueries evQueries = eto.getQueries();
		for (TestDataSource t : TestDataSource.values()) {
			EventListI elist = expected.get(t);
			List<EventI> elistL = elist.getEvents();
			for (EventI e : elistL) {
				List<String> name = new ArrayList<String>();
				name.add(e.getName());
				EventListI alist = evQueries.getEvents(name,t.toString());
				Assert.assertNotNull(alist);
				Assert.assertEquals(alist.getEvents().size(), 1);
				Assert.assertEquals(alist.getEvents().get(0), e);
			}
			CompareLists<EventI> cmp = new CompareLists<EventI>();
			double start = elistL.get(0).getTime() - 1;
			double finish = elistL.get(elistL.size() - 1).getTime() + 1;
			EventListI alist = evQueries.getEvents(start, finish,t.toString());
			cmp.compare(alist.getEvents(), elistL);
			String startS = elistL.get(0).getName();
			String stopS = elistL.get(elistL.size() - 1).getName();
			alist = evQueries.getEvents(startS, stopS, t.toString());
			cmp.compare(alist.getEvents(), elistL);
		}
		evQueries.close();

	}
}
