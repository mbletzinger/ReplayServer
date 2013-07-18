package org.nees.illinois.replay.db;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Class which contains the JDBC parameters that are required to connect to a
 * database.
 * @author Michael Bletzinger
 */
public class DbInfo {
	/**
	 * URL to the database.
	 */
	private final String connectionUrl;
	/**
	 * Java class which acts as the JDBC wrapper for the database.
	 */
	private final String driver;
	/**
	 * Initial data base name to start the connection.
	 */
	private String experiment;

	/**
	 * Password for the account to access the database connection.
	 */
	private final String passwd;
	/**
	 * Account name to access the database connection.
	 */
	private final String user;

	/**
	 * @param driver
	 *            Java class which acts as the JDBC wrapper for the database.
	 * @param dbUrl
	 *            URL to the database
	 * @param logon
	 *            Account name to access the database connection.
	 * @param passwd
	 *            Password for the account to access the database connection.
	 * @param experiment
	 *            Initial data base name to start the connection.
	 */
	@Inject
	public DbInfo(@Named("dbDriver") final String driver,
			@Named("dbUrl") final String dbUrl,
			@Named("dbLogon") final String logon,
			@Named("dbPasswd") final String passwd,
			@Named("experiment") final String experiment) {
		super();
		this.driver = driver;
		this.connectionUrl = dbUrl;
		this.user = logon.equals("NULL") ? null : logon;
		this.passwd = passwd.equals("NULL") ? null : passwd;
		this.experiment = experiment;
	}

	/**
	 * @return the connectionUrl
	 */
	public final String getConnectionUrl() {
		return connectionUrl;
	}

	/**
	 * @return the driver
	 */
	public final String getDriver() {
		return driver;
	}

	/**
	 * @return the experiment
	 */
	public final String getExperiment() {
		return experiment;
	}

	/**
	 * @return the passwd
	 */
	public final String getPasswd() {
		return passwd;
	}

	/**
	 * @return the user
	 */
	public final String getUser() {
		return user;
	}

	/**
	 * @param experiment
	 *            the experiment to set
	 */
	public final void setExperiment(final String experiment) {
		this.experiment = experiment;
	}
}
