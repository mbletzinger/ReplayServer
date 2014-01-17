package org.nees.illinois.replay.common.types;
/**
 * * Wrapper for start and stop boundary values such as time, events, and row
 * numbers.
 * @author Michael Bletzinger
 *
 */
public interface TimeBoundsI {

	/**
	 * @return the start
	 */
	double getStart();

	/**
	 * @return the startName
	 */
	String getStartName();

	/**
	 * @return the stop
	 */
	double getStop();

	/**
	 * @return the stopName
	 */
	String getStopName();

}
