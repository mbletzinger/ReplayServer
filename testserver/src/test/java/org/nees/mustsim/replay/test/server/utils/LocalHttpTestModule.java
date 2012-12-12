package org.nees.mustsim.replay.test.server.utils;

import org.nees.mustsim.replay.channels.ChannelNameRegistry;
import org.nees.mustsim.replay.data.DataUpdatesI;
import org.nees.mustsim.replay.queries.DataQueryI;
import org.nees.mustsim.replay.restlet.HostInfo;
import org.nees.mustsim.replay.restlet.ReplayServerApplication;
import org.nees.mustsim.replay.test.data.TestDataQuery;
import org.nees.mustsim.replay.test.data.TestDataUpdates;

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
