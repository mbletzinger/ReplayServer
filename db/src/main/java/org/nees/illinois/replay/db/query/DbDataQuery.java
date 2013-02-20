package org.nees.illinois.replay.db.query;

import java.util.List;

import org.nees.illinois.replay.data.DataQueryI;
import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.data.StepNumber;
import org.nees.illinois.replay.db.DbPools;
import org.nees.illinois.replay.db.query.DbQueryProcessor.QueryType;
import org.nees.illinois.replay.db.statement.StatementProcessor;
import org.nees.illinois.replay.db.statement.DbTablesMap;
import org.nees.illinois.replay.registries.ExperimentRegistries;

import com.google.inject.Inject;

public class DbDataQuery implements DataQueryI {

	private final DbPools pools;
	private ExperimentRegistries experiment;

	@Inject
	public DbDataQuery(DbPools pools) {
		super();
		this.pools = pools;
	}

	private DoubleMatrix doQuery(QueryType qtype, String name, double start,
			double stop) {
		StatementProcessor dbSt = pools.createDbStatement(experiment.getExperiment(),false);
		TableQueriesList dbSpec;
		dbSpec = (TableQueriesList) experiment.getQueries().getQuery(name, RateType.CONT);
		DbQueryProcessor dbQuerySt = new DbQueryProcessor(dbSt, dbSpec);
		return dbQuerySt.getData(qtype, start, stop);
	}

	private DoubleMatrix doQuery(QueryType qtype, String name,
			StepNumber start, StepNumber stop) {
		StatementProcessor dbSt = pools.createDbStatement(experiment.getExperiment(),false);
		TableQueriesList dbSpec;
		dbSpec = (TableQueriesList) experiment.getQueries().getQuery(name, RateType.STEP);
		DbQueryProcessor dbQuerySt = new DbQueryProcessor(dbSt, dbSpec);
		return dbQuerySt.getData(qtype, start, stop);
	}

	@Override
	public DoubleMatrix doQuery(String name) {
		return doQuery(QueryType.Step, name, null, null);
	}

	@Override
	public DoubleMatrix doQuery(String name, double start) {
		return doQuery(QueryType.Cont, name, start, 0);
	}

	@Override
	public DoubleMatrix doQuery(String name, double start, double stop) {
		return doQuery(QueryType.ContWithStop, name, start, stop);
	}

	@Override
	public DoubleMatrix doQuery(String name, StepNumber start) {
		return doQuery(QueryType.StepWithStart, name, start, null);
	}

	@Override
	public DoubleMatrix doQuery(String name, StepNumber start, StepNumber stop) {
		return doQuery(QueryType.StepWithStop, name, start, stop);
	}

	@Override
	public boolean isQuery(String name) {
		return experiment.getQueries().getQuery(name, null) != null;
	}

	@Override
	public boolean setQuery(String name, List<String> channels) {
		// DbTableSpecs is getting a clone because query tables are either added or removed; never changed.
		DbTablesMap specs = (DbTablesMap) experiment.getChnlNamesMgmt().clone();
		TableQueriesList dq = new TableQueriesList(channels, name, specs, RateType.CONT);
		experiment.getQueries().setQuery(name, RateType.CONT, dq);
		dq = new TableQueriesList(channels, name, specs, RateType.STEP);
		experiment.getQueries().setQuery(name, RateType.STEP, dq);
		return true;
	}

	@Override
	public void setExperiment(ExperimentRegistries experiment) {
		this.experiment = experiment;
	}

	@Override
	public ExperimentRegistries getExperiment() {
		return experiment;
	}
}
