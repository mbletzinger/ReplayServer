package org.nees.illinois.replay.test.db;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.nees.illinois.replay.common.registries.ExperimentRegistries;
import org.nees.illinois.replay.common.registries.TableRegistry;
import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.DoubleMatrixI;
import org.nees.illinois.replay.db.DbPools;
import org.nees.illinois.replay.db.data.DbDataUpdates;
import org.nees.illinois.replay.db.statement.StatementProcessor;
import org.nees.illinois.replay.test.db.derby.process.DerbyDbControl;
import org.nees.illinois.replay.test.db.utils.DbTableCheck;
import org.nees.illinois.replay.test.db.utils.DbTestsModule;
import org.nees.illinois.replay.test.db.utils.UpdateDataSets;
import org.nees.illinois.replay.test.utils.TestDatasetType;
import org.nees.illinois.replay.test.utils.TestDatasets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Test the creation and updates of data tables.
 * @author Michael Bletzinger
 */
public class TestDbUpdates {
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
	 * Test data sets.
	 */
	private TestDatasets set;
	/**
	 * Logger.
	 **/
	private final Logger log = LoggerFactory.getLogger(TestDbUpdates.class);

	/**
	 * Test Table creation.
	 */
	@Test
	public final void testCreateTable() {
		DbDataUpdates dbdu = new DbDataUpdates(pools);
		DbTableCheck dbtc = new DbTableCheck(pools, experimentName);
		ExperimentRegistries experiment = new ExperimentRegistries(
				experimentName);
		dbdu.setExperiment(experiment);
		for (TestDatasetType t : set.getTableTypes()) {
			String tname = dbdu.createTable(set.getTableName(t), set.getTt(t),
					set.getChannels(t));
			Assert.assertNotNull(tname);
			List<String> dbnames = new ArrayList<String>();
			dbnames.add("TIME");
			dbnames.addAll(experiment.getCnr().names2Ids(set.getChannels(t)));
			dbtc.checkTable(tname, dbnames);
			Assert.assertTrue(dbdu.removeTable(set.getTableName(t)));
		}

	}

	/**
	 * Test the ability to add data to a table.
	 */
	@Test
	public final void testUpdateTable() {
		DbDataUpdates dbdu = new DbDataUpdates(pools);
		DbTableCheck dbtc = new DbTableCheck(pools, experimentName);
		ExperimentRegistries experiment = new ExperimentRegistries(
				experimentName);
		dbdu.setExperiment(experiment);
		for (TestDatasetType t : set.getTableTypes()) {
			log.debug("Checking table " + t.name());
			String tname = dbdu.createTable(set.getTableName(t), set.getTt(t),
					set.getChannels(t));
			Assert.assertNotNull(tname);
			UpdateDataSets uds = new UpdateDataSets(set);
			DoubleMatrix expected = null;
			for (int u = 0; u < uds.numberOfUpdates(); u++) {
				DoubleMatrixI dm = uds.generate(t, u);
				dbdu.update(set.getTableName(t), dm.getData());
				if(expected == null ) {
					expected = new DoubleMatrix(dm.getData());
				} else {
					for(List<Double> row : dm.toList()) {
						expected.append(row);
					}
				}
				log.debug("Checking update " + u);
				dbtc.checkTableContents(set.getTableDefinition(t, experiment.getCnr(), new TableRegistry()), expected);
			}

			Assert.assertTrue(dbdu.removeTable(set.getTableName(t)));
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
		set = new TestDatasets(false, experimentName);
	}

	/**
	 * Clean up the databases.
	 * @throws Exception
	 *             If anything breaks.
	 */
	@AfterMethod
	public final void tearDown() throws Exception {
		StatementProcessor dbSt = pools
				.createDbStatement(experimentName, false);
		dbSt.noComplaints("DROP TABLE TestTable");
		dbSt.close();
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
}
