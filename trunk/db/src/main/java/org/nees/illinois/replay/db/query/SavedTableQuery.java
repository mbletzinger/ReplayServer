package org.nees.illinois.replay.db.query;

import org.nees.illinois.replay.data.NumberOfColumns;
import org.nees.illinois.replay.data.RateType;

public class SavedTableQuery extends NumberOfColumns {
	private final String select;
	public SavedTableQuery(String select, int dataColumns, RateType rate) {
		super(dataColumns, rate);
		this.select = select;
	}

	/**
	 * @return the select
	 */
	public String getSelect() {
		return select;
	}
	public SavedTableQuery cloneWithTimeConstraint(String timeConstraint) {
		return new SavedTableQuery(select + timeConstraint, dataColumns, rate);
	}
}
