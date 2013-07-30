package org.nees.illinois.replay.test.utils;

/**
 * Enumerates the various channel list used for the mock test data.
 * @author Michael Bletzinger
 */
public enum TestDatasetType {
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
	Krypton,
	/**
	 * Query with a columns-after merge.
	 */
	QueryAfter,
	/**
	 * Query with a columns-before merge.
	 */
	QueryBefore,
	/**
	 * Query with only DAQ channels.
	 */
	QueryDaq,
	/**
	 * Query with the columns inserted in the middle of the set.
	 */
	QueryMiddle,
	/**
	 * Query with the columns mixed.
	 */
	QueryMixed,
	/**
	 * Query from just the Operations Manager.
	 */
	QueryOm,
	/**
	 * Query from three sources.
	 */
	QueryTriple
}
