package org.nees.illinois.replay.injection.guice;

import org.nees.illinois.replay.injection.composites.CompI;
import org.nees.illinois.replay.injection.composites.SimpleComposite;


public class SimpleCompositeModule extends SimpleBlockModule {
	
	public SimpleCompositeModule(String blockName) {
		super(blockName);
	}

	@Override
	protected void configure() {
		super.configure();
		bind(CompI.class).to(SimpleComposite.class);
	}

}
