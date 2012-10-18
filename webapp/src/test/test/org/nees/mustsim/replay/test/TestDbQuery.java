package org.nees.mustsim.replay.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nees.mustsim.replay.db.DbConnections;
import org.nees.mustsim.replay.db.data.ChannelNameRegistry;
import org.nees.mustsim.replay.db.data.DbDataUpdates;
import org.nees.mustsim.replay.db.data.DoubleMatrix;
import org.nees.mustsim.replay.db.data.Mtx2Str;
import org.nees.mustsim.replay.db.query.DbQueryStatements;
import org.nees.mustsim.replay.db.query.DbQueryStatements.QueryType;
import org.nees.mustsim.replay.db.query.DbQuerySpec;
import org.nees.mustsim.replay.db.statement.DbStatement;
import org.nees.mustsim.replay.db.statement.DbTableSpecs;
import org.nees.mustsim.replay.db.statement.RateType;
import org.nees.mustsim.replay.db.statement.TableType;
import org.nees.mustsim.replay.test.utils.ChannelLists;
import org.nees.mustsim.replay.test.utils.DataGenerator;

public class TestDbQuery {
	private DbConnections dbc;
	private DbTableSpecs specs;
	private double[][] omContData = new double[10][7];
	private double[][] daqContData = new double[15][6];
	private double[][] omStepData = new double[10][10];
	private double[][] daqStepData = new double[15][9];
	private final Logger log = Logger.getLogger(TestDbQuery.class);
	@Before
	public void setUp() throws Exception {
		String dbName = "TESTDB";
		dbc = new DbConnections("org.apache.derby.jdbc.ClientDriver", dbName,
				"jdbc:derby://localhost:1527/", true);
		specs = new DbTableSpecs(new ChannelNameRegistry(), dbName);
		omContData = DataGenerator.initData(RateType.CONT, 10, 6,0.5);
		daqContData = DataGenerator.initData(RateType.CONT, 15, 5,1);
		omStepData = DataGenerator.initData(RateType.STEP, 10, 6, 0.5);
		daqStepData = DataGenerator.initData(RateType.STEP, 15, 5,1);
	}

	@After
	public void tearDown() throws Exception {
		DbStatement dbSt = dbc.createDbStatement();
		DbDataUpdates dbu = new DbDataUpdates(dbSt, specs);
		dbu.removeTable(TableType.OM);
		dbu.removeTable(TableType.DAQ);
		dbSt.close();
		dbc.close();
	}

	@Test
	public void testSelects() {
		
		ChannelLists cl = new ChannelLists();
		DbStatement dbSt = dbc.createDbStatement();
		DbDataUpdates dbu = new DbDataUpdates(dbSt, specs);
		dbu.createTable(TableType.OM, cl.getChannels(TableType.OM, false));
		dbu.createTable(TableType.DAQ, cl.getChannels(TableType.DAQ, false));

		List<String> chnls = new ArrayList<String>();
		chnls.add("OM/CntrlSensor/D_West_X_3");
		chnls.add("OM/Cmd/LBCB1/Actuator/C_LBCB1_X1_0");
		DbQuerySpec dbs = new DbQuerySpec(chnls, "OM_Channels", specs,RateType.CONT);
		Assert.assertEquals("[om_channel4, om_channel1]", dbs.getSelectOrder().toString());
		Assert.assertEquals("[0, 1]", Mtx2Str.iArray2String(dbs.getQuery2selectMap()));

		chnls = new ArrayList<String>();
		chnls.add("OM/CntrlSensor/D_West_X_3");
		chnls.add("OM/Cmd/LBCB1/Actuator/C_LBCB1_X1_0");
		chnls.add("DAQ/StrainGauge/Steel/WestFlange/FirstFloor/SGWFF1WL03B_W7_SG_B3_3");
		dbs = new DbQuerySpec(chnls, "Mixed_Channels", specs, RateType.STEP);
		Assert.assertEquals("[daq_channel9, om_channel4, om_channel1]", dbs.getSelectOrder().toString());
		Assert.assertEquals("[1, 2, 0]", Mtx2Str.iArray2String(dbs.getQuery2selectMap()));

	}

	@Test
	public void testSelectData() {
		ChannelLists cl = new ChannelLists();
		DbStatement dbSt = dbc.createDbStatement();
		DbDataUpdates dbu = new DbDataUpdates(dbSt, specs);
		dbu.createTable(TableType.OM, cl.getChannels(TableType.OM, false));
		dbu.update(TableType.OM, RateType.CONT, omContData);
		dbu.update(TableType.OM, RateType.STEP, omStepData);
		dbu.createTable(TableType.DAQ, cl.getChannels(TableType.DAQ, false));
		dbu.update(TableType.DAQ, RateType.CONT, daqContData);
		dbu.update(TableType.DAQ, RateType.STEP, daqStepData);
		List<String> chnls = new ArrayList<String>();
		chnls.add("OM/CntrlSensor/D_West_X_3");
		chnls.add("OM/Cmd/LBCB1/Actuator/C_LBCB1_X1_0");
		DbQuerySpec dbs = new DbQuerySpec(chnls, "OM_Channels", specs,RateType.STEP);
		DbQueryStatements ddr = new DbQueryStatements(dbSt, dbs);
		DoubleMatrix r1 = ddr.getData(QueryType.Step,0,0,0);
		log.debug("Results: " + r1.toString());
		chnls = new ArrayList<String>();
		chnls.add("OM/CntrlSensor/D_West_X_3");
		chnls.add("OM/Cmd/LBCB1/Actuator/C_LBCB1_X1_0");
		chnls.add("DAQ/StrainGauge/Steel/WestFlange/FirstFloor/SGWFF1WL03B_W7_SG_B3_3");
		dbs = new DbQuerySpec(chnls, "Mixed_Channels", specs, RateType.CONT);
		DbQueryStatements ddr2 = new DbQueryStatements(dbSt, dbs);
		DoubleMatrix r2 = ddr2.getData(QueryType.Cont,0,0,0);
		log.debug("Results: " + r2.toString());

	}
}
