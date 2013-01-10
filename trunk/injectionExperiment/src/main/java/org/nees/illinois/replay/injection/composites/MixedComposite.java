package org.nees.illinois.replay.injection.composites;

import org.nees.illinois.replay.injection.blocks.BlockI;
import org.nees.illinois.replay.injection.blocks.DataTypeBlock;

import com.google.inject.Inject;

public class MixedComposite implements BlockI, CompI {

	private final BlockI block;
	private final DataTypeBlock datatype;
	private final String identity;

	@Inject
	public MixedComposite(String identity, BlockI block, DataTypeBlock datatype) {
		super();
		this.identity = identity;
		this.block = block;
		this.datatype = datatype;
	}

	/**
	 * @return the block
	 */
	public BlockI getBlock() {
		return block;
	}

	/**
	 * @return the datatype
	 */
	public DataTypeBlock getDatatype() {
		return datatype;
	}

	/**
	 * @return the identity
	 */
	public String getIdentity() {
		return identity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MixedComposite [identity=" + identity + ", block=" + block
				+ ", datatype=" + datatype + "]";
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
