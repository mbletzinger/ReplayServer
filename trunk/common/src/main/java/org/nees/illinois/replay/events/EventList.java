package org.nees.illinois.replay.events;

import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.common.types.TableIdentityI;

/**
 * Class which maintains a timeline of events from a source.
 * @author Michael Bletzinger
 */
public class EventList implements EventListI {
	/**
	 * @param tableId
	 *            Source of the events.
	 */
	public EventList(final TableIdentityI tableId) {
		this.tableId = tableId;
	}

	/**
	 * @param tableId
	 *            Source of the events.
	 * @param events
	 *            list.
	 */
	public EventList(final TableIdentityI tableId, final List<EventI> events) {
		this.tableId = tableId;
		this.events.addAll(events);
	}

	/**
	 * Source of the events.
	 */
	private final TableIdentityI tableId;
	/**
	 * Event list.
	 */
	private final List<EventI> events = new ArrayList<EventI>();

	@Override
	public final List<EventI> getEvents() {
		return events;
	}

	@Override
	public final TableIdentityI getSource() {
		return tableId;
	}

	@Override
	public final List<Double> getTimeline() {
		List<Double> result = new ArrayList<Double>();
		for (EventI e : events) {
			result.add(e.getTime());
		}
		return result;
	}

	@Override
	public final EventI find(final String name) {
		for (EventI e : events) {
			if (e.getName().equals(name)) {
				return e;
			}
		}
		return null;
	}

	@Override
	public final void addEvent(final EventI e) {
		events.add(e);
	}

}
