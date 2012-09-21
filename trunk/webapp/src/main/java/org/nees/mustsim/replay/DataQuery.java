package org.nees.mustsim.replay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataQuery {
	private final List<String> channelOrder;
	private final DbTableCreation creation;
	private final String name;
	private final String rateToken = "xRATEx";
	private final String rateColumn = "yRATECOLUMNy";
	private final Map<RateType, String> selects = new HashMap<RateType, String>();

	public DataQuery(List<String> channelOrder, String name,
			DbTableCreation creation) {
		super();
		this.channelOrder = channelOrder;
		this.name = name;
		this.creation = creation;
		createSelects();
	}

	private void createSelects() {
		Map<TableType, String> lists = new HashMap<TableType, String>();
		for (TableType t : TableType.values()) {
			String s = selectString(t);
			if (s != null) {
				lists.put(t, s);
			}
		}
		String raw = "";
		boolean first = true;
		for (TableType t : TableType.values()) {
			String s = lists.get(t);
			if (s != null) {
				raw += (first ? "" : " UNION ") + s;
				first = false;
			}
		}
		raw += ";";
		for (RateType r : RateType.values()) {
			String s = raw.replaceAll(rateToken, r.toString());
			s = s.replaceAll(rateColumn, (r.equals(RateType.CONT) ? "Time"
					: "Step"));
			selects.put(r, s);
		}
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public String getSelect(RateType rate) {
		return selects.get(rate);
	}

	private String selectString(TableType table) {
		List<String> sublist = new ArrayList<String>();
		List<String> tableList = creation.getColumns(table);
		boolean empty = true;
		for (String c : channelOrder) {
			if (tableList.contains(c)) {
				sublist.add(c);
				empty = false;
			}
		}

		if (empty) {
			return null;
		}

		String tableName = table + "_" + rateToken; // Rate is replaced when
													// used
		String result = "SELECT " + rateColumn + " ";
		boolean first = true;
		for (String c : sublist) {
			result += (first ? "" : ", ") + c;
			first = false;
		}
		result += " FROM " + tableName;
		return result;
	}
}
