package org.nees.mustsim.replay.conversions;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.restlet.representation.Representation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Representation2ChannelList {
	private List<String> channels = new ArrayList<String>();
	private final Logger log = LoggerFactory
			.getLogger(Representation2ChannelList.class);

	public Representation2ChannelList(Representation rep) {
		super();
		DataInputStream din = null;
		try {
			din = new DataInputStream(rep.getStream());
		} catch (IOException e1) {
			try {
				log.error("Could not read representation \"" + rep.getText() + "\"");
			} catch (IOException e) {
				log.error("Could not read representation \"" + rep + "\"");
			}
			return;
		}
		StringBuffer theString = new StringBuffer();
		boolean done = false;
		while(! done) {
			char c;
			try {
				c = din.readChar();
			} catch (IOException e) {
				done = true;
				break;
			}
			theString.append(c);
		}
		channels = Str2CL.str2cl(theString.toString());

	}

	/**
	 * @return the channels
	 */
	public List<String> getChannels() {
		return channels;
	}

	/**
	 * @param channels
	 *            the channels to set
	 */
	public void setChannels(List<String> channels) {
		this.channels = channels;
	}
}
