package org.nees.illinois.replay.test.server.utils;

import java.util.List;

import org.nees.illinois.replay.conversions.ChannelList2Representation;
import org.nees.illinois.replay.data.TableType;
import org.nees.illinois.replay.registries.ChannelNameRegistry;
import org.nees.illinois.replay.test.utils.ChannelLists;
import org.nees.illinois.replay.test.utils.ChannelLists.ChannelListType;
import org.restlet.representation.Representation;

public abstract class RequestBuilder {

	private final ChannelListType chanType;
	private final ChannelLists cl = new ChannelLists();
	private final ChannelNameRegistry expectedCnr = new ChannelNameRegistry();
	public RequestBuilder(ChannelListType chanType) {
		super();
		this.chanType = chanType;
	}
	protected Representation getChannelRep() {
		List<String> channels = cl.getChannels(chanType);
		ChannelList2Representation cl2rep = new ChannelList2Representation(
				channels);
		for (String c : channels) {
			expectedCnr.addChannel(cl.getTt(chanType), c);
		}
		return cl2rep.getRep();
	}
}
