package org.nees.illinois.replay.common.types;

import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.data.MatrixSpecI;

/**
 * Class which specifies the name and columns of a database table. The class
 * differentiates between data columns and columns which contain time data.
 * @author Michael Bletzinger
 */
public class TableDef implements TableDefinitionI {
	/**
	 * List of data column names.
	 */
	private final List<String> dataColumns;

	/**
	 * Table name info.
	 */
	private final String tableId;

	/**
	 * Source name.
	 */
	private final String source;
	/**
	 * List of time column names.
	 */
	private final List<String> timeColumns = new ArrayList<String>();
	{
		timeColumns.add("time");
	}

	/**
	 * @param dataColumns
	 *            List of data column names.
	 * @param tableId
	 *            Table name info.
	 * @param source
	 *            Source name.
	 */
	public TableDef(final List<String> dataColumns,final String tableId,
			final String source) {
		super();
		this.dataColumns = dataColumns;
		this.tableId = tableId;
		this.source = source;
	}

	@Override
	public final void addDataColumn(final String channel) {
		dataColumns.add(channel);
	}

	@Override
	public final void appendColumns(final MatrixSpecI other) {
		TableDefinitionI otherTC = (TableDefinitionI) other;
		dataColumns.addAll(otherTC.getColumns(false));
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

	@Override
	public String getSource() {
		return source;
	}

	/*
	 * (non-Javadoc)
	 * @see org.nees.illinois.replay.common.types.TableColumnsI#getTableId()
	 */
	@Override
	public final String getTableId() {
		return tableId;
	}

	@Override
	public final int numberOfTimeColumns() {
		return timeColumns.size();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		return "TableColumns [dataColumns=" + dataColumns + ", timeColumns="
				+ timeColumns + ", tableId=" + tableId + ", source=" + source
				+ "]";
	}
}
