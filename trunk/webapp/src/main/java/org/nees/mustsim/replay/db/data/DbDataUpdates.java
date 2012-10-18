package org.nees.mustsim.replay.db.data;

import java.util.List;

import org.nees.mustsim.replay.db.statement.DataInsertStatement;
import org.nees.mustsim.replay.db.statement.DbStatement;
import org.nees.mustsim.replay.db.statement.DbTableSpecs;
import org.nees.mustsim.replay.db.statement.RateType;
import org.nees.mustsim.replay.db.statement.TableType;

public class DbDataUpdates {
	private final DbStatement dbSt;
	private final DbTableSpecs specs;

	public DbDataUpdates(DbStatement dbSt, DbTableSpecs create) {
		super();
		this.dbSt = dbSt;
		this.specs = create;
	}

	public boolean createTable(TableType table, List<String> channels) {
		boolean result = true;
		specs.addTable(table, channels);
		for (RateType r : RateType.values()) {
			String statement = specs.createTableStatement(table, r);
			result = result && dbSt.execute(statement);
		}
		return result;
	}

	public boolean removeTable(TableType table) {
		boolean result = true;
		for (RateType r : RateType.values()) {
			dbSt.execute("DROP TABLE " + specs.tableName(table, r));
		}
		return result;
	}

	public boolean update(TableType table, RateType rate, double[][] data) {
		boolean result = true;
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