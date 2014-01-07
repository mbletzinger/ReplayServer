package org.nees.illinois.replay.test.utils.data;


/**
 * Class which generates a row of data to be used in unit testing.
 * @author Michael Bletzinger
 */
public class DataRowGenerator {
	/**
	 * Number of columns in the row.
	 */
	private final int numberOfColumns;
	/**
	 * Iteration counter to alter the data from row to row.
	 */
	private int unique = 1;
	/**
	 * number of time columns.
	 */
	private final int timeColumns = 1;

	/**
	 * Generate a row.
	 * @param time
	 *            Time generator to use for the first 4 columns.
	 * @return Array of doubles.
	 */
	public final double[] genRecord(final TimeGenerator time) {
		double[] result = genData(timeColumns, time.getRecordNumber());
		result[0] = time.getTime();
		return result;
	}

	/**
	 * @param numberOfColumns
	 *            Number of columns in the row.
	 */
	public DataRowGenerator(final int numberOfColumns) {
		super();
		this.numberOfColumns = numberOfColumns;
	}

	/**
	 * Generate data.
	 * @param start
	 *            Start of the data columns.
	 * @param recordNumber
	 *            Row number used to uniquefy the data.
	 * @return Array of doubles.
	 */
	private double[] genData(final int start, final int recordNumber) {
		double[] result = new double[start + numberOfColumns];
		for (int c = 0; c < numberOfColumns; c++) {
			final int recordNumberModulus = 20;
			final double uniquefyMultiplier = 0.01;
			final double columnUniquefyScale = 0.003;
			result[c + start] = ((recordNumber % recordNumberModulus)
					* uniquefyMultiplier + c * columnUniquefyScale * unique)
					* (c % 2 == 0 ? 1 : -1);
		}
		final int columnUniquefyMax = 3;
		unique++;
		if (unique == columnUniquefyMax) {
			unique = 1;
		}
		return result;

	}
}
