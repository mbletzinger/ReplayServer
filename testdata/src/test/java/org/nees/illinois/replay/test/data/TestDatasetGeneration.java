package org.nees.illinois.replay.test.data;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.DoubleMatrixI;
import org.nees.illinois.replay.data.MergeSet;
import org.nees.illinois.replay.events.EventListI;
import org.nees.illinois.replay.test.utils.gen.DerivedTimeGenerator;
import org.nees.illinois.replay.test.utils.gen.DoubleMatrixGenerator;
import org.nees.illinois.replay.test.utils.gen.EventsGenerator;
import org.nees.illinois.replay.test.utils.gen.TimeGenerator;
import org.nees.illinois.replay.test.utils.types.MatrixMixType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

/**
 * Test the generation and merging of test sets.
 * @author Michael Bletzinger
 */

@Test(groups = { "test_test_data" })
public class TestDatasetGeneration {
	/**
	 * Time Increment for data generation.
	 */
	private final double timeMultiplier1 = 0.02;
	/**
	 * Start time for data generation.
	 */
	private final double startTime1 = 200.0;
	/**
	 * Number of rows to generate.
	 */
	private final int numberOfRows = 10;
	/**
	 * Number of columns to generate.
	 */
	private final int numberOfColumns = 4;
	/**
	 * Logger.
	 **/
	private final Logger log = LoggerFactory
			.getLogger(TestDatasetGeneration.class);
	/**
	 * Expected gap size.
	 */
	private final double gapS = 0.2;
	/**
	 * Expected gap start time.
	 */
	private final double gapST = 0.1;

	/**
	 * Generate a simple dataset.
	 * @return
	 */
	@Test
	public final void test01SimpleGenerate() {
		final double[][] expected1 = {
				{ 200.0, 0.0, -0.0030, 0.0060, -0.009000000000000001 },
				{ 200.02, 0.01, -0.016, 0.022, -0.028000000000000004 },
				{ 200.04, 0.02, -0.023, 0.026000000000000002, -0.029 },
				{ 200.06, 0.03, -0.036, 0.041999999999999996, -0.048 },
				{ 200.08, 0.04, -0.043000000000000003, 0.046, -0.049 },
				{ 200.1, 0.05, -0.056, 0.062, -0.068 },
				{ 200.12, 0.06, -0.063, 0.066, -0.069 },
				{ 200.14, 0.07, -0.07600000000000001, 0.082,
					-0.08800000000000001 },
					{ 200.16, 0.08, -0.083, 0.08600000000000001, -0.089 },
					{ 200.18, 0.09, -0.096, 0.102, -0.108 }, };
		final double[][] expected2 = {
				{ 200.0, 0.0, -0.0030, 0.0060, -0.009000000000000001 },
				{ 200.02, 0.01, -0.016, 0.022, -0.028000000000000004 },
				{ 200.04, 0.02, -0.023, 0.026000000000000002, -0.029 },
				{ 200.06, 0.03, -0.036, 0.041999999999999996, -0.048 },
				{ 200.08, 0.04, -0.043000000000000003, 0.046, -0.049 },
				{ 200.1, 0.05, -0.056, 0.062, -0.068 },
				{ 200.3, 0.15, -0.153, 0.156, -0.159 },
				{ 200.32, 0.16, -0.166, 0.17200000000000001, -0.178 },
				{ 200.34, 0.17, -0.17300000000000001, 0.17600000000000002,
					-0.17900000000000002 },
					{ 200.36, 0.18, -0.186, 0.192, -0.198 }, };
		final String expectedEvents1 = "[\n" +
				"\tEvent [time=200.02, name=Event Name 0, description=\"An event that happened 0\", source=DAQ 1]\n" +
				"\tEvent [time=200.06, name=Event Name 1, description=\"An event that happened 1\", source=DAQ 1]\n" +
				"\tEvent [time=200.1, name=Event Name 2, description=\"An event that happened 2\", source=DAQ 1]\n" +
				"\tEvent [time=200.32, name=Event Name 3, description=\"An event that happened 3\", source=DAQ 1]\n" +
				"\tEvent [time=200.36, name=Event Name 4, description=\"An event that happened 4\", source=DAQ 1]\n" +
				"]";
		final String expectedEvents2 = "[\n" +
				"\tEvent [time=200.02, name=2_5_2, description=\" Step 2_5_2\", source=DAQ 1]\n" +
				"\tEvent [time=200.06, name=3_6_3, description=\" Step 3_6_3\", source=DAQ 1]\n" +
				"\tEvent [time=200.1, name=4_7_4, description=\" Step 4_7_4\", source=DAQ 1]\n" +
				"\tEvent [time=200.32, name=5_8_5, description=\" Step 5_8_5\", source=DAQ 1]\n" +
				"\tEvent [time=200.36, name=6_9_6, description=\" Step 6_9_6\", source=DAQ 1]\n" +
				"]";

		TimeGenerator timeGen = new TimeGenerator(timeMultiplier1, startTime1);
		DoubleMatrixGenerator dmGen = new DoubleMatrixGenerator(numberOfRows,
				numberOfColumns, timeGen);
		DoubleMatrixI dm = new DoubleMatrix(dmGen.generate());
		log.debug("Double Matrix that was generated [" + dm + "]");
		DoubleMatrixGenerator.compareData(dm.getData(), expected1);
		timeGen.setGapSize(gapS);
		timeGen.setGapStartTime(gapST);
		timeGen.reset();
		dm = new DoubleMatrix(dmGen.generate());
		log.debug("Double Matrix that was generated [" + dm + "]");
		DoubleMatrixGenerator.compareData(dm.getData(), expected2);
		EventsGenerator evg = new EventsGenerator();
		EventListI events = evg.generate(dm, 2, "DAQ 1", false);
		Assert.assertEquals(expectedEvents1, events.toString());
		events = evg.generate(dm, 2, "DAQ 1", true);
		Assert.assertEquals(expectedEvents2, events.toString());
//		log.debug("Created " + events);
	}

