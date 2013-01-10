package org.nees.illinois.replay.injection.guice;

import org.nees.illinois.replay.injection.blocks.BlockI;
import org.nees.illinois.replay.injection.blocks.DataTypeBlock;

import com.google.inject.AbstractModule;

public class DataTypeBlockModule extends AbstractModule {

	public DataTypeBlockModule() {
	}

	@Override
	protected void configure() {
		bind(BlockI.class).toInstance(new DataTypeBlock("DataType String", 1, 2.3));
	}

}
