package org.nees.illinois.replay.common.types;

/**
 * Interface to access the full-name of a database table. The naming convention
 * is most likely NEES site-specific and has to follow database naming
 * conventions. This interface takes the guesswork out of this process.
 * @author Michael Bletzinger
 */
public interface TableIdentityI {
	/**
	 * @return database name of table
	 */
	String getDbName();

	/**
	 * @return the research version of the table name
	 */
	String getDatasetName();
}
