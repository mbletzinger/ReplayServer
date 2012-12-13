package org.nees.illinois.replay.db.query;

import java.util.List;

import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.data.StepNumber;
import org.nees.illinois.replay.db.DbPools;
import org.nees.illinois.replay.db.query.DbQueryStatements.QueryType;
import org.nees.illinois.replay.db.statement.DbStatement;
import org.nees.illinois.replay.db.statement.DbTableSpecs;
import org.nees.illinois.replay.queries.DataQueryI;
import org.nees.illinois.replay.queries.QueryRegistry;

public class DbDataQuery implements DataQueryI {
	private final QueryRegistry contDbq = new QueryRegistry();
	private final DbPools pools;
	private final DbTableSpecs specs;
	private final QueryRegistry stepDbq = new QueryRegistry();
	private String experiment;

	public DbDataQuery(DbPools pools, DbTableSpecs specs) {
		super();
		this.pools = pools;
		this.specs = specs;
	}

	private DoubleMatrix doQuery(QueryType qtype, String name, double start,
			double stop) {
		DbStatement dbSt = pools.createDbStatement(experiment);
		DbQuerySpec dbSpec;
		dbSpec = (DbQuerySpec) contDbq.getQuery(name);
		DbQueryStatements dbQuerySt = new DbQueryStatements(dbSt, dbSpec);
		return dbQuerySt.getData(qtype, start, stop);
	}

	private DoubleMatrix doQuery(QueryType qtype, String name,
			StepNumber start, StepNumber stop) {
		DbStatement dbSt = pools.createDbStatement(experiment);
		DbQuerySpec dbSpec;
		dbSpec = (DbQuerySpec) stepDbq.getQuery(name);
		DbQueryStatements dbQuerySt = new DbQueryStatements(dbSt, dbSpec);
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
		return stepDbq.getQuery(name) != null;
	}

	@Override
	public boolean setQuery(String name, List<String> channels) {
		DbQuerySpec dq = new DbQuerySpec(channels, name, specs, RateType.CONT);
		contDbq.setQuery(name, dq);
		dq = new DbQuerySpec(channels, name, specs, RateType.STEP);
		stepDbq.setQuery(name, dq);
		return true;
	}

	@Override
	public void setExperiment(String experiment) {
		this.experiment = experiment;
	}

	@Override
	public String getExperiment() {
		return experiment;
	}
}
