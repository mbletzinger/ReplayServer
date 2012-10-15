package org.nees.mustsim.replay.db.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nees.mustsim.replay.db.statement.DbTableSpecs;
import org.nees.mustsim.replay.db.statement.RateType;
import org.nees.mustsim.replay.db.statement.TableType;

public class DbSelect {
	private int[] channelMap;
	private final List<String> channelOrder;

	private final DbTableSpecs specs;

	private final String name;
	private final String rateColumn;
	private String select;
	private final RateType rate;
	private final List<String> tableOrder = new ArrayList<String>();

	public DbSelect(List<String> channelOrder, String name,
			DbTableSpecs creation, RateType rate) {
		super();
		this.channelOrder = new ArrayList<String>();
		for (String c : channelOrder) {
			this.channelOrder.add(creation.getCnr().getId(c));
		}
		this.name = name;
		this.specs = creation;
		this.rate = rate;
		rateColumn = (rate.equals(RateType.CONT) ? "time" : "step");
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
		boolean first = true;
		for (TableType t : TableType.values()) {
			String s = lists.get(t);
			if (s != null) {
				select += (first ? "" : " UNION ") + s;
				first = false;

			}
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
	public DbTableSpecs getSpecs() {
		return specs;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public String getSelect() {
		return select;
	}

	public String getSelect(double start, double stop) {
		return select +  " WHERE time BETWEEN " + start + " AND " + stop;
	}
	public String getSelect(double start) {
		return select +  " WHERE time GREATER THAN " + start;
	}

	/**
	 * @return the tableOrder
	 */
	public List<String> getTableOrder() {
		return tableOrder;
	}

	private String selectString(TableType table) {
		List<String> sublist = new ArrayList<String>();
		List<String> tableList = specs.getColumns(table);
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

		String tableName = specs.tableName(table, rate);
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
