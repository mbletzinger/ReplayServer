package org.nees.illinois.replay.db.statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.nees.illinois.replay.data.Mtx2Str;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for inserting data with a prepared statement.
 * @author Michael Bletzinger
 */
public class DataInsertStatement extends InsertStatement {
	/**
	 * Logger.
	 */
	private final Logger log = LoggerFactory
			.getLogger(DataInsertStatement.class);

	/**
	 * Builds the initial string of a prepared statement and returns an
	 * instance.
	 * @param connection
	 *            JDBC connection.
	 * @param dataTableName
	 *            Name of the table which the data is being inserted into.
	 * @param numberOfChannels
	 *            Number of fields.
	 * @return The instance.
	 */
	public static DataInsertStatement getStatement(final Connection connection,
			final String dataTableName, final int numberOfChannels) {
		String statement = "INSERT INTO \"" + dataTableName + "\" VALUES (";
		for (int c = 0; c < numberOfChannels; c++) {
			statement += (c == 0 ? "" : ", ") + "?";
		}
		statement += ")";
		return new DataInsertStatement(connection, statement);
	}

	/**
	 * @param connection
	 *            JDBC connection.
	 * @param prepped
	 *            Initial string of the prepared statement.
	 */
	public DataInsertStatement(final Connection connection, final String prepped) {
		super(connection, prepped);
	}

	/**
	 * Add a data record.
	 * @param data
	 *            array of doubles to be inserted.
	 * @return True if successful.
	 */
	public final boolean add(final double[] data) {
		PreparedStatement statement = getBuilder().getStatement();
		log.debug("Adding " + Mtx2Str.array2String(data));
		try {
			for (int c = 0; c < data.length; c++) {
				statement.setDouble(c + 1, data[c]);
			}
			statement.addBatch();
		} catch (SQLException e) {
			log.error("Cannot add data because ", e);
			return false;
		}
		return true;
	}
}
