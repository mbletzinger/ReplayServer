package org.nees.illinois.replay.db.query;

import java.util.List;

import org.nees.illinois.replay.common.registries.ExperimentRegistries;
import org.nees.illinois.replay.common.registries.QueryDefiner;
import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.DoubleMatrixI;
import org.nees.illinois.replay.db.DbPools;
import org.nees.illinois.replay.subresource.DataQuerySubResourceI;

import com.google.inject.Inject;

/**
 * Class which implements the query subresource which queries for data in
 * various forms.
 * @author Michael Bletzinger
 */
public class DbDataQuerySubResources implements DataQuerySubResourceI {
	/**
	 * Database connections.
	 */
	private final DbPools pools;
	/**
	 * Global registries for an experiment.
	 */
	private ExperimentRegistries experiment;

	/**
	 * @param pools
	 *            Database connections.
	 */
	@Inject
	public DbDataQuerySubResources(final DbPools pools) {
		super();
		this.pools = pools;
	}

	// private DoubleMatrix doQuery(QueryType qtype, String name, double start,
	// double stop) {
	// StatementProcessor dbSt =
	// pools.createDbStatement(experiment.getExperiment(),false);
	// SavedQueryWTablesList dbSpec;
	// dbSpec = (SavedQueryWTablesList) experiment.getQueries().getQuery(name);
	// DbQueryRouter dbQuerySt = new DbQueryRouter(dbSt, dbSpec);
	// return dbQuerySt.getData(qtype, start, stop);
	// }
	//
	// private DoubleMatrix doQuery(QueryType qtype, String name,
	// StepNumber start, StepNumber stop) {
	// StatementProcessor dbSt =
	// pools.createDbStatement(experiment.getExperiment(),false);
	// SavedQueryWTablesList dbSpec;
	// dbSpec = (SavedQueryWTablesList) experiment.getQueries().getQuery(name);
	// DbQueryRouter dbQuerySt = new DbQueryRouter(dbSt, dbSpec);
	// return dbQuerySt.getData(qtype, start, stop);
	// }

	@Override
	public final DoubleMatrix doQuery(final String name, final double start,
			final double stop) {
		return doQuery(QueryType.ContWithStop, name, start, stop);
	}

	@Override
	public final DoubleMatrixI doQuery(final String name, final List<Double> times) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final ExperimentRegistries getExperiment() {
		return experiment;
	}

	@Override
	public final boolean isQuery(final String name) {
		return experiment.getQueries().getQuery(name) != null;
	}

	@Override
	public final void setExperiment(final ExperimentRegistries experiment) {
		this.experiment = experiment;
	}

	@Override
	public final boolean setQuery(final String name, final List<String> channels) {
		QueryDefiner qd = experiment.createQueryDefiner();
		qd.define(name, channels);
		return true;
	}
}
