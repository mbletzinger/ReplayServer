package org.nees.mustsim.replay.restlet;

import java.io.InputStream;
import java.io.OutputStream;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

public interface ChannelListResource {
	@Get
	public  OutputStream getList(String name);
	@Post
	public void setList(String name, InputStream list);
	@Delete
	public void removeList(String name);

}
