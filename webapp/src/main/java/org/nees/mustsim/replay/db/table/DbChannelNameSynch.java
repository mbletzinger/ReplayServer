package org.nees.mustsim.replay.db.table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.nees.mustsim.replay.db.statement.ChannelInsertStatement;
import org.nees.mustsim.replay.db.statement.DbStatement;

public class DbChannelNameSynch {
	private final String channelTable = "CHANNEL_NAMES";
	private final DbStatement db;
	private final Logger log = Logger.getLogger(DbChannelNameSynch.class);
	private final ChannelNameRegistry registry;

	public DbChannelNameSynch(ChannelNameRegistry registry, DbStatement db) {
		super();
		this.registry = registry;
		this.db = db;
	}

	public void createTable() {
		db.execute("CREATE TABLE " + channelTable + "(name, id)");
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
		return names;
	}

	public void initialize() {
		Map<String, String> names = getValues();
		if (names.isEmpty() == false) {
			registry.init(names);
		}
	}

	public void removeTable() {
		db.execute("DROP TABLE " + channelTable);
	}

	public void synchronize() {
		Map<String, String> names = getValues();
		Map<String, String> reg = registry.getClone();
		ChannelInsertStatement prep = new ChannelInsertStatement(channelTable);
		db.createPrepStatement(prep);
		for (String n : reg.keySet()) {
			if (names.containsKey(n) == false) {
				prep.add(n, reg.get(n));
			}
		}
		if (prep.execute() == null) {
			log.error("Channel name synchronize failed");
			return;
		}
	}
}
