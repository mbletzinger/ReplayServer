package org.nees.illinois.replay.injection.guice;

import org.nees.illinois.replay.injection.blocks.BlockI;
import org.nees.illinois.replay.injection.blocks.DataTypeBlock;
import org.nees.illinois.replay.injection.blocks.DuplicateTypeBlock;
import org.nees.illinois.replay.injection.blocks.SimpleBlock;
import org.nees.illinois.replay.injection.composites.AnnotatedComposite;
import org.nees.illinois.replay.injection.composites.CompI;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class AnnotatedNoDefaultBlockModule extends AbstractModule {

	private final String blockName;

	public AnnotatedNoDefaultBlockModule(String blockName) {
		super();
		this.blockName = blockName;
	}

	@Override
	protected void configure() {
		bind(BlockI.class).annotatedWith(Names.named("Simple")).toInstance(new SimpleBlock(blockName));
		bind(BlockI.class).annotatedWith(Names.named("DataType")).toInstance(new DataTypeBlock(blockName, 1, 2.3));
		bind(BlockI.class).annotatedWith(Names.named("Duplicate")).toInstance(new DuplicateTypeBlock(blockName, blockName + "2", 1.2, 3.4, 5, 6));
		bind(CompI.class).to(AnnotatedComposite.class);
	}

}
