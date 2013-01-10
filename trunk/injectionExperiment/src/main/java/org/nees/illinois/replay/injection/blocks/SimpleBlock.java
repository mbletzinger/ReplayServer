package org.nees.illinois.replay.injection.blocks;

import com.google.inject.Inject;

public class SimpleBlock implements BlockI {

	final String name;

	@Inject
	public SimpleBlock(String name) {
		super();
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SimpleBlock \"" + name + "\"";
	}

	@Override
	public String publish() {
		return toString();
	}

}
