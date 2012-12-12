package org.nees.illinois.replay.data;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DoubleMatrix {
	private final double[][] data;

	private final boolean[][] isNull;
	
	private final Logger log = LoggerFactory.getLogger(DoubleMatrix.class);

	public DoubleMatrix(List<List<Double>> idata, int columnSize) {
		data = new double[idata.size()][columnSize];
		isNull = new boolean[idata.size()][columnSize];
		int r = 0;
		for (List<Double> row : idata) {
			int c = 0;
			for (Double col : row) {
				log.debug("Filling data[" + r + "][" + c + "]");
				if (col != null) {
					data[r][c] = col;
				} else {
					isNull[r][c] = true;
				}
				c++;
			}
			r++;
		}
	}

	public DoubleMatrix(double [][] idata, int columnSize) {
		data = idata;
		isNull = new boolean [idata.length][idata[0].length];
	}

	/**
	 * @return the data
	 */
	public double[][] getData() {
		return data;
	}

	/**
	 * @return the isNull
	 */
	public boolean[][] getIsNull() {
		return isNull;
	}

	public boolean isNull(int row, int col) {
		return isNull[row][col];
	}

	public void set(int row, int col, double value) {
		data[row][col] = value;
		isNull[row][col] = false;
	}

	public int[] sizes() {
		int[] result = new int[2];
		result[0] = data.length;
		result[1] = data[0].length;
		return result;
	}

	public List<List<Double>> toList() {
		List<List<Double>> result = new ArrayList<List<Double>>();
		for (int r = 0; r < data.length; r++) {
			List<Double> row = new ArrayList<Double>();
			result.add(row);
			for (int c = 0; c < data[r].length; c++) {
				row.add((isNull[r][c] ? null : data[r][c]));
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result = "";
		for (int r = 0; r < data.length; r++) {
			result += "\n\t[";
			for (int c = 0; c < data[r].length; c++) {
				result += (c == 0 ? "" : ", ")
						+ (isNull[r][c] ? "null" : data[r][c]);
			}
			result += "]";
		}
		return result;
	}

	public double value(int row, int col) {
		return data[row][col];
	}
}
