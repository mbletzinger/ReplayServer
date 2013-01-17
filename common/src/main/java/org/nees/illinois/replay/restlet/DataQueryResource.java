package org.nees.illinois.replay.restlet;

import org.restlet.representation.Representation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;

public interface DataQueryResource {

	@Get("txt")
	public Representation getText() throws ResourceException;
	
	@Get("bin")
	public Representation getBin() throws ResourceException;
	@Put
	public void set(Representation channels) throws ResourceException;

	@Delete
	public void removeList(String query) throws ResourceException;
}
