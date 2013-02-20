package org.nees.illinois.replay.test.db.utils;

import org.nees.illinois.replay.data.DataUpdateI;
import org.nees.illinois.replay.db.data.DbDataUpdates;
import org.nees.illinois.replay.db.guice.DerbyModule;
import org.nees.illinois.replay.db.guice.LocalTestMySqlModule;
import org.nees.illinois.replay.db.statement.DbTablesMap;
import org.nees.illinois.replay.registries.ChannelNameManagement;
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
			install(new LocalTestMySqlModule());
		} else {
			install(new DerbyModule());
		}
		bind(DataUpdateI.class).to(DbDataUpdates.class);
		bind(ChannelNameManagement.class).to(DbTablesMap.class);
	}

}
