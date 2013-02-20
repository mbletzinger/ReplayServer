package org.nees.illinois.replay.db.guice;

import org.nees.illinois.replay.db.DbPools;
import org.nees.illinois.replay.registries.ExperimentModule;

public abstract class ResourceProviderWithDb extends ExperimentModule {

	private final DbPools pools;

	public ResourceProviderWithDb(DbPools pools) {
		this.pools = pools;
		setExperiment("dummyExperiment");
	}

	/**
	 * @return the pools
	 */
	public DbPools getPools() {
		return pools;
	}

}
