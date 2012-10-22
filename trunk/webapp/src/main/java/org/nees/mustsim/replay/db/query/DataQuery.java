package org.nees.mustsim.replay.db.query;

import java.util.List;

import org.nees.mustsim.replay.data.DataQueryI;
import org.nees.mustsim.replay.data.DoubleMatrix;
import org.nees.mustsim.replay.data.RateType;
import org.nees.mustsim.replay.db.DbConnections;
import org.nees.mustsim.replay.db.query.DbQueryStatements.QueryType;
import org.nees.mustsim.replay.db.statement.DbStatement;
import org.nees.mustsim.replay.db.statement.DbTableSpecs;

public class DataQuery implements DataQueryI {
	private final DbQueryRegistry contDbq = new DbQueryRegistry();
	private final DbConnections dbConnection;
	private final DbTableSpecs specs;
	private final DbQueryRegistry stepDbq = new DbQueryRegistry();

	public DataQuery(DbConnections dbConnection, DbTableSpecs specs) {
		super();
		this.dbConnection = dbConnection;
		this.specs = specs;
	}

	private DoubleMatrix doQuery(QueryType qtype, String name, double start,
			double stop) {
		DbStatement dbSt = dbConnection.createDbStatement();
		DbQuerySpec dbSpec;
		if (qtype.equals(QueryType.Step)) {
			dbSpec = stepDbq.getQuery(name);
		} else {
			dbSpec = contDbq.getQuery(name);
		}
		DbQueryStatements dbQuerySt = new DbQueryStatements(dbSt, dbSpec);
		return dbQuerySt.getData(qtype, 0, start, stop);
	}

	@Override
	public DoubleMatrix doQuery(String name) {
		return doQuery(QueryType.Step, name, 0, 0);
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
