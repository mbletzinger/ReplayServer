package org.nees.illinois.replay.restlet.guice;

import org.nees.illinois.replay.data.ChannelNameRegistry;
import org.nees.illinois.replay.data.DataQueryI;
import org.nees.illinois.replay.data.DataUpdateI;
import org.nees.illinois.replay.db.DbPools;
import org.nees.illinois.replay.db.DerbyPools;
import org.nees.illinois.replay.db.data.DbDataUpdates;
import org.nees.illinois.replay.db.query.DbDataQuery;
import org.nees.illinois.replay.restlet.HostInfo;
import org.nees.illinois.replay.restlet.ReplayServerApplication;
import org.nees.mustsim.replay.test.server.restlet.LocalRestletTestApplication;
import org.nees.mustsim.replay.test.server.restlet.LocalTestHostInfo;

import com.google.inject.AbstractModule;

public class LocalRestletTestDbModule extends AbstractModule {

	public LocalRestletTestDbModule() {
	}

	@Override
	protected void configure() {
		bind(HostInfo.class).to(LocalTestHostInfo.class);
		bind(ReplayServerApplication.class).to(LocalRestletTestApplication.class);
		bind(DataUpdateI.class).to(DbDataUpdates.class);
		bind(DataQueryI.class).to(DbDataQuery.class);
		bind(ChannelNameRegistry.class);
		bind(DbPools.class).to(DerbyPools.class);
	}

}
