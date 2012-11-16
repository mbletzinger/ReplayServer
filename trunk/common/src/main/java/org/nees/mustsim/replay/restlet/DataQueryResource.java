package org.nees.mustsim.replay.restlet;

import org.restlet.representation.Representation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

public interface DataQueryResource {

	@Get("txt|bin")
	public Representation get();
	
	@Put
	public void set(Representation channels);

	@Delete
	public void removeList(String query);
}
