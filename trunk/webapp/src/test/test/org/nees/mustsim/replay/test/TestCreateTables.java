package org.nees.mustsim.replay.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.nees.mustsim.replay.DbTables;
import org.nees.mustsim.replay.DbTables.rateTypes;
import org.nees.mustsim.replay.DbTables.tableTypes;

public class TestCreateTables {
	private DbTables dbT = new DbTables("TESTDB");

	@Before
	public void setUp() throws Exception {
		List<String> channels = new ArrayList<String>();
		channels.add("_OM_Cmd_LBCB1_Actuator_C__LBCB1__X1");
		channels.add("_OM_Disp_LBCB1_Cartesian_D__LBCB1__RY");
		channels.add("_OM_Load_LBCB1_Actuator_L__LBCB2__Z1");
		channels.add("_OM_CntrlSensor_D__West__X");
		dbT.addTable(tableTypes.OM, channels);
		channels.clear();
		channels.add("_DAQ_DisplacementSensor_WestFlange_FirstFloor_DTV02F1A__W7__LinPot05");
		channels.add("_DAQ_StrainGauge_Steel_Web_SecondFloor_SGWWF2WL05K__W7__SG__K5");
		channels.add("_DAQ_StrainGauge_Steel_WestFlange_FirstFloor_SGWFF1WL03B__W7__SG__B3");
		dbT.addTable(tableTypes.DAQ, channels);
	}

	@Test
	public void testCreateTableStatement() {
		String result = dbT.createTableStatement(tableTypes.OM, rateTypes.CONT);
		Assert.assertEquals("CREATE TABLE TESTDB.OM_CONT("
				+ "Time double NOT NULL,"
				+ " _OM_Cmd_LBCB1_Actuator_C__LBCB1__X1 double NOT NULL,"
				+ " _OM_Disp_LBCB1_Cartesian_D__LBCB1__RY double NOT NULL,"
				+ " _OM_Load_LBCB1_Actuator_L__LBCB2__Z1 double NOT NULL,"
				+ " _OM_CntrlSensor_D__West__X double NOT NULL,"
				+ " PRIMARY KEY (Time));", result);
		result = dbT.createTableStatement(tableTypes.OM, rateTypes.STEP);
		Assert.assertEquals("CREATE TABLE TESTDB.OM_STEP(" + ""
				+ "Time double NOT NULL," + " Step int NOT NULL,"
				+ " Substep int NOT NULL," + " CorrectionStep int NOT NULL,"
				+ " _OM_Cmd_LBCB1_Actuator_C__LBCB1__X1 double NOT NULL,"
				+ " _OM_Disp_LBCB1_Cartesian_D__LBCB1__RY double NOT NULL,"
				+ " _OM_Load_LBCB1_Actuator_L__LBCB2__Z1 double NOT NULL,"
				+ " _OM_CntrlSensor_D__West__X double NOT NULL,"
				+ " PRIMARY KEY (Step));", result);
		result = dbT.createTableStatement(tableTypes.DAQ, rateTypes.CONT);
		Assert.assertEquals(
				"CREATE TABLE TESTDB.DAQ_CONT("
						+ "Time double NOT NULL,"
						+ " _DAQ_DisplacementSensor_WestFlange_FirstFloor_DTV02F1A__W7__LinPot05 double NOT NULL,"
						+ " _DAQ_StrainGauge_Steel_Web_SecondFloor_SGWWF2WL05K__W7__SG__K5 double NOT NULL,"
						+ " _DAQ_StrainGauge_Steel_WestFlange_FirstFloor_SGWFF1WL03B__W7__SG__B3 double NOT NULL,"
						+ " PRIMARY KEY (Time));", result);
		result = dbT.createTableStatement(tableTypes.DAQ, rateTypes.STEP);
		Assert.assertEquals(
				"CREATE TABLE TESTDB.DAQ_STEP("
						+ "Time double NOT NULL,"
						+ " Step int NOT NULL,"
						+ " Substep int NOT NULL,"
						+ " CorrectionStep int NOT NULL,"
						+ " _DAQ_DisplacementSensor_WestFlange_FirstFloor_DTV02F1A__W7__LinPot05 double NOT NULL,"
						+ " _DAQ_StrainGauge_Steel_Web_SecondFloor_SGWWF2WL05K__W7__SG__K5 double NOT NULL,"
						+ " _DAQ_StrainGauge_Steel_WestFlange_FirstFloor_SGWFF1WL03B__W7__SG__B3 double NOT NULL,"
						+ " PRIMARY KEY (Step));", result);
	}

}
