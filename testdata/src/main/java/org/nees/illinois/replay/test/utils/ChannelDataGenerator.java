package org.nees.illinois.replay.test.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nees.illinois.replay.data.DoubleMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChannelDataGenerator {
	public enum TestingParts {
		All, First, Second
	}

	private final ChannelTestingList ctl;

	private final Map<TestingParts, DoubleMatrix> dataParts = new HashMap<TestingParts, DoubleMatrix>();

	private final int numberOfRows;

	private DataRowGenerator rowGen;;

	private final MatrixMixType rowMix;
	private final double startTime = 222.0;
	private TimeGenerator time;
	private final double timeMultiplier = .02;
	private final Logger log = LoggerFactory
			.getLogger(ChannelDataGenerator.class);

	public ChannelDataGenerator(ChannelTestingList ctl, MatrixMixType rowMix,
			int numberOfRows) {
		super();
		this.ctl = ctl;
		this.rowMix = rowMix;
		this.numberOfRows = numberOfRows;
	}

	public void generateParts() {
		List<String> newChannels = ctl.getNewChannels();
		List<String> existing = ctl.getExistingList();
		if (rowMix.equals(MatrixMixType.AddAfter)) {
			mixAddAfter(newChannels.size(), existing.size());
		} else if (rowMix.equals(MatrixMixType.AddBefore)) {
			mixAddBefore(newChannels.size(), existing.size());
		} else if (rowMix.equals(MatrixMixType.AddMiddle)) {
			mixAddBetween(newChannels.size(), existing.size());
		} else if (rowMix.equals(MatrixMixType.AddInterleaved)) {
			mixAddInterleaved(newChannels.size(), existing.size());
		} else {
			mixAddMerged(newChannels.size(), existing.size());
		}

	}

	public void generateWhole() {
		List<String> newChannels = ctl.getNewChannels();
		List<String> existing = ctl.getExistingList();
		int fsz = existing.size();
		int ssz = newChannels.size();
		int moreColumns = fsz > ssz ? fsz : ssz;
		;
		List<Double> nulls = new ArrayList<Double>();
		for (int i = 0; i < moreColumns; i++) {
			nulls.add(null);
		}

		DoubleMatrix fData = dataParts.get(TestingParts.First);
		DoubleMatrix sData = dataParts.get(TestingParts.Second);
		List<List<Double>> fLists;
		if (fData == null) {
			fLists = new ArrayList<List<Double>>();
		} else {
			fLists = fData.toList();
		}
		List<List<Double>> sLists = sData.toList();
		List<List<Double>> allL = new ArrayList<List<Double>>();

		if (rowMix.equals(MatrixMixType.AddMerged)) {
			for (int r = 0; r < numberOfRows; r++) {
				List<Double> row = new ArrayList<Double>();
				row.addAll(fLists.get(r));
				List<Double> second = sLists.get(r);
				row.addAll(second.subList(4, second.size()));
				allL.add(row);
			}
		} else {
			List<Double> tmp = new ArrayList<Double>();
			for (List<Double> row : fLists) {
				row.addAll(nulls.subList(0, ssz));
			}
			for (List<Double> row : sLists) {
				tmp.clear();
				tmp.addAll(row);
				row.clear();
				row.addAll(tmp.subList(0, 4)); // add time columns
				row.addAll(nulls.subList(0, fsz));
				row.addAll(tmp.subList(4, tmp.size())); // add data
			}

			if (fData != null) {
				allL.addAll(fLists);
			}
			if (sData != null) {
				allL.addAll(sLists);
			}
		}
		SortableDoubleMatrix sdm = new SortableDoubleMatrix(allL);
		sdm.sort();
		dataParts.put(TestingParts.All, sdm);
	}

	public void mixColumns() {
		int fcsz = ctl.getExistingList().size();
		int scsz = ctl.getNewChannels().size();
		MatrixMixType mix = ctl.getMix();
		List<List<Double>> accum = (dataParts.get(TestingParts.All)).toList();
		if (mix.equals(MatrixMixType.AddAfter)) {
			return;
		}
		if (mix.equals(MatrixMixType.AddMerged)) {
			log.error(mix + " is not a valid colum mix type");
			return;
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
				if (fcsz > scsz) {
					smaller = scsz;
					firstIsBigger = true;
				}
				for (int c = 0; c < smaller; c++) {
					row.add(tmp.get(4 + fcsz + c));
					row.add(tmp.get(4 + c));
				}
				if (firstIsBigger) {
					row.addAll(tmp.subList(4 + smaller, fcsz));
				} else {
					row.addAll(tmp.subList(4 + fcsz + smaller, tmp.size()));
				}
			}
		}
		dataParts.put(TestingParts.All, new DoubleMatrix(accum));
	}

	private DoubleMatrix genSeparateSet(int rows, int cols) {
		if (cols == 0) {
			return null;
		}
		rowGen = new DataRowGenerator(cols);
		int numberOfColumns = cols;
		double[][] data = new double[rows][numberOfColumns];
		for (int r = 0; r < rows; r++) {
			data[r] = rowGen.genRecord(time);
			time.increment();
			time.incrementStep();
		}
		return new DoubleMatrix(data);
	}

	/**
	 * @return the ctl
	 */
	public ChannelTestingList getCtl() {
		return ctl;
	}

	public DoubleMatrix getData(TestingParts part) {
		return dataParts.get(part);
	}

	/**
	 * @return the dataParts
	 */
	public Map<TestingParts, DoubleMatrix> getDataParts() {
		return dataParts;
	}

	/**
	 * @return the numberOfRows
	 */
	public int getNumberOfRows() {
		return numberOfRows;
	}

	/**
	 * @return the rowMix
	 */
	public MatrixMixType getRowMix() {
		return rowMix;
	}

	private void mixAddAfter(int ssz, int fsz) {
		time = new TimeGenerator(timeMultiplier, startTime);
		DoubleMatrix sData = genSeparateSet(numberOfRows, ssz);
		DoubleMatrix fData = genSeparateSet(numberOfRows, fsz);
		dataParts.put(TestingParts.First, fData);
		dataParts.put(TestingParts.Second, sData);
	}

	private void mixAddBefore(int ssz, int fsz) {
		time = new TimeGenerator(timeMultiplier, startTime);
		DoubleMatrix fData = genSeparateSet(numberOfRows, fsz);
		DoubleMatrix sData = genSeparateSet(numberOfRows, ssz);
		dataParts.put(TestingParts.First, fData);
		dataParts.put(TestingParts.Second, sData);
	}

	private void mixAddBetween(int ssz, int fsz) {
		time = new TimeGenerator(timeMultiplier, startTime);
		int bef = numberOfRows / 2;
		int aft = numberOfRows - bef;
		DoubleMatrix fData = genSeparateSet(bef, fsz);
		DoubleMatrix sData = genSeparateSet(numberOfRows, ssz);
		DoubleMatrix aData = genSeparateSet(aft, fsz);
		List<List<Double>> consolidated = fData.toList();
		consolidated.addAll(aData.toList());

		dataParts.put(TestingParts.First, new DoubleMatrix(consolidated));
		dataParts.put(TestingParts.Second, sData);
	}

	private void mixAddInterleaved(int ssz, int fsz) {
		time = new TimeGenerator(timeMultiplier, startTime);
		DoubleMatrix fData = genSeparateSet(numberOfRows, fsz);
		time = new TimeGenerator(timeMultiplier, startTime + .01);
		DoubleMatrix sData = genSeparateSet(numberOfRows, ssz);
		dataParts.put(TestingParts.First, fData);
		dataParts.put(TestingParts.Second, sData);
	}

	private void mixAddMerged(int ssz, int fsz) {
		time = new TimeGenerator(timeMultiplier, startTime);
		DoubleMatrix fData = genSeparateSet(numberOfRows, fsz);
		time = new TimeGenerator(timeMultiplier, startTime);
		DoubleMatrix sData = genSeparateSet(numberOfRows, ssz);
		dataParts.put(TestingParts.First, fData);
		dataParts.put(TestingParts.Second, sData);
	}
}
