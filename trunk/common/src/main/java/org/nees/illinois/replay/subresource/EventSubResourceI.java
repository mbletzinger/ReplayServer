package org.nees.illinois.replay.subresource;

import org.nees.illinois.replay.common.types.TableIdentityI;
import org.nees.illinois.replay.events.EventI;
import org.nees.illinois.replay.events.EventType;

/**
 * Interface to the event subresource to manage time events.
 * @author Michael Bletzinger
 *
 */
public interface EventSubResourceI extends SubResourceI {
	/**
	 * Create a new event.
	 *@param type
	 *event type
	 *@param name
	 *Common name of the event.
	 *@param description
	 *Text describing the event.
	 *@param time
	 *Timestamp for the event in seconds
	 *@param source
	 *Source that generated the event.
	 *@return
	 *The newly created event.
	 */
	EventI createEvent(EventType type, String name, String description, double time, TableIdentityI source);
	/**
	 * Return an event object base on the common name.
	 *@param name
	 *The common name.
	 *@return
	 *The stored event.
	 */
	EventI getEvent(String name);
}
