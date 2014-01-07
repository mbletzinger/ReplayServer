package org.nees.illinois.replay.test.utils.data;

import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.DoubleMatrixI;

/**
 * Class which returns a subset of the double matrix.
 * @author Michael Bletzinger
 */
public class SubsetCarver {
	/**
	 * Original data matrix.
	 */
	private final DoubleMatrixI original;
	/**
	 * Starting subset column. A null value means use the first column;
	 */
	private Integer startColumn;
	/**
	 * Starting subset row. A null value means use the first row;
	 */
	private Integer startRow;
	/**
	 * Ending subset column. A null value means use the last column;
	 */
	private Integer stopColumn;
	/**
	 * Ending subset row. A null value means use the last row;
	 */
	private Integer stopRow;

	/**
	 * @param original
	 *            double matrix.
	 */
	public SubsetCarver(final DoubleMatrixI original) {
		this.original = original;
	}

	/**
	 * @return the startColumn
	 */
	public final Integer getStartColumn() {
		return startColumn;
	}

	/**
	 * @return the startRow
	 */
	public final Integer getStartRow() {
		return startRow;
	}

	/**
	 * @return the stopColumn
	 */
	public final Integer getStopColumn() {
		return stopColumn;
	}

	/**
	 * @return the stopRow
	 */
	public final Integer getStopRow() {
		return stopRow;
	}

	/**
	 * @param startColumn
	 *            the startColumn to set
	 */
	public final void setStartColumn(final Integer startColumn) {
		this.startColumn = startColumn;
	}

	/**
	 * @param startRow
	 *            the startRow to set
	 */
	public final void setStartRow(final Integer startRow) {
		this.startRow = startRow;
	}

	/**
	 * @param stopColumn
	 *            the stopColumn to set
	 */
	public final void setStopColumn(final Integer stopColumn) {
		this.stopColumn = stopColumn;
	}

	/**
	 * @param stopRow
	 *            the stopRow to set
	 */
	public final void setStopRow(final Integer stopRow) {
		this.stopRow = stopRow;
	}

	/**
	 * @return the subset specified by the boundary values.
	 */
	public final DoubleMatrixI subset() {
		List<List<Double>> data = original.toList();
		int[] sizes = original.sizes();
		int startR = (startRow == null ? 0 : startRow.intValue());
		int stopR = (stopRow == null ? sizes[0] : stopRow.intValue());

		int startC = (startColumn == null ? 0 : startColumn.intValue());
		int stopC = (stopColumn == null ? sizes[1] : stopColumn.intValue());
		List<List<Double>> result = new ArrayList<List<Double>>();
		for (int r = startR; r < stopR; r++) {
			List<Double> orow = data.get(r);
			List<Double> row = new ArrayList<Double>();
			for (int c = startC; c < stopC; c++) {
				row.add(orow.get(c));
			}
			result.add(row);
		}
		return new DoubleMatrix(result);
	}

}
