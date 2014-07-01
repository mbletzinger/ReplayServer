package org.nees.illinois.replay.db.query;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nees.illinois.replay.common.types.CompositeQueryI;
import org.nees.illinois.replay.common.types.TableDefinitionI;
import org.nees.illinois.replay.common.types.TimeBoundsI;
import org.nees.illinois.replay.data.DoubleMatrixI;
import org.nees.illinois.replay.data.MergeSet;
import org.nees.illinois.replay.data.SubsetSlicer;
import org.nees.illinois.replay.db.events.EventQueries;
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
	 * Query for event times.
	 */
	private final EventQueries eventQ;

	/**
	 * @param query
	 *            Composite query to be executed.
	 * @param connection
	 *            Database connection.
	 * @param eventQ query used to look up event times.
	 */
	public CompositeQueryExecutor(final CompositeQueryI query,final Connection connection, final EventQueries eventQ) {
		this.query = query;
		this.connection = connection;
		this.eventQ = eventQ;
	}

	/**
	 * Query each table for the set of discrete events.
	 * @param events
	 *            set.
	 */
	private void eventQueryTables(final List<String> events) {
		Map<String, TableDefinitionI> tableMap = query.getTableQueries();
		for (String t : tableMap.keySet()) {
			TableDefinitionI tbl = tableMap.get(t);
			TimeBoundsQueryExecutor texec = new TimeBoundsQueryExecutor(tbl,
					connection);
			EventBoundsQueryExecutor exec = new EventBoundsQueryExecutor(texec, eventQ);
			DoubleMatrixI dm = exec.query(events);
			if (dm == null) {
				log.debug("Table " + t + " returned nothing for "
						+ query.getName());
				continue;
			}
			tableResults.put(t, dm);
		}
	}

	/**
	 * Run a query within the specified time frame.
	 * @param tb time boundaries of the query.
	 * @return double matrix of the results.
	 */
	public final DoubleMatrixI execute(final TimeBoundsI tb) {
		switch(tb.getType()) {
		case StartStopTime:
			Map<String, TableDefinitionI> tableMap = query.getTableQueries();
			if( tableMap.keySet().size() > 1) {
				log.error("Query " + query.getName() + " cannot be used with time boundaries");
				return null;
			}
			queryTables(tb.getStart(), tb.getStop());
			break;
		case EventList:
			eventQueryTables(tb.getNames());
			break;
		case StartStopEvent:
			queryTables(tb.getStartName(), tb.getStopName());
			break;
		case TimesList:
			List<Double> times = new ArrayList<Double>();
			for(double d : tb.getTimes()) {
				times.add(d);
			}
			queryTables(times);
			break;
		default:
			log.debug("Time bounds type " + tb.getType() + " is not recognized");
		}
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
			TimeBoundsQueryExecutor exec = new TimeBoundsQueryExecutor(tbl,
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
	 * Run a query based on a discrete set of times.
	 * @param times
	 *            set of discrete times.
	 * @return double matrix of the results.
	 */
	private DoubleMatrixI queryTables(final List<Double> times) {
		Map<String, TableDefinitionI> tableMap = query.getTableQueries();
		for (String t : tableMap.keySet()) {
			TableDefinitionI tbl = tableMap.get(t);
			TimeBoundsQueryExecutor exec = new TimeBoundsQueryExecutor(tbl,
					connection);
			DoubleMatrixI dm = exec.query(times);
			if (dm == null) {
				log.debug("Table " + t + " returned nothing for "
						+ query.getName());
				continue;
			}
			log.debug("Query " + query.getName() + " returned: " + dm);
			tableResults.put(t, dm);
		}
		mergeResults();
		return sortResults();
	}

	/**
	 * Query each table for the specified time frame.
	 * @param start event
	 *            of the time frame.
	 * @param stop event
	 *            of the time frame.
	 */
	private void queryTables(final String start, final String stop) {
		Map<String, TableDefinitionI> tableMap = query.getTableQueries();
		for (String t : tableMap.keySet()) {
			TableDefinitionI tbl = tableMap.get(t);
			TimeBoundsQueryExecutor qexec = new TimeBoundsQueryExecutor(tbl,
					connection);
			EventBoundsQueryExecutor exec = new EventBoundsQueryExecutor(qexec, eventQ);
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
	 * Sort the result columns in the order specified by the composite query.
	 * @return the sorted results.
	 */
	private DoubleMatrixI sortResults() {
		List<Integer> slices = new ArrayList<Integer>();
		slices.add(0); // time column
		for (String c : query.getQueryList()) {
			int k = mergedChannels.indexOf(c);
			slices.add(k + 1);
		}
		SubsetSlicer slicer = new SubsetSlicer(results.getResult());
		slicer.setSliceColumn(true);
		slicer.addSlices(slices);
		return slicer.slice();
	}
}
