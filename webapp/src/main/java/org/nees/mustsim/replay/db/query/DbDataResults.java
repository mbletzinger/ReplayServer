package org.nees.mustsim.replay.db.query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.nees.mustsim.replay.db.data.DoubleMatrix;
import org.nees.mustsim.replay.db.data.Interpolate;
import org.nees.mustsim.replay.db.statement.DbStatement;

public class DbDataResults {
	private final DbStatement dbSt;
	private final DbSelect select;
	private final Logger log = Logger.getLogger(DbDataResults.class);
	public DoubleMatrix getData() {
		ResultSet rs = dbSt.query(select.getSelect());
		int columns = select.getTableOrder().size();
		List<List<Double>> data = new ArrayList<List<Double>>();
		int r = 0;
		int rowSize = 0;;
		
		try {
			
			while (rs.next()) {
				List<Double> row = new ArrayList<Double>();
				log.debug("Processing rs row " + r);
				for (int i = 0; i < columns; i++) {
					double val = rs.getDouble(i + 1);
					if(rs.wasNull()) {
						row.add(null);
					}
					row.add(val);
				}
				if(row.size() > rowSize) {
					rowSize = row.size();
					data.add(row);
				}
				r++;
			}
		} catch (SQLException e) {
			log.error("Result Set fetch failed because ", e);
			return null;
		}
		DoubleMatrix result = new DoubleMatrix(data, rowSize);
		Interpolate intpl = new Interpolate(result);
		intpl.fix();
		return result;
	}
	public DbDataResults(DbStatement dbSt, DbSelect select) {
		super();
		this.dbSt = dbSt;
		this.select = select;
	}
}
