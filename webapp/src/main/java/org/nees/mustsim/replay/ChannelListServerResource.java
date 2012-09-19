package org.nees.mustsim.replay;

import java.io.InputStream;
import java.io.OutputStream;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class ChannelListServerResource extends ServerResource implements ChannelListResource {

	@Override
	@Get
	public OutputStream getList(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Post
	public void setList(String name, InputStream list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Delete
	public void removeList(String name) {
		// TODO Auto-generated method stub
		
	}

}
