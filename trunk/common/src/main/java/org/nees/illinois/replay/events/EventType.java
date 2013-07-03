package org.nees.illinois.replay.events;

/**
 * Enumerate the different types of events.
 * @author Michael Bletzinger
 */
public enum EventType {
	/**
	 * One time event added by a user or a post process algorithm.
	 */
	Defined,
	/**
	 * Iteration step for a simulation.
	 */
	Iteration,
	/**
	 * Step number identifying the time event for event-driven experiments.
	 */
	StepNumber,
}
