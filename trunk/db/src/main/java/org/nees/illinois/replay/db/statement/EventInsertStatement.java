package org.nees.illinois.replay.db.statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.nees.illinois.replay.events.EventI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Inserts events into events table.
 * @author Michael Bletzinger
 */
public class EventInsertStatement extends InsertStatement {

	/**
	 * Logger.
	 **/
	private final Logger log = LoggerFactory
			.getLogger(EventInsertStatement.class);

	/**
	 * @param connection
	 *            Database connection.
	 * @param eventTableName
	 *            Table name.
	 */
	public EventInsertStatement(final Connection connection,
			final String eventTableName) {
		super(connection, "INSERT INTO \"" + eventTableName
				+ "\" VALUES (? ? ? ? ?)");
	}

	/**
	 * Add a data record.
	 * @param event
	 *            array of doubles to be inserted.
	 * @return True if successful.
	 */
	public final boolean add(final EventI event) {
		PreparedStatement statement = getBuilder().getStatement();
		log.debug("Adding " + event.getName());
		try {
			statement.setDouble(0, event.getTime());
			statement.setString(1, event.getName());
			statement.setString(1, event.getType().name());
			statement.setString(1, event.getSource());
			statement.setString(1, event.getDescription());
			statement.addBatch();
		} catch (SQLException e) {
			log.error("Cannot add data because ", e);
			return false;
		}
		return true;
	}

}
