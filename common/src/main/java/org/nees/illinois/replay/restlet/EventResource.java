package org.nees.illinois.replay.restlet;

import org.restlet.representation.Representation;
import org.restlet.resource.Put;
/**
 * Interface for the restlet resource used for data updates. The interface
 * expects a event definition with a representation that contains the description and a timestamp.
 * @author Michael Bletzinger
 */
public interface EventResource {
	/**
	 * Define a table to be updated.
	 * @param event
	 *            Event description.
	 * @throws ResourceException
	 */
	@Put void set(Representation event);
}
