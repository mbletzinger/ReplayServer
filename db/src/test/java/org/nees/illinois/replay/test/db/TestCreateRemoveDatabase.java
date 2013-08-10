package org.nees.illinois.replay.test.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.nees.illinois.replay.db.DbInfo;
import org.nees.illinois.replay.db.DbOperationsI;
import org.nees.illinois.replay.db.DbPools;
import org.nees.illinois.replay.test.db.derby.process.DerbyDbControl;
import org.nees.illinois.replay.test.db.utils.DbTestsModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

/**
 * Class which tests database operations.
 * @author Michael Bletzinger
 */
@Test(groups = { "create-db" })
public class TestCreateRemoveDatabase {
	/**
	 * Connection URL to the database.
	 */
	private String connectionUrl;
	/**
	 * Operations to be tested.
	 */
	private DbOperationsI dbm;
	/**
	 * Additional controls for Derby so that it cleans up after itself.
	 */
	private final DerbyDbControl ddbc = new DerbyDbControl();
	/**
	 * JDBC wrapper for the database.
	 */
	private String driver;
	/**
	 * Experiment name.
	 */
	private String experiment;
	/**
	 * Flag that is true if we are testing MySQL.
	 */
	private boolean ismysql;
	/**
	 * Logger.
	 */
	private final Logger log = LoggerFactory
			.getLogger(TestCreateRemoveDatabase.class);
	/**
	 * Password to use for the database connection.
	 */
	private String passwd;
	/**
	 * Account name to use for the database connection.
	 */
	private String user;

	/**
	 * Tests if a database can be created.
	 * @throws Exception
	 *             If there are any problems.
	 */
	@Test
	public final void createDatabase() throws Exception {
		Connection connection = null;
		if (ismysql) {
			dbm.createDatabase(experiment);
		}
		dbm.setExperiment(experiment);
		connection = dbm.generateConnection(true);
		testWithStatement(connection);
		dbm.closeConnection(connection);
		if (ismysql) {
			dbm.removeDatabase(experiment);
		}
	}

	/**
	 * Tests if a pool can be created.
	 */
	@Test
	public final void createDatabasePool() {
		try {
			// load the database driver (make sure this is in your classpath!)
			Class.forName(driver);
		} catch (Exception e) {
			log.error("Driver " + driver + " did not load ", e);
			Assert.fail();
			return;
		}
		Connection connection = null;
		if (ismysql) {
			try {
				dbm.createDatabase(experiment);
			} catch (Exception e) {
				log.error("Create database failed because ", e);
				Assert.fail();
			}
		}
		// setup the connection pool
		BoneCPConfig config = new BoneCPConfig();
		config.setJdbcUrl(dbm.filterUrl(connectionUrl, experiment)); // jdbc
																		// url
		// specific to
		// your database,
		// eg jdbc:mysql://127.0.0.1/yourdb
		final int minConnectionsPerPartition = 5;
		final int maxConnectionsPerPartition = 10;
		config.setMinConnectionsPerPartition(minConnectionsPerPartition);
		config.setMaxConnectionsPerPartition(maxConnectionsPerPartition);
		config.setPartitionCount(1);
		if (user != null) {
			config.setUsername(user);
			config.setPassword(passwd);
		}
		BoneCP connectionPool;
		try {
			connectionPool = new BoneCP(config);
		} catch (SQLException e1) {
			log.error("Connection Pool failed to start ", e1);
			Assert.fail();
			return;
		} // setup the connection pool

		try {
			connection = connectionPool.getConnection();
		} catch (SQLException e1) {
			log.error("getConnection failed because ", e1);
			Assert.fail();
		} // fetch a connection

		if (connection != null) {
			log.info("Connection successful!");
			testWithStatement(connection);
		}
		connectionPool.shutdown(); // shutdown connection pool.
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				log.error("shutdown failed because ", e);
				Assert.fail();
			}
		}
		if (ismysql) {
			try {
				dbm.removeDatabase(experiment);
			} catch (Exception e) {
				log.error("Database removal failed because ", e);
				Assert.fail();
			}
		}
	}

	/**
	 * Determines what type of database program we are testing and sets up the
	 * test parameters accordingly.
	 * @throws Exception
	 *             If any of the preparations failed.
	 */
	@BeforeMethod
	public final void setUp()
	// public final void setUp(@Optional("mysql") final String db)
			throws Exception {
		DbTestsModule guiceMod = new DbTestsModule(ismysql ? "mysql" : "derby");
		experiment = "Hybrid Masonry #1";
		Injector injector = Guice.createInjector(guiceMod);
		DbPools pools = injector.getInstance(DbPools.class);
		DbInfo info = pools.getInfo();
		dbm = pools.getOps();
		driver = info.getDriver();
		connectionUrl = info.getConnectionUrl();
		user = info.getUser();
		passwd = info.getPasswd();
	}

	/**
	 * Determines the database application used and does extra stuff for derby.
	 * @param db
	 *            TestNG parameter for telling which database program to use.
	 */
	@Parameters("db")
	@BeforeClass
	public final void startDb(@Optional("derby") final String db) {
		ismysql = db.equals("mysql");
		if (ismysql == false) {
			ddbc.startDerby();
		}
	}

	/**
	 * Tell the Derby database to give up if that is the database we are
	 * testing.
	 */
	@AfterClass
	public final void teardown() {
		if (ismysql == false) {
			ddbc.stopDerby();
		}
	}

	/**
	 * Try an SQL statement.
	 * @param connection
	 *            JDBC connection to use to execute the statement.
	 */
	private void testWithStatement(final Connection connection) {
		Statement stmt = null;
		final String table = "TestTable";
		try {
			stmt = connection.createStatement();
		} catch (SQLException e) {
			log.error("Create statement failed because ", e);
			Assert.fail();
		}
		try {
			stmt.execute("CREATE TABLE " + table
					+ " (col1 double, col2 double)");
		} catch (SQLException e) {
			log.error("Create table failed because ", e);
			Assert.fail();
		} // do something with the connection.
		DatabaseMetaData databaseMetaData = null;
		try {
			databaseMetaData = connection.getMetaData();
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
				log.debug("Checking table name " + tableName);
				if (tableName.equals(table.toUpperCase())) {
					found = true;
				}
			}
		} catch (SQLException e1) {
			log.error("Metadata query failed ", e1);
			Assert.fail();
		}
		Assert.assertTrue(found);
		try {
			stmt.execute("DROP TABLE TestTable");
		} catch (SQLException e) {
			log.error("Remove table failed because ", e);
			Assert.fail();
		} // do something with the connection.

	}

}
