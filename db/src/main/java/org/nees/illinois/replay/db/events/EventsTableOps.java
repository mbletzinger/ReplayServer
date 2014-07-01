package org.nees.illinois.replay.db.events;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.nees.illinois.replay.common.registries.ExperimentRegistries;
import org.nees.illinois.replay.db.DbPools;
import org.nees.illinois.replay.db.statement.EventInsertStatement;
import org.nees.illinois.replay.db.statement.StatementProcessor;
import org.nees.illinois.replay.events.EventI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages the events table for an experiment database.
 * @author Michael Bletzinger
 */
public class EventsTableOps {
	/**
	 * Experiment scoped registries.
	 */
	private ExperimentRegistries er;

	/**
	 * Logger.
	 **/
	private final Logger log = LoggerFactory.getLogger(EventsTableOps.class);

	/**
	 * Database connection pools.
	 */
	private final DbPools pools;
	/**
	 * @param pools
	 *            Database connection pools.
	 * @param er
	 *            Name of the experiment.
	 */
	public EventsTableOps(final DbPools pools, final ExperimentRegistries er) {
		this.pools = pools;
		this.er = er;

	}
	/**
	 * Add an event to the event table.
	 * @param event
	 *            Event to add.
	 */
	public final void add(final EventI event) {
		EventInsertStatement eis = new EventInsertStatement(
				pools.fetchConnection(er.getExperiment(), false), er.getEventTableName());
		eis.add(event);
		eis.getBuilder().execute();
	}
	/**
	 * Create the event table.
	 * @return true if successful.
	 */
	public final boolean create() {
		final String createTable = "CREATE TABLE " + er.getEventTableName() + "("
				+ "TIME DOUBLE NOT NULL," + " NAME VARCHAR(200) NOT NULL,"
				+ " DESCRIPTION VARCHAR(2000),"
				+ " SOURCE VARCHAR(200) NOT NULL)";
		StatementProcessor statement = pools
				.createDbStatement(er.getExperiment(), true);
		boolean result = statement.execute(createTable);
		statement.close();
		return result;
	}

	/**
	 * Check to see if the event table has been set up.
	 * @return The table exists in the database.
	 */
	public final boolean exists() {
		DatabaseMetaData databaseMetaData = null;
		try {
			databaseMetaData = pools.fetchConnection(er.getExperiment(), false)
					.getMetaData();
		} catch (SQLException e1) {
			log.error("Metadata query failed ", e1);
			return false;
		}
		ResultSet result = null;
		try {
			result = databaseMetaData.getTables(null, null, null, null);
		} catch (SQLException e1) {
			log.error("Metadata query failed ", e1);
			return false;
		}
		boolean found = false;
		try {
			while (result.next()) {
				final int nameColumn = 3;
				String tableName = result.getString(nameColumn);
				log.debug("Checking table name " + tableName);
				if (tableName.equals(er.getEventTableName())) {
					found = true;
				}
			}
		} catch (SQLException e1) {
			log.error("Metadata query failed ", e1);
			return false;
		}
		return found;
	}

	/**
	 * @return the er
	 */
	public final ExperimentRegistries getEr() {
		return er;
	}

	/**
	 * @return the pools
	 */
	public final DbPools getPools() {
		return pools;
	}

	/**
	 * @return a new instance of {@link EventQueries}.
	 */
	public final EventQueries getQueries() {
		return new EventQueries(pools.fetchConnection(er.getExperiment(), false),
				er);
	}
	/**
	 * Remove table.
	 * @return True if successful.
	 */
	public final boolean remove() {
		StatementProcessor statement = pools
				.createDbStatement(er.getExperiment(), true);
		boolean result = statement.execute("DROP TABLE " + er.getEventTableName());
		statement.close();
		return result;
	}
	/**
	 * @param er the er to set
	 */
	public final void setEr(final ExperimentRegistries er) {
		this.er = er;
	}
}
