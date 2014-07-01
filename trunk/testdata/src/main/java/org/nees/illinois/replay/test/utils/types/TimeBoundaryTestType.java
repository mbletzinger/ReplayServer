package org.nees.illinois.replay.test.utils.types;

/**
 * Query test types.
 * @author Michael Bletzinger
 */
public enum TimeBoundaryTestType {
	/**
	 * Include a slice of continuous data from start to stop.
	 */
	ContWithStartStop,
	/**
	 * Include a single dataset based on a time stamp.
	 */
	ContWithTime,
	/**
	 * Continuous with a start to the end of the dataset.
	 */
	ContWithStart2End,
	/**
	 * Include a single data set based on the event.
	 */
	Event,
	/**
	 * Include all event data between two events.
	 */
	EventsWithStartStop,
	/**
	 * Include all data between two events.
	 */
	ContWithEventStartStop;
}
