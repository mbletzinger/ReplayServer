package org.nees.mustsim.replay.restlet;

import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.Put;

public interface DataTableResource {
	@Put
	public void set(Representation channels);

	@Post
	public void update(Representation data);
}
