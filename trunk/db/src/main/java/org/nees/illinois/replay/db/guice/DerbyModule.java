package org.nees.illinois.replay.db.guice;

import org.nees.illinois.replay.db.DbOperationsI;
import org.nees.illinois.replay.db.DerbyDbOps;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

/**
 * Adds derby DB specific parameters to the configuration.
 * @author Michael Bletzinger
 */
public class DerbyModule extends AbstractModule {
	/**
	 * Empty constructor.
	 */
	public DerbyModule() {
	}

	/**
	 * Adds the additional configuration parameters for the Derby database.
	 */
	@Override
	protected final void configure() {
		bind(String.class).annotatedWith(Names.named("dbDriver")).toInstance(
				"org.apache.derby.jdbc.ClientDriver");
		bind(String.class).annotatedWith(Names.named("dbUrl")).toInstance(
				"jdbc:derby://localhost:1527/");
		bind(String.class).annotatedWith(Names.named("dbLogon")).toInstance(
				"foo");
		bind(String.class).annotatedWith(Names.named("dbPasswd")).toInstance(
				"bar");
		bind(String.class).annotatedWith(Names.named("experiment")).toInstance(
				"dummyExperiment");
		bind(DbOperationsI.class).to(DerbyDbOps.class);
	}

}
