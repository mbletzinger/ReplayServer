package org.nees.illinois.replay.db.statement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.nees.illinois.replay.data.Mtx2Str;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataInsertStatement  implements InsertStatementI {
	private final PrepStatementBuilder prep;
	private final Logger log = LoggerFactory
			.getLogger(DataInsertStatement.class);
	private final 
	public static DataInsertStatement getStatement(String dataTableName,
			int numberOfChannels) {
		String statement = "INSERT INTO " + dataTableName + " VALUES (";
		for (int c = 0; c < numberOfChannels; c++) {
			statement += (c == 0 ? "" : ", ") + "?";
		}
		statement += ")";
		return new DataInsertStatement(dataTableName, statement);
	}

	public DataInsertStatement(Connection connection) {
		this.prep = new PrepStatementBuilder(connection, prepped);
	}

	public boolean add(double[] data) {
		PreparedStatement statement = prep.getStatement();
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

	@Override
	public void start() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void add(List<Object> record) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getBuilder() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setConnection(Connection connection) {
		// TODO Auto-generated method stub
		
	}
}
