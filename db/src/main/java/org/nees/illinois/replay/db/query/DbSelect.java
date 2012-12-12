package org.nees.illinois.replay.db.query;

import org.nees.illinois.replay.data.NumberOfColumns;
import org.nees.illinois.replay.data.RateType;

public class DbSelect extends NumberOfColumns {
	private final String select;
	public DbSelect(String select, int dataColumns, RateType rate) {
		super(dataColumns, rate);
		this.select = select;
	}

	/**
	 * @return the select
	 */
	public String getSelect() {
		return select;
	}
	public DbSelect cloneWithTimeConstraint(String timeConstraint) {
		return new DbSelect(select + timeConstraint, dataColumns, rate);
	}
}
