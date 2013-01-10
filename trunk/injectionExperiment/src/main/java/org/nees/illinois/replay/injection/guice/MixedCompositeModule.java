package org.nees.illinois.replay.injection.guice;

import org.nees.illinois.replay.injection.blocks.DataTypeBlock;
import org.nees.illinois.replay.injection.composites.CompI;
import org.nees.illinois.replay.injection.composites.MixedComposite;

public class MixedCompositeModule extends SimpleBlockModule {

	/* (non-Javadoc)
	 * @see org.nees.illinois.replay.injection.guice.SimpleBlockModule#configure()
	 */
	@Override
	protected void configure() {
		super.configure();
		bind(CompI.class).to(MixedComposite.class);
		bind(DataTypeBlock.class).toInstance(new DataTypeBlock("DataType String", 1, 2.3));
		bind(String.class).toInstance(new String("Mixed String"));
	}

	public MixedCompositeModule(String blockName) {
		super(blockName);
	}

}
