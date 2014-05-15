package org.nees.illinois.replay.db.query;

import java.util.List;

import org.nees.illinois.replay.common.registries.ExperimentRegistries;
import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.DoubleMatrixI;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.db.DbPools;
import org.nees.illinois.replay.db.query.old.SavedQueryWTablesList;
import org.nees.illinois.replay.db.query.old.DbQueryRouter.QueryType;
import org.nees.illinois.replay.subresource.DataQuerySubResourceI;

import com.google.inject.Inject;

/**
 * Class which implements the query subresource which queries for data in
 * various forms.
 * @author Michael Bletzinger
 */
public class DbDataQuery implements DataQuerySubResourceI {
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
	public DbDataQuery(final DbPools pools) {
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
	public final DoubleMatrix doQuery(String name, final double start,
			final double stop) {
		return doQuery(QueryType.ContWithStop, name, start, stop);
	}

	@Override
	public final boolean isQuery(final String name) {
		return experiment.getQueries().getQuery(name) != null;
	}

	@Override
	public final boolean setQuery(final String name, final List<String> channels) {
		// DbTableSpecs is getting a clone because query tables are either added
		// or removed; never changed.
		DbTablesMap specs = (DbTablesMap) experiment.getChnlNamesMgmt().clone();
		SavedQueryWTablesList dq = new SavedQueryWTablesList(channels, name,
				specs, RateType.CONT);
		experiment.getQueries().setQuery(name, dq);
		dq = new SavedQueryWTablesList(channels, name, specs, RateType.STEP);
		experiment.getQueries().setQuery(name, dq);
		return true;
	}

	@Override
	public final void setExperiment(final ExperimentRegistries experiment) {
		this.experiment = experiment;
	}

	@Override
	public final ExperimentRegistries getExperiment() {
		return experiment;
	}

	@Override
	public final DoubleMatrixI doQuery(final String name, final List<Double> times) {
		// TODO Auto-generated method stub
		return null;
	}
}
