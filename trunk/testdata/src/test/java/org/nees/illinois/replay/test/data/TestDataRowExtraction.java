package org.nees.illinois.replay.test.data;

import org.junit.Assert;
import org.nees.illinois.replay.common.types.TimeBoundsI;
import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.DoubleMatrixI;
import org.nees.illinois.replay.events.EventListI;
import org.nees.illinois.replay.test.utils.QueryDataRowsExtractor;
import org.nees.illinois.replay.test.utils.TestDataset;
import org.nees.illinois.replay.test.utils.gen.DoubleMatrixGenerator;
import org.nees.illinois.replay.test.utils.gen.EventsGenerator;
import org.nees.illinois.replay.test.utils.gen.TimeGenerator;
import org.nees.illinois.replay.test.utils.types.QueryRowDataTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Test the extraction of expected query data.
 * @author Michael Bletzinger
 */
@Test(groups = { "extract" })
public class TestDataRowExtraction {
	/**
	 * Dataset to test on.
	 */
	private TestDataset set;
	/**
	 * Size of the continuous dataset.
	 */
	private final int[] sizes = { 20, 10 };
	/**
	 * Time increment for time stamps.
	 */
	private final double timeMultiplier = 0.2;
	/**
	 * Starting timestamp.
	 */
	private final double startTime = 200.0;

	/**
	 * Logger.
	 **/
	private final Logger log = LoggerFactory
			.getLogger(TestDataRowExtraction.class);
	/**
	 * tolerance for double equates.
	 */
	private final double delta = 0.0001;

	/**
	 * Setup the test dataset.
	 */
	@BeforeClass
	public final void beforeClass() {
		TimeGenerator timegen = new TimeGenerator(timeMultiplier, startTime);
		DoubleMatrixGenerator dmg = new DoubleMatrixGenerator(sizes[0],
				sizes[1], timegen);
		DoubleMatrixI da = new DoubleMatrix(dmg.generate());
		EventsGenerator evg = new EventsGenerator();
		final int skips = 3;
		EventListI events = evg.generate(da, skips, "Krypton3", false);
		set = new TestDataset(da, events, "Krypton3");
		log.debug("Generated " + da);
	}

	/**
	 * Test creating query boundaries from the test dataset.
	 */
	@Test
	public final void test01TimeBoundaries() {
		final double[] bounds1 = { 201.4, 202.6 };
		final double[] bounds2 = { 201.6, 202.8 };
		final String[] bounds3 = { "Event Name 2", "Event Name 4" };
		QueryDataRowsExtractor qdre = new QueryDataRowsExtractor(set);
		TimeBoundsI tb = qdre.getTimeBounds(false);
		Assert.assertEquals(bounds1[0], tb.getStart(), delta);
		Assert.assertEquals(bounds1[1], tb.getStop(), delta);
		log.debug("Time bounds for continuous is " + tb);
		tb = qdre.getTimeBounds(true);
		Assert.assertEquals(bounds2[0], tb.getStart(), delta);
		Assert.assertEquals(bounds2[1], tb.getStop(), delta);
		Assert.assertEquals(bounds3[0], tb.getStartName());
		Assert.assertEquals(bounds3[1], tb.getStopName());
		log.debug("Time bounds for events is " + tb);

	}

	/**
	 * Include a slice of continuous data from start to stop.
	 */
	@Test
	public final void test02ContWithStartStop() {
		final double[][] expected = {
				{ 201.4, 0.07, -0.07600000000000001, 0.082,
					-0.08800000000000001, 0.094, -0.1, 0.10600000000000001,
					-0.11200000000000002, 0.11800000000000001, -0.124 },
					{ 201.6, 0.08, -0.083, 0.08600000000000001, -0.089, 0.092,
						-0.095, 0.098, -0.101, 0.10400000000000001, -0.107 },
						{ 201.8, 0.09, -0.096, 0.102, -0.108, 0.11399999999999999,
							-0.12, 0.126, -0.132, 0.138, -0.144 },
							{ 202.0, 0.1, -0.10300000000000001, 0.10600000000000001,
								-0.10900000000000001, 0.112, -0.115,
								0.11800000000000001, -0.12100000000000001, 0.124,
								-0.127 },
								{ 202.2, 0.11, -0.116, 0.122, -0.128, 0.134, -0.14,
									0.14600000000000002, -0.152, 0.158, -0.164 },
									{ 202.4, 0.12, -0.123, 0.126, -0.129, 0.132, -0.135, 0.138,
										-0.141, 0.144, -0.147 },

		};
		QueryDataRowsExtractor qdre = new QueryDataRowsExtractor(set);
		DoubleMatrixI actual = qdre
				.getExpected(QueryRowDataTypes.ContWithStartStop);
		log.debug("Result " + actual);
		DoubleMatrixGenerator.compareData(actual.getData(), expected);
	}

