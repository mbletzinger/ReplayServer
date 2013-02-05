package org.nees.illinois.replay.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.nees.illinois.replay.db.statement.DbStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

public class DbPools {
	private final Map<String, BoneCP> connectionPools = new HashMap<String, BoneCP>();

	private final String dbUrl;

	private final String driver;

	private final DbPoolFilters filters;

	private final Logger log = LoggerFactory.getLogger(DbPools.class);

	private final String logon;
	private final String passwd;

	@Inject
	public DbPools(@Named("dbDriver") String driver,
			@Named("dbUrl") String dbUrl, @Named("dbLogon") String logon,
			@Named("dbPasswd") String passwd, DbPoolFilters filters) {
		this.dbUrl = dbUrl;
		this.driver = driver;
		this.logon = logon.equals("NULL") ? null : logon;
		this.passwd = passwd.equals("NULL") ? null : passwd;
		this.filters = filters;
	}

	public void close() {
		for (BoneCP c : connectionPools.values()) {
			c.shutdown();
		}
	}

	private void createConnection(String experiment) {
		String connectionUrl = filters.filterUrl(dbUrl, experiment);
		try {
			// load the database driver (make sure this is in your classpath!)
			Class.forName(driver);
		} catch (Exception e) {
			log.error("Driver " + driver + " did not load ", e);
			return;
		}
		// setup the connection pool
		BoneCPConfig config = new BoneCPConfig();
		config.setJdbcUrl(connectionUrl); // jdbc url specific to your database,
											// eg jdbc:mysql://127.0.0.1/yourdb
		config.setMinConnectionsPerPartition(5);
		config.setMaxConnectionsPerPartition(10);
		config.setPartitionCount(1);
		if (logon != null) {
			config.setUsername(logon);
			if (passwd != null) {
				config.setPassword(passwd);
			}
		}
		BoneCP pool = null;
		log.debug("Creating pool for \"" + config.getJdbcUrl() + "\"");
		try {
			pool = new BoneCP(config);
		} catch (SQLException e1) {
			log.error("Connection Pool failed to start ", e1);
			return;
		} // setup the connection pool
		connectionPools.put(experiment, pool);
	}

	public DbStatement createDbStatement(String experiment) {
		Connection connection = fetchConnection(experiment);
		if (connection == null) {
			return null;
		}
		return new DbStatement(connection);
	}

	public Connection fetchConnection(String experiment) {
		Connection connection = null;
		if (connectionPools.containsKey(experiment) == false) {
			createConnection(experiment);
		}
		try {
			connection = connectionPools.get(experiment).getConnection();
		} catch (SQLException e1) {
			log.error("getConnection failed because ", e1);
		} // fetch a connection
		return connection;
	}

	/**
	 * @return the dbUrl
	 */
	public String getDbUrl() {
		return dbUrl;
	}

	/**
	 * @return the driver
	 */
	public String getDriver() {
		return driver;
	}

	/**
	 * @return the filters
	 */
	public DbPoolFilters getFilters() {
		return filters;
	}

	/**
	 * @return the logon
	 */
	public String getLogon() {
		return logon;
	}

	/**
	 * @return the passwd
	 */
	public String getPasswd() {
		return passwd;
	}

}
