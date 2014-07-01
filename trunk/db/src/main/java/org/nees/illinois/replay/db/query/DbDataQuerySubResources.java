package org.nees.illinois.replay.db.query;

import java.sql.Connection;
import java.util.List;

import org.nees.illinois.replay.common.registries.ExperimentRegistries;
import org.nees.illinois.replay.common.registries.QueryDefiner;
import org.nees.illinois.replay.common.types.CompositeQueryI;
import org.nees.illinois.replay.common.types.TimeBoundsI;
import org.nees.illinois.replay.data.DoubleMatrixI;
import org.nees.illinois.replay.db.DbPools;
import org.nees.illinois.replay.db.events.EventQueries;
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

	@Override
	public final DoubleMatrixI doQuery(final String name, final TimeBoundsI tb) {
		CompositeQueryI query = experiment.getQueries().getQuery(name);
		Connection connection = pools.fetchConnection(experiment.getExperiment(), false);
		EventQueries equery = new EventQueries(connection, experiment);
		CompositeQueryExecutor exec = new CompositeQueryExecutor(query, connection, equery);
		return exec.execute(tb);
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
