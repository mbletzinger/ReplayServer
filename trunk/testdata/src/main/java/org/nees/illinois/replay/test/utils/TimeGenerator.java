package org.nees.illinois.replay.test.utils;

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
	 * Starting step number.
	 */
	private final int[] startStepNumber = { 1, 0, 1 };
	/**
	 * Start time.
	 */
	private final double startTime;
	/**
	 * Number of step types.
	 */
	private final int stepTuples = 3;
	/**
	 * Current step number.
	 */
	private int[] stepNumber = new int[stepTuples];
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
		stepNumber = startStepNumber;
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
	 * @return the startStepNumber
	 */
	public final int[] getStartStepNumber() {
		return startStepNumber;
	}

	/**
	 * @return the startTime
	 */
	public final double getStartTime() {
		return startTime;
	}

	/**
	 * @return the stepNumber
	 */
	public final int[] getStepNumber() {
		return stepNumber;
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
	 * Next step number.
	 */
	public final void incrementStep() {
		final int stepBase = 4;
		final int substepBase = 3;
		final int correctionScaleFactor = 10;
		if (recordNumber % stepBase == 0) {
			stepNumber[0]++;
			stepNumber[1] = 0;
			stepNumber[2] = 0;
		}
		if (recordNumber % substepBase == 0) {
			stepNumber[1]++;
			stepNumber[2] = 0;
		}
		stepNumber[2] += timeMultiplier * correctionScaleFactor;
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
