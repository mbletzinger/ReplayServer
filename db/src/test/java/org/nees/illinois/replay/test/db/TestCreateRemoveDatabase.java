package org.nees.illinois.replay.test.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.AfterClass;
import org.nees.illinois.replay.db.DbInfo;
import org.nees.illinois.replay.db.DbOperationsI;
import org.nees.illinois.replay.db.DbPools;
import org.nees.illinois.replay.test.db.derby.process.DerbyDbControl;
import org.nees.illinois.replay.test.db.utils.DbTestsModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

public class TestCreateRemoveDatabase {
	private String connectionUrl;
	private DbOperationsI dbm;
	private final DerbyDbControl ddbc = new DerbyDbControl();
	private String driver;
	private String experiment;
	private boolean ismysql;
	private final Logger log = LoggerFactory
			.getLogger(TestCreateRemoveDatabase.class);
	private String passwd;
	private String user;

	@Test
	public void createDatabase() throws Exception {
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

	@Test
	public void createDatabasePool() {
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
		config.setMinConnectionsPerPartition(5);
		config.setMaxConnectionsPerPartition(10);
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

	@Parameters("db")
	@BeforeClass
	public void setUp(@Optional("derby") String db) throws Exception {
		DbTestsModule guiceMod = new DbTestsModule(db);
		experiment = "HybridMasonry1";
		guiceMod.setExperiment(experiment);
		Injector injector = Guice.createInjector(guiceMod);
		DbPools pools = injector.getInstance(DbPools.class);
		DbInfo info = pools.getInfo();
		dbm = pools.getOps();
		driver = info.getDriver();
		connectionUrl = info.getConnectionUrl();
		user = info.getUser();
		passwd = info.getPasswd();
		ismysql = db.equals("mysql");
		if (ismysql == false) {
			ddbc.startDerby();
		}
	}

	@AfterClass
	public void teardown() {
		if (ismysql == false) {
			ddbc.stopDerby();
		}
	}

	private void testWithStatement(Connection connection) {
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

}
