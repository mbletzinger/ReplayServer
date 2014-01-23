package org.nees.illinois.replay.test.utils.data;

/**
 * Class to generate timestamps and step numbers based on the start time and
 * intervals.
 * @author Michael Bletzinger
 */
public class TimeGenerator {
	/**
	 * Generation counter for calculating intervals.
	 */
	private int recordNumber = 0;
	/**
	 * Start time.
	 */
	private final double startTime;
	/**
	 * Time interval.
	 */
	private final double timeMultiplier;

	/**
	 * @param timeMultiplier
	 *            Time interval.
	 * @param startTime
	 *            Start time.
	 */
	public TimeGenerator(final double timeMultiplier, final double startTime) {
		this.timeMultiplier = timeMultiplier;
		this.startTime = startTime;
	}

	/**
	 * @return the recordNumber
	 */
	public final int getRecordNumber() {
		return recordNumber;
	}

	/**
	 * @return the startTime
	 */
	public final double getStartTime() {
		return startTime;
	}

	/**
	 * Generate a timestamp.
	 * @return the timestamp.
	 */
	public final double getTime() {
		return startTime + (recordNumber * timeMultiplier);
	}

	/**
	 * @return the timeMultiplier
	 */
	public final double getTimeMultiplier() {
		return timeMultiplier;
	}

	/**
	 * Next record.
	 */
	public final void increment() {
		recordNumber++;
	}

	/**
	 * Reset record counter.
	 */
	public final void reset() {
		recordNumber = 0;
	}

	/**
	 * @param recordNumber
	 *            the recordNumber to set
	 */
	public final void setRecordNumber(final int recordNumber) {
		this.recordNumber = recordNumber;
	}
}
