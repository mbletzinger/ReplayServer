package org.nees.illinois.replay.db.guice;

import org.nees.illinois.replay.db.DbPoolFilters;
import org.nees.illinois.replay.db.MySqlFilters;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class MySqlModule extends AbstractModule {

	public MySqlModule() {
	}

	@Override
	protected void configure() {
		bind(String.class).annotatedWith(Names.named("dbDriver")).toInstance("com.mysql.jdbc.Driver");
		bind(String.class).annotatedWith(Names.named("dbUrl")).toInstance("jdbc:mysql://localhost:3306/");
		bind(String.class).annotatedWith(Names.named("dbLogon")).toInstance("replay");
		bind(String.class).annotatedWith(Names.named("dbPasswd")).toInstance("nees@mustsim08");
		bind(DbPoolFilters.class).to(MySqlFilters.class);

	}

}
