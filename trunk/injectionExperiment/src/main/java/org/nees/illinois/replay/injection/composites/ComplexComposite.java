package org.nees.illinois.replay.injection.composites;

import org.nees.illinois.replay.injection.blocks.BlockI;
import org.nees.illinois.replay.injection.blocks.DuplicateTypeBlock;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class ComplexComposite implements BlockI, CompI {
	final BlockI block;

	final CompI composite;

	final DuplicateTypeBlock dup;

	final String name;

	@Inject
	public ComplexComposite(String name, DuplicateTypeBlock dup, BlockI block,
			@Named("1st Order") CompI composite) {
		super();
		this.name = name;
		this.dup = dup;
		this.block = block;
		this.composite = composite;
	}

	@Override
	public String cpublish() {
		return publish();
	}
	/**
	 * @return the block
	 */
	public BlockI getBlock() {
		return block;
	}
	/**
	 * @return the composite
	 */
	public CompI getComposite() {
		return composite;
	}

	/**
	 * @return the dup
	 */
	public DuplicateTypeBlock getDup() {
		return dup;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	@Override
	public String publish() {
		return toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ComplexComposite [name=" + name + ", dup=" + dup + ", block="
				+ block + ", composite=" + composite + "]";
	}

}
