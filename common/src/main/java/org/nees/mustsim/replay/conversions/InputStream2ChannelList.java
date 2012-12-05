package org.nees.mustsim.replay.conversions;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class InputStream2ChannelList {
	private List<String> channels = new ArrayList<String>();
//	private final Logger log = LoggerFactory
//			.getLogger(InputStream2ChannelList.class);

	public InputStream2ChannelList(InputStream in) {
		super();
		DataInputStream din = null;
		din = new DataInputStream(in);
		StringBuffer theString = new StringBuffer();
		boolean done = false;
		while (!done) {
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
