package org.nees.illinois.replay.db.statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.nees.illinois.replay.common.types.TableIdentityI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for inserting table identities into the database.
 * @author Michael Bletzinger
 */
public class TableIdInsertStatement extends InsertStatement {
	/**
	 * Logger.
	 **/
	private final Logger log = LoggerFactory
			.getLogger(TableIdInsertStatement.class);

	/**
	 * @param connection
	 *            JDBC connection.
	 * @param tableName
	 *            Name of the table containing the identities.
	 */
	public TableIdInsertStatement(final Connection connection,
			final String tableName) {
		super(connection, "INSERT INTO " + tableName + " VALUES (?, ?)");
	}

	/**
	 * Add a table identity to the statement.
	 * @param name
	 *            Name of table.
	 * @param id
	 *            Table ID.
	 * @return True if successful.
	 */
	public final boolean add(final String name, final TableIdentityI id) {
		PreparedStatement statement = getBuilder().getStatement();
		try {
			statement.setString(1, name);
			statement.setString(2, id.getDbName());
			statement.addBatch();
		} catch (SQLException e) {
			log.error("Cannot add " + id + " because ", e);
			return false;
		}
		return true;
	}
}
