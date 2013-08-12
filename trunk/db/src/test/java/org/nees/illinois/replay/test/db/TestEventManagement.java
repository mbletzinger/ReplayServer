package org.nees.illinois.replay.test.db;

import java.util.List;

import org.nees.illinois.replay.common.registries.ExperimentRegistries;
import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.DoubleMatrixI;
import org.nees.illinois.replay.db.DbPools;
import org.nees.illinois.replay.db.events.DbEvents;
import org.nees.illinois.replay.db.events.EventsTableOps;
import org.nees.illinois.replay.events.EventI;
import org.nees.illinois.replay.events.EventListI;
import org.nees.illinois.replay.events.EventType;
import org.nees.illinois.replay.test.db.derby.process.DerbyDbControl;
import org.nees.illinois.replay.test.db.utils.DbTestsModule;
import org.nees.illinois.replay.test.db.utils.UpdateDataSets;
import org.nees.illinois.replay.test.utils.EventsGenerator;
import org.nees.illinois.replay.test.utils.TestDatasetType;
import org.nees.illinois.replay.test.utils.TestDatasets;
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
	private final String experimentName = "HybridMasonry1";
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
	 * Test events generator.
	 */
	private EventsGenerator teg;
	/**
	 * Dataset.
	 */
	private TestDatasets set;

	/**
	 * Test posting events.
	 */
	@Test
	public final void testEventStorage() {
		ExperimentRegistries er = new ExperimentRegistries(experimentName);
		DbEvents dbEvents = new DbEvents(pools, er);
		for (TestDatasetType t : set.getTableTypes()) {
			DoubleMatrixI dm = generateData(t);
			final int interval = 3;
			EventListI list = teg.generate(dm, interval, set.getTableName(t));
			for (EventI l : list.getEvents()) {
				EventI dbe = dbEvents.createEvent(l.getType().name(),
						l.getTime(), l.getName(), l.getName(), l.getSource());
				Assert.assertEquals(dbe, l);
			}
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
		pools.getOps().createDatabase(experimentName);
		teg = new EventsGenerator(EventType.StepNumber);
		set = new TestDatasets(false, experimentName);
	}

	/**
	 * Clean up the databases.
	 * @throws Exception
	 *             If anything breaks.
	 */
	@AfterMethod
	public final void tearDown() throws Exception {
		EventsTableOps eto = new EventsTableOps(pools, experimentName);
		eto.remove();
	}

	/**
	 * Set up the event table.
	 * @throws Exception
	 *             If anything breaks.
	 */
	@BeforeMethod
	public final void setup1() throws Exception {
		EventsTableOps eto = new EventsTableOps(pools, experimentName);
		Assert.assertTrue(eto.create());
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
	 * Generate dataset to tag events against.
	 * @param t
	 *            Test type
	 * @return double matrix with test data.
	 */
	private DoubleMatrixI generateData(final TestDatasetType t) {
		DoubleMatrix data = null;
		UpdateDataSets uds = new UpdateDataSets(set);
		for (int u = 0; u < uds.numberOfUpdates(); u++) {
			DoubleMatrixI dm = uds.generate(t, u);
			if (data == null) {
				data = new DoubleMatrix(dm.getData());
			} else {
				for (List<Double> row : dm.toList()) {
					data.append(row);
				}
			}
		}
		return data;
	}
}
