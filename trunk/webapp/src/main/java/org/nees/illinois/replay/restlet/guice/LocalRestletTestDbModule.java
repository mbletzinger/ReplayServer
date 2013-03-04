package org.nees.illinois.replay.restlet.guice;

import org.nees.illinois.replay.data.DataQueryI;
import org.nees.illinois.replay.data.DataUpdateI;
import org.nees.illinois.replay.db.data.DbDataUpdates;
import org.nees.illinois.replay.db.query.DbDataQuery;
import org.nees.illinois.replay.registries.ExperimentModule;
import org.nees.illinois.replay.restlet.DataQueryServerResource;
import org.nees.illinois.replay.restlet.DataTableServerResource;
import org.nees.illinois.replay.restlet.HostInfo;
import org.nees.illinois.replay.restlet.ReplayServerApplication;
import org.nees.illinois.replay.test.server.restlet.LocalRestletTestApplication;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class LocalRestletTestDbModule extends AbstractModule {
	private final DbConfigType dbType;

	public LocalRestletTestDbModule(DbConfigType dbType) {
		this.dbType = dbType;
	}

	@Override
	protected void configure() {
		dbType.configure(this.binder());
		boolean tracing  = dbType.equals(DbConfigType.ProductionMySql) ? false : true;
		bind(String.class).annotatedWith(Names.named("appRoot")).toInstance(new String("/data"));
		bind(Boolean.class).annotatedWith(Names.named("tracing")).toInstance(new Boolean(tracing));
		bind(Class.class).annotatedWith(Names.named("TableResource")).toInstance(DataTableServerResource.class);
		bind(Class.class).annotatedWith(Names.named("QueryResource")).toInstance(DataQueryServerResource.class);
		bind(HostInfo.class).toInstance(new HostInfo(8111, "localhost",true));
		bind(ReplayServerApplication.class).to(LocalRestletTestApplication.class);
		bind(DataUpdateI.class).to(DbDataUpdates.class);
		bind(DataQueryI.class).to(DbDataQuery.class);
		bind(ExperimentModule.class).to(DbTestsModule.class);
	}

}
