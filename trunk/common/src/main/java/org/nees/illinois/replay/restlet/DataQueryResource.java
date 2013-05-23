package org.nees.illinois.replay.restlet;

import org.restlet.representation.Representation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

/**
 * Interface for the restlet resource used for query requests. The interface
 * assumes that a query is set once at the beginning of a session and then used
 * repeatedly with different rates, start times, and stop times.
 * @author Michael Bletzinger
 */
public interface DataQueryResource {

	// @Get("txt")
	// public Representation getText() throws ResourceException;

	/**
	 * Returns binary data from a query request.
	 * @return Double matrix representation of the data.
	 */
	@Get("bin")
	Representation getBin();

	/**
	 * Establishes a query for the session. A query can only be used within one
	 * experiment.
	 * @param channels
	 *            String array representation of the channel names that comprise
	 *            the query.
	 * @throws ResourceException
	 */
	@Put
	void set(Representation channels);

	/**
	 * Removes a query for the session.
	 * @param query
	 *            Name of the query.
	 */
	@Delete
	void removeList(String query);
}
