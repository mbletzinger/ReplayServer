package org.nees.illinois.replay.db.registry.synch;

/**
 * Interface which defines how registries of an experiment are synchronized with
 * the back end database.
 * @author Michael Bletzinger
 */
public interface RegistrySynchI {

	/**
	 * Create the registry table.
	 */
	void createTable();

	/**
	 * Load a registry with values from the database.
	 */
	void load();

	/**
	 * Remove the registry table. Used for testing.
	 */
	void removeTable();

	/**
	 * Dump the contents of the registry into the database.
	 */
	void save();

}
