package org.nees.illinois.replay.subresource;

import java.util.List;

import org.nees.illinois.replay.common.types.TableIdentityI;
import org.nees.illinois.replay.events.EventI;
import org.nees.illinois.replay.events.EventListI;

/**
 * Interface to the event subresource to manage time events.
 * @author Michael Bletzinger
 */
public interface EventSubResourceI extends SubResourceI {
	/**
	 * Create a new event.
	 * @param type
	 *            event type
	 * @param name
	 *            Common name of the event.
	 * @param description
	 *            Text describing the event.
	 * @param time
	 *            Timestamp for the event in seconds
	 * @param source
	 *            Source that generated the event.
	 * @return The newly created event.
	 */
	EventI createEvent(String type, String name, String description,
			double time, TableIdentityI source);

	/**
	 * Return a list of event objects based on the common names.
	 * @param names
	 *            List of common names.
	 * @return The stored event objects..
	 */
	EventListI getEvents(List<String> names);

	/**
	 * Return a list of event objects based on the common names.
	 * @param start
	 *            First event.
	 * @param stop
	 *            Last event.
	 * @return List of events within the timeframe.
	 */
	EventListI getEvents(String start, String stop);
}
