/**
 * 
 */
package org.nees.illinois.replay.common.types;

import org.nees.illinois.replay.data.RateType;

/**
 * Database table name which includes a {@link RateType sample rate} label.
 * @author Michael Bletzinger
 * 
 */
public class TableId implements TableIdentityI, Cloneable {
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {
		return new TableId(dbname, tablename, rate);
	}
/**
 * Database name.
 */
	private final String dbname;
	/**
	 * Sampling rate.
	 */
	private RateType rate;
	/**
	 * Name of the table.
	 */
	private final String tablename;

	/**
	 * @param dbname
 * Database name.
	 * @param tablename
	 * Name of the table.
	 * @param rate
	 * Sampling rate.
	 */
	public TableId(final String dbname, final String tablename, final RateType rate) {
		super();
		this.dbname = dbname;
		this.tablename = tablename;
		this.rate = rate;
	}

	/**
	 * @return the dbname
	 */
	public final String getDbname() {
		return dbname;
	}

	/**
	 * @return the rate
	 */
	public final RateType getRate() {
		return rate;
	}

	/**
	 * @return the tablename
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

	@Override
	public final String getFullTableName() {
		return dbname + "." + tablename + "_" + rate;
	}

}
