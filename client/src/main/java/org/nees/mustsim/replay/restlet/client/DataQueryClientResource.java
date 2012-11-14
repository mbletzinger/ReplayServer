package org.nees.mustsim.replay.restlet.client;

import org.nees.mustsim.replay.restlet.DataQueryResource;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

public class DataQueryClientResource extends ClientResource implements
		DataQueryResource {

	@Override
	@Get
	public Representation get(String query, String rate, String start,
			String stop) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Put
	public void set(String query, String channels) {
		// TODO Auto-generated method stub

	}

	@Override
	@Delete
	public void removeList(String query) {
		// TODO Auto-generated method stub

	}

}
