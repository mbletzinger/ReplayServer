package org.nees.illinois.replay.common.registries;

import java.util.HashMap;
import java.util.Map;

import org.nees.illinois.replay.events.EventType;

/**
 * Generate event id's using integers.
 * @author Michael Bletzinger
 */
public class EventIdGenerator {
	/**
	 * Uniquifier indexes for the event ids.
	 */
	private final Map<EventType, Integer> indexes = new HashMap<EventType, Integer>();

	/**
	 * Create a unique event id based on the type.
	 * @param e
	 *            Type of event.
	 * @return ID.
	 */
	public final String create(final EventType e) {
		int c = indexes.get(e);
		String result = e.name() + "_" + c;
		c++;
		indexes.put(e, new Integer(c));
		return result;
	}

	/**
	 * Default constructor.
	 */
	public EventIdGenerator() {
		for (EventType e : EventType.values()) {
			indexes.put(e, new Integer(0));
		}
	}

	/**
	 * @param values
	 *            Preliminary indexes.
	 */
	public EventIdGenerator(final Map<EventType, Integer> values) {
		indexes.putAll(values);
	}

}
