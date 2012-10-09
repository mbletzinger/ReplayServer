package org.nees.mustsim.replay.db.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nees.mustsim.replay.db.table.DbTableCreation;
import org.nees.mustsim.replay.db.table.RateType;
import org.nees.mustsim.replay.db.table.TableType;

public class DbSelect {
	private int [] channelMap;
	private final List<String> channelOrder;

	private final DbTableCreation creation;

	private final String name;

	private final String rateColumn = "yRATECOLUMNy";
	private final String rateToken = "xRATEx";
	private final Map<RateType, String> selects = new HashMap<RateType, String>();
	private final List<String> tableOrder = new ArrayList<String>();
	public DbSelect(List<String> channelOrder, String name,
			DbTableCreation creation) {
		super();
		this.channelOrder = new ArrayList<String>();
		for(String c : channelOrder) {
			this.channelOrder.add(creation.getCnr().getId(c));
		}
		this.name = name;
		this.creation = creation;
		createSelects();
	}
	private void createSelects() {
		Map<TableType, String> lists = new HashMap<TableType, String>();
		tableOrder.clear();
		channelMap = new int[channelOrder.size()];
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
		raw += "";
		for (RateType r : RateType.values()) {
			String s = raw.replaceAll(rateToken, r.toString());
			s = s.replaceAll(rateColumn, (r.equals(RateType.CONT) ? "Time"
					: "Step"));
			selects.put(r, s);
		}
		int i = 0;
		for (String c : channelOrder) {
			channelMap[i] = tableOrder.indexOf(c);
			i++;
		}
	}
	/**
	 * @return the channelMap
	 */
	public int[] getChannelMap() {
		return channelMap;
	}
	/**
	 * @return the channelOrder
	 */
	public List<String> getChannelOrder() {
		return channelOrder;
	}

	/**
	 * @return the creation
	 */
	public DbTableCreation getCreation() {
		return creation;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public String getSelect() {
		return selects.get(RateType.STEP);
	}
	
	public String getSelect(double start, double stop) {
		String result = selects.get(RateType.CONT);
		result += " WHERE time BETWEEN " + start + " AND " + stop;
		return result;
	}

	/**
	 * @return the tableOrder
	 */
	public List<String> getTableOrder() {
		return tableOrder;
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
		tableOrder.addAll(sublist);
		for (String c : sublist) {
			result += (first ? "" : ", ") + c;
			first = false;
		}
		result += " FROM " + tableName;
		return result;
	}
}
