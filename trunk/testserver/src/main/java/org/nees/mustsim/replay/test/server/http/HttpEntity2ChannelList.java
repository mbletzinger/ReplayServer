package org.nees.mustsim.replay.test.server.http;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.nees.illinois.replay.conversions.InputStream2ChannelList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpEntity2ChannelList {
	private final InputStream2ChannelList il2cl;

	private final Logger log = LoggerFactory
			.getLogger(HttpEntity2ChannelList.class);

	public HttpEntity2ChannelList(HttpEntity ent) {
		super();
		InputStream2ChannelList i2c = null;
		try {
			i2c = new InputStream2ChannelList(ent.getContent());
		} catch (IOException e1) {
				log.error("Could not read representation \"" + ent.toString()
						+ "\"");
			il2cl = null;
			return;
		}
		il2cl = i2c;
	}

	/**
	 * @return the il2cl
	 */
	public InputStream2ChannelList getIl2cl() {
		return il2cl;
	}
}
