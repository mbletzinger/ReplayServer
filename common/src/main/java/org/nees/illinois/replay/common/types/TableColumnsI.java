/**
 * 
 */
package org.nees.illinois.replay.common.types;

import java.util.List;

/**
 * Interface to access the columns and name of a database table. The interface
 * allows the time columns such as timestamps and step to be excluded.
 * 
 * @author Michael Bletzinger
 * 
 */
public interface TableColumnsI {
	/**
	 * 
	 * @param withTime
	 *            include the time and step columns
	 * @return List of column names
	 */
	public List<String> getColumns(boolean withTime);

	/**
	 * 
	 * @param withTime
	 *            include the time and step columns
	 * @return number of columns
	 */
	public int getNumberOfColumns(boolean withTime);

	/**
	 * 
	 * @return The TableIdentityI instance which identifies the database table
	 */
	public TableIdentityI getTableId();

}
