package org.nees.illinois.replay.data;

/**
 * enum type which selects either continuously sampled data or data that is
 * sampled per iteration step.
 * 
 * @author Michael Bletzinger
 * 
 */
public enum RateType {
	/**
	 * Data sampled continuously.
	 */
	CONT,
	/**
	 * Data sampled per iteration step.
	 */
	STEP
}
