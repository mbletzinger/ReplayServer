package org.nees.illinois.replay.test.db.utils;

import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.DoubleMatrixI;
import org.nees.illinois.replay.test.utils.DoubleArrayDataGenerator;
import org.nees.illinois.replay.test.utils.TestDatasetType;
import org.nees.illinois.replay.test.utils.TestDatasets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages test data sets for update testing.
 * @author Michael Bletzinger
 */
public class UpdateDataSets {
	/**
	 * Set of dataset descriptions.
	 */
	private final TestDatasets set;
	/**
	 * List of update data set parameters.
	 */
	private final List<UpdateSpec> updateSizes = new ArrayList<UpdateSpec>();
	/**
	 * Time interval to use.
	 */
	private final double interval = 0.02;
	/**
	 * Start time.
	 */
	private final double startTime = 222.0;
	/**
	 * Logger.
	 **/
	private final Logger log = LoggerFactory.getLogger(UpdateDataSets.class);

	/**
	 * Generate a double matrix for an update set.
	 * @param type
	 *            Set type.
	 * @param updateNumber
	 *            Identifies the update.
	 * @return the data.
	 */
	public final DoubleMatrixI generate(final TestDatasetType type,
			final int updateNumber) {
		int rows = updateSizes.get(updateNumber).getRowSize();
		int columns = set.getChannels(type).size();
		double time = updateSizes.get(updateNumber).getStartTime();
		DoubleArrayDataGenerator dg = new DoubleArrayDataGenerator(rows,
				columns, interval, time);
		double[][] data = dg.generate();
		DoubleMatrixI result = new DoubleMatrix(data);
		log.debug("For list type " + type + " update " + updateNumber
				+ " creating " + result);
		return result;
	}

	/**
	 * @param set
	 *            Set of dataset descriptions.
	 */
	public UpdateDataSets(final TestDatasets set) {
		this.set = set;
		final int[] sizes = { 4, 2, 6, 3, 5 };
		int cumRows = 0;
		for (int s : sizes) {
			double tm = cumRows * interval + startTime;
			updateSizes.add(new UpdateSpec(s, tm));
		}
	}

	/**
	 * @return The number of updates.
	 */
	public final int numberOfUpdates() {
		return updateSizes.size();
	}
}
