package org.nees.illinois.replay.test.utils.types;

/**
 * Enumerates the various ways matrix rows from the mock test data are mixed.
 * @author Michael Bletzinger
 */
public enum MatrixMixType {
	/**
	 * New rows before.
	 */
	AddBefore,
	/**
	 * New rows after.
	 */
	AddAfter,
	/**
	 * New rows inserted in the middle.
	 */
	AddMiddle,
	/**
	 * New rows interspersed.
	 */
	AddInterleaved,
	/**
	 * New rows have the same time record.
	 */
	AddMerged
}
