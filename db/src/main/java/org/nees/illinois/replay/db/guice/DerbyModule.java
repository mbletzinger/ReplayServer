package org.nees.illinois.replay.db.guice;

import org.nees.illinois.replay.db.DbPoolFilters;
import org.nees.illinois.replay.db.DerbyFilters;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class DerbyModule extends AbstractModule {

	public DerbyModule() {
	}

	@Override
	protected void configure() {
		bind(String.class).annotatedWith(Names.named("dbDriver")).toInstance("org.apache.derby.jdbc.ClientDriver");
		bind(String.class).annotatedWith(Names.named("dbUrl")).toInstance("jdbc:derby://localhost:1527/");
		bind(String.class).annotatedWith(Names.named("dbLogon")).toInstance("NULL");
		bind(String.class).annotatedWith(Names.named("dbPasswd")).toInstance("NULL");
		bind(DbPoolFilters.class).to(DerbyFilters.class);

	}

}
