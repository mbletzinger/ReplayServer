package org.nees.illinois.replay.data;

import java.util.ArrayList;
import java.util.List;

public class NumberOfColumns {
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "/dataColumns=" + dataColumns + "/headers="
				+ headers;
	}

	public static int dataColumns(int size, RateType rate) {
		return size - 4;// make sure this matches the current header array size
	}

	protected final int dataColumns;
	protected final List<String> headers = new ArrayList<String>();
	protected final RateType tableRate;
	{
		headers.add("time");
		headers.add("step");
		headers.add("substep");
		headers.add("correctionstep");

	}

	public NumberOfColumns(int dataColumns, RateType tableRate) {
		this.dataColumns = dataColumns;
		this.tableRate = tableRate;
	}

	/**
	 * @return the headers
	 */
	public List<String> getHeaders() {
		return headers;
	}

	public int getNumber(boolean withTime) {
		int result = dataColumns;
		if (withTime) {
			result += headers.size();
		}
		return result;
	}

	/**
	 * @return the rate
	 */
	public RateType getTableRate() {
		return tableRate;
	}

	public int getTimeNumber() {
		return headers.size();
	}
}
