package org.nees.illinois.replay.restlet;

import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.Put;

/**
 * Interface for the restlet resource used for data updates. The interface
 * expects a table definition set at the start of a session with a name, a
 * {@link TableType type}, a {@link RateType rate}, and a list of channels.
 * @author Michael Bletzinger
 */
public interface DataTableResource {
	/**
	 * Define a table to be updated.
	 * @param channels
	 *            List of channel names
	 * @throws ResourceException
	 */
	@Put void set(Representation channels);

	/**
	 * Add more data to the table.
	 * @param data
	 * Data to add.
	 */
	@Post
	void update(Representation data);
}
