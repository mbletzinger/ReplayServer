package org.nees.mustsim.replay.data;

import java.util.ArrayList;
import java.util.List;


public class NumberOfColumns {
	protected final int dataColumns;
	protected final List<String> headers = new ArrayList<String>();
	protected final RateType rate;
	public NumberOfColumns(int dataColumns, RateType rate) {
		headers.add("time");
		if (rate.equals(RateType.STEP)) {
			headers.add("step");
			headers.add("substep");
			headers.add("correctionstep");
		}
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
		if(withTime) {
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
	public static int dataColumns(int size, RateType rate) {
		return size - (rate.equals(RateType.CONT) ? 1 : 4);
	}
}
