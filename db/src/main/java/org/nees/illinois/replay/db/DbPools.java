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

	private final DbInfo info;

	private final Logger log = LoggerFactory.getLogger(DbPools.class);

	private final DbOperationsI ops;

	@Inject
	public DbPools(DbInfo info, DbOperationsI filters) {
		this.info = info;
		this.ops = filters;
	}

	public void close() {
		for (BoneCP c : connectionPools.values()) {
			c.shutdown();
		}
		connectionPools.clear();
	}

	private void createConnection(String experiment, boolean createDb) {
		String connectionUrl = ops.filterUrl(info.getConnectionUrl(),
				experiment);
		try {
			// load the database driver (make sure this is in your classpath!)
			Class.forName(info.getDriver());
		} catch (Exception e) {
			log.error("Driver " + info.getDriver() + " did not load ", e);
			return;
		}
		if (createDb) {
			try {
				boolean exist = ops.isDatabase(experiment);
				if (exist == false) {
					ops.createDatabase(experiment);
				}
			} catch (Exception e) {
				log.error("Could not create db " + experiment + " because ", e);
				return;
			}
		}
		// setup the connection pool
		BoneCPConfig config = new BoneCPConfig();
		config.setJdbcUrl(connectionUrl); // jdbc url specific to your database,
											// eg jdbc:mysql://127.0.0.1/yourdb
		config.setMinConnectionsPerPartition(5);
		config.setMaxConnectionsPerPartition(10);
		config.setPartitionCount(1);
		if (info.getUser() != null) {
			config.setUsername(info.getUser());
			if (info.getPasswd() != null) {
				config.setPassword(info.getPasswd());
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

	public DbStatement createDbStatement(String experiment, boolean createDb) {
		Connection connection = fetchConnection(experiment, createDb);
		if (connection == null) {
			return null;
		}
		return new DbStatement(connection);
	}

	public Connection fetchConnection(String experiment, boolean createDb) {
		Connection connection = null;
		if (connectionPools.containsKey(experiment) == false) {
			createConnection(experiment, createDb);
		}
		try {
			connection = connectionPools.get(experiment).getConnection();
		} catch (SQLException e1) {
			log.error("getConnection failed because ", e1);
		} // fetch a connection
		return connection;
	}

	/**
	 * @return the info
	 */
	public DbInfo getInfo() {
		return info;
	}

	/**
	 * @return the ops
	 */
	public DbOperationsI getOps() {
		return ops;
	}

}
