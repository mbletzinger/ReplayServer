package org.nees.illinois.replay.test.server.guice;

import org.nees.illinois.replay.data.DataQueryI;
import org.nees.illinois.replay.data.DataUpdateI;
import org.nees.illinois.replay.registries.ExperimentModule;
import org.nees.illinois.replay.registries.QueryRegistry;
import org.nees.illinois.replay.test.data.TestDataQuery;
import org.nees.illinois.replay.test.data.TestDataUpdates;

import com.google.inject.AbstractModule;

public class ResourcesTestModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(DataUpdateI.class).to(TestDataUpdates.class);
		bind(DataQueryI.class).to(TestDataQuery.class);
		bind(QueryRegistry.class).toInstance(new QueryRegistry());
		bind(ExperimentModule.class).to(TestServerModule.class);
	}

}
