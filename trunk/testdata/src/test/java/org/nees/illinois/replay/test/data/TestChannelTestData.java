package org.nees.illinois.replay.test.data;

import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.test.utils.ChannelDataGenerator;
import org.nees.illinois.replay.test.utils.ChannelDataGenerator.TestingParts;
import org.nees.illinois.replay.test.utils.ChannelTestingList;
import org.nees.illinois.replay.test.utils.DoubleArrayDataGenerator;
import org.nees.illinois.replay.test.utils.MatrixMixType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestChannelTestData {
	private final Logger log = LoggerFactory
			.getLogger(TestChannelTestData.class);

	@Test
	public void TestIndividualMatrices() {
		List<String> newChannels = new ArrayList<String>();
		for (int i = 1; i < 4; i++) {
			newChannels.add("Test" + i);
		}
		ChannelTestingList ctl = new ChannelTestingList(MatrixMixType.AddAfter,
				null, newChannels, "Simple");
		ChannelDataGenerator cdg = new ChannelDataGenerator(ctl,
				MatrixMixType.AddAfter, 2);
		cdg.generateParts();
		cdg.generateWhole();
		double[][] expected = { { 222.0, 1.0, 0.0, 1.0, 0.0, -0.0030, 0.0060 },
				{ 222.02, 1.0, 0.0, 1.0, 0.01, -0.016, 0.022 } };
		log.debug("First " + cdg.getData(TestingParts.First));
		Assert.assertNull(cdg.getData(TestingParts.First));
		log.debug("Second " + cdg.getData(TestingParts.Second));
		DoubleArrayDataGenerator.compareData(cdg.getData(TestingParts.Second)
				.getData(), expected);
		log.debug("All " + cdg.getData(TestingParts.All));
		DoubleArrayDataGenerator.compareData(cdg.getData(TestingParts.All)
				.getData(), expected);
	}

	@Test(dependsOnMethods = { "TestIndividualMatrices" })
	public void TestAddAfter() {
		double[][] expect1st = {
				{ 222.04, 1.0, 0.0, 1.0, 0.02, -0.023, 0.026000000000000002 },
				{ 222.06, 1.0, 1.0, 0.0, 0.03, -0.036, 0.041999999999999996 } };
		double[][] expect2nd = {
				{ 222.0, 1.0, 0.0, 1.0, 0.0, -0.0030, 0.0060 },
				{ 222.02, 1.0, 0.0, 1.0, 0.01, -0.016, 0.022 } };
		double[][] expectAll = {
				{ 222.0, 1.0, 0.0, 1.0, Double.NaN, Double.NaN, Double.NaN,
						0.0, -0.0030, 0.0060 },
				{ 222.02, 1.0, 0.0, 1.0, Double.NaN, Double.NaN, Double.NaN,
						0.01, -0.016, 0.022 },
				{ 222.04, 1.0, 0.0, 1.0, 0.02, -0.023, 0.026000000000000002,
						Double.NaN, Double.NaN, Double.NaN },
				{ 222.06, 1.0, 1.0, 0.0, 0.03, -0.036, 0.041999999999999996,
						Double.NaN, Double.NaN, Double.NaN } };

		List<String> firstChannels = new ArrayList<String>();
		for (int i = 1; i < 4; i++) {
			firstChannels.add("Test" + i);
		}
		ChannelTestingList ctl_p = new ChannelTestingList(
				MatrixMixType.AddAfter, null, firstChannels, "Simple");

		List<String> secondChannels = new ArrayList<String>();
		for (int i = 1; i < 4; i++) {
			secondChannels.add("Check" + i);
		}
		for (MatrixMixType m : MatrixMixType.values()) {
			if(m.equals(MatrixMixType.AddMerged)) {
				continue;
			}
			ChannelTestingList ctl = new ChannelTestingList(m, ctl_p,
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

	@Test(dependsOnMethods = { "TestIndividualMatrices" })
	public void TestAddBefore() {
		List<String> firstChannels = new ArrayList<String>();
		for (int i = 1; i < 4; i++) {
			firstChannels.add("Test" + i);
		}
		ChannelTestingList ctl_p = new ChannelTestingList(
				MatrixMixType.AddAfter, null, firstChannels, "Simple");

		List<String> secondChannels = new ArrayList<String>();
		for (int i = 1; i < 4; i++) {
			secondChannels.add("Check" + i);
		}
		ChannelTestingList ctl = new ChannelTestingList(MatrixMixType.AddAfter,
				ctl_p, secondChannels, "AddAfter");
		ChannelDataGenerator cdg = new ChannelDataGenerator(ctl,
				MatrixMixType.AddBefore, 2);
		cdg.generateParts();
		cdg.generateWhole();
		double[][] expected = { { 222.0, 1.0, 0.0, 1.0, 0.0, -0.0030, 0.0060 },
				{ 222.02, 1.0, 0.0, 1.0, 0.01, -0.016, 0.022 } };
		log.debug("First " + cdg.getData(TestingParts.First));
		DoubleArrayDataGenerator.compareData(cdg.getData(TestingParts.First)
				.getData(), expected);
		log.debug("Second " + cdg.getData(TestingParts.Second));
		double[][] expected1 = {
				{ 222.04, 1.0, 0.0, 1.0, 0.02, -0.023, 0.026000000000000002 },
				{ 222.06, 1.0, 1.0, 0.0, 0.03, -0.036, 0.041999999999999996 } };
		DoubleArrayDataGenerator.compareData(cdg.getData(TestingParts.Second)
				.getData(), expected1);
		log.debug("All " + cdg.getData(TestingParts.All));
		double[][] expected2 = {
				{ 222.0, 1.0, 0.0, 1.0, 0.0, -0.0030, 0.0060, Double.NaN,
						Double.NaN, Double.NaN },
				{ 222.02, 1.0, 0.0, 1.0, 0.01, -0.016, 0.022, Double.NaN,
						Double.NaN, Double.NaN },
				{ 222.04, 1.0, 0.0, 1.0, Double.NaN, Double.NaN, Double.NaN,
						0.02, -0.023, 0.026000000000000002 },
				{ 222.06, 1.0, 1.0, 0.0, Double.NaN, Double.NaN, Double.NaN,
						0.03, -0.036, 0.041999999999999996 } };
		DoubleArrayDataGenerator.compareData(cdg.getData(TestingParts.All)
				.getData(), expected2);
	}

	@Test(dependsOnMethods = { "TestIndividualMatrices" })
	public void TestAddBetween() {
		List<String> firstChannels = new ArrayList<String>();
		for (int i = 1; i < 4; i++) {
			firstChannels.add("Test" + i);
		}
		ChannelTestingList ctl_p = new ChannelTestingList(
				MatrixMixType.AddAfter, null, firstChannels, "Simple");

		List<String> secondChannels = new ArrayList<String>();
		for (int i = 1; i < 4; i++) {
			secondChannels.add("Check" + i);
		}
		ChannelTestingList ctl = new ChannelTestingList(MatrixMixType.AddAfter,
				ctl_p, secondChannels, "AddAfter");
		ChannelDataGenerator cdg = new ChannelDataGenerator(ctl,
				MatrixMixType.AddMiddle, 2);
		cdg.generateParts();
		cdg.generateWhole();
		double[][] expected = { { 222.0, 1.0, 0.0, 1.0, 0.0, -0.0030, 0.0060 },
				{ 222.06, 1.0, 1.0, 0.0, 0.03, -0.033, 0.036 } };
		log.debug("First " + cdg.getData(TestingParts.First));
		DoubleArrayDataGenerator.compareData(cdg.getData(TestingParts.First)
				.getData(), expected);
		log.debug("Second " + cdg.getData(TestingParts.Second));
		double[][] expected1 = {
				{ 222.02, 1.0, 0.0, 1.0, 0.01, -0.013000000000000001, 0.016 },
				{ 222.04, 1.0, 0.0, 1.0, 0.02, -0.026000000000000002, 0.032 } };
		DoubleArrayDataGenerator.compareData(cdg.getData(TestingParts.Second)
				.getData(), expected1);
		log.debug("All " + cdg.getData(TestingParts.All));
		double[][] expected2 = {
				{ 222.0, 1.0, 0.0, 1.0, 0.0, -0.0030, 0.0060, Double.NaN,
						Double.NaN, Double.NaN },
				{ 222.02, 1.0, 0.0, 1.0, Double.NaN, Double.NaN, Double.NaN,
						0.01, -0.013000000000000001, 0.016 },
				{ 222.04, 1.0, 0.0, 1.0, Double.NaN, Double.NaN, Double.NaN,
						0.02, -0.026000000000000002, 0.032 },
				{ 222.06, 1.0, 1.0, 0.0, 0.03, -0.033, 0.036, Double.NaN,
						Double.NaN, Double.NaN } };
		DoubleArrayDataGenerator.compareData(cdg.getData(TestingParts.All)
				.getData(), expected2);
	}

	@Test(dependsOnMethods = { "TestIndividualMatrices" })
	public void TestAddInterleaved() {
		List<String> firstChannels = new ArrayList<String>();
		for (int i = 1; i < 4; i++) {
			firstChannels.add("Test" + i);
		}
		ChannelTestingList ctl_p = new ChannelTestingList(
				MatrixMixType.AddAfter, null, firstChannels, "Simple");

		List<String> secondChannels = new ArrayList<String>();
		for (int i = 1; i < 4; i++) {
			secondChannels.add("Check" + i);
		}
		ChannelTestingList ctl = new ChannelTestingList(MatrixMixType.AddAfter,
				ctl_p, secondChannels, "AddAfter");
		ChannelDataGenerator cdg = new ChannelDataGenerator(ctl,
				MatrixMixType.AddInterleaved, 2);
		cdg.generateParts();
		cdg.generateWhole();
		double[][] expected = { { 222.0, 1.0, 0.0, 1.0, 0.0, -0.0030, 0.0060 },
				{ 222.02, 1.0, 0.0, 1.0, 0.01, -0.016, 0.022 } };
		log.debug("First " + cdg.getData(TestingParts.First));
		DoubleArrayDataGenerator.compareData(cdg.getData(TestingParts.First)
				.getData(), expected);
		log.debug("Second " + cdg.getData(TestingParts.Second));
		double[][] expected1 = {
				{ 222.01, 1.0, 0.0, 1.0, 0.0, -0.0030, 0.0060 },
				{ 222.03, 1.0, 0.0, 1.0, 0.01, -0.016, 0.022 } };
		DoubleArrayDataGenerator.compareData(cdg.getData(TestingParts.Second)
				.getData(), expected1);
		log.debug("All " + cdg.getData(TestingParts.All));
		double[][] expected2 = {
				{ 222.0, 1.0, 0.0, 1.0, 0.0, -0.0030, 0.0060, Double.NaN,
						Double.NaN, Double.NaN },
				{ 222.01, 1.0, 0.0, 1.0, Double.NaN, Double.NaN, Double.NaN,
						0.0, -0.0030, 0.0060 },
				{ 222.02, 1.0, 0.0, 1.0, 0.01, -0.016, 0.022, Double.NaN,
						Double.NaN, Double.NaN },
				{ 222.03, 1.0, 0.0, 1.0, Double.NaN, Double.NaN, Double.NaN,
						0.01, -0.016, 0.022 } };
		DoubleArrayDataGenerator.compareData(cdg.getData(TestingParts.All)
				.getData(), expected2);
	}

	@Test(dependsOnMethods = { "TestIndividualMatrices" })
	public void TestAddMerged() {
		List<String> firstChannels = new ArrayList<String>();
		for (int i = 1; i < 4; i++) {
			firstChannels.add("Test" + i);
		}
		ChannelTestingList ctl_p = new ChannelTestingList(
				MatrixMixType.AddAfter, null, firstChannels, "Simple");

		List<String> secondChannels = new ArrayList<String>();
		for (int i = 1; i < 4; i++) {
			secondChannels.add("Check" + i);
		}
		ChannelTestingList ctl = new ChannelTestingList(MatrixMixType.AddAfter,
				ctl_p, secondChannels, "AddAfter");
		ChannelDataGenerator cdg = new ChannelDataGenerator(ctl,
				MatrixMixType.AddMerged, 2);
		cdg.generateParts();
		cdg.generateWhole();
		double[][] expected = { { 222.0, 1.0, 0.0, 1.0, 0.0, -0.0030, 0.0060 },
				{ 222.02, 1.0, 0.0, 1.0, 0.01, -0.016, 0.022 } };
		log.debug("First " + cdg.getData(TestingParts.First));
		DoubleArrayDataGenerator.compareData(cdg.getData(TestingParts.First)
				.getData(), expected);
		log.debug("Second " + cdg.getData(TestingParts.Second));
		double[][] expected1 = {
				{ 222.0, 1.0, 0.0, 1.0, 0.0, -0.0030, 0.0060 },
				{ 222.02, 1.0, 0.0, 1.0, 0.01, -0.016, 0.022 } };
		DoubleArrayDataGenerator.compareData(cdg.getData(TestingParts.Second)
				.getData(), expected1);
		log.debug("All " + cdg.getData(TestingParts.All));
		double[][] expected2 = {
				{ 222.0, 1.0, 0.0, 1.0, 0.0, -0.0030, 0.0060, 0.0, -0.0030,
						0.0060 },
				{ 222.02, 1.0, 0.0, 1.0, 0.01, -0.016, 0.022, 0.01, -0.016,
						0.022 } };
		DoubleArrayDataGenerator.compareData(cdg.getData(TestingParts.All)
				.getData(), expected2);
	}

	private double[][] columnMix(MatrixMixType mix, double[][] data,
			ChannelTestingList ctl) {
		int fcsz = ctl.getExistingList().size();
		int scsz = ctl.getNewChannels().size();
		List<List<Double>> accum = (new DoubleMatrix(data)).toList();
		if(mix.equals(MatrixMixType.AddAfter)) {
			return data;
		}
		if(mix.equals(MatrixMixType.AddMerged)) {
			log.error(mix + " is not a valid colum mix type");
			Assert.fail();
			return null;
		}
		for (List<Double> row : accum) {
			List<Double> tmp = new ArrayList<Double>();
			tmp.addAll(row);
			row.clear();
			row.addAll(tmp.subList(0, 4));
			if (mix.equals(MatrixMixType.AddBefore)) {
				row.addAll(tmp.subList(fcsz + 4, tmp.size()));
				row.addAll(tmp.subList(4, fcsz + 4));
			}
			if (mix.equals(MatrixMixType.AddMiddle)) {
				int hscsz = scsz / 2;
				row.addAll(tmp.subList(fcsz + 4, fcsz + 4 + hscsz));
				row.addAll(tmp.subList(4, fcsz + 4));
				row.addAll(tmp.subList(fcsz + 4 + hscsz, tmp.size()));
			}
			if (mix.equals(MatrixMixType.AddInterleaved)) {
				int smaller = fcsz;
				boolean firstIsBigger = false;
				if(fcsz > scsz) {
					smaller = scsz;
					firstIsBigger = true;
				}
				for ( int c = 0; c < smaller; c++) {
					row.add(tmp.get(4 + fcsz + c));
					row.add(tmp.get(4 + c));
				}
				if(firstIsBigger) {
					row.addAll(tmp.subList(4 + smaller, fcsz));
				} else {
					row.addAll(tmp.subList(4 + fcsz + smaller, tmp.size()));
				}
			}
		}
		DoubleMatrix result = new DoubleMatrix(accum);
		log.debug("Expected mix " + result);
		return result.getData();
	}
}
