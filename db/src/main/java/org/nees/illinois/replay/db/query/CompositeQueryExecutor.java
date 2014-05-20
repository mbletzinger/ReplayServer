package org.nees.illinois.replay.db.query;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nees.illinois.replay.common.types.CompositeQueryI;
import org.nees.illinois.replay.common.types.TableDefinitionI;
import org.nees.illinois.replay.data.DoubleMatrixI;
import org.nees.illinois.replay.data.MergeSet;
import org.nees.illinois.replay.data.SubsetSlicer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Coordinates the execution of a composite query.
 * @author Michael Bletzinger
 */
public class CompositeQueryExecutor {

	/**
	 * Composite query to be executed.
	 */
	private final CompositeQueryI query;

	/**
	 * Database connection.
	 */
	private final Connection connection;
	/**
	 * Map of individual table results.
	 */
	private final Map<String, DoubleMatrixI> tableResults = new HashMap<String, DoubleMatrixI>();
	/**
	 * Merged results in the wrong column order.
	 */
	private final MergeSet results = new MergeSet();
	/**
	 * Order of the columns in the merged results.
	 */
	private final List<String> mergedChannels = new ArrayList<String>();
	/**
	 * Logger.
	 **/
	private final Logger log = LoggerFactory
			.getLogger(CompositeQueryExecutor.class);
	/**
	 * @param query
	 *            Composite query to be executed.
	 * @param connection
	 *            Database connection.
	 */
	public CompositeQueryExecutor(final CompositeQueryI query,final Connection connection) {
		this.query = query;
		this.connection = connection;
	}

	/**
	 * Run a query within the specified time frame.
	 * @param start
	 *            of the time frame.
	 * @param stop
	 *            of the time frame.
	 * @return double matrix of the results.
	 */
	public final DoubleMatrixI execute(final double start, final double stop) {
		queryTables(start, stop);
		mergeResults();
		return sortResults();
	}

	/**
	 * Run a query based on a discrete set of times.
	 * @param times
	 *            set
	 * @return double matrix of the results.
	 */
	public final DoubleMatrixI execute(final List<Double> times) {
		queryTables(times);
		mergeResults();
		return sortResults();
	}

	/**
	 * Merge the results of all of the tables.
	 */
	private void mergeResults() {
		for (String t : tableResults.keySet()) {
			DoubleMatrixI dm = tableResults.get(t);
			results.merge(dm);
			TableDefinitionI td = query.getTableQueries().get(t);
			mergedChannels.addAll(td.getColumns(false));
		}
	}

	/**
	 * Query each table for the specified time frame.
	 * @param start
	 *            of the time frame.
	 * @param stop
	 *            of the time frame.
	 */
	private void queryTables(final double start, final double stop) {
		Map<String, TableDefinitionI> tableMap = query.getTableQueries();
		for (String t : tableMap.keySet()) {
			TableDefinitionI tbl = tableMap.get(t);
			OneTableQueryExecutor exec = new OneTableQueryExecutor(tbl,
					connection);
			DoubleMatrixI dm = exec.query(start, stop);
			if (dm == null) {
				log.debug("Table " + t + " returned nothing for "
						+ query.getName());
				continue;
			}
			tableResults.put(t, dm);
		}
	}

	/**
	 * Query each table for the set of discrete times.
	 * @param times
	 *            set.
	 */
	private void queryTables(final List<Double> times) {
		Map<String, TableDefinitionI> tableMap = query.getTableQueries();
		for (String t : tableMap.keySet()) {
			TableDefinitionI tbl = tableMap.get(t);
			OneTableQueryExecutor exec = new OneTableQueryExecutor(tbl,
					connection);
			DoubleMatrixI dm = exec.query(times);
			if (dm == null) {
				log.debug("Table " + t + " returned nothing for "
						+ query.getName());
				continue;
			}
			tableResults.put(t, dm);
		}
	}

	/**
	 * Sort the result columns in the order specified by the composite query.
	 * @return the sorted results.
	 */
	private DoubleMatrixI sortResults() {
		List<Integer> slices = new ArrayList<Integer>();
		for (String c : query.getQueryList()) {
			int k = mergedChannels.indexOf(c);
			slices.add(k);
		}
		SubsetSlicer slicer = new SubsetSlicer(results.getResult());
		slicer.addSlices(slices);
		return slicer.slice();
	}
}
