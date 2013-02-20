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

public class TableQueriesList extends SavedQuery {
	private int[] query2selectMap;

	private final List<SavedTableQuery> select = new ArrayList<SavedTableQuery>();
	private final List<String> selectOrder = new ArrayList<String>();
	private final DbTablesMap specs;
	private final Logger log = LoggerFactory.getLogger(TableQueriesList.class);
	public TableQueriesList(List<String> channelOrder, String name,
			DbTablesMap specs, RateType rate) {
		super(channelOrder, name, specs.getCnr(), rate);
		this.specs = specs;
		createSelects();
	}

	private SavedTableQuery createSelect(TableType table) {
		List<String> sublist = new ArrayList<String>();
		List<String> tableList = specs.getColumns(table);
		if(tableList.isEmpty()) {
			log.debug("No channels for " + table + " are specified");
			return null;
		}
		log.debug("extracting selector order for " + table + " from" + tableList + " and " + queryOrder);
	
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
		log.debug("selectOrder is " + sublist);
		selectOrder.addAll(sublist);
		for (String c : sublist) {
			result += ", " + c;
		}
		result += " FROM " + tableName;
		return new SavedTableQuery(result, sublist.size(), noc.getRate());
	}

	private void createSelects() {
		Map<TableType, SavedTableQuery> lists = new HashMap<TableType, SavedTableQuery>();
		selectOrder.clear();
		query2selectMap = new int[queryOrder.size()];
		for (TableType t : TableType.values()) {
			SavedTableQuery s = createSelect(t);
			if (s != null) {
				lists.put(t, s);
			}
		}
		for (TableType t : TableType.values()) {
			SavedTableQuery s = lists.get(t);
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

	public List<SavedTableQuery> getSelect() {
		return select;
	}

	public List<SavedTableQuery> getSelect(double start) {
		List<SavedTableQuery> result = new ArrayList<SavedTableQuery>();
		for (SavedTableQuery s : select) {
			result.add(s.cloneWithTimeConstraint(" WHERE time >= " + start));
		}
		return result;
	}

	public List<SavedTableQuery> getSelect(double start, double stop) {
		List<SavedTableQuery> result = new ArrayList<SavedTableQuery>();
		for (SavedTableQuery s : select) {
			result.add(s.cloneWithTimeConstraint(" WHERE time BETWEEN " + start
					+ " AND " + stop));
		}
		return result;
	}

	public List<SavedTableQuery> getSelect(StepNumber start) {
		List<SavedTableQuery> result = new ArrayList<SavedTableQuery>();
		for (SavedTableQuery s : select) {
			result.add(s.cloneWithTimeConstraint(" WHERE step >= "
					+ start.getStep()));
		}
		return result;
	}

	public List<SavedTableQuery> getSelect(StepNumber start, StepNumber stop) {
		List<SavedTableQuery> result = new ArrayList<SavedTableQuery>();
		for (SavedTableQuery s : select) {
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
}
