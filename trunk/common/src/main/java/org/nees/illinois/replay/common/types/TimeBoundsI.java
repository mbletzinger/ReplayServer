package org.nees.illinois.replay.common.types;

public interface TimeBoundsI {

	/**
	 * @return the start
	 */
	abstract double getStart();

	/**
	 * @return the startName
	 */
	abstract String getStartName();

	/**
	 * @return the stop
	 */
	abstract double getStop();

	/**
	 * @return the stopName
	 */
	abstract String getStopName();

}