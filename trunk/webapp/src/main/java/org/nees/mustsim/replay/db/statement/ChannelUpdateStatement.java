package org.nees.mustsim.replay.db.statement;

import java.sql.SQLException;

import org.apache.log4j.Logger;

public class ChannelUpdateStatement extends PrepStatement {

	public ChannelUpdateStatement(String channelTableName) {
		super("UPDATE " + channelTableName + "ID=? WHERE NAME=?", Logger.getLogger(ChannelUpdateStatement.class));
	}
	public boolean add(String name, String id) {
		try {
			statement.setString(1, id);
			statement.setString(2, name);
			statement.addBatch();
		} catch (SQLException e) {
			log.error("Cannot update id and string because ", e);
			return false;
		}
		return true;
	}
}
