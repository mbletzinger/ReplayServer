package org.nees.illinois.replay.db.statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to build and execute a prepared statement.
 * @author Michael Bletzinger
 */
public class PrepStatementProcessor {
	/**
	 * JDBC connection.
	 */
	private final Connection connection;
	/**
	 * Logger.
	 */
	private final Logger log = LoggerFactory
			.getLogger(PrepStatementProcessor.class);
	/**
	 * Initial string for the prepared statement.
	 */
	private final String prepInit;
	/**
	 * JDBC prepared statement.
	 */
	private PreparedStatement statement;

	/**
	 * @param connection
	 *            JDBC connection.
	 * @param prepInit
	 *            Initial string for the prepared statement.
	 */
	public PrepStatementProcessor(final Connection connection,
			final String prepInit) {
		super();
		this.connection = connection;
		this.prepInit = prepInit;
	}

	/**
	 * Create the statement.
	 * @return True if successful.
	 */
	public final boolean create() {
		try {
			statement = connection.prepareStatement(prepInit);
		} catch (SQLException e) {
			log.error("Create prep statement \"" + prepInit
					+ "\" failed because", e);
			return false;
		}
		return true;
	}

	/**
	 * Execute the statement.
	 * @return the number of items that were executed.
	 */
	public final int[] execute() {
		int[] result = null;
		try {
			result = statement.executeBatch();
		} catch (SQLException e) {
			log.error("Execute prep statement \"" + prepInit
					+ "\" failed because", e);
			return result;
		}
		try {
			statement.close();
			connection.close();
		} catch (SQLException e) {
			log.error("Closing prep statement \"" + prepInit
					+ "\" failed because", e);
			return result;
		}

		return result;
	}

	/**
	 * @return the connection
	 */
	public final Connection getConnection() {
		return connection;
	}

	/**
	 * @return the initial prep string.
	 */
	public final String getPrepInit() {
		return prepInit;
	}

	/**
	 * @return the statement
	 */
	public final PreparedStatement getStatement() {
		return statement;
	}
}
