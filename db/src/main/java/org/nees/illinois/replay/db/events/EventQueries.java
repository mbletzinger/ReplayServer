package org.nees.illinois.replay.db.events;

import java.sql.Connection;
import java.util.List;

import org.nees.illinois.replay.events.EventI;

/**
 * Class that executes queries against the event table.
 * @author Michael Bletzinger
 *
 */
public class EventQueries {
	/**
	 * Database connection.
	 */
	private final Connection connection;
	/**
	 * Get a list of events.
	 *@param names
	 *event names.
	 *@param source
	 *Source of the events.
	 *@return list of events.
	 */
	public List<EventI> getEvents(List<String> names, String source) {
	}
	public List<EventI> getEvents(final String start, final String stop, final String source) {		
	}
}
