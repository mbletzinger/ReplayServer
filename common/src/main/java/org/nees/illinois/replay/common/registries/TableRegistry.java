/**
 *
 */
package org.nees.illinois.replay.common.registries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nees.illinois.replay.common.types.TableDefinitionI;
import org.nees.illinois.replay.common.types.TableIdentityI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Registry of database tables used in an experiment.
 * @author Michael Bletzinger
 */
public class TableRegistry {
	/**
	 * Logger.
	 */
	private final Logger log = LoggerFactory.getLogger(TableRegistry.class);
	/**
	 * Map of table id's to the table {@link TableDefinitionI definitions}.
	 */
	private final Map<String, TableDefinitionI> definitions = new HashMap<String, TableDefinitionI>();

	/**
	 * Find the table that *owns* a channel.
	 * @param dbchannel
	 *            Channel to find.
	 * @return {@link TableIdentityI Identity} of the table that owns the
	 *         channel.
	 */
	public final TableIdentityI findTable(final String dbchannel) {
		for (TableDefinitionI t : definitions.values()) {
			if (t.getColumns(false).contains(dbchannel)) {
				return t.getTableId();
			}
		}
		return null;
	}

	/**
	 * @return the table names
	 */
	public final List<String> getNames() {
		List<String> keys = new ArrayList<String>();
		keys.addAll(definitions.keySet());
		Collections.sort(keys);
		return keys;
	}

	/**
	 * Get the table definition associated with the name.
	 * @param name
	 *            Name of table
	 * @return Table {@link TableDefinitionI definition}
	 */
	public final TableDefinitionI getTable(final String name) {
		return definitions.get(name);
	}
	/**
	 * Initialization function used to synchronize with the database. For
	 * internal use only.
	 * @param definitions
	 *            Map of table definitions.
	 */
	public final void init(final Map<String, TableDefinitionI> definitions) {
		this.definitions.clear();
		this.definitions.putAll(definitions);
	}
	/**
	 * Set the table definition associated with the name.
	 * @param name
	 *            Name of table
	 * @param table
	 *            Table {@link TableDefinitionI definition}
	 */
	public final void setTable(final String name, final TableDefinitionI table) {
		TableDefinitionI old = getTable(name);
		if (old != null) {
			log.error("Table " + name + " already in the registry as " + old);
			return;
		}
		definitions.put(name, table);
	}

	/**
	 * @return the definitions.  Used only for synchronization.
	 */
	public final Map<String, TableDefinitionI> getDefinitions() {
		return definitions;
	}


}
