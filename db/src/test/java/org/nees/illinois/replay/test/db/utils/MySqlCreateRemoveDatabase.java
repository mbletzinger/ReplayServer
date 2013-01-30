package org.nees.illinois.replay.test.db.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.nees.illinois.replay.db.DbPools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

public class MySqlCreateRemoveDatabase implements DbManagement {
	protected final String connectionUrl;
	protected final String driver;
	protected final String experiment;
	private final Logger log = LoggerFactory
			.getLogger(MySqlCreateRemoveDatabase.class);
	protected final String passwd;
	protected final String user;

	public MySqlCreateRemoveDatabase(DbPools pools, String experiment) {
		driver = pools.getDriver();
		connectionUrl = pools.getDbUrl();
		user = pools.getLogon();
		passwd = pools.getPasswd();
		this.experiment = experiment;
	}

	/* (non-Javadoc)
	 * @see org.nees.illinois.replay.test.db.utils.DbManagement#closeConnection(java.sql.Connection)
	 */
	@Override
	public void closeConnection(Connection connection) {
		try {
			connection.close();
		} catch (SQLException e) {
			log.error("Connection failed to close", e);
			Assert.fail();
		}
	}

	/* (non-Javadoc)
	 * @see org.nees.illinois.replay.test.db.utils.DbManagement#createDatabase(java.sql.Connection)
	 */
	@Override
	public void createDatabase(Connection connection) {
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
		} catch (SQLException e) {
			log.error("Create statement failed because ", e);
			Assert.fail();
		}
		try {
			stmt.execute("CREATE DATABASE " + experiment);
		} catch (SQLException e) {
			log.error("Create database failed because ", e);
			Assert.fail();
		}
	}

	/* (non-Javadoc)
	 * @see org.nees.illinois.replay.test.db.utils.DbManagement#generateConnection(boolean)
	 */
	@Override
	public Connection generateConnection(boolean withDatabase) {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e1) {
			log.error("connection failed because " + driver
					+ " could not be found");
			Assert.fail();
		}
		log.info("driver loaded");
		String connect = connectionUrl;
		if (withDatabase) {
			connect += experiment;
		}
		log.debug("Connecting to \"" + connect + "\"," + user + "," + passwd);
		Connection connection = null;
		try {
			if (user == null) {
				connection = DriverManager.getConnection(connect);
			} else {
				connection = DriverManager.getConnection(connect, user, passwd);
			}
		} catch (SQLException e) {
			log.error("connection failed because ", e);
			Assert.fail();
		}
		log.info("connected to database");
		return connection;
	}

	@Override
	public String getExperiment() {
return experiment;	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.nees.illinois.replay.test.db.utils.DbManagement#removeDatabase(java
	 * .sql.Connection)
	 */
	@Override
	public void removeDatabase(Connection connection) {
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
		} catch (SQLException e) {
			log.error("Drop statement failed because ", e);
			Assert.fail();
		}
		try {
			stmt.execute("DROP DATABASE " + experiment);
		} catch (SQLException e) {
			log.error("Drop database failed because ", e);
			Assert.fail();
		}
	}

}
