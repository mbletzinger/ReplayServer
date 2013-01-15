package org.nees.illinois.replay.db.data;

import java.util.List;

import org.nees.illinois.replay.data.DataUpdateI;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.data.TableType;
import org.nees.illinois.replay.db.DbPools;
import org.nees.illinois.replay.db.statement.DataInsertStatement;
import org.nees.illinois.replay.db.statement.DbStatement;
import org.nees.illinois.replay.db.statement.DbTableSpecs;
import org.nees.illinois.replay.registries.ChannelLookups;
import org.nees.illinois.replay.registries.ExperimentRegistries;

import com.google.inject.Inject;

public class DbDataUpdates implements DataUpdateI {

	private ExperimentRegistries er;

	private final DbPools pools;

	private  DbTableSpecs specs;
	@Inject
	public DbDataUpdates(DbPools pools) {
		super();
		this.pools = pools;
	}
	
	@Override
	public boolean createTable(TableType table, List<String> channels) {
		boolean result = true;
		specs = (DbTableSpecs) er.getLookups();
		DbStatement dbSt = pools.createDbStatement(specs.getDbname());
		specs.addTable(table, channels);
		for (RateType r : RateType.values()) {
			String statement = specs.createTableStatement(table, r);
			result = result && dbSt.execute(statement);
		}
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
	public DbTableSpecs getSpecs() {
		return specs;
	}

	@Override
	public boolean removeTable(TableType table) {
		boolean result = true;
		this.specs = (DbTableSpecs) er.getLookupsClone();
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
	public void setExperiment(ExperimentRegistries experiment) {
		this.er = experiment;
	}

	@Override
	public boolean update(TableType table, RateType rate, double[][] data) {
		boolean result = true;
		this.specs = (DbTableSpecs) er.getLookupsClone();
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
}
