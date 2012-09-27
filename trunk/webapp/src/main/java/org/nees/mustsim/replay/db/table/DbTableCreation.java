package org.nees.mustsim.replay.db.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nees.mustsim.replay.db.data.ChannelNameRegistry;


public class DbTableCreation {
	/**
	 * @return the cnr
	 */
	public ChannelNameRegistry getCnr() {
		return cnr;
	}

	private final Map<TableType, List<String>> columns = new HashMap<TableType, List<String>>();
	private final ChannelNameRegistry cnr;
	private final String dbname;


	public DbTableCreation(ChannelNameRegistry cnr, String dbname) {
		super();
		this.cnr = cnr;
		this.dbname = dbname;
	}

	private String addHeaders(RateType rate) {
		if (rate.equals(RateType.CONT)) {
			return "Time double NOT NULL";
		} else {
			return "Time double NOT NULL, Step double NOT NULL, Substep double NOT NULL, CorrectionStep double NOT NULL";
		}
	}

	public void addTable(TableType table, List<String> channels) {
		List<String> list = new ArrayList<String>();
		list.addAll(lookupChannels(table, channels));
		columns.put(table, list);
	}

	private List<String> lookupChannels(TableType table, List<String> channels) {
		List<String> result = new ArrayList<String>();
		for (String c : channels) {
			String dc = cnr.addChannel(table, c);
			result.add(dc);
		}
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
		result +=  ")";
		return result;
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

	public int recordLength(TableType table, RateType rate) {
		int result = columns.get(table).size();
		if (rate.equals(RateType.CONT)) {
			result++;
		} else {
			result += 4;
		}
		return result;
	}

	public String tableName(TableType table, RateType rate) {
		return dbname + "." + table + "_" + rate;
	}
}
