package org.nees.illinois.replay.injection.blocks;

import com.google.inject.Inject;

public class DataTypeBlock {

	final int count;
	final String id;
	final double ratio;
	
	@Inject
	public DataTypeBlock(String id, int count, double ratio) {
		super();
		this.id = id;
		this.count = count;
		this.ratio = ratio;
	}
	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @return the ratio
	 */
	public double getRatio() {
		return ratio;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DataTypeBlock \"" + id + "\" " + count + " " + ratio;
	}

}
