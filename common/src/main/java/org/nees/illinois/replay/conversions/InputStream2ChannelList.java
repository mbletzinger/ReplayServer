package org.nees.illinois.replay.conversions;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Converts an input stream into a string array of channel names.
 * @author Michael Bletzinger
 */
public class InputStream2ChannelList {
	/**
	 * Resulting array of channel names.
	 */
	private List<String> channels = new ArrayList<String>();

	// private final Logger log = LoggerFactory
	// .getLogger(InputStream2ChannelList.class);
	/**
	 * Constructor.
	 * @param in
	 *            Input stream to be converted.
	 */
	public InputStream2ChannelList(final InputStream in) {
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
	 * @return the channel name list.
	 */
	public final List<String> getChannels() {
		return channels;
	}

	// /**
	// * @param channels
	// * the channels to set
	// */
	// public void setChannels(List<String> channels) {
	// this.channels = channels;
	// }
}
