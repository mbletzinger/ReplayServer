package org.nees.illinois.replay.test.data;

import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.DoubleMatrixI;
import org.nees.illinois.replay.test.utils.gen.DoubleArrayDataGenerator;
import org.nees.illinois.replay.test.utils.gen.QueryChannelListsForMerging;
import org.nees.illinois.replay.test.utils.types.MatrixMixType;
import org.nees.illinois.replay.test.utils.types.TestingParts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * This class test the mock data set up in org.nees.illinois.replay.test.utils.
 * @author Michael Bletzinger
 */
@Test(groups = { "test_test_data" })
public class TestChannelTestData {
	/**
	 * Logger.
	 */
	private final Logger log = LoggerFactory
			.getLogger(TestChannelTestData.class);
	/**
	 * Test matrix size.
	 */
	private final int numberOfChannels = 4;

	/**
	 * Column mixer.
	 * @param mix
	 *            Mix type.
	 * @param data
	 *            Input data.
	 * @param ctl
	 *            Channel lists for data.
	 * @return Mixed columns.
	 */
	private double[][] columnMix(final MatrixMixType mix,
			final double[][] data, final QueryChannelListsForMerging ctl) {
		int fcsz = ctl.getExistingList().size();
		int scsz = ctl.getNewChannels().size();
		List<List<Double>> accum = (new DoubleMatrix(data)).toList();
		if (mix.equals(MatrixMixType.AddAfter)) {
			return data;
		}
		if (mix.equals(MatrixMixType.AddMerged)) {
			log.error(mix + " is not a valid colum mix type");
			Assert.fail();
			return null;
		}
		final int timeColumns = 1;
		for (List<Double> row : accum) {
			List<Double> tmp = new ArrayList<Double>();
			tmp.addAll(row);
			row.clear();
			row.addAll(tmp.subList(0, timeColumns));
			if (mix.equals(MatrixMixType.AddBefore)) {
				row.addAll(tmp.subList(fcsz + timeColumns, tmp.size()));
				row.addAll(tmp.subList(timeColumns, fcsz + timeColumns));
			}
			if (mix.equals(MatrixMixType.AddMiddle)) {
				int hscsz = scsz / 2;
				row.addAll(tmp.subList(fcsz + timeColumns, fcsz + timeColumns
						+ hscsz));
				row.addAll(tmp.subList(timeColumns, fcsz + timeColumns));
				row.addAll(tmp.subList(fcsz + timeColumns + hscsz, tmp.size()));
			}
			if (mix.equals(MatrixMixType.AddInterleaved)) {
				int smaller = fcsz;
				boolean firstIsBigger = false;
				if (fcsz > scsz) {
					smaller = scsz;
					firstIsBigger = true;
				}
				for (int c = 0; c < smaller; c++) {
					row.add(tmp.get(timeColumns + fcsz + c));
					row.add(tmp.get(timeColumns + c));
				}
				if (firstIsBigger) {
					row.addAll(tmp.subList(timeColumns + smaller, fcsz));
				} else {
					row.addAll(tmp.subList(timeColumns + fcsz + smaller,
							tmp.size()));
				}
			}
		}
		DoubleMatrixI result = new DoubleMatrix(accum);
		log.debug("Expected mix " + result);
		return result.getData();
	}

