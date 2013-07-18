package org.nees.illinois.replay.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

/**
 * Class which implements the database operations for the Derby database.
 * @author Michael Bletzinger
 */
public class DerbyDbOps implements DbOperationsI {
	/**
	 * Flag that is true if an SQL exception was received.
	 */
	private boolean gotSQLExc = false;
	/**
	 * Parameters of the Derby database.
	 */
	private final DbInfo info;
	/**
	 * Logger.
	 */
	private final Logger log = LoggerFactory.getLogger(DerbyDbOps.class);

	/**
	 * @param info
	 *            Parameters of the Derby database.
	 */
	@Inject
	public DerbyDbOps(final DbInfo info) {
		super();
		this.info = info;
	}

	@Override
	public final void closeConnection(final Connection connection)
			throws Exception {
		try {
			DriverManager.getConnection(info.getConnectionUrl()
					+ ";shutdown=true");
		} catch (SQLException se) {
			if (se.getSQLState().equals("XJ015")) {
				gotSQLExc = true;
			}
		}
		if (!gotSQLExc) {
			log.error("Database did not shut down normally");
			throw new SQLException();
		} else {
			log.info("Database shut down normally");
		}
	}

	@Override
	public void createDatabase(final String experiment) {

	}

	@Override
	public final String filterUrl(final String url, final String experiment) {
		return url + experiment + ";create=true";
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
	public final String getExperiment() {
		return info.getExperiment();
	}

	@Override
	public final boolean isDatabase(final String experiment) {
		return true;
	}

	@Override
	public void removeDatabase(final String experiment) {
	}

	@Override
	public final void setExperiment(final String experiment) {
		info.setExperiment(experiment);
	}
}
