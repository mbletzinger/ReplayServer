package org.nees.illinois.replay.db.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.nees.illinois.replay.db.statement.ChannelInsertStatement;
import org.nees.illinois.replay.db.statement.StatementProcessor;
import org.nees.illinois.replay.registries.ChannelNameRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbChannelNameSynch {
	private final String channelTable = "CHANNEL_NAMES";
	private final StatementProcessor db;
	private final Logger log = LoggerFactory
			.getLogger(DbChannelNameSynch.class);
	private final ChannelNameRegistry cnr;
	private final String afterLastChannel = "AfterLastChannel";

	public DbChannelNameSynch(ChannelNameRegistry cnr, StatementProcessor db) {
		super();
		this.cnr = cnr;
		this.db = db;
	}

	public void createTable() {
		db.execute("CREATE TABLE " + channelTable
				+ "(name varchar(100), id varchar(50))");
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
			cnr.init(names, alastChannel);
		}
	}

	public void removeTable() {
		db.noComplaints("DROP TABLE " + channelTable);
	}

	public void synchronize() {
		removeTable();
		createTable();
		Map<String, String> reg = cnr.getClone();
		ChannelInsertStatement prep = new ChannelInsertStatement(channelTable);
		db.createPrepStatement(prep);
		for (String n : reg.keySet()) {
			prep.add(n, reg.get(n));
		}
		prep.add(afterLastChannel, Long.toString(cnr.getAfterLastChannel()));
		if (prep.execute() == null) {
			log.error("Channel name synchronize failed");
			return;
		}
	}
}
