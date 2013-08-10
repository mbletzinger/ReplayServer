package org.nees.illinois.replay.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.nees.illinois.replay.db.statement.StatementProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

/**
 * Class which manages a set of Bone Connection Pools. Information found at
 * http://jolbox.com
 * @author Michael Bletzinger
 */
@Singleton
public class DbPools {
	/**
	 * Map of pools for each experiment.
	 */
	private final Map<String, BoneCP> connectionPools = new HashMap<String, BoneCP>();
	/**
	 * Information of the database being used.
	 */
	private final DbInfo info;
	/**
	 * Logger.
	 */
	private final Logger log = LoggerFactory.getLogger(DbPools.class);
	/**
	 * Operations for the database.
	 */
	private final DbOperationsI ops;

	/**
	 * @param info
	 *            Information of the database being used.
	 * @param filters
	 *            Operations for the database.
	 */
	@Inject
	public DbPools(final DbInfo info, final DbOperationsI filters) {
		this.info = info;
		this.ops = filters;
	}

	/**
	 * Close all of the pools.
	 */
	public final void close() {
		for (BoneCP c : connectionPools.values()) {
			c.shutdown();
		}
		connectionPools.clear();
	}

	/**
	 * Open a connection for an experiment database.
	 * @param experiment
	 *            Name of the experiment.
	 * @param createDb
	 *            True of if the database should be created if it does not
	 *            exist.
	 */
	private void createConnection(final String experiment,
			final boolean createDb) {
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
		final int minConnectionsPerPartition = 5;
		final int maxConnectionsPerPartition = 10;

		config.setMinConnectionsPerPartition(minConnectionsPerPartition);
		config.setMaxConnectionsPerPartition(maxConnectionsPerPartition);
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

	/**
	 * Open a connection to the database associated with the experiment and
	 * return a statement processor.
	 * @param experiment
	 *            Name of the experiment.
	 * @param createDb
	 *            True of if the database should be created if it does not
	 *            exist.
	 * @return the statement processor.
	 */
	public final StatementProcessor createDbStatement(final String experiment,
			final boolean createDb) {
		Connection connection = fetchConnection(experiment, createDb);
		if (connection == null) {
			log.equals("No connection received");
			return null;
		}
		return new StatementProcessor(connection);
	}

	/**
	 * Open a connection to the database associated with the experiment and
	 * return a JDBC connection reference.
	 * @param experiment
	 *            Name of the experiment.
	 * @param createDb
	 *            True of if the database should be created if it does not
	 *            exist.
	 * @return the connection.
	 */
	public final Connection fetchConnection(final String experiment,
			final boolean createDb) {
		Connection connection = null;
		if (connectionPools.containsKey(experiment) == false) {
			createConnection(experiment, createDb);
		}
		try {
			BoneCP pool = connectionPools.get(experiment);
			connection = pool.getConnection();
		} catch (SQLException e1) {
			log.error("getConnection failed because ", e1);
		} // fetch a connection
		log.debug("Connection fetched for " + experiment);
		return connection;
	}

	/**
	 * @return the info
	 */
	public final DbInfo getInfo() {
		return info;
	}

	/**
	 * @return the ops
	 */
	public final DbOperationsI getOps() {
		return ops;
	}

}
