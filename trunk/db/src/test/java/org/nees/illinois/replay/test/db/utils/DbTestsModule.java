package org.nees.illinois.replay.test.db.utils;

import org.nees.illinois.replay.data.DataUpdateI;
import org.nees.illinois.replay.db.data.DbDataUpdates;
import org.nees.illinois.replay.db.guice.DerbyModule;
import org.nees.illinois.replay.db.guice.MySqlModule;
import org.nees.illinois.replay.db.statement.DbTableSpecs;
import org.nees.illinois.replay.registries.ChannelLookups;
import org.nees.illinois.replay.registries.ExperimentModule;

import com.google.inject.name.Names;

public class DbTestsModule extends ExperimentModule {

	private final String db;
	
	public DbTestsModule(String db) {
		this.db = db;
		setExperiment("Dummy1");
	}

	@Override
	protected void configure() {
		if(db.equals("mysql")) {
			install(new MySqlModule());
		} else {
			install(new DerbyModule());
		}
		bind(DataUpdateI.class).to(DbDataUpdates.class);
		bind(ChannelLookups.class).to(DbTableSpecs.class);
		bind(String.class).annotatedWith(Names.named("experiment")).toInstance(getExperiment());
	}

}
