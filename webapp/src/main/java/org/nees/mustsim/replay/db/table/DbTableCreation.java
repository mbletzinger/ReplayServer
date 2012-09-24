package org.nees.mustsim.replay.db.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DbTableCreation {
	private final Map<TableType, List<String>> columns = new HashMap<TableType, List<String>>();

	private final String dbname;

	public DbTableCreation(String dbname) {
		super();
		this.dbname = dbname;
	}

	private String[] addHeaders(RateType rate) {
		if (rate.equals(RateType.CONT)) {
			return new String[] { "Time double NOT NULL", "PRIMARY KEY (Time)" };
		} else {
			return new String[] {
					"Time double NOT NULL, Step int NOT NULL, Substep int NOT NULL, CorrectionStep int NOT NULL",
					"PRIMARY KEY (Step)" };
		}
	}

	public void addTable(TableType table, List<String> channels) {
		List<String> list = new ArrayList<String>();
		list.addAll(channels);
		columns.put(table, list);
	}

	public String createTableStatement(TableType table, RateType rate) {
		String result = "CREATE TABLE " + tableName(table, rate) + "(";
		String[] headers = addHeaders(rate);
		result += headers[0];
		List<String> channels = columns.get(table);
		for (String channel : channels) { // we are assuming that the slashes
											// have been converted to underlines
											// here and underlines to double
											// underlines
			result += ", " + channel + " double NOT NULL";
		}
		result += ", " + headers[1] + ")";
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
