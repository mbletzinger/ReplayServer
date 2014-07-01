package org.nees.illinois.replay.test.db;

import org.nees.illinois.replay.common.registries.ExperimentRegistries;
import org.nees.illinois.replay.common.types.TableDefinitionI;
import org.nees.illinois.replay.db.DbPools;
import org.nees.illinois.replay.db.data.DbDataUpdates;
import org.nees.illinois.replay.test.db.derby.process.DerbyDbControl;
import org.nees.illinois.replay.test.db.utils.DbTableCheck;
import org.nees.illinois.replay.test.db.utils.DbTestsModule;
import org.nees.illinois.replay.test.utils.DatasetsDirector;
import org.nees.illinois.replay.test.utils.TestDataset;
import org.nees.illinois.replay.test.utils.TestDatasetParameters;
import org.nees.illinois.replay.test.utils.types.ExperimentNames;
import org.nees.illinois.replay.test.utils.types.TestDataSource;
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
 * Test uploading data sets from the data director into the database.
 * @author Michael Bletzinger
 */
public class TestDataSetUploads {
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
			.getLogger(TestDataSetUploads.class);

	/**
	 * Close the database after removing all test content.
	 */
	@AfterClass
	public final void afterClass() {
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
	 * Create the database connection and the data director.
	 * @param db
	 *            Label for database.
	 * @throws Exception
	 *             if something breaks.
	 */
	@Parameters("db")
	@BeforeClass
	public final void beforeClass(@Optional("derby") final String db)
			throws Exception {
		guiceMod = new DbTestsModule(db);
		Injector injector = Guice.createInjector(guiceMod);
		pools = injector.getInstance(DbPools.class);
		ismysql = db.equals("mysql");
		if (ismysql == false) {
			ddbc.startDerby();
		}
		pools.getOps().createDatabase(experimentName.name());
		final int numberOfRows = 40;
		final int numberOfEvents = 10;
		qsd = new DatasetsDirector(experimentName, numberOfRows, numberOfEvents);
	}

	/**
	 * Test uploading the entire dataset for each source.
	 */
	@Test
	public final void test01UploadBulk() {
		ExperimentRegistries expreg = new ExperimentRegistries(
				experimentName.toString());
		DbDataUpdates ddu = new DbDataUpdates(pools);
		ddu.setExperiment(expreg);
		DbTableCheck dbCheck = new DbTableCheck(pools, experimentName.toString());
		for (TestDataSource tds : TestDataSource.values()) {
			TestDataset dat = qsd.getDataset(tds);
			TestDatasetParameters set = qsd.getParameters();
			ddu.createTable(tds.toString(), set.getTt(tds),
					set.getChannels(tds));
			ddu.update(tds.toString(), dat.getData().getData());
			TableDefinitionI td = expreg.getTableDefs()
					.getTable(tds.toString());
			dbCheck.checkTableContents(td, dat.getData());
		}
		for(TestDataSource tds : TestDataSource.values()) {
			ddu.removeTable(tds.toString());
		}
	}

}
