/**
 *
 */
package org.nees.illinois.replay.common.types;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Michael Bletzinger
 */
public class CompositeQuery implements CompositeQueryI {
	/**
	 * Query name.
	 */
	private final String name;

	/**
	 * Column order of the query results.
	 */
	private final List<String> queryOrder;

	/**
	 * {@link Map Map} of table queries used to generate a select statement for
	 * each table.
	 */
	private final Map<TableIdentityI, TableDefinitionI> tableQueries = new HashMap<TableIdentityI, TableDefinitionI>();

	/**
	 * @param name
	 *            Query name
	 * @param queryOrder
	 *            Column order of the query results
	 */
	public CompositeQuery(final String name, final List<String> queryOrder) {
		this.name = name;
		this.queryOrder = queryOrder;
	}

	/*
	 * (non-Javadoc)
	 * @see org.nees.illinois.replay.common.types.QueryI#getName()
	 */
	@Override
	public final String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * @see org.nees.illinois.replay.common.types.QueryI#getQueryList()
	 */
	@Override
	public final List<String> getQueryList() {
		return queryOrder;
	}

	/*
	 * (non-Javadoc)
	 * @see org.nees.illinois.replay.common.types.QueryI#getTableQueries()
	 */
	@Override
	public final Map<TableIdentityI, TableDefinitionI> getTableQueries() {
		return tableQueries;
	}

}
