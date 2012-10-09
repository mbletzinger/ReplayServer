package org.nees.mustsim.replay.test;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nees.mustsim.replay.db.DbConnections;
import org.nees.mustsim.replay.db.data.ChannelNameRegistry;
import org.nees.mustsim.replay.db.data.DbDataUpdates;
import org.nees.mustsim.replay.db.data.Mtx2Str;
import org.nees.mustsim.replay.db.query.DbSelect;
import org.nees.mustsim.replay.db.statement.DbStatement;
import org.nees.mustsim.replay.db.table.DbTableCreation;
import org.nees.mustsim.replay.db.table.RateType;
import org.nees.mustsim.replay.db.table.TableType;
import org.nees.mustsim.replay.test.utils.ChannelLists;
import org.nees.mustsim.replay.test.utils.DataGenerator;

public class TestDbSelect {
	private DbConnections dbc;
	private DbTableCreation create;
	private double[][] omContData = new double[10][5];
	private double[][] daqContData = new double[15][4];
	private double[][] omStepData = new double[10][8];
	private double[][] daqStepData = new double[15][7];
	@Before
	public void setUp() throws Exception {
		String dbName = "TESTDB";
		dbc = new DbConnections("org.apache.derby.jdbc.ClientDriver", dbName,
				"jdbc:derby://localhost:1527/", true);
		create = new DbTableCreation(new ChannelNameRegistry(), dbName);
		omContData = DataGenerator.initData(RateType.CONT, 10, 4);
		daqContData = DataGenerator.initData(RateType.CONT, 15, 3);
		omStepData = DataGenerator.initData(RateType.STEP, 10, 4);
		daqStepData = DataGenerator.initData(RateType.STEP, 15, 3);
	}

	@After
	public void tearDown() throws Exception {
		DbStatement dbSt = dbc.createDbStatement();
		DbDataUpdates dbu = new DbDataUpdates(dbSt, create);
		dbu.removeTable(TableType.OM);
		dbu.removeTable(TableType.DAQ);
		dbSt.close();
		dbc.close();
	}

	@Test
	public void testSelects() {
		
		ChannelLists cl = new ChannelLists();
		DbStatement dbSt = dbc.createDbStatement();
		DbDataUpdates dbu = new DbDataUpdates(dbSt, create);
		dbu.createTable(TableType.OM, cl.getChannels(TableType.OM, false));
		dbu.createTable(TableType.DAQ, cl.getChannels(TableType.DAQ, false));

		List<String> chnls = new ArrayList<String>();
		chnls.add("OM/CntrlSensor/D_West_X");
		chnls.add("OM/Cmd/LBCB1/Actuator/C_LBCB1_X1");
		DbSelect dbs = new DbSelect(chnls, "OM_Channels", create);
		Assert.assertEquals("[om_channel4, om_channel1]", dbs.getTableOrder().toString());
		Assert.assertEquals("[0, 1]", Mtx2Str.iArray2String(dbs.getChannelMap()));

		chnls = new ArrayList<String>();
		chnls.add("DAQ/StrainGauge/Steel/WestFlange/FirstFloor/SGWFF1WL03B_W7_SG_B3");
		chnls.add("OM/CntrlSensor/D_West_X");
		chnls.add("OM/Cmd/LBCB1/Actuator/C_LBCB1_X1");
		dbs = new DbSelect(chnls, "Mixed_Channels", create);
		Assert.assertEquals("[om_channel4, om_channel1, daq_channel7]", dbs.getTableOrder().toString());
		Assert.assertEquals("[2, 0, 1]", Mtx2Str.iArray2String(dbs.getChannelMap()));

	}

	@Test
	public void testSelectData() {
		ChannelLists cl = new ChannelLists();
		DbStatement dbSt = dbc.createDbStatement();
		DbDataUpdates dbu = new DbDataUpdates(dbSt, create);
		dbu.createTable(TableType.OM, cl.getChannels(TableType.OM, false));
		dbu.update(TableType.OM, RateType.CONT, omContData);
		dbu.update(TableType.OM, RateType.STEP, omStepData);
		dbu.createTable(TableType.DAQ, cl.getChannels(TableType.DAQ, false));
		dbu.update(TableType.DAQ, RateType.CONT, daqContData);
		dbu.update(TableType.DAQ, RateType.STEP, daqStepData);
		List<String> chnls = new ArrayList<String>();
		chnls.add("OM/CntrlSensor/D_West_X");
		chnls.add("OM/Cmd/LBCB1/Actuator/C_LBCB1_X1");
		DbSelect dbs = new DbSelect(chnls, "OM_Channels", create);
		ResultSet rs = dbSt.query(dbs.getSelect());

		chnls = new ArrayList<String>();
		chnls.add("DAQ/StrainGauge/Steel/WestFlange/FirstFloor/SGWFF1WL03B_W7_SG_B3");
		chnls.add("OM/CntrlSensor/D_West_X");
		chnls.add("OM/Cmd/LBCB1/Actuator/C_LBCB1_X1");
		dbs = new DbSelect(chnls, "Mixed_Channels", create);

	}
}
