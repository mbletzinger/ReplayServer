package org.nees.illinois.replay.events;

import org.nees.illinois.replay.common.types.TableIdentityI;

/**
 * Interface for characterizing a generic event. The time of the event is based
 * on local system time which is not assumed to be synchronized. This allows for
 * events from independent data streams to be related.
 * @author Michael Bletzinger
 */
public interface EventI extends Comparable<EventI>{
	/**
	 * @return User friendly name of the event.
	 */
	String getName();

	/**
	 * @return Unique string identifying the event.
	 */
	String getid();

	/**
	 * @return Optional description.
	 */
	String getDescription();

	/**
	 * The time stamp is local unsynchronized computer time.
	 * @return The timestamp of the event in seconds.
	 */
	double getTime();

	/**
	 * @return Return the event type.
	 */
	EventType getType();

	/**
	 * @return Get the source which recorded the event.
	 */
	TableIdentityI getSource();

}
