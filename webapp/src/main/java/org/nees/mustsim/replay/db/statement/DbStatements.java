package org.nees.mustsim.replay.db.statement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import junit.framework.Assert;

import org.apache.log4j.Logger;

public class DbStatements {
	private final Connection connection;
	private final Logger log = Logger.getLogger(DbStatements.class);

	public DbStatements(Connection connection) {
		super();
		this.connection = connection;
	}

	public boolean createPrepStatement(PrepStatement prep) {
		return prep.create(connection);
	}

	public boolean execute(String statement) {
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
		} catch (SQLException e) {
			log.error("Create statement \"" + statement + "\" failed because ",
					e);
			return false;
		}
		try {
			stmt.execute(statement);
		} catch (SQLException e) {
			log.error("Execute of \"" + statement + "\" failed because ", e);
			return false;
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					log.error("Close of \"" + statement + "\" failed because ",
							e);
					return false;
				}
			}
		}
		log.info("executed \"" + statement + "\"");
		return true;

	}

	public ResultSet query(String statement) {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.createStatement();
		} catch (SQLException e) {
			log.error("Create statement \"" + statement + "\" failed because ",
					e);
			Assert.fail();
		}
		try {
			rs = stmt.executeQuery(statement);
		} catch (SQLException e) {
			log.error("\"" + statement + "\" failed because ", e);
			return null;
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					log.error("\"" + statement + "\" failed because ", e);
					return null;
				}
			}
		}
		log.info("executed \"" + statement + "\"");
		return rs;
	}
	public void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			log.error("Connection could not be closed because ",e);
		}
	}
}
