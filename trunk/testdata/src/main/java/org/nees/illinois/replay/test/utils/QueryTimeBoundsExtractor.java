package org.nees.illinois.replay.test.utils;

import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.common.types.TimeBounds;
import org.nees.illinois.replay.common.types.TimeBoundsI;
import org.nees.illinois.replay.data.DoubleMatrixI;
import org.nees.illinois.replay.events.EventI;
import org.nees.illinois.replay.test.utils.types.TimeBoundaryTestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create time boundary instances based on test queries.
 * @author Michael Bletzinger
 */
public class QueryTimeBoundsExtractor {
	/**
	 * Query set size ratio compared to the originating dataset.
	 */
	private final double qratio = 0.3;
	/**
	 * Index of the starting row or event.
	 */
	private int startIdx;
	/**
	 * Index of the last row or event.
	 */
	private int stopIdx;
	/**
	 * Data from which the query time boundaries are extracted.
	 */
	private final TestDataset data;

	/**
	 * Logger.
	 **/
	private final Logger log = LoggerFactory
			.getLogger(QueryTimeBoundsExtractor.class);

	/**
	 * @param data
	 *            Data from which the query time boundaries are extracted.
	 */
	public QueryTimeBoundsExtractor(final TestDataset data) {
		this.data = data;
	}

	/**
	 * Calculate the index boundaries of a query.
	 * @param useEvents
	 *            Use the event list if true.
	 */
	private void calculateBounds(final boolean useEvents) {
		int size = getSize(useEvents);
		int qsize = (int) Math.round(size * qratio);
		int buffer = Math.round((size - qsize) / 2);
		startIdx = buffer;
		stopIdx = buffer + qsize;
		log.debug("Indexes start " + startIdx + " and stop " + stopIdx);
	}

	/**
	 * @return time boundaries based on a single event.
	 */
	private TimeBoundsI createEvent() {
		List<String> list = new ArrayList<String>();
		EventI e = data.getEvent(startIdx);
		list.add(e.getName());
		return new TimeBounds(list);
	}

	/**
	 * @return time boundaries based on start and stop events.
	 */
	private TimeBoundsI createEventBounds() {
		List<EventI> events = data.getEvents().getEvents();
		String startName = events.get(startIdx).getName();
		String stopName = events.get(stopIdx).getName();
		double start = events.get(startIdx).getTime();
		double stop = events.get(stopIdx).getTime();
		return new TimeBounds(start, startName, stop, stopName);
	}

	/**
	 * @return time boundaries based on an event list.
	 */
	private TimeBoundsI createEventList() {
		List<String> list = new ArrayList<String>();
		List<EventI> events = data.getEvents().getEvents()
				.subList(startIdx, stopIdx + 1);
		for (EventI e : events) {
			list.add(e.getName());
		}
		return new TimeBounds(list);
	}

	/**
	 * @return time boundaries based on a single time.
	 */
	private TimeBoundsI createSingleTime() {
		DoubleMatrixI dm = data.getData();
		double start = dm.value(startIdx, 0);
		double [] list = { start };
		return new TimeBounds(list);
	}

	/**
	 * @param noStopTime
	 *            Sets only the start time to indicate a continuous block to the end.
	 * @return time boundaries based on start and stop times.
	 */
	private TimeBoundsI createTimeBounds(final boolean noStopTime) {
		DoubleMatrixI dm = data.getData();
		double start = dm.value(startIdx, 0);
		double stop = dm.value(stopIdx, 0);
		if (noStopTime) {
			stop = Double.NaN;
			stopIdx = dm.sizes()[0] - 1;
		}
		log.debug("Timestamps start " + start + " and stop " + stop);
		return new TimeBounds(start, stop);
	}

	/**
	 * Return the size of the dataset.
	 * @param useEvents
	 *            use the event list.
	 * @return the size.
	 */
	private int getSize(final boolean useEvents) {
		if (useEvents) {
			return data.getEvents().getEvents().size();
		}
		int[] sz = data.getData().sizes();
		return sz[0];
	}

	/**
	 * @return the startIdx
	 */
	public final int getStartIdx() {
		return startIdx;
	}

	/**
	 * @return the stopIdx
	 */
	public final int getStopIdx() {
		return stopIdx;
	}

	/**
	 * Calculate query time boundary parameters based on the size of the data
	 * set.
	 * @param qrt
	 *            Type of boundaries to extract.
	 * @return boundaries of the query set.
	 */
	public final TimeBoundsI getTimeBounds(final TimeBoundaryTestType qrt) {
		switch (qrt) {
		case ContWithStartStop:
			calculateBounds(false);
			return createTimeBounds(false);
		case ContWithTime:
			calculateBounds(false);
			return createSingleTime();
		case EventsWithStartStop:
			calculateBounds(true);
			return createEventList();
		case Event:
			calculateBounds(true);
			return createEvent();
		case ContWithEventStartStop:
			calculateBounds(true);
			return createEventBounds();
		case ContWithStart2End:
			calculateBounds(false);
			return createTimeBounds(true);
		default:
			log.error(qrt + " not recognized");
			return null;
		}
	}

	/**
	 * @param startIdx
	 *            the startIdx to set
	 */
	public final void setStartIdx(final int startIdx) {
		this.startIdx = startIdx;
	}

	/**
	 * @param stopIdx
	 *            the stopIdx to set
	 */
	public final void setStopIdx(final int stopIdx) {
		this.stopIdx = stopIdx;
	}
}
