package org.nees.illinois.replay.test.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nees.illinois.replay.common.registries.ChannelNameRegistry;
import org.nees.illinois.replay.common.registries.TableRegistry;
import org.nees.illinois.replay.common.types.TableDefinitionI;
import org.nees.illinois.replay.common.types.TimeBounds;
import org.nees.illinois.replay.common.types.TimeBoundsI;
import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.DoubleMatrixI;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.events.EventListI;
import org.nees.illinois.replay.events.EventType;
import org.nees.illinois.replay.test.utils.gen.ChannelDataGenerator;
import org.nees.illinois.replay.test.utils.gen.DoubleArrayDataGenerator;
import org.nees.illinois.replay.test.utils.gen.EventsGenerator;
import org.nees.illinois.replay.test.utils.gen.QueryChannelListsForMerging;
import org.nees.illinois.replay.test.utils.types.ExperimentNames;
import org.nees.illinois.replay.test.utils.types.MatrixMixType;
import org.nees.illinois.replay.test.utils.types.QueryRowDataTypes;
import org.nees.illinois.replay.test.utils.types.TestDatasetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

/**
 * Class which sets up test data for unit and integration testing.
 * @author Michael Bletzinger
 */
public class QuerySetsDirector {

	/**
	 * List checker.
	 */
	private final CompareLists<String> check = new CompareLists<String>();

	/**
	 * {@link TestDatasetParameters} used for query testing.
	 */
	private final TestDatasetParameters set;
	/**
	 * {@link QueryDataRowsExtractor} used to generate expected query results.
	 */
	private final QueryDataRowsExtractor qsets;
	/**
	 * The {@link ChannelNameRegistry} that is supposed to exist after testing.
	 */
	private final ChannelNameRegistry expectedCnr = new ChannelNameRegistry();
	/**
	 * The {@link TableRegistry} that is supposed to exist after testing.
	 */
	private final TableRegistry expectedTblr = new TableRegistry();
	/**
	 * The experiment associated with this dataset.
	 */
	private final ExperimentNames experiment;
	/**
	 * Logger.
	 */
	private final Logger log = LoggerFactory.getLogger(QuerySetsDirector.class);
	private final Map<TestDatasetType, QueryDataRowsExtractor> querySets = new HashMap<TestDatasetType, QueryDataRowsExtractor>();
	
	/**
	 * @param experiment
	 *            The experiment associated with this dataset.
	 */
	public QuerySetsDirector(final ExperimentNames experiment) {
		super();
		this.experiment = experiment;
		this.set = new TestDatasetParameters(
				experiment.equals(ExperimentNames.HybridMasonry2),
				experiment.name());
		this.set.fillCnr(expectedCnr);
		this.set.fillTblr(expectedTblr);
		
	}

	/**
	 * Check to see if the list of channels is expected.
	 * @param typ
	 *            Type of query
	 * @param channels
	 *            Channels to check.
	 */
	public final void checkChannels(final TestDatasetType typ,
			final List<String> channels) {
		checkChannels(set.getChannels(typ), channels);
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
			final QueryRowDataTypes qt, final TestDatasetType quy,
			final DoubleMatrixI data) {
		DoubleMatrixI expected = generate(experiment, qt, quy);
		log.debug("For " + qt + " and " + quy);
		log.debug("CHECKING  expected " + expected + "\nWITH actual " + data);
		DoubleArrayDataGenerator
				.compareData(data.getData(), expected.getData());
	}

	/**
	 * Check if the channel name registry is as expected.
	 * @param cnr
	 *            Channel name registry to be checked.
	 */
	public final void checkExpectedCnr(final ChannelNameRegistry cnr) {
		check.compare(cnr.getNames(), expectedCnr.getNames());
	}

	/**
	 * Check to see if the table registry has the expected entries.
	 * @param actual
	 *            The registry to be checked.
	 */
	public final void checkExpectedTableRegistry(final TableRegistry actual) {
		check.compare(actual.getNames(), expectedTblr.getNames());
		for (String exname : expectedTblr.getNames()) {
			log.debug("Checking table " + exname);
			checkTableDefinitions(actual.getTable(exname),
					expectedTblr.getTable(exname));
		}
	}

	/**
	 * Compares two table definitions.
	 * @param actual
	 *            first.
	 * @param expected
	 *            second.
	 */
	private void checkTableDefinitions(final TableDefinitionI actual,
			final TableDefinitionI expected) {
		Assert.assertEquals(actual.getTableId(), expected.getTableId());
		Assert.assertEquals(actual.getNumberOfColumns(true),
				expected.getNumberOfColumns(true));
		check.compare(actual.getColumns(true), expected.getColumns(true));
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
	public final TestDataset generate(final ExperimentNames experiment,
			final QueryRowDataTypes qt, final TestDatasetType quy) {
		int row = queryTableSizes.get(qt);
		List<String> channels = set.getChannels(quy);
		DoubleMatrixI result = generate(quy.name(), row, channels.size(), null);
		switch(qt) {
		case ContWithStartStop:
		case ContWithTime:
			return result;
		case Event:
		case EventsWithStartStop:
			EventListI ev = generate(quy.name(), result, set.getTableName(quy));
			
		}
	}

	/**
	 * Generate a matrix of doubles based on the specified parameters.
	 * @param name
	 *            Name of the query.
	 * @param rows
	 *            Number of rows.
	 * @param cols
	 *            Number of columns.
	 * @param startTime TODO
	 * @return Matrix of doubles.
	 */
	private DoubleMatrixI generate(final String name, final int rows,
			final int cols, double startTime) {
		DoubleArrayDataGenerator dg = new DoubleArrayDataGenerator(rows, cols,
				timeInterval, startTime);
		double[][] data = dg.generate();
		DoubleMatrixI result = new DoubleMatrix(data);
		log.debug("For list type " + name + " creating " + result);
		return result;
	}

	/**
	 * Generate events based on the data set.
	 * @param name
	 *            Name of query.
	 * @param data
	 *            Dataset from which event records are extracted.
	 * @param source
	 *            Source of the event list.
	 * @return Event list.
	 */
	private EventListI generate(final String name, final DoubleMatrixI data,
			final String source) {
		EventsGenerator gen = new EventsGenerator(EventType.StepNumber);
		return gen.generate(data, eventInterval, source, false);
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
			final TestDatasetType quy, final QueryRowDataTypes qt,
			final MatrixMixType rowMix) {
		QueryChannelListsForMerging qctl = set.getTestQuery(quy);
		int row = queryTableSizes.get(qt);
		return new ChannelDataGenerator(qctl, rowMix, row);
	}

	/**
	 * @return the cltm
	 */
	public final TestDatasetParameters getSet() {
		return set;
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
	public final Map<QueryRowDataTypes, RateType> getQueryRates() {
		return queryRates;
	}

	/**
	 * @return the queryTableSize
	 */
	public final Map<QueryRowDataTypes, Integer> getQueryTableSizes() {
		return queryTableSizes;
	}

	/**
	 * @return the queryTimes
	 */
	public final Map<QueryRowDataTypes, TimeBounds> getQueryTimes() {
		return queryTimes;
	}

	/**
	 * Return the rate based on the query type.
	 * @param qt
	 *            Query type.
	 * @return rate.
	 */
	public final RateType getRate(final QueryRowDataTypes qt) {
		return queryRates.get(qt);
	}

	/**
	 * Return a {@link TimeBounds} based on the query type.
	 * @param qt
	 *            query type.
	 * @return Times for the query.
	 */
	public final TimeBoundsI getTimes(final QueryRowDataTypes qt) {
		return queryTimes.get(qt);
	}
}
