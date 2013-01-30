package org.nees.illinois.replay.test.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.nees.illinois.replay.db.DbPools;
import org.nees.illinois.replay.test.db.utils.DbManagement;
import org.nees.illinois.replay.test.db.utils.DbTestsModule;
import org.nees.illinois.replay.test.db.utils.DerbyCreateRemoveDatabase;
import org.nees.illinois.replay.test.db.utils.MySqlCreateRemoveDatabase;
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
	private final Logger log = Logger
			.getLogger(TestCreateRemoveDatabase.class);
	private String driver;
	private String connectionUrl;
	private String user;
	private String passwd;
	private String experiment;
	private DbManagement dbm;
	private boolean ismysql;

	@Parameters("db")
	@BeforeClass
	public void setUp(@Optional("derby") String db) throws Exception {
		DbTestsModule guiceMod = new DbTestsModule(db);
		experiment = "HybridMasonry1";
		guiceMod.setExperiment(experiment);
		Injector injector = Guice.createInjector(guiceMod);
		DbPools pools = injector.getInstance(DbPools.class);
		driver = pools.getDriver();
		connectionUrl = pools.getDbUrl();
		user = pools.getLogon();
		passwd = pools.getPasswd();
		ismysql = db.equals("mysql");
		if (ismysql) {
			dbm = new MySqlCreateRemoveDatabase(pools, experiment);
		} else {
			dbm = new DerbyCreateRemoveDatabase(pools, experiment);
		}
	}

	@Test
	public void createDatabase() {
		Connection connection = null;
		if (ismysql) {
			connection = dbm.generateConnection(false);
			dbm.createDatabase(connection);
			dbm.closeConnection(connection);
		}
		connection = dbm.generateConnection(true);
		testWithStatement(connection);
		dbm.closeConnection(connection);
		if (ismysql) {
			connection = dbm.generateConnection(false);
			dbm.removeDatabase(connection);
			dbm.closeConnection(connection);
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
			connection = dbm.generateConnection(false);
			dbm.createDatabase(connection);
			dbm.closeConnection(connection);
		}
		// setup the connection pool
		BoneCPConfig config = new BoneCPConfig();
		config.setJdbcUrl(connectionUrl + dbm.getExperiment()); // jdbc url specific to
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
				e.printStackTrace();
			}
		}
		if (ismysql) {
			connection = dbm.generateConnection(false);
			dbm.removeDatabase(connection);
			dbm.closeConnection(connection);
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
