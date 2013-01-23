package org.nees.illinois.replay.data;

import java.util.ArrayList;
import java.util.List;

public class NumberOfColumns {
	public static int dataColumns(int size, RateType rate) {
		return size - 4;// make sure this matches the current header array size
	}

	protected final int dataColumns;
	protected final List<String> headers = new ArrayList<String>();
	protected final RateType rate;
	{
		headers.add("time");
		headers.add("step");
		headers.add("substep");
		headers.add("correctionstep");

	}

	public NumberOfColumns(int dataColumns, RateType rate) {
		this.dataColumns = dataColumns;
		this.rate = rate;
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
	public RateType getRate() {
		return rate;
	}

	public int getTimeNumber() {
		return headers.size();
	}
}
