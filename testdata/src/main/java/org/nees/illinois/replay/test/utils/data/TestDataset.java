package org.nees.illinois.replay.test.utils.data;

import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.common.types.TimeBoundsI;
import org.nees.illinois.replay.data.DoubleMatrixI;
import org.nees.illinois.replay.events.EventI;
import org.nees.illinois.replay.events.EventListI;

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
	 * Transform event index bounds into events.
	 * @param bounds
	 *            the event index start and stop bounds.
	 * @return The events corresponding to the bounds.
	 */
	public final List<EventI> getEventBounds(final TimeBoundsI bounds) {
		List<EventI> result = new ArrayList<EventI>();
		EventI e = events.find(bounds.getStartName());
		result.add(e);
		e = events.find(bounds.getStopName());
		result.add(e);
		return result;
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
	 * @return the events
	 */
	public final EventListI getEvents() {
		return events;
	}

	/**
	 * Get all of the events between the times defined by the
	 * bounds.
	 * @param bounds
	 *            start and stop indexes in the event list.
	 * @return the event list.
	 */
	public final List<EventI> getEventsSubset(final TimeBoundsI bounds) {
		return events.slice(bounds);
	}

	/**
	 * @return the source
	 */
	public final String getSource() {
		return source;
	}
}
