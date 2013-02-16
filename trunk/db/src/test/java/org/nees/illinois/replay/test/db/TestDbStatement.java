package org.nees.illinois.replay.test.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.nees.illinois.replay.db.DbPools;
import org.nees.illinois.replay.db.statement.DbStatement;
import org.nees.illinois.replay.test.db.derby.process.DerbyDbControl;
import org.nees.illinois.replay.test.db.utils.DbTestsModule;
import org.nees.illinois.replay.test.db.utils.TestPrepStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class TestDbStatement {
	private double[][] data = new double[5][2];
	private DbPools pools;
	private final DerbyDbControl ddbc = new DerbyDbControl();
	final String experiment = "HybridMasonry1";
	DbTestsModule guiceMod;
	private boolean ismysql;
	private final Logger log = LoggerFactory.getLogger(TestDbStatement.class);

	@Parameters("db")
	@BeforeClass
	public void setUp(@Optional("db") String db) throws Exception {
		guiceMod = new DbTestsModule(db);
		guiceMod.setExperiment("HybridMasonry1");
		Injector injector = Guice.createInjector(guiceMod);
		pools = injector.getInstance(DbPools.class);
		ismysql = db.equals("mysql");
		if (ismysql == false) {
			ddbc.startDerby();
		}
		pools.getOps().createDatabase(experiment);
		
		for (int i = 0; i < 5; i++) {
			data[i][0] = i * 0.5 + .001;
			data[i][1] = i * -0.02 + .001;
		}
	}

	@AfterMethod
	public void tearDown() throws Exception {
		DbStatement dbSt = pools.createDbStatement(experiment,false);
		dbSt.noComplaints("DROP TABLE TestTable");
		dbSt.close();
	}

	@AfterClass
	public void teardown1() {
		try {
			pools.getOps().removeDatabase("HybridMasonry1");
		} catch (Exception e) {
			log.error("Database remove failed because ", e);
			AssertJUnit.fail();
		}
		if (ismysql == false) {
			ddbc.stopDerby();
		}
	}

	@Test
	public void testCreatePrepStatement() {
		String tblName = "TestTable";
		DbStatement dbSt = pools.createDbStatement(experiment,true);
		dbSt.execute("CREATE TABLE " + tblName + " (col1 double, col2 double)");
		TestPrepStatement prep = new TestPrepStatement(tblName);
		dbSt.createPrepStatement(prep);
		for (int i = 0; i < 5; i++) {
			prep.add(data[i][0], data[i][1]);
		}
		int[] result = prep.execute();
		AssertJUnit.assertNotNull(result);
		dbSt.execute("DROP TABLE TestTable");
		dbSt.close();
	}

	@Test
	public void testExecute() {
		DbStatement dbSt = pools.createDbStatement(experiment,true);
		boolean result = dbSt
				.execute("CREATE TABLE TestTable (col1 double, col2 double)");
		AssertJUnit.assertTrue(result);
		result = dbSt.execute("DROP TABLE TestTable");
		AssertJUnit.assertTrue(result);
		dbSt.close();
	}

	@Test
	public void testQuery1() {
		String tblName = "TestTable";
		DbStatement dbSt = pools.createDbStatement(experiment,false);
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
				AssertJUnit.fail();
			}
			String rStr1 = "Result = [";
			String rStr2 = "              [";
			for (int k = 0; k < 5; k++) {
				rStr1 += (k == 0 ? "" : ", ") + result[k][0];
				rStr2 += (k == 0 ? "" : ", ") + result[k][1];
			}
			log.info(rStr1 + "]\n" + rStr2 + "]");
			dbSt.closeQuery(rs);
		}
		for (int i = 0; i < 5; i++) {
			AssertJUnit.assertEquals(data[i][0], result[i][0], .001);
			AssertJUnit.assertEquals(data[i][1], result[i][1], .001);
		}
		dbSt.execute("DROP TABLE TestTable");
		dbSt.close();
	}

	@Test
	public void testQuery2() {
		String tblName = "TestTable";
		DbStatement dbSt = pools.createDbStatement(experiment,false);
		dbSt.execute("CREATE TABLE " + tblName + " (col1 double, col2 double)");
		TestPrepStatement prep = new TestPrepStatement(tblName);
		dbSt.createPrepStatement(prep);
		for (int i = 0; i < 5; i++) {
			prep.add(data[i][0], data[i][1]);
		}
		prep.execute();
		ResultSet rs = dbSt.query("SELECT * FROM " + tblName);
		double[][] result = new double[5][2];
		log.info("result size is [" + result.length + "][" + result[0].length
				+ "]");
		int r = 0;
		try {
			while (rs.next()) {
				result[r][0] = rs.getDouble("col1");
				result[r][1] = rs.getDouble("col2");
				r++;
			}
		} catch (SQLException e) {
			log.error("Result Set fetch failed because ", e);
			AssertJUnit.fail();
		}
		dbSt.closeQuery(rs);
		for (int i = 0; i < 5; i++) {
			AssertJUnit.assertEquals(data[i][0], result[i][0], .001);
			AssertJUnit.assertEquals(data[i][1], result[i][1], .001);
		}
		dbSt.execute("DROP TABLE TestTable");
		dbSt.close();
	}

}
