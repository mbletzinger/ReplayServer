package org.nees.illinois.replay.events;

import java.util.List;

import org.nees.illinois.replay.common.types.TableIdentityI;

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
	 * @return the time source.
	 */
	TableIdentityI getSource();

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
}
