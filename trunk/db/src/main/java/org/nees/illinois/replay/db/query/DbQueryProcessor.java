package org.nees.illinois.replay.db.query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.MatrixFix;
import org.nees.illinois.replay.data.MergeSet;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.data.StepNumber;
import org.nees.illinois.replay.db.statement.StatementProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbQueryProcessor {
	public enum QueryType {
		Cont, ContWithStop, Step, StepWithStart, StepWithStop
	}

	private final TableQueriesList dbSel;
	private final StatementProcessor dbSt;

	private final Logger log = LoggerFactory.getLogger(DbQueryProcessor.class);

	public DbQueryProcessor(StatementProcessor dbSt, TableQueriesList dbSel) {
		super();
		this.dbSt = dbSt;
		this.dbSel = dbSel;
	}

	public DoubleMatrix getData(QueryType qtype, double start, double stop) {
		List<SavedTableQuery> selectList = null;
		MergeSet mSet = null;
		if (qtype.equals(QueryType.Cont)) {
			selectList = dbSel.getSelect(start);
			mSet = new MergeSet(RateType.CONT);
		}
		if (qtype.equals(QueryType.ContWithStop)) {
			selectList = dbSel.getSelect(start, stop);
			mSet = new MergeSet(RateType.CONT);
		}
		return collectQueries(selectList, mSet);
	}

	public DoubleMatrix getData(QueryType qtype, StepNumber start,
			StepNumber stop) {
		List<SavedTableQuery> selectList = null;
		MergeSet mSet = null;
		if (qtype.equals(QueryType.Step)) {
			selectList = dbSel.getSelect();
			mSet = new MergeSet(RateType.STEP);
		}
		if (qtype.equals(QueryType.Cont)) {
			selectList = dbSel.getSelect(start);
			mSet = new MergeSet(RateType.CONT);
		}
		if (qtype.equals(QueryType.ContWithStop)) {
			selectList = dbSel.getSelect(start, stop);
			mSet = new MergeSet(RateType.CONT);
		}
		for (SavedTableQuery s : selectList) {
			List<List<Double>> set = singleQuery(s);
			if (set != null) {
				mSet.merge(set);
			}
		}
		return collectQueries(selectList, mSet);
	}

	private DoubleMatrix collectQueries(List<SavedTableQuery> selectList, MergeSet mSet) {
		for (SavedTableQuery s : selectList) {
			List<List<Double>> set = singleQuery(s);
			if (set != null) {
				mSet.merge(set);
			}
		}
		if(mSet.isEmpty()) {
			return null;
		}
		DoubleMatrix result = new DoubleMatrix(mSet.getRecords(),
				mSet.getColumnSize(true));
		MatrixFix intpl = new MatrixFix(result);
		intpl.fix();
		return result;
	}

	private List<List<Double>> singleQuery(SavedTableQuery select) {
		ResultSet rs = dbSt.query(select.getSelect());
		int columns = select.getNumber(true);
		List<List<Double>> data = new ArrayList<List<Double>>();
		int r = 0;
		int rowSize = 0;
		if (rs == null) {
			return null;
		}
		try {

			while (rs.next()) {
				List<Double> row = new ArrayList<Double>();
				for (int i = 0; i < columns; i++) {
					double val = rs.getDouble(i + 1);
					if (rs.wasNull()) {
						row.add(null);
					}
					row.add(val);
				}
				if (row.size() > rowSize) {
					rowSize = row.size();
				}
				data.add(row);
				r++;
			}
			log.debug("Processed " + r + " rows");
		} catch (SQLException e) {
			log.error("Result Set fetch failed because ", e);
			return null;
		}
		log.debug("Query returned " + new DoubleMatrix(data, columns));
		return data;
	}
}
