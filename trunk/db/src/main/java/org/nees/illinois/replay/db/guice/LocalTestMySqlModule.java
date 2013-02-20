package org.nees.illinois.replay.db.guice;

import org.nees.illinois.replay.db.DbOperationsI;
import org.nees.illinois.replay.db.MySqlDbOps;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class LocalTestMySqlModule extends AbstractModule {

	public LocalTestMySqlModule() {
	}

	@Override
	protected void configure() {
		bind(String.class).annotatedWith(Names.named("dbDriver")).toInstance("com.mysql.jdbc.Driver");
		bind(String.class).annotatedWith(Names.named("dbUrl")).toInstance("jdbc:mysql://localhost:3306/");
		bind(String.class).annotatedWith(Names.named("dbLogon")).toInstance("replay");
		bind(String.class).annotatedWith(Names.named("dbPasswd")).toInstance("nees@mustsim08");
		bind(String.class).annotatedWith(Names.named("experiment")).toInstance("dummyExperiment");
		bind(DbOperationsI.class).to(MySqlDbOps.class);

	}

}
