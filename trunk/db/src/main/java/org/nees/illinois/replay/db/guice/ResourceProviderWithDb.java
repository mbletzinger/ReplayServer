package org.nees.illinois.replay.db.guice;

import org.nees.illinois.replay.common.registries.ExperimentModuleDeleteMe;
import org.nees.illinois.replay.db.DbPools;

public abstract class ResourceProviderWithDb extends ExperimentModuleDeleteMe {

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
