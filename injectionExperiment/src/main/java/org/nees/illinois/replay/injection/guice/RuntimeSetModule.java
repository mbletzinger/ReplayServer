package org.nees.illinois.replay.injection.guice;

import com.google.inject.AbstractModule;

public class RuntimeSetModule extends AbstractModule{

	protected final String setName;
	protected final int setCount;
	protected final double setRatio;

	public RuntimeSetModule(String setName, int setCount, double setRatio) {
		super();
		this.setName = setName;
		this.setCount = setCount;
		this.setRatio = setRatio;
	}

	@Override
	protected void configure() {		
	}

}
