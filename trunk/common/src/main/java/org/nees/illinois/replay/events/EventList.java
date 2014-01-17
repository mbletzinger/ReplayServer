package org.nees.illinois.replay.events;

import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.common.types.TimeBoundsI;

/**
 * Class which maintains a timeline of events from a source.
 * @author Michael Bletzinger
 */
public class EventList implements EventListI {
	/**
	 * Event list.
	 */
	private final List<EventI> events = new ArrayList<EventI>();

	/**
	 * Create empty list.
	 */
	public EventList() {
	}

	/**
	 * @param events
	 *            list.
	 */
	public EventList(final List<EventI> events) {
		this.events.addAll(events);
	}

	@Override
	public final void addEvent(final EventI e) {
		events.add(e);
	}

	@Override
	public final List<EventI> filterBySource(final String source) {
		List<EventI> result = new ArrayList<EventI>();
		for (EventI e : events) {
			if (source.equals(e.getSource())) {
				result.add(e);
			}
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
	public final List<EventI> getEvents() {
		return events;
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
	public final List<EventI> slice(final TimeBoundsI bounds) {
		List<EventI> result = new ArrayList<EventI>();
		for (EventI e : events) {
			if (result.isEmpty()) {
				if (e.getName().equals(bounds.getStartName())) {
					result.add(e);
				}
				continue;
			}
			result.add(e);
			if (e.getName().equals(bounds.getStopName())) {
				return result;
			}
		}
		return null;
	}
}
