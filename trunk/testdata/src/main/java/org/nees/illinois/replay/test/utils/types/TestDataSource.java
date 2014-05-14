package org.nees.illinois.replay.test.utils.types;

/**
 * Enumerates the various channel list used for the mock test data.
 * @author Michael Bletzinger
 */
public enum TestDataSource {
	/**
	 * Channels from the DAQ system.
	 */
	DAQ,
	/**
	 * Channels from the 2nd DAQ system.
	 */
	DAQ2,
	/**
	 * Channels from the LBCB Operations Manager.
	 */
	OM,
	/**
	 * Channels from the 2nd LBCB.
	 */
	OM2,
	/**
	 * Channels from the Krypton System.
	 */
	Krypton1,
	/**
	 * Channels from another Krypton System.
	 */
	Krypton2,
}
