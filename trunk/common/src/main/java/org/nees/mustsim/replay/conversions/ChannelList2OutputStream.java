package org.nees.mustsim.replay.conversions;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChannelList2OutputStream {
	private final byte[] buffer;
	private final Logger log = LoggerFactory
			.getLogger(ChannelList2OutputStream.class);
	private final OutputStream out;

	public ChannelList2OutputStream(OutputStream os) {
		super();
		buffer = null;
		out = os;
	}

	public ChannelList2OutputStream(List<String> channels) {
		super();
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		out = bout;
		writeChannels(channels);
		buffer = bout.toByteArray();
	}

	/**
	 * @return the buffer
	 */
	public byte[] getBuffer() {
		return buffer;
	}

	/**
	 * @return the out
	 */
	public OutputStream getOut() {
		return out;
	}

	public void writeChannels(List<String> channels) {
		DataOutputStream dout = new DataOutputStream(out);
		String str = "";
		boolean first = true;
		for (String s : channels) {
			str += (first ? "" : ",") + s;
			first = false;
		}
		try {
			dout.writeChars(str);
		} catch (IOException e) {
			log.error("Could not write the string [" + str + "] because ", e);
		} finally {
			try {
				dout.close();
			} catch (IOException e) {
			}
		}
	}
}
