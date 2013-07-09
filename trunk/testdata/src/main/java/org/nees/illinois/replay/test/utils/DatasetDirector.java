package org.nees.illinois.replay.test.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nees.illinois.replay.common.registries.ChannelNameRegistry;
import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.DoubleMatrixI;
import org.nees.illinois.replay.data.RateType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

/**
 * Class which sets up test data for unit and integration testing.
 * @author Michael Bletzinger
 */
public class DatasetDirector {
	/**
	 * Choice of two test experiments.
	 * @author Michael Bletzinger
	 */
	public enum ExperimentNames {
		/**
		 * First test experiment.
		 */
		HybridMasonry1,
		/**
		 * Second experiment if needed.
		 */
		HybridMasonry2
	}

	/**
	 * Query test types.
	 * @author Michael Bletzinger
	 */
	public enum QueryParaTypes {
		/**
		 * Include a single dataset based on a time stamp.
		 */
		ContWithTime,
		/**
		 * Include a slice of continuous data from start to stop.
		 */
		ContWithStartStop,
		/**
		 * Include a single data set based on the event.
		 */
		Event,
		/**
		 * Include all event data between two events.
		 */
		EventsWithStartStop
	}

	/**
	 * Wrapper for start and stop times or events.
	 * @author Michael Bletzinger
	 */
	public class TimeSpec {
		/**
		 * Generic start time or event.
		 */
		private final Object start;
		/**
		 * Generic stop time or event.
		 */
		private final Object stop;

		/**
		 * @param start
		 *            Generic start time or event.
		 * @param stop
		 *            Generic stop time or event.
		 */
		public TimeSpec(final Object start, final Object stop) {
			super();
			this.start = start;
			this.stop = stop;
		}

		/**
		 * @return the start
		 */
		public final Object getStart() {
			return start;
		}

		/**
		 * @return the stop
		 */
		public final Object getStop() {
			return stop;
		}

	}

	/**
	 * {@link ChannelListTestMaps} used for query testing.
	 */
	private final ChannelListTestMaps cltm;
	/**
	 * The {@link ChannelNameRegistry} that is supposed to exist after testing.
	 */
	private final ChannelNameRegistry expectedCnr = new ChannelNameRegistry();
	/**
	 * The experiment associated with this dataset.
	 */
	private final ExperimentNames experiment;
	/**
	 * Logger.
	 */
	private final Logger log = LoggerFactory.getLogger(DatasetDirector.class);;
	/**
	 * Map of rate types to the various test queries.
	 */
	private final Map<QueryParaTypes, RateType> queryRates = new HashMap<QueryParaTypes, RateType>();;
	/**
	 * Map of table sizes for the various test queries.
	 */
	private final Map<QueryParaTypes, Integer> queryTableSizes = new HashMap<QueryParaTypes, Integer>();
	/**
	 * Map of start/stop time specifications for the various test queries.
	 */
	private final Map<QueryParaTypes, TimeSpec> queryTimes = new HashMap<DatasetDirector.QueryParaTypes, DatasetDirector.TimeSpec>();
	{
		final double startTime = 222.0;
		final double stopTime = 223.0;
		final int twentyRecords = 20;
		final int tenRecords = 10;
		final int fortyRecords = 40;
		final int fiveRecords = 5;
		queryTableSizes.put(QueryParaTypes.ContWithTime, twentyRecords);
		queryTableSizes.put(QueryParaTypes.ContWithStartStop, tenRecords);
		queryTableSizes.put(QueryParaTypes.Event, fortyRecords);
		queryTableSizes.put(QueryParaTypes.EventsWithStartStop, fiveRecords);

		queryRates.put(QueryParaTypes.ContWithTime, RateType.TIME);
		queryRates.put(QueryParaTypes.ContWithStartStop, RateType.TIME);
		queryRates.put(QueryParaTypes.Event, RateType.EVENT);
		queryRates.put(QueryParaTypes.EventsWithStartStop, RateType.EVENT);

		queryTimes.put(QueryParaTypes.ContWithTime, new TimeSpec(new Double(
				startTime), null));
		queryTimes.put(QueryParaTypes.ContWithStartStop, new TimeSpec(
				new Double(startTime), new Double(stopTime)));
		queryTimes.put(QueryParaTypes.Event, new TimeSpec(null, null));
		// queryTimes.put(QueryParaTypes.EventsWithStartStop, new TimeSpec(
		// new StepNumber(1.0, 0.0, 1.0, null, null, null), new StepNumber(3.0,
		// 22.0, 1.0, null, null, null)));
	}

	/**
	 * @param experiment
	 *            The experiment associated with this dataset.
	 */
	public DatasetDirector(final ExperimentNames experiment) {
		super();
		this.experiment = experiment;
		this.cltm = new ChannelListTestMaps(
				experiment.equals(ExperimentNames.HybridMasonry2),
				experiment.name());
		this.cltm.fillCnr(expectedCnr);
	}

	/**
	 * Check to see if the list of channels is expected.
	 * @param typ
	 *            Type of query
	 * @param channels
	 *            Channels to check.
	 */
	public final void checkChannels(final ChannelListType typ,
			final List<String> channels) {
		checkChannels(cltm.getChannels(typ), channels);
	}

