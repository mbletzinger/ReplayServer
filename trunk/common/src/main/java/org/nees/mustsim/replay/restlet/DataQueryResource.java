package org.nees.mustsim.replay.restlet;

import java.io.InputStream;
import java.io.OutputStream;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

public interface DataQueryResource {

	@Get
	public OutputStream get(String query, String start, String stop);
	
	@Put
	public void set(String query, InputStream channels);

	@Delete
	public void removeList(String query);
}
