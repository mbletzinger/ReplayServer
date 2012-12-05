package org.nees.mustsim.replay.conversions;

import java.io.IOException;

import org.restlet.representation.Representation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Representation2ChannelList {
	private final InputStream2ChannelList il2cl;

	private final Logger log = LoggerFactory
			.getLogger(Representation2ChannelList.class);

	public Representation2ChannelList(Representation rep) {
		super();
		InputStream2ChannelList i2c = null;
		try {
			i2c = new InputStream2ChannelList(rep.getStream());
		} catch (IOException e1) {
			try {
				log.error("Could not read representation \"" + rep.getText()
						+ "\"");
			} catch (IOException e) {
				log.error("Could not read representation \"" + rep + "\"");
			}
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
