package org.nees.illinois.replay.db.statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for inserting table identities into the database.
 * @author Michael Bletzinger
 */
public class TableDefInsertStatement extends InsertStatement {
	/**
	 * Logger.
	 **/
	private final Logger log = LoggerFactory
			.getLogger(TableDefInsertStatement.class);

	/**
	 * @param connection
	 *            JDBC connection.
	 * @param tableName
	 *            Name of the table containing the identities.
	 */
	public TableDefInsertStatement(final Connection connection,
			final String tableName) {
		super(connection, "INSERT INTO " + tableName + " VALUES (?, ?, ?)");
	}

	/**
	 * Add a table identity to the statement.
	 * @param name
	 *            Name of table.
	 * @param id
	 *            Table ID.
	 * @param channelStr
	 *            String of channel names.
	 * @return True if successful.
	 */
	public final boolean add(final String name, final String id,
			final String channelStr) {
		PreparedStatement statement = getBuilder().getStatement();
		final int thirdColumn = 3;
		try {
			statement.setString(1, name);
			statement.setString(2, id);
			statement.setString(thirdColumn, channelStr);
			statement.addBatch();
		} catch (SQLException e) {
			log.error("Cannot add " + id + " because ", e);
			return false;
		}
		return true;
	}
}
