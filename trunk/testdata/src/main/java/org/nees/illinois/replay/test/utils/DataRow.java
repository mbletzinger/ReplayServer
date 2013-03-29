package org.nees.illinois.replay.test.utils;

import java.util.List;

public class DataRow implements Comparable<DataRow> {

	private final List<Double> data;

	public DataRow(List<Double> data) {
		super();
		this.data = data;
	}

	@Override
	public int compareTo(DataRow other) {
		Double me = data.get(0);
		Double him = other.data.get(0);
		if(me == null) {
			return -1;
		}
		if(him == null) {
			return 1;
		}
		return me.compareTo(him);
	}

	/**
	 * @return the data
	 */
	public List<Double> getData() {
		return data;
	}

}
