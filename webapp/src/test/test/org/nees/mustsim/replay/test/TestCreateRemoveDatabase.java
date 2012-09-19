package org.nees.mustsim.replay.test;
import java.sql.DriverManager;
import java.sql.SQLException;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nees.mustsim.replay.test.utils.DerbyDbControl;

public class TestCreateRemoveDatabase {
	private DerbyDbControl derby = new DerbyDbControl();
	private final Logger log = Logger.getLogger(TestCreateRemoveDatabase.class);

	@Before
	public void setUp() throws Exception {
		derby.startDerby();
	}

	@After
	public void tearDown() throws Exception {
		derby.stopDerby();
	}

	@Test
	public void createDatabase() {
		String driver = "org.apache.derby.jdbc.EmbeddedDriver";
		String dbName = "TestDB";
		String connectionURL = "jdbc:derby:" + dbName + ";create=true";
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e1) {
			log.error("connection failed because " + driver
					+ " could not be found");
			Assert.fail();
		}
		log.info("driver loaded");
		try {
			DriverManager.getConnection(connectionURL);
		} catch (SQLException e) {
			log.error("connection failed");
			Assert.fail();
		}
		log.info("connected to database");
		boolean gotSQLExc = false;
		try {
			DriverManager.getConnection("jdbc:derby:;shutdown=true");
		} catch (SQLException se) {
			if (se.getSQLState().equals("XJ015")) {
				gotSQLExc = true;
			}
		}
		if (!gotSQLExc) {
			log.error("Database did not shut down normally");
			Assert.fail();
		} else {
			log.info("Database shut down normally");
		}

	}

}
