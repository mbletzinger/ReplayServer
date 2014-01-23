package org.nees.illinois.replay.test.utils.data.tricks;

import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.DoubleMatrixI;
import org.nees.illinois.replay.events.EventI;

/**
 * Class that looks up event timestamps in the continuous and returns the
 * corresponding rows.
 * @author Michael Bletzinger
 */
public class Events2DataRows {
	/**
	 * Data set to get event rows from.
	 */
	private final DoubleMatrixI dm;

	/**
	 * @param dm
	 *            Data set to get event rows from.
	 */
	public Events2DataRows(final DoubleMatrixI dm) {
		this.dm = dm;
	}

	/**
	 * Return the corresponding rows from an event list based on the timestamps.
	 * @param elist
	 *            The event list.
	 * @return matrix of doubles.
	 */
	public final DoubleMatrixI getData(final List<EventI> elist) {
		List<List<Double>> data = new ArrayList<List<Double>>();
		for (EventI e : elist) {
			int idx = dm.timeIndex(e.getTime());
			List<Double> row = dm.getRow(idx);
			data.add(row);
		}
		return new DoubleMatrix(data);
	}

}
