package org.nees.mustsim.replay.conversions;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DoubleMatrix2OutputStream {

	public static long streamsSize(double[][] data) {
		long result = 4; // 2 bytes per 2 ints
		long elements = data.length * data[0].length;
		result += elements * 4; // 4 bytes per double
		return result;
	}

	private final byte[] buffer;

	private final Logger log = LoggerFactory
			.getLogger(DoubleMatrix2OutputStream.class);
	private final OutputStream out;

	public DoubleMatrix2OutputStream(double[][] data) {
		super();
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		writeData(data, bout);
		buffer = bout.toByteArray();
		this.out = bout;
	}

	public DoubleMatrix2OutputStream(OutputStream os) {
		super();
		buffer = null;
		out = os;
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

	public void writeData(double[][] data, OutputStream aout) {

		DataOutputStream dout = new DataOutputStream(aout);
		try {
			dout.writeInt(data.length);
			dout.writeInt(data[0].length);
		} catch (IOException e) {
			log.error("Could not write data array because ", e);
			return;
		}
		for (int r = 0; r < data.length; r++) {
			for (int c = 0; c < data[0].length; c++)
				try {
					dout.writeDouble(data[r][c]);
				} catch (IOException e) {
					log.error("Could not write data array because ", e);
					return;
				} finally {
					try {
						dout.close();
					} catch (IOException e) {
					}
				}
		}
	}

}
