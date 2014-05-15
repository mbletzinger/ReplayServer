package org.nees.illinois.replay.db.statement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for executing database statements other than prepared statements.
 * @author Michael Bletzinger
 */
public class StatementProcessor {
	/**
	 * JDBC connection.
	 */
	private final Connection connection;
	/**
	 * Logger.
	 */
	private final Logger log = LoggerFactory
			.getLogger(StatementProcessor.class);

	/**
	 * @param connection
	 *            JDBC connection.
	 */
	public StatementProcessor(final Connection connection) {
		super();
		this.connection = connection;
	}

	/**
	 * Closes the connection.
	 */
	public final void close() {
		try {
			log.debug("Connection closed");
			connection.close();
		} catch (SQLException e) {
			log.error("Connection could not be closed because ", e);
		}
	}

	/**
	 * Closes the query statement.
	 * @param rs
	 *            ResultSet which points to the query statement.
	 */
	public final void closeQuery(final ResultSet rs) {
		try {
			rs.getStatement().close();
		} catch (SQLException e) {
			log.error("Statement close failed because ", e);
		}
	}

	/**
	 * Executes a statement.
	 * @param statement
	 *            The statement.
	 * @return True if successful.
	 */
	public final boolean execute(final String statement) {
		Statement stmt = null;
		log.debug("Executing " + statement);
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
		log.debug("Executed \"" + statement + "\"");
		return true;

	}

	/**
	 * @return the connection
	 */
	public final Connection getConnection() {
		return connection;
	}

	/**
	 * Executes a statement without any error reporting. Useful for shutting
	 * down stuff.
	 * @param statement
	 *            The statement.
	 */
	public final void noComplaints(final String statement) {
		log.debug("Executing " + statement);
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
		} catch (SQLException e) {
			return;
		}
		try {
			stmt.execute(statement);
		} catch (SQLException e) {
			return;
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					return;
				}
			}
		}
		log.debug("Executed \"" + statement + "\"");
	}

	/**
	 * Executes a select statement.
	 * @param statement
	 *            The statement.
	 * @return The results.
	 */
	public final ResultSet query(final String statement) {
		log.debug("Querying " + statement);
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.createStatement();
		} catch (SQLException e) {
			log.error("Query statement \"" + statement + "\" failed because ",
					e);
			return null;
		}
		try {
			rs = stmt.executeQuery(statement);
		} catch (SQLException e) {
			log.error("\"" + statement + "\" failed because ", e);
			return null;
		} finally {
			if (rs == null && stmt != null) {
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
}
