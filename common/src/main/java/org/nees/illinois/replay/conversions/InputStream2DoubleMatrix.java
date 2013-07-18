package org.nees.illinois.replay.conversions;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.DoubleMatrixI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Converts an input stream into a double matrix.
 * @author Michael Bletzinger
 */
public class InputStream2DoubleMatrix {
	/**
	 * Logger.
	 */
	private final Logger log = LoggerFactory
			.getLogger(InputStream2DoubleMatrix.class);
	/**
	 * Resulting double matrix in list form.
	 */
	private final List<List<Double>> numbers = new ArrayList<List<Double>>();

	/**
	 * Constructor that also does the conversion. The input stream is assumed to
	 * be headed by to integers which contain the row and column sizes followed
	 * by the appropriate number of double values. See the
	 * {@link DataInputStream DataInputStream} class for information about
	 * number formats and sizes.
	 * @param in
	 *            Input stream to convert.
	 */
	public InputStream2DoubleMatrix(final InputStream in) {
		super();
		DataInputStream din = new DataInputStream(in);
		int[] hdrs = new int[2];
		try {
			hdrs[0] = din.readInt();
			hdrs[1] = din.readInt();
		} catch (IOException e) {
			log.error("Cannot read data because", e);
			return;
		}
		int rows = hdrs[0];
		int columns = hdrs[1];
		log.debug("Reading input " + rows + " x " + columns);
		for (int r = 0; r < rows; r++) {
			List<Double> row = new ArrayList<Double>();
			numbers.add(row);
			for (int c = 0; c < columns; c++) {
				try {
					row.add(din.readDouble());
				} catch (IOException e) {
					log.error("Cannot read data because", e);
					numbers.clear();
					return;
				}
			}
		}
	}

	/**
	 * Get double matrix as a DoubleMatrix.
	 * @return the DoubleMatrix instance.
	 */
	public final DoubleMatrixI getMatrix() {
		return new DoubleMatrix(numbers);
	}

	/**
	 * @return the numbers in list form.
	 */
	public final List<List<Double>> getNumbers() {
		return numbers;
	}

}
