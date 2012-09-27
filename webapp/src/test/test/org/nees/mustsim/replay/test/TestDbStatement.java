package org.nees.mustsim.replay.test;

import java.sql.ResultSet;
import java.sql.SQLException;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nees.mustsim.replay.db.DbConnections;
import org.nees.mustsim.replay.db.statement.DbStatement;
import org.nees.mustsim.replay.test.utils.TestPrepStatement;

public class TestDbStatement {
	private DbConnections dbc;
	private double[][] data = new double[5][2];
	private final Logger log = Logger.getLogger(TestDbStatement.class);

	@Before
	public void setUp() throws Exception {
		dbc = new DbConnections("org.apache.derby.jdbc.ClientDriver", "TESTDB",
				"jdbc:derby://localhost:1527/", true);
		for (int i = 0; i < 5; i++) {
			data[i][0] = i * 0.5 + .001;
			data[i][1] = i * -0.02 + .001;
		}
	}

	@After
	public void tearDown() throws Exception {
		DbStatement dbSt = dbc.createDbStatement();
		dbSt.noComplaints("DROP TABLE TestTable");
		dbSt.close();
		dbc.close();
	}

	@Test
	public void testExecute() {
		DbStatement dbSt = dbc.createDbStatement();
		boolean result = dbSt
				.execute("CREATE TABLE TestTable (col1 double, col2 double)");
		Assert.assertTrue(result);
		result = dbSt.execute("DROP TABLE TestTable");
		Assert.assertTrue(result);
		dbSt.close();
	}

	@Test
	public void testCreatePrepStatement() {
		String tblName = "TestTable";
		DbStatement dbSt = dbc.createDbStatement();
		dbSt.execute("CREATE TABLE " + tblName + " (col1 double, col2 double)");
		TestPrepStatement prep = new TestPrepStatement(tblName);
		dbSt.createPrepStatement(prep);
		for (int i = 0; i < 5; i++) {
			prep.add(data[i][0], data[i][1]);
		}
		int [] result = prep.execute();
		Assert.assertNotNull(result);
		dbSt.execute("DROP TABLE TestTable");
		dbSt.close();
	}

	@Test
	public void testQuery1() {
		String tblName = "TestTable";
		DbStatement dbSt = dbc.createDbStatement();
		dbSt.execute("CREATE TABLE " + tblName + " (col1 double, col2 double)");
		double[][] result = new double[5][2];
		for (int i = 0; i < 5; i++) {
			TestPrepStatement prep = new TestPrepStatement(tblName);
			dbSt.createPrepStatement(prep);
			prep.add(data[i][0], data[i][1]);
			prep.execute();
			ResultSet rs = dbSt.query("SELECT * FROM " + tblName);
			int r = 0;
			try {
				while (rs.next()) {
					result[r][0] = rs.getDouble("col1");
					result[r][1] = rs.getDouble("col2");
					r++;
				}
			} catch (SQLException e) {
				log.error("Result Set fetch failed because ", e);
				Assert.fail();
			}
			String rStr1 = "Result = [";
			String rStr2 = "              [";
			for ( int k = 0; k < 5; k++) {
				rStr1 += (k == 0 ? "" : ", ") + result[k][0];
				rStr2 += (k == 0 ? "" : ", ") + result[k][1];
			}
			log.info(rStr1 + "]\n" + rStr2 + "]");
			dbSt.closeQuery(rs);
		}
		for (int i = 0; i < 5; i++) {
			Assert.assertEquals(data[i][0], result[i][0], .001);
			Assert.assertEquals(data[i][1], result[i][1], .001);
		}
		dbSt.execute("DROP TABLE TestTable");
		dbSt.close();
	}

	@Test
	public void testQuery2() {
		String tblName = "TestTable";
		DbStatement dbSt = dbc.createDbStatement();
		dbSt.execute("CREATE TABLE " + tblName + " (col1 double, col2 double)");
		TestPrepStatement prep = new TestPrepStatement(tblName);
		dbSt.createPrepStatement(prep);
		for (int i = 0; i < 5; i++) {
			prep.add(data[i][0], data[i][1]);
		}
		prep.execute();
		ResultSet rs = dbSt.query("SELECT * FROM " + tblName);
		double[][] result = new double[5][2];
		log.info("result size is [" + result.length + "][" + result[0].length + "]");
		int r = 0;
		try {
			while (rs.next()) {
				result[r][0] = rs.getDouble("col1");
				result[r][1] = rs.getDouble("col2");
				r++;
			}
		} catch (SQLException e) {
			log.error("Result Set fetch failed because ", e);
			Assert.fail();
		}
		dbSt.closeQuery(rs);
		for (int i = 0; i < 5; i++) {
			Assert.assertEquals(data[i][0], result[i][0], .001);
			Assert.assertEquals(data[i][1], result[i][1], .001);
		}
		dbSt.execute("DROP TABLE TestTable");
		dbSt.close();
	}

}
