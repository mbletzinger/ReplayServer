package org.nees.illinois.replay.test.db.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.nees.illinois.replay.db.DbPools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.AssertJUnit;

public class DerbyCreateRemoveDatabase extends MySqlCreateRemoveDatabase {

	private final Logger log = LoggerFactory
			.getLogger(DerbyCreateRemoveDatabase.class);

	public DerbyCreateRemoveDatabase(DbPools pools, String experiment) {
		super(pools, experiment + ";create=true");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.nees.illinois.replay.test.db.utils.MySqlCreateRemoveDatabase#
	 * closeConnection(java.sql.Connection)
	 */
	@Override
	public void closeConnection(Connection connection) {
		boolean gotSQLExc = false;
		try {
			DriverManager.getConnection(connectionUrl + ";shutdown=true");
		} catch (SQLException se) {
			if (se.getSQLState().equals("XJ015")) {
				gotSQLExc = true;
			}
		}
		if (!gotSQLExc) {
			log.error("Database did not shut down normally");
			AssertJUnit.fail();
		} else {
			log.info("Database shut down normally");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.nees.illinois.replay.test.db.utils.MySqlCreateRemoveDatabase#
	 * createDatabase(java.sql.Connection)
	 */
	@Override
	public void createDatabase(Connection connection) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.nees.illinois.replay.test.db.utils.MySqlCreateRemoveDatabase#
	 * generateConnection(boolean)
	 */
	@Override
	public Connection generateConnection(boolean withDatabase) {

		return super.generateConnection(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.nees.illinois.replay.test.db.utils.MySqlCreateRemoveDatabase#
	 * removeDatabase(java.sql.Connection)
	 */
	@Override
	public void removeDatabase(Connection connection) {
	}

}
