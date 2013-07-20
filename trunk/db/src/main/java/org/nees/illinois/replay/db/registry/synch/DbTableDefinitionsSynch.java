package org.nees.illinois.replay.db.registry.synch;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.nees.illinois.replay.common.registries.TableIdentityRegistry;
import org.nees.illinois.replay.common.registries.TableRegistry;
import org.nees.illinois.replay.common.registries.TableType;
import org.nees.illinois.replay.common.types.TableDef;
import org.nees.illinois.replay.common.types.TableDefinitionI;
import org.nees.illinois.replay.common.types.TableId;
import org.nees.illinois.replay.common.types.TableIdentityI;
import org.nees.illinois.replay.db.statement.StatementProcessor;
import org.nees.illinois.replay.db.statement.TableIdIndexesInsertStatement;
import org.nees.illinois.replay.db.statement.TableDefInsertStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Synchronizes the {@link TableIdentityRegistry} with the experiment database.
 * @author Michael Bletzinger
 */
public class DbTableDefinitionsSynch implements RegistrySynchI {
	/**
	 * Name of the table id table.
	 */
	private final String defTable = "TABLE_DEFINITIONS";
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
	private final Logger log = LoggerFactory
			.getLogger(DbTableDefinitionsSynch.class);
	/**
	 * Table ID registry to synchronize.
	 */
	private final TableIdentityRegistry tir;
	/**
	 * Table definitions registry to synchronize.
	 */
	private final TableRegistry tableReg;
	/**
	 * Encode and decode channel lists.
	 */
	private final EncodeDecodeList<String, ParseElement<String>> encoder = new EncodeDecodeList<String, ParseElement<String>>(
			new StringDecoder());

	/**
	 * @param tir
	 *            ID registry to synchronize.
	 * @param db
	 *            Database statement processor for queries.
	 * @param tableReg
	 *            Definitions registry to synchronize.
	 */
	public DbTableDefinitionsSynch(final TableIdentityRegistry tir,
			final TableRegistry tableReg, final StatementProcessor db) {
		super();
		this.tir = tir;
		this.db = db;
		this.tableReg = tableReg;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.nees.illinois.replay.db.registry.synch.RegistrySynch#createTable()
	 */
	@Override
	public final void createTable() {
		db.execute("CREATE TABLE "
				+ defTable
				+ "(rname varchar(100), dbname varchar(50), channels varchar(2000))");
		db.execute("CREATE TABLE " + tableIdIndexesTable
				+ "(type varchar(100), index int)");
	}

	/**
	 * Get all of the table definitions from the database.
	 * @return Map of names and definitions.
	 */
	private Map<String, TableDefinitionI> getValues() {
		ResultSet rs = db.query("SELECT rname, dbname, channels FROM "
				+ defTable);
		Map<String, TableDefinitionI> defs = new HashMap<String, TableDefinitionI>();
		try {
			while (rs.next()) {
				String name = rs.getString("rname");
				String id = rs.getString("dbname");
				String channelStr = rs.getString("channels");
				TableIdentityI tblId = new TableId(name, id);
				TableDefinitionI tbldef;
				try {
					tbldef = new TableDef(encoder.parse(channelStr), tblId);
				} catch (IllegalParameterException e) {
					log.error("Could not parse [" + channelStr + "] because", e);
					return null;
				}
				defs.put(name, tbldef);
			}
		} catch (SQLException e) {
			log.error("Query to table " + defTable + " failed because ", e);
		}
		db.closeQuery(rs);
		return defs;
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
		Map<String, TableDefinitionI> defs = getValues();
		Map<TableType, Integer> indexes = getIndexes();
		if (defs.isEmpty() == false) {
			tableReg.init(defs);
			Map<String, TableIdentityI> names = new HashMap<String, TableIdentityI>();
			for (TableDefinitionI d : defs.values()) {
				TableIdentityI id = d.getTableId();
				names.put(id.getDatasetName(), id);
			}
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
		db.noComplaints("DROP TABLE " + defTable);
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
		Map<String, TableDefinitionI> reg = tableReg.getDefinitions();
		TableDefInsertStatement prep = new TableDefInsertStatement(connection,
				defTable);
		for (String n : reg.keySet()) {
			TableDefinitionI def = reg.get(n);
			prep.add(n, def.getTableId(),
					encoder.encode(def.getColumns(false)));
		}
		if (prep.getBuilder().execute() == null) {
			log.error("Table definition synchronize failed");
			return;
		}

		Map<TableType, Integer> indexes = tir.getAfterLastIndex();
		TableIdIndexesInsertStatement prepI = new TableIdIndexesInsertStatement(
				connection, tableIdIndexesTable);
		for (TableType t : indexes.keySet()) {
			prepI.add(t, indexes.get(t));
		}
		if (prepI.getBuilder().execute() == null) {
			log.error("Table index synchronize failed");
			return;
		}
	}
}
