package org.nees.mustsim.replay.db.query;

import org.nees.mustsim.replay.db.statement.NumberOfColumns;
import org.nees.mustsim.replay.db.statement.RateType;

public class DbSelect extends NumberOfColumns {
	private final String select;
	public DbSelect(String select, int size, RateType rate) {
		super(size, rate);
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
