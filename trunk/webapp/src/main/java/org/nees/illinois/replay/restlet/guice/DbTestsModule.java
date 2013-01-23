package org.nees.illinois.replay.restlet.guice;

import org.nees.illinois.replay.data.DataUpdateI;
import org.nees.illinois.replay.db.DbPools;
import org.nees.illinois.replay.db.DerbyPools;
import org.nees.illinois.replay.db.data.DbDataUpdates;
import org.nees.illinois.replay.db.statement.DbTableSpecs;
import org.nees.illinois.replay.registries.ChannelLookups;
import org.nees.illinois.replay.registries.ExperimentModule;

import com.google.inject.name.Names;

public class DbTestsModule extends ExperimentModule {

	public DbTestsModule() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void configure() {
		bind(DataUpdateI.class).to(DbDataUpdates.class);
		bind(String.class).annotatedWith(Names.named("dbname")).toInstance(getExperiment());
		bind(ChannelLookups.class).to(DbTableSpecs.class);
		bind(DbPools.class).to(DerbyPools.class);
		bind(String.class).annotatedWith(Names.named("experiment")).toInstance(getExperiment());
	}

}
