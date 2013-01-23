package org.nees.illinois.replay.conversions;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.data.DoubleMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InputStream2DoubleMatrix {
	/**
	 * @return the numbers
	 */
	public List<List<Double>> getNumbers() {
		return numbers;
	}
	public DoubleMatrix getMatrix() {
		return new DoubleMatrix(numbers, numbers.get(0).size());
	}

	private final List<List<Double>> numbers = new ArrayList<List<Double>>();
	
	private final Logger log = LoggerFactory
			.getLogger(InputStream2DoubleMatrix.class);

	public InputStream2DoubleMatrix(InputStream in) {
		super();
		DataInputStream din = new DataInputStream(in);
		int [] hdrs = new int [2];
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

}
