package org.nees.illinois.replay.db.query;

import org.nees.illinois.replay.data.NumberOfColumns;
import org.nees.illinois.replay.data.RateType;

public class NumberOfColumnsWithSelect extends NumberOfColumns {
	/* (non-Javadoc)
	 * @see org.nees.illinois.replay.data.NumberOfColumns#toString()
	 */
	@Override
	public String toString() {
		String result = super.toString();
		result += "/select=" + select;
		return result;
	}
	private final String select;
	public NumberOfColumnsWithSelect(String select, int dataColumns, RateType rate) {
		super(dataColumns, rate);
		this.select = select;
	}

	/**
	 * @return the select
	 */
	public String getSelect() {
		return select;
	}
	public NumberOfColumnsWithSelect cloneWithTimeConstraint(String timeConstraint) {
		return new NumberOfColumnsWithSelect(select + timeConstraint, dataColumns, tableRate);
	}
}
