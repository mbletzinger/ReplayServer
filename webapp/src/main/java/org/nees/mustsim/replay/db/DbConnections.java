package org.nees.mustsim.replay.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.nees.mustsim.replay.db.statement.DbStatement;
import org.nees.mustsim.replay.db.statement.DbTableSpecs;
import org.nees.mustsim.replay.db.statement.RateType;
import org.nees.mustsim.replay.db.statement.TableType;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

public class DbConnections {
	private BoneCP connectionPool;
	private final Logger log = Logger.getLogger(DbConnections.class);
	private final boolean resetTables;

	public Connection fetchConnection() {
		Connection connection = null;
		try {
			connection = connectionPool.getConnection();
		} catch (SQLException e1) {
			log.error("getConnection failed because ", e1);
		} // fetch a connection
		return connection;
	}
	public DbStatement createDbStatement() {
		try {
			return new DbStatement(connectionPool.getConnection());
		} catch (SQLException e) {
			log.error("Connection fetch failed ", e);
			return null;
		}
	}
		
	public DbConnections(String driver, String dbName, String dbUrl, boolean resetTables) {
		String connectionUrl = dbUrl + dbName;
		this.resetTables = resetTables;
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
		try {
			connectionPool = new BoneCP(config);
		} catch (SQLException e1) {
			log.error("Connection Pool failed to start ", e1);
			return;
		} // setup the connection pool
	}
	public void close() {
		connectionPool.shutdown();
	}
}
