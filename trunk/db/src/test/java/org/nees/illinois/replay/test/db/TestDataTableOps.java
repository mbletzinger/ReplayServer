package org.nees.illinois.replay.test.db;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.nees.illinois.replay.common.registries.ExperimentRegistries;
import org.nees.illinois.replay.common.registries.TableDefiner;
import org.nees.illinois.replay.common.types.TableDefinitionI;
import org.nees.illinois.replay.db.DbPools;
import org.nees.illinois.replay.db.data.DataTableOps;
import org.nees.illinois.replay.db.statement.StatementProcessor;
import org.nees.illinois.replay.test.db.derby.process.DerbyDbControl;
import org.nees.illinois.replay.test.db.utils.DbTestsModule;
import org.nees.illinois.replay.test.utils.CompareLists;
import org.nees.illinois.replay.test.utils.TestDatasetType;
import org.nees.illinois.replay.test.utils.TestDatasets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Tests the creation and removal of data tables in the database.
 * @author Michael Bletzinger
 */
public class TestDataTableOps {
	/**
	 * Pools of database connections.
	 */
	private DbPools pools;
	/**
	 * Experiment test name.
	 */
	private ExperimentRegistries registries;
	/**
	 * True if database used for testing is MySQL.
	 */
	private boolean ismysql;
	/**
	 * Extra controls for Derby.
	 */
	private final DerbyDbControl ddbc = new DerbyDbControl();

	/**
	 * Logger.
	 **/
	private final Logger log = LoggerFactory.getLogger(TestDataTableOps.class);

	/**
	 * Stand up a database for testing.
	 * @param db
	 *            label of database to set up.
	 * @throws Exception
	 *             If something goes wrong with the database.
	 */
	@Parameters("db")
	@BeforeClass
	public final void setUp(@Optional("derby") final String db)
			throws Exception {
		DbTestsModule guiceMod = new DbTestsModule(db);
		Injector injector = Guice.createInjector(guiceMod);
		pools = injector.getInstance(DbPools.class);
		ismysql = db.equals("mysql");
		if (ismysql == false) {
			ddbc.startDerby();
		}
	}

	/**
	 * Shut down the database and clean up everything.
	 * @throws Exception
	 *             If something goes wrong with the database.
	 */
	@AfterClass
	public final void tearDown() throws Exception {
		pools.getOps().removeDatabase(registries.getExperiment());
		pools.close();
		if (ismysql == false) {
			ddbc.stopDerby();
		}
	}

	/**
	 * Tests the creation and removal of data tables in the database.
	 */
	@Test
	public final void testDataTableOps() {
		setExperiment("HybridMasonry1");
		TestDatasets sets = new TestDatasets(false, registries.getExperiment());
		for (TestDatasetType t : sets.getTableTypes()) {
			TableDefiner defnr = registries.createTableDefiner();
			StatementProcessor dbSt = pools.createDbStatement(
					registries.getExperiment(), true);
			TableDefinitionI td = defnr.define(sets.getTableName(t),
					sets.getTt(t), sets.getChannels(t));
			DataTableOps dto = new DataTableOps(td, dbSt);
			Assert.assertTrue(dto.create());
			check4Table(td.getTableId(), td.getColumns(true));
			dbSt = pools.createDbStatement(registries.getExperiment(), false);
			dto = new DataTableOps(td, dbSt);
			Assert.assertTrue(dto.remove());
		}
	}

	/**
	 * Set the name of the experiment.
	 * @param experiment
	 *            name of the experiment.
	 */
	private void setExperiment(final String experiment) {
		registries = new ExperimentRegistries(experiment);
	}

	/**
	 * Verify that a table exists in the database.
	 * @param name
	 *            of the table.
	 * @param channels
	 *            expected channel list.
	 */
	private void check4Table(final String name, final List<String> channels) {
		List<String> expected = new ArrayList<String>();
		DatabaseMetaData databaseMetaData = null;
		try {
			databaseMetaData = pools.fetchConnection(
					registries.getExperiment(), false).getMetaData();
		} catch (SQLException e1) {
			log.error("Metadata query failed ", e1);
			Assert.fail();
		}
		ResultSet result = null;
		try {
			result = databaseMetaData.getTables(null, null, null, null);
		} catch (SQLException e1) {
			log.error("Metadata query failed ", e1);
			Assert.fail();
		}
		boolean found = false;
		try {
			while (result.next()) {
				final int nameColumn = 3;
				String tableName = result.getString(nameColumn);
				log.debug("Checking table name " + tableName + " with " + name);
				if (tableName.equals(name)) {
					found = true;
				}
			}
		} catch (SQLException e1) {
			log.error("Metadata query failed ", e1);
			Assert.fail();
		}
		Assert.assertTrue(found);
		try {
			result = databaseMetaData.getColumns(null, null,
					"\"" + name + "\"", null);
		} catch (SQLException e) {
			log.error("Metadata query failed ", e);
			Assert.fail();
		}
		List<String> tableCols = new ArrayList<String>();
		try {
			while (result.next()) {
				final int nameColumn = 4;
				String colName = result.getString(nameColumn);
				tableCols.add(colName);
			}
		} catch (SQLException e1) {
			log.error("Metadata query failed ", e1);
			Assert.fail();
		}
		CompareLists<String> comparor = new CompareLists<String>();
		comparor.compare(tableCols, expected);
	}
}
