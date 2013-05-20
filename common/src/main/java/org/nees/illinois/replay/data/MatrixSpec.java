/**
*
*/
package org.nees.illinois.replay.data;

/**
 * @author Michael Bletzinger
 *
 */
public class MatrixSpec implements MatrixSpecI {
	/**
	 * Number of columns in the matrix.
	 */
	private int numberOfColumns;

	/**
	 *@param numberOfColumns
	 *Number of columns in the matrix.
	 */
	public MatrixSpec(final int numberOfColumns) {
		this.numberOfColumns = numberOfColumns;
	}

	/* (non-Javadoc)
	 * @see org.nees.illinois.replay.common.types.MatrixSpecsI#getNumberOfColumns(boolean)
	 */
	@Override
	public final int getNumberOfColumns(final boolean withTime) {
		return numberOfColumns;
	}

	/* (non-Javadoc)
	 * @see org.nees.illinois.replay.common.types.MatrixSpecsI#numberOfTimeColumns()
	 */
	@Override
	public final int numberOfTimeColumns() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.nees.illinois.replay.common.types.MatrixSpecsI#appendColumns(org.nees.illinois.replay.common.types.MatrixSpecsI)
	 */
	@Override
	public final void appendColumns(final MatrixSpecI other) {
		numberOfColumns += other.getNumberOfColumns(false);
	}

}
