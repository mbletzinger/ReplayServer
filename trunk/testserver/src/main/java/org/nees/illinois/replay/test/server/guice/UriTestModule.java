package org.nees.illinois.replay.test.server.guice;

import org.nees.illinois.replay.data.DataQueryI;
import org.nees.illinois.replay.data.DataUpdateI;
import org.nees.illinois.replay.registries.ExperimentModule;
import org.nees.illinois.replay.restlet.HostInfo;
import org.nees.illinois.replay.test.data.TestDataQuery;
import org.nees.illinois.replay.test.data.TestDataUpdates;
import org.nees.illinois.replay.test.server.restlet.UriTestServerResource;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class UriTestModule extends AbstractModule {

	public UriTestModule() {
	}

	@Override
	protected void configure() {
		bind(String.class).annotatedWith(Names.named("appRoot")).toInstance(new String("/test1/data"));
		bind(Boolean.class).annotatedWith(Names.named("tracing")).toInstance(new Boolean(true));
		bind(Class.class).annotatedWith(Names.named("TableResource")).toInstance(UriTestServerResource.class);
		bind(Class.class).annotatedWith(Names.named("QueryResource")).toInstance(UriTestServerResource.class);
		bind(HostInfo.class).toInstance(new HostInfo(8111, "localhost",true));
		bind(DataUpdateI.class).to(TestDataUpdates.class);
		bind(DataQueryI.class).to(TestDataQuery.class);
		bind(ExperimentModule.class).to(TestServerModule.class);
	}

}
