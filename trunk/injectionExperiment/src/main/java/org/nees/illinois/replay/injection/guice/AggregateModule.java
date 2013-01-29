package org.nees.illinois.replay.injection.guice;

import com.google.inject.AbstractModule;

public class AggregateModule extends AbstractModule {

	public final String blockName;

	public AggregateModule(String blockName) {
		super();
		this.blockName = blockName;
	}

	@Override
	protected void configure() {
		install(new AnnotatedNoDefaultBlockModule(blockName));
		install(new DataTypeBlockModule());
	}

	/**
	 * @return the blockName
	 */
	public String getBlockName() {
		return blockName;
	}

}