	/**
	 * Check to see if the list of channels is expected.
	 * @param expected
	 *            List of expected channels.
	 * @param actual
	 *            List of actual channels.
	 */
	public final void checkChannels(final List<String> expected,
			final List<String> actual) {
		log.debug("CHECKING  " + expected + "\nWITH " + actual);
		Assert.assertEquals(actual.size(), expected.size());
		for (int i = 0; i < actual.size(); i++) {
			Assert.assertEquals(actual.get(i), expected.get(i));
		}
	}

	/**
	 * Checks to see if the data matrix is as expected.
	 * @param experiment
	 *            Name of experiment data set.
	 * @param qt
	 *            Query test type.
	 * @param quy
	 *            Test query.
	 * @param data
	 *            Data being checked.
	 */
	public final void checkData(final ExperimentNames experiment,
			final QueryParaTypes qt, final ChannelListType quy,
			final DoubleMatrixI data) {
		DoubleMatrixI expected = generate(experiment, qt, quy);
		log.debug("For " + qt + " and " + quy);
		log.debug("CHECKING  expected " + expected + "\nWITH actual " + data);
		DoubleArrayDataGenerator
				.compareData(data.getData(), expected.getData());
	}

	/**
	 * Check if the channel name registry is as expected.
	 * @param experiment
	 *            Name of the experiment data set.
	 * @param cnr
	 *            Channel name registry to be checked.
	 */
	public final void checkExpectedCnr(final ExperimentNames experiment,
			final ChannelNameRegistry cnr) {
		log.debug("CHECKING  expected " + expectedCnr + "\nWITH actual " + cnr);
		Assert.assertEquals(cnr.toString(), expectedCnr.toString());
	}

	/**
	 * Generate a data matrix for the specified parameters.
	 * @param experiment
	 *            Experiment data set.
	 * @param qt
	 *            Query test type
	 * @param quy
	 *            Test query.
	 * @return matrix of data.
	 */
	public final DoubleMatrixI generate(final ExperimentNames experiment,
			final QueryParaTypes qt, final ChannelListType quy) {
		int row = queryTableSizes.get(qt);
		List<String> channels = cltm.getChannels(quy);
		RateType rate = queryRates.get(qt);
		return generate(quy.name(), row, channels.size(), rate);
	}

	/**
	 * Generate a matrix of doubles based on the specified parameters.
	 * @param name
	 *            Name of the query.
	 * @param rows
	 *            Number of rows.
	 * @param cols
	 *            Number of columns.
	 * @param rate
	 *            Sampling rate.
	 * @return Matrix of doubles.
	 */
	private DoubleMatrixI generate(final String name, final int rows,
			final int cols, final RateType rate) {
		final double startTime = 222.0;
		final double interval = 0.02;
		// log.debug("For " + rate + " query " + name + " creating " + rows +
		// "x"
		// + cols + " matrix");
		DoubleArrayDataGenerator dg = new DoubleArrayDataGenerator(rows, cols,
				interval, startTime);
		double[][] data = dg.generate();
		DoubleMatrixI result = new DoubleMatrix(data);
		log.debug("For " + rate + " list type " + name + " creating " + result);
		return result;
	}

	/**
	 * Create a {@link ChannelDataGenerator} instance based on the specified
	 * parameters.
	 * @param quy
	 *            Test query.
	 * @param qt
	 *            Query type.
	 * @param rowMix
	 *            Mix type for the rows.
	 * @return Instance of ChannelDataGenerator.
	 */
	public final ChannelDataGenerator generateQueryData(
			final ChannelListType quy, final QueryParaTypes qt,
			final MatrixMixType rowMix) {
		QueryChannelLists qctl = cltm.getChannelLists(quy);
		int row = queryTableSizes.get(qt);
		return new ChannelDataGenerator(qctl, rowMix, row);
	}

	/**
	 * @return the cltm
	 */
	public final ChannelListTestMaps getCltm() {
		return cltm;
	}

	/**
	 * @return the expectedCnr
	 */
	public final ChannelNameRegistry getExpectedCnr() {
		return expectedCnr;
	}

	/**
	 * @return the experiment
	 */
	public final ExperimentNames getExperiment() {
		return experiment;
	}

	/**
	 * @return the queryRates
	 */
	public final Map<QueryParaTypes, RateType> getQueryRates() {
		return queryRates;
	}

	/**
	 * @return the queryTableSize
	 */
	public final Map<QueryParaTypes, Integer> getQueryTableSizes() {
		return queryTableSizes;
	}

	/**
	 * @return the queryTimes
	 */
	public final Map<QueryParaTypes, TimeSpec> getQueryTimes() {
		return queryTimes;
	}

	/**
	 * Return the rate based on the query type.
	 * @param qt
	 *            Query type.
	 * @return rate.
	 */
	public final RateType getRate(final QueryParaTypes qt) {
		return queryRates.get(qt);
	}

	/**
	 * Return a {@link TimeSpec} based on the query type.
	 * @param qt
	 *            query type.
	 * @return Times for the query.
	 */
	public final TimeSpec getTimes(final QueryParaTypes qt) {
		return queryTimes.get(qt);
	}
}
