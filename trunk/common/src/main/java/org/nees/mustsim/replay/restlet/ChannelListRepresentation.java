package org.nees.mustsim.replay.restlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.nees.mustsim.replay.conversions.ChannelList2OutputStream;
import org.restlet.data.MediaType;
import org.restlet.representation.OutputRepresentation;

public class ChannelListRepresentation extends OutputRepresentation {

	private final List<String> channels;

	public ChannelListRepresentation(long expectedSize, List<String> channels) {
		super(MediaType.APPLICATION_JAVA, expectedSize);
		this.channels = channels;
	}

	@Override
	public void write(OutputStream os) throws IOException {
		ChannelList2OutputStream chnl2os = new ChannelList2OutputStream(os);
		chnl2os.writeChannels(channels);
	}

}
