package org.nees.illinois.replay.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class which fills in null elements in a matrix through interpolation or
 * extrapolation.
 * @author Michael Bletzinger
 */
public class MatrixFix {
	/**
	 * Type of null problem.
	 * @author Michael Bletzinger
	 */
	private enum MatrixProblemType {
		/**
		 * Nulls occurring at the beginning of the column.
		 */
		Early,
		/**
		 * Column is empty.
		 */
		Empty,
		/**
		 * Column has no nulls.
		 */
		Full,
		/**
		 * Column has nulls interspersed.
		 */
		Gaps,
		/**
		 * Nulls occurring towards the end of the column.
		 */
		Late
	}

	/**
	 * Logger.
	 */
	private final Logger log = LoggerFactory.getLogger(MatrixFix.class);
	/**
	 * Matrix to be fixed.
	 */
	private final DoubleMatrix matrix;

	/**
	 * Constructor.
	 * @param indata
	 *            Matrix to be fixed.
	 */
	public MatrixFix(final DoubleMatrix indata) {
		super();
		this.matrix = indata;
	};

	/**
	 * Determine the null gap issues for a column.
	 * @param column
	 *            Column index.
	 * @return The issue.
	 */
	private MatrixProblemType columnScan(final int column) {
		boolean hasContents = false;
		boolean hasNulls = false;
		int[] sizes = matrix.sizes();
		boolean nullStart = matrix.isNull(0, column);
		boolean nullEnd = matrix.isNull(sizes[0] - 1, column);
		for (int r = 0; r < sizes[0]; r++) {
			if (matrix.isNull(r, column)) {
				hasNulls = true;
			} else {
				hasContents = true;
			}
		}
		if (hasContents) {
			if (nullStart) {
				return MatrixProblemType.Late;
			}

			if (nullEnd) {
				return MatrixProblemType.Early;
			}
			if (hasNulls) {
				return MatrixProblemType.Gaps;
			}
			return MatrixProblemType.Full;
		}
		return MatrixProblemType.Empty;
	}

	/**
	 * Extrapolate to fill in the nulls of a column.
	 * @param scan
	 *            Type of problem.
	 * @param column
	 *            Index of column.
	 */
	private void extrapolate(final MatrixProblemType scan, final int column) {
		int[] sizes = matrix.sizes();
		int start = 0;
		int interval = 1;
		int end = sizes[0];
		if (scan.equals(MatrixProblemType.Early)) {
			start = sizes[0] - 1;
			interval = -1;
			end = 0;
		}
		// find non-zero value
		int row = start;
		while (matrix.isNull(row, column) && row != end) {
			row += interval;
		}
		int r = start;
		double value = matrix.value(row, column);
		while (r != row) {
			matrix.set(r, column, value);
			r += interval;
		}
	}

	/**
	 * Fix the matrix.
	 */
	public final void fix() {
		int[] sizes = matrix.sizes();
		log.debug("Matrix sizes are [" + sizes[0] + "][" + sizes[1] + "]");
		for (int c = 0; c < sizes[1]; c++) {
			MatrixProblemType scan = columnScan(c);
			while (scan.equals(MatrixProblemType.Full) == false
					&& scan.equals(MatrixProblemType.Empty) == false) {
				log.debug("Scanning column " + c + " is " + scan);
				if (scan.equals(MatrixProblemType.Late)
						|| scan.equals(MatrixProblemType.Early)) {
					extrapolate(scan, c);
					scan = columnScan(c);
					continue;
				}
				int r = 0;
				while (matrix.isNull(r, c) == false) {
					r++;
				}
				interpolate(c, r);
				scan = columnScan(c);
			}
		}

	}

	/**
	 * @return the matrix
	 */
	public final DoubleMatrix getMatrix() {
		return matrix;
	}

	/**
	 * Interpolate to fill in null values in a column.
	 * @param column
	 *            Column index.
	 * @param firstNull
	 *            The first occurrence of a null value.
	 */
	private void interpolate(final int column, final int firstNull) {
		int nonNull = firstNull;

		while (matrix.isNull(nonNull, column)) {
			nonNull++;
		}

		double startVal = matrix.value(firstNull - 1, column);
		double stopVal = matrix.value(nonNull, column);
		double increment = (stopVal - startVal) / (nonNull - (firstNull - 1));
		log.debug("interpolate values: start=" + startVal + " stop=" + stopVal
				+ " non=" + nonNull + " first=" + firstNull + " incr="
				+ increment);
		for (int i = firstNull; i < nonNull; i++) {
			matrix.set(i, column, (startVal + increment * (i - firstNull + 1)));
		}

	}

}
