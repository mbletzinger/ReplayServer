package org.nees.mustsim.replay;

import java.io.InputStream;
import java.io.OutputStream;

import org.restlet.resource.Get;
import org.restlet.resource.Post;

public interface DataResource {

	@Get
	public OutputStream getData(String channels, String rate, String start, String stop);
	
	@Post
	public void addData(String table, InputStream data);
}
