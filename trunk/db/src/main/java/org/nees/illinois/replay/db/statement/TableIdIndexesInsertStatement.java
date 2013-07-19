package org.nees.illinois.replay.db.statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.nees.illinois.replay.common.registries.TableType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for inserting table identity register indexes into the database.
 * @author Michael Bletzinger
 */
public class TableIdIndexesInsertStatement extends InsertStatement {
	/**
	 * Logger.
	 **/
	private final Logger log = LoggerFactory
			.getLogger(TableIdIndexesInsertStatement.class);

	/**
	 * @param connection
	 *            JDBC connection.
	 * @param tableName
	 *            Name of the table containing the channels.
	 */
	public TableIdIndexesInsertStatement(final Connection connection,
			final String tableName) {
		super(connection, "INSERT INTO " + tableName + " VALUES (?, ?)");
	}

	/**
	 * Add a channel to the statement.
	 * @param type
	 *            Table type.
	 * @param idx
	 *            Current index.
	 * @return True if successful.
	 */
	public final boolean add(final TableType type, final int idx) {
		PreparedStatement statement = getBuilder().getStatement();
		try {
			statement.setString(1, type.name());
			statement.setInt(2, idx);
			statement.addBatch();
		} catch (SQLException e) {
			log.error("Cannot add id and string because ", e);
			return false;
		}
		return true;
	}
}
