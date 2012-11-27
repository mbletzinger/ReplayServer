package org.nees.mustsim.replay.restlet.client;

import org.nees.mustsim.replay.restlet.DataTableResource;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Post;
import org.restlet.resource.Put;

public class DataTableClientResource extends ClientResource implements
		DataTableResource {

	@Override
	@Put
	public void set(Representation channels) {
	}

	@Override
	@Post
	public void update(Representation data) {
		// TODO Auto-generated method stub

	}

}
