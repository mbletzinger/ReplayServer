package org.nees.mustsim.replay.conversions;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChannelList2OutputStream {
	private final byte [] buffer;
	private final Logger log = LoggerFactory
			.getLogger(ChannelList2OutputStream.class);
	private OutputStream out;
	public ChannelList2OutputStream(List<String>channels) {
		super();
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		String str = "";
		boolean first = true;
		for (String s : channels) {
			str += (first ? "" : ",") + s;
			first = false;
		}
		DataOutputStream dout = new DataOutputStream(bout);
		out = dout;
		try {
			dout.writeChars(str);
		} catch (IOException e) {
			log.error("Could not write the string [" + str + "] because ",e);
			out = null;
		}
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
	

}
