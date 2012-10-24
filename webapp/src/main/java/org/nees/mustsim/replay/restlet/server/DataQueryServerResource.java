package org.nees.mustsim.replay.restlet.server;

import java.io.InputStream;
import java.io.OutputStream;

import org.nees.mustsim.replay.restlet.DataQueryResource;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

public class DataQueryServerResource extends ServerResource implements
		DataQueryResource {

	@Override
	@Get
	public OutputStream get(String query, String start, String stop) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Put
	public void set(String query, InputStream channels) {
		// TODO Auto-generated method stub

	}

	@Override
	@Delete
	public void removeList(String query) {
		// TODO Auto-generated method stub

	}

}
