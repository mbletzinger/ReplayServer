package org.nees.mustsim.replay.db.data;

import java.util.List;

public class DoubleMatrix {
	private final double [][] data;
	private final boolean [][] isNull;
	public DoubleMatrix(List<List<Double>> idata, int rowSize) {
		data = new double[idata.size()][rowSize];
		isNull = new boolean[idata.size()][rowSize];
		int r = 0;
		for (List<Double> row : idata) {
			int c = 0;
			for (Double col : row) {
				if(col != null) {
					data[r][c] = col;
				} else {
					isNull[r][c] = true;
				}
				c++;
			}
			r++;
		}
	}
	public boolean isNull(int row, int col) {
		return isNull[row][col];
	}
	public double value(int row, int col) {
		return data[row][col];
	}
	public int [] sizes() {
		int [] result = new int[2];
		result[0] = data.length;
		result[1] = data[0].length;
		return result;
	}
	public void set(int row, int col, double value) {
		data[row][col] = value;
		isNull[row][col] = false;
	}
}
