package org.nees.illinois.replay.common.registries;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.nees.illinois.replay.common.types.CompositeQueryI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a registry that stores replay server queries. Each
 * {@link CompositeQueryI query} consists of a name and a set of channel names
 * which define the search dataset. Other parts of the query request such as the
 * time request and the data sampling request are not stored in the registry.
 * Instead they are added when the search is performed.
 * @author Michael Bletzinger
 */
public class QueryRegistry {
	/**
	 * Map of query names with the associated {@link CompositeQueryI query}.
	 */
	private final ConcurrentMap<String, CompositeQueryI> queries = new ConcurrentHashMap<String, CompositeQueryI>();
	/**
	 * Logger.
	 */
	private final Logger log = LoggerFactory.getLogger(QueryRegistry.class);

	/**
	 * Get the query associated with the name.
	 * @param name
	 *            Query name
	 * @return The {@link CompositeQueryI query}.
	 */
	public final CompositeQueryI getQuery(final String name) {
		return queries.get(name);
	}

	/**
	 * Set a query mapped to the name.
	 * @param name
	 *            Query name
	 * @param cq
	 *            The {@link CompositeQueryI query}.
	 * @return Return true if the query replaced an older version.
	 */
	public final boolean setQuery(final String name, final CompositeQueryI cq) {
		boolean result = false;
		if (queries.containsKey(name)) {
			log.info("Replacing Query \"" + name + "\"");
			result = true;
		} else {
			log.info("Adding Query \"" + name + "\"");
		}
		queries.put(name, cq);
		return result;
	}

}
