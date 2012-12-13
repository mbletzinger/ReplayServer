package org.nees.illinois.replay.db.data.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.nees.illinois.replay.channels.ChannelNameRegistry;
import org.nees.illinois.replay.db.statement.ChannelInsertStatement;
import org.nees.illinois.replay.db.statement.DbStatement;

public class DbChannelNameSynch {
	private final String channelTable = "CHANNEL_NAMES";
	private final DbStatement db;
	private final Logger log = Logger.getLogger(DbChannelNameSynch.class);
	private final ChannelNameRegistry registry;
	private final String afterLastChannel = "AfterLastChannel";

	public DbChannelNameSynch(ChannelNameRegistry registry, DbStatement db) {
		super();
		this.registry = registry;
		this.db = db;
	}

	public void createTable() {
		db.execute("CREATE TABLE " + channelTable + "(name varchar(100), id varchar(50))");
	}

	private Map<String, String> getValues() {
		ResultSet rs = db.query("SELECT name, id FROM " + channelTable);
		Map<String, String> names = new HashMap<String, String>();
		try {
			while (rs.next()) {
				String name = rs.getString("name");
				String id = rs.getString("id");
				names.put(name, id);
			}
		} catch (SQLException e) {
			log.error("Query to table " + channelTable + " failed because ", e);
		}
		db.closeQuery(rs);
		return names;
	}

	public void initialize() {
		Map<String, String> names = getValues();
		if (names.isEmpty() == false) {
			long alastChannel = Long.parseLong(names.get(afterLastChannel));
			names.remove(afterLastChannel);
			registry.init(names, alastChannel);
		}
	}

	public void removeTable() {
		db.noComplaints("DROP TABLE " + channelTable);
	}

	public void synchronize() {
		removeTable();
		createTable();
		Map<String, String> reg = registry.getClone();
		ChannelInsertStatement prep = new ChannelInsertStatement(channelTable);
		db.createPrepStatement(prep);
		for (String n : reg.keySet()) {
			prep.add(n, reg.get(n));
		}
		prep.add(afterLastChannel,
				Long.toString(registry.getAfterLastChannel()));
		if (prep.execute() == null) {
			log.error("Channel name synchronize failed");
			return;
		}
	}
}