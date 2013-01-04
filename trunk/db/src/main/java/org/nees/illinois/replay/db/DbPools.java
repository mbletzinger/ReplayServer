package org.nees.illinois.replay.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.nees.illinois.replay.db.statement.DbStatement;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

public abstract class DbPools {
	private final Map<String, BoneCP> connectionPools = new HashMap<String, BoneCP>();
	private final Logger log = Logger.getLogger(DbPools.class);
	private final String dbUrl;
	private final String driver;
	private final String logon;
	private final String passwd;

	public Connection fetchConnection(String experiment) {
		Connection connection = null;
		if(connectionPools.containsKey(experiment) == false) {
			createConnection(experiment);
		}
		try {
			connection = connectionPools.get(experiment).getConnection();
		} catch (SQLException e1) {
			log.error("getConnection failed because ", e1);
		} // fetch a connection
		return connection;
	}

	public DbStatement createDbStatement(String experiment) {
		Connection connection = fetchConnection(experiment);
		if(connection == null) {
			return null;
		}
		return new DbStatement(connection);
	}

	public DbPools(String driver, String dbUrl, String logon, String passwd) {
		this.dbUrl = dbUrl;
		this.driver = driver;
		this.logon = logon;
		this.passwd = passwd;
	}

	private void createConnection(String experiment) {
		String connectionUrl = filterUrl(dbUrl, experiment);
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
		if(logon != null) {
			config.setUsername(logon);
			if(passwd != null) {
				config.setPassword(passwd);
			}
		}
		BoneCP pool = null;
		try {
			pool = new BoneCP(config);
		} catch (SQLException e1) {
			log.error("Connection Pool failed to start ", e1);
			return;
		} // setup the connection pool
		connectionPools.put(experiment, pool);
	}

	public void close() {
		for (BoneCP c : connectionPools.values()) {
			c.shutdown();
		}
	}
	public abstract String filterUrl(String url, String experiment); 
}
