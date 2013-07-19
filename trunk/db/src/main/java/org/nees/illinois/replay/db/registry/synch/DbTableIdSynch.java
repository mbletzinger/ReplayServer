package org.nees.illinois.replay.db.registry.synch;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.nees.illinois.replay.common.registries.TableIdentityRegistry;
import org.nees.illinois.replay.common.registries.TableType;
import org.nees.illinois.replay.common.types.TableId;
import org.nees.illinois.replay.common.types.TableIdentityI;
import org.nees.illinois.replay.db.statement.StatementProcessor;
import org.nees.illinois.replay.db.statement.TableIdIndexesInsertStatement;
import org.nees.illinois.replay.db.statement.TableIdInsertStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Synchronizes the {@link TableIdentityRegistry} with the experiment database.
 * @author Michael Bletzinger
 */
public class DbTableIdSynch implements RegistrySynchI {
	/**
	 * Name of the table id table.
	 */
	private final String tableIdTable = "TABLE_IDENTITIES";
	/**
	 * Name of the index table.
	 */
	private final String tableIdIndexesTable = "TABLE_IDENTITY_INDEXES";
	/**
	 * Database statement processor for queries.
	 */
	private final StatementProcessor db;
	/**
	 * Logger.
	 */
	private final Logger log = LoggerFactory.getLogger(DbTableIdSynch.class);
	/**
	 * Registry to synchronize.
	 */
	private final TableIdentityRegistry tir;

	/**
	 * @param tir
	 *            Registry to synchronize.
	 * @param db
	 *            Database statement processor for queries.
	 */
	public DbTableIdSynch(final TableIdentityRegistry tir,
			final StatementProcessor db) {
		super();
		this.tir = tir;
		this.db = db;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.nees.illinois.replay.db.registry.synch.RegistrySynch#createTable()
	 */
	@Override
	public final void createTable() {
		db.execute("CREATE TABLE " + tableIdTable
				+ "(rname varchar(100), dbname varchar(50))");
		db.execute("CREATE TABLE " + tableIdIndexesTable
				+ "(type varchar(100), index int)");
	}

	/**
	 * Get all of the table names from the database.
	 * @return Map of names and IDs.
	 */
	private Map<String, TableIdentityI> getValues() {
		ResultSet rs = db.query("SELECT rname, dbname FROM " + tableIdTable);
		Map<String, TableIdentityI> names = new HashMap<String, TableIdentityI>();
		try {
			while (rs.next()) {
				String name = rs.getString("rname");
				String id = rs.getString("dbname");
				TableIdentityI tblId = new TableId(name, id);
				names.put(name, tblId);
			}
		} catch (SQLException e) {
			log.error("Query to table " + tableIdTable + " failed because ", e);
		}
		db.closeQuery(rs);
		return names;
	}

	/**
	 * Get all of the table indexes from the database.
	 * @return Map of table types and IDs.
	 */
	private Map<TableType, Integer> getIndexes() {
		ResultSet rs = db.query("SELECT type, index FROM "
				+ tableIdIndexesTable);
		Map<TableType, Integer> indexes = new HashMap<TableType, Integer>();
		try {
			while (rs.next()) {
				TableType type = TableType.valueOf(rs.getString("type"));
				int idx = rs.getInt("index");
				indexes.put(type, idx);
			}
		} catch (SQLException e) {
			log.error("Query to table " + tableIdIndexesTable
					+ " failed because ", e);
		}
		db.closeQuery(rs);
		return indexes;
	}

	/*
	 * (non-Javadoc)
	 * @see org.nees.illinois.replay.db.registry.synch.RegistrySynch#load()
	 */
	@Override
	public final void load() {
		Map<String, TableIdentityI> names = getValues();
		Map<TableType, Integer> indexes = getIndexes();
		if (names.isEmpty() == false) {
			tir.init(names, indexes);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.nees.illinois.replay.db.registry.synch.RegistrySynch#removeTable()
	 */
	@Override
	public final void removeTable() {
		db.noComplaints("DROP TABLE " + tableIdTable);
	}

	/*
	 * (non-Javadoc)
	 * @see org.nees.illinois.replay.db.registry.synch.RegistrySynch#save()
	 */
	@Override
	public final void save() {
		Connection connection = db.getConnection();
		removeTable();
		createTable();
		Map<String, TableIdentityI> reg = tir.getIdentities();
		TableIdInsertStatement prep = new TableIdInsertStatement(connection,
				tableIdTable);
		for (String n : reg.keySet()) {
			prep.add(n, reg.get(n));
		}
		if (prep.getBuilder().execute() == null) {
			log.error("Table identity synchronize failed");
			return;
		}

		Map<TableType, Integer> indexes = tir.getAfterLastIndex();
		TableIdIndexesInsertStatement prepI = new TableIdIndexesInsertStatement(connection, tableIdIndexesTable);
		for (TableType t : indexes.keySet()) {
			prepI.add(t, indexes.get(t));
		}
		if (prepI.getBuilder().execute() == null) {
			log.error("Table index synchronize failed");
			return;
		}
	}
}
