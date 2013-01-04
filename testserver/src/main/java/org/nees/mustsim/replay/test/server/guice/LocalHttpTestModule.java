package org.nees.mustsim.replay.test.server.guice;

import org.nees.illinois.replay.data.ChannelNameRegistry;
import org.nees.illinois.replay.data.DataUpdatesI;
import org.nees.illinois.replay.queries.DataQueryI;
import org.nees.illinois.replay.restlet.HostInfo;
import org.nees.illinois.replay.restlet.ReplayServerApplication;
import org.nees.mustsim.replay.test.data.TestDataQuery;
import org.nees.mustsim.replay.test.data.TestDataUpdates;
import org.nees.mustsim.replay.test.server.restlet.LocalHttpTestApplication;
import org.nees.mustsim.replay.test.server.restlet.LocalTestHostInfo;

import com.google.inject.AbstractModule;

public class LocalHttpTestModule extends AbstractModule {

	public LocalHttpTestModule() {
	}

	@Override
	protected void configure() {
		bind(HostInfo.class).to(LocalTestHostInfo.class);
		bind(ReplayServerApplication.class).to(LocalHttpTestApplication.class);
		bind(DataUpdatesI.class).to(TestDataUpdates.class);
		bind(DataQueryI.class).to(TestDataQuery.class);
		bind(ChannelNameRegistry.class);
	}

}
