package org.nees.illinois.replay.data;

import java.util.ArrayList;
import java.util.List;


/**
 * Performs merges on a particular row.
 * @author Michael Bletzinger
 */
public class MergeRecord implements Comparable<MergeRecord> {
	/**
	 * Column descriptions.
	 */
	private MatrixSpecI spec;

	/**
	 * Rate to do the comparisons with.
	 */
	private final RateType compareRate;
	/**
	 * Flag indicating that the merge is complete for this row.
	 */
	private boolean merged = false;
	/**
	 * Internal representation.
	 */
	private List<Double> record;

	/**
	 * Constructor.
	 * @param compareRate
	 *            Used to determine which time columns.
	 * @param spec
	 *            Column descriptions.
	 * @param list
	 *            Row of data.
	 */
	public MergeRecord(final RateType compareRate, final MatrixSpecI spec,
			final List<Double> list) {
		super();
		this.record = list;
		this.spec = spec;
		this.compareRate = compareRate;
	}

	/**
	 * Appends new columns after the existing columns.
	 * @param after
	 *            New data.
	 * @param newCols
	 *            New table definition.
	 */
	public final void append(final List<Double> after,
			final MatrixSpecI newCols) {
		int i = spec.numberOfTimeColumns();
		record.addAll(after.subList(i, after.size()));
		spec.appendColumns(newCols);
		merged = true;
	}

	/**
	 * 'appends empty elements to a row.
	 * @param newCols
	 *            Spec for the new columns.
	 */
	public final void appendNulls(final MatrixSpecI newCols) {
		List<Double> nulls = new ArrayList<Double>();
		int num = newCols.getNumberOfColumns(true);
		for (int n = 0; n < num; n++) {
			nulls.add(null);
		}
		append(nulls, null);
		spec.appendColumns(newCols);
	}

	@Override
	public final int compareTo(final MergeRecord arg0) {
		TimeCompare tc = new TimeCompare(compareRate);
		return tc.compareRow(record, arg0.record);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(final Object obj) {
		if (obj instanceof MergeRecord == false) {
			return false;
		}
		return compareTo((MergeRecord) obj) == 0;
	}

	/**
	 * @return the noc
	 */
	public final MatrixSpecI getSpec() {
		return spec;
	}

	/**
	 * @return the list
	 */
	public final List<Double> getRecord() {
		return record;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		return super.hashCode();
	}

	/**
	 * @return the merged
	 */
	public final boolean isMerged() {
		return merged;
	}

	/**
	 * Prepend columns to the existing record. Note that the matrix spec of the
	 * prepended records will be come the spec of the merged record.
	 * @param before
	 *            Columns to prepend.
	 * @param newCols
	 *            Matrix spec for the prepended columns.
	 */
	public final void prepend(final List<Double> before,
			final MatrixSpecI newCols) {
		int i = spec.numberOfTimeColumns();
		record.addAll(i, before.subList(i, before.size()));
		newCols.appendColumns(spec);
		spec = newCols;
		merged = true;
	}

	/**
	 * Prepend a set number of nulls to the existing record.
	 * @param newCols
	 *            matrix spec for the prepended data.
	 */
	public final void prependNulls(final MatrixSpecI newCols) {
		List<Double> nulls = new ArrayList<Double>();
		int num = newCols.getNumberOfColumns(true);
		for (int n = 0; n < num; n++) {
			nulls.add(null);
		}
		prepend(nulls, newCols);
	}

	/**
	 * @param merged
	 *            the merged to set
	 */
	public final void setMerged(final boolean merged) {
		this.merged = merged;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		String result = "";
		boolean first = true;
		for (Double d : record) {
			result += (first ? "" : ", ") + d;
			first = false;
		}
		return result;
	}
}
