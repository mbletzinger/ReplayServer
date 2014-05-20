package org.nees.illinois.replay.test.utils;

import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.common.types.TimeBounds;
import org.nees.illinois.replay.common.types.TimeBoundsI;
import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.DoubleMatrixI;
import org.nees.illinois.replay.data.SubsetCarver;
import org.nees.illinois.replay.events.EventI;
import org.nees.illinois.replay.events.EventListI;
import org.nees.illinois.replay.test.utils.tricks.Events2DataRows;
import org.nees.illinois.replay.test.utils.types.QueryRowDataTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class which generated expected data from various test queries.
 * @author Michael Bletzinger
 */
public class QueryDataRowsExtractor {
	/**
	 * Data from which the query data is extracted.
	 */
	private final TestDataset data;
	/**
	 * Logger.
	 **/
	private final Logger log = LoggerFactory
			.getLogger(QueryDataRowsExtractor.class);
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
	 * @param data
	 *            Data from which the query data is extracted.
	 */
	public QueryDataRowsExtractor(final TestDataset data) {
		this.data = data;
	}

	/**
	 * Calculate the index boundaries of a query.
	 *@param useEvents  Use the event list if true.
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
	 * Carve out a set of query records around the middle of the data set.
	 * @return a double matrix subset.
	 */
	private DoubleMatrixI getContinuousRecordChunk() {
		calculateBounds(false);
		DoubleMatrixI dm = data.getData();
		SubsetCarver carve = new SubsetCarver(dm);
		carve.setStartRow(startIdx);
		carve.setStopRow(stopIdx);
		return carve.subset();
	}

	/**
	 * Carve out a set of query records around the middle of the data set.
	 * @return a double matrix subset.
	 */
	private DoubleMatrixI getEventRecordChunk() {
		calculateBounds(true);
		DoubleMatrixI dm = data.getData();
		SubsetCarver carve = new SubsetCarver(dm);
		EventI startEvent = data.getEvent(startIdx);
		EventI stopEvent = data.getEvent(stopIdx);
		int strt = data.getData().timeIndex(startEvent.getTime());
		int stp = data.getData().timeIndex(stopEvent.getTime());
		carve.setStartRow(strt);
		carve.setStopRow(stp);
		return carve.subset();

	}

	/**
	 * Return a matrix of data where each row corresponds to an event within the
	 * event bounds.
	 * @return matrix of data.
	 */
	private DoubleMatrixI getEventRecords() {
		EventListI events = data.getEvents();
		DoubleMatrixI dm = data.getData();
		TimeBoundsI boundaries = getTimeBounds(true);
		List<EventI> list = events.slice(boundaries);
		Events2DataRows e2dr = new Events2DataRows(dm);
		return e2dr.getData(list);
	}

	/**
	 * Get expected query records based on the {@link QueryRowDataTypes}.
	 * @param qpt
	 *            query parameter type.
	 * @return a double matrix that is expected from this query parameter type.
	 */
	public final DoubleMatrixI getExpected(final QueryRowDataTypes qpt) {
		DoubleMatrixI result = null;
		switch (qpt) {
		case ContWithStartStop:
			result = getContinuousRecordChunk();
			break;
		case ContWithEventStartStop:
			result = getEventRecordChunk();
			break;
		case ContWithTime:
			result = getSingleContinuousRecord();
			break;
		case Event:
			result = getSingleEventRecord();
			break;
		case EventsWithStartStop:
			result = getEventRecords();
			break;
		default:
			log.error("Don't know about query parameter type " + qpt);
		}
		if (result == null) {
			return null;
		}
		return result;
	}

	/**
	 * Return a row of data corresponding to a timestamp.
	 * @return A double matrix containing one row of data.
	 */
	private DoubleMatrixI getSingleContinuousRecord() {
		calculateBounds(false);
		List<Double> row = data.getData().toList().get(startIdx);
		List<List<Double>> result = new ArrayList<List<Double>>();
		result.add(row);
		return new DoubleMatrix(result);
	}

	/**
	 * Return a data row corresponding to an event occurrence.
	 * @return double matrix containing one row.
	 */
	private DoubleMatrixI getSingleEventRecord() {
		calculateBounds(true);
		List<Double> row = data.getEventData(startIdx);
		List<List<Double>> result = new ArrayList<List<Double>>();
		result.add(row);
		return new DoubleMatrix(result);
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
	 * Calculate query time boundary parameters based on the size of the data
	 * set.
	 * @param useEvents
	 *            True if the boundaries are for the event list.
	 * @return boundaries of the query set.
	 */
	public final TimeBoundsI getTimeBounds(final boolean useEvents) {
		double start;
		double stop;
		calculateBounds(useEvents);
		if (useEvents) {
			List<EventI> events = data.getEvents().getEvents();
			String startName = events.get(startIdx).getName();
			String stopName = events.get(stopIdx).getName();
			start = events.get(startIdx).getTime();
			stop = events.get(stopIdx).getTime();
			return new TimeBounds(start, stop, startName, stopName);
		} else {
			DoubleMatrixI dm = data.getData();
			start = dm.value(startIdx, 0);
			stop = dm.value(stopIdx, 0);
			log.debug("Timestamps start " + start + " and stop " + stop);
			return new TimeBounds(start, stop);
		}
	}
}
