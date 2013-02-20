package org.nees.illinois.replay.db.data;

import java.util.List;

import org.nees.illinois.replay.data.DataUpdateI;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.data.TableType;
import org.nees.illinois.replay.db.DbPools;
import org.nees.illinois.replay.db.statement.DataInsertStatement;
import org.nees.illinois.replay.db.statement.StatementProcessor;
import org.nees.illinois.replay.db.statement.DbTablesMap;
import org.nees.illinois.replay.registries.ExperimentRegistries;

import com.google.inject.Inject;

public class DbDataUpdates implements DataUpdateI {

	private ExperimentRegistries er;

	private final DbPools pools;

	private  DbTablesMap specs;
	@Inject
	public DbDataUpdates(DbPools pools) {
		super();
		this.pools = pools;
	}
	
	@Override
	public boolean createTable(TableType table, List<String> channels) {
		boolean result = true;
		specs = (DbTablesMap) er.getChnlNamesMgmt();
		StatementProcessor dbSt = pools.createDbStatement(specs.getDbname(), true);
		specs.addTable(table, channels);
		for (RateType r : RateType.values()) {
			String statement = specs.createTableStatement(table, r);
			result = result && dbSt.execute(statement);
		}
		dbSt.close();
		return result;
	}

	@Override
	public ExperimentRegistries getExperiment() {
		return er;
	}

	/**
	 * @return the pools
	 */
	public synchronized DbPools getPools() {
		return pools;
	}

	/**
	 * @return the specs
	 */
	public DbTablesMap getSpecs() {
		return specs;
	}

	@Override
	public boolean removeTable(TableType table) {
		boolean result = true;
		this.specs = (DbTablesMap) er.getLookupsClone();
		StatementProcessor dbSt = pools.createDbStatement(specs.getDbname(),false);
		for (RateType r : RateType.values()) {
			dbSt.execute("DROP TABLE " + specs.tableName(table, r));
		}
		dbSt.close();
		return result;
	}

	/**
	 * @param experiment
	 *            the experiment to set
	 */
	@Override
	public void setExperiment(ExperimentRegistries experiment) {
		this.er = experiment;
	}

	@Override
	public boolean update(TableType table, RateType rate, double[][] data) {
		boolean result = true;
		this.specs = (DbTablesMap) er.getLookupsClone();
		StatementProcessor dbSt = pools.createDbStatement(specs.getDbname(),false);
		DataInsertStatement prep = DataInsertStatement.getStatement(
				specs.tableName(table, rate), data[0].length);
		result = dbSt.createPrepStatement(prep);
		if (result == false) {
			dbSt.close();
			return result;
		}
		for (int r = 0; r < data.length; r++) {
			prep.add(data[r]);
		}
		int[] records = prep.execute();
		result = records.length > 0;
		dbSt.close();
		return result;
	}
}
