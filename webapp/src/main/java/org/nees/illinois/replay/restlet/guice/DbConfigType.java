package org.nees.illinois.replay.restlet.guice;

import org.nees.illinois.replay.db.guice.DerbyModule;
import org.nees.illinois.replay.db.guice.LocalTestMySqlModule;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;

public enum DbConfigType {
	LocalTestDerby,
	LocalTestMySql,
	RemoteMySql,
	ProductionMySql;
	public void configure(Binder builder) {
		AbstractModule dbMod;
		if(this.ordinal() == 0) {
			dbMod = new DerbyModule();
		} else if(this.ordinal() == 1) {
			dbMod = new LocalTestMySqlModule();
		} else {
			dbMod = new ProductionMySqlModule();
		}
		dbMod.configure(builder);
	}
}
