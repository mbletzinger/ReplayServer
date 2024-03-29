package org.nees.illinois.replay.events;

import org.nees.illinois.replay.common.types.EqualsWithNulls;

/**
 * Class which contains a defined event.
 * @author Michael Bletzinger
 */
public class Event implements EventI {
	/**
	 * Optional description of the event.
	 */
	private final String description;

	/**
	 * Name of the event.
	 */
	private final String name;

	/**
	 * Source that originated the event.
	 */
	private final String source;
	/**
	 * time of the event in seconds after 1900.
	 */
	private final double time;

	/**
	 * @param name
	 *            Name of the event.
	 * @param time
	 *            time of the event in seconds after 1900.
	 * @param description
	 *            Optional description of the event.
	 * @param source
	 *            Source that originated the event.
	 */
	public Event(final String name, final double time,
			final String description, final String source) {
		this.name = name;
		this.time = time;
		this.description = description;
		this.source = source;
	}

	@Override
	public final int compareTo(final EventI arg0) {
		return name.compareTo(arg0.getName());
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(final Object obj) {
		EqualsWithNulls<String> eq = new EqualsWithNulls<String>();
		if (obj instanceof Event == false) {
			return false;
		}

		Event it = (Event) obj;
		if (name.equals(it.name) == false) {
			return false;
		}
		if (Double.compare(time, it.time) != 0) {
			return false;
		}
		if (eq.equate(description, it.description) == false) {
			return false;
		}
		return eq.equate(source, it.source);
	}

	@Override
	public final String getDescription() {
		return description;
	}

	@Override
	public final String getName() {
		return name;
	}

	@Override
	public final String getSource() {
		return source;
	}

	@Override
	public final double getTime() {
		return time;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		return super.hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		return "Event [time=" + time + ", name=" + name
				+ ", description=\"" + description + "\", source=" + source  + "]";
	}
}
