package org.nees.mustsim.replay.db.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nees.mustsim.replay.db.statement.DbTableSpecs;
import org.nees.mustsim.replay.db.statement.NumberOfColumns;
import org.nees.mustsim.replay.db.statement.RateType;
import org.nees.mustsim.replay.db.statement.TableType;

public class DbQuerySpec {
	/**
	 * @return the noc
	 */
	public NumberOfColumns getNoc() {
		return noc;
	}
	private int[] query2selectMap;
	private final List<String> queryOrder;

	private final DbTableSpecs specs;

	private final String name;
	private final List<DbSelect> select = new ArrayList<DbSelect>();
	private final List<String> selectOrder = new ArrayList<String>();
	private final NumberOfColumns noc;

	public DbQuerySpec(List<String> channelOrder, String name,
			DbTableSpecs specs, RateType rate) {
		super();
		this.queryOrder = new ArrayList<String>();
		for (String c : channelOrder) {
			this.queryOrder.add(specs.getCnr().getId(c));
		}
		this.name = name;
		this.specs = specs;
		this.noc = new NumberOfColumns(channelOrder.size(), rate);
		createSelects();
	}

	private void createSelects() {
		Map<TableType, DbSelect> lists = new HashMap<TableType, DbSelect>();
		selectOrder.clear();
		query2selectMap = new int[queryOrder.size()];
		for (TableType t : TableType.values()) {
			DbSelect s = selectString(t);
			if (s != null) {
				lists.put(t, s);
			}
		}
		for (TableType t : TableType.values()) {
			DbSelect s = lists.get(t);
			if (s != null) {
				select.add(s);
			}
		}
		int i = 0;
		for (String c : queryOrder) {
			query2selectMap[i] = selectOrder.indexOf(c);
			i++;
		}
	}

	/**
	 * @return the channelMap
	 */
	public int[] getQuery2selectMap() {
		return query2selectMap;
	}

	/**
	 * @return the channelOrder
	 */
	public List<String> getQueryOrder() {
		return queryOrder;
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

	public List<DbSelect> getSelect() {
		return select;
	}

	public List<DbSelect> getSelect(double start, double stop) {
		List<DbSelect> result = new ArrayList<DbSelect>();
		for (DbSelect s : select) {
		result.add(s.cloneWithTimeConstraint(" WHERE time BETWEEN " + start + " AND " + stop));
		}
		return result;
	}
	
	public List<DbSelect> getSelect(double start) {
		List<DbSelect> result = new ArrayList<DbSelect>();
		for (DbSelect s : select) {
		result.add(s.cloneWithTimeConstraint(" WHERE time >= " + start));
		}
		return result;
	}

	/**
	 * @return the tableOrder
	 */
	public List<String> getSelectOrder() {
		return selectOrder;
	}

	private DbSelect selectString(TableType table) {
		List<String> sublist = new ArrayList<String>();
		List<String> tableList = specs.getColumns(table);
		boolean empty = true;
		for (String c : queryOrder) {
			if (tableList.contains(c)) {
				sublist.add(c);
				empty = false;
			}
		}

		if (empty) {
			return null;
		}

		String tableName = specs.tableName(table, noc.getRate());
		String result = "SELECT " + getHeaderQuery();
		selectOrder.addAll(sublist);
		for (String c : sublist) {
			result += ", " + c;
		}
		result += " FROM " + tableName;
		return new DbSelect(result, sublist.size() + noc.getTimeNumber(), noc.getRate());
	}
	private String getHeaderQuery() {
		String result = "";
		boolean first = true;
		for (String h : noc.getHeaders()) {
			result += (first ? "" : ", ") + h;
			first = false;
		}
		return result;
	}
}