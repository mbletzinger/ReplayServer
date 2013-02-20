package org.nees.illinois.replay.test.db;

import java.sql.Connection;
import java.util.List;

import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.Mtx2Str;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.data.TableType;
import org.nees.illinois.replay.db.DbPools;
import org.nees.illinois.replay.db.data.DbDataUpdates;
import org.nees.illinois.replay.db.query.TableQueriesList;
import org.nees.illinois.replay.db.query.DbQueryProcessor;
import org.nees.illinois.replay.db.query.DbQueryProcessor.QueryType;
import org.nees.illinois.replay.db.query.SavedTableQuery;
import org.nees.illinois.replay.db.statement.StatementProcessor;
import org.nees.illinois.replay.db.statement.DbTablesMap;
import org.nees.illinois.replay.registries.ChannelNameManagement;
import org.nees.illinois.replay.registries.ExperimentModule;
import org.nees.illinois.replay.registries.ExperimentRegistries;
import org.nees.illinois.replay.test.db.derby.process.DerbyDbControl;
import org.nees.illinois.replay.test.db.utils.DbTestsModule;
import org.nees.illinois.replay.test.utils.ChannelLists;
import org.nees.illinois.replay.test.utils.ChannelLists.ChannelListType;
import org.nees.illinois.replay.test.utils.DataGenerator;
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

public class TestDbQuery {
	private double[][] daqContData = new double[15][6];
	private double[][] daqStepData = new double[15][9];
	private DbPools pools;
	private DbDataUpdates dbu;
	private final DerbyDbControl ddbc = new DerbyDbControl();
	private ExperimentRegistries er;
	private ExperimentModule guiceMod;
	private boolean ismysql;
	private final Logger log = LoggerFactory.getLogger(TestDbQuery.class);
	private double[][] omContData = new double[20][7];
	private double[][] omStepData = new double[20][10];

	@Parameters("db")
	@BeforeClass
	public void setUp(@Optional("derby") String db) throws Exception {
		guiceMod = new DbTestsModule(db);
		omContData = DataGenerator.initData(20, 6, 0.7);
		daqContData = DataGenerator.initData(15, 5, 1.0);
		omStepData = DataGenerator.initData(20, 6, 0.2);
		daqStepData = DataGenerator.initData(15, 5, 0.3);
		guiceMod.setExperiment("HybridMasonry1");

		ismysql = db.equals("mysql");
		if (ismysql == false) {
			ddbc.startDerby();
		}

		Thread.sleep(5000);
		Injector injector = Guice.createInjector(guiceMod);
		er = injector.getInstance(ExperimentRegistries.class);
		er.setLookups(injector.getProvider(ChannelNameManagement.class));
		dbu = injector.getInstance(DbDataUpdates.class);
		dbu.setExperiment(er);
		pools = dbu.getPools();

	}

	@AfterMethod
	public void tearDown() throws Exception {
		DbDataUpdates dbu = new DbDataUpdates(pools);
		dbu.setExperiment(er);
		dbu.removeTable(TableType.OM);
		dbu.removeTable(TableType.DAQ);
	}

	@AfterClass
	public void teardown1() throws Exception {
		pools.getOps().removeDatabase(pools.getInfo().getExperiment());
		pools.close();
		if (ismysql == false) {
			ddbc.stopDerby();
		}
	}

	@Test
	public void testQuerySpec() {

		ChannelLists cl = new ChannelLists();

		dbu.createTable(TableType.OM, cl.getChannels(ChannelListType.OM));
		dbu.createTable(TableType.DAQ, cl.getChannels(ChannelListType.DAQ));
		DbTablesMap specs = dbu.getSpecs();

		List<String> chnls = cl.getChannels(ChannelListType.Query1);
		TableQueriesList dbs = new TableQueriesList(chnls, "OM_Channels", specs,
				RateType.CONT);
		AssertJUnit.assertEquals("[om_channel4, om_channel1]", dbs
				.getSelectOrder().toString());
		AssertJUnit.assertEquals("[0, 1]",
				Mtx2Str.iArray2String(dbs.getQuery2selectMap()));

		chnls = cl.getChannels(ChannelListType.Query2);
		specs = dbu.getSpecs();
		dbs = new TableQueriesList(chnls, "Mixed_Channels", specs, RateType.STEP);
		AssertJUnit.assertEquals("[daq_channel9, om_channel4, om_channel1]",
				dbs.getSelectOrder().toString());
		AssertJUnit.assertEquals("[1, 2, 0]",
				Mtx2Str.iArray2String(dbs.getQuery2selectMap()));

	}

