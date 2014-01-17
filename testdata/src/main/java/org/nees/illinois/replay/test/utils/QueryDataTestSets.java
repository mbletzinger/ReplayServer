package org.nees.illinois.replay.test.utils;

import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.common.types.TimeBoundsI;
import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.DoubleMatrixI;
import org.nees.illinois.replay.events.EventI;
import org.nees.illinois.replay.events.EventListI;
import org.nees.illinois.replay.test.utils.data.Events2DataRows;
import org.nees.illinois.replay.test.utils.data.SubsetCarver;
import org.nees.illinois.replay.test.utils.data.SubsetSlicer;
import org.nees.illinois.replay.test.utils.data.TestDataset;
import org.nees.illinois.replay.test.utils.types.QueryParaTypes;
import org.nees.illinois.replay.test.utils.types.TestDatasetType;
import org.nees.illinois.replay.test.utils.types.TestTimeBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class which generated expected data from various test queries.
 * @author Michael Bletzinger
 */
public class QueryDataTestSets {
	/**
	 * Data from which the query data is extracted.
	 */
	private final TestDataset data;
	/**
	 * Data set type for this instance.
	 */
	private final TestDatasetType dataSetType;
	/**
	 * Logger.
	 **/
	private final Logger log = LoggerFactory.getLogger(QueryDataTestSets.class);
	/**
	 * Query set size ratio compared to the originating dataset.
	 */
	private final double qratio = 0.3;

	/**
	 * Query type for this instance.
	 */
	private final TestDatasetType queryType;
	/**
	 * Channel Mappings.
	 */
	private final TestDatasetParameters tdp;

	/**
	 * @param data
	 *            Data from which the query data is extracted.
	 * @param dataSetType
	 *            Data set type for this instance.
	 * @param tdp
	 *            Channel Mappings.
	 * @param queryType
	 *            Query type for this instance.
	 */
	public QueryDataTestSets(final TestDataset data,
			final TestDatasetType dataSetType,final TestDatasetParameters tdp,
			final TestDatasetType queryType) {
		this.data = data;
		this.dataSetType = dataSetType;
		this.tdp = tdp;
		this.queryType = queryType;
	}

	/**
	 * Calculate query set based on the size of the data set.
	 * @param size
	 *            data set size.
	 * @param useEvents
	 *            True if the boundaries are for the event list.
	 * @return boundaries of the query set.
	 */
	private TestTimeBounds getBoundaries(final int size, final boolean useEvents) {
		int qsize = (int) Math.round(size * qratio);
		int buffer = Math.round((size - qsize) / 2);
		TestTimeBounds result;
		if (useEvents) {
			result = new TestTimeBounds(buffer, qsize, data.getEvents());
		} else {
			result = new TestTimeBounds(buffer, qsize, data.getData());
		}
		return result;
	}

	/**
	 * Map the indexes of the query channel names from the data channel list.
	 * @return list of indexes.
	 */
	private List<Integer> getChannelIndexes() {
		List<Integer> result = new ArrayList<Integer>();
		List<String> queryChannels = tdp.getChannels(queryType);
		List<String> dataChannels = tdp.getChannels(dataSetType);
		for (String c : queryChannels) {
			int index = dataChannels.indexOf(c);
			result.add(new Integer(index));
		}
		return result;
	}

	/**
	 * Carve out a set of query records around the middle of the data set.
	 * @return a double matrix subset.
	 */
	private DoubleMatrixI getContinuousRecordChunk() {
		DoubleMatrixI dm = data.getData();
		int[] sizes = dm.sizes();
		TimeBoundsI boundaries = getBoundaries(sizes[0], false);
		SubsetCarver carve = new SubsetCarver(dm);
		int startIdx = dm.timeIndex(boundaries.getStart());
		int stopIdx = dm.timeIndex(boundaries.getStop());
		carve.setStartRow(startIdx);
		carve.setStopRow(stopIdx);
		return carve.subset();
	}

	/**
	 * Carve out a set of query records around the middle of the data set.
	 * @return a double matrix subset.
	 */
	private DoubleMatrixI getEventRecordChunk() {
		EventListI events = data.getEvents();
		DoubleMatrixI dm = data.getData();
		TestTimeBounds boundaries = getBoundaries(events.getTimeline()
				.size(), true);
		List<EventI> list = events.slice(boundaries);
		SubsetCarver carve = new SubsetCarver(data.getData());
		EventI e = list.get(0);
		int startIdx = dm.timeIndex(e.getTime());
		e = list.get(list.size() - 1);
		int stopIdx = dm.timeIndex(e.getTime());
		carve.setStartRow(startIdx);
		carve.setStopRow(stopIdx);
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
		TestTimeBounds boundaries = getBoundaries(events.getTimeline()
				.size(), true);
		List<EventI> list = events.slice(boundaries);
		Events2DataRows e2dr = new Events2DataRows(dm);
		return e2dr.getData(list);
	}

	/**
	 * Get expected query records based on the {@link QueryParaTypes}.
	 * @param qpt
	 *            query parameter type.
	 * @return a double matrix that is expected from this query parameter type.
	 */
	public final DoubleMatrixI getExpected(final QueryParaTypes qpt) {
		DoubleMatrixI result = null;
		switch (qpt) {
		case ContWithStartStop:
			result = getContinuousRecordChunk();
		case ContWithEventStartStop:
			result = getEventRecordChunk();
		case ContWithTime:
			result = getSingleContinuousRecord();
		case Event:
			result = getSingleEventRecord();
		case EventsWithStartStop:
			result = getEventRecords();
		default:
			log.error("Don't know about query parameter type " + qpt);
		}
		if (result == null) {
			return null;
		}
		return sliceChannelColumns(result);
	}

	/**
	 * Return a row of data corresponding to a timestamp.
	 * @return A double matrix containing one row of data.
	 */
	private DoubleMatrixI getSingleContinuousRecord() {
		int[] sizes = data.getData().sizes();
		TestTimeBounds boundaries = getBoundaries(sizes[0], false);
		List<Double> row = data.getData().toList().get(boundaries.getStartIdx());
		List<List<Double>> result = new ArrayList<List<Double>>();
		result.add(row);
		return new DoubleMatrix(result);
	}

	/**
	 * Return a data row corresponding to an event occurrence.
	 * @return double matrix containing one row.
	 */
	private DoubleMatrixI getSingleEventRecord() {
		TestTimeBounds boundaries = getBoundaries(data.getEvents().getTimeline()
				.size(), true);
		List<Double> row = data.getEventData(boundaries.getStartIdx());
		List<List<Double>> result = new ArrayList<List<Double>>();
		result.add(row);
		return new DoubleMatrix(result);
	}

	/**
	 * Trim the data set columns down to the columns in the query set.
	 * @param original
	 *            data set.
	 * @return trimmed data set.
	 */
	private DoubleMatrixI sliceChannelColumns(final DoubleMatrixI original) {
		List<Integer> cols = getChannelIndexes();
		SubsetSlicer slicer = new SubsetSlicer(original);
		slicer.setSliceColumn(true);
		slicer.addSlices(cols);
		return slicer.slice();
	}
}
