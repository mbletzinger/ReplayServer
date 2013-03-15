package org.nees.illinois.replay.test.utils;

public class TimeGenerator {
	private int recordNumber = 0;

	private final int [] startStepNumber = { 1, 0, 1};

	private final double startTime;

	private int[] stepNumber = new int[3];

	private final double timeMultiplier;
	public TimeGenerator(double timeMultiplier, double startTime) {
		stepNumber = startStepNumber;
		this.timeMultiplier = timeMultiplier;
		this.startTime = startTime;
	}

	/**
	 * @return the recordNumber
	 */
	public int getRecordNumber() {
		return recordNumber;
	}

	/**
	 * @return the startStepNumber
	 */
	public int[] getStartStepNumber() {
		return startStepNumber;
	}
	/**
	 * @return the startTime
	 */
	public double getStartTime() {
		return startTime;
	}
	/**
	 * @return the stepNumber
	 */
	public int[] getStepNumber() {
		return stepNumber;
	}
	public double getTime() {
		return startTime + (recordNumber * timeMultiplier);	
	}
	/**
	 * @return the timeMultiplier
	 */
	public double getTimeMultiplier() {
		return timeMultiplier;
	}

	public void increment() {
		recordNumber++;
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
	
	public void reset() {
		recordNumber = 0;
	}

	/**
	 * @param recordNumber the recordNumber to set
	 */
	public void setRecordNumber(int recordNumber) {
		this.recordNumber = recordNumber;
	}
}
