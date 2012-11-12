package org.nees.mustsim.replay.restlet.client;

import java.io.InputStream;

import org.nees.mustsim.replay.conversions.InputStream2ChannelList;
import org.nees.mustsim.replay.restlet.DataTableResource;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Post;
import org.restlet.resource.Put;

public class DataTableClientResource extends ClientResource implements
		DataTableResource {

	@Override
	@Put
	public void set(String table, InputStream channels) {
		InputStream2ChannelList is2cl = new InputStream2ChannelList(channels);
	}

	@Override
	@Post
	public void update(String table, String rate, InputStream data) {
		// TODO Auto-generated method stub

	}

}
