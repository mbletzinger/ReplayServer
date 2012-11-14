package org.nees.mustsim.replay.conversions;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.restlet.representation.Representation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Representation2DoubleMatrix {
	/**
	 * @return the numbers
	 */
	public List<List<Double>> getNumbers() {
		return numbers;
	}

	private final List<List<Double>> numbers = new ArrayList<List<Double>>();
	private final Logger log = LoggerFactory
			.getLogger(Representation2DoubleMatrix.class);

	public Representation2DoubleMatrix(Representation rep) {
		super();
		DataInputStream din = null;
		try {
			din = new DataInputStream(rep.getStream());
		} catch (IOException e1) {
			try {
				log.error("Could not read representation \"" + rep.getText() + "\"");
			} catch (IOException e) {
				log.error("Could not read representation \"" + rep + "\"");
			}
			return;
		}
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
