package org.nees.illinois.replay.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class combines different matrices together. The matrices are assumed to
 * have unique data columns and rows of data that may or may not be the same
 * time record. The class has an accumulator which contains the results of the
 * merge. The accumulator is empty when an instance is created and then filled
 * with the contents of the first matrix using the merge operation. Subsequent
 * calls to the merge method updates the accumulator with merged data. Multiple
 * matrices can be merged with this class. For example:
 * <nl>
 * <li>The merge object is created with an empty accumulator.</li>
 * <li>The object is "merged" with matrix A which results in the accumulator
 * containing matrix A.</li>
 * <li>When this is merged with matrix B, the accumulator would contain the
 * results of the merge AB.</li>
 * <li>Then matrix C could be merged. After this merge the accumulator would
 * contained the merged results ABC.</li>
 * @author Michael Bletzinger
 */
public class MergeSet {
	/**
	 * Accumulator containing the results of the merge.
	 */
	private final List<MergeRecord> accum = new ArrayList<MergeRecord>();
	/**
	 * Logger.
	 */
	private final Logger log = LoggerFactory.getLogger(MergeSet.class);
	/**
	 * Rate used to determine which columns to use for time comparisons.
	 */
	private final RateType rate;

	/**
	 * @param rate
	 *            Rate used to determine which columns to use for time
	 *            comparisons.
	 */
	public MergeSet(final RateType rate) {
		super();
		this.rate = rate;
	}

	/**
	 * return the current number of columns in the accumulator.
	 * @param withTime
	 *            Include the time columns if true.
	 * @return Number of columns.
	 */
	public final int getColumnSize(final boolean withTime) {
		return accum.get(0).getSpec().getNumberOfColumns(withTime);
	}

	/**
	 * Returns a copy of the accumulator as a double matrix.
	 * @return The double matrix copy.
	 */
	public final DoubleMatrix getResult() {
		List<List<Double>> result = new ArrayList<List<Double>>();
		Collections.sort(accum);
		for (MergeRecord r : accum) {
			result.add(r.getRecord());
		}
		return new DoubleMatrix(result, accum.get(0).getSpec());
	}

	/**
	 * Indicates whether the accumulator is empty or not.
	 * @return True if is is empty.
	 */
	public final boolean isEmpty() {
		return accum.isEmpty();
	}

	/**
	 * * To illustrate the merge:
	 * <nl>
	 * <li>Matrix B is being merged into matrix A.</li>
	 * <li>For a given row from matrix B.
	 * <ul>
	 * <li>If the row matches the time of any row then the row from B is
	 * appended to row A.</li>
	 * <li>Otherwise a new row is added to A which consists of an array of null
	 * values the size of A columns prepended to the row from B.</li>
	 * </ul>
	 * </li>
	 * <li>Any row from A that does not have any appended data from B is
	 * appended with an array of null values the size of B columns.</li>
	 * @param toMerge
	 *            Matrix to merge with the accumulator.
	 */
	public final void merge(final DoubleMatrix toMerge) {
		List<MergeRecord> tmr = new ArrayList<MergeRecord>();
		for (List<Double> r : toMerge.toList()) {
			tmr.add(new MergeRecord(rate, toMerge.getSpec(), r));
		}
		String msg = "Merging ";
		for (MergeRecord mr : tmr) {
			msg += "\n\t[" + mr + "]";
		}
		log.debug(msg);
		if (accum.isEmpty()) {
			accum.addAll(tmr);
			return;
		}

		MatrixSpecI tsz = tmr.get(0).getSpec();
		MatrixSpecI asz = accum.get(0).getSpec();

		log.debug("toMerge column " + tsz.getNumberOfColumns(true)
				+ " Accum columns " + asz.getNumberOfColumns(true));

		for (MergeRecord r : tmr) {
			int i = accum.indexOf(r);
			if (i < 0) {
				continue;
			}
			accum.get(i).append(r.getRecord(), null);
			r.setMerged(true);
		}
		for (MergeRecord r : accum) {
			if (r.isMerged()) {
				r.setMerged(false);
			} else {
				r.appendNulls(tsz);
			}
		}
		for (MergeRecord r : tmr) {
			if (r.isMerged() == false) {
				r.prependNulls(asz);
				accum.add(r);
			}
		}

	}

	/**
	 * (non-Javadoc).
	 * @see java.lang.Object#toString()
	 * @return String representation of accumulator.
	 */
	@Override
	public final String toString() {
		String result = "";
		for (MergeRecord r : accum) {
			result += "\n\t[" + r + "]";
		}
		return result;
	}
}