	/**
	 * Test appending matrices.
	 */
	@Test(dependsOnMethods = { "testIndividualMatrices" })
	public final void testAddAfter() {
		final double[][] expect1st = {
				{ 222.04, 0.02, -0.023, 0.026000000000000002 },
				{ 222.06, 0.03, -0.036, 0.041999999999999996 } };
		final double[][] expect2nd = { { 222.0, 0.0, -0.0030, 0.0060 },
				{ 222.02, 0.01, -0.016, 0.022 } };
		final double[][] expectAll = {
				{ 222.0, Double.NaN, Double.NaN, Double.NaN, 0.0, -0.0030,
					0.0060 },
					{ 222.02, Double.NaN, Double.NaN, Double.NaN, 0.01, -0.016,
						0.022 },
						{ 222.04, 0.02, -0.023, 0.026000000000000002, Double.NaN,
							Double.NaN, Double.NaN },
							{ 222.06, 0.03, -0.036, 0.041999999999999996, Double.NaN,
								Double.NaN, Double.NaN } };

		List<String> firstChannels = new ArrayList<String>();
		for (int i = 1; i < numberOfChannels; i++) {
			firstChannels.add("Test" + i);
		}
		QueryChannelListsForMerging ctlP = new QueryChannelListsForMerging(MatrixMixType.AddAfter,
				null, firstChannels, "Simple");

		List<String> secondChannels = new ArrayList<String>();
		for (int i = 1; i < numberOfChannels; i++) {
			secondChannels.add("Check" + i);
		}
		for (MatrixMixType m : MatrixMixType.values()) {
			if (m.equals(MatrixMixType.AddMerged)) {
				continue;
			}
			QueryChannelListsForMerging ctl = new QueryChannelListsForMerging(m, ctlP,
					secondChannels, "AddAfter");
			ChannelDataGenerator cdg = new ChannelDataGenerator(ctl,
					MatrixMixType.AddAfter, 2);
			cdg.generateParts();
			cdg.generateWhole();
			cdg.mixColumns();
			log.debug("First " + cdg.getData(TestingParts.First));
			DoubleArrayDataGenerator.compareData(cdg
					.getData(TestingParts.First).getData(), expect1st);
			log.debug("Second " + cdg.getData(TestingParts.Second));
			DoubleArrayDataGenerator.compareData(
					cdg.getData(TestingParts.Second).getData(), expect2nd);
			log.debug("All  with column mix " + m + " "
					+ cdg.getData(TestingParts.All));
			DoubleArrayDataGenerator.compareData(cdg.getData(TestingParts.All)
					.getData(), columnMix(m, expectAll, ctl));
		}
	}

	/**
	 * Test prepending matrices.
	 */
	@Test(dependsOnMethods = { "testIndividualMatrices" })
	public final void testAddBefore() {
		List<String> firstChannels = new ArrayList<String>();
		for (int i = 1; i < numberOfChannels; i++) {
			firstChannels.add("Test" + i);
		}
		QueryChannelListsForMerging ctlP = new QueryChannelListsForMerging(MatrixMixType.AddAfter,
				null, firstChannels, "Simple");

		List<String> secondChannels = new ArrayList<String>();
		for (int i = 1; i < numberOfChannels; i++) {
			secondChannels.add("Check" + i);
		}
		QueryChannelListsForMerging ctl = new QueryChannelListsForMerging(MatrixMixType.AddAfter,
				ctlP, secondChannels, "AddAfter");
		ChannelDataGenerator cdg = new ChannelDataGenerator(ctl,
				MatrixMixType.AddBefore, 2);
		cdg.generateParts();
		cdg.generateWhole();
		cdg.mixColumns();
		final double[][] expected = { { 222.0, 0.0, -0.0030, 0.0060 },
				{ 222.02, 0.01, -0.016, 0.022 } };
		log.debug("First " + cdg.getData(TestingParts.First));
		DoubleArrayDataGenerator.compareData(cdg.getData(TestingParts.First)
				.getData(), expected);
		log.debug("Second " + cdg.getData(TestingParts.Second));
		final double[][] expected1 = {
				{ 222.04, 0.02, -0.023, 0.026000000000000002 },
				{ 222.06, 0.03, -0.036, 0.041999999999999996 } };
		DoubleArrayDataGenerator.compareData(cdg.getData(TestingParts.Second)
				.getData(), expected1);
		log.debug("All " + cdg.getData(TestingParts.All));
		final double[][] expected2 = {
				{ 222.0, 0.0, -0.0030, 0.0060, Double.NaN, Double.NaN,
					Double.NaN },
					{ 222.02, 0.01, -0.016, 0.022, Double.NaN, Double.NaN,
						Double.NaN },
						{ 222.04, Double.NaN, Double.NaN, Double.NaN, 0.02, -0.023,
							0.026000000000000002 },
							{ 222.06, Double.NaN, Double.NaN, Double.NaN, 0.03, -0.036,
								0.041999999999999996 } };
		DoubleArrayDataGenerator.compareData(cdg.getData(TestingParts.All)
				.getData(), expected2);
	}

