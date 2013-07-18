package org.nees.illinois.replay.db;

import java.sql.Connection;

/**
 * Interface to database wrappers to handle tasks that fall outside of JBDC.
 * @author Michael Bletzinger
 */
public interface DbOperationsI {
	/**
	 * Add or subtract text to the connection URL based on the needs of a
	 * specific database.
	 * @param url
	 *            Connection URL.
	 * @param experiment
	 *            Initial experiment.
	 * @return Corrected URL.
	 */
	 String filterUrl(String url, String experiment);

	/**
	 * Create a new database for the experiment.
	 * @param experiment
	 *            Name of the experiment.
	 * @throws Exception
	 *             when the creation fails.
	 */
	 void createDatabase(String experiment) throws Exception;

	/**
	 * Tells whether the database for an experiment exists or not.
	 * @param experiment
	 *            Name of the experiment.
	 * @return True if the database for the experiment exists.
	 * @throws Exception
	 *             If the query fails for any reason.
	 */
	 boolean isDatabase(String experiment) throws Exception;

	/**
	 * Removes a database. Mainly used for integration testing.
	 * @param experiment
	 *            Name of the experiment whose database needs to be removed.
	 * @throws Exception
	 *             If the remove fails.
	 */
	 void removeDatabase(String experiment) throws Exception;

	/**
	 * Create a JDBC connection.
	 * @param withDatabase
	 *            Go ahead and open the experiment database set earlier.
	 * @return The JDBC connection.
	 * @throws Exception
	 *             If the connection could not be made.
	 */
	 Connection generateConnection(boolean withDatabase) throws Exception;

	/**
	 * Close a JDBC connection.
	 * @param connection
	 *            Connection reference.
	 * @throws Exception
	 *             If the close failed.
	 */
	 void closeConnection(Connection connection) throws Exception;

	/**
	 * Set the database to the one associated with the experiment.
	 * @param experiment
	 *            Name of the experiment.
	 */
	 void setExperiment(String experiment);

	/**
	 * Get the name of the current experiment.
	 * @return the name.
	 */
	 String getExperiment();
}
