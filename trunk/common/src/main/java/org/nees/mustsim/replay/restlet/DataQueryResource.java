package org.nees.mustsim.replay.restlet;

import org.restlet.representation.Representation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

public interface DataQueryResource {

	@Get("txt|bin")
	public Representation get(String query, String rate, String start, String stop);
	
	@Put
	public void set(String query, String channels);

	@Delete
	public void removeList(String query);
}