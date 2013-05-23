package org.nees.illinois.replay.conversions;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class which converts a double matrix into a buffered output stream.
 * @author Michael Bletzinger
 */
public class DoubleMatrix2OutputStream {

	// public static long streamsSize(double[][] data) {
	// long result = 4; // 2 bytes per 2 ints
	// long elements = data.length * data[0].length;
	// result += elements * 4; // 4 bytes per double
	// return result;
	// }
	/**
	 * Buffer for output stream.
	 */
	private final byte[] buffer;

	/**
	 * Logger.
	 */
	private final Logger log = LoggerFactory
			.getLogger(DoubleMatrix2OutputStream.class);
	/**
	 * Output stream.
	 */
	private final OutputStream out;

	/**
	 * Constructor.
	 * @param data
	 *            Matrix to convert.
	 */
	public DoubleMatrix2OutputStream(final double[][] data) {
		super();
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		writeData(data, bout);
		buffer = bout.toByteArray();
		this.out = bout;
	}

	// /**
	// * Constructor.
	// *@param os
	// */
	// public DoubleMatrix2OutputStream(OutputStream os) {
	// super();
	// buffer = null;
	// out = os;
	// }

	/**
	 * @return the stream buffer.
	 */
	public final byte[] getBuffer() {
		return buffer;
	}

	/**
	 * @return the output stream.
	 */
	public final OutputStream getOut() {
		return out;
	}

	/**
	 * Function that does the actual conversion. The stream has a header of 2
	 * integers for the row size and column size. This is followed by the
	 * remaining data as doubles. See the {@link DataOutputStream
	 * DataOutputStream} class for information about the number formats.
	 * @param data
	 *            double matrix to write.
	 * @param aout
	 *            Output stream.
	 */
	private void writeData(final double[][] data, final OutputStream aout) {

		DataOutputStream dout = new DataOutputStream(aout);
		log.debug("Writing to stream " + data.length + " x " + data[0].length);
		try {
			dout.writeInt(data.length);
			dout.writeInt(data[0].length);
		} catch (IOException e) {
			log.error("Could not write data array because ", e);
			return;
		}
		for (int r = 0; r < data.length; r++) {
			for (int c = 0; c < data[0].length; c++) {
				try {
					dout.writeDouble(data[r][c]);
				} catch (IOException e) {
					log.error("Could not write data array because ", e);
					return;
				} finally {
					try {
						dout.close();
					} catch (IOException e) {
						log.debug("ignoring close error", e);
					}
				}
			}
		}
	}

}
