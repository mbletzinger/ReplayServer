package org.nees.illinois.replay.db.events;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.common.registries.ExperimentRegistries;
import org.nees.illinois.replay.common.registries.TableRegistry;
import org.nees.illinois.replay.common.types.TableDefinitionI;
import org.nees.illinois.replay.db.query.QuerySelectFactory;
import org.nees.illinois.replay.db.statement.StatementProcessor;
import org.nees.illinois.replay.events.Event;
import org.nees.illinois.replay.events.EventI;
import org.nees.illinois.replay.events.EventList;
import org.nees.illinois.replay.events.EventListI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that executes queries against the event table.
 * @author Michael Bletzinger
 */
public class EventQueries {
	/**
	 * Database connection.
	 */
	private final Connection connection;
	/**
	 * Experiment scoped registries.
	 */
	private final ExperimentRegistries er;
	/**
	 * Logger.
	 **/
	private final Logger log = LoggerFactory.getLogger(EventQueries.class);
	/**
	 * Query strings.
	 */
	private final QuerySelectFactory queries = new QuerySelectFactory();

	/**
	 * @param connection
	 *            Database connection.
	 * @param er
	 *            Experiment scoped registries.
	 */
	public EventQueries(final Connection connection,
			final ExperimentRegistries er) {
		this.connection = connection;
		this.er = er;
	}

	/**
	 * Close the database connection.
	 */
	public final void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			log.error("Connectiion close failed because", e);
		}
	}

	/**
	 * @return the connection
	 */
	public final Connection getConnection() {
		return connection;
	}

	/**
	 * Get all of the events that fall within a time window.
	 * @param start
	 *            Start of the time window.
	 * @param stop
	 *            End of the time window.
	 * @param source
	 *            Source of the events. Use null if you don't want to specify a
	 *            source.
	 * @return List of events.
	 */
	public final EventListI getEvents(final double start, final double stop,
			final String source) {
		String eventTableName = er.getEventTableName();
		StatementProcessor dbSt = new StatementProcessor(connection);
		String selectS;
		if (source == null) {
			selectS = queries.selectTimeRange(eventTableName, start, stop);
		} else {
			selectS = queries.selectTimeRange(eventTableName, start, stop,
					source2TableId(source));
		}
		ResultSet rs = dbSt.query(selectS);
		List<EventI> events = resultSet2Events(rs);
		dbSt.closeQuery(rs);
		return new EventList(events);
	}

	/**
	 * Get a list of events.
	 * @param names
	 *            event names.
	 * @param source
	 *            Source of the events. Use null if you don't want to specify a
	 *            source.
	 * @return list of events.
	 */
	public final EventListI getEvents(final List<String> names,
			final String source) {
		StatementProcessor dbSt = new StatementProcessor(connection);
		String selectS;
		String eventTableName = er.getEventTableName();
		if (source == null) {
			selectS = queries.selectDiscreteNames(eventTableName, names);
		} else {
			selectS = queries.selectDiscreteNames(eventTableName, names,
					source2TableId(source));
		}
		ResultSet rs = dbSt.query(selectS);
		List<EventI> events = resultSet2Events(rs);
		dbSt.closeQuery(rs);
		EventListI result = new EventList(events);
		log.debug("Results: " + result);
		return result;
	}

	/**
	 * Get an event using the name or source. Get the event from all sources if
	 * source is null.
	 * @param name
	 *            of the event.
	 * @param source
	 *            of the event or null.
	 * @return list of events.
	 */
	public final EventListI getEvents(final String name, final String source) {
		String eventTableName = er.getEventTableName();
		StatementProcessor dbSt = new StatementProcessor(connection);
		String selectS;
		List<String> list = new ArrayList<String>();
		list.add(name);
		if (source == null) {
			selectS = queries.selectDiscreteNames(eventTableName, list);
		} else {
			selectS = queries.selectDiscreteNames(eventTableName, list,
					source2TableId(source));
		}
		ResultSet rs = dbSt.query(selectS);
		List<EventI> events = resultSet2Events(rs);
		dbSt.closeQuery(rs);
		return new EventList(events);
	}

	/**
	 * Get all the events between the border events.
	 * @param start
	 *            Starting event.
	 * @param stop
	 *            Stopping event.
	 * @param source
	 *            Source of the events. Use null if you don't want to specify a
	 *            source.
	 * @return List of events.
	 */
	public final EventListI getEvents(final String start, final String stop,
			final String source) {
		List<String> borders = new ArrayList<String>();
		borders.add(start);
		borders.add(stop);
		EventListI borderEL = getEvents(borders, source);
		List<EventI> borderE = borderEL.getEvents();
		final double borderMargin = 1.0;
		return getEvents(borderE.get(0).getTime() - borderMargin, borderE
				.get(1).getTime() + borderMargin, source);
	}

	/**
	 * Turn a result set into an event list.
	 * @param rs
	 *            The {@link ResultSet}.
	 * @return list of events.
	 */
	private List<EventI> resultSet2Events(final ResultSet rs) {
		List<EventI> result = new ArrayList<EventI>();
		String[] headers = { "TIME", "NAME", "DESCRIPTION", "SOURCE" };
		boolean[] isDouble = { true, false, false, false };
		try {
			while (rs.next()) {
				List<Double> numbers = new ArrayList<Double>();
				List<String> strings = new ArrayList<String>();
				for (int c = 0;c < headers.length;c++) {
					if (isDouble[c]) {
						Double n = rs.getDouble(c + 1);
						numbers.add(n);
					} else {
						String value = rs.getString(c + 1);
						strings.add(value);
					}
				}
				EventI e = null;
				final int descriptionC = 1;
				final int sourceC = 2;
				String tableid = strings.get(sourceC);
				TableRegistry tr = er.getTableDefs();
				String source = tr.id2Name(tableid);
				e = new Event(strings.get(0), numbers.get(0),
						strings.get(descriptionC), source);
				result.add(e);
			}
		} catch (SQLException e) {
			log.error("Result Set fetch failed because ", e);
			return null;
		}
		return result;
	}

	/**
	 * Map source name to a table id.
	 * @param source
	 *            name.
	 * @return table id.
	 */
	private String source2TableId(final String source) {
		TableRegistry tr = er.getTableDefs();
		TableDefinitionI tbl = tr.getTable(source);
		if(tbl == null) {
			log.error("Source " + source + " is not in the table registry");
			return null;
		}
		return tbl.getTableId();
	}
}
