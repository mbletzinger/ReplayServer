package org.nees.illinois.replay.db.statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for inserting channel identities into the database.
 * @author Michael Bletzinger
 */
public class ChannelInsertStatement extends InsertStatement {
	/**
	 * Logger.
	 **/
	private final Logger log = LoggerFactory
			.getLogger(ChannelInsertStatement.class);

	/**
	 * @param connection
	 *            JDBC connection.
	 * @param channelTableName
	 *            Name of the table containing the channels.
	 */
	public ChannelInsertStatement(final Connection connection,
			final String channelTableName) {
		super(connection, "INSERT INTO " + channelTableName + " VALUES (?, ?)");
	}

	/**
	 * Add a channel to the statement.
	 * @param name
	 *            Name of channel.
	 * @param id
	 *            Database ID of the channel.
	 * @return True if successful.
	 */
	public final boolean add(final String name, final String id) {
		PreparedStatement statement = getBuilder().getStatement();
		try {
			statement.setString(1, name);
			statement.setString(2, id);
			statement.addBatch();
		} catch (SQLException e) {
			log.error("Cannot add id and string because ", e);
			return false;
		}
		return true;
	}
}
