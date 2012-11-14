package org.nees.mustsim.replay.conversions;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.restlet.representation.ByteArrayRepresentation;
import org.restlet.representation.Representation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChannelList2Representation {
	private final byte[] buffer;
	private final Logger log = LoggerFactory
			.getLogger(ChannelList2Representation.class);
	private final Representation rep;


	public ChannelList2Representation(List<String> channels) {
		super();
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		writeChannels(channels, bout);
		buffer = bout.toByteArray();
		this.rep = new ByteArrayRepresentation(buffer);
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
	public Representation getRep() {
		return rep;
	}

	private void writeChannels(List<String> channels, OutputStream out) {
		DataOutputStream dout = new DataOutputStream(out);
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
