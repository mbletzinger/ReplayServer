package org.nees.illinois.replay.test.utils.types;
/**
 * List of Query test cases.
 * @author Michael Bletzinger
 *
 */
public enum QueryTestCases {
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
