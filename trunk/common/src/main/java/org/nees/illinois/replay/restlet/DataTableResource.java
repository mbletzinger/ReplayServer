package org.nees.illinois.replay.restlet;

import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;

public interface DataTableResource {
	@Put
	public void set(Representation channels) throws ResourceException;

	@Post
	public void update(Representation data) throws ResourceException;
}
