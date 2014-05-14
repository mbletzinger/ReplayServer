package org.nees.illinois.replay.test.utils.gen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.DoubleMatrixI;
import org.nees.illinois.replay.data.MatrixSpecI;

/**
 * Class which sorts the rows by the first column assumed to be time in seconds.
 * @author Michael Bletzinger
 */
public class SortableDoubleMatrix implements DoubleMatrixI {

	/**
	 * Actual double matrix.
	 */
	private DoubleMatrix dm;

	/**
	 * Create an instance from a double[][] array type.
	 * @param idata
	 *            The double [][] array.
	 */
	public SortableDoubleMatrix(final double[][] idata) {
		dm = new DoubleMatrix(idata);
	}

	/**
	 * Create an instance from a double list.
	 * @param idata
	 *            The double list.
	 */
	public SortableDoubleMatrix(final List<List<Double>> idata) {
		dm = new DoubleMatrix(idata);
	}

	@Override
	public final void append(final List<Double> row) {
		dm.append(row);
	}

	@Override
	public final void clear() {
		dm.clear();
	}

	@Override
	public final double[][] getData() {
		return dm.getData();
	}

	@Override
	public final List<Double> getRow(final int row) {
		return dm.getRow(row);
	}

	@Override
	public final MatrixSpecI getSpec() {
		return dm.getSpec();
	}

	@Override
	public final boolean isNull(final int row, final int col) {
		return dm.isNull(row, col);
	}

	@Override
	public final void set(final int row, final int col, final Double value) {
		dm.set(row, col, value);
	}

	@Override
	public final int[] sizes() {
		return dm.sizes();
	}

	/**
	 * Sort the matrix row-wise.
	 */
	public final void sort() {
		List<DataRow> sortMe = new ArrayList<DataRow>();
		for (List<Double> r : dm.toList()) {
			DataRow dr = new DataRow(r);
			sortMe.add(dr);
		}
		Collections.sort(sortMe);
		List<List<Double>> data = new ArrayList<List<Double>>();
		for (DataRow dr : sortMe) {
			List<Double> r = dr.getData();
			data.add(r);
		}
		dm = new DoubleMatrix(data);
	}

	@Override
	public final int timeIndex(final double timestamp) {
		return dm.timeIndex(timestamp);
	}

	@Override
	public final double timeWindow() {
		return dm.timeWindow();
	}

	@Override
	public final List<List<Double>> toList() {
		return dm.toList();
	}

	@Override
	public final double value(final int row, final int col) {
		return dm.value(row, col);
	}
}
