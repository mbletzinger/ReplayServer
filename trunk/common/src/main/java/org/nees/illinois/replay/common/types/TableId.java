package org.nees.illinois.replay.common.types;

/**
 * Database table name which includes a {@link RateType sample rate} label.
 * @author Michael Bletzinger
 */
public class TableId implements TableIdentityI, Cloneable {
	/**
	 * Researcher version of the table name.
	 */
	private final String researchName;

	/**
	 * Name of the table that has the data for this channel.
	 */
	private final String dbTable;

	/**
	 * @param researchName
	 *            Researcher version of the table name.
	 * @param dbTable
	 *            Database column name.
	 */
	public TableId(final String researchName, final String dbTable) {
		super();
		this.dbTable = dbTable;
		this.researchName = researchName;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final Object clone() {
		return new TableId(researchName, dbTable);
	}

	@Override
	public final String getDatasetName() {
		return researchName;
	}

	@Override
	public final String getDbName() {
		return getDbTable();
	}

	/**
	 * @return the table name.
	 */
	public final String getDbTable() {
		return dbTable;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		return "TableId [datasetname=" + researchName + ", tablename="
				+ dbTable + "]";
	}
}
