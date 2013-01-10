package org.nees.illinois.replay.injection.composites;

import org.nees.illinois.replay.injection.blocks.BlockI;

import com.google.inject.Inject;

public class SimpleComposite implements BlockI, CompI {
	private final BlockI block;

	@Inject
	public SimpleComposite(BlockI block) {
		super();
		this.block = block;
	}

	/**
	 * @return the block
	 */
	public BlockI getBlock() {
		return block;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SimpleComposite [block=" + block + "]";
	}

	@Override
	public String publish() {
		return toString();
	}

	@Override
	public String cpublish() {
		return publish();
	}

}