	/**
	 * Generate derived datasets.
	 */
	@Test
	public final void test02GenerateDerived() {
		TimeGenerator timeGen = new TimeGenerator(timeMultiplier1, startTime1);
		log.debug("Original time generator " + timeGen);
		List<TimeGenerator> expected = new ArrayList<TimeGenerator>();
		final double beforeT = 199.8;
		final double afterT = 200.2;
		final double middleT = 200.1;
		final double leaveT = 200.01;
		expected.add(new TimeGenerator(timeMultiplier1, beforeT));
		expected.add(new TimeGenerator(timeMultiplier1, afterT));
		expected.add(new TimeGenerator(timeMultiplier1, middleT));
		expected.add(new TimeGenerator(timeMultiplier1, leaveT));
		expected.add(timeGen);
		TimeGenerator timeGenWithGap = new TimeGenerator(timeGen);
		timeGenWithGap.setGapSize(gapS);
		timeGenWithGap.setGapStartTime(gapST);
		int i = 0;
		for (MatrixMixType m : MatrixMixType.values()) {
			log.debug("Testing " + m);
			TimeGenerator orig = new TimeGenerator(timeGen);
			DerivedTimeGenerator dgen = new DerivedTimeGenerator(m,
					numberOfRows * timeMultiplier1);
			TimeGenerator dtimegen = dgen.derive(orig);
//			log.debug("Mix " + m + " generated " + dtimegen);
//			log.debug("Mix " + m + " original modified " + orig);
			Assert.assertEquals(expected.get(i), dtimegen);
			if (m.equals(MatrixMixType.AddMiddle)) {
				Assert.assertEquals(timeGenWithGap, orig);
			} else {
				Assert.assertEquals(timeGen, orig);
			}
			i++;
		}
	}

