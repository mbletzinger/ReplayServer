package org.nees.illinois.replay.test.db;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.AssertJUnit;
import java.util.List;

import org.apache.log4j.Logger;
import org.nees.illinois.replay.data.ChannelNameRegistry;
import org.nees.illinois.replay.data.DoubleMatrix;
import org.nees.illinois.replay.data.Mtx2Str;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.data.TableType;
import org.nees.illinois.replay.db.DbPools;
import org.nees.illinois.replay.db.DerbyPools;
import org.nees.illinois.replay.db.data.server.DbDataUpdates;
import org.nees.illinois.replay.db.query.DbQuerySpec;
import org.nees.illinois.replay.db.query.DbQueryStatements;
import org.nees.illinois.replay.db.query.DbSelect;
import org.nees.illinois.replay.db.query.DbQueryStatements.QueryType;
import org.nees.illinois.replay.db.statement.DbStatement;
import org.nees.illinois.replay.db.statement.DbTableSpecs;
import org.nees.illinois.replay.test.utils.ChannelLists;
import org.nees.illinois.replay.test.utils.DataGenerator;
import org.nees.illinois.replay.test.utils.ChannelLists.ChannelListType;

public class TestDbQuery {
	private DbPools dbc;
	private ChannelNameRegistry cnr = new ChannelNameRegistry();
	private double[][] omContData = new double[20][7];
	private double[][] daqContData = new double[15][6];
	private double[][] omStepData = new double[20][10];
	private double[][] daqStepData = new double[15][9];
	private final Logger log = Logger.getLogger(TestDbQuery.class);
	private String experiment = "HybridMasonry1";

	@BeforeMethod
	public void setUp() throws Exception {
		dbc = new DerbyPools();
		omContData = DataGenerator.initData(RateType.CONT, 20, 6, 0.7);
		 daqContData = DataGenerator.initData(RateType.CONT, 15, 5, 1.0);
		 omStepData = DataGenerator.initData(RateType.STEP, 20, 6, 0.2);
		 daqStepData = DataGenerator.initData(RateType.STEP, 15, 5, 0.3);
	}

	@AfterMethod
	public void tearDown() throws Exception {
		DbDataUpdates dbu = new DbDataUpdates(dbc, cnr);
		dbu.setExperiment(experiment);
		dbu.removeTable(TableType.OM);
		dbu.removeTable(TableType.DAQ);
		DbStatement dbSt = dbc.createDbStatement(experiment);
		dbSt.close();
		dbc.close();
	}

	@Test
	public void testSelects() {

		ChannelLists cl = new ChannelLists();

		DbDataUpdates dbu = new DbDataUpdates(dbc, cnr);
		dbu.setExperiment(experiment);
		DbTableSpecs specs = dbu.getSpecs();
		dbu.createTable(TableType.OM, cl.getChannels(ChannelListType.OM));
		dbu.createTable(TableType.DAQ, cl.getChannels(ChannelListType.DAQ));

		List<String> chnls = cl.getChannels(ChannelListType.Query1);
		for (RateType r : RateType.values()) {
			DbQuerySpec dbs = new DbQuerySpec(chnls, "OM_Channels", specs, r);
			List<DbSelect> selects = dbs.getSelect();
			AssertJUnit.assertEquals(1, selects.size());
			AssertJUnit.assertEquals(2, selects.get(0).getNumber(false));
			AssertJUnit.assertEquals((r.equals(RateType.CONT) ? 3 : 6),
					selects.get(0).getNumber(true));
			selects = dbs.getSelect(10.0);
			AssertJUnit.assertEquals(1, selects.size());
			AssertJUnit.assertEquals(2, selects.get(0).getNumber(false));
			AssertJUnit.assertEquals((r.equals(RateType.CONT) ? 3 : 6),
					selects.get(0).getNumber(true));
			AssertJUnit.assertTrue(selects.get(0).getSelect().contains("10.0"));
			selects = dbs.getSelect(10.0,900.85);
			AssertJUnit.assertEquals(1, selects.size());
			AssertJUnit.assertEquals(2, selects.get(0).getNumber(false));
			AssertJUnit.assertEquals((r.equals(RateType.CONT) ? 3 : 6),
					selects.get(0).getNumber(true));
			AssertJUnit.assertTrue(selects.get(0).getSelect().contains("10.0"));
			AssertJUnit.assertTrue(selects.get(0).getSelect().contains("900.85"));
		}
		chnls = cl.getChannels(ChannelListType.Query2);
		for (RateType r : RateType.values()) {
			DbQuerySpec dbs = new DbQuerySpec(chnls, "OM_Channels", specs, r);
			List<DbSelect> selects = dbs.getSelect();
			AssertJUnit.assertEquals(2, selects.size());
			selects = dbs.getSelect(10.0);
			AssertJUnit.assertEquals(2, selects.size());
			selects = dbs.getSelect(10.0,900.85);
			AssertJUnit.assertEquals(2, selects.size());
		}

	}

