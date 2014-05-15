package org.nees.illinois.replay.test.db;

import org.nees.illinois.replay.db.DbPools;
import org.nees.illinois.replay.test.db.derby.process.DerbyDbControl;
import org.nees.illinois.replay.test.db.utils.DbTestsModule;
import org.nees.illinois.replay.test.utils.DatasetsDirector;
import org.nees.illinois.replay.test.utils.types.ExperimentNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	 * Test data.
	 */
	private DatasetsDirector qsd;
	/**
	 * Logger.
	 **/
	private final Logger log = LoggerFactory
			.getLogger(TestSimpleDataQueries.class);

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
		pools.getOps().createDatabase(experimentName.name());
		final int numberOfRows = 50;
		final int numberOfEvents = 10;
		qsd = new DatasetsDirector(experimentName, numberOfRows, numberOfEvents);
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

	@Test
	public void testQueries() {
	}

}
