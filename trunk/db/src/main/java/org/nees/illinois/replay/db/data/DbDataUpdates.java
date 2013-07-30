package org.nees.illinois.replay.db.data;

import java.util.List;

import org.nees.illinois.replay.common.registries.ExperimentRegistries;
import org.nees.illinois.replay.common.registries.TableDefiner;
import org.nees.illinois.replay.common.registries.TableType;
import org.nees.illinois.replay.common.types.TableDefinitionI;
import org.nees.illinois.replay.common.types.TableIdentityI;
import org.nees.illinois.replay.db.DbPools;
import org.nees.illinois.replay.db.statement.DataInsertStatement;
import org.nees.illinois.replay.db.statement.StatementProcessor;
import org.nees.illinois.replay.subresource.DataUpdateSubResourceI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

/**
 * Class which implements the {@link DataUpdateSubResourceI} with a database
 * back end. The database pool connections are injected. The experiment
 * registries are set per request.
 * @author Michael Bletzinger
 */
public class DbDataUpdates implements DataUpdateSubResourceI {
	/**
	 * Experiment scoped registries.
	 */
	private ExperimentRegistries er;
	/**
	 * Database connection pools.
	 */
	private final DbPools pools;
	/**
	 * Logger.
	 **/
	private final Logger log = LoggerFactory.getLogger(DbDataUpdates.class);

	/**
	 * @param pools
	 *            Database connection pools.
	 */
	@Inject
	public DbDataUpdates(final DbPools pools) {
		super();
		this.pools = pools;
	}

	@Override
	public final TableIdentityI createTable(final String name,
			final TableType table, final List<String> channels) {
		if (er == null) {
			log.error("Need to set the experiment before creating a table");
			return null;
		}
		TableDefiner def = er.createTableDefiner();
		StatementProcessor dbSt = pools.createDbStatement(er.getExperiment(),
				true);
		TableDefinitionI td = def.define(name, table, channels);
		DataTableOps dtc = new DataTableOps(td, dbSt);
		boolean success = dtc.create();
		return (success ? td.getTableId() : null);
	}

	@Override
	public final ExperimentRegistries getExperiment() {
		return er;
	}

	/**
	 * @return the pools
	 */
	public final DbPools getPools() {
		return pools;
	}

	@Override
	public final boolean removeTable(final TableIdentityI table) {
		boolean result = true;
		StatementProcessor dbSt = pools.createDbStatement(er.getExperiment(),
				false);
		TableDefinitionI td = er.getTableDefs()
				.getTable(table.getDatasetName());
		DataTableOps dtc = new DataTableOps(td, dbSt);
		result = dtc.remove();
		return result;
	}

	@Override
	public final void setExperiment(final ExperimentRegistries experiment) {
		this.er = experiment;
	}

	@Override
	public final boolean update(final String tableString, final double[][] data) {
		TableIdentityI tid = er.getTableIds().getId(tableString);
		DataInsertStatement prep = DataInsertStatement.getStatement(
				pools.fetchConnection(er.getExperiment(), false),
				tid.getDbName(), data[0].length);
		for (int r = 0; r < data.length; r++) {
			prep.add(data[r]);
		}
		int[] records = prep.getBuilder().execute();
		return records.length > 0;
	}
}
