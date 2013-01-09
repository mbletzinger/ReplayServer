package org.nees.illinois.replay.injection.composites;

import org.nees.illinois.replay.injection.blocks.SimpleBlock;

import com.google.inject.Inject;

public class DuplicateComposite {

	private final SimpleBlock block;
	private final SimpleBlock block1;

	@Inject
	public DuplicateComposite(SimpleBlock block, SimpleBlock block1) {
		super();
		this.block = block;
		this.block1 = block1;
	}

	/**
	 * @return the block
	 */
	public SimpleBlock getBlock() {
		return block;
	}

	/**
	 * @return the block1
	 */
	public SimpleBlock getBlock1() {
		return block1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DuplicateComposite [block=" + block + ", block1=" + block1
				+ "]";
	}

}
