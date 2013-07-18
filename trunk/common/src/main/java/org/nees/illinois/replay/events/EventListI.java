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
}
