package org.nees.illinois.replay.test.db;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

public class TestCreateRemoveDatabase {
	private final Logger log = Logger.getLogger(TestCreateRemoveDatabase.class);

	@Test
	public void createDatabase() {
		String driver = "org.apache.derby.jdbc.ClientDriver";
		String dbName = "HybridMasonry1";
		String connectionURL = "jdbc:derby://localhost:1527/" + dbName + ";create=true";
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e1) {
			log.error("connection failed because " + driver
					+ " could not be found");
			AssertJUnit.fail();
		}
		log.info("driver loaded");
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connectionURL);
		} catch (SQLException e) {
			log.error("connection failed because ",e);
			AssertJUnit.fail();
		}
		
		log.info("connected to database");
		testWithStatement(connection);
		
		boolean gotSQLExc = false;
		try {
			DriverManager.getConnection("jdbc:derby:;shutdown=true");
		} catch (SQLException se) {
			if (se.getSQLState().equals("XJ015")) {
				gotSQLExc = true;
			}
		}
		if (!gotSQLExc) {
			log.error("Database did not shut down normally");
			AssertJUnit.fail();
		} else {
			log.info("Database shut down normally");
		}

	}

	@Test
	public void createDatabasePool() {
		String driver = "org.apache.derby.jdbc.ClientDriver";
		String dbName = "HybridMasonry1";
		String connectionURL = "jdbc:derby://localhost:1527/" + dbName;
		try {
			// load the database driver (make sure this is in your classpath!)
			Class.forName(driver);
		} catch (Exception e) {
			log.error("Driver " + driver + " did not load ", e);
			AssertJUnit.fail();
			return;
		}
		Connection connection = null;
		// setup the connection pool
		BoneCPConfig config = new BoneCPConfig();
		config.setJdbcUrl(connectionURL); // jdbc url specific to your database,
											// eg jdbc:mysql://127.0.0.1/yourdb
		config.setMinConnectionsPerPartition(5);
		config.setMaxConnectionsPerPartition(10);
		config.setPartitionCount(1);
		BoneCP connectionPool;
		try {
			connectionPool = new BoneCP(config);
		} catch (SQLException e1) {
			log.error("Connection Pool failed to start ", e1);
			AssertJUnit.fail();
			return;
		} // setup the connection pool

		try {
			connection = connectionPool.getConnection();
		} catch (SQLException e1) {
			log.error("getConnection failed because ", e1);
			AssertJUnit.fail();
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
	}

	@Test(enabled = false)
	private void testWithStatement(Connection connection) {
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
		} catch (SQLException e) {
			log.error("Create statement failed because ", e);
			AssertJUnit.fail();
		}
		try {
			stmt.execute("CREATE TABLE TestTable (col1 double, col2 double)");
		} catch (SQLException e) {
			log.error("Create table failed because ", e);
			AssertJUnit.fail();
		} // do something with the connection.
		try {
			stmt.execute("DROP TABLE TestTable");
		} catch (SQLException e) {
			log.error("Remove table failed because ", e);
			AssertJUnit.fail();
		} // do something with the connection.
		
	}	
}
