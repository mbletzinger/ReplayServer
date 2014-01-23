package org.nees.illinois.replay.test.utils.gen;

import java.util.List;


/**
 * Class which wraps a data row from a {@link DoubleMatrix} into a
 * {@link Comparable} object. The first column of the row is compared.
 * @author Michael Bletzinger
 */
public class DataRow implements Comparable<DataRow> {
	/**
	 * Data row.
	 */
	private final List<Double> data;

	/**
	 * @param data
	 *            row of data.
	 */
	public DataRow(final List<Double> data) {
		super();
		this.data = data;
	}

	@Override
	public final int compareTo(final DataRow other) {
		Double me = data.get(0);
		Double him = other.data.get(0);
		if (me == null) {
			return -1;
		}
		if (him == null) {
			return 1;
		}
		return me.compareTo(him);
	}

	/**
	 * @return the data
	 */
	public final List<Double> getData() {
		return data;
	}

}
