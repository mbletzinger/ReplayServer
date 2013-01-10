package org.nees.illinois.replay.injection.composites;

import org.nees.illinois.replay.injection.blocks.BlockI;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class AnnotatedComposite implements BlockI, CompI {

	private final BlockI datatype;

	private final BlockI duplicate;

	private final String name;

	private final BlockI simple;

	@Inject
	public AnnotatedComposite(String name, @Named("Simple") BlockI simple,
			@Named("DataType") BlockI datatype,
			@Named("Duplicate") BlockI duplicate) {
		super();
		this.name = name;
		this.simple = simple;
		this.datatype = datatype;
		this.duplicate = duplicate;
	}

	@Override
	public String cpublish() {
		return publish();
	}

	/**
	 * @return the datatype
	 */
	public BlockI getDatatype() {
		return datatype;
	}

	/**
	 * @return the duplicate
	 */
	public BlockI getDuplicate() {
		return duplicate;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the simple
	 */
	public BlockI getSimple() {
		return simple;
	}

	@Override
	public String publish() {
		return toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AnnotatedComposite [name=" + name + ", simple=" + simple
				+ ", datatype=" + datatype + ", duplicate=" + duplicate + "]";
	}

}
