package org.nees.illinois.replay.test.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.nees.illinois.replay.data.DoubleMatrix;

public class SortableDoubleMatrix extends DoubleMatrix {

	public SortableDoubleMatrix(double[][] idata) {
		super(idata);
	}

	public SortableDoubleMatrix(List<List<Double>> idata) {
		super(idata);
	}

	public void sort() {
		List<DataRow> sortMe = new ArrayList<DataRow>();
		for(List<Double> r : data) {
			DataRow dr = new DataRow(r);
			sortMe.add(dr);
		}
		Collections.sort(sortMe);
		data.clear();
		for(DataRow dr : sortMe) {
			List<Double> r = dr.getData();
			data.add(r);
		}
	}
}
