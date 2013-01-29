package org.nees.illinois.replay.injection.guice;

import org.nees.illinois.replay.injection.blocks.DataTypeBlock;
import org.nees.illinois.replay.injection.blocks.DuplicateTypeBlock;
import org.nees.illinois.replay.injection.composites.CompI;
import org.nees.illinois.replay.injection.composites.ComplexComposite;
import org.nees.illinois.replay.injection.composites.MixedComposite;

import com.google.inject.name.Names;

public class ComplexCompositeModule extends SimpleBlockModule {

	private String dupString1 = "Dup String 1";

	private String dupString2 = "Dup String 2";

	private String firstOrder = "1st Order";

	private String secondOrder = "2nd Order";

	public ComplexCompositeModule(String blockName) {
		super(blockName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.nees.illinois.replay.injection.guice.SimpleBlockModule#configure()
	 */
	@Override
	protected void configure() {
		super.configure();
		bind(CompI.class).to(ComplexComposite.class);
		bind(DuplicateTypeBlock.class).toInstance(
				new DuplicateTypeBlock(dupString1, dupString2, 1.2, 3.4, 5, 6));
		bind(DataTypeBlock.class).toInstance(
				new DataTypeBlock("DataType String", 1, 2.3));
		bind(CompI.class).annotatedWith(Names.named(firstOrder)).to(
				MixedComposite.class);
		bind(String.class).toInstance(new String(secondOrder));
	}

	/**
	 * @return the dupString1
	 */
	public synchronized String getDupString1() {
		return dupString1;
	}

	/**
	 * @return the dupString2
	 */
	public synchronized String getDupString2() {
		return dupString2;
	}

	/**
	 * @return the firstOrder
	 */
	public synchronized String getFirstOrder() {
		return firstOrder;
	}

	/**
	 * @return the secondOrder
	 */
	public synchronized String getSecondOrder() {
		return secondOrder;
	}

	/**
	 * @param dupString1
	 *            the dupString1 to set
	 */
	public synchronized void setDupString1(String dupString1) {
		this.dupString1 = dupString1;
	}

	/**
	 * @param dupString2
	 *            the dupString2 to set
	 */
	public synchronized void setDupString2(String dupString2) {
		this.dupString2 = dupString2;
	}

	/**
	 * @param firstOrder
	 *            the firstOrder to set
	 */
	public synchronized void setFirstOrder(String firstOrder) {
		this.firstOrder = firstOrder;
	}

	/**
	 * @param secondOrder
	 *            the secondOrder to set
	 */
	public synchronized void setSecondOrder(String secondOrder) {
		this.secondOrder = secondOrder;
	}

}