	/**
	 * Include a single dataset based on a time stamp.
	 */
	@Test
	public final void test03ContWithTime() {
		final double[][] expected = { { 201.4, 0.07, -0.07600000000000001,
			0.082, -0.08800000000000001, 0.094, -0.1, 0.10600000000000001,
			-0.11200000000000002, 0.11800000000000001, -0.124 } };
		QueryDataRowsExtractor qdre = new QueryDataRowsExtractor(set);
		DoubleMatrixI actual = qdre.getExpected(QueryRowDataTypes.ContWithTime);
		log.debug("Result " + actual);
		DoubleMatrixGenerator.compareData(actual.getData(), expected);

	}

	/**
	 * Include a single data set based on the event.
	 */
	@Test
	public final void test04Event() {
		final double[][] expected = { { 201.6, 0.08, -0.083,
			0.08600000000000001, -0.089, 0.092, -0.095, 0.098, -0.101,
			0.10400000000000001, -0.107 }, };
		QueryDataRowsExtractor qdre = new QueryDataRowsExtractor(set);
		DoubleMatrixI actual = qdre.getExpected(QueryRowDataTypes.Event);
		log.debug("Result " + actual);
		DoubleMatrixGenerator.compareData(actual.getData(), expected);
	}

	/**
	 * Include all event data between two events.
	 */
	@Test
	public final void test05EventsWithStartStop() {
		final double[][] expected = {
				{201.6, 0.08, -0.083, 0.08600000000000001, -0.089, 0.092, -0.095, 0.098, -0.101, 0.10400000000000001, -0.107},
				{202.2, 0.11, -0.116, 0.122, -0.128, 0.134, -0.14, 0.14600000000000002, -0.152, 0.158, -0.164},
				{ 202.8, 0.14, -0.14300000000000002, 0.14600000000000002,
					-0.14900000000000002, 0.15200000000000002,
					-0.15500000000000003, 0.15800000000000003, -0.161,
					0.164, -0.167 },
		};
		QueryDataRowsExtractor qdre = new QueryDataRowsExtractor(set);
		DoubleMatrixI actual = qdre.getExpected(QueryRowDataTypes.EventsWithStartStop);
		log.debug("Result " + actual);
		DoubleMatrixGenerator.compareData(actual.getData(), expected);
	}

	/**
	 * Include all data between two events.
	 */
	@Test
	public final void test06ContWithEventStartStop() {
		final double[][] expected = {
				{201.6, 0.08, -0.083, 0.08600000000000001, -0.089, 0.092, -0.095, 0.098, -0.101, 0.10400000000000001, -0.107},
				{201.8, 0.09, -0.096, 0.102, -0.108, 0.11399999999999999, -0.12, 0.126, -0.132, 0.138, -0.144},
				{202.0, 0.1, -0.10300000000000001, 0.10600000000000001, -0.10900000000000001, 0.112, -0.115, 0.11800000000000001, -0.12100000000000001, 0.124, -0.127},
				{202.2, 0.11, -0.116, 0.122, -0.128, 0.134, -0.14, 0.14600000000000002, -0.152, 0.158, -0.164},
				{202.4, 0.12, -0.123, 0.126, -0.129, 0.132, -0.135, 0.138, -0.141, 0.144, -0.147},
				{ 202.6, 0.13, -0.136, 0.14200000000000002,
					-0.14800000000000002, 0.154, -0.16, 0.166,
					-0.17200000000000001, 0.178, -0.184 },		};
		QueryDataRowsExtractor qdre = new QueryDataRowsExtractor(set);
		DoubleMatrixI actual = qdre.getExpected(QueryRowDataTypes.ContWithEventStartStop);
		log.debug("Result " + actual);
		DoubleMatrixGenerator.compareData(actual.getData(), expected);
	}

}
