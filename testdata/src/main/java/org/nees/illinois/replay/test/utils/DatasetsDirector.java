package org.nees.illinois.replay.test.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nees.illinois.replay.common.registries.ChannelNameRegistry;
import org.nees.illinois.replay.common.registries.TableRegistry;
import org.nees.illinois.replay.common.types.TableDefinitionI;
import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.DoubleMatrixI;
import org.nees.illinois.replay.events.EventListI;
import org.nees.illinois.replay.test.utils.gen.DerivedTimeGenerator;
import org.nees.illinois.replay.test.utils.gen.DoubleMatrixGenerator;
import org.nees.illinois.replay.test.utils.gen.EventsGenerator;
import org.nees.illinois.replay.test.utils.gen.TimeGenerator;
import org.nees.illinois.replay.test.utils.types.ExperimentNames;
import org.nees.illinois.replay.test.utils.types.MatrixMixType;
import org.nees.illinois.replay.test.utils.types.QueryRowDataTypes;
import org.nees.illinois.replay.test.utils.types.TestDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

/**
 * Class which sets up test data for unit and integration testing.
 * @author Michael Bletzinger
 */
public class DatasetsDirector {

	/**
	 * List checker.
	 */
	private final CompareLists<String> check = new CompareLists<String>();

	/**
	 * {@link TestDatasetParameters} provides the channel lists used for query
	 * testing..
	 */
	private final TestDatasetParameters set;
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
	private final Logger log = LoggerFactory.getLogger(DatasetsDirector.class);

	/**
	 * Map of data sources and their data.
	 */
	private final Map<TestDataSource, TestDataset> dataSets = new HashMap<TestDataSource, TestDataset>();

	/**
	 * Number of data rows.
	 */
	private final int numberOfRows;

	/**
	 * Map of test table types to start times.
	 */
	private final Map<TestDataSource, TimeGenerator> cl2Times = new HashMap<TestDataSource, TimeGenerator>();
	/**
	 * Data start time.
	 */
	private final double startTime = 221.23;
	/**
	 * Time interval for continuous data.
	 */
	private final double timeMultiplier = 0.2;
	/**
	 * Number of records per generated event.
	 */
	private final int eInterval;

	/**
	 * @param experiment
	 *            The experiment associated with this dataset.
	 * @param numberOfRows
	 *            Number of data rows.
	 * @param numberOfEvents
	 *            Number of events.
	 */
	public DatasetsDirector(final ExperimentNames experiment,
			final int numberOfRows,final int numberOfEvents) {
		super();
		this.experiment = experiment;
		this.set = new TestDatasetParameters(
				experiment.equals(ExperimentNames.HybridMasonry2),
				experiment.name());
		this.set.fillCnr(expectedCnr);
		this.set.fillTblr(expectedTblr);
		this.numberOfRows = numberOfRows;
		double interval = numberOfRows * timeMultiplier;
		eInterval = numberOfRows / numberOfEvents;

		double stime = startTime;
		TimeGenerator dtg = new TimeGenerator(timeMultiplier, stime);
		DerivedTimeGenerator d2tg = new DerivedTimeGenerator(
				MatrixMixType.AddAfter, interval);
		TimeGenerator d3tg = d2tg.derive(dtg);
		cl2Times.put(TestDataSource.DAQ2, d3tg);
		d2tg = new DerivedTimeGenerator(MatrixMixType.AddMiddle, interval);
		d3tg = d2tg.derive(dtg);
		cl2Times.put(TestDataSource.Krypton2, d3tg);
		d2tg = new DerivedTimeGenerator(MatrixMixType.AddBefore, interval);
		d3tg = d2tg.derive(dtg);
		cl2Times.put(TestDataSource.OM, d3tg);
		d2tg = new DerivedTimeGenerator(MatrixMixType.AddInterleaved, interval);
		d3tg = d2tg.derive(dtg);
		cl2Times.put(TestDataSource.OM2, d3tg);
		d2tg = new DerivedTimeGenerator(MatrixMixType.AddMerged, interval);
		d3tg = d2tg.derive(dtg);
		cl2Times.put(TestDataSource.Krypton1, d3tg);
		cl2Times.put(TestDataSource.DAQ, dtg);
		for (TestDataSource src : TestDataSource.values()) {
			TestDataset set = createDataset(src);
			dataSets.put(src, set);
		}

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
		for (int i = 0;i < actual.size();i++) {
			Assert.assertEquals(actual.get(i), expected.get(i));
		}
	}

	/**
	 * Check to see if the list of channels is expected.
	 * @param typ
	 *            Type of query
	 * @param channels
	 *            Channels to check.
	 */
	public final void checkChannels(final TestDataSource typ,
			final List<String> channels) {
		checkChannels(set.getChannels(typ), channels);
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
			final QueryRowDataTypes qt, final TestDataSource quy,
			final DoubleMatrixI data) {
		log.debug("For " + qt + " and " + quy);
		QueryDataRowsExtractor extract = extractQueryData(quy);
		DoubleMatrixI expected = extract.getExpected(qt);
		log.debug("CHECKING  expected " + expected + "\nWITH actual " + data);
		DoubleMatrixGenerator.compareData(data.getData(), expected.getData());
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
	 * @param quy
	 *            Test query.
	 * @return matrix of data.
	 */
	private TestDataset createDataset(final TestDataSource quy) {
		List<String> channels = set.getChannels(quy);
		DoubleMatrixGenerator dmg = new DoubleMatrixGenerator(numberOfRows,
				channels.size(), getTimesGen(quy));
		double[][] cdat = dmg.generate();
		DoubleMatrixI dm = new DoubleMatrix(cdat);
		EventsGenerator eg = new EventsGenerator();
		EventListI elist = eg.generate(dm, eInterval, quy.toString(), false);
		return new TestDataset(dm, elist, quy.toString());
	}

	/**
	 * create an extraction object which generates expected data from a query.
	 * @param src
	 *            indicates which dataset to use.
	 * @return extractor.
	 */
	public final QueryDataRowsExtractor extractQueryData(
			final TestDataSource src) {
		TestDataset data = dataSets.get(src);
		QueryDataRowsExtractor result = new QueryDataRowsExtractor(data);
		return result;
	}

	/**
	 * Generate a data matrix for the specified parameters.
	 * @param quy
	 *            Test query.
	 * @return matrix of data.
	 */
	public final TestDataset getDataset(final TestDataSource quy) {
		return dataSets.get(quy);
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
	 * @return the cltm
	 */
	public final TestDatasetParameters getSet() {
		return set;
	}

	/**
	 * Get the start time for the dataset type.
	 * @param typ
	 *            dataset type.
	 * @return the start time.
	 */
	public final TimeGenerator getTimesGen(final TestDataSource typ) {
		return cl2Times.get(typ);
	}
}
