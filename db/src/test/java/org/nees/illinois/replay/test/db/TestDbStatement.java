package org.nees.illinois.replay.test.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.nees.illinois.replay.db.DbPools;
import org.nees.illinois.replay.db.statement.StatementProcessor;
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

/**
 * Test various database statements.
 * @author Michael Bletzinger
 */
@Test(groups = { "db-statement" })
public class TestDbStatement {
	/**
	 * Data property.
	 */
	private final int numDataRows = 5;
	/**
	 * Data property.
	 */
	private final int numDataColumns = 2;
	/**
	 * Data property.
	 */
	private final double[] dataMultipliers = { 0.5, -0.02 };
	/**
	 * Data property.
	 */
	private final double tolerance = 0.001;
	/**
	 * Data to use.
	 */
	private final double[][] data = new double[numDataRows][numDataColumns];
	/**
	 * Map of database pools.
	 */
	private DbPools pools;
	/**
	 * Extra Derby controls.
	 */
	private final DerbyDbControl ddbc = new DerbyDbControl();
	/**
	 * Experiment name.
	 */
	private final String experiment = "HybridMasonry1";
	/**
	 * Module for injecting database properties.
	 */
	private DbTestsModule guiceMod;
	/**
	 * Flag that is true if the database is MySQL.
	 */
	private boolean ismysql;
	/**
	 * Table name.
	 */
	private final String tblName = "\"Test Table\"";

	/**
	 * Logger.
	 */
	private final Logger log = LoggerFactory.getLogger(TestDbStatement.class);

	/**
	 * Set up the data for the tests.
	 * @param db
	 *            Label for database.
	 * @throws Exception
	 *             if something breaks.
	 */
	@Parameters("db")
	@BeforeClass
	public final void setUp(@Optional("derby") final String db)
			throws Exception {
		guiceMod = new DbTestsModule(db);
		Injector injector = Guice.createInjector(guiceMod);
		pools = injector.getInstance(DbPools.class);
		ismysql = db.equals("mysql");
		if (ismysql == false) {
			ddbc.startDerby();
		}
		pools.getOps().createDatabase(experiment);

		for (int i = 0;i < numDataRows;i++) {
			data[i][0] = i * dataMultipliers[0] + tolerance;
			data[i][1] = i * dataMultipliers[1] + tolerance;
		}
	}

	/**
	 * Clean up the databases.
	 * @throws Exception
	 *             If anything breaks.
	 */
	@AfterMethod
	public final void tearDown() throws Exception {
		StatementProcessor dbSt = pools.createDbStatement(experiment, false);
		dbSt.noComplaints("DROP TABLE " + tblName);
		dbSt.close();
	}

	/**
	 * Remove the database and shut down the pools.
	 */
	@AfterClass
	public final void teardown1() {
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

	/**
	 * Test table management.
	 */
	@Test
	public final void testCreateAndDropTable() {
		StatementProcessor dbSt = pools.createDbStatement(experiment, true);
		boolean result = dbSt.execute("CREATE TABLE " + tblName
				+ " (col1 double, col2 double)");
		AssertJUnit.assertTrue(result);
		result = dbSt.execute("DROP TABLE " + tblName);
		AssertJUnit.assertTrue(result);
		dbSt.close();
	}

	/**
	 * Test a prepared statement.
	 */
	@Test
	public final void testCreatePrepStatement() {
		StatementProcessor dbSt = pools.createDbStatement(experiment, true);
		dbSt.execute("CREATE TABLE " + tblName + " (col1 double, col2 double)");
		TestPrepStatement prep = new TestPrepStatement(tblName.replaceAll("\"",
				""), dbSt.getConnection());
		for (int i = 0;i < numDataRows;i++) {
			prep.add(data[i][0], data[i][1]);
		}
		int[] result = prep.getBuilder().execute();
		AssertJUnit.assertNotNull(result);
		dbSt.execute("DROP TABLE " + tblName);
		dbSt.close();
	}

	/**
	 * Test a select statement.
	 */
	@Test
	public final void testQuery1() {
		StatementProcessor dbSt = pools.createDbStatement(experiment, false);
		dbSt.execute("CREATE TABLE " + tblName + " (col1 double, col2 double)");
		double[][] result = new double[numDataRows][numDataColumns];
		for (int i = 0;i < numDataRows;i++) {
			TestPrepStatement prep = new TestPrepStatement(tblName.replaceAll(
					"\"", ""), dbSt.getConnection());
			prep.add(data[i][0], data[i][1]);
			int [] updated = prep.getBuilder().execute();
//			try {
//				if (dbSt.getConnection().isClosed()) {
//					log.error("Whuuut?");
//					Assert.fail();
//				}
//			} catch (SQLException e1) {
//				log.error("WHUUUUUT?", e1);
//				Assert.fail();
//			}
			dbSt = pools.createDbStatement(experiment, false);

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
			for (int k = 0;k < numDataRows;k++) {
				rStr1 += (k == 0 ? "" : ", ") + result[k][0];
				rStr2 += (k == 0 ? "" : ", ") + result[k][1];
			}
			log.info(rStr1 + "]\n" + rStr2 + "]");
			dbSt.closeQuery(rs);
		}
		for (int i = 0;i < numDataRows;i++) {
			AssertJUnit.assertEquals(data[i][0], result[i][0], tolerance);
			AssertJUnit.assertEquals(data[i][1], result[i][1], tolerance);
		}
		dbSt.execute("DROP TABLE " + tblName);
		dbSt.close();
	}

	/**
	 * Test a different query.
	 */
	@Test
	public final void testQuery2() {
		StatementProcessor dbSt = pools.createDbStatement(experiment, false);
		dbSt.execute("CREATE TABLE " + tblName + " (col1 double, col2 double)");
		TestPrepStatement prep = new TestPrepStatement(tblName.replaceAll("\"",
				""), dbSt.getConnection());
		for (int i = 0;i < numDataRows;i++) {
			prep.add(data[i][0], data[i][1]);
		}
		prep.getBuilder().execute();
		dbSt = pools.createDbStatement(experiment, false);
		ResultSet rs = dbSt.query("SELECT * FROM " + tblName);
		double[][] result = new double[numDataRows][numDataColumns];
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
		for (int i = 0;i < numDataRows;i++) {
			AssertJUnit.assertEquals(data[i][0], result[i][0], tolerance);
			AssertJUnit.assertEquals(data[i][1], result[i][1], tolerance);
		}
		dbSt.execute("DROP TABLE " + tblName);
		dbSt.close();
	}

}
