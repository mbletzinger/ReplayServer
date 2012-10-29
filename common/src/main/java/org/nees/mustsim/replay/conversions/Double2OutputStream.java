package org.nees.mustsim.replay.conversions;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Double2OutputStream {
	private final byte [] buffer;

	private final Logger log = LoggerFactory
			.getLogger(Double2OutputStream.class);
	private OutputStream out;
	public Double2OutputStream(double [][] data) {
		super();
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream(bout);
		out = dout;
		try {
			dout.writeInt(data.length);
			dout.writeInt(data[0].length);
		} catch (IOException e) {
			log.error("Could not write data array because ", e);
			out = null;
			buffer = bout.toByteArray();
			return;
		}
		for (int r = 0; r < data.length; r++) {
			for(int c = 0; c < data[0].length; c++)
				try {
					dout.writeDouble(data[r][c]);
				} catch (IOException e) {
					log.error("Could not write data array because ", e);
					out = null;
					buffer = bout.toByteArray();
					return;
				}
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
