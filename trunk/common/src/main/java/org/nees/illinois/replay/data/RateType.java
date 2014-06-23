package org.nees.illinois.replay.data;

/**
 * enum type which selects either continuously sampled data or data that is
 * sampled at discrete events.
 * @author Michael Bletzinger
 */
public enum RateType {
	/**
	 * Data sampled continuously.
	 */
	CONTINUOUS,
	/**
	 * Data sampled at discrete events.
	 */
	DISCRETE
}
