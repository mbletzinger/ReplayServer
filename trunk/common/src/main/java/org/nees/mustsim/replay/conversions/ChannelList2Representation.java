package org.nees.mustsim.replay.conversions;

import java.util.List;

import org.restlet.representation.ByteArrayRepresentation;
import org.restlet.representation.Representation;

public class ChannelList2Representation {
	private final ChannelList2OutputStream cl2os;

//	private final Logger log = LoggerFactory
//			.getLogger(ChannelList2Representation.class);
	private final Representation rep;

	public ChannelList2Representation(List<String> channels) {
		super();
		cl2os = new ChannelList2OutputStream(channels);
		this.rep = new ByteArrayRepresentation(cl2os.getBuffer());
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
	public Representation getRep() {
		return rep;
	}
}
