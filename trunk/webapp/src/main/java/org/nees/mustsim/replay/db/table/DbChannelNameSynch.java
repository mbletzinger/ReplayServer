package org.nees.mustsim.replay.db.table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.nees.mustsim.replay.db.statement.ChannelInsertStatement;
import org.nees.mustsim.replay.db.statement.ChannelUpdateStatement;
import org.nees.mustsim.replay.db.statement.DbStatements;

public class DbChannelNameSynch {
	private final ChannelNameRegistry registry;
	private final String channelTable = "CHANNEL_NAMES";
	private final Logger log = Logger.getLogger(DbChannelNameSynch.class);
	private final DbStatements db;
	
	
	public DbChannelNameSynch(ChannelNameRegistry registry, DbStatements db) {
		super();
		this.registry = registry;
		this.db = db;
	}
	public void synchronize() {
		Map<String, String> names = getValues();
		Map<String,String> reg = registry.getClone();
		ChannelUpdateStatement prep = new ChannelUpdateStatement(channelTable);
		db.createPrepStatement(prep);
		for (String n : names.keySet()) {
			String id = names.get(n);
			if(id.equals(reg.get(n)) == false) {
				prep.add(n, id);
			}
		}
		if (prep.execute() == false) {
			log.error("Channel name synchronize failed");
			return;
		}
		ChannelInsertStatement prep2 = new ChannelInsertStatement(channelTable);
		db.createPrepStatement(prep2);
		for (String n : reg.keySet()) {
			if (names.containsKey(n) == false) {
				prep2.add(n, reg.get(n));
			}
		}
		if (prep.execute() == false) {
			log.error("Channel name synchronize failed");
			return;
		}
	}
	public void initialize() {
		Map<String, String> names = getValues();
		if(names.isEmpty() == false) {
			registry.init(names);
		}
	}
	public void createTable() {
		db.execute("CREATE TABLE " + channelTable + "(name, id)");
	}
	public void removeTable() {
		db.execute("DROP TABLE " + channelTable);		
	}
	private Map<String, String> getValues() {
		ResultSet rs = db.query("SELECT name, id FROM " + channelTable);
		Map<String, String> names = new HashMap<String, String>();
		try {
			while(rs.next()) {
				String name = rs.getString("name");
				String id = rs.getString("id");
				names.put(name, id);
			}
		} catch (SQLException e) {
			log.error("Query to table " + channelTable + " failed because ",e);
		}
		return names;
	}
}
