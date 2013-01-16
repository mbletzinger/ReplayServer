package org.nees.illinois.replay.test.server.guice;

import org.nees.illinois.replay.data.DataQueryI;
import org.nees.illinois.replay.data.DataUpdateI;
import org.nees.illinois.replay.restlet.HostInfo;
import org.nees.illinois.replay.restlet.ReplayServerApplication;
import org.nees.illinois.replay.test.data.TestDataQuery;
import org.nees.illinois.replay.test.data.TestDataUpdates;
import org.nees.illinois.replay.test.server.restlet.LocalRestletTestApplication;

import com.google.inject.AbstractModule;

public class LocalRestletTestModule extends AbstractModule {

	public LocalRestletTestModule() {
	}

	@Override
	protected void configure() {
		bind(HostInfo.class).toInstance(new HostInfo(8111, "localhost",true));
		bind(ReplayServerApplication.class).to(LocalRestletTestApplication.class);
		bind(DataUpdateI.class).to(TestDataUpdates.class);
		bind(DataQueryI.class).to(TestDataQuery.class);
	}

}
