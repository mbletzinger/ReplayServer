package org.nees.illinois.replay.common.types;

import org.nees.illinois.replay.data.RateType;

/**
 * Database table name which includes a {@link RateType sample rate} label.
 * @author Michael Bletzinger
 */
public class TableId implements TableIdentityI, Cloneable {
	/**
	 * Database name.
	 */
	private final String databasename;

	/**
	 * Researcher version of the table name.
	 */
	private final String datasetname;

	/**
	 * Name of the table.
	 */
	private final String tablename;

	/**
	 * @param datasetname
	 *            Researcher version of the table name.
	 * @param databasename
	 *            Database name.
	 * @param tablename
	 *            Name of the table.
	 */
	public TableId(final String datasetname, final String databasename,
			final String tablename) {
		super();
		this.databasename = databasename;
		this.tablename = tablename;
		this.datasetname = datasetname;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final Object clone() {
		return new TableId(null, databasename, tablename);
	}

	/**
	 * @return the dbname
	 */
	public final String getDatabasename() {
		return databasename;
	}

	@Override
	public final String getDatasetName() {
		return datasetname;
	}

	@Override
	public final String getDbName() {
		return databasename + "." + tablename;
	}

	/**
	 * @return the table name.
	 */
	public final String getTablename() {
		return tablename;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		return "TableId [datasetname=" + datasetname + ", databasename="
				+ databasename + ", tablename=" + tablename
				+ "]";
	}
}