	/**
	 * Merge derived datasets.
	 */
	@Test
	public final void test03MergeGenerated() {
		final double[][] addBeforeExpected = {
				{ 199.8, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.0,
					-0.0030, 0.0060, -0.009000000000000001 },
					{ 199.82000000000002, Double.NaN, Double.NaN, Double.NaN,
						Double.NaN, 0.01, -0.016, 0.022, -0.028000000000000004 },
						{ 199.84, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.02,
							-0.023, 0.026000000000000002, -0.029 },
							{ 199.86, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.03,
								-0.036, 0.041999999999999996, -0.048 },
								{ 199.88000000000002, Double.NaN, Double.NaN, Double.NaN,
									Double.NaN, 0.04, -0.043000000000000003, 0.046, -0.049 },
									{ 199.9, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.05,
										-0.056, 0.062, -0.068 },
										{ 199.92000000000002, Double.NaN, Double.NaN, Double.NaN,
											Double.NaN, 0.06, -0.063, 0.066, -0.069 },
											{ 199.94, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.07,
												-0.07600000000000001, 0.082, -0.08800000000000001 },
												{ 199.96, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.08,
													-0.083, 0.08600000000000001, -0.089 },
													{ 199.98000000000002, Double.NaN, Double.NaN, Double.NaN,
														Double.NaN, 0.09, -0.096, 0.102, -0.108 },
														{ 200.0, 0.0, -0.0030, 0.0060, -0.009000000000000001,
															Double.NaN, Double.NaN, Double.NaN, Double.NaN },
															{ 200.02, 0.01, -0.016, 0.022, -0.028000000000000004,
																Double.NaN, Double.NaN, Double.NaN, Double.NaN },
																{ 200.04, 0.02, -0.023, 0.026000000000000002, -0.029,
																	Double.NaN, Double.NaN, Double.NaN, Double.NaN },
																	{ 200.06, 0.03, -0.036, 0.041999999999999996, -0.048,
																		Double.NaN, Double.NaN, Double.NaN, Double.NaN },
																		{ 200.08, 0.04, -0.043000000000000003, 0.046, -0.049,
																			Double.NaN, Double.NaN, Double.NaN, Double.NaN },
																			{ 200.1, 0.05, -0.056, 0.062, -0.068, Double.NaN, Double.NaN,
																				Double.NaN, Double.NaN },
																				{ 200.12, 0.06, -0.063, 0.066, -0.069, Double.NaN, Double.NaN,
																					Double.NaN, Double.NaN },
																					{ 200.14, 0.07, -0.07600000000000001, 0.082,
																						-0.08800000000000001, Double.NaN, Double.NaN,
																						Double.NaN, Double.NaN },
																						{ 200.16, 0.08, -0.083, 0.08600000000000001, -0.089,
																							Double.NaN, Double.NaN, Double.NaN, Double.NaN },
																							{ 200.18, 0.09, -0.096, 0.102, -0.108, Double.NaN, Double.NaN,
																								Double.NaN, Double.NaN }, };
		final double[][] addAfterExpected = {
				{ 200.0, 0.0, -0.0030, 0.0060, -0.009000000000000001,
					Double.NaN, Double.NaN, Double.NaN, Double.NaN },
					{ 200.02, 0.01, -0.016, 0.022, -0.028000000000000004,
						Double.NaN, Double.NaN, Double.NaN, Double.NaN },
						{ 200.04, 0.02, -0.023, 0.026000000000000002, -0.029,
							Double.NaN, Double.NaN, Double.NaN, Double.NaN },
							{ 200.06, 0.03, -0.036, 0.041999999999999996, -0.048,
								Double.NaN, Double.NaN, Double.NaN, Double.NaN },
								{ 200.08, 0.04, -0.043000000000000003, 0.046, -0.049,
									Double.NaN, Double.NaN, Double.NaN, Double.NaN },
									{ 200.1, 0.05, -0.056, 0.062, -0.068, Double.NaN, Double.NaN,
										Double.NaN, Double.NaN },
										{ 200.12, 0.06, -0.063, 0.066, -0.069, Double.NaN, Double.NaN,
											Double.NaN, Double.NaN },
											{ 200.14, 0.07, -0.07600000000000001, 0.082,
												-0.08800000000000001, Double.NaN, Double.NaN,
												Double.NaN, Double.NaN },
												{ 200.16, 0.08, -0.083, 0.08600000000000001, -0.089,
													Double.NaN, Double.NaN, Double.NaN, Double.NaN },
													{ 200.18, 0.09, -0.096, 0.102, -0.108, Double.NaN, Double.NaN,
														Double.NaN, Double.NaN },
														{ 200.2, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.0,
															-0.0030, 0.0060, -0.009000000000000001 },
															{ 200.22, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.01,
																-0.016, 0.022, -0.028000000000000004 },
																{ 200.23999999999998, Double.NaN, Double.NaN, Double.NaN,
																	Double.NaN, 0.02, -0.023, 0.026000000000000002, -0.029 },
																	{ 200.26, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.03,
																		-0.036, 0.041999999999999996, -0.048 },
																		{ 200.28, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.04,
																			-0.043000000000000003, 0.046, -0.049 },
																			{ 200.29999999999998, Double.NaN, Double.NaN, Double.NaN,
																				Double.NaN, 0.05, -0.056, 0.062, -0.068 },
																				{ 200.32, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.06,
																					-0.063, 0.066, -0.069 },
																					{ 200.33999999999997, Double.NaN, Double.NaN, Double.NaN,
																						Double.NaN, 0.07, -0.07600000000000001, 0.082,
																						-0.08800000000000001 },
																						{ 200.35999999999999, Double.NaN, Double.NaN, Double.NaN,
																							Double.NaN, 0.08, -0.083, 0.08600000000000001, -0.089 },
																							{ 200.38, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.09,
																								-0.096, 0.102, -0.108 }, };
		final double[][] addMiddleExpected = {
				{ 200.0, 0.0, -0.0030, 0.0060, -0.009000000000000001,
					Double.NaN, Double.NaN, Double.NaN, Double.NaN },
					{ 200.02, 0.01, -0.016, 0.022, -0.028000000000000004,
						Double.NaN, Double.NaN, Double.NaN, Double.NaN },
						{ 200.04, 0.02, -0.023, 0.026000000000000002, -0.029,
							Double.NaN, Double.NaN, Double.NaN, Double.NaN },
							{ 200.06, 0.03, -0.036, 0.041999999999999996, -0.048,
								Double.NaN, Double.NaN, Double.NaN, Double.NaN },
								{ 200.08, 0.04, -0.043000000000000003, 0.046, -0.049,
									Double.NaN, Double.NaN, Double.NaN, Double.NaN },
									{ 200.1, 0.05, -0.056, 0.062, -0.068, 0.0, -0.0030, 0.0060,
										-0.009000000000000001 },
										{ 200.12, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.01,
											-0.016, 0.022, -0.028000000000000004 },
											{ 200.14, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.02,
												-0.023, 0.026000000000000002, -0.029 },
												{ 200.16, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.03,
													-0.036, 0.041999999999999996, -0.048 },
													{ 200.18, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.04,
														-0.043000000000000003, 0.046, -0.049 },
														{ 200.2, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.05,
															-0.056, 0.062, -0.068 },
															{ 200.22, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.06,
																-0.063, 0.066, -0.069 },
																{ 200.23999999999998, Double.NaN, Double.NaN, Double.NaN,
																	Double.NaN, 0.07, -0.07600000000000001, 0.082,
																	-0.08800000000000001 },
																	{ 200.26, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.08,
																		-0.083, 0.08600000000000001, -0.089 },
																		{ 200.28, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.09,
																			-0.096, 0.102, -0.108 },
																			{ 200.3, 0.15, -0.153, 0.156, -0.159, Double.NaN, Double.NaN,
																				Double.NaN, Double.NaN },
																				{ 200.32, 0.16, -0.166, 0.17200000000000001, -0.178,
																					Double.NaN, Double.NaN, Double.NaN, Double.NaN },
																					{ 200.34, 0.17, -0.17300000000000001, 0.17600000000000002,
																						-0.17900000000000002, Double.NaN, Double.NaN,
																						Double.NaN, Double.NaN },
																						{ 200.36, 0.18, -0.186, 0.192, -0.198, Double.NaN, Double.NaN,
																							Double.NaN, Double.NaN }, };
		final double[][] addInterleavedExpected = {
				{ 200.0, 0.0, -0.0030, 0.0060, -0.009000000000000001,
					Double.NaN, Double.NaN, Double.NaN, Double.NaN },
					{ 200.01, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.0,
						-0.0030, 0.0060, -0.009000000000000001 },
						{ 200.02, 0.01, -0.016, 0.022, -0.028000000000000004,
							Double.NaN, Double.NaN, Double.NaN, Double.NaN },
							{ 200.03, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.01,
								-0.016, 0.022, -0.028000000000000004 },
								{ 200.04, 0.02, -0.023, 0.026000000000000002, -0.029,
									Double.NaN, Double.NaN, Double.NaN, Double.NaN },
									{ 200.04999999999998, Double.NaN, Double.NaN, Double.NaN,
										Double.NaN, 0.02, -0.023, 0.026000000000000002, -0.029 },
										{ 200.06, 0.03, -0.036, 0.041999999999999996, -0.048,
											Double.NaN, Double.NaN, Double.NaN, Double.NaN },
											{ 200.07, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.03,
												-0.036, 0.041999999999999996, -0.048 },
												{ 200.08, 0.04, -0.043000000000000003, 0.046, -0.049,
													Double.NaN, Double.NaN, Double.NaN, Double.NaN },
													{ 200.09, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.04,
														-0.043000000000000003, 0.046, -0.049 },
														{ 200.1, 0.05, -0.056, 0.062, -0.068, Double.NaN, Double.NaN,
															Double.NaN, Double.NaN },
															{ 200.10999999999999, Double.NaN, Double.NaN, Double.NaN,
																Double.NaN, 0.05, -0.056, 0.062, -0.068 },
																{ 200.12, 0.06, -0.063, 0.066, -0.069, Double.NaN, Double.NaN,
																	Double.NaN, Double.NaN },
																	{ 200.13, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.06,
																		-0.063, 0.066, -0.069 },
																		{ 200.14, 0.07, -0.07600000000000001, 0.082,
																			-0.08800000000000001, Double.NaN, Double.NaN,
																			Double.NaN, Double.NaN },
																			{ 200.14999999999998, Double.NaN, Double.NaN, Double.NaN,
																				Double.NaN, 0.07, -0.07600000000000001, 0.082,
																				-0.08800000000000001 },
																				{ 200.16, 0.08, -0.083, 0.08600000000000001, -0.089,
																					Double.NaN, Double.NaN, Double.NaN, Double.NaN },
																					{ 200.17, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.08,
																						-0.083, 0.08600000000000001, -0.089 },
																						{ 200.18, 0.09, -0.096, 0.102, -0.108, Double.NaN, Double.NaN,
																							Double.NaN, Double.NaN },
																							{ 200.19, Double.NaN, Double.NaN, Double.NaN, Double.NaN, 0.09,
																								-0.096, 0.102, -0.108 }, };

		final double[][] addMergedExpected = {
				{ 200.0, 0.0, -0.0030, 0.0060, -0.009000000000000001, 0.0,
					-0.0030, 0.0060, -0.009000000000000001 },
					{ 200.02, 0.01, -0.016, 0.022, -0.028000000000000004, 0.01,
						-0.016, 0.022, -0.028000000000000004 },
						{ 200.04, 0.02, -0.023, 0.026000000000000002, -0.029, 0.02,
							-0.023, 0.026000000000000002, -0.029 },
							{ 200.06, 0.03, -0.036, 0.041999999999999996, -0.048, 0.03,
								-0.036, 0.041999999999999996, -0.048 },
								{ 200.08, 0.04, -0.043000000000000003, 0.046, -0.049, 0.04,
									-0.043000000000000003, 0.046, -0.049 },
									{ 200.1, 0.05, -0.056, 0.062, -0.068, 0.05, -0.056, 0.062,
										-0.068 },
										{ 200.12, 0.06, -0.063, 0.066, -0.069, 0.06, -0.063, 0.066,
											-0.069 },
											{ 200.14, 0.07, -0.07600000000000001, 0.082,
												-0.08800000000000001, 0.07, -0.07600000000000001,
												0.082, -0.08800000000000001 },
												{ 200.16, 0.08, -0.083, 0.08600000000000001, -0.089, 0.08,
													-0.083, 0.08600000000000001, -0.089 },
													{ 200.18, 0.09, -0.096, 0.102, -0.108, 0.09, -0.096, 0.102,
														-0.108 },

		};
		List<DoubleMatrixI> expected = new ArrayList<DoubleMatrixI>();
		expected.add(new DoubleMatrix(addBeforeExpected));
		expected.add(new DoubleMatrix(addAfterExpected));
		expected.add(new DoubleMatrix(addMiddleExpected));
		expected.add(new DoubleMatrix(addInterleavedExpected));
		expected.add(new DoubleMatrix(addMergedExpected));
		TimeGenerator timeGen = new TimeGenerator(timeMultiplier1, startTime1);
		int i = 0;
		for (MatrixMixType m : MatrixMixType.values()) {
			TimeGenerator orig = new TimeGenerator(timeGen);
			DerivedTimeGenerator dgen = new DerivedTimeGenerator(m,
					numberOfRows * timeMultiplier1);
			TimeGenerator dtimegen = dgen.derive(orig);
			DoubleMatrixGenerator dmGen = new DoubleMatrixGenerator(
					numberOfRows, numberOfColumns, orig);
			DoubleMatrixI dm = new DoubleMatrix(dmGen.generate());
//			log.debug("Double Matrix that was generated [" + dm + "]");
			MergeSet mrg = new MergeSet();
			mrg.merge(dm);
			DoubleMatrixGenerator ddGen = new DoubleMatrixGenerator(
					numberOfRows, numberOfColumns, dtimegen);
			DoubleMatrixI ddm = new DoubleMatrix(ddGen.generate());
			mrg.merge(ddm);
//			log.debug("Double Matrix that was generated  for " + m + " is {"
//					+ mrg.getResult() + "}");
			DoubleMatrixGenerator.compareData(mrg.getResult().getData(), expected.get(i).getData());
			i++;
		}

	}

}
