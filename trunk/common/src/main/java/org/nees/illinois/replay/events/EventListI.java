package org.nees.illinois.replay.events;

import java.util.List;

/**
 * Interface to a list of events which are synchronized to the same time source.
 * @author Michael Bletzinger
 */
public interface EventListI {
	/**
	 * @return List of events ordered by time.
	 */
	List<EventI> getEvents();

	/**
	 * @return list of times in seconds when the events occurred.
	 */
	List<Double> getTimeline();

	/**
	 * Find an event based on a name.
	 * @param name
	 *            of the event to find.
	 * @return an event based on the name.
	 */
	EventI find(String name);

	/**
	 * Add an event to the list.
	 * @param e
	 *            event to add.
	 */
	void addEvent(EventI e);

	/**
	 * Filter the list for events of the same type.
	 * @param type
	 *            Event type for filter.
	 * @return list of events.
	 */
	List<EventI> filterByType(EventType type);

	/**
	 * Filter the list for the same source.
	 * @param source
	 *            Source of the events.
	 * @return list of events.
	 */
	List<EventI> filterBySource(String source);
}
