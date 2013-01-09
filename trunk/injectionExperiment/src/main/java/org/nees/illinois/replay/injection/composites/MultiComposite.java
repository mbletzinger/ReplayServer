package org.nees.illinois.replay.injection.composites;

import org.nees.illinois.replay.injection.blocks.DataTypeBlock;
import org.nees.illinois.replay.injection.blocks.DuplicateTypeBlock;
import org.nees.illinois.replay.injection.blocks.SimpleBlock;

import com.google.inject.Inject;

public class MultiComposite {
	final SimpleBlock block;
	final DataTypeBlock dtblock;
	final DuplicateTypeBlock dup;
	
	@Inject
	public MultiComposite(SimpleBlock block, DataTypeBlock dtblock,
			DuplicateTypeBlock dup) {
		super();
		this.block = block;
		this.dtblock = dtblock;
		this.dup = dup;
	}

	/**
	 * @return the block
	 */
	public SimpleBlock getBlock() {
		return block;
	}

	/**
	 * @return the dtblock
	 */
	public DataTypeBlock getDtblock() {
		return dtblock;
	}

	/**
	 * @return the dup
	 */
	public DuplicateTypeBlock getDup() {
		return dup;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MultiComposite [block=" + block + ", dtblock=" + dtblock
				+ ", dup=" + dup + "]";
	}

}
