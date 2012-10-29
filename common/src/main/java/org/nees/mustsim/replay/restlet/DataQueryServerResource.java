package org.nees.mustsim.replay.restlet;

import java.io.InputStream;

import org.restlet.representation.Representation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

public class DataQueryServerResource extends ServerResource implements
		DataQueryResource {

	@Override
	@Get
	public Representation get(String query, String rate, String start, String stop) {
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
