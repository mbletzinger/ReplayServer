package org.nees.illinois.replay.common.types;

/**
 * List of ways to define a time frame.
 * @author Michael Bletzinger
 */
public enum TimeBoundsType {
	/**
	 * Time frame defined by a discrete list of events.
	 */
	EventList,
	/**
	 * Time frame defined by start and stop events.
	 */
	StartStopEvent,
	/**
	 * Time frame defined by start and stop times.
	 */
	StartStopTime,
}