	@Test
	public void testSelectData() {
		ChannelLists cl = new ChannelLists();

		dbu.createTable(TableType.OM, cl.getChannels(ChannelListType.OM));
		dbu.update(TableType.OM, RateType.CONT, omContData);
		dbu.update(TableType.OM, RateType.STEP, omStepData);
		dbu.createTable(TableType.DAQ, cl.getChannels(ChannelListType.DAQ));
		dbu.update(TableType.DAQ, RateType.CONT, daqContData);
		dbu.update(TableType.DAQ, RateType.STEP, daqStepData);
		DbTablesMap specs = dbu.getSpecs();
		List<String> chnls = cl.getChannels(ChannelListType.Query1);
		TableQueriesList dbs = new TableQueriesList(chnls, "OM_Channels", specs,
				RateType.STEP);
		StatementProcessor dbSt = pools.createDbStatement(er.getExperiment(),false);
		DbQueryProcessor ddr = new DbQueryProcessor(dbSt, dbs);
		DoubleMatrix r1 = ddr.getData(QueryType.Step, null, null);
		log.debug("Results: " + r1.toString());
		chnls = cl.getChannels(ChannelListType.Query2);
		dbs = new TableQueriesList(chnls, "Mixed_Channels", specs, RateType.CONT);
		DbQueryProcessor ddr2 = new DbQueryProcessor(dbSt, dbs);
		DoubleMatrix r2 = ddr2.getData(QueryType.Cont, 0, 0);
		log.debug("Results: " + r2.toString());

	}

	@Test
	public void testSelects() {

		ChannelLists cl = new ChannelLists();

		dbu.createTable(TableType.OM, cl.getChannels(ChannelListType.OM));
		dbu.createTable(TableType.DAQ, cl.getChannels(ChannelListType.DAQ));
		DbTablesMap specs = dbu.getSpecs();

		List<String> chnls = cl.getChannels(ChannelListType.Query1);
		for (RateType r : RateType.values()) {
			TableQueriesList dbs = new TableQueriesList(chnls, "OM_Channels", specs, r);
			List<SavedTableQuery> selects = dbs.getSelect();
			AssertJUnit.assertEquals(1, selects.size());
			AssertJUnit.assertEquals(2, selects.get(0).getNumber(false));
			AssertJUnit.assertEquals(6, selects.get(0).getNumber(true));
			selects = dbs.getSelect(10.0);
			AssertJUnit.assertEquals(1, selects.size());
			AssertJUnit.assertEquals(2, selects.get(0).getNumber(false));
			AssertJUnit.assertEquals(6, selects.get(0).getNumber(true));
			AssertJUnit.assertTrue(selects.get(0).getSelect().contains("10.0"));
			selects = dbs.getSelect(10.0, 900.85);
			AssertJUnit.assertEquals(1, selects.size());
			AssertJUnit.assertEquals(2, selects.get(0).getNumber(false));
			AssertJUnit.assertEquals(6, selects.get(0).getNumber(true));
			AssertJUnit.assertTrue(selects.get(0).getSelect().contains("10.0"));
			AssertJUnit.assertTrue(selects.get(0).getSelect()
					.contains("900.85"));
		}
		chnls = cl.getChannels(ChannelListType.Query2);
		for (RateType r : RateType.values()) {
			TableQueriesList dbs = new TableQueriesList(chnls, "OM_Channels", specs, r);
			List<SavedTableQuery> selects = dbs.getSelect();
			AssertJUnit.assertEquals(2, selects.size());
			selects = dbs.getSelect(10.0);
			AssertJUnit.assertEquals(2, selects.size());
			selects = dbs.getSelect(10.0, 900.85);
			AssertJUnit.assertEquals(2, selects.size());
		}

	}
}
