package org.nees.illinois.replay.injection.blocks;

import com.google.inject.Inject;

public class DuplicateTypeBlock implements BlockI {

	final int count;
	final int count1;
	final String id;
	final String id2;
	final double ratio;
	final double ratio3;

	@Inject
	public DuplicateTypeBlock(String id, String id2, double ratio,
			double ratio3, int count, int count1) {
		super();
		this.id = id;
		this.id2 = id2;
		this.ratio = ratio;
		this.ratio3 = ratio3;
		this.count = count;
		this.count1 = count1;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @return the count1
	 */
	public int getCount1() {
		return count1;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the id2
	 */
	public String getId2() {
		return id2;
	}

	/**
	 * @return the ratio
	 */
	public double getRatio() {
		return ratio;
	}

	/**
	 * @return the ratio3
	 */
	public double getRatio3() {
		return ratio3;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DuplicateTypeBlock [id=" + id + ", id2=" + id2 + ", ratio="
				+ ratio + ", ratio3=" + ratio3 + ", count=" + count
				+ ", count1=" + count1 + "]";
	}
	@Override
	public String publish() {
		return toString();
	}

}
