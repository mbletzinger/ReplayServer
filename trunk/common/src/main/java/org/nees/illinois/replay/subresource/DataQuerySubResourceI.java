package org.nees.illinois.replay.subresource;

import java.util.List;

import org.nees.illinois.replay.common.types.TimeBoundsI;
import org.nees.illinois.replay.data.DoubleMatrixI;

/**
 * Interface to the query sub-resource which serves queries to the restlet
 * resources. The interface allows for the substitution of database-free test
 * code so that the restlet code can be tested separately.
 * @author Michael Bletzinger
 */
public interface DataQuerySubResourceI extends SubResourceI {

	/**
	 * Perform a query.
	 * @param name
	 *            Name of the query.
	 * @param bounds
	 *            Time constraints of the query.
	 * @return Channel data.
	 */
	DoubleMatrixI doQuery(String name, TimeBoundsI bounds);

	/**
	 * Determines if the name identifies a stored query.
	 * @param name
	 *            Name of query.
	 * @return True if the query exists.
	 */
	boolean isQuery(String name);

	/**
	 * Define a query.
	 * @param name
	 *            Name of the query.
	 * @param channels
	 *            Channels that are part of the query.
	 * @return True on success.
	 */
	boolean setQuery(String name, List<String> channels);
}
