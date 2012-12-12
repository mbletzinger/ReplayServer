package org.nees.illinois.replay.db.data.server;

import java.util.List;

import org.nees.illinois.replay.data.DataUpdatesI;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.data.TableType;
import org.nees.illinois.replay.db.DbConnections;
import org.nees.illinois.replay.db.statement.DataInsertStatement;
import org.nees.illinois.replay.db.statement.DbStatement;
import org.nees.illinois.replay.db.statement.DbTableSpecs;

public class DbDataUpdates implements DataUpdatesI {
	private final DbConnections connection;
	private final DbTableSpecs specs;

	public DbDataUpdates(DbConnections connection, DbTableSpecs specs) {
		super();
		this.connection = connection;
		this.specs = specs;
	}

	@Override
	public boolean createTable(TableType table, List<String> channels) {
		boolean result = true;
		DbStatement dbSt = connection.createDbStatement();
		specs.addTable(table, channels);
		for (RateType r : RateType.values()) {
			String statement = specs.createTableStatement(table, r);
			result = result && dbSt.execute(statement);
		}
		return result;
	}

	@Override
	public boolean removeTable(TableType table) {
		boolean result = true;
		DbStatement dbSt = connection.createDbStatement();
		for (RateType r : RateType.values()) {
			dbSt.execute("DROP TABLE " + specs.tableName(table, r));
		}
		return result;
	}

	@Override
	public boolean update(TableType table, RateType rate, double[][] data) {
		boolean result = true;
		DbStatement dbSt = connection.createDbStatement();
		DataInsertStatement prep = DataInsertStatement.getStatement(
				specs.tableName(table, rate), data[0].length);
		result = dbSt.createPrepStatement(prep);
		if (result == false) {
			return result;
		}
		for (int r = 0; r < data.length; r++) {
			prep.add(data[r]);
		}
		int[] records = prep.execute();
		result = records.length > 0;
		return result;
	}
}
