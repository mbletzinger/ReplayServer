package org.nees.illinois.replay.injection.composites;

import org.nees.illinois.replay.injection.blocks.SimpleBlock;

import com.google.inject.Inject;

public class SimpleComposite {
	private final SimpleBlock block;

	@Inject
	public SimpleComposite(SimpleBlock block) {
		super();
		this.block = block;
	}

	/**
	 * @return the block
	 */
	public SimpleBlock getBlock() {
		return block;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SimpleComposite [block=" + block + "]";
	}


}