	@Test
	public void testQuerySpec() {

		ChannelLists cl = new ChannelLists();

		DbDataUpdates dbu = new DbDataUpdates(dbc, cnr);
		dbu.setExperiment(experiment);
		DbTableSpecs specs = dbu.getSpecs();
		dbu.createTable(TableType.OM, cl.getChannels(ChannelListType.OM));
		dbu.createTable(TableType.DAQ, cl.getChannels(ChannelListType.DAQ));

		List<String> 	chnls = cl.getChannels(ChannelListType.Query1);
		DbQuerySpec dbs = new DbQuerySpec(chnls, "OM_Channels", specs,
				RateType.CONT);
		AssertJUnit.assertEquals("[om_channel4, om_channel1]", dbs.getSelectOrder()
				.toString());
		AssertJUnit.assertEquals("[0, 1]",
				Mtx2Str.iArray2String(dbs.getQuery2selectMap()));

		chnls = cl.getChannels(ChannelListType.Query2);
		dbs = new DbQuerySpec(chnls, "Mixed_Channels", specs, RateType.STEP);
		AssertJUnit.assertEquals("[daq_channel9, om_channel4, om_channel1]", dbs
				.getSelectOrder().toString());
		AssertJUnit.assertEquals("[1, 2, 0]",
				Mtx2Str.iArray2String(dbs.getQuery2selectMap()));

	}

	@Test
	public void testSelectData() {
		ChannelLists cl = new ChannelLists();

		DbDataUpdates dbu = new DbDataUpdates(dbc, cnr);
		dbu.setExperiment(experiment);
		DbTableSpecs specs = dbu.getSpecs();
		dbu.createTable(TableType.OM, cl.getChannels(ChannelListType.OM));
		dbu.update(TableType.OM, RateType.CONT, omContData);
		dbu.update(TableType.OM, RateType.STEP, omStepData);
		dbu.createTable(TableType.DAQ, cl.getChannels(ChannelListType.DAQ));
		dbu.update(TableType.DAQ, RateType.CONT, daqContData);
		dbu.update(TableType.DAQ, RateType.STEP, daqStepData);
		List<String> 	chnls = cl.getChannels(ChannelListType.Query1);
		DbQuerySpec dbs = new DbQuerySpec(chnls, "OM_Channels", specs,
				RateType.STEP);
		DbStatement dbSt = dbc.createDbStatement(experiment);
		DbQueryStatements ddr = new DbQueryStatements(dbSt, dbs);
		DoubleMatrix r1 = ddr.getData(QueryType.Step, null, null);
		log.debug("Results: " + r1.toString());
		chnls = cl.getChannels(ChannelListType.Query2);
		dbs = new DbQuerySpec(chnls, "Mixed_Channels", specs, RateType.CONT);
		DbQueryStatements ddr2 = new DbQueryStatements(dbSt, dbs);
		DoubleMatrix r2 = ddr2.getData(QueryType.Cont, 0, 0);
		log.debug("Results: " + r2.toString());

	}
}
