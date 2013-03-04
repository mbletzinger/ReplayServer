package org.nees.illinois.replay.restlet.guice;

import org.nees.illinois.replay.data.DataUpdateI;
import org.nees.illinois.replay.db.data.DbDataUpdates;
import org.nees.illinois.replay.db.statement.DbTablesMap;
import org.nees.illinois.replay.registries.ChannelNameManagement;
import org.nees.illinois.replay.registries.ExperimentModule;

import com.google.inject.name.Names;

public class DbTestsModule extends ExperimentModule {

	public DbTestsModule() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void configure() {
		bind(ChannelNameManagement.class).to(DbTablesMap.class);
		bind(String.class).annotatedWith(Names.named("experiment")).toInstance(getExperiment());
	}

}
