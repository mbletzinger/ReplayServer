package org.nees.illinois.replay.test.utils;

import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.common.types.TimeBoundsI;
import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.DoubleMatrixI;
import org.nees.illinois.replay.data.SubsetCarver;
import org.nees.illinois.replay.events.EventI;
import org.nees.illinois.replay.events.EventListI;
import org.nees.illinois.replay.test.utils.tricks.Events2DataRows;
import org.nees.illinois.replay.test.utils.types.TimeBoundaryTestType;
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
	 * Time boundaries extractor.
	 */
	private final QueryTimeBoundsExtractor times;

	/**
	 * @param data
	 *            Data from which the query data is extracted.
	 */
	public QueryDataRowsExtractor(final TestDataset data) {
		this.data = data;
		this.times = new QueryTimeBoundsExtractor(data);
	}

	/**
	 * Carve out a set of query records around the middle of the data set.
	 * @param qpt
	 *            time boundary type.
	 * @return a double matrix subset.
	 */
	private DoubleMatrixI getContinuousRecordChunk(
			final TimeBoundaryTestType qpt) {
		times.getTimeBounds(qpt);
		DoubleMatrixI dm = data.getData();
		SubsetCarver carve = new SubsetCarver(dm);
		carve.setStartRow(times.getStartIdx());
		carve.setStopRow(times.getStopIdx());
		return carve.subset();
	}

	/**
	 * Carve out a set of query records around the middle of the data set.
	 * @param qpt
	 *            time boundary type.
	 * @return a double matrix subset.
	 */
	private DoubleMatrixI getEventRecordChunk(final TimeBoundaryTestType qpt) {
		times.getTimeBounds(qpt);
		DoubleMatrixI dm = data.getData();
		SubsetCarver carve = new SubsetCarver(dm);
		EventI startEvent = data.getEvent(times.getStartIdx());
		EventI stopEvent = data.getEvent(times.getStopIdx());
		int strt = data.getData().timeIndex(startEvent.getTime());
		int stp = data.getData().timeIndex(stopEvent.getTime());
		carve.setStartRow(strt);
		carve.setStopRow(stp);
		return carve.subset();

	}

	/**
	 * Return a matrix of data where each row corresponds to an event within the
	 * event bounds.
	 * @param qpt
	 *            time boundary type.
	 * @return matrix of data.
	 */
	private DoubleMatrixI getEventRecords(final TimeBoundaryTestType qpt) {
		EventListI events = data.getEvents();
		DoubleMatrixI dm = data.getData();
		TimeBoundsI boundaries = times.getTimeBounds(qpt);
		log.debug("slicing events based on " + boundaries);
		List<EventI> list = events.slice(boundaries);
		Events2DataRows e2dr = new Events2DataRows(dm);
		return e2dr.getData(list);
	}

	/**
	 * Get expected query records based on the {@link TimeBoundaryTestType}.
	 * @param qpt
	 *            time boundary type.
	 * @return a double matrix that is expected from this query parameter type.
	 */
	public final DoubleMatrixI getExpected(final TimeBoundaryTestType qpt) {
		DoubleMatrixI result = null;
		switch (qpt) {
		case ContWithStartStop:
			result = getContinuousRecordChunk(qpt);
			break;
		case ContWithEventStartStop:
			result = getEventRecordChunk(qpt);
			break;
		case ContWithTime:
			result = getSingleContinuousRecord(qpt);
			break;
		case Event:
			result = getSingleEventRecord(qpt);
			break;
		case EventsWithStartStop:
			result = getEventRecords(qpt);
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
	 * @param qpt
	 *            time boundary type.
	 * @return A double matrix containing one row of data.
	 */
	private DoubleMatrixI getSingleContinuousRecord(
			final TimeBoundaryTestType qpt) {
		times.getTimeBounds(qpt);
		List<Double> row = data.getData().toList().get(times.getStartIdx());
		List<List<Double>> result = new ArrayList<List<Double>>();
		result.add(row);
		return new DoubleMatrix(result);
	}

	/**
	 * Return a data row corresponding to an event occurrence.
	 * @param qpt
	 *            time boundary type.
	 * @return double matrix containing one row.
	 */
	private DoubleMatrixI getSingleEventRecord(final TimeBoundaryTestType qpt) {
		times.getTimeBounds(qpt);
		List<Double> row = data.getEventData(times.getStartIdx());
		List<List<Double>> result = new ArrayList<List<Double>>();
		result.add(row);
		return new DoubleMatrix(result);
	}

	/**
	 * @return the times
	 */
	public final QueryTimeBoundsExtractor getTimes() {
		return times;
	}
}
