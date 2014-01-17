package org.nees.illinois.replay.test.utils.mgen;

import java.util.List;
/**
 * Interface for mixing different types of columns.
 * @author Michael Bletzinger
 *
 */
public interface GenerateColumnI {
	/**
	 * The generate function.
	 *@param result
	 *Matrix of numbers where the column is to be appended
	 *@param slopeNegative
	 *True iif numbers are in descending order
	 */
	void gen(List<List<Double>> result, boolean slopeNegative);
}
