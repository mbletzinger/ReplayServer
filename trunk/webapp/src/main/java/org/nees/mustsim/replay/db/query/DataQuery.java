package org.nees.mustsim.replay.db.query;

import java.util.List;

import org.nees.mustsim.replay.data.DataQueryI;
import org.nees.mustsim.replay.data.DoubleMatrix;
import org.nees.mustsim.replay.data.QueryRegistry;
import org.nees.mustsim.replay.data.RateType;
import org.nees.mustsim.replay.data.StepNumber;
import org.nees.mustsim.replay.db.DbConnections;
import org.nees.mustsim.replay.db.query.DbQueryStatements.QueryType;
import org.nees.mustsim.replay.db.statement.DbStatement;
import org.nees.mustsim.replay.db.statement.DbTableSpecs;

public class DataQuery implements DataQueryI {
	private final QueryRegistry contDbq = new QueryRegistry();
	private final DbConnections dbConnection;
	private final DbTableSpecs specs;
	private final QueryRegistry stepDbq = new QueryRegistry();

	public DataQuery(DbConnections dbConnection, DbTableSpecs specs) {
		super();
		this.dbConnection = dbConnection;
		this.specs = specs;
	}

	private DoubleMatrix doQuery(QueryType qtype, String name, double start,
			double stop) {
		DbStatement dbSt = dbConnection.createDbStatement();
		DbQuerySpec dbSpec;
		dbSpec = (DbQuerySpec) contDbq.getQuery(name);
		DbQueryStatements dbQuerySt = new DbQueryStatements(dbSt, dbSpec);
		return dbQuerySt.getData(qtype, start, stop);
	}

	private DoubleMatrix doQuery(QueryType qtype, String name, StepNumber start,
			StepNumber stop) {
		DbStatement dbSt = dbConnection.createDbStatement();
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

}
