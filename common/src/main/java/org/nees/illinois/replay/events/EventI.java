package org.nees.illinois.replay.events;


/**
 * Interface for characterizing a generic event. The time of the event is based
 * on local system time which is not assumed to be synchronized. This allows for
 * events from independent data streams to be related.
 * @author Michael Bletzinger
 */
public interface EventI extends Comparable<EventI>{
	/**
	 * @return Optional description.
	 */
	String getDescription();

	/**
	 * @return User friendly name of the event.
	 */
	String getName();

	/**
	 * @return Get the source which recorded the event.
	 */
	String getSource();

	/**
	 * The time stamp is local unsynchronized computer time.
	 * @return The timestamp of the event in seconds.
	 */
	double getTime();

}
