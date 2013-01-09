package org.nees.illinois.replay.injection.composites;

import org.nees.illinois.replay.injection.blocks.DataTypeBlock;
import org.nees.illinois.replay.injection.blocks.SimpleBlock;

import com.google.inject.Inject;

public class MixedComposite {

	private final SimpleBlock block;
	private final DataTypeBlock datatype;
	private final String identity;

	@Inject
	public MixedComposite(String identity, SimpleBlock block,
			DataTypeBlock datatype) {
		super();
		this.identity = identity;
		this.block = block;
		this.datatype = datatype;
	}

	/**
	 * @return the block
	 */
	public SimpleBlock getBlock() {
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

}
