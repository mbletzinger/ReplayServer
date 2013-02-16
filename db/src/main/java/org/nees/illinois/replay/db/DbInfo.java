package org.nees.illinois.replay.db;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class DbInfo {
	private final String connectionUrl;

	private final String driver;

	private String experiment;

	private final String passwd;

	private final String user;

	@Inject
	public DbInfo(@Named("dbDriver") String driver,
			@Named("dbUrl") String dbUrl, @Named("dbLogon") String logon,
			@Named("dbPasswd") String passwd,
			@Named("experiment") String experiment) {
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
	public String getConnectionUrl() {
		return connectionUrl;
	}

	/**
	 * @return the driver
	 */
	public String getDriver() {
		return driver;
	}

	/**
	 * @return the experiment
	 */
	public String getExperiment() {
		return experiment;
	}

	/**
	 * @return the passwd
	 */
	public String getPasswd() {
		return passwd;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param experiment
	 *            the experiment to set
	 */
	public void setExperiment(String experiment) {
		this.experiment = experiment;
	}
}
