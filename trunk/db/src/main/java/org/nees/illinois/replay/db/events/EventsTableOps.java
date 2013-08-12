package org.nees.illinois.replay.db.events;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

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
	 * @return the eventTableName
	 */
	public final String getEventTableName() {
		return eventTableName;
	}

	/**
	 * @return the experiment
	 */
	public final String getExperiment() {
		return experiment;
	}

	/**
	 * @return the pools
	 */
	public final DbPools getPools() {
		return pools;
	}

	/**
	 * Name of the events table.
	 */
	private final String eventTableName = "EXPERIMENT_EVENTS";
	/**
	 * Name of the experiment.
	 */
	private final String experiment;
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
	 * @param experiment
	 *            Name of the experiment.
	 */
	public EventsTableOps(final DbPools pools, final String experiment) {
		this.pools = pools;
		this.experiment = experiment;
	}

	/**
	 * Add an event to the event table.
	 * @param event
	 *            Event to add.
	 */
	public final void add(final EventI event) {
		EventInsertStatement eis = new EventInsertStatement(
				pools.fetchConnection(experiment, false), eventTableName);
		eis.add(event);
		eis.getBuilder().execute();
	}

	/**
	 * Create the event table.
	 * @return true if successful.
	 */
	public final boolean create() {
		final String createTable = "CREATE TABLE " + eventTableName + "("
				+ "TIME DOUBLE NOT NULL," + " NAME VARCHAR(200) NOT NULL,"
				+ " TYPE VARCHAR(50) NOT NULL,"
				+ " SOURCE VARCHAR(200) NOT NULL,"
				+ " DESCRIPTION VARCHAR(2000)," + " STEPINDEX DOUBLE)";
		StatementProcessor statement = pools
				.createDbStatement(experiment, true);
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
			databaseMetaData = pools.fetchConnection(experiment, false)
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
				if (tableName.equals(eventTableName)) {
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
	 * Remove table.
	 * @return True if successful.
	 */
	public final boolean remove() {
		StatementProcessor statement = pools
				.createDbStatement(experiment, true);
		boolean result = statement.execute("DROP TABLE " + eventTableName);
		statement.close();
		return result;
	}

	/**
	 * @return a new instance of {@link EventQueries}.
	 */
	public final EventQueries getQueries() {
		return new EventQueries(pools.fetchConnection(experiment, false),
				eventTableName);
	}
}
