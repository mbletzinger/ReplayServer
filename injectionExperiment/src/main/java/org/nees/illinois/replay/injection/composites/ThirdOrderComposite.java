package org.nees.illinois.replay.injection.composites;

import org.nees.illinois.replay.injection.blocks.SimpleBlock;

import com.google.inject.Inject;

public class ThirdOrderComposite {

	private final SimpleBlock block;

	private final SecondOrderComposite second;

	@Inject
	public ThirdOrderComposite(SimpleBlock block, SecondOrderComposite second) {
		super();
		this.block = block;
		this.second = second;
	}

	/**
	 * @return the block
	 */
	public SimpleBlock getBlock() {
		return block;
	}
	/**
	 * @return the second
	 */
	public SecondOrderComposite getSecond() {
		return second;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ThirdOrderComposite [block=" + block + ", second=" + second
				+ "]";
	}

}
