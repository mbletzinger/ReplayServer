package org.nees.mustsim.replay.restlet;

import java.io.InputStream;

import org.restlet.resource.Post;
import org.restlet.resource.Put;

public interface DataTableResource {
	@Put
	public void set(String table, InputStream channels);

	@Post
	public void update(String table, String rate, InputStream data);
}
