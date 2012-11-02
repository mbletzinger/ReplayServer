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
	private final OutputStream out;
	public Double2OutputStream(double [][] data) {
		super();
		out = new ByteArrayOutputStream();
		writeData(data);
		buffer = ((ByteArrayOutputStream)out).toByteArray();
	}

	public Double2OutputStream(OutputStream os) {
		super();
		buffer = null;
		out = os;
	}
	
	public void writeData(double [][] data) {

		DataOutputStream dout = new DataOutputStream(out);
		try {
			dout.writeInt(data.length);
			dout.writeInt(data[0].length);
		} catch (IOException e) {
			log.error("Could not write data array because ", e);
			return;
		}
		for (int r = 0; r < data.length; r++) {
			for(int c = 0; c < data[0].length; c++)
				try {
					dout.writeDouble(data[r][c]);
				} catch (IOException e) {
					log.error("Could not write data array because ", e);
					return;
				}
		}
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
	public static long streamsSize(double [][]data) {
		long result = 4; // 2 bytes per 2 ints
		long elements = data.length * data[0].length;
		result += elements * 4; // 4 bytes per double
		return result;		
	}

}
