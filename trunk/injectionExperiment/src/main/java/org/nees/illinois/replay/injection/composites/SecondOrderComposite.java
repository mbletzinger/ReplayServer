package org.nees.illinois.replay.injection.composites;

import org.nees.illinois.replay.injection.blocks.SimpleBlock;

import com.google.inject.Inject;

public class SecondOrderComposite {

	private final SimpleBlock block;

	private final MixedComposite mixed;

	@Inject
	public SecondOrderComposite(SimpleBlock block, MixedComposite mixed) {
		super();
		this.block = block;
		this.mixed = mixed;
	}

	/**
	 * @return the block
	 */
	public SimpleBlock getBlock() {
		return block;
	}

	/**
	 * @return the mixed
	 */
	public MixedComposite getMixed() {
		return mixed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SecondOrderComposite [block=" + block + ", mixed=" + mixed
				+ "]";
	}

}
