package org.nees.illinois.replay.test.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.nees.illinois.replay.data.Mtx2Str;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.data.TableType;
import org.nees.illinois.replay.db.DbPools;
import org.nees.illinois.replay.db.data.DbDataUpdates;
import org.nees.illinois.replay.db.statement.StatementProcessor;
import org.nees.illinois.replay.db.statement.DbTablesMap;
import org.nees.illinois.replay.registries.ChannelNameManagement;
import org.nees.illinois.replay.registries.ChannelNameRegistry;
import org.nees.illinois.replay.registries.ExperimentModule;
import org.nees.illinois.replay.registries.ExperimentRegistries;
import org.nees.illinois.replay.test.db.derby.process.DerbyDbControl;
import org.nees.illinois.replay.test.db.utils.DbTestsModule;
import org.nees.illinois.replay.test.utils.ChannelListTestMaps;
import org.nees.illinois.replay.test.utils.ChannelListType;
import org.nees.illinois.replay.test.utils.DoubleArrayDataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class TestDataStatements {
	private DbPools pools;
	private double[][] omContData;
	private double[][] daqContData;
	private double[][] omStepData;
	private double[][] daqStepData;
	private ChannelNameRegistry cnr = new ChannelNameRegistry();
	private final DerbyDbControl ddbc = new DerbyDbControl();

	private final Logger log = LoggerFactory
			.getLogger(TestDataStatements.class);
	private ExperimentRegistries er;
	private DbDataUpdates dbu;
	private ExperimentModule guiceMod;
	private boolean ismysql;

	@BeforeMethod
	public void setUp() throws Exception {
		DoubleArrayDataGenerator dadGen = new DoubleArrayDataGenerator(20, 6, 0.5, 222.0);
		omContData = dadGen.generate();
		dadGen = new DoubleArrayDataGenerator(15, 5, 1, 222.0);
		daqContData = dadGen.generate();
		dadGen = new DoubleArrayDataGenerator(20, 6, 0.5, 222.0);
		omStepData = dadGen.generate();
		dadGen = new DoubleArrayDataGenerator(15, 5, 1, 222.0);
		daqStepData = dadGen.generate();
		guiceMod.setExperiment("HybridMasonry1");
		Injector injector = Guice.createInjector(guiceMod);
		er = injector.getInstance(ExperimentRegistries.class);
		er.setLookups(injector.getProvider(ChannelNameManagement.class));
		dbu = injector.getInstance(DbDataUpdates.class);
		dbu.setExperiment(er);
		pools = dbu.getPools();
		pools.getOps().createDatabase("HybridMasonry1");
	}
	
	@Parameters("db")
	@BeforeClass
	public void setup1(@Optional("mysql") String db) {
		guiceMod = new DbTestsModule(db);
		ismysql = db.equals("mysql");
		if (ismysql == false) {
			ddbc.startDerby();
		}
	}

	@AfterMethod
	public void tearDown() throws Exception {

		dbu.removeTable(TableType.OM);
		dbu.removeTable(TableType.DAQ);
		pools.getOps().removeDatabase("HybridMasonry1");
		pools.close();
	}
	
	@AfterClass
	public void teardown1() {
		if (ismysql == false) {
			ddbc.stopDerby();
		}
	}

	@Test
	public void testContDataUpdate() {
		ChannelListTestMaps cl = new ChannelListTestMaps(false,er.getExperiment());
		DbTablesMap specs = new DbTablesMap(cnr, er.getExperiment());
		dbu.createTable(TableType.OM, cl.getChannels(ChannelListType.OM));
		dbu.update(TableType.OM, RateType.CONT, omContData);

		String tblName = specs.tableName(TableType.OM, RateType.CONT);
		queryData(tblName, omContData);

		dbu.createTable(TableType.DAQ, cl.getChannels(ChannelListType.DAQ));
		// log.debug("Adding data " +
		// Mtx2Str.matrix2String(Mtx2Str.timeOffset(omContData)));
		dbu.update(TableType.DAQ, RateType.CONT, daqContData);

		tblName = specs.tableName(TableType.DAQ, RateType.CONT);
		queryData(tblName, daqContData);
	}

	@Test
	public void testStepDataUpdate() {
		ChannelListTestMaps cl = new ChannelListTestMaps(false,er.getExperiment());
		DbTablesMap specs = new DbTablesMap(cnr, er.getExperiment());
		dbu.createTable(TableType.OM, cl.getChannels(ChannelListType.OM));
		log.debug("Adding data "
				+ Mtx2Str.matrix2String(Mtx2Str.timeOffset(omContData)));
		dbu.update(TableType.OM, RateType.STEP, omStepData);

		String tblName = specs.tableName(TableType.OM, RateType.STEP);
		queryData(tblName, omStepData);

		dbu.createTable(TableType.DAQ, cl.getChannels(ChannelListType.DAQ));
		log.debug("Adding data "
				+ Mtx2Str.matrix2String(Mtx2Str.timeOffset(omStepData)));
		dbu.update(TableType.DAQ, RateType.STEP, daqStepData);

		tblName = specs.tableName(TableType.DAQ, RateType.STEP);
		queryData(tblName, daqStepData);
	}

	private void queryData(String tblName, double[][] expected) {
		StatementProcessor dbSt = pools.createDbStatement(er.getExperiment(),false);
		ResultSet rs = dbSt.query("SELECT * FROM " + tblName);
		int columns = 0;
		try {
			columns = rs.getMetaData().getColumnCount();
		} catch (SQLException e) {
			log.error("ResultSet has no metadata because ", e);
			dbSt.closeQuery(rs);
			dbSt.close();
			AssertJUnit.fail();
		}
		AssertJUnit.assertEquals(expected[0].length, columns);
		double[][] rsData = new double[expected.length][columns];
		int r = 0;
		try {
			while (rs.next()) {
				log.debug("Processing rs row " + r);
				for (int i = 0; i < columns; i++) {
					rsData[r][i] = rs.getDouble(i + 1);
				}
				r++;
			}
		} catch (SQLException e) {
			log.error("Result Set fetch failed because ", e);
			dbSt.closeQuery(rs);
			dbSt.close();
			AssertJUnit.fail();
		}
		DoubleArrayDataGenerator.compareData(rsData, expected);
		dbSt.closeQuery(rs);
		dbSt.close();
	}
}
