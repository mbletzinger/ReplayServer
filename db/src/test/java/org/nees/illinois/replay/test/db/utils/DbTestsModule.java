package org.nees.illinois.replay.test.db.utils;

import org.nees.illinois.replay.db.guice.DerbyModule;
import org.nees.illinois.replay.db.guice.LocalTestMySqlModule;

import com.google.inject.AbstractModule;

/**
 * Class which inject the appropriate database for testing.
 * @author Michael Bletzinger
 */
public class DbTestsModule extends AbstractModule {
	/**
	 * Database label.
	 */
	private final String db;

	/**
	 * @param db
	 *            Database label.
	 */
	public DbTestsModule(final String db) {
		this.db = db;
	}

	@Override
	protected final void configure() {
		if (db.equals("mysql")) {
			install(new LocalTestMySqlModule());
		} else {
			install(new DerbyModule());
		}
	}

}
