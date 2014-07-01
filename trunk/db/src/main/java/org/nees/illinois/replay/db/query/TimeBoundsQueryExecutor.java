package org.nees.illinois.replay.db.query;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.common.types.TableDefinitionI;
import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.DoubleMatrixI;
import org.nees.illinois.replay.db.statement.StatementProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Executes a query based on one table {@link TableDefinitionI definition} and
 * some time parameters.
 * @author Michael Bletzinger
 */
public class TimeBoundsQueryExecutor {
	/**
	 * Database connection.
	 */
	private final Connection connection;
	/**
	 * Logger.
	 **/
	private final Logger log = LoggerFactory
			.getLogger(TimeBoundsQueryExecutor.class);
	/**
	 * Query select statement builder.
	 */
	private final QuerySelectFactory queryStrings = new QuerySelectFactory();
	/**
	 * Defines the table and its selected columns.
	 */
	private final TableDefinitionI table;

	/**
	 * @param table
	 *            Defines the table and its selected columns.
	 * @param connection
	 *            Database connection.
	 */
	public TimeBoundsQueryExecutor(final TableDefinitionI table,
			final Connection connection) {
		this.table = table;
		this.connection = connection;
	}

	/**
	 * Execute a select statement and extract the matrix of values.
	 * @param select
	 *            query string to execute.
	 * @return double matrix of values.
	 */
	private DoubleMatrixI execute(final String select) {
		StatementProcessor dbSt = new StatementProcessor(connection);
		ResultSet rs = dbSt.query(select);
		if (rs == null) {
			log.error("\"" + select + "\" returned nuttin'");
			return null;
		}
		List<List<Double>> result = new ArrayList<List<Double>>();
		int columns = table.getNumberOfColumns(true);
		try {
			while (rs.next()) {
				List<Double> row = new ArrayList<Double>();
				for (int c = 0;c < columns;c++) {
					double value = rs.getDouble(c + 1);
					row.add(new Double(value));
				}
				result.add(row);
			}
		} catch (SQLException e) {
			log.error("\"" + select + "\" failed because ", e);
			return null;
		}
		return new DoubleMatrix(result);
	}

	/**
	 * @return the connection
	 */
	public final Connection getConnection() {
		return connection;
	}

	/**
	 * @return the queryStrings
	 */
	public final QuerySelectFactory getQueryStrings() {
		return queryStrings;
	}

	/**
	 * @return the table
	 */
	public final TableDefinitionI getTable() {
		return table;
	}

	/**
	 * Query for a range of times.
	 * @param start
	 *            Start time.
	 * @param stop
	 *            Stop time.
	 * @return double matrix of values.
	 */
	public final DoubleMatrixI query(final double start, final double stop) {
		String select;
		if (Double.isNaN(stop)) {
			select = queryStrings.selectTime2End(table.getTableId(), start, table.getColumns(true));
		} else {
			select = queryStrings.selectTimeRange(table.getTableId(), start,
					stop, table.getColumns(true));
		}
		return execute(select);
	}

	/**
	 * Query for a list of times.
	 * @param times
	 *            list times
	 * @return double matrix of values.
	 */
	public final DoubleMatrixI query(final List<Double> times) {
		String select = queryStrings.selectDiscreteTimes(table.getTableId(),
				times, table.getColumns(true));
		return execute(select);
	}
}
