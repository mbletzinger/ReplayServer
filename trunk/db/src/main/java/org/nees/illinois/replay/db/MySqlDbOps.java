package org.nees.illinois.replay.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

/**
 * Class which implements database operations for the MySQL database.
 * @author Michael Bletzinger
 */
public class MySqlDbOps implements DbOperationsI {
	/**
	 * Parameters for the MySQL database.
	 */
	private final DbInfo info;
	/**
	 * Logger.
	 */
	private final Logger log = LoggerFactory.getLogger(MySqlDbOps.class);

	/**
	 * @param info
	 *            Parameters for the MySQL database.
	 */
	@Inject
	public MySqlDbOps(final DbInfo info) {
		super();
		this.info = info;
	}

	@Override
	public final void createDatabase(final String experiment) throws Exception {
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
		log.info("DB " + experiment + " created");
		closeConnection(connection);
	}

	@Override
	public final String filterUrl(final String url, final String experiment) {
		return url + "\"" + experiment + "\"";
	}

	@Override
	public final Connection generateConnection(final boolean withDatabase)
			throws Exception {
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
	public final boolean isDatabase(final String experiment) throws Exception {
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
				String dbName = rs.getString(1); // "TABLE_SCHEM"
				log.debug("DbName " + dbName);
				if (dbName.equals(experiment)) {
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
	public final void removeDatabase(final String experiment) throws Exception {
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
		log.info("DB " + experiment + " removed");
		closeConnection(connection);
	}

	@Override
	public final void closeConnection(final Connection connection)
			throws SQLException {
		try {
			connection.close();
		} catch (SQLException e) {
			log.error("Connection failed to close", e);
			throw e;
		}
	}

	@Override
	public final void setExperiment(final String experiment) {
		info.setExperiment(experiment);
	}

	@Override
	public final String getExperiment() {
		return info.getExperiment();
	}

}
