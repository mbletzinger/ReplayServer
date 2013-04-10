/**
 * 
 */
package org.nees.illinois.replay.common.types;

import java.util.ArrayList;
import java.util.List;

/**
 * Class which specifies the name and columns of a database table. The class
 * differentiates between data columns and columns which contain time data.
 * 
 * @author Michael Bletzinger
 */
public class TableColumns implements TableColumnsI {
	/**
	 * List of data column names.
	 */
	private final List<String> dataColumns;
	/**
	 * List of time column names.
	 */
	private final List<String> timeColumns = new ArrayList<String>();
	/**
	 * Table name info.
	 */
	private final TableIdentityI tableId;

	{
		timeColumns.add("time");
		timeColumns.add("step");
		timeColumns.add("substep");
		timeColumns.add("correctionstep");

	}

	/**
	 * @param dataColumns
	 *            List of data column names.
	 * @param tableId
	 *            Table name info.
	 */
	public TableColumns(final List<String> dataColumns,
			final TableIdentityI tableId) {
		super();
		this.dataColumns = dataColumns;
		this.tableId = tableId;
	}

	/*
	 * (non-Javadoc)
	 * @see org.nees.illinois.replay.common.types.TableColumnsI#getColumns()
	 */
	@Override
	public final List<String> getColumns(final boolean withTime) {
		if (withTime) {
			List<String> result = new ArrayList<String>(timeColumns);
			result.addAll(dataColumns);
			return result;
		}
		return new ArrayList<String>(dataColumns);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.nees.illinois.replay.common.types.TableColumnsI#getNumberOfColumns
	 * (boolean)
	 */
	@Override
	public final int getNumberOfColumns(final boolean withTime) {
		int result = dataColumns.size();
		if (withTime) {
			result += timeColumns.size();
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see org.nees.illinois.replay.common.types.TableColumnsI#getTableId()
	 */
	@Override
	public final TableIdentityI getTableId() {
		return tableId;
	}

}
