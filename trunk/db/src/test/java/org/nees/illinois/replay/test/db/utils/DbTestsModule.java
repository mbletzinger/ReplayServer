package org.nees.illinois.replay.test.db.utils;

import org.nees.illinois.replay.data.DataUpdateI;
import org.nees.illinois.replay.db.DbPools;
import org.nees.illinois.replay.db.DerbyPools;
import org.nees.illinois.replay.db.MySQLPools;
import org.nees.illinois.replay.db.data.DbDataUpdates;
import org.nees.illinois.replay.db.statement.DbTableSpecs;
import org.nees.illinois.replay.registries.ChannelLookups;
import org.nees.illinois.replay.registries.ExperimentModule;

import com.google.inject.name.Names;

public class DbTestsModule extends ExperimentModule {

	private final String db;
	
	public DbTestsModule(String db) {
		this.db = db;
	}

	@Override
	protected void configure() {
		bind(DataUpdateI.class).to(DbDataUpdates.class);
		bind(String.class).annotatedWith(Names.named("dbname")).toInstance(getExperiment());
		bind(ChannelLookups.class).to(DbTableSpecs.class);
		if(db.equals("mysql")) {
			bind(DbPools.class).to(MySQLPools.class);
		} else {
			bind(DbPools.class).to(DerbyPools.class);
		}
		bind(String.class).annotatedWith(Names.named("experiment")).toInstance(getExperiment());
	}

}
