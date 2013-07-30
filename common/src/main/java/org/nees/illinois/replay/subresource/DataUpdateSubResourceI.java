package org.nees.illinois.replay.subresource;

import java.util.List;

import org.nees.illinois.replay.common.registries.TableType;
import org.nees.illinois.replay.common.types.TableIdentityI;

/**
 * Interface to the data sub-resource which manages data updates for the restlet
 * resources. The interface allows for the substitution of database-free test
 * code so that the restlet code can be tested separately.
 * @author Michael Bletzinger
 */
public interface DataUpdateSubResourceI extends SubResourceI {
	/**
	 * Creates a data table for a set of channels.
	 * @param name
	 *            of the table.
	 * @param table
	 *            Table type.
	 * @param channels
	 *            List of channels for the table.
	 * @return Table identity
	 */
	TableIdentityI createTable(String name, TableType table,
			List<String> channels);

	/**
	 * Removes a table.
	 * @param table
	 *            Identity of the table.
	 * @return True if successful.
	 */
	boolean removeTable(TableIdentityI table);

	/**
	 * Add data to a table.
	 * @param tableString
	 *            Table identity.
	 * @param data
	 *            Data to add.
	 * @return True if successful.
	 */
	boolean update(String tableString, double[][] data);
}
