package org.nees.illinois.replay.db.registry.synch;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.nees.illinois.replay.common.registries.ChannelNameRegistry;
import org.nees.illinois.replay.db.statement.ChannelInsertStatement;
import org.nees.illinois.replay.db.statement.StatementProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Synchronizes the {@link ChannelNameRegistry} with the experiment database.
 * @author Michael Bletzinger
 */
public class DbChannelNameSynch implements RegistrySynchI {
	/**
	 * Name of the channel name table.
	 */
	private final String channelTable = "CHANNEL_NAMES";
	/**
	 * Database statement processor for queries.
	 */
	private final StatementProcessor db;
	/**
	 * Logger.
	 */
	private final Logger log = LoggerFactory
			.getLogger(DbChannelNameSynch.class);
	/**
	 * Registry to synchronize.
	 */
	private final ChannelNameRegistry cnr;
	/**
	 * Special record to save the channel index.
	 */
	private final String afterLastChannel = "AfterLastChannel";

	/**
	 * @param cnr
	 *            Registry to synchronize.
	 * @param db
	 *            Database statement processor for queries.
	 */
	public DbChannelNameSynch(final ChannelNameRegistry cnr, final StatementProcessor db) {
		super();
		this.cnr = cnr;
		this.db = db;
	}

	/* (non-Javadoc)
	 * @see org.nees.illinois.replay.db.registry.synch.RegistrySynch#createTable()
	 */
	@Override
	public final void createTable() {
		db.execute("CREATE TABLE " + channelTable
				+ "(name varchar(100), id varchar(50))");
	}

	/**
	 * Get all of the channel names from the database.
	 * @return Map of names and IDs.
	 */
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

	/* (non-Javadoc)
	 * @see org.nees.illinois.replay.db.registry.synch.RegistrySynch#load()
	 */
	@Override
	public final void load() {
		Map<String, String> names = getValues();
		if (names.isEmpty() == false) {
			long alastChannel = Long.parseLong(names.get(afterLastChannel));
			names.remove(afterLastChannel);
			cnr.init(names, alastChannel);
		}
	}

	/* (non-Javadoc)
	 * @see org.nees.illinois.replay.db.registry.synch.RegistrySynch#removeTable()
	 */
	@Override
	public final void removeTable() {
		db.noComplaints("DROP TABLE " + channelTable);
	}

	/* (non-Javadoc)
	 * @see org.nees.illinois.replay.db.registry.synch.RegistrySynch#save()
	 */
	@Override
	public final void save() {
		Connection connection = db.getConnection();
		removeTable();
		createTable();
		Map<String, String> reg = cnr.getNamesMap();
		ChannelInsertStatement prep = new ChannelInsertStatement(connection,
				channelTable);
		for (String n : reg.keySet()) {
			prep.add(n, reg.get(n));
		}
		prep.add(afterLastChannel, Long.toString(cnr.getAfterLastChannel()));
		if (prep.getBuilder().execute() == null) {
			log.error("Channel name synchronize failed");
			return;
		}
	}
}
