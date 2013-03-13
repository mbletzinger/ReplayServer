package org.nees.illinois.replay.test.utils;

public class TimeGenerator {
	/**
	 * @return the recordNumber
	 */
	public int getRecordNumber() {
		return recordNumber;
	}

	/**
	 * @param recordNumber the recordNumber to set
	 */
	public void setRecordNumber(int recordNumber) {
		this.recordNumber = recordNumber;
	}

	/**
	 * @return the stepNumber
	 */
	public int[] getStepNumber() {
		return stepNumber;
	}

	/**
	 * @return the timeMultiplier
	 */
	public double getTimeMultiplier() {
		return timeMultiplier;
	}

	/**
	 * @return the startTime
	 */
	public double getStartTime() {
		return startTime;
	}

	private int[] stepNumber = new int[3];
	private final double timeMultiplier;
	private final double startTime;
	private int recordNumber = 0;
	public TimeGenerator(double timeMultiplier, double startTime) {
		stepNumber[0] = 1;
		stepNumber[1] = 0;
		stepNumber[2] = 1;
		this.timeMultiplier = timeMultiplier;
		this.startTime = startTime;
	}

	public void incrementStep() {
		if (recordNumber % 4 == 0) {
			stepNumber[0]++;
			stepNumber[1] = 0;
			stepNumber[2] = 0;
		}
		if (recordNumber % 3 == 0) {
			stepNumber[1]++;
			stepNumber[2] = 0;
		}
		stepNumber[2] += timeMultiplier * 10;
	}
	
	public void increment() {
		recordNumber++;
	}
	
	public double getTime() {
		return startTime + (recordNumber * timeMultiplier);	
	}

	public void reset() {
		recordNumber = 0;
	}
}
