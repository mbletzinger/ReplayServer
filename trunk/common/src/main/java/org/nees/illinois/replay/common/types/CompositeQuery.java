/**
 *
 */
package org.nees.illinois.replay.common.types;

import java.util.List;

import org.nees.illinois.replay.data.RateType;

/**
 * @author Michael Bletzinger
 */
public class CompositeQuery implements CompositeQueryI {
	/**
	 * Query name
	 */
	private final String name;

	/**
	 * Column order of the query results
	 */
	private final List<String> queryOrder;

	/**
	 * Sampling rate of the queried data
	 **/
	private RateType rate;

	/**
	 * @param name
	 *            Query name
	 * @param queryOrder
	 *            Column order of the query results
	 * @param rate
	 *            Sampling rate of the queried data
	 */
	public CompositeQuery(final String name, final List<String> queryOrder, final RateType rate) {
		this.name = name;
		this.queryOrder = queryOrder;
		this.rate = rate;
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
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return the queryOrder
	 */
	public final List<String> getQueryOrder() {
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
	public final List<TableColumnsI> getTableQueries() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param rate the rate to set
	 */
	public final void setRate(final RateType rate) {
		this.rate = rate;
	}

}
