package org.nees.illinois.replay.events;

/**
 * Interface to characterize an iteration step. This can either be a ramp for an
 * experimental test or an iteration for an simulation. The interface introduce
 * an iteration step index which can be used to sort events.
 * @author Michael Bletzinger
 */
public interface IterationStepI extends EventI {
	/**
	 * Step indexes are floating point to allow the time meshing between data
	 * streams from different sources.
	 * @return double value representing an iteration step.
	 */
	double getStepIndex();

}

