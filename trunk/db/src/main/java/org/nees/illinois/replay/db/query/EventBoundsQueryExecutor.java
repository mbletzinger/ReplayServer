package org.nees.illinois.replay.db.query;

import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.data.DoubleMatrixI;
import org.nees.illinois.replay.db.events.EventQueries;
import org.nees.illinois.replay.events.EventListI;

/**
 * Executes a query based on one table and some event parameters.
 * @author Michael Bletzinger
 */
public class EventBoundsQueryExecutor {
	/**
	 * TimeBoundsQueryExecutor instance for this table.
	 */
	private final TimeBoundsQueryExecutor tquery;
	/**
	 * Event query executor.
	 */
	private final EventQueries equery;

	/**
	 * @param tquery
	 *            TimeBoundsQueryExecutor instance for this table.
	 * @param equery
	 *            Event query executor.
	 */
	public EventBoundsQueryExecutor(final TimeBoundsQueryExecutor tquery,
			final EventQueries equery) {
		this.tquery = tquery;
		this.equery = equery;
	}

	/**
	 * Query based on a list of discrete events.
	 * @param events
	 *            list.
	 * @return results of the query as a double matrix.
	 */
	public final DoubleMatrixI query(final List<String> events) {
		EventListI list = equery.getEvents(events, tquery.getTable()
				.getSource());
		List<Double> times = list.getTimeline();
		return tquery.query(times);
	}

	/**
	 * Query based on a start and stop event.
	 * @param start
	 *            event.
	 * @param stop
	 *            event.
	 * @return results of the query as a double matrix.
	 */
	public final DoubleMatrixI query(final String start, final String stop) {
		List<String> list = new ArrayList<String>();
		list.add(start);
		list.add(stop);
		EventListI events = equery.getEvents(list, tquery.getTable().getSource());
		List<Double> times = events.getTimeline();
		return tquery.query(times.get(0), times.get(1));
	}
}
