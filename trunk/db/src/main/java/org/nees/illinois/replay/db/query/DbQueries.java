package org.nees.illinois.replay.db.query;

import java.util.List;

/**
 * Class which sets up select statements.
 * @author Michael Bletzinger
 */
public class DbQueries {
	/**
	 * Creates query fragment for source filter.
	 * @param source
	 *            Name of source.
	 * @return query fragment.
	 */
	private String addSource(final String source) {
		return " AND (SOURCE = " + source + ")";
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
			first = false;
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
	 * @param source
	 *            Source filter.
	 * @return Query string.
	 */
	public final String selectDiscreteNames(final String tableName,
			final List<String> names, final String source) {
		return selectDiscreteNames(tableName, names) + addSource(source);
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
			first = false;
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
	 * @param columns list of columns.
	 * @return Query string.
	 */
	public final String selectDiscreteTimes(final String tableName,
			final List<Double> times, final List<String> columns) {
		String result = "SELECT ";
		boolean first = true;
		for( String h : columns) {
			result += (first ? "" : ", ") + h;
			first = false;
		}
		result += " FROM \"" + tableName + "\" WHERE SOURCE IN (";
		first = true;
		for (Double t : times) {
			result += (first ? "" : ", ") + t;
			first = false;
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
	 * @param source
	 *            Source filter.
	 * @return Query string.
	 */
	public final String selectDiscreteTimes(final String tableName,
			final List<Double> times, final String source) {
		return selectDiscreteTimes(tableName, times) + addSource(source);
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
	 * Select a time window of database records from a start time to the current
	 * end.
	 * @param tableName
	 *            Name of the table.
	 * @param start
	 *            Start time.
	 * @param columns columns to return.
	 * @return Query string.
	 */
	public final String selectTime2End(final String tableName,
			final double start, final List<String> columns) {
		String result = "SELECT";
		boolean first = true;
		for( String h : columns) {
			result += (first ? "" : ", ") + h;
			first = false;
		}
		result += " FROM \"" + tableName + "\" WHERE (TIME >= " + start
				+ ")";
		return result;
	}

	/**
	 * Select a time window of database records.
	 * @param tableName
	 *            Name of the table.
	 * @param start
	 *            Start time.
	 * @param source
	 *            Source filter.
	 * @return Query string.
	 */
	public final String selectTime2End(final String tableName,
			final double start, final String source) {
		return selectTime2End(tableName, start) + addSource(source);
	}

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
	 * Select a time window of database records.
	 * @param tableName
	 *            Name of the table.
	 * @param start
	 *            Start time.
	 * @param stop
	 *            Stop time.
	 * @param columns columns to return.
	 * @return Query string.
	 */
	public final String selectTimeRange(final String tableName,
			final double start, final double stop, final List<String> columns) {
		String result = "SELECT";
		boolean first = true;
		for( String h : columns) {
			result += (first ? "" : ", ") + h;
			first = false;
		}
		result += " FROM \"" + tableName + "\" WHERE TIME ( BETWEEN "
				+ start + " AND " + stop + ")";
		return result;
	}

	/**
	 * Select a time window of database records.
	 * @param tableName
	 *            Name of the table.
	 * @param start
	 *            Start time.
	 * @param stop
	 *            Stop time.
	 * @param source
	 *            Source filter.
	 * @return Query string.
	 */
	public final String selectTimeRange(final String tableName,
			final double start, final double stop, final String source) {
		return selectTimeRange(tableName, start, stop) + addSource(source);
	}
}
