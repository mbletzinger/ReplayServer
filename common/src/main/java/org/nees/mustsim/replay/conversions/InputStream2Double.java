package org.nees.mustsim.replay.conversions;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InputStream2Double {
	/**
	 * @return the numbers
	 */
	public List<List<Double>> getNumbers() {
		return numbers;
	}

	private final List<List<Double>> numbers = new ArrayList<List<Double>>();
	private final Logger log = LoggerFactory
			.getLogger(InputStream2Double.class);

	public InputStream2Double(InputStream in) {
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
