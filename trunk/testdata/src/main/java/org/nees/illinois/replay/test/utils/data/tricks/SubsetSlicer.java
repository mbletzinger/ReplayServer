package org.nees.illinois.replay.test.utils.data.tricks;

import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.DoubleMatrixI;

/**
 * Class which extracts discreet rows or columns from a double matrix.
 * @author Michael Bletzinger
 */
public class SubsetSlicer {
	/**
	 * Original matrix.
	 */
	private final DoubleMatrixI original;
	/**
	 * Flag indicating sliced columns or sliced rows.
	 */
	private boolean sliceColumn;
	/**
	 * List of slice indexes.
	 */
	private final List<Integer> slices = new ArrayList<Integer>();

	/**
	 * @param original
	 *            Matrix to slice.
	 */
	public SubsetSlicer(final DoubleMatrixI original) {
		this.original = original;
	}

	/**
	 * Add an index to slice.
	 * @param s
	 *            index.
	 */
	public final void addSlice(final int s) {
		slices.add(new Integer(s));
	}

	/**
	 * Add a list of indexes for slices.
	 * @param slices
	 *            array of indexes.
	 */
	public final void addSlices(final int[] slices) {
		for (int s : slices) {
			addSlice(s);
		}
	}

	/**
	 * Add a list of indexes for slices.
	 * @param slices
	 *            array of indexes.
	 */
	public final void addSlices(final List<Integer> slices) {
		for (int s : slices) {
			addSlice(s);
		}
	}

	/**
	 * Remove all of the slice indexes.
	 */
	public final void clearSlices() {
		slices.clear();
	}

	/**
	 * @return the sliceColumn
	 */
	public final boolean isSliceColumn() {
		return sliceColumn;
	}

	/**
	 * @param sliceColumn
	 *            the sliceColumn to set
	 */
	public final void setSliceColumn(final boolean sliceColumn) {
		this.sliceColumn = sliceColumn;
	}

	/**
	 * Slice the original matrix with discreet rows or columns and create a new
	 * matrix from the results.
	 * @return matrix of slices
	 */
	public final DoubleMatrixI slice() {
		if (sliceColumn) {
			return sliceColumns();
		}
		return sliceRows();
	}

	/**
	 * Slice the original matrix by columns.
	 * @return matrix of slices
	 */
	private DoubleMatrixI sliceColumns() {
		List<List<Double>> result = new ArrayList<List<Double>>();
		for (List<Double> orow : original.toList()) {
			List<Double> row = new ArrayList<Double>();
			for (int s : slices) {
				row.add(orow.get(s));
			}
			result.add(row);
		}
		return new DoubleMatrix(result);
	}

	/**
	 * Slice the original matrix by rows.
	 * @return matrix of slices
	 */
	private DoubleMatrixI sliceRows() {
		List<List<Double>> result = new ArrayList<List<Double>>();
		for (int s : slices) {
			result.add(original.getRow(s));
		}
		return new DoubleMatrix(result);
	}
}
