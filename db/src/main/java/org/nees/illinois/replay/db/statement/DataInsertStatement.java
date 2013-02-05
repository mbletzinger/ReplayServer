package org.nees.illinois.replay.db.statement;

import java.sql.SQLException;

import org.nees.illinois.replay.data.Mtx2Str;
import org.slf4j.LoggerFactory;

public class DataInsertStatement extends PrepStatement {
	public static DataInsertStatement getStatement(String dataTableName,
			int numberOfChannels) {
		String statement = "INSERT INTO " + dataTableName + " VALUES (";
		for (int c = 0; c < numberOfChannels; c++) {
			statement += (c == 0 ? "" : ", ") + "?";
		}
		statement += ")";
		return new DataInsertStatement(dataTableName, statement);
	}

	public DataInsertStatement(String dataTableName, String statement) {
		super(statement, LoggerFactory.getLogger(DataInsertStatement.class));
	}

	public boolean add(double[] data) {
		log.debug("Adding " + Mtx2Str.array2String(data));
		try {
			for (int c = 0; c < data.length; c++) {
				statement.setDouble(c + 1, data[c]);
			}
			statement.addBatch();
		} catch (SQLException e) {
			log.error("Cannot add data because ", e);
			return false;
		}
		return true;
	}
}
