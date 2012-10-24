package org.nees.mustsim.replay.restlet.server;

import java.io.InputStream;

import org.nees.mustsim.replay.restlet.DataTableResource;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

public class DataTableServerResource extends ServerResource implements
		DataTableResource {

	@Override
	@Put
	public void set(String table, InputStream channels) {
		// TODO Auto-generated method stub

	}

	@Override
	@Post
	public void update(String table, InputStream data) {
		// TODO Auto-generated method stub

	}

}