	/**
	 * Test inserting matrices.
	 */
	@Test(dependsOnMethods = { "testIndividualMatrices" })
	public final void testAddBetween() {
		List<String> firstChannels = new ArrayList<String>();
		for (int i = 1; i < numberOfChannels; i++) {
			firstChannels.add("Test" + i);
		}
		QueryChannelListsForMerging ctlP = new QueryChannelListsForMerging(MatrixMixType.AddAfter,
				null, firstChannels, "Simple");

		List<String> secondChannels = new ArrayList<String>();
		for (int i = 1; i < numberOfChannels; i++) {
			secondChannels.add("Check" + i);
		}
		QueryChannelListsForMerging ctl = new QueryChannelListsForMerging(MatrixMixType.AddAfter,
				ctlP, secondChannels, "AddAfter");
		ChannelDataGenerator cdg = new ChannelDataGenerator(ctl,
				MatrixMixType.AddMiddle, 2);
		cdg.generateParts();
		cdg.generateWhole();
		cdg.mixColumns();
		final double[][] expected = { { 222.0, 0.0, -0.0030, 0.0060 },
				{ 222.06, 0.03, -0.033, 0.036 } };
		log.debug("First " + cdg.getData(TestingParts.First));
		DoubleArrayDataGenerator.compareData(cdg.getData(TestingParts.First)
				.getData(), expected);
		log.debug("Second " + cdg.getData(TestingParts.Second));
		final double[][] expected1 = {
				{ 222.02, 0.01, -0.013000000000000001, 0.016 },
				{ 222.04, 0.02, -0.026000000000000002, 0.032 } };
		DoubleArrayDataGenerator.compareData(cdg.getData(TestingParts.Second)
				.getData(), expected1);
		log.debug("All " + cdg.getData(TestingParts.All));
		final double[][] expected2 = {
				{ 222.0, 0.0, -0.0030, 0.0060, Double.NaN, Double.NaN,
					Double.NaN },
					{ 222.02, Double.NaN, Double.NaN, Double.NaN, 0.01,
						-0.013000000000000001, 0.016 },
						{ 222.04, Double.NaN, Double.NaN, Double.NaN, 0.02,
							-0.026000000000000002, 0.032 },
							{ 222.06, 0.03, -0.033, 0.036, Double.NaN, Double.NaN,
								Double.NaN } };
		DoubleArrayDataGenerator.compareData(cdg.getData(TestingParts.All)
				.getData(), expected2);
	}

	/**
	 * Test mixing matrices.
	 */
	@Test(dependsOnMethods = { "testIndividualMatrices" })
	public final void testAddInterleaved() {
		List<String> firstChannels = new ArrayList<String>();
		for (int i = 1; i < numberOfChannels; i++) {
			firstChannels.add("Test" + i);
		}
		QueryChannelListsForMerging ctlP = new QueryChannelListsForMerging(MatrixMixType.AddAfter,
				null, firstChannels, "Simple");

		List<String> secondChannels = new ArrayList<String>();
		for (int i = 1; i < numberOfChannels; i++) {
			secondChannels.add("Check" + i);
		}
		QueryChannelListsForMerging ctl = new QueryChannelListsForMerging(MatrixMixType.AddAfter,
				ctlP, secondChannels, "AddAfter");
		ChannelDataGenerator cdg = new ChannelDataGenerator(ctl,
				MatrixMixType.AddInterleaved, 2);
		cdg.generateParts();
		cdg.generateWhole();
		cdg.mixColumns();
		final double[][] expected = { { 222.0, 0.0, -0.0030, 0.0060 },
				{ 222.02, 0.01, -0.016, 0.022 } };
		log.debug("First " + cdg.getData(TestingParts.First));
		DoubleArrayDataGenerator.compareData(cdg.getData(TestingParts.First)
				.getData(), expected);
		log.debug("Second " + cdg.getData(TestingParts.Second));
		final double[][] expected1 = { { 222.01, 0.0, -0.0030, 0.0060 },
				{ 222.03, 0.01, -0.016, 0.022 } };
		DoubleArrayDataGenerator.compareData(cdg.getData(TestingParts.Second)
				.getData(), expected1);
		log.debug("All " + cdg.getData(TestingParts.All));
		final double[][] expected2 = {
				{ 222.0, 0.0, -0.0030, 0.0060, Double.NaN, Double.NaN,
					Double.NaN },
					{ 222.01, Double.NaN, Double.NaN, Double.NaN, 0.0, -0.0030,
						0.0060 },
						{ 222.02, 0.01, -0.016, 0.022, Double.NaN, Double.NaN,
							Double.NaN },
							{ 222.03, Double.NaN, Double.NaN, Double.NaN, 0.01, -0.016,
								0.022 } };
		DoubleArrayDataGenerator.compareData(cdg.getData(TestingParts.All)
				.getData(), expected2);
	}

