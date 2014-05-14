package org.nees.illinois.replay.events;

import java.util.List;

import org.nees.illinois.replay.common.types.TimeBoundsI;

/**
 * Interface to a list of events which are synchronized to the same time source.
 * @author Michael Bletzinger
 */
public interface EventListI {
	/**
	 * Add an event to the list.
	 * @param e
	 *            event to add.
	 */
	void addEvent(EventI e);

	/**
	 * Filter the list for the same source.
	 * @param source
	 *            Source of the events.
	 * @return list of events.
	 */
	List<EventI> filterBySource(String source);

	/**
	 * Find an event based on a timestamp.
	 * @param time
	 *            the timestamp.
	 * @return the event.
	 */
	EventI find(double time);

	/**
	 * Find an event based on a name.
	 * @param name
	 *            of the event to find.
	 * @return an event based on the name.
	 */
	EventI find(String name);

	/**
	 * Get an event at the list index.
	 * @param idx
	 *            the index.
	 * @return the event.
	 */
	EventI getEvent(int idx);

	/**
	 * @return List of events ordered by time.
	 */
	List<EventI> getEvents();

	/**
	 * @return list of times in seconds when the events occurred.
	 */
	List<Double> getTimeline();

	/**
	 * Find the sequence of events based on the boundaries.
	 * @param bounds
	 *            boundaries of the event sequence.
	 * @return The event sequence slice.
	 */
	List<EventI> slice(TimeBoundsI bounds);
}
