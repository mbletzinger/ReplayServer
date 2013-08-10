package org.nees.illinois.replay.db.query;

import java.util.List;

/**
 * Class which sets up select statements.
 * @author Michael Bletzinger
 */
public class DbQueries {
	/**
	 * Select a time window of database records.
	 * @param tableName
	 *            Name of the table.
	 * @param start
	 *            Start time.
	 * @param stop
	 *            Stop time.
	 * @return Query string.
	 */
	public final String selectTimeRange(final String tableName,
			final double start, final double stop) {
		return "SELECT * FROM \"" + tableName + "\" WHERE TIME ( BETWEEN "
				+ start + " AND " + stop + ")";
	}

	/**
	 * Select a time window of database records from a start time to the current
	 * end.
	 * @param tableName
	 *            Name of the table.
	 * @param start
	 *            Start time.
	 * @return Query string.
	 */
	public final String selectTime2End(final String tableName,
			final double start) {
		return "SELECT * FROM \"" + tableName + "\" WHERE (TIME >= " + start
				+ ")";
	}

	/**
	 * Select a time window of database records.
	 * @param tableName
	 *            Name of the table.
	 * @param start
	 *            Start time.
	 * @param stop
	 *            Stop time.
	 * @param type
	 *            Type filter.
	 * @return Query string.
	 */
	public final String selectTimeRange(final String tableName,
			final double start, final double stop, final String type) {
		return selectTimeRange(tableName, start, stop) + addType(type);
	}

	/**
	 * Select a time window of database records.
	 * @param tableName
	 *            Name of the table.
	 * @param start
	 *            Start time.
	 * @param type
	 *            Type filter.
	 * @return Query string.
	 */
	public final String selectTime2End(final String tableName,
			final double start, final String type) {
		return selectTime2End(tableName, start) + addType(type);
	}

	/**
	 * Select records which are based on a discrete set of sources.
	 * @param tableName
	 *            Name of table.
	 * @param names
	 *            Names of the sources.
	 * @return Query string.
	 */
	public final String selectDiscreteNames(final String tableName,
			final List<String> names) {
		String result = "SELECT * FROM \"" + tableName + "\" WHERE SOURCE IN (";
		boolean first = true;
		for (String s : names) {
			result += (first ? "" : ", ") + s;
		}
		result += ")";
		return result;
	}

	/**
	 * Select records which are based on a discrete set of sources.
	 * @param tableName
	 *            Name of table.
	 * @param names
	 *            Names of the sources.
	 * @param type
	 *            Type filter.
	 * @return Query string.
	 */
	public final String selectDiscreteNames(final String tableName,
			final List<String> names, final String type) {
		return selectDiscreteNames(tableName, names) + addType(type);
	}

	/**
	 * Select records which are based on a discrete set of times.
	 * @param tableName
	 *            Name of table.
	 * @param times
	 *            List of times.
	 * @return Query string.
	 */
	public final String selectDiscreteTimes(final String tableName,
			final List<Double> times) {
		String result = "SELECT * FROM \"" + tableName + "\" WHERE SOURCE IN (";
		boolean first = true;
		for (Double t : times) {
			result += (first ? "" : ", ") + t;
		}
		result += ")";
		return result;
	}

	/**
	 * Select records which are based on a discrete set of times.
	 * @param tableName
	 *            Name of table.
	 * @param times
	 *            List of times.
	 * @param type
	 *            Type filter.
	 * @return Query string.
	 */
	public final String selectDiscreteTimes(final String tableName,
			final List<Double> times, final String type) {
		return selectDiscreteTimes(tableName, times) + addType(type);
	}

	/**
	 * Creates query fragment for type filter.
	 * @param type
	 *            Name of type.
	 * @return query fragment.
	 */
	private String addType(final String type) {
		return " AND (TYPE = " + type + ")";
	}
}
