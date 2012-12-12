package org.nees.mustsim.replay.test.guice;

import org.nees.mustsim.replay.channels.ChannelNameRegistry;
import org.nees.mustsim.replay.data.DataUpdatesI;
import org.nees.mustsim.replay.queries.DataQueryI;
import org.nees.mustsim.replay.restlet.HostInfo;
import org.nees.mustsim.replay.restlet.ReplayServerApplication;
import org.nees.mustsim.replay.test.data.TestDataQuery;
import org.nees.mustsim.replay.test.data.TestDataUpdates;
import org.nees.mustsim.replay.test.restlet.LocalTestHostInfo;
import org.nees.mustsim.replay.test.restlet.UriTestApplication;

import com.google.inject.AbstractModule;

public class UriTestModule extends AbstractModule {

	public UriTestModule() {
	}

	@Override
	protected void configure() {
		bind(HostInfo.class).to(LocalTestHostInfo.class);
		bind(ReplayServerApplication.class).to(UriTestApplication.class);
		bind(DataUpdatesI.class).to(TestDataUpdates.class);
		bind(DataQueryI.class).to(TestDataQuery.class);
		bind(ChannelNameRegistry.class);
	}

}
