package org.nees.illinois.replay.test.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nees.illinois.replay.channels.ChannelNameRegistry;
import org.nees.illinois.replay.data.Mtx2Str;
import org.nees.illinois.replay.data.RateType;
import org.nees.illinois.replay.data.TableType;
import org.nees.illinois.replay.db.DbConnections;
import org.nees.illinois.replay.db.data.server.DbDataUpdates;
import org.nees.illinois.replay.db.statement.DbStatement;
import org.nees.illinois.replay.db.statement.DbTableSpecs;
import org.nees.illinois.replay.test.utils.ChannelLists;
import org.nees.illinois.replay.test.utils.DataGenerator;
import org.nees.illinois.replay.test.utils.ChannelLists.ChannelListType;

public class TestDataStatements {
	private DbConnections dbc;
	private double[][] omContData = new double[10][7];
	private double[][] daqContData = new double[15][6];
	private double[][] omStepData = new double[10][10];
	private double[][] daqStepData = new double[15][9];
	private DbTableSpecs specs;
	private final Logger log = Logger.getLogger(TestDataStatements.class);

	@Before
	public void setUp() throws Exception {
		String dbName = "TESTDB";
		dbc = new DbConnections("org.apache.derby.jdbc.ClientDriver", dbName,
				"jdbc:derby://localhost:1527/");
		omContData = DataGenerator.initData(RateType.CONT, 20, 6, 0.5);
		daqContData = DataGenerator.initData(RateType.CONT, 15, 5, 1);
		omStepData = DataGenerator.initData(RateType.STEP, 20, 6, 0.5);
		daqStepData = DataGenerator.initData(RateType.STEP, 15, 5,1);
		specs = new DbTableSpecs(new ChannelNameRegistry(), dbName);
	}

	@After
	public void tearDown() throws Exception {

		DbDataUpdates dbu = new DbDataUpdates(dbc, specs);
		dbu.removeTable(TableType.OM);
		dbu.removeTable(TableType.DAQ);
		DbStatement dbSt = dbc.createDbStatement();
		dbSt.close();
		dbc.close();
	}

	@Test
	public void testContDataUpdate() {
		ChannelLists cl = new ChannelLists();
		DbDataUpdates dbu = new DbDataUpdates(dbc, specs);
		dbu.createTable(TableType.OM, cl.getChannels(ChannelListType.OM));
		// log.debug("Adding data " +
		// Mtx2Str.matrix2String(Mtx2Str.timeOffset(omContData)));
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
		ChannelLists cl = new ChannelLists();
		DbDataUpdates dbu = new DbDataUpdates(dbc, specs);
		dbu.createTable(TableType.OM, cl.getChannels(ChannelListType.OM));
		 log.debug("Adding data " +
		 Mtx2Str.matrix2String(Mtx2Str.timeOffset(omContData)));
		dbu.update(TableType.OM, RateType.STEP, omStepData);

		String tblName = specs.tableName(TableType.OM, RateType.STEP);
		queryData(tblName, omStepData);

		dbu.createTable(TableType.DAQ, cl.getChannels(ChannelListType.DAQ));
		 log.debug("Adding data " +
		 Mtx2Str.matrix2String(Mtx2Str.timeOffset(omStepData)));
		dbu.update(TableType.DAQ, RateType.STEP, daqStepData);

		tblName = specs.tableName(TableType.DAQ, RateType.STEP);
		queryData(tblName, daqStepData);
	}

	private void queryData(String tblName, double[][] expected) {
		DbStatement dbSt = dbc.createDbStatement();
		ResultSet rs = dbSt.query("SELECT * FROM " + tblName);
		int columns = 0;
		try {
			columns = rs.getMetaData().getColumnCount();
		} catch (SQLException e) {
			log.error("ResultSet has no metadata because ", e);
			Assert.fail();
		}
		Assert.assertEquals(expected[0].length, columns);
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
			Assert.fail();
		}
		DataGenerator.compareData(expected, rsData);
	}
}
