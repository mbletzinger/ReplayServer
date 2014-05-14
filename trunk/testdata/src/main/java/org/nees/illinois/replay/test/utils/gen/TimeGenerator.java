package org.nees.illinois.replay.test.utils.gen;

/**
 * Class to generate timestamps and step numbers based on the start time and
 * intervals. <br>Be aware that unlike other classes, this class's internal state is
 * magically modified and can lead to unexpected results if not used carefully.
 * The general solution to this is to use the cloning constructor for this class
 * to reuse the generator in its original state. There are two places where the
 * magic happens:
 * <dl>
 * <dt>Internal Record Number
 * <dd>The object keeps a count of the number of times that have been generated.
 * Use the reset() function to clear this count.
 * <dt>Gap Parameters
 * <dd>The DerivedDataSetsGenerator will add gap parameters to the original time
 * generator to accommodate the derived generator.
 * </dl>
 * @author Michael Bletzinger
 */
public class TimeGenerator {
	/**
	 * Size of the time gap in seconds.
	 */
	private double gapSize = 0.0;
	/**
	 * Start time in seconds for a gap in the time sequence.
	 */
	private double gapStartTime = 0.0;
	/**
	 * True if a gap exists.
	 */
	private boolean hasGap = false;
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
	public TimeGenerator(final double timeMultiplier,final double startTime) {
		this.timeMultiplier = timeMultiplier;
		this.startTime = startTime;
	}

	/**
	 * Clone a copy of a time generator.
	 * @param other
	 *            the original generator/
	 */
	public TimeGenerator(final TimeGenerator other) {
		this.timeMultiplier = other.timeMultiplier;
		this.startTime = other.startTime;
	}

	/**
	 * Check if the time has fallen within a specified gap. If so adjust the
	 * recordNumber to jump the gap.
	 */
	private void checkForGap() {
		if (hasGap == false) {
			return;
		}
		double ctime = getTime();
		boolean started = (ctime - startTime) >= gapStartTime;
		boolean ended = (ctime - startTime) >= (gapStartTime + gapSize);
		if (started == false) {
			return;
		}
		if (ended) {
			return;
		}
		recordNumber = (int) Math.round((gapStartTime + gapSize)
				/ timeMultiplier);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(final Object orig) {
		TimeGenerator obj = (TimeGenerator) orig;
		boolean cmp = startTime == obj.startTime;
		if (cmp == false) {
			return cmp;
		}

		cmp = timeMultiplier == obj.timeMultiplier;
		if (cmp == false) {
			return cmp;
		}

		if (hasGap != obj.hasGap) {
			return false;
		}
		cmp = gapSize == obj.gapSize;
		if (cmp == false) {
			return cmp;
		}
		return gapStartTime == gapStartTime;
	}

	/**
	 * @return the gapSize
	 */
	public final double getGapSize() {
		return gapSize;
	}

	/**
	 * @return the gapStartTime
	 */
	public final double getGapStartTime() {
		return gapStartTime;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		return super.hashCode();
	}

	/**
	 * Next record.
	 */
	public final void increment() {
		recordNumber++;
		checkForGap();
	}

	/**
	 * @return the hasGap
	 */
	public final boolean isHasGap() {
		return hasGap;
	}

	/**
	 * Reset record counter.
	 */
	public final void reset() {
		recordNumber = 0;
	}

	/**
	 * @param gapSize
	 *            the gapSize to set
	 */
	public final void setGapSize(final double gapSize) {
		this.gapSize = gapSize;
		setHasGap(gapSize != 0.0);
	}

	/**
	 * @param gapStartTime
	 *            the gapStartTime to set
	 */
	public final void setGapStartTime(final double gapStartTime) {
		this.gapStartTime = gapStartTime;
		setHasGap(gapStartTime != 0.0);
	}

	/**
	 * @param hasGap
	 *            the hasGap to set
	 */
	public final void setHasGap(final boolean hasGap) {
		this.hasGap = hasGap;
	}

	/**
	 * @param recordNumber
	 *            the recordNumber to set
	 */
	public final void setRecordNumber(final int recordNumber) {
		this.recordNumber = recordNumber;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		String result = "TimeGenerator [ startTime=" + startTime
				+ ", timeMultiplier=" + timeMultiplier;
		if (hasGap == false) {
			result += "]";
			return result;
		}

		result += ", gapSize=" + gapSize + ", gapStartTime=" + gapStartTime
				+ "]";
		return result;
	}
}
