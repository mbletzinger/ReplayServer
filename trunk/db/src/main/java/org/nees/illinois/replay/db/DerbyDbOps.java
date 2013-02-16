package org.nees.illinois.replay.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

public class DerbyDbOps implements DbOperationsI {
	boolean gotSQLExc = false;
	private final DbInfo info;
	private final Logger log = LoggerFactory.getLogger(DerbyDbOps.class);

	@Inject
	public DerbyDbOps(DbInfo info) {
		super();
		this.info = info;
	}

	@Override
	public void closeConnection(Connection connection) throws Exception {
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
	public void createDatabase(String experiment) {

	}

	@Override
	public String filterUrl(String url, String experiment) {
		return url + experiment + ";create=true";
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
	public String getExperiment() {
		return info.getExperiment();
	}

	@Override
	public boolean isDatabase(String experiment) {
		return true;
	}

	@Override
	public void removeDatabase(String experiment) {
	}

	@Override
	public void setExperiment(String experiment) {
		info.setExperiment(experiment);
	}
}
