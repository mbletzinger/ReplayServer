package org.nees.illinois.replay.common.registries;

/**
 * Enumerator which defines database table types to help map research data to
 * the server.
 * @author Michael Bletzinger
 */
public enum TableType {
	/**
	 * Data from data acquisition systems.
	 */
	DAQ,
	/**
	 * Data from control systems.
	 */
	Control,
	/**
	 * Data from simulation coordinators.
	 */
	UiSimCor,
	/**
	 * Data from the DMM systems.
	 */
	Krypton,
	/**
	 * Data from simulation runs.
	 */
	Simulated,
	/**
	 * Data showing the expected results.
	 */
	Expected,
	/**
	 * Converted data from the NEES Warehouse.
	 */
	Converted,
	/**
	 * Corrected data from the NEES Warehouse.
	 */
	Corrected,
	/**
	 * Derived data from the NEES Warehouse.
	 */
	Derived
}
