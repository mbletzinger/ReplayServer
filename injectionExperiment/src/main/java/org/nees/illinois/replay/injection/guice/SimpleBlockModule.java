package org.nees.illinois.replay.injection.guice;

import org.nees.illinois.replay.injection.blocks.BlockI;
import org.nees.illinois.replay.injection.blocks.SimpleBlock;

import com.google.inject.AbstractModule;

public class SimpleBlockModule extends AbstractModule {

	private String blockName;
	public SimpleBlockModule(String blockName) {
		this.blockName = blockName;
	}

	@Override
	protected void configure() {
		bind(BlockI.class).toInstance(new SimpleBlock(blockName));
	}

}
