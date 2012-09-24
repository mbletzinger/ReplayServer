package org.nees.mustsim.replay.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nees.mustsim.replay.db.query.DbSelect;
import org.nees.mustsim.replay.db.table.DbTableCreation;
import org.nees.mustsim.replay.db.table.DbTableUpdate;
import org.nees.mustsim.replay.db.table.RateType;
import org.nees.mustsim.replay.db.table.TableType;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

public class TestTableUpdates {
	private DbTableCreation dbT = new DbTableCreation("TESTDB");
	private final Logger log = Logger.getLogger(TestTableUpdates.class);
	private BoneCP connectionPool;

	@Before
	public void setUp() throws Exception {
		List<String> channels = new ArrayList<String>();
		channels.add("OM_Cmd_LBCB1_Actuator_C__LBCB1__X1");
		channels.add("OM_Disp_LBCB1_Cartesian_D__LBCB1__RY");
		channels.add("OM_Load_LBCB1_Actuator_L__LBCB2__Z1");
		channels.add("OM_CntrlSensor_D__West__X");
		dbT.addTable(TableType.OM, channels);
		channels.clear();
		channels.add("DAQ_DisplacementSensor_WestFlange_FirstFloor_DTV02F1A__W7__LinPot05");
		channels.add("DAQ_StrainGauge_Steel_Web_SecondFloor_SGWWF2WL05K__W7__SG__K5");
		channels.add("DAQ_StrainGauge_Steel_WestFlange_FirstFloor_SGWFF1WL03B__W7__SG__B3");

		dbT.addTable(TableType.DAQ, channels);
		String driver = "org.apache.derby.jdbc.ClientDriver";
		String dbName = dbT.getDbname();
		String connectionURL = "jdbc:derby://localhost:1527/" + dbName;
		try {
			// load the database driver (make sure this is in your classpath!)
			Class.forName(driver);
		} catch (Exception e) {
			log.error("Driver " + driver + " did not load ", e);
			Assert.fail();
			return;
		}
		// setup the connection pool
		BoneCPConfig config = new BoneCPConfig();
		config.setJdbcUrl(connectionURL); // jdbc url specific to your database,
											// eg jdbc:mysql://127.0.0.1/yourdb
		config.setMinConnectionsPerPartition(5);
		config.setMaxConnectionsPerPartition(10);
		config.setPartitionCount(1);
		try {
			connectionPool = new BoneCP(config);
		} catch (SQLException e1) {
			log.error("Connection Pool failed to start ", e1);
			Assert.fail();
			return;
		} // setup the connection pool

	}

	@After
	public void teardown() {
		Connection connection = fetchConnection();
		for (RateType r : RateType.values()) {
			executeStatement("DROP TABLE " + dbT.tableName(TableType.OM, r),
					connection);
			executeStatement("DROP TABLE " + dbT.tableName(TableType.DAQ, r),
					connection);
		}
		connectionPool.shutdown(); // shutdown connection pool.
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private Connection fetchConnection() {
		Connection connection = null;
		try {
			connection = connectionPool.getConnection();
		} catch (SQLException e1) {
			log.error("getConnection failed because ", e1);
			Assert.fail();
		} // fetch a connection
		return connection;
	}

	private void executeStatement(String statement, Connection connection) {
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
		} catch (SQLException e) {
			log.error("Create statement failed because ", e);
			Assert.fail();
		}
		try {
			stmt.execute(statement);
		} catch (SQLException e) {
			log.error("\"" + statement + "\" failed because ", e);
			Assert.fail();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					log.error("\"" + statement + "\" failed because ", e);
					Assert.fail();
				}
			}
		}
		log.info("executed \"" + statement + "\"");

	}

	@Test
	public void testCreateTableStatement() {
		Connection connection = fetchConnection();
		String result = dbT.createTableStatement(TableType.OM, RateType.CONT);
		executeStatement(result, connection);
		result = dbT.createTableStatement(TableType.OM, RateType.STEP);
		executeStatement(result, connection);
		result = dbT.createTableStatement(TableType.DAQ, RateType.CONT);
		executeStatement(result, connection);
		result = dbT.createTableStatement(TableType.DAQ, RateType.STEP);
		executeStatement(result, connection);
	}

	@Test
	public void testUpdateTableStatement() {
		DbTableUpdate update = new DbTableUpdate(dbT);
		double contData = 1234563.234;
		double[] stepData = { 1234563.234, 2.0, 3.0, 123.0 };
		double[] omData = { 1.2, 2.3, 3.4, 4.5 };
		double[] daqData = { .012, .023, .034 };

		Connection connection = fetchConnection();
		String stmt = dbT.createTableStatement(TableType.OM, RateType.CONT);
		executeStatement(stmt, connection);
		stmt = dbT.createTableStatement(TableType.OM, RateType.STEP);
		executeStatement(stmt, connection);
		stmt = dbT.createTableStatement(TableType.DAQ, RateType.CONT);
		executeStatement(stmt, connection);
		stmt = dbT.createTableStatement(TableType.DAQ, RateType.STEP);
		executeStatement(stmt, connection);

		double[] result = new double[omData.length + 1];
		System.arraycopy(omData, 0, result, 1, omData.length);
		result[0] = contData;
		String sresult = update.update(TableType.OM, RateType.CONT, result);
		executeStatement(sresult, connection);

		result = new double[omData.length + stepData.length];
		System.arraycopy(omData, 0, result, stepData.length, omData.length);
		System.arraycopy(stepData, 0, result, 0, stepData.length);
		sresult = update.update(TableType.OM, RateType.STEP, result);
		executeStatement(sresult, connection);

		result = new double[daqData.length + 1];
		System.arraycopy(daqData, 0, result, 1, daqData.length);
		result[0] = contData;
		sresult = update.update(TableType.DAQ, RateType.CONT, result);
		executeStatement(sresult, connection);

		result = new double[daqData.length + stepData.length];
		System.arraycopy(daqData, 0, result, stepData.length, daqData.length);
		System.arraycopy(stepData, 0, result, 0, stepData.length);
		sresult = update.update(TableType.DAQ, RateType.STEP, result);
		executeStatement(sresult, connection);

	}
}
