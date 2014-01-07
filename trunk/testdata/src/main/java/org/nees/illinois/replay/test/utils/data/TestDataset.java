package org.nees.illinois.replay.test.utils.data;

import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.DoubleMatrixI;
import org.nees.illinois.replay.events.EventListI;
import org.nees.illinois.replay.test.utils.types.TimeSpec;

/**
 * Class containing test data for continuous and event queries.
 * @author Michael Bletzinger
 */
public class TestDataset {
	/**
	 * Source string.
	 */
	private final String source;
	/**
	 * Continuous data.
	 */
	private final DoubleMatrixI data;
	/**
	 * Event data.
	 */
	private final EventListI events;
	/**
	 * Time tolerance for conditionals.
	 */
	private final double timeTolerance = 0.0005;

	/**
	 * @param data
	 *            Continuous data.
	 * @param events
	 *            Event data.
	 * @param source
	 *            Source of the data
	 */
	public TestDataset(final DoubleMatrixI data, final EventListI events,
			final String source) {
		this.data = data;
		this.events = events;
		this.source = source;
	}

	/**
	 * @return the data
	 */
	public final DoubleMatrixI getData() {
		return data;
	}

	/**
	 * @return the events
	 */
	public final EventListI getEvents() {
		return events;
	}

	/**
	 * @return the source
	 */
	public final String getSource() {
		return source;
	}

	/**
	 * Get the times for all of the events between the times defined by the
	 * bounds.
	 * @param bounds
	 *            start and stop indexes in the event list.
	 * @return the timeline.
	 */
	public final List<Double> getTimeline(final TimeSpec<Integer> bounds) {
		List<Double> timeline = events.getTimeline();
		int start = bounds.getStart();
		int stop = bounds.getStop();
		return timeline.subList(start, stop);
	}

	/**
	 * Transform event index bounds to timestamp bounds for the continuous data.
	 * @param bounds
	 *            the event index start and stop bounds.
	 * @return The equivalent timestamp bounds.
	 */
	public final TimeSpec<Double> getTimestampBounds(final TimeSpec<Integer> bounds) {
		List<Double> timeline = events.getTimeline();
		int start = bounds.getStart();
		int stop = bounds.getStop();
		return new TimeSpec<Double>(timeline.get(start), timeline.get(stop));
	}

	/**
	 * Get the continuous data record corresponding to an event index.
	 * @param event
	 *            index
	 * @return the data record.
	 */
	public final List<Double> getEventData(final int event) {
		for (List<Double> row : data.toList()) {
			if (Math.abs(row.get(0) - event) < timeTolerance) {
				return row;
			}
		}
		return null;
	}

	/**
	 * Get continuous data between the bounds defined by the event index bounds.
	 * @param bounds
	 *            the start and stop index bounds.
	 * @return a double matrix of data.
	 */
	public final DoubleMatrixI getEventData(final TimeSpec<Integer> bounds) {
		List<Double> timeline = getTimeline(bounds);
		List<List<Double>> result = new ArrayList<List<Double>>();
		for (List<Double> row : data.toList()) {
			for (double t : timeline) {
				if (Math.abs(row.get(0) - t) < timeTolerance) {
					result.add(row);
					break;
				}
			}
		}
		return new DoubleMatrix(result);
	}
}
