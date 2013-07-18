package org.nees.illinois.replay.test.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.db.DbOperationsI;
import org.nees.illinois.replay.db.DbPools;
import org.nees.illinois.replay.test.db.derby.process.DerbyDbControl;
import org.nees.illinois.replay.test.db.utils.DbTestsModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Class to see if the database pools work.
 * @author Michael Bletzinger
 */
@Test(groups = { "db-pools" })
public class TestDbPools {
	/**
	 * Extra controls for Derby.
	 */
	private final DerbyDbControl ddbc = new DerbyDbControl();
	/**
	 * GUICE module for injecting database parameters.
	 */
	private DbTestsModule guiceMod;
	/**
	 * Flag that is true if we are using MySQL.
	 */
	private boolean ismysql;
	/**
	 * Our pools that are being tested.
	 */
	private DbPools pools;
	/**
	 * Logger.
	 */
	private final Logger log = LoggerFactory.getLogger(TestDbPools.class);
	/**
	 * List of experiment names to try.
	 */
	private List<String> dbNames = new ArrayList<String>();

	/**
	 * Prepare for submerged examination in the pools.
	 */
	@Test
	public final void testDbPools() {
		if (ismysql) {
			checkExistence(false);
		}
		for (String experiment : dbNames) {
			Connection connection = testDbCreate(experiment);
			try {
				connection.close();
			} catch (SQLException e) {
				log.error("Failed to close connection to  " + experiment, e);
				Assert.fail();
			}
		}
		checkExistence(true);
		for (String experiment : dbNames) {
			Connection connection = pools.fetchConnection(experiment, false);
			testWithStatement(connection);
			try {
				connection.close();
			} catch (SQLException e) {
				log.error("Failed to close connection to  " + experiment, e);
				Assert.fail();
			}
		}
		for (String exp : dbNames) {
			try {
				log.debug("Removing db " + exp);
				pools.getOps().removeDatabase(exp);
			} catch (Exception e) {
				log.error("Failed to remove db " + exp, e);
				Assert.fail();
			}
		}
		pools.close();
		if (ismysql) {
			checkExistence(false);
		}
		for (String experiment : dbNames) {
			Connection connection = pools.fetchConnection(experiment, true);
			testWithStatement(connection);
			try {
				connection.close();
			} catch (SQLException e) {
				log.error("Failed to close connection to  " + experiment, e);
				Assert.fail();
			}
		}
		checkExistence(true);
	}

	/**
	 * Set up the test for the particular database.
	 * @param db
	 *            label of the database to use for the test.
	 */
	@Parameters("db")
	@BeforeClass
	public final void beforeClass(@Optional("derby") final String db) {
		dbNames.add("BeamColumn30Percent");
		dbNames.add("CABERPier1");
		dbNames.add("BeamColumn40Percent");
		dbNames.add("CABER3Pier");
		dbNames.add("ComplexWall2");
		guiceMod = new DbTestsModule(db);
		ismysql = db.equals("mysql");
		if (ismysql == false) {
			ddbc.startDerby();
		}

		Injector injector = Guice.createInjector(guiceMod);
		pools = injector.getInstance(DbPools.class);

	}

	/**
	 * Clean up and shut down the databases.
	 */
	@AfterClass
	public final void afterClass() {
		for (String exp : dbNames) {
			try {
				log.debug("Removing db " + exp);
				pools.getOps().removeDatabase(exp);
			} catch (Exception e) {
				log.error("Failed to remove db " + exp, e);
				Assert.fail();
			}
		}
		if (ismysql == false) {
			ddbc.stopDerby();
		}
	}

	/**
	 * Test if we can create an experiment.
	 * @param experiment
	 *            Name of experiment to create.
	 * @return The connection.
	 */
	private Connection testDbCreate(final String experiment) {
		try {
			pools.getOps().createDatabase(experiment);
		} catch (Exception e) {
			log.error("Failed to create db " + experiment, e);
			Assert.fail();
		}
		return pools.fetchConnection(experiment, false);
	}

	/**
	 * Execute a statement.
	 * @param connection
	 *            used to execute the statement.
	 */
	private void testWithStatement(final Connection connection) {
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
		} catch (SQLException e) {
			log.error("Create statement failed because ", e);
			Assert.fail();
		}
		try {
			stmt.execute("CREATE TABLE TestTable (col1 double, col2 double)");
		} catch (SQLException e) {
			log.error("Create table failed because ", e);
			Assert.fail();
		} // do something with the connection.
		try {
			stmt.execute("DROP TABLE TestTable");
		} catch (SQLException e) {
			log.error("Remove table failed because ", e);
			Assert.fail();
		} // do something with the connection.

	}

	/**
	 * Check for the existance of a database.
	 * @param shouldExist
	 *            True if the database is supposed to exist.
	 */
	private void checkExistence(final boolean shouldExist) {
		for (String experiment : dbNames) {
			DbOperationsI dbo = pools.getOps();
			boolean exists = false;
			try {
				exists = dbo.isDatabase(experiment);
			} catch (Exception e) {
				log.error("Failed to check db server for  " + experiment, e);
				Assert.fail();
			}
			Assert.assertEquals(exists, shouldExist);
		}

	}

}
