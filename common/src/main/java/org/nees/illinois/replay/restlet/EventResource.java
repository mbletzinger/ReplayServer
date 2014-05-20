package org.nees.illinois.replay.restlet;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;

/**
 * Interface for the restlet resource used for data updates. The interface
 * expects a event definition with a representation that contains the
 * description and a timestamp.
 * @author Michael Bletzinger
 */
public interface EventResource {
	/**
	 * Get a list of events that fall within the specified time frame.
	 * @param start
	 *            of the time frame.
	 * @param stop
	 *            of the time frame.
	 * @return {@link Representation} containing a list of events.
	 */
	@Get
	Representation getEvents(double start, double stop);

	/**
	 * Get a list of events that fall within the specified time frame.
	 * @param start
	 *            event name for the time frame.
	 * @param stop
	 *            event for the time frame.
	 * @return {@link Representation} containing a list of events.
	 */
	@Get
	Representation getEvents(String start, String stop);

	/**
	 * Define a table to be updated.
	 * @param event
	 *            Event description.
	 * @throws ResourceException
	 */
	@Post
	void update(Representation event);
}
