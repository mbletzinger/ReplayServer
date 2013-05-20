/**
 *
 */
package org.nees.illinois.replay.common.types;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nees.illinois.replay.data.RateType;

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
	 * Sampling rate of the queried data.
	 **/
	private RateType rate;

	/**
	 * {@link Map Map} of table queries used to generate a select statement for
	 * each table.
	 */
	private final Map<TableIdentityI, TableColumnsI> tableQueries = new HashMap<TableIdentityI, TableColumnsI>();

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

	/**
	 * @return the rate
	 */
	public final RateType getRate() {
		return rate;
	}

	/*
	 * (non-Javadoc)
	 * @see org.nees.illinois.replay.common.types.QueryI#getTableQueries()
	 */
	@Override
	public final Map<TableIdentityI, TableColumnsI> getTableQueries() {
		return tableQueries;
	}

	/**
	 * @param rate
	 *            the rate to set
	 */
	public final void setRate(final RateType rate) {
		this.rate = rate;
	}

}
