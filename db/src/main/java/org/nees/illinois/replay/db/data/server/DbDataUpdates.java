package org.nees.illinois.replay.db.data.server;

import java.util.List;

import org.nees.illinois.replay.data.ChannelNameRegistry;
import org.nees.illinois.replay.data.DataUpdatesI;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.data.TableType;
import org.nees.illinois.replay.db.DbPools;
import org.nees.illinois.replay.db.statement.DataInsertStatement;
import org.nees.illinois.replay.db.statement.DbStatement;
import org.nees.illinois.replay.db.statement.DbTableSpecs;

import com.google.inject.Inject;

public class DbDataUpdates implements DataUpdatesI {

	/**
	 * @return the specs
	 */
	public DbTableSpecs getSpecs() {
		return specs;
	}

	private final DbPools pools;
	private final DbTableSpecs specs;

	@Inject
	public DbDataUpdates(DbPools pools, ChannelNameRegistry cnr) {
		super();
		this.pools = pools;
		this.specs = new DbTableSpecs(cnr, "");
	}

	@Override
	public boolean createTable(TableType table, List<String> channels) {
		boolean result = true;
		DbStatement dbSt = pools.createDbStatement(specs.getDbname());
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
		DbStatement dbSt = pools.createDbStatement(specs.getDbname());
		for (RateType r : RateType.values()) {
			dbSt.execute("DROP TABLE " + specs.tableName(table, r));
		}
		return result;
	}

	/**
	 * @param experiment
	 *            the experiment to set
	 */
	@Override
	public void setExperiment(String experiment) {
		this.specs.setDbname(experiment);
	}

	@Override
	public boolean update(TableType table, RateType rate, double[][] data) {
		boolean result = true;

		DbStatement dbSt = pools.createDbStatement(specs.getDbname());
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

	@Override
	public String getExperiment() {
		return specs.getDbname();
	}
}
