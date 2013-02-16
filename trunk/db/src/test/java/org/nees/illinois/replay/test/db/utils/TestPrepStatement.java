package org.nees.illinois.replay.test.db.utils;

import java.sql.SQLException;

import org.nees.illinois.replay.db.statement.PrepStatement;
import org.slf4j.LoggerFactory;

public class TestPrepStatement extends PrepStatement {

	public TestPrepStatement(String dbTableName) {
		super("INSERT INTO " + dbTableName + "  VALUES(?,?)", LoggerFactory.getLogger(TestPrepStatement.class));
	}
	public boolean add(double x1, double x2) {
		try {
			statement.setDouble(1, x1);
			statement.setDouble(2, x2);
			statement.addBatch();
		} catch (SQLException e) {
			log.error("Cannot add x1 & x2 because ", e);
			return false;
		}
		return true;		
	}
}
