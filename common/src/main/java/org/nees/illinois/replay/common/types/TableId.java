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
	 * Sampling rate.
	 */
	private RateType rate;

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
	 * @param rate
	 *            Sampling rate.
	 */
	public TableId(final String datasetname, final String databasename,
			final String tablename, final RateType rate) {
		super();
		this.databasename = databasename;
		this.tablename = tablename;
		this.datasetname = datasetname;
		this.rate = rate;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final Object clone() {
		return new TableId(null, databasename, tablename, rate);
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
		return databasename + "." + tablename + "_" + rate;
	}

	/**
	 * @return the rate
	 */
	public final RateType getRate() {
		return rate;
	}

	/**
	 * @return the table name.
	 */
	public final String getTablename() {
		return tablename;
	}

	/**
	 * @param rate
	 *            the rate to set
	 */
	public final void setRate(final RateType rate) {
		this.rate = rate;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		return "TableId [datasetname=" + datasetname + ", databasename="
				+ databasename + ", rate=" + rate + ", tablename=" + tablename
				+ "]";
	}
}
