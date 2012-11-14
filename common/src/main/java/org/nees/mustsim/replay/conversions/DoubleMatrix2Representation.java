package org.nees.mustsim.replay.conversions;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.restlet.representation.ByteArrayRepresentation;
import org.restlet.representation.Representation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DoubleMatrix2Representation {
	private final byte[] buffer;

	private final Logger log = LoggerFactory
			.getLogger(DoubleMatrix2Representation.class);
	private final Representation rep;

	public DoubleMatrix2Representation(double[][] data) {
		super();
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		writeData(data, bout);
		buffer = bout.toByteArray();
		this.rep = new ByteArrayRepresentation(buffer);
	}

	public DoubleMatrix2Representation(Representation os) {
		super();
		buffer = null;
		rep = os;
	}

	public void writeData(double[][] data, OutputStream out) {

		DataOutputStream dout = new DataOutputStream(out);
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

	public static long streamsSize(double[][] data) {
		long result = 4; // 2 bytes per 2 ints
		long elements = data.length * data[0].length;
		result += elements * 4; // 4 bytes per double
		return result;
	}

}
