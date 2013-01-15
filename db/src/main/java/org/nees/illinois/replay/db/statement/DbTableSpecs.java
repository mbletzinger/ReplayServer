package org.nees.illinois.replay.db.statement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nees.illinois.replay.data.NumberOfColumns;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.data.TableType;
import org.nees.illinois.replay.registries.ChannelLookups;
import org.nees.illinois.replay.registries.ChannelNameRegistry;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class DbTableSpecs extends ChannelLookups {

	private final Map<TableType, List<String>> columns = new HashMap<TableType, List<String>>();

	private String dbname;
	public DbTableSpecs(ChannelNameRegistry cnr, String dbname) {
		super(cnr);
		this.dbname = dbname;
	}

	@Inject
	public DbTableSpecs(@Named("dbname")String dbname) {
		super();
		this.dbname = dbname;
	}

	private String addHeaders(RateType rate) {
		NumberOfColumns noc = new NumberOfColumns(1, rate);
		String result = "";
		boolean first = true;
		for (String h : noc.getHeaders()) {
			result += (first ? "" : ", ") + h + " double NOT NULL";
			first = false;
		}
		return result;
	}

	public void addTable(TableType table, List<String> channels) {
		List<String> list = new ArrayList<String>();
		list.addAll(lookupChannels(table, channels));
		columns.put(table, list);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.nees.illinois.replay.registries.ChannelLookups#clone()
	 */
	@Override
	public ChannelLookups clone() {
		ChannelNameRegistry clone = new ChannelNameRegistry();
		clone.init(getCnr().getClone(), getCnr().getAfterLastChannel());
		DbTableSpecs result = new DbTableSpecs(clone, dbname);
		result.columns.putAll(columns);
		return result;
	}

	public String createTableStatement(TableType table, RateType rate) {
		String result = "CREATE TABLE " + tableName(table, rate) + "(";
		String header = addHeaders(rate);
		result += header;
		List<String> channels = columns.get(table);
		for (String channel : channels) { // we are assuming that the slashes
											// have been converted to underlines
											// here and underlines to double
											// underlines
			result += ", " + channel + " double NOT NULL";
		}
		result += ")";
		return result;
	}

	/**
	 * @return the columns
	 */
	public Map<TableType, List<String>> getColumns() {
		return columns;
	}

	public List<String> getColumns(TableType table) {
		List<String> result = new ArrayList<String>();
		List<String> cols = columns.get(table);
		if (cols != null) {
			result.addAll(cols);
		}
		return result;
	}

	/**
	 * @return the dbname
	 */
	public String getDbname() {
		return dbname;
	}

	/**
	 * @param dbname
	 *            the dbname to set
	 */
	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	public String tableName(TableType table, RateType rate) {
		return dbname + "." + table + "_" + rate;
	}
}
