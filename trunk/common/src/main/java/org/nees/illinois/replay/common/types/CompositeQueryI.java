/**
 * 
 */
package org.nees.illinois.replay.common.types;

import java.util.List;

/**
 * Interface to reference the information needed to execute a database query. A
 * query can span several different tables requiring a sequence of <b>select</b>
 * statements. The interface allows access to each table and its selected
 * channels. It also provides an overall list that dictates the column order of
 * the data.
 * 
 * @author Michael Bletzinger
 * 
 */
public interface CompositeQueryI {
	/**
	 * 
	 * @return name of query
	 */
	String getName();

	/**
	 * 
	 * @return list of TableColumnsI which are the database tables that need to
	 *         be searched for this query and the channels fore each table
	 */
	List<TableColumnsI> getTableQueries();

	/**
	 * 
	 * @return list of channels for the query that describes the column order of
	 *         the query
	 */
	List<String> getQueryList();

}
