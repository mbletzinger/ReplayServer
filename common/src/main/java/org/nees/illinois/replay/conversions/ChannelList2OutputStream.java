package org.nees.illinois.replay.conversions;

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


	public ChannelList2OutputStream(List<String> channels) {
		super();
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		writeChannels(channels, bout);
		buffer = bout.toByteArray();
		this.out = bout;
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

	private void writeChannels(List<String> channels, OutputStream bout) {
		DataOutputStream dout = new DataOutputStream(bout);
		String str = Str2CL.cl2str(channels);
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
