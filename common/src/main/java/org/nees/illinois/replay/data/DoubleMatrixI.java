package org.nees.illinois.replay.data;

import java.util.List;

/**
 * Interface for a matrix of doubles.
 * @author Michael Bletzinger
 */
public interface DoubleMatrixI {

	/**
	 * Append a row to the matrix.
	 * @param row
	 *            to append.
	 */
	void append(List<Double> row);

	/**
	 * Empty the matrix.
	 */
	void clear();

	/**
	 * @return the data as a double[][] array. Null elements are replaced by
	 *         Double.NaNs.
	 */
	double[][] getData();

	/**
	 * Returns a row.
	 * @param row
	 *            index of the row
	 * @return list of doubles.
	 */
	List<Double> getRow(int row);

	/**
	 * @return {@link MatrixSpecI} which contains information about the matrix.
	 */
	MatrixSpecI getSpec();

	/**
	 * Tells whether the element is null or not.
	 * @param row
	 *            Row index of element.
	 * @param col
	 *            Column index of element.
	 * @return True if the element is null.
	 */
	boolean isNull(int row, int col);

	/**
	 * Sets the value of an element.
	 * @param row
	 *            Row index of element.
	 * @param col
	 *            Column index of element.
	 * @param value
	 *            Value to be set. Can be null.
	 */
	void set(int row, int col, Double value);

	/**
	 * Returns the 2D size of the matrix.
	 * @return Array of sizes [row, column]
	 */
	int[] sizes();

	/**
	 * Returns the index of the timestamp.
	 * @param timestamp
	 *            that needs the index.
	 * @return the index.
	 */
	int timeIndex(double timestamp);

	/**
	 * Returns the matrix as a double list.
	 * @return The double list.
	 */
	List<List<Double>> toList();

	/**
	 * Returns the value of an element. If the element is null returns
	 * Double.NaN.
	 * @param row
	 *            Row index of element.
	 * @param col
	 *            Column index of element.
	 * @return The element value.
	 */
	double value(int row, int col);
}
