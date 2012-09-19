package org.nees.mustsim.replay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbTables {
	public enum tableTypes { OM, DAQ, Krypton1, Krypton2};
	public enum rateTypes { CONT, STEP };
	private final Map<tableTypes, List<String>> columns = new HashMap<tableTypes, List<String>>();
	private final String dbname;
	public DbTables(String dbname) {
		super();
		this.dbname = dbname;
	}
	public void addTable(tableTypes table, List<String> channels) {
		List<String> list = new ArrayList<String>();
		list.addAll(channels);
		columns.put(table, list);
	}
	public String createTableStatement(tableTypes table, rateTypes rate) {
		String result = "CREATE TABLE " + dbname + "." + table + "_" + rate + "(";
		String [] headers = addHeaders(rate);
		result += headers[0];
		List<String> channels = columns.get(table);
		for (String channel : channels) { // we are assuming that the slashes have been converted to underlines here and underlines to double underlines
			result += ", " + channel + " double NOT NULL"; 
		}
		result += ", " + headers[1] + ");";
		return result;
	}
	private String [] addHeaders(rateTypes rate) {
		if (rate.equals(rateTypes.CONT)) {
			return new String [] { "Time double NOT NULL", "PRIMARY KEY (Time)" };
		}
		else {
			return new String [] { "Time double NOT NULL, Step int NOT NULL, Substep int NOT NULL, CorrectionStep int NOT NULL",  "PRIMARY KEY (Step)" };
		}
	}
}
