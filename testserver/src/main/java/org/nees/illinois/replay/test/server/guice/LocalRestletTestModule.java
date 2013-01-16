package org.nees.illinois.replay.test.server.guice;

import org.nees.illinois.replay.data.DataQueryI;
import org.nees.illinois.replay.data.DataUpdateI;
import org.nees.illinois.replay.registries.ExperimentModule;
import org.nees.illinois.replay.restlet.DataQueryServerResource;
import org.nees.illinois.replay.restlet.DataTableServerResource;
import org.nees.illinois.replay.restlet.HostInfo;
import org.nees.illinois.replay.restlet.ReplayServerApplication;
import org.nees.illinois.replay.test.data.TestDataQuery;
import org.nees.illinois.replay.test.data.TestDataUpdates;
import org.nees.illinois.replay.test.server.restlet.LocalRestletTestApplication;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class LocalRestletTestModule extends AbstractModule {

	public LocalRestletTestModule() {
	}

	@Override
	protected void configure() {
		bind(String.class).annotatedWith(Names.named("appRoot")).toInstance(new String("/test/data"));
		bind(Boolean.class).annotatedWith(Names.named("tracing")).toInstance(new Boolean(true));
		bind(Class.class).annotatedWith(Names.named("TableResource")).toInstance(DataTableServerResource.class);
		bind(Class.class).annotatedWith(Names.named("QueryResource")).toInstance(DataQueryServerResource.class);
		bind(HostInfo.class).toInstance(new HostInfo(8111, "localhost",true));
		bind(ReplayServerApplication.class).to(LocalRestletTestApplication.class);
		bind(DataUpdateI.class).to(TestDataUpdates.class);
		bind(DataQueryI.class).to(TestDataQuery.class);
		bind(ExperimentModule.class).to(TestServerModule.class);
	}

}
