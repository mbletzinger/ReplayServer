package org.nees.illinois.replay.test.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.data.TableType;
import org.nees.illinois.replay.db.DbPools;
import org.nees.illinois.replay.db.statement.DbTableSpecs;
import org.nees.illinois.replay.registries.ChannelNameRegistry;
import org.nees.illinois.replay.test.db.utils.DbManagement;
import org.nees.illinois.replay.test.db.utils.DbTestsModule;
import org.nees.illinois.replay.test.db.utils.DerbyCreateRemoveDatabase;
import org.nees.illinois.replay.test.db.utils.MySqlCreateRemoveDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

public class TestTableUpdates {
	private DbTableSpecs dbT;
	private final Logger log = LoggerFactory.getLogger(TestTableUpdates.class);
	private BoneCP connectionPool;
	private DbManagement dbm;
	private boolean ismysql;

	@Parameters("db")
	@BeforeClass
	public void setUp(@Optional("derby") String db) throws Exception {
		DbTestsModule guiceMod = new DbTestsModule(db);
		String experiment = "HybridMasonry1";
		guiceMod.setExperiment(experiment);
		Injector injector = Guice.createInjector(guiceMod);
		DbPools pools = injector.getInstance(DbPools.class);
		String driver = pools.getDriver();
		String connectionUrl = pools.getDbUrl();
		String user = pools.getLogon();
		String passwd = pools.getPasswd();
		ismysql = db.equals("mysql");
		if (ismysql) {
			dbm = new MySqlCreateRemoveDatabase(pools, experiment);
		} else {
			dbm = new DerbyCreateRemoveDatabase(pools, experiment);
		}
		dbT = new DbTableSpecs(new ChannelNameRegistry(), experiment);
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
		try {
			// load the database driver (make sure this is in your classpath!)
			Class.forName(driver);
		} catch (Exception e) {
			log.error("Driver " + driver + " did not load ", e);
			Assert.fail();
			return;
		}
		Connection connection = null;
		if (ismysql) {
			connection = dbm.generateConnection(false);
			dbm.createDatabase(connection);
			dbm.closeConnection(connection);
		}
		// setup the connection pool
		BoneCPConfig config = new BoneCPConfig();
		config.setJdbcUrl(connectionUrl + dbm.getExperiment()); // jdbc url
																// specific to
		// your database,
		// eg jdbc:mysql://127.0.0.1/yourdb
		config.setMinConnectionsPerPartition(5);
		config.setMaxConnectionsPerPartition(10);
		config.setPartitionCount(1);
		if (user != null) {
			config.setUsername(user);
			config.setPassword(passwd);
		}
		try {
			connectionPool = new BoneCP(config);
		} catch (SQLException e1) {
			log.error("Connection Pool failed to start ", e1);
			Assert.fail();
			return;
		} // setup the connection pool

	}

	@AfterMethod
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
		if (ismysql) {
			connection = dbm.generateConnection(false);
			dbm.removeDatabase(connection);
			dbm.closeConnection(connection);
		}
	}

	private Connection fetchConnection() {
		Connection connection = null;
		try {
			connection = connectionPool.getConnection();
		} catch (SQLException e1) {
			log.error("getConnection failed because ", e1);
			AssertJUnit.fail();
		} // fetch a connection
		return connection;
	}

	private void executeStatement(String statement, Connection connection) {
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
		} catch (SQLException e) {
			log.error("Create statement failed because ", e);
			AssertJUnit.fail();
		}
		try {
			stmt.execute(statement);
		} catch (SQLException e) {
			log.error("\"" + statement + "\" failed because ", e);
			AssertJUnit.fail();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					log.error("\"" + statement + "\" failed because ", e);
					AssertJUnit.fail();
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

}
