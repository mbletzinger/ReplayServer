package org.nees.illinois.replay.test.server.guice;

import org.nees.illinois.replay.data.DataUpdateI;
import org.nees.illinois.replay.registries.ChannelLookups;
import org.nees.illinois.replay.registries.ExperimentModule;
import org.nees.illinois.replay.test.data.TestDataUpdates;

import com.google.inject.name.Names;

public class TestServerModule extends ExperimentModule {

	public TestServerModule() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void configure() {
		bind(DataUpdateI.class).to(TestDataUpdates.class);
		bind(String.class).annotatedWith(Names.named("experiment")).toInstance(getExperiment());
	}

}
