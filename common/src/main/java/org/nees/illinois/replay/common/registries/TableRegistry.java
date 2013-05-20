/**
 *
 */
package org.nees.illinois.replay.common.registries;

import java.util.HashMap;
import java.util.Map;

import org.nees.illinois.replay.common.types.TableColumnsI;
import org.nees.illinois.replay.common.types.TableIdentityI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Registry of database tables used in an experiment.
 * @author Michael Bletzinger
 */
public class TableRegistry {
	/**
	 * Map of table id's to the table {@link TableColumnsI definitions}.
	 */
	private final Map<String, TableColumnsI> tableMap = new HashMap<String, TableColumnsI>();
	/**
	 * Logger.
	 */
	private final Logger log = LoggerFactory.getLogger(TableRegistry.class);

	/**
	 * Get the table definition associated with the name.
	 * @param name
	 *            Name of table
	 * @return Table {@link TableColumnsI definition}
	 */
	public final TableColumnsI getTable(final String name) {
		return tableMap.get(name);
	}

	/**
	 * Set the table definition associated with the name.
	 * @param name
	 *            Name of table
	 * @param table
	 *            Table {@link TableColumnsI definition}
	 */
	public final void setTable(final String name, final TableColumnsI table) {
		TableColumnsI old = getTable(name);
		if (old != null) {
			log.error("Table " + name + " already in the registry as " + old);
			return;
		}
		tableMap.put(name, table);
	}

	/**
	 * Find the table that *owns* a channel.
	 * @param dbchannel
	 *            Channel to find.
	 * @return {@link TableIdentityI Identity} of the table that owns the
	 *         channel.
	 */
	public final TableIdentityI findTable(final String dbchannel) {
		for (TableColumnsI t : tableMap.values()) {
			if (t.getColumns(false).contains(dbchannel)) {
				return t.getTableId();
			}
		}
		return null;
	}

}
