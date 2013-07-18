package org.nees.illinois.replay.test.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.DoubleMatrixI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class which generates data sets based on the various channel lists in a
 * {@link QueryChannelLists} specification.
 * @author Michael Bletzinger
 */
public class ChannelDataGenerator {
	/**
	 * Defines which set of channel names to include.
	 * @author Michael Bletzinger
	 */
	public enum TestingParts {
		/**
		 * Include all channels.
		 */
		All,
		/**
		 * Include the first set.
		 */
		First,
		/**
		 * include the second set.
		 */
		Second
	}

	/**
	 * List of {@link QueryChannelLists} used to test queries.
	 */
	private final QueryChannelLists ctl;
	/**
	 * Map of data sets used to query data.
	 */
	private final Map<TestingParts, DoubleMatrixI> dataParts = new HashMap<TestingParts, DoubleMatrixI>();
	/**
	 * Number of data rows to generate.
	 */
	private final int numberOfRows;
	/**
	 * Data generator.
	 */
	private DataRowGenerator rowGen;;
	/**
	 * Defines how the data parts are combined both row-wise and column-wise.
	 */
	private final MatrixMixType rowMix;
	/**
	 * Start time for data records.
	 */
	private final double startTime = 222.0;
	/**
	 * Generate for time.
	 */
	private TimeGenerator time;
	/**
	 * Determines time interval between data records.
	 */
	private final double timeMultiplier = .02;
	/**
	 * Number of time columns.
	 */
	private final int timeColumns = 1;
	/**
	 * Logger.
	 */
	private final Logger log = LoggerFactory
			.getLogger(ChannelDataGenerator.class);

	/**
	 * Constructor.
	 * @param ctl
	 *            Map of data sets used to query data.
	 * @param rowMix
	 *            Defines how the data rows are combined.
	 * @param numberOfRows
	 *            Number of data rows to generate.
	 */
	public ChannelDataGenerator(final QueryChannelLists ctl,
			final MatrixMixType rowMix, final int numberOfRows) {
		super();
		this.ctl = ctl;
		this.rowMix = rowMix;
		this.numberOfRows = numberOfRows;
	}

	/**
	 * Generates the individual data sets.
	 */
	public final void generateParts() {
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

	/**
	 * Generates the expected combined data set.
	 */
	public final void generateWhole() {
		List<String> newChannels = ctl.getNewChannels();
		List<String> existing = ctl.getExistingList();
		int fsz = existing.size();
		int ssz = newChannels.size();
		int moreColumns = fsz > ssz ? fsz : ssz;
		List<Double> nulls = new ArrayList<Double>();
		for (int i = 0; i < moreColumns; i++) {
			nulls.add(null);
		}

		DoubleMatrixI fData = dataParts.get(TestingParts.First);
		DoubleMatrixI sData = dataParts.get(TestingParts.Second);
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
				row.addAll(second.subList(timeColumns, second.size()));
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
				row.addAll(tmp.subList(0, timeColumns)); // add time columns
				row.addAll(nulls.subList(0, fsz));
				row.addAll(tmp.subList(timeColumns, tmp.size())); // add
																	// data
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

	/**
	 * Mix the data columns based on the {@link QueryChannelLists}
	 * specifications.
	 */
	public final void mixColumns() {
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
		dataParts.put(TestingParts.All, new DoubleMatrix(accum));
	}

	/**
	 * Generate a data set.
	 * @param rows
	 *            number of rows.
	 * @param cols
	 *            number of columns.
	 * @return double matrix of data.
	 */
	private DoubleMatrix genSeparateSet(final int rows, final int cols) {
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
	 * @return the query channel lists.
	 */
	public final QueryChannelLists getCtl() {
		return ctl;
	}

	/**
	 * Get a dataset.
	 * @param part
	 *            Which data set.
	 * @return double matrix of data.
	 */
	public final DoubleMatrixI getData(final TestingParts part) {
		return dataParts.get(part);
	}

	/**
	 * @return the dataParts
	 */
	public final Map<TestingParts, DoubleMatrixI> getDataParts() {
		return dataParts;
	}

	/**
	 * @return the numberOfRows
	 */
	public final int getNumberOfRows() {
		return numberOfRows;
	}

	/**
	 * @return the rowMix
	 */
	public final MatrixMixType getRowMix() {
		return rowMix;
	}

	/**
	 * Add the first data set after the second one.
	 * @param ssz
	 *            Size of first data set.
	 * @param fsz
	 *            Size of the second data set.
	 */
	private void mixAddAfter(final int ssz, final int fsz) {
		time = new TimeGenerator(timeMultiplier, startTime);
		DoubleMatrix sData = genSeparateSet(numberOfRows, ssz);
		DoubleMatrix fData = genSeparateSet(numberOfRows, fsz);
		dataParts.put(TestingParts.First, fData);
		dataParts.put(TestingParts.Second, sData);
	}

	/**
	 * Add the first data set before the second one.
	 * @param ssz
	 *            Size of first data set.
	 * @param fsz
	 *            Size of the second data set.
	 */
	private void mixAddBefore(final int ssz, final int fsz) {
		time = new TimeGenerator(timeMultiplier, startTime);
		DoubleMatrix fData = genSeparateSet(numberOfRows, fsz);
		DoubleMatrix sData = genSeparateSet(numberOfRows, ssz);
		dataParts.put(TestingParts.First, fData);
		dataParts.put(TestingParts.Second, sData);
	}

	/**
	 * Insert the rows of the first data set into the middle of the second one.
	 * @param ssz
	 *            Size of first data set.
	 * @param fsz
	 *            Size of the second data set.
	 */
	private void mixAddBetween(final int ssz, final int fsz) {
		time = new TimeGenerator(timeMultiplier, startTime);
		int bef = numberOfRows / 2;
		int aft = numberOfRows - bef;
		DoubleMatrixI fData = genSeparateSet(bef, fsz);
		DoubleMatrix sData = genSeparateSet(numberOfRows, ssz);
		DoubleMatrixI aData = genSeparateSet(aft, fsz);
		List<List<Double>> consolidated = fData.toList();
		consolidated.addAll(aData.toList());

		dataParts.put(TestingParts.First, new DoubleMatrix(consolidated));
		dataParts.put(TestingParts.Second, sData);
	}

	/**
	 * Alternate the first data set rows with the second one.
	 * @param ssz
	 *            Size of first data set.
	 * @param fsz
	 *            Size of the second data set.
	 */
	private void mixAddInterleaved(final int ssz, final int fsz) {
		time = new TimeGenerator(timeMultiplier, startTime);
		final double timeOffset = 0.01;
		DoubleMatrix fData = genSeparateSet(numberOfRows, fsz);
		time = new TimeGenerator(timeMultiplier, startTime + timeOffset);
		DoubleMatrix sData = genSeparateSet(numberOfRows, ssz);
		dataParts.put(TestingParts.First, fData);
		dataParts.put(TestingParts.Second, sData);
	}

	/**
	 * Change the times of the first data set rows to match the rows of the
	 * second one.
	 * @param ssz
	 *            Size of first data set.
	 * @param fsz
	 *            Size of the second data set.
	 */
	private void mixAddMerged(final int ssz, final int fsz) {
		time = new TimeGenerator(timeMultiplier, startTime);
		DoubleMatrix fData = genSeparateSet(numberOfRows, fsz);
		time = new TimeGenerator(timeMultiplier, startTime);
		DoubleMatrix sData = genSeparateSet(numberOfRows, ssz);
		dataParts.put(TestingParts.First, fData);
		dataParts.put(TestingParts.Second, sData);
	}
}
