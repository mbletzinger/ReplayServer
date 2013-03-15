package org.nees.illinois.replay.test.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nees.illinois.replay.data.DoubleMatrix;

public class ChannelDataGenerator {
	public enum TestingParts {
		First, Second, All
	};

	private final ChannelTestingList ctl;
	private final MatrixMixType rowMix;
	private final int numberOfRows;
	private int[] sizes;
	private TimeGenerator time;
	private DataRowGenerator rows;
	private final double timeMultiplier = .02;
	private final double startTime = 222.0;

	public ChannelDataGenerator(ChannelTestingList ctl, MatrixMixType rowMix,
			int numberOfRows) {
		super();
		this.ctl = ctl;
		this.rowMix = rowMix;
		this.numberOfRows = numberOfRows;
	}

	private final Map<TestingParts, DoubleMatrix> dataParts = new HashMap<TestingParts, DoubleMatrix>();
	private DoubleMatrix result;

	public void generate() {
		List<String> first = ctl.getNewChannels();
		List<String> second = ctl.getExisting().combine();
		
		if (rowMix.equals(MatrixMixType.AddAfter)) {
			mixAddAfter(first.size(), second.size());
		} else if (rowMix.equals(MatrixMixType.AddBefore)) {
			mixAddBefore(first.size(), second.size());
		} else if (rowMix.equals(MatrixMixType.AddMiddle)) {
			mixAddBetween(first.size(), second.size());
		} else if (rowMix.equals(MatrixMixType.AddInterleaved)) {
			mixAddInterleaved(first.size(), second.size());
		} else {
			mixAddMerged(first.size(), second.size());
		}

		DoubleMatrix fData = dataParts.get(TestingParts.First);
		DoubleMatrix sData = dataParts.get(TestingParts.Second);

		List<List<Double>> allL = new ArrayList<List<Double>>();
		allL.addAll(fData.toList());
		allL.addAll(sData.toList());
		SortableDoubleMatrix sdm = new SortableDoubleMatrix(allL);
		sdm.sort();
		dataParts.put(TestingParts.All, sdm);

	}

	public DoubleMatrix getData(TestingParts part) {
		return dataParts.get(part);
	}

	private DoubleMatrix genSeperateSet(int sz) {
		rows = new DataRowGenerator(sz);
		int numberOfColumns = sz;
		double[][] data = new double[numberOfRows][numberOfColumns];
		for (int r = 0; r < numberOfRows; r++) {
			data[r] = rows.genRecord(time);
			time.increment();
			time.incrementStep();
		}
		return new DoubleMatrix(data);
	}

	private void mixAddBefore(int fsz, int ssz) {
		time = new TimeGenerator(timeMultiplier, startTime);
		DoubleMatrix fData = genSeperateSet(fsz);
		DoubleMatrix sData = genSeperateSet(ssz);
		dataParts.put(TestingParts.First, fData);
		dataParts.put(TestingParts.Second, sData);
	}

	private void mixAddBetween(int fsz, int ssz) {
		time = new TimeGenerator(timeMultiplier, startTime);
		int bef = fsz / 2;
		int aft = fsz - bef;
		DoubleMatrix fData = genSeperateSet(bef);
		DoubleMatrix sData = genSeperateSet(ssz);
		DoubleMatrix aData = genSeperateSet(aft);

		dataParts.put(TestingParts.First, fData);
		dataParts.put(TestingParts.Second, sData);
	}

	private void mixAddAfter(int fsz, int ssz) {
		time = new TimeGenerator(timeMultiplier, startTime);
		DoubleMatrix sData = genSeperateSet(ssz);
		DoubleMatrix fData = genSeperateSet(fsz);
		dataParts.put(TestingParts.First, fData);
		dataParts.put(TestingParts.Second, sData);
	}

	private void mixAddInterleaved(int fsz, int ssz) {
		time = new TimeGenerator(timeMultiplier, startTime);
		DoubleMatrix fData = genSeperateSet(fsz);
		time = new TimeGenerator(timeMultiplier + .01, startTime + .03);
		DoubleMatrix sData = genSeperateSet(ssz);
		dataParts.put(TestingParts.First, fData);
		dataParts.put(TestingParts.Second, sData);
	}

	private void mixAddMerged(int fsz, int ssz) {
		time = new TimeGenerator(timeMultiplier, startTime);
		DoubleMatrix fData = genSeperateSet(fsz);
		time = new TimeGenerator(timeMultiplier, startTime);
		DoubleMatrix sData = genSeperateSet(ssz);
		dataParts.put(TestingParts.First, fData);
		dataParts.put(TestingParts.Second, sData);
	}
}
