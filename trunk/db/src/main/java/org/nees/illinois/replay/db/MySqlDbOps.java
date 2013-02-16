package org.nees.illinois.replay.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

public class MySqlDbOps implements DbOperationsI {
	private final DbInfo info;
	private final Logger log = LoggerFactory.getLogger(MySqlDbOps.class);

	@Inject
	public MySqlDbOps(DbInfo info) {
		super();
		this.info = info;
	}

	@Override
	public void createDatabase(String experiment) throws Exception {
		Connection connection = generateConnection(false);
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
		} catch (SQLException e) {
			log.error("Create statement failed because ", e);
			closeConnection(connection);
			throw e;
		}
		try {
			stmt.execute("CREATE DATABASE " + experiment);
		} catch (SQLException e) {
			log.error("Create database failed because ", e);
			closeConnection(connection);
			throw e;
		}
		closeConnection(connection);
	}

	@Override
	public String filterUrl(String url, String experiment) {
		return url + experiment;
	}

	@Override
	public Connection generateConnection(boolean withDatabase) throws Exception {
		try {
			Class.forName(info.getDriver());
		} catch (ClassNotFoundException e1) {
			log.error("connection failed because " + info.getDriver()
					+ " could not be found");
			throw e1;
		}
		log.info("info.getDriver() loaded");
		String connect = info.getConnectionUrl();
		if (withDatabase) {
			connect = filterUrl(info.getConnectionUrl(), info.getExperiment());
		}
		log.debug("Connecting to \"" + connect + "\"," + info.getUser() + ","
				+ info.getPasswd());
		Connection connection = null;
		try {
			if (info.getUser() == null) {
				connection = DriverManager.getConnection(connect);
			} else {
				connection = DriverManager.getConnection(connect,
						info.getUser(), info.getPasswd());
			}
		} catch (SQLException e) {
			log.error("connection failed because ", e);
			throw e;
		}
		log.info("connected to database");
		return connection;
	}

	@Override
	public boolean isDatabase(String experiment) throws Exception {
		Connection connection = generateConnection(false);
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
		} catch (SQLException e) {
			log.error("Drop statement failed because ", e);
			throw e;
		}
		ResultSet rs = null;
		boolean result = false;
		try {
			stmt.execute("SHOW DATABASES");
			rs = stmt.getResultSet();
			while (rs.next()) {
				  String dbName = rs.getString(1);    // "TABLE_SCHEM"
				  log.debug("DbName "+ dbName);
				  if(dbName.equals(experiment)) {
					  result = true;
				  }
				}
		} catch (SQLException e) {
			log.error("Drop database failed because ", e);
			closeConnection(connection);
			throw e;
		}
		closeConnection(connection);
		return result;
	}

	@Override
	public void removeDatabase(String experiment) throws Exception {
		Connection connection = generateConnection(false);
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
		} catch (SQLException e) {
			log.error("Drop statement failed because ", e);
			throw e;
		}
		try {
			stmt.execute("DROP DATABASE " + experiment);
		} catch (SQLException e) {
			log.error("Drop database failed because ", e);
			closeConnection(connection);
			throw e;
		}
		closeConnection(connection);
	}

	@Override
	public void closeConnection(Connection connection) throws SQLException {
		try {
			connection.close();
		} catch (SQLException e) {
			log.error("Connection failed to close", e);
			throw e;
		}
	}

	@Override
	public void setExperiment(String experiment) {
		info.setExperiment(experiment);
	}

	@Override
	public String getExperiment() {
		return info.getExperiment();
	}

}
