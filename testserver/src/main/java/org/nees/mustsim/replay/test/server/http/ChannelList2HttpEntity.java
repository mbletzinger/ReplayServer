package org.nees.mustsim.replay.test.server.http;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.nees.illinois.replay.conversions.ChannelList2OutputStream;

public class ChannelList2HttpEntity {
	private final ChannelList2OutputStream cl2os;

//	private final Logger log = LoggerFactory
//			.getLogger(ChannelList2Representation.class);
	private final HttpEntity ent;

	public ChannelList2HttpEntity(List<String> channels) {
		super();
		cl2os = new ChannelList2OutputStream(channels);
		this.ent = new ByteArrayEntity(cl2os.getBuffer());
	}

	/**
	 * @return the cl2os
	 */
	public ChannelList2OutputStream getCl2os() {
		return cl2os;
	}

	/**
	 * @return the representation
	 */
	public HttpEntity getEnt() {
		return ent;
	}
}
