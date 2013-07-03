package org.nees.illinois.replay.subresource;

import java.util.List;

import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.events.StepNumber;


/**
 * Interface to the query sub-resource which serves queries to the restlet
 * resources. The interface allows for the substitution of database-free test
 * code so that the restlet code can be tested separately.
 * @author Michael Bletzinger
 */
public interface DataQuerySubResourceI extends SubResourceI {
	/**
	 * Define a query.
	 * @param name
	 *            Name of the query.
	 * @param channels
	 *            Channels that are part of the query.
	 * @return True on success.
	 */
	boolean setQuery(String name, List<String> channels);

	/**
	 * Perform a query. Rate type is assumed to be steps. Scope is assumed to be
	 * all steps.
	 * @param name
	 *            Name of the query.
	 * @return Channel data.
	 */
	DoubleMatrix doQuery(String name);

	/**
	 * Perform a query. Rate is continuous data. Scope includes data to the end
	 * of the test.
	 * @param name
	 *            Name of the query.
	 * @param start
	 *            Timestamp in seconds from Jan 1 1901 that sets the start of
	 *            the query.
	 * @return Channel data.
	 */
	DoubleMatrix doQuery(String name, double start);

	/**
	 * Perform a query. Rate is continuous data.
	 * @param name
	 *            Name of the query.
	 * @param start
	 *            Timestamp in seconds from Jan 1 1901 that sets the start of
	 *            the query.
	 * @param stop
	 *            Timestamp in seconds from Jan 1 1901 that sets the end of the
	 *            query.
	 * @return Channel data.
	 */
	DoubleMatrix doQuery(String name, double start, double stop);

	/**
	 * Perform a query. Rate is step data. Scope includes data to the end of the
	 * test.
	 * @param name
	 *            Name of the query.
	 * @param start
	 *            Starting step number for the query.
	 * @param rate
	 *            Rate type data that should be queried.
	 * @return Channel data.
	 */
	DoubleMatrix doQuery(String name, StepNumber start, RateType rate);

	/**
	 * Perform a query. Rate is step data. Scope includes data to the end of the
	 * test.
	 * @param name
	 *            Name of the query.
	 * @param start
	 *            Starting step number for the query.
	 * @param stop
	 *            Ending step number for the query.
	 * @param rate
	 *            Rate type data that should be queried.
	 * @return Channel data.
	 */
	DoubleMatrix doQuery(String name, StepNumber start, StepNumber stop,
			RateType rate);

	/**
	 * Determines if the name identifies a stored query.
	 * @param name
	 *            Name of query.
	 * @return True if the query exists.
	 */
	boolean isQuery(String name);
}
