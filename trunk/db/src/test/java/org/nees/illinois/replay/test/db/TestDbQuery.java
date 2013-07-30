package org.nees.illinois.replay.test.db;

import java.util.ArrayList;
import java.util.List;

import org.nees.illinois.replay.common.registries.ChannelNameManagement;
import org.nees.illinois.replay.common.registries.ExperimentModuleDeleteMe;
import org.nees.illinois.replay.common.registries.ExperimentRegistries;
import org.nees.illinois.replay.common.registries.TableType;
import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.Mtx2Str;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.db.DbPools;
import org.nees.illinois.replay.db.data.DbDataUpdates;
import org.nees.illinois.replay.db.query.DbQueryRouter;
import org.nees.illinois.replay.db.query.DbQueryRouter.QueryType;
import org.nees.illinois.replay.db.query.NumberOfColumnsWithSelect;
import org.nees.illinois.replay.db.query.SavedQueryWTablesList;
import org.nees.illinois.replay.db.statement.DbTablesMap;
import org.nees.illinois.replay.db.statement.StatementProcessor;
import org.nees.illinois.replay.test.db.derby.process.DerbyDbControl;
import org.nees.illinois.replay.test.db.utils.DbTestsModule;
import org.nees.illinois.replay.test.utils.TestDatasets;
import org.nees.illinois.replay.test.utils.TestDatasetType;
import org.nees.illinois.replay.test.utils.QueryChannelLists;
import org.nees.illinois.replay.test.utils.CompareLists;
import org.nees.illinois.replay.test.utils.DoubleArrayDataGenerator;
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
	private ExperimentModuleDeleteMe guiceMod;
	private boolean ismysql;
	private final Logger log = LoggerFactory.getLogger(TestDbQuery.class);
	private double[][] omContData = new double[20][7];
	private double[][] omStepData = new double[20][10];

	@Parameters("db")
	@BeforeClass
	public void setUp(@Optional("derby") String db) throws Exception {
		guiceMod = new DbTestsModule(db);
		DoubleArrayDataGenerator dadGen = new DoubleArrayDataGenerator(20, 6, 0.5, 222.0);
		omContData = dadGen.generate();
		dadGen = new DoubleArrayDataGenerator(15, 5, 1, 222.0);
		daqContData = dadGen.generate();
		dadGen = new DoubleArrayDataGenerator(20, 6, 0.5, 222.0);
		omStepData = dadGen.generate();
		dadGen = new DoubleArrayDataGenerator(15, 5, 1, 222.0);
		daqStepData = dadGen.generate();
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
		dbu.removeTable(TableType.Control);
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
	public void testQuerySpec() {  // Need more test for query types

		TestDatasets cl = new TestDatasets(false,er.getExperiment());

		dbu.createTable(null, TableType.Control, cl.getChannels(TestDatasetType.OM));
		dbu.createTable(null, TableType.DAQ, cl.getChannels(TestDatasetType.DAQ));
		DbTablesMap specs = dbu.getSpecs();

		QueryChannelLists ctl = cl.getTestQuery(TestDatasetType.QueryOm);
		log.debug("ctl = " + ctl);
		SavedQueryWTablesList dbs = new SavedQueryWTablesList(ctl.combine(), "OM_Channels", specs,
				RateType.CONT);
		log.debug("dbs = " + dbs);
		CompareLists<String> compare = new CompareLists<String>();
		log.debug("Comparing [" + cl.getCnrNames(ctl.combine()) + " to " + dbs.getQueryOrder());
		compare.compare(dbs.getQueryOrder(), cl.getCnrNames(ctl.combine()));
		List<String> selectList = new ArrayList<String>();
		selectList.addAll(ctl.getExistingList());
		selectList.addAll(ctl.getNewChannels());
		compare.compare(dbs.getSelectOrder(), cl.getCnrNames(selectList));

		ctl = cl.getTestQuery(TestDatasetType.QueryDaq);
		log.debug("ctl = " + ctl);
		specs = dbu.getSpecs();
		dbs = new SavedQueryWTablesList(ctl.combine(), "Mixed_Channels", specs, RateType.STEP);
		log.debug("dbs = " + dbs);
		compare.compare(dbs.getQueryOrder(), cl.getCnrNames(ctl.combine()));
		selectList = new ArrayList<String>();
		selectList.addAll(ctl.getExistingList());
		selectList.addAll(ctl.getNewChannels());
		compare.compare(dbs.getSelectOrder(), cl.getCnrNames(selectList));
	}

	@Test
	public void testSelectData() {  // Need expected results here
		TestDatasets cl = new TestDatasets(false,er.getExperiment());

		dbu.createTable(null, TableType.Control, cl.getChannels(TestDatasetType.OM));
		dbu.update(TableType.Control, omContData);
		dbu.update(TableType.Control, omStepData);
		dbu.createTable(null, TableType.DAQ, cl.getChannels(TestDatasetType.DAQ));
		dbu.update(TableType.DAQ, daqContData);
		dbu.update(TableType.DAQ, daqStepData);
		DbTablesMap specs = dbu.getSpecs();
		QueryChannelLists chnls = cl.getTestQuery(TestDatasetType.QueryOm);
		SavedQueryWTablesList dbs = new SavedQueryWTablesList(chnls.combine(), "OM_Channels", specs,
				RateType.STEP);
		StatementProcessor dbSt = pools.createDbStatement(er.getExperiment(),false);
		DbQueryRouter ddr = new DbQueryRouter(dbSt, dbs);
		DoubleMatrix r1 = ddr.getData(QueryType.Step, null, null);
		log.debug("Results: " + r1.toString());
		chnls = cl.getTestQuery(TestDatasetType.QueryDaq);
		dbs = new SavedQueryWTablesList(chnls.combine(), "Mixed_Channels", specs, RateType.CONT);
		DbQueryRouter ddr2 = new DbQueryRouter(dbSt, dbs);
		DoubleMatrix r2 = ddr2.getData(QueryType.Cont, 0, 0);
		log.debug("Results: " + r2.toString());

	}

	@Test
	public void testSelects() {

		TestDatasets cl = new TestDatasets(false,er.getExperiment());

		dbu.createTable(null, TableType.Control, cl.getChannels(TestDatasetType.OM));
		dbu.createTable(null, TableType.DAQ, cl.getChannels(TestDatasetType.DAQ));
		DbTablesMap specs = dbu.getSpecs();

		QueryChannelLists chnls = cl.getTestQuery(TestDatasetType.QueryOm);
		for (RateType r : RateType.values()) {
			SavedQueryWTablesList dbs = new SavedQueryWTablesList(chnls.combine(), "OM_Channels", specs, r);
			List<NumberOfColumnsWithSelect> selects = dbs.getSelect();
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
		chnls = cl.getTestQuery(TestDatasetType.QueryDaq);
		for (RateType r : RateType.values()) {
			SavedQueryWTablesList dbs = new SavedQueryWTablesList(chnls.combine(), "OM_Channels", specs, r);
			List<NumberOfColumnsWithSelect> selects = dbs.getSelect();
			AssertJUnit.assertEquals(2, selects.size());
			selects = dbs.getSelect(10.0);
			AssertJUnit.assertEquals(2, selects.size());
			selects = dbs.getSelect(10.0, 900.85);
			AssertJUnit.assertEquals(2, selects.size());
		}

	}
}
