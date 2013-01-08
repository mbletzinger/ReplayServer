package org.nees.illinois.replay.db.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nees.illinois.replay.data.ChannelUpdates;
import org.nees.illinois.replay.data.QuerySpec;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.data.StepNumber;
import org.nees.illinois.replay.data.TableType;
import org.nees.illinois.replay.db.statement.DbTableSpecs;

public class DbQuerySpec extends QuerySpec {
	private int[] query2selectMap;

	private final List<DbSelect> select = new ArrayList<DbSelect>();
	private final List<String> selectOrder = new ArrayList<String>();
	private final DbTableSpecs specs;

	public DbQuerySpec(List<String> channelOrder, String name,
			DbTableSpecs specs, RateType rate) {
		super(channelOrder, name, specs.getCnr(), rate);
		this.specs = specs;
		createSelects();
	}

	private DbSelect createSelect(TableType table) {
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
		return new DbSelect(result, sublist.size(), noc.getRate());
	}

	private void createSelects() {
		Map<TableType, DbSelect> lists = new HashMap<TableType, DbSelect>();
		selectOrder.clear();
		query2selectMap = new int[queryOrder.size()];
		for (TableType t : TableType.values()) {
			DbSelect s = createSelect(t);
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

	private String getHeaderQuery() {
		String result = "";
		boolean first = true;
		for (String h : noc.getHeaders()) {
			result += (first ? "" : ", ") + h;
			first = false;
		}
		return result;
	}

	/**
	 * @return the channelMap
	 */
	public int[] getQuery2selectMap() {
		return query2selectMap;
	}

	public List<DbSelect> getSelect() {
		return select;
	}

	public List<DbSelect> getSelect(double start) {
		List<DbSelect> result = new ArrayList<DbSelect>();
		for (DbSelect s : select) {
			result.add(s.cloneWithTimeConstraint(" WHERE time >= " + start));
		}
		return result;
	}

	public List<DbSelect> getSelect(double start, double stop) {
		List<DbSelect> result = new ArrayList<DbSelect>();
		for (DbSelect s : select) {
			result.add(s.cloneWithTimeConstraint(" WHERE time BETWEEN " + start
					+ " AND " + stop));
		}
		return result;
	}

	public List<DbSelect> getSelect(StepNumber start) {
		List<DbSelect> result = new ArrayList<DbSelect>();
		for (DbSelect s : select) {
			result.add(s.cloneWithTimeConstraint(" WHERE step >= "
					+ start.getStep()));
		}
		return result;
	}

	public List<DbSelect> getSelect(StepNumber start, StepNumber stop) {
		List<DbSelect> result = new ArrayList<DbSelect>();
		for (DbSelect s : select) {
			result.add(s.cloneWithTimeConstraint(" WHERE step BETWEEN "
					+ start.getStep() + " AND " + stop.getStep()));
		}
		return result;
	}

	/**
	 * @return the tableOrder
	 */
	public List<String> getSelectOrder() {
		return selectOrder;
	}

	/**
	 * @return the creation
	 */
	public ChannelUpdates getSpecs() {
		return specs;
	}
}
