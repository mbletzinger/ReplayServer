package org.nees.illinois.replay.injection.guice;

import org.nees.illinois.replay.injection.blocks.DataTypeBlock;
import org.nees.illinois.replay.injection.blocks.DuplicateTypeBlock;
import org.nees.illinois.replay.injection.composites.CompI;
import org.nees.illinois.replay.injection.composites.ComplexComposite;
import org.nees.illinois.replay.injection.composites.MixedComposite;

import com.google.inject.name.Names;

public class ComplexCompositeModule extends SimpleBlockModule {

	/* (non-Javadoc)
	 * @see org.nees.illinois.replay.injection.guice.SimpleBlockModule#configure()
	 */
	@Override
	protected void configure() {
		super.configure();
		bind(CompI.class).to(ComplexComposite.class);
		bind(DuplicateTypeBlock.class).toInstance(new DuplicateTypeBlock("Dup String 1", "Dup String 2", 1.2, 3.4, 5, 6));
		bind(DataTypeBlock.class).toInstance(new DataTypeBlock("DataType String", 1, 2.3));
		bind(CompI.class).annotatedWith(Names.named("1st Order")).to(MixedComposite.class);
		bind(String.class).toInstance(new String("2nd Order"));
	}

	public ComplexCompositeModule(String blockName) {
		super(blockName);
	}
	
	

}
