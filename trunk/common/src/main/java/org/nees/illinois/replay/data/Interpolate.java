package org.nees.illinois.replay.data;

import org.apache.log4j.Logger;

public class Interpolate {
	public enum ColScanType {
		Early, Empty, Full, Gaps, Late
	}

	private final Logger log = Logger.getLogger(Interpolate.class);
	private final DoubleMatrix matrix;

	public Interpolate(DoubleMatrix indata) {
		super();
		this.matrix = indata;
	};

	public ColScanType columnScan(int column) {
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
				return ColScanType.Late;
			}

			if (nullEnd) {
				return ColScanType.Early;
			}
			if (hasNulls) {
				return ColScanType.Gaps;
			}
			return ColScanType.Full;
		}
		return ColScanType.Empty;
	}

	public void extrapolate(ColScanType scan, int column) {
		int[] sizes = matrix.sizes();
		int start = 0;
		int interval = 1;
		int end = sizes[0];
		if (scan.equals(ColScanType.Early)) {
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

	public void fix() {
		int[] sizes = matrix.sizes();
		log.debug("Matrix sizes are ["+sizes[0] + "][" + sizes[1] + "]");
		for (int c = 0; c < sizes[1]; c++) {
			ColScanType scan = columnScan(c);
			while (scan.equals(ColScanType.Full) == false
					&& scan.equals(ColScanType.Empty) == false) {
				log.debug("Scanning column " + c + " is " + scan);
				if (scan.equals(ColScanType.Late)
						|| scan.equals(ColScanType.Early)) {
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
	public DoubleMatrix getMatrix() {
		return matrix;
	}

	public void interpolate(int column, int firstNull) {
		int nonNull = firstNull;

		while (matrix.isNull(nonNull, column)) {
			nonNull++;
		}

		double startVal = matrix.value(firstNull - 1, column);
		double stopVal = matrix.value(nonNull, column);
		double increment = (stopVal - startVal) / (nonNull - (firstNull - 1));
		log.debug("interpolate values: start=" + startVal + " stop=" + stopVal + " non=" + nonNull + " first=" + firstNull + " incr=" + increment);
		for (int i = firstNull; i < nonNull; i++) {
			matrix.set(i, column, (startVal + increment * (i - firstNull + 1)));
		}

	}

}
