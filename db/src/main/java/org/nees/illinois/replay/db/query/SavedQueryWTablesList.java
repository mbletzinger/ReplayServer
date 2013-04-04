package org.nees.illinois.replay.db.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.data.StepNumber;
import org.nees.illinois.replay.data.TableType;
import org.nees.illinois.replay.db.statement.DbTablesMap;
import org.nees.illinois.replay.registries.ChannelNameManagement;
import org.nees.illinois.replay.registries.SavedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SavedQueryWTablesList extends SavedQuery {

	private final Logger log = LoggerFactory.getLogger(SavedQueryWTablesList.class);

	private int[] query2selectMap;

	private final List<NumberOfColumnsWithSelect> select = new ArrayList<NumberOfColumnsWithSelect>();
	private final List<String> selectOrder = new ArrayList<String>();
	private final DbTablesMap specs;

	public SavedQueryWTablesList(List<String> channelOrder, String name,
			DbTablesMap specs, RateType rate) {
		super(channelOrder, name, specs.getCnr(), rate);
		this.specs = specs;
		createSelects();
	}

	private NumberOfColumnsWithSelect createSelect(TableType table) {
		List<String> sublist = new ArrayList<String>();
		List<String> tableList = specs.getColumns(table);
		if (tableList.isEmpty()) {
			log.debug("No channels for " + table + " are specified");
			return null;
		}
		log.debug("extracting selector order for " + table + " from"
				+ tableList + " and " + queryOrder);

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

		String tableName = specs.tableName(table, noc.getTableRate());
		String result = "SELECT " + getHeaderQuery();
		log.debug("selectOrder is " + sublist);
		selectOrder.addAll(sublist);
		for (String c : sublist) {
			result += ", " + c;
		}
		result += " FROM " + tableName;
		return new NumberOfColumnsWithSelect(result, sublist.size(), noc.getTableRate());
	}

	private void createSelects() {
		Map<TableType, NumberOfColumnsWithSelect> lists = new HashMap<TableType, NumberOfColumnsWithSelect>();
		selectOrder.clear();
		query2selectMap = new int[queryOrder.size()];
		for (TableType t : TableType.values()) {
			NumberOfColumnsWithSelect s = createSelect(t);
			if (s != null) {
				lists.put(t, s);
			}
		}
		for (TableType t : TableType.values()) {
			NumberOfColumnsWithSelect s = lists.get(t);
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

	public List<NumberOfColumnsWithSelect> getSelect() {
		return select;
	}

	public List<NumberOfColumnsWithSelect> getSelect(double start) {
		List<NumberOfColumnsWithSelect> result = new ArrayList<NumberOfColumnsWithSelect>();
		for (NumberOfColumnsWithSelect s : select) {
			result.add(s.cloneWithTimeConstraint(" WHERE time >= " + start));
		}
		return result;
	}

	public List<NumberOfColumnsWithSelect> getSelect(double start, double stop) {
		List<NumberOfColumnsWithSelect> result = new ArrayList<NumberOfColumnsWithSelect>();
		for (NumberOfColumnsWithSelect s : select) {
			result.add(s.cloneWithTimeConstraint(" WHERE time BETWEEN " + start
					+ " AND " + stop));
		}
		return result;
	}

	public List<NumberOfColumnsWithSelect> getSelect(StepNumber start) {
		List<NumberOfColumnsWithSelect> result = new ArrayList<NumberOfColumnsWithSelect>();
		for (NumberOfColumnsWithSelect s : select) {
			result.add(s.cloneWithTimeConstraint(" WHERE step >= "
					+ start.getStep()));
		}
		return result;
	}

	public List<NumberOfColumnsWithSelect> getSelect(StepNumber start, StepNumber stop) {
		List<NumberOfColumnsWithSelect> result = new ArrayList<NumberOfColumnsWithSelect>();
		for (NumberOfColumnsWithSelect s : select) {
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
	public ChannelNameManagement getSpecs() {
		return specs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.nees.illinois.replay.registries.SavedQuery#toString()
	 */
	@Override
	public String toString() {
		String result = super.toString();
		result += "/select=" + select + "/selectOrder=" + selectOrder;
		return result;
	}
}
