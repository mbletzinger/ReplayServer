package org.nees.mustsim.replay.db.statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public abstract class PrepStatement {
	protected PreparedStatement statement;
	protected final Logger log;
	private final String prepped;
	public int[] execute() {
		int [] result = null;
		try {
			result = statement.executeBatch();
		} catch (SQLException e) {
			log.error("Execute prep statement \"" + prepped + "\" failed because",e);
			return result;
		}
		try {
			statement.close();
		} catch (SQLException e) {
			log.error("Closing prep statement \"" + prepped + "\" failed because",e);
			return result;
		}
		
		return result;
	}
	public PrepStatement(String prepped, Logger log) {
		super();
		this.prepped = prepped;
		this.log = log;
		log.debug("Prep statement is " + prepped);
	}
	public boolean create(Connection connection) {
		try {
			statement = connection.prepareStatement(prepped);
		} catch (SQLException e) {
			log.error("Create prep statement \"" + prepped + "\" failed because",e);
			return false;
		}
		return true;
	}
}