	/**
	 * Test merging matrices.
	 */
	@Test(dependsOnMethods = { "testIndividualMatrices" })
	public final void testAddMerged() {
		List<String> firstChannels = new ArrayList<String>();
		for (int i = 1; i < numberOfChannels; i++) {
			firstChannels.add("Test" + i);
		}
		QueryChannelListsForMerging ctlP = new QueryChannelListsForMerging(MatrixMixType.AddAfter,
				null, firstChannels, "Simple");

		List<String> secondChannels = new ArrayList<String>();
		for (int i = 1; i < numberOfChannels; i++) {
			secondChannels.add("Check" + i);
		}
		QueryChannelListsForMerging ctl = new QueryChannelListsForMerging(MatrixMixType.AddAfter,
				ctlP, secondChannels, "AddAfter");
		ChannelDataGenerator cdg = new ChannelDataGenerator(ctl,
				MatrixMixType.AddMerged, 2);
		cdg.generateParts();
		cdg.generateWhole();
		cdg.mixColumns();
		final double[][] expected = { { 222.0, 0.0, -0.0030, 0.0060 },
				{ 222.02, 0.01, -0.016, 0.022 } };
		log.debug("First " + cdg.getData(TestingParts.First));
		DoubleArrayDataGenerator.compareData(cdg.getData(TestingParts.First)
				.getData(), expected);
		log.debug("Second " + cdg.getData(TestingParts.Second));
		final double[][] expected1 = { { 222.0, 0.0, -0.0030, 0.0060 },
				{ 222.02, 0.01, -0.016, 0.022 } };
		DoubleArrayDataGenerator.compareData(cdg.getData(TestingParts.Second)
				.getData(), expected1);
		log.debug("All " + cdg.getData(TestingParts.All));
		final double[][] expected2 = {
				{ 222.0, 0.0, -0.0030, 0.0060, 0.0, -0.0030, 0.0060 },
				{ 222.02, 0.01, -0.016, 0.022, 0.01, -0.016, 0.022 } };
		DoubleArrayDataGenerator.compareData(cdg.getData(TestingParts.All)
				.getData(), expected2);
	}

	/**
	 * Test matrix generation.
	 */
	@Test
	public final void testIndividualMatrices() {
		List<String> newChannels = new ArrayList<String>();
		for (int i = 1; i < numberOfChannels; i++) {
			newChannels.add("Test" + i);
		}
		QueryChannelListsForMerging ctl = new QueryChannelListsForMerging(MatrixMixType.AddAfter,
				null, newChannels, "Simple");
		ChannelDataGenerator cdg = new ChannelDataGenerator(ctl,
				MatrixMixType.AddAfter, 2);
		cdg.generateParts();
		cdg.generateWhole();
		final double[][] expected = { { 222.0, 0.0, -0.0030, 0.0060 },
				{ 222.02, 0.01, -0.016, 0.022 } };
		log.debug("First " + cdg.getData(TestingParts.First));
		Assert.assertNull(cdg.getData(TestingParts.First));
		log.debug("Second " + cdg.getData(TestingParts.Second));
		DoubleArrayDataGenerator.compareData(cdg.getData(TestingParts.Second)
				.getData(), expected);
		log.debug("All " + cdg.getData(TestingParts.All));
		DoubleArrayDataGenerator.compareData(cdg.getData(TestingParts.All)
				.getData(), expected);
	}
}
