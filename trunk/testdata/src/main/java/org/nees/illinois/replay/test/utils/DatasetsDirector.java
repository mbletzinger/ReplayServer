package org.nees.illinois.replay.test.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nees.illinois.replay.common.registries.ChannelNameRegistry;
import org.nees.illinois.replay.common.registries.TableRegistry;
import org.nees.illinois.replay.common.types.TableDefinitionI;
import org.nees.illinois.replay.common.types.TimeBoundsI;
import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.DoubleMatrixI;
import org.nees.illinois.replay.data.MergeSet;
import org.nees.illinois.replay.data.SubsetSlicer;
import org.nees.illinois.replay.events.EventListI;
import org.nees.illinois.replay.test.utils.gen.DerivedTimeGenerator;
import org.nees.illinois.replay.test.utils.gen.DoubleMatrixGenerator;
import org.nees.illinois.replay.test.utils.gen.EventsGenerator;
import org.nees.illinois.replay.test.utils.gen.TestCompositeQuery;
import org.nees.illinois.replay.test.utils.gen.TimeGenerator;
import org.nees.illinois.replay.test.utils.types.ExperimentNames;
import org.nees.illinois.replay.test.utils.types.MatrixMixType;
import org.nees.illinois.replay.test.utils.types.SimpleTestQueryResults;
import org.nees.illinois.replay.test.utils.types.TestDataSource;
import org.nees.illinois.replay.test.utils.types.TestingParts;
import org.nees.illinois.replay.test.utils.types.TimeBoundaryTestType;
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
	private final TestDatasetParameters parameters;
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
	 * Extract rows expected for a test query from the test dataset.
	 */
	private QueryDataRowsExtractor extract;

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
		this.parameters = new TestDatasetParameters(
				experiment.equals(ExperimentNames.HybridMasonry2),
				experiment.name());
		this.parameters.fillCnr(expectedCnr);
		this.parameters.fillTblr(expectedTblr);
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
		checkChannels(parameters.getChannels(typ), channels);
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
	 * @param part
	 *            Which part of the composite query to check.
	 */
	public final void checkData(final ExperimentNames experiment,
			final TimeBoundaryTestType qt, final TestCompositeQuery quy,
			final DoubleMatrixI data, final TestingParts part) {
		log.debug("For " + qt + " , " + part + " and, " + quy);
		DoubleMatrixI expected = null;
		TestDataSource src1 = null;
		if (quy.getExisting() != null) {
			src1 = quy.getExisting().getSource();
		}
		TestDataSource src2 = quy.getSource();
		switch (part) {
		case All:
			SimpleTestQueryResults result = getAllQueryData(quy, qt);
			expected = sortResults(result.getData(), quy.combine(),
					result.getChannels());
			break;
		case First:
			extract = extractQueryData(src1);
			expected = sortResults(extract.getExpected(qt),
					quy.getExistingList(), src1);
			break;
		case Second:
			extract = extractQueryData(src2);
			expected = sortResults(extract.getExpected(qt),
					quy.getNewChannels(), src2);
			break;
		default:
			log.error(part + " not recognized");
			Assert.fail();
		}
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
		List<String> channels = parameters.getChannels(quy);
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
		if (data == null) {
			log.error("Data for source " + src + " does not exist");
			return null;
		}
		QueryDataRowsExtractor result = new QueryDataRowsExtractor(data);
		return result;
	}

	/**
	 * Recursive function to get all of the expected data for a composite test
	 * query. The recursion is based on how many nested TestCompositeQuery
	 * objects there are.
	 * @param quy
	 *            composite query.
	 * @param qt
	 *            row order of data.
	 * @return consolidated expected data.
	 */
	private SimpleTestQueryResults getAllQueryData(
			final TestCompositeQuery quy, final TimeBoundaryTestType qt) {
		SimpleTestQueryResults oldData = getAllQueryData(quy.getExisting(), qt);
		extract = extractQueryData(quy.getSource());
		DoubleMatrixI newData = extract.getExpected(qt);
		List<String> channels = oldData.getChannels();
		channels.addAll(parameters.getChannels(quy.getSource()));
		MergeSet mrg = new MergeSet();
		mrg.merge(oldData.getData());
		mrg.merge(newData);
		return new SimpleTestQueryResults(quy.getSource(), channels,
				mrg.getResult());
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
	 * @return the test parameters.
	 */
	public final TestDatasetParameters getParameters() {
		return parameters;
	}

	/**
	 * Return the time boundaries for the current dataset based on the query
	 * time boundary type.
	 * @param qpt
	 *            test time boundary type.
	 * @param quy
	 *            test query.
	 * @param part
	 *            part of the query that needs time bounds.
	 * @return time boundaries.
	 */
	public final TimeBoundsI getTimeBounds(final TimeBoundaryTestType qpt,
			final TestCompositeQuery quy, final TestingParts part) {

		TestDataSource src1 = null;
		if (quy.getExisting() != null) {
			src1 = quy.getExisting().getSource();
		}
		TestDataSource src2 = quy.getSource();
		switch (part) {
		case First:
			if (src1 == null) {
				log.error("First source for " + quy + "is not defined.");
				return null;
			}
			extract = extractQueryData(src1);
			break;
		case All:
		case Second:
			if (src2 == null) {
				log.error("Second source for " + quy + "is not defined.");
				return null;
			}
			extract = extractQueryData(quy.getSource());
			break;
		default:
			log.error(part + " not recognized");
			Assert.fail();
		}
		QueryTimeBoundsExtractor times = extract.getTimes();
		return times.getTimeBounds(qpt);
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

	/**
	 * Sort the result columns in the order specified by the composite query.
	 * @param data
	 *            to sort.
	 * @param qorder
	 *            query order of the columns
	 * @param dorder
	 *            data order of the columns
	 * @return the sorted results.
	 */
	private DoubleMatrixI sortResults(final DoubleMatrixI data,
			final List<String> qorder, final List<String> dorder) {
		List<Integer> slices = new ArrayList<Integer>();
		slices.add(0);
		for (String c : qorder) {
			int k = dorder.indexOf(c);
			slices.add(k + 1);
		}
		SubsetSlicer slicer = new SubsetSlicer(data);
		slicer.setSliceColumn(true);
		slicer.addSlices(slices);
		return slicer.slice();
	}

	/**
	 * Sort the result columns in the order specified by the composite query.
	 * @param data
	 *            to sort.
	 * @param qorder
	 *            query order of the columns
	 * @param src
	 *            of the data
	 * @return the sorted results.
	 */
	private DoubleMatrixI sortResults(final DoubleMatrixI data,
			final List<String> qorder, final TestDataSource src) {
		List<String> dorder = parameters.getChannels(src);
		return sortResults(data, qorder, dorder);
	}
}
