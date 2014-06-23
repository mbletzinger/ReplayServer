package org.nees.illinois.replay.events;

import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.common.types.TimeBoundsI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	 * Logger.
	 **/
	private final Logger log = LoggerFactory.getLogger(EventList.class);

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
	public final EventI find(final double time) {
		for (EventI e : events) {
			if (e.getTime() == time) {
				return e;
			}
		}
		return null;
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
	public final EventI getEvent(final int idx) {
		return events.get(idx);
	}

	@Override
	public final List<String> getEventNames() {
		List<String> result = new ArrayList<String>();
		for (EventI e : events) {
			if(result.contains(e.getName())) {
				continue;
			}
			result.add(e.getName());
		}
		return result;
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

	/**
	 * @param bounds
	 *            requested time boundaries.
	 * @return list containing all of the events between the boundaries.
	 */
	private List<EventI> names2Events(final TimeBoundsI bounds) {
		List<EventI> result = new ArrayList<EventI>();
		for (String n : bounds.getNames()) {
			EventI e = find(n);
			if (e == null) {
				continue;
			}
			result.add(e);
		}
		return (result.isEmpty() ? null : result);
	}

	@Override
	public final List<EventI> slice(final TimeBoundsI bounds) {
		switch (bounds.getType()) {
		case StartStopEvent:
			return sliceStartAndStop(bounds);
		case EventList:
			return names2Events(bounds);
		case StartStopTime:
			log.error("Not an event based boundary type");
			return null;
		default:
			log.error(bounds.getType() + " not recognized");
			return null;
		}
	}

	/**
	 * @param bounds
	 *            requested time boundaries.
	 * @return list containing the start and stop events.
	 */
	private List<EventI> sliceStartAndStop(final TimeBoundsI bounds) {
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

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		String result = "[\n";
		for (EventI e : events) {
			result += "\t" + e + "\n";
		}
		result += "]";
		return result;
	}
}
