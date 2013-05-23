package org.nees.illinois.replay.conversions;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class which converts a String array into a buffered output stream.
 * @author Michael Bletzinger
 */
public class ChannelList2OutputStream {
	/**
	 * Buffer for output stream.
	 */
	private final byte[] buffer;
	/**
	 * Logger.
	 */
	private final Logger log = LoggerFactory
			.getLogger(ChannelList2OutputStream.class);
	/**
	 * Output stream.
	 */
	private final OutputStream out;

	/**
	 * Constructor.
	 * @param channels
	 *            String array of channel names.
	 */
	public ChannelList2OutputStream(final List<String> channels) {
		super();
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		writeChannels(channels, bout);
		buffer = bout.toByteArray();
		this.out = bout;
	}

	/**
	 * @return the buffer
	 */
	public final byte[] getBuffer() {
		return buffer;
	}

	/**
	 * @return the out
	 */
	public final OutputStream getOut() {
		return out;
	}

	/**
	 * Does the actual conversion.
	 * @param channels
	 *            String array of channel names.
	 * @param bout
	 *            Buffered output stream.
	 */
	private void writeChannels(final List<String> channels, final OutputStream bout) {
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
				log.debug("ignoring close error",e);
			}
		}
	}
}
