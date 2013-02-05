package org.nees.illinois.replay.db.statement;

import java.sql.SQLException;

import org.slf4j.LoggerFactory;

public class ChannelInsertStatement extends PrepStatement {

	public ChannelInsertStatement(String channelTableName) {
		super("INSERT INTO " + channelTableName + " VALUES (?, ?)", LoggerFactory.getLogger(ChannelInsertStatement.class));
	}
	public boolean add(String name, String id) {
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
