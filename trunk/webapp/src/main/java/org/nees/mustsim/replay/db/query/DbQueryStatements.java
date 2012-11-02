package org.nees.mustsim.replay.db.query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.nees.mustsim.replay.data.DoubleMatrix;
import org.nees.mustsim.replay.data.Interpolate;
import org.nees.mustsim.replay.data.MergeSet;
import org.nees.mustsim.replay.data.RateType;
import org.nees.mustsim.replay.data.StepNumber;
import org.nees.mustsim.replay.db.statement.DbStatement;

public class DbQueryStatements {
	public enum QueryType {
		Cont, ContWithStop, Step, StepWithStart, StepWithStop
	}
	private final DbQuerySpec dbSel;
	private final DbStatement dbSt;

	private final Logger log = Logger.getLogger(DbQueryStatements.class);;

	public DbQueryStatements(DbStatement dbSt, DbQuerySpec dbSel) {
		super();
		this.dbSt = dbSt;
		this.dbSel = dbSel;
	}

	public DoubleMatrix getData(QueryType qtype, double start,
			double stop) {
		List<DbSelect> selectList = null;
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

	public DoubleMatrix getData(QueryType qtype, StepNumber start, StepNumber stop) {
		List<DbSelect> selectList = null;
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
		for (DbSelect s : selectList) {
			List<List<Double>> set = singleQuery(s);
			if (set != null) {
				mSet.merge(set);			
			}
		}
		return collectQueries(selectList, mSet);
	}

	private DoubleMatrix collectQueries(List<DbSelect> selectList, MergeSet mSet) {
		for (DbSelect s : selectList) {
			List<List<Double>> set = singleQuery(s);
			if (set != null) {
				mSet.merge(set);			
			}
		}
		DoubleMatrix result = new DoubleMatrix(mSet.getRecords(), mSet.getColumnSize(true));
		Interpolate intpl = new Interpolate(result);
		intpl.fix();
		return result;		
	}
	private List<List<Double>> singleQuery(DbSelect select) {
		ResultSet rs = dbSt.query(select.getSelect());
		int columns = select.getNumber(true);
		List<List<Double>> data = new ArrayList<List<Double>>();
		int r = 0;
		int rowSize = 0;
		if(rs == null) {
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
