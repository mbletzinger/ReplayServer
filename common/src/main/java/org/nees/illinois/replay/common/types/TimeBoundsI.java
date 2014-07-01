package org.nees.illinois.replay.common.types;

import java.util.List;

/**
 * * Wrapper for start and stop boundary values such as time, events, and row
 * numbers.
 * @author Michael Bletzinger
 */
public interface TimeBoundsI {

	/**
	 * A query using a lists of discrete names indicating times.
	 * @return a list of names.
	 */
	List<String> getNames();

	/**
	 * Timestamp in seconds from Jan 1 1901 that sets the start of
	 *            the query.
	 * @return the start.
	 */
	double getStart();

	/**
	 * @return the startName.
	 */
	String getStartName();

	/**
	 * Timestamp in seconds from Jan 1 1901 that sets the end of the
	 *            query.
	 * @return the stop.
	 */
	double getStop();

	/**
	 * @return the stopName.
	 */
	String getStopName();

	/**
	 * A query boundary based on a list of discrete times.
	 *@return the list of times.
	 */
	double []  getTimes();

	/**
	 * @return the way the time bounds are defined.
	 */
	TimeBoundsType getType();

}
