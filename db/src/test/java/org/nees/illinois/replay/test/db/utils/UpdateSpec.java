package org.nees.illinois.replay.test.db.utils;

/**
 * Class which describes a test update dataset.
 * @author Michael Bletzinger
 */
public class UpdateSpec {
	/**
	 * @return the rowSize
	 */
	public final int getRowSize() {
		return rowSize;
	}

	/**
	 * @return the startTime
	 */
	public final double getStartTime() {
		return startTime;
	}

	/**
	 * Number of rows to generate.
	 */
	private final int rowSize;
	/**
	 * Time of first row.
	 */
	private final double startTime;

	/**
	 * @param rowSize
	 *            Number of rows to generate.
	 * @param startTime
	 *            Time of first row.
	 */
	public UpdateSpec(final int rowSize, final double startTime) {
		this.rowSize = rowSize;
		this.startTime = startTime;
	}
}
