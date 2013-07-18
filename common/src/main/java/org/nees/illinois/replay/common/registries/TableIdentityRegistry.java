/**
 *
 */
package org.nees.illinois.replay.common.registries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nees.illinois.replay.common.types.TableId;
import org.nees.illinois.replay.common.types.TableIdentityI;

/**
 * Class which manages the database table names for an experiment. The names are
 * mapped to a database friendly format using the {@link TableType TableType}
 * descriptors.
 * @author Michael Bletzinger
 */
public class TableIdentityRegistry {
	/**
	 * Maps the current unused index of a table type to {@link TableType
	 * TableType}.
	 */
	private final Map<TableType, Integer> afterLastIndex = new HashMap<TableType, Integer>();

	/**
	 * Maps the experiment table name to its table {@link TableIdentityI
	 * identity}.
	 */
	private final Map<String, TableIdentityI> identities = new HashMap<String, TableIdentityI>();

	/**
	 *
	 */
	public TableIdentityRegistry() {
		for (TableType t : TableType.values()) {
			afterLastIndex.put(t, new Integer(0));
		}
	}

	/**
	 * Store a new table name in the registry.
	 * @param experiment
	 *            Name of the experiment
	 * @param datasetname
	 *            New name.
	 * @param type
	 *            {@link TableType Table type}.
	 * @return The table {@link TableIdentityI identity}.
	 */
	public final TableIdentityI addTable(final String experiment,
			final String datasetname, final TableType type) {
		TableIdentityI result = identities.get(datasetname);
		if (result == null) {
			String dbtn = newName(type);
			result = new TableId(datasetname, experiment, dbtn);
			identities.put(datasetname, result);
		}
		return result;
	}

	/**
	 * Return the current unused index for a table type.
	 * @param type
	 *            {@link TableType Table type}.
	 * @return The last unused index.
	 */
	public final int getAfterLastIndex(final TableType type) {
		return afterLastIndex.get(type);
	}

	/**
	 * Get the database friendly name for a table.
	 * @param name
	 *            Experiment table name.
	 * @return The table {@link TableIdentityI identity}.
	 */
	public final TableIdentityI getId(final String name) {
		return identities.get(name);
	}

	/**
	 * @return the table names
	 */
	public final List<String> getNames() {
		List<String> keys = new ArrayList<String>();
		keys.addAll(identities.keySet());
		Collections.sort(keys);
		return keys;
	}

	/**
	 * Initialization function used to synchronize with the database. For
	 * internal use only.
	 * @param identities
	 *            Map of table identities.
	 * @param indexes
	 *            Map of table indexes.
	 */
	public final void init(final Map<String, TableIdentityI> identities,
			final Map<TableType, Integer> indexes) {
		afterLastIndex.clear();
		afterLastIndex.putAll(indexes);
		this.identities.clear();
		this.identities.putAll(identities);
	}

	/**
	 * Create a new table name.
	 * @param type
	 *            Table type.
	 * @return New name.
	 */
	private String newName(final TableType type) {
		int idx = afterLastIndex.get(type);
		String result = type.toString().toLowerCase() + "_";
		result += idx;
		afterLastIndex.put(type, new Integer(idx + 1));
		return result;
	}
}
