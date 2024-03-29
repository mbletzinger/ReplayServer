package org.nees.illinois.replay.common.types;

import java.util.List;

import org.nees.illinois.replay.data.MatrixSpecI;

/**
 * Interface to access the columns and name of a database table. The interface
 * allows the time columns such as timestamps and step to be excluded.
 * @author Michael Bletzinger
 */
public interface TableDefinitionI extends MatrixSpecI {
	/**
	 * Add a data column to the definition.
	 * @param channel
	 *            name of data column
	 */
	void addDataColumn(String channel);

	/**
	 * @param withTime
	 *            include the time and step columns
	 * @return List of column names
	 */
	List<String> getColumns(boolean withTime);

	/**
	 * @param withTime
	 *            include the time columns
	 * @return number of columns
	 */
	@Override
	int getNumberOfColumns(boolean withTime);

	/**
	 * @return The source name that produced the data.
	 */
	String getSource();

	/**
	 * @return The name which identifies the database table
	 */
	String getTableId();

}
