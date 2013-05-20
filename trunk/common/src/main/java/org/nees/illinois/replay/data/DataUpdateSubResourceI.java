package org.nees.illinois.replay.data;

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
	 * @param table
	 *            Table type.
	 * @param channels
	 *            List of channels for the table.
	 * @return Table identity
	 */
	TableIdentityI createTable(TableType table, List<String> channels);

	/**
	 * Removes a table.
	 * @param table
	 *            Identity of the table.
	 * @return True if successful.
	 */
	boolean removeTable(TableIdentityI table);

	/**
	 * Add data to a table.
	 * @param table
	 *            Table identity.
	 * @param rate
	 *            Table rate type.
	 * @param data
	 *            Data to add.
	 * @return True if successful.
	 */
	boolean update(TableIdentityI table, RateType rate, double[][] data);
}
