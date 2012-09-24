package org.nees.mustsim.replay.restlet;

import java.io.InputStream;
import java.io.OutputStream;

import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;


public class DataServerResource extends ServerResource implements DataResource {

	@Override
	@Get
	public OutputStream getData(String channels, String rate, String start,
			String stop) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Post
	public void addData(String table, InputStream data) {
		// TODO Auto-generated method stub
		
	}
	
	

}
